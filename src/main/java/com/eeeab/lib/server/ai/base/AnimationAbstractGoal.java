package com.eeeab.lib.server.ai.base;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.lib.server.animation.EMAnimatedEntity;
import com.eeeab.lib.server.animation.EMAnimation;
import com.eeeab.lib.server.handler.EMAnimationHandler;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class AnimationAbstractGoal<T extends EEEABMobLibrary & EMAnimatedEntity> extends Goal {
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
        return this.test(this.entity.getEMAnimation());
    }

    @Override
    public void start() {
        this.entity.hurtInterruptsAnimation = this.canInterruptsAnimation;
    }

    @Override
    public boolean canContinueToUse() {
        return this.test(this.entity.getEMAnimation()) && this.entity.getEMAnimationTick() < this.entity.getEMAnimation().getDuration();
    }

    @Override
    public void stop() {
        if (this.test(this.entity.getEMAnimation())) {
            EMAnimationHandler.INSTANCE.sendEMAnimationMessage(this.entity, EMAnimatedEntity.NO_EMANIMATION);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    //获取当前动画与传入动画进行对比
    protected abstract boolean test(EMAnimation entity);
}
