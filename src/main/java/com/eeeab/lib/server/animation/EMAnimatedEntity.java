package com.eeeab.lib.server.animation;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;

public interface EMAnimatedEntity extends IAnimatedEntity {
    EMAnimation NO_EMANIMATION = EMAnimation.create(0);

    int getEMAnimationTick();

    void setEMAnimationTick(int tick);

    EMAnimation getEMAnimation();

    void setEMAnimation(EMAnimation animation);

    EMAnimation[] getEMAnimations();
}
