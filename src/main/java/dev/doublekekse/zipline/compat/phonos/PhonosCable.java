package dev.doublekekse.zipline.compat.phonos;

import dev.doublekekse.zipline.Cable;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record PhonosCable(Vec3 from, Vec3 to, Vec3 delta, Vec3 direction, double length) implements Cable {
    public static PhonosCable from(Vec3 from, Vec3 to) {
        var delta = to.subtract(from);
        var direction = delta.normalize();
        var length = delta.length();

        return new PhonosCable(from, to, delta, direction, length);
    }

    @Override
    public double getProgress(Vec3 playerPos) {
        Vec3 playerToStart = playerPos.subtract(from);
        double t = playerToStart.dot(direction) / length;
        t = Mth.clamp(t, 0.0, 1.0);

        return t;
    }

    @Override
    public Vec3 getPoint(double progress) {
        double x = (progress * delta.x);
        double z = (progress * delta.z);

        double dy = length * 0.15 * (0.25 - (Math.pow(progress - 0.5, 2)));
        double y = (progress * delta.y) - dy;


        return from.add(new Vec3(x, y, z));
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
