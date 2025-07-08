package com.totallynotsuspicious.core.world;

import com.totallynotsuspicious.core.nations.Nation;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.chunk.Chunk;
import org.ladysnake.cca.api.v3.component.Component;

public class NationClaimChunkComponent implements Component {
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

    public boolean claim(Nation nation) {
        if (this.claimedNation == Nation.NATIONLESS) {
            this.claimedNation = nation;
            return true;
        }

        return false;
    }

    public void forceClaim(Nation nation) {
        this.claimedNation = nation;
    }

    public Nation getClaimedNation() {
        return claimedNation;
    }

    public boolean canModify(Nation nation) {
        if (this.claimedNation == Nation.NATIONLESS) {
            return true;
        } else {
            return this.claimedNation == nation;
        }
    }
}