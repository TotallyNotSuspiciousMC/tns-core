package com.totallynotsuspicious.core.world;

import com.totallynotsuspicious.core.nations.Nation;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.chunk.Chunk;
import org.ladysnake.cca.api.v3.component.Component;

public class NationClaimChunkComponent implements Component {
    public static final Event<ClaimEvent> ON_CLAIMED = EventFactory.createArrayBacked(
            ClaimEvent.class,
            listeners -> (world, claim, oldNation) -> {
                for (ClaimEvent listener : listeners) {
                    listener.onClaimChanged(world, claim, oldNation);
                }
            }
    );

    public static final Event<ClaimEvent> ON_UNCLAIMED = EventFactory.createArrayBacked(
            ClaimEvent.class,
            listeners -> (world, claim, oldNation) -> {
                for (ClaimEvent listener : listeners) {
                    listener.onClaimChanged(world, claim, oldNation);
                }
            }
    );

    private static final String CLAIMED_NATION_KEY = "claimed_nation";

    private final Chunk chunk;
    private Nation claimedNation = Nation.NATIONLESS;

    public NationClaimChunkComponent(Chunk chunk) {
        this.chunk = chunk;
    }

    public static NationClaimChunkComponent get(Chunk chunk) {
        return TNSCoreChunkComponents.NATION_CLAIM.get(chunk);
    }

    @Override
    public void readData(ReadView readView) {
        this.claimedNation = readView.read(CLAIMED_NATION_KEY, Nation.CODEC).orElse(Nation.NATIONLESS);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.put(CLAIMED_NATION_KEY, Nation.CODEC, this.claimedNation);
    }

    public boolean tryClaim(ServerWorld world, Nation nation) {
        if (this.claimedNation.isNationless()) {
            this.setClaim(world, nation);
            return true;
        }

        return false;
    }

    public void setClaim(ServerWorld world, Nation nation) {
        Nation oldNation = this.claimedNation;
        this.claimedNation = nation;

        if (this.claimedNation.isNotNationless()) {
            ON_CLAIMED.invoker().onClaimChanged(world, this, oldNation);
        } else {
            ON_UNCLAIMED.invoker().onClaimChanged(world, this, oldNation);
        }
    }

    public Nation getClaimedNation() {
        return claimedNation;
    }

    public boolean isBuildingAllowedBy(Nation nation) {
        if (this.claimedNation == Nation.NATIONLESS) {
            return true;
        } else {
            return this.claimedNation == nation;
        }
    }

    public Chunk getChunk() {
        return chunk;
    }

    @FunctionalInterface
    public interface ClaimEvent {
        void onClaimChanged(ServerWorld world, NationClaimChunkComponent claim, Nation oldNation);
    }
}