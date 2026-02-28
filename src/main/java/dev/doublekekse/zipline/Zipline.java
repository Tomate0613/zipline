package dev.doublekekse.zipline;

import dev.doublekekse.zipline.registry.ZiplineCreativeTabs;
import dev.doublekekse.zipline.registry.ZiplineItems;
import dev.doublekekse.zipline.registry.ZiplineSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class Zipline implements ModInitializer {
    @Override
    public void onInitialize() {
        ZiplineItems.register();
        ZiplineCreativeTabs.register();
        ZiplineSoundEvents.register();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("zipline", path);
    }
}
