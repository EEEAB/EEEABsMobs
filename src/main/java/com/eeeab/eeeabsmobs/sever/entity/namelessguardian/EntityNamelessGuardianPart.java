package com.eeeab.eeeabsmobs.sever.entity.namelessguardian;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;

//参考自: package net.minecraft.world.entity.boss.EnderDragonPart
public class EntityNamelessGuardianPart extends PartEntity<EntityNamelessGuardian> {
    public final EntityNamelessGuardian guardian;
    public final String partName;
    private final EntityDimensions size;

    public EntityNamelessGuardianPart(EntityNamelessGuardian parent, String partName, float width, float height) {
        super(parent);
        this.size = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
        this.guardian = parent;
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
        this.setYRot(guardian.getYRot() % 360.0F);
        this.setXRot(guardian.getXRot() % 360.0F);
    }

    public boolean isPickable() {
        return true;
    }

    public boolean hurt(DamageSource source, float damage) {
        return !this.isInvulnerableTo(source) && this.guardian.hurt( source, damage);
    }

    public boolean is(Entity p_31031_) {
        return this == p_31031_ || this.guardian == p_31031_;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    public EntityDimensions getDimensions(Pose p_31023_) {
        return this.size.scale(1.0F);
    }

    public boolean shouldBeSaved() {
        return false;
    }
}
