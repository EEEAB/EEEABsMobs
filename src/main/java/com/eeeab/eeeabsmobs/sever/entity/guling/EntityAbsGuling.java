package com.eeeab.eeeabsmobs.sever.entity.guling;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public abstract class EntityAbsGuling extends EEEABMobLibrary implements Enemy {
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityAbsGuling.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ALWAYS_ACTIVE = SynchedEntityData.defineId(EntityAbsGuling.class, EntityDataSerializers.BOOLEAN);

    public EntityAbsGuling(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        MobEffect effect = effectInstance.getEffect();
        return MobEffects.POISON != effect && MobEffects.MOVEMENT_SLOWDOWN != effect && super.canBeAffected(effectInstance);
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return this.isActive();
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.getEntity() instanceof EntityAbsGuling && EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get()) return false;
        if ((!active || getTarget() == null) && source.getEntity() instanceof LivingEntity livingEntity
                && !(livingEntity instanceof Player player && player.isCreative() || this.level().getDifficulty() == Difficulty.PEACEFUL)
                && (!EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get() || !(livingEntity instanceof EntityAbsGuling))) {
            this.setLastHurtByMob(livingEntity);
        }
        if (source.is(DamageTypeTags.IS_EXPLOSION)) damage *= 0.5F;
        return super.hurt(source, damage);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.5D), this.getRandomY(), this.getRandomZ(1.5D), d0, d1, d2);
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof EntityAbsGuling) {
            return EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get() || (this.getTeam() == null && entity.getTeam() == null);
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        return groupData;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, true);
        this.entityData.define(DATA_ALWAYS_ACTIVE, true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_ACTIVE, compound.getBoolean("isActive"));
        this.entityData.set(DATA_ALWAYS_ACTIVE, compound.getBoolean("isAlwaysActive"));
        this.active = this.isActive();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isActive", this.entityData.get(DATA_ACTIVE));
        compound.putBoolean("isAlwaysActive", this.entityData.get(DATA_ALWAYS_ACTIVE));
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
    }

    public boolean isAlwaysActive() {
        return this.entityData.get(DATA_ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean alwaysActive) {
        this.entityData.set(DATA_ALWAYS_ACTIVE, alwaysActive);
    }

    protected int getCoolingTimerUtil(int maxCooling, int minCooling, float healthPercentage) {
        float maximumCoolingPercentage = 1 - healthPercentage;
        float ratio = 1 - (this.getHealthPercentage() / 100);
        if (ratio > maximumCoolingPercentage) {
            ratio = maximumCoolingPercentage;
        }
        return (int) (maxCooling - (ratio / maximumCoolingPercentage) * (maxCooling - minCooling));
    }

    protected void doWalkEffect(int amount) {
        if (this.level().isClientSide) {
            for (int l = 0; l < amount; l++) {
                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY() - (double) 0.2F);
                int k = Mth.floor(this.getZ());
                BlockPos pos = new BlockPos(i, j, k);
                BlockState blockstate = this.level().getBlockState(pos);
                if (!blockstate.isAir()) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos),
                            this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            this.getY() + 0.1D, this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
                }
            }
        }
    }
}
