package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

import java.util.function.Consumer;
import java.util.function.Function;

public final class TNSCoreItems {
    public static final Item TREE_BANNER_PATTERN = registerSimple(
            "tree_banner_pattern",
            Items.MOJANG_BANNER_PATTERN,
            settings -> settings.maxCount(1)
                    .rarity(Rarity.COMMON)
                    .component(DataComponentTypes.PROVIDES_BANNER_PATTERNS, TNSBannerPatternTags.TREE_PATTERN_ITEM)
    );

    public static final Item HAPPY_GHAST_TREAT = register(
            "happy_ghast_treat",
            settings -> new HappyGhastTreatItem(
                    settings.maxCount(16)
                            .rarity(Rarity.COMMON)
            )
    );

    public static void initialize() {
        TNSCore.LOGGER.debug("Initialized TNS items");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(TREE_BANNER_PATTERN);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(HAPPY_GHAST_TREAT);
        });
    }

    private static Item register(String name, Function<Item.Settings, Item> settingsBuilder) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, TNSCore.id(name));
        Item.Settings settings = new Item.Settings()
                .registryKey(key);
        Item item = settingsBuilder.apply(settings);

        return Registry.register(Registries.ITEM, key, item);
    }

    private static Item registerSimple(String name, Item clientItem, Consumer<Item.Settings> settingsBuilder) {
        return register(name, settings -> {
            settingsBuilder.accept(settings);
            return new DefaultedModelPolymerItem(settings, clientItem);
        });
    }

    private TNSCoreItems() {

    }
}