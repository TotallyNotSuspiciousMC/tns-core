package com.totallynotsuspicious.core.nations;

import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.Set;

public final class NationsManager {
    private static final String NATIONS_ONBOARDED_TAG = "nations_onboarded";
    private static final RegistryKey<Dialog> WELCOME_DIALOG = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("welcome"));
    private static final RegistryKey<Dialog> JOIN_NATION_DIALOG = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("join_nation"));
    private static final RegistryKey<Dialog> CONFIRM_NATIONLESS_DIALOG = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("confirm_nationless"));

    public static void initialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Set<String> tags = handler.getPlayer().getCommandTags();
            if (!tags.contains(NATIONS_ONBOARDED_TAG)) {
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
    }

    private static void joinNation(ServerPlayerEntity player, JoinNationPayload payload) {
        if (payload.nation() == Nation.NATIONLESS && !payload.confirmed()) {
            openDialog(player, CONFIRM_NATIONLESS_DIALOG);
        } else if (PlayerNationComponent.get(player).tryJoinNation(payload.nation())) {
            TNSCore.LOGGER.info("Player {} joined nation {}", player.getName(), payload.nation());
            player.getCommandTags().add(NATIONS_ONBOARDED_TAG);
        } else {
            TNSCore.LOGGER.info(
                    "Player {} tried to join the nation {}, but it has been too long since they first joined a nation.",
                    player.getName(),
                    payload.nation()
            );

            player.sendMessage(
                    Text.literal("Sorry, it has been too long since you first joined your nation to freely change nations now. Please make a ticket on Discord requesting to change nations. Include in your ticket which nation you want to switch to or leave, the nation you are currently in, and ")
                            .formatted(Formatting.RED)
                            .append(Text.literal("explain why you want to change nations.").formatted(Formatting.ITALIC))
            );
        }
    }

    private static void openDialog(ServerPlayerEntity player, RegistryKey<Dialog> dialogKey) {
        RegistryEntry<Dialog> dialog = Objects.requireNonNull(player.getServer())
                .getRegistryManager()
                .getOrThrow(RegistryKeys.DIALOG)
                .getOrThrow(dialogKey);

        player.openDialog(dialog);
    }

    private NationsManager() {

    }
}