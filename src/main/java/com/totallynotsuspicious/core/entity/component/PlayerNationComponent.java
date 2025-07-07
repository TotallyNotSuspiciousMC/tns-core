package com.totallynotsuspicious.core.entity.component;

import com.totallynotsuspicious.core.codec.TNSCodecs;
import com.totallynotsuspicious.core.nations.Nation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PlayerNationComponent implements Component {
    private static final String TIME_JOINED_NATION_KEY = "time_joined_first_nation";
    private static final String NATION_KEY = "nation";

    private final PlayerEntity player;

    @Nullable
    private Instant timeJoinedFirstNation = null;

    private Nation nation = Nation.NATIONLESS;

    public PlayerNationComponent(PlayerEntity player) {
        this.player = player;
    }

    public boolean tryJoinNation(Nation nation) {
        if (this.player instanceof ServerPlayerEntity serverPlayer) {
            return this.tryJoinNationServer(serverPlayer, nation);
        }
        return false;
    }

    private boolean tryJoinNationServer(ServerPlayerEntity serverPlayer, Nation nation) {
        Instant now = Instant.now();

        if (!this.canJoinNationNow(now)) {
            return false;
        }

        this.nation = nation;

        if (this.timeJoinedFirstNation == null && this.nation != Nation.NATIONLESS) {
            this.timeJoinedFirstNation = now;
        }

        serverPlayer.sendMessage(nation.getJoinMessage());
        serverPlayer.sendMessage(
                Text.literal("For the next week, you can request to change your nation at any time by pressing ")
                        .formatted(Formatting.GRAY, Formatting.ITALIC)
                        .append(Text.keybind("key.quickActions"))
                        .append(Text.literal(". After this time, you must make a ticket on Discord to change nations."))
        );

        return true;
    }

    @Override
    public void readData(ReadView readView) {
        this.timeJoinedFirstNation = readView.read(TIME_JOINED_NATION_KEY, TNSCodecs.INSTANT_CODEC).orElse(null);
        this.nation = readView.read(NATION_KEY, Nation.CODEC).orElse(Nation.NATIONLESS);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putNullable(TIME_JOINED_NATION_KEY, TNSCodecs.INSTANT_CODEC, this.timeJoinedFirstNation);
        writeView.put(NATION_KEY, Nation.CODEC, this.nation);
    }

    private boolean canJoinNationNow(Instant now) {
        if (this.timeJoinedFirstNation == null || this.nation == Nation.NATIONLESS) {
            return true;
        }
        return now.isBefore(this.timeJoinedFirstNation.plus(30L, ChronoUnit.SECONDS));
    }

    public static PlayerNationComponent get(PlayerEntity player) {
        return TNSCoreEntityComponents.PLAYER_NATION.get(player);
    }
}