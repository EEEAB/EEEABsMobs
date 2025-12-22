package com.eeeab.eeeabsmobs.sever.entity.effect.projectile;

import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityExplode;
import com.eeeab.eeeabsmobs.sever.entity.effect.IEntity;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityPulsedGrenade extends Projectile implements IEntity {
    private static final float GRAVITY = 0.03F;
    private float radius = 2.5F;
    private boolean cameraShake;

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
        LivingEntity owner = getLivingOwner();
        float damage = owner instanceof Player ? this.getPlayerDamage() : this.getDamage();
        EntityExplode.explode(this.level(), this.position(), this.damageSources().explosion(this, owner), this, this.radius, damage);
        if (this.cameraShake) EntityCameraShake.cameraShake(this.level(), this.position(), 16F, 0.125F, 5, 15);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        LivingEntity owner = getLivingOwner();
        if (result.getEntity() instanceof LivingEntity entityHit && entityHit != owner) {
            entityHit.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 300, 0, false, false, true), this);
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

    private LivingEntity getLivingOwner() {
        return getOwner() instanceof LivingEntity ? (LivingEntity) getOwner() : null;
    }

    private float getDamage() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicEarthshaker.pulsedGrenade.damage.get().floatValue();
    }

    private float getPlayerDamage() {
        return ModConfigHandler.COMMON.items.howitzerConfig1.get().floatValue();
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void defineSynchedData() {
    }
}
