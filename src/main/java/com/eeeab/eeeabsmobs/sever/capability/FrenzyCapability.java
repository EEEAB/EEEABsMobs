package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

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
        private final UUID ATTACK_UUID = UUID.fromString("3039A932-0AF9-E41C-2DD5-996DCCF1E8A0");
        private final UUID SPEED_UUID = UUID.fromString("CB12110E-C2D2-2E2B-3F2E-97956CB5A564");
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
                AttributeInstance attack = entity.getAttribute(Attributes.ATTACK_DAMAGE);
                AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attack != null && speed != null) {
                    int i = Math.min(this.level + 1, 5);
                    double attackValue = attack.getBaseValue() * 0.2 * i;
                    //如果效果持有者是玩家时，增加的攻击力采用固定数值
                    if (entity instanceof Player) attackValue = i;
                    double speedValue = speed.getBaseValue() * 0.2 * i;
                    attack.removePermanentModifier(ATTACK_UUID);
                    speed.removePermanentModifier(SPEED_UUID);
                    attack.addPermanentModifier(new AttributeModifier(ATTACK_UUID, "Add frenzy attack", attackValue, AttributeModifier.Operation.ADDITION));
                    speed.addPermanentModifier(new AttributeModifier(SPEED_UUID, "Add frenzy speed", speedValue, AttributeModifier.Operation.ADDITION));
                }
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            if (this.isFrenzy()) {
                MobEffectInstance effect = entity.getEffect(EffectInit.FRENZY_EFFECT.get());
                if (effect != null) {
                    int duration = effect.getDuration();
                    //副作用：每一级增加2.5秒的持续时间
                    int durationTick = 50 * (this.level + 1);
                    if (duration <= 10 && duration >= 0) {
                        if (entity instanceof Player player) {
                            //当玩家持有唤魂项链时 副作用持续时间减少一半
                            if (player.getInventory().items.stream().anyMatch(i -> i.is(ItemInit.SOUL_SUMMONING_NECKLACE.get()))) {
                                player.getCooldowns().addCooldown(ItemInit.SOUL_SUMMONING_NECKLACE.get(), 20);
                                durationTick /= 2;
                            }
                        }
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTick, 1, false, true, true));
                        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, durationTick, this.level, false, true, true));
                        entity.addEffect(new MobEffectInstance(EffectInit.ARMOR_LOWER_EFFECT.get(), durationTick, this.level, false, true, true));
                        return;
                    }
                }
                if (entity instanceof Player player) {
                    if (this.count <= 0 && player.tickCount % 10 == 0) player.getFoodData().eat(1, 1F);
                    if (player.isSprinting() && !player.getUseItem().getItem().isEdible()) {
                        if (this.count < 20) this.count++;
                    } else this.count--;
                    if (this.count >= 20 && player.getFoodData().getFoodLevel() > 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, this.level, false, false, true));
                        player.causeFoodExhaustion(0.25F);
                        boolean checkCanDestroyBlock = EMConfigHandler.COMMON.OTHER.enableFrenzyDestroyBlock.get();
                        if (!entity.level().isClientSide && checkCanDestroyBlock) {
                            AABB bb = entity.getBoundingBox();
                            int minx = Mth.floor(bb.minX - 2.75F);
                            int miny = Mth.floor(bb.minY + 0.15D);
                            int minz = Mth.floor(bb.minZ - 2.75F);
                            BlockPos min = new BlockPos(minx, miny, minz);
                            int maxx = Mth.floor(bb.maxX + 3.75F);
                            int maxy = Mth.floor(bb.maxY + 0.5D);
                            int maxz = Mth.floor(bb.maxZ + 2.75F);
                            BlockPos max = new BlockPos(maxx, maxy, maxz);
                            if (entity.level().hasChunksAt(min, max)) {
                                BlockPos.betweenClosedStream(min, max).
                                        filter((pos) -> ModEntityUtils.canDestroyBlock(entity.level(), pos, entity, 2F) && entity.level().getBlockEntity(pos) == null).
                                        forEach((pos) -> entity.level().destroyBlock(pos, false));
                            }
                        }
                        List<LivingEntity> entities = player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(1F, 0.2F, 1F));
                        for (LivingEntity hitEntity : entities) {
                            if (hitEntity == player) continue;
                            boolean hitFlag = false;
                            if (player.isSprinting()) {
                                hitFlag = hitEntity.hurt(hitEntity.damageSources().mobAttack(player), this.level);
                            }
                            //如果命中目标 则施加眩晕效果
                            if (hitFlag) {
                                double angle = this.getAngleBetweenEntities(entity, hitEntity);
                                double x = 3F * Math.cos(Math.toRadians(angle - 90));
                                double z = 3F * Math.sin(Math.toRadians(angle - 90));
                                hitEntity.setDeltaMovement(x, 0.35, z);
                                hitEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 20, 0, false, false, true));
                            }
                        }
                        if (player.level().isClientSide) {
                            double x = player.getX();
                            double y = player.getY() + player.getBbHeight() / 2;
                            double z = player.getZ();
                            double motionX = player.getDeltaMovement().x;
                            double motionY = player.getDeltaMovement().y;
                            double motionZ = player.getDeltaMovement().z;
                            if (player.tickCount % 3 == 0) {
                                player.level().addParticle(new ParticleRing.RingData((float) Math.toRadians(-player.yBodyRot), 0, 30, 0.8f, 0.8f, 0.9f, 0.08f, 32f, false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), x + 8f * motionX, y + 1.5f * motionY, z + 8f * motionZ, 0, 0, 0);
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
            if (entity != null && this.isFrenzy()) {
                this.count = 0;
                this.isFrenzy = false;
                AttributeInstance attack = entity.getAttribute(Attributes.ATTACK_DAMAGE);
                AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attack != null && speed != null) {
                    attack.removePermanentModifier(ATTACK_UUID);
                    speed.removePermanentModifier(SPEED_UUID);
                }
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("is_frenzy", this.isFrenzy);
            nbt.putInt("level", this.level);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.isFrenzy = nbt.getBoolean("is_frenzy");
            this.level = nbt.getInt("level");
        }
    }

    //能力提供器
    public static class FrenzyCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<FrenzyCapability.FrenzyCapabilityImpl> instance = LazyOptional.of(FrenzyCapability.FrenzyCapabilityImpl::new);

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
