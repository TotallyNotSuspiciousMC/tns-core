package com.totallynotsuspicious.core.tag;

import com.totallynotsuspicious.core.registry.TNSBiomeKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class TNSBiomeTagGenerator extends FabricTagProvider<Biome> {
    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricDataOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public TNSBiomeTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        builder(BiomeTags.IS_DEEP_OCEAN)
                .addOptional(TNSBiomeKeys.DEEP_ANCIENT_OCEAN);

        builder(BiomeTags.OCEAN_RUIN_COLD_HAS_STRUCTURE)
                .addOptional(TNSBiomeKeys.DEEP_ANCIENT_OCEAN);

        builder(ConventionalBiomeTags.IS_DEEP_OCEAN)
                .addOptional(TNSBiomeKeys.DEEP_ANCIENT_OCEAN);

        builder(BiomeTags.OCEAN_MONUMENT_HAS_STRUCTURE)
                .addOptional(TNSBiomeKeys.DEEP_ANCIENT_OCEAN)
                .setReplace(true);
    }
}