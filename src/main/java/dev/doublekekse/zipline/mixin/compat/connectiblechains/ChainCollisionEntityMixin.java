package dev.doublekekse.zipline.mixin.compat.connectiblechains;

import com.github.legoatoom.connectiblechains.entity.ChainCollisionEntity;
import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChainCollisionEntity.class)
public abstract class ChainCollisionEntityMixin extends Entity {
    @Shadow
    public abstract InteractionResult interact(Player player, InteractionHand hand);

    public ChainCollisionEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        var defaultValue = super.canCollideWith(entity);

        if (!defaultValue) {
            return false;
        }

        if (!(entity instanceof Player player)) {
            return true;
        }

        var mainHand = player.getMainHandItem();
        var offHand = player.getOffhandItem();

        return !mainHand.is(ZiplineItems.ZIPLINE) && !offHand.is(ZiplineItems.ZIPLINE);
    }
}
