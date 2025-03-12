package dev.doublekekse.zipline.compat.superposition;

import dev.doublekekse.zipline.Cable;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.modogthedev.superposition.core.SuperpositionConstants;
import org.modogthedev.superposition.system.cable.rope_system.RopeNode;
import org.modogthedev.superposition.util.CatmulRomSpline;

public record SuperpositionCable(org.modogthedev.superposition.system.cable.Cable cable) implements Cable {
    @Override
    public double getProgress(Vec3 playerPos) {
        var points = cable.getPoints();
        var size = points.size();

        if (size == 0) return 0;

        var closestIndex = -1;
        var closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            var point = points.get(i);

            var dist = point.getPosition().distanceToSqr(playerPos);
            if (dist < closestDistance) {
                closestDistance = dist;
                closestIndex = i;
            }
        }

        double t = ((double) closestIndex) / (size - 1);
        t = Mth.clamp(t, 0.0, 1.0);

        return t;
    }

    @Override
    public Vec3 getPoint(double progress) {
        var points = cable.getPoints();
        var spline = CatmulRomSpline.generateSpline(points.stream().map(RopeNode::getPosition).toList(), SuperpositionConstants.cableSegments);

        return spline.get((int) (progress * (spline.size() - 1)));
    }

    @Override
    public Vec3 getClosestPoint(Vec3 pos) {
        var t = getProgress(pos);
        return getPoint(t);
    }

    private Vec3 delta() {
        var points = cable.getPoints();
        return points.getLast().getPosition().subtract(points.getFirst().getPosition());
    }

    @Override
    public Vec3 direction() {
        return delta().normalize();
    }

    @Override
    public double length() {
        return delta().length();
    }
}
