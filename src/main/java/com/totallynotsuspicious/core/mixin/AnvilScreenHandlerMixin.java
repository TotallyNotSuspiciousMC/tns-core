package com.totallynotsuspicious.core.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Inject(
            method = "getNextCost",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void removeCostScaling(int cost, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }
}