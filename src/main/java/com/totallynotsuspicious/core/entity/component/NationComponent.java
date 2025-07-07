package com.totallynotsuspicious.core.entity.component;

import com.totallynotsuspicious.core.nations.Nation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

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
//        this.lastChosenNation = Nation.byName(readView.getOptionalString(LAST_CHOSEN_NATION_KEY).orElse("none")).orElse(null);
    }

    @Override
    public void writeData(WriteView writeView) {
        if (this.lastChosenNation != null) {
            writeView.putString(LAST_CHOSEN_NATION_KEY, this.lastChosenNation.asString());
        }
    }
}