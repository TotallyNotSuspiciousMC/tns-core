package com.totallynotsuspicious.core.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class TNSEntityTypeTagGenerator extends FabricTagsProvider.EntityTypeTagsProvider {
    public TNSEntityTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(EntityTypeTags.SENSITIVE_TO_IMPALING)
                .add(EntityTypeIds.DROWNED);
    }
}