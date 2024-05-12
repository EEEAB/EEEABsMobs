package com.eeeab.lib.server.event;

import com.eeeab.lib.server.animation.EMAnimatedEntity;
import com.eeeab.lib.server.animation.EMAnimation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class EMAnimationEvent<T extends Entity & EMAnimatedEntity> extends Event {
    protected EMAnimation animation;
    private final T entity;

    EMAnimationEvent(T entity, EMAnimation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    public T getEntity() {
        return this.entity;
    }

    public EMAnimation getAnimation() {
        return this.animation;
    }

    @Cancelable
    public static class Start<T extends Entity & EMAnimatedEntity> extends EMAnimationEvent<T> {
        public Start(T entity, EMAnimation animation) {
            super(entity, animation);
        }

        public void setAnimation(EMAnimation animation) {
            this.animation = animation;
        }
    }

    public static class Tick<T extends Entity & EMAnimatedEntity> extends EMAnimationEvent<T> {
        protected int tick;

        public Tick(T entity, EMAnimation animation, int tick) {
            super(entity, animation);
            this.tick = tick;
        }

        public int getTick() {
            return this.tick;
        }
    }
}
