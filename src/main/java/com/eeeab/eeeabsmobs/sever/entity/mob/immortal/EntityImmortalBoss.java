package com.eeeab.eeeabsmobs.sever.entity.mob.immortal;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.util.*;
import com.eeeab.eeeabsmobs.client.particle.util.AnimData.KeyTrack;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent.Attractor;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent.PropertyControl;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.eeeab.eeeabsmobs.client.particle.util.RibbonComponent.PropertyOverLength;
import com.eeeab.eeeabsmobs.client.particle.util.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.mob.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate.*;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalMagicCircle.MagicCircleType;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.TickBasedProbabilityBooster;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.DamageAdaptation;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.ModDamageSource;
import com.eeeab.eeeabsmobs.sever.init.*;
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
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EntityImmortalBoss extends EntityAbsImmortal implements IBoss {
    public final Animation dieAnimation = Animation.create(60);
    public final Animation spawnAnimation = Animation.create(100);
    public final Animation switchStageAnimation = Animation.create(100);
    public final Animation teleportAnimation = Animation.create(25).doesOverlap();
    public final Animation smashGround1Animation = Animation.create(45);
    public final Animation smashGround2Animation = Animation.create(80);
    public final Animation smashGround3Animation = Animation.create(50);
    public final Animation punchRightAnimation = Animation.create(40).doesOverlap();
    public final Animation punchLeftAnimation = Animation.create(40).doesOverlap();
    public final Animation hardPunchRightAnimation = Animation.create(45).doesOverlap();
    public final Animation hardPunchLeftAnimation = Animation.create(45).doesOverlap();
    public final Animation pouncePreAnimation = Animation.create(15);
    public final Animation pounceHoldAnimation = Animation.create(42);
    public final Animation pounceEndAnimation = Animation.create(15);
    public final Animation pounceSmashAnimation = Animation.create(50);
    public final Animation pouncePickAnimation = Animation.create(40);
    public final Animation attractAnimation = Animation.create(70);
    public final Animation shoryukenAnimation = Animation.create(80);
    public final Animation trackingShurikenAnimation = Animation.create(25).doesOverlap();
    public final Animation unleashEnergyAnimation = Animation.create(100);
    public final Animation armBlockAnimation = Animation.create(15);
    public final Animation armBlockHoldAnimation = Animation.create(50);
    public final Animation armBlockEndAnimation = Animation.create(10);
    public final Animation armBlockCounterattackAnimation = Animation.create(20).doesOverlap();
    public final Animation hurt1Animation = Animation.create(60).doesOverlap();
    public final Animation stunAnimation = Animation.create(100);
    private final Animation[] animations = new Animation[]{dieAnimation, spawnAnimation,
            //switchStageAnimation,
            teleportAnimation,
            armBlockAnimation,
            armBlockHoldAnimation,
            armBlockEndAnimation,
            armBlockCounterattackAnimation,
            punchRightAnimation,
            hardPunchRightAnimation,
            punchLeftAnimation,
            hardPunchLeftAnimation,
            smashGround1Animation,
            smashGround2Animation,
            smashGround3Animation,
            pouncePreAnimation,
            pounceHoldAnimation,
            pounceEndAnimation,
            pounceSmashAnimation,
            pouncePickAnimation,
            attractAnimation,
            shoryukenAnimation,
            trackingShurikenAnimation,
            unleashEnergyAnimation,
            hurt1Animation,
            stunAnimation,
    };
    private static final int MAX_BLOCK_HURT_COUNT = 5;
    private static final float MAX_COUNTERATTACK_DAMAGE_AMOUNT_THRESHOLD = 30F;
    private static final float MAX_CUMULATIVE_COUNTERATTACK_DAMAGE_THRESHOLD = 50F;
    private static final UniformInt SMASH_TIME = TimeUtil.rangeOfSeconds(10, 14);
    private static final UniformInt POUNCE_TIME = TimeUtil.rangeOfSeconds(12, 16);
    private static final UniformInt ATTRACT_TIME = TimeUtil.rangeOfSeconds(14, 18);
    private static final UniformInt SHURIKEN_TIME = TimeUtil.rangeOfSeconds(18, 25);
    private static final UniformInt LASER_TIME = TimeUtil.rangeOfSeconds(20, 30);
    private static final Predicate<LivingEntity> TARGET_CONDITIONS = entity -> entity.isAlive() && !entity.getType().is(ModTagKey.IMMORTAL_IGNORE_HUNT_TARGETS) && entity.isAttackable() && (entity instanceof Enemy || entity instanceof NeutralMob || (entity instanceof Player player && !player.isCreative() && !player.isSpectator()));
    private static final float[][] BLOCK_OFFSETS = {{-0.75F, -0.75F}, {-0.75F, 0.75F}, {0.75F, 0.75F}, {0.75F, -0.75F}};
    private static final ParticleComponent[] ATTRACT_COMPONENT = {new PropertyControl(EnumParticleProperty.RED, AnimData.startAndEnd(0.3F, 0.56F), false), new PropertyControl(EnumParticleProperty.GREEN, AnimData.startAndEnd(0.388F, 0.85F), false), new PropertyControl(EnumParticleProperty.BLUE, AnimData.startAndEnd(0.55F, 0.98F), false),};
    private static final EntityDataAccessor<Byte> DATA_STAGE = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_TELEPORT_TYPE = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_KATANA_HOLD = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ALWAYS_ACTIVE = SynchedEntityData.defineId(EntityImmortalBoss.class, EntityDataSerializers.BOOLEAN);
    private int hurtCount;
    private float damageAmountCumulative;
    private int stunCount;
    private int timeUntilAttract;
    private int timeUntilBlock;
    private int blockingHurtCount;
    private int timeUntilPounce;
    private int timeUntilSmash;
    private int timeUntilShoryuken;
    private int timeUntilShuriken;
    private int timeUntilTeleport;
    private int timeUntilLaser;
    private int battleTimestamp;
    private int destroyBlocksTick;
    private int closeProximityTickCount;
    private int unableAttackTickCount;
    private int universalCDTime;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] hand;//0:left 1:right
    //用于控制某些关键帧时段可以插入其他动画
    private boolean canInterruptsAnimation;
    private boolean LRFlag;//T:left F:right
    private Vec3 lPreHandPos = Vec3.ZERO;
    private Vec3 rPreHandPos = Vec3.ZERO;
    private final DamageAdaptation damageAdaptation;
    private final TickBasedProbabilityBooster smashDerivedSkillProb;
    public List<LivingEntity> targets = List.of();
    public final ControlledAnimation coreControlled = new ControlledAnimation(10);
    public final ControlledAnimation glowControlled = new ControlledAnimation(20);
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
        if (this.level().isClientSide) {
            this.hand = new Vec3[]{new Vec3(0, 0, 0), new Vec3(0, 0, 0)};
        }
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.EPIC_BOSS;
    }

    @Override
    public float getStepHeight() {
        return 2.5F;
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
        return new EMBodyRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new EMPathNavigateGround(this, level);
    }

    @Override
    protected ModConfigHandler.BossCommonConfig getBossConfig() {
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
        return !this.isActive() || ((animation == this.switchStageAnimation || animation == this.teleportAnimation || animation == this.pounceHoldAnimation || animation == this.armBlockCounterattackAnimation) || super.isInvulnerableTo(damageSource));
    }

    @Override
    public boolean isInvisible() {
        return !this.alphaControlled.isStop() || super.isInvisible();
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public void heal(float healAmount) {
        MinecraftForge.EVENT_BUS.post(new LivingHealEvent(this, healAmount));
        if (healAmount <= 0) return;
        float f = this.getHealth();
        if (f > 0.0F) {
            this.setHealth(f + healAmount);
        }
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.immortals.immortal.combatConfig;
    }

    @Override
    protected BossEvent.BossBarColor bossBarColor() {
        return BossEvent.BossBarColor.RED;
    }

    @Override
    protected boolean canShowBossBar() {
        return this.isActive();
    }

    @Override
    protected boolean setDarkenScreen() {
        return this.getAnimation() == this.unleashEnergyAnimation;
    }

    @Override
    protected void onAnimationStart(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.armBlockCounterattackAnimation) {
                this.blockingHurtCount = 0;
            } else if (animation == this.attractAnimation || animation == this.shoryukenAnimation) {
                if (!this.LRFlag) this.LRFlag = true;
            } else if (animation == this.unleashEnergyAnimation) {
                this.timeUntilLaser = this.getCoolingTimerUtil(LASER_TIME.sample(this.random), 10F);
            }
            if (animation == this.stunAnimation) {
                if (this.stunCount > 1) {
                    this.universalCDTime = animation.getDuration();
                    if (this.level().getDifficulty() == Difficulty.HARD) this.stunCount = 0;
                }
            } else this.unableAttackTickCount = 0;
        }
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (this.isSwitching()) this.setHealth(this.getMaxHealth());
            if (animation == this.armBlockEndAnimation) {
                this.blockingHurtCount = 0;
                this.blockEntity = null;
            } else if (animation == this.smashGround3Animation) {
                if (blockEntity != null) this.universalCDTime = this.random.nextInt(5) + 1;
                this.blockEntity = null;
            } else if (animation == this.unleashEnergyAnimation) {
                if (this.getTarget() != null && this.targetDistance >= 8) this.universalCDTime = 20;
            } else if (animation == this.trackingShurikenAnimation) {
                if (this.getTarget() instanceof Player && this.targets.size() < 2) this.universalCDTime = 20 + this.random.nextInt(10);
            } else if (animation == this.hurt1Animation) {
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
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
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
        this.goalSelector.addGoal(0, new ImmortalGroupAI(this, () -> switchStageAnimation, () -> teleportAnimation, () -> spawnAnimation));
        this.goalSelector.addGoal(1, new ImmortalSpecialGoal(this));
        this.goalSelector.addGoal(1, new ImmortalBlockGoal(this));
        this.goalSelector.addGoal(1, new ImmortalComboGoal(this));
        this.goalSelector.addGoal(1, new ImmortalMagicGoal(this));
        this.goalSelector.addGoal(1, new ImmortalPounceGoal(this));
        this.goalSelector.addGoal(1, new ImmortalShakeGroundGoal(this));
        this.goalSelector.addGoal(2, new ImmortalStunGoal(this));
        this.goalSelector.addGoal(2, new AnimationDie<>(this));
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
        if (this.isNoAnimation() || (animation != this.teleportAnimation && animation != this.pounceHoldAnimation && animation != this.shoryukenAnimation && animation != this.smashGround2Animation && !(animation != this.attractAnimation || tick >= 40))) {
            this.pushEntitiesAway(1.9F, getBbHeight(), 1.9F, 1.9F);
        }

        if (!this.isActive()) this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

        //动画效果
        if (ImmortalStage.STAGE1 == this.getStage()) {
            if (animation == this.punchRightAnimation || animation == this.punchLeftAnimation) {
                boolean right = animation == this.punchRightAnimation;
                this.doTrailEffect(animation, tick == 11, tick > 11 && tick <= 15, !right);
                if (tick == 12) this.doPuncturedAirFlowEffect(right);
            } else if (animation == this.hardPunchRightAnimation || animation == this.hardPunchLeftAnimation) {
                boolean right = animation == this.hardPunchRightAnimation;
                this.doTrailEffect(animation, tick == 19, tick > 19 && tick < 24, !right);
                if (tick == 20 || tick == 22) this.doPuncturedAirFlowEffect(right);
            } else if (animation == this.armBlockCounterattackAnimation) {
                if (tick == 2) {
                    this.doShakeGroundEffect(15, 0F, 0F, 0.5F, 0.94F, false, false);
                    this.doCollisionEffect(0, 30F, 0.5F);
                }
                if (tick == 3) this.shakeGround(0F, 20F, 0.2F, 3, 0);
                if (tick == 12) this.doImmortalMagicMatrixEffect(MagicCircleType.POWER, 7, 1.5F, 0.5F, 9F);
            } else if (animation == this.smashGround1Animation) {
                this.doTrailEffect(animation, tick == 17, tick > 17 && tick <= 21, null);
                if (tick == 20) this.doShakeGroundEffect(12, 2.5F, 0F, 0.58F, 0.94F, false, true);
            } else if (animation == this.smashGround2Animation) {
                this.doTrailEffect(animation, tick == 13, tick > 13 && tick <= 17, null);
                if (tick == 16) this.doShakeGroundEffect(14, 2.2F, 0F, 0.55F, 0.945F, false, true);
            } else if (animation == this.smashGround3Animation) {
                this.doTrailEffect(animation, tick == 10, tick > 10 && tick <= 20, null);
                if (tick == 19) {
                    float width = this.getBbWidth() / 2;
                    this.doShakeGroundEffect(9, 2.4F, width, 0.5F, 0.91F, false, false);
                    this.doShakeGroundEffect(9, 2.4F, width, 0.5F, 0.91F, true, false);
                } else if (this.level().isClientSide && tick == 21) {
                    Vec3 pos = this.getPosOffset(false, 2.4F, 0F, 0.2F);
                    ParticleDust.DustData particle = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.27f, 0.25f, 0.23f, 50f, 38, ParticleDust.EnumDustBehavior.GROW, 0.952F);
                    ModParticleUtils.annularParticleOutburst(this.level(), 12, particle, pos.x, pos.y, pos.z, 1.75F, 0.5F, 126F, 2F, this.getYRot());
                    ModParticleUtils.annularParticleOutburst(this.level(), 8, ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 0.5F, 0.1F, 130F, 1F, this.getYRot());
                }
            } else if (animation == this.pouncePreAnimation) {
                if (tick == 5) this.doImmortalMagicMatrixEffect(MagicCircleType.SPEED, 5, 2.5F, 0.4F, 8F);
            } else if (animation == this.pounceSmashAnimation) {
                this.doTrailEffect(animation, tick == 8, tick > 8 && tick <= 11, true);
                if (tick < 10 && this.level().isClientSide && this.getDeltaMovement().horizontalDistanceSqr() > 0.1) {
                    ModParticleUtils.blockParticleDirectionality(this.level(), this.getX(), this.getY() - 0.1, this.getZ(), Math.toRadians(this.getYRot()), 10, BLOCK_OFFSETS, pos -> this.level().getBlockState(pos), 5F);
                } else if (tick == 10) this.doShakeGroundEffect(15, 3.7F, 0F, 0.58F, 0.93F, false, true);
            } else if (animation == this.pouncePickAnimation) {
                this.doTrailEffect(animation, tick == 7, tick > 7 && tick < 12, null);
                if (tick < 10 && this.level().isClientSide && this.getDeltaMovement().horizontalDistanceSqr() > 0.1) {
                    ModParticleUtils.blockParticleDirectionality(this.level(), this.getX(), this.getY() - 0.1, this.getZ(), Math.toRadians(this.getYRot()), 10, BLOCK_OFFSETS, pos -> this.level().getBlockState(pos), 5F);
                }
            } else if (animation == this.pounceHoldAnimation) {
                if (tick == 1) {
                    if (!this.isSilent()) this.level().playLocalSound(this.blockPosition(), SoundInit.IMMORTAL_SUBSONIC.get(), this.getSoundSource(), 0.4F, 1.5F, false);
                    this.doHitEffect(10, this.random.nextInt(10), 60, 0, 0.55F, true);
                    ParticleDust.DustData particle = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.28f, 0.26f, 0.24f, 50f, 16, ParticleDust.EnumDustBehavior.SHRINK, 1f);
                    ModParticleUtils.roundParticleOutburst(this.level(), 10, new ParticleOptions[]{particle}, this.getX(), this.getY(0.25), this.getZ(), 1F);
                    for (LivingEntity hit : this.getNearByLivingEntities(5F)) {
                        double angle = this.getAngleBetweenEntities(this, hit);
                        double x = Math.cos(Math.toRadians(angle - 90));
                        double z = Math.sin(Math.toRadians(angle - 90));
                        hit.setDeltaMovement(x, 0.25, z);
                        if (hit instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(hit));
                        }
                    }
                } else if (this.level().isClientSide && this.getDeltaMovement().horizontalDistanceSqr() > 0.1) {
                    if (tick < 8 && tick % 2 == 0) {
                        this.level().addParticle(new ParticleRing.RingData((float) Math.toRadians(-this.getYRot()), (float) Math.toRadians(-this.getXRot()), 40, 0.56F, 0.85F, 0.98F, 0.14F, 90f - 20f * (tick / 6f), false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), this.getX() + this.getDeltaMovement().x * 1.5, this.getY(0.5), this.getZ() + this.getDeltaMovement().z * 1.5, 0, 0, 0);
                    }
                    ModParticleUtils.blockParticleDirectionality(this.level(), this.getX(), this.getY() - 0.1, this.getZ(), Math.toRadians(this.getYRot()), 10, BLOCK_OFFSETS, pos -> this.level().getBlockState(pos), 5F);
                }
            } else if (animation == this.attractAnimation) {
                this.doAttractEffect();
                if (tick == 32) this.doImmortalMagicMatrixEffect(MagicCircleType.POWER, 5, 1.5F, 0.45F, 9F);
                this.doTrailEffect(animation, tick == 35, tick > 35 && tick <= 42, false);
            } else if (animation == this.hurt1Animation) {
                if (tick == 15) this.doShakeGroundEffect(10, 1F, this.getBbWidth() * 0.6F, 0.55F, 0.91F, true, true);
            } else if (animation == this.shoryukenAnimation) {
                this.doTrailEffect(animation, tick == 14, tick > 14 && tick <= 20, false);
                this.doTrailEffect(animation, tick == 44, tick > 44 && tick <= 48, true);
                if (tick == 47) this.doShakeGroundEffect(12, 4.75F, 0F, 0.58F, 0.92F, false, true);
            }
        } else {
            //TODO 待补充
        }
        if (animation == this.trackingShurikenAnimation) {
            if (tick == 8) this.doImmortalMagicMatrixEffect(MagicCircleType.HARMFUL, 12, 2F, 0.5F, 15F);
        } else if (animation == this.unleashEnergyAnimation) {
            if (tick == 1) if (!this.isSilent()) this.level().playLocalSound(this.blockPosition(), SoundInit.IMMORTAL_ACCUMULATING.get(), this.getSoundSource(), 1.5F, 1F, false);
            if (tick >= 30 && tick < 80) {
                if (tick > 35) this.strongKnockBlock();
                this.doUnleashEnergyEffect();
            } else if (tick == 80) if (!this.isSilent()) this.level().playLocalSound(this.blockPosition(), SoundInit.IMMORTAL_ACCUMULATING_END.get(), this.getSoundSource(), 1.5F, 1F, false);
        } else if (animation == this.stunAnimation) {
            if (tick % 20 == 0 && tick < 80) this.glowControlled.resetTimer();
        } else if (animation == this.spawnAnimation) {
            if (!this.isActive()) this.setActive(true);
        } else if (animation == this.switchStageAnimation) {
            //TODO 待补充
        }
        this.yRotO = this.getYRot();
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!this.level().isClientSide) {
            LivingEntity target = this.getTarget();

            if (target != null && !target.isAlive()) this.setTarget(null);

            if (this.isSwitching()) this.heal(this.getMaxHealth() / this.switchStageAnimation.getDuration());

            //if (!this.isNoAi() && this.isAlwaysActive() && !this.isActive() && this.isNoAnimation()) this.playAnimation(this.spawnAnimation);

            boolean canAttack = !this.isNoAi() && target != null && !this.isSwitching() && this.universalCDTime <= 0;

            if (canAttack) {
                this.targetDistance = this.distanceTo(target) - target.getBbWidth() / 2;
                boolean hasTargetLineOfSight = this.getSensing().hasLineOfSight(target);
                boolean targetComingCloser = ModEntityUtils.checkTargetComingCloser(this, target);
                double targetRelativeHeight = Math.abs(this.getY() - target.getY());
                float targetRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this, target);
                float HP = this.getHealthPercentage();
                if (this.battleTimestamp == 0) this.battleTimestamp = this.tickCount;
                if (ImmortalStage.STAGE1 == this.getStage()) {
                    if (this.timeUntilPounce <= 0 && (this.isNoAnimation() || this.canInterruptsAnimation) && hasTargetLineOfSight && targetRelativeHeight < 6 && this.targetDistance > 12 && this.targetDistance < ImmortalPounceGoal.MAX_DISTANCE && this.random.nextFloat() < 0.5F) {
                        this.playAnimation(this.pouncePreAnimation);
                        this.timeUntilPounce = this.getCoolingTimerUtil(POUNCE_TIME.sample(this.random), 5F);
                    } else if (this.timeUntilShoryuken <= 0 && (this.isNoAnimation() || this.canInterruptsAnimation) && hasTargetLineOfSight && (!this.LRFlag || this.random.nextInt(10) == 0) && targetRelativeHeight < 5 && this.targetDistance > 4.5 && this.targetDistance < 9 && (targetRelativeAngle <= 30F && targetRelativeAngle >= -30F)) {
                        this.playAnimation(this.shoryukenAnimation);
                        if (HP < 0.8F) this.smashDerivedSkillProb.onTick();
                        this.timeUntilShoryuken = this.getCoolingTimerUtil(POUNCE_TIME.sample(this.random), 3F);
                    } else if (this.timeUntilSmash <= 0 && (this.isNoAnimation() || this.canInterruptsAnimation) && this.onGround() && targetRelativeHeight < 6 && this.targetDistance < 12 && this.random.nextFloat() < this.smashDerivedSkillProb.getProbability()) {
                        this.smashDerivedSkillProb.resetProbability();
                        boolean flag = (this.targetDistance < 6.5 || targetComingCloser && this.targetDistance < 7.5) || this.timeUntilBlock > 0;
                        this.playAnimation(flag ? this.smashGround2Animation : this.smashGround3Animation);
                        this.timeUntilSmash = this.getCoolingTimerUtil((int) (SMASH_TIME.sample(this.random) * 1.5), 5F);
                    } else if (this.timeUntilAttract <= 0 && (this.getCumulativeBattleTick() > 1600 || HP < 0.9F) && (this.isNoAnimation() || this.canInterruptsAnimation) && targetRelativeHeight < 6 && this.targetDistance >= 4.5 && this.targetDistance < 8 && (ModEntityUtils.isTargetFacingAway(this, target, 180) || this.random.nextFloat() < 0.3F)) {
                        this.playAnimation(this.attractAnimation);
                        this.timeUntilAttract = this.getCoolingTimerUtil(ATTRACT_TIME.sample(this.random), 5F);
                    } else if (this.timeUntilSmash <= 0 && this.canInterruptsAnimation && (this.getCumulativeBattleTick() > 1200 || HP < 0.9F) && targetRelativeHeight <= 5 && this.targetDistance <= 5 && this.random.nextFloat() < 1F - this.smashDerivedSkillProb.getProbability()) {
                        this.playAnimation(this.smashGround1Animation);
                        if (HP < 0.8F) this.smashDerivedSkillProb.onTick();
                        this.timeUntilSmash = this.getCoolingTimerUtil(SMASH_TIME.sample(this.random), 5F);
                    }
                }
                if (this.timeUntilLaser <= 0 && (this.getCumulativeBattleTick() > 2400 || HP < 0.6F) && this.isNoAnimation() && targetRelativeHeight < 10 && this.targetDistance >= 12 && this.targetDistance < EntityImmortalLaser.IMMORTAL_RADIUS) {
                    this.playAnimation(this.unleashEnergyAnimation);
                } else if (this.timeUntilShuriken <= 0 && (this.getCumulativeBattleTick() > 1800 || HP < 0.7F) && (this.isNoAnimation() || this.canInterruptsAnimation) && (this.targetDistance < 32 && (this.targetDistance > 5 || this.targets.size() >= 3))) {
                    this.playAnimation(this.trackingShurikenAnimation);
                    this.timeUntilShuriken = this.getCoolingTimerUtil(SHURIKEN_TIME.sample(this.random), 8F);
                } else if (this.timeUntilTeleport <= 0 && (this.isNoAnimation() || this.canInterruptsAnimation) && (this.hurtCount > 30 || this.unableAttackTickCount >= 150 || (this.closeProximityTickCount >= 400 && this.random.nextFloat() < 0.5F))) {
                    this.hurtCount = 0;
                    this.closeProximityTickCount = 0;
                    if (this.unableAttackTickCount >= 150) this.setTeleportType(TeleportType.FORCE);
                    else this.setTeleportType(ModEntityUtils.isTargetFacingAway(this, target, 180) ? TeleportType.FRONT : this.timeUntilPounce <= 0 && this.random.nextFloat() < this.getHealth() / this.getMaxHealth() ? TeleportType.BEHIND : TeleportType.SNEAK);
                    this.playAnimation(this.teleportAnimation);
                    this.timeUntilTeleport = this.getCoolingTimerUtil(200, 5F);
                } else if (this.attackTick <= 0 && this.isNoAnimation() && this.targetDistance <= 5 && this.random.nextFloat() < 0.65F) {
                    this.playAnimation(this.getBaseAttackByStage());
                    this.attackTick = 5;
                }
                if (this.targetDistance <= 5 && targetRelativeHeight <= 5) {
                    this.closeProximityTickCount++;
                    if (this.getCumulativeBattleTick() > 3600 || HP < 0.5F) this.closeProximityTickCount++;
                } else if (tickCount % 2 == 0) {
                    this.unableAttackTickCount++;
                    if (this.closeProximityTickCount > 0) this.closeProximityTickCount--;
                }
            }
            if (target == null && this.tickCount % 100 == 0 && this.battleTimestamp > 0) this.battleTimestamp = (int) (this.battleTimestamp * 0.5);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.universalCDTime > 0) this.universalCDTime--;
            if (this.isNoAnimation() && this.attackTick > 0) this.attackTick--;
            if (!this.inBlocking() && this.timeUntilBlock > 0) this.timeUntilBlock--;
            if (this.timeUntilSmash > 0) this.timeUntilSmash--;
            if (this.timeUntilAttract > 0) this.timeUntilAttract--;
            if (this.timeUntilPounce > 0) this.timeUntilPounce--;
            if (this.timeUntilShoryuken > 0) this.timeUntilShoryuken--;
            if (this.timeUntilShuriken > 0) this.timeUntilShuriken--;
            if (this.timeUntilTeleport > 0) this.timeUntilTeleport--;
            if (this.timeUntilLaser > 0) this.timeUntilLaser--;
            if (!this.inBlocking() && this.tickCount % 20 == 0 && this.hurtTime <= 0) this.hurtCount = 0;
            if (!this.isNoAi() && this.destroyBlocksTick > 0) {
                this.destroyBlocksTick--;
                if (this.destroyBlocksTick == 0) {
                    ModEntityUtils.breakBlocksInRect(this.level(), this, 50F, 2, 5, 2, 0, 0, this.checkCanDropItems(), true);
                }
            }
            if (!this.isNoAi() && this.tickCount % 30 == 0 && this.getTarget() != null) {
                this.targets = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32, 6, 32), e -> this.getTarget() == e || TARGET_CONDITIONS.test(e));
            }
        }
        int tick = this.getAnimationTick();
        Animation animation = this.getAnimation();
        this.hurtControlled.decreaseTimer();
        this.glowControlled.incrementOrDecreaseTimer(this.isActive() && !this.isDeadOrDying() && (animation != this.spawnAnimation || tick > 25));
        this.coreControlled.incrementOrDecreaseTimer(!this.isNoAnimation() && (animation != this.spawnAnimation && animation != this.unleashEnergyAnimation) && !this.inBlocking());
        this.alphaControlled.incrementOrDecreaseTimer(animation == this.teleportAnimation && tick >= 10 && tick <= 20, 2);
        if (animation == this.teleportAnimation) {
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
        if (!this.level().isClientSide) {
            Entity entity = source.getEntity();
            this.hurtCount++;
            this.lastDamageSource = source;
            byte pierceLevel = 0;
            if (source.getDirectEntity() instanceof AbstractArrow arrow) pierceLevel = arrow.getPierceLevel();
            if (entity != null) {
                boolean inUnleash = this.getAnimation() == this.unleashEnergyAnimation;
                if (inUnleash && (ModEntityUtils.isProjectileSource(source) || !ModEntityUtils.checkDirectEntityConsistency(source))) return false;
                float attackArc = 100F;
                float relativeAngle = ModEntityUtils.getTargetRelativeAngle(this, entity.position());
                boolean inFront = relativeAngle <= attackArc / 1.5F && relativeAngle >= -attackArc / 1.5F;
                boolean inBack = (relativeAngle <= -180F + attackArc / 2F && relativeAngle >= -180F - attackArc / 2F) || (relativeAngle >= 180F - attackArc / 2F && relativeAngle <= 180F + attackArc / 2F);
                if (!(source.is(DamageTypeTags.BYPASSES_ARMOR) || pierceLevel > 0) && !inFront && !inBack) damage *= 0.7F;
                /*
                    防守【格挡】/【反击】机制:
                    当伤害源来自正面且并非绕过无敌、伤害值>1、在无动作或可打断动作状态下可触发【格挡】
                    在【格挡】期间受到伤害减少90%与具有更少的攻击间隔，此时再次受到伤害时会延续格挡时间；反之则会再没有受到任何伤害的2.5秒后停止格挡
                    当格挡次数达到3次或伤害超过阈值时，触发【反击】
                    格挡冷却时间 = 150tick + 每1点伤害 × 3.3333tick
                 */
                if (!this.isNoAi() && this.timeUntilBlock <= 0 && (this.inBlocking() || (damage > 1F && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && inFront && (this.isNoAnimation() || this.canInterruptsAnimation)))) {
                    if (entity instanceof LivingEntity living) this.blockEntity = living;
                    float pitch = 0.4F * ModMathUtils.getTickFactor(damage, MAX_BLOCK_HURT_COUNT, false);
                    if (damage > MAX_COUNTERATTACK_DAMAGE_AMOUNT_THRESHOLD || ++this.blockingHurtCount >= MAX_BLOCK_HURT_COUNT || (this.damageAmountCumulative += damage) > MAX_CUMULATIVE_COUNTERATTACK_DAMAGE_THRESHOLD) {
                        this.damageAmountCumulative = 0;
                        this.timeUntilBlock = 150 + (int) Math.min(damage * 3.3333F, 150F);
                        this.playSound(SoundInit.IMMORTAL_BLOCKING_COUNTER.get(), 1.5F, 1.3F - pitch);
                        this.playAnimation(this.armBlockCounterattackAnimation);
                        return false;
                    } else if (this.isNoAnimation()) this.playAnimation(this.armBlockAnimation);
                    else this.playAnimation(this.armBlockHoldAnimation);
                    this.playSound(SoundInit.IMMORTAL_BLOCKING.get(), 0.9F, this.getVoicePitch() + pitch - 0.2F);
                    this.level().broadcastEntityEvent(this, (byte) 5);
                    this.invulnerableTime /= 2;
                    super.hurt(source, damage * 0.1F);
                    return false;
                }
            }
            damage *= 1F - Mth.clamp((this.targets.size() - 1) * 4F, 0F, 20F) / 100F;
            if (this.getAnimation() != this.stunAnimation && (this.invulnerableTime <= 10 || source.is(DamageTypeTags.BYPASSES_COOLDOWN))) {
                damage = this.damageAdaptation.damageAfterAdaptingOnce(this, this.lastDamageSource, damage);
                if (this.damageAdaptation.isFullyAdapted(this, this.lastDamageSource)) this.level().broadcastEntityEvent(this, (byte) 4);
            }
            if (entity != null || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                boolean flag = super.hurt(source, damage);
                if (flag) this.stunCheck(source, damage);
                return flag;
            } else if (this.destroyBlocksTick <= 0) this.destroyBlocksTick = 20;
        } else if (this.inBlocking()) this.hurtTime = 0;
        return false;
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
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.AIR)) {
            return InteractionResult.PASS;
        } else if (this.isActive() || this.isNoAi()) {
            return InteractionResult.PASS;
        } else if (itemStack.is(ItemInit.GUARDIAN_CORE.get())) {
            if (!player.getAbilities().instabuild) itemStack.shrink(1);
            this.playAnimation(this.spawnAnimation);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
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
        this.heal(Math.min(entity.getMaxHealth() * 0.05F + maxHealth * 0.01F, maxHealth * 0.05F));
        return super.killedEntity(level, entity);
    }

    /**
     * 是否触发硬直检查
     *
     * @param source 伤害源
     * @param damage 伤害量
     */
    private void stunCheck(DamageSource source, float damage) {
        if (this.getAnimation() == this.attractAnimation && this.getAnimationTick() < 50 && damage >= 5F && source.is(DamageTypeTags.IS_EXPLOSION)) {
            Entity entity = source.getEntity();
            if (entity instanceof LivingEntity livingEntity) this.blockEntity = livingEntity;
            if (!this.level().isClientSide) this.level().broadcastEntityEvent(this, (byte) 11);
            this.playAnimation(this.hurt1Animation);
            this.stunCount++;
        }
    }

    public int getCumulativeBattleTick() {
        if (this.battleTimestamp > 0 && this.battleTimestamp < this.tickCount) {
            return this.tickCount - this.battleTimestamp;
        }
        return 0;
    }

    public boolean checkComboRange(double range, double checkRange) {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return true;
        } else if (target.isAlive()) {
            boolean targetComingCloser = ModEntityUtils.checkTargetComingCloser(this, target);
            return this.targetDistance < range || (this.targetDistance < range + checkRange && targetComingCloser);
        }
        return false;
    }

    public boolean doHurtTarget(LivingEntity target, boolean disableShield, boolean addEffect, boolean critHeal, boolean ignoreArmor, float baseDamageMultiplier, float damageMultiplier) {
        return doHurtTarget(ModDamageSource.immortalAttack(this, critHeal && target.getAttributeValue(AttributeInit.CRIT_CHANCE.get()) > 1D, ignoreArmor), target, disableShield, addEffect, ignoreArmor, baseDamageMultiplier, damageMultiplier);
    }

    public boolean doHurtTarget(DamageSource source, LivingEntity target, boolean disableShield, boolean addEffect, boolean ignoreArmor, float baseDamageMultiplier, float damageMultiplier) {
        double baseATK = this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        //当目标数量＞1时，根据目标数量增加攻击伤害，每个目标增加0.4基础攻击伤害
        baseATK += Mth.clamp((targets.size() - 1) * 0.4F, 0F, 2F);
        boolean flag = target.hurt(source, (float) ((baseATK * baseDamageMultiplier) + getDamageAmountByTargetHealthPct(target)) * damageMultiplier);
        if (flag && addEffect) {
            ModEntityUtils.addEffectStackingAmplifier(this, target, EffectInit.ERODE_EFFECT.get(), 300, 5, true, true, true, true, false);
        } else if (disableShield) {
            Difficulty difficulty = this.level().getDifficulty();
            this.disableShield(target, Difficulty.HARD == difficulty ? 110 : 100);
        }
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

    public void pursuit(float pursuitDistance, float y, double speedModifier) {
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
        return a == this.armBlockEndAnimation || a == this.armBlockHoldAnimation || a == this.armBlockAnimation;
    }

    private Animation getBaseAttackByStage() {
        try {
            if (this.getStage() == ImmortalStage.STAGE1) {
                return this.LRFlag ? this.punchLeftAnimation : this.punchRightAnimation;
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
        return this.getAnimation() == this.switchStageAnimation;
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
            int distance = 16;
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

    private int getCoolingTimerUtil(int baseCD, float coolDownReductionPerTarget) {
        int targetCount = this.targets.size() - 1;
        if (targetCount <= 0) return baseCD;
        return (int) (baseCD * (1 - (Math.min(targetCount, 5) * coolDownReductionPerTarget) / 100));
    }

    private void reflectPotionEffect(MobEffectInstance effectInstance, Entity entity) {
        if (entity != this && entity instanceof LivingEntity bouncer && !effectInstance.getEffect().isInstantenous()) {
            bouncer.forceAddEffect(effectInstance, null);
        }
    }

    private void nextBossStage() {
        if (this.getHealth() <= 0) this.setHealth(0.1F);
        this.stopAllSuperpositionAnimation();
        this.playAnimation(this.switchStageAnimation);
        ImmortalStage stage = ImmortalStage.STAGE2;
        String stageStr = this.getStage().toString().toLowerCase();
        addImmortalHAAModifier(this.getAttribute(Attributes.MAX_HEALTH), UUID.fromString("E2F534E6-4A55-4B72-A9D2-3157D084A281"), stageStr, stage.addHealth);
        addImmortalHAAModifier(this.getAttribute(Attributes.ARMOR), UUID.fromString("FA2FB8E8-FFE8-4D77-B23B-E0110D4A175F"), stageStr, stage.addArmor);
        addImmortalHAAModifier(this.getAttribute(Attributes.ATTACK_DAMAGE), UUID.fromString("F8DBD65D-3C4A-4851-83D3-4CB22964A196"), stageStr, stage.addAttack);
        this.entityData.set(DATA_STAGE, stage.id);
        this.damageAdaptation.clearCache();
    }

    private static void addImmortalHAAModifier(@Nullable AttributeInstance instance, UUID uuid, String stageStr, float amount) {
        if (instance != null) {
            instance.removePermanentModifier(uuid);
            instance.addPermanentModifier(new AttributeModifier(uuid, "Immortal " + stageStr + " modifier", amount, AttributeModifier.Operation.ADDITION));
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
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.BIG_RING.get(), pos.x, pos.y, pos.z, 0, 0, 0, false, (float) Math.toRadians(-this.yBodyRot), 0, 0, 0, scale, 1, 1, 1, 1, 1, duration, true, false, false, new ParticleComponent[]{new PropertyControl(EnumParticleProperty.ALPHA, new KeyTrack(new float[]{1, 0.5F, 0.1F}, new float[]{0, 0.9F, 1}), false), new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(0, scale), false), new PropertyControl(EnumParticleProperty.RED, new KeyTrack(new float[]{0.18F, magicType.r}, new float[]{0.4F, 0.8F}), false), new PropertyControl(EnumParticleProperty.GREEN, new KeyTrack(new float[]{0.44F, magicType.g}, new float[]{0.4F, 0.8F}), false), new PropertyControl(EnumParticleProperty.BLUE, new KeyTrack(new float[]{0.6F, magicType.b}, new float[]{0.4F, 0.8F}), false),});
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
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.BIG_RING.get(), x, y, z, 0, 0.007, 0, false, Math.toRadians(-this.getYRot()), (float) Math.toRadians(this.random.nextBoolean() ? -5 : 5), 0, 0, 1, 1, 1, 1, 1, 1, 10, true, false, false, new ParticleComponent[]{new Attractor(new Vec3[]{this.position().add(sideX + frontX, this.getBbHeight() * 0.5, sideZ + frontZ)}, 1.5F + (1F - tickFactor), 0, Attractor.EnumAttractorBehavior.EXPONENTIAL), new PropertyControl(EnumParticleProperty.SCALE, new KeyTrack(new float[]{40F + 10F * tickFactor, 0.1F}, new float[]{0, 1}), false), new PropertyOverLength(EnumRibbonProperty.ALPHA, KeyTrack.startAndEnd(0.6F, 0F)), ATTRACT_COMPONENT[0], ATTRACT_COMPONENT[1], ATTRACT_COMPONENT[2]});
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
        if (animation == this.shoryukenAnimation || animation == this.pounceSmashAnimation || animation == this.pouncePickAnimation) {
            if (animation == this.shoryukenAnimation) ModParticleUtils.blockParticleDirectionality(this.level(), rightPos.x, rightPos.y - 2F, rightPos.z, Math.toRadians(this.getYRot()), 15, BLOCK_OFFSETS, pos -> this.level().getBlockState(pos), 3F);
            speedMultiplier = 0.25F;
            duration = 10;
        } else if (animation == this.attractAnimation || animation == this.hardPunchRightAnimation || animation == this.hardPunchLeftAnimation) {
            speedMultiplier = 0.12F;
        } else if (animation == this.smashGround1Animation || animation == this.smashGround2Animation || animation == this.smashGround3Animation) {
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 400.0D).add(Attributes.ARMOR, 10.0D).add(Attributes.ARMOR_TOUGHNESS, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.36D).add(Attributes.FOLLOW_RANGE, 64.0D).add(Attributes.ATTACK_DAMAGE, 12.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(ForgeMod.ENTITY_GRAVITY.get(), 0.09D);
    }

    public int getTimeUntilLaser() {
        return this.timeUntilLaser;
    }

    public int getStunCount() {
        return this.stunCount;
    }

    public void setInterruptsAnimation(boolean flag) {
        this.canInterruptsAnimation = flag;
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
    public void playAnimation(Animation animation) {
        this.canInterruptsAnimation = false;
        super.playAnimation(animation);
    }

    @Override
    public boolean isGlow() {
        return !this.glowControlled.isStop();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        if (this.getAnimation() != this.stunAnimation && this.damageAdaptation.isFullyAdapted(this, damageSource)) {
            this.playSound(SoundInit.IMMORTAL_ADAPT.get(), 1F, this.getVoicePitch() + 0.1F);
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
        if (this.getTarget() == null && this.isActive() && this.getAnimation() != this.spawnAnimation) {
            super.playAmbientSound();
        }
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    public void anchorToGround() {
        this.setDeltaMovement(0, !this.onGround() && this.getDeltaMovement().y > 0 ? -0.005D : this.getDeltaMovement().y, 0);
    }

    @Override
    protected boolean canPlayMusic() {
        return this.isActive() && super.canPlayMusic();
    }

    @Override
    public SoundEvent getBossMusic() {
        return SoundInit.THE_ARMY_OF_MINOTAUR.get();
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
        return this.dieAnimation;
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
    }

    static class ImmortalGroupAI extends AnimationGroupAI<EntityImmortalBoss> {
        @SafeVarargs
        public ImmortalGroupAI(EntityImmortalBoss entity, Supplier<Animation>... animations) {
            super(entity, animations);
        }

        @Override
        public void tick() {
            Animation animation = this.entity.getAnimation();
            if (animation == this.entity.teleportAnimation) {
                LivingEntity target = this.entity.getTarget();
                if (target != null) {
                    this.entity.lookAt(target, 90F, 30F);
                    this.entity.getLookControl().setLookAt(target, 90F, 30F);
                }
            } else if (animation == this.entity.spawnAnimation) {
                //TODO 待补充
            } else if (animation == this.entity.switchStageAnimation) {
                //TODO 待补充
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
