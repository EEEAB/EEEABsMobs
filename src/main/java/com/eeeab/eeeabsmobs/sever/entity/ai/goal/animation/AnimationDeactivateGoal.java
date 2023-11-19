package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;

public class AnimationDeactivateGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends AnimationCommonGoal<T> {
    public AnimationDeactivateGoal(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void stop() {
        super.stop();
        entity.active = false;
    }
}
