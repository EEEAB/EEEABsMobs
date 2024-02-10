package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class AnimationAttackGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends AnimationCommonGoal<T> {
    protected LivingEntity attackTarget;
    protected int damageKeyframes;
    protected float attackDistance;
    protected float damageMultiplier;
    protected float knockBackMultiplier;

    public AnimationAttackGoal(T entity, Animation animation, int damageKeyframes, float attackDistance, float applyDamage, float applyKnockBack) {
        this(entity, animation, false, damageKeyframes, attackDistance, applyDamage, applyKnockBack);
    }

    public AnimationAttackGoal(T entity, Animation animation, boolean hurtInterruptsAnimation, int damageKeyframes, float attackDistance, float damageMultiplier, float knockBackMultiplier) {
        super(entity, animation, false, hurtInterruptsAnimation);
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
