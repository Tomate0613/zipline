package dev.doublekekse.zipline;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record StraightCable(
    Vec3 start,
    Vec3 end,
    Vec3 direction,
    double length
) implements Cable {
    public StraightCable(Vec3 start, Vec3 end) {
        this(start, end, end.subtract(start).normalize(), start.distanceTo(end));
    }

    public double getProgress(Vec3 playerPos) {
        Vec3 playerToStart = playerPos.subtract(start);
        double t = playerToStart.dot(direction) / length; // Parametric position
        t = Mth.clamp(t, 0.0, 1.0);

        return t;
    }

    public Vec3 getPoint(double progress) {
        return start.add(direction.scale(progress * length));
    }

    public Vec3 getClosestPoint(Vec3 pos) {
        var t = getProgress(pos);
        return getPoint(t);
    }
}
