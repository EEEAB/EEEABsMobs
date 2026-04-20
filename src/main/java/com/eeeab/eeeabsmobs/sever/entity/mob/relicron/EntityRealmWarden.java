package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.OverlapAnimationState;
import com.eeeab.animate.server.animation.keyframe.Keyframe;
import com.eeeab.animate.server.animation.keyframe.KeyframeManager;
import com.eeeab.animate.server.animation.release.AnimationReleaseManager;
import com.eeeab.animate.server.animation.release.AnimationRule;
import com.eeeab.animate.server.animation.release.ConditionFactory;
import com.eeeab.animate.server.animation.release.cooldown.HealthScaledCooldown;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedBlockParticle;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.ParticleRotation;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent.PropertyOverLength;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedBlockParticleData;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedParticleData;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.client.render.LightningBolt;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.ModBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntitySurge;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityTelegraph;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityAnnihilatorMissile;
import com.eeeab.eeeabsmobs.sever.entity.mob.CrackinessEntity;
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
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntFunction;

public class EntityRealmWarden extends EntityAbsRelicron implements IBoss, CrackinessEntity<EntityRealmWarden> {
    public static final Animation GROUND_POUND_ANIMATION = Animation.create(40).doesOverlap();
    public static final Animation HEAVY_SWING_ANIMATION = Animation.create(70);
    public static final Animation DERIVED_HEAVY_SWING_ANIMATION = Animation.create(95);
    public static final Animation STOMP_ANIMATION = Animation.create(35).doesOverlap();
    public static final Animation STOMP_ANIMATION2 = Animation.create(35).doesOverlap().setSpeed(1.4F);
    public static final Animation ELBOW_STRIKE_ANIMATION = Animation.create(40).doesOverlap();
    public static final Animation SWEEP_ANIMATION = Animation.create(80).doesOverlap();
    public static final Animation TURNAROUND_SWEEP_ANIMATION = Animation.create(45).doesOverlap();
    public static final Animation BACKSTEP_ANIMATION = Animation.create(100);
    public static final Animation BACKSTEP_LANDING_ANIMATION = Animation.create(35).doesOverlap();
    public static final Animation LEAP_ANIMATION = Animation.create(100);//35
    public static final Animation LEAP_LANDING_ANIMATION = Animation.create(80);
    public static final Animation JUMP_SMASH_START_ANIMATION = Animation.create(35);//25
    public static final Animation JUMP_SMASH_ANIMATION = Animation.create(30);
    public static final Animation DERIVED_JUMP_SMASH_ANIMATION = Animation.create(50);
    public static final Animation DOUBLE_FIST_SLAM_ANIMATION = Animation.create(40).doesOverlap();
    public static final Animation HEAVY_SMASH_ANIMATION = Animation.create(50);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(90);
    public static final Animation DIE_ANIMATION = Animation.create(80).setSpeed(1.2F);
    private static final Animation[] ANIMATIONS = new Animation[]{
            GROUND_POUND_ANIMATION,
            HEAVY_SWING_ANIMATION,
            DERIVED_HEAVY_SWING_ANIMATION,
            STOMP_ANIMATION,
            STOMP_ANIMATION2,
            ELBOW_STRIKE_ANIMATION,
            SWEEP_ANIMATION,
            TURNAROUND_SWEEP_ANIMATION,
            BACKSTEP_ANIMATION,
            BACKSTEP_LANDING_ANIMATION,
            LEAP_ANIMATION,
            LEAP_LANDING_ANIMATION,
            JUMP_SMASH_START_ANIMATION,
            JUMP_SMASH_ANIMATION,
            DERIVED_JUMP_SMASH_ANIMATION,
            DOUBLE_FIST_SLAM_ANIMATION,
            HEAVY_SMASH_ANIMATION,
            ACTIVATE_ANIMATION,
            DIE_ANIMATION,
    };
    private static final KeyframeManager<EntityRealmWarden> KEYFRAME_MANAGER;
    private static final AnimationReleaseManager<EntityRealmWarden> ANIMATION_RELEASE_MANAGER;
    private static final LightningBolt.LightningBoltBuilder REALMWARDEN_BOLT = new LightningBolt.LightningBoltBuilder().count(1);
    private static final EntityDataAccessor<Integer> DATA_LEAP_HOLD = SynchedEntityData.defineId(EntityRealmWarden.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SEC_PHASE = SynchedEntityData.defineId(EntityRealmWarden.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_REST_POS = SynchedEntityData.defineId(EntityRealmWarden.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final UUID SEC_PHASE_ATTACK_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-1234-567890ABCDEF");
    private static final Vector4f[] COLORS = new Vector4f[]{new Vector4f(1F, 0.65F, 0.21F, 1F), new Vector4f(0.37F, 0.8F, 0.89F, 1F)};
    private static final float[][] BLOCK_OFFSETS = {{-0.5F, -0.5F}, {-0.5F, 0.5F}, {0.5F, 0.5F}, {0.5F, -0.5F}};
    private static final float[][] SHORT_BLOCK_OFFSETS = {{-0.5F, 0.5F}};
    private static final int BIT_NONE = 1;
    private static final int BIT_LOW = 2;
    private static final int BIT_MEDIUM = 4;
    private static final int BIT_HIGH = 8;
    private static final int TIME_UNTIL_OUT_OF_BATTLE_HEAL = 50;
    private final AnimationState groundPoundAnimationState = new OverlapAnimationState(GROUND_POUND_ANIMATION);
    private final AnimationState stompAnimationState = new OverlapAnimationState(STOMP_ANIMATION);
    private final AnimationState stompAnimationState2 = new OverlapAnimationState(STOMP_ANIMATION2);
    private final AnimationState elbowStrikeAnimationState = new OverlapAnimationState(ELBOW_STRIKE_ANIMATION);
    private final AnimationState sweepAnimationState = new OverlapAnimationState(SWEEP_ANIMATION);
    private final AnimationState turnaroundSweepAnimationState = new OverlapAnimationState(TURNAROUND_SWEEP_ANIMATION);
    private final AnimationState backstepLandingAnimationState = new OverlapAnimationState(BACKSTEP_LANDING_ANIMATION);
    private final AnimationState doubleFistSlamAnimationState = new OverlapAnimationState(DOUBLE_FIST_SLAM_ANIMATION);
    public final ControlledAnimation alphaControlled = new ControlledAnimation(5);
    @OnlyIn(Dist.CLIENT)
    public Vec3[] modelParts;//0:left 1:right
    public ControlledAnimation glowControlled = new ControlledAnimation(20);
    private Vec3 lPrePos = Vec3.ZERO;
    private Vec3 rPrePos = Vec3.ZERO;
    @Nullable
    private LivingEntity leapDownTarget;
    @Nullable
    private Vec3 cachedLandingPos;
    private boolean shouldPlayLeapAnimation;
    private double cachedLandingPosY;
    private int triggeredPhashBits;
    private int hoverTimer;

    public EntityRealmWarden(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.active = false;
        this.dropAfterDeathAnim = false;
        this.clearRedundantAnimationsOnDeath = true;
        if (this.level().isClientSide) {
            modelParts = new Vec3[]{new Vec3(0, 0, 0), new Vec3(0, 0, 0)};
        }
    }

    static {
        KEYFRAME_MANAGER = setupAnimations();
        ANIMATION_RELEASE_MANAGER = setupAnimationRules();
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.EPIC_BOSS;
    }

    @Override//减少实体在水下的空气供应
    protected int decreaseAirSupply(int air) {
        return air;
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
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return super.isInvulnerableTo(damageSource) || animation == LEAP_ANIMATION || animation == LEAP_LANDING_ANIMATION || animation == BACKSTEP_ANIMATION || animation == ACTIVATE_ANIMATION;
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
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 0.875F;
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.realmwarden.combatConfig;
    }

    @Override
    protected ModConfigHandler.BossConfig getBossConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.realmwarden.bossConfig;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            //非正常结束情况
            if (animation == LEAP_ANIMATION && this.getLeapStage() == LeapStage.DOWN) {
                this.setLeapStage(LeapStage.LEAP);
                this.playAnimation(LEAP_LANDING_ANIMATION);
            }
        }
    }

    @Override
    protected float activeRange() {
        return 12F;
    }

    @Override
    protected void updateActivationState() {
        if (this.isAlwaysActive()) {
            this.setActive(true);
            this.active = true;
        } else {
            boolean active = this.isActive();
            LivingEntity target = this.getTarget();
            if (!active && this.isNoAnimation() && target != null && this.targetDistance <= this.activeRange()) {
                this.playAnimation(ACTIVATE_ANIMATION);
            }
            if (active && this.isAlive() && target == null && this.timeUntilDeactivate >= TIME_UNTIL_OUT_OF_BATTLE_HEAL) {
                if (ModConfigHandler.COMMON.mobs.relicrons.outOfBattleHeal.get()) this.heal(this.getHealth() * 0.05F);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LEAP_HOLD, LeapStage.LEAP.id);
        this.entityData.define(DATA_SEC_PHASE, false);
        this.entityData.define(DATA_REST_POS, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSecondPhase(compound.getBoolean("isSecondPhase"));
        if (compound.contains("restPos")) {
            this.setRestPos(NbtUtils.readBlockPos(compound.getCompound("restPos")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isSecondPhase", this.isSecondPhase());
        this.getRestPos().ifPresent(pos -> compound.put("restPos", NbtUtils.writeBlockPos(pos)));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(5, new RelicronRandomStrollGoal(this, 1));
    }

    @Override
    protected void registerCustomGoals() {
        super.registerCustomGoals();
        this.goalSelector.addGoal(0, new RWSweepGoal(this));
        this.goalSelector.addGoal(1, new RWHeavySwingGoal(this));
        this.goalSelector.addGoal(1, new RWStompGoal(this));
        this.goalSelector.addGoal(1, new RWElbowStrikeGoal(this));
        this.goalSelector.addGoal(1, new RWBackstepGoal(this));
        this.goalSelector.addGoal(1, new RWJumpSmashGoal(this));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, LEAP_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, LEAP_LANDING_ANIMATION) {
            @Override
            public void tick() {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            }
        });
        this.goalSelector.addGoal(1, new CacheableTargetAnimationAI(this) {
            @Override
            protected boolean test(Animation animation) {
                return animation == GROUND_POUND_ANIMATION;
            }

            @Override
            public void tick() {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                int tick = entity.getAnimationTick();
                if (tick > 5 && tick < 13) lookAtTarget(15);
                else entity.setYRot(entity.yRotO);
            }
        });
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, ACTIVATE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.0, 0));
    }

    @Override
    public void tick() {
        super.tick();
        this.alphaControlled.updatePrevTimer();
        this.glowControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);

        this.pushEntitiesAway(1F, getBbHeight(), 1F, 1F);

        if (!this.isActive()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }

        if (!this.level().isClientSide) {
            LeapStage stage = this.getLeapStage();
            if (stage == LeapStage.HOLD) {
                if (this.leapDownTarget == null || this.hoverTimer >= 10) {
                    this.hoverTimer = 0;
                    this.setLeapStage(LeapStage.DOWN);
                    this.setDeltaMovement(0, -1, 0);
                    return;
                }
                if (hoverTimer < 5) {
                    if (!this.isAlwaysActive() && this.getRestPos().isPresent()) {
                        this.cachedLandingPos = Vec3.atBottomCenterOf(this.getRestPos().get());
                        this.cachedLandingPosY = cachedLandingPos.y;
                    } else if (this.leapDownTarget.level() == this.level()) {
                        if (this.leapDownTarget.onGround()) {
                            this.cachedLandingPos = this.leapDownTarget.position();
                        } else if (this.cachedLandingPos == null) {
                            Vec3 pos = this.leapDownTarget.position();
                            this.cachedLandingPos = new Vec3(pos.x, Math.min(this.cachedLandingPosY, pos.y), pos.z);
                        }
                    }
                } else if (hoverTimer == 5) {
                    Vec3 pos;
                    if (this.cachedLandingPos != null) {
                        pos = this.cachedLandingPos;
                        if (!this.tryTeleportTo(pos.x, pos.y, pos.z)) pos = getDefaultLandingPos();
                    } else pos = getDefaultLandingPos();
                    EntityTelegraph.spawn(this.level(), pos.add(0, 0.1, 0), 10, 0xFFFF2020, 8F);
                    this.cachedLandingPos = null;
                    this.cachedLandingPosY = 0;
                }
                this.hoverTimer++;
                this.setDeltaMovement(Vec3.ZERO);
            } else if (stage == LeapStage.DOWN && this.onGround()) {
                this.setLeapStage(LeapStage.LEAP);
                this.playAnimation(LEAP_LANDING_ANIMATION);
            }
        } else {
            if (this.isAlive() && this.isActive()) {
                float moveX = (float) (this.getX() - this.xo);
                float moveZ = (float) (this.getZ() - this.zo);
                float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
                if (speed > 0.05) {
                    if (this.random.nextInt(5) == 0) this.doWalkEffect(2);
                    if (this.isNoAnimation() && !this.isSilent() && this.tickCount % 15 == 1) {
                        this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.REALM_WARDEN_STEP.get(), this.getSoundSource(), 1.5F, 1.3F, false);
                    }
                }
                if (this.isSecondPhase() && this.tickCount % 2 == 0 && this.random.nextInt(3) == 0) {
                    Vec3 leftPos;
                    Vec3 rightPos;
                    if (this.isNoAnimation()) {
                        float width = getBbWidth() * 0.8F;
                        float height = getBbHeight() * 0.25F;
                        leftPos = ModEntityUtils.offsetRandomVec(this.getPosOffset(false, 0F, width, height), this.random, 1F, 2F, 0);
                        rightPos = ModEntityUtils.offsetRandomVec(this.getPosOffset(true, 0F, width, height), this.random, 1F, 2F, 0);
                    } else {
                        if (this.modelParts == null || this.modelParts.length < 2) return;
                        leftPos = ModEntityUtils.offsetRandomVec(this.modelParts[0], this.random, 1F, 2F, 0.25F);
                        rightPos = ModEntityUtils.offsetRandomVec(this.modelParts[1], this.random, 1F, 2F, 0.25F);
                    }
                    this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), leftPos.x, leftPos.y, leftPos.z, 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), rightPos.x, rightPos.y, rightPos.z, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.alphaControlled.incrementOrDecreaseTimer(this.getLeapStage() == LeapStage.HOLD);
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = this.getTarget();
        if (this.isActive() && !shouldPlayLeapAnimation && target != null) {
            CrackinessType current = getCrackiness(this);
            int targetBit = 0;
            if (current == CrackinessType.LOW) {
                targetBit = BIT_LOW;
            } else if (current == CrackinessType.MEDIUM) {
                targetBit = BIT_MEDIUM;
            } else if (current == CrackinessType.HIGH) {
                targetBit = BIT_HIGH;
            } else if ((isAlwaysActive() && current == CrackinessType.NONE) || (!isAlwaysActive() && this.getHealthPercentage() <= 0.95)) {
                targetBit = BIT_NONE;
            }
            boolean shouldTrigger = false;
            if (targetBit != 0 && (triggeredPhashBits & targetBit) == 0) {
                if (this.level().getDifficulty() == Difficulty.HARD) {
                    shouldTrigger = true;
                } else {
                    if (targetBit == BIT_NONE || targetBit == BIT_MEDIUM) {
                        shouldTrigger = true;
                    }
                }
            }
            if (shouldTrigger) {
                triggeredPhashBits |= targetBit;
                shouldPlayLeapAnimation = true;
                if (targetBit == BIT_MEDIUM) {
                    if (!isSecondPhase() && !(target instanceof EntityAbsRelicron)/*自家人不发警报*/) {
                        this.playSound(SoundInit.REALM_WARDEN_VOICE2.get(), 3F, 1F);
                    }
                    setSecondPhase(true);
                }
            }
        }

        if (this.shouldPlayLeapAnimation && this.isNoAnimation() && target != null) {
            if (this.distanceToSqr(target) < 1024D /* 32格 */ && (onGround() || isInWater() || isInLava())) {
                this.leapDownTarget = target;
                this.playAnimation(LEAP_ANIMATION);
                this.shouldPlayLeapAnimation = false;
                return;
            }
        }

        ANIMATION_RELEASE_MANAGER.tick(this, this.getCooldownManager());
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (this.isSecondPhase()) {
                if (ModEntityUtils.isProjectileSource(source) || source.isIndirect()) {
                    damage = Math.min(damage * 0.1F, getHealth() * 0.025F);
                }
            } else damage *= 0.8F;
            if (shouldPlayLeapAnimation) damage *= 0.2F;
        }
        return super.hurt(source, damage);
    }

    @Override
    public boolean doHurtTarget(DamageSource damageSource, Entity entity, float damageMultiplier, float knockBackMultiplier, boolean canDisableShield) {
        boolean secondPhase = this.isSecondPhase();
        if (secondPhase && entity instanceof LivingEntity target) {
            MobEffectInstance instance = target.getEffect(EffectInit.ELECTRIFIED_EFFECT.get());
            if (instance != null) damageMultiplier += Mth.clamp((instance.getAmplifier() + 1) * 0.05F, 0F, 0.5F);
        }
        if (super.doHurtTarget(damageSource, entity, damageMultiplier, knockBackMultiplier, canDisableShield)) {
            if (secondPhase && entity instanceof LivingEntity target) {
                boolean hardMode = this.level().getDifficulty() == Difficulty.HARD;
                ModEntityUtils.addEffectStackingAmplifier(null, target, EffectInit.ELECTRIFIED_EFFECT.get(), 150,
                        hardMode ? 10 : 5, false, true, hardMode || this.random.nextBoolean(), false);
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        this.setRestPos(this.blockPosition());
        return groupData;
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.25D), this.getY() + (this.random.nextFloat() * this.getBbHeight() * 0.7), this.getRandomZ(1.25D), d0, d1, d2);
        }
    }

    @Override
    public AnimationState getOverlapAnimationState(Animation animation) {
        if (GROUND_POUND_ANIMATION == animation) {
            return this.groundPoundAnimationState;
        } else if (STOMP_ANIMATION == animation) {
            return this.stompAnimationState;
        } else if (STOMP_ANIMATION2 == animation) {
            return this.stompAnimationState2;
        } else if (ELBOW_STRIKE_ANIMATION == animation) {
            return this.elbowStrikeAnimationState;
        } else if (SWEEP_ANIMATION == animation) {
            return this.sweepAnimationState;
        } else if (TURNAROUND_SWEEP_ANIMATION == animation) {
            return this.turnaroundSweepAnimationState;
        } else if (BACKSTEP_LANDING_ANIMATION == animation) {
            return this.backstepLandingAnimationState;
        } else if (DOUBLE_FIST_SLAM_ANIMATION == animation) {
            return this.doubleFistSlamAnimationState;
        }
        return null;
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
        return ACTIVATE_ANIMATION;
    }

    @Override
    protected SoundEvent getActiveSound() {
        return SoundInit.REALM_WARDEN_HUM.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        this.playSound(SoundInit.REALM_WARDEN_HURT.get(), this.getSoundVolume(), this.getVoicePitch() - 0.1F);
        return null;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public void playAmbientSound() {
        if (this.getTarget() == null) {
            super.playAmbientSound();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.REALM_WARDEN_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.REALM_WARDEN_DEATH.get();
    }

    @Override
    public KeyframeManager<EntityRealmWarden> getKeyframeManager() {
        return KEYFRAME_MANAGER;
    }

    @Override
    public boolean isGlow() {
        return super.isGlow() || (this.getAnimation() == DIE_ANIMATION && this.getAnimationTick() < 35);
    }

    @Override
    public SoundEvent getBossMusic() {
        return SoundInit.GUARDIANS.get();
    }

    @Override
    protected boolean canPlayMusic() {
        return this.isActive() && super.canPlayMusic();
    }

    public LeapStage getLeapStage() {
        return LeapStage.byId(this.entityData.get(DATA_LEAP_HOLD));
    }

    public void setLeapStage(LeapStage stage) {
        this.entityData.set(DATA_LEAP_HOLD, stage.id);
    }

    public boolean isSecondPhase() {
        return this.entityData.get(DATA_SEC_PHASE);
    }

    private void setSecondPhase(boolean secondPhase) {
        if (this.entityData.get(DATA_SEC_PHASE) == secondPhase) return;
        this.entityData.set(DATA_SEC_PHASE, secondPhase);
        if (secondPhase) {
            if (this.level().isClientSide) return;
            AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attack == null) return;
            if (attack.getModifier(SEC_PHASE_ATTACK_UUID) != null) attack.removeModifier(SEC_PHASE_ATTACK_UUID);
            attack.addPermanentModifier(new AttributeModifier(SEC_PHASE_ATTACK_UUID, "Second Phase Attack", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    public Optional<BlockPos> getRestPos() {
        return getEntityData().get(DATA_REST_POS);
    }

    public void setRestPos(BlockPos pos) {
        getEntityData().set(DATA_REST_POS, Optional.of(pos));
    }

    private static KeyframeManager<EntityRealmWarden> setupAnimations() {
        KeyframeManager<EntityRealmWarden> manager = new KeyframeManager<>();
        KeyframeManager.KeyframeManegerBuilder<EntityRealmWarden> builder = manager.builder();
        Keyframe<EntityRealmWarden> doPlayAttackSound = (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_ATTACK.get(), 1.5F, 1F);
        };
        Keyframe<EntityRealmWarden> doPlayShortHumSound = (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_SHORTHUM.get(), 1.2F, 1F + (entity.random.nextFloat() - 0.5F) * 0.1F);
        };
        builder.forAnimation(ACTIVATE_ANIMATION).everyTick((entity, animation, tick) -> {
            if (!entity.isActive()) entity.setActive(true);
            boolean inServer = !entity.level().isClientSide;
            if (inServer && tick == 1 && entity.getActiveSound() != null) entity.playSound(entity.getActiveSound(), 2F, 0.8F);
            if (inServer && tick == 20 && (entity.getTarget() != null && !(entity.getTarget() instanceof EntityAbsRelicron))) {
                entity.playSound(SoundInit.REALM_WARDEN_VOICE1.get(), 3F, 1F);
            }
            if (tick == 45 || tick == 54) {
                if (inServer) entity.playSound(SoundInit.REALM_WARDEN_ATTACK.get(), 1F, 1F);
                else {
                    Vec3 pos = entity.getPosOffset(tick == 45, 0F, entity.getBbWidth(), 0);
                    ModParticleUtils.blockParticlesAround(entity.level(), pos.x, pos.y, pos.z, 12, 0.25, 0.75, 0.1,
                            0.2, 0.1, 0.3, -0.2, 0.1, (pos2, state) -> getBlockParticleData(entity.random, state, pos, 2));
                    ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 28F, 25, ParticleDust.EnumDustBehavior.GROW, 0.74F);
                    ModParticleUtils.annularParticleOutburst(entity.level(), 16, dustData, pos.x, pos.y, pos.z, 1.55F, 0.5);
                }
            }
            if (tick == 70) {
                if (inServer) {
                    entity.playSound(SoundInit.REALM_WARDEN_ATTACK.get(), 1F, 0.9F);
                    entity.playSound(SoundInit.REALM_WARDEN_ATTACK.get(), 1F, 1.1F);
                } else {
                    Vec3 pos = entity.position();
                    ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 35F, 30, ParticleDust.EnumDustBehavior.GROW, 0.74F);
                    ModParticleUtils.annularParticleOutburst(entity.level(), 16, dustData, pos.x, pos.y, pos.z, 1.6F, 0.5);
                    Vec3 rPos = entity.getPosOffset(true, 0F, entity.getBbWidth(), 0);
                    ModParticleUtils.blockParticlesAround(entity.level(), rPos.x, rPos.y, rPos.z, 14, 0.25, 1.25, 0.15,
                            0.25, 0.15, 0.35, -0.2, 0.1, (pos2, state) -> getBlockParticleData(entity.random, state, rPos, 2));
                    Vec3 lPos = entity.getPosOffset(false, 0F, entity.getBbWidth(), 0);
                    ModParticleUtils.blockParticlesAround(entity.level(), lPos.x, lPos.y, lPos.z, 14, 0.25, 1.25, 0.15,
                            0.25, 0.15, 0.35, -0.2, 0.1, (pos2, state) -> getBlockParticleData(entity.random, state, lPos, 2));
                }
            }
        });
        builder.forAnimation(DIE_ANIMATION).everyNTick(1, 55, 2, (entity, animation, tick) -> {
            if (!entity.level().isClientSide) return;
            double y = entity.getY() + entity.random.nextFloat() * (tick > 15 ? entity.getBbHeight() * 0.7 : entity.getBbHeight());
            entity.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), entity.getRandomX(0.8), y, entity.getRandomZ(0.8), 0, 0, 0);
        });
        builder.forAnimation(GROUND_POUND_ANIMATION)
                .atTick(1, doPlayShortHumSound).atTick(15, doPlayAttackSound)
                .atTick(17, (entity, animation, tick) -> {
                    Vec3 pos = entity.getPosOffset(true, 1.6F, 1F, 0F);
                    entity.doGroundPoundEffect(pos, 1F, 1F, new double[]{70, 55, 35});
                    for (LivingEntity hitEntity : ShockWaveUtils.doRingShockWave(entity, pos, 2.1, 0, false, 20)) {
                        entity.doHurtTarget(hitEntity, 0.9F, 0F);
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, false);
                    }
                });
        Keyframe<EntityRealmWarden> heavySwingKF1 = (entity, animation, tick) -> entity.rangeAttack(4, entity.getBbHeight(), 4, 4, 180F, 90F, hitEntity -> {
            entity.doHurtTarget(hitEntity, 0.6F, 0);
            ModEntityUtils.forceKnockBack(entity, hitEntity, 0.5F, false);
        });
        Keyframe<EntityRealmWarden> heavySwingKF2 = (entity, animation, tick) -> entity.rangeAttack(4.25, entity.getBbHeight(), 4.25, 4.5, 90F, 180F, hitEntity -> {
            entity.doHurtTarget(ModDamageSource.bypassCoolDown(entity), hitEntity, 0.7F, 0, false);
            ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, false);
        });
        Keyframe<EntityRealmWarden> doPlayWhooshSound = (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_WHOOSH.get(), 1.4F, 0.7F);
        };
        builder.forAnimation(HEAVY_SWING_ANIMATION)
                .atTick(17, doPlayWhooshSound)
                .atTick(19, heavySwingKF1)
                .atTick(33, heavySwingKF2)
                .atTick(44, (entity, animation, tick) -> entity.rangeAttack(4.5, entity.getBbHeight(), 4.5, 4.5, 140F, 180F, hitEntity -> {
                    if (entity.doHurtTarget(hitEntity, 0.8F, 0, true)) {
                        entity.stun(null, hitEntity, 30, false);
                    }
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 1.25F, false);
                }))
                .inRange(19, 22, (entity, animation, tick) -> {
                    entity.doTrailEffect(animation, tick == 19, tick > 19, true, null);
                    if (entity.level().isClientSide) return;
                    if (tick == 20) EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.15F, 0, 5);
                }).inRange(31, 45, (entity, animation, tick) -> {
                    entity.doTrailEffect(animation, tick == 31, tick > 31, false, new Vec3(0, -1.2, 0));
                    if (entity.level().isClientSide) return;
                    if (tick == 34 || tick == 44) EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.2F, 0, 5);
                    if (tick == 32 || tick == 39) entity.playSound(SoundInit.REALM_WARDEN_WHOOSH.get(), 1.4F, tick == 39 ? 0.7F : 0.9F);
                });
        builder.forAnimation(DERIVED_HEAVY_SWING_ANIMATION)
                .atTick(1, (entity, animation, tick) -> {
                    if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_HUM.get(), 1.4F, 1.1F + (entity.random.nextFloat() - 0.5F) * 0.1F);
                }).atTick(17, doPlayWhooshSound)
                .atTick(19, heavySwingKF1)
                .atTick(35, heavySwingKF2)
                .atTick(66, (entity, animation, tick) -> {
                    if (!entity.level().isClientSide) if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_BLAST.get(), 1.5F, 1.5F);
                })
                .atTick(69, (entity, animation, tick) -> {
                    Vec3 pos = entity.getPosOffset(false, 3F, 0F, 0F);
                    entity.doGroundPoundEffect(pos, 1.4F, 1.4F, null);
                    for (LivingEntity hitEntity : ShockWaveUtils.doRingShockWave(entity, pos, 2.4, -0.03F, false, 20)) {
                        entity.doHurtTarget(hitEntity, 1.1F, 0F, true);
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 1.25F, false);
                    }
                }).inRange(18, 22, (entity, animation, tick) -> {
                    entity.doTrailEffect(animation, tick == 20, tick > 20, true, null);
                    if (tick == 20) EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.15F, 0, 5);
                })
                .inRange(31, 41, (entity, animation, tick) -> {
                    entity.doTrailEffect(animation, tick == 31, tick > 31, false, new Vec3(0, -1.5, 0));
                    if (entity.level().isClientSide) return;
                    if (tick == 37) entity.playSound(SoundInit.REALM_WARDEN_WHOOSH.get(), 1.4F, 0.9F);
                    if (tick == 39) EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.2F, 0, 5);
                });
        Keyframe<EntityRealmWarden> stompKeyFrame = (entity, animation, tick) -> {
            Vec3 pos = entity.getPosOffset(true, 1.3F, 0.5F, 0F);
            entity.doGroundPoundEffect(pos, 0.8F, 1.1F, new double[]{75, 55, 45});
            for (LivingEntity hitEntity : ShockWaveUtils.doRingShockWave(entity, pos, 1.75, 0, false, 20)) {
                boolean flag = entity.doHurtTarget(hitEntity, animation == STOMP_ANIMATION ? 0.7F : 0.8F, 0F);
                ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, !flag);
            }
        };
        builder.forAnimation(STOMP_ANIMATION).atTick(13, doPlayAttackSound).atTick(15, stompKeyFrame);
        builder.forAnimation(STOMP_ANIMATION2).atTick(11, doPlayAttackSound).atTick(13, stompKeyFrame);
        builder.forAnimation(ELBOW_STRIKE_ANIMATION).atTick(1, doPlayShortHumSound)
                .inRange(13, 21, (entity, animation, tick) -> {
                    entity.doTrailEffect(animation, tick == 19, tick > 19, null, new Vec3(0, -0.5, 0));
                    entity.doTrailEffect(animation, tick == 13, tick > 13 && tick < 19, true, null);
                    if (entity.level().isClientSide) return;
                    if (tick == 18) {
                        if (entity.level().getDifficulty() == Difficulty.EASY && entity.random.nextFloat() <= 0.25F) {
                            entity.playSound(SoundInit.MAN.get(), 2.5F, 1F);
                        } else entity.playSound(SoundInit.REALM_WARDEN_WHOOSH.get(), 1.4F, 0.9F);
                    }
                    if (tick == 21) entity.rangeAttack(4.5, entity.getBbHeight(), 4.5, 4.5, 90F, 180F, hitEntity -> {
                        entity.doHurtTarget(hitEntity, 0.8F, 0);
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, false);
                    });
                });
        builder.forAnimation(SWEEP_ANIMATION).atTick(1, (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_HUM.get(), 1.4F, 1.1F + (entity.random.nextFloat() - 0.5F) * 0.1F);
        }).inRange(19, 29, (entity, animation, tick) -> {
            entity.doTrailEffect(animation, tick == 19, tick > 19, null, null);
            if (entity.level().isClientSide) return;
            boolean flag = tick == 24;
            if (tick == 21 || flag) {
                double range = flag ? 4 : 3.5;
                entity.rangeAttack(range, entity.getBbHeight(), range, range, 180F, 160F, hitEntity -> {
                    entity.doHurtTarget(ModDamageSource.bypassCoolDown(entity), hitEntity, 0.6F, 0, false);
                    if (flag) ModEntityUtils.forceKnockBack(entity, hitEntity, 0.5F, false);
                });
            }
            if (tick == 19 || tick == 23) entity.playSound(SoundInit.REALM_WARDEN_WHOOSH.get(), 1.4F, tick == 23 ? 0.7F : 0.9F);
        }).atTick(46, (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_BLAST.get(), 1.2F, 1.5F);
        }).atTick(49, doPlayAttackSound).atTick(50, (entity, animation, tick) -> {
            Vec3 pos = entity.getPosOffset(true, 3.2F, 1.75F, 0F);
            entity.doGroundPoundEffect(pos, 1F, 1.1F, new double[]{85, 70, 45});
            for (LivingEntity hitEntity : ShockWaveUtils.doRingShockWave(entity, pos, 2, 0, false, 20)) {
                entity.doHurtTarget(hitEntity, 1F, 0F, true);
                ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, false);
            }
        });
        builder.forAnimation(BACKSTEP_ANIMATION).atTick(1, doPlayShortHumSound)
                .atTick(4, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        if (entity.modelParts == null || entity.modelParts.length < 2) return;
                        entity.doBackstepEffect(new Vec3(entity.modelParts[0].x, entity.getY() + 0.1, entity.modelParts[0].z));
                        entity.doBackstepEffect(new Vec3(entity.modelParts[1].x, entity.getY() + 0.1, entity.modelParts[1].z));
                    } else EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.15F, 2, 4);
                });
        builder.forAnimation(BACKSTEP_LANDING_ANIMATION).atTick(1, (entity, animation, tick) -> {
                    if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_HUM.get(), 1.4F, 1.1F + (entity.random.nextFloat() - 0.5F) * 0.1F);
                })
                .atTick(4, (entity, animation, tick) -> entity.doGroundPoundEffect(entity.position(), 0.9F, 1.2F, null))
                .inRange(5, 7, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        if (entity.modelParts == null || entity.modelParts.length < 2) return;
                        entity.doBackstepLandingEffect(entity.modelParts[0]);
                        entity.doBackstepLandingEffect(entity.modelParts[1]);
                    } else if (tick == 5) entity.spawnAnnihilatorMissiles();
                });
        builder.forAnimation(LEAP_ANIMATION).inRange(1, 19, (entity, animation, tick) -> {
            boolean leap = tick == 17;
            entity.doLeapEffect(tick, leap);
            if (leap && !entity.level().isClientSide) {
                entity.playSound(SoundInit.REALM_WARDEN_LEAP.get(), 1.5F * entity.getSoundVolumeScale(), 1F);
                entity.leapDownTarget = entity.getTarget();
                entity.cachedLandingPosY = entity.getY();
                entity.hoverTimer = 0;
                entity.cachedLandingPos = null;
                entity.alphaControlled.resetTimer();
                entity.setLeapStage(LeapStage.LEAP);
                entity.push(0, 3, 0);
            }
            if (!entity.level().isClientSide && tick % 10 == 1) entity.playSound(SoundInit.REALM_WARDEN_AIRFLOW.get(), 2F * entity.getSoundVolumeScale(), 1.5F);
        }).atTick(21, (entity, animation, tick) -> entity.setLeapStage(LeapStage.HOLD));
        builder.forAnimation(LEAP_LANDING_ANIMATION).atTick(3, (entity, animation, tick) -> {
                    if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_LANDING.get());
                }).atTick(5, (entity, animation, tick) -> {
                    entity.doLeapLandingEffect(ParticleInit.CRIT_RING.get(), 60, 40, 1, 1, new double[]{80, 70}, true);
                    if (entity.level().isClientSide) return;
                    entity.rangeAttack(8, 8, 8, 8, hitEntity -> {
                        entity.doHurtTarget(hitEntity, 1F, 0, true);
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, entity.getX() - hitEntity.getX(), entity.getZ() - hitEntity.getZ(), false);
                    });
                    EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.2F, 2, 4);
                    entity.playSound(SoundInit.REALM_WARDEN_SHAKE_GROUND.get(), 1.5F, 1.2F);
                }).atTick(15, (entity, animation, tick) -> EntityTelegraph.spawn(entity.level(), entity.position().add(0, 0.1, 0), 15, entity.isSecondPhase() ? 0xFF0080FF : 0xFFFF2020, entity.isSecondPhase() || !entity.isAlwaysActive() ? 12 : 10))
                .inRange(16, 39, (entity, animation, tick) -> {
                    List<LivingEntity> entities = entity.getNearByEntities(LivingEntity.class, 16, 16, 16, 16);
                    for (LivingEntity inRangeEntity : entities) {
                        if (inRangeEntity instanceof Player player && player.getAbilities().invulnerable) continue;
                        Vec3 diff = inRangeEntity.position().subtract(entity.position());
                        diff = diff.normalize().scale(0.065);
                        inRangeEntity.setDeltaMovement(inRangeEntity.getDeltaMovement().subtract(diff));
                    }
                    if (entity.level().isClientSide) return;
                    if (tick == 16) entity.playSound(SoundInit.REALM_WARDEN_HUM.get(), 1.4F, 0.8F + (entity.random.nextFloat() - 0.5F) * 0.1F);
                    if (tick == 37) entity.playSound(SoundInit.REALM_WARDEN_BLAST.get(), 1.5F, 1.75F);
                }).atTick(40, (entity, animation, tick) -> {
                    entity.doLeapLandingEffect(ParticleInit.GLOW.get(), 120, 100, 1.4F, 2F, new double[]{70, 50}, false);
                    ShockWaveUtils.doRingShockWave(entity, entity.getPosOffset(false, 0F, entity.getBbWidth(), 0), 2, 0.05F, false, 10);
                    ShockWaveUtils.doRingShockWave(entity, entity.getPosOffset(true, 0F, entity.getBbWidth(), 0), 2, 0.05F, false, 10);
                    if (entity.level().isClientSide) return;
                    entity.playSound(SoundInit.REALM_WARDEN_ATTACK.get(), 1.5F, 1F);
                    if (entity.isSecondPhase()) entity.playSound(SoundInit.REALM_WARDEN_SHOCK.get(), 1.5F, 1.2F + (entity.random.nextFloat() - 0.5F) * 0.1F);
                    EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.25F, 4, 3);
                    int size = entity.isSecondPhase() || !entity.isAlwaysActive() ? 24 : 20;
                    AABB area = ModEntityUtils.makeAABBWithSize(entity.getX(), entity.getY(), entity.getZ(), 0, size, size, size);
                    List<LivingEntity> entities = entity.level().getEntitiesOfClass(LivingEntity.class, area, target -> target != entity && !entity.isAlliedTo(target));
                    for (LivingEntity hitEntity : entities) {
                        entity.doHurtTarget(ModDamageSource.bypassArmor(entity), hitEntity, 1.2F, 0, true);
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 1F, entity.getX() - hitEntity.getX(), entity.getZ() - hitEntity.getZ(), false);
                    }
                }).inRange(40, 43, (entity, animation, tick) -> entity.doLandingExplosionEffect(tick));
        builder.forAnimation(JUMP_SMASH_START_ANIMATION).everyTick((entity, animation, tick) -> {
            if (entity.level().isClientSide) {
                float yaw = entity.yBodyRot;
                double rad;
                if (tick >= 18) rad = Math.toRadians(yaw + 150);
                else rad = Math.toRadians(yaw + 140 - tick);
                double dirX = -Math.sin(rad);
                double dirZ = Math.cos(rad);
                float width = entity.getBbWidth();
                Vec3 pos = entity.getPosOffset(true, -width / 2, width * 0.2F, entity.getBbHeight() * 0.75F);
                for (int i = 0; i < 3; i++) {
                    double speedBase = 1.5;
                    double spread = 1.5;
                    double vx = dirX * speedBase + (entity.random.nextDouble() - 0.5) * spread;
                    double vy = entity.random.nextDouble() + 0.1;
                    double vz = dirZ * speedBase + (entity.random.nextDouble() - 0.5) * spread;
                    int duration = Mth.randomBetweenInclusive(entity.random, 5, 15);
                    ParticleOptions dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.55F, 0.56F, 0.59F, 25F, duration, ParticleDust.EnumDustBehavior.SHRINK, 0.85F);
                    entity.level().addParticle(dustData, pos.x, pos.y, pos.z, vx, vy, vz);
                }
            } else {
                if (!entity.level().isClientSide && tick % 25 == 1) entity.playSound(SoundInit.REALM_WARDEN_AIRFLOW.get(), 2F, 1.75F);
                if (tick == 1) entity.playSound(SoundInit.REALM_WARDEN_HUM.get(), 1.4F, 0.9F + (entity.random.nextFloat() - 0.5F) * 0.1F);
            }
        });
        jumpSmashKeyframe(builder, JUMP_SMASH_ANIMATION, doPlayAttackSound);
        jumpSmashKeyframe(builder, DERIVED_JUMP_SMASH_ANIMATION, doPlayAttackSound).atTick(25, (entity, animation, tick) -> {
            Vec3 pos = entity.getPosOffset(false, 2.4F, 0F, 0F);
            entity.doGroundPoundEffect(pos, 1.5F, 1.5F, 1F, null, entity.isSecondPhase() ? new double[]{0.49F, 0.9F, 1F, 0.72F} : null);
            if (entity.level().isClientSide) {
                Vector4f color = entity.getColorByPhase();
                AdvancedParticleBase.spawnParticle(entity.level(), ParticleInit.ADV_RING3.get(), pos.x, pos.y + 0.375F, pos.z, 0, 0, 0, true, 0, 0, 0,
                        0, 0, color.x, color.y, color.z, 0, 1, 7, true, false, false, new ParticleComponent[]{
                                new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(10F, 55F), false),
                                new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.5F, 0.1F), false),
                        });
            } else {
                entity.playSound(SoundInit.REALM_WARDEN_ATTACK.get(), 1.5F, 1F);
                AABB area = ModEntityUtils.makeAABBWithSize(pos.x, pos.y, pos.z, 0, 7, 14, 7);
                List<LivingEntity> entities = entity.level().getEntitiesOfClass(LivingEntity.class, area, target -> target != entity && !entity.isAlliedTo(target));
                for (LivingEntity hitEntity : entities) {
                    entity.doHurtTarget(ModDamageSource.bypassArmor(entity), hitEntity, 1F, 0, true);
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 1F, pos.x - hitEntity.getX(), pos.z - hitEntity.getZ(), false);
                }
            }
        });
        builder.forAnimation(TURNAROUND_SWEEP_ANIMATION)
                .inRange(14, 21, (entity, animation, tick) -> {
                    entity.doTrailEffect(animation, tick == 14, tick < 21, false, new Vec3(0, -0.75, 0));
                    entity.doTrailEffect(animation, tick == 15, tick > 15, true, new Vec3(0, -0.5, 0));
                    if (entity.level().isClientSide) return;
                    if (tick == 16) entity.playSound(SoundInit.REALM_WARDEN_WHOOSH.get(), 1.4F, 0.9F);
                    if (tick == 18) {
                        entity.playSound(SoundInit.REALM_WARDEN_ATTACK.get(), 1.2F, 1F);
                        entity.rangeAttack(4, entity.getBbHeight(), 4, 4, hitEntity -> {
                            entity.doHurtTarget(hitEntity, 0.7F, 0);
                            ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, false);
                        });
                        EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.15F, 3, 3);
                    }
                });
        builder.forAnimation(DOUBLE_FIST_SLAM_ANIMATION).atTick(12, (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_BLAST.get(), 1.2F, 1.5F);
        }).atTick(14, doPlayAttackSound).atTick(16, (entity, animation, tick) -> {
            entity.doFistSlamEffect();
            if (entity.level().isClientSide) return;
            entity.rangeAttack(3.75, entity.getBbHeight(), 3.75, 3.75, 45F, 45F, hitEntity -> {
                if (entity.doHurtTarget(ModDamageSource.bypassShield(entity, entity), hitEntity, 1, 1, true)) {
                    entity.stun(null, hitEntity, 30, false);
                }
                ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, false);
            });
            EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.2F, 3, 3);
            if (entity.isSecondPhase()) entity.playSound(SoundInit.REALM_WARDEN_SHOCK.get(), 1F, 1.2F + (entity.random.nextFloat() - 0.5F) * 0.1F);
        });
        builder.forAnimation(HEAVY_SMASH_ANIMATION).atTick(1, (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_HUM.get(), 1.4F, 0.9F + (entity.random.nextFloat() - 0.5F) * 0.1F);
        }).atTick(24, (entity, animation, tick) -> {
            if (!entity.level().isClientSide) entity.playSound(SoundInit.REALM_WARDEN_BLAST.get(), 1.5F, 1.5F);
        }).atTick(27, (entity, animation, tick) -> {
            Vec3 pos = entity.getPosOffset(false, 3F, 0F, 0F);
            entity.doGroundPoundEffect(pos, 1.4F, 1.5F, null);
            for (LivingEntity hitEntity : ShockWaveUtils.doRingShockWave(entity, pos, 2.5, -0.03F, false, 20)) {
                entity.doHurtTarget(hitEntity, 1.1F, 0F, true);
                ModEntityUtils.forceKnockBack(entity, hitEntity, 1.25F, false);
            }
        });
        return manager;
    }

    private static KeyframeManager.KeyframeManegerBuilder<EntityRealmWarden> jumpSmashKeyframe(KeyframeManager.KeyframeManegerBuilder<EntityRealmWarden> builder, Animation defineAnimation, Keyframe<EntityRealmWarden> doPlayAttackSound) {
        return builder.forAnimation(defineAnimation).atTick(5, doPlayAttackSound).atTick(7, (entity, animation, tick) -> {
            Vec3 pos = entity.getPosOffset(false, 2.4F, 0F, 0F);
            entity.doGroundPoundEffect(pos, 1.2F, 1.2F, new double[]{70, 60, 50});
            for (LivingEntity hitEntity : ShockWaveUtils.doRingShockWave(entity, pos, 2.25, -0.015F, false, 20)) {
                entity.doHurtTarget(hitEntity, 1F, 0F, true);
                ModEntityUtils.forceKnockBack(entity, hitEntity, 0.75F, false);
            }
        });
    }

    private static AnimationReleaseManager<EntityRealmWarden> setupAnimationRules() {
        AnimationReleaseManager<EntityRealmWarden> manager = new AnimationReleaseManager<>();
        AnimationReleaseManager.Builder<EntityRealmWarden> builder = manager.builder();
        HealthScaledCooldown generator = new HealthScaledCooldown(380, 40, 60, 0.5F, true);//14~18(21

        AnimationRule<EntityRealmWarden> leapRule = builder.define(LEAP_ANIMATION)
                .onlyCombo()
                .cooldown(entity -> 1200)
                .build();

        AnimationRule<EntityRealmWarden> backStepRule = builder.define(BACKSTEP_ANIMATION)
                .priority(2)
                .cooldown(generator)
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.getCooldownManager().isReady(JUMP_SMASH_START_ANIMATION) || entity.getCooldownManager().isReady(LEAP_ANIMATION),
                        ConditionFactory.angleRange(-120, 120),
                        ConditionFactory.hybridDistanceRange(16, 0, 6)
                )).build();

        HealthScaledCooldown heavySwing = new HealthScaledCooldown(420, 80, 80, 0.5F, true);//13~21(25
        AnimationRule<EntityRealmWarden> heavySwingRule = builder.define(HEAVY_SWING_ANIMATION)
                .cooldown(heavySwing)
                .condition(ConditionFactory.and(
                        ConditionFactory.heightDiff(4.45),
                        ConditionFactory.distanceRange(2, 7, 8),
                        ConditionFactory.randomChanceOnHighHealth(0.6F, 0.4F)
                )).onSuccess(e -> e.getCooldownManager().setCD(DERIVED_HEAVY_SWING_ANIMATION, heavySwing.generate(e))).build();

        AnimationRule<EntityRealmWarden> derivedHeavySwingRule = builder.define(DERIVED_HEAVY_SWING_ANIMATION)
                .cooldown(heavySwing)
                .condition(ConditionFactory.and(
                        ConditionFactory.heightDiff(4.45),
                        ConditionFactory.distanceRange(2, 9, 10),
                        ConditionFactory.randomChanceOnLowHealth(0.2F, 0.6F)
                )).onSuccess(e -> e.getCooldownManager().setCD(HEAVY_SWING_ANIMATION, heavySwing.generate(e))).build();

        AnimationRule<EntityRealmWarden> heavySmashRule = builder.define(HEAVY_SMASH_ANIMATION)
                .onlyCombo()
                .cooldown(entity -> 120)
                .build();

        AnimationRule<EntityRealmWarden> doubleFistSlamRule = builder.define(DOUBLE_FIST_SLAM_ANIMATION)
                .onlyCombo()
                .triggerAtTick(28)
                .condition(ConditionFactory.randomChanceOnLowHealth(0F, 0.8F))
                .next(heavySmashRule, 1.5, 0.75)
                .next(backStepRule)
                .nextH(leapRule, 0.25)
                .build();

        AnimationRule<EntityRealmWarden> turnaroundSweepRule = builder.define(TURNAROUND_SWEEP_ANIMATION)
                .priority(2)
                .triggerAtTick(30)
                .cooldown(new HealthScaledCooldown(300, 50, 50, 0.5F, true))//10-15(17.5
                .condition(ConditionFactory.and(
                        ConditionFactory.angleRange(100, 260),
                        ConditionFactory.distanceRange(0, 6, 8)
                )).nextH(doubleFistSlamRule, 0.85)
                .build();

        AnimationRule<EntityRealmWarden> jumpSmashStartRule = builder.define(JUMP_SMASH_START_ANIMATION)
                .cooldown(generator)
                .condition(ConditionFactory.and(
                        ConditionFactory.distanceRange(10, 24),
                        ConditionFactory.randomChanceOnLowHealth(0.2F, 0.8F)
                ))
                .build();

        HealthScaledCooldown groundPound = new HealthScaledCooldown(350, 50, 100, 0.5F, true);//10~15(20
        AnimationRule<EntityRealmWarden> groundPoundRule = builder.define(GROUND_POUND_ANIMATION)
                .triggerAtTick(33)
                .cooldown(groundPound)
                .condition(ConditionFactory.and(
                        ConditionFactory.angleRange(-60, 60),
                        ConditionFactory.distanceRange(0, 4, 6),
                        ConditionFactory.randomChanceOnLowHealth(0.4F, 0.6F)
                ))
                .nextW(turnaroundSweepRule, 1.2)
                .nextW(backStepRule, 1.2)
                .nextH(derivedHeavySwingRule, 0.5)
                .nextH(jumpSmashStartRule, 0.5)
                .nextH(leapRule, 0.25)
                .next(heavySwingRule, 0.9, 0.75)
                .build();

        AnimationRule<EntityRealmWarden> sweepRule = builder.define(SWEEP_ANIMATION)
                .triggerAtTick(67)
                .cooldown(new HealthScaledCooldown(480, 80, 100, 0.5F, true))//15-19(24
                .condition(ConditionFactory.and(
                        ConditionFactory.angleRange(-90, 90),
                        ConditionFactory.heightDiff(4.45),
                        ConditionFactory.distanceRange(2, 7, 8),
                        ConditionFactory.randomChanceOnLowHealth(0.3F, 0.7F)
                )).nextW(backStepRule, 1.2)
                .next(turnaroundSweepRule)
                .nextH(leapRule, 0.25)
                .nextH(derivedHeavySwingRule, 0.5)
                .nextH(heavySwingRule, 0.75)
                .next(doubleFistSlamRule, 0.5, 0.25, ConditionFactory.and(
                        ConditionFactory.distanceRange(0, 9, 10),
                        ConditionFactory.randomChanceOnLowHealth(0F, 0.5F)
                ))
                .build();

        AnimationRule<EntityRealmWarden> stompRule2 = builder.define(STOMP_ANIMATION2)
                .onlyCombo()
                .triggerAtTick(20)
                .condition(ConditionFactory.distanceRange(0, 6, 8))
                .nextW(groundPoundRule, 1.2)
                .nextW(backStepRule, 1.2)
                .next(derivedHeavySwingRule, 1.2, 0.5)
                .next(sweepRule)
                .nextH(leapRule, 0.25)
                .nextW(heavySwingRule, 0.9)
                .nextW(turnaroundSweepRule, 0.9)
                .build();

        AnimationRule<EntityRealmWarden> stompRule = builder.define(STOMP_ANIMATION)
                .priority(2)
                .triggerAtTick(26)
                .cooldown(new HealthScaledCooldown(300, 40, 40, 0.5F, true))//11-15(17
                .condition(ConditionFactory.and(
                        ConditionFactory.angleRange(-60, 60),
                        ConditionFactory.distanceRange(0, 6, 7),
                        ConditionFactory.randomChanceOnLowHealth(0.4F, 0.6F)
                ))
                .nextW(stompRule2, 0.75)
                .nextW(groundPoundRule, 0.75)
                .nextW(turnaroundSweepRule, 1)
                .build();

        AnimationRule<EntityRealmWarden> elbowStrikeRule = builder.define(ELBOW_STRIKE_ANIMATION)
                .priority(2)
                .triggerAtTick(35)
                .cooldown(new HealthScaledCooldown(260, 60, 60, 0.5F, true))//7-13.5(16
                .condition(ConditionFactory.and(
                        ConditionFactory.angleRange(-60, 60),
                        ConditionFactory.distanceRange(0, 4, 5),
                        ConditionFactory.randomChanceOnHighHealth(0.65F, 0.45F)
                ))
                .nextW(turnaroundSweepRule, 1.2)
                .nextW(stompRule, 1.1)
                .nextH(doubleFistSlamRule, 0.75)
                .nextH(derivedHeavySwingRule, 0.5)
                .nextH(jumpSmashStartRule, 0.5)
                .next(heavySwingRule, 0.9, 0.75)
                .build();

        AnimationRule<EntityRealmWarden> backStepLandingRule = builder.define(BACKSTEP_LANDING_ANIMATION)
                .onlyCombo()
                .triggerAtTick(25)
                .next(leapRule, 1.5, 0.25)
                .nextW(jumpSmashStartRule, 1.2)
                .nextH(stompRule, 0.75)
                .next(sweepRule)
                .next(groundPoundRule)
                .build();

        manager.registerRule(heavySwingRule);
        manager.registerRule(derivedHeavySwingRule);
        manager.registerRule(backStepRule);
        manager.registerRule(groundPoundRule);
        manager.registerRule(stompRule);
        manager.registerRule(stompRule2);
        manager.registerRule(elbowStrikeRule);
        manager.registerRule(sweepRule);
        manager.registerRule(turnaroundSweepRule);
        manager.registerRule(jumpSmashStartRule);
        manager.registerRule(doubleFistSlamRule);
        manager.registerRule(heavySmashRule);
        manager.registerRule(backStepLandingRule);
        manager.registerRule(leapRule);
        builder.condition(e -> !e.shouldPlayLeapAnimation && e.isActive());
        return manager;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 400.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(ForgeMod.ENTITY_GRAVITY.get(), 0.125D)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 4D);
    }

    private void spawnAnnihilatorMissiles() {
        if (this.level().isClientSide) return;
        Vec3 look = this.getLookAngle();
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = look.cross(up).normalize();
        Vec3 left = right.scale(-1);
        Vec3 back = look.scale(-1);
        Vec3 targetPos = this.position().add(look.scale(12 + this.random.nextDouble() * 3));
        double backOffset = 0.5, heightOffset = 3.5, sideOffset = 0.6, upWeight = 2.5;
        int sideCount = this.isSecondPhase() ? 5 : 4;
        Vec3 backUp = back.scale(backOffset).add(up.scale(heightOffset));
        Vec3 leftSpawn = this.position().add(backUp).add(left.scale(sideOffset));
        Vec3 rightSpawn = this.position().add(backUp).add(right.scale(sideOffset));
        Vec3 leftBase = left.add(up.scale(upWeight)).normalize();
        Vec3 rightBase = right.add(up.scale(upWeight)).normalize();
        for (int side = 0; side < 2; side++) {
            this.playSound(SoundInit.REALM_WARDEN_LAUNCH.get(), 1.5F, 1F);
            Vec3 spawn = (side == 0) ? leftSpawn : rightSpawn;
            Vec3 baseDir = (side == 0) ? leftBase : rightBase;
            for (int i = 0; i < sideCount; i++) {
                this.shootMissile(spawn, targetPos, baseDir);
            }
        }
    }

    private void shootMissile(Vec3 spawnPos, Vec3 targetPos, Vec3 motion) {
        float speed = (float) (0.14 + this.random.nextDouble() * 0.02);
        EntityAnnihilatorMissile missile = new EntityAnnihilatorMissile(this.level(), this, EntityAnnihilatorMissile.ElementType.SPARKFERNO);
        missile.setTargetPos(this.getTarget(), targetPos.offsetRandom(this.random, 5F));
        missile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        missile.shoot(motion.x, motion.y, motion.z, speed, 10F);
        this.level().addFreshEntity(missile);
    }

    private Vector4f getColorByPhase() {
        return this.isSecondPhase() ? COLORS[1] : COLORS[0];
    }

    private Vec3 getDefaultLandingPos() {
        return new Vec3(this.getX(), this.cachedLandingPosY, this.getZ());
    }

    private float getSoundVolumeScale() {
        if (getTarget() instanceof EntityAbsRelicron) return 1F;
        return triggeredPhashBits == BIT_MEDIUM ? 0.2F : 1F;
    }

    private boolean tryTeleportTo(double x, double y, double z) {
        double startY = y + this.getBbHeight() * 2;
        BlockPos blockpos = BlockPos.containing(x, startY, z);
        if (!this.level().hasChunkAt(blockpos)) return false;
        AABB newBB = this.getBoundingBox().move(x - this.getX(), startY - this.getY(), z - this.getZ());
        if (this.level().noCollision(this, newBB)) {
            this.teleportTo(x, startY, z);
            return true;
        }
        return false;
    }

    private void pounce(int tick, int offset, float duration, boolean inversion, float speedModifier) {
        double radians = Math.toRadians(this.getYRot() + 90F);
        Vec3 pounceVec = new Vec3(Math.cos(radians), 0.0, Math.sin(radians)).normalize();
        float tickFactor = ModMathUtils.getTickFactor(tick - offset, duration, inversion);
        float distFactor = ModMathUtils.getTickFactor(this.targetDistance, this.getBbWidth(), false);
        float speedMultiplier = ModMathUtils.calculateSpeedMultiplier(tickFactor, distFactor, 1, speedModifier);
        double baseValue = this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) * 2F;
        double moveSpeed = Math.min(this.getAttributeValue(Attributes.MOVEMENT_SPEED), baseValue + baseValue * 0.4);
        this.setDeltaMovement(pounceVec.x * moveSpeed * speedMultiplier, this.getDeltaMovement().y, pounceVec.z * moveSpeed * speedMultiplier);
    }

    private void doLandingExplosionEffect(int tick) {
        if (this.level().isClientSide) {
            if (this.isSecondPhase()) {
                if (this.modelParts == null || this.modelParts.length < 2) return;
                Vec3 leftStart = this.modelParts[0].add(0, -1, 0);
                Vec3 rightStart = this.modelParts[1].add(0, -1, 0);
                for (int i = 0; i < 5; i++) {
                    double leftPitch = Math.toRadians(this.yBodyRot) + Math.toRadians((this.random.nextFloat() - 0.5) * 160);
                    Vec3 leftEnd = leftStart.add(Math.cos(leftPitch) * this.random.nextFloat() * 12, this.random.nextFloat() * getBbHeight() / 2, Math.sin(leftPitch) * this.random.nextFloat() * 12);
                    double rightPitch = Math.toRadians(this.yBodyRot + 180) + Math.toRadians((this.random.nextFloat() - 0.5) * 160);
                    Vec3 rightEnd = rightStart.add(Math.cos(rightPitch) * this.random.nextFloat() * 12, this.random.nextFloat() * getBbHeight() / 2, Math.sin(rightPitch) * this.random.nextFloat() * 12);
                    this.doLightBlotEffect(leftStart, leftEnd, i == 0 ? 6 : 5, i == 0 ? 0.2F : 0.1F, 0.12F, 0.08F, i == 0 ? LightningBolt.FadeFunction.NONE : LightningBolt.FadeFunction.fade(0.6F));
                    this.doLightBlotEffect(rightStart, rightEnd, i == 0 ? 6 : 5, i == 0 ? 0.2F : 0.1F, 0.12F, 0.08F, i == 0 ? LightningBolt.FadeFunction.NONE : LightningBolt.FadeFunction.fade(0.6F));
                }
            } else {
                AdvancedParticleData particleData = EntityRealmWarden.getRibbonParticleData(0.52F, 0.3F, 1F, 0.65F, 0.21F);
                for (int i = 0; i < 12; i++) {
                    Vec3 centerPos = this.position();
                    Vec3 randomPos = this.position().offsetRandom(random, 15F);
                    double toParticleX = randomPos.x - centerPos.x;
                    double toParticleZ = randomPos.z - centerPos.z;
                    double length = Math.sqrt(toParticleX * toParticleX + toParticleZ * toParticleZ);
                    if (length > 0) {
                        toParticleX /= length;
                        toParticleZ /= length;
                    }
                    double angleRad = Math.toRadians(Mth.randomBetweenInclusive(random, 75, 90));
                    double speed = Mth.randomBetween(random, 1.5F, 2F) * (0.8 + random.nextDouble() * 0.4);
                    double horizontalSpeed = speed * Math.cos(angleRad);
                    double verticalSpeed = speed * Math.sin(angleRad);
                    this.level().addParticle(particleData, randomPos.x, centerPos.y, randomPos.z, toParticleX * horizontalSpeed, verticalSpeed, toParticleZ * horizontalSpeed);
                }
            }
        } else if (tick == 40 && this.isSecondPhase()) {
            int layers = 3;
            double baseRadius = 4;
            double radiusStep = 4;
            int baseCount = 4;
            int countStep = 3;
            double angleOffsetStep = this.random.nextFloat() * Math.PI;
            Vec3 center = this.position();
            for (int layer = 0; layer < layers; layer++) {
                double radius = baseRadius + layer * radiusStep;
                int count = baseCount + layer * countStep;
                double angleOffset = layer * angleOffsetStep;
                double step = 2 * Math.PI / count;
                for (int i = 0; i < count; i++) {
                    float angle = (float) (angleOffset + i * step);
                    double x = center.x + Math.cos(angle) * radius;
                    double z = center.z + Math.sin(angle) * radius;
                    int warmupDelay = layer * 7 + this.random.nextInt(15);
                    Vec3 point = ModEntityUtils.checkSummonEntityPointNullable(this.level(), x, z, center.y - 2, center.y + 2);
                    if (point == null) point = new Vec3(x, this.getY(), z);
                    EntitySurge surge = new EntitySurge(this.level(), point.x, point.y, point.z, angle, warmupDelay, this);
                    this.level().addFreshEntity(surge);
                }
            }
        }
    }

    private void doFistSlamEffect() {
        if (this.level().isClientSide) {
            Vec3 pos = this.getPosOffset(false, 2.1F, 0F, this.getBbHeight() * 0.5F);
            Vector4f color = this.getColorByPhase();
            this.doFlashingEffect(ParticleInit.THUMP_RING.get(), pos, 45F, 45F, 0, color.x, color.y, color.z);
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_RING3.get(), pos.x, this.getY() + 0.1, pos.z, 0, 0, 0, false, 0, Math.PI / 2F, 0,
                    0, 0, 0.98F, 0.98F, 0.98F, 0, 1, 4, false, false, false, new ParticleComponent[]{
                            new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(0F, 40F), false),
                            new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.4F, 0F), false),
                    });
            ModParticleUtils.roundParticleOutburst(this.level(), 15, new ParticleOptions[]{
                    getRibbonParticleData(0.5F, 0.1F, 1F, 0.89F, 0.64F)
            }, pos.x, pos.y, pos.z, 2.5F);
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 40F, 25, ParticleDust.EnumDustBehavior.GROW, 0.74F);
            ModParticleUtils.annularParticleOutburst(this.level(), 18, dustData, pos.x, this.getY(), pos.z, 2F, 0.6);
            if (this.isSecondPhase()) {
                for (int i = 0; i < 6; i++) {
                    Vec3 start = pos.offsetRandom(this.random, 0.5F);
                    Vec3 end = start.add((this.random.nextFloat() - 0.5) * 2.5, (this.random.nextFloat() - 0.5) * 6, (this.random.nextFloat() - 0.5) * 2.5);
                    this.doLightBlotEffect(start, end, 5, 0.08F, 0.05F, 0.05F, LightningBolt.FadeFunction.fade(0.6F));
                }
            }
        }
    }

    private void doFlashingEffect(ParticleType<AdvancedParticleData> type, Vec3 pos, float scale1, float scale2, float pitch, float finalR, float finalG, float finalB) {
        if (isSecondPhase()) {
            scale1 *= 1.2F;
            scale2 *= 1.2F;
        }
        PropertyControl component0 = new PropertyControl(EnumParticleProperty.RED, new AnimData.KeyTrack(new float[]{1F, finalR}, new float[]{0F, 0.25F}), false);
        PropertyControl component1 = new PropertyControl(EnumParticleProperty.GREEN, new AnimData.KeyTrack(new float[]{1F, finalG}, new float[]{0F, 0.25F}), false);
        PropertyControl component2 = new PropertyControl(EnumParticleProperty.BLUE, new AnimData.KeyTrack(new float[]{1F, finalB}, new float[]{0F, 0.25F}), false);
        AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.GLOW.get(), pos.x, pos.y, pos.z, 0, 0, 0, true, 0, 0, 0,
                0, 0, 1, 1, 1, 0, 1, 4, true, false, false, new ParticleComponent[]{
                        new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(15F, scale1), false),
                        new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(1F, 0F), false),
                        component0, component1, component2,
                });
        AdvancedParticleBase.spawnParticle(this.level(), type, pos.x, pos.y, pos.z, 0, 0, 0, pitch == 0, 0, pitch, 0,
                0, 0, 1, 1, 1, 0, 1, 4, true, false, false, new ParticleComponent[]{
                        new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(15F, scale2), false),
                        new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(1F, 0F), false),
                        component0, component1, component2,
                });
    }

    private void doGroundPoundEffect(Vec3 pos, float scale, float yBlockPtcScale, double @Nullable [] angles) {
        doGroundPoundEffect(pos, scale, scale, yBlockPtcScale, angles, null);
    }

    private void doGroundPoundEffect(Vec3 pos, float scale, float volume, float yBlockPtcScale, double @Nullable [] angles, double @Nullable [] colors) {
        boolean secondPhase = this.isSecondPhase();
        if (this.level().isClientSide) {
            if (angles == null) angles = new double[]{55, 35, 25};
            if (colors == null) colors = new double[]{1F, 0.89F, 0.64F, 0.6F};
            int[] particles = {6, 8, 5};
            double[] radii = {0.6 * scale, 1 * scale, 1.2 * scale};
            double[] speeds = {1.8, 2.2, 2.5};
            double[] scales = {0.08, 0.1, 0.12};
            ModParticleUtils.multiLayerBowlParticles(this.level(), pos, 3, particles, radii, speeds, angles, colors, scales, 0.55F);
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 40F, 35, ParticleDust.EnumDustBehavior.GROW, 0.74F);
            ModParticleUtils.annularParticleOutburst(this.level(), 18, dustData, pos.x, pos.y, pos.z, 1.6F, 0.6);
            Vector4f color = this.getColorByPhase();
            this.level().addParticle(new ParticleRing.RingData(ParticleInit.RADIAL_OPACITY_RING.get(), 0F, (float) (Math.PI / 2F), 12, color.x, color.y, color.z, 1F, 70F * scale, false,
                    ParticleRing.EnumRingBehavior.GROW), pos.x, pos.y + 0.375F, pos.z, 0, 0, 0);
            ModParticleUtils.blockParticlesAround(this.level(), pos.x, pos.y, pos.z, (int) (8 * scale), 0.5 * scale, 1 * scale, 0.15,
                    0.25, 0.2 * yBlockPtcScale, 0.5 * yBlockPtcScale, -0.2, 0.1, (pos2, state) -> getBlockParticleData(this.random, state, pos, 2.5));
            this.doFlashingEffect(ParticleInit.THUMP_RING.get(), pos.add(0, 0.1, 0), 60F * scale, 40F * scale, (float) (Math.PI / 2F), color.x, color.y, color.z);
            if (secondPhase) {
                for (int i = 0; i < (int) (6 * scale); i++) {
                    Vec3 start = pos.offsetRandom(this.random, 1F);
                    Vec3 end = start.add((this.random.nextFloat() - 0.5) * 6 * scale, 1 + this.random.nextFloat(), (this.random.nextFloat() - 0.5) * 6 * scale);
                    this.doLightBlotEffect(start, end, 5, 0.08F, 0.08F, 0.1F, LightningBolt.FadeFunction.fade(0.75F));
                }
            }
        } else {
            this.playSound(SoundInit.REALM_WARDEN_SHAKE_GROUND.get(), 1.2F * volume, 1.2F * volume);
            if (secondPhase) this.playSound(SoundInit.REALM_WARDEN_SHOCK.get(), volume, 1.3F + (this.random.nextFloat() - 0.5F) * 0.1F);
            EntityCameraShake.cameraShake(this.level(), pos, 20, 0.2F * scale, 3, 4);
        }
    }

    private void doTrailEffect(Animation animation, boolean startFlag, boolean holdFlag, @Nullable Boolean left, @Nullable Vec3 offset) {
        if (this.level().isClientSide && this.modelParts != null && this.modelParts.length > 0) {
            if (startFlag) {
                lPrePos = this.modelParts[0];
                rPrePos = this.modelParts[1];
            } else if (holdFlag) {
                offset = offset == null ? Vec3.ZERO : offset;
                Vec3 leftPos = this.modelParts[0];
                double lLength = this.lPrePos.subtract(leftPos).length();
                int lNumDusts = (int) Math.min(Math.floor(2 * lLength), 14);
                if (left != null && !left) lNumDusts = 0;
                Vec3 rightPos = this.modelParts[1];
                double rLength = this.rPrePos.subtract(rightPos).length();
                int rNumDusts = (int) Math.min(Math.floor(2 * rLength), 14);
                if (left != null && left) rNumDusts = 0;
                this.spawnSwipeParticle(animation, lPrePos, leftPos, lNumDusts, true, offset);
                this.spawnSwipeParticle(animation, rPrePos, rightPos, rNumDusts, false, offset);
                this.lPrePos = leftPos;
                this.rPrePos = rightPos;
            }
        }
    }

    private void spawnSwipeParticle(Animation animation, Vec3 start, Vec3 end, int numDusts, boolean left, Vec3 offset) {
        for (int i = 0; i < numDusts; i++) {
            double speedMultiplier = 0.05;
            double x = start.x + i * (end.x - start.x) / numDusts;
            double y = start.y + i * (end.y - start.y) / numDusts;
            double z = start.z + i * (end.z - start.z) / numDusts;
            float randomFactor;
            double dx = x - this.getX();
            double dy = y - this.getY();
            double dz = z - this.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance > 0) {
                boolean isElbowStrike = animation == ELBOW_STRIKE_ANIMATION;
                if (isElbowStrike || animation == SWEEP_ANIMATION || animation == TURNAROUND_SWEEP_ANIMATION) {
                    if (i == 0) ModParticleUtils.blockParticleDirectionality(this.level(), x + offset.x, this.getY(), z + offset.z, Math.toRadians(this.getYRot()), numDusts, BLOCK_OFFSETS, 5F);
                    if (i % 2 == 1) {
                        AdvancedParticleData particleData = getRibbonParticleData(Mth.randomBetween(this.random, 0.2F, 0.25F), 0.07F, 1F, 0.89F, 0.64F);
                        ModParticleUtils.blockParticleDirectionality(this.level(), x + offset.x, this.getY(), z + offset.z, -0.2F, Math.toRadians(this.getYRot()), numDusts, SHORT_BLOCK_OFFSETS, 0.14F,
                                hardness -> hardness > 1 || hardness < 0, (pos, state) -> particleData);
                    }
                    if (isElbowStrike && left) return;
                }
                double speed = 1.5;
                double xSpeed = (dx / distance) * speed;
                double ySpeed = (this.random.nextDouble() - 0.1) * 0.5 * speedMultiplier;
                double zSpeed = (dz / distance) * speed;
                randomFactor = 0.5F;
                xSpeed += (this.random.nextDouble() - 0.5) * randomFactor * 0.1;
                zSpeed += (this.random.nextDouble() - 0.5) * randomFactor * 0.1;
                int duration = 8 + this.random.nextInt(5);
                ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 30F, duration, ParticleDust.EnumDustBehavior.GROW, 0.76F);
                this.level().addParticle(dustData, x + offset.x, y + offset.y, z + offset.z, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    private void doBackstepEffect(Vec3 pos) {
        int[] particles = {6, 8};
        double[] radii = {0.6, 1};
        double[] speeds = {1.8, 2.2};
        double[] angles = {80, 60};
        double[] scales = {0.06, 0.06};
        double[] color = {1F, 0.89F, 0.64F, 0.6F};
        ModParticleUtils.blockParticlesAround(this.level(), pos.x, pos.y, pos.z, 8, 1, 1.5, 2, 5, 3, 6, -0.2, 0.1);
        AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.THUMP_RING.get(), pos.x, pos.y, pos.z, 0, 0, 0, false, 0, Math.PI / 2F, 0,
                0, 0, 1F, 0.65F, 0.21F, 0, 1, 4, true, false, false, new ParticleComponent[]{
                        new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(1F, 0F), false),
                        new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(0F, 60F), false)
                });
        ModParticleUtils.multiLayerBowlParticles(this.level(), pos, 3, particles, radii, speeds, angles, color, scales, 0.55F);
    }

    private void doBackstepLandingEffect(Vec3 pos) {
        int length = 5;
        for (int i = 1; i <= length; ++i) {
            double d0 = pos.x + (this.random.nextDouble() - this.random.nextDouble()) * 0.75D;
            double d1 = pos.y + (this.random.nextDouble() - this.random.nextDouble()) * 0.75D;
            double d2 = pos.z + (this.random.nextDouble() - this.random.nextDouble()) * 0.75D;
            this.level().addParticle(ParticleInit.BLAZE_EXPLOSION.get(), d0, d1, d2, i / (float) length, 0.0D, 0.0D);
        }
    }

    private void doLeapEffect(int tick, boolean leap) {
        if (leap) {
            this.doGroundPoundEffect(this.position(), 1.2F, 1.2F * getSoundVolumeScale(), 1.5F, new double[]{85, 80, 75}, null);
            return;
        }
        if (this.level().isClientSide) {
            if (tick % 2 == 1) {
                int duration = Mth.randomBetweenInclusive(this.random, 10, 15);
                ParticleOptions dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.55F, 0.56F, 0.59F, 30F, duration, ParticleDust.EnumDustBehavior.SHRINK, 0.9F);
                spawnRotatingDustEffect(dustData, this.level(), position().add(0, 0.75, 0), 2, 6, 0.8, 1.2, 0.5);
            }
            if (tick % 3 != 1) return;
            float startYaw = Mth.randomBetween(this.random, 0, Mth.PI * 2);
            float targetYaw = startYaw + Mth.randomBetween(this.random, Mth.PI / 3, Mth.PI / 2);
            int duration = Mth.randomBetweenInclusive(this.random, 5, 7);
            int startScale = Mth.randomBetweenInclusive(this.random, 5, 20);
            int targetScale = startScale + Mth.randomBetweenInclusive(this.random, 40, 50);
            boolean inversion = this.random.nextBoolean();
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.VORTEX.get(), getRandomX(0.1), getY() + 0.25, getRandomZ(0.1), 0, 0, 0, false, startYaw, Math.PI / 2, 0, 0,
                    0, 0.55, 0.56, 0.59, 0.98, 1, duration, false, true, false, new ParticleComponent[]{
                            new ParticleComponent.FreeFallSimulator(0.225F),
                            new PropertyControl(EnumParticleProperty.YAW, AnimData.startAndEnd(inversion ? startYaw : -startYaw, inversion ? targetYaw : -targetYaw), false),
                            new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(startScale, targetScale), false),
                            new PropertyControl(EnumParticleProperty.ALPHA, new AnimData.KeyTrack(new float[]{0.6F, 0.6F, 0}, new float[]{0, 0.25F, 1}), false),
                    });
        }
    }

    private static void spawnRotatingDustEffect(ParticleOptions particle, Level level, Vec3 centerPos, double radius, int count, double radialSpeed, double tangentialSpeed, double verticalSpread) {
        RandomSource random = level.random;
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * random.nextDouble();
            double x = centerPos.x + radius * Math.cos(angle);
            double z = centerPos.z + radius * Math.sin(angle);
            double y = centerPos.y + (random.nextDouble() - 0.5) * verticalSpread;
            Vec3 radialDir = new Vec3(Math.cos(angle), 0, Math.sin(angle));
            Vec3 tangentialDir = new Vec3(-Math.sin(angle), 0, Math.cos(angle));
            double vx = radialSpeed * radialDir.x + tangentialSpeed * tangentialDir.x;
            double vy = (random.nextDouble() - 0.5) * 0.1;
            double vz = radialSpeed * radialDir.z + tangentialSpeed * tangentialDir.z;
            level.addParticle(particle, x, y, z, vx, vy, vz);
        }
    }

    private void doLeapLandingEffect(ParticleType<AdvancedParticleData> type, float scale1, float scale2, float dustSpeed, float effectScale, double[] angles, boolean landing) {
        if (this.level().isClientSide) {
            Vec3 pos = this.position();
            Vector4f color = this.getColorByPhase();
            this.doFlashingEffect(type, pos.add(0, 0.1, 0), scale1, scale2, (float) (Math.PI / 2F), color.x, color.y, color.z);
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 40F, 40, ParticleDust.EnumDustBehavior.GROW, 0.79F);
            ModParticleUtils.annularParticleOutburst(this.level(), 20 * dustSpeed, dustData, pos.x, pos.y + 0.1, pos.z, 1.6F * dustSpeed, 0.5);
            ModParticleUtils.blockParticlesAround(this.level(), getX(), getY(), getZ(), 15, 0.75 * effectScale, 1.5 * effectScale,
                    0.1 * effectScale, 0.2 * effectScale, 0.3, 0.7, -0.2, 0.1, (pos2, state) -> getBlockParticleData(this.random, state, pos, 2));
            int[] particles = {8, 10};
            double[] radii = {2, 3};
            double[] speeds = {1.8, 2.2};
            double[] colors = {1F, 0.89F, 0.64F, 0.6F};
            double[] scales = {0.12, 0.14};
            ModParticleUtils.multiLayerBowlParticles(this.level(), pos, 3, particles, radii, speeds, angles, colors, scales, 0.55F);
        }
    }

    private void doLightBlotEffect(Vec3 start, Vec3 end, int lifespan, float size, float parallelNoise, float spreadFactor, LightningBolt.FadeFunction function) {
        if (!this.level().isClientSide) return;
        LightningBolt bolt = REALMWARDEN_BOLT.color(BOLT_COLORS[this.random.nextInt(2)]).lifespan(lifespan).fadeFunction(function)
                .size(size).parallelNoise(parallelNoise).spreadFactor(spreadFactor).build(start, end, this.random);
        ClientProxy.LIGHTNING_RENDER.update(this, bolt);
    }

    private static AdvancedParticleData getRibbonParticleData(float airDiffusionSpeed, float scale, float r, float g, float b) {
        return AdvancedParticleBase.createParticleData(ParticleInit.ADV_ORB.get(), new ParticleRotation.EulerAngles(0, 0, 0), 0,
                0, 0, 0, 0, airDiffusionSpeed, 3, true, false, new ParticleComponent[]{
                        new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 4, 0, 0, 0, scale, r, g, b, 0.6F, true, true,
                                new ParticleComponent[]{
                                        new PropertyOverLength(EnumRibbonProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0.6F, 0F)),
                                        new PropertyOverLength(EnumRibbonProperty.SCALE, AnimData.KeyTrack.startAndEnd(0.1F, 1F)),
                                }, false)
                }, false);
    }

    private static AdvancedBlockParticleData getBlockParticleData(RandomSource random, BlockState state, Vec3 pos, double scale) {
        return AdvancedBlockParticle.createBlockParticleData(state, null, pos.x, pos.y, pos.z, (random.nextFloat() * 0.5F + 0.5F) * 2 / scale,
                0.6, 0.6, 0.6, 1, 0.98, 40, false, true, new ParticleComponent[]{
                        new ParticleComponent.FreeFallSimulator(2F)
                }
        );
    }

    static abstract class CacheableTargetAnimationAI extends AnimationAI<EntityRealmWarden> {
        @Nullable
        protected LivingEntity target;

        protected CacheableTargetAnimationAI(EntityRealmWarden entity) {
            super(entity);
        }

        @Override
        public void start() {
            super.start();
            target = entity.getTarget();
        }

        @Override
        public void stop() {
            super.stop();
            target = null;
        }

        @Override
        public void tick() {
            if (target == null) target = entity.getTarget();
        }

        protected void lookAtTarget(float yawSpeed) {
            if (target == null) return;
            entity.lookAt(target, yawSpeed, 30F);
            entity.getLookControl().setLookAt(target, yawSpeed, 30F);
        }
    }

    static class RWHeavySwingGoal extends CacheableTargetAnimationAI {
        protected RWHeavySwingGoal(EntityRealmWarden entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == HEAVY_SWING_ANIMATION || animation == DERIVED_HEAVY_SWING_ANIMATION;
        }

        @Override
        public void tick() {
            super.tick();
            int tick = entity.getAnimationTick();
            boolean derived = entity.getAnimation() == DERIVED_HEAVY_SWING_ANIMATION;
            if (tick <= 14) {
                lookAtTarget(30);
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            } else if (tick >= (derived ? 65 : 50)) {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                if (tick > (derived ? 85 : 55)) lookAtTarget(5);
                else entity.setYRot(entity.yRotO);
            } else {
                if (tick < 20) {
                    entity.pounce(tick, 15, 5, true, 2);
                } else if (tick >= 25 && tick <= 30) {
                    lookAtTarget(10);
                    entity.pounce(tick, 22, 8, false, 2);
                } else if (tick > 30 && tick <= 38) {
                    lookAtTarget(15);
                    entity.pounce(tick, 31, 28, true, 2);
                } else if (derived && tick >= 54 && tick < 60) {
                    if (tick < 59) lookAtTarget(20);
                    entity.pounce(tick, 48, 5, false, 3);
                }
            }
        }
    }

    static class RWStompGoal extends CacheableTargetAnimationAI {
        protected RWStompGoal(EntityRealmWarden entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == STOMP_ANIMATION || animation == STOMP_ANIMATION2 || animation == HEAVY_SMASH_ANIMATION;
        }

        @Override
        public void tick() {
            super.tick();
            int tick = entity.getAnimationTick();
            Animation animation = entity.getAnimation();
            if (animation != HEAVY_SMASH_ANIMATION) {
                boolean derived = animation == STOMP_ANIMATION2;
                if (tick <= 10 || tick > (derived ? 17 : 30)) {
                    lookAtTarget(15);
                } else entity.setYRot(entity.yRotO);
                if (derived ? (tick >= 4 && tick < 9) : (tick >= 7 && tick < 12)) {
                    entity.pounce(tick, derived ? 4 : 7, 6, true, 4);
                } else if (tick > (derived ? 13 : 15)) entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            } else {
                if (tick < 17 || tick > 45) lookAtTarget(15);
                else entity.setYRot(entity.yRotO);
                if (tick >= 15 && tick <= 18) {
                    entity.pounce(tick, 15, 8, true, 5);
                } else if (tick > 27) entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            }
        }
    }

    static class RWElbowStrikeGoal extends CacheableTargetAnimationAI {
        protected RWElbowStrikeGoal(EntityRealmWarden entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == ELBOW_STRIKE_ANIMATION || animation == DOUBLE_FIST_SLAM_ANIMATION;
        }

        @Override
        public void tick() {
            super.tick();
            int tick = entity.getAnimationTick();
            boolean derived = entity.getAnimation() == DOUBLE_FIST_SLAM_ANIMATION;
            if ((tick > 5 && tick < (derived ? 14 : 20))) {
                lookAtTarget(derived ? 20 : 30);
            } else if (tick > 30) {
                lookAtTarget(10);
            } else entity.setYRot(entity.yRotO);

            if (derived && tick >= 8 && tick <= 13) {
                entity.pounce(tick, 8, 7, true, 5);
            } else if (!derived || tick > 15) entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
        }
    }

    static class RWSweepGoal extends CacheableTargetAnimationAI {
        private final ControlledAnimation spinAnim = new ControlledAnimation(7);
        private float startYaw;

        public RWSweepGoal(EntityRealmWarden entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == SWEEP_ANIMATION || animation == TURNAROUND_SWEEP_ANIMATION;
        }

        @Override
        public void start() {
            super.start();
            spinAnim.resetTimer();
        }

        @Override
        public void tick() {
            super.tick();
            int tick = entity.getAnimationTick();
            if (entity.getAnimation() == TURNAROUND_SWEEP_ANIMATION) {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                if (tick < 11) {
                    startYaw = entity.getYRot();
                } else if (tick <= 17) {
                    spinAnim.increaseTimer();
                    float targetYaw = startYaw + 160F * spinAnim.getAnimationProgressSinSqrt();
                    entity.setYRot(targetYaw);
                    entity.yBodyRot = entity.yHeadRot = targetYaw;
                    spinAnim.updatePrevTimer();
                }
                return;
            }
            if (tick < 45) lookAtTarget(30);
            else {
                if (tick > 75) lookAtTarget(10);
                else entity.setYRot(entity.yRotO);
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            }
            if (tick > 17 && tick < 27) {
                entity.pounce(tick, 18, 10, true, 3);
            } else if (tick >= 40 && tick < 47) {
                entity.pounce(tick, 40, 12, true, 3);
            }
        }
    }

    static class RWBackstepGoal extends AnimationGroupAI<EntityRealmWarden> {
        protected RWBackstepGoal(EntityRealmWarden entity) {
            super(entity, BACKSTEP_ANIMATION, BACKSTEP_LANDING_ANIMATION);
        }

        @Override
        public void tick() {
            Animation animation = entity.getAnimation();
            if (animation == BACKSTEP_ANIMATION) {
                int tick = entity.getAnimationTick();
                LivingEntity target = entity.getTarget();
                if (tick < 4) {
                    if (target != null) {
                        entity.lookAt(target, 60F, 60F);
                        entity.getLookControl().setLookAt(target, 60F, 60F);
                    }
                } else if (tick == 4) {
                    double desiredDistance = 20;
                    double verticalSpeed = 0.8;
                    double drag = 0.93;
                    double airTicks = 2 * verticalSpeed / entity.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get());
                    double factor = drag * (1 - Math.pow(drag, airTicks)) / (1 - drag);
                    if (factor < 1e-6) factor = 1;
                    double horizontalSpeed = desiredDistance / factor;
                    Vec3 backDir = entity.getLookAngle().scale(-1).normalize();
                    if (target != null && Math.abs(entity.getY() - target.getY()) >= 8) {
                        horizontalSpeed *= 0.1F;
                    }
                    entity.setDeltaMovement(backDir.x * horizontalSpeed, verticalSpeed, backDir.z * horizontalSpeed);
                }
                if (tick > 16) {
                    entity.setYRot(entity.yRotO);
                    nextAnimation(animation, BACKSTEP_LANDING_ANIMATION, entity.onGround() || tick >= animation.getDuration() - 1);
                }
            } else {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                LivingEntity target = entity.getTarget();
                if (target != null) {
                    entity.lookAt(target, 30F, 30F);
                    entity.getLookControl().setLookAt(target, 30F, 30F);
                }
            }
        }
    }

    static class RWJumpSmashGoal extends AnimationGroupAI<EntityRealmWarden> {
        private static final double ARRIVAL_THRESHOLD_SQR = 9;
        private static final double MAX_RANGE = 20;
        private Vec3 targetPosCache;
        private double distanceCache;

        protected RWJumpSmashGoal(EntityRealmWarden entity) {
            super(entity, JUMP_SMASH_START_ANIMATION, JUMP_SMASH_ANIMATION, DERIVED_JUMP_SMASH_ANIMATION);
        }

        @Override
        public void stop() {
            super.stop();
            targetPosCache = null;
            distanceCache = 0;
        }

        @Override
        public void tick() {
            Animation animation = entity.getAnimation();
            int tick = entity.getAnimationTick();
            if (animation == JUMP_SMASH_START_ANIMATION) {
                LivingEntity target = entity.getTarget();
                if (tick < 16) {
                    entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                    if (target != null) {
                        Vec3 start = entity.position();
                        Vec3 toTarget = target.position().subtract(start);
                        double actualRange = Math.min(toTarget.length(), MAX_RANGE);
                        Vec3 direction = toTarget.normalize();
                        targetPosCache = start.add(direction.scale(actualRange));
                        distanceCache = entity.distanceTo(target);
                        entity.lookAt(target, 30F, 30F);
                        entity.getLookControl().setLookAt(target, 30F, 30F);
                    }
                } else {
                    boolean flag = tick == 34;
                    if (targetPosCache != null) {
                        Vec3 toTarget = targetPosCache.subtract(entity.position());
                        double distSqr = toTarget.lengthSqr();
                        if (distSqr <= ARRIVAL_THRESHOLD_SQR || (tick >= 24 && entity.getDeltaMovement().horizontalDistanceSqr() <= 1.0E-3D)) {
                            entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                            targetPosCache = null;
                            distanceCache = 0;
                            flag = tick >= 24;
                        } else {
                            float distanceFactor = Math.max(ModMathUtils.getTickFactor((float) Math.sqrt(distSqr), (float) distanceCache, false), 0.75F);
                            float speedMultiplier = ModMathUtils.calculateSpeedMultiplier(1, distanceFactor, 1F, 3F);
                            Vec3 direction = toTarget.normalize();
                            entity.setDeltaMovement(direction.x * speedMultiplier, entity.getDeltaMovement().y, direction.z * speedMultiplier);
                            entity.setYRot((float) (Math.atan2(direction.z, direction.x) * 180 / Math.PI) - 90);
                        }
                    } else if (tick >= 24) flag = true;
                    nextAnimation(animation, entity.isSecondPhase() ? DERIVED_JUMP_SMASH_ANIMATION : JUMP_SMASH_ANIMATION, flag);
                }
            } else entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
        }
    }

    public enum LeapStage {
        LEAP(0), HOLD(1), DOWN(2);
        private static final IntFunction<LeapStage> BY_ID = ByIdMap.sparse(c -> c.id, values(), LEAP);
        public final int id;

        LeapStage(int id) {
            this.id = id;
        }

        public static LeapStage byId(int id) {
            return BY_ID.apply(id);
        }
    }
}
