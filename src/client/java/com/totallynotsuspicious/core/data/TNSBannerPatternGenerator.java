package com.totallynotsuspicious.core.data;

import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.item.TNSBannerPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TNSBannerPatternGenerator extends FabricDynamicRegistryProvider {
    public TNSBannerPatternGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.add(
                TNSBannerPatterns.TREE,
                new BannerPattern(TNSCore.id("tree"), "block.tnscore.banner.tree")
        );
    }

    @Override
    public String getName() {
        return "TNSBannerPatternGenerator";
    }
}