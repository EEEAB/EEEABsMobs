package com.eeeab.eeeabsmobs.sever.entity.impl.effect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;

public class EntityMovingController extends Entity {
    public EntityMovingController(EntityType<? extends EntityMovingController> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && tickCount >= 70 && !isVehicle()) discard();
    }


    //骑手将不会呈现坐下的姿势
    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    //是否与被骑乘实体互动
    @Override
    public boolean canRiderInteract() {
        return false;
    }


    //骑手与被骑乘实体的偏移量
    @Override
    public double getPassengersRidingOffset() {
        return 0;
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //在流体中也能骑乘该实体
    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }

    @Override
    public void positionRider(Entity passenger,Entity.MoveFunction function) {
        if (this.hasPassenger(passenger)) {
            if (passenger instanceof Player) passenger.setPos(this.getX(), this.getY(), this.getZ());
            else passenger.absMoveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }
}
