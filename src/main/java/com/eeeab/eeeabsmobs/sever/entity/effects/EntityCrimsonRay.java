package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityAbsCorpse;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityCrimsonRay extends EntityMagicEffects {
    public static final double ATTACK_RADIUS = 16;
    public static final int PRE_SHOOT_DURATION = 10;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float yaw, pitch;
    public float preYaw, prePitch;
    public ControlledAnimation displayControlled = new ControlledAnimation(3);
    public boolean ON = true;
    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(EntityCrimsonRay.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(EntityCrimsonRay.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(EntityCrimsonRay.class, EntityDataSerializers.INT);

    public EntityCrimsonRay(EntityType<? extends EntityCrimsonRay> type, Level level) {
        super(type, level);
        noCulling = true;
    }

    public EntityCrimsonRay(Level world, LivingEntity caster, Vec3 pos, int duration) {
        this(EntityInit.CRIMSON_RAY.get(), world);
        this.caster = caster;
        this.setPitch((float) (Math.PI / 2));
        this.setYaw((float) ((this.getYRot() - 90.0F) * Math.PI / 180.0F));
        this.setDuration(duration);
        this.setPos(pos);
        this.calculateEndPos();
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    public EntityCrimsonRay(Level world, LivingEntity caster, Vec3 pos, int duration, float pitch, float yaw) {
        this(world, caster, pos, duration);
        this.setPitch(pitch);
        this.setYaw(yaw);
        this.setDuration(duration);
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
        preYaw = yaw;
        prePitch = pitch;
        yaw = this.getYaw();
        pitch = this.getPitch();
        xo = this.getX();
        yo = this.getY();
        zo = this.getZ();
        if (this.tickCount == 1 && this.level().isClientSide) {
            this.caster = (LivingEntity) this.level().getEntity(getCasterId());
        }

        if (this.tickCount >= PRE_SHOOT_DURATION) {
            if (this.tickCount == PRE_SHOOT_DURATION) {
                this.playSound(SoundInit.CRIMSON_RAY.get(), 1F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
            this.calculateEndPos();
            List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ)).entities;
            if (blockSide != null) {
                if (!level().isClientSide) {
                    BlockPos minPos = new BlockPos(Mth.floor(this.collidePosX - 0.5), Mth.floor(this.collidePosY - 0.5), Mth.floor(this.collidePosZ - 0.5));
                    BlockPos maxPos = new BlockPos(Mth.floor(this.collidePosX + 0.5), Mth.floor(this.collidePosY + 0.5), Mth.floor(this.collidePosZ + 0.5));
                    BlockPos.betweenClosedStream(minPos, maxPos).forEach(pos -> {
                        if (ModEntityUtils.canDestroyBlock(this.level(), pos, this, 50) && ModEntityUtils.canMobDestroy(this)) {
                            this.level().destroyBlock(pos, false);
                        }
                    });
                } else {
                    this.spawnExplosionParticles();
                }
            }
            if (!this.level().isClientSide) {
                for (LivingEntity target : hit) {
                    if (target == this.caster || target instanceof EntityAbsCorpse && EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get())
                        continue;
                    target.hurt(this.damageSources().indirectMagic(target, caster), 5F + target.getMaxHealth() * 0.01F);
                }
            }
        }

        if ((!ON && displayControlled.isStop()) || (this.caster != null && !caster.isAlive())) {
            this.discard();
        }
        if (ON && this.isAccumulating()) {
            this.displayControlled.increaseTimer();
        } else {
            this.displayControlled.decreaseTimer();
        }
        if (this.tickCount - PRE_SHOOT_DURATION > this.getDuration()) {
            ON = false;
        }
    }

    public boolean isAccumulating() {
        return this.tickCount > PRE_SHOOT_DURATION;
    }

    private void spawnExplosionParticles() {
        for (int i = 0; i < 2; i++) {
            final float velocity = 0.12F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * -velocity;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.LARGE_SMOKE, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(YAW, 0F);
        getEntityData().define(PITCH, 0F);
        getEntityData().define(DURATION, 0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < Math.pow(ATTACK_RADIUS, 3);
    }

    public float getYaw() {
        return getEntityData().get(YAW);
    }

    public void setYaw(float rotAngle) {
        getEntityData().set(YAW, rotAngle);
    }

    public float getPitch() {
        return getEntityData().get(PITCH);
    }

    public void setPitch(float rotAngle) {
        getEntityData().set(PITCH, rotAngle);
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }

    private void calculateEndPos() {
        double radius = ATTACK_RADIUS;
        if (level().isClientSide()) {
            endPosX = getX() + radius * Math.cos(yaw) * Math.cos(pitch);
            endPosZ = getZ() + radius * Math.sin(yaw) * Math.cos(pitch);
            endPosY = getY() + radius * Math.sin(pitch);
        } else {
            endPosX = getX() + radius * Math.cos(getYaw()) * Math.cos(getPitch());
            endPosZ = getZ() + radius * Math.sin(getYaw()) * Math.cos(getPitch());
            endPosY = getY() + radius * Math.sin(getPitch());
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
