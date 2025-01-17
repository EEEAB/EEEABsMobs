package com.eeeab.animate.server.handler;

import com.eeeab.animate.server.message.StopAnimationMessage;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.event.AnimationEvent;
import com.eeeab.animate.server.message.AnimationMessage;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public enum EMAnimationHandler {
    INSTANCE;

    public <T extends Entity & EMAnimatedEntity> void sendEMAnimationMessage(T entity, @NotNull(value = "animation cannot be null", exception = IllegalArgumentException.class) Animation animation) {
        if (!entity.level.isClientSide) {
            entity.setAnimation(animation);
            entity.setAnimationTick(0);
            EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new AnimationMessage(entity.getId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
        }
    }

    public <T extends Entity & EMAnimatedEntity> void sendEMAnimationMessage(T entity, boolean onlyStopSuperposition) {
        if (!entity.level.isClientSide) {
            if (entity.getAnimations() == null || entity.getAnimations().length == 0) {
                System.out.println(entity.getName().getString() + " animations cannot be null");
                return;
            }
            EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new StopAnimationMessage(entity.getId(), onlyStopSuperposition));
        }
    }

    public <T extends Entity & EMAnimatedEntity> void updateAnimations(T entity) {
        Animation animation = entity.getAnimation();
        if (animation == null) {
            entity.setAnimation(EMAnimatedEntity.NO_ANIMATION);
        } else {
            if (animation != EMAnimatedEntity.NO_ANIMATION) {
                if (entity.getAnimationTick() == 0) {
                    AnimationEvent<T> event = new AnimationEvent.Start<>(entity, animation);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        this.sendEMAnimationMessage(entity, event.getAnimation());
                    }
                }
                if (entity.getAnimationTick() < animation.getDuration()) {
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                    MinecraftForge.EVENT_BUS.post(new AnimationEvent.Tick<>(entity, animation, entity.getAnimationTick()));
                }
                if (entity.getAnimationTick() == animation.getDuration()) {
                    animation.stop();
                    entity.setAnimation(EMAnimatedEntity.NO_ANIMATION);
                    entity.setAnimationTick(0);
                }
            }
        }
    }
}