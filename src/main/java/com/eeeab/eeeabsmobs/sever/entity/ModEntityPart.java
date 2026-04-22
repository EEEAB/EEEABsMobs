package com.eeeab.eeeabsmobs.sever.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;

//参考自: package net.minecraft.world.entity.boss.EnderDragonPart
public class ModEntityPart<T extends LivingEntity> extends PartEntity<T> {
    public final T entity;
    public final String partName;
    private final EntityDimensions size;

    public ModEntityPart(T parent, String partName, float width, float height) {
        super(parent);
        this.size = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
        this.entity = parent;
        this.partName = partName;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {
    }

    @Override
    protected void setRot(float yaw, float pitch) {
        this.setYRot(entity.getYRot() % 360.0F);
        this.setXRot(entity.getXRot() % 360.0F);
    }

    public boolean isPickable() {
        return true;
    }

    public boolean hurt(DamageSource source, float damage) {
        return !this.isInvulnerableTo(source) && this.entity.hurt(source, damage);
    }

    public boolean is(Entity entity) {
        return this == entity || this.entity == entity;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    public EntityDimensions getDimensions(Pose pose) {
        return this.size.scale(1.0F);
    }

    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || (source.getEntity() != null && entity.isAlliedTo(source.getEntity()));
    }
}
