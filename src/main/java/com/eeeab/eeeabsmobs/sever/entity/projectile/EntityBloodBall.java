package com.eeeab.eeeabsmobs.sever.entity.projectile;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityExplode;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class EntityBloodBall extends Projectile implements IEntity {
    private int duration;
    private float damage = 5F;
    private boolean locating;
    private final boolean isHeal;
    private static final int MAX_ACTIVE = 400;
    private static final EntityDataAccessor<Integer> DATA_POWER = SynchedEntityData.defineId(EntityBloodBall.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UUID = SynchedEntityData.defineId(EntityBloodBall.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final BlockParticleOption REDSTONE_PARTICLE = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState());
    public final ControlledAnimation scaleControlled = new ControlledAnimation(20);

    public EntityBloodBall(EntityType<? extends EntityBloodBall> entityType, Level level) {
        super(entityType, level);
        isHeal = false;
    }

    public EntityBloodBall(Level level, int duration, boolean isHeal, int power) {
        super(EntityInit.BLOOD_BALL.get(), level);
        this.duration = duration;
        this.isHeal = isHeal;
        setPower(Mth.clamp(power, 1, 10));
    }

    @Override
    public void tick() {
        scaleControlled.updatePrevTimer();
        Entity entity = getOwner();
        if (level().isClientSide || (entity == null || !entity.isRemoved()) && level().hasChunkAt(blockPosition())) {
            super.tick();
            move(MoverType.SELF, getDeltaMovement());

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                onHit(hitresult);
            }

            checkInsideBlocks();

            if (tickCount >= MAX_ACTIVE) {
                discard();
            } else {
                Entity owner = getOwner();
                if (tickCount < duration && owner != null && owner.isAlive()) {
                    setPos(owner.position().add(0, 5, 0));
                } else if (level().isClientSide) {
                    for (int i = 0; i < 2; ++i) {
                        double dx = getX() + random.nextGaussian() * 0.15D;
                        double dy = getY(1) - random.nextGaussian() * 0.15D;
                        double dz = getZ() + random.nextGaussian() * 0.15D;
                        level().addParticle(REDSTONE_PARTICLE, dx, dy, dz, -getDeltaMovement().x() * 0.25F, -getDeltaMovement().y() * 0.25F, -getDeltaMovement().z() * 0.25F);
                    }
                }
                scaleControlled.increaseTimer();
            }

            if (!level().isClientSide && tickCount >= duration && !locating) {
                locating = true;
                if (getSavedTargetByUUID() != null && getSavedTargetByUUID().isAlive() && !isHeal) {
                    shoot(getSavedTargetByUUID(), -0.1F, 1.5F);
                } else {
                    shoot(getOwner(), 0.2F, 0.5F);
                }
            }
        } else {
            discard();
        }
    }

    private void shoot(Entity target, float yOffset, float velocity) {
        if (target != null) {
            double x = target.getX() - getX();
            double y = target.getY(0.3333333333333333D) - getY();
            double z = target.getZ() - getZ();
            shoot(x, y + Math.sqrt(x * x + z * z) * yOffset, z, velocity, 1.0F);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        preDestroy(hitResult.getEntity());
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        preDestroy(null);
    }

    private void preDestroy(@Nullable Entity entity) {
        if (!level().isClientSide) {
            if (getOwner() == entity && entity instanceof LivingEntity livingEntity) {
                if (isHeal) {
                    livingEntity.heal(Math.min(livingEntity.getMaxHealth() * 0.05F * getPower(), livingEntity.getMaxHealth() * 0.5F));
                    level().broadcastEntityEvent(livingEntity, (byte) 14);
                }
            } else {
                EntityExplode.explode(level(), position(), damageSources().explosion(this, entity), null, getPower(), damage * getPower());
                EntityCameraShake.cameraShake(level(), position(), 16F, 0.125F, 5, 15);
            }
        }
        discard();
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
        return !locating;
    }

    @Override
    public float getPickRadius() {
        return 1F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else if (level().isClientSide) {
            return false;
        } else if (locating) {
            return false;
        } else {
            markHurt();
            Entity entity = source.getEntity();
            if (entity != null) {
                if (getOwner() instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 50, 0, false, false));
                }
                preDestroy(null);
                return true;
            } else {
                return false;
            }
        }
    }

    public void setTargetUUID(UUID uuid) {
        entityData.set(DATA_TARGET_UUID, Optional.of(uuid));
    }

    public UUID getTargetUUID() {
        return entityData.get(DATA_TARGET_UUID).orElse(null);
    }

    @Nullable
    public Entity getSavedTargetByUUID() {
        if (level() instanceof ServerLevel level) {
            if (getTargetUUID() != null) {
                return level.getEntity(getTargetUUID());
            }
        }
        return null;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_TARGET_UUID, Optional.empty());
        entityData.define(DATA_POWER, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (getTargetUUID() != null) {
            nbt.putUUID("target_uuid", getTargetUUID());
        }
        nbt.putBoolean("locating", locating);
        nbt.putInt("power", getPower());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.hasUUID("target_uuid")) {
            setTargetUUID(nbt.getUUID("target_uuid"));
        }
        locating = nbt.getBoolean("locating");
        setPower(nbt.getInt("power"));
    }

    public int getPower() {
        return entityData.get(DATA_POWER);
    }

    public void setPower(int power) {
        entityData.set(DATA_POWER, power);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
