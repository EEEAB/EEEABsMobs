package com.eeeab.lib.server.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.lib.server.animation.EMAnimatedEntity;
import com.eeeab.lib.server.animation.EMAnimation;
import com.eeeab.lib.server.event.EMAnimationEvent;
import com.eeeab.lib.server.message.EMAnimationMessage;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

public enum EMAnimationHandler {
    INSTANCE;

    public <T extends Entity & EMAnimatedEntity> void sendEMAnimationMessage(T entity, EMAnimation animation) {
        if (entity.level().isClientSide) {
            return;
        }
        entity.setEMAnimation(animation);
        animation.start(entity.tickCount);
        EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new EMAnimationMessage(entity.getId(), ArrayUtils.indexOf(entity.getEMAnimations(), animation)));
    }

    public <T extends Entity & EMAnimatedEntity> void updateAnimations(T entity) {
        if (entity.getEMAnimation() == null) {
            entity.setEMAnimation(EMAnimatedEntity.NO_EMANIMATION);
        } else {
            if (entity.getEMAnimation() != EMAnimatedEntity.NO_EMANIMATION) {
                if (entity.getEMAnimationTick() == 0) {
                    EMAnimationEvent event = new EMAnimationEvent.Start<>(entity, entity.getEMAnimation());
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        this.sendEMAnimationMessage(entity, event.getAnimation());
                    }
                }
                if (entity.getEMAnimationTick() < entity.getEMAnimation().getDuration()) {
                    entity.setEMAnimationTick(entity.getEMAnimationTick() + 1);
                    MinecraftForge.EVENT_BUS.post(new EMAnimationEvent.Tick<>(entity, entity.getEMAnimation(), entity.getEMAnimationTick()));
                }
                if (entity.getEMAnimationTick() == entity.getEMAnimation().getDuration()) {
                    if (!entity.getEMAnimation().isLooping()) {
                        entity.setEMAnimation(EMAnimatedEntity.NO_EMANIMATION);
                    }
                    entity.setEMAnimationTick(0);
                }
            }
        }
    }

}