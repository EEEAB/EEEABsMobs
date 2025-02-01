package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.TickBasedProbabilityBooster;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.world.entity.LivingEntity;

public class ImmortalComboGoal extends AnimationGroupAI<EntityImmortal> {
    private final TickBasedProbabilityBooster consecutivePunchProb = new TickBasedProbabilityBooster(0.2F, 0.05F, 0.5F);
    private final TickBasedProbabilityBooster criticalHitProb = new TickBasedProbabilityBooster(0.1F, 0.01F, 0.25F);
    private static final float PUNCH_ATTACK_RANGE = 5F;
    private boolean comboFlag;

    public ImmortalComboGoal(EntityImmortal entity) {
        super(entity, () -> entity.punchRightAnimation, () -> entity.hardPunchRightAnimation, () -> entity.punchLeftAnimation, () -> entity.hardPunchLeftAnimation);
    }

    @Override
    public void stop() {
        entity.canInterruptsAnimation = false;
        super.stop();
        comboFlag = false;
        consecutivePunchProb.resetProbability();
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == entity.punchRightAnimation || animation == entity.punchLeftAnimation) {
            if (tick < 11) entity.anchorToGround();
            if (tick <= 12 || tick > 20) {
                if (target != null) {
                    entity.lookAt(target, 30F, 30F);
                    entity.getLookControl().setLookAt(target, 30F, 30F);
                } else entity.setYRot(entity.yRotO);
            }
            boolean left = animation == entity.punchLeftAnimation;
            if (tick == 10) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.5F, entity.getVoicePitch());
            if (tick == 11) entity.pursuit(5F, 0, 0.35);
            else if (tick >= 14 && tick <= 16) {
                doHurtTargetAndTryBreakBlock(PUNCH_ATTACK_RANGE, left ? 40F : 120F, left ? 120F : 40F, 0.5F, tick > 14, false, tick == 14, false, false, 0.89F, 1.0F);
            } else if (tick > 16) {
                if (!comboFlag && entity.checkComboRange(5, 1) && tick > 20 && tick < 25) {
                    if (nextAnimation(animation, entity.getRandom().nextFloat() < criticalHitProb.getProbability(), left ? entity.hardPunchRightAnimation : entity.hardPunchLeftAnimation)) {
                        criticalHitProb.resetProbability();
                        comboFlag = true;
                    } else if (nextAnimation(animation, entity.getRandom().nextFloat() < consecutivePunchProb.onTick().getProbability(), left ? entity.punchRightAnimation : entity.punchLeftAnimation)) {
                        criticalHitProb.onTick();
                        comboFlag = true;
                    }
                } else if (tick >= 26) {
                    if (comboFlag) {
                        if (tick == 26) entity.canInterruptsAnimation = true;
                        else if (tick <= 30 && entity.getHealthPercentage() < 80 && entity.getRandom().nextInt(10) == 0) {
                            entity.canInterruptsAnimation = false;
                            nextAnimation(animation, true, left ? entity.hardPunchRightAnimation : entity.hardPunchLeftAnimation);
                        }
                    } else if (tick == 30) entity.canInterruptsAnimation = true;
                }
            }
        } else if (animation == entity.hardPunchRightAnimation || animation == entity.hardPunchLeftAnimation) {
            if (tick < 20) entity.anchorToGround();
            if (tick < 20 || tick > 34) {
                if (target != null) {
                    entity.lookAt(target, 30F, 30F);
                    entity.getLookControl().setLookAt(target, 30F, 30F);
                }
            } else entity.setYRot(entity.yRotO);
            if (tick == 19) entity.playSound(SoundInit.IMMORTAL_ATTACK2.get(), 1.5F, 1.4F);
            if (tick >= 21 && tick < 24) {
                boolean left = animation == entity.hardPunchLeftAnimation;
                if (tick == 21) entity.pursuit(6F, 0, 0.5);
                doHurtTargetAndTryBreakBlock(PUNCH_ATTACK_RANGE + 0.5F, left ? 20F : 90F, left ? 90F : 20F, 1F, tick < 23, true, tick < 23, true, tick < 23, 0.9F, 0.8F);
            }
        }
    }

    private void doHurtTargetAndTryBreakBlock(float attackDistance, float rightAttackArc, float leftAttackArc, float strength, boolean continuous, boolean disableShied, boolean canHit, boolean ignoreArmor, boolean canStun, float baseDamageMultiplier, float damageMultiplier) {
        boolean hitFlag = false;
        for (LivingEntity entityHit : entity.getNearByLivingEntities(attackDistance, PUNCH_ATTACK_RANGE, attackDistance, attackDistance)) {
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit);
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
            if (entityHitDistance <= attackDistance && (entityRelativeAngle >= -leftAttackArc / 2 && entityRelativeAngle <= rightAttackArc / 2) || (entityRelativeAngle >= 360 - leftAttackArc / 2 || entityRelativeAngle <= -360 + rightAttackArc / 2)) {
                boolean hit = true;
                if (canHit) {
                    if (canStun) entity.stun(null, entityHit, 30, false);
                    hit = entity.immortalHurtTarget(entityHit, disableShied, entityHit.hasEffect(EffectInit.ERODE_EFFECT.get()), true, ignoreArmor, baseDamageMultiplier, damageMultiplier);
                    if (!disableShied) entity.disableShield(entityHit, 40);
                    if (!hitFlag) {
                        hitFlag = true;
                        if (hit) entity.playSound(disableShied ? SoundInit.IMMORTAL_PUNCH_HARD_HIT.get() : SoundInit.IMMORTAL_PUNCH_HIT.get(), 1F, disableShied ? 1F : 1.1F);
                        entity.shakeGround(0F, 20F, 0.3F, 1, 5);
                        entity.level().broadcastEntityEvent(entity, (byte) (disableShied ? 7 : 6));
                    }
                }
                entity.knockBack(entityHit, hit ? strength : strength - 0.2, hit && canHit ? 0.15 : 0.1, true, continuous);
            }
        }
        if (canHit && ModEntityUtils.canMobDestroy(entity)) {
            int offset = (int) (attackDistance / 2);
            if (ModEntityUtils.advancedBreakBlocks(entity.level(), entity, 50, offset, (int) attackDistance, offset, 0, offset, entity.checkCanDropItems(), true)) {
                if (!hitFlag) entity.level().broadcastEntityEvent(entity, (byte) 7);
            }
        }
    }
}
