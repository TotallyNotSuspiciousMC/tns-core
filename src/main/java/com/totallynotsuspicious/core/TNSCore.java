package com.totallynotsuspicious.core;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.totallynotsuspicious.core.compat.TNSCorePlaceholders;
import com.totallynotsuspicious.core.compat.TNSCoreSquareMapMarkers;
import com.totallynotsuspicious.core.entity.TNSCoreStatusEffects;
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
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static net.minecraft.server.command.CommandManager.literal;

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
        CommandRegistrationCallback.EVENT.register(this::registerAboutCommand);
        TNSLootModifiers.initialize();
        if (FabricLoader.getInstance().isModLoaded("squaremap")) {
            ServerLifecycleEvents.SERVER_STARTED.register(new TNSCoreSquareMapMarkers());
        }
        PersonalityQuizService.initialize();
        TNSCoreItems.initialize();
        TNSCoreStatusEffects.initialize();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    private void registerAboutCommand(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment
    ) {
        LiteralArgumentBuilder<ServerCommandSource> tns = literal("tns")
                .then(literal("about")
                        .executes(ctx -> {
                            ctx.getSource().sendFeedback(
                                    () -> Text.literal(
                                                    "Copyright (C) 2025 TotallyNotSuspiciousMC. This server relies upon the TNS Core mod, which is free software licensed under AGPL-3.0-or-later. You may obtain a copy of the full license and corresponding source at: "
                                            )
                                            .append(Text.literal("https://github.com/TotallyNotSuspiciousMC/tns-core").setStyle(
                                                    Style.EMPTY
                                                            .withUnderline(true)
                                                            .withColor(Formatting.BLUE)
                                                            .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://github.com/TotallyNotSuspiciousMC/tns-core")))
                                            )),
                                    false
                            );

                            return Command.SINGLE_SUCCESS;
                        })
                );

        dispatcher.register(tns);
    }
}