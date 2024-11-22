package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityImmortalLaser extends EntityAbsBeam {
    public static final double IMMORTAL_RADIUS = 32;
    private static final float MAX_RADIANS = 0.6108652381980153F;
    private static final float ROTATION_SPEED = MAX_RADIANS / 10F;
    private boolean isRotating = true;

    public EntityImmortalLaser(EntityType<? extends EntityImmortalLaser> type, Level level) {
        super(type, level, 1);
    }

    public EntityImmortalLaser(Level world, LivingEntity caster, double x, double y, double z, float yaw, int duration) {
        this(EntityInit.IMMORTAL_LASER.get(), world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(-Mth.PI / 2F);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos(IMMORTAL_RADIUS);
        EEEABMobs.PROXY.playImmortalLaserSound(this);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    protected void beamTick() {
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
                    if (target == this.caster) continue;
                    float baseDamage = 5F;
                    MobEffectInstance instance = target.getEffect(EffectInit.ERODE_EFFECT.get());
                    if (instance != null) baseDamage += (instance.getAmplifier() + 1) * 0.5F;
                    target.hurt(this.damageSources().indirectMagic(this, caster), baseDamage + target.getMaxHealth() * 0.01F);
                    ModEntityUtils.addEffectStackingAmplifier(this, target, EffectInit.ERODE_EFFECT.get(), 300, 5, true, true, true, true, true);
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
    protected float getBaseScale() {
        return 0.3F;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        if (this.caster == null) return distance < 2048;
        double d0 = this.caster.getBoundingBox().getSize();
        if (Double.isNaN(d0)) d0 = 1.0D;
        d0 *= 64.0D;
        return distance < d0 * d0;
    }
}
