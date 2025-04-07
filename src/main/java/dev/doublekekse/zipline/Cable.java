package dev.doublekekse.zipline;

import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Collections;

public interface Cable {
    double getProgress(Vec3 playerPos);

    Vec3 getPoint(double progress);

    Vec3 getClosestPoint(Vec3 pos);

    Vec3 direction(double progress);

    double length();

    default Collection<Cable> getNext(boolean forward) {
        return Collections.emptyList();
    }

    default boolean isValid() {
        return true;
    }
}
