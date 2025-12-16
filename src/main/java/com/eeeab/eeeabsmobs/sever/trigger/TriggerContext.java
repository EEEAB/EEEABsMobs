package com.eeeab.eeeabsmobs.sever.trigger;

import com.eeeab.animate.server.animation.Animation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class TriggerContext {
    private final LivingEntity entity;
    private final boolean firstEncounter;
    private final double followRange;
    private Animation animation;

    public TriggerContext(LivingEntity entity, boolean firstEncounter) {
        this.entity = entity;
        this.firstEncounter = firstEncounter;
        this.followRange = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
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

    public double getFollowRange() {
        return followRange;
    }

    public Animation getAnimation() {
        return animation;
    }

    public float getHealthRatio() {
        return entity.getHealth() / entity.getMaxHealth();
    }
}