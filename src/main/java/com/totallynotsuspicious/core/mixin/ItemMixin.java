package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.totallynotsuspicious.core.item.HappyGhastTreatItem;
import com.totallynotsuspicious.core.item.TNSCoreItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin {
    @WrapMethod(
            method = "use"
    )
    private InteractionResult onUse(Level level, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        ItemStack stack = player.getItemInHand(hand);
        Entity vehicle = player.getVehicle();

        if (vehicle instanceof HappyGhast happyGhast && stack.is(TNSCoreItemTags.HAPPY_GHAST_TREAT)) {
            HappyGhastTreatItem.feedToHappyGhast(stack, player, happyGhast);
            return InteractionResult.CONSUME;
        }

        return original.call(level, player, hand);
    }
}