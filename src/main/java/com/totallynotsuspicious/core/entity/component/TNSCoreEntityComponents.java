package com.totallynotsuspicious.core.entity.component;

import com.totallynotsuspicious.core.TNSCore;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public final class TNSCoreEntityComponents implements EntityComponentInitializer {
    static final ComponentKey<PlayerNationComponent> PLAYER_NATION = ComponentRegistry.getOrCreate(
            TNSCore.id("player_nation"),
            PlayerNationComponent.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PLAYER_NATION, PlayerNationComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}