package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.totallynotsuspicious.core.event.PlaceBlockCallback;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @WrapMethod(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;"
    )
    private ActionResult onPlace(ItemPlacementContext context, Operation<ActionResult> original) {
        ActionResult result = original.call(context);

        if (result != ActionResult.FAIL) {
            PlaceBlockCallback.EVENT.invoker().onBlockPlace(context);
        }

        return result;
    }
}