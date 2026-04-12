package com.eeeab.eeeabsmobs.sever.entity.trigger;

import com.eeeab.animate.server.animation.Animation;
import net.minecraft.world.entity.LivingEntity;

public class TriggerContext {
    private final LivingEntity entity;
    private final boolean firstEncounter;
    private Animation animation;

    public TriggerContext(LivingEntity entity, boolean firstEncounter) {
        this.entity = entity;
        this.firstEncounter = firstEncounter;
    }

    public TriggerContext(LivingEntity entity, Animation animation) {
        this(entity, false);
        this.animation = animation;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public boolean isFirstEncounter() {
        return firstEncounter;
    }

    public Animation getAnimation() {
        return animation;
    }

    public float getHealthRatio() {
        return entity.getHealth() / entity.getMaxHealth();
    }
}