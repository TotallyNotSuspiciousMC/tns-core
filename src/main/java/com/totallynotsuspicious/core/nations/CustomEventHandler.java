package com.totallynotsuspicious.core.nations;

import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.Optional;

@FunctionalInterface
public interface CustomEventHandler {
    void apply(ServerPlayNetworkHandler handler, Optional<NbtElement> payload);
}
