package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;

public class AnimationHurt<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationHurt(T entity, boolean stopGoal) {
        super(entity, entity::getHurtAnimation, stopGoal);
    }
}
