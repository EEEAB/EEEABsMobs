package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityAbsRelicron;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.ModDamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityOverloadExplode extends EntityExplode {
    public EntityOverloadExplode(EntityType<?> type, Level level) {
        super(type, level);
    }

    public EntityOverloadExplode(Level level, @Nullable LivingEntity causer, float radius, float damage) {
        this(EntityInit.OVERLOAD_EXPLODE.get(), level);
        this.setRadius(radius);
        this.damage = damage;
        this.baseDamage = damage;
        this.setOwner(causer);
    }

    @Override
    protected void doHurtEntity(DamageSource damageSource, Entity hitEntity, float damage) {
        damageSource = ModDamageSource.overloadExplode(hitEntity, this);
        if (hitEntity instanceof LivingEntity livingEntity) {
            if (hitEntity instanceof EntityAbsRelicron && !(getOwner() instanceof EntityAbsRelicron)) return;
            //由于该伤害源无视无敌帧 通过预算与限制造成伤害量来尽量避免被秒杀的风险
            float actualDamage = getActualDamage(damage, livingEntity);
            hitEntity.hurt(damageSource, actualDamage);
        } else {
            hitEntity.hurt(damageSource, damage);
        }
    }

    private static float getActualDamage(float damage, LivingEntity livingEntity) {
        float totalDamage = damage + livingEntity.getMaxHealth() * 0.05F;
        if (!(livingEntity instanceof Player)) return totalDamage;
        float remainingHealth = livingEntity.getHealth() - totalDamage;
        float minSafeHealth = Math.max(1.0F, livingEntity.getMaxHealth() * 0.05F);
        float actualDamage;
        if (remainingHealth < minSafeHealth) {
            actualDamage = Math.min(totalDamage, livingEntity.getHealth() - minSafeHealth);
        } else {
            actualDamage = totalDamage;
        }
        return actualDamage;
    }

    @Override
    protected void doKnockbackEntity(Entity hit, double strength, double x, double y, double z) {
    }

    @Override
    protected void doExplodeEffect() {
        super.doExplodeEffect();
        if (this.level().isClientSide) {
            Vec3 pos = this.position();
            double range = Math.min(1, this.getRadius() * 2);
            for (int i = 0; i < 6; i++) {
                double d0 = pos.x + (this.random.nextDouble() - this.random.nextDouble()) * range;
                double d1 = pos.y + (this.random.nextDouble() - this.random.nextDouble()) * range;
                double d2 = pos.z + (this.random.nextDouble() - this.random.nextDouble()) * range;
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), d0, d1, d2, 0D, 0D, 0D);
            }
        }
    }

    @Override
    protected boolean seenPercentCheck() {
        return false;
    }

    public static void explode(Level world, Vec3 vec3, @Nullable LivingEntity causer, float radius, float damage) {
        if (!world.isClientSide) {
            EntityOverloadExplode explode = new EntityOverloadExplode(world, causer, radius, damage);
            explode.setPos(vec3);
            world.addFreshEntity(explode);
        }
    }
}
