package com.eeeab.animate.server.animation.release;

import com.eeeab.animate.server.animation.AnimatedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class WeightedRule<T extends Mob & AnimatedEntity> {
    private final AnimationCondition<T> condition;
    private final AnimationRule<T> rule;
    private final double weight;
    private final double HP;

    public WeightedRule(AnimationRule<T> rule, AnimationCondition<T> condition, double weight, double HP) {
        this.rule = rule;
        this.condition = condition;
        this.weight = weight;
        this.HP = HP;
    }

    public AnimationRule<T> getRule() {
        return rule;
    }

    public double getWeight() {
        return weight;
    }

    public boolean test(T entity, LivingEntity target) {
        if (entity.getHealth() / entity.getMaxHealth() > HP) return false;
        return condition != null && condition.test(entity, target);
    }
}