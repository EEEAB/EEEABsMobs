package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalMagicCircle;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ImmortalBlockGoal extends AnimationGroupAI<EntityImmortal> {
    public ImmortalBlockGoal(EntityImmortal entity) {
        super(entity, () -> entity.armBlockAnimation, () -> entity.armBlockHoldAnimation, () -> entity.armBlockEndAnimation, () -> entity.armBlockCounterattackAnimation);
    }

    @Override
    public void tick() {
        entity.setDeltaMovement(entity.getFluidFallingAdjustedMovement(0.08D, entity.getDeltaMovement().y <= 0.0D, entity.getDeltaMovement().multiply(0, 1, 0)));
        if (entity.blockEntity == null || !entity.blockEntity.isAlive()) {
            entity.blockEntity = entity.getTarget();
        } else if (entity.blockEntity.isAlive()) {
            entity.lookAt(entity.blockEntity, 30F, 30F);
            entity.getLookControl().setLookAt(entity.blockEntity, 30F, 30F);
        }
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == entity.armBlockAnimation) {
            nextAnimation(animation, entity.armBlockHoldAnimation);
        } else if (animation == entity.armBlockHoldAnimation) {
            nextAnimation(animation, entity.armBlockEndAnimation);
        } else if (animation == entity.armBlockCounterattackAnimation) {
            if (tick == 4) {
                entity.rangeAttack(6, 6, 6, 6, hitEntity -> {
                    entity.immortalHurtTarget(hitEntity, true, false, false, false, 0.65F, 0.9F);
                    if (!hitEntity.isInvulnerable()) {
                        if (hitEntity instanceof Player player && player.getAbilities().invulnerable) return;
                        double angle = entity.getAngleBetweenEntities(entity, hitEntity);
                        double x = 1.5F * Math.cos(Math.toRadians(angle - 90));
                        double z = 1.5F * Math.sin(Math.toRadians(angle - 90));
                        hitEntity.setDeltaMovement(x, 0.35, z);
                        if (hitEntity instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(hitEntity));
                        }
                    }
                });
            }
            if (tick >= 15) {
                if (!nextAnimation(entity.armBlockCounterattackAnimation, entity.blockEntity != null && entity.getHealthPercentage() < 50 && entity.getTimeUntilLaser() <= 0
                        && entity.distanceTo(entity.blockEntity) > 10 && this.entity.getRandom().nextFloat() < 0.5F, entity.unleashEnergyAnimation)) {
                    nextAnimation(entity.armBlockCounterattackAnimation, entity.smashGround3Animation, true);
                }
                if (tick == 15) EntityImmortalMagicCircle.spawn(entity.level(), entity, entity.position().add(0, 0.25, 0), 3F, 0F, 20, this.entity.getYRot(), EntityImmortalMagicCircle.MagicCircleType.POWER, true);
            }
        }
    }
}
