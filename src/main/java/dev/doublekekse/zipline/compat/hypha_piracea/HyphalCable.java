package dev.doublekekse.zipline.compat.hypha_piracea;

import dev.doublekekse.zipline.Cable;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import phanastrae.hyphapiracea.electromagnetism.WireLine;

public class HyphalCable implements Cable {
    private final WireLine wireLine;
    private final double length;

    public HyphalCable(WireLine wireLine) {
        this.wireLine = wireLine;
        this.length = wireLine.getStartToEnd().length();
    }

    @Override
    public double getProgress(Vec3 playerPos) {
        Vec3 playerToStart = playerPos.subtract(wireLine.getStart());
        double t = playerToStart.dot(direction()) / length; // Parametric position
        t = Mth.clamp(t, 0.0, 1.0);

        return t;
    }

    private double lerpedY(double lerpFactor) {
        float relY = (float) (wireLine.getEnd().y - wireLine.getStart().y);

        if (relY > 0) {
            return relY * lerpFactor * lerpFactor;
        }


        return relY - relY * (1.0F - lerpFactor) * (1.0F - lerpFactor);
    }

    @Override
    public Vec3 getPoint(double progress) {
        var y = lerpedY(progress) + wireLine.getStart().y;
        return wireLine.getStart().add(direction().scale(progress * length)).with(Direction.Axis.Y, y);
    }

    @Override
    public Vec3 getClosestPoint(Vec3 pos) {
        return getPoint(getProgress(pos));
    }

    @Override
    public Vec3 direction() {
        return wireLine.getIVec();
    }

    @Override
    public double length() {
        return length;
    }
}
