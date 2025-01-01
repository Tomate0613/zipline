package dev.doublekekse.zipline.client;

import dev.doublekekse.zipline.compat.hypha_piracea.HyphaPiraceaCompat;
import dev.doublekekse.zipline.duck.GameRendererDuck;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class ZiplineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        compat();
    }

    public void compat() {
        if (FabricLoader.getInstance().isModLoaded("hyphapiracea")) {
            HyphaPiraceaCompat.register();
        }
    }

    public static void ziplineTilt(float yaw) {
        ((GameRendererDuck) Minecraft.getInstance().gameRenderer).zipline$setZiplineTilt(yaw);
    }
}
