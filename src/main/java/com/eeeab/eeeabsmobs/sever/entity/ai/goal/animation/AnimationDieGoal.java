package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;

public class AnimationDieGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends AnimationCommonGoal<T> {
    public AnimationDieGoal(T entity) {
        super(entity, entity.getDeathAnimation(), false);
    }

}
