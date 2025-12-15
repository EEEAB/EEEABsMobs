package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
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
    private static final EntityDataAccessor<Boolean> DATA_EXPLODE = SynchedEntityData.defineId(EntityExplode.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(EntityExplode.class, EntityDataSerializers.FLOAT);
    protected DamageSource damageSource;
    protected float damage;

    public EntityExplode(EntityType<?> type, Level level) {
        super(type, level);
    }

    public EntityExplode(Level level, DamageSource damageSource, @Nullable LivingEntity caster, float radius, float damage) {
        this(EntityInit.EXPLODE.get(), level);
        this.setRadius(radius);
        this.damageSource = damageSource;
        this.damage = damage;
        this.caster = caster;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isExplode()) {
            this.discard();
        } else {
            this.entityData.set(DATA_EXPLODE, true);
            this.explode();
            this.doExplodeEffect();
        }
    }

    protected void explode() {
        Vec3 pos = this.position();
        float f1 = this.getRadius() * 2F;
        int k1 = Mth.floor(pos.x - f1 - 1);
        int l1 = Mth.floor(pos.x + f1 + 1);
        int i2 = Mth.floor(pos.y - f1 - 1);
        int i1 = Mth.floor(pos.y + f1 + 1);
        int j2 = Mth.floor(pos.z - f1 - 1);
        int j1 = Mth.floor(pos.z + f1 + 1);
        List<Entity> list = this.level().getEntities(this, new AABB(k1, i2, j2, l1, i1, j1), e -> e != this.caster || !e.ignoreExplosion());
        for (Entity hit : list) {
            double d12 = Math.sqrt(hit.distanceToSqr(pos)) / (double) f1;
            if (d12 <= 1) {
                double d5 = hit.getX() - pos.x;
                double d7 = (hit instanceof PrimedTnt ? hit.getY() : hit.getEyeY()) - pos.y;
                double d9 = hit.getZ() - pos.z;
                double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                if (d13 != 0) {
                    d5 /= d13;
                    d7 /= d13;
                    d9 /= d13;
                    double d14 = Explosion.getSeenPercent(pos, hit);
                    double d10 = (1 - d12) * d14;
                    //damage = Math.min((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f1 + 1.0D), damage);
                    if (hit.isAttackable()) {
                        float finalDamage = damage;
                        if (hit instanceof LivingEntity target && this.caster instanceof IMob mob) finalDamage += mob.getDamageAmountByTargetHealthPct(target);
                        doHurtEntity(this.damageSource, hit, finalDamage);
                    }
                    doKnockbackEntity(hit, d10, d5, d7, d9);
                }
            }
        }
    }

    protected void doHurtEntity(DamageSource damageSource, Entity hitEntity, float damage) {
        if (damageSource == null) {
            damageSource = this.damageSources().explosion(this, this);
        }
        hitEntity.hurt(damageSource, damage);
    }

    protected void doKnockbackEntity(Entity hitEntity, double strength, double x, double y, double z) {
        double d0;
        if (hitEntity instanceof LivingEntity livingEntity) {
            d0 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingEntity, strength);
        } else {
            d0 = strength;
        }
        x *= d0;
        y *= d0;
        z *= d0;
        Vec3 vec31 = new Vec3(x, y, z);
        hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().add(vec31));
    }

    protected void doExplodeEffect() {
        if (this.level().isClientSide) {
            Vec3 vec3 = this.position();
            this.level().playLocalSound(vec3.x, vec3.y, vec3.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            if (!(this.getRadius() < 2.0F)) {
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, vec3.x, vec3.y, vec3.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.level().addParticle(ParticleTypes.EXPLOSION, vec3.x, vec3.y, vec3.z, 1.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void setDeltaMovement(Vec3 deltaMovement) {
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
     * @param caster       造成爆炸的实体
     * @param radius       半径
     * @param damage       造成的伤害(受到方块阻挡与距离爆炸中心的影响)
     */
    public static void explode(Level world, Vec3 vec3, DamageSource damageSource, @Nullable LivingEntity caster, float radius, float damage) {
        if (!world.isClientSide) {
            EntityExplode explode = new EntityExplode(world, damageSource, caster, radius, damage);
            explode.setPos(vec3);
            world.addFreshEntity(explode);
        }
    }
}
