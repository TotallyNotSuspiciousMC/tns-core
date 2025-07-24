package com.totallynotsuspicious.core.nations.claims;

import com.thedeathlycow.novoatlas.registry.NovoAtlasResourceKeys;
import com.thedeathlycow.novoatlas.world.gen.MapImage;
import com.thedeathlycow.novoatlas.world.gen.MapInfo;
import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.nations.Nation;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;

public final class ClaimsLookupV2 {
    public static final RegistryKey<MapImage> IMAGE_KEY = RegistryKey.of(NovoAtlasResourceKeys.BIOME_MAP, TNSCore.id("claim_map"));
    public static final RegistryKey<MapInfo> OVERWORLD_MAP_INFO = RegistryKey.of(NovoAtlasResourceKeys.MAP_INFO, TNSCore.id("overworld"));

    public static Nation getClaimedNation(World world, BlockPos pos) {
        if (world.getRegistryKey() != World.OVERWORLD) {
            return Nation.NATIONLESS;
        }

        MapImage image = MapInfo.lookupBiomeMap(IMAGE_KEY);

        int chunkX = ChunkSectionPos.getSectionCoord(pos.getX());
        int chunkZ = ChunkSectionPos.getSectionCoord(pos.getZ());

        MapInfo info = world.getRegistryManager()
                .getOrThrow(NovoAtlasResourceKeys.MAP_INFO)
                .getValueOrThrow(OVERWORLD_MAP_INFO);

        int color = image.sample(chunkX, chunkZ, info, Integer.MIN_VALUE);

        return Color2NationLookup.getNation(color);
    }

    public static class Color2NationLookup {
        public static final int FIDELIS_COLOR = 0xff0000;
        public static final int PANDORA_COLOR = 0xffff00;
        public static final int TAURE_ARANIE_COLOR = 0x00ff00;
        public static final int VAYUNE_COLOR = 0x0000ff;
        public static final int NO_MANS_LAND_COLOR = 0x000000;

        public static Nation getNation(int color) {
            return switch (color) {
                case FIDELIS_COLOR -> Nation.FIDELIS;
                case PANDORA_COLOR -> Nation.PANDORA;
                case TAURE_ARANIE_COLOR -> Nation.TAURE_ARANIE;
                case VAYUNE_COLOR -> Nation.VAYUNE;
                case NO_MANS_LAND_COLOR -> Nation.NO_MANS_LAND;
                default -> Nation.NATIONLESS;
            };
        }

        private Color2NationLookup() {
        }
    }

    private ClaimsLookupV2() {

    }
}