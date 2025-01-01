package dev.doublekekse.zipline.compat.hypha_piracea;

import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import net.minecraft.client.Minecraft;
import phanastrae.hyphapiracea.world.HyphaPiraceaLevelAttachment;

public class HyphaPiraceaCompat {
    public static void register() {
        Cables.registerProvider((offsetPlayerPos, squaredRadius) -> {
            assert Minecraft.getInstance().level != null;

            var levelAttachment = HyphaPiraceaLevelAttachment.getAttachment(Minecraft.getInstance().level);

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var wireBlockEntity : levelAttachment.getWireBlockEntitiesWithWires()) {
                var wire = wireBlockEntity.getWireLine();
                var cable = new HyphalCable(wire);
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
