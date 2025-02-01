package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalMagicCircle;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ImmortalAttractGoal extends AnimationAI<EntityImmortal> {
    public ImmortalAttractGoal(EntityImmortal entity) {
        super(entity);
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.attractAnimation;
    }


    @Override
    public void tick() {
        int tick = entity.getAnimationTick();
        if (tick < 40) {
            if (tick < 38) entity.anchorToGround();
            this.slowlyLookAtTarget();
            if (tick == 29) EntityImmortalMagicCircle.spawn(entity.level(), entity, entity.position().add(0, 0.25, 0), 2.5F, 0F, 10 + this.entity.getRandom().nextInt(11), entity.yHeadRot, EntityImmortalMagicCircle.MagicCircleType.POWER, true);
            else if (tick == 36) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.6F, entity.getVoicePitch());
            else if (tick == 39) {
                entity.pursuit(8F, 0, 0.5);
                entity.level().broadcastEntityEvent(entity, (byte) 9);
            }
        } else {
            if (tick == 40 || tick == 41 || tick == 42) {
                if (tick == 40) entity.shakeGround(0F, 20F, 0.25F, 2, 3);
                float attackArc = 180;
                boolean hitFlag = false;
                float attackDistance = 5F;
                for (Entity entityHit : entity.getNearByEntities(Entity.class, attackDistance, attackDistance, attackDistance, attackDistance)) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit.position());
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                    if (entityHitDistance <= attackDistance && ((entityRelativeAngle >= -attackArc / 2 && entityRelativeAngle <= attackArc / 2) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        if (tick == 40) {
                            if (entityHit instanceof LivingEntity livingHit) {
                                int preInvulnerableTime = entityHit.invulnerableTime;
                                entityHit.invulnerableTime = 0;
                                entity.stun(null, livingHit, 40, false);
                                if (!entity.immortalHurtTarget(livingHit, true, livingHit.hasEffect(EffectInit.ERODE_EFFECT.get()), false, false, 1.0F, 1.2F)) entityHit.invulnerableTime = preInvulnerableTime;
                                if (!hitFlag) {
                                    hitFlag = true;
                                    entity.playSound(SoundInit.IMMORTAL_PUNCH_HARD_HIT.get(), 1.2F, 1.1F);
                                    entity.level().broadcastEntityEvent(entity, (byte) 10);
                                }
                            } else if (!entityHit.isRemoved() && entityHit.isAttackable()) {
                                entity.doHurtTarget(entityHit);
                            }
                        }
                    }
                    if (entityHit instanceof LivingEntity livingHit) entity.knockBack(livingHit, 1, 0.2, true, tick > 40);
                }
                int offset = (int) (attackDistance / 2);
                if (tick == 41 && ModEntityUtils.canMobDestroy(entity) && ModEntityUtils.advancedBreakBlocks(entity.level(), entity, 50, offset, (int) attackDistance, offset, 0, offset, entity.checkCanDropItems(), true)) {
                    entity.level().broadcastEntityEvent(entity, (byte) 10);
                }
            }
            this.slowlyLookAtTarget();
        }
    }

    private void slowlyLookAtTarget() {
        LivingEntity target = entity.getTarget();
        if (target != null) {
            float lookAtSpeed = 5F;
            if (entity.distanceTo(target) > 5) lookAtSpeed = 90F;
            entity.getLookControl().setLookAt(target, lookAtSpeed, 30F);
            entity.lookAt(target, lookAtSpeed, 30F);
        }
    }
}
