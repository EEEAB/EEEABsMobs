package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class EntityExplode extends EntityMagicEffects {
    private DamageSource damageSource;
    private float maxDamage;
    private static final EntityDataAccessor<Boolean> DATA_EXPLODE = SynchedEntityData.defineId(EntityExplode.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(EntityExplode.class, EntityDataSerializers.FLOAT);

    public EntityExplode(EntityType<EntityExplode> type, Level level) {
        super(type, level);
    }

    public EntityExplode(Level level, DamageSource damageSource, float radius, float maxDamage) {
        this(EntityInit.EXPLODE.get(), level);
        this.setRadius(radius);
        this.damageSource = damageSource;
        this.maxDamage = maxDamage;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isExplode()) {
            this.discard();
        } else {
            this.entityData.set(DATA_EXPLODE, true);
            this.explode(damageSource, this, maxDamage);
            this.doExplodeEffect(this.position(), this.getRadius());
        }
    }

    private void explode(DamageSource damageSource, @Nullable Entity entity, float maxDamage) {
        Vec3 vec3 = this.position();
        float f2 = this.getRadius() * 2.0F;
        int k1 = Mth.floor(vec3.x - (double) f2 - 1.0D);
        int l1 = Mth.floor(vec3.x + (double) f2 + 1.0D);
        int i2 = Mth.floor(vec3.y - (double) f2 - 1.0D);
        int i1 = Mth.floor(vec3.y + (double) f2 + 1.0D);
        int j2 = Mth.floor(vec3.z - (double) f2 - 1.0D);
        int j1 = Mth.floor(vec3.z + (double) f2 + 1.0D);
        List<Entity> list = this.level().getEntities(entity, new AABB(k1, i2, j2, l1, i1, j1));
        for (Entity hit : list) {
            if (!hit.ignoreExplosion()) {
                double d12 = Math.sqrt(hit.distanceToSqr(vec3)) / (double) f2;
                if (d12 <= 1.0D) {
                    double d5 = hit.getX() - vec3.x;
                    double d7 = (hit instanceof PrimedTnt ? hit.getY() : hit.getEyeY()) - vec3.y;
                    double d9 = hit.getZ() - vec3.z;
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 /= d13;
                        d7 /= d13;
                        d9 /= d13;
                        double d14 = Explosion.getSeenPercent(vec3, hit);
                        double d10 = (1.0D - d12) * d14;
                        float damage = Math.min(((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f2 + 1.0D)), maxDamage);
                        if (damageSource == null) {
                            damageSource = this.damageSources().explosion(this, this);
                        }
                        hit.hurt(damageSource, damage);
                        double d11;
                        if (hit instanceof LivingEntity livingEntity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingEntity, d10);
                        } else {
                            d11 = d10;
                        }
                        d5 *= d11;
                        d7 *= d11;
                        d9 *= d11;
                        Vec3 vec31 = new Vec3(d5, d7, d9);
                        hit.setDeltaMovement(hit.getDeltaMovement().add(vec31));
                    }
                }
            }
        }
    }

    private void doExplodeEffect(Vec3 vec3, float radius) {
        if (this.level().isClientSide) {
            this.level().playLocalSound(vec3.x, vec3.y, vec3.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            if (!(radius < 2.0F)) {
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, vec3.x, vec3.y, vec3.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.level().addParticle(ParticleTypes.EXPLOSION, vec3.x, vec3.y, vec3.z, 1.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_EXPLODE, false);
        this.entityData.define(DATA_RADIUS, 1F);
    }

    public boolean isExplode() {
        return this.entityData.get(DATA_EXPLODE);
    }

    public float getRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    public void setRadius(float radius) {
        this.entityData.set(DATA_RADIUS, radius);
    }

    /**
     * 模拟原版爆炸
     *
     * @param vec3         爆炸坐标
     * @param damageSource 伤害源
     * @param radius       半径
     * @param maxDamage    最大伤害限制
     */
    public static void explode(Level world, Vec3 vec3, DamageSource damageSource, float radius, float maxDamage) {
        if (!world.isClientSide) {
            EntityExplode explode = new EntityExplode(world, damageSource, radius, maxDamage);
            explode.setPos(vec3);
            world.addFreshEntity(explode);
        }
    }
}
