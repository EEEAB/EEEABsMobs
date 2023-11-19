package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationAbstractGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian.ATTACK_ANIMATION_3;
import static com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian.ATTACK_ANIMATION_2;

public class GuardianComboGoal extends AnimationAbstractGoal<EntityNamelessGuardian> {
    private final float range;
    private final float attackArc;
    private boolean isPowered;

    public GuardianComboGoal(EntityNamelessGuardian entity, float range, float attackArc) {
        super(entity);
        this.range = range;
        this.attackArc = attackArc;
    }

    @Override
    public void start() {
        super.start();
        this.isPowered = entity.isPowered();
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == EntityNamelessGuardian.ATTACK_ANIMATION_1 || animation == ATTACK_ANIMATION_2 || animation == ATTACK_ANIMATION_3;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        entity.setDeltaMovement(0, entity.onGround() ? 0 : entity.getDeltaMovement().y, 0);
        if (entity.getAnimation() == EntityNamelessGuardian.ATTACK_ANIMATION_1) {
            int tick = entity.getAnimationTick();
            int lookAtFrame = isPowered ? 19 : 15;
            //如果是狂化状态,则追加攻击力的40%
            float baseDamageMultiplier = isPowered ? 1.4F : 1.0F;
            if (tick < lookAtFrame && target != null) {
                //entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 16) {
                if (entity.targetDistance > 1.8 || entity.getTarget() == null)
                    entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 1.5, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 1.5));
            } else if (tick == 19) {
                List<LivingEntity> entities = entity.getNearByLivingEntities(range, 4F, range, range);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= 10F && entityRelativeAngle >= -(attackArc + 20) / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, true, true);
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                        ModEntityUtils.forceKnockBack(hitEntity, 0.35F, ratioX, ratioZ, 1.0F, false);
                    }
                }
            } else if (tick == 24 && entity.checkCanAttackRange(2.5, range) &&
                    ((!isPowered && entity.getHealthPercentage() >= 90 && entity.getRandom().nextFloat() < 0.3F) ||
                            (!isPowered && entity.getHealthPercentage() < 90 && entity.getRandom().nextFloat() < 0.6F) ||
                            (isPowered && entity.getRandom().nextFloat() < 0.9F))) {
                entity.playAnimation(ATTACK_ANIMATION_2);
            }

        } else if (entity.getAnimation() == ATTACK_ANIMATION_2) {
            int tick = entity.getAnimationTick();
            int lookAtFrame = isPowered ? 15 : 9;
            float attackArc1 = attackArc + 40;
            /* 如果是狂化状态,则追加基础攻击力的50% */
            float baseDamageMultiplier = isPowered ? 1.5F : 1F;
            if (tick < lookAtFrame && target != null) {
                //entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 12) {
                if (entity.targetDistance > 1.8 || entity.getTarget() == null)
                    entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 1.7, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 1.7));
            } else if (tick == 15) {
                List<LivingEntity> entities = entity.getNearByLivingEntities(range + 0.5F, 4F, range + 0.5F, range + 0.5F);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range + 0.5F && (entityRelativeAngle <= attackArc1 / 2F && entityRelativeAngle >= -attackArc1 / 2F) || (entityRelativeAngle >= 360 - attackArc1 / 2F || entityRelativeAngle <= -360 + attackArc1 / 2F))) {
                        entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, true, true);
                        entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        ModEntityUtils.forceKnockBack(hitEntity, 0.35F, ratioX, ratioZ, 1.0F, false);
                    }
                }
            } else if (tick == 25) {
                if (entity.checkCanAttackRange(3, range) && ((!isPowered && entity.getHealthPercentage() > 80 && entity.getRandom().nextFloat() < 0.15F) ||
                        (!isPowered && entity.getHealthPercentage() <= 80 && entity.getRandom().nextFloat() < 0.6F) || (isPowered && entity.getRandom().nextFloat() < 0.9F))) {
                    entity.playAnimation(EntityNamelessGuardian.ATTACK_ANIMATION_3);
                }
            }
        } else if (entity.getAnimation() == ATTACK_ANIMATION_3) {
            int tick = entity.getAnimationTick();
            float baseDamageMultiplier = isPowered ? 1.5F : 1.2F;
            if (tick < 15 && target != null) {
                //entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 12) {
                if (entity.targetDistance > 1.8 || entity.getTarget() == null)
                    entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 2.5, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 2.5));
            } else if (tick == 15) {
                List<LivingEntity> entities = entity.getNearByLivingEntities(range + 0.4F, 5.0F, range + 0.4F, range + 0.4F);
                for (LivingEntity hitEntity : entities) {
                    entity.guardianHurtTarget(entity, hitEntity, 0.1F, 1.0F, baseDamageMultiplier, true, true);
                    entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                    double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                    ModEntityUtils.forceKnockBack(hitEntity, 1.0F, ratioX, ratioZ, 0.01F, false);
                }
            }
        }
    }
}
