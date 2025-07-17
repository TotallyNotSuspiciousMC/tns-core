package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class TNSBannerPatternTags {
    public static final TagKey<BannerPattern> TREE_PATTERN_ITEM = of("pattern_item/tree");

    private static TagKey<BannerPattern> of(String id) {
        return TagKey.of(RegistryKeys.BANNER_PATTERN, TNSCore.id(id));
    }

    private TNSBannerPatternTags() {

    }
}