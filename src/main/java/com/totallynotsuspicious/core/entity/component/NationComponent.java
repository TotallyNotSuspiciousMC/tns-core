package com.totallynotsuspicious.core.entity.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.HashMap;
import java.util.Map;

public class NationComponent implements Component {
    private static final String LAST_CHOSEN_NATION_KEY = "last_chosen_nation";

    private final PlayerEntity player;

    @Nullable
    private Nation lastChosenNation = null;

    public NationComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void readData(ReadView readView) {
        this.lastChosenNation = Nation.byName(readView.getOptionalString(LAST_CHOSEN_NATION_KEY).orElse(null));
    }

    @Override
    public void writeData(WriteView writeView) {
        if (this.lastChosenNation != null) {
            writeView.putString(LAST_CHOSEN_NATION_KEY, this.lastChosenNation.asString());
        }
    }

    public enum Nation implements StringIdentifiable {
        FIDELIS("fidelis"),
        PANDORA("pandora"),
        TAURE_ARANOE("taure_aranoe"),
        VAYUNE("vayune");

        private static final Map<String, Nation> names = new HashMap<>();

        private final String name;

        Nation(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Nation byName(String name) {
            return names.get(name);
        }
    }
}