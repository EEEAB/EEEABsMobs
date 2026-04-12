package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;

public class AnimationBlock<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationBlock(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.blockEntity = null;
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
