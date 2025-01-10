package dev.doublekekse.zipline.client;

import dev.doublekekse.zipline.compat.connectiblechains.ConnectibleChainsCompat;
import dev.doublekekse.zipline.duck.GameRendererDuck;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class ZiplineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        compat();

        /*
        ClientTickEvents.START_WORLD_TICK.register((level) -> {
            var cable = Cables.getClosestCable(level.players().get(0).position(), 10);

            if (cable == null) {
                return;
            }

            for (double i = 0; i < 1; i += 0.01) {
                var d = cable.getPoint(i);

                level.addParticle(ParticleTypes.CRIT,
                    d.x, d.y, d.z,
                    cable.direction().x * 0.1, cable.direction().y * 0.1, cable.direction().z * 0.1
                );
            }
        });
         */
    }

    public void compat() {
        if (FabricLoader.getInstance().isModLoaded("connectiblechains")) {
            ConnectibleChainsCompat.register();
        }
    }

    public static void ziplineTilt(float yaw) {
        ((GameRendererDuck) Minecraft.getInstance().gameRenderer).zipline$setZiplineTilt(yaw);
    }
}
