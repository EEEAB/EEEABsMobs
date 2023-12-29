package com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian;

import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EEConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EEBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.*;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.*;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationAbstractGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EEPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityFallingBlock;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.ModDamageSource;
import com.eeeab.eeeabsmobs.sever.util.ModTag;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
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
import net.minecraft.tags.DamageTypeTags;
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
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
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

//创建于 2023/1/17
public class EntityNamelessGuardian extends EEEABMobLibrary implements IBoss, GlowEntity, PowerableMob, NeedStopAiEntity {
    public static final Animation DIE_ANIMATION = Animation.create(60);
    public static final Animation ROAR_ANIMATION = Animation.create(80);
    public static final Animation ATTACK_ANIMATION_1 = Animation.create(40);
    public static final Animation ATTACK_ANIMATION_2 = Animation.create(35);
    public static final Animation ATTACK_ANIMATION_3 = Animation.create(44);
    public static final Animation ROBUST_ATTACK_ANIMATION = Animation.create(82);
    public static final Animation SMASH_ATTACK_ANIMATION = Animation.create(40);
    public static final Animation POUNCE_ATTACK_ANIMATION_1 = Animation.create(16);
    public static final Animation POUNCE_ATTACK_ANIMATION_2 = Animation.create(29);
    public static final Animation POUNCE_ATTACK_ANIMATION_3 = Animation.create(18);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(56);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(40);
    public static final Animation WEAK_ANIMATION_1 = Animation.create(40);
    public static final Animation WEAK_ANIMATION_2 = Animation.create(200);
    public static final Animation WEAK_ANIMATION_3 = Animation.create(40);
    public static final Animation LEAP_ANIMATION = Animation.create(105);
    public static final Animation SMASH_DOWN_ANIMATION = Animation.create(21);
    public static final Animation LASER_ANIMATION = Animation.create(120);
    public static final Animation CONCUSSION_ANIMATION = Animation.create(24);
    public static final Animation ATTACK2_ANIMATION_1 = Animation.create(40);
    public static final Animation ATTACK2_ANIMATION_2 = Animation.create(30);
    public static final Animation ATTACK2_ANIMATION_3 = Animation.create(20);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            ROAR_ANIMATION,
            ATTACK_ANIMATION_1,
            ATTACK_ANIMATION_2,
            ATTACK_ANIMATION_3,
            SMASH_ATTACK_ANIMATION,
            ROBUST_ATTACK_ANIMATION,
            POUNCE_ATTACK_ANIMATION_1,
            POUNCE_ATTACK_ANIMATION_2,
            POUNCE_ATTACK_ANIMATION_3,
            ACTIVATE_ANIMATION,
            DEACTIVATE_ANIMATION,
            WEAK_ANIMATION_1,
            WEAK_ANIMATION_2,
            WEAK_ANIMATION_3,
            LEAP_ANIMATION,
            SMASH_DOWN_ANIMATION,
            LASER_ANIMATION,
            CONCUSSION_ANIMATION,
            ATTACK2_ANIMATION_1,
            ATTACK2_ANIMATION_2,
            ATTACK2_ANIMATION_3
    };

    private final EELookAtGoal lookAtPlayerGoal = new EELookAtGoal(this, Player.class, 8.0F);
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
    private int guardianInvulnerableTime;
    //用来判断因为特殊情况导致部分技能无法释放,用于计算时长
    private int noUseSkillFromLongTick;
    private boolean executeWeak;
    private boolean shouldUseSkill;
    private boolean FIRST = true;
    private int attackTick;
    //BGM高潮部分的时长
    private static final int MADNESS_TICK = 1300;
    private static final int USE_SKILL_TIME_OUT_MAX_LIMIT = 300;
    private final static int MAX_NEXT_MADNESS_TICK = 900;
    private final static int MIN_NEXT_MADNESS_TICK = 600;
    private final static int MAX_SMASH_ATTACK_TICK = 200;
    private final static int MIN_SMASH_ATTACK_TICK = 160;
    private final static int MAX_POUNCE_ATTACK_TICK = 400;
    private final static int MIN_POUNCE_ATTACK_TICK = 200;
    private final static int MAX_LEAP_TICK = 500;
    private final static int MIN_LEAP_TICK = 280;
    private final static int MAX_LASER_ATTACK_TICK = 380;
    private final static int MIN_LASER_ATTACK_TICK = 300;
    private final static int WEAK_FRAME = 8;
    private final static int HARD_MODE_WEAK_FRAME = 4;
    private final EntityNamelessGuardianPart core;
    private final EntityNamelessGuardianPart[] subEntities;
    private static final float[][] ROBUST_ATTACK_BLOCK_OFFSETS = {
            {-0.5F, -0.5F},
            {-0.5F, 0.5F},
            {0.5F, 0.5F},
            {0.5F, -0.5F}
    };

    public boolean inShoot = false;
    public final ControlledAnimation coreControlled = new ControlledAnimation(10);

    public EntityNamelessGuardian(EntityType<? extends EntityNamelessGuardian> type, Level level) {
        super(type, level);
        this.xpReward = 30;
        this.active = false;
        this.core = new EntityNamelessGuardianPart(this, "core", 0.6F, 0.6F);
        this.subEntities = new EntityNamelessGuardianPart[]{this.core};
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

    @Override//是否免疫药水效果
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return this.isActive() && !this.isPowered() && super.addEffect(effectInstance, entity);
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
        return new EEBodyRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new EEPathNavigateGround(this, level);
    }


    @Override
    protected EEConfigHandler.AttributeConfig getAttributeConfig() {
        return EEConfigHandler.COMMON.MOB.GUARDIAN.combatConfig;
    }

    @Override
    protected boolean showBossBloodBars() {
        return EEConfigHandler.COMMON.OTHER.enableShowBloodBars.get();
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
        return 35;
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    protected void onAnimationStart(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == WEAK_ANIMATION_1) {
                this.setExecuteWeak(true);
                this.setPowered(false);
                this.setNextMadnessTick(getCoolingTimerUtil(MAX_NEXT_MADNESS_TICK, MIN_NEXT_MADNESS_TICK, 0.5F));
            } else if (animation == POUNCE_ATTACK_ANIMATION_1 || animation == LASER_ANIMATION) {
                this.noUseSkillFromLongTick = 0;
                this.shouldUseSkill = false;
            }
        }
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == DEACTIVATE_ANIMATION || animation == ACTIVATE_ANIMATION) {
                this.FIRST = true;
                this.attackTick = this.madnessTick = this.guardianInvulnerableTime = this.noUseSkillFromLongTick = 0;
                setExecuteWeak(false);
            } else if (animation == ROAR_ANIMATION) {
                this.madnessTick = MADNESS_TICK;
                this.FIRST = false;
                this.laserTick = this.pounceTick = this.smashTick = this.attackTick = 0;
                this.removeAllEffects();
            } else if (animation == WEAK_ANIMATION_3) {
                this.setExecuteWeak(false);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //this.goalSelector.addGoal(0, new FloatGoal(this) {
        //    @Override
        //    public boolean canUse() {
        //        return EntityNamelessGuardian.this.isInWater() && EntityNamelessGuardian.this.getFluidHeight(FluidTags.WATER) > EntityNamelessGuardian.this.getFluidJumpThreshold() || EntityNamelessGuardian.this.isInFluidType((fluidType, height) -> EntityNamelessGuardian.this.canSwimInFluidType(fluidType) && height > EntityNamelessGuardian.this.getFluidJumpThreshold());
        //    }
        //});
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        //this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, EntityTestllager.class, 0, true, false, null));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationHurtGoal<>(this, false));
        this.goalSelector.addGoal(1, new AnimationDieGoal<>(this));
        this.goalSelector.addGoal(1, new GuardianCombo1Goal(this, 3.5F, 100F));
        this.goalSelector.addGoal(1, new GuardianCombo2Goal(this, 5.0F, 100F));
        this.goalSelector.addGoal(1, new GuardianLobedAttackGoal(this, SMASH_ATTACK_ANIMATION));
        this.goalSelector.addGoal(1, new GuardianPounceAttackGoal(this, 3F));
        this.goalSelector.addGoal(1, new GuardianRobustAttackGoal(this, ROBUST_ATTACK_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationActivateGoal<>(this, ACTIVATE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationDeactivateGoal<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationCommonGoal<>(this, ROAR_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationAbstractGoal<>(this) {

            @Override
            protected boolean test(Animation animation) {
                return animation == EntityNamelessGuardian.WEAK_ANIMATION_1 || animation == EntityNamelessGuardian.WEAK_ANIMATION_2 || animation == EntityNamelessGuardian.WEAK_ANIMATION_3;
            }

            @Override
            public void tick() {
                setDeltaMovement(0, getDeltaMovement().y, 0);
                if (entity.getAnimation() == EntityNamelessGuardian.WEAK_ANIMATION_1) {
                    if (entity.getAnimationTick() == EntityNamelessGuardian.WEAK_ANIMATION_1.getDuration() - 1) {
                        entity.playAnimation(WEAK_ANIMATION_2);
                    }
                } else if (entity.getAnimation() == EntityNamelessGuardian.WEAK_ANIMATION_2) {
                    int frame = Difficulty.HARD.equals(entity.level().getDifficulty()) ? HARD_MODE_WEAK_FRAME : WEAK_FRAME;
                    if (entity.getAnimationTick() == (frame * 20)) {
                        entity.playAnimation(WEAK_ANIMATION_3);
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new GuardianLeapGoal(this, LEAP_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationCommonGoal<>(this, SMASH_DOWN_ANIMATION) {
            @Override
            public void tick() {
                int tick = getAnimationTick();
                if (tick >= 2 && tick < 9) {
                    entity.lobedAttack(tick + 6, 2.0F, 0.0F, 3F, entity.isPowered() ? 0.625F : 0.5F);
                    if (tick == 2) {
                        entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1F + entity.getRandom().nextFloat() * 0.1F);
                        EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.125F, 8, 17);
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new GuardianShootLaserGoal(this, LASER_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationFullRangeAttackGoal<>(this, CONCUSSION_ANIMATION, 6.5F, 8, 2.5F, 1.0F, true));
        this.goalSelector.addGoal(2, new GuardianAIGoal(this));
        this.goalSelector.addGoal(2, new AnimationDieGoal<>(this));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return ((!this.isActive() || animation == ACTIVATE_ANIMATION || animation == DEACTIVATE_ANIMATION || animation == ROAR_ANIMATION || animation == WEAK_ANIMATION_1 || animation == WEAK_ANIMATION_3)) || super.isInvulnerableTo(damageSource);
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
        floatGuardian();
        this.coreControlled.updatePrevTimer();

        AnimationHandler.INSTANCE.updateAnimations(this);

        if (!this.level().isClientSide) {
            if (this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);

            if (this.isActive() && this.getAnimation() == NO_ANIMATION && !this.isNoAi())
                this.playAnimation(ATTACK2_ANIMATION_1);
            //this.playAnimation(WEAK_ANIMATION_1);

            if (this.getTarget() != null && this.isActive() && !this.isPowered() && this.noConflictingTasks() && ((this.FIRST && this.getHealthPercentage() <= 60) || (!this.FIRST && this.getNextMadnessTick() <= 0))) {
                this.removeAllEffects();
                this.setNoAi(false);
                this.playAnimation(ROAR_ANIMATION);
            }

            if (this.isUnnatural()) {
                this.setActive(true);
                this.active = true;
            } else if (this.noConflictingTasks() && !this.isNoAi()) {
                if (this.isActive()) {
                    if (this.getTarget() == null && !this.isPowered() && zza == 0 && this.isAtRestPos()) {
                        this.playAnimation(DEACTIVATE_ANIMATION);
                        this.setActive(false);
                    }
                } else if (this.getTarget() != null && this.targetDistance <= 5) {
                    this.playAnimation(ACTIVATE_ANIMATION);
                    this.setActive(true);
                }
            }

            if (!this.isUnnatural()) {
                //this.setYRot(this.yBodyRot);
                if (this.noConflictingTasks() && this.getTarget() == null && this.getNavigation().isDone() && !this.isAtRestPos() && this.isActive()) {
                    this.moveToRestPos();
                }
            }

            if (!this.active && this.getAnimation() != ACTIVATE_ANIMATION) {
                if (EEConfigHandler.COMMON.MOB.GUARDIAN.enableNonCombatHeal.get()) this.heal(0.5F);
            }
            if (this.active && getTarget() != null && this.targetDistance < 6.0f && this.isPowered()) {
                if (this.laserTick <= 0) {
                    this.noUseSkillFromLongTick++;
                }
                if (this.pounceTick <= 0) {
                    this.noUseSkillFromLongTick++;
                }
            }

            if (this.getTarget() != null && this.active && this.isTimeOutToUseSkill() && !this.shouldUseSkill && this.getAnimation() == NO_ANIMATION && !this.isNoAi()) {
                this.playAnimation(CONCUSSION_ANIMATION);
                this.shouldUseSkill = true;
            }
        }

        this.pushEntitiesAway(1.7F, getBbHeight(), 1.7F, 1.7F);

        if (!this.isActive()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }

        if (this.isGlow()) {
            this.coreControlled.increaseTimer();
        } else {
            this.coreControlled.decreaseTimer();
        }

        if (this.getAnimation() != NO_ANIMATION
                && this.getAnimation() != POUNCE_ATTACK_ANIMATION_1
                && this.getAnimation() != POUNCE_ATTACK_ANIMATION_2
                && this.getAnimation() != POUNCE_ATTACK_ANIMATION_3
                && this.getAnimation() != ROAR_ANIMATION
                && this.getAnimation() != LASER_ANIMATION
                || !this.isActive()) {
            this.yHeadRot = this.yBodyRot = this.getYRot();
        }

        int tick = this.getAnimationTick();
        if (this.getAnimation() == ROAR_ANIMATION) {
            if (!this.isPowered()) this.setPowered(true);
            this.doRoarEffect();
        } else if (this.getAnimation() == CONCUSSION_ANIMATION) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            if (tick == 8) {
                ModParticleUtils.sphericalParticleOutburst(level(), this, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME, ParticleInit.GUARDIAN_SPARK.get()}, 2.5F, 10F, 0, 0, 3);
                EntityCameraShake.cameraShake(level(), position(), 30, 0.125F, 0, 20);
            } else if (tick > 8) {
                this.strongKnockBlock();
            }
            if (this.getTarget() != null) this.lookAt(getTarget(), 30F, 30F);
        } else

            //if ((this.fallDistance > 0.2 && !this.onGround() &&) || this.getAnimation() == DODGE_ANIMATION)
            //    this.shouldPlayLandAnimation = true;
            //if (this.onGround() && this.shouldPlayLandAnimation && this.getAnimation() != DODGE_ANIMATION) {
            //    if (!this.level().isClientSide && this.getAnimation() == NO_ANIMATION) {
            //        this.playAnimation(LANDING_ANIMATION);
            //        this.shouldPlayLandAnimation = false;
            //    }
            //}

            if (this.getAnimation() == ATTACK_ANIMATION_1 && tick == 15) {
                this.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, this.getVoicePitch());
            } else if (this.getAnimation() == ATTACK_ANIMATION_2 && tick == 11) {
                this.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.05f, this.getVoicePitch() + 0.15f);
            } else if (this.getAnimation() == ATTACK_ANIMATION_3 && tick == 11) {
                this.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2.2f, this.getVoicePitch() - 0.2f);
            } else if (this.getAnimation() == ROBUST_ATTACK_ANIMATION) {
                if (tick < 35) {
                    this.preRobustAttack();
                } else if (tick == 35) {
                    this.robustAttackEffect();
                }
            } else if (this.getAnimation() == SMASH_ATTACK_ANIMATION) {
                this.doSmashEffect();
            } else if (this.getAnimation() == POUNCE_ATTACK_ANIMATION_2) {
                this.doWalkEffect(10);
                if (tick > 2) this.doBreakAirEffect(tick);
            } else if (this.getAnimation() == WEAK_ANIMATION_2) {
                if (this.level().isClientSide && tick % 5 == 0) {
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY() - 1.5F, this.getRandomZ(0.5D), -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D);
                }
            } else if (this.getAnimation() == LASER_ANIMATION) {
                if (tick == 22) this.playSound(SoundInit.LASER.get(), 2f, 1.0f);
                else if (this.level().isClientSide) this.inShoot = tick > 23 && tick < 99;
            } else if (this.getAnimation() == LEAP_ANIMATION && tick == 13) {
                if (!this.level().isClientSide) this.level().broadcastEntityEvent(this, (byte) 7);
            } else if (this.getAnimation() == SMASH_DOWN_ANIMATION && tick == 4) {
                if (!this.level().isClientSide) this.level().broadcastEntityEvent(this, (byte) 7);
            } else if (this.getAnimation() == ACTIVATE_ANIMATION) {
                LivingEntity target = getTarget();
                if (target != null && tick > 40) {
                    //this.getLookControl().setLookAt(target, 30F, 30F);
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

        if (this.getAnimation() == NO_ANIMATION && this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F
                && this.random.nextInt(5) == 0) {
            this.doWalkEffect(1);
        }

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);

        if (this.level().isClientSide && speed > 0.05 && this.isActive() && !this.isSilent()) {
            if (this.frame % 22 == 1 && this.getAnimation() == NO_ANIMATION) {
                this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.NAMELESS_GUARDIAN_STEP.get(), this.getSoundSource(), 1F, 1.05F, false);
            }
            if (this.frame % 5 == 1 && this.getAnimation() == POUNCE_ATTACK_ANIMATION_2) {
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


    @Override
    public void handleEntityEvent(byte id) {
        if (id == 6) {
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24f, 0.24f, 0.24f, 40f, 20, ParticleDust.EnumDustBehavior.GROW, 1.0f);
            ModParticleUtils.annularParticleOutburst(level(), 15, new ParticleOptions[]{dustData}, getX(), getY(), getZ(), 0.9, 0.5);
            ModParticleUtils.annularParticleOutburst(level(), 15, new ParticleOptions[]{dustData}, getX(), getY(), getZ(), 0.6, 0.5);
        } else if (id == 7) {
            ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24f, 0.24f, 0.24f, 40f, 25, ParticleDust.EnumDustBehavior.SHRINK, 1.0f);
            ModParticleUtils.annularParticleOutburst(level(), 15, new ParticleOptions[]{dustData}, getX(), this.getY(), getZ(), 0.8F, 0.1);
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(1.2D), this.getRandomY(), this.getRandomZ(1.2D), d0, d1, d2);
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.5D), this.getRandomY(), this.getRandomZ(1.5D), d0, d1, d2);
        }
        for (int i = 0; i < 10; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), this.getRandomX(1.4D), this.getRandomY(), this.getRandomZ(1.4D), d0, d1, d2);
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

            if (this.attackTick > 0 && this.getAnimation() == NO_ANIMATION) {
                this.attackTick--;
            }

            if (this.madnessTick > 0 && this.getTarget() != null) {
                this.madnessTick--;
            }

            if (this.nextMadnessTick > 0) {
                this.nextMadnessTick--;
            }
        }

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
            float maximumDamageCap = (float) (EEConfigHandler.COMMON.MOB.GUARDIAN.maximumDamageCap.damageCap.get() * 1F);
            float maxHurtDamage = getMaxHealth() * maximumDamageCap;
            if ((!active || getTarget() == null) && entity instanceof LivingEntity livingEntity
                    && !(livingEntity instanceof Player player && player.isCreative() || this.level().getDifficulty() == Difficulty.PEACEFUL)
                    && !(livingEntity instanceof EntityNamelessGuardian))
                this.setLastHurtByMob(livingEntity);//使得可以有多个仇恨目标
            if (this.guardianInvulnerableTime > 0) {
                return false;
            } else if (entity != null) {
                if (this.isPowered()) {
                    if (this.guardianInvulnerableTime <= 0) guardianInvulnerableTime = 25 /*不能小于等于10*/;
                    if (ModEntityUtils.isProjectileSource(source)) return false;
                }
                if (this.getAnimation() == WEAK_ANIMATION_2) {
                    maxHurtDamage = /* 如果伤害源是magic类型 则没有限制伤害,反之则受到最大伤害上限2倍伤害*/source.is(ModTag.MAGIC_UNRESISTANT_TO) ? damage : getMaxHealth() * (maximumDamageCap * 2);
                    maxHurtDamage = /* 防止超过生命值上限 */Math.min(maxHurtDamage, getMaxHealth());
                }
                damage = Math.min(damage, maxHurtDamage);
                return super.hurt(source, damage);
            } else if (source.is(ModTag.GENERAL_UNRESISTANT_TO)) {
                return super.hurt(source, damage);
            }
        }
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        Entity entity = pSource.getEntity();
        if (entity instanceof Player) {
            ItemEntity itementity = this.spawnAtLocation(ItemInit.GUARDIAN_AXE.get());
            if (itementity != null) {
                itementity.setExtendedLifetime();
                itementity.setGlowingTag(true);
            }
        }
    }

    public boolean checkCanAttackRange(double checkRange, double range) {
        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
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
        this.FIRST = compound.getBoolean("isFirst");
        this.setActive(compound.getBoolean("isActive"));
        this.setPowered(compound.getBoolean("power"));
        this.setUnnatural(compound.getBoolean("isUnnatural"));
        this.setMadnessTick(compound.getInt("madnessCountdownTick"));
        this.setNextMadnessTick(compound.getInt("nextMadnessTick"));
        this.coreControlled.setTimer(compound.getInt("coreLighting"));
        active = isActive();
        //this.setExecuteWeak(compound.getBoolean("executeWeak"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.getRestPos().ifPresent(spawnPos -> compound.put("spawnPos", NbtUtils.writeBlockPos(spawnPos)));
        compound.putBoolean("isFirst", this.FIRST);
        compound.putBoolean("power", this.entityData.get(DATA_POWER));
        compound.putBoolean("isUnnatural", this.entityData.get(DATA_IS_UNNATURAL));
        compound.putBoolean("isActive", this.entityData.get(DATA_ACTIVE));
        compound.putInt("madnessCountdownTick", this.madnessTick);
        compound.putInt("nextMadnessTick", this.nextMadnessTick);
        compound.putInt("coreLighting", this.coreControlled.getTimer());
        //compound.putBoolean("executeWeak", this.executeWeak);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 300.0D).
                add(Attributes.ARMOR, 10).
                add(Attributes.ATTACK_DAMAGE, 14.0D).
                add(Attributes.FOLLOW_RANGE, 50.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compoundTag) {
        this.setUnnatural(spawnType == MobSpawnType.SPAWN_EGG);
        this.setRestPos(this.blockPosition());
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, compoundTag);
    }

    public float getAttackDamageAttributeValue() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
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
    public Animation getHurtAnimation() {
        return NO_ANIMATION;
    }

    @Override
    public boolean noConflictingTasks() {
        return !this.executeWeak && getAnimation() == IAnimatedEntity.NO_ANIMATION;
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
                boolean canRobust = dist <= 144D && this.isPowered && this.guardian.getMadnessTick() <= 0;
                boolean canSmash = (checkAttackHeight || target.onGround()) && (this.random.nextFloat() < 0.6F && dist <= (this.isPowered ? 50D : 25D) && this.guardian.getSmashTick() <= 0);
                if (dist < 16D && !canSmash && !canRobust) {
                    this.guardian.playAnimation(ATTACK_ANIMATION_1);
                } else if (canRobust) {
                    this.guardian.playAnimation(ROBUST_ATTACK_ANIMATION);
                } else if (canSmash) {
                    this.guardian.playAnimation(SMASH_ATTACK_ANIMATION);
                    this.guardian.smashTick = this.guardian.getCoolingTimerUtil(MAX_SMASH_ATTACK_TICK, MIN_SMASH_ATTACK_TICK, 0.2F);
                }

                boolean canLaser = this.random.nextFloat() < 0.6F && this.isPowered && (((checkAttackHeight ? this.guardian.targetDistance > 10.0D : this.guardian.targetDistance > 4.0D) && (entityRelativeAngle < 60.0 || entityRelativeAngle > 300) && this.guardian.targetDistance < EntityGuardianLaser.GUARDIAN_RADIUS && this.guardian.getLaserTick() <= 0) || this.guardian.isTimeOutToUseSkill() && this.guardian.shouldUseSkill);
                boolean canPouch = checkAttackHeight && (this.random.nextFloat() < 0.6F && this.guardian.targetDistance > 6.0D && this.guardian.targetDistance < 16.0 && this.guardian.getPounceTick() <= 0 || this.guardian.isTimeOutToUseSkill() && this.guardian.shouldUseSkill);
                boolean canLeap = this.random.nextFloat() < 0.6F && this.guardian.targetDistance > 12.0D && this.guardian.targetDistance < 24.0 && this.guardian.getLeapTick() <= 0;
                if (canLaser) {
                    this.guardian.playAnimation(LASER_ANIMATION);
                    this.guardian.laserTick = this.guardian.getCoolingTimerUtil(MAX_LASER_ATTACK_TICK, MIN_LASER_ATTACK_TICK, 0.5F);
                } else if (canPouch) {
                    this.guardian.playAnimation(POUNCE_ATTACK_ANIMATION_1);
                    this.guardian.pounceTick = this.guardian.getCoolingTimerUtil(MAX_POUNCE_ATTACK_TICK, MIN_POUNCE_ATTACK_TICK, 0.5F);
                } else if (canLeap) {
                    this.guardian.playAnimation(LEAP_ANIMATION);
                    this.guardian.leapTick = this.guardian.getCoolingTimerUtil(MAX_LEAP_TICK, MIN_LEAP_TICK, 0.2F);
                }
            }
        }
    }

    private void preRobustAttack() {
        List<Entity> entities = getNearByEntities(Entity.class, 16, 16, 16, 16);
        for (Entity inRangeEntity : entities) {
            if (inRangeEntity instanceof Player player && player.getAbilities().invulnerable) continue;
            Vec3 diff = inRangeEntity.position().subtract(this.position().add(Math.cos(Math.toRadians(this.yBodyRot + 90)) * 3.2F, 0, Math.sin(Math.toRadians(this.yBodyRot + 90)) * 3.2F));
            diff = diff.normalize().scale(0.08);
            inRangeEntity.setDeltaMovement(inRangeEntity.getDeltaMovement().subtract(diff));
            if (inRangeEntity.getY() > this.getY() + 3) {
                inRangeEntity.setDeltaMovement(inRangeEntity.getDeltaMovement().subtract(0, 0.06, 0));
            }
        }
    }

    public void robustAttack(int distance, float hitEntityMaxHealth, float baseDamageMultiplier, float damageMultiplier, boolean shouldHeal) {
        ServerLevel level = (ServerLevel) this.level();
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double spread = Math.PI * 2.0;
        int arcLen = Mth.ceil(distance * spread);
        double minY = this.getBoundingBox().minY - 2;
        double maxY = this.getBoundingBox().maxY;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = this.getX() + vx * distance + 4.0 * Math.cos((double) (this.yBodyRot + 90.0F) * Math.PI / 180.0D);
            double pz = this.getZ() + vz * distance + 4.0 * Math.sin((double) (this.yBodyRot + 90.0F) * Math.PI / 180.0D);

            AABB aabb = new AABB(px - 1.5D, minY, pz - 1.5D, px + 1.5D, maxY, pz + 1.5D);
            List<Entity> entities = level().getEntitiesOfClass(Entity.class, aabb);
            for (Entity hit : entities) {
                if (hit.onGround()) {
                    if (hit == this || hit instanceof EntityFallingBlock) {
                        continue;
                    }
                    if (hit instanceof LivingEntity livingEntity) {
                        this.guardianHurtTarget(ModDamageSource.guardianRobustAttack(this), this, livingEntity, hitEntityMaxHealth, baseDamageMultiplier, damageMultiplier, shouldHeal, false);
                    }
                    double magnitude = level().random.nextGaussian() * 0.15 + 0.1;
                    double angle = this.getAngleBetweenEntities(this, hit);
                    double x1 = Math.cos(Math.toRadians(angle - 90));
                    double z1 = Math.sin(Math.toRadians(angle - 90));
                    float x = 0F, y = 0F, z = 0F;
                    x += x1 * magnitude * 0.15;
                    y += 0.1 + 0.2 * magnitude * 0.1;
                    z += z1 * magnitude * 0.15;
                    if (hit instanceof ServerPlayer) {
                        ((ServerPlayer) hit).connection.send(new ClientboundSetEntityMotionPacket(hit));
                    }
                    hit.setDeltaMovement(hit.getDeltaMovement().add(x, y, z));
                }
            }
            int hitX = Mth.floor(px);
            int hitZ = Mth.floor(pz);
            BlockPos pos = new BlockPos(hitX, hitY, hitZ);
            ModEntityUtils.spawnFallingBlockByPos(level, pos);
        }
    }

    public void lobedAttack(int tick, double spreadArc, double offset, float maxFallingDistance, float damageMultiply) {
        ServerLevel level = (ServerLevel) this.level();
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double spread = Math.PI * spreadArc;
        double maxY = this.getBoundingBox().maxY;
        double minY = this.getBoundingBox().minY - 2D;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        if (tick % 2 == 0) {
            int distance = tick / 2 - 2;
            //取整(向上取整)
            int arcLen = Mth.ceil(distance * spread);
            for (int i = 0; i < arcLen; i++) {
                double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                double vx = Math.cos(theta);
                double vz = Math.sin(theta);
                double px = this.getX() + vx * distance + offset * Math.cos((double) (this.yBodyRot + 90.0F) * Math.PI / 180.0D);
                double pz = this.getZ() + vz * distance + offset * Math.sin((double) (this.yBodyRot + 90.0F) * Math.PI / 180.0D);
                float factor = 1 - distance / maxFallingDistance;

                //范围攻击
                AABB aabb = new AABB(px - 1.5D, minY, pz - 1.5D, px + 1.5D, maxY, pz + 1.5D);
                List<Entity> entities = level().getEntitiesOfClass(Entity.class, aabb);

                for (Entity hit : entities) {
                    if (hit.onGround()) {
                        if (hit == this || hit instanceof EntityFallingBlock) {
                            continue;
                        }
                        if (hit instanceof LivingEntity livingEntity) {
                            this.guardianHurtTarget(this, livingEntity, 0.05F, 0.5F, damageMultiply, false, false);
                        }

                        double magnitude = level().random.nextDouble() * 0.15 + 0.1;
                        double angle = this.getAngleBetweenEntities(this, hit);
                        double x1 = Math.cos(Math.toRadians(angle - 90));
                        double z1 = Math.sin(Math.toRadians(angle - 90));
                        float x = 0F, y = 0F, z = 0F;
                        x += x1 * magnitude * 0.15;
                        y += 0.1 + factor * 0.15;
                        z += z1 * magnitude * 0.15;
                        hit.setDeltaMovement(hit.getDeltaMovement().add(x, y, z));
                        if (hit instanceof ServerPlayer) {
                            ((ServerPlayer) hit).connection.send(new ClientboundSetEntityMotionPacket(hit));
                        }
                    }
                }
                if (random.nextBoolean()) {
                    //取整(向下取整)
                    int hitX = Mth.floor(px);
                    //取整(向下取整)
                    int hitZ = Mth.floor(pz);
                    BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                    ModEntityUtils.spawnFallingBlockByPos(level, pos, factor);
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
                ModParticleUtils.sphericalParticleOutburst(level(), this, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME, ParticleInit.GUARDIAN_SPARK.get()}, 2.5F, 10F, 0, 0, 3);
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

    private void robustAttackEffect() {
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
                    for (int n = 0; n < 20; n++) {
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

    private void doWalkEffect(int amount) {
        if (this.level().isClientSide) {
            for (int l = 0; l < amount; l++) {
                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY() - (double) 0.2F);
                int k = Mth.floor(this.getZ());
                BlockPos pos = new BlockPos(i, j, k);
                BlockState blockstate = this.level().getBlockState(pos);
                if (!blockstate.isAir()) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos),
                            this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            this.getY() + 0.1D, this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
                            4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
                }
            }
        }
    }

    private void doBreakAirEffect(int tick) {
        if (this.level().isClientSide) {
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


    public boolean guardianHurtTarget(EntityNamelessGuardian guardian, LivingEntity hitEntity, float hitEntityMaxHealth, float baseDamageMultiplier, float damageMultiplier, boolean shouldHeal, boolean disableShield) {
        return this.guardianHurtTarget(this.damageSources().mobAttack(guardian), guardian, hitEntity, hitEntityMaxHealth, baseDamageMultiplier, damageMultiplier, shouldHeal, disableShield);
    }

    public boolean guardianHurtTarget(DamageSource damageSource, EntityNamelessGuardian guardian, LivingEntity hitEntity, float hitEntityMaxHealth, float baseDamageMultiplier, float damageMultiplier, boolean shouldHeal, boolean disableShield) {
        float finalDamage = ((guardian.getAttackDamageAttributeValue() * baseDamageMultiplier) + hitEntity.getMaxHealth() * hitEntityMaxHealth) * damageMultiplier;
        boolean flag = hitEntity.hurt(damageSource, finalDamage);
        double suckBloodCap = EEConfigHandler.COMMON.MOB.GUARDIAN.suckBloodFactor.get();
        if (flag && shouldHeal) guardian.heal((float) Mth.clamp(finalDamage * 0.2F, 0F, getMaxHealth() * suckBloodCap));
        if (disableShield && hitEntity instanceof Player player && player.isBlocking()) {
            player.disableShield(true);
            flag = true;
        }
        return flag;
    }

    @Override
    public void playAnimation(Animation animation) {
        if (animation != NO_ANIMATION && this.attackTick <= 0 && !this.isPowered()) {
            this.attackTick = 10;
        }
        super.playAnimation(animation);
    }


    private int getCoolingTimerUtil(int maxCooling, int minCooling, float healthPercentage) {
        //if (maxCooling <= minCooling || healthPercentage > 1 && healthPercentage < 0) return maxCooling;
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

    public void setExecuteWeak(boolean executeWeak) {
        this.executeWeak = executeWeak;
    }

    public int getNextMadnessTick() {
        return nextMadnessTick;
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
        return pounceTick;
    }

    public int getSmashTick() {
        return smashTick;
    }

    public int getMadnessTick() {
        return madnessTick;
    }

    public void setMadnessTick(int madnessTick) {
        this.madnessTick = madnessTick;
    }

    public int getLeapTick() {
        return leapTick;
    }

    public int getLaserTick() {
        return laserTick;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    public boolean isTimeOutToUseSkill() {
        return noUseSkillFromLongTick >= USE_SKILL_TIME_OUT_MAX_LIMIT;
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
        return this.getHealth() > 0 && this.isActive() && !(this.getAnimation() == WEAK_ANIMATION_1 || this.getAnimation() == WEAK_ANIMATION_2 || (this.getAnimation() == WEAK_ANIMATION_3 && this.getAnimationTick() < 20));
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    //@Override
    //public boolean isPowered() {
    //    return this.isMadness();
    //}

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
        return (this.getAnimation() == ROAR_ANIMATION && this.getAnimationTick() == 34) || (this.getAnimation() == WEAK_ANIMATION_1 && this.getAnimationTick() == 1);
    }

    @Override
    public SoundEvent getBossMusic() {
        return this.isPowered() ? SoundInit.GUARDIANS_CLIMAX.get() : SoundInit.GUARDIANS_PRELUDE.get();
    }

    @Override
    protected float getSoundVolume() {
        return this.getTarget() == null ? 0.5F : super.getSoundVolume();
    }

}
