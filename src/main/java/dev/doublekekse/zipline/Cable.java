package dev.doublekekse.zipline;

import net.minecraft.world.phys.Vec3;

public interface Cable {
    double getProgress(Vec3 playerPos);

    Vec3 getPoint(double progress);

    Vec3 getClosestPoint(Vec3 pos);

    Vec3 direction();

    double length();
}
