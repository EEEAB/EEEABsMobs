package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.AnimationNotification;
import com.eeeab.animate.server.animation.keyframe.CondKeyframe;
import com.eeeab.animate.server.animation.keyframe.Keyframe;
import com.eeeab.animate.server.animation.keyframe.KeyframeManager;
import com.eeeab.animate.server.animation.release.AnimationReleaseManager;
import com.eeeab.animate.server.animation.release.ConditionFactory;
import com.eeeab.animate.server.animation.release.cooldown.FixedRangeCooldown;
import com.eeeab.animate.server.animation.release.cooldown.HealthScaledCooldown;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.ModBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityElectromagnetic;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityPulsedGrenade;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityRelicEarthshaker extends EntityAbsRelicron implements RangedAttackMob {
    public static final Animation DIE_ANIMATION = Animation.create(60);
    public static final Animation ACTIVE_ANIMATION = Animation.create(30);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(30);
    public static final Animation ATTACK_LEFT_ANIMATION = Animation.create(40);
    public static final Animation ATTACK_RIGHT_ANIMATION = Animation.create(40);
    public static final Animation SMASH_ATTACK_ANIMATION = Animation.create(35);
    public static final Animation RANGE_ATTACK_ANIMATION = Animation.create(100);
    public static final Animation RANGE_ATTACKSTOP_ANIMATION = Animation.create(10);
    public static final Animation ELECTROMAGNETIC_ANIMATION = AnimationNotification.create(100, null);
    private static final Animation[] ANIMATIONS = new Animation[]{
            SMASH_ATTACK_ANIMATION,
            RANGE_ATTACK_ANIMATION,
            ACTIVE_ANIMATION,
            DEACTIVATE_ANIMATION,
            DIE_ANIMATION,
            ELECTROMAGNETIC_ANIMATION,
            ATTACK_LEFT_ANIMATION,
            ATTACK_RIGHT_ANIMATION,
            RANGE_ATTACKSTOP_ANIMATION,
    };
    private static final KeyframeManager<EntityRelicEarthshaker> KEYFRAME_MANAGER;
    private static final AnimationReleaseManager<EntityRelicEarthshaker> ANIMATION_RELEASE_MANAGER;
    //0:left 1:right 注：该变量被KeyframeManager引用 必须初始化 避免NoSuchFieldError问题
    public Vec3[] hand = null;
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);
    public final ControlledAnimation hotControlled = new ControlledAnimation(20);
    public final ControlledAnimation electromagneticControlled = new ControlledAnimation(20);

    public EntityRelicEarthshaker(EntityType<? extends EntityAbsRelicron> type, Level level) {
        super(type, level);
        this.active = false;
        this.dropAfterDeathAnim = false;
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
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
        return MobLevel.ELITE;
    }

    @Override//减少实体在水下的空气供应
    protected int decreaseAirSupply(int air) {
        return air;
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
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.9F;
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
        return ModConfigHandler.COMMON.mobs.relicrons.relicEarthshaker.combatConfig;
    }

    @Override
    protected void onAnimationStart(Animation animation) {
        super.onAnimationStart(animation);
        if (!this.level().isClientSide) {
            if (animation == ELECTROMAGNETIC_ANIMATION) {
                this.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 170, 0, false, false));
            }
        }
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
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, ACTIVE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationDeactivate<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, RANGE_ATTACK_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, ELECTROMAGNETIC_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationGroupAI<>(this, ATTACK_LEFT_ANIMATION, ATTACK_RIGHT_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, SMASH_ATTACK_ANIMATION, 4F, 8F, 21, 1.5F, 1.5F, true) {
            @Override
            public void tick() {
                this.entity.setDeltaMovement(0F, this.entity.getDeltaMovement().y, 0F);
                int tick = this.entity.getAnimationTick();
                if (tick == 22) {
                    ModEntityUtils.breakBlocksInRect(entity.level(), entity, 3, 3, 4, 3, 0, 2, true);
                }
                LivingEntity target = this.entity.getTarget();
                if (tick < 17 && target != null) {
                    entity.lookAt(target, 30F, 30F);
                    this.entity.getLookControl().setLookAt(target, 30F, 30F);
                }
                super.tick();
            }

            @Override
            protected void onHit(LivingEntity entity) {
                if (!this.entity.hotControlled.isStop() && !this.entity.isAlliedTo(entity)) entity.setSecondsOnFire(3);
            }
        });
        this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.0, 30, ATTACK_LEFT_ANIMATION, ATTACK_RIGHT_ANIMATION));
    }

    @Override
    public void tick() {
        super.tick();
        this.glowControlled.updatePrevTimer();
        this.hotControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);

        this.pushEntitiesAway(1.2F, getBbHeight(), 1.2F, 1.2F);

        if (this.level().isClientSide && this.isActive() && this.isAlive()) {
            if (this.tickCount % 5 == 0 && this.random.nextInt(3) == 0) {
                Vec3 pos = this.getPosOffset(false, -1.25F, 0F, getBbHeight() * 0.75F).offsetRandom(this.random, 1F);
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
            }
            if (!this.hotControlled.isStop() && this.hand != null && this.hand.length > 0 && this.tickCount % 3 == 0) {
                this.doHandEffect(this.hand[0]);
                this.doHandEffect(this.hand[1]);
            }
            float moveX = (float) (this.getX() - this.xo);
            float moveZ = (float) (this.getZ() - this.zo);
            float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
            if (speed > 0.05) {
                if (this.random.nextInt(5) == 0) this.doWalkEffect(1);
                if (!this.isSilent() && this.frame % 10 == 1 && (this.isNoAnimation() || this.getAnimation() == RANGE_ATTACK_ANIMATION)) {
                    this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.RELIC_EARTHSHAKER_STEP.get(), this.getSoundSource(), 0.8F, 1F, false);
                }
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
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());
        if (this.getAnimation() != RANGE_ATTACK_ANIMATION) {
            if (tickCount % 10 == 0) this.hotControlled.decreaseTimer();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (source.is(ModResourceKey.OVERLOAD_EXPLODE)) {
                damage *= 2F;
            } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                damage *= 0.5F;
            }
        }
        return super.hurt(source, damage);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 30; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            Vec3 pos = this.getPosOffset(false, this.getBbWidth(), 0F, (float) (0.1F + (this.getBbHeight() * 0.5) * this.random.nextDouble()));
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, pos.x + this.getBbWidth() * (2.0D * this.random.nextDouble() - 1.0D) * 1.4, pos.y, pos.z + this.getBbWidth() * (2.0D * this.random.nextDouble() - 1.0D) * 1.4, d0, d1, d2);
        }
    }

    //@Override
    //protected void dropCustomDeathLoot(DamageSource source, int pLooting, boolean pRecentlyHit) {
    //    super.dropCustomDeathLoot(source, pLooting, pRecentlyHit);
    //    ItemEntity itementity = this.spawnAtLocation(ItemInit.ANCIENT_DRIVE_CRYSTAL.get());
    //    if (itementity != null) {
    //        itementity.setExtendedLifetime();
    //        itementity.setGlowingTag(true);
    //    }
    //}

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getActiveAnimation() {
        return ACTIVE_ANIMATION;
    }

    @Override
    public Animation getDeactivateAnimation() {
        return DEACTIVATE_ANIMATION;
    }

    @Override
    public KeyframeManager<EntityRelicEarthshaker> getKeyframeManager() {
        return KEYFRAME_MANAGER;
    }

    @Override
    protected SoundEvent getActiveSound() {
        return SoundInit.RELICRON_ACTIVE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.RELIC_EARTHSHAKER_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundInit.RELIC_EARTHSHAKER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.RELIC_EARTHSHAKER_IDLE.get();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        this.playSound(SoundInit.RELIC_EARTHSHAKER_LAUNCH_GRENADE.get());
        this.performRangedAttack(target, velocity, true);
        this.performRangedAttack(target, velocity, false);
    }

    private static KeyframeManager<EntityRelicEarthshaker> setupAnimations() {
        KeyframeManager<EntityRelicEarthshaker> manager = new KeyframeManager<>();
        KeyframeManager.KeyframeManegerBuilder<EntityRelicEarthshaker> builder = manager.builder();
        builder.forAnimation(SMASH_ATTACK_ANIMATION)
                .atTick(4, (entity, animation, tick) -> entity.playSound(SoundInit.RELIC_EARTHSHAKER_PRE_ATTACK.get(), 0.5F, entity.getVoicePitch()))
                .atTick(20, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        Vec3 pos = entity.getPosOffset(false, entity.getBbWidth(), 0F, 0.1F);
                        int[] particles = {10, 15, 10};
                        double[] radii = {1.5, 2, 2.5};
                        double[] speeds = {0.8, 0.7, 0.6};
                        double[] angles = {35, 25, 15};
                        double[] color = {0.8, 0.8, 0.8, 0.5};
                        ModParticleUtils.multiLayerBowlParticles(entity.level(), pos, 2, particles, radii, speeds, angles, color, null, 0.9F);
                        entity.level().addParticle(new ParticleRing.RingData(0F, (float) (Math.PI / 2F), 8, 0.8F, 0.8F, 0.8F, 0.9F, 80F, false, ParticleRing.EnumRingBehavior.GROW), pos.x, pos.y, pos.z, 0, 0, 0);
                        entity.doPoundGroundEffect(pos, 1.4F, 0.87F);
                        ModParticleUtils.blockParticlesAround(entity.level(), pos.x, entity.getY(), pos.z, 45, 1, 3, 2, 5, 3, 6, -0.2, 0.1);
                    }
                    entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + entity.random.nextFloat() * 0.1F);
                    EntityCameraShake.cameraShake(entity.level(), entity.position(), 15, 0.125F, 2, 3);
                });
        builder.forAnimation(RANGE_ATTACK_ANIMATION)
                .inRange(5, 89, new Keyframe<>() {
                    private int lostTargetDelay;

                    @Override
                    public void handle(EntityRelicEarthshaker entity, Animation animation, int tick) {
                        LivingEntity target = entity.getTarget();
                        if (!entity.level().isClientSide) {
                            if (this.lostTargetDelay > 0) this.lostTargetDelay--;
                            if (target != null && target.isAlive() && entity.canAttack(target) && this.lostTargetDelay < 60) {
                                entity.lookAt(target, 30F, 30F);
                                entity.getLookControl().setLookAt(target, 30F, 30F);
                                this.moveGoal(entity, target);
                                if (!entity.getSensing().hasLineOfSight(target)) this.lostTargetDelay += 2;
                            } else {
                                entity.playAnimation(RANGE_ATTACKSTOP_ANIMATION);
                                //entity.getCooldownManager().resetCD(RANGE_ATTACK_ANIMATION);
                                return;
                            }
                        }
                        if (tick % 19 == 1) {
                            if (!entity.level().isClientSide) {
                                if (target != null) entity.performRangedAttack(target, 1F);
                            } else if (entity.hand != null && entity.hand.length > 0) {
                                entity.doShotEffect(entity.hand[0]);
                                entity.doShotEffect(entity.hand[1]);
                            }
                            entity.hotControlled.increaseTimer(5);
                        }
                    }

                    private void moveGoal(EntityRelicEarthshaker entity, LivingEntity target) {
                        double moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.75;
                        double distance = entity.distanceToSqr(target);
                        if (distance < Math.pow(9, 2)) {
                            Vec3 vec3 = entity.findTargetPoint(target, entity);
                            entity.setDeltaMovement(vec3.x * moveSpeed, entity.getDeltaMovement().y, vec3.z * moveSpeed);
                        } else if (distance > Math.pow(12, 2)) {
                            Vec3 vec3 = entity.findTargetPoint(entity, target);
                            entity.setDeltaMovement(vec3.x * moveSpeed, entity.getDeltaMovement().y, vec3.z * moveSpeed);
                        } else entity.setDeltaMovement(0F, entity.onGround() && !entity.isNoGravity() ? -0.01F : entity.getDeltaMovement().y, 0F);
                    }
                });
        builder.forAnimation(ACTIVE_ANIMATION)
                .inRange(1, 3, (entity, animation, tick) -> {
                    if (entity.hand != null) {
                        entity.spawnBlockEffect(entity.hand[0]);
                        entity.spawnBlockEffect(entity.hand[1]);
                    }
                });
        builder.forAnimation(DEACTIVATE_ANIMATION)
                .inRange(21, 24, (entity, animation, tick) -> {
                    if (entity.hand != null) {
                        entity.spawnBlockEffect(entity.hand[0]);
                        entity.spawnBlockEffect(entity.hand[1]);
                    }
                });
        builder.forAnimation(DIE_ANIMATION)
                .inRange(0, 19, (entity, animation, tick) -> {
                    LivingEntity target = entity.getTarget();
                    if (target != null) entity.getLookControl().setLookAt(target, 30F, 30F);
                })
                .atTick(36, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        Vec3 pos = entity.getPosOffset(false, entity.getBbWidth(), 0F, 0F);
                        entity.doPoundGroundEffect(pos, 1.2F, 0.91F);
                        if (!entity.hotControlled.isStop()) ModParticleUtils.annularParticleOutburst(entity.level(), 10, ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, 0.3F, 0.2);
                    } else entity.playSound(SoundInit.RELIC_EARTHSHAKER_FALL.get());
                })
                .atTick(40, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) return;
                    EntityCameraShake.cameraShake(entity.level(), entity.position(), 15, 0.125F, 2, 8);
                    entity.rangeAttack(4.5, entity.getBbHeight() * 0.6, 4.5, 4.5, 120F, 120F, hitEntity -> {
                        if (entity.doHurtTarget(hitEntity, 2.5F, 0F, true) && entity.getHandHeat()) {
                            hitEntity.setSecondsOnFire(3);
                        }
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 0.5F, ratioX, ratioZ, true);
                    });
                });
        builder.conditional(new CondKeyframe<>() {
            @Override
            public boolean shouldHandle(EntityRelicEarthshaker entity, Animation animation, int tick) {
                return ATTACK_LEFT_ANIMATION == animation || ATTACK_RIGHT_ANIMATION == animation;
            }

            @Override
            public void handle(EntityRelicEarthshaker entity, Animation animation, int tick) {
                entity.setDeltaMovement(0F, entity.getDeltaMovement().y, 0F);
                LivingEntity target = entity.getTarget();
                boolean isServer = !entity.level().isClientSide;
                if (!(tick < 20 && tick > 7) && target != null) {
                    entity.lookAt(target, 30F, 30F);
                    entity.getLookControl().setLookAt(target, 30F, 30F);
                }
                if (tick == 11 && isServer) {
                    entity.rangeAttack(4, 4.25, 4, 4.25, 35F, 35F, hitEntity -> {
                        if (entity.doHurtTarget(hitEntity, 1F, 0F) && entity.getHandHeat()) {
                            hitEntity.setSecondsOnFire(3);
                        }
                        double ratioX = -Math.sin(entity.yBodyRot * ((float) Math.PI / 180F));
                        double ratioZ = Math.cos(entity.yBodyRot * ((float) Math.PI / 180F));
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 0.8F, ratioX, ratioZ, true);
                    });
                } else if (tick == 28 && isServer) {
                    float leftArc = 45F, rightArc = 45F;
                    if (entity.getAnimation() == ATTACK_RIGHT_ANIMATION) rightArc = 90F;
                    else leftArc = 90F;
                    entity.rangeAttack(3.5, 4, 3.5, 4, leftArc, rightArc, hitEntity -> {
                        if (entity.doHurtTarget(hitEntity, 1.25F, 0F) && entity.getHandHeat()) {
                            hitEntity.setSecondsOnFire(3);
                        }
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 0.5F, entity.getX() - hitEntity.getX(), entity.getZ() - hitEntity.getZ(), true);
                    });
                } else if ((tick == 10 || tick == 27)) entity.playSound(SoundInit.RELIC_EARTHSHAKER_ATTACK.get(), entity.getSoundVolume(), entity.getVoicePitch());
            }
        });
        builder.conditional(new CondKeyframe<>() {
            @Override
            public boolean shouldHandle(EntityRelicEarthshaker entity, Animation animation, int tick) {
                return ELECTROMAGNETIC_ANIMATION == animation;
            }

            @Override
            public void handle(EntityRelicEarthshaker entity, Animation animation, int tick) {
                entity.setDeltaMovement(0F, entity.getDeltaMovement().y, 0F);
                if (tick == 1) entity.playSound(SoundInit.RELIC_EARTHSHAKER_PRE_ATTACK.get(), 0.75F, 0.5F);
                else if (tick > 20 && tick < 90) {
                    entity.electromagneticControlled.increaseTimer();
                    if (tick == 42 || tick == 64 || tick == 86) {
                        entity.playSound(SoundInit.RELIC_EARTHSHAKER_ELECTROMAGNETIC.get(), 1F, (entity.random.nextFloat() - entity.random.nextFloat()) * 0.2F + 1.0F);
                        entity.hotControlled.increaseTimer(7);
                        if (entity.level().isClientSide) {
                            AdvancedParticleBase.spawnParticle(entity.level(), ParticleInit.ADV_RING2.get(), entity.getX(), entity.getY() + 0.1F, entity.getZ(),
                                    0, 0, 0, false, 0, Math.PI / 2F, 0,
                                    0, 1, 0.56, 0.78, 0.86, 0.8, 1, 20,
                                    true, true, false, new ParticleComponent[]{
                                            new PropertyControl(PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(1F, 0F), false),
                                            new PropertyControl(PropertyControl.EnumParticleProperty.SCALE, AnimData.KeyTrack.startAndEnd(10F, 60F), false),
                                            new PropertyControl(PropertyControl.EnumParticleProperty.YAW, AnimData.KeyTrack.startAndEnd(0F, Mth.PI * 2F), true)
                                    });
                        } else {
                            final int count = 10;
                            float offset = (float) Math.toRadians(entity.getRandom().nextGaussian() * 360 - 180);
                            for (int i = 0; i < count; ++i) {
                                float f1 = (float) (entity.getYRot() + (i + offset) * (float) Math.PI * (2.0 / count));
                                EntityElectromagnetic.shoot(entity.level(), entity, entity.position(), 2.0F, count + 2, 3, (f1 * (180F / (float) Math.PI)) - 90F, tick == 64);
                            }
                        }
                    } else if (tick < 30 && entity.hand != null) {
                        entity.spawnBlockEffect(entity.hand[0]);
                        entity.spawnBlockEffect(entity.hand[1]);
                    }
                    if (tick % 3 == 0) entity.doFractalEffect(3 + entity.random.nextInt(3));
                } else entity.electromagneticControlled.decreaseTimer();
            }
        });
        return manager;
    }

    private void doPoundGroundEffect(Vec3 pos, float dustSpeed, float airDiffusionSpeed) {
        ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 35F, 30, ParticleDust.EnumDustBehavior.GROW, airDiffusionSpeed);
        ModParticleUtils.annularParticleOutburst(this.level(), 20, dustData, pos.x, pos.y, pos.z, dustSpeed, 0.5);
    }

    private static AnimationReleaseManager<EntityRelicEarthshaker> setupAnimationRules() {
        AnimationReleaseManager<EntityRelicEarthshaker> manager = new AnimationReleaseManager<>();
        AnimationReleaseManager.Builder<EntityRelicEarthshaker> builder = manager.builder();
        builder.register(builder.define(SMASH_ATTACK_ANIMATION)
                .cooldown(new HealthScaledCooldown(400, 50, 50, 0.3F))
                .condition(ConditionFactory.and(
                        ConditionFactory.hybridDistanceRange(8, 0, 4.25),
                        ConditionFactory.randomChance(0.4F)
                )).priority(2));
        builder.register(builder.define(RANGE_ATTACK_ANIMATION)
                .cooldown(new FixedRangeCooldown(450, 50))
                .condition(ConditionFactory.and(
                        ConditionFactory.hasLineOfSight(),
                        ConditionFactory.hybridDistanceRange(8, 4.5, 14),
                        ConditionFactory.randomChance(0.4F)
                )).priority(1));
        builder.register(builder.define(ELECTROMAGNETIC_ANIMATION)
                .cooldown(new HealthScaledCooldown(450, 50, 50, 0.3F))
                .condition(ConditionFactory.and(
                        (entity, target) -> entity.getHealthPercentage() <= 0.8F,
                        ConditionFactory.distanceRange(6.5, 12),
                        ConditionFactory.randomChance(0.4F)
                )));
        builder.condition(EntityAbsRelicron::isActive);
        return manager;
    }

    private void performRangedAttack(LivingEntity target, float velocity, boolean right) {
        Vec3 lookVec = this.getLookAngle();
        Vec3 rightVec = new Vec3(-lookVec.z, 0, lookVec.x).normalize();
        double sideMultiplier = right ? 1.0 : -1.0;
        double offsetDistance = this.getBbWidth() * 0.82F;
        double xOffset = rightVec.x * offsetDistance * sideMultiplier;
        double zOffset = rightVec.z * offsetDistance * sideMultiplier;
        Vec3 vec3 = this.position().add(lookVec);
        EntityPulsedGrenade grenade = new EntityPulsedGrenade(this.level(), this, right);
        grenade.setRadius(Mth.randomBetween(this.random, 2F, 2.5F));
        grenade.moveTo(vec3.x + xOffset, this.getY(0.49D), vec3.z + zOffset);
        double d1 = target.getX() - (this.getX() + xOffset);
        double d2 = target.getY(-0.5F) - this.getY();
        double d3 = target.getZ() - (this.getZ() + zOffset);
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.12F;
        grenade.shoot(d1, d2 + d4, d3, velocity, 5F);
        this.level().addFreshEntity(grenade);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(ForgeMod.ENTITY_GRAVITY.get(), 0.125D)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5D);
    }

    public boolean getHandHeat() {
        return !this.hotControlled.isStop();
    }

    public Vec3 findTargetPoint(Entity entity, Entity target) {
        Vec3 vec3 = target.position();
        return (new Vec3(vec3.x - entity.getX(), 0.0, vec3.z - entity.getZ())).normalize();
    }

    private void doHandEffect(Vec3 pos) {
        if (pos != null) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            ParticleOptions[] options = new ParticleOptions[]{ParticleTypes.LARGE_SMOKE, ParticleTypes.SMOKE, ParticleTypes.SOUL_FIRE_FLAME};
            this.level().addParticle(options[this.random.nextInt(options.length)], pos.x, pos.y, pos.z, d0, d1, d2);
        }
    }

    private void doShotEffect(Vec3 pos) {
        float yawRad = (float) Math.toRadians(this.getYRot());
        float pitchRad = (float) Math.toRadians(this.getXRot());
        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);
        Vec3 forward = new Vec3(x, y, z);
        AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.THUMP_RING.get(), pos.x + forward.x * 0.8, pos.y, pos.z + forward.z * 0.8
                , 0, 0, 0, true, 0, 0, 0, 0, 1F,
                1, 0.94, 0.69, 1, 1, 4, true, false, false
                , new ParticleComponent[]{
                        new PropertyControl(PropertyControl.EnumParticleProperty.SCALE, AnimData.startAndEnd(4F, 20F), false),
                        new PropertyControl(PropertyControl.EnumParticleProperty.ALPHA, new AnimData.KeyTrack(new float[]{1F, 1F, 0.5F, 0F}, new float[]{0F, 0.5F, 0.75F, 1F}), false)
                });
        AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.CROSS_FLASH.get(), pos.x + forward.x * 0.8, pos.y, pos.z + forward.z * 0.8
                , 0, 0, 0, true, 0, 0, 0, 0, 1F,
                1, 0.94, 0.69, 1, 1, 3, true, false, false
                , new ParticleComponent[]{
                        new PropertyControl(PropertyControl.EnumParticleProperty.SCALE, new AnimData.KeyTrack(new float[]{0F, 8F, 0F}, new float[]{0F, 0.5F, 1F}), false)
                });
    }

    private void spawnBlockEffect(Vec3 pos) {
        if (this.level().isClientSide) {
            BlockState state = this.level().getBlockState(BlockPos.containing(pos.x, this.getY() - 0.2, pos.z));
            if (state.getRenderShape() != RenderShape.INVISIBLE) {
                for (int i = 0; i < 5; i++) {
                    double d0 = pos.x + (double) Mth.randomBetween(this.random, -0.2F, 0.2F);
                    double d1 = this.getY();
                    double d2 = pos.z + (double) Mth.randomBetween(this.random, -0.2F, 0.2F);
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    private void doFractalEffect(int count) {
        if (this.level().isClientSide) {
            for (int i = 0; i < count; i++) {
                double angle = Math.toRadians(this.yBodyRot + 270.0F) + random.nextDouble() * 2 * Math.PI;
                double minHorizontalOffset = 1;
                double maxHorizontalOffset = 3.5;
                double minVerticalOffset = 0;
                double maxVerticalOffset = this.getBbHeight();
                double horizontalRadius = minHorizontalOffset + (random.nextDouble() * (maxHorizontalOffset - minHorizontalOffset));
                double offsetX = Math.cos(angle) * horizontalRadius;
                double offsetZ = Math.sin(angle) * horizontalRadius;
                double offsetY = minVerticalOffset + (random.nextDouble() * (maxVerticalOffset - minVerticalOffset));
                doFractalEffect(this, this.position().add(0, this.getBbHeight() * 0.7, 0), this.getPosOffset(false, -1F, 0F, (float) offsetY).add(offsetX, 0, offsetZ), 0.1F, 0.2F);
            }
        }
    }
}
