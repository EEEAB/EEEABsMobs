package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityTelegraph extends Entity implements IEntity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(EntityTelegraph.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(EntityTelegraph.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DURATION = SynchedEntityData.defineId(EntityTelegraph.class, EntityDataSerializers.INT);
    public ControlledAnimation interimControlled = new ControlledAnimation(10);
    public ControlledAnimation controlled;

    public EntityTelegraph(EntityType<?> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.setNoGravity(true);
    }

    public EntityTelegraph(Level level, Vec3 pos, int duration, int color, float radius) {
        this(EntityInit.TELEGRAPH.get(), level);
        this.entityData.set(DATA_RADIUS, radius);
        this.entityData.set(DATA_COLOR, color);
        this.entityData.set(DATA_DURATION, Math.max(duration, 1));
        this.setPos(pos);
    }

    @Override
    public void tick() {
        if (this.controlled == null) this.controlled = new ControlledAnimation(this.entityData.get(DATA_DURATION));
        super.tick();
        this.controlled.updatePrevTimer();
        this.interimControlled.updatePrevTimer();
        if (this.controlled.isEnd()) {
            if (this.interimControlled.isEnd()) {
                this.discard();
            }
            this.interimControlled.increaseTimer();
        } else {
            this.controlled.increaseTimer();
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_RADIUS, 0F);
        this.entityData.define(DATA_COLOR, 0);
        this.entityData.define(DATA_DURATION, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    public float getRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    public int getColor() {
        return this.entityData.get(DATA_COLOR);
    }

    public static void spawn(Level level, Vec3 pos, int duration, int color, float radius) {
        if (!level.isClientSide) {
            EntityTelegraph telegraph = new EntityTelegraph(level, pos, duration, color, radius);
            level.addFreshEntity(telegraph);
        }
    }
}
