package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public abstract class EntityMagicEffects extends Entity implements IEntity {
    public LivingEntity caster;
    private static final EntityDataAccessor<Integer> DATA_CASTER_ID = SynchedEntityData.defineId(EntityMagicEffects.class, EntityDataSerializers.INT);

    public EntityMagicEffects(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_CASTER_ID, -1);
    }

    public void setCasterId(int id) {
        entityData.set(DATA_CASTER_ID, id);
    }

    public int getCasterId() {
        return entityData.get(DATA_CASTER_ID);
    }

    @Override
    public void tick() {
        this.baseTick();
    }

    @Override//在实体上渲染火焰效果
    public boolean displayFireAnimation() {
        return false;
    }

    public List<LivingEntity> getNearByLivingEntities(double range) {
        return this.getNearByEntities(LivingEntity.class, range, range, range, range);
    }

    public List<LivingEntity> getNearByLivingEntities(double rangeX, double height, double rangeZ, double radius) {
        return this.getNearByEntities(LivingEntity.class, rangeX, height, rangeZ, radius);
    }

    public <T extends Entity> List<T> getNearByEntities(Class<T> entityClass, double x, double y, double z, double radius) {
        return level().getEntitiesOfClass(entityClass, getBoundingBox().inflate(x, y, z), targetEntity -> targetEntity != this &&
                distanceTo(targetEntity) <= radius + targetEntity.getBbWidth() / 2f && targetEntity.getY() <= getY() + y);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        //entityData.set(DATA_CASTER_ID, compoundTag.getInt("caster"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        //compoundTag.putInt("caster", getCasterId());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
