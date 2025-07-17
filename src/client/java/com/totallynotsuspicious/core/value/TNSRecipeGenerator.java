package com.totallynotsuspicious.core.value;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class TNSRecipeGenerator extends FabricRecipeProvider {
    public TNSRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                createShapeless(RecipeCategory.MISC, Items.GLOW_LICHEN)
                        .criterion(hasItem(Items.GLOW_INK_SAC), conditionsFromItem(Items.GLOW_INK_SAC))
                        .input(Items.VINE)
                        .input(Items.GLOW_INK_SAC)
                        .offerTo(exporter);

                createShapeless(RecipeCategory.BUILDING_BLOCKS, Items.RED_SAND)
                        .criterion(hasItem(Items.SAND), conditionsFromItem(Items.SAND))
                        .input(Items.SAND)
                        .input(Items.REDSTONE)
                        .offerTo(exporter);

                createShapeless(RecipeCategory.BUILDING_BLOCKS, Items.QUARTZ, 4)
                        .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                        .input(Items.QUARTZ_BLOCK)
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, Items.ENDER_EYE)
                        .group("ender_eye")
                        .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                        .pattern("#E#")
                        .pattern("P*S")
                        .pattern("#R#")
                        .input('#', Items.BLAZE_POWDER)
                        .input('E', Items.ECHO_SHARD)
                        .input('P', Items.PRISMARINE_SHARD)
                        .input('*', Items.ENDER_PEARL)
                        .input('S', Items.SLIME_BALL)
                        .input('R', Items.RESIN_CLUMP)
                        .offerTo(exporter);

                createShaped(RecipeCategory.BUILDING_BLOCKS, Items.SPONGE, 4)
                        .criterion(hasItem(Items.NAUTILUS_SHELL), conditionsFromItem(Items.NAUTILUS_SHELL))
                        .pattern("# #")
                        .pattern("NFN")
                        .pattern("# #")
                        .input('#', ItemTags.WOOL)
                        .input('N', Items.NAUTILUS_SHELL)
                        .input('F', Items.PUFFERFISH)
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "TNSRecipeGenerator";
    }
}