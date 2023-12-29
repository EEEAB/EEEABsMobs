package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationAbstractGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GuardianCombo2Goal extends AnimationAbstractGoal<EntityNamelessGuardian> {
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
        return animation == EntityNamelessGuardian.ATTACK2_ANIMATION_1 || animation == EntityNamelessGuardian.ATTACK2_ANIMATION_2 || animation == EntityNamelessGuardian.ATTACK2_ANIMATION_3;
    }

    @Override
    public void tick() {
        Animation animation = this.entity.getAnimation();
        LivingEntity target = this.entity.getTarget();
        int tick = this.entity.getAnimationTick();
        float baseDamageMultiplier = isPowered ? 1.2F : 2.0F;
        if (animation == EntityNamelessGuardian.ATTACK2_ANIMATION_1) {
            int lookAtFrame = isPowered ? 12 : 8;
            if (tick < lookAtFrame && target != null) {
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 7) {
                if (this.entity.targetDistance > 1.8 || entity.getTarget() == null)
                    this.entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 1.2, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 1.2));
            } else if (tick == 12) {
                List<LivingEntity> entities = this.entity.getNearByLivingEntities(range);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= (attackArc + 70) / 2F && entityRelativeAngle >= -(attackArc - 20) / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, true, true);
                        hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().add(0, 0.5, 0));
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    }
                }
            } else if (tick == 17 /*&& entity.checkCanAttackRange(2.5F, range)*/) {
                this.entity.playAnimation(EntityNamelessGuardian.ATTACK2_ANIMATION_2);
            }
        } else if (animation == EntityNamelessGuardian.ATTACK2_ANIMATION_2) {
            tick = this.entity.getAnimationTick();
            int lookAtFrame = isPowered ? 12 : 8;
            if (tick < lookAtFrame && target != null) {
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 6) {
                if (this.entity.targetDistance > 1.8 || entity.getTarget() == null)
                    this.entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 1.5, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 1.5));
            } else if (tick == 10) {
                List<LivingEntity> entities = this.entity.getNearByLivingEntities(range);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= attackArc / 2F && entityRelativeAngle >= -attackArc - 20 / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, true, true);
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        ModEntityUtils.forceKnockBack(hitEntity, 0.35F, ratioX, ratioZ, 1.0F, false);
                    }
                }
            }
        } else if (animation == EntityNamelessGuardian.ATTACK2_ANIMATION_3) {

        }
    }

}
