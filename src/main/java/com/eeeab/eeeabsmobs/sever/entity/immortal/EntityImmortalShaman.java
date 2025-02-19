package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.AnimationSpellAI;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.capability.VertigoCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.IMobLevel;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.KeepDistanceGoal;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton.EntityAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton.EntityImmortalKnight;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.item.ItemImmortalStaff;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class EntityImmortalShaman extends EntityAbsImmortal implements IEntity, RangedAttackMob, NeedStopAiEntity {
    public final Animation spellCastingFRAnimation = Animation.create(24);
    public final Animation spellCastingSummonAnimation = Animation.create(40);
    public final Animation spellCastingBombAnimation = Animation.create(30);
    public final Animation spellCastingHealAnimation = Animation.create(60);
    public final Animation spellCastingWololoAnimation = Animation.create(40);
    public final Animation avoidAnimation = Animation.create(15);
    public final Animation dieAnimation = Animation.create(30);
    private final Animation[] animations = new Animation[]{
            spellCastingFRAnimation,
            spellCastingSummonAnimation,
            spellCastingBombAnimation,
            spellCastingHealAnimation,
            spellCastingWololoAnimation,
            avoidAnimation,
            dieAnimation
    };
    private final VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(this, HandlerCapability.STUN_CAPABILITY);
    //无需在退出游戏后存储数据
    private int hurtCountBeforeHeal = 0;
    private int nextHealTick = 0;
    @Nullable
    private Sheep wololoTarget;
    private static final int CAN_STOP_HEAL_COUNT = 3;
    private static final EntityType<EntityAbsImmortalSkeleton>[] SUMMON_SKELETONS = new EntityType[]{
            EntityInit.IMMORTAL_ARCHER.get(),
            EntityInit.IMMORTAL_WARRIOR.get(),
            EntityInit.IMMORTAL_MAGE.get()
    };

    public EntityImmortalShaman(EntityType<? extends EntityImmortalShaman> type, Level level) {
        super(type, level);
        this.active = true;
    }

    @Override
    public IMobLevel.MobLevel getMobLevel() {
        return IMobLevel.MobLevel.HARD;
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
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, () -> spellCastingHealAnimation));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, () -> spellCastingSummonAnimation));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, () -> spellCastingBombAnimation));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, () -> spellCastingWololoAnimation));
        this.goalSelector.addGoal(1, new ShamanAnimationCommonGoal(this, () -> avoidAnimation));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, () -> spellCastingFRAnimation, 4.5F, 9, 2.0F, 0.625F, true) {
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
        this.goalSelector.addGoal(3, new ShamanAvoidGoal(this));
        this.goalSelector.addGoal(4, new ShamanBombingGoal(this));
        this.goalSelector.addGoal(5, new ShamanWololoGoal(this));
        this.goalSelector.addGoal(6, new KeepDistanceGoal<>(this, 1.0D, 16.0F, 1.5F));
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

    //如果有该buff 则应该陷入虚弱状态
    public boolean isWeakness() {
        if (this.capability != null) {
            return this.capability.flag();
        }
        return false;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.spellCastingFRAnimation) {
                this.attackTick = 200;
            } else if (animation == this.spellCastingHealAnimation) {
                int timer;
                if (this.hurtCountBeforeHeal < CAN_STOP_HEAL_COUNT && !this.isWeakness()) {
                    this.heal((float) (this.getMaxHealth() * EMConfigHandler.COMMON.MOB.IMMORTAL.IMMORTAL_SHAMAN.healPercentage.get()));
                    this.level().broadcastEntityEvent(this, (byte) 12);
                    timer = 15;
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 13);
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
        EMAnimationHandler.INSTANCE.updateAnimations(this);

        if (!this.level().isClientSide && this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);

        if (isWeakness()) {
            this.yHeadRot = this.yBodyRot = this.getYRot();
        }

        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            if (!this.level().isClientSide && this.noConflictingTasks() && !this.isNoAi() && this.attackTick <= 0 && ((targetDistance <= 5.0F && ModEntityUtils.checkTargetComingCloser(this, target)) || this.targetDistance < 4.0F)) {
                this.playAnimation(this.spellCastingFRAnimation);
            }
        }

        if (!this.level().isClientSide && this.noConflictingTasks() && !this.isNoAi() && this.getHealthPercentage() != 100 && this.nextHealTick <= 0) {
            if ((this.getTarget() != null && this.targetDistance > 8) || this.getTarget() == null) {
                this.playAnimation(this.spellCastingHealAnimation);
            }
        }

        if (this.getAnimation() == this.spellCastingWololoAnimation) {
            if (this.level().isClientSide) this.addParticlesAroundHeart(30);
        } else if (this.getAnimation() == this.spellCastingHealAnimation) {
            if (!this.level().isClientSide) {
                if (this.getAnimationTick() == 1) this.playSound(SoundInit.IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING.get());
                if (this.isWeakness()) {
                    this.playAnimation(NO_ANIMATION);//在治疗过程中被中断,直接结束
                    this.level().broadcastEntityEvent(this, (byte) 13);
                } else if (getAnimationTick() > 5 && getAnimationTick() < 40 && this.tickCount % 5 == 0) {
                    this.level().broadcastEntityEvent(this, (byte) 14);
                }
            } else {
                this.addParticlesAroundHeart(55);
            }
        } else if (this.getAnimation() == this.spellCastingSummonAnimation) {
            if (this.level().isClientSide) {
                float speed = 0.08f;
                float yaw = this.random.nextFloat() * (2 * Mth.PI);
                float ym = this.random.nextFloat() * 0.01f;
                float xm = speed * Mth.cos(yaw);
                float zm = speed * Mth.sin(yaw);
                this.level().addParticle(ParticleTypes.SOUL, this.getX(), this.getY() + 0.1f, this.getZ(), ym, xm, zm);
                this.addParticlesAroundHeart(40);
            }
        } else if (this.getAnimation() == this.spellCastingBombAnimation) {
            if (this.getTarget() != null)
                this.lookAt(this.getTarget(), 30F, 30F);
            if (this.level().isClientSide && this.getAnimationTick() >= 10 && this.getAnimationTick() <= 12) {
                this.spawnExplosionParticles(5, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE}, 0.15F);
            }
            if (!this.level().isClientSide) this.level().broadcastEntityEvent(this, (byte) 4);
        } else if (this.getAnimation() == this.spellCastingFRAnimation) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            if (this.level().isClientSide && this.getAnimationTick() == 12) {
                this.spawnExplosionParticles(50, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME}, 0.4F);
            }
            if (this.getAnimationTick() == 18) {
                EntityCameraShake.cameraShake(level(), position(), 20, 0.02f, 10, 15);
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
        if (this.level().isClientSide) {
            return false;
        } else {
            if ((source.getEntity() != null || source.is(DamageTypes.LAVA)) && this.getAnimation() == this.spellCastingHealAnimation && !(this.hurtTime > 0)) {
                this.hurtCountBeforeHeal++;
            }
            if (this.isWeakness()) {
                damage *= 1.5F;
            } else if (ModEntityUtils.isProjectileSource(source)) {
                damage *= 0.5F;
            }
            return super.hurt(source, damage);
        }
    }


    @Override
    public void handleEntityEvent(byte id) {
        if (id == 12) {
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        } else if (id == 13) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        } else if (id == 14) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        } else super.handleEntityEvent(id);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 10; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.012D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.2D), this.getRandomY() - 0.5D, this.getRandomZ(0.2D), d0, d1, d2);
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
        return !this.isWeakness() && this.isNoAnimation();
    }


    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D).
                add(Attributes.ATTACK_DAMAGE, 8.0D).
                add(Attributes.ARMOR, 2.0D).
                add(Attributes.MOVEMENT_SPEED, 0.35D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.LUCK, 1.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    private void spawnExplosionParticles(int amount, ParticleOptions[] particles, float velocity) {
        ModParticleUtils.randomAnnularParticleOutburst(this.level(), amount, particles, this.getX(), this.getY(), this.getZ(), velocity);
    }

    private void addParticlesAroundHeart(int duration) {
        int tick = this.getAnimationTick();
        if (tick < duration && tick % 2 == 0) {
            ModParticleUtils.advAttractorParticle(ParticleInit.SPELL_CASTING.get(), this, 12, 0f, 2f, 12, new ParticleComponent[]{
                    new ParticleComponent.Attractor(new Vec3[]{this.position().add(0, this.getBbHeight() * 0.65F, 0)}, 1.2f, 0.1f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0f, 0.6f), false)
            }, true);
        }
    }

    private void addParticlesAroundSelf(ParticleOptions options) {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(options, this.getRandomX(1.0D), this.getRandomY() + 0.35D, this.getRandomZ(1.0D), d0, d1, d2);
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
        shamanBomb = new EntityShamanBomb(level(), this, d2, d3, d4);
        shamanBomb.setOwner(this);
        shamanBomb.setDamage((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        shamanBomb.setDangerous(dangerous);
        shamanBomb.setPos(this.getX() + vec3.x * d1, this.getY(0.74D), shamanBomb.getZ() + vec3.z * d1);
        this.level().addFreshEntity(shamanBomb);
    }

    private class ShamanAnimationCommonGoal extends AnimationSimpleAI<EntityImmortalShaman> {
        public ShamanAnimationCommonGoal(EntityImmortalShaman entity, Supplier<Animation> animationSupplier) {
            super(entity, animationSupplier);
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

    static class ShamanSummonGoal extends AnimationSpellAI<EntityImmortalShaman> {
        private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting().selector(e -> !(e instanceof EntityImmortalKnight));

        public ShamanSummonGoal(EntityImmortalShaman spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int size = this.spellCaster.level().getNearbyEntities(EntityAbsImmortalSkeleton.class, TARGETING_CONDITIONS, this.spellCaster, this.spellCaster.getBoundingBox().inflate(32.0D)).size();
                return size < this.spellCaster.random.nextInt(9) + 1 && this.spellCaster.getTarget() != null && this.spellCaster.targetDistance > 8.0;
            }
        }

        @Override
        protected int getSpellCastingCooling() {
            return 360;
        }

        @Override
        protected void inSpellCasting() {
            double minY, maxY;
            if (this.spellCaster.getTarget() != null) {
                LivingEntity livingEntity = this.spellCaster.getTarget();
                minY = Math.min(livingEntity.getY(), this.spellCaster.getY());
                maxY = Math.max(livingEntity.getY(), this.spellCaster.getY()) + 1.0D;
            } else {
                maxY = minY = this.spellCaster.getY();
            }
            Vec3 looking = this.spellCaster.getLookAngle();
            float totalOffset = 3F;
            int count = 3;
            float angleStep = totalOffset / (count + 1);
            for (int i = 0; i < count; i++) {
                float angle = -totalOffset / 2.0F + (i + 1) * angleStep;
                Vec3 vec3 = looking.yRot(angle);
                float f0 = (float) Mth.atan2(vec3.z, vec3.x);
                Vec3 point = ModEntityUtils.checkSummonEntityPoint(this.spellCaster, this.spellCaster.getX() + Math.cos(f0) * 3F, this.spellCaster.getZ() + Math.sin(f0) * 3F, minY, maxY);
                summonEntity(point);
            }
        }

        private void summonEntity(Vec3 vec3) {
            if (!this.spellCaster.level().isClientSide && vec3 != null) {
                EntityAbsImmortalSkeleton skeleton;
                if (this.spellCaster.level().getDifficulty().getId() <= Difficulty.EASY.getId()) {
                    skeleton = EntityInit.IMMORTAL_SKELETON.get().create(this.spellCaster.level());
                } else {
                    EntityType<EntityAbsImmortalSkeleton> type = SUMMON_SKELETONS[this.spellCaster.getRandom().nextInt(SUMMON_SKELETONS.length)];
                    skeleton = type.create(this.spellCaster.level());
                }
                if (skeleton != null) {
                    skeleton.setYRot(skeleton.yHeadRot = skeleton.yHeadRotO = skeleton.yRotO = this.spellCaster.getYRot());
                    skeleton.setInitSpawn();
                    skeleton.finalizeSpawn((ServerLevel) this.spellCaster.level(), this.spellCaster.level().getCurrentDifficultyAt(BlockPos.containing(vec3.x, vec3.y, vec3.z)), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    skeleton.setOwner(this.spellCaster);
                    skeleton.setPos(vec3);
                    this.spellCaster.level().addFreshEntity(skeleton);
                }
            }
        }

        @Override
        protected SoundEvent getSpellCastingSound() {
            return SoundInit.IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING.get();
        }

        @Override
        protected Animation getEMAnimation() {
            return this.spellCaster.spellCastingSummonAnimation;
        }
    }

    static class ShamanBombingGoal extends AnimationSpellAI<EntityImmortalShaman> {
        public ShamanBombingGoal(EntityImmortalShaman spellCaster) {
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
            if (target != null) {
                boolean hold = this.spellCaster.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(ItemInit.IMMORTAL_STAFF.get()) || this.spellCaster.getItemInHand(InteractionHand.OFF_HAND).getItem().equals(ItemInit.IMMORTAL_STAFF.get());
                this.spellCaster.performRangedAttack(target, hold && ItemImmortalStaff.isDangerBomb(this.spellCaster));
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
        protected Animation getEMAnimation() {
            return this.spellCaster.spellCastingBombAnimation;
        }
    }

    static class ShamanAvoidGoal extends AnimationSpellAI<EntityImmortalShaman> {
        private float avoidYaw;

        public ShamanAvoidGoal(EntityImmortalShaman spellCaster) {
            super(spellCaster);
        }

        @Override
        public void start() {
            super.start();
            double angle = this.spellCaster.getTarget() != null ? this.spellCaster.getAngleBetweenEntities(this.spellCaster, this.spellCaster.getTarget()) : this.spellCaster.yBodyRot;
            this.avoidYaw = (float) Math.toRadians(angle + 90);
        }

        @Override
        public boolean canUse() {
            if (super.canUse()) {
                LivingEntity target = this.spellCaster.getTarget();
                if (target != null) {
                    float distance = this.spellCaster.distanceTo(target) - target.getBbWidth() / 2f;
                    return (distance < 5.0F && ModEntityUtils.checkTargetComingCloser(this.spellCaster, target)) || distance < 3F;
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
            if ((this.spellCaster.onGround() || this.spellCaster.isInLava() || this.spellCaster.isInWater())) {
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
        protected Animation getEMAnimation() {
            return this.spellCaster.avoidAnimation;
        }
    }

    //参考自: net.minecraft.world.entity.monster.Evoker.EvokerWololoSpellGoal
    static class ShamanWololoGoal extends AnimationSpellAI<EntityImmortalShaman> {
        private final TargetingConditions wololoTargeting = TargetingConditions.forNonCombat().range(16.0D).selector((entity) -> ((Sheep) entity).getColor() == DyeColor.RED);

        public ShamanWololoGoal(EntityImmortalShaman spellCaster) {
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
                List<Sheep> sheepList = this.spellCaster.level().getNearbyEntities(Sheep.class, this.wololoTargeting, this.spellCaster, this.spellCaster.getBoundingBox().inflate(16, 4, 16));
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
        protected Animation getEMAnimation() {
            return this.spellCaster.spellCastingWololoAnimation;
        }
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
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
