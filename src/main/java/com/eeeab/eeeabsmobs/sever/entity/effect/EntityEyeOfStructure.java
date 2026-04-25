package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemFindStructureEye;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

//参考自: net.minecraft.world.entity.EyeOfEnder
public class EntityEyeOfStructure extends Entity implements ItemSupplier, IEntity {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(EntityEyeOfStructure.class, EntityDataSerializers.ITEM_STACK);
    private double tx;
    private double ty;
    private double tz;
    private int life;
    private boolean canConsumeItem;

    public EntityEyeOfStructure(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }


    public EntityEyeOfStructure(Level level, double px, double py, double pz, boolean canConsumeItem) {
        super(EntityInit.EYE_OF_STRUCTURE.get(), level);
        this.setPos(px, py, pz);
        this.canConsumeItem = canConsumeItem;
    }

    public void setItem(ItemStack itemStack) {
        this.getEntityData().set(DATA_ITEM_STACK, itemStack.copyWithCount(1));
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return this.level().isClientSide && !this.level().getBlockState(this.blockPosition().above()).isAir() || super.isCurrentlyGlowing();
    }

    private ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }
        d0 *= 64.0D;
        return distance < d0 * d0;
    }

    public void signalTo(BlockPos blockPos) {
        double d0 = (double) blockPos.getX();
        int i = blockPos.getY();
        double d1 = (double) blockPos.getZ();
        double d2 = d0 - this.getX();
        double d3 = d1 - this.getZ();
        double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        if (d4 > 12.0D) {
            this.tx = this.getX() + d2 / d4 * 12.0D;
            this.tz = this.getZ() + d3 / d4 * 12.0D;
            this.ty = this.getY() + 8.0D;
        } else {
            this.tx = d0;
            this.ty = (double) i;
            this.tz = d1;
        }
        this.life = 0;
    }

    @Override
    public void lerpMotion(double px, double py, double pz) {
        this.setDeltaMovement(px, py, pz);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = Math.sqrt(px * px + pz * pz);
            this.setYRot((float) (Mth.atan2(px, pz) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(py, d0) * (double) (180F / (float) Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    @Override
    public void tick() {
        super.tick();
        ItemStack item = getItem();
        if (item.isEmpty()) {
            discard();
        }
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        double d3 = vec3.horizontalDistance();
        this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(vec3.y, d3) * (double) (180F / (float) Math.PI))));
        this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI))));
        if (!this.level().isClientSide) {
            double d4 = this.tx - d0;
            double d5 = this.tz - d2;
            float f = (float) Math.sqrt(d4 * d4 + d5 * d5);
            float f1 = (float) Mth.atan2(d5, d4);
            double d6 = Mth.lerp(0.0025D, d3, (double) f);
            double d7 = vec3.y;
            if (f < 1.0F) {
                d6 *= 0.8D;
                d7 *= 0.8D;
            }

            int j = this.getY() < this.ty ? 1 : -1;
            vec3 = new Vec3(Math.cos((double) f1) * d6, d7 + ((double) j - d7) * (double) 0.015F, Math.sin((double) f1) * d6);
            this.setDeltaMovement(vec3);
        }

        float speed = 0.25F;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * speed, d1 - vec3.y * speed, d2 - vec3.z * speed, vec3.x, vec3.y, vec3.z);
            }
        } else if (this.tickCount % 4 == 0) {
            ParticleOptions particle = ParticleTypes.PORTAL;
            if (item.getItem() instanceof ItemFindStructureEye structureEye) {
                particle = structureEye.getTrailParticle();
            }
            this.level().addParticle(particle, d0 - vec3.x * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, d1 - vec3.y * speed, d2 - vec3.z * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, vec3.x, vec3.y, vec3.z);
        }

        if (!this.level().isClientSide) {
            this.setPos(d0, d1, d2);
            ++this.life;
            if (this.life > 80 && !this.level().isClientSide) {
                this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                this.level().broadcastEntityEvent(this, (byte) 5);
                this.discard();
                if (this.canConsumeItem) this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), item));
            }
        } else {
            this.setPosRaw(d0, d1, d2);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 5) {
            ModParticleUtils.roundParticleOutburst(level(), 30, new ParticleOptions[]{new ItemParticleOption(ParticleTypes.ITEM, getItem())}, getX(), getY(), getZ(), 1);
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) {
            compoundTag.put("Item", itemstack.save(new CompoundTag()));
        }
        compoundTag.putBoolean("CanConsumeItem", this.canConsumeItem);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        ItemStack itemstack = ItemStack.of(compoundTag.getCompound("Item"));
        this.setItem(itemstack);
        this.canConsumeItem = compoundTag.getBoolean("CanConsumeItem");
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    private static float lerpRotation(float f1, float f2) {
        while (f2 - f1 < -180.0F) {
            f1 -= 360.0F;
        }

        while (f2 - f1 >= 180.0F) {
            f1 += 360.0F;
        }

        return Mth.lerp(0.2F, f1, f2);
    }
}
