package dev.doublekekse.zipline.item;

import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import dev.doublekekse.zipline.client.ZiplineClient;
import dev.doublekekse.zipline.registry.ZiplineSoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ZiplineItem extends Item {
    public Cable cable = null;

    private int directionFactor;
    private boolean actuallyUsing = false;
    private Vec3 lastDir = null;
    private double speed = 0;
    private double progress;

    private static final double HANG_OFFSET = 2.12;
    private static final double TOP_VERTICAL_SNAP_FACTOR = 0.3;
    private static final double SNAP_RADIUS = 3;
    private static final double MAX_TURN_ANGLE =  0.707;

    public ZiplineItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        super.onUseTick(level, livingEntity, itemStack, i);


        if (!level.isClientSide || !(livingEntity instanceof Player player) || !player.isLocalPlayer()) {
            return;
        }


        if (!actuallyUsing) {
            var playerPos = player.position();

            var offsetPlayerPos = playerPos.add(0, HANG_OFFSET, 0);

            cable = Cables.getClosestCable(offsetPlayerPos, SNAP_RADIUS);

            if (cable == null || !cable.isValid()) {
                return;
            }

            var closestPoint = cable.getClosestPoint(offsetPlayerPos);
            var playerAttachPos = closestPoint.add(0, -HANG_OFFSET, 0);

            if (closestPoint.y > playerPos.y + TOP_VERTICAL_SNAP_FACTOR * HANG_OFFSET && !isInvalidPosition(player, playerAttachPos.subtract(playerPos))) {
                enable(player, offsetPlayerPos);
            }
        }


        if (actuallyUsing) {
            ziplineTick(player, level);
        }
    }

    void enable(Player player, Vec3 offsetPlayerPos) {
        actuallyUsing = true;
        speed = player.getDeltaMovement().length();
        progress = cable.getProgress(offsetPlayerPos);
        directionFactor = player.getLookAngle().dot(cable.direction(progress)) >= 0 ? 1 : -1;

        var futureT = progress + directionFactor * .1 / cable.length();
        var delta = cable.getPoint(futureT).subtract(offsetPlayerPos);

        float yaw = (float) (Mth.atan2(delta.z, delta.x) * 57.2957763671875 - player.getYRot());
        ZiplineClient.ziplineTilt(yaw);

        player.playSound(ZiplineSoundEvents.ZIPLINE_ATTACH, 0.6f, 1);
    }

    void disable() {
        cable = null;
        actuallyUsing = false;
        speed = 0;
    }

    void updateSpeed() {
        if (speed < 1.6) {
            speed = Mth.lerp(0.03, speed, 1.6);
        }
    }

    void updateProgress() {
        progress += directionFactor * speed / cable.length();

        progress = Mth.clamp(progress, 0.0, 1.0);
    }

    boolean hasCollision(Iterable<VoxelShape> shapes) {
        for (var shape : shapes) {
            if (!shape.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    boolean isInvalidPosition(Player player, Vec3 deltaPos) {
        AABB collisionBox = player.getBoundingBox().move(deltaPos);
        Iterable<VoxelShape> blockCollisions = player.level().getBlockCollisions(player, collisionBox);

        return hasCollision(blockCollisions);
    }

    void ziplineTick(Player player, Level level) {
        if(!cable.isValid()) {
            interruptUsing(player);
            return;
        }

        var closestPoint = cable.getPoint(progress);

        updateSpeed();
        updateProgress();

        Vec3 newPosition = cable.getPoint(progress);
        Vec3 newOffsetPosition = new Vec3(newPosition.x, newPosition.y - HANG_OFFSET, newPosition.z);

        lastDir = newPosition.subtract(closestPoint);

        if (isInvalidPosition(player, lastDir)) {
            interruptUsing(player);

            return;
        }


        player.setPos(newOffsetPosition);


        player.setDeltaMovement(0, 0, 0);

        player.playSound(ZiplineSoundEvents.ZIPLINE_USE, 1.0F, .3f + (float) (speed));

        if (progress >= 1.0 || progress <= 0.0) {
            var nextCables = cable.getNext(directionFactor == 1);

            var playerDir = player.getLookAngle();
            var cableDir = lastDir.normalize();

            double highestDotProduct = -1;
            Cable nextCable = null;

            for (var next : nextCables) {
                if (cable.equals(next)) {
                    continue;
                }

                var lookDotProduct = next.direction(0).dot(playerDir);
                var cableDotProduct = next.direction(0).dot(cableDir);

                if (lookDotProduct > highestDotProduct && cableDotProduct > MAX_TURN_ANGLE) {
                    highestDotProduct = lookDotProduct;
                    nextCable = next;
                }
            }

            if (nextCable == null) {
                interruptUsing(player);
                return;
            }

            cable = nextCable;
            directionFactor = 1;
            progress = 0;
        }
    }

    void interruptUsing(Player player) {
        player.stopUsingItem();
        applyExitMomentum(player);
        player.getCooldowns().addCooldown(this, 20);

        player.playSound(ZiplineSoundEvents.ZIPLINE_INTERRUPT, 0.5f, 1);
        disable();
    }

    void applyExitMomentum(LivingEntity livingEntity) {
        livingEntity.addDeltaMovement(lastDir.scale(.5));
        livingEntity.addDeltaMovement(livingEntity.getLookAngle().with(Direction.Axis.Y, 0).scale(.5));
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        player.getCooldowns().addCooldown(this, 10);

        if (!level.isClientSide) {
            return;
        }

        if (actuallyUsing) {
            livingEntity.addDeltaMovement(new Vec3(0, 0.8, 0));
            applyExitMomentum(livingEntity);
            disable();
        }

        super.releaseUsing(itemStack, level, livingEntity, i);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        var itemStack = player.getItemInHand(interactionHand);

        player.startUsingItem(interactionHand);

        if (!level.isClientSide) {
            return InteractionResultHolder.consume(itemStack);
        }

        if (player.isLocalPlayer()) {
            actuallyUsing = false;
            speed = 0;
        }

        return InteractionResultHolder.consume(itemStack);
    }
}
