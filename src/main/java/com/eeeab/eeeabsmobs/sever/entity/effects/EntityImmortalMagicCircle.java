package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityImmortalMagicCircle extends EntityMagicEffects {
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_SPEED = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_DURATION = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.STRING);
    public final ControlledAnimation processController = new ControlledAnimation(5);
    public boolean NO = true;

    public enum MagicCircleType {
        NONE, SPEED, STRENGTH, NEGATIVE, GUARD;
    }

    public EntityImmortalMagicCircle(EntityType<EntityImmortalMagicCircle> type, Level level) {
        super(type, level);
    }

    public EntityImmortalMagicCircle(Level level) {
        super(EntityInit.MAGIC_CIRCLE.get(), level);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        processController.updatePrevTimer();

        if (NO && processController.increaseTimerChain().isEnd()) {
            NO = false;
        }
        if (!NO && tickCount > getDuration()) {
            if (processController.decreaseTimerChain().isStop()) {
                discard();
            }
        }
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 deltaMovement) {
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SCALE, 1F);
        this.entityData.define(DATA_SPEED, 0F);
        this.entityData.define(DATA_DURATION, 20);
        this.entityData.define(DATA_TYPE, MagicCircleType.NONE.toString());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("scale", getScale());
        compoundTag.putFloat("speed", getSpeed());
        compoundTag.putInt("duration", getDuration());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setScale(compoundTag.getFloat("scale"));
        setSpeed(compoundTag.getFloat("speed"));
        setDuration(compoundTag.getInt("duration"));
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 2048;
    }

    public float getScale() {
        return entityData.get(DATA_SCALE);
    }

    public void setScale(float scale) {
        entityData.set(DATA_SCALE, scale);
    }

    public float getSpeed() {
        return entityData.get(DATA_SPEED);
    }

    public void setSpeed(float speed) {
        entityData.set(DATA_SPEED, speed);
    }

    public int getDuration() {
        return entityData.get(DATA_DURATION);
    }

    public void setDuration(int duration) {
        entityData.set(DATA_DURATION, duration);
    }

    public MagicCircleType getMagicCircleType() {
        String type = entityData.get(DATA_TYPE);
        if (type.isEmpty()) return MagicCircleType.NONE;
        return MagicCircleType.valueOf(type);
    }

    public void setMagicCircleType(MagicCircleType type) {
        entityData.set(DATA_TYPE, type.toString());
    }

    public static void spawn(Level level, Vec3 pos, float scale, float speed, int duration, MagicCircleType type) {
        if (!level.isClientSide) {
            EntityImmortalMagicCircle entity = new EntityImmortalMagicCircle(level);
            entity.setScale(scale);
            entity.setSpeed(speed);
            entity.setDuration(10 + duration);
            entity.setMagicCircleType(type);
            entity.setPos(pos);
            level.addFreshEntity(entity);
        }
    }
}
