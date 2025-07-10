package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin {
    @WrapOperation(
            method = "canSpawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I",
                    ordinal = 1
            )
    )
    private static int blockSlimeChunkSpawning(Random instance, int i, Operation<Integer> original) {
        // call base to preserve side effects, but ignore file return value

        return 1; // will fail comparison to 0
    }
}