package dev.doublekekse.zipline.compat.vivatech;

import dev.doublekekse.zipline.Cable;
import falseresync.vivatech.client.wire.WireParameters;
import falseresync.vivatech.client.wire.WireRenderingRegistry;
import falseresync.vivatech.common.power.wire.Wire;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record VivatechWireCable(Vec3 from, Vec3 to, Vec3 delta, Vec3 direction, double length,
                                WireParameters parameters) implements Cable {
    public static VivatechWireCable from(Wire wire) {
        var fromPos = new Vec3(wire.start());
        var toPos = new Vec3(wire.end());

        var delta = toPos.subtract(fromPos);
        var direction = delta.normalize();
        var length = delta.length();

        var parameters = WireRenderingRegistry.getAndBuild(wire);

        return new VivatechWireCable(fromPos, toPos, delta, direction, length, parameters);
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
        double x = from.x + (progress * delta.x);
        double z = from.z + (progress * delta.z);

        double y = parameters.getSaggedYForX((float) (from.y + (progress * delta.y)), (float) (progress * length));

        return new Vec3(x, y, z);
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
