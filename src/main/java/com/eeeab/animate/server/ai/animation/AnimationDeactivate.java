package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;

public class AnimationDeactivate<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationDeactivate(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void stop() {
        super.stop();
        entity.active = false;
    }
}
