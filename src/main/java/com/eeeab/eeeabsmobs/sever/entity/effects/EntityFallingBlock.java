package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.mojang.math.Quaternion;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class EntityFallingBlock extends Entity {
    public static float DROP_FACTORS = 0.1f;
    private static final EntityDataAccessor<String> MODE = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> ANIM_V_Y = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<BlockState>> BLOCK_STATE = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.BLOCK_STATE);
    //Quaternionf
    private static final EntityDataAccessor<Float> X = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> Y = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> Z = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> W = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.FLOAT);
    public float animY = 0;
    public float prevAnimY = 0;

    public enum FallingMoveType {
        RENDER_MOVE, OVERALL_MOVE, SIMULATE_RUPTURE
    }

    public EntityFallingBlock(EntityType<EntityFallingBlock> entityType, Level level) {
        super(entityType, level);
        this.setDuration(20);
    }

    public EntityFallingBlock(Level worldIn, BlockState blockState, float vy) {
        super(EntityInit.FALLING_BLOCK.get(), worldIn);
        this.setMode(FallingMoveType.RENDER_MOVE);
        this.setBlockState(blockState);
        this.setAnimVY(vy);
    }

    public EntityFallingBlock(Level level, double px, double py, double pz, BlockState blockState, int duration) {
        super(EntityInit.FALLING_BLOCK.get(), level);
        this.setMode(FallingMoveType.OVERALL_MOVE);
        this.setBlockState(blockState);
        this.setPos(px, py + (double) ((1.0F - this.getBbHeight()) / 2.0F), pz);
        this.setDuration(duration);
        this.xo = px;
        this.yo = py;
        this.zo = pz;
        this.setDeltaMovement(Vec3.ZERO);
    }

    public EntityFallingBlock(Level level, BlockState blockState, Quaternion quaternionf, int duration, float vy) {
        super(EntityInit.FALLING_BLOCK.get(), level);
        this.setMode(FallingMoveType.SIMULATE_RUPTURE);
        this.setBlockState(blockState);
        this.setQuaternionf(quaternionf);
        this.setDuration(duration);
        this.setAnimVY(vy);
    }

    protected void defineSynchedData() {
        this.entityData.define(BLOCK_STATE, Optional.of(Blocks.DIRT.defaultBlockState()));
        this.entityData.define(MODE, FallingMoveType.RENDER_MOVE.toString());
        this.entityData.define(ANIM_V_Y, 1F);
        this.entityData.define(DURATION, 20);
        //Quaternionf
        this.entityData.define(X, 0F);
        this.entityData.define(Y, 0F);
        this.entityData.define(Z, 0F);
        this.entityData.define(W, 1F);
    }


    public void tick() {
        if (getMode() != FallingMoveType.OVERALL_MOVE) setDeltaMovement(0, 0, 0);
        super.tick();
        if (getMode() == FallingMoveType.OVERALL_MOVE) {
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, DROP_FACTORS / 2, 0.0D));
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
            if ((this.isOnGround() && this.tickCount > this.getDuration()) || this.tickCount > 300) {
                this.discard();
            }
        } else if (getMode() == FallingMoveType.RENDER_MOVE) {
            this.updateY(getAnimVY(), DROP_FACTORS);
        } else {
            if (this.level.isClientSide) {
                if (this.tickCount == 1 && this.random.nextInt(4) == 0) {
                    BlockState blockstate = this.getBlockState();
                    if (blockstate != null && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                        for (int i = 0; i < 3; ++i) {
                            double d0 = this.getX() + (double) Mth.randomBetween(this.random, -0.2F, 0.2F);
                            double d1 = this.getY() + 0.5;
                            double d2 = this.getZ() + (double) Mth.randomBetween(this.random, -0.2F, 0.2F);
                            this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }
            float animVY = getAnimVY();
            if (animVY < 0 && this.tickCount <= this.getDuration()) {
                //保持一致 避免渲染计算插值时导致抽搐问题
                prevAnimY = animY;
                return;
            }
            this.updateY(animVY, 0.2f);
        }
    }

    private void updateY(float animVY, float dropFactors) {
        prevAnimY = animY;
        animY += animVY;
        if (animY < -0.5) discard();
        setAnimVY(animVY - dropFactors);
    }


    @Override
    public void setDeltaMovement(double x, double y, double z) {
        if (getMode() == FallingMoveType.OVERALL_MOVE) {
            super.setDeltaMovement(x, y, z);
        }
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 deltaMovement) {
        if (getMode() == FallingMoveType.OVERALL_MOVE) {
            super.setDeltaMovement(deltaMovement);
        }
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        BlockState blockState = this.getBlockState();
        compoundTag.put("block_state", NbtUtils.writeBlockState(blockState));
        compoundTag.putInt("duration", getDuration());
        compoundTag.putInt("tickTimer", tickCount);
        compoundTag.putFloat("vy", getEntityData().get(ANIM_V_Y));
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setBlockState(NbtUtils.readBlockState(compoundTag.getCompound("block_state")));
        setDuration(compoundTag.getInt("duration"));
        tickCount = compoundTag.getInt("tickTimer");
        setAnimVY(compoundTag.getFloat("vy"));
    }

    public boolean displayFireAnimation() {
        return false;
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockState getBlockState() {
        return this.entityData.get(BLOCK_STATE).orElse(null);
    }

    public void setBlockState(BlockState p_270267_) {
        this.entityData.set(BLOCK_STATE, Optional.ofNullable(p_270267_));
    }

    public FallingMoveType getMode() {
        String mode = this.entityData.get(MODE);
        if (mode.isEmpty()) return FallingMoveType.RENDER_MOVE;
        return FallingMoveType.valueOf(mode);
    }

    public void setMode(FallingMoveType type) {
        this.entityData.set(MODE, type.toString());
    }

    public float getAnimVY() {
        return getEntityData().get(ANIM_V_Y);
    }

    private void setAnimVY(float vy) {
        getEntityData().set(ANIM_V_Y, vy);
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }

    public Quaternion getQuaternionf() {
        return new Quaternion(getEntityData().get(X), getEntityData().get(Y), getEntityData().get(Z), getEntityData().get(W));
    }

    public void setQuaternionf(Quaternion quaternionf) {
        getEntityData().set(X, quaternionf.i());
        getEntityData().set(Y, quaternionf.j());
        getEntityData().set(Z, quaternionf.k());
        getEntityData().set(W, quaternionf.r());
    }
}
