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
            BlockPos.ORIGIN
    );

    public static final NationData PANDORA = new NationData(
            Text.literal("Pandora").formatted(Formatting.GOLD, Formatting.BOLD),
            Formatting.GOLD,
            BlockPos.ORIGIN
    );

    public static final NationData TAURE_ARANIE = new NationData(
            Text.literal("Taure Aranië").formatted(Formatting.GREEN, Formatting.BOLD),
            Formatting.GREEN,
            BlockPos.ORIGIN
    );

    public static final NationData VAYUNE = new NationData(
            Text.literal("Vayune").formatted(Formatting.AQUA, Formatting.BOLD),
            Formatting.AQUA,
            BlockPos.ORIGIN
    );
}
