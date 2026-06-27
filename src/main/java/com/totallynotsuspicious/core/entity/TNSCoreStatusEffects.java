package com.totallynotsuspicious.core.entity;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class TNSCoreStatusEffects {
    public static final Holder<MobEffect> SWIFT_FLIGHT = registerHolder(
            "swift_flight",
            new SimplePolymerStatusEffect(MobEffectCategory.BENEFICIAL, 0x42f5f5)
                    .addAttributeModifier(
                            Attributes.FLYING_SPEED,
                            TNSCore.id("extra_speed_boost"),
                            0.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
    );

    public static void initialize() {
        TNSCore.LOGGER.debug("init tns status effects");
    }

    private static Holder<MobEffect> registerHolder(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, TNSCore.id(name), statusEffect);
    }

    private TNSCoreStatusEffects() {

    }
}