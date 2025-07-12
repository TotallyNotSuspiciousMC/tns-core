package com.totallynotsuspicious.core.nations;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public record NationData(
        Text title,
        Formatting color,
        BlockPos home
) {
    public static final NationData NATIONLESS = new NationData(
            Text.literal("Nationless").formatted(Formatting.DARK_GRAY, Formatting.ITALIC),
            Formatting.GRAY,
            BlockPos.ORIGIN
    );

    public static final NationData FIDELIS = new NationData(
            Text.literal("Fidelis").formatted(Formatting.RED, Formatting.BOLD),
            Formatting.RED,
            new BlockPos(-1964, 97, 213)
    );

    public static final NationData PANDORA = new NationData(
            Text.literal("Pandora").formatted(Formatting.GOLD, Formatting.BOLD),
            Formatting.GOLD,
            new BlockPos(1207, 75, -1751)
    );

    public static final NationData TAURE_ARANIE = new NationData(
            Text.literal("Taure Aranië").formatted(Formatting.GREEN, Formatting.BOLD),
            Formatting.GREEN,
            new BlockPos(2113, 120, 69)
    );

    public static final NationData VAYUNE = new NationData(
            Text.literal("Vayune").formatted(Formatting.AQUA, Formatting.BOLD),
            Formatting.AQUA,
            new BlockPos(2198, 96, 830)
    );
}
