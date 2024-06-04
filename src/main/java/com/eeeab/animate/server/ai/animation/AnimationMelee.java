package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.function.Supplier;

public class AnimationMelee<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationSimpleAI<T> {
    protected LivingEntity attackTarget;
    protected int damageKeyframes;
    protected float attackDistance;
    protected float damageMultiplier;
    protected float knockBackMultiplier;

    public AnimationMelee(T entity, Supplier<Animation> animationSupplier, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack) {
        this(entity, animationSupplier, false, damageKeyframes, attackDistance, applyDamage, applyKnockBack);
    }

    public AnimationMelee(T entity, Supplier<Animation> animationSupplier, boolean hurtInterruptsAnimation, int damageKeyframes, float attackDistance, float damageMultiplier, float knockBackMultiplier) {
        super(entity, animationSupplier, false, hurtInterruptsAnimation);
        this.attackTarget = null;
        this.damageKeyframes = damageKeyframes;
        this.attackDistance = attackDistance;
        this.damageMultiplier = damageMultiplier;
        this.knockBackMultiplier = knockBackMultiplier;
        setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public void start() {
        super.start();
        attackTarget = entity.getTarget();
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getAnimationTick() < damageKeyframes && attackTarget != null) {
            entity.lookAt(attackTarget, 30F, 30F);
        } else {
            entity.setYRot(entity.yRotO);
        }
        if (entity.getAnimationTick() == damageKeyframes) {
            if (attackTarget != null && entity.targetDistance <= attackDistance) {
                entity.doHurtTarget(attackTarget, damageMultiplier, knockBackMultiplier);
                onAttack(entity, attackTarget, damageMultiplier, knockBackMultiplier);
            }
        }
    }

    protected void onAttack(T entity, LivingEntity attackTarget, float damageMultiplier, float knockBackMultiplier) {
    }
}
