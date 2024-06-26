package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.ai.AnimationAI;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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
        entity.setDeltaMovement(0, entity.onGround() ? 0 : entity.getDeltaMovement().y, 0);
        if (entity.getAnimation() == entity.attackAnimation1) {
            int tick = entity.getAnimationTick();
            int lookAtFrame = isPowered ? 15 : 8;
            float baseDamageMultiplier = isPowered ? 1.0F : 0.8F;
            if (tick < lookAtFrame && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 10) {
                pursuit(4F, 1.7F);
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.05f, entity.getVoicePitch() + 0.15f);
            } else if (tick == 12) {
                List<LivingEntity> entities = entity.getNearByLivingEntities(range - 0.5F, 4F, range - 0.5F, range - 0.5F);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range - 0.5F && (entityRelativeAngle <= 10F && entityRelativeAngle >= -(attackArc + 20) / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, baseDamageMultiplier, true, true, true);
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                        ModEntityUtils.forceKnockBack(hitEntity, 0.35F, ratioX, ratioZ, 1.0F, false);
                    }
                }
            } else if (tick == 20 && entity.checkCanAttackRange(2.0, range) && canToggleAnimation(90)) {
                entity.playAnimation(entity.attackAnimation2);
            }

        } else if (entity.getAnimation() == entity.attackAnimation2) {
            int tick = entity.getAnimationTick();
            int lookAtFrame = isPowered ? 15 : 9;
            float attackArc1 = attackArc + 40;
            float baseDamageMultiplier = isPowered ? 1.0F : 0.8F;
            if (tick < lookAtFrame && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 12) {
                pursuit(4F, 1.7F);
            } else if (tick == 11) {
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, entity.getVoicePitch());
            } else if (tick == 15) {
                List<LivingEntity> entities = entity.getNearByLivingEntities(range - 0.1F, 4F, range - 0.1F, range - 0.1F);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range - 0.1F && (entityRelativeAngle <= attackArc1 / 2F && entityRelativeAngle >= -attackArc1 / 2F) || (entityRelativeAngle >= 360 - attackArc1 / 2F || entityRelativeAngle <= -360 + attackArc1 / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.025F, 1.0F, baseDamageMultiplier, true, true, true);
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        ModEntityUtils.forceKnockBack(hitEntity, 0.35F, ratioX, ratioZ, 1.0F, false);
                    }
                }
            } else if (tick == 25) {
                if (entity.checkCanAttackRange(2.5, range) && canToggleAnimation(80)) {
                    entity.playAnimation(entity.attackAnimation3);
                }
            }
        } else if (entity.getAnimation() == entity.attackAnimation3) {
            int tick = entity.getAnimationTick();
            float baseDamageMultiplier = isPowered ? 1.2F : 1.0F;
            if (tick < 15 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 12) {
                pursuit(5F, 2.5F);
            } else if (tick == 11) {
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.2f, entity.getVoicePitch() - 0.2f);
            } else if (tick == 15) {
                List<LivingEntity> entities = entity.getNearByLivingEntities(range + 0.1F, 5.0F, range + 0.1F, range + 0.1F);
                for (LivingEntity hitEntity : entities) {
                    double duration = 1.5;
                    if (Difficulty.HARD.equals(entity.level().getDifficulty())) duration = 2.5;
                    if (hitEntity instanceof Player player && !player.isCreative() && !player.isBlocking()) {
                        player.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), (int) (duration * 20), 0, false, false, true));
                    } else if (!(hitEntity instanceof Player) && !hitEntity.isBlocking()) {
                        hitEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), (int) (duration * 20), 0, false, false, true));
                    }
                    entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, true, true, true);
                    entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                    ModEntityUtils.forceKnockBack(hitEntity, 1.0F, ratioX, ratioZ, 0.01F, false);
                }
                entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + entity.getRandom().nextFloat() * 0.1F);
            } else if (tick > 15 && tick < 22) {
                entity.shockAttack(entity.damageSources().mobAttack(entity), tick - 13, 1F, 1F, 1.5F, 0.025F, 0.5F, (isPowered ? 1.0F : 0.8F), true, false, false);
            }
        }
    }

    private boolean canToggleAnimation(int healthPercentage) {
        return (!isPowered && ((entity.getHealthPercentage() >= healthPercentage && entity.getRandom().nextFloat() < 0.4F) ||
                ((entity.getHealthPercentage() < healthPercentage || !entity.isFirstMadness()) && entity.getRandom().nextFloat() < 0.6F))) ||
                (isPowered && entity.getRandom().nextFloat() < 0.9F);
    }

    private void pursuit(float pursuitDistance, float moveMultiplier) {
        float targetDistance = entity.targetDistance;
        if (entity.getTarget() != null && targetDistance < pursuitDistance && targetDistance > 0) {
            moveMultiplier = --targetDistance;
        }
        targetDistance = entity.targetDistance;
        if (entity.getTarget() == null || targetDistance > 1.8F)
            entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * moveMultiplier, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * moveMultiplier));
    }
}
