package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ProjectileWeaponItem;

import java.util.function.Supplier;

public class AnimationRange<T extends EEEABMobLibrary & EMAnimatedEntity & RangedAttackMob> extends AnimationSimpleAI<T> {
    private final int attackFrame;
    private final SoundEvent attackSound;

    public AnimationRange(T entity, Supplier<Animation> animationSupplier, int attackFrame, SoundEvent attackSound) {
        this(entity, animationSupplier, attackFrame, attackSound, true, false);
    }

    public AnimationRange(T entity, Supplier<Animation> animationSupplier, int attackFrame, SoundEvent attackSound, boolean canStopGoal, boolean hurtInterruptsAnimation) {
        super(entity, animationSupplier, canStopGoal, hurtInterruptsAnimation);
        this.attackFrame = attackFrame;
        this.attackSound = attackSound;
    }

    @Override
    public void start() {
        super.start();
        entity.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.entity, item -> item instanceof ProjectileWeaponItem));
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

