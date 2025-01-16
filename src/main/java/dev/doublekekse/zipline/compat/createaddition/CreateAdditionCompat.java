package dev.doublekekse.zipline.compat.createaddition;

import com.mrh0.createaddition.blocks.connector.base.AbstractConnectorBlockEntity;
import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import net.minecraft.client.Minecraft;

public class CreateAdditionCompat {

    public static void register() {
        Cables.registerProvider(((offsetPlayerPos, squaredRadius) -> {
            Cable nearestCable = null;
            double nearestDist = squaredRadius;

            var level = Minecraft.getInstance().level;
            assert level != null;

            var connectorPositions = CreateAdditionLevelAttachment.getAttachment(level).connectorPositions;
            var iterator = connectorPositions.iterator();

            while (iterator.hasNext()) {
                var blockPos = iterator.next();
                var be = level.getBlockEntity(blockPos);

                if (!(be instanceof AbstractConnectorBlockEntity connector)) {
                    iterator.remove();
                    continue;
                }

                for (int i = 0; i < connector.getAvailableNode(); i++) {
                    var from = connector.getPos();
                    var to = connector.getNodePos(i);

                    assert to != null;

                    var cable = CreateAdditionCable.from(from, to);
                    var closestPoint = cable.getClosestPoint(offsetPlayerPos);

                    var distance = closestPoint.distanceToSqr(offsetPlayerPos);

                    if (distance < nearestDist) {
                        nearestDist = distance;
                        nearestCable = cable;
                    }
                }
            }

            return nearestCable;
        }));
    }
}
