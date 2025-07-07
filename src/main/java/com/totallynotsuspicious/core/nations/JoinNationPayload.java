package com.totallynotsuspicious.core.nations;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;

public record JoinNationPayload(
        Nation nation,
        boolean confirmed
) {
    public static final Codec<JoinNationPayload> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Nation.CODEC
                            .fieldOf("nation")
                            .forGetter(JoinNationPayload::nation),
                    Codec.BOOL
                            .optionalFieldOf("confirmed", false)
                            .forGetter(JoinNationPayload::confirmed)
            ).apply(instance, JoinNationPayload::new)
    );

    public static JoinNationPayload decode(NbtElement nbt) {
        return CODEC.decode(NbtOps.INSTANCE, nbt)
                .mapOrElse(Pair::getFirst, error -> new JoinNationPayload(Nation.NATIONLESS, false));
    }

}
