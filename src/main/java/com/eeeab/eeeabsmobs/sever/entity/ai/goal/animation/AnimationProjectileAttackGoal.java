package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class AnimationProjectileAttackGoal<T extends EEEABMobLibrary & IAnimatedEntity & RangedAttackMob> extends AnimationCommonGoal<T> {
    private final int attackFrame;
    private final SoundEvent attackSound;

    public AnimationProjectileAttackGoal(T entity, Animation animation, int attackFrame, SoundEvent attackSound) {
        this(entity, animation, attackFrame, attackSound, true, false);

    }

    public AnimationProjectileAttackGoal(T entity, Animation animation, int attackFrame, SoundEvent attackSound, boolean canStopGoal, boolean hurtInterruptsAnimation) {
        super(entity, animation, canStopGoal, hurtInterruptsAnimation);
        this.attackFrame = attackFrame;
        this.attackSound = attackSound;
    }

    @Override
    public void stop() {
        super.stop();
        entity.stopUsingItem();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity entityTarget = entity.getTarget();
        if (entityTarget != null) {
            if (entity.getAnimationTick() < attackFrame) {
                entity.lookAt(entityTarget, 30F, 30F);
            }
            if (entity.getAnimationTick() == attackFrame) {
                entity.performRangedAttack(entityTarget, 0);
                if (attackSound != null) {
                    entity.playSound(attackSound, 1.0F, 1.0F);
                }
                entity.stopUsingItem();
            }
        }
    }
}

