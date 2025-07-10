package com.totallynotsuspicious.core.registry;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

public final class TNSBiomeKeys {
    public static final RegistryKey<Biome> DEEP_ANCIENT_OCEAN = key("deep_ancient_ocean");

    private static RegistryKey<Biome> key(String path) {
        return RegistryKey.of(RegistryKeys.BIOME, TNSCore.id(path));
    }

    private TNSBiomeKeys() {

    }
}