package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class VertigoCapability {
    public static ResourceLocation ID = new ResourceLocation(EEEABMobs.MOD_ID, "vertigo_processor_cap");

    public interface IVertigoCapability extends INBTSerializable<CompoundTag> {
        boolean isVertigo();

        float getYaw();

        float getPitch();

        float getYawHead();

        float getCRenderYawOffset();

        float getCSwingProgress();

        float getCLimbSwingAmount();

        void onStart(LivingEntity entity);

        void onEnd(LivingEntity entity);

        void tick(LivingEntity entity);
    }

    public static class VertigoCapabilityImp implements IVertigoCapability {
        private float yaw;
        private float pitch;
        private float yawHead;
        private boolean isVertigo;
        private float swingProgress;
        private float renderYawOffset;
        private float limbSwingAmount;
        private UUID prevAttackTargetUUID;

        @Override
        public boolean isVertigo() {
            return isVertigo;
        }

        public void setVertigo(boolean vertigo) {
            isVertigo = vertigo;
        }

        @Override
        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        @Override
        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        @Override
        public float getYawHead() {
            return yawHead;
        }

        public void setYawHead(float yawHead) {
            this.yawHead = yawHead;
        }

        public UUID getPrevAttackTargetUUID() {
            return prevAttackTargetUUID;
        }

        public void setPrevAttackTargetUUID(UUID prevAttackTargetUUID) {
            this.prevAttackTargetUUID = prevAttackTargetUUID;
        }

        @Override
        public float getCRenderYawOffset() {
            return renderYawOffset;
        }

        public void setRenderYawOffset(float renderYawOffset) {
            this.renderYawOffset = renderYawOffset;
        }

        @Override
        public float getCSwingProgress() {
            return swingProgress;
        }

        public void setSwingProgress(float swingProgress) {
            this.swingProgress = swingProgress;
        }

        @Override
        public float getCLimbSwingAmount() {
            return limbSwingAmount;
        }

        public void setLimbSwingAmount(float limbSwingAmount) {
            this.limbSwingAmount = limbSwingAmount;
        }

        @Override
        public void onStart(LivingEntity entity) {
            if (entity != null) {
                //强制看向面前的地面
                entity.lookAt(EntityAnchorArgument.Anchor.FEET, new Vec3(entity.position().x() +
                        Math.cos(Math.toRadians(entity.yHeadRot + 90)),
                        entity.position().y() - 1, entity.position().z() +
                        Math.sin(Math.toRadians(entity.yHeadRot + 90))));
                isVertigo = true;
                yaw = entity.getYRot();
                pitch = entity.getXRot();
                yawHead = entity.yHeadRot;
                limbSwingAmount = 0;
                renderYawOffset = entity.yBodyRot;
                swingProgress = entity.attackAnim;

                entity.stopUsingItem();

                if (entity instanceof Mob mob && mob.getTarget() != null) {
                    setPrevAttackTargetUUID(mob.getTarget().getUUID());
                    //mob.setTarget(null);
                    //if (mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
                    //    mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, Optional.empty());
                }
            }
        }

        @Override
        public void onEnd(LivingEntity entity) {
            if (entity != null) {
                if (isVertigo) {
                    isVertigo = false;
                    if (entity instanceof Mob mob) {
                        if (getPrevAttackTargetUUID() != null) {
                            Player target = mob.level().getPlayerByUUID(getPrevAttackTargetUUID());
                            if (target != null) mob.setTarget(target);
                        }
                    }
                }
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            if (isVertigo) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 50, false, false, false));
                if (entity.level().isClientSide()) {
                    for (int i = 0; i < 5; i++) {
                        Vec3 pos = new Vec3(entity.getX(), entity.getY() + (entity.getBbHeight() + 0.25D), entity.getZ()).add(new Vec3(1.0D, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))));
                        entity.level().addParticle(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 0, 0, 0);
                    }
                }
                if (entity instanceof Mob mob) mob.getNavigation().stop();
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putFloat("yaw", getYaw());
            compoundTag.putFloat("pitch", getPitch());
            compoundTag.putFloat("yawHead", getYawHead());
            compoundTag.putBoolean("isVertigo", isVertigo());
            if (getPrevAttackTargetUUID() != null)
                compoundTag.putUUID("prevAttackTargetUUID", getPrevAttackTargetUUID());
            compoundTag.putFloat("renderYawOffset", getCRenderYawOffset());
            compoundTag.putFloat("swingProgress", getCSwingProgress());
            compoundTag.putFloat("limbSwingAmount", getCLimbSwingAmount());
            return compoundTag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            setYaw(nbt.getFloat("yaw"));
            setPitch(nbt.getFloat("pitch"));
            setYawHead(nbt.getFloat("yawHead"));
            setVertigo(nbt.getBoolean("isVertigo"));
            try {
                setPrevAttackTargetUUID(nbt.getUUID("prevAttackTargetUUID"));
            } catch (NullPointerException ignored) {
            }
            setRenderYawOffset(nbt.getFloat("renderYawOffset"));
            setSwingProgress(nbt.getFloat("swingProgress"));
            setLimbSwingAmount(nbt.getFloat("limbSwingAmount"));
        }
    }

    //能力提供器
    public static class VertigoCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<IVertigoCapability> instance = LazyOptional.of(VertigoCapabilityImp::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return HandlerCapability.MOVING_CONTROLLER_CAPABILITY.orEmpty(cap, instance.cast());
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
