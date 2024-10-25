package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.damage.EMDamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class EntityGuardianLaser extends EntityAbsBeam {
    public static final double GUARDIAN_RADIUS = 32;
    private static final EntityDataAccessor<Boolean> DATA_IS_PLAYER = SynchedEntityData.defineId(EntityGuardianLaser.class, EntityDataSerializers.BOOLEAN);

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;

    public EntityGuardianLaser(EntityType<? extends EntityGuardianLaser> type, Level level) {
        super(type, level, 20);
        if (level.isClientSide) {
            attractorPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }

    public EntityGuardianLaser(EntityType<? extends EntityGuardianLaser> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos(GUARDIAN_RADIUS);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    public void beamTick() {
        if (!this.level().isClientSide) {
            if (isPlayer() && this.caster instanceof Player) {
                this.updateWithPlayer();
            } else if (this.caster instanceof EntityNamelessGuardian) {
                this.updateWithGuardian();
            } else if (this.caster != null) {
                this.updateWithEntity(0F, 0.75F);
            }
        }

        if (caster != null) {
            this.yaw = (float) Math.toRadians(caster.yHeadRot + 90);
            this.pitch = (float) -Math.toRadians(caster.getXRot());
        }

        if (level().isClientSide && tickCount <= this.getCountDown() / 2 && caster != null) {
            double rootX = caster.getX();
            double rootY = caster.getY() + caster.getBbHeight() / 2f + 0.3f;
            double rootZ = caster.getZ();
            this.attractorPos[0] = new Vec3(rootX, rootY, rootZ);
            ModParticleUtils.advAttractorParticle(ParticleInit.ADV_ORB.get(), caster, 12, 0.5f, 5.0f, 8, new ParticleComponent[]{
                    new ParticleComponent.Attractor(this.attractorPos, 1.6f, 0.0f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0f, 0.6f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.KeyTrack.startAndEnd(6f, 3f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, AnimData.KeyTrack.startAndEnd(1f, 0.3f), false),
            }, false);
        }
        if (this.tickCount >= this.getCountDown()) {
            this.calculateEndPos(isPlayer() ? EMConfigHandler.COMMON.ENTITY.guardianLaserShootRadius.get() : GUARDIAN_RADIUS);
            List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ)).getEntities();
            if (this.blockSide != null) {
                this.spawnExplosionParticles();
                if (!this.level().isClientSide && !isPlayer() && EMConfigHandler.COMMON.ENTITY.enableGenerateScorchEntity.get()) {
                    EntityScorch scorch = new EntityScorch(this.level(), this.prevCollidePosX, this.prevCollidePosY, this.prevCollidePosZ);
                    this.level().addFreshEntity(scorch);
                }
            }
            if (!this.level().isClientSide) {
                for (LivingEntity target : hit) {
                    target.setSecondsOnFire(3);
                    if (this.caster instanceof EntityNamelessGuardian guardian) {
                        guardian.guardianHurtTarget(EMDamageSource.guardianLaserAttack(this, guardian), guardian, target, 0.035F, 0.22F, 1F, true, false, false);
                    } else if (this.caster != null) {
                        target.hurt(this.damageSources().mobAttack(this.caster), 5F + target.getMaxHealth() * 0.01F);
                    }
                }
            }
        }
    }

    @Override
    protected void spawnExplosionParticles() {
        for (int i = 0; i < 2; i++) {
            final float velocity = 0.2F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * velocity;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        level().addParticle(ParticleTypes.LAVA, collidePosX, collidePosY, collidePosZ, 0, 0, 0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_PLAYER, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (this.caster == null) discard();
    }

    public boolean isPlayer() {
        return getEntityData().get(DATA_IS_PLAYER);
    }

    public void setPlayer(boolean flag) {
        getEntityData().set(DATA_IS_PLAYER, flag);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        Double radius = EMConfigHandler.COMMON.ENTITY.guardianLaserShootRadius.get();
        return isPlayer() ? distance < (radius * radius) * 2 : distance < (GUARDIAN_RADIUS * GUARDIAN_RADIUS) * 2;
    }

    private void updateWithPlayer() {
        this.setYaw((float) Math.toRadians(caster.yHeadRot + 90));
        this.setPitch((float) Math.toRadians(-caster.getXRot()));
        Vec3 vecOffset = caster.getLookAngle().normalize().scale(1);
        this.setPos(caster.getX() + vecOffset.x(), caster.getY() + caster.getBbHeight() * 0.5F + vecOffset.y(), caster.getZ() + vecOffset.z());
    }

    private void updateWithGuardian() {
        this.updateWithEntity(1.35F, 0.8F);
    }

    private void updateWithEntity(float offset, float yOffset) {
        double radians = Math.toRadians(this.caster.yHeadRot + 90);
        this.setYaw((float) radians);
        this.setPitch((float) ((double) (-this.caster.getXRot()) * Math.PI / 180.0));
        double offsetX = Math.cos(radians) * offset;
        double offsetZ = Math.sin(radians) * offset;
        this.setPos(this.caster.getX() + offsetX, this.caster.getY(yOffset), this.caster.getZ() + offsetZ);
    }
}
