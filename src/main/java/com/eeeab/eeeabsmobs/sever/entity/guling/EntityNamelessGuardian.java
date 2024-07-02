package com.eeeab.eeeabsmobs.sever.entity.guling;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.advancements.EMCriteriaTriggers;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.*;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityFallingBlock;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

//创建于 2023/1/17
public class EntityNamelessGuardian extends EntityAbsGuling implements IBoss, GlowEntity, PowerableMob, NeedStopAiEntity {
    public final Animation dieAnimation = Animation.create(51);
    public final Animation roarAnimation = Animation.create(80);
    public final Animation attackAnimation1 = Animation.create(36);
    public final Animation attackAnimation2 = Animation.create(35);
    public final Animation attackAnimation3 = Animation.create(44);
    public final Animation attackAnimation4 = Animation.create(40);
    public final Animation attackAnimation5 = Animation.create(36);
    public final Animation attackAnimation6 = Animation.create(36);
    public final Animation robustAttackAnimation = Animation.create(70);
    public final Animation smashAttackAnimation = Animation.create(40);
    public final Animation pounceAttackAnimation1 = Animation.create(16);
    public final Animation pounceAttackAnimation2 = Animation.createLoop(38);
    public final Animation pounceAttackAnimation3 = Animation.create(18);
    public final Animation activateAnimation = Animation.create(56);
    public final Animation deactivateAnimation = Animation.create(40);
    public final Animation weakAnimation1 = Animation.create(40);
    public final Animation weakAnimation2 = Animation.create(200);
    public final Animation weakAnimation3 = Animation.create(40);
    public final Animation leapAnimation = Animation.create(105);
    public final Animation smashDownAnimation = Animation.create(21);
    public final Animation laserAnimation = Animation.create(120);
    public final Animation concussionAnimation = Animation.create(30);
    public final Animation shakeGroundAttackAnimation1 = Animation.create(60);
    public final Animation shakeGroundAttackAnimation2 = Animation.create(56);
    public final Animation shakeGroundAttackAnimation3 = Animation.create(60);
    private final Animation[] animations = new Animation[]{
            dieAnimation,
            dieAnimation,
            roarAnimation,
            attackAnimation1,
            attackAnimation2,
            attackAnimation3,
            attackAnimation4,
            attackAnimation5,
            attackAnimation6,
            robustAttackAnimation,
            smashAttackAnimation,
            pounceAttackAnimation1,
            pounceAttackAnimation2,
            pounceAttackAnimation3,
            activateAnimation,
            deactivateAnimation,
            weakAnimation1,
            weakAnimation2,
            weakAnimation3,
            leapAnimation,
            smashDownAnimation,
            laserAnimation,
            concussionAnimation,
            shakeGroundAttackAnimation1,
            shakeGroundAttackAnimation2,
            shakeGroundAttackAnimation3
    };

    private final EMLookAtGoal lookAtPlayerGoal = new EMLookAtGoal(this, Player.class, 8.0F);
    private final WaterAvoidingRandomStrollGoal waterAvoidingRandomStrollGoal = new WaterAvoidingRandomStrollGoal(this, 1.0D);
    private final RandomLookAroundGoal randomLookAroundGoal = new RandomLookAroundGoal(this);
    private static final EntityDataAccessor<Boolean> DATA_POWER = SynchedEntityData.defineId(EntityNamelessGuardian.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityNamelessGuardian.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_UNNATURAL = SynchedEntityData.defineId(EntityNamelessGuardian.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_REST_POSITION = SynchedEntityData.defineId(EntityNamelessGuardian.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private int madnessTick;
    private int nextMadnessTick;
    private int pounceTick;
    private int smashTick;
    private int leapTick;
    private int laserTick;
    private int shakeGroundTick;
    private int robustTick;
    private int guardianInvulnerableTime;
    //用来判断因为特殊情况导致部分技能无法释放,用于计算时长
    private int noUseSkillFromLongTick;
    private boolean executeWeak;
    private boolean shouldUseSkill;
    //判断是否是首次进入强化状态
    private boolean fmFlag = true;
    private int attackTick;
    //BGM高潮部分的时长
    public static final int MADNESS_TICK = 1300;
    public static final int NEVER_STOP = -1;
    private static final int USE_SKILL_TIME_OUT_MAX_LIMIT = 300;
    private final static int NEXT_MADNESS_TICK = 600;
    private final static int MAX_SMASH_ATTACK_TICK = 300;
    private final static int MIN_SMASH_ATTACK_TICK = 200;
    private final static int MAX_POUNCE_ATTACK_TICK = 400;
    private final static int MIN_POUNCE_ATTACK_TICK = 200;
    private final static int MAX_LEAP_TICK = 500;
    private final static int MIN_LEAP_TICK = 300;
    private final static int MAX_LASER_ATTACK_TICK = 400;
    private final static int MIN_LASER_ATTACK_TICK = 300;
    private final static int SHAKE_GROUND_ATTACK_TICK = 400;
    private final static int ROBUST_ATTACK_TICK = 650;
    private final static int WEAK_STATE_TICK = 160;
    private final static int HARD_MODE_STATE_TICK = 80;
    private final EntityNamelessGuardianPart core;
    private final EntityNamelessGuardianPart[] subEntities;
    private final DynamicGameEventListener<EntityNamelessGuardian.Listener> dynamicListener;
    private static final float[][] ROBUST_ATTACK_BLOCK_OFFSETS = {
            {-0.5F, -0.5F},
            {-0.5F, 0.5F},
            {0.5F, 0.5F},
            {0.5F, -0.5F}
    };

    public final ControlledAnimation coreControlled = new ControlledAnimation(10);
    public final ControlledAnimation explodeControlled = new ControlledAnimation(30);
    public final ControlledAnimation accumulationControlled = new ControlledAnimation(10);

    public EntityNamelessGuardian(EntityType<? extends EntityNamelessGuardian> type, Level level) {
        super(type, level);
        this.active = false;
        this.core = new EntityNamelessGuardianPart(this, "core", 0.6F, 0.6F);
        this.subEntities = new EntityNamelessGuardianPart[]{this.core};
        this.dynamicListener = new DynamicGameEventListener<>(new Listener());
        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);
        this.canplayHurtAnimation = false;
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_BOSS;
    }

    @Override
    public float getStepHeight() {
        return 1.5F;
    }

    @Override//可以站立的流体
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.LAVA);
    }

    @Override//是否免疫摔伤
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override//添加药水效果
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()) && super.addEffect(effectInstance, entity);
    }

    @Override//强制添加药水效果
    public void forceAddEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()))
            super.forceAddEffect(effectInstance, entity);
    }

    @Override//添加效果时额外条件
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()) && super.canBeAffected(effectInstance);
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

    @Override//被方块阻塞
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive() && !this.isActive();
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.95F;
    }

    @Override
    @NotNull
    protected BodyRotationControl createBodyControl() {
        return new EMBodyRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new EMPathNavigateGround(this, level);
    }


    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.GULING.NAMELESS_GUARDIAN.combatConfig;
    }

    @Override
    protected boolean showBossBloodBars() {
        return EMConfigHandler.COMMON.OTHER.enableShowBloodBars.get();
    }

    @Override
    protected BossEvent.BossBarColor bossBloodBarsColor() {
        return BossEvent.BossBarColor.BLUE;
    }

    @Override
    protected boolean setDarkenScreen() {
        return this.isPowered();
    }

    @Override
    public int getMaxHeadXRot() {
        return 30;
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return !(this.getAnimation() == this.leapAnimation || this.getAnimation() == this.smashDownAnimation);
    }

    @Override
    protected void onAnimationStart(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.weakAnimation1) {
                this.setExecuteWeak(true);
                this.setPowered(false);
                int duration = Difficulty.HARD.equals(this.level().getDifficulty()) ? HARD_MODE_STATE_TICK : WEAK_STATE_TICK;
                this.setNextMadnessTick(NEXT_MADNESS_TICK + duration);
                this.resetTimeOutToUseSkill();
            } else if (animation == this.pounceAttackAnimation1 || animation == this.laserAnimation) {
                this.resetTimeOutToUseSkill();
            }
        }
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.deactivateAnimation || animation == this.activateAnimation) {
                this.fmFlag = true;
                this.attackTick = this.madnessTick = this.guardianInvulnerableTime = 0;
                this.setExecuteWeak(false);
                this.resetTimeOutToUseSkill();
            } else if (animation == this.roarAnimation) {
                this.setMadnessTick(this.isChallengeMode() ? NEVER_STOP : MADNESS_TICK);
                this.setRobustTick();
                if (this.isFirstMadness() && !this.isChallengeMode()) this.fmFlag = false;
                this.laserTick = this.pounceTick = this.smashTick = this.attackTick = 0;
                this.removeAllEffects();
            } else if (animation == this.weakAnimation3) {
                this.setExecuteWeak(false);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, EntityAbsGuling.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new GuardianCombo1Goal(this, 4F, 100F));
        this.goalSelector.addGoal(1, new GuardianCombo2Goal(this, 4.5F, 100F));
        this.goalSelector.addGoal(1, new GuardianLobedAttackGoal(this, () -> smashAttackAnimation));
        this.goalSelector.addGoal(1, new GuardianPounceAttackGoal(this, 3F));
        this.goalSelector.addGoal(1, new GuardianRobustAttackGoal(this, () -> robustAttackAnimation));
        this.goalSelector.addGoal(1, new GuardianShakeGroundAttackGoal(this));
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> activateAnimation));
        this.goalSelector.addGoal(1, new AnimationDeactivate<>(this, () -> deactivateAnimation));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> roarAnimation));
        this.goalSelector.addGoal(1, new AnimationAI<>(this) {

            @Override
            protected boolean test(Animation animation) {
                return animation == entity.weakAnimation1 || animation == entity.weakAnimation2 || animation == entity.weakAnimation3;
            }

            @Override
            public void tick() {
                setDeltaMovement(0, getDeltaMovement().y, 0);
                if (entity.getAnimation() == entity.weakAnimation1) {
                    if (entity.getAnimationTick() == entity.weakAnimation1.getDuration() - 1) {
                        entity.playAnimation(entity.weakAnimation2);
                    }
                } else if (entity.getAnimation() == entity.weakAnimation2) {
                    int duration = Difficulty.HARD.equals(entity.level().getDifficulty()) ? HARD_MODE_STATE_TICK : WEAK_STATE_TICK;
                    if (entity.getAnimationTick() == duration) {
                        entity.playAnimation(entity.weakAnimation3);
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new GuardianLeapGoal(this, () -> leapAnimation));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> smashDownAnimation) {
            @Override
            public void tick() {
                int tick = entity.getAnimationTick();
                if (tick >= 2 && tick < 6) {
                    entity.shockAttack(entity.damageSources().mobAttack(entity), tick + 1, 1.5F, 2F, 0F, 0.02F, 0.5F, (entity.isPowered() ? 0.8F : 0.6F), false, true, false);
                    if (tick == 2) {
                        entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1F + entity.getRandom().nextFloat() * 0.1F);
                        EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.125F, 8, 17);
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new GuardianShootLaserGoal(this, () -> laserAnimation));
        this.goalSelector.addGoal(2, new AnimationRepel<>(this, () -> concussionAnimation, 6.5F, 8, 2.5F, 0.2F, true));
        this.goalSelector.addGoal(2, new GuardianAIGoal(this));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return ((!this.isActive() || animation == this.activateAnimation || animation == this.deactivateAnimation || animation == this.roarAnimation || animation == this.weakAnimation1 || animation == this.weakAnimation3)) || super.isInvulnerableTo(damageSource);
    }

    @Override
    protected void pushEntitiesAway(float X, float Y, float Z, float radius) {
        List<LivingEntity> entityList = getNearByLivingEntities(X, Y, Z, radius);
        for (Entity entity : entityList) {
            double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
            entity.setDeltaMovement(-0.10 * Math.cos(angle), entity.getDeltaMovement().y, -0.10 * Math.sin(angle));
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.floatGuardian();
        this.coreControlled.updatePrevTimer();
        this.explodeControlled.updatePrevTimer();
        this.accumulationControlled.updatePrevTimer();

        EMAnimationHandler.INSTANCE.updateAnimations(this);

        if (!this.level().isClientSide) {
            if (this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);

            if (this.getTarget() != null && this.isActive() && !this.isPowered() && this.noConflictingTasks() && (this.isChallengeMode() || (this.isFirstMadness() && this.getHealthPercentage() <= 60) || (!this.isFirstMadness() && this.getNextMadnessTick() <= 0))) {
                this.removeAllEffects();
                this.setNoAi(false);
                this.playAnimation(this.roarAnimation);
            }

            if (this.isUnnatural()) {
                this.setActive(true);
                this.active = true;
            } else if (this.noConflictingTasks() && !this.isNoAi()) {
                if (this.isActive()) {
                    if (this.getTarget() == null && !this.isPowered() && zza == 0 && this.isAtRestPos()) {
                        this.playAnimation(this.deactivateAnimation);
                        this.setActive(false);
                    }
                } else if (this.getTarget() != null && this.targetDistance <= 5) {
                    this.playAnimation(this.activateAnimation);
                    this.setActive(true);
                }
            }

            if (!this.isUnnatural()) {
                if (this.noConflictingTasks() && this.getTarget() == null && this.getNavigation().isDone() && !this.isAtRestPos() && this.isActive()) {
                    this.moveToRestPos();
                }
            }

            if (!this.active && this.getAnimation() != this.activateAnimation) {
                if (EMConfigHandler.COMMON.MOB.GULING.NAMELESS_GUARDIAN.enableNonCombatHeal.get()) this.heal(0.5F);
            }

            if (this.getTarget() != null && this.active && this.isTimeOutToUseSkill() && !this.shouldUseSkill && this.isNoAnimation() && !this.isNoAi()) {
                this.playAnimation(this.concussionAnimation);
                this.shouldUseSkill = true;
            }
        }

        this.pushEntitiesAway(1.7F, getBbHeight(), 1.7F, 1.7F);

        if (!this.isActive()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }

        if (this.getAnimation() != this.getNoAnimation()
                && this.getAnimation() != this.pounceAttackAnimation1
                && this.getAnimation() != this.pounceAttackAnimation2
                && this.getAnimation() != this.pounceAttackAnimation3
                && this.getAnimation() != this.roarAnimation
                && this.getAnimation() != this.laserAnimation
                || !this.isActive()) {
            this.yHeadRot = this.yBodyRot = this.getYRot();
        }

        int tick = this.getAnimationTick();
        if (this.getAnimation() == this.dieAnimation) {
            this.doExplodeEffect();
        } else if (this.getAnimation() == this.roarAnimation) {
            this.setPowered(true);
            this.doRoarEffect();
        } else if (this.getAnimation() == this.concussionAnimation) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            if (tick == 8) {
                ModParticleUtils.sphericalParticleOutburst(level(), 10F, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME, ParticleInit.GUARDIAN_SPARK.get()}, this, 2.5F, 0, 0, 3);
                EntityCameraShake.cameraShake(level(), position(), 30, 0.125F, 0, 20);
            } else if (tick > 8) {
                this.strongKnockBlock();
            }
            if (this.getTarget() != null) this.lookAt(getTarget(), 30F, 30F);
        } else if (this.getAnimation() == this.attackAnimation6) {
            if (tick == 14) this.doSplashParticlesEffect(20);
        } else if (this.getAnimation() == this.shakeGroundAttackAnimation1) {
            if (tick == 24) this.doSplashParticlesEffect(25);
        } else if (this.getAnimation() == this.shakeGroundAttackAnimation2) {
            if (tick == 17) this.doSplashParticlesEffect(20);
        } else if (this.getAnimation() == this.shakeGroundAttackAnimation3) {
            if (tick == 26) this.doSplashParticlesEffect(30);
        } else if (this.getAnimation() == this.robustAttackAnimation) {
            if (tick < 35) {
                this.preRobustAttack();
            } else if (tick == 35) {
                this.doSplashParticlesEffect(30);
            }
        } else if (this.getAnimation() == this.smashAttackAnimation) {
            this.doSmashEffect();
        } else if (this.getAnimation() == this.pounceAttackAnimation2) {
            this.doWalkEffect(10);
            if (tick > 2) this.doBreakAirEffect();
        } else if (this.getAnimation() == this.weakAnimation2) {
            if (this.level().isClientSide && tick % 5 == 0) {
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY() - 1.5F, this.getRandomZ(0.5D), -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D);
            }
        } else if (this.getAnimation() == this.leapAnimation && tick == 13) {
            if (!this.level().isClientSide) this.level().broadcastEntityEvent(this, (byte) 7);
        } else if (this.getAnimation() == this.smashDownAnimation && tick == 4) {
            if (!this.level().isClientSide) this.level().broadcastEntityEvent(this, (byte) 7);
        } else if (this.getAnimation() == this.activateAnimation) {
            LivingEntity target = getTarget();
            if (target != null && tick > 40) {
                this.lookAt(target, 30F, 30F);
            }
        }

        if (this.isPowered()) {
            if (this.isPassenger()) this.stopRiding();
            if (this.isNoAi()) this.setNoAi(false);
            if (this.level().isClientSide && this.tickCount % 10 == 0) {
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), this.getRandomX(0.7D), this.getY() + 1F, this.getRandomZ(0.7D), 0.0D, 0.07D, 0.0D);
            }
        }

        if (this.isNoAnimation() && this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F
                && this.random.nextInt(5) == 0) {
            this.doWalkEffect(1);
        }

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);

        if (this.level().isClientSide && speed > 0.05 && this.isActive() && !this.isSilent()) {
            if (this.frame % 22 == 1 && this.isNoAnimation()) {
                this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.NAMELESS_GUARDIAN_STEP.get(), this.getSoundSource(), 1F, 1.05F, false);
            }
            if (this.frame % 5 == 1 && this.getAnimation() == this.pounceAttackAnimation2) {
                this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.NAMELESS_GUARDIAN_STEP.get(), this.getSoundSource(), 1F, 1.05F, false);
            }
        }
    }

    private void strongKnockBlock() {
        List<Entity> entities = getNearByEntities(Entity.class, 8, 8, 8, 8);
        double mx = 0, my = 0;
        for (Entity entity : entities) {
            if (entity instanceof Projectile) {
                double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                mx = -Math.cos(angle);
                my = -Math.sin(angle);
            } else if (entity instanceof LivingEntity) {
                if (entity == this) continue;
                if (entity instanceof Player player && player.isCreative()) continue;
                double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                double distance = distanceTo(entity) - 4;
                mx = Math.min(1 / (distance * distance), 1) * -1 * Math.cos(angle);
                my = Math.min(1 / (distance * distance), 1) * -1 * Math.sin(angle);
            }
            entity.setDeltaMovement(entity.getDeltaMovement().add(mx, 0, my));
        }
    }

    public float getExplodeCoefficient(float partialTicks) {
        return Mth.lerp(partialTicks, (float) this.explodeControlled.getPrevTimer(), (float) this.explodeControlled.getTimer()) / (float) (this.explodeControlled.getDuration() - 2);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 6) {
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24f, 0.24f, 0.24f, 40f, 20, ParticleDust.EnumDustBehavior.GROW, 1.0f);
            ModParticleUtils.annularParticleOutburst(level(), 15, new ParticleOptions[]{dustData}, getX(), getY(), getZ(), 0.9, 0.5);
            ModParticleUtils.annularParticleOutburst(level(), 15, new ParticleOptions[]{dustData}, getX(), getY(), getZ(), 0.6, 0.5);
        } else if (id == 7) {
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24f, 0.24f, 0.24f, 40f, 25, ParticleDust.EnumDustBehavior.SHRINK, 1.0f);
            ModParticleUtils.annularParticleOutburst(level(), 15, new ParticleOptions[]{dustData}, getX(), this.getY(), getZ(), 0.8F, 0.1);
        } else if (id == 8) {
            ModParticleUtils.roundParticleOutburst(level(), 200, new ParticleOptions[]{ParticleTypes.LARGE_SMOKE, ParticleTypes.SMOKE, ParticleTypes.EXPLOSION}, getX(), this.getY(0.1), getZ(), 1);
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void makePoofParticles() {
        if (this.isPowered()) {
            for (int i = 0; i < 10; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), this.getRandomX(1.4D), this.getRandomY(), this.getRandomZ(1.4D), d0, d1, d2);
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(1.2D), this.getRandomY(), this.getRandomZ(1.2D), d0, d1, d2);
            }
        } else {
            super.makePoofParticles();
        }
    }

    private void floatGuardian() {
        if (this.isInLava()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.1D, 0.0D));
            }
        }
    }


    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.guardianInvulnerableTime > 0) {
                this.guardianInvulnerableTime--;
            }
            if (this.pounceTick > 0) {
                this.pounceTick--;
            }

            if (this.smashTick > 0) {
                this.smashTick--;
            }

            if (this.leapTick > 0) {
                this.leapTick--;
            }

            if (this.laserTick > 0) {
                this.laserTick--;
            }

            if (this.shakeGroundTick > 0) {
                this.shakeGroundTick--;
            }

            if (this.attackTick > 0 && this.isNoAnimation()) {
                this.attackTick--;
            }

            if (this.madnessTick > 0 && this.getTarget() != null && this.getAnimation() != this.leapAnimation) {
                this.madnessTick--;
            }

            if (this.nextMadnessTick > 0) {
                this.nextMadnessTick--;
            }

            if (this.robustTick > 0) {
                this.robustTick--;
            }

            if (this.active && this.isPowered() && getTarget() != null && this.targetDistance < 6.0f) {
                if (this.laserTick <= 0) {
                    this.noUseSkillFromLongTick++;
                }
                if (this.pounceTick <= 0) {
                    this.noUseSkillFromLongTick++;
                }
            }
        }

        this.coreControlled.incrementOrDecreaseTimer(this.isGlow());

        int tick = this.getAnimationTick();
        this.accumulationControlled.incrementOrDecreaseTimer(this.getAnimation() == this.laserAnimation && tick > 8 && tick < 99);

        //并无实际用途,仅作为测试 复制自EndDragon
        if (!this.isNoAi()) {
            float arc = this.yBodyRot * ((float) Math.PI / 180F);
            float fx = Mth.sin(arc) * (1.0F - Math.abs(this.getXRot() / 90F));
            float fx2 = Mth.cos(arc) * (1.0F - Math.abs(this.getXRot() / 90F));

            Vec3[] avec3 = new Vec3[this.subEntities.length];

            for (int j = 0; j < this.subEntities.length; ++j) {
                avec3[j] = new Vec3(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
            }

            this.tickPart(this.core, (fx * -0.8F), 2.6F, (-fx2 * -0.8F));

            //遍历所有碰撞箱
            for (int l = 0; l < this.subEntities.length; ++l) {
                this.subEntities[l].xo = avec3[l].x;
                this.subEntities[l].yo = avec3[l].y;
                this.subEntities[l].zo = avec3[l].z;
                this.subEntities[l].xOld = avec3[l].x;
                this.subEntities[l].yOld = avec3[l].y;
                this.subEntities[l].zOld = avec3[l].z;
            }
        }
    }


    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!this.level().isClientSide/* 在服务端进行判断 */) {
            Entity entity = source.getEntity();
            if ((!active || getTarget() == null) && entity instanceof LivingEntity livingEntity
                    && !(livingEntity instanceof Player player && player.isCreative() || this.level().getDifficulty() == Difficulty.PEACEFUL)
                    && (!EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get() || !(livingEntity instanceof EntityAbsGuling))) {
                this.setLastHurtByMob(livingEntity);//使得可以有多个仇恨目标
            }
            if (this.guardianInvulnerableTime > 0) {
                return false;
            } else if (entity != null) {
                if (this.isPowered()) {
                    if (this.guardianInvulnerableTime <= 0) guardianInvulnerableTime = 20 /*不能小于等于10*/;
                    if (ModEntityUtils.isProjectileSource(source)) return false;
                }
                damage = Math.min(damage, EMConfigHandler.COMMON.MOB.GULING.NAMELESS_GUARDIAN.maximumDamageCap.damageCap.get().floatValue());
                return super.hurt(source, damage);
            } else if (source.is(EMTagKey.GENERAL_UNRESISTANT_TO)) {
                return super.hurt(source, damage);
            }
        }
        return false;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
        super.thunderHit(level, lightningBolt);
        if (!this.isPowered()) {
            if (!this.isChallengeMode()) fmFlag = false;
            this.setNextMadnessTick(0);
        } else {
            this.heal(this.getMaxHealth() * 0.01F);
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(source, pLooting, pRecentlyHit);
        Entity entity = source.getEntity();
        if (entity != null) {
            ItemEntity itementity = this.spawnAtLocation(ItemInit.GUARDIAN_CORE.get());
            if (itementity != null) {
                itementity.setExtendedLifetime();
                itementity.setGlowingTag(true);
            }
            if (this.isChallengeMode() || this.random.nextBoolean()) {
                itementity = this.spawnAtLocation(ItemInit.GUARDIAN_AXE.get());
                if (itementity != null) {
                    itementity.setExtendedLifetime();
                    itementity.setGlowingTag(true);
                }
            }
        }
    }

    public boolean checkCanAttackRange(double checkRange, double range) {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return true;
        } else if (target.isAlive()) {
            Vec3 betweenEntitiesVec = this.position().subtract(target.position());
            boolean targetComingCloser = target.getDeltaMovement().dot(betweenEntitiesVec) > 0 && target.getDeltaMovement().lengthSqr() > 0.015;
            return this.targetDistance < checkRange + range || (this.targetDistance < range + 5 + checkRange && targetComingCloser);
        }
        return false;
    }

    private boolean isAtRestPos() {
        Optional<BlockPos> restPos = getRestPos();
        return restPos.filter(blockPos -> blockPos.distSqr(blockPosition()) < 9).isPresent();
    }

    private void moveToRestPos() {
        if (getRestPos().isPresent()) {
            BlockPos pos = this.getRestPos().get();
            getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_POWER, false);
        this.entityData.define(DATA_IS_UNNATURAL, false);
        this.entityData.define(DATA_REST_POSITION, Optional.empty());
        this.entityData.define(DATA_ACTIVE, true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("spawnPos")) {
            this.setRestPos(NbtUtils.readBlockPos(compound.getCompound("spawnPos")));
        }
        this.fmFlag = compound.getBoolean("fmFlag");
        this.setActive(compound.getBoolean("isActive"));
        this.setPowered(compound.getBoolean("power"));
        this.setUnnatural(compound.getBoolean("isUnnatural"));
        this.setMadnessTick(compound.getInt("madnessCountdownTick"));
        this.setNextMadnessTick(compound.getInt("nextMadnessTick"));
        active = isActive();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.getRestPos().ifPresent(spawnPos -> compound.put("spawnPos", NbtUtils.writeBlockPos(spawnPos)));
        if (!this.isChallengeMode() && this.madnessTick != NEVER_STOP) {
            compound.putBoolean("power", this.entityData.get(DATA_POWER));
            compound.putInt("madnessCountdownTick", this.madnessTick);
        }
        compound.putBoolean("fmFlag", this.fmFlag);
        compound.putBoolean("isUnnatural", this.entityData.get(DATA_IS_UNNATURAL));
        compound.putBoolean("isActive", this.entityData.get(DATA_ACTIVE));
        compound.putInt("nextMadnessTick", this.nextMadnessTick);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 350.0D).
                add(Attributes.ARMOR, 10.0D).
                add(Attributes.ATTACK_DAMAGE, 15.0D).
                add(Attributes.FOLLOW_RANGE, 50.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compoundTag) {
        this.setUnnatural(spawnType == MobSpawnType.SPAWN_EGG);
        this.setRestPos(this.blockPosition());
        return groupData;
    }

    public float getAttackDamageAttributeValue() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
    }

    @Override
    public boolean noConflictingTasks() {
        return !this.executeWeak && this.isNoAnimation();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);//TODO 未来可能添加激活道具?
    }

    static class GuardianAIGoal extends Goal {
        private final EntityNamelessGuardian guardian;
        private final RandomSource random;
        private boolean isPowered;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int rePath;

        public GuardianAIGoal(EntityNamelessGuardian guardian) {
            this.guardian = guardian;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
            random = guardian.getRandom();
        }


        @Override
        public void start() {
            super.start();
            this.isPowered = this.guardian.isPowered();
            this.rePath = 0;
        }

        @Override
        public boolean canUse() {
            return this.guardian.getTarget() != null && this.guardian.getTarget().isAlive() && this.guardian.isActive() && this.guardian.noConflictingTasks();
        }

        @Override
        public void stop() {
            this.guardian.getNavigation().stop();
            this.isPowered = false;
        }

        @Override
        public void tick() {
            LivingEntity target = this.guardian.getTarget();
            if (target == null) return;
            double dist = this.guardian.distanceToSqr(this.targetX, this.targetY, this.targetZ);
            this.guardian.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (--this.rePath <= 0 && (
                    this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                            target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                    this.guardian.getNavigation().isDone()
            ) {
                this.targetX = target.getX();
                this.targetY = target.getY();
                this.targetZ = target.getZ();
                this.rePath = 4 + this.guardian.getRandom().nextInt(7);
                if (dist > 1024D) {
                    this.rePath += 10;
                } else if (dist > 256D) {
                    this.rePath += 5;
                }
                if (!this.guardian.getNavigation().moveTo(target, 1.0D)) {
                    this.rePath += 15;
                }
            }
            dist = this.guardian.distanceToSqr(this.targetX, this.targetY, this.targetZ);
            if (this.guardian.attackTick <= 0 && this.guardian.getSensing().hasLineOfSight(target)) {
                boolean checkAttackHeight = target.getY() - guardian.getY() < 4 && target.getY() - this.guardian.getY() > -4;
                double entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this.guardian, target);
                boolean canRobust = dist <= 144D && this.isPowered && (this.guardian.getMadnessTick() == 0 || this.guardian.isChallengeMode() && this.guardian.getRobustTick() == 0);
                boolean canSmash = (checkAttackHeight || target.onGround()) && (this.random.nextFloat() < 0.6F && dist <= (this.isPowered ? 50D : 25D) && this.guardian.getSmashTick() <= 0);
                if (dist < 25D && !canSmash && !canRobust) {
                    Animation attackAnimation = this.random.nextBoolean() ? this.guardian.attackAnimation1 : this.guardian.attackAnimation4;
                    this.guardian.playAnimation(attackAnimation);
                } else if (canRobust) {
                    this.guardian.playAnimation(this.guardian.robustAttackAnimation);
                    this.guardian.setRobustTick();
                } else if (canSmash) {
                    this.guardian.playAnimation(this.guardian.smashAttackAnimation);
                    this.guardian.setSmashTick(this.guardian.getCoolingTimerUtil(MAX_SMASH_ATTACK_TICK, MIN_SMASH_ATTACK_TICK, 0.2F));
                }

                boolean canLaser = this.random.nextFloat() < 0.6F && this.isPowered && checkModeOrPreventTimeouts(120) && (((checkAttackHeight ? this.guardian.targetDistance > 10.0D : this.guardian.targetDistance > 4.0D) && (entityRelativeAngle < 60.0 || entityRelativeAngle > 300) && this.guardian.targetDistance < EntityGuardianLaser.GUARDIAN_RADIUS && this.guardian.getLaserTick() <= 0) || this.guardian.isTimeOutToUseSkill());
                boolean canShakeGround = (checkAttackHeight || target.onGround()) && !this.guardian.isTimeOutToUseSkill() && this.random.nextFloat() < 0.6F && (this.guardian.getHealthPercentage() <= 75 || !this.guardian.isFirstMadness()) && this.guardian.targetDistance < 6.0D && this.guardian.getShakeGroundTick() <= 0 && checkModeOrPreventTimeouts(180);
                boolean canLeap = this.random.nextFloat() < 0.6F && this.guardian.targetDistance > 12.0D && this.guardian.targetDistance < 24.0 && this.guardian.getLeapTick() <= 0 && checkModeOrPreventTimeouts(100);
                boolean canPouch = checkAttackHeight && checkModeOrPreventTimeouts(80) && (this.random.nextFloat() < 0.6F && this.guardian.targetDistance > 6.0D && this.guardian.targetDistance < 14.0 && this.guardian.getPounceTick() <= 0 || this.guardian.isTimeOutToUseSkill());
                if (canShakeGround) {
                    this.guardian.playAnimation(this.guardian.shakeGroundAttackAnimation1);
                    this.guardian.setShakeGroundTick(SHAKE_GROUND_ATTACK_TICK);
                } else if (canLaser) {
                    this.guardian.playAnimation(this.guardian.laserAnimation);
                    this.guardian.setLaserTick(this.guardian.getCoolingTimerUtil(MAX_LASER_ATTACK_TICK, MIN_LASER_ATTACK_TICK, 0.5F));
                } else if (canPouch) {
                    this.guardian.playAnimation(this.guardian.pounceAttackAnimation1);
                    this.guardian.setPounceTick(this.guardian.getCoolingTimerUtil(MAX_POUNCE_ATTACK_TICK, MIN_POUNCE_ATTACK_TICK, 0.5F));
                } else if (canLeap) {
                    this.guardian.playAnimation(this.guardian.leapAnimation);
                    this.guardian.setLeapTick(this.guardian.getCoolingTimerUtil(MAX_LEAP_TICK, MIN_LEAP_TICK, 0.2F));
                }
            }
        }

        private boolean checkModeOrPreventTimeouts(int tick) {
            return this.guardian.isChallengeMode() || this.guardian.getMadnessTick() > tick || !this.guardian.isPowered();
        }
    }

    private void preRobustAttack() {
        List<Entity> entities = getNearByEntities(Entity.class, 16, 16, 16, 16);
        for (Entity inRangeEntity : entities) {
            if (inRangeEntity instanceof Player player && player.getAbilities().invulnerable) continue;
            if (!(inRangeEntity instanceof LivingEntity)) continue;
            Vec3 diff = inRangeEntity.position().subtract(this.position().add(Math.cos(Math.toRadians(this.yBodyRot + 90)) * 3.2F, 0, Math.sin(Math.toRadians(this.yBodyRot + 90)) * 3.2F));
            diff = diff.normalize().scale(0.08);
            inRangeEntity.setDeltaMovement(inRangeEntity.getDeltaMovement().subtract(diff));
            if (inRangeEntity.getY() > this.getY() + 3) {
                inRangeEntity.setDeltaMovement(inRangeEntity.getDeltaMovement().subtract(0, 0.06, 0));
            }
        }
    }

    /**
     * 无名守卫者通用撼地攻击
     *
     * @param damageSource         伤害源
     * @param distance             距离
     * @param maxFallingDistance   最大y轴起伏
     * @param spreadArc            攻击角度
     * @param offset               前后偏移
     * @param hitEntityMaxHealth   目标最大生命百分比
     * @param baseDamageMultiplier 基础伤害乘数
     * @param damageMultiplier     总伤害乘数
     * @param disableShield        是否禁用盾牌
     * @param randomOffset         是否生成方块随机y轴偏移
     * @param continuous           是否在同一时刻发生
     */
    public void shockAttack(DamageSource damageSource, int distance, float maxFallingDistance, double spreadArc, double offset, float hitEntityMaxHealth,
                            float baseDamageMultiplier, float damageMultiplier, boolean disableShield, boolean randomOffset, boolean continuous) {
        ServerLevel level = (ServerLevel) this.level();
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double spread = Math.PI * spreadArc;
        int arcLen = Mth.ceil(distance * spread);
        double minY = this.getBoundingBox().minY - 2D;
        double maxY = this.getBoundingBox().maxY;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = this.getX() + vx * distance + offset * Math.cos((double) (this.yBodyRot + 90.0F) * Math.PI / 180.0D);
            double pz = this.getZ() + vz * distance + offset * Math.sin((double) (this.yBodyRot + 90.0F) * Math.PI / 180.0D);
            AABB aabb = new AABB(px - 1.5D, minY, pz - 1.5D, px + 1.5D, maxY, pz + 1.5D);
            List<Entity> entities = level().getEntitiesOfClass(Entity.class, aabb);
            float factor = 1F - ((float) distance / 2F - 2F) / maxFallingDistance;
            for (Entity hit : entities) {
                if (hit.onGround()) {
                    if (hit == this || hit instanceof EntityFallingBlock) {
                        continue;
                    }
                    if (hit instanceof LivingEntity livingEntity) {
                        this.guardianHurtTarget(damageSource, this, livingEntity, hitEntityMaxHealth, baseDamageMultiplier, damageMultiplier, false, disableShield, false);
                    }
                    double magnitude = level().random.nextGaussian() * 0.15F + 0.1F;
                    double angle = this.getAngleBetweenEntities(this, hit);
                    double x1 = Math.cos(Math.toRadians(angle - 90));
                    double z1 = Math.sin(Math.toRadians(angle - 90));
                    float x = 0F, y = 0F, z = 0F;
                    x += (float) (x1 * magnitude * 0.15);
                    y += (float) (0.1 + factor * 0.15) * 0.5F;
                    z += (float) (z1 * magnitude * 0.15);
                    if (hit instanceof ServerPlayer) {
                        ((ServerPlayer) hit).connection.send(new ClientboundSetEntityMotionPacket(hit));
                    }
                    if (continuous) y *= 0.5F;
                    hit.setDeltaMovement(hit.getDeltaMovement().add(x, y, z));
                }
            }
            if (continuous || this.getRandom().nextBoolean()) {
                int hitX = Mth.floor(px);
                int hitZ = Mth.floor(pz);
                BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                if (randomOffset) {
                    ModEntityUtils.spawnFallingBlockByPos(level, pos, factor);
                } else {
                    ModEntityUtils.spawnFallingBlockByPos(level, pos);
                }
            }
        }
    }

    private void doRoarEffect() {
        int tick = this.getAnimationTick();
        if (tick > 10 && this.getTarget() != null) {
            this.getLookControl().setLookAt(this.getTarget(), 30F, 30F);
        }
        if (tick == 1) {
            if (!this.isSilent())
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundInit.NAMELESS_GUARDIAN_ACCUMULATING.get(), this.getSoundSource(), 2F, 3.5F, false);
        } else if (tick >= 30 && tick < 70) {
            if (tick == 32) {
                ModParticleUtils.sphericalParticleOutburst(level(), 10F, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME, ParticleInit.GUARDIAN_SPARK.get()}, this, 2.5F, 0, 0, 3);
                if (!this.isSilent())
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.TOTEM_USE, this.getSoundSource(), 1F, 0.95F, false);
                this.playSound(SoundInit.NAMELESS_GUARDIAN_MADNESS.get(), 1.5F, 0.92F);
                EntityCameraShake.cameraShake(level(), position(), 20, 0.125F, 20, 20);
            }
            this.strongKnockBlock();
            if (!this.level().isClientSide && tick % 10 == 0) this.level().broadcastEntityEvent(this, (byte) 6);
        }
    }

    private void doSmashEffect() {
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int tick = this.getAnimationTick();
        int maxDistance = 6;
        if (tick >= 20 && tick < 33) {
            if (tick == 22) {
                ModParticleUtils.annularParticleOutburstOnGround(level(), this.isPowered() ? ParticleInit.GUARDIAN_SPARK.get() : ParticleTypes.SMOKE, this, 12, 8, 0.1, 0.8, -0.5, this.isPowered() ? 0.075 : 0.065);
            }
            if (tick % 2 == 0) {
                tick -= this.isPowered() ? 7 : 11;
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = Mth.ceil(distance * spread);
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = getX() + vx * distance;
                    double pz = getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    if (this.random.nextBoolean()) {
                        int amount = this.random.nextInt(3);
                        while (amount-- > 0) {
                            double velX = vx * 0.085;
                            double velY = factor * 0.3 + 0.025;
                            double velZ = vz * 0.085;
                            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24f, 0.24f, 0.24f, 35f, isPowered() ? 30 : 25, ParticleDust.EnumDustBehavior.GROW, 1.08f);
                            this.level().addParticle(dustData, px + this.random.nextFloat() * 2 - 1, this.getBoundingBox().minY + 0.1, pz + this.random.nextFloat() * 2 - 1, velX, velY, velZ);
                        }
                    }
                }
            }
        }
    }

    public void doSplashParticlesEffect(int count) {
        if (this.level().isClientSide) {
            double theta = yBodyRot * (Math.PI / 180);
            double perpX = Math.cos(theta);
            double perpZ = Math.sin(theta);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            double x = getX() + 4.0 * vecX;
            double y = getBoundingBox().minY + 0.1;
            double z = getZ() + 4.0 * vecZ;
            int hitY = Mth.floor(getY() - 0.2);
            for (float[] robustAttackBlockOffset : ROBUST_ATTACK_BLOCK_OFFSETS) {
                float ox = robustAttackBlockOffset[0], oy = robustAttackBlockOffset[1];
                int hitX = Mth.floor(x + ox);
                int hitZ = Mth.floor(z + oy);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = level().getBlockState(hit);
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    for (int n = 0; n < count; n++) {
                        double pa = random.nextDouble() * 2 * Math.PI;
                        double pd = random.nextDouble() * 0.6 + 0.1;
                        double px = x + Math.cos(pa) * pd;
                        double pz = z + Math.sin(pa) * pd;
                        double magnitude = random.nextDouble() * 4 + 5;
                        double velX = perpX * magnitude;
                        double velY = random.nextDouble() * 3 + 6;
                        double velZ = perpZ * magnitude;
                        if (vecX * (pz - getZ()) - vecZ * (px - getX()) > 0) {
                            velX = -velX;
                            velZ = -velZ;
                        }
                        level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), px, y, pz, velX, velY, velZ);
                    }
                }
            }
        }
    }

    private void doBreakAirEffect() {
        if (this.level().isClientSide) {
            int tick = this.getAnimationTick();
            float alpha = this.isPowered() ? 0.18F : 0.14F;
            int keyFrame = this.isPowered() ? 22 : 18;
            if (tick < keyFrame) {
                double x = this.getX();
                double y = this.getY() + this.getBbHeight() / 2;
                double z = this.getZ();
                double motionX = this.getDeltaMovement().x;
                double motionY = this.getDeltaMovement().y;
                double motionZ = this.getDeltaMovement().z;
                //float yaw = (float) Math.atan2(motionX, motionZ);
                //float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
                float yaw = (float) Math.toRadians(-this.getYRot());
                float pitch = (float) Math.toRadians(-this.getXRot());
                if (tick % 3 == 0) {
                    this.level().addParticle(new ParticleRing.RingData(yaw, pitch, 40, 0.8f, 0.8f, 0.9f, alpha, 50f, false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), x + 6f * motionX, y + 1.5f * motionY, z + 6f * motionZ, 0, 0, 0);
                }
            }

        }
    }

    private void doExplodeEffect() {
        int tick = this.getAnimationTick();
        if (tick == 10) this.playSound(SoundInit.NAMELESS_GUARDIAN_ACCUMULATING.get(), 2F, 2.5F);
        if (tick > 21) this.explodeControlled.increaseTimer();
        this.setDeltaMovement(0, 0, 0);
        LivingEntity target = this.getTarget();
        if (target != null) this.getLookControl().setLookAt(target, 30F, 30F);
        if (!this.level().isClientSide) {
            if (tick == 50) {
                this.level().broadcastEntityEvent(this, (byte) 8);
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5F, false, Level.ExplosionInteraction.NONE);
                EntityCameraShake.cameraShake(level(), position(), 20, 0.2F, 10, 20);
            }
        }
    }


    public boolean guardianHurtTarget(EntityNamelessGuardian guardian, LivingEntity hitEntity, float hitEntityMaxHealth, float baseDamageMultiplier, float damageMultiplier, boolean shouldHeal, boolean disableShield, boolean ignoreHit) {
        return this.guardianHurtTarget(this.damageSources().mobAttack(guardian), guardian, hitEntity, hitEntityMaxHealth, baseDamageMultiplier, damageMultiplier, shouldHeal, disableShield, ignoreHit);
    }

    public boolean guardianHurtTarget(DamageSource damageSource, EntityNamelessGuardian guardian, LivingEntity hitEntity, float hitEntityMaxHealth, float baseDamageMultiplier, float damageMultiplier,
                                      boolean shouldHeal, boolean disableShield, boolean ignoreHit) {
        float finalDamage = ((guardian.getAttackDamageAttributeValue() * baseDamageMultiplier) + hitEntity.getMaxHealth() * hitEntityMaxHealth) * damageMultiplier;
        if (damageSource.is(EMResourceKey.GUARDIAN_LASER)) {
            finalDamage = ModEntityUtils.actualDamageIsCalculatedBasedOnArmor(finalDamage, hitEntity.getArmorValue(), (float) hitEntity.getAttributeValue(Attributes.ARMOR_TOUGHNESS), 0.8F);
        }
        boolean flag = hitEntity.hurt(damageSource, finalDamage);
        double suckBloodMultiplier = EMConfigHandler.COMMON.MOB.GULING.NAMELESS_GUARDIAN.suckBloodMultiplier.get();
        //治疗值 = 攻击力15% + 生命上限1.5% - 目标护甲值5%
        float heal = (guardian.getAttackDamageAttributeValue() * 0.15F) + (guardian.getMaxHealth() * 0.015F) - (Mth.clamp(hitEntity.getArmorValue() * 0.05F, 0F, 1.5F));
        boolean blocking = hitEntity instanceof Player && hitEntity.isBlocking();
        boolean checkConfig = EMConfigHandler.COMMON.MOB.GULING.NAMELESS_GUARDIAN.enableForcedSuckBlood.get() || this.isChallengeMode();
        if ((flag || (ignoreHit && this.isPowered() && !blocking && checkConfig)) && shouldHeal) {
            if (!flag) heal *= 0.5F;//未能造成伤害减少吸血量
            guardian.heal((float) (heal * suckBloodMultiplier));
        }
        if (disableShield && blocking) {
            Player player = (Player) hitEntity;
            player.disableShield(true);
            flag = true;
        }
        return flag;
    }

    @Override
    public void playAnimation(Animation animation) {
        if (animation != this.getNoAnimation() && this.attackTick <= 0 && !this.isPowered()) {
            this.attackTick = 10;
        }
        super.playAnimation(animation);
    }

    private int getCoolingTimerUtil(int maxCooling, int minCooling, float healthPercentage) {
        float maximumCoolingPercentage = 1 - healthPercentage;
        float ratio = 1 - (this.getHealthPercentage() / 100);
        if (ratio > maximumCoolingPercentage) {
            ratio = maximumCoolingPercentage;
        }
        return (int) (maxCooling - (ratio / maximumCoolingPercentage) * (maxCooling - minCooling));
    }

    public void setPowered(boolean flag) {
        this.entityData.set(DATA_POWER, flag);
    }

    @Override
    public boolean isPowered() {
        return this.entityData.get(DATA_POWER);
    }

    public void setUnnatural(boolean flag) {
        this.entityData.set(DATA_IS_UNNATURAL, flag);
        if (flag && !this.level().isClientSide) {
            this.goalSelector.addGoal(6, waterAvoidingRandomStrollGoal);
            this.goalSelector.addGoal(7, lookAtPlayerGoal);
            this.goalSelector.addGoal(8, randomLookAroundGoal);
        }
    }

    public boolean isFirstMadness() {
        return this.fmFlag;
    }

    public boolean isChallengeMode() {
        return EMConfigHandler.COMMON.MOB.GULING.NAMELESS_GUARDIAN.challengeMode.get();
    }

    public void setExecuteWeak(boolean executeWeak) {
        this.executeWeak = executeWeak;
    }

    public int getNextMadnessTick() {
        return this.nextMadnessTick;
    }

    public void setNextMadnessTick(int nextMadnessTick) {
        this.nextMadnessTick = nextMadnessTick;
    }

    public boolean isUnnatural() {
        return entityData.get(DATA_IS_UNNATURAL);
    }

    public Optional<BlockPos> getRestPos() {
        return getEntityData().get(DATA_REST_POSITION);
    }

    public void setRestPos(BlockPos pos) {
        getEntityData().set(DATA_REST_POSITION, Optional.of(pos));
    }

    public int getPounceTick() {
        return this.pounceTick;
    }

    public void setPounceTick(int pounceTick) {
        this.pounceTick = pounceTick;
    }

    public int getSmashTick() {
        return this.smashTick;
    }

    public void setSmashTick(int smashTick) {
        this.smashTick = smashTick;
    }

    public int getMadnessTick() {
        return this.madnessTick;
    }

    public void setMadnessTick(int madnessTick) {
        this.madnessTick = madnessTick;
    }

    public int getLeapTick() {
        return this.leapTick;
    }

    public void setLeapTick(int leapTick) {
        this.leapTick = leapTick;
    }

    public int getLaserTick() {
        return this.laserTick;
    }

    public void setLaserTick(int laserTick) {
        this.laserTick = laserTick;
    }

    public int getShakeGroundTick() {
        return this.shakeGroundTick;
    }

    public void setShakeGroundTick(int shakeGroundTick) {
        this.shakeGroundTick = shakeGroundTick;
    }

    public int getRobustTick() {
        return this.robustTick;
    }

    public void setRobustTick() {
        this.robustTick = ROBUST_ATTACK_TICK;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    public boolean isTimeOutToUseSkill() {
        return this.noUseSkillFromLongTick >= USE_SKILL_TIME_OUT_MAX_LIMIT || this.shouldUseSkill;
    }

    private void resetTimeOutToUseSkill() {
        this.noUseSkillFromLongTick = 0;
        this.shouldUseSkill = false;
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


    private void tickPart(EntityNamelessGuardianPart part, double x, double y, double z) {
        part.setPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    @Override
    public boolean isGlow() {
        return this.getHealth() > 0 && this.isActive() && !(this.getAnimation() == this.weakAnimation1 || this.getAnimation() == this.weakAnimation2 || (this.getAnimation() == this.weakAnimation3 && this.getAnimationTick() < 20));
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.NAMELESS_GUARDIAN_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        this.playSound(SoundInit.NAMELESS_GUARDIAN_HURT.get(), 0.5F, this.getVoicePitch());
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.NAMELESS_GUARDIAN_IDLE.get();
    }

    @Override
    public void playAmbientSound() {
        if (this.getTarget() == null && this.isActive()) {
            super.playAmbientSound();
        }
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    @Override
    protected boolean canPlayMusic() {
        return this.isActive() && super.canPlayMusic();
    }

    @Override
    protected boolean canHandOffMusic() {
        return (this.getAnimation() == this.roarAnimation && this.getAnimationTick() == 34) || (this.getAnimation() == this.weakAnimation1 && this.getAnimationTick() == 1);
    }

    @Override
    public SoundEvent getBossMusic() {
        return !this.isPowered() ? SoundInit.GUARDIANS_PRELUDE.get() : this.isChallengeMode() ? SoundInit.GUARDIANS.get() : SoundInit.GUARDIANS_CLIMAX.get();
    }

    @Override
    protected float getSoundVolume() {
        return this.getTarget() == null ? 0.5F : super.getSoundVolume();
    }

    public class Listener implements GameEventListener {

        @Override
        public PositionSource getListenerSource() {
            return new EntityPositionSource(EntityNamelessGuardian.this, EntityNamelessGuardian.this.getEyeHeight());
        }

        @Override
        public int getListenerRadius() {
            return 16;
        }

        @Override
        public boolean handleGameEvent(ServerLevel level, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3) {
            if (gameEvent == GameEvent.ENTITY_DIE) {
                Entity entity = context.sourceEntity();
                if (entity instanceof LivingEntity livingEntity) {
                    this.tryAdvancement(level, livingEntity);
                }
            }
            return false;
        }

        private void tryAdvancement(Level level, LivingEntity entity) {
            LivingEntity livingentity = entity.getLastHurtByMob();
            if (livingentity instanceof ServerPlayer serverplayer) {
                if (entity instanceof EntityNamelessGuardian guardian && guardian.isChallengeMode()) {
                    DamageSource damagesource = entity.getLastDamageSource() == null ? level.damageSources().playerAttack(serverplayer) : entity.getLastDamageSource();
                    EMCriteriaTriggers.KILL_BOSS_IN_CHALLENGE_MODE.trigger(serverplayer, entity, damagesource);
                }
            }

        }
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> listenerConsumer) {
        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            listenerConsumer.accept(this.dynamicListener, serverLevel);
        }
    }
}
