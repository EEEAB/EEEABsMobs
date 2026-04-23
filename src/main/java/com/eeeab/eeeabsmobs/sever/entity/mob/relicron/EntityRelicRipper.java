package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.OverlapAnimationState;
import com.eeeab.animate.server.animation.keyframe.CondKeyframe;
import com.eeeab.animate.server.animation.keyframe.KeyframeManager;
import com.eeeab.animate.server.animation.release.AnimationCondition;
import com.eeeab.animate.server.animation.release.AnimationReleaseManager;
import com.eeeab.animate.server.animation.release.ConditionFactory;
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
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.client.render.LightningBolt;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.ModBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.ModPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.ModDamageSource;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityRelicRipper extends EntityAbsRelicron {
    public static final Animation ACTIVE_ANIMATION = Animation.create(40);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(20);
    public static final Animation DIE_ANIMATION = Animation.create(25);
    public static final Animation SWEEP_ANIMATION1 = Animation.create(40);
    public static final Animation SWEEP_ANIMATION2 = Animation.create(70).doesOverlap();
    public static final Animation SMASH_ANIMATION = Animation.create(80);
    public static final Animation CUTTING_START_ANIMATION = Animation.create(20);
    public static final Animation CUTTING_KEEP_ANIMATION = Animation.create(100);
    public static final Animation CUTTING_END_ANIMATION = Animation.create(20).doesOverlap();
    private static final Animation[] ANIMATIONS = new Animation[]{
            SWEEP_ANIMATION1,
            SWEEP_ANIMATION2,
            SMASH_ANIMATION,
            CUTTING_START_ANIMATION,
            CUTTING_KEEP_ANIMATION,
            CUTTING_END_ANIMATION,
            ACTIVE_ANIMATION,
            DEACTIVATE_ANIMATION,
            DIE_ANIMATION,
    };
    private static final KeyframeManager<EntityRelicRipper> KEYFRAME_MANAGER;
    private static final AnimationCondition<EntityRelicRipper> DERIVED_SKILL_CHECK;
    private static final AnimationReleaseManager<EntityRelicRipper> ANIMATION_RELEASE_MANAGER;
    private static final int MIN_FRAME_INTERVAL = 10;
    private static final float[][] BLOCK_OFFSETS = {{-0.25F, -0.25F}, {-0.25F, 0.25F}, {0.25F, 0.25F}, {0.25F, -0.25F},};
    private boolean derivedSkill;
    private int lastStepFrame;
    private Vec3 preSaw = Vec3.ZERO;
    private final OverlapAnimationState sweepAnimationState = new OverlapAnimationState(SWEEP_ANIMATION2);
    private final OverlapAnimationState cuttingEndAnimationState = new OverlapAnimationState(CUTTING_END_ANIMATION);
    @OnlyIn(Dist.CLIENT)
    public Vec3 saw;
    public final ControlledAnimation sawControlled = new ControlledAnimation(10);
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);

    public EntityRelicRipper(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.active = false;
        if (this.level().isClientSide) {
            this.saw = new Vec3(0, 0, 0);
        }
    }

    static {
        KEYFRAME_MANAGER = setupAnimations();
        DERIVED_SKILL_CHECK = ConditionFactory.randomChanceOnLowHealth(0.1F, 0.7F);
        ANIMATION_RELEASE_MANAGER = setupAnimationRules();
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.NORMAL;
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
    protected void onAnimationStart(Animation animation) {
        if (animation == DIE_ANIMATION) this.sawControlled.setTimer(this.sawControlled.getDuration());
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicRipper.combatConfig;
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
        this.goalSelector.addGoal(1, new RRCuttingAttackGoal(this));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, SWEEP_ANIMATION1, false));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, SWEEP_ANIMATION2, false));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, SMASH_ANIMATION, false));
        this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.0, 20));
    }

    @Override
    public void tick() {
        super.tick();
        this.sawControlled.updatePrevTimer();
        this.glowControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);

        this.pushEntitiesAway(1F, getBbHeight(), 1F, 1F);

        if (this.level().isClientSide && this.isActive()) {
            Animation animation = this.getAnimation();
            boolean isNoAnimation = NO_ANIMATION == animation;
            if (isNoAnimation && this.tickCount % 5 == 0 && this.random.nextInt(5) == 0) {
                Vec3 pos = this.getPosOffset(true, 1.5F, getBbWidth(), getBbHeight() * 0.55F);
                pos = pos.offsetRandom(this.random, 0.5F);
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
            }
            if (!this.isSilent() && !this.sawControlled.isStop() && this.tickCount % 4 == 1) {
                this.level().playLocalSound(this.saw.x, this.saw.y, this.saw.z, SoundInit.RELIC_RIPPER_SAW.get(), this.getSoundSource(),
                        this.getVoicePitch() * this.sawControlled.getAnimationFraction(), this.getVoicePitch(), false);
            }
            if (this.isSilent() || !this.isAlive()) return;
            float moveX = (float) (this.getX() - this.xo);
            float moveZ = (float) (this.getZ() - this.zo);
            float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
            if (speed > 0.05 && this.frame - lastStepFrame >= MIN_FRAME_INTERVAL) {
                int interval = isNoAnimation || animation == SWEEP_ANIMATION2 ? 15 : 19;
                if (this.frame % interval == 0) {
                    this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.RELIC_RIPPER_STEP.get(), this.getSoundSource(), 1F, this.getVoicePitch(), false);
                    lastStepFrame = this.frame;
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
        if ((this.getHealth() <= 0F && !this.isRemoved() && this.tickCount % 2 == 0) || this.isNoAnimation()) this.sawControlled.decreaseTimer();
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (source.is(ModResourceKey.OVERLOAD_EXPLODE)) {
                damage *= 2F;
            }
            return super.hurt(source, damage);
        }
    }

    @Override
    public boolean doHurtTarget(DamageSource damageSource, Entity entity, float damageMultiplier, float knockBackMultiplier, boolean canDisableShield) {
        if (!super.doHurtTarget(damageSource, entity, damageMultiplier, knockBackMultiplier, canDisableShield)) {
            return false;
        } else {
            if (entity instanceof LivingEntity target) {
                target.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 200, 0, false, false, true), null);
            }
            return true;
        }
    }

    @Override
    protected void dying() {
        Animation animation = this.getAnimation();
        if (this.getDeathAnimation() != null && animation != this.getDeathAnimation()) {
            if (animation == SWEEP_ANIMATION2) this.stopAllSuperpositionAnimation();
            this.playAnimation(this.getDeathAnimation());
        }
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
    public Animation getActiveAnimation() {
        return ACTIVE_ANIMATION;
    }

    @Override
    public Animation getDeactivateAnimation() {
        return DEACTIVATE_ANIMATION;
    }

    @Override
    public KeyframeManager<EntityRelicRipper> getKeyframeManager() {
        return KEYFRAME_MANAGER;
    }

    @Override
    public AnimationState getOverlapAnimationState(Animation animation) {
        if (SWEEP_ANIMATION2 == animation) {
            return this.sweepAnimationState;
        } else if (CUTTING_END_ANIMATION == animation) {
            return this.cuttingEndAnimationState;
        }
        return null;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.1F;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        this.playSound(SoundInit.RELIC_RIPPER_HURT.get(), 1F, this.getVoicePitch());
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.RELIC_RIPPER_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 75.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.ATTACK_DAMAGE, 9.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ARMOR, 8.0D);
    }

    private static KeyframeManager<EntityRelicRipper> setupAnimations() {
        KeyframeManager<EntityRelicRipper> manager = new KeyframeManager<>();
        KeyframeManager.KeyframeManegerBuilder<EntityRelicRipper> builder = manager.builder();
        builder.forAnimation(SWEEP_ANIMATION1)
                .inRange(10, 35, (entity, animation, tick) -> {
                    entity.distractingMovement();
                    entity.sawControlled.incrementOrDecreaseTimer(tick < 25);
                    entity.doTrailEffect(tick == 20, tick > 20 && tick <= 25);
                    if (tick == 20) entity.playSound(SoundInit.RELIC_RIPPER_WHOOSH.get(), 2.5F, entity.getVoicePitch());
                    if (!entity.level().isClientSide) {
                        LivingEntity target = entity.getTarget();
                        if (target != null) entity.lookAt(target, 30F, 30F);
                        if (tick == 22) entity.rangeAttack(3, entity.getBbHeight() + 0.2, 3, 3, 90F, 220F, null);
                    }
                });
        builder.forAnimation(SWEEP_ANIMATION2)
                .everyTick((entity, animation, tick) -> {
                    entity.sawControlled.incrementOrDecreaseTimer(tick < 60);
                    if (tick == 19 || tick == 36 || tick == 45) entity.playSound(SoundInit.RELIC_RIPPER_WHOOSH.get(), 2.5F, entity.getVoicePitch());
                    entity.doTrailEffect(tick == 20, tick > 20 && tick <= 25);
                    entity.doTrailEffect(tick == 35, tick > 35 && tick <= 50);
                    if (!entity.level().isClientSide) {
                        LivingEntity target = entity.getTarget();
                        if (target != null) entity.lookAt(target, 30F, 30F);
                        if (tick == 22) entity.rangeAttack(3, entity.getBbHeight() + 0.2, 3, 3, 100F, 200F, null);
                        if (tick == 36 || tick == 45) entity.rangeAttack(3, entity.getBbHeight(), 3, 3, e -> {
                            entity.doHurtTarget(ModDamageSource.bypassCoolDown(entity), e, 1, 0.25F, false);
                        });
                        if (tick == 60 && entity.derivedSkill) {
                            entity.playAnimation(SMASH_ANIMATION);
                        }
                    }
                });
        builder.forAnimation(SMASH_ANIMATION)
                .inRange(1, 65, (entity, animation, tick) -> {
                    entity.derivedSkill = false;
                    entity.sawControlled.decreaseTimer();
                    entity.distractingMovement();
                    entity.doTrailEffect(tick == 18, tick > 18 && tick <= 20);
                    entity.doTrailEffect(tick == 36, tick > 36 && tick <= 38);
                    entity.doTrailEffect(tick == 62, tick > 62 && tick <= 64);
                    if (tick == 17 || tick == 35 || tick == 60) entity.playSound(SoundInit.RELIC_RIPPER_WHOOSH.get(), 2.5F, entity.getVoicePitch());
                    if (tick == 18 || tick == 36 || tick == 62) entity.playSound(SoundInit.RELIC_RIPPER_SHAKE_GROUND.get(), 1F, entity.getVoicePitch());
                    if (entity.level().isClientSide && entity.saw != null && (tick == 21 || tick == 39 || tick == 65)) {
                        boolean strong = tick == 65;
                        entity.doGroundSlamEffect(strong);
                    } else {
                        LivingEntity target = entity.getTarget();
                        if (target != null) entity.lookAt(target, 30F, 30F);
                        if (tick == 20 || tick == 38 || tick == 63) {
                            Vec3 pos = entity.getPosOffset(true, 2F, 1.75F, 0F);
                            for (Entity targetEntity : entity.level().getEntitiesOfClass(Entity.class, ModEntityUtils.makeAABBWithSize(pos.x, pos.y, pos.z, 0, 4, entity.getBbHeight() * 1.2, 4), targetEntity -> targetEntity != entity && targetEntity.isAttackable() && !entity.isAlliedTo(targetEntity))) {
                                entity.doHurtTarget(targetEntity, tick == 38 ? 1F : 1.2F, 1F, tick == 63);
                            }
                        }
                    }
                });
        builder.forAnimation(CUTTING_KEEP_ANIMATION)
                .inRange(1, 89, (entity, animation, tick) -> {
                    entity.sawControlled.increaseTimer();
                    if (entity.level().isClientSide && entity.saw != null) {
                        if (tick == 1 || tick % 5 == 0) entity.doFractalEffect(4 + entity.random.nextInt(2), 0.5);
                        ModParticleUtils.blockParticleDirectionality(entity.level(), entity.saw.x, entity.saw.y - 0.8, entity.saw.z, Math.toRadians(entity.getYRot()), 1, BLOCK_OFFSETS, 0.5F);
                    }
                });
        builder.conditional(new CondKeyframe<>() {
            @Override
            public boolean shouldHandle(EntityRelicRipper entity, Animation animation, int tick) {
                return CUTTING_START_ANIMATION == animation;
            }

            @Override
            public void handle(EntityRelicRipper entity, Animation animation, int tick) {
                entity.sawControlled.incrementOrDecreaseTimer(tick >= 10);
            }
        });
        builder.conditional(new CondKeyframe<>() {
            @Override
            public boolean shouldHandle(EntityRelicRipper entity, Animation animation, int tick) {
                return CUTTING_END_ANIMATION == animation && !entity.sawControlled.isStop();
            }

            @Override
            public void handle(EntityRelicRipper entity, Animation animation, int tick) {
                entity.sawControlled.decreaseTimer();
            }
        });
        return manager;
    }

    private static AnimationReleaseManager<EntityRelicRipper> setupAnimationRules() {
        AnimationReleaseManager<EntityRelicRipper> manager = new AnimationReleaseManager<>();
        AnimationReleaseManager.Builder<EntityRelicRipper> builder = manager.builder();
        builder.register(builder.define(CUTTING_START_ANIMATION)
                .cooldown(new FixedRangeCooldown(400, 20))
                .condition(ConditionFactory.and(
                        ConditionFactory.distanceRange(0, 5),
                        ConditionFactory.randomChance(0.3F)
                ))
                .onSuccess(e -> e.derivedSkill = DERIVED_SKILL_CHECK.test(e, null))
                .priority(2));
        builder.register(builder.define(SWEEP_ANIMATION1)
                .cooldown(new FixedRangeCooldown(160, 20))
                .condition(ConditionFactory.and(
                        ConditionFactory.distanceRange(0, 3.5),
                        ConditionFactory.randomChance(0.3F)
                ))
                .priority(1));
        HealthScaledCooldown derivedCD = new HealthScaledCooldown(340, 20, 60, 0.3F);
        AnimationCondition<EntityRelicRipper> derivedCond = ConditionFactory.and(
                DERIVED_SKILL_CHECK,
                ConditionFactory.distanceRange(0, 4),
                ConditionFactory.randomChance(0.4F)
        );
        builder.register(builder.define(SWEEP_ANIMATION2)
                .cooldown(derivedCD)
                .condition(derivedCond));
        builder.register(builder.define(SMASH_ANIMATION)
                .cooldown(derivedCD)
                .condition(derivedCond));
        builder.condition(EntityAbsRelicron::isActive);
        return manager;
    }

    public boolean derivedSkillCheck() {
        float healthPct = this.getHealthPercentage();
        float prob = 0.1F;
        if (healthPct <= 0.3F) prob += 0.7F;
        else prob += 0.7F * (1 - (healthPct - 0.3F) / 0.7F);
        return prob > this.random.nextFloat();
    }

    private void distractingMovement() {
        Vec3 currentMotion = this.getDeltaMovement();
        this.setDeltaMovement(currentMotion.x * 0.8, currentMotion.y, currentMotion.z * 0.8);
    }

    private void doFractalEffect(int count, double scale) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double minHorizontalOffset = 1.5;
            double maxHorizontalOffset = 2;
            double minVerticalOffset = 0.2;
            double maxVerticalOffset = this.getBbHeight() / 2;
            double horizontalRadius = minHorizontalOffset + (random.nextDouble() * (maxHorizontalOffset - minHorizontalOffset));
            double offsetX = Math.cos(angle) * horizontalRadius * scale;
            double offsetZ = Math.sin(angle) * horizontalRadius * scale;
            double offsetY = minVerticalOffset + (random.nextDouble() * (maxVerticalOffset - minVerticalOffset)) * scale;
            LightningBolt bolt = RELICRON_BOLT.color(BOLT_COLORS[this.random.nextInt(2)]).lifespan(4)
                    .size(0.06F).spreadFactor(0.15F).fadeFunction(LightningBolt.FadeFunction.fade(0.5F)).build(this.saw, this.saw.add(offsetX, offsetY, offsetZ), this.random);
            ClientProxy.LIGHTNING_RENDER.update(this, bolt);
        }
    }

    private void doTrailEffect(boolean startFlag, boolean holdFlag) {
        if (this.level().isClientSide && this.saw != null) {
            if (startFlag) {
                this.preSaw = saw;
            } else if (holdFlag) {
                Vec3 rightPos = this.saw;
                double rLength = this.preSaw.subtract(rightPos).length();
                this.spawnSwipeParticle(this.preSaw, rightPos, (int) Math.min(Math.floor(2 * rLength), 12));
                this.preSaw = rightPos;
            }
        }
    }

    private void spawnSwipeParticle(Vec3 start, Vec3 end, int numDusts) {
        for (int i = 0; i < numDusts; i++) {
            double speedMultiplier = 0.05;
            double xSpeed = (this.random.nextDouble() - 0.5) * 2 * speedMultiplier;
            double ySpeed = (this.random.nextDouble() - 0.1) * 0.5 * speedMultiplier;
            double zSpeed = (this.random.nextDouble() - 0.5) * 2 * speedMultiplier;
            double x = start.x + i * (end.x - start.x) / numDusts;
            double y = start.y + i * (end.y - start.y) / numDusts;
            double z = start.z + i * (end.z - start.z) / numDusts;
            int tick = this.getAnimationTick();
            float randomFactor;
            for (int j = 0; j < 2; j++) {
                randomFactor = 0.15F;
                float xOffset = randomFactor * (2 * this.random.nextFloat() - 1);
                float yOffset = randomFactor * (2 * this.random.nextFloat() - 1);
                float zOffset = randomFactor * (2 * this.random.nextFloat() - 1);
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), x + xOffset, y + yOffset, z + zOffset, xSpeed, ySpeed, zSpeed);
            }
            if (this.getAnimation() == SWEEP_ANIMATION2 && (tick >= 38 && tick < 48)) {
                double dx = x - this.getX();
                double dy = y - this.getY();
                double dz = z - this.getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (distance > 0) {
                    double speed = 0.3;
                    xSpeed = (dx / distance) * speed;
                    zSpeed = (dz / distance) * speed;
                    randomFactor = 0.3F;
                    xSpeed += (this.random.nextDouble() - 0.5) * randomFactor * 0.1;
                    zSpeed += (this.random.nextDouble() - 0.5) * randomFactor * 0.1;
                }
                ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 25f, tick > 40 ? 8 : 10, ParticleDust.EnumDustBehavior.GROW, 0.98F);
                this.level().addParticle(dustData, x, y - 0.75F, z, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    private void doGroundSlamEffect(boolean strong) {
        this.level().addParticle(new ParticleRing.RingData(0F, (float) (Math.PI / 2F), strong ? 12 : 10, 0.8F, 0.8F, 0.8F, 0.9F, 70F, false, ParticleRing.EnumRingBehavior.GROW), this.saw.x, this.getY() + 0.1, this.saw.z, 0, 0, 0);
        this.doFractalEffect(strong ? 8 + this.random.nextInt(3) : 6, strong ? 1.1 : 0.9);
        ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 22F, strong ? 30 : 25, ParticleDust.EnumDustBehavior.GROW, 0.8F);
        ModParticleUtils.annularParticleOutburst(this.level(), 20, dustData, this.saw.x, this.getY() + 0.15, this.saw.z, 1.2, 0.5);
        //int length = 4 + this.random.nextInt(3);
        //for (int i = 0; i < length; i++) {
        //    double angle = 2 * Math.PI * i / length;
        //    double x = this.saw.x + 0.5 * Math.cos(angle);
        //    double y = this.saw.y - 0.25;
        //    double z = this.saw.z + 0.5 * Math.sin(angle);
        //    double xSpeed = Math.cos(angle) * 0.25;
        //    double zSpeed = Math.sin(angle) * 0.25;
        //    float scale = (float) (20 + random.nextDouble() * 10);
        //    float light = this.random.nextFloat() * 0.1F;
        //    int duration = 15 + this.random.nextInt(6);
        //    ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24F + light, 0.24F + light, 0.24F + light, scale, duration, ParticleDust.EnumDustBehavior.GROW, 0.95F);
        //    this.level().addParticle(dustData, x, y, z, xSpeed, 0.0007, zSpeed);
        //}
        AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.GLOW.get(), this.saw.x, this.getY() + 0.15, this.saw.z, 0, 0, 0, true, 0, 0, 0, 0,
                1, BOLT_COLORS[0].x, BOLT_COLORS[0].y, BOLT_COLORS[0].z, BOLT_COLORS[0].w, 1, 4, true, false, false, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.startAndEnd(1F, 0F), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.startAndEnd(10, 35), false),
                });
        int[] particles = {8, 12};
        double[] radii = {0.8, 1.2};
        double[] speeds = {0.5, 0.4};
        double[] angles = {35, 25};
        double[] color = {0.8, 0.8, 0.8, 0.4};
        ModParticleUtils.multiLayerBowlParticles(this.level(), new Vec3(this.saw.x, this.getY(), this.saw.z), 3, particles, radii, speeds, angles, color, null, 0.98F);
        ModParticleUtils.blockParticlesAround(this.level(), this.saw.x, this.getY(), this.saw.z, 20, 0.5, 1.2, 1, 3, 2, strong ? 4 : 3, -0.2, 0.1);
    }

    static class RRCuttingAttackGoal extends AnimationGroupAI<EntityRelicRipper> {
        public RRCuttingAttackGoal(EntityRelicRipper entity) {
            super(entity, false, CUTTING_START_ANIMATION, CUTTING_KEEP_ANIMATION, CUTTING_END_ANIMATION);
        }

        @Override
        public void tick() {
            entity.distractingMovement();
            Animation animation = entity.getAnimation();
            int tick = entity.getAnimationTick();
            if (CUTTING_START_ANIMATION == animation) nextAnimation(animation, CUTTING_KEEP_ANIMATION);
            else if (CUTTING_KEEP_ANIMATION == animation) {
                LivingEntity target = entity.getTarget();
                if (target != null) {
                    entity.lookAt(target, 30F, 60F);
                    entity.getLookControl().setLookAt(target, 30F, 60F);
                }
                if (tick % 5 == 0) entity.rangeAttack(2.5, entity.getBbHeight() * 0.75, 2.5, 2.5, 60F, 60F,
                        hitEntity -> {
                            if (entity.doHurtTarget(ModDamageSource.bypassCoolDown(entity), hitEntity, 0.35F, 0, false)) {
                                if (EntityRelicAnnihilator.canBeControlled(entity, hitEntity)) {
                                    double ratioX = -Math.sin(entity.yBodyRot * ((float) Math.PI / 180F));
                                    double ratioZ = Math.cos(entity.yBodyRot * ((float) Math.PI / 180F));
                                    ModEntityUtils.forceKnockBack(entity, hitEntity, 0.25F, ratioX, ratioZ, hitEntity instanceof Player);
                                }
                            }
                        });
                if (entity.derivedSkill) nextAnimation(animation, tick == 41, CUTTING_END_ANIMATION);
                else nextAnimation(animation, CUTTING_END_ANIMATION);
            } else if (CUTTING_END_ANIMATION == animation) {
                if (entity.derivedSkill && tick == 10) entity.playAnimation(SWEEP_ANIMATION2);
            }
        }
    }
}
