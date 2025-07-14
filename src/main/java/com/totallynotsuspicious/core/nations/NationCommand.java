package com.totallynotsuspicious.core.nations;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.logging.LogUtils;
import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import com.totallynotsuspicious.core.mixin.EntityAccessor;
import com.totallynotsuspicious.core.mixin.PlayerManagerAccesor;
import com.totallynotsuspicious.core.mixin.PlayerSaveHandlerAccessor;
import com.totallynotsuspicious.core.nations.claims.ClaimsLookupV2;
import com.totallynotsuspicious.core.world.NationClaimChunkComponent;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.ReadView;
import net.minecraft.text.Text;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PlayerSaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class NationCommand {
    public static final Dynamic2CommandExceptionType UNKNOWN_PROPERTY_EXCEPTION = new Dynamic2CommandExceptionType(
            (claimedFor, alreadyClaimed) -> Text.translatable("tnscore.commands.nation.claim.fail", claimedFor, alreadyClaimed)
    );

    private static boolean hasManagePermission(ServerCommandSource src) {
        return src.hasPermissionLevel(3) || Permissions.check(src, "tnscore.nations.manage");
    }

    private static boolean hasHomePermission(ServerCommandSource src) {
        return src.hasPermissionLevel(2) || Permissions.check(src, "tnscore.nations.home");
    }

    private static boolean hasOtherHomePermission(ServerCommandSource src) {
        return src.hasPermissionLevel(2) || Permissions.check(src, "tnscore.nations.home.other");
    }

    public static void registerCommand(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment
    ) {
        final String playerArg = "player";
        final String nationArg = "nation";
        final String posArg = "pos";
        final String forceArg = "force";

        LiteralArgumentBuilder<ServerCommandSource> nation = literal("nation")
                .then(literal("reset")
                        .requires(NationCommand::hasManagePermission)
                        .then(argument(playerArg, EntityArgumentType.player())
                                .executes(ctx -> executeReset(
                                        ctx.getSource(),
                                        EntityArgumentType.getPlayer(ctx, playerArg)
                                ))
                        )
                )
                .then(literal("set")
                        .requires(NationCommand::hasManagePermission)
                        .then(argument(playerArg, EntityArgumentType.player())
                                .then(nationArgument(nationArg).executes(ctx -> executeSet(
                                                ctx.getSource(),
                                                EntityArgumentType.getPlayer(ctx, playerArg),
                                                StringArgumentType.getString(ctx, nationArg)
                                        ))
                                )
                        )
                )
                .then(literal("get")
                        .requires(NationCommand::hasManagePermission)
                        .then(argument(playerArg, GameProfileArgumentType.gameProfile())
                                .executes(ctx -> executeGet(
                                        ctx.getSource(),
                                        GameProfileArgumentType.getProfileArgument(ctx, playerArg).iterator().next()
                                ))
                        )
                )
                .then(literal("home")
                        .requires(NationCommand::hasHomePermission)
                        .then(nationArgument(nationArg)
                                .requires(NationCommand::hasOtherHomePermission)
                                .executes(ctx -> executeHome(
                                        ctx.getSource(),
                                        Nation.CODEC.byId(StringArgumentType.getString(ctx, nationArg), Nation.NATIONLESS)
                                ))
                        )
                        .executes(ctx -> executeHome(
                                ctx.getSource(),
                                PlayerNationComponent.get(ctx.getSource().getPlayerOrThrow()).getNation()
                        ))
                )
                .then(literal("count")
                        .requires(NationCommand::hasManagePermission)
                        .executes(ctx -> {
                            try {
                                return executeCountAllNations(ctx.getSource());
                            } catch (Exception e) {
                                TNSCore.LOGGER.error("Error while counting nations", e);
                                throw new RuntimeException(e);
                            }
                        })
                )
                .then(literal("claim")
//                        .requires(NationCommand::hasManagePermission)
//                        .then(argument(posArg, BlockPosArgumentType.blockPos())
//                                .then(nationArgument(nationArg)
//                                        .executes(ctx -> executeClaim(
//                                                ctx.getSource(),
//                                                BlockPosArgumentType.getBlockPos(ctx, posArg),
//                                                Nation.CODEC.byId(StringArgumentType.getString(ctx, nationArg), Nation.NATIONLESS),
//                                                false
//                                        ))
//                                        .then(argument(forceArg, BoolArgumentType.bool())
//                                                .executes(ctx -> executeClaim(
//                                                        ctx.getSource(),
//                                                        BlockPosArgumentType.getBlockPos(ctx, posArg),
//                                                        Nation.CODEC.byId(StringArgumentType.getString(ctx, nationArg), Nation.NATIONLESS),
//                                                        BoolArgumentType.getBool(ctx, forceArg)
//                                                ))
//                                        )
//                                )
//                        )
                                .then(literal("query")
                                        .then(argument(posArg, BlockPosArgumentType.blockPos())
                                                .executes(ctx -> executeQueryClaim(
                                                        ctx.getSource(),
                                                        BlockPosArgumentType.getBlockPos(ctx, posArg)
                                                )))
                                )
                );

        dispatcher.register(nation);
    }

    private static int executeQueryClaim(ServerCommandSource source, BlockPos pos) {
        Nation claimed = ClaimsLookupV2.getClaimedNation(source.getWorld(), pos);

        if (claimed.isNotNationless()) {
            source.sendFeedback(() -> Text.translatable("tnscore.commands.nation.claim.query.claimed", claimed.getTitle()), false);
        } else {
            source.sendFeedback(() -> Text.translatable("tnscore.commands.nation.claim.query.nationless"), false);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int executeClaim(ServerCommandSource source, BlockPos pos, Nation nation, boolean force) throws CommandSyntaxException {
        Chunk chunk = source.getWorld().getChunk(pos);
        NationClaimChunkComponent component = NationClaimChunkComponent.get(chunk);

        boolean success;
        if (force) {
            component.setClaim(source.getWorld(), nation);
            success = true;
        } else {
            success = component.tryClaim(source.getWorld(), nation);
        }

        if (success) {
            source.sendFeedback(() -> Text.translatable("tnscore.commands.nation.claim.success", nation.getTitle()), true);
        } else {
            throw UNKNOWN_PROPERTY_EXCEPTION.create(nation.getTitle(), component.getClaimedNation().getTitle());
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int executeHome(ServerCommandSource source, Nation nation) throws CommandSyntaxException {
        Entity entity = source.getEntityOrThrow();
        nation.teleportToHome(entity);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeGet(ServerCommandSource source, GameProfile profile) throws CommandSyntaxException {
        ServerPlayerEntity player = getRequestedPlayer(source.getServer(), profile);

        Nation nation = PlayerNationComponent.get(player).getNation();

        if (nation.isNotNationless()) {
            source.sendFeedback(
                    () -> Text.translatable("tnscore.commands.nation.get", player.getName(), nation.getTitle()),
                    false
            );
        } else {
            source.sendFeedback(
                    () -> Text.translatable("tnscore.commands.nation.get.nationless", player.getName()),
                    false
            );
        }

        return nation.ordinal();
    }

    private static int executeReset(ServerCommandSource source, ServerPlayerEntity player) {
        PlayerNationComponent.get(player).reset();
        source.sendFeedback(
                () -> Text.translatable("tnscore.commands.nation.reset.success", player.getName()),
                true
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int executeSet(ServerCommandSource source, ServerPlayerEntity player, String nationName) {
        Nation nation = Nation.CODEC.byId(nationName, Nation.NATIONLESS);
        PlayerNationComponent.get(player).joinNation(nation);

        source.sendFeedback(
                () -> Text.translatable("tnscore.commands.nation.set.success", player.getName(), nation.getTitle()),
                true
        );

        return Command.SINGLE_SUCCESS;
    }

    private static RequiredArgumentBuilder<ServerCommandSource, String> nationArgument(String name) {
        return argument(name, StringArgumentType.word())
                .suggests((context, builder) -> CompletableFuture.supplyAsync(() -> {
                    Arrays.stream(Nation.values()).forEach(ntn -> builder.suggest(ntn.asString()));

                    return builder.build();
                }));
    }


    // CREDIT:
    // InvView https://github.com/PotatoPresident/InvView/blob/master/src/main/java/us/potatoboy/invview/ViewCommand.java
    private static ServerPlayerEntity getRequestedPlayer(MinecraftServer server, GameProfile profile)
            throws CommandSyntaxException {
        ServerPlayerEntity requestedPlayer = server.getPlayerManager().getPlayer(profile.getName());

        // If player is not currently online
        if (requestedPlayer == null) {
            requestedPlayer = new ServerPlayerEntity(server, server.getOverworld(), profile, SyncedClientOptions.createDefault());

            Optional<ReadView> readViewOpt = server.getPlayerManager()
                    .loadPlayerData(requestedPlayer, new ErrorReporter.Logging(LogUtils.getLogger()));

            // Avoids player's dimension being reset to the overworld
            if (readViewOpt.isPresent()) {
                ReadView readView = readViewOpt.get();
                Optional<String> dimension = readView.getOptionalString("Dimension");

                if (dimension.isPresent()) {
                    ServerWorld world = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(dimension.get())));

                    if (world != null) {
                        ((EntityAccessor) requestedPlayer).invokeSetWorld(world);
                    }
                }
            }
        }

        return requestedPlayer;
    }

    private static int executeCountAllNations(ServerCommandSource source) throws IOException {
        MinecraftServer server = source.getServer();

        PlayerSaveHandler saveHandler = ((PlayerManagerAccesor) server.getPlayerManager()).getSaveHandler();
        Path saveFilePath = ((PlayerSaveHandlerAccessor) saveHandler).getPlayerDataDir().toPath();

        Map<Nation, Long> counts;
        try (
                Stream<Path> files = Files.list(saveFilePath);
                ErrorReporter.Logging errorReporter = new ErrorReporter.Logging(TNSCore.LOGGER)
        ) {
            counts = files.filter(path -> !path.endsWith(".dat_old"))
                    .map(path -> {
                        try {
                            return NbtIo.readCompressed(path, NbtSizeTracker.ofUnlimitedBytes());
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    })
                    .map(nbt -> NbtReadView.create(errorReporter, server.getRegistryManager(), nbt))
                    .map(view -> view.getReadView("cardinal_components").getReadView("tns-core:player_nation"))
                    .map(view -> view.read("nation", Nation.CODEC).orElse(Nation.NATIONLESS))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        }

        long total = counts.values().stream().mapToLong(l -> l).sum();

        if (total == 0) {
            source.sendError(Text.translatable("tnscore.commands.nation.count.error"));
            return 0;
        }

        for (Map.Entry<Nation, Long> count : counts.entrySet()) {
            long value = count.getValue();
            String percentage = String.format("%.2f", (double) value / total * 100);

            source.sendFeedback(
                    () -> Text.translatable(
                            "tnscore.commands.nation.count.entry",
                            count.getKey().getTitle(),
                            value,
                            percentage
                    ),
                    false
            );
        }

        return (int) total;
    }

    private NationCommand() {

    }
}