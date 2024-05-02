package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.EMDamageSource;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//参考自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/server/entity/effects/EntitySolarBeam.java
public class EntityGuardianLaser extends EntityMagicEffects {
    public static final double GUARDIAN_RADIUS = 32;
    public static final double PLAYER_RADIUS = 16;
    public static final int PRE_SHOOT_DURATION = 20;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float yHeadRotAngle, xHeadRotAngle;
    public ControlledAnimation displayControlled = new ControlledAnimation(3);
    public boolean ON = true;
    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> Y_HEAD_ROT_ANGLE = SynchedEntityData.defineId(EntityGuardianLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> X_HEAD_ROT_ANGLE = SynchedEntityData.defineId(EntityGuardianLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(EntityGuardianLaser.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_PLAYER = SynchedEntityData.defineId(EntityGuardianLaser.class, EntityDataSerializers.BOOLEAN);

    public float prevYHeadRotAngle;
    public float prevXHeadRotAngle;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;

    public EntityGuardianLaser(EntityType<? extends EntityGuardianLaser> type, Level level) {
        super(type, level);
        noCulling = true;
        if (level.isClientSide) {
            attractorPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }


    public EntityGuardianLaser(EntityType<? extends EntityGuardianLaser> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setYHeadRotAngle(yaw);
        this.setXHeadRotAngle(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos();
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    public void tick() {
        super.tick();
        prevCollidePosX = collidePosX;
        prevCollidePosY = collidePosY;
        prevCollidePosZ = collidePosZ;
        prevYHeadRotAngle = yHeadRotAngle;
        prevXHeadRotAngle = xHeadRotAngle;
        xo = getX();
        yo = getY();
        zo = getZ();
        if (tickCount == 1 && level().isClientSide) {
            caster = (LivingEntity) level().getEntity(getCasterId());
        }

        if (!level().isClientSide) {
            if (isPlayer()) {
                this.updateWithPlayer();
            } else if (caster instanceof EntityNamelessGuardian) {
                this.updateWithGuardian();
            }
        }

        if (caster != null) {
            yHeadRotAngle = (float) Math.toRadians(caster.yHeadRot + 90);
            xHeadRotAngle = (float) -Math.toRadians(caster.getXRot());
        }

        if (!ON && displayControlled.isStop()) {
            this.discard();
        }
        if (ON && !this.isAccumulating()) {
            displayControlled.increaseTimer();
        } else {
            displayControlled.decreaseTimer();
        }

        if (caster != null && !caster.isAlive()) discard();

        if (level().isClientSide && tickCount <= PRE_SHOOT_DURATION / 2 && caster != null) {
            double rootX = caster.getX();
            double rootY = caster.getY() + caster.getBbHeight() / 2f + 0.3f;
            double rootZ = caster.getZ();
            attractorPos[0] = new Vec3(rootX, rootY, rootZ);
            ModParticleUtils.advAttractorParticle(ParticleInit.ADV_ORB.get(), caster, 12, 0.5f, 5.0f, 8, new ParticleComponent[]{
                    new ParticleComponent.Attractor(attractorPos, 1.6f, 0.0f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0f, 0.6f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.KeyTrack.startAndEnd(6f, 3f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, AnimData.KeyTrack.startAndEnd(1f, 0.3f), false),
            }, false);
        }
        if (tickCount > PRE_SHOOT_DURATION) {
            this.calculateEndPos();
            List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ)).entities;
            if (blockSide != null) {
                spawnExplosionParticles(2);
                if (!level().isClientSide && !isPlayer() && EMConfigHandler.COMMON.ENTITY.GUARDIAN_LASER.enableGenerateScorchEntity.get()) {
                    EntityScorch scorch = new EntityScorch(level(), prevCollidePosX, prevCollidePosY, prevCollidePosZ);
                    level().addFreshEntity(scorch);
                }
            }
            if (!level().isClientSide) {
                for (LivingEntity target : hit) {
                    target.setSecondsOnFire(3);
                    if (caster instanceof EntityNamelessGuardian guardian) {
                        guardian.guardianHurtTarget(EMDamageSource.guardianLaserAttack(this, guardian), guardian, target, 0.03F, 0.1F, 1F, true, false);
                    } else if (caster != null) {
                        target.hurt(this.damageSources().mobAttack(caster), 5F + target.getMaxHealth() * 0.01F);
                    }
                }
            }
        }

        if (tickCount - PRE_SHOOT_DURATION > getDuration()) {
            ON = false;
        }
    }

    public boolean isAccumulating() {
        return this.tickCount <= PRE_SHOOT_DURATION;
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.2F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * velocity;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            level().addParticle(ParticleTypes.LAVA, collidePosX, collidePosY, collidePosZ, 0, 0, 0);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(Y_HEAD_ROT_ANGLE, 0F);
        getEntityData().define(X_HEAD_ROT_ANGLE, 0F);
        getEntityData().define(DURATION, 0);
        getEntityData().define(IS_PLAYER, false);
    }

    public float getYHeadRotAngle() {
        return getEntityData().get(Y_HEAD_ROT_ANGLE);
    }

    public void setYHeadRotAngle(float rotAngle) {
        getEntityData().set(Y_HEAD_ROT_ANGLE, rotAngle);
    }

    public boolean isPlayer() {
        return getEntityData().get(IS_PLAYER);
    }

    public void setPlayer(boolean flag) {
        getEntityData().set(IS_PLAYER, flag);
    }

    public float getXHeadRotAngle() {
        return getEntityData().get(X_HEAD_ROT_ANGLE);
    }

    public void setXHeadRotAngle(float rotAngle) {
        getEntityData().set(X_HEAD_ROT_ANGLE, rotAngle);
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }


    private void calculateEndPos() {
        double radius = isPlayer() ? PLAYER_RADIUS : GUARDIAN_RADIUS;
        if (level().isClientSide()) {
            endPosX = getX() + radius * Math.cos(yHeadRotAngle) * Math.cos(xHeadRotAngle);
            endPosZ = getZ() + radius * Math.sin(yHeadRotAngle) * Math.cos(xHeadRotAngle);
            endPosY = getY() + radius * Math.sin(xHeadRotAngle);
        } else {
            endPosX = getX() + radius * Math.cos(getYHeadRotAngle()) * Math.cos(getXHeadRotAngle());
            endPosZ = getZ() + radius * Math.sin(getYHeadRotAngle()) * Math.cos(getXHeadRotAngle());
            endPosY = getY() + radius * Math.sin(getXHeadRotAngle());
        }
    }


    public LaserHitResult raytraceEntities(Level world, Vec3 from, Vec3 to) {
        LaserHitResult result = new LaserHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));

        if (result.getBlockHit() != null) {
            Vec3 hitVec = result.getBlockHit().getLocation();
            collidePosX = hitVec.x;
            collidePosY = hitVec.y;
            collidePosZ = hitVec.z;
            blockSide = result.getBlockHit().getDirection();
        } else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(getX(), collidePosX), Math.min(getY(), collidePosY), Math.min(getZ(), collidePosZ), Math.max(getX(), collidePosX), Math.max(getY(), collidePosY), Math.max(getZ(), collidePosZ)).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == this.caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }


    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return isPlayer() ? distance < (PLAYER_RADIUS * PLAYER_RADIUS) * 2 : distance < (GUARDIAN_RADIUS * GUARDIAN_RADIUS) * 2;
    }

    private void updateWithPlayer() {
        this.setYHeadRotAngle((float) Math.toRadians(caster.yHeadRot + 90));
        this.setXHeadRotAngle((float) Math.toRadians(-caster.getXRot()));
        Vec3 vecOffset = caster.getLookAngle().normalize().scale(1);
        this.setPos(caster.getX() + vecOffset.x(), caster.getY() + caster.getBbHeight() * 0.5F + vecOffset.y(), caster.getZ() + vecOffset.z());
    }

    private void updateWithGuardian() {
        double radians = Math.toRadians(this.caster.yHeadRot + 90);
        this.setYHeadRotAngle((float) radians);
        this.setXHeadRotAngle((float) ((double) (-this.caster.getXRot()) * Math.PI / 180.0));
        double offsetX = Math.cos(radians) * 1.35;
        double offsetZ = Math.sin(radians) * 1.35;
        this.setPos(this.caster.getX() + offsetX, this.caster.getY() + (caster.getBbHeight() * 0.75f), this.caster.getZ() + offsetZ);
    }

    public static class LaserHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
