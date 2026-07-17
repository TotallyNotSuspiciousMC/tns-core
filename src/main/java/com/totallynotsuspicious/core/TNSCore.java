package com.totallynotsuspicious.core;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.totallynotsuspicious.core.entity.TNSCoreStatusEffects;
import com.totallynotsuspicious.core.entity.TNSLootModifiers;
import com.totallynotsuspicious.core.item.TNSCoreItems;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;


public class TNSCore implements ModInitializer {
    public static final String MOD_ID = "tns-core";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        if (!PolymerResourcePackUtils.addModAssets(MOD_ID)) {
            LOGGER.error("Unable to construct Polymer mod assets for {}", MOD_ID);
        }

        CommandRegistrationCallback.EVENT.register(this::registerAboutCommand);
        TNSLootModifiers.initialize();
        TNSCoreItems.initialize();
        TNSCoreStatusEffects.initialize();

        // removes the terralith snow modifications
        BiomeModifications.create(id("remove_smooth_snow"))
                .add(
                        ModificationPhase.REMOVALS,
                        BiomeSelectors.foundInOverworld(),
                        (biomeSelectionContext, biomeModificationContext) -> {
                            biomeModificationContext.getGenerationSettings().removeFeature(placedFeatureResourceKey("snowy/layer/first"));
                            biomeModificationContext.getGenerationSettings().removeFeature(placedFeatureResourceKey("snowy/layer/second"));
                            biomeModificationContext.getGenerationSettings().removeFeature(placedFeatureResourceKey("snowy/layer/third"));
                        }
                );
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    private static ResourceKey<PlacedFeature> placedFeatureResourceKey(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath("terralith", path));
    }

    private void registerAboutCommand(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext registryAccess,
            Commands.CommandSelection environment
    ) {
        LiteralArgumentBuilder<CommandSourceStack> tns = Commands.literal("tns")
                .then(Commands.literal("about").executes(ctx -> runAbout(ctx.getSource())));

        dispatcher.register(tns);
    }

    private static int runAbout(CommandSourceStack source) {
        final String src = "https://github.com/TotallyNotSuspiciousMC/tns-core";

        source.sendSuccess(
                () -> Component.literal(
                                "Copyright (C) 2025 TotallyNotSuspiciousMC. This server relies upon the TNS Core mod, which is free software licensed under AGPL-3.0-or-later. You may obtain a copy of the full license and corresponding source at: "
                        )
                        .append(Component.literal(src).setStyle(
                                Style.EMPTY
                                        .withUnderlined(true)
                                        .withColor(ChatFormatting.BLUE)
                                        .withClickEvent(new ClickEvent.OpenUrl(URI.create(src)))
                        )),
                false
        );
        return Command.SINGLE_SUCCESS;
    }
}