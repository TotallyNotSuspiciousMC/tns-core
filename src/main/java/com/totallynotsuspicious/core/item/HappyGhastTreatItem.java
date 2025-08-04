package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class HappyGhastTreatItem extends DefaultedModelPolymerItem {
    private static final double BOOST_PER_TREAT = 0.1;
    private static final double MAX_BOOST = 0.5;
    private static final Identifier BASE_SPEED_BOOST = TNSCore.id("base_speed_boost");

    public HappyGhastTreatItem(Settings settings, Item polymerItem) {
        super(settings, polymerItem);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if (world.isClient() || user.isSpectator()) {
            return ActionResult.PASS;
        }

        if (stack.isOf(this) && entity instanceof HappyGhastEntity happyGhast) {
            EntityAttributeInstance flyingSpeed = happyGhast.getAttributeInstance(EntityAttributes.FLYING_SPEED);
            EntityAttributeModifier baseModifier = flyingSpeed.getModifier(BASE_SPEED_BOOST);

            double speed = baseModifier != null ? baseModifier.value() : 0;
            boolean applyEffect = speed >= MAX_BOOST;
            speed = MathHelper.clamp(speed + BOOST_PER_TREAT, 0, MAX_BOOST);

            if (applyEffect) {
                
            } else {
                if (baseModifier != null) {
                    flyingSpeed.removeModifier(baseModifier);
                }

                flyingSpeed.addPersistentModifier(new EntityAttributeModifier(
                        BASE_SPEED_BOOST,
                        speed,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
            }

            stack.decrementUnlessCreative(1, user);
            happyGhast.playSound(SoundEvents.ENTITY_HAPPY_GHAST_AMBIENT);

            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }
}