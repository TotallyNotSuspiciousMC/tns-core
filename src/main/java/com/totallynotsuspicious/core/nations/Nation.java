package com.totallynotsuspicious.core.nations;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum Nation implements StringIdentifiable {
    FIDELIS("fidelis"),
    PANDORA("pandora"),
    TAURE_ARANOE("taure_aranie"),
    VAYUNE("vayune"),
    NATIONLESS("nationless");

    public static final Codec<Nation> CODEC = StringIdentifiable.createCodec(Nation::values);

    private final String name;

    Nation(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}