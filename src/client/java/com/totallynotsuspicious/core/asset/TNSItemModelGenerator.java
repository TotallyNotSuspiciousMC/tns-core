package com.totallynotsuspicious.core.asset;

import com.totallynotsuspicious.core.item.TNSCoreItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class TNSItemModelGenerator extends FabricModelProvider {

    public TNSItemModelGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(TNSCoreItems.TREE_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
    }
}