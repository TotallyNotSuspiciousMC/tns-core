package com.totallynotsuspicious.core.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemUsageContext;

@FunctionalInterface
public interface PlaceBlockCallback {
    Event<PlaceBlockCallback> EVENT = EventFactory.createArrayBacked(
            PlaceBlockCallback.class,
            listeners -> context -> {
                for (PlaceBlockCallback listener : listeners) {
                    listener.onBlockPlace(context);
                }
            }
    );

    void onBlockPlace(ItemUsageContext context);
}