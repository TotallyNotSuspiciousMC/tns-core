package com.totallynotsuspicious.core.nations;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;

public enum Nation implements StringIdentifiable {

    NATIONLESS("nationless", NationData.NATIONLESS),
    FIDELIS("fidelis", NationData.FIDELIS),
    PANDORA("pandora", NationData.PANDORA),
    TAURE_ARANOE("taure_aranie", NationData.TAURE_ARANIE),
    VAYUNE("vayune", NationData.VAYUNE);

    public static final EnumCodec<Nation> CODEC = StringIdentifiable.createCodec(Nation::values);

    private final String name;

    private final NationData data;

    Nation(String name, NationData data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Text getTitle() {
        return this.data.title();
    }

    public BlockPos getHome() {
        return this.data.home();
    }

    public NationData getData() {
        return data;
    }

    public Text getJoinMessage() {
        if (this == NATIONLESS) {
            return Text.translatable("tnscore.nations.join.accepted.nationless")
                    .formatted(Formatting.GRAY);
        } else {
            return Text.translatable("tnscore.nations.join.accepted.normal", this.getTitle())
                    .formatted(Formatting.GRAY);
        }
    }
}