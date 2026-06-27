package com.totallynotsuspicious.core.tag;

import com.totallynotsuspicious.core.item.TNSBannerPatternTags;
import com.totallynotsuspicious.core.item.TNSBannerPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

public class TNSBannerPatternTagGenerator extends FabricTagsProvider<BannerPattern> {
    public TNSBannerPatternTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.BANNER_PATTERN, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(TNSBannerPatternTags.TREE_PATTERN_ITEM)
                .addOptional(TNSBannerPatterns.TREE);
    }
}