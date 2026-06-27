package com.totallynotsuspicious.core.entity;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class TNSLootModifiers {
    private static final ResourceKey<LootTable> ENTITIES_HUSK = vanilla("entities/husk");

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key == ENTITIES_HUSK) {
                tableBuilder.pool(
                        LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.SAND)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0f, 1.0f)))
                                )
                                .build()
                );
            }
        });
    }

    private static ResourceKey<LootTable> vanilla(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace(name));
    }

    private TNSLootModifiers() {

    }
}