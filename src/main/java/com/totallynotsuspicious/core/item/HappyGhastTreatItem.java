package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import com.totallynotsuspicious.core.nations.Nation;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class HappyGhastTreatItem extends SimplePolymerItem {
    public HappyGhastTreatItem(Settings settings) {
        super(settings.component(DataComponentTypes.CONSUMABLE, ConsumableComponents.FOOD));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient() && user instanceof PlayerEntity player) {
            PlayerNationComponent component = PlayerNationComponent.get(player);

            if (component.getNation() == Nation.VAYUNE || Permissions.check(player, "tnscore.convert_happy_ghast_treat")) {
                player.giveOrDropStack(TNSCoreItems.ENCHANTED_HAPPY_GHAST_TREAT.getDefaultStack());
            }
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    @Nullable
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return PolymerResourcePackUtils.hasMainPack(context)
                ? super.getPolymerItemModel(stack, context)
                : Items.SNOWBALL.getComponents().get(DataComponentTypes.ITEM_MODEL);
    }
}