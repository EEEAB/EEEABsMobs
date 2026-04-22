package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.AnimationNotification;
import com.eeeab.animate.server.animation.OverlapAnimationState;
import com.eeeab.animate.server.animation.keyframe.CondKeyframe;
import com.eeeab.animate.server.animation.keyframe.KeyframeManager;
import com.eeeab.animate.server.animation.release.AnimationCondition;
import com.eeeab.animate.server.animation.release.AnimationReleaseManager;
import com.eeeab.animate.server.animation.release.AnimationRule;
import com.eeeab.animate.server.animation.release.ConditionFactory;
import com.eeeab.animate.server.animation.release.cooldown.CooldownManager;
import com.eeeab.animate.server.animation.release.cooldown.FixedRangeCooldown;
import com.eeeab.animate.server.animation.release.cooldown.HealthScaledCooldown;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.client.render.LightningBolt;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.ModBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate.GuardianLeapGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityElectromagnetic;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityInfraredRay;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityAnnihilatorMissile;
import com.eeeab.eeeabsmobs.sever.entity.mob.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.ModDamageSource;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.ModMathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

public class EntityRelicAnnihilator extends EntityAbsRelicron implements IBoss, RangedAttackMob {
    public static final Animation DIE_ANIMATION = Animation.create(60);
    public static final Animation SLASH_ANIMATION = Animation.create(50).doesOverlap();
    public static final Animation SWING_ANIMATION = Animation.create(50).doesOverlap();
    public static final Animation STAB_ANIMATION = Animation.create(60).doesOverlap();
    public static final Animation CYCLONE_ANIMATION = Animation.create(60);
    public static final Animation SHOT_ANIMATION1 = AnimationNotification.create(35, "weak_point");
    public static final Animation SHOT_ANIMATION2 = Animation.create(20).doesLoop();
    public static final Animation SHOT_ANIMATION3 = Animation.create(15);
    public static final Animation TRICKSHOT_ANIMATION1 = AnimationNotification.create(20, "weak_point");
    public static final Animation TRICKSHOT_ANIMATION2 = Animation.create(20).doesLoop();
    public static final Animation TRICKSHOT_ANIMATION3 = Animation.create(20);
    public static final Animation LASER_ANIMATION = AnimationNotification.create(90, "weak_point");
    public static final Animation GROUND_POUND_ANIMATION = Animation.create(50);
    public static final Animation GROUND_POUND_ANIMATION2 = Animation.create(50);
    public static final Animation GROUND_SLAM_ANIMATION1 = Animation.create(15);
    public static final Animation GROUND_SLAM_ANIMATION2 = Animation.create(100);
    public static final Animation GROUND_SLAM_ANIMATION3 = Animation.create(40);
    public static final Animation STUN_ANIMATION = Animation.create(80);
    public static final Animation BACKDASH_ANIMATION = Animation.create(10);
    public static final Animation ACTIVE_ANIMATION = Animation.create(50);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(40);
    private static final Animation[] ANIMATIONS = new Animation[]{
            SLASH_ANIMATION,
            SWING_ANIMATION,
            STAB_ANIMATION,
            CYCLONE_ANIMATION,
            LASER_ANIMATION,
            GROUND_POUND_ANIMATION,
            GROUND_POUND_ANIMATION2,
            GROUND_SLAM_ANIMATION1,
            GROUND_SLAM_ANIMATION2,
            GROUND_SLAM_ANIMATION3,
            SHOT_ANIMATION1,
            SHOT_ANIMATION2,
            SHOT_ANIMATION3,
            TRICKSHOT_ANIMATION1,
            TRICKSHOT_ANIMATION2,
            TRICKSHOT_ANIMATION3,
            DIE_ANIMATION,
            STUN_ANIMATION,
            BACKDASH_ANIMATION,
            ACTIVE_ANIMATION,
            DEACTIVATE_ANIMATION,
    };
    private static final KeyframeManager<EntityRelicAnnihilator> KEYFRAME_MANAGER;
    private static final AnimationReleaseManager<EntityRelicAnnihilator> ANIMATION_RELEASE_MANAGER;
    private static final EntityDataAccessor<Boolean> DATA_BLIND = SynchedEntityData.defineId(EntityRelicAnnihilator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_REST_POS = SynchedEntityData.defineId(EntityRelicAnnihilator.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private int blindnessDuration;
    private boolean LRFlag;//T:left F:right
    @OnlyIn(Dist.CLIENT)
    public Vec3 muzzle;
    private Vec3 preMuzzle = Vec3.ZERO;
    @OnlyIn(Dist.CLIENT)
    public Vec3 saw;
    private Vec3 preSaw = Vec3.ZERO;
    private final EntityRelicAnnihilatorPart scope;
    private final EntityRelicAnnihilatorPart[] subEntities;
    private final OverlapAnimationState slashAnimationState = new OverlapAnimationState(SLASH_ANIMATION);
    private final OverlapAnimationState swingAnimationState = new OverlapAnimationState(SWING_ANIMATION);
    private final OverlapAnimationState stabAnimationState = new OverlapAnimationState(STAB_ANIMATION);
    public final ControlledAnimation handControlled = new ControlledAnimation(10);
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);
    public final ControlledAnimation sawControlled = new ControlledAnimation(10);

    public EntityRelicAnnihilator(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.active = false;
        this.LRFlag = level.random.nextBoolean();
        this.clearRedundantAnimationsOnDeath = true;
        this.dropAfterDeathAnim = false;
        this.scope = new EntityRelicAnnihilatorPart(this, "scope", 0.825F, 0.75F);
        this.subEntities = new EntityRelicAnnihilatorPart[]{this.scope};
        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        if (this.level().isClientSide) {
            this.muzzle = new Vec3(0, 0, 0);
            this.saw = new Vec3(0, 0, 0);
        }
    }

    static {
        KEYFRAME_MANAGER = setupAnimations();
        ANIMATION_RELEASE_MANAGER = setupAnimationRules();
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.BOSS;
    }

    @Override
    public boolean isStunned() {
        return super.isStunned() || this.getAnimation() == STUN_ANIMATION;
    }

    @Override//是否在实体上渲染着火效果
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    @Override//是否被流体推动
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height;
    }

    @Override//被方块阻塞
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return super.isInvulnerableTo(damageSource) || animation == BACKDASH_ANIMATION || animation == ACTIVE_ANIMATION || animation == DEACTIVATE_ANIMATION;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subEntities.length; i++)
            this.subEntities[i].setId(id + i + 1);
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    @NotNull
    protected BodyRotationControl createBodyControl() {
        return new ModBodyRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new ModPathNavigateGround(this, level);
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicAnnihilator.combatConfig;
    }

    @Override
    protected ModConfigHandler.BossConfig getBossConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicAnnihilator.bossConfig;
    }

    @Override
    protected float activeRange() {
        return 7F;
    }

    @Override
    protected void updateActivationState() {
        this.timeUntilDeactivate = 0;
        super.updateActivationState();
        if (!this.isAlwaysActive() && this.isNoAnimation() && this.isActive() && this.getTarget() == null && this.zza == 0 && this.isAtRestPos()) {
            this.playAnimation(this.getDeactivateAnimation());
            this.setActive(false);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, true) {
            @Override
            public boolean canUse() {
                if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
                    return false;
                }
                this.findTarget();
                return this.target != null && this.canAttack(this.target, this.targetConditions);
            }
        });
        this.goalSelector.addGoal(5, new RelicronRandomStrollGoal(this, 0.8));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(0, new AnimationSimpleAI<>(this, STUN_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, BACKDASH_ANIMATION) {
            @Override
            public void tick() {
                LivingEntity target = getTarget();
                if (target != null) {
                    lookAt(target, 30F, 30F);
                    getLookControl().setLookAt(target, 30F, 30F);
                }
                backOff(target, getRetreatSpeed(3F, getBbWidth() * 3, targetDistance));
            }
        });
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new RAMeleeAttackGoal(this));
        this.goalSelector.addGoal(1, new RARangeAttackGoal(this));
        this.goalSelector.addGoal(1, new RACycloneAttackGoal(this));
        this.goalSelector.addGoal(1, new RALeapAttackGoal(this));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, GROUND_SLAM_ANIMATION3, 3F, 6F, 3, 1.2F, 1.2F, true));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, GROUND_POUND_ANIMATION, 2.5F, 6F, 20, 1.2F, 1.2F, true));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, GROUND_POUND_ANIMATION2, 3F, 7F, 20, 1.25F, 1.3F, true) {
            @Override
            protected void onHit(LivingEntity entity) {
                entity.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 200, 0, false, false, true));
            }
        });
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, ACTIVE_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationDeactivate<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.addGoal(2, new KeepDistanceGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        this.handControlled.updatePrevTimer();
        this.glowControlled.updatePrevTimer();
        this.sawControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);

        if (!this.isActive()) this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

        Animation animation = this.getAnimation();

        if (!this.isAlwaysActive() && !this.isNoAi() && animation == NO_ANIMATION && this.isActive() && this.getTarget() == null
                && this.getNavigation().isDone() && !this.isAtRestPos()) {
            this.tryMoveToRestPos();
        }

        if (this.level().isClientSide && this.isAlive() && this.isActive() && animation != CYCLONE_ANIMATION) {
            if (this.isBlinded() && animation == NO_ANIMATION && this.tickCount % 3 == 0) {
                Vec3 pos = this.scope.position().offsetRandom(this.random, 1.2F);
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, pos.x, pos.y + 0.2, pos.z, this.random.nextGaussian() * 0.01, 0.007, this.random.nextGaussian() * 0.01);
            }
            if (this.saw != null && this.tickCount % 5 == 0 && this.random.nextInt(5) == 0) {
                Vec3 pos = this.isNoAnimation() ? this.getPosOffset(true, 0F, getBbWidth(), getBbHeight() * 0.25F) : this.saw;
                pos = pos.offsetRandom(this.random, 0.75F);
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
            }
            if (this.getDeltaMovement().horizontalDistanceSqr() > 0.005) {
                float bbWidth = this.getBbWidth();
                float width = bbWidth * 0.45F;
                float frontBack = 0;
                if (animation == STAB_ANIMATION) width = bbWidth * 0.55F;
                if (animation == TRICKSHOT_ANIMATION2) {
                    frontBack = bbWidth * -0.3F;
                    this.doWalkEffect(this.getPosOffset(true, frontBack, bbWidth * 1.1F, 0));
                }
                this.doWalkEffect(this.getPosOffset(true, frontBack, width, 0));
                this.doWalkEffect(this.getPosOffset(false, 0, width, 0));
            }
            if (!this.isSilent() && !this.sawControlled.isStop() && this.tickCount % 4 == 1) {
                this.level().playLocalSound(this.saw.x, this.saw.y, this.saw.z, SoundInit.RELIC_ANNIHILATOR_SAW.get(), this.getSoundSource(),
                        this.getVoicePitch() * this.sawControlled.getAnimationFraction(), this.getVoicePitch() + 0.2F, false);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        ANIMATION_RELEASE_MANAGER.tick(this, this.getCooldownManager());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isNoAnimation()) this.sawControlled.decreaseTimer();
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());
        if (!this.level().isClientSide) {
            if (this.blindnessDuration > 0) this.blindnessDuration--;
            else this.setBlind(false);
        }

        if (!this.isNoAi()) {
            Vec3[] avec3 = new Vec3[]{new Vec3(this.subEntities[0].getX(), this.subEntities[0].getY(), this.subEntities[0].getZ())};
            float y = this.getEyeHeight();
            float sides = 0;
            float frontBack = 0.5F;
            Animation animation = this.getAnimation();
            if (animation == SHOT_ANIMATION1 || animation == SHOT_ANIMATION2) {
                y *= 0.86F;
                sides = this.getBbWidth() * 0.18F;
                frontBack = 0.4F;
            } else if (animation == TRICKSHOT_ANIMATION1 || animation == TRICKSHOT_ANIMATION2) {
                y *= 0.77F;
                frontBack = 0.7F;
            } else if (animation == LASER_ANIMATION) {
                y *= 0.575F;
                frontBack = 1.2F;
            }
            this.scope.setPos(this.getPosOffset(true, frontBack, sides, y - 0.3F));
            this.scope.xo = avec3[0].x;
            this.scope.yo = avec3[0].y;
            this.scope.zo = avec3[0].z;
            this.scope.xOld = avec3[0].x;
            this.scope.yOld = avec3[0].y;
            this.scope.zOld = avec3[0].z;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        return this.hurt(source, damage, false);
    }

    public boolean hurt(DamageSource source, float damage, boolean isCriticalSpot) {
        if (this.level().isClientSide) {
            return false;
        } else if (source.getEntity() == this) {
            return false;
        } else {
            float multiplier = 0.8F;
            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                Animation animation = this.getAnimation();
                if (animation == LASER_ANIMATION || animation == SHOT_ANIMATION2 || animation == TRICKSHOT_ANIMATION2) {
                    multiplier = 0.2F;
                }
            } else multiplier = 1F;
            if (isCriticalSpot) {
                damage += this.getHealth() * 0.05F;
                multiplier = 1F;
            }
            return super.hurt(source, damage * multiplier);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        this.performRangedAttack(this.position().add(0, this.getBbHeight(), 0));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        if (spawnType == MobSpawnType.STRUCTURE) this.setYRot(yBodyRot = 180);
        this.setRestPos(this.blockPosition());
        return super.finalizeSpawn(level, instance, spawnType, groupData, tag);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(this.random.nextFloat() < 0.3F ? ParticleInit.GUARDIAN_SPARK.get() : ParticleTypes.LARGE_SMOKE, this.getRandomX(1.5D), this.getY() + (0.1F + (this.getBbHeight() * 0.75) * this.random.nextDouble()), this.getRandomZ(1.5D), d0, d1, d2);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BLIND, false);
        this.entityData.define(DATA_REST_POS, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("restPos")) {
            this.setRestPos(NbtUtils.readBlockPos(compound.getCompound("restPos")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.getRestPos().ifPresent(pos -> compound.put("restPos", NbtUtils.writeBlockPos(pos)));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 300.0D).
                add(Attributes.MOVEMENT_SPEED, 0.34D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.ATTACK_DAMAGE, 12.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).
                add(Attributes.ARMOR, 10.0D).
                add(ForgeMod.ENTITY_GRAVITY.get(), 0.125D).
                add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 4D);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        SoundType soundtype = state.getSoundType(this.level(), pos, this);
        this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch());
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.RELIC_ANNIHILATOR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(SoundInit.RELIC_ANNIHILATOR_DEATH.get(), this.getSoundVolume(), this.getVoicePitch() + 0.2F);
        return null;
    }

    @Override
    protected SoundEvent getActiveSound() {
        return SoundInit.RELIC_ANNIHILATOR_ACTIVE.get();
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.05F;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.RELIC_ANNIHILATOR_IDLE.get();
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    protected Animation getActiveAnimation() {
        return ACTIVE_ANIMATION;
    }

    @Override
    protected Animation getDeactivateAnimation() {
        return DEACTIVATE_ANIMATION;
    }

    @Override
    public KeyframeManager<EntityRelicAnnihilator> getKeyframeManager() {
        return KEYFRAME_MANAGER;
    }

    @Override
    public AnimationState getOverlapAnimationState(Animation animation) {
        if (SLASH_ANIMATION == animation) {
            return this.slashAnimationState;
        } else if (SWING_ANIMATION == animation) {
            return this.swingAnimationState;
        } else if (STAB_ANIMATION == animation) {
            return this.stabAnimationState;
        }
        return null;
    }

    @Override
    protected float getVulnMultiplier() {
        return this.isStunned() ? 1.2F : 1F;
    }

    public boolean isBlinded() {
        return this.entityData.get(DATA_BLIND);
    }

    public void setBlind(boolean blind) {
        if (blind) this.blindnessDuration = 400;
        this.entityData.set(DATA_BLIND, blind);
    }

    public Optional<BlockPos> getRestPos() {
        return getEntityData().get(DATA_REST_POS);
    }

    public void setRestPos(BlockPos pos) {
        getEntityData().set(DATA_REST_POS, Optional.of(pos));
    }

    private boolean isAtRestPos() {
        Optional<BlockPos> restPos = getRestPos();
        return restPos.filter(pos -> pos.distSqr(blockPosition()) < 9).isPresent();
    }

    private void tryMoveToRestPos() {
        boolean unableReach = true;
        if (getRestPos().isPresent()) {
            BlockPos pos = getRestPos().get();
            if (getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1)) {
                unableReach = false;
            }
        }
        if (unableReach) {
            setRestPos(blockPosition());
        }
    }

    private static KeyframeManager<EntityRelicAnnihilator> setupAnimations() {
        KeyframeManager<EntityRelicAnnihilator> manager = new KeyframeManager<>();
        KeyframeManager.KeyframeManegerBuilder<EntityRelicAnnihilator> builder = manager.builder();
        builder.forAnimation(SLASH_ANIMATION)
                .inRange(10, 35, (entity, animation, tick) -> {
                    entity.sawControlled.incrementOrDecreaseTimer(tick >= 10 && tick < 25);
                    if (tick == 15) entity.playSound(SoundInit.RELIC_ANNIHILATOR_ATTACK.get(), 1F, entity.getVoicePitch());
                    entity.doTrailEffect(tick == 17, tick > 17 && tick < 25, false);
                });
        builder.forAnimation(SWING_ANIMATION)
                .inRange(15, 24, (entity, animation, tick) -> {
                    if (tick == 15) entity.playSound(SoundInit.RELIC_ANNIHILATOR_ATTACK.get(), 1F, entity.getVoicePitch());
                    entity.doTrailEffect(tick == 17, tick > 17 && tick < 25, true);
                });
        builder.forAnimation(STAB_ANIMATION)
                .inRange(5, 55, (entity, animation, tick) -> {
                    if (tick == 5) entity.playSound(SoundInit.RELIC_ANNIHILATOR_PRE_ATTACK.get(), 1F, entity.getVoicePitch());
                    entity.sawControlled.incrementOrDecreaseTimer(tick >= 8 && tick < 45);
                    entity.doTrailEffect(tick == 21, tick > 21 && tick < 32, false);
                    if (tick > 31 && tick < 42) entity.doFractalEffect();
                });
        builder.forAnimation(CYCLONE_ANIMATION)
                .inRange(5, 50, (entity, animation, tick) -> {
                    entity.sawControlled.incrementOrDecreaseTimer(tick >= 5 && tick < 40);
                    if (tick == 5) entity.playSound(SoundInit.RELIC_ANNIHILATOR_PRE_ATTACK.get(), 1F, entity.getVoicePitch());
                    if (tick > 16 && tick < 37 && tick % 5 == 0) entity.playSound(SoundInit.RELIC_ANNIHILATOR_WHOOSH.get(), 2.5F, entity.getVoicePitch());
                    if (tick > 20 && tick < 39) entity.doCycloneEffect();
                    entity.doTrailEffect(tick == 22, tick > 22 && tick < 43, false);
                });
        builder.forAnimation(LASER_ANIMATION)
                .inRange(5, 52, (entity, animation, tick) -> {
                    if (tick == 5) entity.playSound(SoundInit.RELIC_ANNIHILATOR_ACTIVE_SCOPE.get(), 1F, entity.getVoicePitch());
                    entity.doShotLaserEffect();
                });
        builder.forAnimation(GROUND_POUND_ANIMATION)
                .inRange(1, 20, (entity, animation, tick) -> {
                    if (tick == 4) entity.playSound(SoundInit.RELIC_ANNIHILATOR_PRE_ATTACK.get(), 1F, entity.getVoicePitch());
                    if (tick == 5) entity.playSound(SoundInit.RELIC_ANNIHILATOR_SMASH.get(), 1F, entity.getVoicePitch());
                    if (tick == 20) entity.doGroundPoundEffect(2.25F, false);
                    entity.doTrailEffect(tick == 18, tick > 18 && tick < 21, true);
                });
        builder.forAnimation(GROUND_POUND_ANIMATION2)
                .inRange(1, 20, (entity, animation, tick) -> {
                    entity.doTrailEffect(tick == 18, tick > 18 && tick < 21, false);
                    if (tick == 4) entity.playSound(SoundInit.RELIC_ANNIHILATOR_PRE_ATTACK.get(), 1F, entity.getVoicePitch());
                    if (tick == 5) entity.playSound(SoundInit.RELIC_ANNIHILATOR_SMASH.get(), 1F, entity.getVoicePitch());
                    if (tick == 20) entity.doGroundPoundEffect(2.25F, false);
                }).inRange(20, 24, (entity, animation, tick) -> {
                            Vec3 pos = entity.getPosOffset(false, 2.25F, 0.2F, 0);
                            if (entity.level().isClientSide) {
                                if (tick == 20) {
                                    AdvancedParticleBase.spawnParticle(entity.level(), ParticleInit.GLOW.get(), pos.x, pos.y, pos.z, 0, 0, 0, true, 0, 0, 0, 0,
                                            1, BOLT_COLORS[0].x, BOLT_COLORS[0].y, BOLT_COLORS[0].z, BOLT_COLORS[0].w, 1, 5, true, false, false, new ParticleComponent[]{
                                                    new PropertyControl(EnumParticleProperty.ALPHA, new AnimData.Oscillator(BOLT_COLORS[0].w, 0, 15, 0), false),
                                                    new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(10, 35), false),
                                            });
                                }
                                for (int i = 0; i < 3; i++) {
                                    RandomSource source = entity.random;
                                    ClientProxy.LIGHTNING_RENDER.update(entity, RELICRON_BOLT.color(BOLT_COLORS[source.nextInt(2)]).build(pos, pos.add((source.nextFloat() - 0.5F) * 5, 0.5 + source.nextFloat() * entity.getBbHeight(), (source.nextFloat() - 0.5F) * 5), entity.random));
                                }
                            } else if (tick == 20 && entity.getHealthPercentage() <= 0.5F) {
                                final int count = 6;
                                float offset = (float) Math.toRadians(entity.getRandom().nextGaussian() * 360 - 180);
                                for (int i = 0; i < count; ++i) {
                                    float f1 = (float) (entity.getYRot() + (i + offset) * (float) Math.PI * (2.0 / count));
                                    pos = ModEntityUtils.checkSummonEntityPoint(entity, pos.x, pos.z, pos.y, pos.y + 1);
                                    EntityElectromagnetic.shoot(entity.level(), entity, pos, 2.0F, count + 4, 5, (f1 * (180F / (float) Math.PI)) - 90F, false);
                                }
                            }
                        }
                );
        builder.forAnimation(GROUND_SLAM_ANIMATION1)
                .atTick(4, (entity, animation, tick) -> entity.playSound(SoundInit.RELIC_ANNIHILATOR_JUMP.get(), 1F, entity.getVoicePitch()))
                .atTick(13, (entity, animation, tick) -> entity.doGroundPoundEffect(0F, false));
        builder.forAnimation(GROUND_SLAM_ANIMATION3)
                .atTick(1, (entity, animation, tick) -> entity.playSound(SoundInit.RELIC_ANNIHILATOR_SMASH.get(), 1F, entity.getVoicePitch() + 1.2F))
                .atTick(4, (entity, animation, tick) -> entity.doGroundPoundEffect(2.25F, true))
                .inRange(16, 20, (entity, animation, tick) -> entity.doTrailEffect(tick == 16, tick > 16 && tick < 21, true));
        builder.forAnimation(DIE_ANIMATION)
                .inRange(1, 6, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        int count = entity.random.nextInt(3) + 1;
                        Vec3 pos = entity.getPosOffset(false, -entity.getBbWidth() * 0.2F, entity.getBbWidth() * 0.1F, entity.getEyeHeight() * 0.9F);
                        for (int i = 0; i < count; i++) {
                            Vec3 spawnPos = pos.offsetRandom(entity.random, 1.2F);
                            entity.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), spawnPos.x, spawnPos.y, spawnPos.z, 0, 0, 0);
                        }
                    }
                });
        builder.forAnimation(STUN_ANIMATION)
                .inRange(1, 10, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        if (tick == 1) {
                            Vec3 pos = entity.getPosOffset(false, entity.getBbWidth() * 0.5F, 0, entity.getEyeHeight());
                            entity.level().addParticle(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 0, 0, 0);
                        }
                        int count = entity.random.nextInt(3) + 1;
                        Vec3 pos = entity.getPosOffset(false, -entity.getBbWidth() * 0.2F, entity.getBbWidth() * 0.1F, entity.getEyeHeight() * 0.9F);
                        for (int i = 0; i < count; i++) {
                            Vec3 spawnPos = pos.offsetRandom(entity.random, 1.2F);
                            entity.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), spawnPos.x, spawnPos.y, spawnPos.z, 0, 0, 0);
                        }
                    } else {
                        LivingEntity target = entity.getTarget();
                        if (tick == 1) entity.playSound(SoundInit.RELIC_ANNIHILATOR_STUN.get(), 2F, entity.getVoicePitch() + 0.2F);
                        if (tick < 10 && target != null) entity.backOff(target, 0.6 * ModMathUtils.getTickFactor(tick, 9, true));
                    }
                })
                .everyNTick(7, 64, 5, (entity, animation, tick) -> {
                    Vec3 pos = entity.getPosOffset(false, entity.getBbWidth() * 0.9F, 0, entity.getEyeHeight() * 0.6F);
                    Vec3 spawnPos = pos.offsetRandom(entity.random, 0.5F);
                    entity.level().addParticle(ParticleTypes.LARGE_SMOKE, spawnPos.x, spawnPos.y, spawnPos.z, -0.15D + entity.random.nextDouble() * 0.15D, -0.15D + entity.random.nextDouble() * 0.15D, -0.15D + entity.random.nextDouble() * 0.15D);
                });
        builder.conditional(new CondKeyframe<>() {
            @Override
            public boolean shouldHandle(EntityRelicAnnihilator entity, Animation animation, int tick) {
                return (animation == SWING_ANIMATION || animation == GROUND_POUND_ANIMATION || animation == DIE_ANIMATION
                        || animation == STUN_ANIMATION || animation == GROUND_POUND_ANIMATION2) && !entity.sawControlled.isStop();
            }

            @Override
            public void handle(EntityRelicAnnihilator entity, Animation animation, int tick) {
                entity.sawControlled.decreaseTimer();
            }
        });
        builder.conditional(new CondKeyframe<>() {
            @Override
            public boolean shouldHandle(EntityRelicAnnihilator entity, Animation animation, int tick) {
                return animation == GROUND_POUND_ANIMATION || animation == GROUND_SLAM_ANIMATION3 || animation == LASER_ANIMATION;
            }

            @Override
            public void handle(EntityRelicAnnihilator entity, Animation animation, int tick) {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            }
        });
        builder.conditional(new CondKeyframe<>() {
            @Override
            public boolean shouldHandle(EntityRelicAnnihilator entity, Animation animation, int tick) {
                return animation == SHOT_ANIMATION1 || animation == SHOT_ANIMATION2 || animation == SHOT_ANIMATION3 ||
                        animation == TRICKSHOT_ANIMATION1 || animation == TRICKSHOT_ANIMATION2 || animation == TRICKSHOT_ANIMATION3;
            }

            @Override
            public void handle(EntityRelicAnnihilator entity, Animation animation, int tick) {
                if (animation == SHOT_ANIMATION2 || animation == TRICKSHOT_ANIMATION2) {
                    if (tick == 9) entity.doMuzzleFlashEffect();
                    else if (tick == 11 && !entity.level().isClientSide) {
                        Vec3 muzzlePos;
                        float width = entity.getBbWidth();
                        float height = entity.getBbHeight();
                        if (animation == SHOT_ANIMATION2) muzzlePos = entity.getPosOffset(false, width * 2.4F, width * 0.625F, height * 0.96F);
                        else muzzlePos = entity.getPosOffset(false, width, width, height * 0.42F);
                        entity.performRangedAttack(muzzlePos);
                    }
                } else {
                    boolean b0 = animation == TRICKSHOT_ANIMATION1;
                    boolean b1 = animation == SHOT_ANIMATION1;
                    if (b1 && tick == 15) entity.playSound(SoundInit.RELIC_ANNIHILATOR_ACTIVE_SCOPE.get(), 1F, entity.getVoicePitch());
                    else if (b0 && tick == 7) entity.playSound(SoundInit.RELIC_ANNIHILATOR_ACTIVE_SCOPE.get(), 1F, entity.getVoicePitch());
                    entity.handControlled.incrementOrDecreaseTimer(b1 || b0);
                    entity.sawControlled.incrementOrDecreaseTimer(tick >= 12 && b0);
                }
            }
        });
        return manager;
    }

    private static AnimationReleaseManager<EntityRelicAnnihilator> setupAnimationRules() {
        AnimationReleaseManager<EntityRelicAnnihilator> manager = new AnimationReleaseManager<>();
        AnimationReleaseManager.Builder<EntityRelicAnnihilator> builder = manager.builder();
        HealthScaledCooldown baseAttack = new HealthScaledCooldown(180, 20, 40, 0.3F, true);
        AnimationCondition<EntityRelicAnnihilator> heightDiff = ConditionFactory.heightDiff(5);
        HealthScaledCooldown groundPound = new HealthScaledCooldown(400, 50, 50, 0.3F, true);
        AnimationRule<EntityRelicAnnihilator> groundPoundBuild = builder.define(GROUND_POUND_ANIMATION)
                .priority(2)
                .cooldown(groundPound)
                .condition(ConditionFactory.and(
                                ConditionFactory.hybridDistanceRange(8, 0, 4),
                                ConditionFactory.randomChanceOnLowHealth(0.1F, 0.7F),
                                (entity, target) -> entity.LRFlag
                        )
                ).onSuccess(e -> {
                    e.getCooldownManager().setCD(GROUND_POUND_ANIMATION2, groundPound.generate(e));
                    e.LRFlag = !e.LRFlag;
                }).build();
        AnimationRule<EntityRelicAnnihilator> groundPoundBuild2 = builder.define(GROUND_POUND_ANIMATION2)
                .priority(2)
                .cooldown(groundPound)
                .condition(ConditionFactory.and(
                                ConditionFactory.hybridDistanceRange(8, 0, 4),
                                ConditionFactory.randomChanceOnLowHealth(0.1F, 0.7F),
                                (entity, target) -> !entity.LRFlag || target.isOnFire() || entity.getHealthPercentage() <= 0.5
                        )
                ).onSuccess(e -> {
                    e.getCooldownManager().setCD(GROUND_POUND_ANIMATION, groundPound.generate(e));
                    e.LRFlag = !e.LRFlag;
                }).build();
        manager.registerRule(groundPoundBuild);
        manager.registerRule(groundPoundBuild2);
        builder.register(builder.define(SWING_ANIMATION)
                .priority(2)
                .cooldown(baseAttack)
                .condition(ConditionFactory.and(
                        ConditionFactory.hybridDistanceRange(5, 0, 5),
                        ConditionFactory.randomChance(0.5F),
                        (entity, target) -> entity.LRFlag
                )).onSuccess(e -> {
                    e.getCooldownManager().setCD(SLASH_ANIMATION, baseAttack.generate(e));
                    e.LRFlag = !e.LRFlag;
                }).triggerAtTick(35).next(groundPoundBuild).next(groundPoundBuild2)
        );
        builder.register(builder.define(SLASH_ANIMATION)
                .priority(2)
                .cooldown(baseAttack)
                .condition(ConditionFactory.and(
                        ConditionFactory.hybridDistanceRange(5, 0, 5),
                        ConditionFactory.randomChance(0.5F),
                        (entity, target) -> !entity.LRFlag
                )).onSuccess(e -> {
                    e.getCooldownManager().setCD(SWING_ANIMATION, baseAttack.generate(e));
                    e.LRFlag = !e.LRFlag;
                }).triggerAtTick(40).next(groundPoundBuild).next(groundPoundBuild2)
        );
        builder.register(builder.define(LASER_ANIMATION)
                .priority(2)
                .cooldown(new FixedRangeCooldown(480, 100, true))
                .condition(ConditionFactory.and(
                                ConditionFactory.healthBelow(0.8F),
                                (entity, target) -> !entity.isBlinded(),
                                ConditionFactory.distanceRange(9, 24),
                                ConditionFactory.randomChance(0.4F),
                                ConditionFactory.hasLineOfSight()
                        )
                ));
        HealthScaledCooldown rangAttack = new HealthScaledCooldown(360, 20, 60, 0.5F, true);
        builder.register(builder.define(SHOT_ANIMATION1)
                .priority(1)
                .cooldown(rangAttack)
                .condition(ConditionFactory.and(
                                ConditionFactory.hybridDistanceRange(7, 7, 32),
                                ConditionFactory.randomChance(0.4F),
                                ConditionFactory.hasLineOfSight()
                        )
                ).onSuccess(entity -> entity.getCooldownManager().setCD(TRICKSHOT_ANIMATION1, rangAttack.generate(entity))));
        builder.register(builder.define(TRICKSHOT_ANIMATION1)
                .priority(1)
                .cooldown(rangAttack)
                .condition(ConditionFactory.and(
                                ConditionFactory.hybridDistanceRange(12, 4, 8),
                                ConditionFactory.randomChance(0.4F)
                        )
                ).onSuccess(entity -> entity.getCooldownManager().setCD(SHOT_ANIMATION1, rangAttack.generate(entity))));
        builder.register(builder.define(STAB_ANIMATION)
                .priority(1)
                .cooldown(new HealthScaledCooldown(300, 10, 40, 0.3F, true))
                .condition(ConditionFactory.and(
                        heightDiff,
                        ConditionFactory.distanceRange(3, 16),
                        ConditionFactory.angleRange(-60F, 60F),
                        ConditionFactory.or(
                                ConditionFactory.randomChance(0.4F),
                                (entity, target) -> entity.getTarget() != null && entity.getTarget().isOnFire()
                        )
                )).triggerAtTick(50).next(groundPoundBuild).next(groundPoundBuild2)
        );
        builder.register(builder.define(CYCLONE_ANIMATION)
                .priority(1)
                .cooldown(new HealthScaledCooldown(300, 25, 25, 0.3F, true))
                .condition(ConditionFactory.and(
                        heightDiff,
                        ConditionFactory.healthBelow(0.8F),
                        ConditionFactory.distanceRange(3, 8, 9),
                        ConditionFactory.angleRange(-60F, 60F),
                        ConditionFactory.or(
                                ConditionFactory.randomChance(0.4F),
                                (entity, target) -> entity.getTarget() != null && entity.getTarget().isOnFire()
                        )
                )));
        builder.register(builder.define(GROUND_SLAM_ANIMATION1)
                .cooldown(new HealthScaledCooldown(350, 50, 50, 0.3F, true))
                .condition(ConditionFactory.and(
                                ConditionFactory.distanceRange(12, 22),
                                ConditionFactory.randomChance(0.4F),
                                ConditionFactory.hasLineOfSight()
                        )
                ));
        builder.register(builder.define(BACKDASH_ANIMATION)
                .cooldown(new FixedRangeCooldown(400, 100, true))
                .condition(ConditionFactory.and(
                        (entity, target) -> {
                            CooldownManager cooldownManager = entity.getCooldownManager();
                            return cooldownManager.isReady(CYCLONE_ANIMATION) || cooldownManager.isReady(STAB_ANIMATION)
                                    || cooldownManager.isReady(LASER_ANIMATION) || cooldownManager.isReady(SHOT_ANIMATION1);
                        },
                        ConditionFactory.hybridDistanceRange(6, 0, 4)
                ))
        );
        builder.condition(EntityAbsRelicron::isActive);
        return manager;
    }

    public boolean doHurtTarget(DamageSource damageSource, LivingEntity entity, float damageMultiplier, float knockBackMultiplier, boolean canDisableShield, boolean charged) {
        if (this.doHurtTarget(damageSource, entity, damageMultiplier, knockBackMultiplier, canDisableShield)) {
            if (canDisableShield) {
                boolean hard = this.level().getDifficulty() == Difficulty.HARD;
                if (this.random.nextFloat() < (hard ? 0.75F : 0.25F)) this.stun(null, entity, 30, false);
            }
            if (charged) entity.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 200, 0, false, false, true));
            return true;
        }
        return false;
    }

    public EntityRelicAnnihilatorPart getPartEntity() {
        return this.scope;
    }

    public void backOff(LivingEntity target, double speed) {
        Vec3 direction;
        if (target != null) {
            direction = new Vec3(this.getX() - target.getX(), 0.0, this.getZ() - target.getZ()).normalize();
        } else {
            speed = 0.4F;
            double radians = Math.toRadians(this.yBodyRot + 270);
            direction = new Vec3(Math.cos(radians), 0.0, Math.sin(radians)).normalize();
        }
        double moveSpeed = this.getAttributeValue(Attributes.MOVEMENT_SPEED) * speed;
        this.setDeltaMovement(direction.x * moveSpeed, this.getDeltaMovement().y, direction.z * moveSpeed);
    }

    public static double getRetreatSpeed(float baseSpeed, double retreatRange, double currentDist) {
        double closeThreshold = 0.5;
        double farThreshold = retreatRange / 2F;
        float minSpeed = baseSpeed * 0.3F;
        double speedBonus = 0.0;
        if (currentDist < retreatRange - closeThreshold) {
            speedBonus = Math.min(baseSpeed * 0.25F, (retreatRange - closeThreshold - currentDist) * 0.2);
        }
        double speedPenalty = 0.0;
        if (currentDist > retreatRange + farThreshold) {
            speedPenalty = Math.min(baseSpeed * 0.35F, (currentDist - retreatRange - farThreshold) * 0.2);
        }
        return Math.max(minSpeed, baseSpeed + speedBonus - speedPenalty);
    }

    public static boolean canBeControlled(LivingEntity entity, LivingEntity target) {
        if (!target.isAlive() || target.isSpectator() || target.isMultipartEntity() || target.isPassenger()) return false;
        AABB box1 = entity.getBoundingBox().inflate(0.5);
        double length1 = box1.getXsize();
        double width1 = box1.getZsize();
        double height1 = box1.getYsize();
        AABB box2 = target.getBoundingBox();
        double length2 = box2.getXsize();
        double width2 = box2.getZsize();
        double height2 = box2.getYsize();
        return (length1 * width1 * height1) >= (length2 * width2 * height2);
    }

    private void doGroundPoundEffect(float offset, boolean block) {
        Vec3 pos = this.getPosOffset(false, offset, 0.2F, 0);
        if (this.level().isClientSide) {
            int[] particles = {15, 20, 10};
            double[] radii = {1, 1.5, 2};
            double[] speeds = {1, 0.9, 0.8};
            double[] angles = {35, 25, 15};
            double[] color = {0.8, 0.8, 0.8, 0.5F};
            ModParticleUtils.multiLayerBowlParticles(this.level(), pos, 2, particles, radii, speeds, angles, color, null, 0.9F);
            this.level().addParticle(new ParticleRing.RingData(ParticleInit.BIG_RING.get(), 0F, (float) (Math.PI / 2F), 10, 0.8F, 0.8F, 0.8F, 0.8F, 90F, false, ParticleRing.EnumRingBehavior.GROW), pos.x, pos.y + 0.5F, pos.z, 0, 0, 0);
            ModParticleUtils.blockParticlesAround(this.level(), pos.x, pos.y - 0.2F, pos.z, 40, 0.5, 2.5, block ? 5 : 3, block ? 12 : 6, block ? 8 : 3, block ? 10 : 6, -0.2, 0.1);
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 30F, 30, ParticleDust.EnumDustBehavior.GROW, 0.76F);
            ModParticleUtils.annularParticleOutburst(this.level(), 20, dustData, pos.x, pos.y, pos.z, 1.55, 0.5);
        }
        if (block) ShockWaveUtils.doRingShockWave(this, pos, 2, 0F, false, 10);
        EntityCameraShake.cameraShake(level(), pos, 15, 0.15F, 2, 4);
    }

    private void doWalkEffect(Vec3 wheelPos) {
        Vec3 movement = this.getDeltaMovement();
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(wheelPos.x, wheelPos.y - 0.2, wheelPos.z);
        BlockState blockState = this.level().getBlockState(blockPos);
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            double particleX = wheelPos.x + (this.random.nextDouble() - 0.5D) * 0.3;
            double particleZ = wheelPos.z + (this.random.nextDouble() - 0.5D) * 0.3;
            particleX = Mth.clamp(particleX, blockPos.getX(), blockPos.getX() + 1D);
            particleZ = Mth.clamp(particleZ, blockPos.getZ(), blockPos.getZ() + 1D);
            double horizontalSpeed = Math.min(movement.horizontalDistance() * 5, 1);
            double speedMultiplier = 0.5 + horizontalSpeed * 1.5;
            double upwardForce = 0.3 + horizontalSpeed * 0.5;
            this.level().addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                    particleX, wheelPos.y, particleZ,
                    movement.x * -speedMultiplier,
                    upwardForce,
                    movement.z * -speedMultiplier
            );
        }
    }

    private void doCycloneEffect() {
        if (this.level().isClientSide && this.tickCount % 3 == 0) {
            Vec3 movement = this.getDeltaMovement();
            int count = 1 + this.random.nextInt(3);
            for (int i = 0; i < count; i++) {
                float yaw = i % 2 == 0 ? Mth.PI : 0;
                AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.VORTEX.get(), this.getRandomX(0.1), this.getRandomY() + 0.1F, this.getRandomZ(0.1),
                        movement.x, movement.y, movement.z, false, 0, (float) (Math.PI / 2F), 0, 0, 10, 0.8F, 0.8F, 0.8F,
                        1, 1, 9 + this.random.nextInt(4), true, true, false,
                        new ParticleComponent[]{
                                new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(10, 40 + this.random.nextInt(20)), false),
                                new PropertyControl(EnumParticleProperty.YAW, AnimData.startAndEnd(yaw, yaw + (float) Math.toRadians(this.random.nextInt(3) + 1)), true),
                                new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.4F + this.random.nextInt(5) * 0.01F, 0.0F), false),
                        });
            }
            if (movement.horizontalDistanceSqr() > (double) 2.5000003E-7F) this.doWalkEffect(3);
        }
    }

    private void doMuzzleFlashEffect() {
        this.playSound(SoundInit.RELIC_ANNIHILATOR_LAUNCH.get(), 1F, this.getVoicePitch());
        if (this.level().isClientSide && this.muzzle != null) {
            float yawRad = (float) Math.toRadians(this.getYRot());
            float pitchRad = (float) Math.toRadians(this.getXRot());
            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);
            Vec3 forward = new Vec3(x, y, z);
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.THUMP_RING.get(), muzzle.x + forward.x * 0.5, muzzle.y, muzzle.z + forward.z * 0.5
                    , 0, 0, 0, true, 0, 0, 0, 0, 1F,
                    1, 0.94, 0.69, 1, 1, 4, true, false, false
                    , new ParticleComponent[]{
                            new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(4F, 20F), false),
                            new PropertyControl(EnumParticleProperty.ALPHA, new AnimData.KeyTrack(new float[]{1F, 1F, 0.5F, 0F}, new float[]{0F, 0.5F, 0.75F, 1F}), false)
                    });
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.CROSS_FLASH.get(), muzzle.x + forward.x * 0.9, muzzle.y, muzzle.z + forward.z * 0.9
                    , 0, 0, 0, true, 0, 0, 0, 0, 1F,
                    1, 0.94, 0.69, 1, 1, 3, true, false, false
                    , new ParticleComponent[]{
                            new PropertyControl(EnumParticleProperty.SCALE, new AnimData.KeyTrack(new float[]{0F, 8F, 0F}, new float[]{0F, 0.5F, 1F}), false)
                    });
            double baseSpeed = 0.5;
            for (int i = 0; i < 15; i++) {
                Vec3 spawnPos = this.muzzle.add(
                        (random.nextDouble() - 0.5) * 2 * 0.1
                        , (random.nextDouble() - 0.5) * 2 * 0.1,
                        (random.nextDouble() - 0.5) * 2 * 0.1
                );
                Vec3 finalDirection = forward.add(
                        (random.nextDouble() - 0.5) * 2 * 0.08,
                        (random.nextDouble() - 0.5) * 2 * 0.08,
                        (random.nextDouble() - 0.5) * 2 * 0.08
                ).normalize();
                boolean large = this.random.nextBoolean();
                if (large) baseSpeed = 0.4;
                Vec3 velocity = finalDirection.scale(baseSpeed * (0.5 + random.nextDouble() * 1.2));
                this.level().addParticle(
                        large ? ParticleTypes.LARGE_SMOKE : ParticleTypes.SMOKE,
                        spawnPos.x, spawnPos.y, spawnPos.z,
                        velocity.x, velocity.y, velocity.z
                );
            }
        }
    }

    public void performRangedAttack(Vec3 muzzlePos) {
        if (this.level().isClientSide) return;
        EntityAnnihilatorMissile.ElementType element;
        LivingEntity target = this.getTarget();
        Vec3 shootVec;
        if (target != null) {
            element = target.hasEffect(EffectInit.ELECTRIFIED_EFFECT.get()) ? EntityAnnihilatorMissile.ElementType.BLAZE : EntityAnnihilatorMissile.ElementType.VOLT;
            if (this.getHealthPercentage() < 0.5F && this.random.nextFloat() < 0.2F) element = EntityAnnihilatorMissile.ElementType.SPARKFERNO;
            Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.4, 0);
            Vec3 projectileMid = muzzlePos.add(0, 0.25, 0);
            shootVec = targetPos.subtract(projectileMid).normalize();
        } else {
            element = EntityAnnihilatorMissile.ElementType.VOLT;
            shootVec = this.getLookAngle();
        }
        EntityAnnihilatorMissile missile = new EntityAnnihilatorMissile(this.level(), this, element);
        missile.setPos(muzzlePos.x, muzzlePos.y, muzzlePos.z);
        missile.shoot(shootVec.x, shootVec.y, shootVec.z, 1.6F, this.isBlinded() ? 30F : 0F);
        this.level().addFreshEntity(missile);
    }

    private void doShotLaserEffect() {
        int tick = this.getAnimationTick();
        if (this.level().isClientSide) {
            float yawRad = (float) Math.toRadians(this.getYRot());
            float pitchRad = (float) Math.toRadians(this.getXRot());
            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);
            Vec3 forward = this.scope.position().add(0, this.scope.getBbHeight() * 0.24, 0).add(x * 0.15, y, z * 0.15);
            if (tick >= 10 && tick < 50) {
                if (tick % 4 == 1) {
                    AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.GLOW.get(), forward.x, forward.y, forward.z
                            , 0, 0, 0, true, 0, 0, 0, 0, 1F,
                            0.77, 0.25, 0.25, 0.7, 1, 4, true, false, false
                            , new ParticleComponent[]{
                                    new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(18, 14), false),
                                    new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.7F, 0F), false)
                            });
                    if (tick < 40)
                        AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_RING.get(), forward.x, forward.y, forward.z
                                , 0, 0, 0, true, (float) Math.toRadians(-this.getYRot()), (float) Math.toRadians(-this.getXRot()), 0, 0, 1F,
                                0.77, 0.25, 0.25, 1, 1, 3, true, false, false
                                , new ParticleComponent[]{
                                        new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(12, 0), false),
                                        new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.8F, 0F), false)
                                });
                }
                if (tick % 3 == 1 && tick < 40) {
                    Vec3 randomPos = forward.offsetRandom(this.random, 5F);
                    AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_ORB.get(), randomPos.x, randomPos.y, randomPos.z
                            , 0, 0, 0, true, 0, 0, 0, 0, 0,
                            0.77, 0.25, 0.25, 1, 1, 3, true, false, false
                            , new ParticleComponent[]{
                                    new ParticleComponent.Attractor(new Vec3[]{forward}, 1.4F, 1F, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                                    new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 6, 0, 0, 0, 0.05F, 1, 0.4, 0.4, 1, true, true,
                                            new ParticleComponent[]{
                                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0.8F, 0F)),
                                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, new AnimData.KeyTrack(new float[]{0.5F, 1F, 0.5F}, new float[]{0F, 0.5F, 1F}))
                                            }, false
                                    )
                            });
                }
            } else if (tick == 52) {
                for (int i = 0; i < 3; i++) {
                    double d0 = 0.1 + i * 0.2;
                    AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_RING2.get(), forward.x, forward.y, forward.z
                            , x * d0, y * d0, z * d0, false, (float) Math.toRadians(-this.getYRot() + 180), (float) Math.toRadians(-this.getXRot()), 0, 0, 1F,
                            0.8, 0.8, 0.8, 1, 1, 6, true, false, false
                            , new ParticleComponent[]{
                                    new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(6, 14 + i * 2.5F), false),
                                    new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.8F, 0F), false)
                            });
                }
                for (int i = 0; i < 8; i++) {
                    Vec3 randomPos = forward.offsetRandom(this.random, 0.5F);
                    Vec3 finalDirection = new Vec3(x, y, z).add(
                            (random.nextDouble() - 0.5) * 2 * 2,
                            (random.nextDouble() - 0.5) * 2 * 2,
                            (random.nextDouble() - 0.5) * 2 * 2
                    ).normalize();
                    Vec3 velocity = finalDirection.scale(0.6 * (0.5 + random.nextDouble() * 0.7));
                    AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_ORB.get(), randomPos.x, randomPos.y, randomPos.z
                            , velocity.x, velocity.y, velocity.z, true, 0, 0, 0, 0, 0,
                            1, 1, 1, 1, 1, 4, true, true, false
                            , new ParticleComponent[]{
                                    new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 5, 0, 0, 0, 0.05F, 0.67, 0.83, 0.88, 1, true, true,
                                            new ParticleComponent[]{
                                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0.8F, 0F)),
                                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, new AnimData.KeyTrack(new float[]{0.5F, 1F, 0.5F}, new float[]{0F, 0.5F, 1F}))
                                            }, false
                                    )
                            });
                }
            }
        } else {
            if (tick == 50) this.playSound(SoundInit.RELIC_ANNIHILATOR_SHOOT_LASER.get(), 1.5F, this.getVoicePitch());
        }
    }

    private void doFractalEffect() {
        if (this.level().isClientSide && this.saw != null) {
            Vec3 start = this.saw.offsetRandom(this.random, 0.5F);
            LightningBolt bolt = RELICRON_BOLT.color(BOLT_COLORS[this.random.nextInt(2)]).spreadFactor(0.12F).lifespan(3)
                    .fadeFunction(LightningBolt.FadeFunction.fade(0.3F)).build(start, start.offsetRandom(this.random, 2F), this.random);
            ClientProxy.LIGHTNING_RENDER.update(this, bolt);
        }
    }

    private void doTrailEffect(boolean startFlag, boolean holdFlag, boolean left) {
        if (this.level().isClientSide && this.saw != null && this.muzzle != null) {
            if (startFlag) {
                this.preSaw = saw;
                this.preMuzzle = muzzle;
            } else if (holdFlag) {
                if (left) {
                    Vec3 leftPos = this.muzzle;
                    double lLength = this.preMuzzle.subtract(leftPos).length();
                    this.spawnSwipeParticle(this.preMuzzle, leftPos, (int) Math.min(Math.floor(2 * lLength), 16), true);
                    this.preMuzzle = leftPos;
                } else {
                    Vec3 rightPos = this.saw;
                    double rLength = this.preSaw.subtract(rightPos).length();
                    this.spawnSwipeParticle(this.preSaw, rightPos, (int) Math.min(Math.floor(2 * rLength), 16), false);
                    this.preSaw = rightPos;
                }
            }
        }
    }

    private void spawnSwipeParticle(Vec3 start, Vec3 end, int numDusts, boolean left) {
        for (int i = 0; i < numDusts; i++) {
            double x = start.x + i * (end.x - start.x) / numDusts;
            double y = start.y + i * (end.y - start.y) / numDusts;
            double z = start.z + i * (end.z - start.z) / numDusts;
            int count = left ? 1 : 2;
            for (int j = 0; j < count; j++) {
                float randomFactor = 0.15F;
                if (left) {
                    double dx = x - this.getX();
                    double dy = y - this.getY();
                    double dz = z - this.getZ();
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (distance > 0) {
                        double speed = 0.3;
                        double xSpeed = (dx / distance) * speed;
                        double ySpeed = (this.random.nextDouble() - 0.1) * 0.05;
                        double zSpeed = (dz / distance) * speed;
                        randomFactor = 0.5F;
                        xSpeed += (this.random.nextDouble() - 0.5) * randomFactor * 0.1;
                        zSpeed += (this.random.nextDouble() - 0.5) * randomFactor * 0.1;
                        ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.3F, 0.3F, 0.3F, 15F,
                                5 + (int) (Math.random() * 4), ParticleDust.EnumDustBehavior.CONSTANT, 1F);
                        this.level().addParticle(dustData, x, y, z, xSpeed, ySpeed, zSpeed);
                    }
                } else {
                    float xOffset = randomFactor * (2 * this.random.nextFloat() - 1);
                    float yOffset = randomFactor * (2 * this.random.nextFloat() - 1);
                    float zOffset = randomFactor * (2 * this.random.nextFloat() - 1);
                    this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), x + xOffset, y + yOffset, z + zOffset, 0, 0, 0);
                }
            }
        }
    }

    static class RAMeleeAttackGoal extends AnimationAI<EntityRelicAnnihilator> {
        private Vec3 pounceVec = Vec3.ZERO;
        private LivingEntity targetCache;
        private boolean stopFlag;
        private float distanceFactor;

        public RAMeleeAttackGoal(EntityRelicAnnihilator entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == SLASH_ANIMATION || animation == SWING_ANIMATION || animation == STAB_ANIMATION;
        }

        @Override
        public void stop() {
            super.stop();
            pounceVec = Vec3.ZERO;
            targetCache = null;
            distanceFactor = 0F;
            stopFlag = false;
        }

        @Override
        public void tick() {
            LivingEntity target = entity.getTarget();
            int tick = entity.getAnimationTick();
            Animation animation = entity.getAnimation();
            if (animation == SLASH_ANIMATION) {
                attack(target, tick, 90F, 200F, false, true);
            } else if (animation == SWING_ANIMATION) {
                attack(target, tick, 200F, 90F, true, false);
            } else {
                boolean blinded = entity.isBlinded();
                if ((tick < 21 || tick > 45)) {
                    targetCache = target;
                    if (target == null) {
                        double radians = Math.toRadians(entity.getYRot() + 90F);
                        pounceVec = new Vec3(Math.cos(radians), 0.0, Math.sin(radians)).normalize();
                        distanceFactor = 0.5F;
                    } else if (!blinded || tick % 5 == 0) {
                        pounceVec = new Vec3(target.getX() - entity.getX(), 0.0, target.getZ() - entity.getZ()).normalize();
                        distanceFactor = ModMathUtils.getTickFactor(Math.min(entity.distanceTo(target), 40F), 20F, false);
                        entity.lookAt(target, 30F, 30F);
                        entity.getLookControl().setLookAt(target, 30F, 30F);
                    }
                }
                if (tick >= 21 && tick <= 30) {
                    float tickFactor = ModMathUtils.getTickFactor(tick - 21, 18F, true);
                    float speedMultiplier = ModMathUtils.calculateSpeedMultiplier(tickFactor, distanceFactor, 2F, 12F);
                    if (targetCache != null && entity.distanceTo(targetCache) < entity.getBbWidth() * 2F || stopFlag) {
                        stopFlag = true;
                        speedMultiplier *= 0.2F;
                    }
                    double baseValue = entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
                    double moveSpeed = Math.min(entity.getAttributeValue(Attributes.MOVEMENT_SPEED), baseValue + baseValue * 0.4);
                    Vec3 motion = new Vec3(pounceVec.x * moveSpeed * speedMultiplier, entity.getDeltaMovement().y, pounceVec.z * moveSpeed * speedMultiplier);
                    if (tick < 25 || blinded) syncRotationWithMotion(entity, motion);
                    entity.setDeltaMovement(motion);
                }
                if (tick >= 25 && tick <= 45) {
                    if (targetCache != null && !blinded) {
                        entity.lookAt(targetCache, entity.level().getDifficulty() == Difficulty.HARD ? 10F : 5F, 30F);
                    }
                    if (tick % 4 != 0) return;
                    Vec3 pos = entity.getPosOffset(true, entity.getBbWidth() * 1.7F, entity.getBbWidth() * 0.3F, 1.5F);
                    float width = entity.getBbWidth() * 2.75F;
                    float radius = entity.getBbHeight() * 1.2F;
                    entity.rangeAttack(width, radius, width, radius, 45F, 45F, hitEntity -> {
                        if (entity.doHurtTarget(ModDamageSource.bypassCoolDown(entity), hitEntity, 0.35F, 0F, false, true) && canBeControlled(entity, hitEntity)) {
                            pullEntityToPosition(hitEntity, pos, 2F);
                        }
                    });
                }
            }
        }

        private void syncRotationWithMotion(LivingEntity entity, Vec3 motion) {
            if (motion.horizontalDistanceSqr() > 0.0001D) {
                float targetYRot = (float) (Math.atan2(motion.z, motion.x) * (180D / Math.PI)) - 90.0F;
                float currentYRot = entity.getYRot();
                float deltaYRot = Mth.wrapDegrees(targetYRot - currentYRot);
                float motionSpeed = (float) motion.length();
                float rotationSpeed = Math.min(0.8F, 0.2F + motionSpeed * 0.3F);
                float newYRot = currentYRot + deltaYRot * rotationSpeed;
                entity.setYRot(newYRot);
                entity.yBodyRot = newYRot;
                entity.yHeadRot = newYRot;
                entity.yRotO = newYRot;
                entity.yBodyRotO = newYRot;
            }
        }

        public static void pullEntityToPosition(LivingEntity target, Vec3 targetPos, float strength) {
            Vec3 direction = targetPos.subtract(target.position());
            Vec3 normalizedDir = direction.normalize();
            double distanceFactor = Math.min(1.0, direction.length() / 10.0);
            double finalStrength = strength * distanceFactor;
            Vec3 currentMotion = target.getDeltaMovement();
            double motionX = currentMotion.x * 0.3 + normalizedDir.x * finalStrength;
            double motionY = currentMotion.y * 0.1 + normalizedDir.y * finalStrength;
            double motionZ = currentMotion.z * 0.3 + normalizedDir.z * finalStrength;
            double maxSpeed = 2.0;
            Vec3 newMotion = new Vec3(motionX, motionY, motionZ);
            if (newMotion.length() > maxSpeed) {
                newMotion = newMotion.normalize().scale(maxSpeed);
            }
            target.setDeltaMovement(newMotion);
            target.hurtMarked = true;
        }

        private void attack(LivingEntity target, int tick, float leftArc, float rightArc, boolean canDisableShield, boolean charged) {
            if (tick >= 20 && tick <= 40) {
                if (tick == 20) {
                    float width = entity.getBbWidth() * 2F;
                    float radius = entity.getBbHeight() * 1.5F;
                    entity.rangeAttack(width, radius, width, radius, leftArc, rightArc, e -> entity.doHurtTarget(entity.damageSources().mobAttack(entity), e, 1F, 1F, canDisableShield, charged));
                } else entity.setYRot(entity.yRotO);
            } else if (target != null) {
                if (tick <= 10 || !entity.isBlinded()) {
                    entity.getLookControl().setLookAt(target, 30F, 30F);
                    entity.lookAt(target, 30F, 30F);
                }
                if (tick < 20) {
                    double radians = Math.toRadians(entity.yBodyRot + 90F);
                    double moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.9;
                    if (entity.distanceTo(target) < entity.getBbWidth() + target.getBbWidth() / 2) return;
                    entity.setDeltaMovement(Math.cos(radians) * moveSpeed, entity.getDeltaMovement().y, Math.sin(radians) * moveSpeed);
                }
            }
        }
    }

    static class RARangeAttackGoal extends AnimationGroupAI<EntityRelicAnnihilator> {
        private int loopCount;

        public RARangeAttackGoal(EntityRelicAnnihilator entity) {
            super(entity, LASER_ANIMATION, SHOT_ANIMATION1, SHOT_ANIMATION2, SHOT_ANIMATION3,
                    TRICKSHOT_ANIMATION1, TRICKSHOT_ANIMATION2, TRICKSHOT_ANIMATION3);
        }

        @Override
        public void start() {
            super.start();
            loopCount = Mth.clamp(4 - Math.round(entity.getHealth() / entity.getMaxHealth() * 3), 1, 3);
            if (entity.level().getDifficulty() == Difficulty.HARD) loopCount += 1;
        }

        @Override
        public void stop() {
            super.stop();
            loopCount = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = entity.getTarget();
            int tick = entity.getAnimationTick();
            Animation animation = entity.getAnimation();
            handleLoopCount(animation, tick);
            handleAnimation(animation, target, tick);
        }

        private void handleLoopCount(Animation animation, int tick) {
            if (tick != 1) return;
            if ((animation == SHOT_ANIMATION2 || animation == TRICKSHOT_ANIMATION2) && loopCount > 0) {
                loopCount--;
            }
        }

        private void handleAnimation(Animation animation, LivingEntity target, int tick) {
            if (animation == LASER_ANIMATION) {
                lootAtTarget(target, 3F, true);
                if (tick == 9) {
                    double x = entity.scope.getX();
                    double y = entity.scope.getY(0.24);
                    double z = entity.scope.getZ();
                    EntityInfraredRay ray = new EntityInfraredRay(entity.level(), entity, x, y, z, 29);
                    entity.level().addFreshEntity(ray);
                } else if (tick == 49) {
                    EntityGuardianLaser laser = new EntityGuardianLaser(entity.level(), entity, entity.getX(), entity.getY(), entity.getZ(), 20);
                    laser.setCountDown(1);
                    EntityGuardianLaser.UserType type = EntityGuardianLaser.UserType.RELIC_ANNIHILATOR;
                    laser.updateWithEntity(entity, type.wOffset, type.hOffset);
                    entity.level().addFreshEntity(laser);
                }
            } else if (animation == SHOT_ANIMATION1 || animation == SHOT_ANIMATION2 || animation == SHOT_ANIMATION3) {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                lootAtTarget(target, 1.5F, true);
                if (animation == SHOT_ANIMATION1) nextAnimation(animation, SHOT_ANIMATION2);
                else if (animation == SHOT_ANIMATION2) nextAnimation(animation, SHOT_ANIMATION3, tick >= animation.getDuration() - 1 && loopCount <= 0);
            } else {
                lootAtTarget(target, 2F, animation != TRICKSHOT_ANIMATION3);
                float retreatRange = animation == TRICKSHOT_ANIMATION3 ? 0 : 3 + entity.getBbWidth();
                entity.backOff(target, getRetreatSpeed(0.7F, retreatRange, entity.targetDistance));
                if (animation == TRICKSHOT_ANIMATION1) nextAnimation(animation, TRICKSHOT_ANIMATION2);
                else if (animation == TRICKSHOT_ANIMATION2) {
                    nextAnimation(animation, TRICKSHOT_ANIMATION3, tick >= animation.getDuration() - 1 && loopCount <= 0);
                }
            }
        }

        private void lootAtTarget(LivingEntity target, float scale, boolean swivel) {
            if (target != null) {
                float rotationAmount = 30F * scale;
                entity.getLookControl().setLookAt(target, rotationAmount, rotationAmount);
                if (swivel) entity.lookAt(target, rotationAmount, rotationAmount);
            }
        }
    }

    static class RACycloneAttackGoal extends AnimationAI<EntityRelicAnnihilator> {
        public RACycloneAttackGoal(EntityRelicAnnihilator entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == CYCLONE_ANIMATION;
        }

        @Override
        public void tick() {
            int tick = entity.getAnimationTick();
            if (tick < (entity.isBlinded() ? 5 : 10)) {
                lookAtTarget();
            } else if (tick >= 15 && tick <= 40) {
                double radians = Math.toRadians(this.entity.getYRot() + 90F);
                Vec3 pounceVec = new Vec3(Math.cos(radians), 0.0, Math.sin(radians)).normalize();
                if (tick == 20 || tick == 24 || tick == 29 || tick == 32 || tick == 36) {
                    entity.rangeAttack(3, 4, 3, 4, hitEntity -> {
                        if (entity.doHurtTarget(ModDamageSource.bypassCoolDown(entity), hitEntity, 0.5F, 1F, false, true) && canBeControlled(entity, hitEntity)) {
                            ModEntityUtils.forceKnockBack(entity, hitEntity, hitEntity.isBlocking() ? 0.4F : 0.7F, -pounceVec.x, -pounceVec.z, false);
                            if (!hitEntity.onGround()) hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().multiply(1F, 0.5F, 1F));
                        }
                    });
                }
                float tickFactor = ModMathUtils.getTickFactor(tick - 15, 60F, true);
                float speedMultiplier = ModMathUtils.calculateSpeedMultiplier(tickFactor, 1F, 2.5F, 3F);
                double baseValue = entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) * 1.5F;
                double moveSpeed = Math.min(entity.getAttributeValue(Attributes.MOVEMENT_SPEED), baseValue + baseValue * 0.4);
                entity.setDeltaMovement(pounceVec.x * moveSpeed * speedMultiplier, entity.getDeltaMovement().y, pounceVec.z * moveSpeed * speedMultiplier);
            } else if (tick > 50) {
                this.lookAtTarget();
            }
        }

        private void lookAtTarget() {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            }
        }
    }

    static class RALeapAttackGoal extends AnimationAI<EntityRelicAnnihilator> {
        public RALeapAttackGoal(EntityRelicAnnihilator entity) {
            super(entity);
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == GROUND_SLAM_ANIMATION1 || animation == GROUND_SLAM_ANIMATION2;
        }

        @Override
        public void tick() {
            LivingEntity target = entity.getTarget();
            int tick = entity.getAnimationTick();
            Animation animation = entity.getAnimation();
            if (animation == GROUND_SLAM_ANIMATION1) {
                if (tick < 11) {
                    if (target != null) {
                        entity.getLookControl().setLookAt(target, 30F, 30F);
                        entity.lookAt(target, 30F, 30F);
                    }
                } else if (tick == 11) {
                    if (target != null) {
                        Vec3 vec3 = GuardianLeapGoal.findTargetPoint(entity, target);
                        double x = vec3.x * 0.155D;
                        double y = 1 + Mth.clamp(vec3.y * 0.02D, 0D, 12D);
                        double z = vec3.z * 0.155D;
                        entity.setDeltaMovement(x, y, z);
                    } else {
                        float radians = (float) Math.toRadians(entity.yBodyRot + 90);
                        entity.setDeltaMovement(3.0 * Math.cos(radians), 1, 3.0 * Math.sin(radians));
                    }
                } else if (tick == 14) entity.playAnimation(GROUND_SLAM_ANIMATION2);
            } else if (animation == GROUND_SLAM_ANIMATION2) {
                if (tick > 5 && entity.onGround()) entity.playAnimation(GROUND_SLAM_ANIMATION3);
            }
        }
    }

    static class KeepDistanceGoal extends AnimationMeleeAI<EntityRelicAnnihilator> {
        private final RandomSource random;
        private boolean isInRetreatPause;
        private int retreatCooldown;
        private int retreatDuration;
        private int delayCounter;

        public KeepDistanceGoal(EntityRelicAnnihilator attacker, Animation... animations) {
            super(attacker, 1, 0, EntityAbsRelicron::isActive, animations);
            this.random = attacker.getRandom();
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.attacker.getTarget();
            return this.customFlag.test(this.attacker) && target != null && target.isAlive() && this.attacker.canAttack(target);
        }

        @Override
        public void stop() {
            super.stop();
            retreatCooldown = retreatDuration = delayCounter = 0;
            isInRetreatPause = false;
        }

        @Override
        public void tick() {
            LivingEntity target = attacker.getTarget();
            if (target == null) return;
            float offset = target.getBbWidth() + attacker.getBbWidth() / 2F;
            double followRange = 4.2 + offset;
            double idealRange = 3.7 + offset;
            double retreatRange = 2.7 + offset;
            attacker.lookAt(target, 30F, 30F);
            attacker.getLookControl().setLookAt(target, 30F, 30F);
            double currentDist = attacker.distanceTo(target);
            //后退停滞
            if (isInRetreatPause) {
                if (currentDist < retreatRange) delayCounter = 0;
                if (--delayCounter <= 0) isInRetreatPause = false;
                attacker.getNavigation().stop();
                return;
            }
            //后退移动
            if (retreatDuration > 0) {
                if (currentDist < retreatRange) delayCounter--;
                retreatDuration--;
                attacker.backOff(target, 0.6);
                if (retreatDuration <= 0) {
                    isInRetreatPause = true;
                    attacker.getNavigation().stop();
                }
                return;
            }
            if (retreatCooldown > 0) retreatCooldown--;
            if (--delayCounter > 0) return;
            if (currentDist > followRange) {
                delayCounter = 5;
                attacker.getNavigation().moveTo(target, 1);
            } else if (currentDist < retreatRange) {
                if (retreatCooldown < 20) retreatCooldown += 2;
                attacker.backOff(target, getRetreatSpeed(0.8F, retreatRange, currentDist));
            } else if (currentDist <= idealRange) {
                delayCounter = 2;
                attacker.getNavigation().stop();
                if (retreatCooldown <= 0 && random.nextFloat() < 0.4F) {
                    retreatDuration = getRetreatDuration(currentDist, retreatRange, followRange);
                    retreatCooldown = 60 + random.nextInt(40);
                    delayCounter = retreatDuration;
                }
            }
        }

        //后退时间计算
        private int getRetreatDuration(double currentDist, double minRange, double maxRange) {
            double ratio = 1 - Math.sqrt(Math.max(0, Math.min(1, (currentDist - minRange) / (maxRange - minRange))));
            int time = 10 + (int) (20 * ratio);
            return Math.max(10, Math.min(30, time + random.nextInt(6) - 3));
        }
    }
}
