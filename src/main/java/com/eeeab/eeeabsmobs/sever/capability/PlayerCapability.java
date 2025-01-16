package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseToPlayer;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.integration.curios.ICuriosApi;
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
        private int SSNCoolingTime;

        @Override
        public void tick(Player player) {
            if (this.SSNCoolingTime > 0) {
                this.SSNCoolingTime--;
            }
        }

        @Override
        public void hurt(Player player, DamageSource source, float damage) {
            Entity entity = source.getEntity();
            if (this.SSNCoolingTime <= 0 && entity != null) {
                Item item = ItemInit.SOUL_SUMMONING_NECKLACE.get();
                if (ICuriosApi.isLoaded()) {
                    if (ICuriosApi.INSTANCE.isPresentInventory(player, item)) {
                        this.doSSNCapability(player, entity, item, damage);
                    }
                } else if (this.ifPresentItem(player, item.getDefaultInstance())) {
                    this.doSSNCapability(player, entity, item, damage);
                }

            }
        }

        /**
         * 唤魂项链能力
         *
         * @param player 玩家
         * @param target 伤害源实体
         * @param item   唤魂项链
         * @param damage 造成的伤害值
         */
        private void doSSNCapability(Player player, Entity target, Item item, float damage) {
            if (!player.getCooldowns().isOnCooldown(item)) {
                this.SSNCumulativeDamage += damage;
                if (this.SSNCumulativeDamage >= EMConfigHandler.COMMON.ITEM.SSNCumulativeMaximumDamage.get().floatValue()) {
                    player.getCooldowns().addCooldown(item, (int) (EMConfigHandler.COMMON.ITEM.SSNCoolingTime.get() * 20));
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
                    this.SSNCoolingTime = 10;
                    this.SSNCumulativeDamage = 0;
                }
            }
        }

        /**
         * 检查玩家背包内是否包含某个物品
         *
         * @param player    玩家
         * @param itemStack 物品栈 包含物品标签和物品本身
         * @return 是否存在
         */
        private boolean ifPresentItem(Player player, ItemStack itemStack) {
            return player.getInventory().contains(itemStack);
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
        private final LazyOptional<PlayerCapabilityImpl> instance = LazyOptional.of(PlayerCapabilityImpl::new);

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
