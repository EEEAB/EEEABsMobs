package com.eeeab.eeeabsmobs.sever.ability;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AbilityType<ENTITY extends LivingEntity, ABILITY extends Ability<ENTITY>> implements Comparable<AbilityType<ENTITY, ABILITY>> {
    private final EEFactory<ENTITY, ABILITY> factory;
    private final String name;

    public AbilityType(EEFactory<ENTITY, ABILITY> factory, String name) {
        this.factory = factory;
        this.name = name;
    }

    public ABILITY getInstance(LivingEntity user) {
        return factory.create(this, (ENTITY) user);
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NotNull AbilityType<ENTITY, ABILITY> type) {
        return this.getName().compareTo(type.getName());//按照首字母升序排序
    }

    @FunctionalInterface
    public interface EEFactory<E extends LivingEntity, A extends Ability<E>> {
        A create(AbilityType<E, A> type, E user);
    }
}
