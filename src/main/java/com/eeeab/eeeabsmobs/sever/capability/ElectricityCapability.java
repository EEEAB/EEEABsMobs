package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricityCapability {
    public static final ResourceLocation ID = new ResourceLocation(EEEABMobs.MOD_ID, "electricity_cap");

    public interface IElectricityCapability extends GeneralCapability, INBTSerializable<CompoundTag> {
    }

    public static class IElectricityCapabilityImpl implements IElectricityCapability {
        private boolean isElectrified;

        @Override
        public boolean flag() {
            return this.isElectrified;
        }

        @Override
        public void tick(LivingEntity entity) {
            if (this.isElectrified) {
                if (entity.level().isClientSide) {
                    if (entity.tickCount % 8 == 0) {
                        for (int i = 0; i < Math.min(entity.getBbHeight() * 3.5, 35); i++) {
                            entity.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), entity.getRandomX(0.6), entity.getY() + (entity.getRandom().nextFloat() * entity.getBbHeight()) * 0.8, entity.getRandomZ(0.6), 0D, 0.007D, 0D);
                        }
                    }
                }
            }
        }

        @Override
        public void onStart(LivingEntity entity) {
            this.isElectrified = true;
        }

        @Override
        public void onEnd(LivingEntity entity) {
            this.isElectrified = false;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("isElectrified", this.isElectrified);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.isElectrified = nbt.getBoolean("isElectrified");
        }
    }

    //能力提供器
    public static class ElectricityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<ElectricityCapability.IElectricityCapabilityImpl> instance = LazyOptional.of(ElectricityCapability.IElectricityCapabilityImpl::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return HandlerCapability.ELECTRICITY_CAPABILITY.orEmpty(cap, instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
