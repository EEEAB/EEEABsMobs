package com.eeeab.eeeabsmobs.sever.entity.guling;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.animation.*;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.CrackinessEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class EntityGulingSentinel extends EntityAbsGuling implements IEntity, GlowEntity, CrackinessEntity<EntityGulingSentinel> {
    public final Animation dieAnimation = Animation.create(20);
    public final Animation hurtAnimation = Animation.create(3);
    public final Animation activeAnimation = Animation.create(20);
    public final Animation deactivateAnimation = Animation.create(20);
    public final Animation shootLaserAnimation = Animation.create(50);
    private final Animation[] animations = new Animation[]{
            this.dieAnimation,
            this.hurtAnimation,
            this.activeAnimation,
            this.deactivateAnimation,
            this.shootLaserAnimation
    };
    private int attackTick;
    private int deactivateTick;
    private static final EntityDimensions DEACTIVATE_SIZE = EntityDimensions.scalable(1, 1);
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityGulingSentinel.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ALWAYS_ACTIVE = SynchedEntityData.defineId(EntityGulingSentinel.class, EntityDataSerializers.BOOLEAN);

    public EntityGulingSentinel(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        this.active = false;
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
        return size.height * 0.75F;
    }

    @Override
    public int getMaxHeadXRot() {
        return 180;
    }

    @Override
    public int getMaxHeadYRot() {
        return 180;
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
    public boolean canBeCollidedWith() {
        return this.isAlive() && !this.isActive();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected AABB makeBoundingBox() {
        return this.isActive() ? super.makeBoundingBox() : DEACTIVATE_SIZE.makeBoundingBox(this.position());
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.GULING.GULING_SENTINEL.combatConfig;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, EntityAbsGuling.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D) {
            @Override
            public boolean canUse() {
                return super.canUse() && EntityGulingSentinel.this.isActive();
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
        this.goalSelector.addGoal(1, new AnimationAI<>(this) {
            @Override
            protected boolean test(Animation animation) {
                return animation == this.entity.shootLaserAnimation;
            }

            @Override
            public void tick() {
                LivingEntity entityTarget = this.entity.getTarget();
                this.entity.setDeltaMovement(0, this.entity.onGround() ? 0 : this.entity.getDeltaMovement().y, 0);
                int tick = this.entity.getAnimationTick();
                if (entityTarget != null) {
                    if (tick < 40) {
                        this.entity.lookAt(entityTarget, 360F, 180F);
                        this.entity.getLookControl().setLookAt(entityTarget, 360F, 180F);
                        this.entity.setYRot(this.entity.yBodyRot);
                    } else {
                        this.entity.setYRot(this.entity.yRotO);
                    }
                }
                if (tick == 5 && !this.entity.level().isClientSide) {
                    double px = this.entity.getX();
                    double py = this.entity.getY() + 1.4;
                    double pz = this.entity.getZ();
                    float yHeadRotAngle = (float) Math.toRadians(this.entity.yHeadRot + 90);
                    float xHeadRotAngle = (float) (float) Math.toRadians(-this.entity.getXRot());
                    EntityGuardianLaser laser = new EntityGuardianLaser(EntityInit.GUARDIAN_LASER.get(), this.entity.level(), this.entity, px, py, pz, yHeadRotAngle, xHeadRotAngle, 5);
                    this.entity.level().addFreshEntity(laser);
                } else if (tick == 28) {
                    this.entity.playSound(SoundInit.GS_ELECTROMAGNETIC.get(), 0.5F, this.entity.getVoicePitch());
                }
            }
        });
        this.goalSelector.addGoal(7, new GSLookAtTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level().isClientSide) {
            if (this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);
            if (!this.isNoAi() && !this.isActive()) {
                if (EMConfigHandler.COMMON.MOB.GULING.GULING_SENTINEL.enableNonCombatHeal.get()) this.heal(0.5F);
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
                this.yHeadRot = this.yBodyRot = this.getYRot();
            }
            if (this.isAlwaysActive()) {
                this.setActive(true);
                this.active = true;
                this.deactivateTick = 0;
            } else if (!this.isNoAi() && this.isNoAnimation()) {
                if (!this.isActive() && this.getTarget() != null && this.targetDistance <= 8) {
                    this.playSound(SoundInit.GSH_FRICTION.get());
                    this.playAnimation(this.activeAnimation);
                    this.setActive(true);
                }
                if (this.isActive() && this.getTarget() == null && this.deactivateTick >= 200) {
                    this.playSound(SoundInit.GSH_FRICTION.get());
                    this.playAnimation(this.deactivateAnimation);
                    this.setActive(false);
                }
            }
            if (!this.isNoAi() && this.isActive() && this.isNoAnimation() && this.getTarget() != null) {
                if (this.targetDistance <= 8) {
                    this.getNavigation().stop();
                } else {
                    this.getNavigation().moveTo(this.getTarget(), 1D);
                }
                if (this.attackTick <= 0) {
                    this.playAnimation(this.shootLaserAnimation);
                    this.attackTick = 100 + this.random.nextInt(100);
                }
            }
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
            if (this.attackTick > 0) {
                this.attackTick--;
            }
            if (this.isActive() && !this.isAlwaysActive()) {
                if (this.getTarget() == null) {
                    this.deactivateTick++;
                } else if (this.deactivateTick > 0) {
                    this.deactivateTick = 0;
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else {
            boolean projectileFlag = ModEntityUtils.isProjectileSource(source);
            if (!this.isActive() && projectileFlag) {
                return false;
            }
            Entity entity = source.getDirectEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (this.getAnimation() != this.getHurtAnimation() && !source.is(EMTagKey.GENERAL_UNRESISTANT_TO) && !source.is(DamageTypes.THORNS)) {
                    livingEntity.hurt(this.damageSources().thorns(this), damage * 0.2F);
                }
                Item item = livingEntity.getMainHandItem().getItem();
                if (item instanceof PickaxeItem) {
                    damage *= 1.5F;
                }
            }
            if (projectileFlag) {
                damage *= 0.5F;
            }
            return super.hurt(source, damage);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, false);
        this.entityData.define(DATA_ALWAYS_ACTIVE, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_ACTIVE, compound.getBoolean("isActive"));
        this.entityData.set(DATA_ALWAYS_ACTIVE, compound.getBoolean("isAlwaysActive"));
        this.active = this.isActive();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isActive", this.entityData.get(DATA_ACTIVE));
        compound.putBoolean("isAlwaysActive", this.entityData.get(DATA_ALWAYS_ACTIVE));
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

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
        this.deactivateTick = 0;
    }

    public boolean isAlwaysActive() {
        return this.entityData.get(DATA_ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean alwaysActive) {
        this.entityData.set(DATA_ALWAYS_ACTIVE, alwaysActive);
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

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.GS_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.GS_DEATH.get();
    }

    @Override
    public boolean isGlow() {
        return this.isActive() && this.isAlive();
    }


    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 50.0D).
                add(Attributes.MOVEMENT_SPEED, 0.25D).
                add(Attributes.FOLLOW_RANGE, 18.0D).
                add(Attributes.ATTACK_DAMAGE, 1.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).
                add(Attributes.ARMOR, 5.0D);
    }

    private static class NoneRotationControl extends BodyRotationControl {
        public NoneRotationControl(Mob mob) {
            super(mob);
        }

        public void clientTick() {
        }
    }

    private static class GSLookAtTargetGoal extends Goal {
        private final EntityGulingSentinel entity;

        public GSLookAtTargetGoal(EntityGulingSentinel entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            boolean noAnimationFlag = this.entity.getAnimation() == this.entity.getAnimation();
            return noAnimationFlag && target != null && target.isAlive() && this.entity.getSensing().hasLineOfSight(target);
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (target != null && target.isAlive() && this.entity.getSensing().hasLineOfSight(target)) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
            }
        }
    }
}
