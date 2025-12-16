package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityRelicRipper extends EntityAbsRelicron {
    public Animation activeAnimation = Animation.create(40);
    public Animation deactivateAnimation = Animation.create(20);
    public Animation dieAnimation = Animation.create(25);
    public Animation sweep1Animation = Animation.create(40);
    public Animation sweep2Animation = Animation.create(70).doesOverlap();
    public Animation smashAnimation = Animation.create(80);
    public Animation cuttingStartAnimation = Animation.create(20);
    public Animation cuttingKeepAnimation = Animation.create(100);
    public Animation cuttingEndAnimation = Animation.create(20).doesOverlap();
    private final Animation[] animations = new Animation[]{
            activeAnimation,
            deactivateAnimation,
            dieAnimation,
            sweep1Animation,
            sweep2Animation,
            smashAnimation,
            cuttingStartAnimation,
            cuttingKeepAnimation,
            cuttingEndAnimation,
    };
    private static final UniformInt SWEEP_INTERVAL = TimeUtil.rangeOfSeconds(7, 9);
    private static final UniformInt CUTTING_INTERVAL = TimeUtil.rangeOfSeconds(19, 21);
    private static final UniformInt DERIVED_SKILL_INTERVAL = TimeUtil.rangeOfSeconds(16, 18);
    private static final int MIN_FRAME_INTERVAL = 10;
    private static final float[][] BLOCK_OFFSETS = {{-0.25F, -0.25F}, {-0.25F, 0.25F}, {0.25F, 0.25F}, {0.25F, -0.25F},};
    private int timeUntilSweep;
    private int timeUntilCutting;
    private int timeUntilDerivedSkill;
    private boolean derivedSkill;
    private int lastStepFrame;
    @OnlyIn(Dist.CLIENT)
    public Vec3 saw;
    private Vec3 preSaw = Vec3.ZERO;
    public final ControlledAnimation sawControlled = new ControlledAnimation(10);
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);

    public EntityRelicRipper(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.active = false;
        this.dropAfterDeathAnim = false;
        if (this.level().isClientSide) {
            this.saw = new Vec3(0, 0, 0);
        }
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
    protected void onAnimationStart(Animation animation) {
        if (animation == this.dieAnimation) this.sawControlled.setTimer(this.sawControlled.getDuration());
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.relicrons.relicRipper.combatConfig;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, EntityAbsRelicron.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1D) {
            @Override
            public boolean canUse() {
                return super.canUse() && EntityRelicRipper.this.isActive();
            }
        });
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> activeAnimation));
        this.goalSelector.addGoal(1, new AnimationDeactivate<>(this, () -> deactivateAnimation));
        this.goalSelector.addGoal(1, new RRSweepAttackGoal(this));
        this.goalSelector.addGoal(1, new RRCuttingAttackGoal(this));
        this.goalSelector.addGoal(1, new RRSmashAttackGoal(this));
        this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.0, 20));
    }

    @Override
    public void tick() {
        super.tick();
        this.sawControlled.updatePrevTimer();
        this.glowControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level().isClientSide) {
            LivingEntity target = this.getTarget();
            boolean canAttack = !this.isNoAi() && this.isActive() && target != null && this.canAttack(target);
            float HP = this.getHealth() / this.getMaxHealth();
            float prob = 0.1F;
            if (HP <= 0.3F) prob += 0.7F;
            else prob += 0.7F * (1 - (HP - 0.3F) / 0.7F);

            if (canAttack && this.isNoAnimation() && this.timeUntilCutting <= 0 && this.targetDistance < 5 && this.random.nextFloat() < 0.6F) {
                this.derivedSkill = prob > this.random.nextFloat() && this.random.nextFloat() < 0.5F;
                this.playAnimation(this.cuttingStartAnimation);
                this.timeUntilCutting = CUTTING_INTERVAL.sample(this.random) + (this.derivedSkill ? 150 : 0);
            }

            if (canAttack && this.isNoAnimation() && this.timeUntilSweep <= 0 && this.targetDistance < 3.5 && this.random.nextFloat() < 0.6F) {
                this.playAnimation(this.sweep1Animation);
                this.timeUntilSweep = SWEEP_INTERVAL.sample(this.random);
            }

            if (canAttack && this.isNoAnimation() && prob > this.random.nextFloat() && this.timeUntilDerivedSkill <= 0 && this.targetDistance < 4) {
                this.playAnimation(this.random.nextBoolean() ? this.sweep2Animation : this.smashAnimation);
                this.timeUntilDerivedSkill = DERIVED_SKILL_INTERVAL.sample(this.random);
            }
        }

        this.pushEntitiesAway(1.6F, getBbHeight(), 1.6F, 1.6F);

        Animation animation = this.getAnimation();
        int tick = this.getAnimationTick();
        if (animation == this.sweep1Animation) {
            this.sawControlled.incrementOrDecreaseTimer(tick >= 10 && tick <= 25);
            if (tick == 20) this.playSound(SoundInit.RELIC_RIPPER_WHOOSH.get(), 2.5F, this.getVoicePitch());
            this.doTrailEffect(tick == 20, tick > 20 && tick <= 25);
        } else if (animation == this.sweep2Animation) {
            this.sawControlled.incrementOrDecreaseTimer(tick >= 1 && tick < 60);
            if (tick == 19 || tick == 36 || tick == 45) this.playSound(SoundInit.RELIC_RIPPER_WHOOSH.get(), 2.5F, this.getVoicePitch());
            this.doTrailEffect(tick == 20, tick > 20 && tick <= 25);
            this.doTrailEffect(tick == 35, tick > 35 && tick <= 50);
        } else if (animation == this.smashAnimation) {
            this.sawControlled.decreaseTimer();
            this.doTrailEffect(tick == 18, tick > 18 && tick <= 20);
            this.doTrailEffect(tick == 36, tick > 36 && tick <= 38);
            this.doTrailEffect(tick == 62, tick > 62 && tick <= 64);
            if (tick == 17 || tick == 35 || tick == 60) this.playSound(SoundInit.RELIC_RIPPER_WHOOSH.get(), 2.5F, this.getVoicePitch());
            if (tick == 18 || tick == 36 || tick == 62) this.playSound(SoundInit.RELIC_RIPPER_SHAKE_GROUND.get(), 1F, this.getVoicePitch());
            if (this.level().isClientSide && this.saw != null && (tick == 21 || tick == 39 || tick == 65)) {
                boolean strong = tick == 65;
                this.spawnGroundSlamParticle(strong);
            }
        } else if (animation == this.cuttingStartAnimation) {
            this.sawControlled.incrementOrDecreaseTimer(tick >= 10);
        } else if (animation == this.cuttingKeepAnimation) {
            this.sawControlled.incrementOrDecreaseTimer(tick < 90);
            if (this.level().isClientSide && this.saw != null) {
                if (tick == 1 || tick % 5 == 0) this.doFractalEffect(1 + this.random.nextInt(2), 0.6);
                ModParticleUtils.blockParticleDirectionality(this.level(), this.saw.x, this.saw.y - 0.8, this.saw.z, Math.toRadians(this.getYRot()), 1, BLOCK_OFFSETS, pos -> level().getBlockState(pos), 0.5F);
            }
        } else if (animation == this.cuttingEndAnimation) {
            this.sawControlled.decreaseTimer();
        }

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        if (this.level().isClientSide && speed > 0.05 && this.isActive() && !this.isSilent()) {
            if (this.frame - lastStepFrame >= MIN_FRAME_INTERVAL) {
                int interval = this.isNoAnimation() ? 15 : 19;
                if (this.frame % interval == 0) {
                    this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.RELIC_RIPPER_STEP.get(), this.getSoundSource(), 1F, this.getVoicePitch(), false);
                    lastStepFrame = this.frame;
                }
            }
        }

        if (this.level().isClientSide && !this.isSilent() && !this.sawControlled.isStop() && this.tickCount % 4 == 1) {
            this.level().playLocalSound(this.saw.x, this.saw.y, this.saw.z, SoundInit.RELIC_RIPPER_SAW.get(), this.getSoundSource(),
                    this.getVoicePitch() * this.sawControlled.getAnimationFraction(), this.getVoicePitch(), false);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if ((this.getHealth() <= 0F && !this.isRemoved() && this.tickCount % 2 == 0) || this.isNoAnimation()) this.sawControlled.decreaseTimer();
        if (!this.level().isClientSide) {
            if (this.timeUntilSweep > 0) this.timeUntilSweep--;
            if (this.timeUntilCutting > 0) this.timeUntilCutting--;
            if (this.timeUntilDerivedSkill > 0) this.timeUntilDerivedSkill--;
        }
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());
    }

    @Override
    public boolean doHurtTarget(Entity entity, float damageMultiplier, float knockBackMultiplier, boolean canDisableShield) {
        if (!super.doHurtTarget(entity, damageMultiplier, knockBackMultiplier, canDisableShield)) {
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
            if (animation == this.sweep2Animation) this.stopAllSuperpositionAnimation();
            this.playAnimation(this.getDeathAnimation());
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 75.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.FOLLOW_RANGE, 18.0D).add(Attributes.ATTACK_DAMAGE, 10.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ARMOR, 5.0D);
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
    public Animation getActiveAnimation() {
        return this.activeAnimation;
    }

    @Override
    public Animation getDeactivateAnimation() {
        return this.deactivateAnimation;
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

    private void distractingMovement() {
        Vec3 currentMotion = this.getDeltaMovement();
        this.setDeltaMovement(currentMotion.x * 0.8, currentMotion.y, currentMotion.z * 0.8);
    }

    private void doFractalEffect(int count, double scale) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double minHorizontalOffset = 2;
            double maxHorizontalOffset = 3.5;
            double minVerticalOffset = 0.2;
            double maxVerticalOffset = this.getBbHeight() * 0.6F;
            double horizontalRadius = minHorizontalOffset + (random.nextDouble() * (maxHorizontalOffset - minHorizontalOffset));
            double offsetX = Math.cos(angle) * horizontalRadius * scale;
            double offsetZ = Math.sin(angle) * horizontalRadius * scale;
            double offsetY = minVerticalOffset + (random.nextDouble() * (maxVerticalOffset - minVerticalOffset)) * scale;
            doFractalEffect(this, this.saw, this.saw.add(offsetX, offsetY, offsetZ), scale >= 1 ? 5 : 2, scale >= 1 ? 3 : 1);
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
            if (this.getAnimation() == this.sweep2Animation && (tick >= 38 && tick < 48)) {
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
                ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.28f, 0.26f, 0.24f, 30f, tick > 40 ? 13 : 15, ParticleDust.EnumDustBehavior.GROW, 1F);
                this.level().addParticle(dustData, x, y - 0.75F, z, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    private void spawnGroundSlamParticle(boolean strong) {
        this.level().addParticle(new ParticleRing.RingData(0F, (float) (Math.PI / 2F), strong ? 14 : 12, 0.8F, 0.8F, 0.8F, 0.9F, 60f, false, ParticleRing.EnumRingBehavior.GROW), this.saw.x, this.getY() + 0.1, this.saw.z, 0, 0, 0);
        this.doFractalEffect(strong ? 6 : 3 + this.random.nextInt(3), 1);
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
        int[] particles = {8, 12};
        double[] radii = {0.8, 1.2};
        double[] speeds = {0.5, 0.4};
        double[] angles = {35, 25};
        double[] color = {0.8, 0.8, 0.8, 0.4};
        ModParticleUtils.multiLayerBowlParticles(this.level(), new Vec3(this.saw.x, this.getY(), this.saw.z), 2, particles, radii, speeds, angles, color);
        ModParticleUtils.blockParticlesAround(this.level(), this.saw.x, this.getY(), this.saw.z, 20, 0.5, 1.2, 1, 3, 2, strong ? 4 : 3, -0.2, 0.1);
    }

    static class RRSweepAttackGoal extends AnimationAI<EntityRelicRipper> {
        public RRSweepAttackGoal(EntityRelicRipper entity) {
            super(entity, false);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == entity.sweep1Animation || animation == entity.sweep2Animation;
        }

        @Override
        public void tick() {
            entity.distractingMovement();
            int tick = entity.getAnimationTick();
            LivingEntity target = entity.getTarget();
            if (target != null) entity.lookAt(target, 30F, 30F);
            if (entity.getAnimation() == entity.sweep1Animation) {
                if (tick == 22) entity.rangeAttack(3, entity.getBbHeight() + 0.2, 3, 3, 90F, 220F, null);
            } else {
                if (tick == 22) entity.rangeAttack(3, entity.getBbHeight() + 0.2, 3, 3, 100F, 200F, null);
                else if (tick == 36 || tick == 45) entity.rangeAttack(2.75, entity.getBbHeight(), 2.75, 2.75, e -> {
                    int time = e.invulnerableTime;
                    e.invulnerableTime = 0;
                    if (!entity.doHurtTarget(e)) {
                        e.invulnerableTime = time;
                    }
                });
                if (tick == 60 && entity.derivedSkill) {
                    entity.playAnimation(entity.smashAnimation);
                }
            }
        }
    }

    static class RRCuttingAttackGoal extends AnimationGroupAI<EntityRelicRipper> {
        public RRCuttingAttackGoal(EntityRelicRipper entity) {
            super(entity, false, () -> entity.cuttingStartAnimation, () -> entity.cuttingKeepAnimation, () -> entity.cuttingEndAnimation);
        }

        @Override
        public void tick() {
            entity.distractingMovement();
            Animation animation = entity.getAnimation();
            int tick = entity.getAnimationTick();
            if (animation == entity.cuttingStartAnimation) nextAnimation(animation, entity.cuttingKeepAnimation);
            else if (animation == entity.cuttingKeepAnimation) {
                LivingEntity target = entity.getTarget();
                if (target != null) {
                    entity.lookAt(target, 15F, 15F);
                    entity.getLookControl().setLookAt(target, 15F, 15F);
                }
                if (tick % 10 == 0) entity.rangeAttack(2.5, 3.5, 2.5, 2.5, 45F, 45F, null);
                if (entity.derivedSkill) nextAnimation(animation, tick == 41, entity.cuttingEndAnimation);
                else nextAnimation(animation, entity.cuttingEndAnimation);
            } else if (animation == entity.cuttingEndAnimation) {
                if (entity.derivedSkill && tick == 10) entity.playAnimation(entity.sweep2Animation);
            }
        }
    }

    static class RRSmashAttackGoal extends AnimationSimpleAI<EntityRelicRipper> {
        public RRSmashAttackGoal(EntityRelicRipper entity) {
            super(entity, () -> entity.smashAnimation, false);
        }

        @Override
        public void stop() {
            super.stop();
            entity.derivedSkill = false;
        }

        @Override
        public void tick() {
            entity.distractingMovement();
            int tick = entity.getAnimationTick();
            LivingEntity target = entity.getTarget();
            if (target != null) entity.lookAt(target, 30F, 30F);
            if (tick == 20 || tick == 38 || tick == 63) {
                Vec3 pos = entity.getPosOffset(true, 2F, 1.75F, 0F);
                for (Entity targetEntity : entity.level().getEntitiesOfClass(Entity.class, ModEntityUtils.makeAABBWithSize(pos.x, pos.y, pos.z, 0, 5, entity.getBbHeight(), 5), targetEntity -> targetEntity != entity && targetEntity.isAttackable() && (!entity.isAlliedTo(targetEntity) || !ModConfigHandler.COMMON.others.enableSameMobsTypeInjury.get()))) {
                    entity.doHurtTarget(targetEntity, tick == 38 ? 1F : 1.2F, 1F, true);
                }
            }
        }
    }
}
