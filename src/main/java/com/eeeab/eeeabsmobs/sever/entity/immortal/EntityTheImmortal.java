package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.ParticleCritRing;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticlePuncturedAirFlow;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ImmortalComboGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ImmortalPounceGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ImmortalShakeGroundGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityAlienPortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import com.eeeab.eeeabsmobs.sever.util.ModUtils;
import com.eeeab.eeeabsmobs.sever.util.damage.DamageAdaptation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

//创建于 2023/1/17
public class EntityTheImmortal extends EntityAbsImmortal implements IBoss {
    public final Animation dieAnimation = Animation.create(60);
    public final Animation spawnAnimation = Animation.create(100);
    public final Animation switchStage2Animation = Animation.create(90);
    public final Animation switchStage3Animation = Animation.create(80);
    public final Animation teleportAnimation = Animation.create(20);
    public final Animation smashGround1Animation = Animation.create(30);
    public final Animation punchCombo1Animation = Animation.coexist(40);
    public final Animation pouncePreAnimation = Animation.create(20);
    public final Animation pounceHoldAnimation = Animation.create(42);
    public final Animation pounceEndAnimation = Animation.coexist(20);
    public final Animation pounceSmashAnimation = Animation.create(50);
    public final Animation pouncePickAnimation = Animation.create(40);
    private final Animation[] animations = new Animation[]{
            dieAnimation,
            spawnAnimation,
            switchStage2Animation,
            switchStage3Animation,
            teleportAnimation,
            smashGround1Animation,
            punchCombo1Animation,
            pouncePreAnimation,
            pounceHoldAnimation,
            pounceEndAnimation,
            pounceSmashAnimation,
            pouncePickAnimation,
    };
    private static final EntityDataAccessor<Integer> DATA_STAGE = SynchedEntityData.defineId(EntityTheImmortal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_SWORDS_HELD = SynchedEntityData.defineId(EntityTheImmortal.class, EntityDataSerializers.BYTE);
    private static final UUID HEALTH_UUID = UUID.fromString("E2F534E6-4A55-4B72-A9D2-3157D084A281");
    private static final UUID ARMOR_UUID = UUID.fromString("FA2FB8E8-FFE8-4D77-B23B-E0110D4A175F");
    private static final UUID ATTACK_UUID = UUID.fromString("F8DBD65D-3C4A-4851-83D3-4CB22964A196");
    //用于标记每个阶段是否释放特殊的技能
    private boolean releaseMark;
    private boolean switching;
    //每20Tick重置一次 用于判断短时间造成伤害次数
    private int hurtCount;
    private int nextTeleportTick;
    private int immortalInvulnerableTime;
    private DamageSource lastDamageSource;
    private final DamageAdaptation damageAdaptation;
    public final ControlledAnimation coreControllerAnimation = new ControlledAnimation(10);
    public final ControlledAnimation glowControllerAnimation = new ControlledAnimation(20);

    public EntityTheImmortal(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
        this.active = false;
        this.damageAdaptation = new DamageAdaptation(EMConfigHandler.COMMON.MOB.IMMORTAL.THE_IMMORTAL.adaptConfig);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_EPIC_BOSS;
    }

    @Override
    public float getStepHeight() {
        return 3F;
    }

    @Override//可以站立的流体
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.LAVA);
    }

    @Override//是否免疫摔伤
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override//添加药水效果
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()) && super.addEffect(effectInstance, entity);
    }

    @Override//强制添加药水效果
    public void forceAddEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()))
            super.forceAddEffect(effectInstance, entity);
    }

    @Override//添加效果时额外条件
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()) && super.canBeAffected(effectInstance);
    }

    @Override//是否在实体上渲染着火效果
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected boolean canBePushedByEntity(Entity entity) {
        return this.isActive();
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
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    //TODO 后续阶段暂时隐藏
    @Override
    public boolean isDeadOrDying() {
        return super.isDeadOrDying() /*&& this.getStage() == Stage.STAGE3*/;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
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
    public void setDeltaMovement(double pX, double pY, double pZ) {
        if (ModUtils.isMinecraftOrMyMod()) return;
        super.setDeltaMovement(pX, pY, pZ);
    }

    @Override
    public void setDeltaMovement(Vec3 deltaMovement) {
        if (ModUtils.isMinecraftOrMyMod()) return;
        super.setDeltaMovement(deltaMovement);
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        if (ModUtils.isMinecraftOrMyMod()) return;
        super.push(pX, pY, pZ);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return this.immortalInvulnerableTime > 0 || !this.isActive()
                || ((animation == this.switchStage2Animation || animation == this.teleportAnimation
                || animation == this.switchStage3Animation || animation == this.pounceHoldAnimation)
                || super.isInvulnerableTo(damageSource));
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
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
        return BossEvent.BossBarColor.RED;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.switchStage2Animation || animation == this.switchStage3Animation) {
                this.setHealth(this.getMaxHealth());
                this.switching = false;
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityAbsImmortal.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        //this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D) {
        //    @Override
        //    public boolean canUse() {
        //        return EntityTheImmortal.this.active && super.canUse();
        //    }
        //});
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, Mob.class, 6.0F));
    }

    @Override
    protected void registerCustomGoals() {
        super.registerCustomGoals();
        this.goalSelector.addGoal(1, new ImmortalComboGoal(this));
        this.goalSelector.addGoal(1, new ImmortalPounceGoal(this));
        this.goalSelector.addGoal(1, new ImmortalShakeGroundGoal(this));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> switchStage2Animation));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> switchStage3Animation));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> teleportAnimation));
        this.goalSelector.addGoal(2, new AnimationActivate<>(this, () -> spawnAnimation));
        //this.goalSelector.addGoal(2, new AnimationMeleePlusAI<>(this, 1.05F, -1).ignoreSight());
    }

    @Override
    public void tick() {
        this.setYRot(this.yBodyRot);
        super.tick();
        this.floatImmortal();
        this.glowControllerAnimation.updatePrevTimer();
        this.coreControllerAnimation.updatePrevTimer();
        if (this.isPassenger()) this.stopRiding();
        //if (!this.level().isClientSide) {
        //    if (this.isActive() && !this.isNoAi() && this.isNoAnimation() && this.checkTeleportConditions(this.getTarget())) {
        //        this.nextTeleportTick = 200 + this.random.nextInt(100);
        //        this.playAnimation(this.teleportAnimation);
        //    }
        //}
        if (this.getAnimation() != this.pounceHoldAnimation) {
            this.pushEntitiesAway(1.7F, getBbHeight(), 1.7F, 1.7F);
        }
        int tick = this.getAnimationTick();
        if (!this.isActive()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
        LivingEntity lookTarget = this.getLastAttacker();
        if (this.getTarget() != null) {
            lookTarget = this.getTarget();
        }
        if (this.getAnimation() == this.punchCombo1Animation) {
            if (tick == 11) {
                this.doPuncturedAirFlowEffect(true);
            } else if (tick == 25) {
                this.doPuncturedAirFlowEffect(false);
            }
        } else if (this.getAnimation() == this.smashGround1Animation) {
            this.setDeltaMovement(0, this.onGround() ? 0 : this.getDeltaMovement().y, 0);
            if (tick == 13) {
                this.doShakeGroundDustEffect(20, 3.5F, 0.45F, 0.96F);
            }
        } else if (this.getAnimation() == this.pounceSmashAnimation) {
            this.setDeltaMovement(0, this.onGround() ? 0 : this.getDeltaMovement().y, 0);
            if (tick == 10) {
                this.doShakeGroundDustEffect(15, 3.7F, 0.58F, 0.92F);
            }
        } else if (this.getAnimation() == this.pounceHoldAnimation) {
            if (tick == 1) {
                this.doHitEffect(18, this.random.nextInt(10), 180, 0, 0.55F);
                ParticleDust.DustData particle = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.28f, 0.26f, 0.24f, 50f, 16, ParticleDust.EnumDustBehavior.SHRINK, 1f);
                ModParticleUtils.roundParticleOutburst(this.level(), 10, new ParticleOptions[]{particle}, this.getX(), this.getY(0.25), this.getZ(), 1F);
            }
        } else if (this.getAnimation() == this.switchStage2Animation || this.getAnimation() == this.switchStage3Animation) {
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.onGround() ? 0 : this.getDeltaMovement().y, 0);
            int startLookTime = 50;
            if (this.getAnimation() == this.switchStage3Animation) startLookTime = 45;
            if (tick > startLookTime && lookTarget != null && lookTarget.isAlive()) {
                this.lookAt(lookTarget, 25F, 30F);
                this.getLookControl().setLookAt(lookTarget, 25F, 30F);
            } else {
                this.setYRot(this.yRotO);
            }
            if (tick == 15) {
                if (!this.level().isClientSide) {
                    EntityAlienPortal entity = new EntityAlienPortal(this.level(), this);
                    double radiansSide = Math.toRadians(this.getYRot() + (this.getAnimation() == this.switchStage2Animation ? 180 : 0));
                    double radians = Math.toRadians(this.getYRot() + 90);
                    double x = this.getX() + Math.cos(radians) + (1.5F * Math.cos(radiansSide));
                    double y = this.getY();
                    double z = this.getZ() + Math.sin(radians) + (1.5F * Math.sin(radiansSide));
                    entity.setPos(x, y, z);
                    this.level().addFreshEntity(entity);
                }
            } else if (tick > 39) {
                this.setHeldIndex(this.getStage().heldIndex);
            }
        } else if (this.getAnimation() == this.spawnAnimation) {
            this.setActive(true);
            if (tick > 50) {
                if (lookTarget != null && lookTarget.isAlive()) {
                    this.lookAt(lookTarget, 30F, 30F);
                    this.getLookControl().setLookAt(lookTarget, 30F, 30F);
                }
            }
        } else if (this.getAnimation() == this.teleportAnimation) {
            if (lookTarget != null && lookTarget.isAlive()) {
                this.lookAt(lookTarget, 200F, 30F);
                this.getLookControl().setLookAt(lookTarget, 200F, 30F);
            }
            if (tick == 10 || tick == 15) {
                this.doTeleportEffect();
            }
        }
        this.yRotO = this.getYRot();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity lookTarget = this.getTarget();
        int tick = this.getAnimationTick();
        if (this.getAnimation() == this.teleportAnimation) {
            if (tick == 15) {
                for (int i = 16; i > 0; i--) {
                    if (this.tryTeleportToTargetBehind(lookTarget, i)) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Animation animation = this.getAnimation();
        if (!this.level().isClientSide) {
            if (this.tickCount % 20 == 0) {
                this.hurtCount = 0;
            }
            if (this.immortalInvulnerableTime > 0) {
                this.immortalInvulnerableTime--;
            }
            if (this.nextTeleportTick > 0) {
                this.nextTeleportTick--;
            }
            if (animation == this.switchStage2Animation || animation == this.switchStage3Animation) {
                this.heal(this.getMaxHealth() / animation.getDuration());
            }
            this.damageAdaptation.tick(this);
        }
        this.glowControllerAnimation.incrementOrDecreaseTimer(this.isActive() && !this.isDeadOrDying() && (this.getAnimation() != this.spawnAnimation || this.getAnimationTick() > 25));
        this.coreControllerAnimation.incrementOrDecreaseTimer(!this.isNoAnimation() && (this.getAnimation() != this.spawnAnimation));
    }

    @Override
    public void setHealth(float health) {
        //判断是否扣血
        if (health < this.getHealth()) {
            float oldDamage = this.getHealth() - health;
            float damageCap = EMConfigHandler.COMMON.MOB.IMMORTAL.THE_IMMORTAL.maximumDamageCap.damageCap.get().floatValue();
            float damage = Math.min(oldDamage, this.lastDamageSource == null || this.lastDamageSource.is(EMTagKey.GENERAL_UNRESISTANT_TO) ? oldDamage : damageCap);
            damage = this.damageAdaptation.damageAfterAdaptingOnce(this.lastDamageSource, damage);
            health = this.getHealth() - damage;
            if (this.switching || this.immortalInvulnerableTime > 0) {
                this.immortalInvulnerableTime = 10;
                return;
            }
        }
        super.setHealth(health);
        //判断是否切换阶段
        if (this.getHealth() <= 0 && this.getStage() != TheImmortalStage.STAGE3) {
            //TODO 后续阶段暂时隐藏
            //this.nextStage(Stage.byStage(this.getStage().index + 1));
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 7) {
            this.doHitEffect(8, 10F, 30F, 4.25F, 0.4F);
        } else if (id == 8) {
            this.doHitEffect(10, -20F, 40F, 3F, 0.6F);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!this.level().isClientSide) {
            this.hurtCount++;
            this.lastDamageSource = source;
            if (this.isInvulnerableTo(source)) {
                return false;
            } else if (this.estimatedHealthAfterDealingDamage(damage) && !this.releaseMark) {
                if (this.isNoAnimation()) {
                    this.releaseMark = true;
                    this.playAnimation(this.getSpecialSkillByStage(this.getStage()));
                }
                return false;
            } else if (source.getEntity() != null || source.is(EMTagKey.GENERAL_UNRESISTANT_TO)) {
                boolean flag = super.hurt(source, damage);

                return flag;
            }
        }
        return false;
    }

    @Override
    protected void blockedByShield(LivingEntity defender) {
    }

    /**
     * 预估造成伤害后是否达到期望生命值百分比
     *
     * @param damage 造成伤害
     * @return 是否达到
     */
    private boolean estimatedHealthAfterDealingDamage(float damage) {
        return (this.getHealth() - damage) / this.getMaxHealth() >= 0.5F;
    }

    /**
     * 根据不同阶段获取特殊技能
     *
     * @param stage 阶段
     * @return 特殊技能动画
     */
    private Animation getSpecialSkillByStage(TheImmortalStage stage) {
        return null;
    }

    private boolean checkTeleportConditions(LivingEntity target) {
        return this.nextTeleportTick <= 0 && this.getTarget() != null && this.distanceToSqr(target) < Math.pow(3, 2) && this.random.nextInt(this.hurtCount >= 3 ? 50 : 200) == 0;
    }

    private void floatImmortal() {
        if (this.isInLava()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.15D, 0.0D));
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STAGE, TheImmortalStage.STAGE1.index);
        this.entityData.define(DATA_SWORDS_HELD, TheImmortalStage.STAGE1.heldIndex);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("stage", this.getStage().index);
        compound.putBoolean("switching", this.switching);
        compound.putBoolean("releaseMark", this.releaseMark);
        compound.putBoolean("isActive", this.isActive());
        compound.putByte("heldIndex", this.getHeldIndex());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_STAGE, compound.getInt("stage"));
        this.switching = compound.getBoolean("switching");
        this.releaseMark = compound.getBoolean("releaseMark");
        this.setActive(compound.getBoolean("isActive"));
        this.setHeldIndex(compound.getByte("heldIndex"));
        if (this.switching) {
            if (this.getStage() == TheImmortalStage.STAGE1) {
                this.nextStage(TheImmortalStage.STAGE2);
            } else if (this.getStage() == TheImmortalStage.STAGE2) {
                this.playAnimation(switchStage2Animation);
            } else if (this.getStage() == TheImmortalStage.STAGE3) {
                this.playAnimation(switchStage3Animation);
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.is(Items.AIR)) {
            return InteractionResult.PASS;
        } else if (this.isActive() || this.isNoAi()) {
            return InteractionResult.PASS;
        } else {
            if (!player.getAbilities().instabuild) {
                System.out.println("物品数量减1");
                //itemStack.shrink(1);
            }
            this.playAnimation(this.spawnAnimation);
            return InteractionResult.SUCCESS;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        this.setActive(false);
        this.nextStage(TheImmortalStage.STAGE1);
        return spawnDataIn;
    }

    public boolean immortalHurtTarget(LivingEntity hitEntity, boolean disableShield) {
        double baseDamage = 1;//this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = hitEntity.hurt(this.damageSources().mobAttack(this), (float) (baseDamage));
        if (flag) {
            //TODO 待补充
        } else if (disableShield && hitEntity instanceof Player player && player.isBlocking()) {
            player.disableShield(true);
        }
        return flag;
    }

    public void knockBack(LivingEntity target, double strength, double yStrength, boolean forced) {
        if (forced) target.hurtMarked = true;
        double d0 = target.getX() - this.getX();
        double d1 = target.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        target.push(d0 / d2 * strength, yStrength, d1 / d2 * strength);
    }

    /**
     * 追击(向前快速一段距离)
     *
     * @param pursuitDistance 最远追击距离
     * @param moveMultiplier  追击距离
     * @param y               移动y轴
     * @param offset          偏移
     */
    public void pursuit(float pursuitDistance, float moveMultiplier, float y, float offset) {
        float targetDistance = this.targetDistance;
        if (this.getTarget() != null && targetDistance < pursuitDistance && targetDistance > 0) {
            moveMultiplier = targetDistance - offset;
        }
        targetDistance = this.targetDistance;
        if (this.getTarget() == null || targetDistance > 3F)
            this.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(this.getYRot() + 90)) * moveMultiplier, y, Math.sin(Math.toRadians(this.getYRot() + 90)) * moveMultiplier));
    }

    private boolean tryTeleportToTargetBehind(@Nullable Entity target, int distance) {
        double radian;
        if (target != null && target.isAlive()) {
            radian = Math.toRadians(this.getAngleBetweenEntities(this, target) + 90);
        } else {
            radian = Math.toRadians(this.random.nextInt(360));
        }
        double d0 = this.getX() - (this.random.nextInt(distance) + distance) * Math.cos(radian);
        double d1 = this.getY() + (double) (this.random.nextInt(6));
        double d2 = this.getZ() - (this.random.nextInt(distance) + distance) * Math.sin(radian);
        if (this.randomTeleport(d0, d1, d2, false)) {
            if (!this.isSilent()) {
                this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            return true;
        }
        return false;
    }

    /**
     * 不朽者切换阶段调用此方法
     *
     * @param stage 下一阶段
     */
    private void nextStage(TheImmortalStage stage) {
        Animation playAnimation;
        try {
            playAnimation = null;
            switch (this.getStage()) {
                case STAGE1 -> {
                    if (stage == TheImmortalStage.STAGE2) {
                        this.switching = true;
                        playAnimation = this.switchStage2Animation;
                    }
                }
                case STAGE2 -> {
                    if (stage == TheImmortalStage.STAGE3) {
                        this.switching = true;
                        playAnimation = this.switchStage3Animation;
                    }
                }
            }
            if (this.getHealth() <= 0) this.setHealth(0.1F);
        } finally {
            this.releaseMark = false;
            this.changeAttribute(stage.addHealth, stage.addArmor, stage.addAttack);
            this.entityData.set(DATA_STAGE, stage.index);
            this.damageAdaptation.clearCache();
        }
        this.playAnimation(playAnimation);
    }

    private void changeAttribute(float addHealth, float addArmor, float addAttack) {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
        AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (health != null) {
            health.removePermanentModifier(HEALTH_UUID);
            health.addPermanentModifier(new AttributeModifier(HEALTH_UUID, "Immortal add health", addHealth, AttributeModifier.Operation.ADDITION));
        }
        if (armor != null) {
            armor.removePermanentModifier(ARMOR_UUID);
            armor.addPermanentModifier(new AttributeModifier(ARMOR_UUID, "Immortal add armor", addArmor, AttributeModifier.Operation.ADDITION));
        }
        if (attack != null) {
            attack.removePermanentModifier(ATTACK_UUID);
            attack.addPermanentModifier(new AttributeModifier(ATTACK_UUID, "Immortal add attack", addAttack, AttributeModifier.Operation.ADDITION));
        }
    }

    private void doTeleportEffect() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 15; ++i) {
                this.level().addParticle(ParticleInit.VERTICAL_LINE.get(), this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0, 0, 0);
            }
        }
    }

    private void doHitEffect(int duration, float xRot, float baseScale, float xyOffset, float yOffset) {
        if (this.level().isClientSide) {
            double radians = Math.toRadians(this.yBodyRot + 90);
            double x = this.getX() + Math.cos(radians) * xyOffset;
            double y = this.getY(yOffset);
            double z = this.getZ() + Math.sin(radians) * xyOffset;
            float alpha = 0.25F + 0.25F * this.random.nextFloat();
            float scale = baseScale + this.random.nextInt(11);
            this.level().addParticle(new ParticleCritRing.CritRingData((float) Math.toRadians(-this.yBodyRot), (float) Math.toRadians(xRot), duration, 0.98F, 0.98F, 0.98F, alpha, scale, false, ParticleRing.EnumRingBehavior.GROW), x, y, z, 0, 0, 0);
        }
    }

    private void doPuncturedAirFlowEffect(boolean right) {
        if (this.level().isClientSide) {
            double radiansSide = Math.toRadians(this.getYRot() + (right ? 180 : 0));
            double radians = Math.toRadians(this.yBodyRot + 90);
            double x = this.getX() + Math.cos(radians) * 1.5F + ((1.5F + this.random.nextFloat()) * Math.cos(radiansSide));
            double y = this.getY(0.6F);
            double z = this.getZ() + Math.sin(radians) * 1.5F + ((1.5F + this.random.nextFloat()) * Math.sin(radiansSide));
            float alpha = 0.08F + 0.08F * this.random.nextFloat();
            float scale = 30F + this.random.nextInt(11);
            ParticlePuncturedAirFlow.PuncturedAirFlowData particle = new ParticlePuncturedAirFlow.PuncturedAirFlowData((float) Math.toRadians(-this.yBodyRot), (float) Math.toRadians(right ? -10 : 10), 14, 0.8F, 0.8F, 0.9F, alpha, scale, false, ParticleRing.EnumRingBehavior.SHRINK);
            this.level().addParticle(particle, x, y, z, -Math.cos(radians) * 0.5F, 0.007F, -Math.sin(radians) * 0.5F);
        }
    }

    private void doShakeGroundDustEffect(int count, float offset, float yOffset, float airDiffusionSpeed) {
        if (this.level().isClientSide) {
            double radians = Math.toRadians(this.getYRot() + 90);
            double offsetX = offset * Math.cos(radians);
            double offsetZ = offset * Math.sin(radians);
            ParticleRing.RingData particle = new ParticleRing.RingData(0F, (float) (Math.PI / 2F), 14, 1F, 1F, 1F, 0.8F, 70F, false, ParticleRing.EnumRingBehavior.GROW);
            this.level().addParticle(particle, this.getX() + offsetX, this.getY() + yOffset, this.getZ() + offsetZ, 0, 0, 0);
            ParticleDust.DustData particle2 = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.28f, 0.26f, 0.24f, 50f, 26, ParticleDust.EnumDustBehavior.GROW, airDiffusionSpeed);
            ModParticleUtils.annularParticleOutburst(this.level(), count, new ParticleOptions[]{particle2}, this.getX() + offsetX, this.getY(), this.getZ() + offsetZ, 0.8F, 0.5F, 360F, 1.7F);
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D).
                add(Attributes.MOVEMENT_SPEED, 0.36D).
                add(Attributes.FOLLOW_RANGE, 64.0D).
                add(Attributes.ATTACK_DAMAGE, 1.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).
                add(ForgeMod.ENTITY_GRAVITY.get(), 0.075D);
    }

    public TheImmortalStage getStage() {
        return TheImmortalStage.byStage(this.entityData.get(DATA_STAGE));
    }

    public byte getHeldIndex() {
        return this.entityData.get(DATA_SWORDS_HELD);
    }

    public void setHeldIndex(byte index) {
        this.entityData.set(DATA_SWORDS_HELD, index);
    }

    @Override
    public boolean isGlow() {
        return !this.glowControllerAnimation.isStop();
    }

    @Override
    protected boolean canPlayMusic() {
        return this.isActive() && super.canPlayMusic();
    }

    @Override
    public SoundEvent getBossMusic() {
        return SoundInit.THE_ARMY_OF_MINOTAUR.get();
    }

    @Override
    public void setOwner(@Nullable EntityAbsImmortal owner) {

    }

    @Override
    public void setOwnerUUID(UUID uuid) {

    }

    @Override
    public void setSpawnParticle(int amount) {

    }

    @Override
    public boolean isSummon() {
        return false;
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
    }
}
