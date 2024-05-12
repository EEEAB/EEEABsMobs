package com.eeeab.lib.server.ai.base;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.lib.server.animation.EMAnimatedEntity;
import com.eeeab.lib.server.animation.EMAnimation;

import java.util.EnumSet;
import java.util.function.Supplier;

public class AnimationCommonGoal<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationAbstractGoal<T> {
    protected final Supplier<EMAnimation> animationSupplier;

    public AnimationCommonGoal(T entity, Supplier<EMAnimation> animationSupplier) {
        super(entity);
        this.animationSupplier = animationSupplier;
    }

    public AnimationCommonGoal(T entity, Supplier<EMAnimation> animationSupplier, boolean canStopGoal) {
        super(entity, canStopGoal);
        this.animationSupplier = animationSupplier;
    }

    public AnimationCommonGoal(T entity, Supplier<EMAnimation> animationSupplier, boolean canStopGoal, boolean hurtInterruptsAnimation) {
        super(entity, canStopGoal, hurtInterruptsAnimation);
        this.animationSupplier = animationSupplier;
    }

    //指定Flag构造器
    public AnimationCommonGoal(T entity, Supplier<EMAnimation> animationSupplier, EnumSet<Flag> interruptFlagTypes) {
        super(entity);
        this.animationSupplier = animationSupplier;
        setFlags(interruptFlagTypes);
    }

    //获取实体当前动画与传入动画进行对比
    @Override
    protected boolean test(EMAnimation animation) {
        return animation == animationSupplier.get();
    }
}
