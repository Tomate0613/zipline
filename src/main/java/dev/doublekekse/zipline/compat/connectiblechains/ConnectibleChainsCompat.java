package dev.doublekekse.zipline.compat.connectiblechains;

import com.github.legoatoom.connectiblechains.ConnectibleChains;
import com.github.legoatoom.connectiblechains.entity.ChainKnotEntity;
import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.AABB;

public class ConnectibleChainsCompat {
    public static void register() {
        Cables.registerProvider((offsetPlayerPos, squaredRadius) -> {
            assert Minecraft.getInstance().level != null;

            var radius = ConnectibleChains.runtimeConfig.getMaxChainRange() + 1;
            var aabb = new AABB(offsetPlayerPos.subtract(radius, radius, radius), offsetPlayerPos.add(radius, radius, radius));

            var knots = Minecraft.getInstance().level.getEntitiesOfClass(ChainKnotEntity.class, aabb, (a) -> true);

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var knot : knots) {
                for (var chainLink : knot.getLinks()) {
                    var from = chainLink.getPrimary();
                    var to = chainLink.getSecondary();


                    var cable = ChainCable.from(from, to);
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
