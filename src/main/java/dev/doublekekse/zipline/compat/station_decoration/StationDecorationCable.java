package dev.doublekekse.zipline.compat.station_decoration;

import dev.doublekekse.zipline.Cable;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.mtr.core.data.Position;
import top.mcmtr.mod.client.MSDMinecraftClientData;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public record StationDecorationCable(
    MSDMinecraftClientData data,
    Position startPos,
    Position endPos,
    Vec3 start,
    Vec3 end,
    Vec3 direction,
    double length
) implements Cable {
    public static StationDecorationCable of(MSDMinecraftClientData data, Position startPos, Position endPos) {
        var start = toVec3(startPos);
        var end = toVec3(endPos);

        var direction = end.subtract(start).normalize();
        var length = start.distanceTo(end);

        return new StationDecorationCable(data, startPos, endPos, start, end, direction, length);
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

    @Override
    public Collection<Cable> getNext(boolean forward) {
        if (forward) {
            var nextEnd = data.positionsToCatenary.get(endPos);
            return nextEnd.keySet().stream().map(newStartPos -> of(data, endPos, newStartPos)).collect(Collectors.toSet());
        } else {
            var nextStart = data.positionsToCatenary.get(startPos);
            return nextStart.keySet().stream().map(newEndPos -> of(data, startPos, newEndPos)).collect(Collectors.toSet());
        }
    }

    static Vec3 toVec3(Position position) {
        return new Vec3(position.getX() + .5, position.getY(), position.getZ() + .5);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StationDecorationCable that = (StationDecorationCable) o;
        return (Objects.equals(endPos, that.endPos) && Objects.equals(startPos, that.startPos)) || (Objects.equals(endPos, that.startPos) && Objects.equals(startPos, that.endPos));
    }

    @Override
    public int hashCode() {
        return startPos.hashCode() < endPos.hashCode() ? Objects.hash(startPos, endPos) : Objects.hash(endPos, startPos);
    }
}
