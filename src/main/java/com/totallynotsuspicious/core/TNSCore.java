package com.totallynotsuspicious.core;

import com.totallynotsuspicious.core.nations.CustomEventHandlerRegistry;
import com.totallynotsuspicious.core.nations.NationsManager;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TNSCore implements ModInitializer {
	public static final String MOD_ID = "tns-core";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		NationsManager.initialize();
		CustomEventHandlerRegistry.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}