package com.eeeab.animate.server.animation.keyframe;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 关键帧管理器
 *
 * @author EEEAB
 */
public class KeyframeManager<T extends Entity & AnimatedEntity> {
    private final Map<Animation, Map<Integer, List<Keyframe<T>>>> tickHandlers = new HashMap<>();
    private final Map<Animation, List<RangeHandler<T>>> rangeHandlers = new HashMap<>();
    private final List<CondKeyframe<T>> conditionalHandlers = new CopyOnWriteArrayList<>();

    public KeyframeManegerBuilder<T> builder() {
        return new KeyframeManegerBuilder<>(this);
    }

    private void register(Animation animation, int tick, Keyframe<T> handler) {
        tickHandlers.computeIfAbsent(animation, k -> new HashMap<>())
                .computeIfAbsent(tick, k -> new ArrayList<>())
                .add(handler);
    }

    private void registerRange(Animation animation, int startTick, int endTick, int n, Keyframe<T> handler) {
        rangeHandlers.computeIfAbsent(animation, k -> new ArrayList<>())
                .add(new RangeHandler<>(startTick, endTick, n, handler));
    }

    private void registerConditional(CondKeyframe<T> handler) {
        conditionalHandlers.add(handler);
    }

    public void tick(T entity) {
        Animation animation = entity.getAnimation();
        if (AnimatedEntity.NO_ANIMATION == animation) return;
        int tick = entity.getAnimationTick();
        //单刻处理器
        Map<Integer, List<Keyframe<T>>> animationTickHandlers = tickHandlers.get(animation);
        if (animationTickHandlers != null) {
            List<Keyframe<T>> keyframes = animationTickHandlers.get(tick);
            if (keyframes != null) {
                for (Keyframe<T> keyframe : keyframes) {
                    keyframe.handle(entity, animation, tick);
                }
            }
        }
        //范围处理器
        List<RangeHandler<T>> ranges = rangeHandlers.get(animation);
        if (ranges != null) {
            for (RangeHandler<T> range : ranges) {
                if (range.matches(tick)) {
                    range.handler.handle(entity, animation, tick);
                }
            }
        }
        //条件处理器
        for (CondKeyframe<T> keyframe : conditionalHandlers) {
            if (keyframe.shouldHandle(entity, animation, tick)) {
                keyframe.handle(entity, animation, tick);
            }
        }
    }

    public void clear() {
        tickHandlers.clear();
        rangeHandlers.clear();
        conditionalHandlers.clear();
    }

    private record RangeHandler<T extends Entity & AnimatedEntity>(int startTick, int endTick, int n, Keyframe<T> handler) {
        public boolean matches(int tick) {
            return tick >= startTick && tick <= endTick && tick % n == 0;
        }
    }

    public static class KeyframeManegerBuilder<T extends Entity & AnimatedEntity> {
        private final KeyframeManager<T> manager;
        private Animation animation;

        public KeyframeManegerBuilder(KeyframeManager<T> manager) {
            this.manager = manager;
        }

        public KeyframeManegerBuilder<T> forAnimation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public KeyframeManegerBuilder<T> atTick(int tick, Keyframe<T> handler) {
            requireNonNull(animation);
            manager.register(animation, tick, handler);
            return this;
        }

        public KeyframeManegerBuilder<T> inRange(int startTick, int endTick, Keyframe<T> keyframe) {
            requireNonNull(animation);
            manager.registerRange(animation, startTick, endTick, 1, keyframe);
            return this;
        }

        public KeyframeManegerBuilder<T> everyTick(Keyframe<T> handler) {
            return everyNTick(1, handler);
        }

        public KeyframeManegerBuilder<T> everyNTick(int n, Keyframe<T> handler) {
            return everyNTick(0, Integer.MAX_VALUE, n, handler);
        }

        public KeyframeManegerBuilder<T> everyNTick(int startTick, int endTick, int n, Keyframe<T> handler) {
            requireNonNull(animation);
            manager.registerRange(animation, startTick, endTick, n, handler);
            return this;
        }

        public void conditional(CondKeyframe<T> handler) {
            manager.registerConditional(handler);
        }

        private static void requireNonNull(Animation animation) {
            if (animation == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }
    }
}