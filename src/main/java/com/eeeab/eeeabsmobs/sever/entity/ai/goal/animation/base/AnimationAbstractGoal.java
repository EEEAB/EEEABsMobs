package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base;

import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class AnimationAbstractGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends Goal {
    protected final T entity;
    protected final boolean canInterruptsAnimation;

    protected AnimationAbstractGoal(T entity) {
        this(entity, true, false);
    }

    protected AnimationAbstractGoal(T entity, boolean canStopGoal) {
        this(entity, canStopGoal, false);
    }

    protected AnimationAbstractGoal(T entity, boolean canStopGoal, boolean hurtInterruptsAnimation) {
        this.entity = entity;
        if (canStopGoal) this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.canInterruptsAnimation = hurtInterruptsAnimation;
    }

    @Override
    public boolean canUse() {
        return this.test(this.entity.getAnimation());
    }

    @Override
    public void start() {
        this.entity.hurtInterruptsAnimation = this.canInterruptsAnimation;
    }

    @Override
    public boolean canContinueToUse() {
        return this.test(this.entity.getAnimation()) && this.entity.getAnimationTick() < this.entity.getAnimation().getDuration();
    }

    @Override
    public void stop() {
        if (this.test(this.entity.getAnimation())) {
            //EEAnimationHandler.INSTANCE.sendEEAnimationSync(this.entity, EEGeckoAnimationEntity.NO_ANIMATION);
            AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    //获取当前动画与传入动画进行对比
    protected abstract boolean test(Animation animation);
}
