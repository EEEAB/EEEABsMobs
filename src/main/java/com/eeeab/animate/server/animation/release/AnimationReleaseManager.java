package com.eeeab.animate.server.animation.release;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.release.cooldown.CooldownManager;
import com.eeeab.animate.server.handler.AnimationHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.*;
import java.util.function.Predicate;

/**
 * 动画释放管理器
 *
 * @author EEEAB
 */
public class AnimationReleaseManager<T extends Mob & AnimatedEntity> {
    private final List<AnimationRule<T>> ruleList = new ArrayList<>();
    private Predicate<T> predicate = e -> true;

    public Builder<T> builder() {
        return new Builder<>(this);
    }

    public void registerRule(AnimationRule<T> rule) {
        ruleList.add(rule);
        ruleList.sort(Comparator.comparingInt((AnimationRule<T> value) -> value.getPriority()).reversed());
    }

    public void tick(T entity, CooldownManager cooldownManager) {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive() || !entity.canAttack(target) || !predicate.test(entity)) return;
        for (AnimationRule<T> rule : ruleList) {
            Optional<WeightedRule<T>> comboResult = rule.checkCombo(entity, target, cooldownManager);
            if (comboResult.isPresent()) {
                releasedAnimation(comboResult.get().getRule(), entity, cooldownManager);
                break;
            }

            if (rule.onlyCombo()) continue;
            if (!cooldownManager.isReady(rule.getAnimation())) continue;

            Optional<AnimationRule<T>> result = rule.check(entity, target);
            if (result.isPresent()) {
                releasedAnimation(result.get(), entity, cooldownManager);
                break;
            }
        }
    }

    private static <T extends Mob & AnimatedEntity> void releasedAnimation(AnimationRule<T> checkResult, T entity, CooldownManager cooldownManager) {
        AnimationHandler.INSTANCE.sendAnimationMessage(entity, checkResult.getAnimation());
        if (checkResult.getOnSuccess() != null) {
            checkResult.getOnSuccess().accept(entity);
        }
        if (checkResult.getCooldownGenerator() != null) {
            int cd = checkResult.getCooldownGenerator().generate(entity);
            cooldownManager.setCD(checkResult.getAnimation(), cd);
        }
    }

    public static class Builder<T extends Mob & AnimatedEntity> {
        private final AnimationReleaseManager<T> manager;

        public Builder(AnimationReleaseManager<T> manager) {
            this.manager = manager;
        }

        public AnimationRule.Builder<T> define(Animation animation) {
            return new AnimationRule.Builder<>(animation);
        }

        public void condition(Predicate<T> predicate) {
            manager.predicate = predicate;
        }

        public void register(AnimationRule.Builder<T> rule) {
            manager.registerRule(rule.build());
        }
    }
}