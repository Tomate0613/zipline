package dev.doublekekse.zipline.mixin.compat.connectiblechains;

import com.github.legoatoom.connectiblechains.entity.ChainCollisionEntity;
import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChainCollisionEntity.class)
public class ChainCollisionEntityMixin {
    @Inject(method = "canBeCollidedWith", at = @At("HEAD"), cancellable = true)
    void canBeCollidedWith(CallbackInfoReturnable<Boolean> cir) {
        assert Minecraft.getInstance().player != null;

        var mainHand = Minecraft.getInstance().player.getMainHandItem();
        var offHand = Minecraft.getInstance().player.getOffhandItem();

        if (mainHand.is(ZiplineItems.ZIPLINE) || offHand.is(ZiplineItems.ZIPLINE)) {
            cir.setReturnValue(false);
        }
    }
}
