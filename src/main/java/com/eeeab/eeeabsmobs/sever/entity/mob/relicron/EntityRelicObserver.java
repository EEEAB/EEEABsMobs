package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationHurt;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.AnimationNotification;
import com.eeeab.animate.server.animation.keyframe.Keyframe;
import com.eeeab.animate.server.animation.keyframe.KeyframeManager;
import com.eeeab.animate.server.animation.release.AnimationReleaseManager;
import com.eeeab.animate.server.animation.release.ConditionFactory;
import com.eeeab.animate.server.animation.release.cooldown.FixedRangeCooldown;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData.KeyTrack;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.render.LightningBolt;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ModLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.mob.CrackinessEntity;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent.PropertyControl.*;

public class EntityRelicObserver extends EntityAbsRelicron implements CrackinessEntity<EntityRelicObserver> {
    public static final Animation DIE_ANIMATION = Animation.create(20);
    public static final Animation HURT_ANIMATION = Animation.create(3);
    public static final Animation ACTIVE_ANIMATION = Animation.create(20);
    public static final Animation ATTACK_ANIMATION = Animation.create(35);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(20);
    public static final Animation LASER_ANIMATION = Animation.create(50);
    public static final Animation STORM_ANIMATION = AnimationNotification.create(50, null);
    private static final Animation[] animations = new Animation[]{
            ATTACK_ANIMATION,
            LASER_ANIMATION,
            STORM_ANIMATION,
            ACTIVE_ANIMATION,
            DEACTIVATE_ANIMATION,
            DIE_ANIMATION,
            HURT_ANIMATION,
    };
    private static final KeyframeManager<EntityRelicObserver> KEYFRAME_MANAGER;
    private static final AnimationReleaseManager<EntityRelicObserver> ANIMATION_RELEASE_MANAGER;
    private static final EntityDimensions DEACTIVATE_SIZE = EntityDimensions.scalable(1, 1);
    private static final BlockParticleOption ROUGH_BOUNDARY_BRICKS = new BlockParticleOption(ParticleTypes.BLOCK, BlockInit.ROUGH_BOUNDARY_BRICKS.get().defaultBlockState());
    public final ControlledAnimation rotControlled = new ControlledAnimation(10);

    public EntityRelicObserver(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.active = false;
        this.canplayHurtAnimation = true;
    }

    static {
        KEYFRAME_MANAGER = setupAnimations();
        ANIMATION_RELEASE_MANAGER = setupAnimationRules();
    }

    @Override//减少实体在水下的空气供应
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 0.73F;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive() && !this.isActive();
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new ModPathNavigateGround(this, level);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.isActive() ? super.getDimensions(pose) : DEACTIVATE_SIZE.scale(this.getScale());
    }

    @Override
    protected AABB makeBoundingBox() {
        return this.isActive() ? super.makeBoundingBox() : DEACTIVATE_SIZE.makeBoundingBox(this.position());
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicObserver.combatConfig;
    }

    @Override
    protected void onAnimationStart(Animation animation) {
        super.onAnimationStart(animation);
        if (!this.level().isClientSide) {
            if (animation == STORM_ANIMATION) {
                this.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 120, 0, false, false), this);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(5, new RelicronRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(8, new ModLookAtGoal(this, LivingEntity.class, 6) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.mob.getTarget() == null;
            }
        });
    }

    @Override
    protected void registerCustomGoals() {
        super.registerCustomGoals();
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, ACTIVE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationDeactivate<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new AnimationHurt<>(this, false));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, STORM_ANIMATION, false));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, ATTACK_ANIMATION, false));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, LASER_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationMeleeAI<>(this, 1));
        //this.goalSelector.addGoal(7, new ObserverLookAtTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        this.rotControlled.updatePrevTimer();
        this.rotControlled.incrementOrDecreaseTimer(this.getAnimation() == STORM_ANIMATION);
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        ANIMATION_RELEASE_MANAGER.tick(this, this.getCooldownManager());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.75D, 1.0D));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else {
            Entity entity = source.getDirectEntity();
            if (this.getAnimation() == STORM_ANIMATION) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.is(DamageTypes.THORNS) && !source.is(DamageTypeTags.IS_EXPLOSION)) {
                        livingEntity.hurt(this.damageSources().thorns(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    }
                }
            }
            if (source.is(ModResourceKey.OVERLOAD_EXPLODE)) {
                damage *= 2F;
            } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                damage *= 0.5F;
            }
            return super.hurt(source, damage);
        }
    }

    @Override
    public void handleDamageEvent(DamageSource damageSource) {
        super.handleDamageEvent(damageSource);
        this.doHurtEffect();
    }

    @Override
    public void setActive(boolean isActive) {
        super.setActive(isActive);
        if (!isActive) {
            this.refreshDimensions();
            this.setYRot(0.0F);
            this.yHeadRot = this.getYRot();
        }
    }

    @Override
    public void stopRiding() {
        super.stopRiding();
        this.yBodyRotO = 0.0F;
        this.yBodyRot = 0.0F;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        this.setYRot(0.0F);
        this.yHeadRot = this.getYRot();
        this.setOldPosAndRot();
        return super.finalizeSpawn(level, instance, spawnType, groupData, tag);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.yBodyRot = 0.0F;
        this.yBodyRotO = 0.0F;
    }

    @Override
    public Animation[] getAnimations() {
        return animations;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
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
    public KeyframeManager<EntityRelicObserver> getKeyframeManager() {
        return KEYFRAME_MANAGER;
    }

    @Override
    protected SoundEvent getActiveSound() {
        return SoundInit.RELICRON_ACTIVE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.RELIC_OBSERVER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.RELIC_OBSERVER_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    private static KeyframeManager<EntityRelicObserver> setupAnimations() {
        KeyframeManager<EntityRelicObserver> manager = new KeyframeManager<>();
        KeyframeManager.KeyframeManegerBuilder<EntityRelicObserver> builder = manager.builder();
        builder.forAnimation(LASER_ANIMATION)
                .atTick(5, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        AdvancedParticleBase.spawnParticle(entity.level(), ParticleInit.GLOW.get(), entity.getX(), entity.getY(), entity.getZ(),
                                0, 0, 0, true, 0, 0, 0, 0,
                                14, 0.56, 0.78, 0.86, 1, 1, 28, true, true, false,
                                new ParticleComponent[]{
                                        new PropertyControl(EnumParticleProperty.ALPHA, new KeyTrack(new float[]{0.0f, 0.7f, 0.7f, 0.0f}, new float[]{0f, 0.3f, 0.8f, 1f}), false),
                                        new PinLocationWithEntity(entity, new Vec3(0, entity.getEyeHeight(), 0))
                                });
                    } else {
                        double px = entity.getX();
                        double py = entity.getEyeY();
                        double pz = entity.getZ();
                        EntityGuardianLaser laser = new EntityGuardianLaser(entity.level(), entity, px, py, pz, 5);
                        entity.level().addFreshEntity(laser);
                    }
                })
                .atTick(26, (entity, animation, tick) -> {
                    entity.doShotLaserEffect();
                    entity.doFractalEffect(3 + entity.random.nextInt(3));
                })
                .atTick(28, (entity, animation, tick) -> {
                    if (!entity.level().isClientSide) entity.playSound(SoundInit.RELIC_OBSERVER_ELECTROMAGNETIC.get(), 1F, entity.getVoicePitch());
                }).everyTick(new Keyframe<>() {
                    private Vec3 targetPos;

                    @Override
                    public void handle(EntityRelicObserver entity, Animation animation, int tick) {
                        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                        LivingEntity target = entity.getTarget();
                        if (target != null) {
                            if (tick < 23 || tick > 35) {
                                entity.lookAt(target, 360F, 180F);
                                entity.getLookControl().setLookAt(target, 360F, 180F);
                                targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
                            } else if (targetPos != null) entity.getLookControl().setLookAt(targetPos);
                            else entity.setYRot(entity.yRotO);
                        }
                        if (entity.level().isClientSide && entity.tickCount % 5 == 0) {
                            entity.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), entity.getRandomX(0.75), entity.getY() + entity.getEyeHeight(), entity.getRandomZ(0.75), 0.0D, 0.0D, 0.0D);
                        }
                    }
                });
        builder.forAnimation(STORM_ANIMATION)
                .everyTick((entity, animation, tick) -> {
                    LivingEntity target = entity.getTarget();
                    if (target != null) {
                        entity.lookAt(target, 360F, 180F);
                        entity.getLookControl().setLookAt(target, 360F, 180F);
                    }
                })
                .atTick(20, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        AdvancedParticleBase.spawnParticle(entity.level(), ParticleInit.GLOW.get(), entity.getX(), entity.getY(), entity.getZ(),
                                0, 0, 0, true, 0, 0, 0, 0,
                                14, 0.56, 0.78, 0.86, 0.8, 1, 26, true, true, false
                                , new ParticleComponent[]{
                                        new PinLocationWithEntity(entity, new Vec3(0, entity.getEyeHeight(), 0)),
                                        new PropertyControl(EnumParticleProperty.ALPHA, new AnimData.Oscillator(0.2f, 0.8f, 15f, 0), true),
                                        new PropertyControl(EnumParticleProperty.SCALE, new AnimData.Oscillator(25f, 5f, 22f, Mth.PI / 3), false)
                                });
                    }
                })
                .inRange(20, 45, (entity, animation, tick) -> {
                    if (entity.level().isClientSide) {
                        if (tick % 5 == 0)
                            AdvancedParticleBase.spawnParticle(entity.level(), ParticleInit.ADV_RING2.get(), entity.getX(), entity.getY(), entity.getZ(),
                                    0, 0, 0, false, 0, Math.PI / 2F, 0, 0,
                                    1, 0.56, 0.78, 0.86, 0.8, 1, 10, true, true, false,
                                    new ParticleComponent[]{
                                            new PinLocationWithEntity(entity, new Vec3(0, entity.getEyeHeight(), 0)),
                                            new PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1F, 0F), false),
                                            new PropertyControl(EnumParticleProperty.SCALE, KeyTrack.startAndEnd(5F, 35F), false),
                                            new PropertyControl(EnumParticleProperty.YAW, new KeyTrack(new float[]{0F, 0.5F}, new float[]{0F, Mth.PI * 2F}),
                                                    true),
                                    });
                        if (entity.random.nextFloat() < 0.6) entity.doFractalEffect(1 + entity.random.nextInt(2));
                    } else {
                        entity.rangeAttack(3, 3, 3, 3, e -> {
                            if (entity.doHurtTarget(e, 1F, 1F)) {
                                e.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 200, 0, false, false, true), entity);
                            }
                        });
                    }
                })
                .everyNTick(18, 36, 6, (entity, animation, tick) -> {
                    entity.playSound(SoundInit.RELIC_OBSERVER_ELECTRIC_PULSE.get(), 1F, 1.2F);
                });
        builder.forAnimation(ATTACK_ANIMATION).inRange(1, 19, (entity, animation, tick) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            }
        }).atTick(20, (entity, animation, tick) -> {
            if (entity.level().isClientSide) {
                Vec3 pos = entity.getPosOffset(false, 1F, 0F, entity.getEyeHeight());
                entity.doAnnularOutburstEffect(pos, (float) Math.toRadians(entity.getYRot()));
                AdvancedParticleBase.spawnParticle(entity.level(), ParticleInit.CRIT_RING.get(), pos.x, pos.y, pos.z
                        , 0, 0, 0, false, Math.toRadians(-entity.getYRot() - 90), Math.toRadians(180), 0, 0, 1,
                        0.9, 0.9, 0.9, 1, 1, 6, false, false, false
                        , new ParticleComponent[]{
                                new PropertyControl(PropertyControl.EnumParticleProperty.SCALE, AnimData.startAndEnd(0F, 25F), false),
                                new PropertyControl(PropertyControl.EnumParticleProperty.ALPHA, AnimData.startAndEnd(1F, 0F), false)
                        });
            } else {
                entity.playSound(SoundInit.RELIC_OBSERVER_ATTACK.get());
                entity.rangeAttack(2.5, entity.getBbHeight(), 2.5, 2.5, 60F, 60F, null);
            }
        });
        return manager;
    }

    private static AnimationReleaseManager<EntityRelicObserver> setupAnimationRules() {
        AnimationReleaseManager<EntityRelicObserver> manager = new AnimationReleaseManager<>();
        AnimationReleaseManager.Builder<EntityRelicObserver> builder = manager.builder();
        builder.register(builder.define(STORM_ANIMATION)
                .priority(1)
                .cooldown(new FixedRangeCooldown(200, 50))
                .condition(ConditionFactory.and(
                        ConditionFactory.randomChance(0.4F),
                        ConditionFactory.distanceRange(0, 3.5)
                )));
        builder.register(builder.define(ATTACK_ANIMATION)
                .cooldown(new FixedRangeCooldown(200, 50))
                .condition(ConditionFactory.and(
                        ConditionFactory.randomChance(0.4F),
                        ConditionFactory.distanceRange(0, 2, 3)
                )));
        builder.register(builder.define(LASER_ANIMATION)
                .cooldown(new FixedRangeCooldown(220, 60))
                .condition(ConditionFactory.and(
                        ConditionFactory.hasLineOfSight(),
                        ConditionFactory.distanceRange(0, 8, 10)
                )));
        builder.condition(EntityAbsRelicron::isActive);
        return manager;
    }

    private void doAnnularOutburstEffect(Vec3 pos, float yRot) {
        if (!this.level().isClientSide) return;
        int count = 6;
        double initialRadius = 0.5F;
        double expansionSpeed = 0.5F;
        for (int i = 0; i < count; i++) {
            double currentAngle = (2 * Math.PI * i) / count;
            double baseOffsetY = initialRadius * Math.cos(currentAngle);
            double baseOffsetZ = initialRadius * Math.sin(currentAngle);
            double worldOffsetX = -baseOffsetZ * Math.sin(yRot);
            double worldOffsetZ = baseOffsetZ * Math.cos(yRot);
            double particleX = pos.x + worldOffsetX;
            double particleY = pos.y + baseOffsetY;
            double particleZ = pos.z + worldOffsetZ;
            double baseVelY = Math.cos(currentAngle) * expansionSpeed;
            double baseVelZ = Math.sin(currentAngle) * expansionSpeed;
            double particleVelX = -baseVelZ * Math.sin(yRot);
            double particleVelZ = baseVelZ * Math.cos(yRot);
            this.level().addParticle(ROUGH_BOUNDARY_BRICKS, particleX, particleY, particleZ, particleVelX, baseVelY, particleVelZ);
        }
    }

    private void doHurtEffect() {
        if (!this.level().isClientSide) return;
        for (int i = 0; i < 5; ++i) {
            double dx = getRandomX(0.5);
            double dy = this.getEyeY();
            double dz = getRandomZ(0.5);
            level().addParticle(ROUGH_BOUNDARY_BRICKS, dx, dy, dz, -getDeltaMovement().x() * 0.25F, -getDeltaMovement().y() * 0.25F, -getDeltaMovement().z() * 0.25F);
        }
    }

    private void doFractalEffect(int count) {
        if (!this.level().isClientSide) return;
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double minHorizontalOffset = 1;
            double maxHorizontalOffset = 2;
            double minVerticalOffset = 0;
            double maxVerticalOffset = 1.5;
            double horizontalRadius = minHorizontalOffset + (random.nextDouble() * (maxHorizontalOffset - minHorizontalOffset));
            double offsetX = Math.cos(angle) * horizontalRadius;
            double offsetZ = Math.sin(angle) * horizontalRadius;
            double offsetY = minVerticalOffset + (random.nextDouble() * (maxVerticalOffset - minVerticalOffset));
            LightningBolt bolt = RELICRON_BOLT.color(BOLT_COLORS[this.random.nextInt(2)]).size(0.06F).spreadFactor(0.15F)
                    .build(this.position().add(0, this.getEyeHeight(), 0), this.position().add(offsetX, this.getEyeHeight() + offsetY, offsetZ), this.random);
            ClientProxy.LIGHTNING_RENDER.update(this, bolt);
        }
    }

    private void doShotLaserEffect() {
        if (!this.level().isClientSide) return;
        float yawRad = (float) Math.toRadians(this.getYRot());
        float pitchRad = (float) Math.toRadians(this.getXRot());
        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);
        Vec3 pos = this.position().add(0, this.getEyeHeight(), 0);
        for (int i = 0; i < 3; i++) {
            double d0 = 0.1 + i * 0.2;
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_RING2.get(), pos.x, pos.y, pos.z
                    , x * d0, y * d0, z * d0, false, (float) Math.toRadians(-this.getYRot() + 180), (float) Math.toRadians(-this.getXRot()), 0, 0, 1F,
                    0.9, 0.9, 0.9, 1, 1, 7, true, false, false
                    , new ParticleComponent[]{
                            new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(4, 10 + i * 2.5F), false),
                            new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(1F, 0.1F), false)
                    });
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FOLLOW_RANGE, 18.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 10.0D);
    }

    //static class NoneRotationControl extends BodyRotationControl {
    //    public NoneRotationControl(Mob mob) {
    //        super(mob);
    //    }
    //
    //    public void clientTick() {
    //    }
    //}
    //
    //static class ObserverLookAtTargetGoal extends Goal {
    //    private final EntityRelicObserver entity;
    //
    //    public ObserverLookAtTargetGoal(EntityRelicObserver entity) {
    //        this.entity = entity;
    //        this.setFlags(EnumSet.of(Flag.LOOK));
    //    }
    //
    //    @Override
    //    public boolean canUse() {
    //        return entity.getTarget() != null;
    //    }
    //
    //    @Override
    //    public boolean requiresUpdateEveryTick() {
    //        return true;
    //    }
    //
    //    @Override
    //    public void tick() {
    //        LivingEntity target = this.entity.getTarget();
    //        if (target != null && target.isAlive()) {
    //            if (target.distanceToSqr(this.entity) < 1024.0D) {
    //                double d1 = target.getX() - this.entity.getX();
    //                double d2 = target.getZ() - this.entity.getZ();
    //                this.entity.setYRot(-((float) Mth.atan2(d1, d2)) * (180F / (float) Math.PI));
    //                this.entity.yBodyRot = this.entity.getYRot();
    //            }
    //        } else {
    //            Vec3 vec3 = this.entity.getDeltaMovement();
    //            this.entity.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI));
    //            this.entity.yBodyRot = this.entity.getYRot();
    //        }
    //    }
    //}
}
