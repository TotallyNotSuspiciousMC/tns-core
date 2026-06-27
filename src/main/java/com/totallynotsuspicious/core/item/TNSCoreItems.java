package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.function.Consumer;
import java.util.function.Function;

public final class TNSCoreItems {
    public static final Item TREE_BANNER_PATTERN = registerSimple(
            TNSCoreItemIds.TREE_BANNER_PATTERN,
            Items.MOJANG_BANNER_PATTERN,
            settings -> settings.stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(TNSBannerPatternTags.TREE_PATTERN_ITEM))
    );

    public static final Item HAPPY_GHAST_TREAT = register(
            TNSCoreItemIds.HAPPY_GHAST_TREAT,
            settings -> new HappyGhastTreatItem(
                    settings.stacksTo(16)
                            .rarity(Rarity.COMMON)
            )
    );

    public static void initialize() {
        TNSCore.LOGGER.debug("Initialized TNS items");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(TREE_BANNER_PATTERN);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.accept(HAPPY_GHAST_TREAT);
        });
    }

    private static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> settingsBuilder) {
        Item.Properties settings = new Item.Properties().setId(key);
        Item item = settingsBuilder.apply(settings);

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static Item registerSimple(ResourceKey<Item> key, Item clientItem, Consumer<Item.Properties> settingsBuilder) {
        return register(key, settings -> {
            settingsBuilder.accept(settings);
            return new DefaultedModelPolymerItem(settings, clientItem);
        });
    }

    private TNSCoreItems() {

    }
}