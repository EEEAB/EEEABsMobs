package com.eeeab.mixin;

import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (super.canAttack(target)){
            return !this.hasEffect(EffectInit.VERTIGO_EFFECT.get());
        }
        return false;
    }
}
