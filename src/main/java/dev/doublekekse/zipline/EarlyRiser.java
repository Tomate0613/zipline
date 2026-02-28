package dev.doublekekse.zipline;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        addItemAnimation("ZIPLINE", true, true);
    }

    void addItemAnimation(String name, boolean twoHanded, boolean affectsOffhandPose) {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }

        String armPose = remapper.mapClassName("intermediary", "net.minecraft.class_572$class_573");
        ClassTinkerers.enumBuilder(armPose, boolean.class, boolean.class).addEnum(name, twoHanded, affectsOffhandPose).build();
    }
}
