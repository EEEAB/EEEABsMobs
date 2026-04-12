package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Consumer;

public class AnimationAreaMelee<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationMelee<T> {
    private final float leftAttackArc;
    private final float rightAttackArc;
    private final float attackHeight;
    private final boolean faceTarget;
    private Consumer<LivingEntity> consumer = null;

    public AnimationAreaMelee(T entity, Animation animation, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack, float attackArc, float attackHeight, boolean faceTarget) {
        this(entity, animation, damageKeyframes, attackDistance, applyDamage, applyKnockBack, attackArc, attackArc, attackHeight, faceTarget);
    }

    public AnimationAreaMelee(T entity, Animation animation, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack, float leftAttackArc, float rightAttackArc, float attackHeight, boolean faceTarget) {
        this(entity, animation, damageKeyframes, attackDistance, applyDamage, applyKnockBack, leftAttackArc, rightAttackArc, attackHeight, faceTarget, false);
    }

    public AnimationAreaMelee(T entity, Animation animation, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack, float leftAttackArc, float rightAttackArc, float attackHeight, boolean faceTarget, boolean canDisableShield) {
        super(entity, animation, damageKeyframes, attackDistance, applyDamage, applyKnockBack);
        this.canDisableShield = canDisableShield;
        this.rightAttackArc = rightAttackArc;
        this.leftAttackArc = leftAttackArc;
        this.attackHeight = attackHeight;
        this.faceTarget = faceTarget;
    }

    public AnimationAreaMelee<T> setCustomHitMethod(Consumer<LivingEntity> entityConsumer) {
        this.consumer = entityConsumer;
        return this;
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
                    if (entityHitDistance <= attackDistance && (entityRelativeAngle <= rightAttackArc / 2 && entityRelativeAngle >= -leftAttackArc / 2) || (entityRelativeAngle >= 360 - leftAttackArc / 2 || entityRelativeAngle <= -360 + rightAttackArc / 2)) {
                        if (this.consumer != null) {
                            this.consumer.accept(entityHit);
                        } else {
                            entity.doHurtTarget(entityHit, damageMultiplier, knockBackMultiplier,canDisableShield);
                        }
                        onAttack(entity, entityHit, damageMultiplier, knockBackMultiplier);
                    }
                }
            }
        }
    }
}
