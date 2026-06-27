package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class TNSCoreItemIds {
    public static final ResourceKey<Item> TREE_BANNER_PATTERN = create("tree_banner_pattern");
    public static final ResourceKey<Item> HAPPY_GHAST_TREAT = create("happy_ghast_treat");

    private static ResourceKey<Item> create(String path) {
        return ResourceKey.create(Registries.ITEM, TNSCore.id(path));
    }

    private TNSCoreItemIds() {

    }
}