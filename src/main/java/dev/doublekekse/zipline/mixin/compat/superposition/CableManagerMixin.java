package dev.doublekekse.zipline.mixin.compat.superposition;

import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.modogthedev.superposition.system.cable.CableManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CableManager.class)
public class CableManagerMixin {
    @Inject(method = "playerEmptyClickEvent", at = @At("HEAD"), cancellable = true)
    private static void playerEmptyClickEvent(Player player, Level level, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.isHolding(ZiplineItems.ZIPLINE)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
