package com.totallynotsuspicious.core.entity;


import eu.pb4.polymer.core.api.other.PolymerMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SimplePolymerStatusEffect extends MobEffect implements PolymerMobEffect {
    public SimplePolymerStatusEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}