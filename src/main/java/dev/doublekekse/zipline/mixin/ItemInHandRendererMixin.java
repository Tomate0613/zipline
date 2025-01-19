package dev.doublekekse.zipline.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    public abstract void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i);

    @Shadow
    protected abstract void renderPlayerArm(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g, HumanoidArm humanoidArm);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    void renderArmWithItem(AbstractClientPlayer abstractClientPlayer, float tickDelta, float xRot, InteractionHand interactionHand, float attackAnim, ItemStack itemStack, float mainHandHeight, PoseStack poseStack, MultiBufferSource multiBufferSource, int lightCoords, CallbackInfo ci) {
        if (!itemStack.is(ZiplineItems.ZIPLINE) || !abstractClientPlayer.isUsingItem()) {
            return;
        }

        poseStack.pushPose();

        boolean bl = interactionHand == InteractionHand.MAIN_HAND;
        HumanoidArm humanoidArm = bl ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();

        boolean bl2 = humanoidArm == HumanoidArm.RIGHT;
        int q = bl2 ? 1 : -1;

        shake(itemStack, abstractClientPlayer, tickDelta, poseStack);

        double pp = 0;
        poseStack.translate(0, pp, 0);
        poseStack.translate(0, -.4, .2);

        poseStack.mulPose(Axis.XP.rotationDegrees(10));
        poseStack.mulPose(Axis.YN.rotationDegrees((float) q * -10.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) q * 70));

        renderPlayerArm(poseStack, multiBufferSource, lightCoords, 0, 0, humanoidArm);
        poseStack.popPose();

        poseStack.pushPose();

        shake(itemStack, abstractClientPlayer, tickDelta, poseStack);

        poseStack.translate((float) q * 0.1, -0.52f, -0.72f);
        poseStack.translate(0, 1.6, -.4);
        poseStack.translate(0, pp, 0);

        this.renderItem(abstractClientPlayer, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !bl2, poseStack, multiBufferSource, lightCoords);

        poseStack.popPose();

        ci.cancel();
    }

    @Unique
    void shake(ItemStack itemStack, AbstractClientPlayer abstractClientPlayer, float tickDelta, PoseStack poseStack) {
        float useFactor = itemStack.getUseDuration() - (abstractClientPlayer.getUseItemRemainingTicks() - tickDelta + 1.0f);

        float m = Mth.sin((useFactor - 0.1f) * 1.3f);
        float q = Mth.sin((useFactor * .3f - 0.4f) * 1.3f);

        float influence = Mth.clamp((useFactor * .1f) - 0.1f, 0, 1);

        float o = m * influence;
        float l = q * influence;

        poseStack.translate(l * 0.003f, o * 0.001f, o * 0.001f);
    }
}
