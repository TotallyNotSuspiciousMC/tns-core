package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
    @ModifyReturnValue(
            method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
            at = @At("RETURN")
    )
    private ItemStack modifyStairsCount(ItemStack original) {
        if (original.isIn(ItemTags.STAIRS) || original.isIn(ItemTags.TRAPDOORS)) {
            original.setCount(6);
        }

        return original;
    }
}