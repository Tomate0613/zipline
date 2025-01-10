package dev.doublekekse.zipline.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    /**
     * Prevent flying kick when using zipline
     */
    @Inject(method = "getMaximumFlyingTicks", at = @At("HEAD"), cancellable = true)
    void getMaximumFlyingTicks(Entity entity, CallbackInfoReturnable<Integer> cir) {
        if (!(entity instanceof Player player)) {
            return;
        }

        if (isUsingZipline(player)) {
            cir.setReturnValue(Integer.MAX_VALUE);
        }
    }

    /**
     * Prevent moving incorrectly kick when using zipline
     */
    @WrapOperation(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSleeping()Z", ordinal = 1))
    boolean handleMovePlayer(ServerPlayer instance, Operation<Boolean> original) {
        if (isUsingZipline(instance)) {
            return true;
        }

        return original.call(instance);
    }

    /**
     * Prevent moving too quickly kick when using zipline
     */
    @WrapOperation(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"))
    boolean isSinglePlayerOwner(ServerGamePacketListenerImpl instance, Operation<Boolean> original) {
        if (isUsingZipline(instance.player)) {
            return true;
        }

        return original.call(instance);
    }

    @Unique
    boolean isUsingZipline(Player player) {
        return player.getUseItem().is(ZiplineItems.ZIPLINE);
    }
}
