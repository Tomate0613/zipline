package dev.doublekekse.zipline.mixin;

import net.minecraft.client.model.HumanoidModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HumanoidModel.ArmPose.class)
public enum ArmPoseMixin {
    ZIPLINE_ZIPLINE(true, true);

    @Shadow
    ArmPoseMixin(final boolean twoHanded, final boolean affectsOffhandPose) {
    }
}
