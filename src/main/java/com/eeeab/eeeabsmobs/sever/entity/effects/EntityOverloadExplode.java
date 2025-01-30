package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.damage.EMDamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityOverloadExplode extends EntityExplode {
    public EntityOverloadExplode(EntityType<?> type, Level level) {
        super(type, level);
    }

    public EntityOverloadExplode(Level level, @Nullable LivingEntity caster, float radius, float maxDamage) {
        this(EntityInit.OVERLOAD_EXPLODE.get(), level);
        this.setRadius(radius);
        this.maxDamage = maxDamage;
        this.caster = caster;
    }

    @Override
    protected void doHurtEntity(DamageSource damageSource, Entity hitEntity, float damage) {
        if (damageSource == null) {
            damageSource = EMDamageSource.overloadExplode(hitEntity, this);
        }
        if (hitEntity instanceof LivingEntity livingEntity) {
            damage = damage + livingEntity.getMaxHealth() * 0.05F;
            float forecast = livingEntity.getHealth() - damage;
            //由于该伤害源无视无敌帧 通过预算与限制造成伤害量来尽量避免被秒杀的风险
            hitEntity.hurt(damageSource, forecast <= 0 ? livingEntity.getHealth() - 1 : damage);
        } else {
            hitEntity.hurt(damageSource,  damage);
        }
    }

    @Override
    protected void doKnockbackEffect(Entity hit, double strength, double x, double y, double z) {
    }

    @Override
    protected void doExplodeEffect(Vec3 vec3, float radius) {
        if (this.level().isClientSide) {
            this.level().playLocalSound(vec3.x, vec3.y, vec3.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            boolean flag = radius < 2.0F;
            int length = flag ? 6 : 8;
            for (int i = 0; i < length; i++) {
                double d0 = vec3.x + (this.random.nextDouble() - this.random.nextDouble()) * 3D;
                double d1 = vec3.y + (this.random.nextDouble() - this.random.nextDouble()) * 3D;
                double d2 = vec3.z + (this.random.nextDouble() - this.random.nextDouble()) * 3D;
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), d0, d1, d2, 0D, 0.007D, 0D);
            }
            length = flag ? 10 : 15;
            for (int i = 0; i < length; i++) {
                double d0 = vec3.x + (this.random.nextDouble() - this.random.nextDouble()) * 1.5D;
                double d1 = vec3.y + (this.random.nextDouble() - this.random.nextDouble()) * 1.5D;
                double d2 = vec3.z + (this.random.nextDouble() - this.random.nextDouble()) * 1.5D;
                final float velocity = 0.3F;
                float yaw = (float) (random.nextFloat() * 2 * Math.PI);
                float f0 = velocity * Mth.cos(yaw);
                float f1 = random.nextFloat() * velocity;
                float f2 = velocity * Mth.sin(yaw);
                this.level().addParticle(ParticleTypes.SMOKE, d0, d1, d2, f0, f1, f2);
            }
            this.level().addParticle(flag ? ParticleInit.OVERLOAD_EXPLOSION.get() : ParticleInit.OVERLOAD_EXPLOSION_EMITTER.get(), vec3.x, vec3.y, vec3.z, 1.0D, 0.0D, 0.0D);
        }
    }

    public static void explode(Level world, Vec3 vec3, @Nullable LivingEntity caster, float radius, float maxDamage) {
        if (!world.isClientSide) {
            EntityOverloadExplode explode = new EntityOverloadExplode(world, caster, radius, maxDamage);
            explode.setPos(vec3);
            world.addFreshEntity(explode);
        }
    }
}
