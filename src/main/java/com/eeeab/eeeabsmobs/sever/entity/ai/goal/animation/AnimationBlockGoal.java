package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;

public class AnimationBlockGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends AnimationCommonGoal<T> {
    public AnimationBlockGoal(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void tick() {
        super.tick();
        if (entity != null && entity.blockEntity != null) {
            entity.lookAt(entity.blockEntity, 90F, 90F);
            entity.getLookControl().setLookAt(entity.blockEntity, 200F, 30F);
        }
    }
}
