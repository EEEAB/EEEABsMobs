package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalMagicCircle;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMMathUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ImmortalPounceGoal extends AnimationAI<EntityImmortal> {
    public static final float MAX_DISTANCE = 32F;
    private Vec3 pounceVec = Vec3.ZERO;
    private LivingEntity targetCache;
    private float distanceFactor;

    public ImmortalPounceGoal(EntityImmortal entity) {
        super(entity);
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public void start() {
        super.start();
        targetCache = entity.getTarget();
    }

    @Override
    public void stop() {
        super.stop();
        pounceVec = Vec3.ZERO;
        targetCache = null;
        distanceFactor = 0F;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.pouncePreAnimation || animation == entity.pounceHoldAnimation || animation == entity.pounceEndAnimation || animation == entity.pounceSmashAnimation || animation == entity.pouncePickAnimation;
    }

    @Override
    public void tick() {
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == entity.pouncePreAnimation) {
            if (tick < 10) {
                if (targetCache != null && !targetCache.isRemoved()) {
                    entity.lookAt(targetCache, 90F, 30F);
                    entity.getLookControl().setLookAt(targetCache, 200F, 30F);
                    Vec3 vec3 = entity.position();
                    pounceVec = new Vec3(targetCache.getX() - vec3.x, 0.0, targetCache.getZ() - vec3.z).normalize();
                    distanceFactor = getDistanceFactor(targetCache, MAX_DISTANCE, false);
                    if (tick == 5) EntityImmortalMagicCircle.spawn(entity.level, entity, entity.position().add(0, 0.25, 0), 3F, -1F, (int) (20 + (90 * distanceFactor)), entity.getYRot(), EntityImmortalMagicCircle.MagicCircleType.SPEED, false);
                } else if (entity.getTarget() != null) targetCache = entity.getTarget();
            } else {
                if (pounceVec.length() == 0) entity.playAnimation(entity.pounceEndAnimation);
                else {
                    entity.shakeGround(0F, MAX_DISTANCE, 0.125F, 2, 4);
                    entity.playAnimation(entity.pounceHoldAnimation);
                }
            }
        } else if (animation == entity.pounceHoldAnimation) {
            if (pounceVec.length() == 0 || distanceFactor == 0) entity.playAnimation(entity.pounceEndAnimation);
            else {
                if (tick < 40) {
                    boolean flag = targetCache != null;
                    if (flag) entity.getLookControl().setLookAt(targetCache, 10F, 10F);
                    float tickFactor = EMMathUtils.getTickFactor(tick, 40F, true);
                    float speedMultiplier = EMMathUtils.calculateSpeedMultiplier(tickFactor, distanceFactor, 3F, 15F);
                    //避免过快 限制移速在2级速度药水(每1级提升初速20%)
                    double baseValue = entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
                    double moveSpeed = Math.min(entity.getAttributeValue(Attributes.MOVEMENT_SPEED), baseValue + baseValue * 0.4);
                    if (tick % 2 == 0) doHurtTarget(4.5F, 5F, 3.5F, 1F, 0.1F, false, false, true, 0.5F);
                    if (flag && entity.distanceToSqr(targetCache) <= entity.getMeleeAttackRangeSqr(targetCache) + 9) {
                        speedMultiplier *= 0.5F;
                        distanceFactor = Math.min(getDistanceFactor(targetCache, MAX_DISTANCE / 2, true), 0.8F);
                        double heightDifference = Math.abs(entity.getY() - targetCache.getY());
                        if (heightDifference >= 6 || entity.getRandom().nextFloat() < 0.4F) entity.playAnimation(entity.pouncePickAnimation);
                        else entity.playAnimation(entity.pounceSmashAnimation);
                    } else if (speedMultiplier < 0.45F) this.entity.playAnimation(entity.pounceEndAnimation);
                    entity.setDeltaMovement(pounceVec.x * moveSpeed * speedMultiplier, entity.getDeltaMovement().y, pounceVec.z * moveSpeed * speedMultiplier);
                } else this.entity.playAnimation(entity.pounceEndAnimation);
            }
        } else if (animation == entity.pounceSmashAnimation) {
            if (tick < 9) {
                lookAtTarget();
                if (tick == 5) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.6F, entity.getVoicePitch());
            } else {
                entity.anchorToGround();
                entity.setYRot(entity.yRotO);
                if (tick == 9) entity.playSound(SoundInit.IMMORTAL_SHAKE_GROUND.get());
                else if (tick == 10) {
                    Vec3 pos = entity.getPosOffset(false, 3F, 0.5F, -1F);
                    for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level, pos, 3.5D, 0F, false, 20)) {
                        if (entityHit == entity) continue;
                        entity.doHurtTarget(entityHit, false, false, true, false, 0.025F, 1.0F, 0.9F);
                        entity.disableShield(entityHit, 50);
                        entity.knockBack(entityHit, 0.2, 0.5, false, false);
                    }
                } else if (tick == 11) entity.shakeGround(0.2F, MAX_DISTANCE / 2, 0.25F, 2, 4);
                else if (tick == 12) entity.level.playSound(null, entity.blockPosition(), SoundInit.IMMORTAL_STONE_CRACK.get(), entity.getSoundSource(), 1F, 1F);
                else if (tick > 30 && targetCache != null) entity.getLookControl().setLookAt(targetCache, 10F, 30F);
            }
        } else if (animation == entity.pouncePickAnimation) {
            if (tick < 12) {
                lookAtTarget();
                if (tick >= 5 && distanceFactor > 0) {
                    float tickFactor = EMMathUtils.getTickFactor(tick, 5F, false);
                    float speedMultiplier = EMMathUtils.calculateSpeedMultiplier(tickFactor, distanceFactor, 3F, 1.5F);
                    double moveSpeed = Math.min(entity.getAttributeValue(Attributes.MOVEMENT_SPEED), entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
                    double radians = Math.toRadians(entity.getYRot() + 90);
                    entity.setDeltaMovement(Math.cos(radians) * moveSpeed * speedMultiplier, entity.getDeltaMovement().y, Math.sin(radians) * moveSpeed * speedMultiplier);
                }
                if (tick == 6) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.6F, entity.getVoicePitch() + 0.2F);
                else if (tick == 10) {
                    doHurtTarget(5F, 6F, 4.5F, 0F, 1F, true, true, false, 1F);
                    entity.shakeGround(0F, MAX_DISTANCE / 2, 0.25F, 2, 4);
                }
            } else {
                if (tick > 15 && targetCache != null) entity.getLookControl().setLookAt(targetCache, 15F, 30F);
                entity.setYRot(entity.yRotO);
            }
        } else if (animation == entity.pounceEndAnimation) {
            if (targetCache != null && targetCache.isAlive()) entity.getLookControl().setLookAt(targetCache, 10F, 30F);
        }
    }

    private void doHurtTarget(float range, float height, float hitDistance, float strength, float yStrength, boolean sendMessage, boolean heal, boolean stun, float damageMultiplier) {
        float attackArc = 120F;
        boolean hitFlag = !sendMessage;
        for (LivingEntity entityHit : entity.getNearByEntities(LivingEntity.class, range, height, range, range)) {
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit);
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
            if (entityHitDistance < 0.75 || (entityHitDistance <= hitDistance && ((entityRelativeAngle >= -attackArc / 2 && entityRelativeAngle <= attackArc / 2) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F)))) {
                if (stun) entity.stun(null, entityHit, 40, false);
                entity.doHurtTarget(entityHit, true, heal && entityHit.hasEffect(EffectInit.ERODE_EFFECT.get()), heal, false, 0.03F, 1.0F, damageMultiplier);
                if (!hitFlag) {
                    hitFlag = true;
                    entity.playSound(SoundInit.IMMORTAL_PUNCH_HIT.get(), 1F, 1.1F);
                    entity.level.broadcastEntityEvent(entity, (byte) 8);
                }
                entity.knockBack(entityHit, strength, yStrength, true, false);
            }
        }
    }

    private float getDistanceFactor(LivingEntity target, float distance, boolean inversion) {
        return EMMathUtils.getTickFactor(Math.min(entity.distanceTo(target), distance * 2F), distance, inversion);
    }

    private void lookAtTarget() {
        if (entity.getTarget() != null && (targetCache == null || !targetCache.isAlive())) {
            targetCache = entity.getTarget();
        } else if (targetCache != null) {
            entity.lookAt(targetCache, 30F, 30F);
            entity.getLookControl().setLookAt(targetCache, 30F, 30F);
        }
    }
}
