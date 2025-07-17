package com.totallynotsuspicious.core;

import com.totallynotsuspicious.core.compat.TNSCorePlaceholders;
import com.totallynotsuspicious.core.compat.TNSCoreSquareMapMarkers;
import com.totallynotsuspicious.core.entity.TNSLootModifiers;
import com.totallynotsuspicious.core.item.TNSCoreItems;
import com.totallynotsuspicious.core.nations.CustomEventHandlerRegistry;
import com.totallynotsuspicious.core.nations.NationCommand;
import com.totallynotsuspicious.core.nations.NationsManager;
import com.totallynotsuspicious.core.nations.quiz.PersonalityQuizService;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TNSCore implements ModInitializer {
    public static final String MOD_ID = "tns-core";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        if (!PolymerResourcePackUtils.addModAssets(MOD_ID)) {
            LOGGER.error("Unable to construct Polymer mod assets for {}", MOD_ID);
        }

        NationsManager.initialize();
        CustomEventHandlerRegistry.initialize();
        TNSCorePlaceholders.initialize();
        CommandRegistrationCallback.EVENT.register(NationCommand::registerCommand);
        TNSLootModifiers.initialize();
        if (FabricLoader.getInstance().isModLoaded("squaremap")) {
            ServerLifecycleEvents.SERVER_STARTED.register(new TNSCoreSquareMapMarkers());
        }
        PersonalityQuizService.initialize();
        TNSCoreItems.initialize();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}