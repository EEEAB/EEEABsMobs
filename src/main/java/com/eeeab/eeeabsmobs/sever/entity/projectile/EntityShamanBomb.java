package com.eeeab.eeeabsmobs.sever.entity.projectile;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.EMDamageSource;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityShamanBomb extends AbstractHurtingProjectile implements IEntity {
    private static final float INERTIA_DANGEROUS = 1.025F;
    private static final float INERTIA_DEFAULT = 0.95F;
    private static final int DANGEROUS_TIMER = 30;
    private static final int NO_DANGEROUS_TIMER = 50;
    private float baseDamage = 8.0F;
    public boolean reboundFlag;
    private static final EntityDataAccessor<Boolean> DANGEROUS = SynchedEntityData.defineId(EntityShamanBomb.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_PLAYER = SynchedEntityData.defineId(EntityShamanBomb.class, EntityDataSerializers.BOOLEAN);
    public final ControlledAnimation scaleControlled = new ControlledAnimation(20);

    public EntityShamanBomb(EntityType<? extends EntityShamanBomb> entityType, Level level) {
        super(entityType, level);
    }

    public EntityShamanBomb(Level level, LivingEntity shooterEntity, double accelX, double accelY, double accelZ) {
        super(EntityInit.SHAMAN_BOMB.get(), shooterEntity, accelX, accelY, accelZ, level);
    }

    @Override
    public void tick() {
        super.tick();
        scaleControlled.updatePrevTimer();
        if (tickCount % 2 == 0) {
            if (isDangerous()) {
                scaleControlled.increaseTimer(2);
            } else {
                scaleControlled.increaseTimer();
            }
        }
        if (tickCount >= (!isDangerous() ? NO_DANGEROUS_TIMER : DANGEROUS_TIMER)) {
            if (!level().isClientSide) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 0.0F, false, Level.ExplosionInteraction.NONE);
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity resultEntity = result.getEntity();//被击中的实体
            Entity ownerEntity = this.getOwner();//(发射|反弹)弹射物的实体
            boolean flag;
            if (ownerEntity instanceof LivingEntity owner && resultEntity instanceof LivingEntity livingEntity) {
                flag = resultEntity.hurt(EMDamageSource.shamanBombing(this, owner), (livingEntity.getMaxHealth() * 0.05F * (isPlayer() ? 0 : 1F)) + baseDamage);
                if (flag && resultEntity.isAlive()) {
                    this.doEnchantDamageEffects(owner, resultEntity);
                }
            } else {
                flag = resultEntity.hurt(this.damageSources().magic(), 11.4514F);
            }

            if (flag && resultEntity instanceof LivingEntity livingEntity) {
                int duration = 10;
                int amplifier = 0;

                if (!isPlayer() && this.level().getDifficulty() == Difficulty.HARD) {
                    duration = 15;
                    amplifier = 1;
                }

                if (!isDangerous()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * duration, amplifier));
                } else {
                    livingEntity.addEffect(new MobEffectInstance(EffectInit.ERODE_EFFECT.get(), 20 * duration, amplifier));
                }

            }
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 0.0F, false, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        this.reboundFlag = super.hurt(source, damage);
        return this.reboundFlag;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide)
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 0.0F, false, Level.ExplosionInteraction.NONE);
        this.discard();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity instanceof EntityAbsImmortal) {
            return !EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get() || isPlayer() || !(getOwner() instanceof EntityAbsImmortal);
        } else if (entity instanceof Player) {
            return getOwner() != entity;
        } else {
            return super.canHitEntity(entity);
        }
    }

    //获取轨道粒子效果
    @Override
    protected ParticleOptions getTrailParticle() {
        return !this.isDangerous() ? ParticleTypes.SOUL : ParticleTypes.SOUL_FIRE_FLAME;
    }

    @Override
    public void remove(RemovalReason reason) {
        EntityCameraShake.cameraShake(level(), position(), 8, 0.15F, 0, 20);
        super.remove(reason);
    }

    //如果实体着火则返回 true 用于在渲染上添加火焰效果
    @Override
    public boolean isOnFire() {
        return false;
    }

    //为true时 则会产生持续一秒的火
    @Override
    protected boolean shouldBurn() {
        return false;
    }

    //如果为true 则可以打回去
    @Override
    public boolean isPickable() {
        return !this.isDangerous();
    }

    //返回弹射物的速度系数 该因子乘以初始速度
    @Override
    protected float getInertia() {
        return this.isDangerous() ? INERTIA_DANGEROUS : INERTIA_DEFAULT;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DANGEROUS, false);
        this.entityData.define(IS_PLAYER, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setIsPlayer(compoundTag.getBoolean("isPlayer"));
        this.tickCount = compoundTag.getInt("tickTimer");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("isPlayer", entityData.get(IS_PLAYER));
        compoundTag.putInt("tickTimer", this.tickCount);
    }

    public boolean isDangerous() {
        return this.entityData.get(DANGEROUS);
    }


    public void setDangerous(boolean flag) {
        if (flag) this.baseDamage *= 1.2F;
        this.entityData.set(DANGEROUS, flag);
    }


    public boolean isPlayer() {
        return this.entityData.get(IS_PLAYER);
    }


    public void setIsPlayer(boolean flag) {
        if (flag) this.baseDamage = EMConfigHandler.COMMON.ITEM.itemImmortalStaffProjectileDamage.get().floatValue();
        this.entityData.set(IS_PLAYER, flag);
    }
}
