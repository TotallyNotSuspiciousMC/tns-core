package com.totallynotsuspicious.core.compat;

import com.totallynotsuspicious.core.nations.Nation;
import com.totallynotsuspicious.core.world.NationClaimChunkComponent;
import net.minecraft.server.world.ServerWorld;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public final class TNSCoreDynmapListener extends DynmapCommonAPIListener {
    private static final TNSCoreDynmapListener INSTANCE = new TNSCoreDynmapListener();

    private DynmapCommonAPI commonAPI = null;
    private MarkerAPI markerAPI = null;
    private MarkerSet markerSet = null;

    public static void initialize() {
        DynmapCommonAPIListener.register(INSTANCE);
        NationClaimChunkComponent.ON_CLAIMED.register(INSTANCE::onChunkClaimed);
        NationClaimChunkComponent.ON_UNCLAIMED.register(INSTANCE::onChunkUnClaimed);
    }

    private TNSCoreDynmapListener() {

    }

    @Override
    public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
        this.commonAPI = dynmapCommonAPI;
        this.markerAPI = commonAPI.getMarkerAPI();
        this.markerSet = markerAPI.createMarkerSet("tnscore_claims", "Nation Claims", null, true);
    }

    private void onChunkClaimed(ServerWorld world, NationClaimChunkComponent claim, Nation oldNation) {
        if (markerSet == null) {
            return;
        }
    }

    private void onChunkUnClaimed(ServerWorld world, NationClaimChunkComponent claim, Nation oldNation) {

    }
}