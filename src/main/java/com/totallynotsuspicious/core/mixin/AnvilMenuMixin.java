package com.totallynotsuspicious.core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @WrapMethod(method = "calculateIncreasedRepairCost")
    private static int removeCostScaling(int baseCost, Operation<Integer> original) {
        original.call(baseCost); // allows any side effects from other mods to still apply
        return 0;
    }
}