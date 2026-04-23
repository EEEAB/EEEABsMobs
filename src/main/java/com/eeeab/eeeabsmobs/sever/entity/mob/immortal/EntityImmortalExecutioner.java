package com.eeeab.eeeabsmobs.sever.entity.mob.immortal;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.animation.AnimationAreaMelee;
import com.eeeab.animate.server.ai.animation.AnimationBlock;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.ModBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ModLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EntityImmortalExecutioner extends EntityAbsImmortal {
    public static final Animation DIE_ANIMATION = Animation.create(35);
    public static final Animation AVOID_ANIMATION = Animation.create(15);
    public static final Animation BLOCK_ANIMATION = Animation.create(15);
    public static final Animation COUNTER_ANIMATION = Animation.create(15);
    public static final Animation ATTACK_LEFT_ANIMATION = Animation.create(17);
    public static final Animation ATTACK_RIGHT_ANIMATION = Animation.create(17);
    public static final Animation SIDESWAY_LEFT_ANIMATION = Animation.create(10);
    public static final Animation SIDESWAY_RIGHT_ANIMATION = Animation.create(10);
    public static final Animation IMPACT_STORAGE_ANIMATION = Animation.create(60);
    public static final Animation IMPACT_HOLD_ANIMATION = Animation.create(20);
    public static final Animation IMPACT_STOP_ANIMATION = Animation.create(10);
    public static final Animation CULL_STORAGE_ANIMATION = Animation.create(20);
    public static final Animation CULL_HOLD_ANIMATION = Animation.create(15);
    public static final Animation CULL_STOP_ANIMATION = Animation.create(10);
    public static final Animation DETONATION_ANIMATION = Animation.create(40);
    private static final Animation[] ANIMATIONS = new Animation[]{
            DIE_ANIMATION,
            AVOID_ANIMATION,
            BLOCK_ANIMATION,
            COUNTER_ANIMATION,
            ATTACK_LEFT_ANIMATION,
            ATTACK_RIGHT_ANIMATION,
            SIDESWAY_LEFT_ANIMATION,
            SIDESWAY_RIGHT_ANIMATION,
            IMPACT_STORAGE_ANIMATION,
            IMPACT_HOLD_ANIMATION,
            IMPACT_STOP_ANIMATION,
            CULL_STORAGE_ANIMATION,
            CULL_HOLD_ANIMATION,
            CULL_STOP_ANIMATION,
            DETONATION_ANIMATION
    };
    private static final int MAX_HURT_COUNT = 6;
    private static final UniformInt AVOID_INTERVAL = TimeUtil.rangeOfSeconds(5, 12);
    private static final UniformInt STAMP_INTERVAL = TimeUtil.rangeOfSeconds(12, 16);
    private static final UniformInt CULL_INTERVAL = TimeUtil.rangeOfSeconds(6, 9);
    private static final UniformInt DETONATION_INTERVAL = TimeUtil.rangeOfSeconds(15, 20);
    private static final EntityDataAccessor<Float> DATA_FLAME_STRENGTH = SynchedEntityData.defineId(EntityImmortalExecutioner.class, EntityDataSerializers.FLOAT);
    @OnlyIn(Dist.CLIENT)
    public Vec3[] fire;
    private int timeUntilAvoid;
    private int timeUntilStamp;
    private int timeUntilCull;
    private int timeUntilDetonation;
    private int hurtCount;
    private int detonationCount;

    public EntityImmortalExecutioner(EntityType<EntityImmortalExecutioner> type, Level level) {
        super(type, level);
        this.active = true;
        this.dropAfterDeathAnim = false;
        if (this.level().isClientSide) {
            this.fire = new Vec3[]{new Vec3(0, 0, 0)};
        }
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
    }

    @Override
    public IMob.MobLevel getMobLevel() {
        return MobLevel.NORMAL;
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.immortals.immortalExecutioner.combatConfig;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 1.1F;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean isOnFire() {
        return this.getRemainingFireTicks() > 0 || this.level().isClientSide && this.getSharedFlag(0);
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
    protected void onAnimationFinish(Animation animation) {
        super.onAnimationFinish(animation);
        if (!this.level().isClientSide) {
            if (CULL_STOP_ANIMATION == animation) {
                this.timeUntilCull = this.getCoolingDuration(CULL_INTERVAL);
            } else if (DETONATION_ANIMATION == animation) {
                this.detonationCount++;
                this.setFlameStrength(this.getFlameStrength() + 0.2F);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.75F, 100));
        this.goalSelector.addGoal(7, new ModLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new ModLookAtGoal(this, Mob.class, 6.0F));
    }

    @Override
    protected void registerCustomGoals() {
        super.registerCustomGoals();
        Consumer<LivingEntity> consumer = this::executionerHurtTarget;
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new ExecutionerCullGoal(this));
        this.goalSelector.addGoal(1, new ExecutionerSharpImpactGoal(this));
        this.goalSelector.addGoal(1, new AnimationBlock<>(this, BLOCK_ANIMATION) {
            @Override
            public void tick() {
                super.tick();
                if (this.entity.getAnimationTick() == 10) {
                    if (this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.entity.targetDistance < 5F
                            && (this.entity.blockEntity != null || this.entity.getRandom().nextBoolean())) {
                        this.entity.playAnimation(COUNTER_ANIMATION);
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new AnimationAreaMelee<>(this, ATTACK_LEFT_ANIMATION, 8, 3F,
                1F, 1F, 80F, 40F, 3.5F, true).setCustomHitMethod(consumer));
        this.goalSelector.addGoal(1, new AnimationAreaMelee<>(this, ATTACK_RIGHT_ANIMATION, 8, 3F,
                1F, 1F, 40F, 80F, 3.5F, true).setCustomHitMethod(consumer));
        this.goalSelector.addGoal(1, new ExecutionerGroupAI(this, true,
                AVOID_ANIMATION,
                SIDESWAY_RIGHT_ANIMATION,
                SIDESWAY_LEFT_ANIMATION,
                DETONATION_ANIMATION
        ));
        this.goalSelector.addGoal(3, new AnimationMeleePlusAI<>(this, 1.0, 10, 1,
                ATTACK_RIGHT_ANIMATION,
                ATTACK_LEFT_ANIMATION
        ));
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (!this.level().isClientSide) {
            boolean isClose = target != null && (this.targetDistance < 6F || ModEntityUtils.checkTargetComingCloser(this, target) && this.targetDistance < 5F);
            if (!this.isNoAi() && !this.isStunned()) {
                if (this.isNoAnimation() && isClose && this.timeUntilAvoid <= 0 && (this.hurtCount == 0 && this.random.nextInt(100) == 0 || this.hurtCount != 0 && this.hurtCount < MAX_HURT_COUNT)) {
                    this.timeUntilAvoid = this.getCoolingDuration(AVOID_INTERVAL);
                    this.checkAndPlayComboAnimations(true, AVOID_ANIMATION, SIDESWAY_LEFT_ANIMATION, SIDESWAY_RIGHT_ANIMATION);
                }
                if (target != null) {
                    if (this.isNoAnimation() && this.timeUntilDetonation <= 0 && this.detonationCount < ModConfigHandler.COMMON.mobs.immortals.immortalExecutioner.maximumDetonationCount.get()) {
                        this.timeUntilDetonation = DETONATION_INTERVAL.sample(this.random);
                        this.playAnimation(DETONATION_ANIMATION);
                    }
                    if (this.isNoAnimation() && this.targetDistance < 10 && this.timeUntilCull <= 0) {
                        this.playAnimation(CULL_STORAGE_ANIMATION);
                    }
                }
                if (this.isNoAnimation() && !isClose && target != null && this.targetDistance < 18 && this.timeUntilStamp <= 0) {
                    this.timeUntilStamp = this.getCoolingDuration(STAMP_INTERVAL);
                    this.playAnimation(IMPACT_STORAGE_ANIMATION);
                }
            }
        }
        int tick = this.getAnimationTick();
        Animation animation = this.getAnimation();
        if (animation == AVOID_ANIMATION) {
            boolean flag = target != null && target.isAlive();
            double angle = flag ? this.getAngleBetweenEntities(this, target) : this.yBodyRot;
            float avoidYaw = flag ? (float) Math.toRadians(angle + 90) : (float) Math.toRadians(angle + 270);
            if (tick == 5) {
                if ((this.onGround() || this.isInLava() || this.isInWater())) {
                    float speed = 3F + 0.5F * this.random.nextFloat();
                    this.fastMove(speed, avoidYaw);
                }
            } else if (tick == 14) {
                this.checkAndPlayComboAnimations((this.onGround() || this.isInLava() || this.isInWater())
                                && (this.random.nextFloat() > this.getHealth() / this.getMaxHealth()
                                || this.random.nextFloat() < Math.max(this.getFlameStrength() - 1F, 0F)),
                        SIDESWAY_LEFT_ANIMATION,
                        SIDESWAY_RIGHT_ANIMATION,
                        CULL_STORAGE_ANIMATION
                );
            }
        } else if (animation == SIDESWAY_LEFT_ANIMATION || animation == SIDESWAY_RIGHT_ANIMATION) {
            boolean isLeft = animation == SIDESWAY_LEFT_ANIMATION;
            if (tick == 1) {
                double angle = this.yBodyRot;
                float speed = 2F + 0.5F * this.random.nextFloat();
                if (target != null && this.targetDistance < 6F) speed += 1.5F;
                int randomAngle = this.random.nextInt(11) - 10;
                float sideAvoidYaw = isLeft ? (float) Math.toRadians(angle + randomAngle) : (float) Math.toRadians(angle + 180 + randomAngle);
                this.fastMove(speed, sideAvoidYaw);
            } else if (tick > 8) {
                this.checkAndPlayComboAnimations(this.random.nextFloat() < 0.6F && target != null && this.targetDistance < 10, CULL_STORAGE_ANIMATION);
            }
        } else if (animation == CULL_HOLD_ANIMATION) {
            if (tick == 3) this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundInit.IMMORTAL_EXECUTIONER_DASH.get(), this.getSoundSource(), 1F, 1F, false);
            if (tick == 4) {
                this.doRibbonEffect();
                this.playSound(SoundInit.IMMORTAL_EXECUTIONER_SCRATCH.get(), 0.8F, this.getVoicePitch());
            }
        } else if (animation == ATTACK_RIGHT_ANIMATION || animation == ATTACK_LEFT_ANIMATION) {
            if (tick == 7) this.playSound(SoundInit.IMMORTAL_EXECUTIONER_SCRATCH.get(), 1.5F, this.getVoicePitch() + 0.5F);
        } else if (animation == COUNTER_ANIMATION) {
            if (tick == 5) this.playSound(SoundInit.IMMORTAL_EXECUTIONER_SCRATCH.get(), 1.2F, this.getVoicePitch() + 0.2F);
        } else if (animation == DETONATION_ANIMATION) {
            this.setDeltaMovement(0, this.onGround() ? 0 : this.getDeltaMovement().y, 0);
            if (tick >= 18 && tick <= 24 && tick % 2 == 0) {
                if (tick == 18) this.doSoulFireGlowEffect();
                int i = (tick - 17) / 2 + 1;
                this.playSound(SoundInit.IMMORTAL_EXECUTIONER_DETONATION.get(), 1F + 0.5F * (i / 3F), 1.75F - (i / 3F));
                ModParticleUtils.sphericalParticleOutburst(level(), 4F, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.SMOKE}, this, this.getBbHeight() * 0.5F, 0, 0, 1 - 0.5 * (i / 3F));
                AABB attackRange = ModEntityUtils.makeAABBWithSize(this.getX(), this.getY(), this.getZ(), 0F, 6F, 12F, 6F);
                for (LivingEntity hitEntity : this.level().getEntitiesOfClass(LivingEntity.class, attackRange, e -> !this.isAlliedTo(e))) {
                    if (this.executionerHurtTarget(hitEntity)) {
                        hitEntity.setSecondsOnFire((int) (2.5 * this.getFlameStrength()));
                    }
                }
            }
        } else if (animation == BLOCK_ANIMATION) this.setDeltaMovement(0, 0, 0);
        if (this.level().isClientSide && this.fire != null && this.fire.length != 0) {
            float power = this.getFirePower();
            boolean isPower = power > 1F;
            if (this.isAlive() && this.active) {
                if (animation != IMPACT_HOLD_ANIMATION) {
                    int duration = Mth.clamp((int) (9 / Math.max(power, 0.1)), 6, 9);
                    if (this.tickCount % duration == 1) {
                        this.doSoulFireEffect(power, duration);
                    }
                } else if (tick == 2) {
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundInit.IMMORTAL_EXECUTIONER_DASH.get(), this.getSoundSource(), 1.5F, 0.8F, false);
                } else if (tick == 3) this.doRibbonEffect();
            }
            if (this.deathTime < 20) {
                if (this.tickCount % 9 == 1) {
                    SimpleParticleType particleType = ParticleTypes.SMOKE;
                    if (this.isUnderWater()) {
                        particleType = ParticleTypes.BUBBLE;
                    }
                    Vec3 vec3 = this.fire[0];
                    for (int i = 0; i < (isPower ? 2 : 5); i++) {
                        double x = vec3.x + (2.0 * this.random.nextDouble() - 1.0) * 0.25;
                        double y = vec3.y + this.getBbHeight() * 0.1F;
                        double z = vec3.z + (2.0 * this.random.nextDouble() - 1.0) * 0.25;
                        this.level().addParticle(particleType, x, y, z, 0, 0.007, 0);
                        if (isPower && this.random.nextFloat() < 0.3F)
                            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0, 0.012, 0);
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.timeUntilAvoid > 0) {
                this.timeUntilAvoid--;
            }

            if (this.timeUntilStamp > 0) {
                this.timeUntilStamp--;
            }

            if (this.timeUntilCull > 0) {
                this.timeUntilCull--;
            }

            if (this.timeUntilDetonation > 0) {
                this.timeUntilDetonation--;
            }

            if (this.tickCount % 15 == 0 && this.hurtCount > 0) {
                this.hurtCount--;
            }
        } else {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.level().playLocalSound(this.getX() + 0.5D, this.getY() + 0.75D, this.getZ() + 0.5D, SoundInit.IMMORTAL_EXECUTIONER_BURN.get(), this.getSoundSource(), Mth.clamp(this.getFirePower(), 0.25F, 1.5F) + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.5F, false);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else if (source.getEntity() != null) {
            float preDamage = damage;
            this.hurtCount++;
            float attackArc = 220F;
            byte pierceLevel = 0;
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this, source.getEntity().position());
            boolean hitFlag = !this.isNoAi() && !source.is(DamageTypeTags.BYPASSES_ARMOR) && (entityRelativeAngle <= attackArc / 2F && entityRelativeAngle >= -attackArc / 2F)
                    || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -attackArc + 90f / 2F);
            if (source.getDirectEntity() instanceof AbstractArrow arrow) pierceLevel = arrow.getPierceLevel();
            boolean projectileFlag = ModEntityUtils.isProjectileSource(source);
            boolean noPierce = pierceLevel == 0;
            if (hitFlag && this.inBlocking()) {
                if (noPierce && this.random.nextBoolean()) this.playSound(SoundInit.IMMORTAL_EXECUTIONER_BLOCK.get());
                if (projectileFlag && noPierce) return false;
                else damage *= 0.5F + Math.min(pierceLevel * 0.125F, 0.5F);
            }
            if (hitFlag && (projectileFlag || this.random.nextFloat() < 0.45F) && this.isNoAnimation() && !this.isStunned()) {
                if (source.getEntity() instanceof LivingEntity block) this.blockEntity = block;
                if (noPierce) this.playSound(SoundInit.IMMORTAL_EXECUTIONER_BLOCK.get());
                this.playAnimation(BLOCK_ANIMATION);
                if (projectileFlag && noPierce) return false;
                else damage *= 0.5F + Math.min(pierceLevel * 0.125F, 0.5F);
            }
            if (this.getTarget() != null && this.isNoAnimation() && this.hurtCount >= MAX_HURT_COUNT) {
                this.hurtCount = 0;
                this.timeUntilAvoid = AVOID_INTERVAL.sample(this.random);
                this.checkAndPlayComboAnimations(true, AVOID_ANIMATION, SIDESWAY_LEFT_ANIMATION, SIDESWAY_RIGHT_ANIMATION);
            }
            if (this.getAnimation() == DETONATION_ANIMATION || this.getAnimation() == IMPACT_STORAGE_ANIMATION)
                damage *= 0.5F;
            if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) damage = preDamage;
        }
        return super.hurt(source, damage);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAME_STRENGTH, 1F);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("flameStrength", this.getFlameStrength());
        compound.putInt("detonationCount", this.detonationCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlameStrength(compound.getFloat("flameStrength"));
        this.detonationCount = compound.getInt("detonationCount");
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.5F * this.getBbWidth() * 2.5F + entity.getBbWidth();
    }

    @Override
    protected void makePoofParticles() {
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
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.IMMORTAL_EXECUTIONER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.IMMORTAL_EXECUTIONER_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.34D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.15D)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 2D);
    }

    /**
     * 获取火焰燃烧强度 不同环境下火焰燃烧的大小有所不同
     *
     * @return 火力值
     */
    public float getFirePower() {
        float multiplier = this.getFlameStrength();
        if (this.isInWaterRainOrBubble() || this.isFullyFrozen()) {
            multiplier *= 0.8F;
        }
        if (this.isInLava() || this.isOnFire()) {
            multiplier += 0.2F;
        }
        return multiplier;
    }

    public float getFlameStrength() {
        return this.entityData.get(DATA_FLAME_STRENGTH);
    }

    public void setFlameStrength(float strength) {
        this.entityData.set(DATA_FLAME_STRENGTH, strength);
    }

    public boolean executionerHurtTarget(LivingEntity hitEntity) {
        double baseDamage = this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = hitEntity.hurt(this.damageSources().mobAttack(this), (float) (baseDamage * Math.max(this.getFirePower(), 0)) + getDamageAmountByTargetHealthPct(hitEntity));
        if (this.random.nextFloat() < Math.max(this.getFlameStrength() - 1F, 0F)) {
            if (hitEntity instanceof Player player && player.isBlocking()) {
                player.disableShield(true);
            }
        }
        if (flag) {
            hitEntity.addEffect(new MobEffectInstance(EffectInit.ERODE_EFFECT.get(), 200), this);
        }
        return flag;
    }

    public boolean inBlocking() {
        return !this.level().isClientSide && this.getAnimation() == BLOCK_ANIMATION && this.getAnimationTick() > 1;
    }

    private int getCoolingDuration(UniformInt uniformInt) {
        int baseCD = uniformInt.sample(this.random);
        return (int) (baseCD * (1F / Math.max(this.getFlameStrength(), 1)));
    }

    private void checkAndPlayComboAnimations(boolean conditions, @NotNull Animation... combos) {
        if (conditions && !this.isStunned()) {
            this.playAnimation(combos[this.random.nextInt(combos.length)]);
        }
    }

    private void fastMove(float speed, float sideAvoidYaw) {
        Vec3 move = this.getDeltaMovement().add(speed * Math.cos(sideAvoidYaw), 0, speed * Math.sin(sideAvoidYaw));
        this.setDeltaMovement(move.x, this.getDeltaMovement().y, move.z);
        this.doRibbonEffect();
    }

    private void doSoulFireEffect(float power, int duration) {
        float scale = 1F + Mth.clamp(4.5F * power, 0F, 7.5F);
        AdvancedParticleBase.spawnParticle(level(), ParticleInit.STRIP_SOUL_FIRE.get(), this.fire[0].x, this.fire[0].y, this.fire[0].z,
                0, 0, 0, true, 0, 0, 0, 0, scale,
                0.1, 0.98, 0.96, 0.5 + this.random.nextFloat() * 0.25F, 0, duration,
                true, false, true,
                new ParticleComponent[]{
                        new ParticleComponent.PinLocation(this.fire),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Y,
                                AnimData.constant(0.2F), true),
                });
    }

    private void doSoulFireGlowEffect() {
        if (this.level().isClientSide) {
            AdvancedParticleBase.spawnParticle(level(), ParticleInit.GLOW.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 24, 0.1, 0.98, 0.96, 1, 1, 24, true, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new AnimData.KeyTrack(
                            new float[]{0.0f, 0.7f, 0.0f},
                            new float[]{0f, 0.5f, 1f}
                    ), false),
                    new ParticleComponent.PinLocation(this.fire),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Y,
                            AnimData.constant(-0.4F), true),
            });
        }
    }

    private EntityImmortalExecutioner.ImpartHitResult raytraceEntities(Vec3 from, Vec3 to) {
        EntityImmortalExecutioner.ImpartHitResult result = new EntityImmortalExecutioner.ImpartHitResult();
        result.setBlockHit(this.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.getBlockHit() != null) {
            to = result.getBlockHit().getLocation();
        }
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(from, to).inflate(1.5), e -> !this.isAlliedTo(e));
        for (LivingEntity entity : entities) {
            if (this == entity) {
                continue;
            }
            AABB aabb = entity.getBoundingBox().inflate(1.5);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    private void doRibbonEffect() {
        if (this.level().isClientSide && this.fire != null && this.fire.length != 0) {
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_ORB.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F,
                    1, 1, 1, 1, 1, 10, true, false, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(this.fire),
                            new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 8, 0, 0, 0, 0.12F, 0.06, 0.94, 1, 0.75, true, true,
                                    new ParticleComponent[]{
                                            new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0.5F, 1F)),
                                            new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, AnimData.KeyTrack.startAndEnd(1F, 0F))
                                    }, false)
                    });
        }
    }

    static class ExecutionerGroupAI extends AnimationGroupAI<EntityImmortalExecutioner> {
        private final boolean lookAtTarget;

        public ExecutionerGroupAI(EntityImmortalExecutioner entity, boolean lookAtTarget, Animation... animations) {
            super(entity, animations);
            this.lookAtTarget = lookAtTarget;
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = this.entity.getTarget();
            if (this.lookAtTarget && target != null) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            }
        }
    }

    static class ExecutionerSharpImpactGoal extends ExecutionerGroupAI {
        private LivingEntity target;
        private Vec3 preVec3 = Vec3.ZERO;

        public ExecutionerSharpImpactGoal(EntityImmortalExecutioner entity) {
            super(entity, false, IMPACT_STORAGE_ANIMATION, IMPACT_HOLD_ANIMATION, IMPACT_STOP_ANIMATION);
        }

        @Override
        public void start() {
            super.start();
            target = this.entity.getTarget();
        }

        @Override
        public void stop() {
            super.stop();
            this.target = null;
            this.preVec3 = Vec3.ZERO;
        }

        @Override
        public void tick() {
            int tick = this.entity.getAnimationTick();
            Animation animation = this.entity.getAnimation();
            if (animation == IMPACT_STORAGE_ANIMATION) {
                this.entity.setDeltaMovement(0, 0.025F, 0);
                if (this.target != null) {
                    this.entity.getLookControl().setLookAt(this.target, 360F, 30F);
                    this.entity.lookAt(this.target, 360F, 30F);
                }
                this.preVec3 = this.entity.position();
                this.nextAnimation(animation, IMPACT_HOLD_ANIMATION, 25 + this.entity.getRandom().nextInt(animation.getDuration() - 25));
            } else if (animation == IMPACT_HOLD_ANIMATION) {
                this.entity.setDeltaMovement(0, 0, 0);
                double baseMoveMultiplier = 15F;
                double deltaY = Math.ceil(this.entity.getY() - 0.5F);
                if (tick == 4) {
                    this.preVec3 = this.entity.position();
                    Vec3 targetVec3 = new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * baseMoveMultiplier, deltaY, Math.sin(Math.toRadians(entity.getYRot() + 90)) * baseMoveMultiplier);
                    if (this.target != null) {
                        Vec3 point = ModEntityUtils.findPounceTargetPoint(this.entity, this.target, 5F);
                        baseMoveMultiplier = Math.min(point.distanceTo(preVec3), 15F);
                        if (baseMoveMultiplier >= 1) {
                            deltaY = point.y - this.entity.getY();
                            Vec3 normalizedVector = point.subtract(preVec3).normalize().scale(baseMoveMultiplier);
                            targetVec3 = new Vec3(normalizedVector.x, deltaY, normalizedVector.z);
                        }
                    }
                    this.entity.move(MoverType.SELF, targetVec3);
                } else if (tick == 5 && preVec3.length() > 0) {
                    ImpartHitResult hitResult = this.entity.raytraceEntities(preVec3, this.entity.position());
                    hitResult.getEntities().forEach(this.entity::executionerHurtTarget);
                    //计算两点之间路径含有方块碰撞 则添加撞击后的眩晕效果
                    if (hitResult.getBlockHit() != null && hitResult.getBlockHit().getType() == HitResult.Type.BLOCK) {
                        this.entity.setPos(hitResult.getBlockHit().getLocation().add(0, 0.5, 0));
                        this.entity.addEffect(new MobEffectInstance(EffectInit.STUN_EFFECT.get(), 50, 0, false, false));
                    }
                }
                this.nextAnimation(animation, IMPACT_STOP_ANIMATION);
            }
        }
    }

    static class ExecutionerCullGoal extends ExecutionerGroupAI {
        public ExecutionerCullGoal(EntityImmortalExecutioner entity) {
            super(entity, true, CULL_STORAGE_ANIMATION, CULL_HOLD_ANIMATION, CULL_STOP_ANIMATION, COUNTER_ANIMATION);
        }

        @Override
        public void tick() {
            Animation animation = this.entity.getAnimation();
            LivingEntity target = this.entity.getTarget();
            int tick = this.entity.getAnimationTick();
            if (animation == CULL_STORAGE_ANIMATION) {
                super.tick();
                this.nextAnimation(animation, CULL_HOLD_ANIMATION);
            } else if (animation == CULL_HOLD_ANIMATION) {
                if (target != null) {
                    if (tick < 4) {
                        super.tick();
                    } else if (tick == 4) {
                        this.pursuit(8F, 8F, target.getY() - this.entity.getY());
                    } else if (tick == 5) {
                        this.doHurtTarget(100F, 2.8F, true);
                    } else if (tick > 6) {
                        this.entity.setYRot(this.entity.yRotO);
                    }
                    this.nextAnimation(animation, CULL_STOP_ANIMATION);
                } else {
                    this.entity.playAnimation(CULL_STOP_ANIMATION);
                }
            } else if (animation == COUNTER_ANIMATION) {
                if (tick < 5) {
                    super.tick();
                } else if (tick == 5) {
                    this.pursuit(5F, 2.5F, target != null ? target.getY() - this.entity.getY() : this.entity.getY());
                } else if (tick == 6) {
                    this.doHurtTarget(140F, 3F, false);
                } else if (tick > 7) {
                    this.entity.setYRot(this.entity.yRotO);
                }
            }
        }

        private void doHurtTarget(float attackArc, float range, boolean knock) {
            entity.rangeAttack(range, range - 0.5, range, range, attackArc, attackArc, hitEntity -> {
                entity.executionerHurtTarget(hitEntity);
                if (knock) {
                    hitEntity.setDeltaMovement(hitEntity.getDeltaMovement().add(0, 0.45, 0));
                }
            });
        }

        private void pursuit(float pursuitDistance, float moveMultiplier, double y) {
            float targetDistance = entity.targetDistance;
            if (entity.getTarget() != null && entity.getTarget().isAlive() && targetDistance < pursuitDistance && targetDistance > 0F) {
                moveMultiplier = targetDistance - 0.25F;
            }
            targetDistance = entity.targetDistance;
            if (entity.getTarget() == null || targetDistance > 1F)
                entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * moveMultiplier, y, Math.sin(Math.toRadians(entity.getYRot() + 90)) * moveMultiplier));
        }
    }

    public static class ImpartHitResult {
        private BlockHitResult blockHit;
        private final List<LivingEntity> entities = new ArrayList<>();

        @Nullable
        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public List<LivingEntity> getEntities() {
            return entities;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
