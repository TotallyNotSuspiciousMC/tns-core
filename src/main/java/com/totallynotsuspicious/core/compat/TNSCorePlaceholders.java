package com.totallynotsuspicious.core.compat;

import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.entity.component.PlayerNationComponent;
import com.totallynotsuspicious.core.nations.Nation;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;

public final class TNSCorePlaceholders {
    public static void initialize() {
        Placeholders.register(
                TNSCore.id("player_nation"),
                (context, argument) -> {
                    if (context.hasPlayer()) {
                        PlayerNationComponent component = PlayerNationComponent.get(context.player());
                        return PlaceholderResult.value(component.getNation().getTitle());
                    }
                    return PlaceholderResult.invalid("A player context is required");
                }
        );

        Placeholders.register(
                TNSCore.id("nation_title"),
                (context, argument) -> {
                    if (argument == null) {
                        return PlaceholderResult.invalid("No argument");
                    }

                    Nation nation = Nation.CODEC.byId(argument);
                    if (nation == null) {
                        return PlaceholderResult.invalid("Unbound nation ID: " + argument);
                    }

                    return PlaceholderResult.value(nation.getTitle());
                }
        );
    }

    private TNSCorePlaceholders() {

    }
}