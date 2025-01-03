package dev.doublekekse.zipline.compat.connectiblechains;

import com.github.legoatoom.connectiblechains.util.Helper;
import dev.doublekekse.zipline.Cable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record ChainCable(Vec3 from, Vec3 to, Vec3 delta, Vec3 direction, double length) implements Cable {
    public static ChainCable from(Entity from, Entity to) {
        var fromPos = from.position();
        var toPos = to.position();

        if (fromPos.y > toPos.y) {
            var swap = toPos;
            toPos = fromPos;
            fromPos = swap;
        }

        var delta = toPos.subtract(fromPos);
        var direction = delta.normalize();
        var length = delta.length();

        return new ChainCable(fromPos, toPos, delta, direction, length);
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
        double distanceXZ = (float) Math.sqrt(Math.fma(delta.x(), delta.x(), delta.z() * delta.z()));
        double wrongDistanceFactor = length / distanceXZ;
        double a = (progress * distanceXZ);
        double y = Helper.drip2(a * wrongDistanceFactor, length, delta.y()) + .4f;

        double x = (progress * delta.x);
        double z = (progress * delta.z);

        return from.add(new Vec3(x, y, z));
    }

    @Override
    public Vec3 getClosestPoint(Vec3 pos) {
        var t = getProgress(pos);
        return getPoint(t);
    }

    @Override
    public Vec3 direction() {
        return direction;
    }

    @Override
    public double length() {
        return length;
    }
}
