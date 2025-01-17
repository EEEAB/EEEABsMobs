package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class AnimationAI<T extends EEEABMobLibrary & EMAnimatedEntity> extends Goal {
    protected final T entity;
    protected final boolean canInterruptsAnimation;

    protected AnimationAI(T entity) {
        this(entity, true, false);
    }

    protected AnimationAI(T entity, boolean canStopGoal) {
        this(entity, canStopGoal, false);
    }

    protected AnimationAI(T entity, boolean canStopGoal, boolean hurtInterruptsAnimation) {
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
            EMAnimationHandler.INSTANCE.sendEMAnimationMessage(this.entity, EMAnimatedEntity.NO_ANIMATION);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    //获取当前动画与传入动画进行对比
    protected abstract boolean test(Animation animation);
}
