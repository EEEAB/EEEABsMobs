package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.integration.curios.ICuriosApi;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrenzyCapability {
    public static final ResourceLocation ID = new ResourceLocation(EEEABMobs.MOD_ID, "frenzy_cap");

    public interface IFrenzyCapability extends GeneralCapability, INBTSerializable<CompoundTag> {
    }

    public static class FrenzyCapabilityImpl implements IFrenzyCapability {
        private boolean isFrenzy;

        @Override
        public boolean flag() {
            return this.isFrenzy;
        }

        @Override
        public void onStart(LivingEntity entity) {
            if (entity != null) {
                this.isFrenzy = true;
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            if (this.isFrenzy) {
                if (!entity.level().isClientSide) {
                    MobEffectInstance effect = entity.getEffect(EffectInit.FRENZY_EFFECT.get());
                    if (effect != null && effect.endsWithin(1)) {
                        //副作用：每一级增加6秒的持续时间
                        int amplifier = effect.getAmplifier();
                        int durationTick = 120 * (amplifier + 1);
                        if (entity instanceof Player player) {
                            //当玩家持有唤魂项链时 将不会有副作用
                            Item item = ItemInit.SOUL_SUMMON_NECKLACE.get();
                            if (ICuriosApi.isLoaded()) {
                                if (ICuriosApi.INSTANCE.isPresentInventory(player, item)) {
                                    return;
                                }
                            } else if (player.getInventory().items.stream().anyMatch(i -> i.is(item))) {
                                return;
                            }
                        }
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTick, 1, false, true, true));
                        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, durationTick, amplifier, false, true, true));
                        entity.addEffect(new MobEffectInstance(EffectInit.ARMOR_LOWER_EFFECT.get(), durationTick, amplifier, false, true, true));
                    }
                }
                if (entity instanceof Player player) {
                    player.causeFoodExhaustion(0.05F);
                }
            }
        }

        @Override
        public void onEnd(LivingEntity entity) {
            if (entity != null && this.isFrenzy) {
                this.isFrenzy = false;
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("isFrenzy", this.isFrenzy);
            //nbt.putInt("level", this.level);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.isFrenzy = nbt.getBoolean("isFrenzy");
            //this.level = nbt.getInt("level");
        }
    }

    //能力提供器
    public static class FrenzyCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<FrenzyCapability.FrenzyCapabilityImpl> instance = LazyOptional.of(FrenzyCapability.FrenzyCapabilityImpl::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CapabilityHandler.FRENZY_CAPABILITY.orEmpty(cap, instance.cast());
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
