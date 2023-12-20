package com.eeeab.eeeabsmobs.sever.entity.impl.effect;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityScorch extends EntityMagicEffects {
    //private static final int ALIVE_TIME = EEConfigHandler.COMMON.EXPERIMENTAL_ENTITY.SCORCH.existTimer.get() * 20;
    private static final int ALIVE_TIME = 20;
    public final ControlledAnimation controlled = new ControlledAnimation(20);

    public EntityScorch(EntityType<? extends EntityScorch> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public EntityScorch(Level level, double x, double y, double z) {
        this(EntityInit.SCORCH.get(), level);
        this.setPos(x, y, z);
    }


    @Override
    public void tick() {
        this.baseTick();
        if (tickCount > ALIVE_TIME) {
            controlled.decreaseTimer();
        } else {
            controlled.increaseTimer(4);
            if (tickCount % 5 == 0 && level().isClientSide) {
                final float velocity = 0.12F;
                float yaw = (float) (random.nextFloat() * 2 * Math.PI);
                float motionY = random.nextFloat() * velocity;
                float motionX = velocity * Mth.cos(yaw) * 0.5F;
                float motionZ = velocity * Mth.sin(yaw) * 0.5F;
                level().addParticle(ParticleTypes.LARGE_SMOKE, getX(), getY() + 0.05F, getZ(), motionX, motionY * 1.2F, motionZ);
            }
        }
        if (controlled.getTimer() == 0 || isInFluidType()) {
            discard();
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 32 * 32;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("tickTimer", tickCount);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        tickCount = compoundTag.getInt("tickTimer");
    }
}
