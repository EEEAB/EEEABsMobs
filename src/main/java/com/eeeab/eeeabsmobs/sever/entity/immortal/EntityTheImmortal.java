package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

//创建于 2023/1/17
public class EntityTheImmortal extends EntityAbsImmortal implements IBoss {
    public static final Animation SUMMONER_MOB_ANIMATION = Animation.create(40);
    private static final Animation[] ANIMATIONS = {
            SUMMONER_MOB_ANIMATION
    };
    private boolean circleDirection;
    private int circleTick;
    private static final EntityDataAccessor<Integer> DATA_MOVING_TYPE = SynchedEntityData.defineId(EntityTheImmortal.class, EntityDataSerializers.INT);

    public EntityTheImmortal(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
        this.active = true;
        this.circleTick += this.random.nextInt(200);
        this.setMovingType(MoveType.CIRCLE);
        //this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_BOSS;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, null));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8F));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, Mob.class, 8F));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationCommonGoal<>(this, SUMMONER_MOB_ANIMATION) {//TODO 待完善功能
            @Override
            public void tick() {
                int tick = this.entity.getAnimationTick();
                LivingEntity target = this.entity.getTarget();
                RandomSource random = this.entity.random;
                if (target != null) {
                    this.entity.lookAt(target, 30F, 30F);
                    if (tick == 20) {
                        double minY = Math.min(target.getY(), this.entity.getY());
                        double maxY = Math.max(target.getY(), this.entity.getY()) + 1.0D;
                        for (int i = 0; i < 3; i++) {
                            Vec3 vec3 = this.entity.position().add(new Vec3(random.nextInt(10) + 2, 0, random.nextInt(10) + 2));
                            Vec3 entityPoint = ModEntityUtils.checkSummonEntityPoint(this.entity, vec3.x(), vec3.z(), minY, maxY);
                            summonEntity(entityPoint);
                        }
                    }
                }
            }

            private void summonEntity(Vec3 vec3) {
                EntityImmortalSkeleton entity = EntityInit.IMMORTAL_SKELETON.get().create(this.entity.level);
                if (!this.entity.level.isClientSide && entity != null && vec3 != null) {
                    entity.setInitSpawn();
                    entity.finalizeSpawn((ServerLevel) this.entity.level, this.entity.level.getCurrentDifficultyAt(new BlockPos(vec3.x, vec3.y, vec3.z)),
                            MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    entity.setOwner(this.entity);
                    entity.setSummonAliveTime(20 * (30 + this.entity.random.nextInt(90)));
                    entity.setPos(vec3);
                    this.entity.level.addFreshEntity(entity);
                }
            }
        });
        this.goalSelector.addGoal(6, new ImmortalMoveGoal(this, 6.5F));
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }
    //@Override
    //protected BodyRotationControl createBodyControl() {
    //    return new EEBodyRotationControl(this);
    //}
    //
    //@Override
    //protected PathNavigation createNavigation(Level pLevel) {
    //    return new EEPathNavigateGround(this, pLevel);
    //}

    @Override
    public void aiStep() {

        //Vec3 vec3 = this.getDeltaMovement().multiply(1, 0.6, 1);
        //LivingEntity target = getTarget();
        //if (target != null) {
        //    double y = vec3.y;
        //    if (this.getY() < target.getY()+1) {
        //        y = Math.max(0, y);
        //        y += 0.3 - y * 0.6;
        //    }
        //
        //    vec3 = new Vec3(vec3.x, y, vec3.z);
        //    Vec3 targetVec = new Vec3(target.getX() - this.getX(), 0, target.getZ() - this.getZ());
        //    Vec3 normalize = targetVec.normalize();
        //    double pX = normalize.x * 0.3 - vec3.x * 0.6;
        //    double pZ = normalize.z * 0.3 - vec3.z * 0.6;
        //    if (targetVec.horizontalDistanceSqr() > 36) {
        //        vec3 = vec3.add(pX, 0, pZ);
        //    } else if (targetVec.horizontalDistanceSqr() < 25) {
        //        vec3 = vec3.subtract(pX, 0, pZ);
        //    } else {
        //        this.lookAt(target, 30F, 30F);
        //    }
        //}
        //this.setDeltaMovement(vec3);
        //if (vec3.horizontalDistanceSqr() > 0.05) {
        //    this.setYRot((float) (Mth.atan2(vec3.z, vec3.x) * (180F / Math.PI) - 90F));
        //}

        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
//        if (!this.level.isClientSide && this.getTarget() != null && this.getAnimation() == NO_ANIMATION) {
//            this.playAnimation(SUMMONER_MOB_ANIMATION);
//        }
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.IMMORTAL.THE_IMMORTAL.combatConfig;
    }

    @Override
    protected boolean showBossBloodBars() {
        return EMConfigHandler.COMMON.OTHER.enableShowBloodBars.get();
    }

    @Override
    protected BossEvent.BossBarColor bossBloodBarsColor() {
        return BossEvent.BossBarColor.BLUE;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MOVING_TYPE, MoveType.KEEP_DISTANCE.getId());
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    public MoveType getMoveType() {
        return MoveType.byId(this.entityData.get(DATA_MOVING_TYPE));
    }

    public void setMovingType(MoveType movingType) {
        this.entityData.set(DATA_MOVING_TYPE, movingType.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.setMovingType(MoveType.byId(compound.getInt("moveId")));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        compound.putInt("moveId", getMoveType().getId());
    }

    private Vec3 updateCirclingPosition(float radius, float speed) {
        LivingEntity target = getTarget();
        if (target != null) {
            if (random.nextInt(200) == 0) {
                circleDirection = !circleDirection;
            }
            if (circleDirection) {
                circleTick++;
            } else {
                circleTick--;
            }
            return circlePosition(target.position(), radius, speed, true, circleTick, 0);
        }
        return null;
    }

    public enum MoveType {
        NONE(-1),
        KEEP_DISTANCE(0),
        CIRCLE(1);

        private final int id;

        MoveType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static MoveType byId(int id) {
            for (MoveType value : values()) {
                if (id == value.getId()) {
                    return value;
                }
            }
            return NONE;
        }
    }

    public static class ImmortalMoveGoal extends Goal {
        private final EntityTheImmortal immortal;
        private boolean inside;
        private final float safeRange;
        private int cooling;

        public ImmortalMoveGoal(EntityTheImmortal immortal, float safeRange) {
            this.immortal = immortal;
            this.safeRange = safeRange;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.immortal.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return (this.canUse() || !this.immortal.getNavigation().isDone());
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void stop() {
            this.immortal.getMoveControl().strafe(0, 0);
        }

        @Override
        public void tick() {
            LivingEntity target = this.immortal.getTarget();
            if (target != null) {
                //TODO 可以优化成工厂模式
                float distanceToTarget = this.immortal.distanceTo(target);

                if (inside && distanceToTarget >= safeRange + 2) {
                    inside = false;
                }
                if (!inside && distanceToTarget <= safeRange) {
                    inside = true;
                }

                if (this.immortal.getMoveType() == MoveType.CIRCLE) {
                    if (!inside) {
                        //超出范围
                        this.immortal.getMoveControl().strafe(0, 0);
                        this.immortal.getNavigation().moveTo(target, 0.6F);
                        this.immortal.getLookControl().setLookAt(target, 30F, 30F);
                    } else {
                        //在安全距离
                        this.immortal.getNavigation().stop();
                        Vec3 circlingPosition = this.immortal.updateCirclingPosition(this.safeRange, 0.3F);
                        double distanceTo = this.immortal.position().distanceTo(circlingPosition);
                        if (distanceTo <= 1.5F) {
                            int strafingFrontBackMul;
                            if (distanceToTarget > this.safeRange + 0.5) {
                                strafingFrontBackMul = 1;
                            } else if (distanceToTarget < this.safeRange - 0.5) {
                                strafingFrontBackMul = -1;
                            } else {
                                strafingFrontBackMul = 0;
                            }

                            Vec3 toTarget = target.position().subtract(this.immortal.position()).multiply(1, 0, 1).normalize();
                            Vec3 toCirclePos = circlingPosition.subtract(this.immortal.position()).multiply(1, 0, 1).normalize();
                            Vec3 cross = toTarget.cross(toCirclePos);
                            int strafingLeftRightMul;
                            if (cross.y > 0) strafingLeftRightMul = 1;
                            else if (cross.y < 0) strafingLeftRightMul = -1;
                            else strafingLeftRightMul = 0;

                            float distScale = (float) Math.min(Math.pow(distanceTo * 1f / 1.5, 0.7), 1.0);

                            this.immortal.getMoveControl().strafe(strafingFrontBackMul * 0.5F, strafingLeftRightMul * 0.5F * distScale);
                            this.immortal.lookAt(target, 30.0F, 30.0F);
                        } else {
                            //不在安全距离
                            this.immortal.getMoveControl().strafe(0, 0);
                            this.immortal.getNavigation().moveTo(circlingPosition.x(), circlingPosition.y(), circlingPosition.z(), 0.53F);
                            this.immortal.getLookControl().setLookAt(target, 30F, 30F);
                        }
                    }
                } else if (this.immortal.getMoveType() == MoveType.KEEP_DISTANCE) {
                    this.immortal.getNavigation().stop();
                    this.immortal.getMoveControl().strafe(0, 0);
                    this.immortal.lookAt(target, 30.0F, 30.0F);
                    this.immortal.getLookControl().setLookAt(target, 30F, 30F);
                    double angle = this.immortal.getAngleBetweenEntities(this.immortal, target);
                    float radian = (float) Math.toRadians(angle + 90F);
                    float distanceMultiple = 1.5F;
                    if (cooling <= 0) {
                        if (!inside) {
                            if (distanceToTarget > safeRange + 4) {
                                //向目标移动
                                keepDistanceMoving(false, distanceMultiple, radian);
                            }
                        } else {
                            if (distanceToTarget < safeRange / 2) {
                                //尝试远离目标
                                distanceMultiple = 2.0F;
                                keepDistanceMoving(true, distanceMultiple, radian);
                            }
                        }
                    }
                }
            }
            if (cooling > 0) {
                cooling--;
            }
        }

        private void keepDistanceMoving(boolean direction, float distanceMultiple, double radian) {
            int t = direction ? 1 : -1;
            Vec3 movement = this.immortal.getDeltaMovement().add(distanceMultiple * Math.cos(radian), 0, distanceMultiple * Math.sin(radian));
            this.immortal.setDeltaMovement(movement.x * t, 0.3, movement.z * t);
            this.cooling = 40;
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0D).
                add(Attributes.MOVEMENT_SPEED, 0.7D).
                add(Attributes.FOLLOW_RANGE, 50.0D).
                add(Attributes.ATTACK_DAMAGE, 1.0D).
                add(Attributes.FLYING_SPEED, 0.5D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }
}
