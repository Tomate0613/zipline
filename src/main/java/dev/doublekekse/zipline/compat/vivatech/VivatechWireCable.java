package dev.doublekekse.zipline.compat.vivatech;

import dev.doublekekse.zipline.Cable;
import falseresync.vivatech.common.power.Wire;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record VivatechWireCable(Vec3 from, Vec3 to, Vec3 delta, Vec3 direction, double length) implements Cable {
    public static VivatechWireCable from(Wire wire) {
        var fromPos = wire.start();
        var toPos = wire.end();

        var delta = toPos.subtract(fromPos);
        var direction = delta.normalize();
        var length = delta.length();

        return new VivatechWireCable(fromPos, toPos, delta, direction, length);
    }

    @Override
    public double getProgress(Vec3 playerPos) {
        Vec3 playerToStart = playerPos.subtract(from);
        double t = playerToStart.dot(direction) / length; // Parametric position
        t = Mth.clamp(t, 0.0, 1.0);

        return t;
    }

    @Override
    public Vec3 getPoint(double progress) {
        double y = getSaggedY(progress, progress * delta.y, length);
        double x = (progress * delta.x);
        double z = (progress * delta.z);

        return from.add(new Vec3(x, y, z));
    }

    private double getSaggedY(double progress, double dY, double length) {
        return dY + getSaggingCoefficient(length) * (4 * progress * (progress - 1));
    }

    private float getSaggingCoefficient(double length) {
        return length < 5 ? 0.3f : 0.4f;
    }

    @Override
    public Vec3 getClosestPoint(Vec3 pos) {
        var t = getProgress(pos);
        return getPoint(t);
    }

    @Override
    public Vec3 direction(double progress) {
        return direction;
    }

    @Override
    public double length() {
        return length;
    }
}
