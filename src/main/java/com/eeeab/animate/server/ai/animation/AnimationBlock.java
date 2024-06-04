package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;

import java.util.function.Supplier;

public class AnimationBlock<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationBlock(T entity, Supplier<Animation> animationSupplier) {
        super(entity, animationSupplier);
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
