package com.totallynotsuspicious.core.value;

import com.totallynotsuspicious.core.registry.TNSBiomeKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.OverworldBiomeCreator;

import java.util.concurrent.CompletableFuture;

public class TNSBiomeGenerator extends FabricDynamicRegistryProvider {
    public TNSBiomeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.add(
                TNSBiomeKeys.DEEP_ANCIENT_OCEAN,
                OverworldBiomeCreator.createNormalOcean(
                        registries.getOrThrow(RegistryKeys.PLACED_FEATURE),
                        registries.getOrThrow(RegistryKeys.CONFIGURED_CARVER),
                        true
                )
        );
    }

    @Override
    public String getName() {
        return "TNSBiomeGenerator";
    }
}