package dev.doublekekse.zipline.mixin.compat.createaddition;

import dev.doublekekse.zipline.compat.createaddition.CreateAdditionLevelAttachment;
import dev.doublekekse.zipline.duck.LevelDuck;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Level.class)
public class LevelMixin implements LevelDuck {
    @Unique
    CreateAdditionLevelAttachment levelAttachment;

    @Inject(method = "<init>", at = @At("RETURN"))
    void init(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i, CallbackInfo ci) {
        if (!FabricLoader.getInstance().isModLoaded("createaddition")) {
            return;
        }

        levelAttachment = new CreateAdditionLevelAttachment();
    }

    @Override
    public CreateAdditionLevelAttachment zipline$getCAAttachment() {
        return levelAttachment;
    }
}
