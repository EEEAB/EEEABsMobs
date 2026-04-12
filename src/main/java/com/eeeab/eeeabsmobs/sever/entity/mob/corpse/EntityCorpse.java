package com.eeeab.eeeabsmobs.sever.entity.mob.corpse;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationMelee;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.sever.capability.FrenzyCapability;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ModLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.CopyOwnerTargetGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.ReFindOwnerGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.RedirectOwnerHatredGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.WhenOwnerDeadGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModWallClimberNavigation;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class EntityCorpse extends EntityAbsCorpse {
    public static final Animation ATTACK_ANIMATION1 = Animation.create(15);
    public static final Animation ATTACK_ANIMATION2 = Animation.create(15);
    public static final Animation ATTACK_ANIMATION3 = Animation.create(15);
    public static final Animation SPAWN_ANIMATION = Animation.create(20);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityCorpse.class, EntityDataSerializers.BYTE);
    private static final Animation[] ANIMATIONS = new Animation[]{
            ATTACK_ANIMATION1,
            ATTACK_ANIMATION2,
            ATTACK_ANIMATION3,
            SPAWN_ANIMATION
    };

    public EntityCorpse(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
        this.active = true;
    }

    @Override
    public boolean isGlow() {
        return super.isGlow() && !this.isFrenzy();
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.corpses.corpse.combatConfig;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new ModWallClimberNavigation(this, level);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override//应该禁用和平模式生成
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void afterSpawn() {
        super.afterSpawn();
        this.playAnimation(SPAWN_ANIMATION);
        this.active = false;
        this.setActive(false);
    }

    @Override
    protected void registerCorpseGoals() {
        super.registerCorpseGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, false));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, EntityCorpseWarlock.class));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, SPAWN_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, ATTACK_ANIMATION1, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, ATTACK_ANIMATION2, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, ATTACK_ANIMATION3, 9, 2F, 1.5F, 1.5F));
        this.goalSelector.addGoal(2, new AnimationMeleeAI<>(this, 1.2D, 5, ATTACK_ANIMATION1, ATTACK_ANIMATION2, ATTACK_ANIMATION3));
        this.goalSelector.addGoal(7, new ModLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new ModLookAtGoal(this, EntityAbsCorpse.class, 6.0F));
        this.goalSelector.addGoal(1, new ReFindOwnerGoal(this, EntityCorpseWarlock.class, 18D));
        this.targetSelector.addGoal(2, new CopyOwnerTargetGoal(this));
        this.goalSelector.addGoal(3, new RedirectOwnerHatredGoal(this, 16F));
        this.goalSelector.addGoal(3, new WhenOwnerDeadGoal(this));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (level().isClientSide && getAnimation() == SPAWN_ANIMATION) {
            setSpawnParticle(4);
        }
    }

    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (!level().isClientSide && active && !isActive()) setActive(true);
        active = isActive();
        if (!active) {
            getNavigation().stop();
            setYRot(yRotO);
            yBodyRot = getYRot();
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    public void die(DamageSource source) {
        if (this.belongsToMob() && !this.level().isClientSide && this.getOwner() != null && this.getOwner().isAlive()) {
            if (source.getEntity() == this.getOwner()) {
                this.getOwner().heal(this.getOwner().getMaxHealth() * 0.01F);
                this.getOwner().level().broadcastEntityEvent(this.getOwner(), (byte) 14);
            }
            this.shiftHatred(this, 16D);
        }
        super.die(source);
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        boolean flag = super.killedEntity(level, entity);
        if (!ModConfigHandler.COMMON.mobs.corpses.corpse.enableConvertToCorpse.get() || !this.belongsToMob()) return flag;
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

    private <T extends EntityAbsCorpse> boolean convertToCorpse(ServerLevel level, EntityType<T> entityType, Mob sourceEntity) {
        T convertEntity = sourceEntity.convertTo(entityType, false);
        if (convertEntity != null) {
            convertEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(convertEntity.blockPosition()), MobSpawnType.CONVERSION, null, null);
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(sourceEntity, convertEntity);
            if (!this.isSilent()) {
                level.levelEvent(null, 1026, this.blockPosition(), 0);
            }
            if (this.isSummon() && this.getOwner() != null && this.getOwner().isAlive()) {
                convertEntity.afterSpawn();
                convertEntity.setOwner(this.getOwner());
            }
            return false;
        }
        return true;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 32.0D).add(Attributes.ATTACK_DAMAGE, 5D);
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

    public boolean isFrenzy() {
        FrenzyCapability.IFrenzyCapability capability = CapabilityHandler.getCapability(this, CapabilityHandler.FRENZY_CAPABILITY);
        if (capability != null) {
            return capability.flag();
        }
        return false;
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
