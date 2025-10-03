package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import com.totallynotsuspicious.core.nations.Nation;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class ResinCandyItem extends SimplePolymerItem {
    public ResinCandyItem(Settings settings) {
        super(settings
                .component(DataComponentTypes.CONSUMABLE, ConsumableComponents.FOOD)
                .food(new FoodComponent.Builder().alwaysEdible().nutrition(2).saturationModifier(0.1f).build())
        );
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (this == TNSCoreItems.RESIN_CANDY && !world.isClient() && user instanceof PlayerEntity player) {
            PlayerNationComponent component = PlayerNationComponent.get(player);

            if (component.getNation() == Nation.TAURE_ARANIE || Permissions.check(player, "tnscore.convert_resin_candy")) {
                player.giveOrDropStack(TNSCoreItems.ENCHANTED_RESIN_CANDY.getDefaultStack());
            }
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    @Nullable
    public Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return Items.RESIN_CLUMP.getComponents().get(DataComponentTypes.ITEM_MODEL);
    }
}
