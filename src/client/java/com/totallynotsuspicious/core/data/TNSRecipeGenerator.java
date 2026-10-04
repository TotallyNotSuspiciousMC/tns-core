package com.totallynotsuspicious.core.data;

import com.totallynotsuspicious.core.item.TNSCoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class TNSRecipeGenerator extends FabricRecipeProvider {
    public TNSRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.MISC, Items.GLOW_LICHEN)
                        .unlockedBy(getHasName(Items.GLOW_INK_SAC), has(Items.GLOW_INK_SAC))
                        .requires(Items.VINE)
                        .requires(Items.GLOW_INK_SAC)
                        .save(output);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Items.RED_SAND)
                        .unlockedBy(getHasName(Items.SAND), has(Items.SAND))
                        .requires(Items.SAND)
                        .requires(Items.REDSTONE)
                        .save(output);

                shapeless(RecipeCategory.BUILDING_BLOCKS, Items.QUARTZ, 4)
                        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                        .requires(Items.QUARTZ_BLOCK)
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, Items.SPONGE, 4)
                        .unlockedBy(getHasName(Items.NAUTILUS_SHELL), has(Items.NAUTILUS_SHELL))
                        .pattern("# #")
                        .pattern("NFN")
                        .pattern("# #")
                        .define('#', ItemTags.WOOL)
                        .define('N', Items.NAUTILUS_SHELL)
                        .define('F', Items.PUFFERFISH)
                        .save(output);

                shapeless(RecipeCategory.MISC, TNSCoreItems.TREE_BANNER_PATTERN)
                        .unlockedBy("has_saplings", has(ItemTags.SAPLINGS))
                        .requires(ItemTags.SAPLINGS)
                        .requires(Items.PAPER)
                        .save(output);

                shapeless(RecipeCategory.MISC, TNSCoreItems.HAPPY_GHAST_TREAT)
                        .unlockedBy(getHasName(Items.SNOWBALL), has(Items.SNOWBALL))
                        .requires(Items.SNOWBALL)
                        .requires(Items.AMETHYST_SHARD)
                        .requires(Items.SUGAR)
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "TNSRecipeGenerator";
    }
}