package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public final class TNSBannerPatternTags {
    public static final TagKey<BannerPattern> TREE_PATTERN_ITEM = of("pattern_item/tree");

    private static TagKey<BannerPattern> of(String id) {
        return TagKey.create(Registries.BANNER_PATTERN, TNSCore.id(id));
    }

    private TNSBannerPatternTags() {

    }
}