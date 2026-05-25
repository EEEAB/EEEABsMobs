package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.eeeabsmobs.server.entity.EEEABMobLibrary;

public class AnimationDie<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationDie(T entity) {
        super(entity, entity.getDeathAnimation(), false);
    }
}
