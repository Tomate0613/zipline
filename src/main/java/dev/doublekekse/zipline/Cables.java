package dev.doublekekse.zipline;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Cables {
    private static final List<CableProvider> providers = new ArrayList<>();

    public static @Nullable Cable getClosestCable(Vec3 offsetPlayerPos, double radius) {

        double nearestDist = radius * radius;
        Cable nearestCable = null;

        for (var provider : providers) {
            var cable = provider.getNearestCable(offsetPlayerPos, nearestDist);

            if (cable == null) {
                continue;
            }

            var closestPoint = cable.getClosestPoint(offsetPlayerPos);
            var distance = closestPoint.distanceToSqr(offsetPlayerPos);

            if (distance < nearestDist) {
                nearestCable = cable;
                nearestDist = distance;
            }
        }

        return nearestCable;
    }

    @FunctionalInterface
    public interface CableProvider {
        Cable getNearestCable(Vec3 offsetPlayerPos, double squaredRadius);
    }

    public static void registerProvider(CableProvider provider) {
        providers.add(provider);
    }
}
