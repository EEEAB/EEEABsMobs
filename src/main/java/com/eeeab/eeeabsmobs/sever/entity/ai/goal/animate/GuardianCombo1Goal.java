package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class GuardianCombo1Goal extends AnimationAI<EntityNamelessGuardian> {
    private final float range;
    private final float attackArc;
    private boolean isPowered;

    public GuardianCombo1Goal(EntityNamelessGuardian entity, float range, float attackArc) {
        super(entity);
        this.range = range;
        this.attackArc = attackArc;
    }

    @Override
    public void start() {
        super.start();
        isPowered = entity.isPowered();
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.attackAnimation1 || animation == entity.attackAnimation2 || animation == entity.attackAnimation3;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        entity.anchorToGround();
        if (entity.getAnimation() == entity.attackAnimation1) {
            int tick = entity.getAnimationTick();
            float baseDamageMultiplier = isPowered ? 1.0F : 0.8F;
            if (tick < 9 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 9) {
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.05f, entity.getVoicePitch() + 0.15f);
            } else if (tick == 10) {
                pursuit(1.5F);
            } else if (tick == 11) {
                entity.rangeAttack(range, 5, range, range + 0.5, attackArc + 20F, 20F, hitEntity -> {
                    entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, baseDamageMultiplier, true, true, true);
                    double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                    entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 0.6F, ratioX, ratioZ, !isPowered);
                });
            } else if (tick == 20 && entity.checkCanAttackRange(1.5, range) && canToggleAnimation(90)) {
                entity.playAnimation(entity.attackAnimation2);
            }

        } else if (entity.getAnimation() == entity.attackAnimation2) {
            int tick = entity.getAnimationTick();
            float baseDamageMultiplier = isPowered ? 1.0F : 0.8F;
            if (tick < 11 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 11) {
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, entity.getVoicePitch());
            } else if (tick == 13) {
                pursuit(1.6F);
                entity.rangeAttack(range, 5, range, range + 0.25, attackArc + 40F, attackArc + 40F, hitEntity -> {
                    entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, baseDamageMultiplier, true, true, true);
                    entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 0.5F, ratioX, ratioZ, !isPowered);
                });
            } else if (tick == 25) {
                if (entity.checkCanAttackRange(1.5, range) && canToggleAnimation(80)) {
                    entity.playAnimation(entity.attackAnimation3);
                }
            }
        } else if (entity.getAnimation() == entity.attackAnimation3) {
            int tick = entity.getAnimationTick();
            float baseDamageMultiplier = isPowered ? 1.2F : 1.0F;
            if (tick < 9 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 11) {
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.2f, entity.getVoicePitch() - 0.2f);
                pursuit(2F);
            } else if (tick == 14) {
                entity.rangeAttack(range + 0.1F, 6F, range + 0.1F, range + 0.5F, hitEntity -> {
                    double duration = 1.5;
                    if (Difficulty.HARD.equals(entity.level().getDifficulty())) duration = 2.5;
                    entity.stun(null, hitEntity, (int) (duration * 20), entity.isChallengeMode());
                    entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, true, true, true);
                    entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 1.8F, ratioX, ratioZ, false);
                });
                entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + entity.getRandom().nextFloat() * 0.1F);
            } else if (tick > 15 && tick < 22) {
                entity.shockAttack(entity.damageSources().mobAttack(entity), tick - 13, 1F, 1F, 1.5F, 0.5F, (isPowered ? 1.0F : 0.8F), true, false, false);
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
            scale = Math.max(targetDistance - entity.getBbWidth() / 2, scale);
        }
        if (target == null || (targetDistance > 2F && Math.abs(target.getY() - entity.getY()) <= 2D)) {
            double radians = Math.toRadians(entity.getYRot() + 90);
            entity.move(MoverType.SELF, new Vec3(Math.cos(radians), 0, Math.sin(radians)).scale(scale));
        }
    }
}
