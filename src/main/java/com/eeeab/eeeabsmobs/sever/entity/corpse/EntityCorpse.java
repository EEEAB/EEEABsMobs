package com.eeeab.eeeabsmobs.sever.entity.corpse;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationAttackGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationHurtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationMeleeAIGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMWallClimberNavigation;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityCorpse extends EntityAbsCorpse implements IEntity {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityCorpse.class, EntityDataSerializers.BYTE);
    public static final Animation ATTACK_ANIMATION_HANDS = Animation.create(20);
    public static final Animation ATTACK_ANIMATION_RIGHT = Animation.create(15);
    public static final Animation ATTACK_ANIMATION_LEFT = Animation.create(15);
    public static final Animation HURT_ANIMATION = Animation.create(0);
    private static final Animation[] ANIMATIONS = new Animation[]{
            ATTACK_ANIMATION_HANDS,
            ATTACK_ANIMATION_RIGHT,
            ATTACK_ANIMATION_LEFT,
            HURT_ANIMATION,
    };
    private static final Animation[] MELEE_ATTACK_ANIMATIONS = new Animation[]{
            ATTACK_ANIMATION_HANDS,
            ATTACK_ANIMATION_RIGHT,
            ATTACK_ANIMATION_LEFT,
    };

    public EntityCorpse(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
        this.active = true;
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.IMMORTAL.CORPSE.combatConfig;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new EMWallClimberNavigation(this, level);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationHurtGoal<>(this, false));
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, ATTACK_ANIMATION_HANDS, 12, 2F, 1.5F, 1.5F));
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, ATTACK_ANIMATION_RIGHT, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, ATTACK_ANIMATION_LEFT, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(2, new AnimationMeleeAIGoal<>(this, 1.2D, 2, MELEE_ATTACK_ANIMATIONS));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, EntityAbsCorpse.class, 6.0F));
    }

    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity, float damageMultiplier, float knockBackMultiplier, boolean canDisableShield) {
        if (!super.doHurtTarget(entity, damageMultiplier, knockBackMultiplier, canDisableShield)) {
            return false;
        } else {
            if (entity instanceof LivingEntity targetEntity) {
                targetEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1), this);
            }
        }
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    public boolean wasKilled(ServerLevel level, LivingEntity entity) {
        boolean flag = super.wasKilled(level, entity);
        if (!EMConfigHandler.COMMON.MOB.IMMORTAL.CORPSE.enableConvertToCorpse.get()) return flag;
        if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD)) {
            if (level.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return flag;
            }
            if (entity instanceof Zombie zombie && !zombie.isBaby()) {
                return this.convertToCorpse(level, EntityInit.CORPSE.get(), zombie);
            }
            if (entity instanceof Villager villager && !villager.isBaby() && villager.getVillagerData().getLevel() == VillagerData.MIN_VILLAGER_LEVEL) {
                return this.convertToCorpse(level, EntityInit.CORPSE_VILLAGER.get(), villager);
            }
        }
        return flag;
    }

    private <T extends Mob> boolean convertToCorpse(ServerLevel level, EntityType<T> entityType, Mob sourceEntity) {
        T convertEntity = sourceEntity.convertTo(entityType, false);
        if (convertEntity != null) {
            convertEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(convertEntity.blockPosition()), MobSpawnType.CONVERSION, null, null);
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(sourceEntity, convertEntity);
            if (!this.isSilent()) {
                level.levelEvent(null, 1026, this.blockPosition(), 0);
            }
            return false;
        }
        return true;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.FOLLOW_RANGE, 18.0D).
                add(Attributes.ATTACK_DAMAGE, 6D);
    }

    /**
     * Returns {@code true} if the WatchableObject (Byte) is 0x01 otherwise returns {@code false}. The WatchableObject is
     * updated using setBesideClimbableBlock.
     */
    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    public void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
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
}
