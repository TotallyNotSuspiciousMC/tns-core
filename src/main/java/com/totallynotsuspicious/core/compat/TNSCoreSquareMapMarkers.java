package com.totallynotsuspicious.core.compat;

import com.thedeathlycow.novoatlas.registry.NovoAtlasResourceKeys;
import com.thedeathlycow.novoatlas.world.gen.MapImage;
import com.thedeathlycow.novoatlas.world.gen.MapInfo;
import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.nations.Nation;
import com.totallynotsuspicious.core.nations.claims.ClaimsLookupV2;
import io.netty.util.concurrent.CompleteFuture;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.Rectangle;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TNSCoreSquareMapMarkers implements ServerLifecycleEvents.ServerStarted {
    private static final Logger LOGGER = LoggerFactory.getLogger(TNSCoreSquareMapMarkers.class);
    private static final Map<Nation, MarkerOptions> NATION_MARKER_OPTIONS = Util.make(
            new EnumMap<>(Nation.class),
            map -> {
                map.put(
                        Nation.FIDELIS,
                        MarkerOptions.builder()
                                .fillColor(new Color(ClaimsLookupV2.Color2NationLookup.FIDELIS_COLOR))
                                .stroke(false)
                                .build()
                );

                map.put(
                        Nation.PANDORA,
                        MarkerOptions.builder()
                                .fillColor(new Color(ClaimsLookupV2.Color2NationLookup.PANDORA_COLOR))
                                .stroke(false)
                                .build()
                );

                map.put(
                        Nation.TAURE_ARANOE,
                        MarkerOptions.builder()
                                .fillColor(new Color(ClaimsLookupV2.Color2NationLookup.TAURE_ARANIE_COLOR))
                                .stroke(false)
                                .build()
                );

                map.put(
                        Nation.VAYUNE,
                        MarkerOptions.builder()
                                .fillColor(new Color(ClaimsLookupV2.Color2NationLookup.VAYUNE_COLOR))
                                .stroke(false)
                                .build()
                );
            }
    );

    @Override
    public void onServerStarted(MinecraftServer server) {
        Squaremap api = SquaremapProvider.get();
        WorldIdentifier overworldID = WorldIdentifier.create(World.OVERWORLD.getValue().getNamespace(), World.OVERWORLD.getValue().getPath());


        ServerWorld gameWorld = server.getOverworld();
        final double worldSize = gameWorld.getWorldBorder().getSize();

        api.getWorldIfEnabled(overworldID).ifPresent(mapWorld -> {
            Key key = Key.of("tnscore_nations_claims");
            SimpleLayerProvider provider = SimpleLayerProvider.builder("Nation Claims")
                    .showControls(true)
                    .defaultHidden(true)
                    .build();

            mapWorld.layerRegistry().register(key, provider);
            TNSCore.LOGGER.info("Registered squaremap layer");

            MapImage image = MapInfo.lookupBiomeMap(ClaimsLookupV2.IMAGE_KEY);
            MapInfo info = gameWorld.getRegistryManager()
                    .getOrThrow(NovoAtlasResourceKeys.MAP_INFO)
                    .getValueOrThrow(ClaimsLookupV2.OVERWORLD_MAP_INFO);

            CompletableFuture.supplyAsync(() -> this.updateMarkers(provider, image, info, worldSize));
        });
    }

    private CompletableFuture<Void> updateMarkers(
            SimpleLayerProvider provider,
            MapImage image,
            MapInfo info,
            double worldSize
    ) {
        LOGGER.info("Updating map markers...");

        provider.clearMarkers();
        int worldRadius = ChunkSectionPos.getSectionCoord(worldSize) / 2;

        for (int x = -worldRadius; x < worldRadius; x++) {
            for (int z = -worldRadius; z < worldRadius; z++) {
                int claimedNationColor = image.sample(x, z, info, Integer.MIN_VALUE);
                Nation nation = ClaimsLookupV2.Color2NationLookup.getNation(claimedNationColor);
                MarkerOptions options = NATION_MARKER_OPTIONS.get(nation);

                if (nation.isNotNationless() && options != null) {
                    Key key = Key.of(String.format("tnscore_claim_%s_%d_%d", nation.name(), x, z));

                    int blockX = ChunkSectionPos.getBlockCoord(x);
                    int blockZ = ChunkSectionPos.getBlockCoord(z);

                    Point p1 = Point.of(blockX, blockZ);
                    Point p2 = Point.of(blockX + 16.0, blockZ + 16.0);

                    Rectangle marker = Marker.rectangle(p1, p2);

                    marker.markerOptions(options);

                    provider.addMarker(key, marker);
                }
            }
        }

        LOGGER.info("Updated map markers");
        return CompletableFuture.completedFuture(null);
    }
}