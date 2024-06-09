package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
                    double speedValue = speed.getBaseValue() * 0.2 * i;
                    attack.removeModifier(ATTACK_UUID);
                    speed.removeModifier(SPEED_UUID);
                    attack.addTransientModifier(new AttributeModifier(ATTACK_UUID, "Add frenzy attack", attackValue, AttributeModifier.Operation.ADDITION));
                    speed.addTransientModifier(new AttributeModifier(SPEED_UUID, "Add frenzy speed", speedValue, AttributeModifier.Operation.ADDITION));
                }
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            if (this.isFrenzy()) {
                MobEffectInstance effect = entity.getEffect(EffectInit.FRENZY_EFFECT.get());
                if (effect != null) {
                    int duration = effect.getDuration();
                    if (duration <= 10 && duration >= 0) {
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200 * (this.level + 1), this.level, false, true, true));
                        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200 * (this.level + 1), this.level, false, true, true));
                        entity.addEffect(new MobEffectInstance(EffectInit.ARMOR_LOWER_EFFECT.get(), 200 * (this.level + 1), this.level, false, true, true));
                        return;
                    }
                }
                if (entity instanceof Player player) {
                    if (player.isSprinting()) this.count++;
                    else this.count = 0;
                    if (this.count > 20) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, this.level, false, false, true));
                        //TODO 冲撞伤害待完善
                        //List<LivingEntity> entities = player.level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getBoundingBox());
                        //for (LivingEntity hitEntity : entities) {
                        //    if (hitEntity == player) continue;
                        //    if (player.isSprinting()) {
                        //        hitEntity.hurt(hitEntity.damageSources().flyIntoWall(), this.level);
                        //    }
                        //}
                        if (player.level.isClientSide) {
                            double x = player.getX();
                            double y = player.getY() + player.getBbHeight() / 2;
                            double z = player.getZ();
                            double motionX = player.getDeltaMovement().x;
                            double motionY = player.getDeltaMovement().y;
                            double motionZ = player.getDeltaMovement().z;
                            float yaw = (float) Math.toRadians(-player.getYRot());
                            float pitch = (float) Math.toRadians(-player.getXRot());
                            if (player.tickCount % 5 == 0) {
                                player.level.addParticle(new ParticleRing.RingData(yaw, pitch, 60, 0.8f, 0.8f, 0.9f, 0.1f, 40f, false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), x + 8f * motionX, y + 1.5f * motionY, z + 8f * motionZ, 0, 0, 0);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onEnd(LivingEntity entity) {
            if (entity != null && this.isFrenzy()) {
                this.isFrenzy = false;
                AttributeInstance attack = entity.getAttribute(Attributes.ATTACK_DAMAGE);
                AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attack != null && speed != null) {
                    attack.removeModifier(ATTACK_UUID);
                    speed.removeModifier(SPEED_UUID);
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
