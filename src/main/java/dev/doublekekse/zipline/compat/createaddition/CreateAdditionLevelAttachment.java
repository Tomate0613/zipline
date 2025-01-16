package dev.doublekekse.zipline.compat.createaddition;

import dev.doublekekse.zipline.duck.LevelDuck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashSet;

public class CreateAdditionLevelAttachment {
    public HashSet<BlockPos> connectorPositions = new HashSet<>();

    public static CreateAdditionLevelAttachment getAttachment(Level level) {
        return ((LevelDuck) level).zipline$getCAAttachment();
    }
}
