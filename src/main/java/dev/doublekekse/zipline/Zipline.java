package dev.doublekekse.zipline;

import dev.doublekekse.zipline.registry.ZiplineCreativeTabs;
import dev.doublekekse.zipline.registry.ZiplineItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class Zipline implements ModInitializer {
    @Override
    public void onInitialize() {
        ZiplineItems.register();
        ZiplineCreativeTabs.register();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("zipline", path);
    }
}
