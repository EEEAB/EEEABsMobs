package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalMagicCircle;
import com.eeeab.eeeabsmobs.sever.entity.mob.immortal.EntityImmortalBoss;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ImmortalBlockGoal extends AnimationGroupAI<EntityImmortalBoss> {
    public ImmortalBlockGoal(EntityImmortalBoss entity) {
        super(entity, EntityImmortalBoss.ARMBLOCK_ANIMATION, EntityImmortalBoss.ARMBLOCK_HOLD_ANIMATION, EntityImmortalBoss.ARMBLOCK_END_ANIMATION, EntityImmortalBoss.ARMBLOCK_COUNTERATTACK_ANIMATION);
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
        if (animation == EntityImmortalBoss.ARMBLOCK_ANIMATION) {
            nextAnimation(animation, EntityImmortalBoss.ARMBLOCK_HOLD_ANIMATION);
        } else if (animation == EntityImmortalBoss.ARMBLOCK_HOLD_ANIMATION) {
            nextAnimation(animation, EntityImmortalBoss.ARMBLOCK_END_ANIMATION);
        } else if (animation == EntityImmortalBoss.ARMBLOCK_COUNTERATTACK_ANIMATION) {
            if (tick == 4) {
                entity.rangeAttack(5, 6, 5, 5, hitEntity -> {
                    entity.stun(null, hitEntity, 20, false);
                    entity.doHurtTarget(hitEntity, true, false, false, 0.65F, 0.9F);
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
                nextAnimation(EntityImmortalBoss.ARMBLOCK_COUNTERATTACK_ANIMATION, EntityImmortalBoss.SMASH_GROUND_ANIMATION3, true);
                if (tick == 15) EntityImmortalMagicCircle.spawn(entity.level(), entity, entity.position().add(0, 0.25, 0), 3F, 0F, 80, this.entity.getYRot(), EntityImmortalMagicCircle.MagicCircleType.POWER, true);
            }
        }
    }
}
