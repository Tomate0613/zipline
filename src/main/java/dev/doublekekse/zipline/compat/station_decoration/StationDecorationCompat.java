package dev.doublekekse.zipline.compat.station_decoration;

import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import dev.doublekekse.zipline.StraightCable;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.mtr.core.data.Position;
import top.mcmtr.mod.client.MSDMinecraftClientData;

public class StationDecorationCompat {
    public static void register() {
        Cables.registerProvider((offsetPlayerPos, squaredRadius) -> {
            assert Minecraft.getInstance().level != null;

            var data = MSDMinecraftClientData.getInstance();
            var catenaries = data.catenaries;

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var catenary : catenaries) {
                var cable = StationDecorationCable.of(data, catenary.getPosition1(), catenary.getPosition2());
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

    static Vec3 toVec3(Position position) {
        return new Vec3(position.getX() + .5, position.getY(), position.getZ() + .5);
    }
}
