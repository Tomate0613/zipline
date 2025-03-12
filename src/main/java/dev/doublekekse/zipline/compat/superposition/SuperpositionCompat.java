package dev.doublekekse.zipline.compat.superposition;

import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import net.minecraft.client.Minecraft;
import org.modogthedev.superposition.system.cable.CableManager;

public class SuperpositionCompat {
    public static void register() {
        Cables.registerProvider((offsetPlayerPos, squaredRadius) -> {
            assert Minecraft.getInstance().level != null;

            var cables = CableManager.getCables(Minecraft.getInstance().level);

            if (cables == null) {
                return null;
            }

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var superCable : cables.values()) {
                var cable = new SuperpositionCable(superCable);
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
