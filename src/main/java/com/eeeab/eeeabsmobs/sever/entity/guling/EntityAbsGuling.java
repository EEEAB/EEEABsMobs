package com.eeeab.eeeabsmobs.sever.entity.guling;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class EntityAbsGuling extends EEEABMobLibrary {
    public EntityAbsGuling(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.getEntity() instanceof EntityAbsGuling && EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get()) {
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.5D), this.getRandomY(), this.getRandomZ(1.5D), d0, d1, d2);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        return groupData;
    }

    protected void doWalkEffect(int amount) {
        if (this.level.isClientSide) {
            for (int l = 0; l < amount; l++) {
                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY() - (double) 0.2F);
                int k = Mth.floor(this.getZ());
                BlockPos pos = new BlockPos(i, j, k);
                BlockState blockstate = this.level.getBlockState(pos);
                if (!blockstate.isAir()) {
                    this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos),
                            this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            this.getY() + 0.1D, this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
                }
            }
        }
    }
}
