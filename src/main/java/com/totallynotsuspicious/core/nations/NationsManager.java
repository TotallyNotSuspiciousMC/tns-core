package com.totallynotsuspicious.core.nations;

import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import com.totallynotsuspicious.core.event.PlaceBlockCallback;
import com.totallynotsuspicious.core.nations.claims.ClaimsLookupV2;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Objects;

public final class NationsManager {
    public static final RegistryKey<Dialog> WELCOME_DIALOG = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("welcome"));
    public static final RegistryKey<Dialog> JOIN_NATION_DIALOG = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("join_nation"));
    public static final RegistryKey<Dialog> CONFIRM_NATIONLESS_DIALOG = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("confirm_nationless"));

    public static void initialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!PlayerNationComponent.get(handler.getPlayer()).isOnboarded()) {
                handler.getPlayer().changeGameMode(GameMode.SPECTATOR);
                openDialog(handler.getPlayer(), WELCOME_DIALOG);

                TNSCore.LOGGER.info("Onboarding {} to nations", handler.getPlayer().getName());
            }
        });


        CustomEventHandlerRegistry.register(TNSCore.id("join_nation_form"), (handler, payload) -> {
            if (payload.isPresent()) {
                joinNation(handler.getPlayer(), JoinNationPayload.decode(payload.orElseThrow()));
            } else {
                openDialog(handler.getPlayer(), JOIN_NATION_DIALOG);
            }
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient()) {
                checkClaimedArea(world, pos, player);
            }
        });

        PlaceBlockCallback.EVENT.register(context -> {
            if (!context.getWorld().isClient()) {
                checkClaimedArea(context.getWorld(), context.getBlockPos(), context.getPlayer());
            }
        });
    }

    private static void checkClaimedArea(World world, BlockPos pos, PlayerEntity player) {
        if (Permissions.check(player, "tnscore.bypassclaims")) {
            return;
        }

        PlayerNationComponent nationComponent = PlayerNationComponent.get(player);
        Nation claimedNation = ClaimsLookupV2.getClaimedNation(world, pos);
        if (claimedNation.isNotNationless() && claimedNation != nationComponent.getNation()) {
            player.sendMessage(
                    Text.translatable("tnscore.nations.claimedChunk", claimedNation.getTitle())
                            .formatted(Formatting.RED),
                    false
            );
        }
    }

    private static void joinNation(ServerPlayerEntity player, JoinNationPayload payload) {
        if (payload.nation() == Nation.NATIONLESS && !payload.confirmed()) {
            openDialog(player, CONFIRM_NATIONLESS_DIALOG);
            return;
        } else if (PlayerNationComponent.get(player).tryJoinNation(payload.nation())) {
            TNSCore.LOGGER.info("Player {} joined nation {}", player.getName(), payload.nation());
        } else {
            TNSCore.LOGGER.info(
                    "Player {} tried to join the nation {}, but it has been too long since they first joined a nation.",
                    player.getName(),
                    payload.nation()
            );

            player.sendMessage(Text.translatable("tnscore.nations.join.deny.tooOld").formatted(Formatting.RED));
        }
        player.changeGameMode(GameMode.SURVIVAL);
    }

    public static void openDialog(ServerPlayerEntity player, RegistryKey<Dialog> dialogKey) {
        RegistryEntry<Dialog> dialog = getEntry(
                Objects.requireNonNull(player.getServer()).getRegistryManager(),
                dialogKey
        );

        player.openDialog(dialog);
    }

    public static RegistryEntry<Dialog> getEntry(DynamicRegistryManager registries, RegistryKey<Dialog> key) {
        return registries.getOrThrow(RegistryKeys.DIALOG).getOrThrow(key);
    }

    private NationsManager() {

    }
}