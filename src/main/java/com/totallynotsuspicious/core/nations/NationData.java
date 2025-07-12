package com.totallynotsuspicious.core.nations;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public record NationData(
        Text title,
        Text description,
        Formatting color,
        BlockPos home
) {
    public static final NationData NATIONLESS = new NationData(
            Text.literal("Nationless").formatted(Formatting.GRAY, Formatting.ITALIC),
            Text.translatable("tnscore.nation.nationless.desc"),
            Formatting.GRAY,
            BlockPos.ORIGIN
    );

    public static final NationData FIDELIS = new NationData(
            Text.literal("Fidelis").formatted(Formatting.RED, Formatting.BOLD),
            Text.translatable("tnscore.nation.fidelis.desc"),
            Formatting.RED,
            new BlockPos(-1964, 97, 213)
    );

    public static final NationData PANDORA = new NationData(
            Text.literal("Pandora").formatted(Formatting.YELLOW, Formatting.BOLD),
            Text.translatable("tnscore.nation.pandora.desc"),
            Formatting.GOLD,
            new BlockPos(1207, 75, -1751)
    );

    public static final NationData TAURE_ARANIE = new NationData(
            Text.literal("Taure Aranië").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
            Text.translatable("tnscore.nation.taureAranie.desc"),
            Formatting.GREEN,
            new BlockPos(2113, 120, 69)
    );

    public static final NationData VAYUNE = new NationData(
            Text.literal("Vayune").formatted(Formatting.AQUA, Formatting.BOLD),
            Text.translatable("tnscore.nation.vayune.desc"),
            Formatting.AQUA,
            new BlockPos(2198, 96, 830)
    );
}
