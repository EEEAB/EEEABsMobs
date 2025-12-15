package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.AnimatedEntity;

import java.util.function.Supplier;

public class AnimationDeactivate<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationDeactivate(T entity, Supplier<Animation> animationSupplier) {
        super(entity, animationSupplier);
    }

    @Override
    public void stop() {
        super.stop();
        entity.active = false;
    }
}
