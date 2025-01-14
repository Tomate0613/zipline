package dev.doublekekse.zipline.compat.createaddition;

import com.mrh0.createaddition.config.Config;
import dev.doublekekse.zipline.Cable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record CreateAdditionCable(Vec3 origin, Vec3 delta, Vec3 direction, double length) implements Cable {
    public static CreateAdditionCable from(BlockPos from, BlockPos to) {
        var fromPos = from.getCenter();
        var toPos = to.getCenter();

        if (fromPos.y > toPos.y) {
            var swap = toPos;
            toPos = fromPos;
            fromPos = swap;
        }

        var delta = toPos.subtract(fromPos);
        var direction = delta.normalize();
        var length = delta.length();

        return new CreateAdditionCable(fromPos, delta, direction, length);
    }

    @Override
    public double getProgress(Vec3 playerPos) {
        Vec3 playerToStart = playerPos.subtract(origin);
        double t = playerToStart.dot(direction) / length; // Parametric position
        t = Mth.clamp(t, 0.0, 1.0);

        return t;
    }


    private static double hang(double f, double dis) {
        return Math.sin((-f * Math.PI)) * (0.5F * dis / Config.SMALL_CONNECTOR_MAX_LENGTH.get());
    }

    @Override
    public Vec3 getPoint(double progress) {
        double y = (delta.y > 0.0F ? delta.y * progress * progress : delta.y - delta.y * (1.0F - progress) * (1.0F - progress)) + hang(progress, length);

        double x = (progress * delta.x);
        double z = (progress * delta.z);

        return origin.add(new Vec3(x, y, z));
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
