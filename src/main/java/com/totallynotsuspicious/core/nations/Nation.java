package com.totallynotsuspicious.core.nations;

import com.mojang.serialization.Codec;
import net.minecraft.text.KeybindTextContent;
import net.minecraft.text.KeybindTranslations;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

public enum Nation implements StringIdentifiable {
    FIDELIS("fidelis", Text.literal("Fidelis").formatted(Formatting.RED, Formatting.BOLD)),
    PANDORA("pandora", Text.literal("Pandora").formatted(Formatting.GOLD, Formatting.BOLD)),
    TAURE_ARANOE("taure_aranie", Text.literal("Taure Aranië").formatted(Formatting.GREEN, Formatting.BOLD)),
    VAYUNE("vayune", Text.literal("Vayune").formatted(Formatting.AQUA, Formatting.BOLD)),
    NATIONLESS("nationless", Text.literal("Nationless").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));

    public static final Codec<Nation> CODEC = StringIdentifiable.createCodec(Nation::values);

    private final String name;

    private final Text title;

    Nation(String name, Text title) {
        this.name = name;
        this.title = title;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Text getTitle() {
        return title;
    }

    public Text getJoinMessage() {
        if (this == NATIONLESS) {
            return Text.literal("You are now ")
                    .formatted(Formatting.GRAY)
                    .append(this.getTitle())
                    .append(Text.literal("!"));
        } else {
            return Text.literal("You have joined ")
                    .formatted(Formatting.GRAY)
                    .append(this.getTitle())
                    .append(Text.literal("!"));
        }
    }
}