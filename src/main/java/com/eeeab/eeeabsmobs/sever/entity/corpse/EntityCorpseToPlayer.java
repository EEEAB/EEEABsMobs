package com.eeeab.eeeabsmobs.sever.entity.corpse;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationMelee;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.*;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.player.PlayerHatredRedirectionGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.player.ReFindPlayerGoal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModMobType;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityCorpseToPlayer extends EEEABMobLibrary implements IEntity, GlowEntity, VenerableEntity<Player> {
    public final Animation attackAnimation1 = Animation.create(15);
    public final Animation attackAnimation2 = Animation.create(15);
    public final Animation attackAnimation3 = Animation.create(15);
    public final Animation spawnAnimation = Animation.create(20);
    private final Animation[] animations = new Animation[]{
            attackAnimation1,
            attackAnimation2,
            attackAnimation3,
            spawnAnimation
    };
    private UUID ownerUUID;
    @Nullable
    private Player owner;
    private int countdown = -1;
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityCorpseToPlayer.class, EntityDataSerializers.BOOLEAN);

    public EntityCorpseToPlayer(EntityType<? extends EntityCorpseToPlayer> type, Level level) {
        super(type, level);
        this.active = true;
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_NONE;
    }

    @Override
    public boolean isGlow() {
        return this.getHealth() > 0F;
    }

    @Override//应该禁用和平模式生成
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public @NotNull MobType getMobType() {
        return ModMobType.PLAYER_SUMMONS;
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.MINION.CORPSE_MINION.combatConfig;
    }

    public void setInitSpawn() {
        this.playAnimation(spawnAnimation);
        this.active = false;
        this.setActive(false);
    }

    @Override
    protected void registerGoals() {
        this.registerCustomGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, Player.class));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> spawnAnimation));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, () -> attackAnimation1, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, () -> attackAnimation2, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, () -> attackAnimation3, 9, 2F, 1.5F, 1.5F));
        this.goalSelector.addGoal(2, new AnimationMeleeAI<>(this, 1.2D, 5, () -> attackAnimation1, () -> attackAnimation2, () -> attackAnimation3));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new ReFindPlayerGoal<>(this));
        this.goalSelector.addGoal(3, new PlayerHatredRedirectionGoal<>(this, 12F));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (level.isClientSide && getAnimation() == spawnAnimation) {
            setSpawnParticle(4);
        }
    }

    @Override
    public void tick() {
        super.tick();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
        if (!level.isClientSide && active && !isActive()) setActive(true);
        if (!this.level.isClientSide && this.getTarget() != null && !this.getTarget().isAlive()) setTarget(null);
        active = isActive();
        if (!active) {
            getNavigation().stop();
            setYRot(yRotO);
            yBodyRot = getYRot();
        }
        if (this.isSummon() && --countdown <= 0) {
            this.hurt(DamageSource.STARVE, getMaxHealth() * 0.25F);
            countdown = 20;
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
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, true);
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level.isClientSide && this.getOwner() != null && source.getEntity() != null) {
            float healAmount = EMConfigHandler.COMMON.MOB.MINION.CORPSE_MINION.minionDeathHealAmount.get().floatValue();
            this.getOwner().heal(healAmount);
            if (healAmount > 0 && this.level instanceof ServerLevel serverLevel) {
                this.level.playSound(null, this.getOwner().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1F, 0.9F + this.random.nextFloat() * 0.2F);
                for (int i = 0; i < 5; i++) {
                    serverLevel.sendParticles(ParticleInit.WARLOCK_HEAL.get(), this.getOwner().getRandomX(1.5), this.getOwner().getY(0.5D), this.getOwner().getRandomZ(1.5), 1, this.random.nextGaussian() * 0.1D, this.random.nextGaussian() * 0.1D, this.random.nextGaussian() * 0.1D, 0.5D);
                }
            }
        }
        super.die(source);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 10; ++i) {
            double d0 = this.random.nextGaussian() * 0.015D;
            double d1 = this.random.nextGaussian() * 0.015D;
            double d2 = this.random.nextGaussian() * 0.015D;
            this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        this.countdown = 400 + this.random.nextInt(200);
        return groupData;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.owner != null && this.owner.getHealth() > 0F) {
            compound.putUUID("owner", owner.getUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOwnerUUID(compound.hasUUID("owner") ? compound.getUUID("owner") : null);
    }


    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.ATTACK_DAMAGE, 6D);
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.CORPSE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.CORPSE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.CORPSE_DEATH.get();
    }

    @Nullable
    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(@Nullable Player owner) {
        this.owner = owner;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }

    @Override
    public boolean isSummon() {
        return true;
    }
}
