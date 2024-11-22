package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityFallingBlock;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMMathUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class ImmortalShakeGroundGoal extends AnimationAI<EntityImmortal> {
    private static final int SHAKE_GROUND_RANGE = 20;

    public ImmortalShakeGroundGoal(EntityImmortal entity) {
        super(entity);
    }

    @Override
    public void stop() {
        entity.canInterruptsAnimation = false;
        super.stop();
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.smashGround1Animation || animation == entity.smashGround2Animation || animation == entity.smashGround3Animation || animation == entity.shoryukenAnimation;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == entity.smashGround1Animation) {
            entity.anchorToGround();
            if (target != null && (tick < 19 || tick > 25)) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            } else entity.setYRot(entity.yRotO);
            if (tick == 15) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.6F, entity.getVoicePitch() + 0.05F);
            else if (tick == 19) entity.playSound(SoundInit.IMMORTAL_SHAKE_GROUND.get(), 1F, 1F);
            else if (tick == 21) {
                double radians = Math.toRadians(entity.getYRot() + 90);
                boolean onGround = entity.onGround();
                for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level(), entity.position().add(1.5F * Math.cos(radians), -1, 1.5F * Math.sin(radians)), 3.8D, -0.03F, false, 12)) {
                    if (entityHit == entity) {
                        continue;
                    }
                    boolean hit = entity.doHurtTarget(entityHit, false, false, false, false, 0.025F, 1.2F, 0.9F);
                    entity.knockBack(entityHit, onGround ? hit ? 0.5 : 0.25 : 0.1, onGround ? hit ? 0.3 : 0.15 : -1, true, false);
                }
            } else if (tick == 22) entity.shakeGround(0.3F, SHAKE_GROUND_RANGE, 0.2F, 5, 5);
        } else if (animation == entity.smashGround2Animation) {
            entity.anchorToGround();
            if (target != null && (tick >= 60 || tick < 16)) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            } else entity.setYRot(entity.yRotO);
            if (tick == 12) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.7F, entity.getVoicePitch() + 0.1F);
            else if (tick == 16) entity.playSound(SoundInit.IMMORTAL_SHAKE_GROUND.get(), 1.2F, 1F);
            else if (tick == 17) {
                double radians = Math.toRadians(entity.getYRot() + 90);
                for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level(), entity.position().add(1.5F * Math.cos(radians), -1, 1.5F * Math.sin(radians)), 5D, 0F, true, 35)) {
                    if (entityHit == entity) continue;
                    entity.doHurtTarget(entityHit, false, false, false, false, 0.025F, 1.1F, 1.1F);
                    entity.knockBack(entityHit, 0.5, 0.05, true, false);
                }
            } else if (tick == 18) entity.shakeGround(0.45F, SHAKE_GROUND_RANGE, 0.2F, 5, 5);
            else if (tick == 42) entity.playSound(SoundInit.IMMORTAL_ATTACK2.get(), 1.5F, 1.2F);
            else if (tick > 45 && tick < 56) {
                if (tick == 46) entity.shakeGround(0F, SHAKE_GROUND_RANGE, 0.2F, 8, 2);
                int i = tick - 45;
                float tickFactor = EMMathUtils.getTickFactor(i, 9, false);
                if (tick % 2 == 0) entity.level().playSound(null, entity.blockPosition(), SoundInit.IMMORTAL_STONE_CRACK.get(), entity.getSoundSource(), 1F, 1F);
                ShockWaveUtils.doAdvShockWave(entity, i, 3F, 2, 1.5, 2, false, false, hit -> {
                    if (hit instanceof LivingEntity entityHit) {
                        entity.doHurtTarget(entityHit, false, false, false, false, 0.03F, 1.0F, 0.6F);
                        entity.knockBack(entityHit, 0.2, 0.05, true, false);
                    } else if (hit instanceof EntityFallingBlock) {
                        hit.setDeltaMovement(hit.getDeltaMovement().add(0, 0.025 * tickFactor, 0));
                    }
                }, 0.1F + 0.65F * tickFactor);
            } else if (tick == 75) entity.canInterruptsAnimation = true;
        } else if (animation == entity.smashGround3Animation) {
            if (entity.blockEntity != null) target = entity.blockEntity;
            if (tick < 20 && target != null) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            } else entity.setYRot(entity.yRotO);
            if (tick > 10 && tick < 20) {
                if (tick == 16) entity.playSound(SoundInit.IMMORTAL_ATTACK2.get(), 1.5F, 1.4F);
                pursuit(target, EMMathUtils.getTickFactor(tick - 10, 5, true), 3F, 6F, 2F);
            } else if (tick >= 20 && tick < 32) {
                entity.anchorToGround();
                if (tick == 20) {
                    float width = entity.getBbWidth() * 0.8F;
                    smashGround(entity.getPosOffset(true, 1.45F, width, -1));
                    smashGround(entity.getPosOffset(false, 1.45F, width, -1));
                } else if (tick == 21) entity.shakeGround(0.3F, SHAKE_GROUND_RANGE, 0.215F, 8, 2);
                int i = tick - 20;
                float tickFactor = EMMathUtils.getTickFactor(i, 9, false);
                if (tick % 2 == 0) entity.level().playSound(null, entity.blockPosition(), SoundInit.IMMORTAL_STONE_CRACK.get(), entity.getSoundSource(), 1F, 1F);
                ShockWaveUtils.doAdvShockWave(entity, i, 3F, 0.7, 1.5, 2, false, false, hit -> {
                    if (hit instanceof LivingEntity entityHit) {
                        entity.doHurtTarget(entityHit, false, false, false, false, 0.03F, 1.0F, 0.75F);
                        entity.knockBack(entityHit, 0.3, 0.05, true, false);
                    } else if (hit instanceof EntityFallingBlock) {
                        hit.setDeltaMovement(hit.getDeltaMovement().add(0, 0.02 * tickFactor, 0));
                    }
                }, 0.2F + 0.65F * tickFactor);
            }
        } else if (animation == entity.shoryukenAnimation) {
            if ((tick < 17 || (tick >= 30 && tick < 40) || tick >= 65) && target != null) {
                float speed = tick >= 65 ? 15F : 30F;
                entity.lookAt(target, speed, speed);
                entity.getLookControl().setLookAt(target, speed, speed);
            } else entity.setYRot(entity.yRotO);
            if (tick > 10 && tick < 30) {
                pursuit(target, EMMathUtils.getTickFactor(tick - 10, 20, true), 5F, 10F, 3F);
            } else if (tick >= 40 && tick <= 45) {
                entity.setYRot(entity.yRotO);
                pursuit(target, EMMathUtils.getTickFactor(tick - 40, 5, true), 3.725F, 7.5F, 7F);
            }
            if (tick == 15) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.7F, entity.getVoicePitch() - 0.05F);
            else if (tick == 16) entity.playSound(SoundInit.IMMORTAL_SHORYUKEN.get(), 0.5F, 0.8F);
            else if (tick == 20) {
                float attackDistance = 5F;
                boolean hitFlag = false;
                for (LivingEntity entityHit : entity.getNearByLivingEntities(attackDistance)) {
                    hitFlag = true;
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit);
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                    if (entityHitDistance <= attackDistance && ((entityRelativeAngle >= -40F / 2 && entityRelativeAngle <= 90) || (entityRelativeAngle >= 360 - 100F / 2F || entityRelativeAngle <= -360 + 100F / 2F))) {
                        entity.doHurtTarget(entityHit, false, entityHit.hasEffect(EffectInit.ERODE_EFFECT.get()), true, false, 0.03F, 0.8F, 1.0F);
                        entity.knockBack(entityHit, 0.65, 0.45, true, false);
                    }
                }
                if (hitFlag) entity.playSound(SoundInit.IMMORTAL_PUNCH_HIT.get(), 1F, 1.1F);
            }
            if (tick == 42) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.7F, entity.getVoicePitch() + 0.1F);
            else if (tick == 44) entity.playSound(SoundInit.IMMORTAL_SHORYUKEN.get(), 0.6F, 0.9F);
            else if (tick == 46) entity.playSound(SoundInit.IMMORTAL_SHAKE_GROUND.get(), 1.2F, 1.2F);
            else if (tick == 47) {
                double radians = Math.toRadians(entity.getYRot() + 90);
                for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level(), entity.position().add(4.5F * Math.cos(radians), -1, 4.5F * Math.sin(radians)), 2.6D, -0.05F, false, 10)) {
                    if (entityHit == entity) continue;
                    int preInvulnerableTime = entityHit.invulnerableTime;
                    entityHit.invulnerableTime = 0;
                    boolean hit = entity.doHurtTarget(entityHit, true, false, false, false, 0.03F, 1.2F, 1.0F);
                    if (!hit) entityHit.invulnerableTime = preInvulnerableTime;
                    entity.knockBack(entityHit, hit ? 0.2 : 0.1, hit ? 0.25 : 0.15, true, false);
                }
            } else if (tick == 48) {
                entity.level().playSound(null, entity.blockPosition(), SoundInit.IMMORTAL_STONE_CRACK.get(), entity.getSoundSource(), 0.9F, 1F);
                entity.shakeGround(0.3F, SHAKE_GROUND_RANGE, 0.2F, 5, 5);
            }
        }
    }

    private void smashGround(Vec3 pos) {
        for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level(), pos, 2.8D, 0F, false, 5)) {
            if (entityHit == entity) continue;
            entity.doHurtTarget(entityHit, true, false, true, false, 0.025F, 1.1F, 1.2F);
            entity.knockBack(entityHit, 0.3, 0.1, true, false);
        }
    }

    private void pursuit(LivingEntity target, float tickFactor, float distance, float maxDistance, float canMoveDistance) {
        float distanceFactor = 0.25F;
        if (target != null) {
            float distanceTo = entity.distanceTo(target);
            if (distanceTo <= canMoveDistance) return;
            distanceFactor = EMMathUtils.getTickFactor(Math.min(distanceTo, distance), maxDistance, false);
        }
        double radians = Math.toRadians(entity.getYRot() + 90);
        Vec3 pounceVec = new Vec3(Math.cos(radians), 0, Math.sin(radians)).normalize();
        double moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float speedMultiplier = EMMathUtils.calculateSpeedMultiplier(tickFactor, distanceFactor, 3F, 15F);
        entity.setDeltaMovement(pounceVec.x * speedMultiplier * moveSpeed, entity.getDeltaMovement().y, pounceVec.z * speedMultiplier * moveSpeed);
    }
}
