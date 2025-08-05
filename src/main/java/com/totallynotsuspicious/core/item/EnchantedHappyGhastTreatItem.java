package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.entity.TNSCoreStatusEffects;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class EnchantedHappyGhastTreatItem extends SimplePolymerItem {
    public EnchantedHappyGhastTreatItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();

        if (world.isClient() || user.isSpectator()) {
            return ActionResult.PASS;
        }

        if (entity instanceof HappyGhastEntity happyGhast) {
            this.feedToHappyGhast(stack, user, happyGhast);
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient() || user.isSpectator()) {
            return super.use(world, user, hand);
        }

        ItemStack stack = user.getStackInHand(hand);

        if (user.getVehicle() instanceof HappyGhastEntity happyGhast) {
            this.feedToHappyGhast(stack, user, happyGhast);
            return ActionResult.CONSUME;
        }

        return super.use(world, user, hand);
    }

    @Override
    @Nullable
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return PolymerResourcePackUtils.hasMainPack(context)
                ? super.getPolymerItemModel(stack, context)
                : Items.SNOWBALL.getComponents().get(DataComponentTypes.ITEM_MODEL);
    }

    private void feedToHappyGhast(ItemStack stack, PlayerEntity user, HappyGhastEntity happyGhast) {
        happyGhast.addStatusEffect(
                new StatusEffectInstance(
                        TNSCoreStatusEffects.SWIFT_FLIGHT,
                        8 * 60 * 20,
                        0
                ),
                user
        );

        stack.decrementUnlessCreative(1, user);
        happyGhast.playSound(SoundEvents.ENTITY_HAPPY_GHAST_AMBIENT);
    }
}