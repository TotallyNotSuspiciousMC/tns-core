package com.totallynotsuspicious.core.nations;

import com.totallynotsuspicious.core.TNSCore;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.Set;

public final class NationsDialogManager {
    private static final String NATIONS_ONBOARDED_TAG = "nations_onboarded";

    private static final RegistryKey<Dialog> WELCOME_DIALOG = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("welcome"));

    public static void initialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Set<String> tags = handler.getPlayer().getCommandTags();
            if (!tags.contains(NATIONS_ONBOARDED_TAG)) {
                onNewPlayerJoin(handler, server);
            }
        });

        CustomDialogReceivedCallback.EVENT.register((handler, id, payload) -> {
            TNSCore.LOGGER.info("Received custom event id {} with payload {}", id, payload);
        });
    }

    private static void onNewPlayerJoin(ServerPlayNetworkHandler handler, MinecraftServer server) {
        RegistryEntry<Dialog> welcome = server.getRegistryManager()
                .getOrThrow(RegistryKeys.DIALOG)
                .getOrThrow(WELCOME_DIALOG);

        handler.getPlayer().openDialog(welcome);
    }

    private NationsDialogManager() {

    }
}