package dev.doublekekse.zipline.client;

import dev.doublekekse.zipline.compat.connectiblechains.ConnectibleChainsCompat;
import dev.doublekekse.zipline.compat.hypha_piracea.HyphaPiraceaCompat;
import dev.doublekekse.zipline.compat.phonos.PhonosCompat;
import dev.doublekekse.zipline.compat.superposition.SuperpositionCompat;
import dev.doublekekse.zipline.compat.vivatech.VivatechCompat;
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
            var players = level.players();
            if (players.isEmpty()) {
                return;
            }

            var cable = Cables.getClosestCable(players.getFirst().position(), 10);

            if (cable == null) {
                return;
            }

            for (double i = 0; i < 1; i += 0.01) {
                var d = cable.getPoint(i);

                level.addParticle(ParticleTypes.CRIT,
                    d.x, d.y, d.z,
                    cable.direction(i).x * 0.1, cable.direction(i).y * 0.1, cable.direction(i).z * 0.1
                );
            }
        });
         */
    }

    public void compat() {
        var loader = FabricLoader.getInstance();
        if (loader.isModLoaded("hyphapiracea")) {
            HyphaPiraceaCompat.register();
        }

        if (loader.isModLoaded("connectiblechains")) {
            ConnectibleChainsCompat.register();
        }

        if (loader.isModLoaded("vivatech")) {
            VivatechCompat.register();
        }

        if (loader.isModLoaded("superposition")) {
            SuperpositionCompat.register();
        }

        if (loader.isModLoaded("phonos")) {
            PhonosCompat.register();
        }
    }

    public static void ziplineTilt(float yaw) {
        ((GameRendererDuck) Minecraft.getInstance().gameRenderer).zipline$setZiplineTilt(yaw);
    }
}
