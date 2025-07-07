package com.totallynotsuspicious.core.nations;

import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CustomEventHandlerRegistry implements CustomDialogReceivedCallback {
    public static final CustomEventHandlerRegistry INSTANCE = new CustomEventHandlerRegistry();

    private final Map<Identifier, CustomEventHandler> dispatch = new HashMap<>();

    public static void register(Identifier id, CustomEventHandler handler) {
        INSTANCE.dispatch.put(id, handler);
    }

    public static void initialize() {
        CustomDialogReceivedCallback.EVENT.register(CustomEventHandlerRegistry.INSTANCE);
    }

    @Override
    public void onCustomDialogReceived(ServerPlayNetworkHandler handler, Identifier id, Optional<NbtElement> payload) {
        CustomEventHandler eventHandler = this.dispatch.get(id);
        if (eventHandler != null) {
            eventHandler.apply(handler, payload);
        }
    }

    private CustomEventHandlerRegistry() {

    }
}