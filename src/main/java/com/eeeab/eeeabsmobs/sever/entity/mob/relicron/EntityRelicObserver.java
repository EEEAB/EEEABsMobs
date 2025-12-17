package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationHurt;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.AnimData;
import com.eeeab.eeeabsmobs.client.particle.util.AnimData.KeyTrack;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.mob.CrackinessEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.mob.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent.PropertyControl.*;

public class EntityRelicObserver extends EntityAbsRelicron implements GlowEntity, CrackinessEntity<EntityRelicObserver> {
    public final Animation dieAnimation = Animation.create(20);
    public final Animation hurtAnimation = Animation.create(3);
    public final Animation activeAnimation = Animation.create(20);
    public final Animation deactivateAnimation = Animation.create(20);
    public final Animation shootLaserAnimation = Animation.create(50);
    public final Animation stormAnimation = Animation.create(50);
    private final Animation[] animations = new Animation[]{
            dieAnimation,
            hurtAnimation,
            activeAnimation,
            deactivateAnimation,
            shootLaserAnimation,
            stormAnimation
    };
    private static final EntityDimensions DEACTIVATE_SIZE = EntityDimensions.scalable(1, 1);
    private static final UniformInt HARD_INTERVAL = TimeUtil.rangeOfSeconds(7, 10);
    private static final UniformInt NORMAL_INTERVAL = TimeUtil.rangeOfSeconds(12, 15);
    private static final BlockParticleOption DEEPSLATE_BRICKS = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DEEPSLATE_BRICKS.defaultBlockState());
    private int timeUntilShootLaser;
    private int timeUntilStorm;
    public final ControlledAnimation rotControlled = new ControlledAnimation(10);

    public EntityRelicObserver(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.active = false;
        this.canplayHurtAnimation = true;
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

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 0.73F;
    }

    @Override//被方块阻塞
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new NoneRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new EMPathNavigateGround(this, level);
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
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, EntityAbsRelicron.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D) {
            @Override
            public boolean canUse() {
                return super.canUse() && EntityRelicObserver.this.isActive();
            }
        });
    }

    @Override
    protected void registerCustomGoals() {
        super.registerCustomGoals();
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> activeAnimation));
        this.goalSelector.addGoal(1, new AnimationDeactivate<>(this, () -> deactivateAnimation));
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new AnimationHurt<>(this, false));
        this.goalSelector.addGoal(1, new AnimationAI<>(this, false) {
            @Override
            protected boolean test(Animation animation) {
                return animation == entity.stormAnimation;
            }

            @Override
            public void tick() {
                LivingEntity target = entity.getTarget();
                if (target != null) {
                    entity.lookAt(target, 360F, 180F);
                    entity.getLookControl().setLookAt(target, 360F, 180F);
                }
                int tick = this.entity.getAnimationTick();
                if (tick >= 20 && tick <= 45) {
                    entity.rangeAttack(3, 3, 3, 3, entity -> {
                        doHurtTarget(entity, 1.5F, 1F);
                        entity.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 300, 0, false, false, true), this.entity);
                    });
                }
                if (tick >= 18 && tick <= 36 && tick % 6 == 0) {
                    entity.playSound(SoundInit.RELIC_OBSERVER_ELECTRIC_PULSE.get(), 1F, 1.2F);
                }
            }
        });
        this.goalSelector.addGoal(1, new AnimationAI<>(this) {
            private Vec3 targetPos;

            @Override
            protected boolean test(Animation animation) {
                return animation == entity.shootLaserAnimation;
            }

            @Override
            public void stop() {
                super.stop();
                targetPos = null;
            }

            @Override
            public void tick() {
                LivingEntity target = entity.getTarget();
                entity.setDeltaMovement(0, this.entity.getDeltaMovement().y, 0);
                int tick = entity.getAnimationTick();
                if (target != null) {
                    if (tick < 22 || tick > 35) {
                        entity.lookAt(target, 360F, 180F);
                        entity.getLookControl().setLookAt(target, 360F, 180F);
                        targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
                    } else if (this.targetPos != null) entity.getLookControl().setLookAt(targetPos);
                    else entity.setYRot(this.entity.yRotO);
                }
                if (tick == 5) {
                    double px = this.entity.getX();
                    double py = this.entity.getY() + this.entity.getEyeHeight();
                    double pz = this.entity.getZ();
                    EntityGuardianLaser laser = new EntityGuardianLaser(this.entity.level(), this.entity, px, py, pz, 5);
                    this.entity.level().addFreshEntity(laser);
                } else if (tick == 28) {
                    this.entity.playSound(SoundInit.RELIC_OBSERVER_ELECTROMAGNETIC.get(), 0.5F, this.entity.getVoicePitch());
                }
            }
        });
        this.goalSelector.addGoal(7, new ObserverLookAtTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        this.rotControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level().isClientSide) {
            LivingEntity target = this.getTarget();
            if (target != null && !target.isAlive()) this.setTarget(null);
            if (!this.isNoAi() && this.isActive() && target != null) {
                int moveDistance = this.timeUntilShootLaser == 0 ? 8 : 3;
                if (this.targetDistance <= moveDistance && this.getAnimation() != this.stormAnimation) this.getNavigation().stop();
                else this.getNavigation().moveTo(target, 1D);
                if (this.isNoAnimation() && this.timeUntilStorm <= 0 && this.targetDistance <= 3.5) {
                    this.playAnimation(this.stormAnimation);
                    this.timeUntilStorm = this.level().getDifficulty() == Difficulty.HARD ? HARD_INTERVAL.sample(this.random) : NORMAL_INTERVAL.sample(this.random);
                } else if (this.isNoAnimation() && this.timeUntilShootLaser <= 0 /*&& Math.abs(this.getY() - target.getY()) < 2*/) {
                    this.playAnimation(this.shootLaserAnimation);
                    this.timeUntilShootLaser = this.level().getDifficulty() == Difficulty.HARD ? HARD_INTERVAL.sample(this.random) : NORMAL_INTERVAL.sample(this.random);
                }
            }
        } else {
            Animation animation = this.getAnimation();
            int tick = this.getAnimationTick();
            if (animation == this.shootLaserAnimation) {
                if (tick == 5) {
                    AdvancedParticleBase.spawnParticle(level(), ParticleInit.GLOW.get(), this.getX(), this.getY(), this.getZ(),
                            0, 0, 0, true, 0, 0, 0, 0,
                            14, 0.56, 0.78, 0.86, 1, 1, 28, true, true, false,
                            new ParticleComponent[]{
                                    new PropertyControl(EnumParticleProperty.ALPHA, new KeyTrack(new float[]{0.0f, 0.6f, 0.6f, 0.0f}, new float[]{0f, 0.3f, 0.8f, 1f}), false),
                                    new PinLocationWithEntity(this, new Vec3(0, this.getEyeHeight(), 0))
                            });
                } else if (tick == 26) {
                    this.doShotLaserEffect();
                    this.doFractalEffect(3 + this.random.nextInt(3));
                }
            } else if (animation == this.stormAnimation) {
                if (tick == 20) {
                    AdvancedParticleBase.spawnParticle(level(), ParticleInit.GLOW.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 14, 0.56, 0.78, 0.86, 0.8, 1, 26, true, true, false, new ParticleComponent[]{new PinLocationWithEntity(this, new Vec3(0, this.getEyeHeight(), 0)), new PropertyControl(EnumParticleProperty.ALPHA, new AnimData.Oscillator(0.05f, 0.5f, 15f, 0), true), new PropertyControl(EnumParticleProperty.SCALE, new AnimData.Oscillator(25f, 5f, 22f, Mth.PI / 3), false)});
                }
                if (tick >= 20 && tick <= 45) {
                    if (this.random.nextFloat() < 0.6) this.doFractalEffect(1 + this.random.nextInt(2));
                    if (tick % 5 == 0) {
                        AdvancedParticleBase.spawnParticle(level(), ParticleInit.BIG_RING.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, false, 0, Math.PI / 2F, 0, 0, 1, 0.56, 0.78, 0.86, 0.8, 1, 10, true, true, false, new ParticleComponent[]{new PinLocationWithEntity(this, new Vec3(0, this.getEyeHeight(), 0)), new PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1F, 0F), false), new PropertyControl(EnumParticleProperty.SCALE, KeyTrack.startAndEnd(5F, 35F), false), new PropertyControl(EnumParticleProperty.YAW, new KeyTrack(new float[]{0F, 0.5F}, new float[]{0F, Mth.PI * 2F}), true),});
                    }
                }
            }
            this.rotControlled.incrementOrDecreaseTimer(animation == this.stormAnimation);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.75D, 1.0D));
        }
        if (!this.level().isClientSide) {
            if (this.timeUntilStorm > 0) {
                this.timeUntilStorm--;
            }
            if (this.timeUntilShootLaser > 0) {
                this.timeUntilShootLaser--;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else {
            boolean isProjectileSource = ModEntityUtils.isProjectileSource(source);
            if ((!this.isActive() || this.getAnimation() == this.stormAnimation) && isProjectileSource) {
                return false;
            }
            if (source.getDirectEntity() instanceof LivingEntity livingEntity) {
                if (livingEntity.getMainHandItem().getItem() instanceof PickaxeItem) {
                    damage *= 1.5F;
                }
            }
            if (isProjectileSource) {
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
        return this.animations;
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
    }

    @Override
    public Animation getHurtAnimation() {
        return this.hurtAnimation;
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

    @Override
    protected float activeRange() {
        return 8F;
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

    private void doHurtEffect() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; ++i) {
                double dx = getRandomX(0.5);
                double dy = this.getEyeY();
                double dz = getRandomZ(0.5);
                level().addParticle(DEEPSLATE_BRICKS, dx, dy, dz, -getDeltaMovement().x() * 0.25F, -getDeltaMovement().y() * 0.25F, -getDeltaMovement().z() * 0.25F);
            }
        }
    }

    private void doFractalEffect(int count) {
        if (this.level().isClientSide) {
            for (int i = 0; i < count; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double minHorizontalOffset = 1;
                double maxHorizontalOffset = 3;
                double minVerticalOffset = 0;
                double maxVerticalOffset = 1.5;
                double horizontalRadius = minHorizontalOffset + (random.nextDouble() * (maxHorizontalOffset - minHorizontalOffset));
                double offsetX = Math.cos(angle) * horizontalRadius;
                double offsetZ = Math.sin(angle) * horizontalRadius;
                double offsetY = minVerticalOffset + (random.nextDouble() * (maxVerticalOffset - minVerticalOffset));
                this.doFractalEffect(this.position().add(0, this.getEyeHeight(), 0), this.position().add(offsetX, this.getEyeHeight() + offsetY, offsetZ));
            }
        }
    }

    private void doShotLaserEffect() {
        if (this.level().isClientSide) {
            float yawRad = (float) Math.toRadians(this.getYRot());
            float pitchRad = (float) Math.toRadians(this.getXRot());
            double x = -Math.sin(yawRad) * Math.cos(pitchRad);
            double y = -Math.sin(pitchRad);
            double z = Math.cos(yawRad) * Math.cos(pitchRad);
            Vec3 pos = this.position().add(0, this.getEyeHeight(), 0);
            for (int i = 0; i < 3; i++) {
                double d0 = 0.1 + i * 0.2;
                AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.BIG_RING.get(), pos.x, pos.y, pos.z
                        , x * d0, y * d0, z * d0, false, (float) Math.toRadians(-this.getYRot() + 180), (float) Math.toRadians(-this.getXRot()), 0, 0, 1F,
                        0.8, 0.8, 0.8, 1, 1, 7, true, false, false
                        , new ParticleComponent[]{
                                new PropertyControl(EnumParticleProperty.SCALE, AnimData.startAndEnd(4, 10 + i * 2.5F), false),
                                new PropertyControl(EnumParticleProperty.ALPHA, AnimData.startAndEnd(0.8F, 0F), false)
                        });
            }
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 18.0D).add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ARMOR, 10.0D);
    }

    static class NoneRotationControl extends BodyRotationControl {
        public NoneRotationControl(Mob mob) {
            super(mob);
        }

        public void clientTick() {
        }
    }

    static class ObserverLookAtTargetGoal extends Goal {
        private final EntityRelicObserver entity;

        public ObserverLookAtTargetGoal(EntityRelicObserver entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (target != null && target.isAlive()) {
                if (target.distanceToSqr(this.entity) < 1024.0D) {
                    double d1 = target.getX() - this.entity.getX();
                    double d2 = target.getZ() - this.entity.getZ();
                    this.entity.setYRot(-((float) Mth.atan2(d1, d2)) * (180F / (float) Math.PI));
                    this.entity.yBodyRot = this.entity.getYRot();
                }
            } else {
                Vec3 vec3 = this.entity.getDeltaMovement();
                this.entity.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI));
                this.entity.yBodyRot = this.entity.getYRot();
            }
        }
    }
}
