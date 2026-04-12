package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class EntityExplode extends EntityMagicEffects {
    private static final EntityDataAccessor<Boolean> DATA_EXPLODE = SynchedEntityData.defineId(EntityExplode.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(EntityExplode.class, EntityDataSerializers.FLOAT);
    protected DamageSource damageSource;
    protected float damage;
    protected float baseDamage;
    protected Entity owner;

    public EntityExplode(EntityType<?> type, Level level) {
        super(type, level);
    }

    public EntityExplode(Level level, DamageSource damageSource, @Nullable Entity owner, float radius, float damage, float baseDamage) {
        this(EntityInit.EXPLODE.get(), level);
        this.setRadius(radius);
        this.damageSource = damageSource;
        this.damage = damage;
        this.baseDamage = baseDamage;
        this.owner = owner;
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

    public static float getSeenPercent(Vec3 explosionVector, Entity entity) {
        AABB aabb = entity.getBoundingBox();
        double d0 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;
            for (double d5 = 0.0D; d5 <= 1.0D; d5 += d0) {
                for (double d6 = 0.0D; d6 <= 1.0D; d6 += d1) {
                    for (double d7 = 0.0D; d7 <= 1.0D; d7 += d2) {
                        double d8 = Mth.lerp(d5, aabb.minX, aabb.maxX);
                        double d9 = Mth.lerp(d6, aabb.minY, aabb.maxY);
                        double d10 = Mth.lerp(d7, aabb.minZ, aabb.maxZ);
                        Vec3 vec3 = new Vec3(d8 + d3, d9, d10 + d4);
                        if (entity.level().clip(new ClipContext(vec3, explosionVector, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    public void explode() {
        Vec3 pos = this.position();
        this.level().gameEvent(this, GameEvent.EXPLODE, pos);
        float f2 = this.getRadius() * 2.0F;
        int k1 = Mth.floor(pos.x - (double) f2 - 1.0D);
        int l1 = Mth.floor(pos.x + (double) f2 + 1.0D);
        int i2 = Mth.floor(pos.y - (double) f2 - 1.0D);
        int i1 = Mth.floor(pos.y + (double) f2 + 1.0D);
        int j2 = Mth.floor(pos.z - (double) f2 - 1.0D);
        int j1 = Mth.floor(pos.z + (double) f2 + 1.0D);
        List<Entity> list = this.level().getEntities(this.owner, new AABB(k1, i2, j2, l1, i1, j1), e -> e.isAttackable() && !e.ignoreExplosion());
        for (Entity hit : list) {
            double d12 = Math.sqrt(hit.distanceToSqr(pos)) / (double) f2;
            if (d12 <= 1.0D) {
                double d5 = hit.getX() - pos.x;
                double d7 = (hit instanceof PrimedTnt ? hit.getY() : hit.getEyeY()) - pos.y;
                double d9 = hit.getZ() - pos.z;
                double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                if (d13 != 0.0D) {
                    d5 /= d13;
                    d7 /= d13;
                    d9 /= d13;
                    double d14 = this.seenPercentCheck() ? Math.max(getSeenPercent(pos, hit), 0.2) : 1F;
                    double d10 = (1.0D - d12) * d14;
                    float finalDamage = baseDamage + (damage - baseDamage) * (1.0F - (float) d12);
                    finalDamage *= (float) d14;
                    doHurtEntity(this.damageSource, hit, finalDamage);
                    this.doKnockbackEntity(hit, d10, d5, d7, d9);
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
            Vec3 pos = this.position();
            this.level().playLocalSound(pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 3.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            float radius = this.getRadius();
            if (radius >= 2.0F) {
                this.level().addParticle(ParticleInit.BLAZE_EXPLOSION_EMITTER.get(), pos.x, pos.y, pos.z, radius, radius, 0.0D);
            } else {
                this.level().addParticle(ParticleInit.BLAZE_EXPLOSION.get(), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
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

    @Override
    public boolean ignoreExplosion() {
        return true;
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

    protected boolean seenPercentCheck() {
        return true;
    }

    /**
     * 模拟原版爆炸
     *
     * @param vec3         爆炸坐标
     * @param damageSource 伤害源
     * @param owner        造成爆炸的实体
     * @param radius       半径
     * @param damage       造成的伤害(受到方块阻挡与距离爆炸中心的影响)
     */
    public static void explode(Level world, Vec3 vec3, DamageSource damageSource, @Nullable Entity owner, float radius, float damage) {
        explode(world, vec3, damageSource, owner, radius, damage, damage);
    }

    public static void explode(Level world, Vec3 vec3, DamageSource damageSource, @Nullable Entity owner, float radius, float damage, float baseDamage) {
        if (!world.isClientSide) {
            EntityExplode explode = new EntityExplode(world, damageSource, owner, radius, damage, baseDamage);
            explode.setPos(vec3);
            world.addFreshEntity(explode);
        }
    }
}
