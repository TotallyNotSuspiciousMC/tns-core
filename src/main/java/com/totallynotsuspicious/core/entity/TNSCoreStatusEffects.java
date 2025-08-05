package com.totallynotsuspicious.core.entity;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public final class TNSCoreStatusEffects {
    public static final RegistryEntry<StatusEffect> SWIFT_FLIGHT = registerReference(
            "swift_flight",
            new SimplePolymerStatusEffect(StatusEffectCategory.BENEFICIAL, 0x42f5f5)
                    .addAttributeModifier(
                            EntityAttributes.FLYING_SPEED,
                            TNSCore.id("extra_speed_boost"),
                            0.5,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
    );

    public static void initialize() {
        TNSCore.LOGGER.debug("init tns status effects");
    }

    private static RegistryEntry<StatusEffect> registerReference(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, TNSCore.id(name), statusEffect);
    }

    private TNSCoreStatusEffects() {

    }
}