package com.totallynotsuspicious.core.entity;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class TNSLootModifiers {
    private static final RegistryKey<LootTable> ENTITIES_HUSK = vanilla("entities/husk");

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key == ENTITIES_HUSK) {
                tableBuilder.pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.SAND)
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
                                        .apply(EnchantedCountIncreaseLootFunction.builder(registries, UniformLootNumberProvider.create(0.0f, 1.0f)))
                                )
                );
            }
        });

        LootTableEvents.REPLACE.register((key, original, source, registries) -> {
            if (key == LootTables.FISHING_TREASURE_GAMEPLAY) {
                return LootTable.builder()
                        .pool(
                                LootPool.builder()
                                        .with(ItemEntry.builder(Items.NAME_TAG))
                                        .with(ItemEntry.builder(Items.SADDLE))
                                        .with(
                                                ItemEntry.builder(Items.BOW)
                                                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0F, 0.25F)))
                                                        .apply(EnchantWithLevelsLootFunction.builder(registries, ConstantLootNumberProvider.create(30.0F)))
                                        )
                                        .with(
                                                ItemEntry.builder(Items.FISHING_ROD)
                                                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0F, 0.25F)))
                                                        .apply(EnchantWithLevelsLootFunction.builder(registries, ConstantLootNumberProvider.create(30.0F)))
                                        )
                                        .with(ItemEntry.builder(Items.BOOK))
                                        .with(ItemEntry.builder(Items.NAUTILUS_SHELL))
                        ).build();
            }

            return null;
        });
    }

    private static RegistryKey<LootTable> vanilla(String name) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.ofVanilla(name));
    }

    private TNSLootModifiers() {

    }
}