package dev.doublekekse.zipline.mixin.compat.phonos;

import io.github.foundationgames.phonos.world.sound.CableConnection;
import io.github.foundationgames.phonos.world.sound.block.BlockEntityOutputs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockEntityOutputs.class)
public interface BlockEntityOutputsAccessor {
    @Accessor(remap = false)
    CableConnection[] getConnections();
}
