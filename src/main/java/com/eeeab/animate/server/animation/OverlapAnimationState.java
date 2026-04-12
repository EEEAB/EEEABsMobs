package com.eeeab.animate.server.animation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

public class OverlapAnimationState extends AnimationState {
    private final int duration;

    public OverlapAnimationState(int duration) {
        this.duration = duration;
    }

    public OverlapAnimationState(Animation animation) {
        this.duration = animation.getDuration();
    }

    @Override
    public void updateTime(float ageInTicks, float speed) {
        super.updateTime(ageInTicks, speed);
        if (this.isStarted()) {
            int accumulateTick = Mth.floor(this.getAccumulatedTime() / 1000F * 20F);
            if (accumulateTick >= this.duration) {
                this.stop();
            }
        }
    }
}
