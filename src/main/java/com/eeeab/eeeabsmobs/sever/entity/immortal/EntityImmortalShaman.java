package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.capability.VertigoCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.KeepDistanceGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationDieGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationFullRangeAttackGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationHurtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationSpellAIGoal;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

//基本AI完成
public class EntityImmortalShaman extends EntityAbsImmortal implements IEntity, RangedAttackMob, NeedStopAiEntity {
    public static final Animation DIE_ANIMATION = Animation.create(80);
    public static final Animation HURT_ANIMATION = Animation.create(0);
    public static final Animation SPELL_CASTING_FR_ATTACK_ANIMATION = Animation.create(30);
    public static final Animation SPELL_CASTING_SUMMON_ANIMATION = Animation.create(44);
    public static final Animation SPELL_CASTING_BOMB_ANIMATION = Animation.create(30);
    public static final Animation SPELL_CASTING_HEAL_ANIMATION = Animation.create(60);
    public static final Animation SPELL_CASTING_WOLOLO_ANIMATION = Animation.create(40);
    public static final Animation AVOID_ANIMATION = Animation.create(15);
    public static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            SPELL_CASTING_FR_ATTACK_ANIMATION,
            SPELL_CASTING_SUMMON_ANIMATION,
            SPELL_CASTING_BOMB_ANIMATION,
            SPELL_CASTING_HEAL_ANIMATION,
            SPELL_CASTING_WOLOLO_ANIMATION,
            AVOID_ANIMATION
    };
    private final VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(this, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);

    //无需在退出游戏后存储数据
    private int hurtCountBeforeHeal = 0;
    private static final int CAN_STOP_HEAL_COUNT = 2;
    private int nextHealTick = 0;
    @Nullable
    private Sheep wololoTarget;

    @OnlyIn(Dist.CLIENT)
    public Vec3[] heartPos;

    public EntityImmortalShaman(EntityType<? extends EntityImmortalShaman> type, Level level) {
        super(type, level);
        this.active = true;
        if (this.level.isClientSide) {
            this.heartPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_ELITE;
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.IMMORTAL.IMMORTAL_SHAMAN.combatConfig;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new EMLookAtGoal(this, EntityImmortalGolem.class, 6.0F));
    }


    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationDieGoal<>(this));
        this.goalSelector.addGoal(1, new AnimationHurtGoal<>(this, false));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, SPELL_CASTING_BOMB_ANIMATION));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, SPELL_CASTING_SUMMON_ANIMATION));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, SPELL_CASTING_HEAL_ANIMATION));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, SPELL_CASTING_WOLOLO_ANIMATION));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, AVOID_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationFullRangeAttackGoal<>(this, SPELL_CASTING_FR_ATTACK_ANIMATION, 4.5F, 14, 2.0F, 5.0F, true) {
            @Override
            public void onHit(LivingEntity entity) {
                if (entity instanceof Player player) {
                    if (player.isBlocking()) {
                        player.disableShield(true);
                    } else {
                        player.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 40, 0, false, false, true));
                    }
                    return;
                }
                entity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 40, 0, false, false, true));
            }

            @Override
            protected boolean preHit(LivingEntity entity) {
                return entity instanceof EntityAbsImmortal && EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get();
            }
        });
        this.goalSelector.addGoal(2, new ShamanSummonGoal(this));
        this.goalSelector.addGoal(3, new ShamanAvoid(this));
        this.goalSelector.addGoal(4, new ShamanBombing(this));
        this.goalSelector.addGoal(5, new ShamanWololo(this));
        this.goalSelector.addGoal(6, new KeepDistanceGoal<>(this, 1.0D, 16.0F, 1.5F));
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return (effectInstance.getEffect() == MobEffects.REGENERATION || effectInstance.getEffect() == EffectInit.VERTIGO_EFFECT.get()) && super.addEffect(effectInstance, entity);
    }

    @Override
    public float getStepHeight() {
        return 1.0F;
    }

    @Override
    public boolean isGlow() {
        return !this.isWeakness() && super.isGlow();
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.8f;
    }

    //TODO 如果有该buff 则应该陷入虚弱状态 (疑似特性,退出重进后客户端的isVertigo()始终为false)
    public boolean isWeakness() {
        if (this.capability != null) {
            return this.capability.isVertigo();
        }
        return false;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level.isClientSide) {
            if (animation == SPELL_CASTING_FR_ATTACK_ANIMATION) {
                this.attackTick = 200;
            } else if (animation == SPELL_CASTING_HEAL_ANIMATION) {
                int timer;
                if (this.hurtCountBeforeHeal < CAN_STOP_HEAL_COUNT && !this.isWeakness()) {
                    this.heal((float) (this.getMaxHealth() * EMConfigHandler.COMMON.MOB.IMMORTAL.IMMORTAL_SHAMAN.healPercentage.get()));
                    this.level.broadcastEntityEvent(this, (byte) 12);
                    timer = 15;
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 13);
                    timer = 20;
                }
                this.nextHealTick = 20 * timer;
                this.hurtCountBeforeHeal = 0;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);

        if (!this.level.isClientSide && this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);

        if (isWeakness()) {
            this.yHeadRot = this.yBodyRot = this.getYRot();
        }

        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            if (!this.level.isClientSide && this.noConflictingTasks() && !this.isNoAi() && this.attackTick <= 0 && ((targetDistance <= 5.0F && ModEntityUtils.checkTargetComingCloser(this, target)) || this.targetDistance < 4.0F)) {
                this.playAnimation(SPELL_CASTING_FR_ATTACK_ANIMATION);
            }
        }

        if (!this.level.isClientSide && this.noConflictingTasks() && !this.isNoAi() && this.getHealthPercentage() != 100 && this.nextHealTick <= 0) {
            if ((this.getTarget() != null && this.targetDistance > 8) || this.getTarget() == null) {
                this.playAnimation(SPELL_CASTING_HEAL_ANIMATION);
            }
        }

        if (this.getAnimation() == SPELL_CASTING_WOLOLO_ANIMATION) {
            if (this.level.isClientSide) this.addParticlesAroundHeart(30);
        } else if (this.getAnimation() == SPELL_CASTING_HEAL_ANIMATION) {
            if (!this.level.isClientSide) {
                if (this.getAnimationTick() == 1) this.playSound(SoundInit.IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING.get());
                if (this.isWeakness()) {
                    this.playAnimation(NO_ANIMATION);//在治疗过程中被中断,直接结束
                    this.level.broadcastEntityEvent(this, (byte) 13);
                } else if (getAnimationTick() > 5 && getAnimationTick() < 40 && this.tickCount % 5 == 0) {
                    this.level.broadcastEntityEvent(this, (byte) 14);
                }
            } else {
                this.addParticlesAroundHeart(55);
            }
        } else if (this.getAnimation() == EntityImmortalShaman.SPELL_CASTING_SUMMON_ANIMATION) {
            if (this.level.isClientSide) {
                float speed = 0.08f;
                float yaw = this.random.nextFloat() * (2 * Mth.PI);
                float ym = this.random.nextFloat() * 0.01f;
                float xm = speed * Mth.cos(yaw);
                float zm = speed * Mth.sin(yaw);
                this.level.addParticle(ParticleTypes.SOUL, this.getX(), this.getY() + 0.1f, this.getZ(), ym, xm, zm);
                this.addParticlesAroundHeart(40);
            }
        } else if (this.getAnimation() == SPELL_CASTING_BOMB_ANIMATION) {
            if (this.getTarget() != null)
                this.lookAt(this.getTarget(), 30F, 30F);
            if (this.level.isClientSide && this.getAnimationTick() >= 10 && this.getAnimationTick() <= 12) {
                this.spawnExplosionParticles(5, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE}, 0.15F);
            }
            if (!this.level.isClientSide) this.level.broadcastEntityEvent(this, (byte) 4);
        } else if (this.getAnimation() == SPELL_CASTING_FR_ATTACK_ANIMATION) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            if (this.level.isClientSide && this.getAnimationTick() == 12) {
                this.spawnExplosionParticles(50, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME}, 0.4F);
            }
            if (this.getAnimationTick() == 18) {
                EntityCameraShake.cameraShake(level, position(), 20, 0.02f, 10, 15);
            }
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.nextHealTick > 0 && !this.isWeakness()) {
            --this.nextHealTick;
        }
        if (this.attackTick > 0) {
            this.attackTick--;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (this.level.isClientSide) {
            return false;
        } else if (entity != null) {
            float maximumDamageCap = (float) (EMConfigHandler.COMMON.MOB.IMMORTAL.IMMORTAL_SHAMAN.maximumDamageCap.damageCap.get() * 1F);
            float maxHurtDamage = getMaxHealth() * maximumDamageCap;
            if (this.getAnimation() == SPELL_CASTING_HEAL_ANIMATION && !(this.hurtTime > 0)) {
                this.hurtCountBeforeHeal++;
            }
            if (!this.isWeakness()) {
                damage = Math.min(damage, maxHurtDamage);
            }
            return super.hurt(source, damage);
        } else if (source == DamageSource.OUT_OF_WORLD || source == DamageSource.GENERIC) {
            return super.hurt(source, damage);
        }
        return false;
    }


    @Override
    public void handleEntityEvent(byte id) {
        if (id == 12) {
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        } else if (id == 13) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        } else if (id == 14) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 10; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.012D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.2D), this.getRandomY() - 0.5D, this.getRandomZ(0.2D), d0, d1, d2);
        }
    }

    @Nullable
    @Override
    //在初始生成时调用
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        RandomSource randomsource = worldIn.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, difficultyIn);
        return spawnDataIn;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemInit.IMMORTAL_STAFF.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    private void setWololoTarget(@Nullable Sheep pWololoTarget) {
        this.wololoTarget = pWololoTarget;
    }

    @Nullable
    private Sheep getWololoTarget() {
        return this.wololoTarget;
    }

    @Override
    public boolean noConflictingTasks() {
        return !this.isWeakness() && this.getAnimation() == IAnimatedEntity.NO_ANIMATION;
    }


    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D).
                add(Attributes.ATTACK_DAMAGE, 1.0D).
                add(Attributes.MOVEMENT_SPEED, 0.35D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    private void spawnExplosionParticles(int amount, ParticleOptions[] particles, float velocity) {
        ModParticleUtils.randomAnnularParticleOutburst(this.level, amount, particles, this.getX(), this.getY(), this.getZ(), velocity);
    }

    private void addParticlesAroundHeart(int duration) {
        if (this.heartPos != null && this.heartPos.length > 0 && this.heartPos[0] != null) {
            if (this.getAnimationTick() < duration) {
                ModParticleUtils.advAttractorParticle(ParticleInit.SPELL_CASTING.get(), this, 8, 0f, 2f, 12, new ParticleComponent[]{
                        new ParticleComponent.Attractor(heartPos, 1.2f, 0.0f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0f, 1f), false)
                }, true);
            }
            //if (this.tickCount % 3 == 0) {
            //    this.level.addParticle(ParticleTypes.SMOKE, this.heartPos[0].x, this.heartPos[0].y, this.heartPos[0].z, 0, 0, 0);
            //}
        }
    }

    private void addParticlesAroundSelf(ParticleOptions options) {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(options, this.getRandomX(1.0D), this.getRandomY() + 0.35D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    @Override
    public void performRangedAttack(LivingEntity entity, float velocity) {
        this.performRangedAttack(entity, false);
    }

    private void performRangedAttack(LivingEntity entity, boolean dangerous) {
        EntityShamanBomb shamanBomb;
        double d1 = 1.0D;
        Vec3 vec3 = this.getViewVector(0.0F);
        double d2 = entity.getX() - (this.getX() + vec3.x * d1);
        double d3 = entity.getY(0.5D) - (0.5D + this.getY(0.5D));
        double d4 = entity.getZ() - (this.getZ() + vec3.z * d1);
        shamanBomb = new EntityShamanBomb(level, this, d2, d3, d4);
        shamanBomb.setOwner(this);
        shamanBomb.setDangerous(dangerous);
        shamanBomb.setPos(this.getX() + vec3.x * d1, this.getY(0.74D), shamanBomb.getZ() + vec3.z * d1);
        this.level.addFreshEntity(shamanBomb);
    }

    private class ShamanAnimationCommonGoal extends AnimationCommonGoal<EntityImmortalShaman> {

        public ShamanAnimationCommonGoal(EntityImmortalShaman entity, Animation animation) {
            super(entity, animation);
        }

        public ShamanAnimationCommonGoal(EntityImmortalShaman entity, Animation animation, EnumSet<Flag> interruptFlagTypes) {
            super(entity, animation, interruptFlagTypes);
        }

        @Override
        public void tick() {
            if (getTarget() != null) {
                getLookControl().setLookAt(getTarget(), 30f, 30f);
            } else if (getWololoTarget() != null) {
                getLookControl().setLookAt(getWololoTarget(), 30f, 30f);
            }
        }
    }


    private class ShamanSummonGoal extends AnimationSpellAIGoal<EntityImmortalShaman> {
        private final TargetingConditions CountTargeting = TargetingConditions.forNonCombat().range(30.0D).ignoreLineOfSight().ignoreInvisibilityTesting();

        public ShamanSummonGoal(EntityImmortalShaman spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int size = this.spellCaster.level.getNearbyEntities(EntityImmortalGolem.class, this.CountTargeting, this.spellCaster, this.spellCaster.getBoundingBox().inflate(30.0D)).size();
                return size < this.spellCaster.random.nextInt(6) + 1 && this.spellCaster.getTarget() != null && this.spellCaster.targetDistance > 8.0;
            }
        }

        @Override
        protected int getSpellCastingCooling() {
            return 360;
        }

        @Override
        protected void inSpellCasting() {
            if (this.spellCaster.getTarget() != null) {
                LivingEntity livingEntity = this.spellCaster.getTarget();
                double minY = Math.min(livingEntity.getY(), this.spellCaster.getY());
                double maxY = Math.max(livingEntity.getY(), this.spellCaster.getY()) + 1.0D;
                Vec3 point1 = ModEntityUtils.checkSummonEntityPoint(this.spellCaster, this.spellCaster.getX() - 1, this.spellCaster.getZ(), minY, maxY);
                summonEntity(point1);
                Vec3 point2 = ModEntityUtils.checkSummonEntityPoint(this.spellCaster, this.spellCaster.getX() + 1, this.spellCaster.getZ(), minY, maxY);
                summonEntity(point2);
            }
        }

        private void summonEntity(Vec3 vec3) {
            EntityImmortalGolem entity = EntityInit.IMMORTAL_GOLEM.get().create(this.spellCaster.level);
            if (!this.spellCaster.level.isClientSide && entity != null && vec3 != null) {
                entity.setInitSpawn();
                entity.finalizeSpawn((ServerLevel) this.spellCaster.level, this.spellCaster.level.getCurrentDifficultyAt(new BlockPos(vec3)), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                entity.setOwner(this.spellCaster);
                entity.setSummonAliveTime(20 * (30 + this.spellCaster.random.nextInt(90)));
                Difficulty difficulty = this.spellCaster.level.getDifficulty();
                entity.setDangerous(this.spellCaster.random.nextInt(10 - difficulty.getId()) == 0);
                entity.setPos(vec3);
                level.addFreshEntity(entity);
            }
        }

        @Override
        protected SoundEvent getSpellCastingSound() {
            return SoundInit.IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING.get();
        }

        @Override
        protected Animation getAnimation() {
            return SPELL_CASTING_SUMMON_ANIMATION;
        }
    }

    private class ShamanBombing extends AnimationSpellAIGoal<EntityImmortalShaman> {

        public ShamanBombing(EntityImmortalShaman spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            return this.spellCaster.getTarget() != null && this.spellCaster.getSensing().hasLineOfSight(this.spellCaster.getTarget()) && this.spellCaster.targetDistance > 6.0;
        }

        @Override
        protected void inSpellCasting() {
            LivingEntity target = this.spellCaster.getTarget();
            RandomSource random = this.spellCaster.getRandom();
            if (target != null) {
                this.spellCaster.performRangedAttack(target, random.nextInt(5) == 0);
            }
        }

        @Override
        protected int getKeyFrames() {
            return 15;
        }

        @Override
        protected int getSpellCastingCooling() {
            return 80;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return SoundInit.IMMORTAL_SHAMAN_PREPARE_SHOOT.get();
        }

        @Override
        protected Animation getAnimation() {
            return SPELL_CASTING_BOMB_ANIMATION;
        }
    }

    private class ShamanAvoid extends AnimationSpellAIGoal<EntityImmortalShaman> {
        private float avoidYaw;

        public ShamanAvoid(EntityImmortalShaman spellCaster) {
            super(spellCaster);
        }

        @Override
        public void start() {
            super.start();
            double angle = this.spellCaster.getTarget() != null ? this.spellCaster.getAngleBetweenEntities(this.spellCaster, this.spellCaster.getTarget()) : yBodyRot;
            this.avoidYaw = (float) Math.toRadians(angle + 90);
        }

        @Override
        public boolean canUse() {
            if (super.canUse()) {
                LivingEntity target = this.spellCaster.getTarget();
                if (target != null) {
                    float distance = distanceTo(target) - target.getBbWidth() / 2f;
                    return (distance < 5.0F && ModEntityUtils.checkTargetComingCloser(EntityImmortalShaman.this, target)) || distance < 3F;
                }
            }
            return false;
        }

        @Override
        protected int getKeyFrames() {
            return 5;
        }

        @Override
        protected void inSpellCasting() {
            if ((this.spellCaster.isOnGround() || this.spellCaster.isInLava() || this.spellCaster.isInWater())) {
                float speed = 1.5F;
                Vec3 move = this.spellCaster.getDeltaMovement().add(speed * Math.cos(this.avoidYaw), 0, speed * Math.sin(this.avoidYaw));
                this.spellCaster.setDeltaMovement(move.x, 0.4, move.z);
            }
        }

        @Override
        protected int getSpellCastingCooling() {
            return 100;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return SoundEvents.PLAYER_ATTACK_SWEEP;
        }

        @Override
        protected Animation getAnimation() {
            return AVOID_ANIMATION;
        }
    }

    //参考自: net.minecraft.world.entity.monster.Evoker.EvokerWololoSpellGoal
    private class ShamanWololo extends AnimationSpellAIGoal<EntityImmortalShaman> {
        private final TargetingConditions wololoTargeting = TargetingConditions.forNonCombat().range(16.0D).selector((entity) -> ((Sheep) entity).getColor() == DyeColor.RED);

        public ShamanWololo(EntityImmortalShaman spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            if (this.spellCaster.getTarget() != null) {
                return false;
            } else if (!this.spellCaster.noConflictingTasks()) {
                return false;
            } else if (!ModEntityUtils.canMobDestroy(this.spellCaster)) {
                return false;
            } else if (this.spellCaster.tickCount >= this.nextAttackTickCount) {
                List<Sheep> sheepList = this.spellCaster.level.getNearbyEntities(Sheep.class, this.wololoTargeting, this.spellCaster, this.spellCaster.getBoundingBox().inflate(16, 4, 16));
                if (sheepList.isEmpty()) {
                    return false;
                } else {
                    this.spellCaster.setWololoTarget(sheepList.get(this.spellCaster.random.nextInt(sheepList.size())));
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.spellCaster.getWololoTarget() != null && this.attackDelay > 0;
        }

        @Override
        protected void inSpellCasting() {
            Sheep sheep = this.spellCaster.getWololoTarget();
            if (sheep != null && sheep.isAlive()) {
                sheep.setColor(DyeColor.BLUE);
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.spellCaster.setWololoTarget(null);
        }

        @Override
        protected int getSpellCastingCooling() {
            return 120;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return SoundInit.IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING.get();
        }

        @Override
        protected Animation getAnimation() {
            return SPELL_CASTING_WOLOLO_ANIMATION;
        }
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
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }


    //受伤音效
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.IMMORTAL_SHAMAN_HURT.get();
    }

    //死亡音效
    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.IMMORTAL_SHAMAN_DEATH.get();
    }
}
