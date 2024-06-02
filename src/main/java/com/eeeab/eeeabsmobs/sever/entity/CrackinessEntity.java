package com.eeeab.eeeabsmobs.sever.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public interface CrackinessEntity<T extends LivingEntity> {
    enum CrackinessType {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);

        private static final List<CrackinessType> BY_DAMAGE = Stream.of(values())
                .sorted(Comparator.comparingDouble((crackiness) -> (double) crackiness.fraction))
                .collect(ImmutableList.toImmutableList());
        private final float fraction;

        CrackinessType(float pFraction) {
            this.fraction = pFraction;
        }

        public static CrackinessType byFraction(float pFraction) {
            for (CrackinessType crackiness : BY_DAMAGE) {
                if (pFraction < crackiness.fraction) {
                    return crackiness;
                }
            }
            return NONE;
        }
    }

    default CrackinessType getCrackiness(T entity) {
        return CrackinessType.byFraction(entity.getHealth() / entity.getMaxHealth());
    }
}
