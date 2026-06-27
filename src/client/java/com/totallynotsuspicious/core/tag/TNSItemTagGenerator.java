package com.totallynotsuspicious.core.tag;

import com.totallynotsuspicious.core.item.TNSCoreItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TNSItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {
    public TNSItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, @Nullable BlockTagsProvider blockTagsProvider) {
        super(output, registryLookupFuture, blockTagsProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(ItemTags.HAPPY_GHAST_TEMPT_ITEMS)
                .add(TNSCoreItemIds.HAPPY_GHAST_TREAT);

        builder(ConventionalItemTags.FOODS)
                .add(TNSCoreItemIds.HAPPY_GHAST_TREAT);

        builder(ConventionalItemTags.ANIMAL_FOODS)
                .add(TNSCoreItemIds.HAPPY_GHAST_TREAT);
    }
}