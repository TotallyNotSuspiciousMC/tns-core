package com.totallynotsuspicious.core.item;

import com.totallynotsuspicious.core.TNSCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class TNSCoreItemTags {
    public static final TagKey<Item> HAPPY_GHAST_TREAT = key("happy_ghast_treat");

    private static TagKey<Item> key(String path) {
        return TagKey.create(Registries.ITEM, TNSCore.id(path));
    }

    private TNSCoreItemTags() {

    }
}