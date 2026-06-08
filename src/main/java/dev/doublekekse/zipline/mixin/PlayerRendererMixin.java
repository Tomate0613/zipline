package dev.doublekekse.zipline.mixin;

import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"), cancellable = true)
    private static void getArmPos(Avatar avatar, ItemStack itemInHand, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        if(itemInHand.is(ZiplineItems.ZIPLINE))  {
            cir.setReturnValue(HumanoidModel.ArmPose.ZIPLINE_ZIPLINE);
        }
    }
}
