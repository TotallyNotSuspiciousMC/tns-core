package com.totallynotsuspicious.core.nations;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class NationCommand {
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

        LiteralArgumentBuilder<ServerCommandSource> nation = literal("nation")
                .then(
                        literal("reset")
                                .requires(NationCommand::hasManagePermission)
                                .then(
                                        argument(playerArg, EntityArgumentType.player())
                                                .executes(ctx -> executeReset(
                                                        ctx.getSource(),
                                                        EntityArgumentType.getPlayer(ctx, playerArg)
                                                ))
                                )
                )
                .then(
                        literal("set")
                                .requires(NationCommand::hasManagePermission)
                                .then(
                                        argument(playerArg, EntityArgumentType.player())
                                                .then(nationArgument(nationArg).executes(ctx -> executeSet(
                                                                ctx.getSource(),
                                                                EntityArgumentType.getPlayer(ctx, playerArg),
                                                                StringArgumentType.getString(ctx, nationArg)
                                                        ))
                                                )
                                )
                )
                .then(
                        literal("get")
                                .requires(NationCommand::hasManagePermission)
                                .then(
                                        argument(playerArg, EntityArgumentType.player())
                                                .executes(ctx -> executeGet(
                                                        ctx.getSource(),
                                                        EntityArgumentType.getPlayer(ctx, playerArg)
                                                ))
                                )
                )
                .then(
                        literal("home")
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
                );

        dispatcher.register(nation);
    }

    private static int executeHome(ServerCommandSource source, Nation nation) throws CommandSyntaxException {
        Entity entity = source.getEntityOrThrow();

        Vec3d homePos = Vec3d.ofCenter(nation.getData().home());
        entity.teleport(
                source.getServer().getWorld(World.OVERWORLD),
                homePos.x,
                homePos.y,
                homePos.z,
                Set.of(),
                0f,
                0f,
                true
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int executeGet(ServerCommandSource source, ServerPlayerEntity player) {
        Nation nation = PlayerNationComponent.get(player).getNation();

        if (nation == Nation.NATIONLESS) {
            source.sendFeedback(
                    () -> Text.translatable("tnscore.commands.nation.get", player.getName(), nation),
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

    private NationCommand() {

    }
}