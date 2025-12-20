package com.totallynotsuspicious.core.world;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class EchoShardDropper {
    public static void tryDrop(ServerWorld serverWorld, BlockPos sensorPos) {
        Vec3d pos = Vec3d.ofCenter(sensorPos);

        ItemEntity echoShard = new ItemEntity(
                serverWorld,
                pos.x,
                pos.y,
                pos.z,
                Items.ECHO_SHARD.getDefaultStack()
        );

        serverWorld.spawnEntity(echoShard);
    }

    private EchoShardDropper() {

    }
}