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
    private static final String ONBOARDED_KEY = "onboarded";

    private final PlayerEntity player;

    @Nullable
    private Instant timeJoinedFirstNation = null;

    private Nation nation = Nation.NATIONLESS;

    private boolean onboarded = false;

    public PlayerNationComponent(PlayerEntity player) {
        this.player = player;
    }

    public void reset() {
        this.timeJoinedFirstNation = null;
        this.nation = Nation.NATIONLESS;
        this.onboarded = false;
    }

    public void joinNation(Nation nation) {
        if (!this.onboarded || this.timeJoinedFirstNation == null) {
            this.timeJoinedFirstNation = Instant.now();
        }
        this.onboarded = true;
        this.nation = nation;
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

        this.joinNation(nation);

        serverPlayer.sendMessage(nation.getJoinMessage());
        serverPlayer.sendMessage(
                Text.translatable("tnscore.nations.join.accepted.change", Text.keybind("key.quickActions"))
                        .formatted(Formatting.GRAY)
        );

        return true;
    }

    @Override
    public void readData(ReadView readView) {
        this.timeJoinedFirstNation = readView.read(TIME_JOINED_NATION_KEY, TNSCodecs.INSTANT_CODEC).orElse(null);
        this.nation = readView.read(NATION_KEY, Nation.CODEC).orElse(Nation.NATIONLESS);
        this.onboarded = readView.getBoolean(ONBOARDED_KEY, false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putNullable(TIME_JOINED_NATION_KEY, TNSCodecs.INSTANT_CODEC, this.timeJoinedFirstNation);
        writeView.put(NATION_KEY, Nation.CODEC, this.nation);
        writeView.putBoolean(ONBOARDED_KEY, this.onboarded);
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

    public boolean isOnboarded() {
        return onboarded;
    }

    public Nation getNation() {
        return this.nation;
    }
}