package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.integration.curios.ICuriosApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrenzyCapability {
    public static final ResourceLocation ID = new ResourceLocation(EEEABMobs.MOD_ID, "frenzy_cap");

    public interface IFrenzyCapability extends INBTSerializable<CompoundTag> {
        boolean isFrenzy();

        void setLevel(int level);

        int getLevel();

        void onStart(LivingEntity entity);

        void onEnd(LivingEntity entity);

        void tick(LivingEntity entity);
    }

    public static class FrenzyCapabilityImpl implements IFrenzyCapability {
        private boolean isFrenzy;
        private long count;
        private int level;

        @Override
        public boolean isFrenzy() {
            return this.isFrenzy;
        }

        @Override
        public void setLevel(int level) {
            this.level = level;
        }


        @Override
        public int getLevel() {
            return this.level;
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
                if (entity instanceof Player player) {
                    player.setAirSupply(player.getMaxAirSupply());
                    if (this.count <= 0 && player.tickCount % 10 == 0) player.getFoodData().eat(1, 1F);
                    if (player.isSprinting() && !player.getUseItem().getItem().isEdible()) {
                        if (this.count < 20) this.count++;
                    } else if (this.count > 0) this.count--;
                    if (this.count >= 20 && player.getFoodData().getFoodLevel() > 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, this.level, false, false, true));
                        player.causeFoodExhaustion(0.25F);
                        boolean inWater = player.isInWater() || player.isInFluidType((fluidType, height) -> player.canSwimInFluidType(fluidType));
                        if (inWater && player.getDeltaMovement().horizontalDistanceSqr() < 0.46D) {
                            Vec3 movement = player.getDeltaMovement().scale(1.05);
                            player.setDeltaMovement(movement);
                        }
                        boolean checkCanDestroyBlock = EMConfigHandler.COMMON.OTHER.enableFrenzyDestroyBlock.get();
                        if (!player.level.isClientSide && !inWater && checkCanDestroyBlock) {
                            Vec3 playerPos = player.position();
                            float yaw = player.getYRot() * ((float) Math.PI / 180F);
                            double forwardX = -Math.sin(yaw);
                            double forwardZ = Math.cos(yaw);
                            int side = (int) (player.getBbWidth() + 1.5);
                            int forward = (int) (player.getBbWidth() + 2.5);
                            for (int x = -side; x <= side; x++) {
                                for (int z = -side; z <= side; z++) {
                                    for (int y = 0; y < player.getBbHeight() + 1; y++) {
                                        double posX = playerPos.x + forwardX * forward + -forwardZ * x;
                                        double posY = playerPos.y + y;
                                        double posZ = playerPos.z + forwardZ * forward + forwardX * z;
                                        BlockPos pos = new BlockPos(posX, posY, posZ);
                                        if (ModEntityUtils.canDestroyBlock(player.level, pos, player, 2F) && player.level.getBlockEntity(pos) == null) {
                                            player.level.destroyBlock(pos, false);
                                        }
                                    }
                                }
                            }
                        }
                        List<LivingEntity> entities = player.level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getBoundingBox());
                        for (LivingEntity hitEntity : entities) {
                            if (hitEntity == player) continue;
                            //如果命中目标 则施加眩晕效果
                            if (hitEntity.hurt(DamageSource.playerAttack(player), this.level)) {
                                double angle = this.getAngleBetweenEntities(entity, hitEntity);
                                double x = 3F * Math.cos(Math.toRadians(angle - 90));
                                double z = 3F * Math.sin(Math.toRadians(angle - 90));
                                hitEntity.setDeltaMovement(x, 0.35, z);
                                hitEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 20, 0, false, false, true));
                            }
                        }
                        if (player.level.isClientSide) {
                            double x = player.getX();
                            double y = player.getY() + player.getBbHeight() / 2;
                            double z = player.getZ();
                            double motionX = player.getDeltaMovement().x;
                            double motionY = player.getDeltaMovement().y;
                            double motionZ = player.getDeltaMovement().z;
                            if (player.tickCount % 3 == 0) {
                                if (inWater) {
                                    for (int i = 0; i < 3; i++) {
                                        player.level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, player.getRandomX(1.5), player.getY(0.5), player.getRandomZ(1.5), -motionX * 0.25, -motionY * 0.25, -motionZ * 0.25);
                                    }
                                } else {
                                    player.level.addParticle(new ParticleRing.RingData((float) Math.toRadians(-player.yBodyRot), 0, 30, 0.8f, 0.8f, 0.9f, 0.08f, 32f, false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), x + 8f * motionX, y + 1.5f * motionY, z + 8f * motionZ, 0, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        //获取实体之间的角度
        public double getAngleBetweenEntities(Entity attacker, Entity target) {
            return Math.atan2(target.getZ() - attacker.getZ(), target.getX() - attacker.getX()) * (180 / Math.PI) + 90;
        }

        @Override
        public void onEnd(LivingEntity entity) {
            if (entity != null && this.isFrenzy) {
                this.count = 0;
                this.isFrenzy = false;
                if (!entity.level.isClientSide) {
                    //副作用：每一级增加6秒的持续时间
                    int durationTick = 120 * (this.level + 1);
                    if (entity instanceof Player player) {
                        //当玩家持有唤魂项链时 将不会有副作用
                        Item item = ItemInit.SOUL_SUMMONING_NECKLACE.get();
                        if (ICuriosApi.isLoaded()) {
                            if (ICuriosApi.INSTANCE.isPresentInventory(player, item)) {
                                return;
                            }
                        } else if (player.getInventory().items.stream().anyMatch(i -> i.is(item))) {
                            return;
                        }
                    }
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTick, 1, false, true, true));
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, durationTick, this.level, false, true, true));
                    entity.addEffect(new MobEffectInstance(EffectInit.ARMOR_LOWER_EFFECT.get(), durationTick, this.level, false, true, true));
                }
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("isFrenzy", this.isFrenzy);
            nbt.putInt("level", this.level);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.isFrenzy = nbt.getBoolean("isFrenzy");
            this.level = nbt.getInt("level");
        }
    }

    //能力提供器
    public static class FrenzyCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<FrenzyCapabilityImpl> instance = LazyOptional.of(FrenzyCapabilityImpl::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return HandlerCapability.FRENZY_EFFECT_CAPABILITY.orEmpty(cap, instance.cast());
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
