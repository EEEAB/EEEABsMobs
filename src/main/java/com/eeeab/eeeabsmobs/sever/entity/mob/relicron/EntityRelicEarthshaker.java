package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.AnimData;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.mob.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityElectromagnetic;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityPulsedGrenade;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityRelicEarthshaker extends EntityAbsRelicron implements GlowEntity, RangedAttackMob {
    public final Animation dieAnimation = Animation.create(60);
    public final Animation activeAnimation = Animation.create(30);
    public final Animation deactivateAnimation = Animation.create(30);
    public final Animation attackAnimationLeft = Animation.create(40);
    public final Animation attackAnimationRight = Animation.create(40);
    public final Animation smashAttackAnimation = Animation.create(35);
    public final Animation rangeAttackAnimation = Animation.create(100);
    public final Animation rangeAttackStopAnimation = Animation.create(10);
    public final Animation electromagneticAnimation = Animation.create(100);
    private final Animation[] animations = new Animation[]{
            this.dieAnimation,
            this.activeAnimation,
            this.deactivateAnimation,
            this.attackAnimationLeft,
            this.attackAnimationRight,
            this.smashAttackAnimation,
            this.rangeAttackAnimation,
            this.rangeAttackStopAnimation,
            this.electromagneticAnimation
    };
    private static final int SMASH_ATTACK_CD = 370;
    private static final int ELECTROMAGNETIC_CD = 350;
    private static final int RANGE_ATTACK_CD = 400;
    private int timeUntilSmashAttack;
    private int timeUntilRangeAttack;
    private int timeUntilElectromagnetic;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] hand;//0:left 1:right
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

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.ELITE;
    }

    @Override
    public float getStepHeight() {
        return 1.5F;
    }

    @Override//减少实体在水下的空气供应
    protected int decreaseAirSupply(int air) {
        return air;
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

    @Override//被方块阻塞
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.86F;
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
        return ModConfigHandler.COMMON.mobs.relicrons.relicEarthshaker.combatConfig;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.smashAttackAnimation) {
                this.timeUntilSmashAttack = getCoolingTimerUtil(SMASH_ATTACK_CD, SMASH_ATTACK_CD - 50, 0.5F);
            } else if (animation == this.rangeAttackAnimation) {
                this.timeUntilRangeAttack = getCoolingTimerUtil(RANGE_ATTACK_CD, RANGE_ATTACK_CD - 50, 0.5F);
            } else if (animation == this.electromagneticAnimation) {
                this.timeUntilElectromagnetic = getCoolingTimerUtil(ELECTROMAGNETIC_CD, ELECTROMAGNETIC_CD - 50, 0.5F);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityAbsRelicron.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, 0, false, false, (golem) -> !(golem instanceof Shulker)));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && EntityRelicEarthshaker.this.isActive();
            }
        });
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> activeAnimation));
        this.goalSelector.addGoal(1, new AnimationDeactivate<>(this, () -> deactivateAnimation));
        this.goalSelector.addGoal(1, new REMeleeAttackGoal(this));
        this.goalSelector.addGoal(1, new RERangeAttackGoal(this));
        this.goalSelector.addGoal(1, new REElectromagneticAttackGoal(this));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, () -> smashAttackAnimation, 5F, 21, 1.5F, 1.5F, true) {
            @Override
            public void tick() {
                this.entity.setDeltaMovement(0F, this.entity.onGround() && !this.entity.isNoGravity() ? -0.01F : this.entity.getDeltaMovement().y, 0F);
                LivingEntity target = this.entity.getTarget();
                if (target != null) {
                    if (this.entity.getAnimationTick() < 17) this.entity.getLookControl().setLookAt(target, 30F, 30F);
                    else this.entity.setYRot(this.entity.yRotO);
                }
                super.tick();
            }

            @Override
            protected void onHit(LivingEntity entity) {
                if (!this.entity.hotControlled.isStop() && !this.entity.isAlliedTo(entity)) entity.setSecondsOnFire(5);
            }
        });
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> rangeAttackAnimation));
        this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.0, 35, () -> attackAnimationLeft, () -> attackAnimationRight));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return (!this.isActive() || animation == activeAnimation || animation == deactivateAnimation) || super.isInvulnerableTo(damageSource);
    }

    @Override
    public void tick() {
        super.tick();
        this.glowControlled.updatePrevTimer();
        this.hotControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);

        if (!this.level().isClientSide) {
            if (this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);
            if (this.checkCanAttack() && this.timeUntilSmashAttack <= 0 && ((targetDistance <= 5.5F && ModEntityUtils.checkTargetComingCloser(this, this.getTarget())) || this.targetDistance < 5F)) {
                this.playAnimation(this.smashAttackAnimation);
            }
            if (this.checkCanAttack() && this.timeUntilRangeAttack <= 0 && Math.pow(this.targetDistance, 2.0) > this.getMeleeAttackRangeSqr(this.getTarget()) + 5) {
                this.playAnimation(this.rangeAttackAnimation);
            }
            if (this.checkCanAttack() && this.timeUntilElectromagnetic <= 0 && (this.getHealthPercentage() <= 0.8F || this.tickCount > 1200) && this.targetDistance < 6.5F) {
                this.playAnimation(this.electromagneticAnimation);
            }
        }

        this.pushEntitiesAway(1.8F, getBbHeight(), 1.8F, 1.8F);

        int tick = this.getAnimationTick();
        Animation animation = this.getAnimation();
        if (animation == this.activeAnimation) {
            if (tick <= 3 && this.hand != null) {
                this.spawnBlockEffect(this.hand[0]);
                this.spawnBlockEffect(this.hand[1]);
            }
        } else if (animation == this.deactivateAnimation) {
            if (tick > 22 && this.hand != null) {
                this.spawnBlockEffect(this.hand[0]);
                this.spawnBlockEffect(this.hand[1]);
            }
        } else if (animation == this.smashAttackAnimation) {
            if (tick == 4) this.playSound(SoundInit.RELIC_EARTHSHAKER_PRE_ATTACK.get(), 0.5F, this.getVoicePitch());
            else if (tick == 12) this.doFractalEffect(3 + this.random.nextInt(3));
            else if (tick == 20) {
                if (this.level().isClientSide) {
                    Vec3 pos = this.getPosOffset(false, this.getBbWidth(), 0F, 0.1F);
                    int[] particles = {10, 15, 10};
                    double[] radii = {1.5, 2, 2.5};
                    double[] speeds = {0.8, 0.7, 0.6};
                    double[] angles = {35, 25, 15};
                    double[] color = {0.8, 0.8, 0.8, 0.5};
                    ModParticleUtils.multiLayerBowlParticles(this.level(), pos, 2, particles, radii, speeds, angles, color);
                    this.level().addParticle(new ParticleRing.RingData(0F, (float) (Math.PI / 2F), 8, 0.8F, 0.8F, 0.8F, 0.9F, 80F, false, ParticleRing.EnumRingBehavior.GROW), pos.x, pos.y, pos.z, 0, 0, 0);
                    if (!this.hotControlled.isStop()) ModParticleUtils.annularParticleOutburstOnGround(level(), ParticleTypes.SOUL_FIRE_FLAME, this, 10, 5, 2.5, this.getBbWidth(), 0, 0.15);
                    ModParticleUtils.blockParticlesAround(this.level(), pos.x, this.getY(), pos.z, 45, 1, 3, 2, 5, 3, 6, -0.2, 0.1);
                }
                this.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + this.random.nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(level(), position(), 15, 0.125F, 2, 3);
            }
        } else if (animation == this.rangeAttackAnimation) {
            if (tick >= 20 && tick % 20 == 0) {
                this.hotControlled.increaseTimer(5);
                if (this.timeUntilElectromagnetic <= 100) this.timeUntilElectromagnetic = 100;
            }
        } else if (animation == this.attackAnimationLeft || animation == this.attackAnimationRight) {
            if (tick == 10 || tick == 27) this.playSound(SoundInit.RELIC_EARTHSHAKER_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
        } else if (animation == this.dieAnimation) {
            if (tick < 20) {
                LivingEntity target = this.getTarget();
                if (target != null) this.getLookControl().setLookAt(target, 30F, 30F);
            } else this.setYRot(this.yRotO);
            if (tick == 36) {
                if (this.level().isClientSide) {
                    Vec3 pos = this.getPosOffset(false, this.getBbWidth(), 0F, 0.1F);
                    ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24F, 0.24F, 0.24F, 40F, 25, ParticleDust.EnumDustBehavior.GROW, 0.98F);
                    ModParticleUtils.annularParticleOutburst(level(), 15, dustData, pos.x, pos.y, pos.z, 0.5F, 0.2);
                    if (!this.hotControlled.isStop()) ModParticleUtils.annularParticleOutburst(level(), 10, ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, 0.3F, 0.2);
                }
            } else if (tick == 39) {
                EntityCameraShake.cameraShake(level(), position(), 15, 0.125F, 0, 10);
            } else if (tick == 40) {
                this.rangeAttack(4.5, this.getBbHeight() * 0.6, 4.5, 4.5, 120F, 120F, hitEntity -> {
                    if (this.doHurtTarget(hitEntity, 3F, 0F, true) && this.getHandHeat()) {
                        hitEntity.setSecondsOnFire(5);
                    }
                    double ratioX = Math.sin(this.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (-Math.cos(this.getYRot() * ((float) Math.PI / 180F)));
                    ModEntityUtils.forceKnockBack(this, hitEntity, 0.5F, ratioX, ratioZ, true);
                });
            }
        } else if (animation == this.electromagneticAnimation) {
            this.setDeltaMovement(0F, this.onGround() && !this.isNoGravity() ? -0.01F : this.getDeltaMovement().y, 0F);
            if (tick == 1) this.playSound(SoundInit.RELIC_EARTHSHAKER_PRE_ATTACK.get(), 0.75F, 0.5F);
            else if (tick > 20 && tick < 90) {
                this.electromagneticControlled.increaseTimer();
                if (tick == 42 || tick == 64 || tick == 86) {
                    this.playSound(SoundInit.RELIC_EARTHSHAKER_ELECTROMAGNETIC.get(), 1F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    if (this.timeUntilRangeAttack <= 100) this.timeUntilRangeAttack = 100;
                    this.electromagneticControlled.decreaseTimer(10);
                    this.hotControlled.increaseTimer(7);
                    if (this.level().isClientSide) {
                        AdvancedParticleBase.spawnParticle(level(), ParticleInit.BIG_RING.get(), this.getX(), this.getY() + 0.1F, this.getZ(), 0, 0, 0, false, 0, Math.PI / 2F, 0, 0, 1, 0.56, 0.78, 0.86, 0.8, 1, 20, true, true, false, new ParticleComponent[]{new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(1F, 0F), false), new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.KeyTrack.startAndEnd(10F, 60F), false), new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.YAW, AnimData.KeyTrack.startAndEnd(0F, Mth.PI * 2F), true),});
                    }
                } else if (tick < 30 && this.hand != null) {
                    this.spawnBlockEffect(this.hand[0]);
                    this.spawnBlockEffect(this.hand[1]);
                }
                if (this.random.nextFloat() < 0.5F) this.doFractalEffect(1 + this.random.nextInt(2));
            } else this.electromagneticControlled.decreaseTimer(2);
        }

        if (!this.isNoAi() && this.isAlive() && this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F && this.random.nextInt(5) == 0) {
            this.doWalkEffect(1);
        }
        if (this.level().isClientSide && this.isActive() && this.isAlive()) {
            if (this.tickCount % 5 == 0 && this.random.nextInt(20) == 0) {
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), this.getRandomX(1.5D), this.getRandomY(), this.getRandomZ(1.5D), 0.0D, 0.07D, 0.0D);
            }
            if (!this.hotControlled.isStop() && this.hand != null && this.hand.length > 0) {
                this.spawnHandEffect(this.hand[0], this.random.nextInt(50) == 0);
                this.spawnHandEffect(this.hand[1], this.random.nextInt(50) == 0);
            }
        }

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        if (this.level().isClientSide && speed > 0.05 && this.isAlive() && !this.isSilent()) {
            if (this.frame % 10 == 1 && (this.isNoAnimation() || animation == this.rangeAttackAnimation)) {
                this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.RELIC_EARTHSHAKER_STEP.get(), this.getSoundSource(), 1F, 1F, false);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.timeUntilRangeAttack > 0) {
                this.timeUntilRangeAttack--;
            }
            if (this.timeUntilSmashAttack > 0) {
                this.timeUntilSmashAttack--;
            }
            if (this.timeUntilElectromagnetic > 0) {
                this.timeUntilElectromagnetic--;
            }
        }
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());
        if (this.getAnimation() != this.rangeAttackAnimation) {
            if (tickCount % 10 == 0) this.hotControlled.decreaseTimer();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                if (this.getAnimation() == this.electromagneticAnimation) {
                    damage *= 0.2F;
                }
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

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(source, pLooting, pRecentlyHit);
        ItemEntity itementity = this.spawnAtLocation(ItemInit.ANCIENT_DRIVE_CRYSTAL.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
            itementity.setGlowingTag(true);
        }
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
    protected SoundEvent getActiveSound() {
        return SoundInit.RELICRON_FRICTION.get();
    }

    @Override
    protected SoundEvent getDeactivateSound() {
        return SoundInit.RELICRON_FRICTION.get();
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
    public void playAmbientSound() {
        if (this.isActive() && this.getAnimation() != this.activeAnimation) {
            super.playAmbientSound();
        }
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D).add(Attributes.ARMOR, 15.0D).add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.FOLLOW_RANGE, 32.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(ForgeMod.ENTITY_GRAVITY.get(), 0.125D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        this.playSound(SoundInit.LAUNCH_GRENADE.get());
        double d1 = target.getX() - this.getX();
        double d2 = target.getY(-0.5F) - this.getY();
        double d3 = target.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.12F;
        for (int i = 1; i <= 2; i++) {
            double yBodyRadians = Math.toRadians(this.yBodyRot + (180 * (i - 1)));
            EntityPulsedGrenade grenade = new EntityPulsedGrenade(this.level(), this);
            grenade.shoot(d1, d2 + d4, d3, velocity, 3F);
            Vec3 vec3 = this.position().add(this.getLookAngle());
            float offset = this.getBbWidth() * 0.82F;
            grenade.setPos(vec3.x + offset * Math.cos(yBodyRadians), this.getY(0.48D), vec3.z + offset * Math.sin(yBodyRadians));
            this.level().addFreshEntity(grenade);
        }
    }

    public boolean getHandHeat() {
        return !this.hotControlled.isStop();
    }

    private boolean checkCanAttack() {
        return !this.isNoAi() && this.isActive() && this.isNoAnimation() && this.getTarget() != null && this.canAttack(this.getTarget());
    }

    private void spawnHandEffect(Vec3 pos, boolean fire) {
        if (pos != null) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(fire ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMOKE, pos.x, pos.y, pos.z, d0, d1, d2);
        }
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
                double angle = random.nextDouble() * 2 * Math.PI;
                double minHorizontalOffset = 1;
                double maxHorizontalOffset = 4;
                double minVerticalOffset = 0;
                double maxVerticalOffset = this.getBbHeight();
                double horizontalRadius = minHorizontalOffset + (random.nextDouble() * (maxHorizontalOffset - minHorizontalOffset));
                double offsetX = Math.cos(angle) * horizontalRadius;
                double offsetZ = Math.sin(angle) * horizontalRadius;
                double offsetY = minVerticalOffset + (random.nextDouble() * (maxVerticalOffset - minVerticalOffset));
                this.doFractalEffect(this.position().add(0, this.getBbHeight() * 0.7, 0), this.getPosOffset(false, -1F, 0F, (float) offsetY).add(offsetX, 0, offsetZ));
            }
        }
    }

    static class REMeleeAttackGoal extends AnimationAI<EntityRelicEarthshaker> {
        protected REMeleeAttackGoal(EntityRelicEarthshaker entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == this.entity.attackAnimationLeft || animation == this.entity.attackAnimationRight;
        }

        @Override
        public void tick() {
            int tick = this.entity.getAnimationTick();
            LivingEntity target = this.entity.getTarget();
            this.entity.setDeltaMovement(0F, this.entity.onGround() && !this.entity.isNoGravity() ? -0.01F : this.entity.getDeltaMovement().y, 0F);
            if (!(tick < 20 && tick > 7) && target != null) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
            } else this.entity.setYRot(this.entity.yRotO);
            if (tick == 11) {
                entity.rangeAttack(6, entity.getBbHeight() * 1.2, 6, 6, 30F, 30F, hitEntity -> {
                    if (entity.doHurtTarget(hitEntity, 1F, 0F, true) && entity.getHandHeat()) {
                        hitEntity.setSecondsOnFire(5);
                    }
                    double ratioX = -Math.sin(entity.yBodyRot * ((float) Math.PI / 180F));
                    double ratioZ = Math.cos(entity.yBodyRot * ((float) Math.PI / 180F));
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 0.8F, ratioX, ratioZ, true);
                });
            } else if (tick == 28) {
                float leftArc = 45F, rightArc = 45F;
                if (this.entity.getAnimation() == this.entity.attackAnimationRight) rightArc = 90F;
                else leftArc = 90F;
                entity.rangeAttack(4.5, 4.5, 4.5, 4.5, leftArc, rightArc, hitEntity -> {
                    if (entity.doHurtTarget(hitEntity, 1.25F, 0F, false) && entity.getHandHeat()) {
                        hitEntity.setSecondsOnFire(5);
                    }
                    ModEntityUtils.forceKnockBack(entity, hitEntity, 0.5F, this.entity.getX() - hitEntity.getX(), this.entity.getZ() - hitEntity.getZ(), true);
                });
            }
        }
    }

    static class RERangeAttackGoal extends AnimationAI<EntityRelicEarthshaker> {
        private int lostTargetDelay;

        public RERangeAttackGoal(EntityRelicEarthshaker entity) {
            super(entity);
        }

        @Override
        public void start() {
            super.start();
            this.lostTargetDelay = 0;
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == this.entity.rangeAttackAnimation;
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            this.entity.setYRot(this.entity.yBodyRot);
            if (this.lostTargetDelay > 0) this.lostTargetDelay--;
            if (target != null && target.isAlive() && this.entity.canAttack(target) && this.lostTargetDelay < 10) {
                if (!this.entity.getSensing().hasLineOfSight(target)) this.lostTargetDelay += 2;
                this.moveGoal(target);
                this.entity.getLookControl().setLookAt(target, 15F, 15F);
                this.entity.lookAt(target, 15F, 15F);
            } else {
                this.entity.playAnimation(this.entity.rangeAttackStopAnimation);
                return;
            }
            int tick = this.entity.getAnimationTick();
            if (tick % 20 == 0 && tick > 10) this.entity.performRangedAttack(target, 1F);
        }

        private void moveGoal(LivingEntity target) {
            double moveSpeed = this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.75;
            double distance = this.entity.distanceToSqr(target);
            if (distance < Math.pow(9, 2)) {
                Vec3 vec3 = findTargetPoint(target, this.entity);
                this.entity.setDeltaMovement(vec3.x * moveSpeed, this.entity.getDeltaMovement().y, vec3.z * moveSpeed);
            } else if (distance > Math.pow(12, 2)) {
                Vec3 vec3 = findTargetPoint(this.entity, target);
                this.entity.setDeltaMovement(vec3.x * moveSpeed, this.entity.getDeltaMovement().y, vec3.z * moveSpeed);
            } else this.entity.setDeltaMovement(0F, this.entity.onGround() && !this.entity.isNoGravity() ? -0.01F : this.entity.getDeltaMovement().y, 0F);
        }

        public static Vec3 findTargetPoint(Entity entity, Entity target) {
            Vec3 vec3 = target.position();
            return (new Vec3(vec3.x - entity.getX(), 0.0, vec3.z - entity.getZ())).normalize();
        }
    }

    static class REElectromagneticAttackGoal extends AnimationAI<EntityRelicEarthshaker> {
        public REElectromagneticAttackGoal(EntityRelicEarthshaker entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == this.entity.electromagneticAnimation;
        }

        @Override
        public void tick() {
            int tick = this.entity.getAnimationTick();
            this.entity.setYRot(this.entity.yRotO);
            this.entity.setDeltaMovement(0F, this.entity.onGround() && !this.entity.isNoGravity() ? -0.01F : this.entity.getDeltaMovement().y, 0F);
            if (!this.entity.level().isClientSide) {
                if (tick == 43 || tick == 65 || tick == 87) {
                    final int count = 10;
                    float offset = (float) Math.toRadians(this.entity.getRandom().nextGaussian() * 360 - 180);
                    for (int i = 0; i < count; ++i) {
                        float f1 = (float) (this.entity.getYRot() + (i + offset) * (float) Math.PI * (2.0 / count));
                        EntityElectromagnetic.shoot(this.entity.level(), this.entity, 2.0F, count + 2, 3, (f1 * (180F / (float) Math.PI)) - 90F, tick == 65);
                    }
                }
            }
        }
    }
}
