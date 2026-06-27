package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
    @ModifyReturnValue(
            method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN")
    )
    private ItemStack modifyStairsCount(ItemStack original) {
        if (original.is(BlockItemTags.STAIRS.item()) || original.is(ItemTags.WOODEN_TRAPDOORS)) {
            original.setCount(Math.max(6, original.count()));
        }

        return original;
    }
}