package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.totallynotsuspicious.core.nations.CustomDialogReceivedCallback;
import net.minecraft.network.packet.c2s.common.CustomClickActionC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerCommonNetworkHandler.class)
public class ServerCommonNetworkHandlerMixin {
    @Shadow @Final protected MinecraftServer server;

    @WrapMethod(
            method = "onCustomClickAction"
    )
    protected void overrideForPlayHandler(CustomClickActionC2SPacket packet, Operation<Void> original) {
        original.call(packet);
    }

    @Mixin(ServerPlayNetworkHandler.class)
    private static class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandlerMixin {
        @Override
        protected void overrideForPlayHandler(CustomClickActionC2SPacket packet, Operation<Void> original) {
            super.overrideForPlayHandler(packet, original);

            this.server.executeSync(() -> {
                CustomDialogReceivedCallback.EVENT.invoker().onCustomDialogReceived(
                        (ServerPlayNetworkHandler) (Object) this,
                        packet.id(),
                        packet.payload()
                );
            });
        }
    }
}