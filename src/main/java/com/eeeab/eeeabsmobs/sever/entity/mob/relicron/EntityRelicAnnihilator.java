package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.AnimData;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent.PropertyControl;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.eeeab.eeeabsmobs.client.particle.util.RibbonComponent;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityAnnihilatorMissile;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.mob.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate.GuardianLeapGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityInfraredRay;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.message.SyncMuzzlePosMessage;
import com.eeeab.eeeabsmobs.sever.util.ModMathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.level.block.Blocks;
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
import java.util.function.Supplier;

public class EntityRelicAnnihilator extends EntityAbsRelicron implements IBoss, RangedAttackMob {
    public final Animation dieAnimation = Animation.create(30);
    public final Animation slashAnimation = Animation.create(50).doesOverlap();
    public final Animation swingAnimation = Animation.create(50).doesOverlap();
    public final Animation stabAnimation = Animation.create(60).doesOverlap();
    public final Animation cycloneAnimation = Animation.create(60);
    public final Animation shot1Animation = Animation.create(35);
    public final Animation shot2Animation = Animation.create(20).doesLoop();
    public final Animation shot3Animation = Animation.create(15);
    public final Animation trickshot1Animation = Animation.create(20);
    public final Animation trickshot2Animation = Animation.create(20).doesLoop();
    public final Animation trickshot3Animation = Animation.create(20);
    public final Animation laserAnimation = Animation.create(90);
    public final Animation groundPoundAnimation = Animation.create(50);
    public final Animation groundsSlam1Animation = Animation.create(15);
    public final Animation groundsSlam2Animation = Animation.create(100);
    public final Animation groundsSlam3Animation = Animation.create(40);
    public final Animation stunAnimation = Animation.create(80);
    private final Animation[] animations = new Animation[]{
            this.dieAnimation,
            this.slashAnimation,
            this.swingAnimation,
            this.stabAnimation,
            this.cycloneAnimation,
            this.shot1Animation,
            this.shot2Animation,
            this.shot3Animation,
            this.trickshot1Animation,
            this.trickshot2Animation,
            this.trickshot3Animation,
            this.laserAnimation,
            this.groundPoundAnimation,
            this.groundsSlam1Animation,
            this.groundsSlam2Animation,
            this.groundsSlam3Animation,
            this.stunAnimation
    };
    private static final EntityDataAccessor<Boolean> DATA_BLIND = SynchedEntityData.defineId(EntityRelicAnnihilator.class, EntityDataSerializers.BOOLEAN);
    private int blindnessDuration;
    private int timeUntilAttack;
    private int timeUntilStab;
    private int timeUntilCyclone;
    private int timeUntilShot;
    private int timeUntilSlam;
    private int timeUntilLaser;
    private int timeUntilPound;
    private boolean LRFlag;//T:left F:right
    @OnlyIn(Dist.CLIENT)
    public Vec3 muzzle;
    private Vec3 preMuzzle = Vec3.ZERO;
    @OnlyIn(Dist.CLIENT)
    public Vec3 saw;
    private Vec3 preSaw = Vec3.ZERO;
    private int serverLastFireTick = -100;
    private final EntityRelicAnnihilatorPart scope;
    private final EntityRelicAnnihilatorPart[] subEntities;
    public final ControlledAnimation controlled = new ControlledAnimation(10);
    public final ControlledAnimation sawControlled = new ControlledAnimation(10);

    public EntityRelicAnnihilator(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.LRFlag = level.random.nextBoolean();
        this.clearRedundantAnimationsOnDeath = true;
        this.scope = new EntityRelicAnnihilatorPart(this, "scope", 0.825F, 0.75F);
        this.subEntities = new EntityRelicAnnihilatorPart[]{this.scope};
        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        if (this.level().isClientSide) {
            this.muzzle = new Vec3(0, 0, 0);
            this.saw = new Vec3(0, 0, 0);
        }
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.BOSS;
    }

    @Override
    public boolean isStunned() {
        return super.isStunned() || this.getAnimation() == this.stunAnimation;
    }

    @Override
    public float getStepHeight() {
        return 1.5F;
    }

    @Override//是否免疫摔伤
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
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
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
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
        return new EMBodyRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new EMPathNavigateGround(this, level);
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicAnnihilator.combatConfig;
    }

    @Override
    protected ModConfigHandler.BossCommonConfig getBossConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicAnnihilator.bossConfig;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityAbsRelicron.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(0, new AnimationSimpleAI<>(this, () -> stunAnimation));
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new GAMeleeAttackGoal(this));
        this.goalSelector.addGoal(1, new GARangeAttackGoal(this));
        this.goalSelector.addGoal(1, new GACycloneAttackGoal(this));
        this.goalSelector.addGoal(1, new GALeapAttackGoal(this));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, () -> groundsSlam3Animation, 4.75F, 3, 1.25F, 1.2F, true));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, () -> groundPoundAnimation, 4.5F, 20, 1.25F, 1.2F, true));
        this.goalSelector.addGoal(2, new KeepDistanceGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        this.controlled.updatePrevTimer();
        this.sawControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);
        LivingEntity target = this.getTarget();

        if (!this.level().isClientSide && !this.isNoAi() && this.isActive()) {
            if (target != null) {
                float HP = this.getHealthPercentage();
                float targetRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this, target);
                if (Math.abs(this.getY() - target.getY()) <= this.getBbHeight()) {
                    if (this.timeUntilAttack <= 0 && this.isNoAnimation() && this.targetDistance < 4.5 && this.random.nextFloat() < 0.7F) {
                        this.playAnimation(this.LRFlag ? this.swingAnimation : this.slashAnimation);
                        this.LRFlag = !this.LRFlag;
                        this.timeUntilAttack = 100 + Mth.randomBetweenInclusive(this.random, 60, 100);//8~10
                        this.timeUntilAttack = this.getCoolingTimerUtil(this.timeUntilAttack, this.timeUntilAttack - 40, 0.3F);
                    } else if (this.timeUntilStab <= 0 && this.isNoAnimation() && this.targetDistance > 6 && this.targetDistance <= 18 && targetRelativeAngle <= 60F && targetRelativeAngle >= -60F && (target.isOnFire() || this.random.nextFloat() < 0.4F)) {
                        this.playAnimation(this.stabAnimation);
                        this.timeUntilStab = 120 + Mth.randomBetweenInclusive(this.random, 120, 240);//12~18
                        this.timeUntilStab = this.getCoolingTimerUtil(this.timeUntilStab, this.timeUntilStab - 40, 0.3F);
                    } else if (HP != 1 && this.timeUntilCyclone <= 0 && this.isNoAnimation() && this.targetDistance > 4 && this.targetDistance < 7 && targetRelativeAngle <= 60F && targetRelativeAngle >= -60F && (target.isOnFire() || this.random.nextFloat() < 0.4F)) {
                        this.playAnimation(this.cycloneAnimation);
                        this.timeUntilCyclone = 120 + Mth.randomBetweenInclusive(this.random, 120, 240);//12~18
                        this.timeUntilCyclone = this.getCoolingTimerUtil(this.timeUntilCyclone, this.timeUntilCyclone - 40, 0.3F);
                    } else if (this.isNoAnimation() && this.validateGroundPound()) {
                        this.playAnimation(this.groundPoundAnimation);
                    }
                }
                boolean hasLineOfSight = this.getSensing().hasLineOfSight(target);
                if (HP <= 0.85F && !this.isBlinded() && this.timeUntilLaser <= 0 && this.isNoAnimation() && this.targetDistance > 9 && this.targetDistance <= 24 && this.random.nextFloat() < 0.6F) {
                    this.playAnimation(this.laserAnimation);
                    this.timeUntilLaser = 250 + Mth.randomBetweenInclusive(this.random, 130, 230);//19~24
                } else if (this.timeUntilShot <= 0 && this.isNoAnimation() && this.targetDistance >= 4.5 && this.targetDistance < 32 && hasLineOfSight && this.random.nextFloat() < 0.5F) {
                    this.playAnimation(this.targetDistance < 7 ? this.trickshot1Animation : this.shot1Animation);
                    this.timeUntilShot = 200 + Mth.randomBetweenInclusive(this.random, 140, 240);//17~22
                    this.timeUntilShot = this.getCoolingTimerUtil(this.timeUntilShot, this.timeUntilShot - 60, 0.5F);
                } else if (this.timeUntilSlam <= 0 && this.isNoAnimation() && this.targetDistance > 14 && hasLineOfSight && this.random.nextFloat() < 0.5F) {
                    this.playAnimation(this.groundsSlam1Animation);
                    this.timeUntilSlam = 200 + Mth.randomBetweenInclusive(this.random, 100, 200);//15~20
                    this.timeUntilSlam = this.getCoolingTimerUtil(this.timeUntilSlam, this.timeUntilSlam - 50, 0.3F);
                }
            }
            if (target != null && !target.isAlive()) this.setTarget(null);
        }


        Animation animation = this.getAnimation();
        int tick = this.getAnimationTick();
        if (animation == this.slashAnimation) {
            this.sawControlled.incrementOrDecreaseTimer(tick >= 10 && tick < 25);
            if (tick == 15) this.playSound(SoundInit.RELIC_ANNIHILATOR_ATTACK.get(), 1F, this.getVoicePitch());
            this.doTrailEffect(tick == 17, tick > 17 && tick < 25, false);
        } else if (animation == this.swingAnimation) {
            this.sawControlled.decreaseTimer();
            if (tick == 15) this.playSound(SoundInit.RELIC_ANNIHILATOR_ATTACK.get(), 1F, this.getVoicePitch());
            this.doTrailEffect(tick == 17, tick > 17 && tick < 25, true);
        } else if (animation == this.stabAnimation) {
            if (tick == 5) this.playSound(SoundInit.RELIC_ANNIHILATOR_PRE_ATTACK.get(), 1F, this.getVoicePitch());
            this.sawControlled.incrementOrDecreaseTimer(tick >= 8 && tick < 45);
            this.doTrailEffect(tick == 21, tick > 21 && tick < 32, false);
            if (tick > 31 && tick < 42) this.doFractalEffect();
        } else if (animation == this.groundPoundAnimation) {
            this.anchorToGround();
            this.sawControlled.decreaseTimer();
            if (tick == 4) this.playSound(SoundInit.RELIC_ANNIHILATOR_PRE_ATTACK.get(), 1F, this.getVoicePitch());
            if (tick == 5) this.playSound(SoundInit.RELIC_ANNIHILATOR_SMASH.get(), 1F, this.getVoicePitch());
            if (tick == 20) this.doGroundPoundEffect(2.1F, false);
            this.doTrailEffect(tick == 18, tick > 18 && tick < 21, true);
        } else if (animation == this.groundsSlam1Animation) {
            if (tick == 4) this.playSound(SoundInit.RELIC_ANNIHILATOR_JUMP.get(), 1F, this.getVoicePitch());
            if (tick == 13) this.doGroundPoundEffect(0F, false);
        } else if (animation == this.groundsSlam3Animation) {
            this.anchorToGround();
            if (tick == 1) this.playSound(SoundInit.RELIC_ANNIHILATOR_SMASH.get(), 1F, this.getVoicePitch() + 1.2F);
            if (tick == 4) this.doGroundPoundEffect(2.25F, true);
            this.doTrailEffect(tick == 16, tick > 16 && tick < 21, true);
        } else if (animation == this.cycloneAnimation) {
            this.sawControlled.incrementOrDecreaseTimer(tick >= 5 && tick < 40);
            if (tick == 5) this.playSound(SoundInit.RELIC_ANNIHILATOR_PRE_ATTACK.get(), 1F, this.getVoicePitch());
            if (tick > 16 && tick < 37 && tick % 5 == 0) this.playSound(SoundInit.RELIC_ANNIHILATOR_WHOOSH.get(), 2.5F, this.getVoicePitch());
            if (tick > 20 && tick < 39) this.doCycloneEffect();
            this.doTrailEffect(tick == 22, tick > 22 && tick < 43, false);
        } else if (animation == this.laserAnimation) {
            if (tick == 5) this.playSound(SoundInit.RELIC_ANNIHILATOR_ACTIVE_SCOPE.get(), 1F, this.getVoicePitch());
            this.anchorToGround();
            this.doShotLaserEffect();
        } else if (animation == this.dieAnimation || animation == this.stunAnimation) {
            this.sawControlled.decreaseTimer();
            if (this.level().isClientSide) {
                if (tick < 7) {
                    int count = this.random.nextInt(3) + 1;
                    Vec3 pos = this.getPosOffset(false, -this.getBbWidth() * 0.2F, this.getBbWidth() * 0.1F, this.getEyeHeight() * 0.9F);
                    for (int i = 0; i < count; i++) {
                        Vec3 spawnPos = pos.offsetRandom(this.random, 1.2F);
                        this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), spawnPos.x, spawnPos.y, spawnPos.z, 0, 0, 0);
                    }
                }
                if (animation == this.stunAnimation) {
                    if (tick == 1) {
                        Vec3 pos = this.getPosOffset(false, this.getBbWidth() * 0.5F, 0, this.getEyeHeight() * 0.8F);
                        this.level().addParticle(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 0, 0, 0);
                    } else if (tick >= 7 && tick % 5 == 1 && tick < 65) {
                        Vec3 pos = this.getPosOffset(false, this.getBbWidth() * 0.9F, 0, this.getEyeHeight() * 0.6F);
                        Vec3 spawnPos = pos.offsetRandom(this.random, 0.5F);
                        this.level().addParticle(ParticleTypes.LARGE_SMOKE, spawnPos.x, spawnPos.y, spawnPos.z, -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D, -0.15D + this.random.nextDouble() * 0.15D);
                    }
                }
            } else if (animation == this.stunAnimation) {
                if (tick == 1) this.playSound(SoundInit.RELIC_ANNIHILATOR_STUN.get(), 2F, this.getVoicePitch() + 0.2F);
                if (tick < 10 && target != null) this.backOff(target, 0.6 * ModMathUtils.getTickFactor(tick, 9, true));
            }
        } else {
            if (animation == this.shot2Animation || animation == this.trickshot2Animation) {
                if (tick == 10) this.doMuzzleFlashEffect();
            } else {
                boolean b0 = animation == this.trickshot1Animation;
                boolean b1 = animation == this.shot1Animation;
                if (b1 && tick == 15) this.playSound(SoundInit.RELIC_ANNIHILATOR_ACTIVE_SCOPE.get(), 1F, this.getVoicePitch());
                else if (b0 && tick == 7) this.playSound(SoundInit.RELIC_ANNIHILATOR_ACTIVE_SCOPE.get(), 1F, this.getVoicePitch());
                this.controlled.incrementOrDecreaseTimer(b1 || b0);
                this.sawControlled.incrementOrDecreaseTimer(tick >= 12 && b0);
            }
        }

        if (this.level().isClientSide && !this.isNoAi() && this.isAlive() && this.getDeltaMovement().horizontalDistanceSqr() > 0.005 && animation != this.cycloneAnimation) {
            float bbWidth = this.getBbWidth();
            float width = bbWidth * 0.45F;
            float frontBack = 0;
            if (animation == this.stabAnimation) width = bbWidth * 0.55F;
            if (animation == this.trickshot2Animation) {
                frontBack = bbWidth * -0.3F;
                this.doWalkEffect(this.getPosOffset(true, frontBack, bbWidth * 1.1F, 0));
            }
            this.doWalkEffect(this.getPosOffset(true, frontBack, width, 0));
            this.doWalkEffect(this.getPosOffset(false, 0, width, 0));
        }

        if (this.level().isClientSide && !this.isSilent() && !this.sawControlled.isStop() && this.tickCount % 4 == 1) {
            this.level().playLocalSound(this.saw.x, this.saw.y, this.saw.z, SoundInit.RELIC_ANNIHILATOR_SAW.get(), this.getSoundSource(),
                    this.getVoicePitch() * this.sawControlled.getAnimationFraction(), this.getVoicePitch() + 0.2F, false);
        }
    }

    public void anchorToGround() {
        this.setDeltaMovement(0, !this.onGround() && this.getDeltaMovement().y > 0 ? -0.005D : this.getDeltaMovement().y, 0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isNoAnimation()) this.sawControlled.decreaseTimer();
        if (!this.level().isClientSide) {
            if (this.timeUntilAttack > 0) this.timeUntilAttack--;
            if (this.timeUntilStab > 0) this.timeUntilStab--;
            if (this.timeUntilCyclone > 0) this.timeUntilCyclone--;
            if (this.timeUntilShot > 0) this.timeUntilShot--;
            if (this.timeUntilSlam > 0) this.timeUntilSlam--;
            if (this.timeUntilLaser > 0) this.timeUntilLaser--;
            if (this.timeUntilPound > 0) this.timeUntilPound--;
            if (this.blindnessDuration > 0) this.blindnessDuration--;
            else this.setBlind(false);
        }

        if (!this.isNoAi()) {
            Vec3[] avec3 = new Vec3[]{new Vec3(this.subEntities[0].getX(), this.subEntities[0].getY(), this.subEntities[0].getZ())};
            float y = this.getEyeHeight();
            float sides = 0;
            float frontBack = 0.5F;
            Animation animation = this.getAnimation();
            if (animation == this.shot1Animation || animation == this.shot2Animation) {
                y *= 0.86F;
                sides = this.getBbWidth() * 0.18F;
                frontBack = 0.4F;
            } else if (animation == this.trickshot1Animation || animation == this.trickshot2Animation) {
                y *= 0.77F;
                frontBack = 0.7F;
            } else if (animation == this.laserAnimation) {
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
                if (animation == this.laserAnimation || animation == this.shot2Animation || animation == this.shot3Animation) {
                    multiplier = 0.2F;
                }
            } else multiplier = 1F;
            if (isCriticalSpot) {
                damage += this.getMaxHealth() * 0.05F;
                multiplier = 1F;
            } else if (this.isStunned()) multiplier = 1.1F;
            return super.hurt(source, damage * multiplier);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(1.5D), this.getY() + (0.1F + (this.getBbHeight() * 0.75) * this.random.nextDouble()), this.getRandomZ(1.5D), d0, d1, d2);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BLIND, false);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 300.0D).
                add(Attributes.MOVEMENT_SPEED, 0.34D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.ATTACK_DAMAGE, 12.0D).
                add(ForgeMod.ENTITY_GRAVITY.get(), 0.125D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).
                add(Attributes.ARMOR, 15.0D);
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
    public Animation[] getAnimations() {
        return this.animations;
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
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

    public boolean doHurtTarget(LivingEntity entity, float damageMultiplier, float knockBackMultiplier, boolean canDisableShield, boolean charged) {
        if (this.doHurtTarget(entity, damageMultiplier, knockBackMultiplier, canDisableShield)) {
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
        AABB box1 = entity.getBoundingBox();
        double length1 = box1.getXsize();
        double width1 = box1.getZsize();
        double height1 = box1.getYsize();
        AABB box2 = target.getBoundingBox();
        double length2 = box2.getXsize();
        double width2 = box2.getZsize();
        double height2 = box2.getYsize();
        boolean flag = (length1 * width1 * height1) >= (length2 * width2 * height2);
        return target.isAlive() && !target.isSpectator() && !target.isPassenger() && flag;
    }

    public boolean validateGroundPound() {
        float HP = this.getHealth() / this.getMaxHealth();
        float prob = 0.1F;
        if (HP <= 0.3F) prob += 0.7F;
        else prob += 0.7F * (1 - (HP - 0.3F) / 0.7F);
        if (this.targetDistance <= 3.5 && this.timeUntilPound <= 0 && this.random.nextFloat() > prob) {
            this.timeUntilPound = 100 + Mth.randomBetweenInclusive(this.random, 150, 250);
            this.timeUntilPound = this.getCoolingTimerUtil(this.timeUntilPound, this.timeUntilPound - 50, 0.3F);
            return true;
        }
        return false;
    }

    private void doGroundPoundEffect(float offset, boolean block) {
        Vec3 pos = this.getPosOffset(false, offset, 0.2F, 0);
        if (this.level().isClientSide) {
            int[] particles = {15, 20, 10};
            double[] radii = {1, 1.5, 2};
            double[] speeds = {1, 0.9, 0.8};
            double[] angles = {35, 25, 15};
            double[] color = {0.8, 0.8, 0.8, 0.5F};
            ModParticleUtils.multiLayerBowlParticles(this.level(), pos, 2, particles, radii, speeds, angles, color);
            this.level().addParticle(new ParticleRing.RingData(0F, (float) (Math.PI / 2F), 10, 0.8F, 0.8F, 0.8F, 0.8F, 90F, false, ParticleRing.EnumRingBehavior.GROW), pos.x, pos.y + 0.5F, pos.z, 0, 0, 0);
            ModParticleUtils.blockParticlesAround(this.level(), pos.x, pos.y - 0.2F, pos.z, 40, 0.5, 2.5, block ? 5 : 3, block ? 12 : 6, block ? 8 : 3, block ? 10 : 6, -0.2, 0.1);
        }
        if (block) ShockWaveUtils.doRingShockWave(this, pos, 2, 0F, false, 10);
        EntityCameraShake.cameraShake(level(), pos, 15, 0.125F, 2, 3);
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
                        movement.x, movement.y, movement.z, false, 0, (float) (Math.PI / 2F), 0, 0, 1, 0.8F, 0.8F, 0.8F,
                        1, 1, 9 + this.random.nextInt(4), true, true, false,
                        new ParticleComponent[]{
                                new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.4F, 0.0F), false),
                                new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(10, 40 + this.random.nextInt(20)), false),
                                new PropertyControl(EnumParticleProperty.YAW, AnimData.startAndEnd(yaw, yaw + (float) Math.toRadians(this.random.nextInt(3) + 1)), true),
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
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.MUZZLE_FLASH.get(), muzzle.x + forward.x * 0.9, muzzle.y, muzzle.z + forward.z * 0.9
                    , 0, 0, 0, true, 0, 0, 0, 0, 1F,
                    1, 1, 1, 1, 1, 3, true, false, false
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
            EEEABMobs.NETWORK.sendToServer(new SyncMuzzlePosMessage(this.getId(), this.muzzle.x, this.muzzle.y, this.muzzle.z));
        }
    }

    //TODO 多人模式下有机率出现发射坐标偏移的情况
    public boolean serverSideVerified() {
        if (this.level().isClientSide) return false;
        return this.tickCount - this.serverLastFireTick >= 5;
    }

    public void performRangedAttack(Vec3 muzzlePos) {
        this.serverLastFireTick = this.tickCount;
        EntityAnnihilatorMissile.ElementType element;
        LivingEntity target = this.getTarget();
        EntityAnnihilatorMissile missile;
        if (target != null) {
            element = target.hasEffect(EffectInit.ELECTRIFIED_EFFECT.get()) ? EntityAnnihilatorMissile.ElementType.BLAZE : EntityAnnihilatorMissile.ElementType.VOLT;
            if (this.getHealthPercentage() < 0.5F && this.random.nextFloat() < 0.2F) element = EntityAnnihilatorMissile.ElementType.SPARKFERNO;
            Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.4, 0);
            missile = new EntityAnnihilatorMissile(this.level(), this, element);
            Vec3 projectileMid = muzzlePos.add(0, missile.getBbHeight() / 2.0, 0);
            Vec3 shootVec = targetPos.subtract(projectileMid).normalize();
            missile.shoot(shootVec.x, shootVec.y, shootVec.z, 1.6F, this.isBlinded() ? 30F : 0F);
        } else {
            missile = new EntityAnnihilatorMissile(this.level(), this, EntityAnnihilatorMissile.ElementType.VOLT);
            missile.shoot(this.getLookAngle().x, this.getLookAngle().y, this.getLookAngle().z, 1.6F, 0F);
        }
        missile.setPos(muzzlePos.x, muzzlePos.y, muzzlePos.z);
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
                    AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.BIG_RING.get(), forward.x, forward.y, forward.z
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
            doFractalEffect(this, this.saw, this.saw.offsetRandom(this.random, 2.5F), 3, 1);
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

    static class GAMeleeAttackGoal extends AnimationAI<EntityRelicAnnihilator> {
        private Vec3 pounceVec = Vec3.ZERO;
        private LivingEntity targetCache;
        private boolean stopFlag;
        private float distanceFactor;

        public GAMeleeAttackGoal(EntityRelicAnnihilator entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == entity.slashAnimation || animation == entity.swingAnimation || animation == entity.stabAnimation;
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
            if (animation == entity.slashAnimation) {
                attack(target, tick, 90F, 200F, false, true);
                if (tick == 40 && target != null && entity.validateGroundPound()) entity.playAnimation(entity.groundPoundAnimation);
            } else if (animation == entity.swingAnimation) {
                attack(target, tick, 200F, 90F, true, false);
                if (tick == 35 && target != null && entity.validateGroundPound()) entity.playAnimation(entity.groundPoundAnimation);
            } else {
                if ((tick < 21 || tick > 45)) {
                    targetCache = target;
                    if (target == null) {
                        double radians = Math.toRadians(entity.getYRot() + 90F);
                        pounceVec = new Vec3(Math.cos(radians), 0.0, Math.sin(radians)).normalize();
                        distanceFactor = 0.5F;
                    } else if (!entity.isBlinded() || tick % 5 == 0) {
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
                    syncRotationWithMotion(entity, motion);
                    entity.setDeltaMovement(motion);
                }
                if (tick >= 25 && tick <= 45) {
                    if (targetCache != null) entity.getLookControl().setLookAt(targetCache, 4F, 30F);
                    if (tick % 4 != 0) return;
                    float width = entity.getBbWidth() * 2.75F;
                    Vec3 pos = entity.getPosOffset(true, entity.getBbWidth() * 1.7F, entity.getBbWidth() * 0.3F, 1.5F);
                    entity.rangeAttack(width, entity.getBbHeight(), width, width, 50F, 50F, hitEntity -> {
                        int time = hitEntity.invulnerableTime;
                        hitEntity.invulnerableTime = 0;
                        if (entity.doHurtTarget(hitEntity, 0.4F, 0F, false, true) && canBeControlled(entity, hitEntity)) {
                            pullEntityToPosition(hitEntity, pos, 2F);
                        } else hitEntity.invulnerableTime = time;
                    });
                } else if (tick == 50 && target != null && entity.validateGroundPound()) entity.playAnimation(entity.groundPoundAnimation);
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
                    entity.rangeAttack(width, entity.getBbHeight(), width, width, leftArc, rightArc, e -> entity.doHurtTarget(e, 1F, 1F, canDisableShield, charged));
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

    static class GARangeAttackGoal extends AnimationGroupAI<EntityRelicAnnihilator> {
        private int loopCount;

        public GARangeAttackGoal(EntityRelicAnnihilator entity) {
            super(entity, () -> entity.laserAnimation, () -> entity.shot1Animation, () -> entity.shot2Animation, () -> entity.shot3Animation,
                    () -> entity.trickshot1Animation, () -> entity.trickshot2Animation, () -> entity.trickshot3Animation);
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
            if (animation == entity.shot1Animation || animation == entity.trickshot1Animation) {
                loopCount = Mth.clamp(4 - Math.round(entity.getHealth() / entity.getMaxHealth() * 3), 1, 3);
                if (entity.level().getDifficulty() == Difficulty.HARD) loopCount += 1;
            } else if ((animation == entity.shot2Animation || animation == entity.trickshot2Animation) && loopCount > 0) {
                loopCount--;
            }
        }

        private void handleAnimation(Animation animation, LivingEntity target, int tick) {
            if (animation == entity.laserAnimation) {
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
                    laser.updateWithEntity(type.wOffset, type.hOffset);
                    entity.level().addFreshEntity(laser);
                }
            } else if (animation == entity.shot1Animation || animation == entity.shot2Animation || animation == entity.shot3Animation) {
                entity.anchorToGround();
                lootAtTarget(target, 3F, true);
                if (animation == entity.shot1Animation) nextAnimation(animation, entity.shot2Animation);
                else if (animation == entity.shot2Animation) nextAnimation(animation, entity.shot3Animation, tick >= animation.getDuration() - 1 && loopCount <= 0);
            } else if (animation == entity.trickshot1Animation || animation == entity.trickshot2Animation || animation == entity.trickshot3Animation) {
                lootAtTarget(target, 2F, animation != entity.trickshot3Animation);
                float retreatRange = animation == entity.trickshot3Animation ? 0 : 3 + entity.getBbWidth();
                entity.backOff(target, getRetreatSpeed(0.7F, retreatRange, entity.targetDistance));
                if (animation == entity.trickshot1Animation) nextAnimation(animation, entity.trickshot2Animation);
                else if (animation == entity.trickshot2Animation) {
                    nextAnimation(animation, entity.trickshot3Animation, tick >= animation.getDuration() - 1 && loopCount <= 0);
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

    static class GACycloneAttackGoal extends AnimationAI<EntityRelicAnnihilator> {
        public GACycloneAttackGoal(EntityRelicAnnihilator entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == entity.cycloneAnimation;
        }

        @Override
        public void tick() {
            int tick = entity.getAnimationTick();
            if (tick < (entity.isBlinded() ? 10 : 15)) {
                lookAtTarget();
            } else if (tick >= 15 && tick <= 40) {
                double radians = Math.toRadians(this.entity.getYRot() + 90F);
                Vec3 pounceVec = new Vec3(Math.cos(radians), 0.0, Math.sin(radians)).normalize();
                if (tick == 20 || tick == 24 || tick == 29 || tick == 32 || tick == 36) {
                    float width = entity.getBbWidth() * 2.5F;
                    entity.rangeAttack(width, entity.getBbHeight(), width, width, hitEntity -> {
                        int time = hitEntity.invulnerableTime;
                        hitEntity.invulnerableTime = 0;
                        if (entity.doHurtTarget(hitEntity, 0.75F, 1F, false, true) && canBeControlled(entity, hitEntity)) {
                            ModEntityUtils.forceKnockBack(entity, hitEntity, 0.7F, -pounceVec.x, -pounceVec.z, false);
                            if (!hitEntity.onGround()) hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().multiply(1F, 0.8F, 1F));
                        } else hitEntity.invulnerableTime = time;
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
                entity.lookAt(target, 90F, 90F);
                entity.getLookControl().setLookAt(target, 90F, 90F);
            }
        }
    }

    static class GALeapAttackGoal extends AnimationAI<EntityRelicAnnihilator> {
        public GALeapAttackGoal(EntityRelicAnnihilator entity) {
            super(entity);
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == entity.groundsSlam1Animation || animation == entity.groundsSlam2Animation;
        }

        @Override
        public void tick() {
            LivingEntity target = entity.getTarget();
            int tick = entity.getAnimationTick();
            Animation animation = entity.getAnimation();
            if (animation == entity.groundsSlam1Animation) {
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
                } else if (tick == 14) entity.playAnimation(entity.groundsSlam2Animation);
            } else if (animation == entity.groundsSlam2Animation) {
                if (tick > 5 && entity.onGround()) entity.playAnimation(entity.groundsSlam3Animation);
            }
        }
    }

    static class KeepDistanceGoal extends AnimationMeleeAI<EntityRelicAnnihilator> {
        private final RandomSource random;
        private boolean isInRetreatPause;
        private int retreatCooldown;
        private int retreatDuration;
        private int delayCounter;

        @SafeVarargs
        public KeepDistanceGoal(EntityRelicAnnihilator attacker, Supplier<Animation>... animations) {
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
