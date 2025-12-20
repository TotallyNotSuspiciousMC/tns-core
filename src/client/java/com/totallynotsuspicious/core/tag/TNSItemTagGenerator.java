package com.totallynotsuspicious.core.tag;

import com.totallynotsuspicious.core.item.TNSCoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class TNSItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public TNSItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(ItemTags.HAPPY_GHAST_TEMPT_ITEMS)
                .add(TNSCoreItems.HAPPY_GHAST_TREAT);

        valueLookupBuilder(ConventionalItemTags.FOODS)
                .add(TNSCoreItems.HAPPY_GHAST_TREAT);

        valueLookupBuilder(ConventionalItemTags.ANIMAL_FOODS)
                .add(TNSCoreItems.HAPPY_GHAST_TREAT);
    }
}