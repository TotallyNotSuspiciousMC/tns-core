package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class TNSBannerPatterns {
    public static final RegistryKey<BannerPattern> TREE = key("tree");

    private static RegistryKey<BannerPattern> key(String id) {
        return RegistryKey.of(RegistryKeys.BANNER_PATTERN, TNSCore.id(id));
    }

    private TNSBannerPatterns() {

    }
}