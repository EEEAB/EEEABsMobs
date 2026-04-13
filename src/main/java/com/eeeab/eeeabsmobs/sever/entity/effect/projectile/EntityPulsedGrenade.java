package com.eeeab.eeeabsmobs.sever.entity.effect.projectile;

import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityExplode;
import com.eeeab.eeeabsmobs.sever.entity.effect.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityPulsedGrenade extends Projectile implements IEntity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(EntityPulsedGrenade.class, EntityDataSerializers.FLOAT);
    private static final float GRAVITY = 0.03F;
    private boolean cameraShake;
    private boolean sentSpikeEvent;

    public EntityPulsedGrenade(EntityType<? extends EntityPulsedGrenade> entityType, Level level) {
        super(entityType, level);
    }

    public EntityPulsedGrenade(Level level, LivingEntity caster, boolean cameraShake) {
        super(EntityInit.PULSED_GRENADE.get(), level);
        this.setOwner(caster);
        this.cameraShake = cameraShake;
    }

    @Override
    public void tick() {
        LivingEntity owner = getLivingOwner();
        if (this.level().isClientSide || (owner == null || !owner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                if (!this.sentSpikeEvent) {
                    this.level().broadcastEntityEvent(this, (byte) 4);
                    this.sentSpikeEvent = true;
                }
                this.onHit(hitresult);
            }
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            if (this.isInWater()) {
                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
            }
            if (this.tickCount > 3) {
                for (int i = 0; i < 4; ++i) {
                    double dx = this.getX() + this.random.nextGaussian() * 0.15D;
                    double dy = this.getY() + this.random.nextGaussian() * 0.15D;
                    double dz = this.getZ() + this.random.nextGaussian() * 0.15D;
                    this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), dx, dy, dz, -this.getDeltaMovement().x() * 0.25F, -this.getDeltaMovement().y() * 0.25F, -this.getDeltaMovement().z() * 0.25F);
                }
            }
            if (!this.isNoGravity()) {
                Vec3 vec31 = this.getDeltaMovement();
                this.setDeltaMovement(vec31.x, vec31.y - GRAVITY, vec31.z);
            }
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            Vec3 center = this.position().add(0, getBbHeight() / 2, 0);
            double x = center.x;
            double y = center.y;
            double z = center.z;
            Entity owner = this.getOwner();
            float range = this.getRadius() * 2;
            AABB aabb = ModEntityUtils.makeAABBWithSize(x, y, z, 0, range, range, range);
            for (Entity target : this.level().getEntities(this, aabb, e -> e != owner && (owner == null || !owner.isAlliedTo(e)) && !e.ignoreExplosion())) {
                double dist = Math.sqrt(target.distanceToSqr(center)) / range;
                if (dist <= 1) {
                    double d0 = target.getX() - x;
                    double d1 = target.getEyeY() - y;
                    double d2 = target.getZ() - z;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    if (d3 != 0) {
                        float damage = owner instanceof Player ? this.getPlayerDamage() : getDamage();
                        if (owner instanceof IMob mob && target instanceof LivingEntity livingEntity) {
                            damage += mob.getDamageAmountByTargetHealthPct(livingEntity);
                        }
                        float percent = EntityExplode.getSeenPercent(new Vec3(x, y, z), target);
                        target.hurt(this.damageSources().explosion(this, owner), damage * percent);
                        double strength = (1.0D - dist) * percent;
                        if (target instanceof LivingEntity livingEntity) {
                            strength = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingEntity, strength);
                        }
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        d0 *= strength;
                        d1 *= strength;
                        d2 *= strength;
                        target.setDeltaMovement(target.getDeltaMovement().add(d0, d1, d2));
                    }
                }
            }
            if (this.cameraShake) EntityCameraShake.cameraShake(this.level(), this.position(), 16F, 0.125F, 5, 15);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        LivingEntity owner = getLivingOwner();
        if (result.getEntity() instanceof LivingEntity entityHit && entityHit != owner) {
            entityHit.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 200, 0, false, false, true), this);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }
        d0 *= 64.0D;
        return distance < d0 * d0;
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            Vec3 pos = this.position();
            this.level().playLocalSound(pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.5F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            float radius = this.getRadius();
            if (radius >= 2.0F) {
                this.level().addParticle(ParticleInit.VOLT_EXPLOSION_EMITTER.get(), pos.x, pos.y, pos.z, radius, radius, 0.0D);
            } else {
                this.level().addParticle(ParticleInit.VOLT_EXPLOSION.get(), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_RADIUS, 1F);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("radius", this.getRadius());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRadius(compound.getFloat("radius"));
    }

    private LivingEntity getLivingOwner() {
        return getOwner() instanceof LivingEntity ? (LivingEntity) getOwner() : null;
    }

    private float getDamage() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicEarthshaker.pulsedGrenade.damage.get().floatValue();
    }

    private float getPlayerDamage() {
        return ModConfigHandler.COMMON.items.busterGauntletConfig1.get().floatValue();
    }

    public float getRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    public void setRadius(float radius) {
        this.entityData.set(DATA_RADIUS, radius);
    }
}
