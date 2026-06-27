package com.totallynotsuspicious.core.world;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public final class EchoShardDropper {
    public static void tryDrop(ServerLevel serverWorld, BlockPos sensorPos) {
        Vec3 pos = Vec3.atCenterOf(sensorPos);

        ItemEntity echoShard = new ItemEntity(
                serverWorld,
                pos.x,
                pos.y,
                pos.z,
                Items.ECHO_SHARD.getDefaultInstance()
        );

        serverWorld.addFreshEntity(echoShard);
    }

    private EchoShardDropper() {

    }
}