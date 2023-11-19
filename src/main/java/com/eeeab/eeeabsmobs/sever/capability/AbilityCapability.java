package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//能力处理器
public class AbilityCapability {
    public static ResourceLocation ID = new ResourceLocation(EEEABMobs.MOD_ID, "ability_processor");

    public interface IAbilityCapability extends INBTSerializable<CompoundTag> {
        void onActive(LivingEntity entity, AbilityType<?, ?> type);
        //能力初始化数据
        void onInit(LivingEntity entity);

        void tick(LivingEntity entity);

        AbilityType<?, ?>[] getAbilityTypeByEntity(LivingEntity entity);

        Map<AbilityType<?, ?>, Ability<?>> getAbilitiesMap();

        Collection<Ability<?>> getAbilities();

        Ability<?> getAbility();

        void setAbility(Ability<?> ability);

        boolean checkConflicting();
    }

    public static class AbilityCapabilityImp implements IAbilityCapability {
        TreeMap<AbilityType<?, ?>, Ability<?>> abilitiesInstances = new TreeMap<>();
        Ability<?> ability = null;
        Map<String, Tag> nbtMap = new HashMap<>();


        @Override
        public void onInit(LivingEntity entity) {
            for (AbilityType<? extends LivingEntity, ?> abilityType : getAbilityTypeByEntity(entity)) {
                Ability<? extends LivingEntity> instance = abilityType.getInstance(entity);
                abilitiesInstances.put(abilityType, instance);
                //读取nbt
                if (nbtMap.containsKey(abilityType.getName())) instance.readNBT(nbtMap.get(abilityType.getName()));
            }
        }

        @Override
        public void onActive(LivingEntity entity, AbilityType<?, ?> type) {
            Ability<?> ability = abilitiesInstances.get(type);
            if (ability != null) {
                ability.start();
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            //遍历能力
            for (Ability<?> ability : abilitiesInstances.values()) {
                ability.tick();
            }
        }

        //获取能力
        @Override
        public AbilityType<?, ?>[] getAbilityTypeByEntity(LivingEntity entity) {
            //获取玩家能力
            if (entity instanceof Player) {
                return AbilityHandler.PLAYER_ABILITY_TYPES;
            }
            return new AbilityType[0];
        }

        @Override
        public Map<AbilityType<?, ?>, Ability<?>> getAbilitiesMap() {
            return abilitiesInstances;
        }

        @Override
        public Collection<Ability<?>> getAbilities() {
            return abilitiesInstances.values();
        }

        @Override
        public Ability<?> getAbility() {
            return ability;
        }

        @Override
        public void setAbility(Ability<?> ability) {
            this.ability = ability;
        }

        @Override
        public boolean checkConflicting() {
            return getAbility() != null;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            for (Map.Entry<AbilityType<?, ?>, Ability<?>> abilityNbt : getAbilitiesMap().entrySet()) {
                CompoundTag writeNbt = abilityNbt.getValue().writeNBT();
                if (!writeNbt.isEmpty()) {
                    compound.put(abilityNbt.getKey().getName(), writeNbt);
                }
            }
            return compound;
        }


        @Override
        public void deserializeNBT(CompoundTag nbt) {
            Set<String> nbtSet = nbt.getAllKeys();
            for (String key : nbtSet) {
                nbtMap.put(key, nbt.get(key));
            }
        }
    }

    //能力提供器
    public static class AbilityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<AbilityCapabilityImp> instance = LazyOptional.of(AbilityCapabilityImp::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return HandlerCapability.CUSTOM_ABILITY_CAPABILITY.orEmpty(cap, instance.cast());
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
