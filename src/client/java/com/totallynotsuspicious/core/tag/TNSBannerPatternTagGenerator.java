package com.totallynotsuspicious.core.tag;

import com.totallynotsuspicious.core.item.TNSBannerPatternTags;
import com.totallynotsuspicious.core.item.TNSBannerPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TNSBannerPatternTagGenerator extends FabricTagProvider<BannerPattern> {
    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricDataOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public TNSBannerPatternTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BANNER_PATTERN, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        builder(TNSBannerPatternTags.TREE_PATTERN_ITEM)
                .addOptional(TNSBannerPatterns.TREE);
    }
}