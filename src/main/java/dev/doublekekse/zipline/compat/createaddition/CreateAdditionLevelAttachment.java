package dev.doublekekse.zipline.compat.createaddition;

import com.mrh0.createaddition.blocks.connector.base.AbstractConnectorBlockEntity;
import dev.doublekekse.zipline.duck.LevelDuck;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CreateAdditionLevelAttachment {
    public List<AbstractConnectorBlockEntity> connectors = new ArrayList<>();

    public static CreateAdditionLevelAttachment getAttachment(Level level) {
        return ((LevelDuck) level).zipline$getCAAttachment();
    }
}
