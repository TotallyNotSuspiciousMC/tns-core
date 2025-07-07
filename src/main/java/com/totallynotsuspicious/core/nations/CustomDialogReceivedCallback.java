package com.totallynotsuspicious.core.nations;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.Optional;

@FunctionalInterface
public interface CustomDialogReceivedCallback {
    Event<CustomDialogReceivedCallback> EVENT = EventFactory.createArrayBacked(
            CustomDialogReceivedCallback.class,
            listeners -> (handler, id, payload) -> {
                for (CustomDialogReceivedCallback listener : listeners) {
                    listener.onCustomDialogReceived(handler, id, payload);
                }
            }
    );

    void onCustomDialogReceived(ServerPlayNetworkHandler handler, Identifier id, Optional<NbtElement> payload);
}