package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base;

import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;

import java.util.EnumSet;

public class AnimationCommonGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends AnimationAbstractGoal<T> {
    protected final Animation animation;

    public AnimationCommonGoal(T entity, Animation animation) {
        super(entity);
        this.animation = animation;
    }

    public AnimationCommonGoal(T entity, Animation animation, boolean canStopGoal) {
        super(entity, canStopGoal);
        this.animation = animation;
    }

    public AnimationCommonGoal(T entity, Animation animation, boolean canStopGoal, boolean hurtInterruptsAnimation) {
        super(entity, canStopGoal, hurtInterruptsAnimation);
        this.animation = animation;
    }

    //指定Flag构造器
    public AnimationCommonGoal(T entity, Animation animation, EnumSet<Flag> interruptFlagTypes) {
        super(entity);
        this.animation = animation;
        setFlags(interruptFlagTypes);
    }

    //获取实体当前动画与传入动画进行对比
    @Override
    protected boolean test(Animation animation) {
        return animation == this.animation;
    }
}
