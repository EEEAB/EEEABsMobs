package com.eeeab.eeeabsmobs.sever.entity.impl.immortal;

import com.eeeab.eeeabsmobs.sever.config.EEConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.util.EEMobType;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.impl.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class EntityImmortal extends EEEABMobLibrary implements GlowEntity {
    protected int attackTick;
    protected boolean attacking;
    private boolean isBySummon;
    private int countdown = -1;
    @Nullable
    private Mob owner;
    private UUID ownerUUID;
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityImmortal.class, EntityDataSerializers.BOOLEAN);

    public EntityImmortal(EntityType<? extends EntityImmortal> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return effectInstance.getEffect() != EffectInit.ERODE_EFFECT.get() && super.addEffect(effectInstance, entity);
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (animation == getSpawnAnimation()) {
            setActive(true);
            active = true;
        }
    }

    @Override//应该禁用和平模式生成
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (level().isClientSide && getAnimation() == getSpawnAnimation()) {
            setSpawnParticle(5);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && active && !isActive()) setActive(true);
        active = isActive();
        if (!active) {
            getNavigation().stop();
            setYRot(yRotO);
            yBodyRot = getYRot();
            if ((onGround() || isInWater() || isInLava()) && getAnimation() == NO_ANIMATION) {
                this.playAnimation(getSpawnAnimation());
            }
            return;
        }

        if (this.isBySummon && --countdown <= 0) {
            this.hurt(damageSources().starve(), 1.0F);
            countdown = 20;
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, 5, false, false, null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 5, false, false, null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Zombie.class, 5, false, false, (zombie) -> !(zombie instanceof ZombifiedPiglin)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, 5, false, false, (illager) -> !(illager instanceof SpellcasterIllager)));
        //this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, EntityTestllager.class, 0, true, false, null));//Only test
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (level().isClientSide) {
            return false;
        } else {
            if (source.getDirectEntity() instanceof EntityShamanBomb shamanBomb) {
                Entity ownerEntity = shamanBomb.getOwner();
                if (ownerEntity instanceof LivingEntity livingEntity) {
                    if (livingEntity.getMobType() == this.getMobType()) {
                        if (EEConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get()) return false;
                    } else if (!shamanBomb.isPlayer()) {
                        this.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 100, 0, false, false));
                    }
                }
            }
            if (source.getEntity() instanceof LivingEntity livingEntity && livingEntity.getMobType() == getMobType() && EEConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get()) {
                return false;
            }
            return super.hurt(source, damage);
        }
    }

    //设置召唤物存在时长
    protected void setSummonAliveTime(int time) {
        this.isBySummon = true;
        this.countdown = time;
    }

    @Override
    public @NotNull MobType getMobType() {
        return EEMobType.IMMORTAL;
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    public void setSpawnParticle(int amount) {
        RandomSource randomsource = this.getRandom();
        BlockState blockstate = this.getBlockStateOn();
        if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < amount; ++i) {
                double d0 = this.getX() + (double) Mth.randomBetween(randomsource, -0.2F, 0.2F);
                double d1 = this.getY();
                double d2 = this.getZ() + (double) Mth.randomBetween(randomsource, -0.2F, 0.2F);
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof LivingEntity && ((LivingEntity) entity).getMobType() == EEMobType.IMMORTAL) {
            return this.getTeam() == null && entity.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, true);
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.isBySummon) {
            compound.putInt("countdown", countdown);
        }
        if (this.owner != null && this.owner.isAlive()) {
            compound.putUUID("owner", owner.getUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("countdown")) {
            this.setSummonAliveTime(compound.getInt("countdown"));
        }
        this.ownerUUID = compound.hasUUID("owner") ? compound.getUUID("owner") : null;
    }

    protected Animation getSpawnAnimation() {
        return null;
    }

    //设置该生物初始生成
    protected void setInitSpawn() {
        this.active = false;
        this.setActive(false);
    }

    @Nullable
    public Mob getOwner() {
        return owner;
    }

    public void setOwner(@Nullable Mob owner) {
        this.owner = owner;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public boolean isBySummon() {
        return this.isBySummon;
    }

    @Override
    public boolean isGlow() {
        return getHealth() > 0;
    }
}
