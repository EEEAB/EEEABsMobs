package com.eeeab.animate.server.animation.release;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.release.cooldown.CooldownGenerator;
import com.eeeab.animate.server.animation.release.cooldown.CooldownManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 动画释放规则
 *
 * @author EEEAB
 */
public class AnimationRule<T extends Mob & AnimatedEntity> {
    private final Animation animation;
    private final AnimationCondition<T> condition;
    private final List<WeightedRule<T>> nextRules;
    private final int priority;
    private final int triggerTick;
    private final boolean onlyCombo;
    @Nullable
    private final Consumer<T> onSuccess;
    @Nullable
    private final CooldownGenerator cooldownGenerator;

    private AnimationRule(Builder<T> builder) {
        this.animation = builder.animation;
        this.condition = builder.condition;
        this.onSuccess = builder.onSuccess;
        this.cooldownGenerator = builder.cooldownGenerator;
        this.priority = builder.priority;
        this.nextRules = builder.nextRules;
        this.triggerTick = builder.triggerTick;
        this.onlyCombo = builder.onlyCombo;
    }

    public AnimationCondition<T> getCondition() {
        return condition;
    }

    public Animation getAnimation() {
        return animation;
    }

    public int getPriority() {
        return priority;
    }

    public boolean onlyCombo() {
        return onlyCombo;
    }

    @Nullable
    public Consumer<T> getOnSuccess() {
        return onSuccess;
    }

    @Nullable
    public CooldownGenerator getCooldownGenerator() {
        return cooldownGenerator;
    }

    public Optional<AnimationRule<T>> check(T entity, LivingEntity target) {
        if (entity.getAnimation() != AnimatedEntity.NO_ANIMATION) {
            return Optional.empty();
        }
        if (condition.test(entity, target)) {
            return Optional.of(this);
        }
        return Optional.empty();
    }

    public Optional<WeightedRule<T>> checkCombo(T entity, LivingEntity target, CooldownManager cooldownManager) {
        if (nextRules.isEmpty() || entity.getAnimation() != animation) {
            return Optional.empty();
        }
        //if (entity.getAnimationTick() != triggerTick) return Optional.empty();
        if (entity.getAnimationTick() < triggerTick) return Optional.empty();
        return Optional.ofNullable(selectRandomWeighted(nextRules, entity, target, cooldownManager));
    }

    private WeightedRule<T> selectRandomWeighted(List<WeightedRule<T>> weightedRules, T entity, LivingEntity target, CooldownManager cooldownManager) {
        List<WeightedRule<T>> available = weightedRules.stream()
                .filter(wr -> cooldownManager.isReady(wr.getRule().getAnimation()))
                .filter(wr -> wr.test(entity, target))
                .toList();
        if (available.isEmpty()) return null;
        double total = available.stream().mapToDouble(WeightedRule::getWeight).sum();
        double r = Math.random() * total;
        double cum = 0;
        for (WeightedRule<T> wr : available) {
            cum += wr.getWeight();
            if (r < cum) return wr;
        }
        return null;
    }

    public static class Builder<T extends Mob & AnimatedEntity> {
        private final Animation animation;
        private AnimationCondition<T> condition;
        private CooldownGenerator cooldownGenerator;
        private Consumer<T> onSuccess;
        private int priority = 0;
        private int triggerTick = -1;
        private boolean onlyCombo;
        private final List<WeightedRule<T>> nextRules = new ArrayList<>();

        public Builder(Animation animation) {
            this.animation = animation;
        }

        public Builder<T> condition(AnimationCondition<T> condition) {
            this.condition = condition;
            return this;
        }

        public Builder<T> onSuccess(Consumer<T> onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        public Builder<T> cooldown(CooldownGenerator generator) {
            this.cooldownGenerator = generator;
            return this;
        }

        public Builder<T> priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder<T> next(AnimationRule<T> animation) {
            return nextW(animation, 1.0);
        }

        public Builder<T> nextH(AnimationRule<T> animation, double HP) {
            return next(animation, 1.0, HP);
        }

        public Builder<T> nextW(AnimationRule<T> animation, double weight) {
            return next(animation, weight, 1, animation.condition);
        }

        public Builder<T> next(AnimationRule<T> animation, double weight, double HP) {
            return next(animation, weight, HP, animation.condition);
        }

        public Builder<T> next(AnimationRule<T> animation, double weight, double HP, AnimationCondition<T> condition) {
            nextRules.add(new WeightedRule<>(animation, condition, weight, HP));
            return this;
        }

        public Builder<T> triggerAtTick(int tick) {
            this.triggerTick = tick;
            return this;
        }

        //仅作为连招过渡的动画使用 必须在condition方法之前使用
        public Builder<T> onlyCombo() {
            this.onlyCombo = true;
            if (this.condition != null) System.out.println("Condition have been overridden");
            this.condition = (entity, target) -> true;
            return this;
        }

        public AnimationRule<T> build() {
            if (condition == null) {
                throw new IllegalStateException("Condition cannot be null");
            }
            if (!nextRules.isEmpty() && (triggerTick < 0 || triggerTick > animation.getDuration())) {
                throw new IllegalStateException("triggerTick out of range for combo: 0 to " + animation.getDuration());
            }
            return new AnimationRule<>(this);
        }
    }
}