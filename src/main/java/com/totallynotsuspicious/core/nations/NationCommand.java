package com.totallynotsuspicious.core.nations;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;


import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class NationCommand {
    public static void registerCommand(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment
    ) {
        final String playerArg = "player";
        final String nationArg = "nation";

        LiteralArgumentBuilder<ServerCommandSource> nation = literal("nation")
                .requires(src -> src.hasPermissionLevel(3) || Permissions.check(src, "tnscore.nations.manage"))
                .then(
                        literal("reset")
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
                                .then(
                                        argument(playerArg, EntityArgumentType.player())
                                                .then(nationArgument(nationArg).executes(ctx -> executeSet(
                                                                ctx.getSource(),
                                                                EntityArgumentType.getPlayer(ctx, playerArg),
                                                                StringArgumentType.getString(ctx, nationArg)
                                                        ))
                                                )
                                )
                );

        dispatcher.register(nation);
    }

    private static int executeReset(ServerCommandSource source, ServerPlayerEntity player) {
        PlayerNationComponent.get(player).reset();
        source.sendFeedback(
                () -> Text.translatable("tnscore.commands.nation.reset.success", player.getName()),
                true
        );
        return 0;
    }

    private static int executeSet(ServerCommandSource source, ServerPlayerEntity player, String nationName) {
        Nation nation = Nation.CODEC.byId(nationName, Nation.NATIONLESS);
        PlayerNationComponent.get(player).joinNation(nation);

        source.sendFeedback(
                () -> Text.translatable("tnscore.commands.nation.set.success", player.getName(), nation.getTitle()),
                true
        );

        return 0;
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