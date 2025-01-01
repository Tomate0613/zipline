package dev.doublekekse.zipline.item;

import dev.doublekekse.zipline.Cable;
import dev.doublekekse.zipline.Cables;
import dev.doublekekse.zipline.client.ZiplineClient;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ZiplineItem extends Item {
    public Cable cable = null;

    private Vec3 initialLookDirection = null;
    private boolean actuallyUsing = false;
    private Vec3 lastDir = null;
    private double speed = 0;
    private double t;

    private static final double HANG_OFFSET = 2.12;
    private static final double TOP_VERTICAL_SNAP_FACTOR = 0.3;
    private static final double SNAP_RADIUS = 3;

    public ZiplineItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 0;
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

            if (cable == null) {
                return;
            }

            var closestPoint = cable.getClosestPoint(offsetPlayerPos);
            var playerAttachPos = closestPoint.add(0, -HANG_OFFSET, 0);

            if (closestPoint.y > playerPos.y + TOP_VERTICAL_SNAP_FACTOR * HANG_OFFSET && !isInvalidPosition(player, playerAttachPos)) {
                enable(player, offsetPlayerPos);
            }
        }

        if (actuallyUsing) {
            ziplineTick(player, level);
        }
    }

    void enable(Player player, Vec3 offsetPlayerPos) {
        initialLookDirection = player.getLookAngle();
        actuallyUsing = true;
        speed = player.getDeltaMovement().length();
        t = cable.getProgress(offsetPlayerPos);

        var futureT = t + directionFactor() * .1 / cable.length();
        var delta = cable.getPoint(futureT).subtract(offsetPlayerPos);

        float yaw = (float) (Mth.atan2(delta.z, delta.x) * 57.2957763671875 - player.getYRot());
        ZiplineClient.ziplineTilt(yaw);

        player.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 0.6f, 1);
    }

    void updateSpeed() {
        if (speed < 1.6) {
            speed = Mth.lerp(0.03, speed, 1.6);
        }
    }

    boolean isForwards() {
        double dotProduct = initialLookDirection.dot(cable.direction());
        return dotProduct >= 0;
    }

    int directionFactor() {
        return isForwards() ? 1 : -1;
    }

    void updateProgress() {
        t += directionFactor() * speed / cable.length();

        t = Mth.clamp(t, 0.0, 1.0);
    }

    boolean isInvalidPosition(Player player, Vec3 pos) {
        return ((LocalPlayer) player).suffocatesAt(BlockPos.containing(pos.add(0, -HANG_OFFSET, 0)));
    }

    void ziplineTick(Player player, Level level) {
        var closestPoint = cable.getPoint(t);

        updateSpeed();
        updateProgress();

        Vec3 newPosition = cable.getPoint(t);
        lastDir = newPosition.subtract(closestPoint);

        var oldPosition = player.position();
        player.setPos(newPosition.x, newPosition.y - HANG_OFFSET, newPosition.z);

        if (isInvalidPosition(player, newPosition)) {
            player.setPos(oldPosition);
            interruptUsing(player);

            return;
        }


        player.setDeltaMovement(0, 0, 0);

        player.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, .3f + (float) (speed));

        if (t >= 1.0 || t <= 0.0) {
            interruptUsing(player);
        }
    }

    void interruptUsing(Player player) {
        player.stopUsingItem();
        applyExitMomentum(player);
        player.getCooldowns().addCooldown(this, 20);

        player.playSound(SoundEvents.IRON_GOLEM_REPAIR, 0.5f, 1);
    }

    void applyExitMomentum(LivingEntity livingEntity) {
        livingEntity.addDeltaMovement(lastDir.scale(.5));
        livingEntity.addDeltaMovement(livingEntity.getLookAngle().with(Direction.Axis.Y, 0).scale(.5));
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        if (livingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 10);
        }

        if (!level.isClientSide) {
            return;
        }

        if (actuallyUsing) {
            livingEntity.addDeltaMovement(new Vec3(0, 0.8, 0));
            applyExitMomentum(livingEntity);
        }

        actuallyUsing = false;
        speed = 0;

        super.releaseUsing(itemStack, level, livingEntity, i);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        var itemStack = player.getItemInHand(interactionHand);

        player.startUsingItem(interactionHand);

        if (!level.isClientSide) {
            return InteractionResultHolder.pass(itemStack);
        }

        if (player.isLocalPlayer()) {
            actuallyUsing = false;
            speed = 0;
        }

        return InteractionResultHolder.consume(itemStack);
    }
}
