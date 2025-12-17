package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.AnimData;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.RibbonComponent;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.mob.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.IBoss;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.FractalPathProvider;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public abstract class EntityAbsRelicron extends EEEABMobLibrary implements Enemy, GlowEntity {
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityAbsRelicron.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ALWAYS_ACTIVE = SynchedEntityData.defineId(EntityAbsRelicron.class, EntityDataSerializers.BOOLEAN);
    private static final int UNTARGETED_ACTIVE_TIME = 300;
    protected int timeUntilDeactivate;

    public EntityAbsRelicron(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        MobEffect effect = effectInstance.getEffect();
        return MobEffects.POISON != effect && MobEffects.MOVEMENT_SLOWDOWN != effect && super.canBeAffected(effectInstance);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return !this.isActive() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return this.isActive();
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive() && !this.isActive();
    }

    @Override
    protected boolean canShowBossBar() {
        return this.isActive();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && !(this instanceof IBoss)) {
            if (!this.isNoAi() && !this.isActive()) {
                if (ModConfigHandler.COMMON.mobs.relicrons.enableNonCombatHeal.get()) this.heal(0.5F);
                this.setDeltaMovement(0F, this.getDeltaMovement().y, 0F);
                this.yHeadRot = this.yBodyRot = this.getYRot();
            }
            if (this.isAlwaysActive()) {
                this.setActive(true);
                this.active = true;
                this.timeUntilDeactivate = 0;
            } else if (!this.isNoAi() && this.isNoAnimation()) {
                if (!this.isActive() && this.getTarget() != null && this.targetDistance <= this.activeRange()) {
                    if (this.getActiveSound() != null) this.playSound(this.getActiveSound());
                    this.playAnimation(this.getActiveAnimation());
                    this.setActive(true);
                }
                if (this.isActive() && this.isAlive() && this.getTarget() == null && this.timeUntilDeactivate >= UNTARGETED_ACTIVE_TIME) {
                    if (this.getDeactivateSound() != null) this.playSound(this.getDeactivateSound());
                    this.playAnimation(this.getDeactivateAnimation());
                    this.setActive(false);
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isActive() && !this.isAlwaysActive()) {
            if (this.getTarget() == null || (this.tickCount % 2 == 0 && !this.getSensing().hasLineOfSight(this.getTarget()))) {
                this.timeUntilDeactivate++;
            } else if (this.timeUntilDeactivate > 0) {
                this.timeUntilDeactivate--;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.getEntity() instanceof EntityAbsRelicron && ModConfigHandler.COMMON.others.enableSameMobsTypeInjury.get()) return false;
        if ((!active || getTarget() == null) && source.getEntity() instanceof LivingEntity livingEntity
                && !(livingEntity instanceof Player player && player.isCreative() || this.level().getDifficulty() == Difficulty.PEACEFUL)
                && (!ModConfigHandler.COMMON.others.enableSameMobsTypeInjury.get() || !(livingEntity instanceof EntityAbsRelicron))) {
            this.setLastHurtByMob(livingEntity);
        }
        if (source.is(ModResourceKey.OVERLOAD_EXPLODE)) damage *= 0.1F;
        return super.hurt(source, damage);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.5D), this.getRandomY(), this.getRandomZ(1.5D), d0, d1, d2);
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof EntityAbsRelicron) {
            return ModConfigHandler.COMMON.others.enableSameMobsTypeInjury.get() || (this.getTeam() == null && entity.getTeam() == null);
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        return groupData;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, true);
        this.entityData.define(DATA_ALWAYS_ACTIVE, true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_ACTIVE, compound.getBoolean("isActive"));
        this.entityData.set(DATA_ALWAYS_ACTIVE, compound.getBoolean("isAlwaysActive"));
        this.active = this.isActive();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isActive", this.entityData.get(DATA_ACTIVE));
        compound.putBoolean("isAlwaysActive", this.entityData.get(DATA_ALWAYS_ACTIVE));
    }

    protected Animation getActiveAnimation() {
        return null;
    }

    protected Animation getDeactivateAnimation() {
        return null;
    }

    protected SoundEvent getActiveSound() {
        return null;
    }

    protected SoundEvent getDeactivateSound() {
        return null;
    }

    protected float activeRange() {
        return 12F;
    }

    @Override
    public boolean isGlow() {
        return this.isActive() && this.getHealth() > 0;
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
        this.timeUntilDeactivate = 0;
    }

    public boolean isAlwaysActive() {
        return this.entityData.get(DATA_ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean alwaysActive) {
        this.entityData.set(DATA_ALWAYS_ACTIVE, alwaysActive);
    }

    protected int getCoolingTimerUtil(int maxCooling, int minCooling, float healthPercentage) {
        float maximumCoolingPercentage = 1 - healthPercentage;
        float ratio = 1 - this.getHealthPercentage();
        if (ratio > maximumCoolingPercentage) {
            ratio = maximumCoolingPercentage;
        }
        return (int) (maxCooling - (ratio / maximumCoolingPercentage) * (maxCooling - minCooling));
    }

    protected void doWalkEffect(int amount) {
        if (this.level().isClientSide) {
            for (int l = 0; l < amount; l++) {
                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY() - (double) 0.2F);
                int k = Mth.floor(this.getZ());
                BlockPos pos = new BlockPos(i, j, k);
                BlockState blockstate = this.level().getBlockState(pos);
                if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos),
                            this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            this.getY() + 0.1D, this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
                }
            }
        }
    }

    protected void doFractalEffect(Vec3 start, Vec3 end) {
        doFractalEffect(this, start, end, 5, 3);
    }

    public static void doFractalEffect(Entity entity, Vec3 start, Vec3 end, int iterations, double roughness) {
        ParticleComponent[] components = new ParticleComponent[]{
                new ParticleComponent.FollowPath(FractalPathProvider.getFractalPath(start, end, iterations, roughness), AnimData.KeyTrack.startAndEnd(0, 1)),
                new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 8, 0, 0, 0,
                        0.05, 0.56, 0.78, 0.86, 0.8F, true, true,
                        new ParticleComponent[]{
                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0.8F, 0F))
                        }, false
                ),
        };
        AdvancedParticleBase.spawnParticle(
                entity.level(),
                ParticleInit.ADV_ORB.get(),
                entity.getX(), entity.getY(), entity.getZ(),
                0, 0, 0,
                true, 0, 0, 0,
                0, 0F,
                1, 1, 1, 1,
                1, 5
                , false, false, false,
                components
        );
    }
}
