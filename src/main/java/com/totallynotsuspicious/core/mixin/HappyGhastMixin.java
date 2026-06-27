package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.totallynotsuspicious.core.item.HappyGhastTreatItem;
import com.totallynotsuspicious.core.item.TNSCoreItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HappyGhast.class)
public class HappyGhastMixin {
    @WrapMethod(
            method = "mobInteract"
    )
    private InteractionResult onMobInteract(Player player, InteractionHand hand, Operation<InteractionResult> original) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(TNSCoreItemTags.HAPPY_GHAST_TREAT)) {
            HappyGhastTreatItem.feedToHappyGhast(stack, player, (HappyGhast) (Object) this);
            return InteractionResult.CONSUME;
        }

        return original.call(player, hand);
    }
}