package com.totallynotsuspicious.core.mixin;

import net.minecraft.world.PlayerSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(PlayerSaveHandler.class)
public interface PlayerSaveHandlerAccessor {
    @Accessor("playerDataDir")
    File getPlayerDataDir();
}