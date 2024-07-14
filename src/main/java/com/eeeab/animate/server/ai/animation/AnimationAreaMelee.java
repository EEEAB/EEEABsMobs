package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Supplier;

public class AnimationAreaMelee<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationMelee<T> {
    private final float attackArc;
    private final float attackHeight;
    private final boolean faceTarget;

    public AnimationAreaMelee(T entity, Supplier<Animation> animationSupplier, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack, float attackArc, float attackHeight, boolean faceTarget) {
        super(entity, animationSupplier, damageKeyframes, attackDistance, applyDamage, applyKnockBack);
        this.attackArc = attackArc;
        this.attackHeight = attackHeight;
        this.faceTarget = faceTarget;
    }

    @Override
    public void tick() {
        if (faceTarget && entity.getAnimationTick() < damageKeyframes && attackTarget != null) {
            entity.lookAt(attackTarget, 30F, 30F);
        } else {
            if (entity.getAnimationTick() == damageKeyframes) {
                List<LivingEntity> entitiesHit = entity.getNearByLivingEntities(attackDistance, attackHeight, attackDistance, attackDistance);
                for (LivingEntity entityHit : entitiesHit) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit);
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                    if (entityHitDistance <= attackArc && (entityRelativeAngle <= attackArc / 2 && entityRelativeAngle >= -attackArc / 2) || (entityRelativeAngle >= 360 - attackArc / 2 || entityRelativeAngle <= -360 + attackArc / 2)) {
                        entity.doHurtTarget(entityHit, damageMultiplier, knockBackMultiplier);
                        onAttack(entity, entityHit, damageMultiplier, knockBackMultiplier);
                    }
                }
            }
            entity.setYRot(entity.yRotO);
        }
    }
}
