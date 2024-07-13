package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseToPlayer;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCapability {
    public static ResourceLocation ID = new ResourceLocation(EEEABMobs.MOD_ID, "player_cap");

    public interface IPlayerCapability extends INBTSerializable<CompoundTag> {

        void tick(Player player);

        void hurt(Player player, DamageSource source, float damage);
    }

    public static class PlayerCapabilityImpl implements IPlayerCapability {
        //唤魂项链累计伤害
        private float SSNCumulativeDamage;
        //唤魂项链内置CD
        private int SSNInvulnerableTime;

        @Override
        public void tick(Player player) {
            if (SSNInvulnerableTime > 0) {
                SSNInvulnerableTime--;
            }
        }

        @Override
        public void hurt(Player player, DamageSource source, float damage) {
            if (/*只计算由实体造成伤害*/source.getEntity() != null && SSNInvulnerableTime <= 0) {
                this.SSNInvulnerableTime = 10;
                for (ItemStack stack : player.getInventory().items) {
                    Item item = stack.getItem();
                    if (stack.is(ItemInit.SOUL_SUMMONING_NECKLACE.get())) {
                        if (!player.getCooldowns().isOnCooldown(item)) {
                            this.SSNCumulativeDamage += damage;
                            if (this.SSNCumulativeDamage >= EMConfigHandler.COMMON.ITEM.SSNCumulativeMaximumDamage.get().floatValue()) {
                                this.doSpawnCorpse(player, source.getEntity());
                                player.getCooldowns().addCooldown(item, EMConfigHandler.COMMON.ITEM.SSNCoolingTime.get() * 20);
                                this.SSNCumulativeDamage = 0;
                            }
                        }
                        return;//减少不必要的遍历
                    }
                }
            }
        }

        //唤魂项链召唤死尸
        private void doSpawnCorpse(Player player, Entity target) {
            if (!player.level().isClientSide) {
                Vec3 vec3 = player.position();
                EntityCorpseToPlayer entity = EntityInit.CORPSE_TO_PLAYER.get().create(player.level());
                if (entity != null) {
                    entity.setInitSpawn();
                    entity.finalizeSpawn((ServerLevel) player.level(), player.level().getCurrentDifficultyAt(BlockPos.containing(vec3.x, vec3.y, vec3.z)), MobSpawnType.MOB_SUMMONED, null, null);
                    entity.moveTo(vec3);
                    entity.setOwner(player);
                    if (player != target && target instanceof LivingEntity livingEntity)
                        entity.setTarget(livingEntity);
                    player.level().addFreshEntity(entity);
                }
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            return new CompoundTag();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
        }
    }

    //能力提供器
    public static class PlayerCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<PlayerCapability.PlayerCapabilityImpl> instance = LazyOptional.of(PlayerCapability.PlayerCapabilityImpl::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return HandlerCapability.PLAYER_CAPABILITY.orEmpty(cap, instance.cast());
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
