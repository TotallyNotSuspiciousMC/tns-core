package com.totallynotsuspicious.core.world;

import com.totallynotsuspicious.core.TNSCore;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class TNSCoreChunkComponents implements ChunkComponentInitializer {
    static final ComponentKey<NationClaimChunkComponent> NATION_CLAIM = ComponentRegistry.getOrCreate(
            TNSCore.id("nation_claim"),
            NationClaimChunkComponent.class
    );

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
        registry.register(NATION_CLAIM, NationClaimChunkComponent::new);
    }
}