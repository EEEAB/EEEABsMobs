package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.animate.server.animation.EMAnimatedEntity;

import java.util.EnumSet;
import java.util.function.Supplier;

public class AnimationSimpleAI<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationAI<T> {
    protected final Supplier<Animation> animationSupplier;

    public AnimationSimpleAI(T entity, Supplier<Animation> animationSupplier) {
        super(entity);
        this.animationSupplier = animationSupplier;
    }

    public AnimationSimpleAI(T entity, Supplier<Animation> animationSupplier, boolean canStopGoal) {
        super(entity, canStopGoal);
        this.animationSupplier = animationSupplier;
    }

    public AnimationSimpleAI(T entity, Supplier<Animation> animationSupplier, boolean canStopGoal, boolean hurtInterruptsAnimation) {
        super(entity, canStopGoal, hurtInterruptsAnimation);
        this.animationSupplier = animationSupplier;
    }

    //指定Flag构造器
    public AnimationSimpleAI(T entity, Supplier<Animation> animationSupplier, EnumSet<Flag> interruptFlagTypes) {
        super(entity);
        this.animationSupplier = animationSupplier;
        setFlags(interruptFlagTypes);
    }

    //获取实体当前动画与传入动画进行对比
    @Override
    protected boolean test(Animation animation) {
        return animation == animationSupplier.get();
    }
}
