package com.eeeab.eeeabsmobs.sever.entity.projectile;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.core.Position;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class EntityPoisonArrow extends AbstractArrow {
    private int duration;

    public EntityPoisonArrow(EntityType<? extends EntityPoisonArrow> entityType, Level level) {
        super(entityType, level);
    }

    public EntityPoisonArrow(Level level, Position position) {
        super(EntityInit.POISON_ARROW.get(), position.x(), position.y(), position.z(), level);
        this.pickup = AbstractArrow.Pickup.DISALLOWED;
        this.duration = 100;
        this.setBaseDamage(6.0D);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        target.addEffect(new MobEffectInstance(MobEffects.POISON, this.duration, 1), this.getEffectSource());
    }
}
