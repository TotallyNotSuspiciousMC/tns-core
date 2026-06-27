package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public final class TNSBannerPatterns {
    public static final ResourceKey<BannerPattern> TREE = key("tree");

    private static ResourceKey<BannerPattern> key(String id) {
        return ResourceKey.create(Registries.BANNER_PATTERN, TNSCore.id(id));
    }

    private TNSBannerPatterns() {

    }
}