package dev.doublekekse.zipline.compat.phonos;

import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import dev.doublekekse.zipline.mixin.compat.phonos.BlockEntityOutputsAccessor;
import io.github.foundationgames.phonos.world.sound.block.OutputBlockEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class PhonosCompat {
    private static final Map<BlockPos, BlockEntityOutputsAccessor> map = new HashMap<>();

    public static void register() {
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((be, world) -> {
            if (be instanceof OutputBlockEntity obe) {
                map.put(be.getBlockPos(), (BlockEntityOutputsAccessor) obe.getOutputs());
            }
        });

        ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((be, world) -> {
            if (be instanceof OutputBlockEntity) {
                map.remove(be.getBlockPos());
            }
        });

        Cables.registerProvider((offsetPlayerPos, squaredRadius) -> {
            var level = Minecraft.getInstance().level;
            assert level != null;

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var outputs : map.values()) {
                for (var connection : outputs.getConnections()) {
                    if (connection == null) {
                        continue;
                    }

                    var start = connection.start.calculatePos(level, 0);
                    var end = connection.end.calculatePos(level, 0);

                    var cable = PhonosCable.from(start, end);
                    var closestPoint = cable.getClosestPoint(offsetPlayerPos);

                    var distance = closestPoint.distanceToSqr(offsetPlayerPos);

                    if (distance < nearestDist) {
                        nearestDist = distance;
                        nearestCable = cable;
                    }
                }
            }

            return nearestCable;
        });
    }
}
