package dev.doublekekse.zipline.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Inject(method = "poseLeftArm", at = @At("HEAD"))
    void poseLeftArm(T state, CallbackInfo ci) {
        if (state.leftArmPose == HumanoidModel.ArmPose.ZIPLINE_ZIPLINE) {
            positionArm(leftArm);
        }
    }

    @Inject(method = "poseRightArm", at = @At("HEAD"))
    void poseRightArm(T state, CallbackInfo ci) {
        if (state.rightArmPose == HumanoidModel.ArmPose.ZIPLINE_ZIPLINE) {
            positionArm(rightArm);
        }
    }

    @Unique
    void positionArm(ModelPart arm) {
        int a = arm == rightArm ? 1 : -1;

        arm.xRot = (float) (-0.9f * Math.PI);
        arm.zRot = .5f * a;
        arm.y = 5;
        arm.z = -2;
    }
}
