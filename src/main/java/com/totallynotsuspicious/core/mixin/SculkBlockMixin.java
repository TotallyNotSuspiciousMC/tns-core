package com.totallynotsuspicious.core.mixin;

import com.totallynotsuspicious.core.world.EchoShardDropper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.SculkSpreader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkBlock.class)
public class SculkBlockMixin {
    @Inject(
            method = "attemptUseCharge",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/LevelAccessor;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
            )
    )
    private void spawnEchoShard(
            SculkSpreader.ChargeCursor cursor,
            LevelAccessor level,
            BlockPos originPos,
            RandomSource random,
            SculkSpreader spreader,
            boolean spreadVein,
            CallbackInfoReturnable<Integer> cir
    ) {
        if (level instanceof ServerLevel serverLevel) {
            EchoShardDropper.tryDrop(serverLevel, cursor.getPos().above());
        }
    }
}