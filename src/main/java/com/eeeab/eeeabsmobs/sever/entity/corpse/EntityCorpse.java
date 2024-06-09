package com.eeeab.eeeabsmobs.sever.entity.corpse;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationMelee;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.sever.capability.FrenzyCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerCopyTargetGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerDieGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerProtectGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerResetGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMWallClimberNavigation;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
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
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class EntityCorpse extends EntityAbsCorpse implements IEntity {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityCorpse.class, EntityDataSerializers.BYTE);
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

    public EntityCorpse(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
        this.active = true;
    }

    public boolean isFrenzy() {
        FrenzyCapability.IFrenzyCapability capability = HandlerCapability.getCapability(this, HandlerCapability.FRENZY_EFFECT_CAPABILITY);
        if (capability != null) {
            return capability.isFrenzy();
        }
        return false;
    }

    @Override
    public boolean isGlow() {
        return super.isGlow() && !this.isFrenzy();
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.CORPSES.CORPSE.combatConfig;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new EMWallClimberNavigation(this, level);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override//是否免疫摔伤
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return fallDistance > 10;
    }

    @Override//应该禁用和平模式生成
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void setInitSpawn() {
        super.setInitSpawn();
        this.playAnimation(spawnAnimation);
        this.active = false;
        this.setActive(false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, false));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, EntityAbsCorpse.class).setAlertOthers());
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
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, EntityAbsCorpse.class, 6.0F));
        this.goalSelector.addGoal(1, new OwnerResetGoal<>(this, EntityCorpseWarlock.class, 18D));
        this.targetSelector.addGoal(2, new OwnerCopyTargetGoal<>(this));
        this.goalSelector.addGoal(3, new OwnerProtectGoal<>(this, 16F));
        this.goalSelector.addGoal(3, new OwnerDieGoal<>(this));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (level().isClientSide && getAnimation() == spawnAnimation) {
            setSpawnParticle(4);
        }
    }

    @Override
    public void tick() {
        super.tick();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
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
        if (!this.level().isClientSide && this.getOwner() != null && this.getOwner().isAlive()) {
            if (source.getEntity() == this.getOwner()) {
                this.getOwner().heal(this.getOwner().getMaxHealth() * 0.01F);
                this.getOwner().level().broadcastEntityEvent(this.getOwner(), (byte) 14);
            }
            this.level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT, this, this.getBoundingBox().inflate(16))
                    .stream().filter(mob -> mob != this && mob != this.getOwner() && mob.isAlive() && mob.getTarget() == this)
                    .forEach(mob -> {
                        mob.setTarget(this.getOwner());
                        Brain<?> mobBrain = mob.getBrain();
                        if (mobBrain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
                            mobBrain.setMemory(MemoryModuleType.ATTACK_TARGET, this.getOwner());
                        }
                    });
        }
        super.die(source);
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        boolean flag = super.killedEntity(level, entity);
        if (!EMConfigHandler.COMMON.MOB.CORPSES.CORPSE.enableConvertToCorpse.get()) return flag;
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
                convertEntity.setInitSpawn();
                convertEntity.setOwner(this.getOwner());
            }
            return false;
        }
        return true;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
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
}
