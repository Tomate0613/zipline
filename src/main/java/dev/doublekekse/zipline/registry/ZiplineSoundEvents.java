package dev.doublekekse.zipline.registry;

import dev.doublekekse.zipline.Zipline;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class ZiplineSoundEvents {
    public static final SoundEvent ZIPLINE_ATTACH = register("zipline_attach");
    public static final SoundEvent ZIPLINE_INTERRUPT = register("zipline_interrupt");
    public static final SoundEvent ZIPLINE_USE = register("zipline_use");

    private static SoundEvent register(String path) {
        var id = Zipline.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void register() {

    }
}
