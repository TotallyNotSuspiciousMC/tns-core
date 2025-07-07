package com.totallynotsuspicious.core.codec;

import com.mojang.serialization.Codec;

import java.time.Instant;

public final class TNSCodecs {
    public static final Codec<Instant> INSTANT_CODEC = Codec.LONG.xmap(Instant::ofEpochSecond, Instant::getEpochSecond);

    private TNSCodecs() {

    }
}