package com.totallynotsuspicious.core.data;

import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.item.TNSBannerPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

public class TNSBannerPatternGenerator extends FabricDynamicRegistryProvider {
    public TNSBannerPatternGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "TNSBannerPatternGenerator";
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(
                TNSBannerPatterns.TREE,
                new BannerPattern(TNSCore.id("tree"), "block.tnscore.banner.tree")
        );
    }
}