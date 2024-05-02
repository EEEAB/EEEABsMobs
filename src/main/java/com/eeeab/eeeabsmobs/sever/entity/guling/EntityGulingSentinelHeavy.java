package com.eeeab.eeeabsmobs.sever.entity.guling;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.block.BlockSignalTarp;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationActivateGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationDeactivateGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationDieGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationFullRangeAttackGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationAbstractGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationMeleeAIPlusGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGrenade;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
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
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.*;
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

import java.util.List;

public class EntityGulingSentinelHeavy extends EntityAbsGuling implements IEntity, GlowEntity, RangedAttackMob {
    public static final Animation DIE_ANIMATION = Animation.create(40);
    public static final Animation ACTIVE_ANIMATION = Animation.create(30);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(30);
    public static final Animation ATTACK_ANIMATION_LEFT = Animation.create(25);
    public static final Animation ATTACK_ANIMATION_RIGHT = Animation.create(25);
    public static final Animation SMASH_ATTACK_ANIMATION = Animation.create(30);
    public static final Animation RANGE_ATTACK_ANIMATION = Animation.create(100);
    public static final Animation RANGE_ATTACK_END_ANIMATION = Animation.create(10);
    public static final Animation ELECTROMAGNETIC_ANIMATION = Animation.create(80);
    private static final Animation[] ANIMATIONS = new Animation[]{
            DIE_ANIMATION,
            ACTIVE_ANIMATION,
            DEACTIVATE_ANIMATION,
            ATTACK_ANIMATION_LEFT,
            ATTACK_ANIMATION_RIGHT,
            SMASH_ATTACK_ANIMATION,
            RANGE_ATTACK_ANIMATION,
            RANGE_ATTACK_END_ANIMATION,
            ELECTROMAGNETIC_ANIMATION
    };
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityGulingSentinelHeavy.class, EntityDataSerializers.BOOLEAN);
    private int deactivateTick;
    private int smashAttackTick;
    private int rangeAttackTick;
    private int electromagneticTick;
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);
    public final ControlledAnimation hotControlled = new ControlledAnimation(20);
    private static final int RANGE_ATTACK_TICK = 350;
    private static final int SMASH_ATTACK_TICK = 300;
    private static final int ELECTROMAGNETIC_TICK = 450;
    @OnlyIn(Dist.CLIENT)
    public Vec3[] hand;//0:left 1:right

    public EntityGulingSentinelHeavy(EntityType<? extends EntityAbsGuling> type, Level level) {
        super(type, level);
        this.active = false;
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        if (this.level.isClientSide) {
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
        if (!this.level.isClientSide) {
            if (animation == SMASH_ATTACK_ANIMATION) {
                this.smashAttackTick = SMASH_ATTACK_TICK;
            } else if (animation == RANGE_ATTACK_ANIMATION) {
                this.rangeAttackTick = RANGE_ATTACK_TICK;
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityAbsGuling.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, 0, false, false, (golem) -> !(golem instanceof Shulker)));
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
        this.goalSelector.addGoal(1, new AnimationDieGoal<>(this));
        this.goalSelector.addGoal(1, new AnimationActivateGoal<>(this, ACTIVE_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationDeactivateGoal<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.addGoal(1, new GSHMeleeAttackGoal(this));
        this.goalSelector.addGoal(1, new GSHRangeAttackGoal(this));
        this.goalSelector.addGoal(1, new GSHElectromagneticAttackGoal(this));
        this.goalSelector.addGoal(1, new AnimationFullRangeAttackGoal<>(this, SMASH_ATTACK_ANIMATION, 7F, 18, 1.5F, 2F, true) {
            @Override
            public void tick() {
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
        this.goalSelector.addGoal(1, new AnimationCommonGoal<>(this, RANGE_ATTACK_END_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationMeleeAIPlusGoal<>(this, 1.0, 15, ATTACK_ANIMATION_LEFT, ATTACK_ANIMATION_RIGHT));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return (!this.isActive() || animation == ACTIVE_ANIMATION || animation == DEACTIVATE_ANIMATION) || super.isInvulnerableTo(damageSource);
    }

    @Override
    public void tick() {
        super.tick();
        this.glowControlled.updatePrevTimer();
        this.hotControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level.isClientSide) {
            if (this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);
            if (!this.isNoAi() && !this.isActive()) {
                if (EMConfigHandler.COMMON.MOB.GULING.GULING_SENTINEL_HEAVY.enableNonCombatHeal.get()) this.heal(0.5F);
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
                this.yHeadRot = this.yBodyRot = this.getYRot();
            }
            if (!this.isNoAi() && !this.isActive() && this.getAnimation() == NO_ANIMATION && this.getTarget() != null && this.targetDistance <= 12) {
                this.playSound(SoundInit.GSH_FRICTION.get());
                this.playAnimation(ACTIVE_ANIMATION);
                this.setActive(true);
            }
            if (!this.isNoAi() && this.isActive() && this.getAnimation() == NO_ANIMATION && this.getTarget() == null && this.deactivateTick >= 300) {
                this.playSound(SoundInit.GSH_FRICTION.get());
                this.playAnimation(DEACTIVATE_ANIMATION);
                this.setActive(false);
            }
            if (!this.isNoAi() && this.isActive() && this.getAnimation() == NO_ANIMATION && this.smashAttackTick <= 0 && this.getTarget() != null && ((targetDistance <= 6.5F && ModEntityUtils.checkTargetComingCloser(this, this.getTarget())) || this.targetDistance < 6.0F)) {
                this.playAnimation(SMASH_ATTACK_ANIMATION);
            }
            if (!this.isNoAi() && this.isActive() && this.getAnimation() == NO_ANIMATION && this.getTarget() != null && this.rangeAttackTick <= 0 && Math.pow(this.targetDistance, 2.0) > this.getMeleeAttackRangeSqr(this.getTarget()) + 5) {
                this.playAnimation(RANGE_ATTACK_ANIMATION);
            }
        }

        this.pushEntitiesAway(1.8F, getBbHeight(), 1.8F, 1.8F);

        int tick = this.getAnimationTick();
        if (getAnimation() == SMASH_ATTACK_ANIMATION) {
            if (tick == 1) {
                this.playSound(SoundInit.GSH_PRE_ATTACK.get(), 0.5F, this.getVoicePitch());
            } else if (tick == 18) {
                this.playSound(SoundEvents.GENERIC_EXPLODE, 1.25F, 1F + this.random.nextFloat() * 0.1F);
                BlockState block = this.level.getBlockState(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY() - 0.2), Mth.floor(this.getZ())));
                ModParticleUtils.annularParticleOutburstOnGround(level, new BlockParticleOption(ParticleTypes.BLOCK, block), this, 16, 8, 3, 0.5, 0, 0.1);
                if (!this.hotControlled.isStop())
                    ModParticleUtils.annularParticleOutburstOnGround(level, ParticleTypes.SOUL_FIRE_FLAME, this, 6, 6, 2.5, 0.5, 0, 0.15);
            }
        } else if (getAnimation() == RANGE_ATTACK_ANIMATION) {
            if (tick >= 20 && tick % 20 == 0) {
                this.hotControlled.increaseTimer(5);
            }
        } else if (getAnimation() == ATTACK_ANIMATION_LEFT || getAnimation() == ATTACK_ANIMATION_RIGHT) {
            if (tick == 9 || tick == 17) {
                this.playSound(SoundInit.GSH_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            }
        }

        if (this.isActive() && this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F && this.random.nextInt(5) == 0) {
            this.doWalkEffect(1);
        }
        if (this.isActive() && this.isAlive()) {
            if (this.level.isClientSide && this.tickCount % 2 == 0 && this.random.nextInt(100) == 0) {
                this.level.addParticle(ParticleInit.GUARDIAN_SPARK.get(), this.getRandomX(1.5D), this.getY(0.5F), this.getRandomZ(1.5D), 0.0D, 0.07D, 0.0D);
            }
            if (this.level.isClientSide && !this.hotControlled.isStop()) {
                if (this.hand != null && this.hand.length > 0) {
                    Vec3 left = this.hand[0];
                    Vec3 right = this.hand[1];
                    if (left != null && right != null) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        if (this.random.nextInt(100) == 0) {
                            this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, left.x, left.y, left.z, d0, d1, d2);
                            this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, right.x, right.y, right.z, d0, d1, d2);
                        } else {
                            this.level.addParticle(ParticleTypes.SMOKE, left.x, left.y, left.z, d0, d1, d2);
                            this.level.addParticle(ParticleTypes.SMOKE, right.x, right.y, right.z, d0, d1, d2);
                        }
                    }
                }
            }
        }

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        if (this.level.isClientSide && speed > 0.05 && this.isActive() && !this.isSilent()) {
            if (this.frame % 10 == 1 && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == RANGE_ATTACK_ANIMATION)) {
                this.level.playLocalSound(getX(), getY(), getZ(), SoundInit.GSH_STEP.get(), this.getSoundSource(), 1F, 1F, false);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
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
                if (this.getTarget() == null || (this.tickCount % 2 == 0 && !this.getSensing().hasLineOfSight(this.getTarget()))) {
                    this.deactivateTick++;
                } else if (this.deactivateTick > 0) {
                    this.deactivateTick--;
                }
            }
        }
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());

        if (getAnimation() != RANGE_ATTACK_ANIMATION) {
            if (tickCount % 10 == 0) this.hotControlled.decreaseTimer();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level.isClientSide) {
            return false;
        } else {
            if (!(source == DamageSource.OUT_OF_WORLD || source == DamageSource.GENERIC)) {
                damage *= 0.8F;//减少20%伤害
            }
        }
        return super.hurt(source, damage);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_ACTIVE, compound.getBoolean("isActive"));
        this.active = this.isActive();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isActive", this.entityData.get(DATA_ACTIVE));
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
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
        if (this.isActive() && this.getAnimation() != ACTIVE_ANIMATION) {
            super.playAmbientSound();
        }
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 150.0D).
                add(Attributes.ARMOR, 20.0D).
                add(Attributes.ATTACK_DAMAGE, 10.0D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.MOVEMENT_SPEED, 0.28D).
                add(ForgeMod.ENTITY_GRAVITY.get(), 0.12D).
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
        double d2 = target.getY(-0.8F) - this.getY();
        double d3 = target.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.12F;
        this.playSound(SoundInit.GSH_SPARK.get());
        for (int i = 1; i <= 2; i++) {
            double yBodyRadians = Math.toRadians(this.yBodyRot + (180 * (i - 1)));
            EntityGrenade grenade = new EntityGrenade(this.level, this);
            grenade.shoot(d1, d2 + d4, d3, velocity, 3);
            Vec3 vec3 = this.position().add(this.getLookAngle());
            grenade.setPos(vec3.x + this.getBbWidth() * 0.8F * Math.cos(yBodyRadians), this.getY(0.48D), vec3.z + this.getBbWidth() * 0.8F * Math.sin(yBodyRadians));
            this.level.addFreshEntity(grenade);
        }
    }

    public void gshHurtTarget(LivingEntity hitEntity, float damageMultiplier, boolean disableShield) {
        double baseDamage = this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = hitEntity.hurt(DamageSource.mobAttack(this), (float) (baseDamage * damageMultiplier));
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


    static class GSHMeleeAttackGoal extends AnimationAbstractGoal<EntityGulingSentinelHeavy> {

        protected GSHMeleeAttackGoal(EntityGulingSentinelHeavy entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == ATTACK_ANIMATION_RIGHT || animation == ATTACK_ANIMATION_LEFT;
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
                if (this.entity.getAnimation() == ATTACK_ANIMATION_RIGHT) {
                    rightArc = 90F;
                } else {
                    leftArc = 90F;
                }
                final float range = 4.5F;
                List<LivingEntity> entities = entity.getNearByLivingEntities(range);
                for (LivingEntity livingEntity : entities) {
                    float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, livingEntity);
                    float entityHitDistance = (float) Math.sqrt((livingEntity.getZ() - entity.getZ()) * (livingEntity.getZ() - entity.getZ()) + (livingEntity.getX() - entity.getX()) * (livingEntity.getX() - entity.getX())) - livingEntity.getBbWidth() / 2F;
                    if ((entityHitDistance <= range && (entityRelativeAngle <= rightArc / 2F && entityRelativeAngle >= -leftArc / 2F) || (entityRelativeAngle >= 360 - leftArc / 2F || entityRelativeAngle <= -360 + leftArc / 2F))) {
                        entity.gshHurtTarget(livingEntity, 1.5F, false);
                    }
                }
            }
        }
    }

    static class GSHRangeAttackGoal extends AnimationCommonGoal<EntityGulingSentinelHeavy> {
        private int lostTargetDelay;

        public GSHRangeAttackGoal(EntityGulingSentinelHeavy entity) {
            super(entity, RANGE_ATTACK_ANIMATION);
        }

        @Override
        public void start() {
            super.start();
            this.lostTargetDelay = 0;
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
                this.entity.getLookControl().setLookAt(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 15F, 30F);
            } else {
                this.entity.playAnimation(RANGE_ATTACK_END_ANIMATION);
                return;
            }
            int tick = this.entity.getAnimationTick();
            if (tick % 20 == 0 && tick > 10) {
                this.entity.performRangedAttack(target, 1.24F);
            }
        }

        private void moveGoal(LivingEntity target) {
            double moveSpeed = this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
            double distance = this.entity.distanceToSqr(target);
            if (distance < Math.pow(6, 2)) {
                Vec3 vec3 = findTargetPoint(target, this.entity);
                this.entity.setDeltaMovement(vec3.x * moveSpeed, this.entity.getDeltaMovement().y, vec3.z * moveSpeed);
            } else if (distance > Math.pow(24, 2)) {
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

    static class GSHElectromagneticAttackGoal extends AnimationCommonGoal<EntityGulingSentinelHeavy> {

        public GSHElectromagneticAttackGoal(EntityGulingSentinelHeavy entity) {
            super(entity, ELECTROMAGNETIC_ANIMATION);
        }

        //TODO 待实现
        @Override
        public void tick() {
            super.tick();
        }
    }
}
