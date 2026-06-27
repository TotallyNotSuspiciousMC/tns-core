package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.entity.TNSCoreStatusEffects;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HappyGhastTreatItem extends SimplePolymerItem {
    public HappyGhastTreatItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
        return PolymerResourcePackUtils.hasMainPack(context)
                ? super.getPolymerItemModel(stack, context, lookup)
                : Items.SNOWBALL.components().get(DataComponents.ITEM_MODEL);
    }

    public static void feedToHappyGhast(ItemStack stack, Player user, HappyGhast happyGhast) {
        happyGhast.addEffect(
                new MobEffectInstance(
                        TNSCoreStatusEffects.SWIFT_FLIGHT,
                        30 * 20
                ),
                user
        );

        stack.consume(1, user);
        happyGhast.playSound(SoundEvents.HAPPY_GHAST_AMBIENT);
    }
}