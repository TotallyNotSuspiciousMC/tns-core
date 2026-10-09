package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Creeper.class)
public class CreeperMixin {
    @WrapOperation(
            method = "explodeCreeper",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
            )
    )
    private void disableCreeperExplosionGriefing(
            ServerLevel instance,
            Entity entity,
            double x, double y, double z,
            float r,
            Level.ExplosionInteraction blockInteraction,
            Operation<Void> original
    ) {
        original.call(instance, entity, x, y, z, r, Level.ExplosionInteraction.NONE);
    }
}