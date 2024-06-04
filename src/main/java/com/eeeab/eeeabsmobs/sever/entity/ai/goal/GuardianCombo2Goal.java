package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.ai.AnimationAI;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GuardianCombo2Goal extends AnimationAI<EntityNamelessGuardian> {
    private boolean isPowered;
    private final float range;
    private final float attackArc;

    public GuardianCombo2Goal(EntityNamelessGuardian entity, float range, float attackArc) {
        super(entity);
        this.range = range;
        this.attackArc = attackArc;
    }

    @Override
    public void start() {
        super.start();
        this.isPowered = this.entity.isPowered();
    }


    @Override
    protected boolean test(Animation animation) {
        return animation == entity.attackAnimation4 || animation == entity.attackAnimation5 || animation == entity.attackAnimation6;
    }

    @Override
    public void tick() {
        Animation animation = this.entity.getAnimation();
        LivingEntity target = this.entity.getTarget();
        int tick = this.entity.getAnimationTick();
        float baseDamageMultiplier = isPowered ? 1.0F : 0.8F;
        entity.setDeltaMovement(0, entity.isOnGround() ? 0 : entity.getDeltaMovement().y, 0);
        if (animation == entity.attackAnimation4) {
            int lookAtFrame = isPowered ? 12 : 8;
            if (tick < lookAtFrame && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 8) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, this.entity.getVoicePitch());
            } else if (tick == 11) {
                pursuit(5F, 1.5F);
            } else if (tick == 12) {
                List<LivingEntity> entities = this.entity.getNearByLivingEntities(range);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= (attackArc + 70) / 2F && entityRelativeAngle >= -(attackArc - 20) / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, baseDamageMultiplier, true, true, true);
                        hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().add(0, 0.6, 0));
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    }
                }
            } else if (tick == 20 && entity.checkCanAttackRange(2.0F, range) && canToggleAnimation(80)) {
                this.entity.playAnimation(entity.attackAnimation5);
            }
        } else if (animation == entity.attackAnimation5) {
            tick = this.entity.getAnimationTick();
            int lookAtFrame = isPowered ? 10 : 4;
            if (tick < lookAtFrame && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 5) {
                pursuit(-1F, 1.4F);
            } else if (tick == 6) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.05f, this.entity.getVoicePitch() + 0.15f);
            } else if (tick == 10) {
                List<LivingEntity> entities = this.entity.getNearByLivingEntities(range);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= (attackArc + 20) / 2F && entityRelativeAngle >= -(attackArc + 20) / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, baseDamageMultiplier, true, true, true);
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        ModEntityUtils.forceKnockBack(hitEntity, 0.5F, ratioX, ratioZ, 1.0F, false);
                    }
                }
            } else if (tick == 20 && entity.checkCanAttackRange(2.5F, range) && canToggleAnimation(70)) {
                this.entity.playAnimation(entity.attackAnimation6);
            }
        } else if (animation == entity.attackAnimation6) {
            tick = this.entity.getAnimationTick();
            baseDamageMultiplier += 0.2F;
            int lookAtFrame = isPowered ? 14 : 12;
            if (tick < lookAtFrame && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 7) {
                pursuit(5F, 2.5F);
            } else if (tick == 10) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.2f, this.entity.getVoicePitch() + 0.15f);
            } else if (tick == 12) {
                for (int i = 0; i < 6; i++) {
                    entity.shockAttack(DamageSource.mobAttack(entity), i, -0.5F, 0.3F, 2F, 0.025F, 0.5F, (isPowered ? 1.0F : 0.8F), true, false, true, true);
                }
            } else if (tick == 13) {
                final float attackArc = 30F;
                final float range = 5.2F;
                List<LivingEntity> entities = entity.getNearByLivingEntities(range, range - 0.6F, range, range);
                for (LivingEntity livingEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, livingEntity);
                    float entityHitDistance = (float) Math.sqrt((livingEntity.getZ() - entity.getZ()) * (livingEntity.getZ() - entity.getZ()) + (livingEntity.getX() - entity.getX()) * (livingEntity.getX() - entity.getX())) - livingEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= attackArc / 2F && entityRelativeAngle >= -attackArc / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.guardianHurtTarget(entity, livingEntity, 0.025F, 1.0F, baseDamageMultiplier, true, true, true);
                    }
                }
            } else if (tick == 14) {
                entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 2F, 0.2F);
            } else if (tick == 15) {
                entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + entity.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(entity.level, entity.position(), 20, 0.125F, 5, 10);
            }
        }
    }

    private boolean canToggleAnimation(int healthPercentage) {
        return (!isPowered && ((entity.getHealthPercentage() >= healthPercentage && entity.getRandom().nextFloat() < 0.4F) ||
                ((entity.getHealthPercentage() < healthPercentage || !entity.isFirstMadness()) && entity.getRandom().nextFloat() < 0.6F))) ||
                (isPowered && entity.getRandom().nextFloat() < 0.9F);
    }

    private void pursuit(float pursuitDistance, float moveMultiplier) {
        float targetDistance = this.entity.targetDistance;
        if (this.entity.getTarget() != null && targetDistance < pursuitDistance && targetDistance > 0) {
            moveMultiplier = --targetDistance;
        }
        targetDistance = this.entity.targetDistance;
        if (this.entity.getTarget() == null || targetDistance > 1.8F)
            this.entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * moveMultiplier, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * moveMultiplier));
    }
}
