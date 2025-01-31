package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.ai.AnimationAI;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

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
        entity.anchorToGround();
        LivingEntity target = this.entity.getTarget();
        int tick = this.entity.getAnimationTick();
        if (animation == entity.attackAnimation4) {
            if (tick < 10 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 9) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, this.entity.getVoicePitch());
            } else if (tick == 11) {
                pursuit(1.7F);
            } else if (tick == 13) {
                entity.rangeAttack(range, range, range, range, attackArc - 80F, attackArc + 40F, hitEntity -> {
                    entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, isPowered ? 1.0F : 0.8F, true, true, true);
                    hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().add(0, 0.4, 0));
                    entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                });
            } else if (tick == 20 && entity.checkCanAttackRange(2.0, range) && canToggleAnimation(80)) {
                this.entity.playAnimation(entity.attackAnimation5);
            }
        } else if (animation == entity.attackAnimation5) {
            tick = this.entity.getAnimationTick();
            if (tick < 10 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 9) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.05f, this.entity.getVoicePitch() + 0.15f);
                pursuit(1.5F);
            } else if (tick == 13) {
                double d0 = range + 0.5;
                entity.rangeAttack(d0, d0, d0, range, attackArc + 20F, attackArc + 20, hitEntity -> {
                    entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, isPowered ? 1.0F : 0.8F, true, true, true);
                    entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 0.5F, ratioX, ratioZ, !isPowered);
                });
            } else if (tick == 20 && entity.checkCanAttackRange(1.5, range) && canToggleAnimation(70)) {
                this.entity.playAnimation(entity.attackAnimation6);
            }
        } else if (animation == entity.attackAnimation6) {
            tick = this.entity.getAnimationTick();
            if (tick < 9 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 8) {
                pursuit(1.6F);
            } else if (tick == 9) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.2f, this.entity.getVoicePitch() + 0.15f);
            } else if (tick == 12) {
                for (int i = 0; i < 6; i++) {
                    entity.shockAttack(entity.damageSources().mobAttack(entity), i, -0.5F, 0.3F, 2F, 0.025F, 0.5F, (isPowered ? 1.0F : 0.8F), false, true, true);
                }
            } else if (tick == 13) {
                entity.rangeAttack(5.2, 4.6, 5.2, 5.2, 30F, 30F, hitEntity -> entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, isPowered ? 1.2F : 1.0F, true, true, true));
            } else if (tick == 14) {
                entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 2F, 0.2F);
            } else if (tick == 15) {
                entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + entity.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.125F, 5, 10);
            }
        }
    }

    private boolean canToggleAnimation(int healthPercentage) {
        return (!isPowered && ((entity.getHealthPercentage() >= healthPercentage && entity.getRandom().nextFloat() < 0.4F) ||
                ((entity.getHealthPercentage() < healthPercentage || !entity.isFirstMadness()) && entity.getRandom().nextFloat() < 0.6F))) ||
                (isPowered && entity.getRandom().nextFloat() < 0.9F);
    }

    private void pursuit(float scale) {
        LivingEntity target = entity.getTarget();
        float targetDistance = entity.targetDistance;
        if (entity.getTarget() != null && targetDistance < entity.getBbWidth() * 2 && targetDistance > 0) {
            scale = Math.max(--targetDistance - entity.getBbWidth() / 2, scale);
        }
        if (target == null || (targetDistance > 2F && Math.abs(target.getY() - entity.getY()) <= 4D)) {
            double radians = Math.toRadians(entity.getYRot() + 90);
            entity.move(MoverType.SELF, new Vec3(Math.cos(radians), 0, Math.sin(radians)).scale(scale));
        }
    }
}
