package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityImmortalLaser extends EntityAbsBeam {
    public static final double IMMORTAL_RADIUS = 32;
    public static final double RENDER_DISTANCE = (IMMORTAL_RADIUS * IMMORTAL_RADIUS) * 2;
    private static final float MAX_RADIANS = 0.6108652381980153F;
    private static final float ROTATION_SPEED = MAX_RADIANS / 10F;
    private static final EntityDataAccessor<Boolean> DATA_PLAY_SOUND = SynchedEntityData.defineId(EntityImmortalLaser.class, EntityDataSerializers.BOOLEAN);
    private boolean isRotating = true;

    public EntityImmortalLaser(EntityType<? extends EntityImmortalLaser> type, Level level) {
        super(type, level, 1);
    }

    public EntityImmortalLaser(Level world, LivingEntity caster, double x, double y, double z, float yaw, int duration, boolean canPlaySound) {
        this(EntityInit.IMMORTAL_LASER.get(), world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(-Mth.PI / 2F);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos(IMMORTAL_RADIUS);
        this.getEntityData().set(DATA_PLAY_SOUND, canPlaySound);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    protected void beamTick() {
        if (this.level().isClientSide && this.tickCount == 1) {
            if (this.entityData.get(DATA_PLAY_SOUND)) EEEABMobs.PROXY.playImmortalLaserSound(this);
        }
        if (this.tickCount >= this.getCountDown()) {
            this.calculateEndPos(IMMORTAL_RADIUS);
            if (this.isRotating) {
                float newPitch = this.getPitch() + ROTATION_SPEED;
                if (newPitch >= MAX_RADIANS) {
                    newPitch = MAX_RADIANS;
                    this.isRotating = false;
                }
                this.setPitch(newPitch);
            }
            List<LivingEntity> entities = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ)).getEntities();
            if (!this.level().isClientSide) {
                for (LivingEntity target : entities) {
                    boolean hurtFlag = false;
                    DamageSource indirectMagic = this.damageSources().indirectMagic(this, caster);
                    if (this.caster instanceof EntityImmortal immortal) {
                        float damageMultiplier = 0F;
                        MobEffectInstance instance = target.getEffect(EffectInit.ERODE_EFFECT.get());
                        if (instance != null) damageMultiplier += (instance.getAmplifier() + 1) * 0.08F;
                        hurtFlag = immortal.doHurtTarget(indirectMagic, target, false, false, false, 0.03F, 0.375F, 1F + damageMultiplier);
                    } else if (this.caster != null) {
                        hurtFlag = target.hurt(indirectMagic, 5F + target.getMaxHealth() * 0.01F);
                    }
                    if (hurtFlag) ModEntityUtils.addEffectStackingAmplifier(this, target, EffectInit.ERODE_EFFECT.get(), 300, 5, true, true, true, true, true);
                }
            } else if (this.tickCount > this.getCountDown()) {
                this.spawnExplosionParticles();
            }
        }
    }

    @Override
    protected void spawnExplosionParticles() {
        if (this.blockSide != null) {
            for (int i = 0; i < 2; i++) {
                final float velocity = 0.2F;
                float yaw = (float) (random.nextFloat() * 2 * Math.PI);
                float motionY = random.nextFloat() * -velocity;
                float motionX = velocity * Mth.cos(yaw);
                float motionZ = velocity * Mth.sin(yaw);
                Vec3i normal = this.blockSide.getNormal();
                level().addParticle(ParticleInit.IMMORTAL_EXPLOSION.get(), prevCollidePosX + normal.getX(), prevCollidePosY + normal.getY() * 0.5, prevCollidePosZ + normal.getZ(), 0, 0, 0);
                level().addParticle(ParticleTypes.LARGE_SMOKE, prevCollidePosX + normal.getX(), prevCollidePosY + normal.getY() * 0.5, prevCollidePosZ + normal.getZ(), motionX, motionY, motionZ);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PLAY_SOUND, false);
    }

    @Override
    protected float getBaseScale() {
        return 0.3F;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < RENDER_DISTANCE;
    }
}
