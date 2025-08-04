package com.totallynotsuspicious.core.entity;

import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SimplePolymerStatusEffect extends StatusEffect implements PolymerStatusEffect {
    public SimplePolymerStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}