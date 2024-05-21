package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;

public class AnimationDie<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationDie(T entity) {
        super(entity, entity::getDeathAnimation, false);
    }
}
