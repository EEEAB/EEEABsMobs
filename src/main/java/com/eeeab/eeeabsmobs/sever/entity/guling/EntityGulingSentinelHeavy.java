package com.eeeab.eeeabsmobs.sever.entity.guling;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationMeleePlusAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityElectromagnetic;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGrenade;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityGulingSentinelHeavy extends EntityAbsGuling implements IEntity, GlowEntity, RangedAttackMob {
    public final Animation dieAnimation = Animation.create(60);
    public final Animation activeAnimation = Animation.create(30);
    public final Animation deactivateAnimation = Animation.create(30);
    public final Animation attackAnimationLeft = Animation.create(25);
    public final Animation attackAnimationRight = Animation.create(25);
    public final Animation smashAttackAnimation = Animation.create(30);
    public final Animation rangeAttackAnimation = Animation.create(100);
    public final Animation rangeAttackStopAnimation = Animation.create(10);
    public final Animation electromagneticAnimation = Animation.create(100);
    private final Animation[] animations = new Animation[]{
            dieAnimation,
            activeAnimation,
            deactivateAnimation,
            attackAnimationLeft,
            attackAnimationRight,
            smashAttackAnimation,
            rangeAttackAnimation,
            rangeAttackStopAnimation,
            electromagneticAnimation
    };
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityGulingSentinelHeavy.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ALWAYS_ACTIVE = SynchedEntityData.defineId(EntityGulingSentinelHeavy.class, EntityDataSerializers.BOOLEAN);
    private int deactivateTick;
    private int smashAttackTick;
    private int rangeAttackTick;
    private int electromagneticTick;
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);
    public final ControlledAnimation hotControlled = new ControlledAnimation(20);
    public final ControlledAnimation electromagneticConControlled = new ControlledAnimation(20);
    private static final int RANGE_ATTACK_TICK = 400;
    private static final int SMASH_ATTACK_TICK = 300;
    private static final int ELECTROMAGNETIC_TICK = 450;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] hand;//0:left 1:right

    public EntityGulingSentinelHeavy(EntityType<? extends EntityAbsGuling> type, Level level) {
        super(type, level);
        this.active = false;
        this.dropAfterDeathAnim = false;
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        if (this.level().isClientSide) {
            this.hand = new Vec3[]{new Vec3(0, 0, 0), new Vec3(0, 0, 0)};
        }
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_ELITE;
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
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive() && !this.isActive();
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
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.GULING.GULING_SENTINEL_HEAVY.combatConfig;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.smashAttackAnimation) {
                this.smashAttackTick = SMASH_ATTACK_TICK;
            } else if (animation == this.rangeAttackAnimation) {
                this.rangeAttackTick = RANGE_ATTACK_TICK;
            } else if (animation == this.electromagneticAnimation) {
                this.electromagneticTick = ELECTROMAGNETIC_TICK;
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityAbsGuling.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, 0, false, false, (golem) -> !(golem instanceof Shulker)));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D)) {
            @Override
            public boolean canUse() {
                return super.canUse() && EntityGulingSentinelHeavy.this.active;
            }};    
        this.goalSelector.addGoal(6, new EMLookAtGoal(this, Mob.class, 6.0F));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && EntityGulingSentinelHeavy.this.active;
            }
        });
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> activeAnimation));
        this.goalSelector.addGoal(1, new AnimationDeactivate<>(this, () -> deactivateAnimation));
        this.goalSelector.addGoal(1, new GSHMeleeAttackGoal(this));
        this.goalSelector.addGoal(1, new GSHRangeAttackGoal(this));
        this.goalSelector.addGoal(1, new GSHElectromagneticAttackGoal(this));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, () -> smashAttackAnimation, 7F, 18, 1.5F, 1.5F, true) {
            @Override
            public void tick() {
                 if (alwaysActive()) {
            setActive(true);
               }
                this.entity.setDeltaMovement(0F, 0F, 0F);
                LivingEntity target = this.entity.getTarget();
                if (target != null) {
                    if (this.entity.getAnimationTick() < 15) {
                        this.entity.getLookControl().setLookAt(target, 30F, 30F);
                    } else {
                        this.entity.setYRot(this.entity.yRotO);
                    }
                }
                super.tick();
            }

            @Override
            protected void onHit(LivingEntity entity) {
                if (!this.entity.hotControlled.isStop()) {
                    entity.setSecondsOnFire(3);
                }
            }
        });
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> rangeAttackAnimation));
        this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.0, 40, () -> attackAnimationLeft, () -> attackAnimationRight));
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
        EMAnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level().isClientSide) {
            if (this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);
            if (!this.isNoAi() && !this.isActive()) {
                if (EMConfigHandler.COMMON.MOB.GULING.GULING_SENTINEL_HEAVY.enableNonCombatHeal.get()) this.heal(0.5F);
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
                this.yHeadRot = this.yBodyRot = this.getYRot();
            }
            if (!this.isNoAi() && !this.isActive() && this.isNoAnimation() && this.getTarget() != null && this.targetDistance <= 12) {
                this.playSound(SoundInit.GSH_FRICTION.get());
                this.playAnimation(this.activeAnimation);
                this.setActive(true);
            }     else if (this.alwaysActive() && !this.isActive()) {
                this.setActive(true);  
            }
            if (!this.isNoAi() && this.isActive() && this.isAlive() && this.isNoAnimation() && this.getTarget() == null && this.deactivateTick >= 300) {
                this.playSound(SoundInit.GSH_FRICTION.get());
                this.playAnimation(this.deactivateAnimation);
                this.setActive(false);
            }
            if (!this.isNoAi() && this.isActive() && this.isNoAnimation() && this.smashAttackTick <= 0 && this.getTarget() != null && ((targetDistance <= 6.5F && ModEntityUtils.checkTargetComingCloser(this, this.getTarget())) || this.targetDistance < 6.0F)) {
                this.playAnimation(this.smashAttackAnimation);
            }
            if (!this.isNoAi() && this.isActive() && this.isNoAnimation() && this.getTarget() != null && this.rangeAttackTick <= 0 && Math.pow(this.targetDistance, 2.0) > this.getMeleeAttackRangeSqr(this.getTarget()) + 5) {
                this.playAnimation(this.rangeAttackAnimation);
            }
            if (!this.isNoAi() && this.isActive() && this.isNoAnimation() && this.getTarget() != null && this.electromagneticTick <= 0 && (this.getHealthPercentage() <= 80 || this.tickCount > 1200) && this.targetDistance < 6.5F) {
                this.playAnimation(this.electromagneticAnimation);
            }
        }

        this.pushEntitiesAway(1.8F, getBbHeight(), 1.8F, 1.8F);

        int tick = this.getAnimationTick();
        if (this.getAnimation() == this.smashAttackAnimation) {
            if (tick == 1) {
                this.playSound(SoundInit.GSH_PRE_ATTACK.get(), 0.5F, this.getVoicePitch());
            } else if (tick == 18) {
                this.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + this.random.nextFloat() * 0.1F);
                BlockState block = this.level().getBlockState(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 0.2), Mth.floor(this.getZ())));
                ModParticleUtils.annularParticleOutburstOnGround(level(), new BlockParticleOption(ParticleTypes.BLOCK, block), this, 16, 8, 3, 0.5, 0, 0.1);
                if (!this.hotControlled.isStop())
                    ModParticleUtils.annularParticleOutburstOnGround(level(), ParticleTypes.SOUL_FIRE_FLAME, this, 6, 6, 2.5, 0.5, 0, 0.15);
            }
        } else if (this.getAnimation() == this.rangeAttackAnimation) {
            if (tick >= 20 && tick % 20 == 0) {
                this.hotControlled.increaseTimer(5);
            }
        } else if (this.getAnimation() == this.attackAnimationLeft || this.getAnimation() == this.attackAnimationRight) {
            if (tick == 9 || tick == 17) {
                this.playSound(SoundInit.GSH_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            }
        } else if (this.getAnimation() == this.dieAnimation) {
            if (tick <= 15) {
                LivingEntity target = this.getTarget();
                if (target != null) {
                    this.lookAt(target, 30F, 30F);
                    this.getLookControl().setLookAt(target, 30F, 30F);
                }
            } else {
                this.setYRot(this.yRotO);
            }
            if (tick == 38) {
                float range = 5F;
                float attackArc = 120F;
                List<LivingEntity> entities = this.getNearByLivingEntities(range - 0.5F, 3F, range - 0.5F, range - 0.5F);
                for (LivingEntity hitEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this, hitEntity);
                    float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - this.getZ()) * (hitEntity.getZ() - this.getZ()) + (hitEntity.getX() - this.getX()) * (hitEntity.getX() - this.getX())) - hitEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range - 0.5F && (entityRelativeAngle <= attackArc / 2F && entityRelativeAngle >= -attackArc / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        this.gshHurtTarget(hitEntity, 3F, true);
                        double ratioX = Math.sin(this.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(this.getYRot() * ((float) Math.PI / 180F)));
                        ModEntityUtils.forceKnockBack(hitEntity, 0.5F, ratioX, ratioZ, 1.0F, false);
                    }
                }
            } else if (tick == 39) {
                boolean hot = !this.hotControlled.isStop();
                EntityCameraShake.cameraShake(level(), position(), 15, 0.125F, 0, 10);
                if (this.level().isClientSide) {
                    ParticleDust.DustData dustData = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.24f, 0.24f, 0.24f, 40f, 25, ParticleDust.EnumDustBehavior.GROW, 1.0f);
                    ParticleOptions[] options;
                    if (hot) {
                        options = new ParticleOptions[]{dustData, ParticleTypes.SOUL_FIRE_FLAME};
                    } else {
                        options = new ParticleOptions[]{dustData};
                    }
                    ModParticleUtils.annularParticleOutburst(level(), 15, options, getX(), this.getY(), getZ(), 0.5F, 0.1);
                }
            }
        } else if (this.getAnimation() == this.electromagneticAnimation) {
            this.setDeltaMovement(0, this.onGround() ? 0 : this.getDeltaMovement().y, 0);
            if (tick == 1) {
                this.playSound(SoundInit.GSH_PRE_ATTACK.get(), 0.75F, 0.5F);
            } else if (tick > 20 && tick < 90) {
                this.electromagneticConControlled.increaseTimer();
                if (tick == 42 || tick == 64 || tick == 86) {
                    this.playSound(SoundInit.GSH_ELECTROMAGNETIC.get(), 1F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.electromagneticConControlled.decreaseTimer(10);
                }
            } else {
                this.electromagneticConControlled.decreaseTimer(2);
            }
        }

        if (this.isActive() && this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F && this.random.nextInt(5) == 0) {
            this.doWalkEffect(1);
        }
        if (this.isActive() && this.isAlive()) {
            if (this.level().isClientSide && this.tickCount % 2 == 0 && this.random.nextInt(100) == 0) {
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), this.getRandomX(1.5D), this.getY(0.5F), this.getRandomZ(1.5D), 0.0D, 0.07D, 0.0D);
            }
            if (this.level().isClientSide && !this.hotControlled.isStop()) {
                if (this.hand != null && this.hand.length > 0) {
                    Vec3 left = this.hand[0];
                    Vec3 right = this.hand[1];
                    if (left != null && right != null) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        if (this.random.nextInt(100) == 0) {
                            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, left.x, left.y, left.z, d0, d1, d2);
                            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, right.x, right.y, right.z, d0, d1, d2);
                        } else {
                            this.level().addParticle(ParticleTypes.SMOKE, left.x, left.y, left.z, d0, d1, d2);
                            this.level().addParticle(ParticleTypes.SMOKE, right.x, right.y, right.z, d0, d1, d2);
                        }
                    }
                }
            }
        }

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        if (this.level().isClientSide && speed > 0.05 && this.isActive() && !this.isSilent()) {
            if (this.frame % 10 == 1 && (this.isNoAnimation() || this.getAnimation() == this.rangeAttackAnimation)) {
                this.level().playLocalSound(getX(), getY(), getZ(), SoundInit.GSH_STEP.get(), this.getSoundSource(), 1F, 1F, false);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.rangeAttackTick > 0) {
                this.rangeAttackTick--;
            }

            if (this.smashAttackTick > 0) {
                this.smashAttackTick--;
            }

            if (this.electromagneticTick > 0) {
                this.electromagneticTick--;
            }

            if (this.isActive()) {
                if (!this.alwaysActive() && this.getTarget() == null || (!this.alwaysActive() && this.tickCount % 2 == 0 && !this.getSensing().hasLineOfSight(this.getTarget()))) {
                    this.deactivateTick++;
                } else if (this.deactivateTick > 0) {
                    this.deactivateTick--;
                }
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
            if (!source.is(EMTagKey.GENERAL_UNRESISTANT_TO)) {
                if (this.getAnimation() == this.electromagneticAnimation) {
                    damage *= 0.2F;
                } else {
                    damage *= 0.8F;
                }
            }
        }
        return super.hurt(source, damage);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, false);
        this.entityData.define(ALWAYS_ACTIVE, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_ACTIVE, compound.getBoolean("isActive"));
        this.active = this.isActive();
        this.entityData.set(ALWAYS_ACTIVE, compound.getBoolean("alwaysActive"));
        this.active = this.alwaysActive();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isActive", this.entityData.get(DATA_ACTIVE));
        compound.putBoolean("alwaysActive", this.entityData.get(ALWAYS_ACTIVE));
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.GSH_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundInit.GSH_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.GSH_IDLE.get();
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
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 150.0D).
                add(Attributes.ARMOR, 15.0D).
                add(Attributes.ATTACK_DAMAGE, 8.0D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.MOVEMENT_SPEED, 0.28D).
                add(ForgeMod.ENTITY_GRAVITY.get(), 0.125D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public boolean isGlow() {
        return this.isActive() && this.getHealth() > 0;
    }

    /**
     * Attack the specified entity using a ranged attack.
     *
     * @param target
     * @param velocity
     */
    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        double d1 = target.getX() - this.getX();
        double d2 = target.getY(-0.5F) - this.getY();
        double d3 = target.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.12F;
        this.playSound(SoundInit.GSH_SPARK.get());
        for (int i = 1; i <= 2; i++) {
            double yBodyRadians = Math.toRadians(this.yBodyRot + (180 * (i - 1)));
            EntityGrenade grenade = new EntityGrenade(this.level(), this);
            grenade.shoot(d1, d2 + d4, d3, velocity, 3F);
            Vec3 vec3 = this.position().add(this.getLookAngle());
            grenade.setPos(vec3.x + this.getBbWidth() * 0.8F * Math.cos(yBodyRadians), this.getY(0.45D), vec3.z + this.getBbWidth() * 0.8F * Math.sin(yBodyRadians));
            this.level().addFreshEntity(grenade);
        }
    }

    public void gshHurtTarget(LivingEntity hitEntity, float damageMultiplier, boolean disableShield) {
        double baseDamage = this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = hitEntity.hurt(this.damageSources().mobAttack(this), (float) (baseDamage * damageMultiplier));
        boolean hot = !this.hotControlled.isStop();
        if (flag) {
            hitEntity.invulnerableTime = 0;
            if (hot) hitEntity.setSecondsOnFire(5);
        } else if (disableShield && hitEntity instanceof Player player && player.isBlocking()) {
            player.disableShield(true);
        }
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
        this.deactivateTick = 0;
    }
            public boolean alwaysActive() {
        return this.entityData.get(ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean alwaysActive) {
        this.entityData.set(ALWAYS_ACTIVE, alwaysActive);
    }


    static class GSHMeleeAttackGoal extends AnimationAI<EntityGulingSentinelHeavy> {

        protected GSHMeleeAttackGoal(EntityGulingSentinelHeavy entity) {
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
            this.entity.setDeltaMovement(0F, 0F, 0F);
            if (!(tick < 15 && tick > 10) && target != null) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 10) {
                final float attackArc = 30F;
                final float range = 6F;
                List<LivingEntity> entities = entity.getNearByLivingEntities(range, this.entity.getBbHeight(), range, range);
                for (LivingEntity livingEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, livingEntity);
                    float entityHitDistance = (float) Math.sqrt((livingEntity.getZ() - entity.getZ()) * (livingEntity.getZ() - entity.getZ()) + (livingEntity.getX() - entity.getX()) * (livingEntity.getX() - entity.getX())) - livingEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= attackArc / 2F && entityRelativeAngle >= -attackArc / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                        entity.gshHurtTarget(livingEntity, 1F, true);
                        double ratioX = -Math.sin(entity.yBodyRot * ((float) Math.PI / 180F));
                        double ratioZ = Math.cos(entity.yBodyRot * ((float) Math.PI / 180F));
                        ModEntityUtils.forceKnockBack(livingEntity, 0.8F, ratioX, ratioZ, 1.5F, false);
                    }
                }
            } else if (tick == 18) {
                float leftArc = 45F;
                float rightArc = 45F;
                if (this.entity.getAnimation() == this.entity.attackAnimationRight) {
                    rightArc = 90F;
                } else {
                    leftArc = 90F;
                }
                final float range = 4.5F;
                List<LivingEntity> entities = entity.getNearByLivingEntities(range);
                for (LivingEntity livingEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, livingEntity);
                    float entityHitDistance = (float) Math.sqrt((livingEntity.getZ() - entity.getZ()) * (livingEntity.getZ() - entity.getZ()) + (livingEntity.getX() - entity.getX()) * (livingEntity.getX() - entity.getX())) - livingEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= rightArc / 2F && entityRelativeAngle >= -leftArc / 2F) || (entityRelativeAngle >= 360 - 90F / 2F || entityRelativeAngle <= -360 + 90F / 2F))) {
                        entity.gshHurtTarget(livingEntity, 1.25F, false);
                    }
                }
            }
        }
    }

    static class GSHRangeAttackGoal extends AnimationAI<EntityGulingSentinelHeavy> {
        private int lostTargetDelay;

        public GSHRangeAttackGoal(EntityGulingSentinelHeavy entity) {
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
            if (target != null && target.isAlive() && this.lostTargetDelay < 10) {
                if (!this.entity.getSensing().hasLineOfSight(target)) {
                    this.lostTargetDelay += 2;
                }
                this.moveGoal(target);
                this.entity.getLookControl().setLookAt(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 30F, 30F);
            } else {
                this.entity.playAnimation(this.entity.rangeAttackStopAnimation);
                return;
            }
            int tick = this.entity.getAnimationTick();
            if (tick % 20 == 0 && tick > 10) {
                this.entity.performRangedAttack(target, 1F);
            }
        }

        private void moveGoal(LivingEntity target) {
            double moveSpeed = this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.75;
            double distance = this.entity.distanceToSqr(target);
            if (distance < Math.pow(6, 2)) {
                Vec3 vec3 = findTargetPoint(target, this.entity);
                this.entity.setDeltaMovement(vec3.x * moveSpeed, this.entity.getDeltaMovement().y, vec3.z * moveSpeed);
            } else if (distance > Math.pow(12, 2)) {
                Vec3 vec3 = findTargetPoint(this.entity, target);
                this.entity.setDeltaMovement(vec3.x * moveSpeed, this.entity.getDeltaMovement().y, vec3.z * moveSpeed);
            } else {
                this.entity.setDeltaMovement(0F, 0F, 0F);
            }
        }

        public static Vec3 findTargetPoint(Entity entity, Entity target) {
            Vec3 vec3 = target.position();
            return (new Vec3(vec3.x - entity.getX(), 0.0, vec3.z - entity.getZ())).normalize();
        }
    }

    static class GSHElectromagneticAttackGoal extends AnimationAI<EntityGulingSentinelHeavy> {

        public GSHElectromagneticAttackGoal(EntityGulingSentinelHeavy entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == this.entity.electromagneticAnimation;
        }

        @Override
        public void tick() {
            int tick = entity.getAnimationTick();
            if (!this.entity.level().isClientSide) {
                if (tick == 43 || tick == 65 || tick == 87) {
                    final int count = 10;
                    float offset = (float) Math.toRadians(this.entity.getRandom().nextGaussian() * 360 - 180);
                    float amount = (float) (this.entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.25F);
                    for (int i = 0; i < count; ++i) {
                        float f1 = (float) (this.entity.getYRot() + (i + offset) * (float) Math.PI * (2.0 / count));
                        EntityElectromagnetic.shoot(this.entity.level(), this.entity, amount, 2.0F, count + 2, 3, (f1 * (180F / (float) Math.PI)) - 90F);
                    }
                }
            }
        }
    }
}
