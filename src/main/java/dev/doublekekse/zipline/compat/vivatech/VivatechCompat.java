package dev.doublekekse.zipline.compat.vivatech;

import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import falseresync.vivatech.client.VivatechClient;
import net.minecraft.client.Minecraft;

public class VivatechCompat {
    public static void register() {
        Cables.registerProvider((offsetPlayerPos, squaredRadius) -> {
            assert Minecraft.getInstance().level != null;

            var wires = VivatechClient.getClientWireManager().getWires();

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var wire : wires) {
                var cable = VivatechWireCable.from(wire);
                var closestPoint = cable.getClosestPoint(offsetPlayerPos);

                var distance = closestPoint.distanceToSqr(offsetPlayerPos);

                if (distance < nearestDist) {
                    nearestDist = distance;
                    nearestCable = cable;
                }

            }

            return nearestCable;
        });
    }
}
