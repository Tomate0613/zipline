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
    public boolean isValid() {
        return cable.getPoints().size() >= 2;
    }

    @Override
    public Vec3 getPoint(double progress) {
        assert progress >= 0 && progress <= 1;

        var points = cable.getPoints();
        var spline = CatmulRomSpline.generateSpline(points.stream().map(RopeNode::getPosition).toList(), SuperpositionConstants.cableSegments);

        return spline.get((int) (Math.clamp(progress, 0, 1) * (spline.size() - 1)));
    }

    @Override
    public Vec3 getClosestPoint(Vec3 pos) {
        var t = getProgress(pos);
        return getPoint(t);
    }

    @Override
    public Vec3 direction(double progress) {
        assert progress >= 0 && progress <= 1;

        var points = cable.getPoints();
        var index = (int) (Math.clamp(progress, 0, 1) * (points.size() - 1));

        if (index == 0) {
            index++;
        }

        return points.get(index).getPosition().subtract(points.get(index - 1).getPosition()).normalize();
    }

    @Override
    public double length() {
        return cable.calculateLength();
    }
}
