package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.List;

public class AnimationAreaAttackGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends AnimationAttackGoal<T> {
    private final float attackArc;
    private final float attackHeight;
    private final boolean faceTarget;

    public AnimationAreaAttackGoal(T entity, Animation animation, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack, float attackArc, float attackHeight) {
        this(entity, animation, damageKeyframes, attackDistance, applyDamage, applyKnockBack, attackArc, attackHeight, true);
    }

    public AnimationAreaAttackGoal(T entity, Animation animation, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack, float attackArc, float attackHeight, boolean faceTarget) {
        super(entity, animation, damageKeyframes, attackDistance, applyDamage, applyKnockBack);
        this.attackArc = attackArc;
        this.attackHeight = attackHeight;
        this.faceTarget = faceTarget;
        if (faceTarget)this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public void tick() {
        if (faceTarget && entity.getAnimationTick() < damageKeyframes && attackTarget != null) {
            entity.lookAt(attackTarget, 30F, 30F);
        } else if (entity.getAnimationTick() == damageKeyframes) {
            hitEntity();
        }
    }

    public void hitEntity() {
        List<LivingEntity> entitiesHit = entity.getNearByLivingEntities(attackDistance, attackHeight, attackDistance, attackDistance);
        boolean hit = false;
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - entity.getZ(), entityHit.getX() - entity.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = entity.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
            if (entityHitDistance <= attackArc && (entityRelativeAngle <= attackArc / 2 && entityRelativeAngle >= -attackArc / 2) || (entityRelativeAngle >= 360 - attackArc / 2 || entityRelativeAngle <= -360 + attackArc / 2)) {
                entity.doHurtTarget(entityHit, damageMultiplier, knockBackMultiplier);
                onAttack(entity,entityHit, damageMultiplier, knockBackMultiplier);
                hit = true;
            }
        }
    }
}
