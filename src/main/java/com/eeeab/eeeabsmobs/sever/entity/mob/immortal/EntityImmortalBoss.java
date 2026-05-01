package com.eeeab.eeeabsmobs.sever.entity.mob.immortal;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.OverlapAnimationState;
import com.eeeab.animate.server.animation.keyframe.Keyframe;
import com.eeeab.animate.server.animation.keyframe.KeyframeManager;
import com.eeeab.animate.server.animation.release.AnimationReleaseManager;
import com.eeeab.animate.server.animation.release.AnimationRule;
import com.eeeab.animate.server.animation.release.ConditionFactory;
import com.eeeab.animate.server.animation.release.cooldown.FixedRangeCooldown;
import com.eeeab.animate.server.animation.release.cooldown.HealthScaledCooldown;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData.KeyTrack;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.Attractor;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent.PropertyOverLength;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedParticleData;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.ModBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ModLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate.*;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalMagicCircle.MagicCircleType;
import com.eeeab.eeeabsmobs.sever.entity.mob.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.TickBasedProbabilityBooster;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.DamageAdaptation;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.ModDamageSource;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.ModMathUtils;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import com.eeeab.eeeabsmobs.sever.util.StackTraceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class EntityImmortalBoss extends EntityAbsImmortal implements IBoss {
    public static final Animation DIE_ANIMATION = Animation.create(60);
    public static final Animation SPAWN_ANIMATION = Animation.create(100);
    public static final Animation SWITCH_STAGE_ANIMATION = Animation.create(100);
    public static final Animation TELEPORT_ANIMATION = Animation.create(25).doesOverlap();
    public static final Animation SMASH_GROUND_ANIMATION1 = Animation.create(45);
    public static final Animation SMASH_GROUND_ANIMATION2 = Animation.create(80);
    public static final Animation SMASH_GROUND_ANIMATION3 = Animation.create(50);
    public static final Animation PUNCH_RIGHT_ANIMATION = Animation.create(40).doesOverlap();
    public static final Animation PUNCH_LEFT_ANIMATION = Animation.create(40).doesOverlap();
    public static final Animation HARDPUNCH_RIGHT_ANIMATION = Animation.create(45).doesOverlap();
    public static final Animation HARDPUNCH_LEFT_ANIMATION = Animation.create(45).doesOverlap();
    public static final Animation POUNCE_PRE_ANIMATION = Animation.create(15);
    public static final Animation POUNCE_HOLD_ANIMATION = Animation.create(42);
    public static final Animation POUNCE_END_ANIMATION = Animation.create(15);
    public static final Animation POUNCE_SMASH_ANIMATION = Animation.create(50).setScale(1.2F);
    public static final Animation POUNCE_PICK_ANIMATION = Animation.create(40);
    public static final Animation ATTRACT_ANIMATION = Animation.create(70).doesOverlap();
    public static final Animation SHORYUKEN_ANIMATION = Animation.create(80).doesOverlap();
    public static final Animation TRACKING_SHURIKEN_ANIMATION = Animation.create(25).doesOverlap();
    public static final Animation UNLEASH_ENERGY_ANIMATION = Animation.create(100);
    public static final Animation ARMBLOCK_ANIMATION = Animation.create(15);
    public static final Animation ARMBLOCK_HOLD_ANIMATION = Animation.create(50);
    public static final Animation ARMBLOCK_END_ANIMATION = Animation.create(10);
    public static final Animation ARMBLOCK_COUNTERATTACK_ANIMATION = Animation.create(20).doesOverlap();
    public static final Animation HURT_ANIMATION1 = Animation.create(60).doesOverlap();
    public static final Animation STUN_ANIMATION = Animation.create(100);
    private static final Animation[] ANIMATIONS = new Animation[]{
            DIE_ANIMATION,
            SPAWN_ANIMATION,
            //SWITCH_STAGE_ANIMATION,
            TELEPORT_ANIMATION,
            ARMBLOCK_ANIMATION,
            ARMBLOCK_HOLD_ANIMATION,
            ARMBLOCK_END_ANIMATION,
            ARMBLOCK_COUNTERATTACK_ANIMATION,
            PUNCH_RIGHT_ANIMATION,
            HARDPUNCH_RIGHT_ANIMATION,
            PUNCH_LEFT_ANIMATION,
            HARDPUNCH_LEFT_ANIMATION,
            SMASH_GROUND_ANIMATION1,
            SMASH_GROUND_ANIMATION2,
            SMASH_GROUND_ANIMATION3,
            POUNCE_PRE_ANIMATION,
            POUNCE_HOLD_ANIMATION,
            POUNCE_END_ANIMATION,
            POUNCE_SMASH_ANIMATION,
            POUNCE_PICK_ANIMATION,
            ATTRACT_ANIMATION,
            SHORYUKEN_ANIMATION,
            TRACKING_SHURIKEN_ANIMATION,
            UNLEASH_ENERGY_ANIMATION,
            HURT_ANIMATION1,
            STUN_ANIMATION,
    };
    private final static KeyframeManager<EntityImmortalBoss> KEYFRAME_MANAGER;
    private static final AnimationReleaseManager<EntityImmortalBoss> ANIMATION_RELEASE_MANAGER;
    private static final int MAX_BLOCK_HURT_COUNT = 5;
    private static final int TIME_UNTIL_TELEPORT = 450;
    private final float MAX_COUNTERATTACK_DAMAGE_AMOUNT_THRESHOLD = getMaxHealth() * 0.075F;
    private final float MAX_CUMULATIVE_COUNTERATTACK_DAMAGE_THRESHOLD = getMaxHealth() * 0.1F;
    private static final Predicate<LivingEntity> TARGET_CONDITIONS = entity -> entity.isAlive() && !entity.getType().is(ModTagKey.IMMORTAL_IGNORE_HUNT_TARGETS) && entity.isAttackable() && (entity instanceof Enemy || entity instanceof NeutralMob || (entity instanceof Player player && !player.isCreative() && !player.isSpectator()));
    private static final float[][] BLOCK_OFFSETS = {{-0.75F, -0.75F}, {-0.75F, 0.75F}, {0.75F, 0.75F}, {0.75F, -0.75F}};
    private static final ParticleComponent[] ATTRACT_COMPONENT = {new PropertyControl(EnumParticleProperty.RED, AnimData.startAndEnd(0.3F, 0.56F), false), new PropertyControl(EnumParticleProperty.GREEN, AnimData.startAndEnd(0.388F, 0.85F), false), new PropertyControl(EnumParticleProperty.BLUE, AnimData.startAndEnd(0.55F, 0.98F), false),};
    private static final EntityDataAccessor<Byte> DATA_STAGE = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_TELEPORT_TYPE = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_KATANA_HOLD = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ALWAYS_ACTIVE = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BOOLEAN);
    private int hurtCount;
    private int invalidAttackCount;
    private float damageAmountCumulative;
    private int stunCount;
    private int timeUntilBlock;
    private int blockingHurtCount;
    private int battleTimestamp;
    private int destroyBlocksTick;
    private int closeProximityTickCount;
    private int unableAttackTickCount;
    private int universalCDTime;
    private boolean shouldBlock;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] hand;//0:left 1:right
    private boolean LRFlag;//T:left F:right
    private Vec3 lPreHandPos = Vec3.ZERO;
    private Vec3 rPreHandPos = Vec3.ZERO;
    private final DamageAdaptation damageAdaptation;
    private final TickBasedProbabilityBooster smashDerivedSkillProb;
    private final TickBasedProbabilityBooster hardpunchDerivedSkillProb;
    private final OverlapAnimationState attractState = new OverlapAnimationState(ATTRACT_ANIMATION);
    private final OverlapAnimationState shoryukenState = new OverlapAnimationState(SHORYUKEN_ANIMATION);
    private final OverlapAnimationState hurtAnimationState = new OverlapAnimationState(HURT_ANIMATION1);
    private final OverlapAnimationState teleportAnimationState = new OverlapAnimationState(TELEPORT_ANIMATION);
    private final OverlapAnimationState punchRightAnimationState = new OverlapAnimationState(PUNCH_RIGHT_ANIMATION);
    private final OverlapAnimationState punchLeftAnimationState = new OverlapAnimationState(PUNCH_LEFT_ANIMATION);
    private final OverlapAnimationState hardPunchRightAnimationState = new OverlapAnimationState(HARDPUNCH_RIGHT_ANIMATION);
    private final OverlapAnimationState hardPunchLeftAnimationState = new OverlapAnimationState(HARDPUNCH_LEFT_ANIMATION);
    private final OverlapAnimationState trackingShurikenAnimationState = new OverlapAnimationState(TRACKING_SHURIKEN_ANIMATION);
    private final OverlapAnimationState armBlockCounterattackAnimationState = new OverlapAnimationState(ARMBLOCK_COUNTERATTACK_ANIMATION);
    private List<LivingEntity> targets = List.of();
    public final ControlledAnimation coreControlled = new ControlledAnimation(10);
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);
    public final ControlledAnimation alphaControlled = new ControlledAnimation(10);
    public final ControlledAnimation hurtControlled = new ControlledAnimation(10);

    public EntityImmortalBoss(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
        this.active = false;
        this.clearRedundantAnimationsOnDeath = true;
        this.LRFlag = level.random.nextBoolean();
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.damageAdaptation = new DamageAdaptation(ModConfigHandler.COMMON.mobs.immortals.immortal.adaptConfig);
        this.smashDerivedSkillProb = new TickBasedProbabilityBooster(0F, 0.2F, 1F);
        this.hardpunchDerivedSkillProb = new TickBasedProbabilityBooster(0F, 0.05F, 1F);
        if (this.level().isClientSide) {
            this.hand = new Vec3[]{new Vec3(0, 0, 0), new Vec3(0, 0, 0)};
        }
    }

    static {
        KEYFRAME_MANAGER = setupAnimations();
        ANIMATION_RELEASE_MANAGER = setupAnimationRules();
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.EPIC_BOSS;
    }

    @Override//可以站立的流体
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.LAVA);
    }

    @Override//添加药水效果
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (!ModEntityUtils.isBeneficial(effectInstance.getEffect())) {
            reflectPotionEffect(effectInstance, entity);
            return false;
        }
        return this.isActive() && super.addEffect(effectInstance, entity);
    }

    @Override//强制添加药水效果
    public void forceAddEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (!ModEntityUtils.isBeneficial(effectInstance.getEffect())) {
            reflectPotionEffect(effectInstance, entity);
            return;
        }
        if (this.isActive()) super.forceAddEffect(effectInstance, entity);
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
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean isDeadOrDying() {
        return super.isDeadOrDying() /*&& this.getStage() == ImmortalStage.STAGE2*/;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
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
    protected ModConfigHandler.BossConfig getBossConfig() {
        return ModConfigHandler.COMMON.mobs.immortals.immortal.bossConfig;
    }

    @Override
    public Map<MobEffect, MobEffectInstance> getActiveEffectsMap() {
        if (StackTraceUtils.isNotMinecraftOrMyModInvoking()) return Map.of();
        return super.getActiveEffectsMap();
    }

    @Override
    public boolean removeAllEffects() {
        if (StackTraceUtils.isNotMinecraftOrMyModInvoking()) return false;
        return super.removeAllEffects();
    }

    @Override
    public void kill() {
        if (StackTraceUtils.isNotMinecraftOrMyModInvoking()) return;
        super.kill();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return !this.isActive() || ((animation == SWITCH_STAGE_ANIMATION || animation == TELEPORT_ANIMATION || animation == POUNCE_HOLD_ANIMATION || animation == ARMBLOCK_COUNTERATTACK_ANIMATION) || super.isInvulnerableTo(damageSource));
    }

    @Override
    public boolean isInvisible() {
        return !this.alphaControlled.isStop() || super.isInvisible();
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    //@Override
    //public void heal(float healAmount) {
    //    MinecraftForge.EVENT_BUS.post(new LivingHealEvent(this, healAmount));
    //    if (healAmount <= 0) return;
    //    float f = this.getHealth();
    //    if (f > 0.0F) {
    //        this.setHealth(f + healAmount);
    //    }
    //}

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.immortals.immortal.combatConfig;
    }

    @Override
    protected boolean canShowBossBar() {
        return this.isActive();
    }

    @Override
    protected boolean setDarkenScreen() {
        return this.getAnimation() == UNLEASH_ENERGY_ANIMATION;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.isDeadOrDying()) {
            this.targets = List.of();
            this.damageAdaptation.clearCache();
        }
    }

    @Override
    protected void onAnimationStart(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == ARMBLOCK_COUNTERATTACK_ANIMATION) {
                this.blockingHurtCount = 0;
            }
            if (animation == STUN_ANIMATION) {
                if (this.stunCount > 1) {
                    this.damageAdaptation.clearCache();
                    this.universalCDTime = animation.getDuration();
                    this.stunCount = 0;
                }
            } else this.unableAttackTickCount = 0;
        }
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (this.isSwitching()) this.setHealth(this.getMaxHealth());
            if (animation == ARMBLOCK_END_ANIMATION) {
                this.blockingHurtCount = 0;
                this.blockEntity = null;
            } else if (animation == SMASH_GROUND_ANIMATION3) {
                if (blockEntity != null) this.universalCDTime = 5;
                this.blockEntity = null;
            } else if (animation == TRACKING_SHURIKEN_ANIMATION) {
                if (this.getTarget() instanceof Player && this.targets.size() < 2) this.universalCDTime = 10;
            } else if (animation == HURT_ANIMATION1) {
                this.blockEntity = null;
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityAbsImmortal.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, TARGET_CONDITIONS));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7D) {
            @Override
            public boolean canUse() {
                return EntityImmortalBoss.this.isActive() && super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new ModLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && EntityImmortalBoss.this.isActive();
            }
        });
        this.registerCustomGoals();
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(0, new ImmortalGroupAI(this, SWITCH_STAGE_ANIMATION, TELEPORT_ANIMATION));
        this.goalSelector.addGoal(0, new ImmortalBlockGoal(this));
        this.goalSelector.addGoal(1, new ImmortalSpecialGoal(this));
        this.goalSelector.addGoal(1, new ImmortalComboGoal(this));
        this.goalSelector.addGoal(1, new ImmortalMagicGoal(this));
        this.goalSelector.addGoal(1, new ImmortalPounceGoal(this));
        this.goalSelector.addGoal(1, new ImmortalShakeGroundGoal(this));
        this.goalSelector.addGoal(2, new ImmortalStunGoal(this));
        this.goalSelector.addGoal(2, new AnimationDie<>(this));
        this.goalSelector.addGoal(2, new AnimationActivate<>(this, SPAWN_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.05D, 0) {
            @Override
            public void tick() {
                if (this.attacker.universalCDTime > 0) {
                    LivingEntity target = this.attacker.getTarget();
                    if (target != null) this.attacker.getLookControl().setLookAt(target);
                    if (this.attacker.isPathFinding()) this.attacker.getNavigation().stop();
                } else super.tick();
            }
        });
    }

    @Override
    public void tick() {
        this.setYRot(this.yBodyRot);
        super.tick();
        this.floatImmortal();
        this.alphaControlled.updatePrevTimer();
        this.glowControlled.updatePrevTimer();
        this.coreControlled.updatePrevTimer();
        this.hurtControlled.updatePrevTimer();

        int tick = this.getAnimationTick();
        Animation animation = this.getAnimation();
        if (this.isNoAnimation() || ((animation != TELEPORT_ANIMATION && animation != POUNCE_HOLD_ANIMATION && animation != SHORYUKEN_ANIMATION && animation != SMASH_GROUND_ANIMATION2)
                && !(animation != ATTRACT_ANIMATION || tick >= 40))) {
            this.pushEntitiesAway(1.9F, getBbHeight(), 1.9F, 1.9F);
        }

        if (!this.isActive()) this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

        this.yRotO = this.getYRot();
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = this.getTarget();
        if (this.isSwitching()) this.heal(this.getMaxHealth() / SWITCH_STAGE_ANIMATION.getDuration());
        boolean canAttack = target != null && !this.isSwitching() && this.universalCDTime <= 0;

        if (canAttack) {
            if (this.battleTimestamp == 0) this.battleTimestamp = this.tickCount;
            ANIMATION_RELEASE_MANAGER.tick(this, this.getCooldownManager());
            if (this.targetDistance <= 5 && Math.abs(this.getY() - target.getY()) <= 5) {
                this.closeProximityTickCount++;
                if (this.getCumulativeBattleTick() > 3600 || this.getHealthPercentage() < 0.5F) this.closeProximityTickCount++;
            } else if (tickCount % 2 == 0) {
                this.unableAttackTickCount++;
                if (this.closeProximityTickCount > 0 && this.closeProximityTickCount < TIME_UNTIL_TELEPORT) this.closeProximityTickCount--;
            }
        }
        if (target == null && this.tickCount % 100 == 0 && this.battleTimestamp > 0) this.battleTimestamp = (int) (this.battleTimestamp * 0.5);
        if (this.tickCount % 300 == 0) this.damageAdaptation.updateCache(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.universalCDTime > 0) this.universalCDTime--;
            if (!this.inBlocking() && this.timeUntilBlock > 0) this.timeUntilBlock--;
            if (!this.inBlocking() && this.tickCount % 20 == 0 && this.hurtTime <= 0) this.hurtCount = 0;
            if (!this.isNoAi() && this.destroyBlocksTick > 0) {
                this.destroyBlocksTick--;
                if (this.destroyBlocksTick == 0) {
                    ModEntityUtils.breakBlocksInRect(this.level(), this, 50F, 2, 5, 2, 0, 0, true);
                }
            }
            if (this.isAlive() && !this.isNoAi() && this.tickCount % 30 == 0 && this.getTarget() != null) {
                this.targets = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32, 6, 32), e -> this.getTarget() == e || TARGET_CONDITIONS.test(e));
            }
        }
        int tick = this.getAnimationTick();
        Animation animation = this.getAnimation();
        this.hurtControlled.decreaseTimer();
        this.glowControlled.incrementOrDecreaseTimer(this.isActive() && !this.isDeadOrDying() /*&& (animation != SPAWN_ANIMATION || tick > 25)*/);
        this.coreControlled.incrementOrDecreaseTimer(!this.isNoAnimation() && (animation != SPAWN_ANIMATION && animation != UNLEASH_ENERGY_ANIMATION) && !this.inBlocking());
        this.alphaControlled.incrementOrDecreaseTimer(animation == TELEPORT_ANIMATION && tick >= 10 && tick <= 20, 2);
        if (animation == TELEPORT_ANIMATION) {
            LivingEntity target = this.getTarget();
            if (tick == 15) {
                boolean teleportFlag = target != null;
                for (int i = 0; i < 16; i++) {
                    if (this.teleportByType(this.getTeleportType(), target)) {
                        teleportFlag = false;
                        break;
                    }
                }
                if (teleportFlag) {
                    for (int i = 0; i < 16; i++) {
                        if (this.teleportByType(TeleportType.SNEAK, target)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            if (this.inBlocking()) this.hurtTime = 0;
            return false;
        }
        this.hurtCount++;
        this.lastDamageSource = source;
        Entity entity = source.getEntity();
        byte pierceLevel = 0;
        if (source.getDirectEntity() instanceof AbstractArrow arrow) {
            pierceLevel = arrow.getPierceLevel();
        }
        boolean bypassesInvul = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        boolean fullyAdapted = this.damageAdaptation.isFullyAdapted(this, source);
        if (entity != null) {
            float attackArc = 100F;
            float relativeAngle = ModEntityUtils.getTargetRelativeAngle(this, entity.position());
            boolean inFront = relativeAngle <= attackArc / 1.5F && relativeAngle >= -attackArc / 1.5F;
            boolean inBack = (relativeAngle <= -180F + attackArc / 2F && relativeAngle >= -180F - attackArc / 2F)
                    || (relativeAngle >= 180F - attackArc / 2F && relativeAngle <= 180F + attackArc / 2F);

            if (!this.shouldBlock && this.getHealthPercentage() < 0.5F &&
                    (this.hurtCount >= MAX_BLOCK_HURT_COUNT || damage > MAX_CUMULATIVE_COUNTERATTACK_DAMAGE_THRESHOLD || fullyAdapted)) {
                this.shouldBlock = true;
            }

            boolean bypassesArmor = source.is(DamageTypeTags.BYPASSES_ARMOR);
            if (!(bypassesArmor || pierceLevel > 0) && !inFront && !inBack) {
                damage *= 0.7F;
            }
        /*
            防守【格挡】/【反击】机制:
            当伤害源来自正面且并非绕过无敌、伤害值>1、在无动作或可打断动作状态下可触发【格挡】
            在【格挡】期间受到伤害减少90%与具有更少的攻击间隔，此时再次受到伤害时会延续格挡时间；反之则会再没有受到任何伤害的2.5秒后停止格挡
            当格挡次数达到5次或伤害超过阈值时，触发【反击】
            格挡冷却时间 = 150tick + 每1点伤害 × 3.3333tick
         */
            boolean canBlock = !this.isNoAi() && this.timeUntilBlock <= 0
                    && (this.inBlocking() || (damage > 1F && !bypassesInvul && inFront && this.isNoAnimation()));
            if (canBlock) {
                if (entity instanceof LivingEntity living) this.blockEntity = living;
                float pitch = 0.4F * ModMathUtils.getTickFactor(damage, MAX_BLOCK_HURT_COUNT, false);
                boolean shouldCounter = damage > MAX_COUNTERATTACK_DAMAGE_AMOUNT_THRESHOLD
                        || ++this.blockingHurtCount >= MAX_BLOCK_HURT_COUNT
                        || (this.damageAmountCumulative += damage) > MAX_CUMULATIVE_COUNTERATTACK_DAMAGE_THRESHOLD
                        || fullyAdapted;
                if (shouldCounter) {
                    this.damageAmountCumulative = 0;
                    this.timeUntilBlock = 150 + (int) Math.min(damage * 3.3333F, 150F);
                    this.playSound(SoundInit.IMMORTAL_BLOCKING_COUNTER.get(), 1.5F, 1.3F - pitch);
                    this.damageAdaptation.adaptToDamage(this.tickCount, source, 0.5F);
                    this.playAnimation(ARMBLOCK_COUNTERATTACK_ANIMATION);
                    return false;
                }
                if (this.isNoAnimation()) {
                    this.playAnimation(ARMBLOCK_ANIMATION);
                } else {
                    this.playAnimation(ARMBLOCK_HOLD_ANIMATION);
                }
                this.playSound(SoundInit.IMMORTAL_BLOCKING.get(), 1F, this.getVoicePitch() + pitch - 0.2F);
                this.level().broadcastEntityEvent(this, (byte) 5);
                this.invulnerableTime /= 2;
                super.hurt(source, Math.min(damage * 0.1F, getHealth() * 0.025F));
                return false;
            }
        }
        damage *= 1F - Mth.clamp(this.targets.size() - 1, 0F, 5F) * 0.1F;
        if (entity != null || bypassesInvul) {
            float originalDamage = damage;
            if (this.getAnimation() == UNLEASH_ENERGY_ANIMATION) {
                damage = Math.min(damage * 0.1F, getHealth() * 0.025F);
            }
            boolean flag = super.hurt(source, damage);
            if (flag) {
                this.stunCheck(source, originalDamage);
            }
            return flag;
        } else if (this.destroyBlocksTick <= 0) {
            this.destroyBlocksTick = 20;
        }
        return false;
    }

    @Override
    protected boolean checkAttackDistance(DamageSource source, double distSqr, Entity entity) {
        if (source.is(DamageTypeTags.IS_EXPLOSION))
            distSqr = Math.min(distSqr * 2, 4096);//上限为配置最大值
        return super.checkAttackDistance(source, distSqr, entity);
    }

    @Override
    public void setHealth(float health) {
        if (!this.level().isClientSide && health < this.getHealth()) {
            if (this.isSwitching()) return;
            if (this.getAnimation() != STUN_ANIMATION) {
                float nowHealth = this.getHealth();
                float damage = nowHealth - health;
                float multiplier = 1 + Mth.clamp(this.targets.size() - 1, 0, 5) * 0.2F;
                health = nowHealth - this.damageAdaptation.adaptToDamage(this, this.lastDamageSource, damage, multiplier);
                if (this.damageAdaptation.isFullyAdapted(this, this.lastDamageSource)) this.level().broadcastEntityEvent(this, (byte) 4);
                this.lastDamageSource = null;
            }
        }
        super.setHealth(health);
        //if (this.getHealth() <= 0 && this.getStage() == ImmortalStage.STAGE1) this.nextBossStage();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.hurtControlled.setTimer(10);
        } else if (id == 5) { //Block collision
            this.doCollisionEffect(10, 15F, 0.3F);
        } else if (id == 6) { //Punch hit
            this.doHitEffect(5, 0, 15F, 4.9F, 0.45F, false);
        } else if (id == 7) { //Punch thump hit
            this.doHitEffect(4, 0, 17F, 4.5F, 0.5F, true);
        } else if (id == 8) { //Pounce pick hit
            this.doHitEffect(5, -20F, 20F, 3F, 0.6F, false);
        } else if (id == 9) { //Attract pre hit
            this.doAttractHitEffect();
        } else if (id == 10) { //Attract hit
            this.doHitEffect(5, 0, 20F, 4.8F, 0.4F, true);
        } else if (id == 11) { //Attract lose
            Vec3 pos = this.getPosOffset(true, 1.5F, this.getBbWidth() * 0.6F, this.getBbHeight() * 0.25F);
            ModParticleUtils.roundParticleOutburst(this.level(), 50, new ParticleOptions[]{new ParticleOrb.OrbData(0.46F, 0.75F, 0.88F, 3, 30)}, pos.x, pos.y, pos.z, 0.8F);
        } else super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STAGE, ImmortalStage.STAGE1.id);
        this.entityData.define(DATA_TELEPORT_TYPE, TeleportType.RANDOM.id);
        this.entityData.define(DATA_KATANA_HOLD, ImmortalStage.STAGE1.holdKatana);
        this.entityData.define(DATA_ALWAYS_ACTIVE, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.isSwitching()) {
            this.setHoldKatana(this.getStage().holdKatana);
            this.setHealth(this.getMaxHealth());
        }
        compound.putByte("bossStage", this.getStage().id);
        compound.putBoolean("isActive", this.isActive());
        //compound.putBoolean("isAlwaysActive", this.isAlwaysActive());
        compound.putBoolean("holdKatana", this.isHoldKatana());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_STAGE, compound.getByte("bossStage"));
        this.setActive(compound.getBoolean("isActive"));
        //this.setAlwaysActive(compound.getBoolean("isAlwaysActive"));
        this.setHoldKatana(compound.getBoolean("holdKatana"));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        /*ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.AIR)) {
            return InteractionResult.PASS;
        } else if (this.isActive() || this.isNoAi()) {
            return InteractionResult.PASS;
        } else if (itemStack.is(ItemInit.GUARDIAN_CORE.get())) {
            if (!player.getAbilities().instabuild) itemStack.shrink(1);
            this.playAnimation(SPAWN_ANIMATION);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;*/
        if (this.isActive() || this.isNoAi()) {
            return InteractionResult.PASS;
        }
        this.playAnimation(SPAWN_ANIMATION);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        //this.setActive(false);
        return spawnDataIn;
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        float maxHealth = this.getMaxHealth();
        this.heal(Math.min(entity.getMaxHealth() * 0.05F + maxHealth * 0.025F, maxHealth * 0.1F));
        return super.killedEntity(level, entity);
    }

    private static KeyframeManager<EntityImmortalBoss> setupAnimations() {
        KeyframeManager<EntityImmortalBoss> manager = new KeyframeManager<>();
        KeyframeManager.KeyframeManegerBuilder<EntityImmortalBoss> builder = manager.builder();
        Keyframe<EntityImmortalBoss> punchKeyframe = (entity, animation, tick) -> {
            boolean right = animation == PUNCH_RIGHT_ANIMATION;
            entity.doTrailEffect(animation, tick == 11, tick > 11 && tick <= 15, !right);
            if (tick == 12) entity.doPuncturedAirFlowEffect(right);
        };
        builder.forAnimation(PUNCH_RIGHT_ANIMATION).inRange(11, 15, punchKeyframe);
        builder.forAnimation(PUNCH_LEFT_ANIMATION).inRange(11, 15, punchKeyframe);
        Keyframe<EntityImmortalBoss> hardpunchKeyframe = (entity, animation, tick) -> {
            boolean right = animation == HARDPUNCH_RIGHT_ANIMATION;
            entity.doTrailEffect(animation, tick == 19, tick > 19 && tick <= 23, !right);
            if (tick == 20 || tick == 22) entity.doPuncturedAirFlowEffect(right);
        };
        builder.forAnimation(HARDPUNCH_RIGHT_ANIMATION).inRange(19, 23, hardpunchKeyframe);
        builder.forAnimation(HARDPUNCH_LEFT_ANIMATION).inRange(19, 23, hardpunchKeyframe);
        builder.forAnimation(ARMBLOCK_COUNTERATTACK_ANIMATION).atTick(2, (entity, animation, tick) -> {
                    entity.doShakeGroundEffect(15, 0F, 0F, 0.5F, 0.94F, false, false);
                    entity.doCollisionEffect(0, 30F, 0.5F);
                }).atTick(3, (entity, animation, tick) -> entity.shakeGround(0F, 20F, 0.2F, 3, 0))
                .atTick(12, (entity, animation, tick) -> entity.doImmortalMagicMatrixEffect(MagicCircleType.POWER, 7, 1.5F, 0.5F, 9F));
        builder.forAnimation(SMASH_GROUND_ANIMATION1).inRange(17, 21, (entity, animation, tick) -> {
            entity.doTrailEffect(animation, tick == 17, tick > 17 && tick <= 21, null);
            if (tick == 20) entity.doShakeGroundEffect(12, 2.5F, 0F, 0.58F, 0.94F, false, true);
        });
        builder.forAnimation(SMASH_GROUND_ANIMATION2).inRange(13, 17, (entity, animation, tick) -> {
            entity.doTrailEffect(animation, tick == 13, tick > 13 && tick <= 17, null);
            if (tick == 16) entity.doShakeGroundEffect(14, 2.2F, 0F, 0.55F, 0.945F, false, true);
        });
        builder.forAnimation(SMASH_GROUND_ANIMATION3).inRange(10, 21, (entity, animation, tick) -> {
            entity.doTrailEffect(animation, tick == 10, tick > 10 && tick <= 20, null);
            if (tick == 19) {
                float width = entity.getBbWidth() / 2;
                entity.doShakeGroundEffect(9, 2.4F, width, 0.5F, 0.91F, false, false);
                entity.doShakeGroundEffect(9, 2.4F, width, 0.5F, 0.91F, true, false);
            } else if (entity.level().isClientSide && tick == 21) {
                Vec3 pos = entity.getPosOffset(false, 2.4F, 0F, 0.2F);
                ParticleDust.DustData particle = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.27f, 0.25f, 0.23f, 50f, 38, ParticleDust.EnumDustBehavior.GROW, 0.952F);
                ModParticleUtils.annularParticleOutburst(entity.level(), 12, particle, pos.x, pos.y, pos.z, 1.75F, 0.5F, 126F, 2F, entity.getYRot());
                ModParticleUtils.annularParticleOutburst(entity.level(), 8, ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 0.5F, 0.1F, 130F, 1F, entity.getYRot());
            }
        });
        builder.forAnimation(POUNCE_PRE_ANIMATION).atTick(5, (entity, animation, tick) -> entity.doImmortalMagicMatrixEffect(MagicCircleType.SPEED, 5, 2.5F, 0.4F, 8F));
        builder.forAnimation(POUNCE_SMASH_ANIMATION).inRange(8, 11, (entity, animation, tick) -> {
            if (tick < 10 && entity.level().isClientSide && entity.getDeltaMovement().horizontalDistanceSqr() > 0.1) {
                ModParticleUtils.blockParticleDirectionality(entity.level(), entity.getX(), entity.getY() - 0.1, entity.getZ(), Math.toRadians(entity.getYRot()), 10, BLOCK_OFFSETS, 5F);
            } else if (tick == 10) entity.doShakeGroundEffect(15, 3.7F, 0F, 0.58F, 0.93F, false, true);
        });
        builder.forAnimation(POUNCE_PICK_ANIMATION).inRange(1, 11, (entity, animation, tick) -> {
            entity.doTrailEffect(animation, tick == 7, tick > 7 && tick < 12, null);
            if (tick < 10 && entity.level().isClientSide && entity.getDeltaMovement().horizontalDistanceSqr() > 0.1) {
                ModParticleUtils.blockParticleDirectionality(entity.level(), entity.getX(), entity.getY() - 0.1, entity.getZ(), Math.toRadians(entity.getYRot()), 10, BLOCK_OFFSETS, 5F);
            }
        });
        builder.forAnimation(POUNCE_HOLD_ANIMATION).atTick(1, (entity, animation, tick) -> {
            if (!entity.isSilent()) entity.level().playLocalSound(entity.blockPosition(), SoundInit.IMMORTAL_SUBSONIC.get(), entity.getSoundSource(), 0.4F, 1.5F, false);
            entity.doHitEffect(10, entity.random.nextInt(10), 60, 0, 0.55F, true);
            ParticleDust.DustData particle = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.28f, 0.26f, 0.24f, 50f, 16, ParticleDust.EnumDustBehavior.SHRINK, 1f);
            ModParticleUtils.roundParticleOutburst(entity.level(), 10, new ParticleOptions[]{particle}, entity.getX(), entity.getY(0.25), entity.getZ(), 1F);
            for (LivingEntity hit : entity.getNearByLivingEntities(5F)) {
                double angle = entity.getAngleBetweenEntities(entity, hit);
                double x = Math.cos(Math.toRadians(angle - 90));
                double z = Math.sin(Math.toRadians(angle - 90));
                hit.setDeltaMovement(x, 0.25, z);
                if (hit instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(hit));
                }
            }
        }).inRange(2, 41, (entity, animation, tick) -> {
            if (entity.level().isClientSide && entity.getDeltaMovement().horizontalDistanceSqr() > 0.1) {
                if (tick < 8 && tick % 2 == 0) {
                    entity.level().addParticle(new ParticleRing.RingData((float) Math.toRadians(-entity.getYRot()), (float) Math.toRadians(-entity.getXRot()),
                            40, 0.56F, 0.85F, 0.98F, 0.14F, 90f - 20f * (tick / 6f), false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), entity.getX() + entity.getDeltaMovement().x * 1.5, entity.getY(0.5), entity.getZ() + entity.getDeltaMovement().z * 1.5, 0, 0, 0);
                }
                ModParticleUtils.blockParticleDirectionality(entity.level(), entity.getX(), entity.getY() - 0.1, entity.getZ(), Math.toRadians(entity.getYRot()), 10, BLOCK_OFFSETS, 5F);
            }
        });
        builder.forAnimation(ATTRACT_ANIMATION).inRange(1, 42, (entity, animation, tick) -> {
            entity.doAttractEffect();
            if (tick == 32) entity.doImmortalMagicMatrixEffect(MagicCircleType.POWER, 5, 1.5F, 0.45F, 9F);
            entity.doTrailEffect(animation, tick == 35, tick > 35 && tick <= 42, false);
        });
        builder.forAnimation(HURT_ANIMATION1).atTick(15, (entity, animation, tick) -> entity.doShakeGroundEffect(10, 1F, entity.getBbWidth() * 0.6F, 0.55F, 0.91F, true, true));
        builder.forAnimation(SHORYUKEN_ANIMATION).inRange(14, 20, (entity, animation, tick) -> entity.doTrailEffect(animation, tick == 14, tick > 14 && tick <= 20, false))
                .inRange(44, 48, (entity, animation, tick) -> {
                    entity.doTrailEffect(animation, tick == 44, tick > 44 && tick <= 48, true);
                    if (tick == 47) entity.doShakeGroundEffect(12, 4.75F, 0F, 0.58F, 0.92F, false, true);
                });
        builder.forAnimation(TRACKING_SHURIKEN_ANIMATION).atTick(8, (entity, animation, tick) -> entity.doImmortalMagicMatrixEffect(MagicCircleType.HARMFUL, 12, 2F, 0.5F, 15F));
        builder.forAnimation(UNLEASH_ENERGY_ANIMATION).atTick(1, (entity, animation, tick) -> {
            if (entity.level().isClientSide && !entity.isSilent()) entity.level().playLocalSound(entity.blockPosition(), SoundInit.IMMORTAL_ACCUMULATING.get(),
                    entity.getSoundSource(), 1.5F, 1F, false);
        }).inRange(30, 79, (entity, animation, tick) -> {
            if (tick > 35) entity.strongKnockBlock();
            entity.doUnleashEnergyEffect();
            if (tick == 80) if (!entity.isSilent()) entity.level().playLocalSound(entity.blockPosition(), SoundInit.IMMORTAL_ACCUMULATING_END.get(), entity.getSoundSource(), 1.5F, 1F, false);
        });
        builder.forAnimation(STUN_ANIMATION).everyNTick(1, 79, 20, (entity, animation, tick) -> entity.glowControlled.resetTimer());
        builder.forAnimation(SPAWN_ANIMATION).inRange(60, 100, (entity, animation, tick) -> {
            if (!entity.isActive()) entity.setActive(true);
        });
        return manager;
    }

    private static AnimationReleaseManager<EntityImmortalBoss> setupAnimationRules() {
        AnimationReleaseManager<EntityImmortalBoss> manager = new AnimationReleaseManager<>();
        AnimationReleaseManager.Builder<EntityImmortalBoss> builder = manager.builder();
        Consumer<EntityImmortalBoss> addSkillProbOnSuccess = entity -> {
            if (entity.getHealthPercentage() < 0.8F) entity.smashDerivedSkillProb.onTick();
        };
        FixedRangeCooldown smashGround = new FixedRangeCooldown(360, 60, true);

        AnimationRule<EntityImmortalBoss> blockRule = builder.define(ARMBLOCK_ANIMATION)
                .priority(1)
                .onlyCombo()
                .cooldown(entity -> 300)
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.shouldBlock,
                        ConditionFactory.distanceRange(0, 3.5, 4)
                ))
                .onSuccess(entity -> {
                    entity.shouldBlock = false;
                    if (entity.timeUntilBlock > 0) entity.timeUntilBlock = 0;
                })
                .build();

        AnimationRule<EntityImmortalBoss> smashGroundRule = builder.define(SMASH_GROUND_ANIMATION1)
                .cooldown(smashGround)
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.getCumulativeBattleTick() > 1200 || entity.getHealthPercentage() < 0.9,
                        ConditionFactory.randomChanceOnHighHealth(0.5F, 0.3F),
                        ConditionFactory.distanceRange(0, 4, 5)
                ))
                .onSuccess(entity -> {
                    addSkillProbOnSuccess.accept(entity);
                    entity.getCooldownManager().setCD(SMASH_GROUND_ANIMATION2, smashGround.generate(entity));
                })
                .build();

        AnimationRule<EntityImmortalBoss> smashGround2Rule = builder.define(SMASH_GROUND_ANIMATION2)
                .cooldown(smashGround)
                .condition(ConditionFactory.and(
                        ConditionFactory.randomChanceOnLowHealth(0F, 0.7F),
                        ConditionFactory.heightDiff(6),
                        ConditionFactory.distanceRange(0, 8, 10)
                ))
                .onSuccess(entity -> {
                    addSkillProbOnSuccess.accept(entity);
                    entity.getCooldownManager().setCD(SMASH_GROUND_ANIMATION1, smashGround.generate(entity));
                })
                .build();

        AnimationRule<EntityImmortalBoss> smashGround3Rule = builder.define(SMASH_GROUND_ANIMATION3)
                .cooldown(smashGround)
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.random.nextFloat() < entity.smashDerivedSkillProb.getProbability(),
                        ConditionFactory.distanceRange(0, 8, 10)
                ))
                .onSuccess(entity -> entity.smashDerivedSkillProb.resetProbability())
                .build();

        AnimationRule<EntityImmortalBoss> pounceRule = builder.define(POUNCE_PRE_ANIMATION)
                .cooldown(new HealthScaledCooldown(460, 20, 40, 0.5F, true))
                .condition(ConditionFactory.and(
                        ConditionFactory.hasLineOfSight(),
                        ConditionFactory.heightDiff(6),
                        ConditionFactory.distanceRange(12, 32),
                        ConditionFactory.randomChance(0.5F)
                )).build();

        AnimationRule<EntityImmortalBoss> trackingShurikenRule = builder.define(TRACKING_SHURIKEN_ANIMATION)
                .triggerAtTick(15)
                .cooldown(new FixedRangeCooldown(430, 70, true))
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.getCumulativeBattleTick() > 1800 || entity.getHealthPercentage() < 0.7,
                        ConditionFactory.hybridDistanceRange(12, 4, 30),
                        ConditionFactory.randomChanceOnLowHealth(0.4F, 0.3F)
                )).next(smashGround3Rule)
                .next(smashGround2Rule)
                .nextW(smashGroundRule, 0.5)
                .next(pounceRule)
                .nextH(blockRule, 0.5)
                .build();

        AnimationRule<EntityImmortalBoss> unleashEnergyRule = builder.define(UNLEASH_ENERGY_ANIMATION)
                .priority(2)
                .cooldown(new HealthScaledCooldown(480, 40, 60, 0.4F, true))
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.invalidAttackCount < 20 || entity.targets.size() > 1,
                        (entity, target) -> entity.getCumulativeBattleTick() > 2400 || entity.getHealthPercentage() < 0.75,
                        ConditionFactory.heightDiff(8),
                        ConditionFactory.distanceRange(9.5, EntityImmortalLaser.IMMORTAL_RADIUS)
                )).build();

        AnimationRule<EntityImmortalBoss> shoryukenRule = builder.define(SHORYUKEN_ANIMATION)
                .triggerAtTick(70)
                .cooldown(new HealthScaledCooldown(300, 20, 40, 0.4F))
                .condition(ConditionFactory.and(
                        ConditionFactory.heightDiff(4.5),
                        ConditionFactory.distanceRange(0, 9),
                        ConditionFactory.angleRange(-30, 30)
                )).onSuccess(addSkillProbOnSuccess)
                .next(smashGround3Rule)
                .next(pounceRule)
                .next(unleashEnergyRule)
                .next(trackingShurikenRule, 0.5, 0.25)
                .nextH(smashGround2Rule, 0.6)
                .nextH(blockRule, 0.5)
                .build();

        AnimationRule<EntityImmortalBoss> attractRule = builder.define(ATTRACT_ANIMATION)
                .triggerAtTick(55)
                .cooldown(new HealthScaledCooldown(400, 40, 20, 0.4F))
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.getCumulativeBattleTick() > 1600 || entity.getHealthPercentage() < 0.9,
                        ConditionFactory.heightDiff(6),
                        ConditionFactory.distanceRange(0, 9, 10),
                        ConditionFactory.randomChanceOnLowHealth(0.3F, 0.7F)
                )).onSuccess(addSkillProbOnSuccess)
                .next(unleashEnergyRule)
                .next(shoryukenRule)
                .next(pounceRule)
                .nextW(smashGround3Rule, 1.5)
                .nextH(blockRule, 0.5)
                .build();

        AnimationRule<EntityImmortalBoss> teleportRule = builder.define(TELEPORT_ANIMATION)
                .priority(2)
                .triggerAtTick(19)
                .condition((entity, target) -> entity.hurtCount > 30 || entity.unableAttackTickCount >= 150
                        || (entity.closeProximityTickCount >= TIME_UNTIL_TELEPORT && entity.random.nextFloat() < 0.5F)
                ).onSuccess(entity -> {
                    entity.hurtCount = 0;
                    entity.closeProximityTickCount = entity.invalidAttackCount > 0 ? entity.random.nextInt(entity.invalidAttackCount) : 0;
                    LivingEntity target = entity.getTarget();
                    if (entity.unableAttackTickCount >= 150) entity.setTeleportType(TeleportType.FORCE);
                    else if (target != null) {
                        TeleportType type;
                        if (entity.getCooldownManager().isReady(POUNCE_PRE_ANIMATION) && entity.random.nextFloat() < entity.getHealth() / entity.getMaxHealth()) {
                            type = TeleportType.BEHIND;
                        } else if (ModEntityUtils.isTargetFacingAway(entity, target, 90)) {
                            type = TeleportType.SNEAK;
                        } else {
                            type = TeleportType.FRONT;
                        }
                        entity.setTeleportType(type);
                    }
                })
                .next(smashGround3Rule)
                .next(shoryukenRule)
                .next(pounceRule)
                .next(unleashEnergyRule)
                .nextH(smashGround2Rule, 0.6)
                .nextW(attractRule, 0.5)
                .nextW(smashGroundRule, 0.5)
                .build();

        AnimationRule<EntityImmortalBoss> hardPunchRightRule = builder.define(HARDPUNCH_RIGHT_ANIMATION)
                .onlyCombo()
                .triggerAtTick(37)
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.random.nextFloat() < entity.hardpunchDerivedSkillProb.getProbability(),
                        ConditionFactory.distanceRange(0, 5, 6)
                )).onSuccess(entity -> {
                    addSkillProbOnSuccess.accept(entity);
                    entity.hardpunchDerivedSkillProb.resetProbability();
                })
                .next(smashGround3Rule)
                .nextW(teleportRule, 1.5F)
                .next(pounceRule)
                .next(shoryukenRule)
                .next(attractRule)
                .next(unleashEnergyRule)
                .nextH(smashGround2Rule, 0.6)
                .nextH(blockRule, 0.5)
                .nextH(trackingShurikenRule, 0.4)
                .build();

        AnimationRule<EntityImmortalBoss> hardPunchLeftRule = builder.define(HARDPUNCH_LEFT_ANIMATION)
                .onlyCombo()
                .triggerAtTick(37)
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.random.nextFloat() < entity.hardpunchDerivedSkillProb.getProbability(),
                        ConditionFactory.distanceRange(0, 5, 6)
                )).onSuccess(entity -> {
                    addSkillProbOnSuccess.accept(entity);
                    entity.hardpunchDerivedSkillProb.resetProbability();
                })
                .next(smashGround3Rule)
                .next(shoryukenRule)
                .next(pounceRule)
                .next(attractRule)
                .next(unleashEnergyRule)
                .nextW(teleportRule, 1.5)
                .nextH(smashGround2Rule, 0.6)
                .nextH(blockRule, 0.5)
                .nextH(trackingShurikenRule, 0.4)
                .build();

        AnimationRule<EntityImmortalBoss> punchRightComboRule = builder.define(PUNCH_RIGHT_ANIMATION)
                .onlyCombo()
                .triggerAtTick(21)
                .condition(ConditionFactory.and(
                        ConditionFactory.distanceRange(0, 5, 6),
                        ConditionFactory.randomChance(0.15F)
                ))
                .onSuccess(entity -> {
                    entity.LRFlag = !entity.LRFlag;
                    entity.hardpunchDerivedSkillProb.onTick();
                })
                .nextW(hardPunchLeftRule, 1.5)
                .nextW(teleportRule, 1.5)
                .next(shoryukenRule)
                .build();
        builder.register(builder.define(PUNCH_LEFT_ANIMATION)
                .priority(2)
                .triggerAtTick(21)
                .cooldown(new FixedRangeCooldown(60, 40))
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.LRFlag,
                        ConditionFactory.heightDiff(4.5),
                        ConditionFactory.distanceRange(0, 5, 6),
                        ConditionFactory.randomChance(0.65F)
                ))
                .onSuccess(entity -> entity.LRFlag = !entity.LRFlag)
                .nextW(teleportRule, 0.75)
                .nextW(shoryukenRule, 0.5)
                .next(punchRightComboRule)
        );
        AnimationRule<EntityImmortalBoss> punchLeftComboRule = builder.define(PUNCH_LEFT_ANIMATION)
                .onlyCombo()
                .triggerAtTick(21)
                .condition(ConditionFactory.and(
                        ConditionFactory.distanceRange(0, 5, 6),
                        ConditionFactory.randomChance(0.15F)
                ))
                .onSuccess(entity -> {
                    entity.LRFlag = !entity.LRFlag;
                    entity.hardpunchDerivedSkillProb.onTick();
                })
                .nextW(hardPunchRightRule, 1.5)
                .next(shoryukenRule)
                .nextW(teleportRule, 1.5)
                .build();
        builder.register(builder.define(PUNCH_RIGHT_ANIMATION)
                .priority(2)
                .triggerAtTick(21)
                .cooldown(new FixedRangeCooldown(60, 40))
                .condition(ConditionFactory.and(
                        (entity, target) -> !entity.LRFlag,
                        ConditionFactory.heightDiff(4.5),
                        ConditionFactory.distanceRange(0, 5, 6),
                        ConditionFactory.randomChance(0.65F)
                ))
                .onSuccess(entity -> entity.LRFlag = !entity.LRFlag)
                .nextW(teleportRule, 0.75)
                .nextW(shoryukenRule, 0.5)
                .next(punchLeftComboRule)
        );
        manager.registerRule(blockRule);
        manager.registerRule(pounceRule);
        manager.registerRule(punchLeftComboRule);
        manager.registerRule(punchRightComboRule);
        manager.registerRule(unleashEnergyRule);
        manager.registerRule(attractRule);
        manager.registerRule(shoryukenRule);
        manager.registerRule(trackingShurikenRule);
        manager.registerRule(smashGroundRule);
        manager.registerRule(smashGround2Rule);
        manager.registerRule(smashGround3Rule);
        manager.registerRule(hardPunchRightRule);
        manager.registerRule(hardPunchLeftRule);
        manager.registerRule(teleportRule);
        builder.condition(e -> !e.isSwitching() && e.universalCDTime <= 0 && e.isActive());
        return manager;
    }

    /**
     * 是否触发硬直检查
     *
     * @param source 伤害源
     * @param damage 伤害量
     */
    private void stunCheck(DamageSource source, float damage) {
        boolean flag = damage >= 5F && source.is(DamageTypeTags.IS_EXPLOSION);
        if (!flag) return;
        Animation animation = this.getAnimation();
        int tick = this.getAnimationTick();
        flag = (animation == ATTRACT_ANIMATION && tick < 50) || (animation == UNLEASH_ENERGY_ANIMATION && tick > 5 && tick < 95);
        if (!flag) return;
        if (this.damageAdaptation.isFullyAdapted(this, source)) return;
        this.stopAllSuperpositionAnimation();
        Entity entity = source.getEntity();
        if (entity instanceof LivingEntity livingEntity) this.blockEntity = livingEntity;
        this.level().broadcastEntityEvent(this, (byte) 11);
        this.playAnimation(HURT_ANIMATION1);
        this.stunCount++;
    }

    public int getCumulativeBattleTick() {
        if (this.battleTimestamp > 0 && this.battleTimestamp < this.tickCount) {
            return this.tickCount - this.battleTimestamp;
        }
        return 0;
    }

    public List<LivingEntity> getCacheTargets() {
        return this.targets;
    }

    public boolean doHurtTarget(LivingEntity target, boolean disableShield, boolean addEffect, boolean ignoreArmor, float baseDamageMultiplier, float damageMultiplier) {
        return doHurtTarget(ModDamageSource.immortalAttack(this, ignoreArmor), target, disableShield, addEffect, ignoreArmor, baseDamageMultiplier, damageMultiplier);
    }

    public boolean doHurtTarget(DamageSource source, LivingEntity target, boolean disableShield, boolean addEffect, boolean ignoreArmor, float baseDamageMultiplier, float damageMultiplier) {
        //当目标数量＞1时，根据目标数量增加攻击伤害，每个目标增加10%伤害 TODO 待完善
        damageMultiplier += Mth.clamp(targets.size() - 1, 0F, 5F) * 0.1F;
        boolean flag = target.hurt(source, (float) ((this.getAttributeValue(Attributes.ATTACK_DAMAGE) * baseDamageMultiplier) + getDamageAmountByTargetHealthPct(target)) * damageMultiplier);
        if (flag) {
            this.invalidAttackCount = 0;
            if (addEffect) ModEntityUtils.addEffectStackingAmplifier(this, target, EffectInit.ERODE_EFFECT.get(), 300, 5, true, true, true, false);
        } else if (disableShield) {
            this.disableShield(target, 100);
        }
        if (!flag) this.invalidAttackCount += 2;
        this.closeProximityTickCount += this.invalidAttackCount;
        return flag;
    }

    public void disableShield(LivingEntity defender, int cd) {
        if (defender.isBlocking()) {
            if (defender instanceof Player player) player.getCooldowns().addCooldown(player.getUseItem().getItem(), cd);
            defender.stopUsingItem();
            this.level().broadcastEntityEvent(defender, (byte) 30);
        }
    }

    public void shakeGround(float shakeStrength, float range, float magnitude, int duration, int fadeDuration) {
        if (shakeStrength > 0F) {
            float y = shakeStrength + 0.1F * this.random.nextFloat();
            for (Entity entity : this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(range, 0, range), Entity::onGround)) {
                if (entity == this) continue;
                float factor = ModMathUtils.getTickFactor(this.distanceTo(entity), range, false);
                entity.push(0, y - (y / 2) * factor, 0);
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(entity));
                }
            }
        }
        EntityCameraShake.cameraShake(this.level(), this.position(), range, magnitude, duration, fadeDuration);
    }

    public void knockBack(LivingEntity target, double strength, double yStrength, boolean forced, boolean continuous) {
        if (forced) target.hurtMarked = true;
        if (!target.onGround() || continuous) {
            strength *= 0.25;
            yStrength *= 0.25;
        }
        double d0 = target.getX() - this.getX();
        double d1 = target.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        target.push(d0 / d2 * strength, yStrength, d1 / d2 * strength);
    }

    public void pounce(float pursuitDistance, float y, double speedModifier) {
        double f0 = Math.cos(Math.toRadians(this.getYRot() + 90));
        double f1 = Math.sin(Math.toRadians(this.getYRot() + 90));
        LivingEntity target = this.getTarget();
        if (target != null) {
            double f3 = this.distanceTo(target) - target.getBbWidth() / 2F;
            f3 = Mth.clamp(f3, 0F, pursuitDistance);
            if (!this.onGround()) speedModifier /= 2F;
            this.push(f0 * speedModifier * f3, y, f1 * speedModifier * f3);
        } else {
            this.push(f0, 0, f1);
        }
    }

    public boolean inBlocking() {
        Animation a = this.getAnimation();
        return a == ARMBLOCK_END_ANIMATION || a == ARMBLOCK_HOLD_ANIMATION || a == ARMBLOCK_ANIMATION;
    }

    private Animation getBaseAttackByStage() {
        try {
            if (this.getStage() == ImmortalStage.STAGE1) {
                return this.LRFlag ? PUNCH_LEFT_ANIMATION : PUNCH_RIGHT_ANIMATION;
            } else {
                return null;
            }
        } finally {
            this.LRFlag = !this.LRFlag;
        }
    }

    private void floatImmortal() {
        if (this.isInLava()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.15D, 0.0D));
            }
        }
    }

    private boolean isSwitching() {
        return this.getAnimation() == SWITCH_STAGE_ANIMATION;
    }

    public boolean teleportByType(TeleportType type, LivingEntity target) {
        if (this.level().isClientSide || !this.isAlive()) return false;
        if (target == null) type = TeleportType.RANDOM;
        double x, y, z;
        if (TeleportType.FORCE == type) {
            x = target.getX();
            y = target.getY();
            z = target.getZ();
            this.teleportTo(x, y, z);
            return true;
        } else if (TeleportType.SNEAK == type) {
            float width = 1F + this.getBbWidth() + target.getBbWidth();
            double radians = Math.toRadians(target.getYRot() + 270);
            x = target.getX() + width * Math.cos(radians);
            y = target.getY();
            z = target.getZ() + width * Math.sin(radians);
        } else if (TeleportType.FRONT == type || TeleportType.BEHIND == type) {
            double radian = Math.toRadians(target.getYRot() + (type == TeleportType.FRONT ? 90 : -90));
            int distance = 12;
            x = this.getX() + (this.random.nextInt(distance) + distance) * Math.cos(radian);
            y = this.getY() + (double) (this.random.nextInt(32) - 16);
            z = this.getZ() + (this.random.nextInt(distance) + distance) * Math.sin(radian);
        } else {
            x = this.getX() + (this.random.nextDouble() - 0.5D) * 32D;
            y = this.getY() + (double) (this.random.nextInt(32) - 16);
            z = this.getZ() + (this.random.nextDouble() - 0.5D) * 32D;
        }
        return this.teleport(x, y, z);
    }

    private boolean teleport(double x, double y, double z) {
        boolean flag = this.randomTeleport(x, y, z, false);
        if (flag) {
            this.level().gameEvent(GameEvent.TELEPORT, this.position(), GameEvent.Context.of(this));
            if (!this.isSilent()) {
                this.level().playSound(null, this.xo, this.yo, this.zo, SoundInit.IMMORTAL_TELEPORT.get(), this.getSoundSource(), 1.5F, 1.0F);
                this.playSound(SoundInit.IMMORTAL_TELEPORT.get(), 1.0F, 1.0F);
            }
        }
        return flag;
    }

    private void reflectPotionEffect(MobEffectInstance effectInstance, Entity entity) {
        if (entity != this && entity instanceof LivingEntity bouncer && !effectInstance.getEffect().isInstantenous()) {
            bouncer.addEffect(effectInstance, null);
        }
    }

    private void strongKnockBlock() {
        List<LivingEntity> entities = this.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this, this.getBoundingBox().inflate(4, 5, 4));
        for (LivingEntity entity : entities) {
            if (entity == this) continue;
            if (entity instanceof Player player && player.getAbilities().invulnerable) continue;
            double angle = Math.toRadians(getAngleBetweenEntities(this, entity) + 90);
            double distance = distanceTo(entity) - 4;
            double mx = Math.min(1 / (distance * distance), 1) * -1 * Math.cos(angle);
            double my = Math.min(1 / (distance * distance), 1) * -1 * Math.sin(angle);
            entity.setDeltaMovement(entity.getDeltaMovement().add(mx, 0, my).scale(0.8));
        }
    }

    private void doCollisionEffect(int count, float scale, float sizeModifier) {
        this.doHitEffect(5, 0, scale + this.random.nextInt(5), 1.45F, 0.625F, false);
        if (this.level().isClientSide) {
            double phi = Math.PI * (3 - Math.sqrt(5));
            if (count > 0) count += this.random.nextInt(5);
            float width = this.getBbWidth() * sizeModifier;
            for (int i = 0; i < count; i++) {
                Vec3 pos = this.getPosOffset(this.random.nextBoolean(), 1.45F, (this.random.nextIntBetweenInclusive(-10, 10) * 0.1F) * width, this.getBbHeight() * 0.625F);
                double theta = phi * i;
                double y = 1 - (i / (count - 1D)) * 2;
                double radius = Math.sqrt(1 - y * y);
                double x = Math.cos(theta) * radius;
                double z = Math.sin(theta) * radius;
                float sideOffset = (float) (random.nextGaussian() * 0.2D) * (random.nextBoolean() ? 1 : -1);
                AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_ORB.get(), pos.x, pos.y, pos.z, x * (sizeModifier + sideOffset), y * sizeModifier, z * (sizeModifier + sideOffset), true, 0, 0, 0, 0, 1F, 230F / 255F, 230F / 255F, 104F / 255F, 0.8F, 0.9F, 5, true, true, false, new ParticleComponent[]{new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 8, 0, 0, 0, 0.01F, 196F / 255F, 196F / 255F, 86F / 255F, 0.8F, true, true, new ParticleComponent[]{new PropertyOverLength(EnumRibbonProperty.ALPHA, KeyTrack.startAndEnd(0.5F, 0F)),}, false),});
            }
        }
    }

    private void doHitEffect(int duration, float xRot, float scale, float xOffset, float yOffset, boolean thump) {
        if (this.level().isClientSide) {
            Vec3 pos = this.getPosOffset(false, xOffset, 0F, this.getBbHeight() * yOffset);
            float alpha = 0.4F + 0.2F * this.random.nextFloat();
            float randomScale = scale + this.random.nextInt(5);
            float colorOffset = 0.1F * this.random.nextFloat();
            ParticleType<AdvancedParticleData> ringParticle = thump ? ParticleInit.THUMP_RING.get() : ParticleInit.CRIT_RING.get();
            AdvancedParticleBase.spawnParticle(this.level(), ringParticle, pos.x, pos.y, pos.z, 0, 0, 0, false, (float) Math.toRadians(-this.yBodyRot), (float) Math.toRadians(xRot), 0, 0, randomScale, 0.75F + colorOffset, 0.75F + colorOffset, 0.75F + colorOffset, alpha, 1, duration, true, true, false, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(alpha * 2F, 0F), false), new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(randomScale * 0.1F, randomScale * 1.5F), false)});
        }
    }

    private void doPuncturedAirFlowEffect(boolean right) {
        if (this.level().isClientSide) {
            double radians = Math.toRadians(this.getYRot() + 90);
            Vec3 pos = this.getPosOffset(right, 1.5F, 1.5F, this.getBbHeight() * 0.6F);
            float alpha = 0.15F + 0.15F * this.random.nextFloat();
            float scale = 30F + this.random.nextInt(11);
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.PUNCTURED_AIR_RING.get(), pos.x, pos.y, pos.z, -Math.cos(radians) * 0.5F, 0.007F, -Math.sin(radians) * 0.5F, false, (float) Math.toRadians(-this.yBodyRot), (float) Math.toRadians(right ? -10 : 10), 0, 0, scale, 0.8F, 0.8F, 0.8F, alpha, 1, 14, true, true, true, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(alpha, 0.1F), false), new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(scale, 0F), false)});
        }
    }

    private void doShakeGroundEffect(int count, float fbOffset, float sideOffset, float yOffset, float airDiffusionSpeed, boolean right, boolean blockEffect) {
        if (this.level().isClientSide) {
            Vec3 pos = this.getPosOffset(right, fbOffset, sideOffset, 0F);
            double px = pos.x;
            double py = pos.y;
            double pz = pos.z;
            float colorOffset = this.random.nextFloat() * 0.04f;
            ParticleRing.RingData particle = new ParticleRing.RingData(0F, (float) (Math.PI / 2F), 20, 1F, 1F, 1F, 0.8F, 70F, false, ParticleRing.EnumRingBehavior.GROW);
            this.level().addParticle(particle, px, py + yOffset, pz, 0, 0, 0);
            ParticleDust.DustData particle2 = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.28f - colorOffset, 0.26f - colorOffset, 0.24f - colorOffset, 50f, 36 + this.random.nextInt(5), ParticleDust.EnumDustBehavior.GROW, airDiffusionSpeed);
            ModParticleUtils.annularParticleOutburst(this.level(), count, particle2, px, py + 0.2F, pz, 0.8F, 0.5F, 360F, 1.7F, this.getYRot());
            if (blockEffect) {
                int x = Mth.floor(px);
                int y = Mth.floor(py);
                int z = Mth.floor(pz);
                BlockState state = this.level().getBlockState(new BlockPos(x, y, z));
                if (state.getRenderShape() != RenderShape.INVISIBLE) {
                    ParticleOptions[] options = {new BlockParticleOption(ParticleTypes.BLOCK, state)};
                    ModParticleUtils.particleOutburst(this.level(), 50, options, px, py + 0.25, pz, new float[][]{new float[]{4F, 0.1F, 4F},}, 1F + 0.5F * this.random.nextFloat());
                }
            }
        }
    }

    private void doImmortalMagicMatrixEffect(MagicCircleType magicType, int duration, float offset, float yOffset, float scale) {
        if (this.level().isClientSide) {
            Vec3 pos = this.getPosOffset(false, offset, 0F, this.getBbHeight() * yOffset);
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_RING2.get(), pos.x, pos.y, pos.z, 0, 0, 0, false, (float) Math.toRadians(-this.yBodyRot), 0, 0, 0, scale, 1, 1, 1, 1, 1, duration, true, false, false, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.ALPHA, new KeyTrack(new float[]{1, 0.5F, 0.1F}, new float[]{0, 0.9F, 1}), false), new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(0, scale), false), new PropertyControl(EnumParticleProperty.RED, new KeyTrack(new float[]{0.18F, magicType.r}, new float[]{0.4F, 0.8F}), false), new PropertyControl(EnumParticleProperty.GREEN, new KeyTrack(new float[]{0.44F, magicType.g}, new float[]{0.4F, 0.8F}), false), new PropertyControl(EnumParticleProperty.BLUE, new KeyTrack(new float[]{0.6F, magicType.b}, new float[]{0.4F, 0.8F}), false),});
        }
    }

    private void doAttractHitEffect() {
        if (this.level().isClientSide) {
            double radians = Math.toRadians(this.getYRot() + 90);
            Vec3 pos = this.getPosOffset(false, 3F, 0F, this.getBbHeight() * 0.5F);
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.THUMP_RING.get(), pos.x, pos.y, pos.z, -Math.cos(radians) * 0.3F, 0.017, -Math.sin(radians) * 0.3F, true, 0, 0, 0, 0, 1, 0.28F, 0.5F, 0.56F, 1, 1, 5, true, false, false, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.SCALE, new KeyTrack(new float[]{0F, 40F, 80F}, new float[]{0F, 0.4F, 1F}), false), new PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(0.5F, 0.1F), false),});
            AdvancedParticleBase.spawnParticle(level(), ParticleInit.GLOW.get(), pos.x, pos.y, pos.z, -Math.cos(radians) * 0.3F, 0.017, -Math.sin(radians) * 0.3F, true, 0, 0, 0, 0, 70, 0.46F, 0.75F, 0.88F, 1, 1, 3, true, true, false, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(0.5F, 0F), false), new PropertyControl(EnumParticleProperty.SCALE, KeyTrack.startAndEnd(70F, 0F), false)});
        }
    }

    private void doAttractEffect() {
        int tick = this.getAnimationTick();
        if (tick < 40) {
            if (tick == 1 && !this.isSilent()) this.level().playLocalSound(this.blockPosition(), SoundInit.IMMORTAL_ATTRACT.get(), this.getSoundSource(), 0.8F, 1F, false);
            List<LivingEntity> entities = getNearByEntities(LivingEntity.class, 10, 10, 10, 10);
            for (LivingEntity inRangeEntity : entities) {
                if (inRangeEntity instanceof Player player && player.getAbilities().invulnerable) continue;
                float attackArc = 180F;
                float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this, inRangeEntity);
                if (!((entityRelativeAngle >= -attackArc / 2 && entityRelativeAngle <= attackArc / 2) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                    continue;
                }
                double radians = Math.toRadians(this.yBodyRot + 90);
                Vec3 diff = inRangeEntity.position().subtract(this.position().add(Math.cos(radians) * 1.5F, 0, Math.sin(radians * 1.5F)));
                diff = diff.normalize().scale(0.05F + ModMathUtils.getTickFactor(tick, 40, true) * 0.1F);
                inRangeEntity.setDeltaMovement(inRangeEntity.getDeltaMovement().subtract(diff));
                if (inRangeEntity.getY() > this.getY() + 10) {
                    inRangeEntity.setDeltaMovement(inRangeEntity.getDeltaMovement().subtract(0, 0.06, 0));
                }
            }
        }
        if (this.level().isClientSide) {
            double zRadians = Math.toRadians(this.getYRot() + 90);
            double frontX = Math.cos(zRadians);
            double frontZ = Math.sin(zRadians);
            double xRadians = Math.toRadians(this.getYRot() + 180);
            double sideX = Math.cos(xRadians);
            double sideZ = Math.sin(xRadians);
            float tickFactor = ModMathUtils.getTickFactor(tick, 35, true);
            if (tick == 4 || tick == 18 || tick == 30) {
                this.spawnBigRingParticle(tickFactor, sideX, sideZ, frontX, frontZ);
            }
            if (tick > 5 && tick < 35) {
                if (this.random.nextFloat() < 0.75F && tick % 5 == 1) {
                    this.spawnPuncturedAirFlowParticle(tickFactor, sideX, sideZ, frontX, frontZ);
                }
                if (tick < 30) {
                    tickFactor = ModMathUtils.getTickFactor(tick, 30, false);
                    double factorOffset = 6F - tickFactor;
                    double randomOffsetX = (this.random.nextFloat() - 0.5) * 2 * factorOffset;
                    double randomOffsetZ = (this.random.nextFloat() - 0.5) * 2 * factorOffset;
                    double x = this.getX() + randomOffsetX + (frontX * factorOffset);
                    double y = this.getY(this.random.nextFloat());
                    double z = this.getZ() + randomOffsetZ + (frontZ * factorOffset);
                    if (tick % 2 == 1 && this.random.nextFloat() < 0.85F) {
                        this.spawnOrbParticle(x, y, z, tickFactor, sideX, frontX, sideZ, frontZ, false);
                    }
                    if (tick % 3 == 1 && this.random.nextFloat() < 0.75F) {
                        this.spawnOrbParticle(x, y, z, tickFactor, sideX, frontX, sideZ, frontZ, true);
                    }
                }
            }
        }
    }

    private void spawnBigRingParticle(float tickFactor, double sideX, double sideZ, double frontX, double frontZ) {
        if (this.level().isClientSide) {
            double factorOffset = 4F + 2F * tickFactor;
            double x = this.getX() + sideX + (frontX * factorOffset);
            double y = this.getY(0.45);
            double z = this.getZ() + sideZ + (frontZ * factorOffset);
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_RING2.get(), x, y, z, 0, 0.007, 0, false, Math.toRadians(-this.getYRot()), (float) Math.toRadians(this.random.nextBoolean() ? -5 : 5), 0, 0, 1, 1, 1, 1, 1, 1, 10, true, false, false, new ParticleComponent[]{new Attractor(new Vec3[]{this.position().add(sideX + frontX, this.getBbHeight() * 0.5, sideZ + frontZ)}, 1.5F + (1F - tickFactor), 0, Attractor.EnumAttractorBehavior.EXPONENTIAL), new PropertyControl(EnumParticleProperty.SCALE, new KeyTrack(new float[]{40F + 10F * tickFactor, 0.1F}, new float[]{0, 1}), false), new PropertyOverLength(EnumRibbonProperty.ALPHA, KeyTrack.startAndEnd(0.6F, 0F)), ATTRACT_COMPONENT[0], ATTRACT_COMPONENT[1], ATTRACT_COMPONENT[2]});
        }
    }

    private void spawnPuncturedAirFlowParticle(float tickFactor, double sideX, double sideZ, double frontX, double frontZ) {
        if (this.level().isClientSide) {
            double factorOffset = 2F + 3F * tickFactor;
            double x = this.getX() + sideX + (frontX * factorOffset);
            double y = this.getY(0.45);
            double z = this.getZ() + sideZ + (frontZ * factorOffset);
            double yaw = Math.toRadians(-this.getYRot());
            float pitch = (float) Math.toRadians(this.random.nextBoolean() ? 180 : 0);
            float roll = (float) Math.toRadians(this.random.nextBoolean() ? -5 : 5);
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.PUNCTURED_AIR_RING.get(), x, y, z, 0, 0.007, 0, false, yaw, pitch, roll, 0, 1, 1, 1, 1, 0.5F, 1, 10 + 5 * tickFactor, true, false, true, new ParticleComponent[]{new Attractor(new Vec3[]{this.position().add(sideX + frontX, this.getBbHeight() * 0.5, sideZ + frontZ)}, 2F - tickFactor, 0.1F, Attractor.EnumAttractorBehavior.EXPONENTIAL), new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(30F, 5F), false), new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.5F, 0F), false), ATTRACT_COMPONENT[0], ATTRACT_COMPONENT[1], ATTRACT_COMPONENT[2]});
        }
    }

    private void spawnOrbParticle(double x, double y, double z, float tickFactor, double sideX, double frontX, double sideZ, double frontZ, boolean ribbon) {
        if (this.level().isClientSide) {
            ParticleComponent component;
            if (ribbon) {
                component = new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 8, 0, 0, 0, 0.12F, 1, 1, 1, 0.8F, true, true, new ParticleComponent[]{new PropertyOverLength(EnumRibbonProperty.ALPHA, KeyTrack.startAndEnd(0.5F, 0F)), new PropertyOverLength(EnumRibbonProperty.SCALE, KeyTrack.startAndEnd(1F, 0F)), ATTRACT_COMPONENT[0], ATTRACT_COMPONENT[1], ATTRACT_COMPONENT[2]}, false);
            } else {
                component = new PropertyControl(EnumParticleProperty.AIR_DIFFUSION_SPEED, AnimData.constant(1F), false);
            }
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_ORB.get(), x, y, z, 0, 0.007, 0, true, 0, 0, 0, 0, 1, 1, 1, 1, 0.5F, 1, 15 - 5 * tickFactor, true, false, false, new ParticleComponent[]{new Attractor(new Vec3[]{this.position().add(sideX + frontX, this.getBbHeight() * 0.5, sideZ + frontZ)}, 1F + tickFactor, 0.25F, Attractor.EnumAttractorBehavior.EXPONENTIAL), new PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(0.5F, 0F), false), new PropertyControl(EnumParticleProperty.SCALE, KeyTrack.startAndEnd(10F, 3F), false), ATTRACT_COMPONENT[0], ATTRACT_COMPONENT[1], ATTRACT_COMPONENT[2], component});
        }
    }

    private void doUnleashEnergyEffect() {
        if (this.level().isClientSide) {
            int tick = this.getAnimationTick();
            if (tick % 6 == 0)
                AdvancedParticleBase.spawnParticle(level(), ParticleInit.ADV_RING.get(), this.getX(), this.getY(0.2), this.getZ(), 0, -0.1F, 0, false, 0, Math.PI / 2F, 0, 0, 1.5F, 0.6, 0.85F, 0.95F, 1, 1, 10, true, true, false, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1F, 0F), false), new PropertyControl(EnumParticleProperty.SCALE, KeyTrack.startAndEnd(15F, 50F), false)});
            Vec3 pos = this.getPosOffset(false, 2F, 0F, this.getBbHeight() * 1.4F);
            if (tick % 3 == 0 && tick < 78) {
                AdvancedParticleBase.spawnEmptyComponentParticle(level(), ParticleInit.GLOW.get(), pos.x, pos.y, pos.z, 0, 0, 0, true, 0, 0, 0, 0, 15 + this.random.nextInt(3), 0.6, 0.85F, 1F, 0.4F, 1, 2, true, false, false);
                AdvancedParticleBase.spawnParticle(level(), ParticleInit.ADV_RING.get(), pos.x, pos.y, pos.z, 0, 0, 0, true, 0, 0, 0, 0, 1.5F, 0.6, 0.85F, 0.95F, 1, 1, 7, true, false, false, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1F, 0F), false), new PropertyControl(EnumParticleProperty.SCALE, KeyTrack.startAndEnd(0F, 15F), false)});
            }
        }
    }

    private void doTrailEffect(Animation animation, boolean startFlag, boolean holdFlag, @Nullable Boolean left) {
        if (this.level().isClientSide && this.hand != null && this.hand.length > 1) {
            if (startFlag) {
                this.lPreHandPos = this.hand[0];
                this.rPreHandPos = this.hand[1];
            } else if (holdFlag) this.doTrailEffect(animation, left);
        }
    }

    private void doTrailEffect(Animation animation, @Nullable Boolean left) {
        Vec3 leftPos = this.hand[0];
        double lLength = this.lPreHandPos.subtract(leftPos).length();
        int lNumDusts = (int) Math.min(Math.floor(2 * lLength), 16);
        if (left != null && !left) lNumDusts = 0;
        Vec3 rightPos = this.hand[1];
        double rLength = this.rPreHandPos.subtract(rightPos).length();
        int rNumDusts = (int) Math.min(Math.floor(2 * rLength), 16);
        if (left != null && left) rNumDusts = 0;

        int duration = 5;
        float speedMultiplier = 0.45F;
        if (animation == SHORYUKEN_ANIMATION || animation == POUNCE_SMASH_ANIMATION || animation == POUNCE_PICK_ANIMATION) {
            if (animation == SHORYUKEN_ANIMATION) ModParticleUtils.blockParticleDirectionality(this.level(), rightPos.x, rightPos.y - 2F, rightPos.z, Math.toRadians(this.getYRot()), 15, BLOCK_OFFSETS, 3F);
            speedMultiplier = 0.25F;
            duration = 10;
        } else if (animation == ATTRACT_ANIMATION || animation == HARDPUNCH_RIGHT_ANIMATION || animation == HARDPUNCH_LEFT_ANIMATION) {
            speedMultiplier = 0.12F;
        } else if (animation == SMASH_GROUND_ANIMATION1 || animation == SMASH_GROUND_ANIMATION2 || animation == SMASH_GROUND_ANIMATION3) {
            speedMultiplier = 0.1F;
        }

        this.spawnSwipeParticle(this.lPreHandPos, leftPos, lNumDusts, speedMultiplier, duration);
        this.spawnSwipeParticle(this.rPreHandPos, rightPos, rNumDusts, speedMultiplier, duration);

        this.lPreHandPos = leftPos;
        this.rPreHandPos = rightPos;
    }

    private void spawnSwipeParticle(Vec3 start, Vec3 end, int numDusts, float speedMultiplier, int baseDuration) {
        for (int i = 0; i < numDusts; i++) {
            double radians = Math.toRadians(this.yBodyRot + 90);
            double xSpeed = Math.cos(radians);
            double zSpeed = Math.sin(radians);
            double x = start.x + i * (end.x - start.x) / numDusts;
            double y = start.y + i * (end.y - start.y) / numDusts;
            double z = start.z + i * (end.z - start.z) / numDusts;
            for (int j = 0; j < 2; j++) {
                float xOffset = 0.5F * (2 * this.random.nextFloat() - 1);
                float yOffset = 0.5F * (2 * this.random.nextFloat() - 1);
                float zOffset = 0.5F * (2 * this.random.nextFloat() - 1);
                float colorOffset = this.random.nextFloat() * 0.1f;
                this.level().addParticle(new ParticleDust.DustData(ParticleInit.DUST.get(), (float) 0.18 - colorOffset, (float) 0.3 - colorOffset, (float) 0.36 - colorOffset, (float) (10D + random.nextDouble() * 10D), baseDuration + numDusts, ParticleDust.EnumDustBehavior.SHRINK, 1f, true), x + xOffset, y + yOffset, z + zOffset, xSpeed * speedMultiplier, 0.007F, zSpeed * speedMultiplier);
            }
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 400.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.36D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(ForgeMod.ENTITY_GRAVITY.get(), 0.09D)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 4D);
    }

    public int getStunCount() {
        return this.stunCount;
    }

    public boolean isAlwaysActive() {
        return this.entityData.get(DATA_ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean alwaysActive) {
        this.entityData.set(DATA_ALWAYS_ACTIVE, alwaysActive);
    }

    public ImmortalStage getStage() {
        return ImmortalStage.byStage(this.entityData.get(DATA_STAGE));
    }

    public TeleportType getTeleportType() {
        return TeleportType.byType(this.entityData.get(DATA_TELEPORT_TYPE));
    }

    public void setTeleportType(@NotNull TeleportType type) {
        this.entityData.set(DATA_TELEPORT_TYPE, type.id);
    }

    public boolean isHoldKatana() {
        return this.entityData.get(DATA_KATANA_HOLD);
    }

    public void setHoldKatana(boolean held) {
        this.entityData.set(DATA_KATANA_HOLD, held);
    }

    @Override
    public boolean isGlow() {
        return !this.glowControlled.isStop();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        if (this.getAnimation() != STUN_ANIMATION && this.damageAdaptation.isFullyAdapted(this, damageSource)) {
            this.playSound(SoundInit.IMMORTAL_ADAPT.get(), 1.5F, this.getVoicePitch() + 0.1F);
        } else {
            this.playSound(SoundInit.IMMORTAL_HURT.get(), 1.5F, this.getVoicePitch());
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.IMMORTAL_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.IMMORTAL_IDLE.get();
    }

    @Override
    public void playAmbientSound() {
        if (this.getTarget() == null && this.isActive() && this.getAnimation() != SPAWN_ANIMATION) {
            super.playAmbientSound();
        }
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    public void anchorToGround() {
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    @Override
    protected boolean canPlayMusic() {
        return this.isActive() && super.canPlayMusic();
    }

    @Override
    public SoundEvent getBossMusic() {
        return SoundInit.THE_IMMORTAL_THEME.get();
    }

    @Override
    public void setOwner(@Nullable EntityAbsImmortal owner) {
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
    }

    @Override
    public void setSpawnParticle(int amount) {
    }

    @Override
    public boolean isSummon() {
        return false;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public KeyframeManager<EntityImmortalBoss> getKeyframeManager() {
        return KEYFRAME_MANAGER;
    }

    @Override
    public AnimationState getOverlapAnimationState(Animation animation) {
        if (TELEPORT_ANIMATION == animation) {
            return this.teleportAnimationState;
        } else if (PUNCH_RIGHT_ANIMATION == animation) {
            return this.punchRightAnimationState;
        } else if (PUNCH_LEFT_ANIMATION == animation) {
            return this.punchLeftAnimationState;
        } else if (HARDPUNCH_RIGHT_ANIMATION == animation) {
            return this.hardPunchRightAnimationState;
        } else if (HARDPUNCH_LEFT_ANIMATION == animation) {
            return this.hardPunchLeftAnimationState;
        } else if (TRACKING_SHURIKEN_ANIMATION == animation) {
            return this.trackingShurikenAnimationState;
        } else if (ARMBLOCK_COUNTERATTACK_ANIMATION == animation) {
            return this.armBlockCounterattackAnimationState;
        } else if (SHORYUKEN_ANIMATION == animation) {
            return this.shoryukenState;
        } else if (ATTRACT_ANIMATION == animation) {
            return this.attractState;
        } else if (HURT_ANIMATION1 == animation) {
            return this.hurtAnimationState;
        }
        return null;
    }

    static class ImmortalGroupAI extends AnimationGroupAI<EntityImmortalBoss> {
        public ImmortalGroupAI(EntityImmortalBoss entity, Animation... animations) {
            super(entity, animations);
        }

        @Override
        public void tick() {
            Animation animation = this.entity.getAnimation();
            if (animation == TELEPORT_ANIMATION) {
                LivingEntity target = this.entity.getTarget();
                if (target != null) {
                    this.entity.lookAt(target, 90F, 30F);
                    this.entity.getLookControl().setLookAt(target, 90F, 30F);
                }
            } else if (animation == SPAWN_ANIMATION) {
            } else if (animation == SWITCH_STAGE_ANIMATION) {
            }
        }
    }

    public enum TeleportType {
        RANDOM(0),
        FRONT(1),
        BEHIND(2),
        SNEAK(3),
        //强制传送:对传送的坐标不做任何检查
        FORCE(4);

        private static final IntFunction<TeleportType> BY_ID = ByIdMap.sparse(c -> c.id, values(), RANDOM);
        private final byte id;

        TeleportType(int id) {
            this.id = (byte) id;
        }

        public static TeleportType byType(int id) {
            return BY_ID.apply(id);
        }
    }
}
