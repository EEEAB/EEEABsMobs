package com.eeeab.eeeabsmobs.sever.capability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StunCapability {
    public static ResourceLocation ID = new ResourceLocation(EEEABMobs.MOD_ID, "stun_processor_cap");

    public interface IStunCapability extends GeneralCapability, INBTSerializable<CompoundTag> {
        //float getYaw();
        //
        //float getPitch();
        //
        //float getYawHead();
        //
        //float getCRenderYawOffset();
        //
        //float getCSwingProgress();
        //
        //float getCLimbSwingAmount();
    }

    public static class StunCapabilityImp implements IStunCapability {
        private boolean inStun;
        //private float yaw;
        //private float pitch;
        //private float yawHead;
        //private float swingProgress;
        //private float renderYawOffset;
        //private float limbSwingAmount;
        //private UUID prevAttackTargetUUID;

        @Override
        public boolean flag() {
            return inStun;
        }

        public void setInStun(boolean inStun) {
            this.inStun = inStun;
        }

        //@Override
        //public float getYaw() {
        //    return yaw;
        //}
        //
        //public void setYaw(float yaw) {
        //    this.yaw = yaw;
        //}
        //
        //@Override
        //public float getPitch() {
        //    return pitch;
        //}
        //
        //public void setPitch(float pitch) {
        //    this.pitch = pitch;
        //}
        //
        //@Override
        //public float getYawHead() {
        //    return yawHead;
        //}

        //public void setYawHead(float yawHead) {
        //    this.yawHead = yawHead;
        //}
        //
        //public UUID getPrevAttackTargetUUID() {
        //    return prevAttackTargetUUID;
        //}
        //
        //public void setPrevAttackTargetUUID(UUID prevAttackTargetUUID) {
        //    this.prevAttackTargetUUID = prevAttackTargetUUID;
        //}
        //
        //@Override
        //public float getCRenderYawOffset() {
        //    return renderYawOffset;
        //}
        //
        //public void setRenderYawOffset(float renderYawOffset) {
        //    this.renderYawOffset = renderYawOffset;
        //}
        //
        //@Override
        //public float getCSwingProgress() {
        //    return swingProgress;
        //}
        //
        //public void setSwingProgress(float swingProgress) {
        //    this.swingProgress = swingProgress;
        //}
        //
        //@Override
        //public float getCLimbSwingAmount() {
        //    return limbSwingAmount;
        //}
        //
        //public void setLimbSwingAmount(float limbSwingAmount) {
        //    this.limbSwingAmount = limbSwingAmount;
        //}

        @Override
        public void onStart(LivingEntity entity) {
            if (entity != null) {
                //entity.lookAt(EntityAnchorArgument.Anchor.FEET, new Vec3(entity.position().x() +
                //        1.5 * Math.cos(Math.toRadians(entity.yHeadRot + 90)),
                //        entity.position().y() - 1, entity.position().z() +
                //        1.5 * Math.sin(Math.toRadians(entity.yHeadRot + 90))));
                //yaw = entity.getYRot();
                //pitch = entity.getXRot();
                //yawHead = entity.yHeadRot;
                //limbSwingAmount = 0;
                //renderYawOffset = entity.yBodyRot;
                //swingProgress = entity.attackAnim;
                entity.stopUsingItem();
            }
            inStun = true;
        }

        @Override
        public void tick(LivingEntity entity) {
            if (inStun) {
                if (entity.level().isClientSide) {
                    for (int i = 0; i < 5; i++) {
                        Vec3 pos = new Vec3(entity.getX(), entity.getY() + (entity.getBbHeight() + 0.25D), entity.getZ()).add(new Vec3(1.0D, 0, 0).yRot((float) Math.toRadians(entity.getRandom().nextInt(360))));
                        entity.level().addParticle(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 0, 0, 0);
                    }
                }
                //if (entity instanceof Mob mob) mob.getNavigation().stop();
            }
        }

        @Override
        public void onEnd(LivingEntity entity) {
            inStun = false;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putBoolean("inStun", flag());
            //compoundTag.putFloat("yaw", getYaw());
            //compoundTag.putFloat("pitch", getPitch());
            //compoundTag.putFloat("yawHead", getYawHead());
            //if (getPrevAttackTargetUUID() != null)
            //    compoundTag.putUUID("prevAttackTargetUUID", getPrevAttackTargetUUID());
            //compoundTag.putFloat("renderYawOffset", getCRenderYawOffset());
            //compoundTag.putFloat("swingProgress", getCSwingProgress());
            //compoundTag.putFloat("limbSwingAmount", getCLimbSwingAmount());
            return compoundTag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            setInStun(nbt.getBoolean("inStun"));
            //setYaw(nbt.getFloat("yaw"));
            //setPitch(nbt.getFloat("pitch"));
            //setYawHead(nbt.getFloat("yawHead"));
            //try {
            //    setPrevAttackTargetUUID(nbt.getUUID("prevAttackTargetUUID"));
            //} catch (NullPointerException ignored) {
            //}
            //setRenderYawOffset(nbt.getFloat("renderYawOffset"));
            //setSwingProgress(nbt.getFloat("swingProgress"));
            //setLimbSwingAmount(nbt.getFloat("limbSwingAmount"));
        }
    }

    //能力提供器
    public static class StunCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<IStunCapability> instance = LazyOptional.of(StunCapabilityImp::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CapabilityHandler.STUN_CAPABILITY.orEmpty(cap, instance.cast());
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
