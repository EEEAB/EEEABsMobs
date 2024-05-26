package com.eeeab.eeeabsmobs.sever.entity.projectile;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityExplode;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class EntityBloodBall extends Projectile implements IEntity {
    private int power;
    private int duration;
    private boolean locating;
    private final boolean isHeal;
    private static final int MAX_ACTIVE = 400;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UUID = SynchedEntityData.defineId(EntityBloodBall.class, EntityDataSerializers.OPTIONAL_UUID);
    public final ControlledAnimation scaleControlled = new ControlledAnimation(20);

    public EntityBloodBall(EntityType<? extends EntityBloodBall> entityType, Level level) {
        super(entityType, level);
        this.isHeal = false;
    }

    public EntityBloodBall(Level level, int duration, boolean isHeal, int power) {
        super(EntityInit.BLOOD_BALL.get(), level);
        this.duration = duration;
        this.isHeal = isHeal;
        this.power = power;
    }

    @Override
    public void tick() {
        this.scaleControlled.updatePrevTimer();
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            this.move(MoverType.SELF, this.getDeltaMovement());

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();

            if (this.tickCount >= MAX_ACTIVE) {
                this.discard();
            } else {
                Entity owner = this.getOwner();
                if (this.tickCount < this.duration && owner != null && owner.isAlive()) {
                    this.setPos(owner.position().add(0, 5, 0));
                }
                this.scaleControlled.increaseTimer();
            }

            if (!this.level().isClientSide && this.tickCount >= this.duration && !this.locating) {
                this.locating = true;
                if (this.getSavedTargetByUUID() != null && this.getSavedTargetByUUID().isAlive() && !this.isHeal) {
                    this.shoot(this.getSavedTargetByUUID(), -0.1F, 1.5F);
                } else {
                    this.shoot(this.getOwner(), 0.2F, 0.5F);
                }
            }
        } else {
            this.discard();
        }
    }

    private void shoot(Entity target, float yOffset, float velocity) {
        if (target != null) {
            double x = target.getX() - this.getX();
            double y = target.getY(0.3333333333333333D) - this.getY();
            double z = target.getZ() - this.getZ();
            this.shoot(x, y + Math.sqrt(x * x + z * z) * yOffset, z, velocity, 1.0F);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        this.preDestroy(hitResult.getEntity());
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.preDestroy(null);
    }

    private void preDestroy(@Nullable Entity entity) {
        if (!this.level().isClientSide) {
            if (this.getOwner() == entity && entity instanceof LivingEntity livingEntity) {
                if (this.isHeal)
                    livingEntity.heal(Math.min(livingEntity.getMaxHealth() * power * 0.05F, livingEntity.getMaxHealth() * 0.5F));
            } else {
                EntityExplode.explode(this.level(), this.position(), this.damageSources().explosion(this, entity), Math.min(power + 1, 5F), 30F);
                EntityCameraShake.cameraShake(this.level(), this.position(), 16F, 0.125F, 5, 15);
            }
        }
        this.discard();
    }

    public boolean isHeal() {
        return isHeal;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    //如果实体着火则返回 true 用于在渲染上添加火焰效果
    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.locating;
    }

    @Override
    public float getPickRadius() {
        return 1F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.level().isClientSide) {
            return false;
        } else if (this.locating) {
            return false;
        } else {
            this.markHurt();
            Entity entity = source.getEntity();
            if (entity != null) {
                if (this.getOwner() instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 50, 0, false, false));
                }
                this.preDestroy(null);
                return true;
            } else {
                return false;
            }
        }
    }

    public void setTargetUUID(UUID uuid) {
        this.entityData.set(DATA_TARGET_UUID, Optional.of(uuid));
    }

    public UUID getTargetUUID() {
        return this.entityData.get(DATA_TARGET_UUID).orElse(null);
    }

    @Nullable
    public Entity getSavedTargetByUUID() {
        if (this.level() instanceof ServerLevel level) {
            if (this.getTargetUUID() != null) {
                return level.getEntity(this.getTargetUUID());
            }
        }
        return null;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_TARGET_UUID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.getTargetUUID() != null) {
            nbt.putUUID("target_uuid", this.getTargetUUID());
        }
        nbt.putBoolean("locating", this.locating);
        nbt.putInt("power", this.power);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.hasUUID("target_uuid")) {
            this.setTargetUUID(nbt.getUUID("target_uuid"));
        }
        this.locating = nbt.getBoolean("locating");
        this.power = nbt.getInt("power");
    }
}
