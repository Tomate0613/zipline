package dev.doublekekse.zipline.mixin.compat.createaddition;

import com.mrh0.createaddition.blocks.connector.base.AbstractConnectorBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.doublekekse.zipline.compat.createaddition.CreateAdditionLevelAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractConnectorBlockEntity.class)
public abstract class AbstractConnectorBlockEntityMixin extends SmartBlockEntity {
    public AbstractConnectorBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "firstTick", at = @At("RETURN"), remap = false)
    void init(CallbackInfo ci) {
        CreateAdditionLevelAttachment.getAttachment(getLevel()).connectorPositions.add(getBlockPos());
    }

    @Inject(method = "remove()V", at = @At("RETURN"), remap = false)
    void remove(CallbackInfo ci) {
        CreateAdditionLevelAttachment.getAttachment(getLevel()).connectorPositions.remove(getBlockPos());
    }
}
