package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalMagicCircle;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import com.eeeab.eeeabsmobs.sever.util.EMMathUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ImmortalPounceGoal extends AnimationAI<EntityTheImmortal> {
    private static final float MAX_DISTANCE = 32F;
    private Vec3 pounceVec = Vec3.ZERO;
    private LivingEntity targetCache;
    private float distanceFactor;

    public ImmortalPounceGoal(EntityTheImmortal entity) {
        super(entity);
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.pouncePreAnimation || animation == entity.pounceHoldAnimation || animation == entity.pounceEndAnimation
                || animation == entity.pounceSmashAnimation || animation == entity.pouncePickAnimation;
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
    public void tick() {
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == entity.pouncePreAnimation) {
            if (tick < 15) {
                if (targetCache != null && targetCache.isAlive()) {
                    entity.lookAt(targetCache, 90F, 30F);
                    entity.getLookControl().setLookAt(targetCache, 90F, 30F);
                    Vec3 vec3 = entity.position();
                    pounceVec = new Vec3(targetCache.getX() - vec3.x, 0.0, targetCache.getZ() - vec3.z).normalize();
                    distanceFactor = getDistanceFactor(targetCache, false);
                    if (tick == 5) {
                        EntityImmortalMagicCircle.spawn(entity.level, entity.position().add(0, 0.75, 0), 3F, -1F, (int) (20 + (90 * distanceFactor)), EntityImmortalMagicCircle.MagicCircleType.SPEED);
                    }
                } else if (entity.getTarget() != null) targetCache = entity.getTarget();
            } else {
                if (pounceVec.length() == 0) {
                    entity.playAnimation(entity.pounceEndAnimation);
                } else {
                    EntityCameraShake.cameraShake(entity.level, entity.position(), MAX_DISTANCE, 0.125F, 0, 5);
                    entity.playAnimation(entity.pounceHoldAnimation);
                }
            }
        } else if (animation == entity.pounceHoldAnimation) {
            if (pounceVec.length() == 0 || distanceFactor == 0) {
                entity.playAnimation(entity.pounceEndAnimation);
            } else {
                if (tick < 40) {
                    float tickFactor = EMMathUtils.getTickFactor(tick, 40F, true);
                    float speedMultiplier = EMMathUtils.calculateSpeedMultiplier(tickFactor, distanceFactor, 3F, 15F);
                    double moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    entity.setDeltaMovement(pounceVec.x * moveSpeed * speedMultiplier, entity.getDeltaMovement().y, pounceVec.z * moveSpeed * speedMultiplier);
                    if (tick % 2 == 0) doHurtTarget(4.5F, 5F, 3.5F, 1F, 0.1F, false);
                    if (targetCache != null && entity.distanceToSqr(targetCache) <= entity.getMeleeAttackRangeSqr(targetCache) + 9) {
                        distanceFactor = getDistanceFactor(targetCache, true);
                        //避免过近距离导致无意义的前移
                        if (distanceFactor >= 0.8) distanceFactor = 0;
                        entity.playAnimation(entity.getRandom().nextBoolean() ? entity.pounceSmashAnimation : entity.pouncePickAnimation);
                    } else if (speedMultiplier < 0.45F) {
                        this.entity.playAnimation(entity.pounceEndAnimation);
                    }
                } else {
                    this.entity.playAnimation(entity.pounceEndAnimation);
                }
            }
        } else if (animation == entity.pounceSmashAnimation) {
            if (tick < 9) {
                lookAtTarget();
            } else {
                entity.setYRot(entity.yRotO);
                if (tick == 10) {
                    double radians = Math.toRadians(entity.getYRot() + 90);
                    for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level, entity.position().add(3F * Math.cos(radians), -1, 3F * Math.sin(radians)), 3.5D, 0F)) {
                        if (entityHit == entity) {
                            continue;
                        }
                        entity.immortalHurtTarget(entityHit, false);
                        entity.knockBack(entityHit, 0.2, 0.5, false);
                    }
                } else if (tick == 11) {
                    EntityCameraShake.cameraShake(entity.level, entity.position(), MAX_DISTANCE / 2, 0.125F, 3, 2);
                } else if (tick > 30 && targetCache != null) {
                    entity.getLookControl().setLookAt(targetCache, 10F, 30F);
                }
            }
        } else if (animation == entity.pouncePickAnimation) {
            if (tick < 12) {
                lookAtTarget();
                if (tick >= 5) {
                    float tickFactor = EMMathUtils.getTickFactor(tick, 7F, false);
                    float speedMultiplier = EMMathUtils.calculateSpeedMultiplier(tickFactor, distanceFactor, 3.5F, 2F);
                    double moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    double radians = Math.toRadians(entity.getYRot() + 90);
                    entity.setDeltaMovement(Math.cos(radians) * moveSpeed * speedMultiplier, entity.getDeltaMovement().y, Math.sin(radians) * moveSpeed * speedMultiplier);
                }
            } else {
                if (tick == 12) doHurtTarget(5F, 6F, 4.5F, 0F, 1F, true);
                else if (tick > 15 && targetCache != null) {
                    entity.getLookControl().setLookAt(targetCache, 15F, 30F);
                }
                entity.setYRot(entity.yRotO);
            }
        } else if (animation == entity.pounceEndAnimation) {
            if (targetCache != null && targetCache.isAlive()) {
                entity.getLookControl().setLookAt(targetCache, 10F, 30F);
            }
        }
    }

    private void doHurtTarget(float range, float height, float hitDistance, float strength, float yStrength, boolean sendMessage) {
        float attackArc = 120F;
        boolean hitFlag = !sendMessage;
        for (LivingEntity entityHit : entity.getNearByEntities(LivingEntity.class, range, height, range, range)) {
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit);
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
            if (entityHitDistance <= hitDistance && ((entityRelativeAngle >= -attackArc / 2 && entityRelativeAngle <= attackArc / 2) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                if (!hitFlag) {
                    hitFlag = true;
                    entity.level.broadcastEntityEvent(entity, (byte) 8);
                }
                entity.immortalHurtTarget(entityHit, false);
                entity.knockBack(entityHit, strength, yStrength, true);
            }
        }
    }

    private float getDistanceFactor(LivingEntity target, boolean inversion) {
        return EMMathUtils.getTickFactor(Math.min(entity.distanceTo(target), MAX_DISTANCE * 2F), MAX_DISTANCE, inversion);
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
