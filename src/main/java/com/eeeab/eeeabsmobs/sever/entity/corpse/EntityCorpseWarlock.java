package com.eeeab.eeeabsmobs.sever.entity.corpse;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.AnimationSpellAI;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.KeepDistanceGoal;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCrimsonCrack;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCrimsonRay;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityBloodBall;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityCorpseWarlock extends EntityAbsCorpse implements IEntity, NeedStopAiEntity, RangedAttackMob {
    public final Animation dieAnimation = Animation.create(0);
    public final Animation attackAnimation = Animation.create(15);
    public final Animation summonAnimation = Animation.create(30);
    public final Animation frenzyAnimation = Animation.create(30);
    public final Animation teleportAnimation = Animation.create(30);
    public final Animation resetPosAnimation = Animation.create(30);
    public final Animation vampireAnimation = Animation.create(90);
    public final Animation robustAnimation = Animation.create(100);
    public final Animation babbleAnimation = Animation.create(666);
    public final Animation tearSpaceAnimation = Animation.create(30);
    private final Animation[] animations = new Animation[]{
            dieAnimation,
            attackAnimation,
            summonAnimation,
            frenzyAnimation,
            teleportAnimation,
            resetPosAnimation,
            vampireAnimation,
            robustAnimation,
            babbleAnimation,
            tearSpaceAnimation,
    };
    //周期检查是否处于出生点坐标计时
    private int nextDPTick;
    private int hurtCount;
    private int nextHealTick;
    private List<EntityCorpse> entities = new ArrayList<>();
    public static final int[] SPAWN_COUNT = new int[]{2, 4, 6};
    private static final int MAX_HURT_COUNT = 3;
    private static final UniformInt NEXT_HEAL_TIME = TimeUtil.rangeOfSeconds(15, 25);
    private static final TargetingConditions IGNORE_ALLIES = TargetingConditions.forCombat().selector(e -> {
        if (e instanceof EntityAbsCorpse) {
            return !EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get();
        }
        return true;
    });
    private static final EntityDataAccessor<Boolean> DATA_IS_HEAL = SynchedEntityData.defineId(EntityCorpseWarlock.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_REST_POSITION = SynchedEntityData.defineId(EntityCorpseWarlock.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    @OnlyIn(Dist.CLIENT)
    public Vec3[] myPos;

    public EntityCorpseWarlock(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
        this.active = true;
        if (this.level().isClientSide) {
            myPos = new Vec3[]{new Vec3(0, 0, 0), new Vec3(0, 0, 0)};
        }
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_ELITE;
    }

    @Override
    public float getStepHeight() {
        return 1F;
    }

    @Override//是否免疫药水效果
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return (this.getAnimation() == this.getNoAnimation() || effectInstance.getEffect() == EffectInit.VERTIGO_EFFECT.get()) && super.addEffect(effectInstance, entity);
    }

    @Override//被方块阻塞
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override//是否在实体上渲染着火效果
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.CORPSES.CORPSE_WARLOCK.combatConfig;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, false).setUnseenMemoryTicks(300));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> summonAnimation, true));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> teleportAnimation, true) {
            @Override
            public void start() {
                super.start();
                for (int i = 0; i < 16; i++) {
                    if (this.entity.tryTeleportToTargetBehind(this.entity.targetDistance < 8F ? this.entity.getTarget() : null)) {
                        break;
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> resetPosAnimation, false) {
            @Override
            public void start() {
                super.start();
                this.entity.tryTeleportToRestPosition();
            }
        });
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> frenzyAnimation, true));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> tearSpaceAnimation, true));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> vampireAnimation, false));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> babbleAnimation, false));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, () -> robustAnimation, false));
        this.goalSelector.addGoal(2, new AnimationRepel<>(this, () -> attackAnimation, 4F, 6, 2F, 2F, true));
        this.goalSelector.addGoal(2, new WarlockTeleportGoal(this));
        this.goalSelector.addGoal(3, new WarlockSummonGoal(this));
        this.goalSelector.addGoal(3, new WarlockTearApartGoal(this));
        this.goalSelector.addGoal(4, new WarlockReinforceGoal(this));
        this.goalSelector.addGoal(5, new WarlockRobustGoal(this));
        this.goalSelector.addGoal(6, new KeepDistanceGoal<>(this, 0.96D, 18F, 2F));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, Mob.class, 6.0F));
    }

    @Override
    public void tick() {
        super.tick();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level().isClientSide && !this.isNoAi() && this.noConflictingTasks() && this.getTarget() == null && this.tickCount % 2 == 0 && this.random.nextInt(400) == 0) {
            this.playAnimation(babbleAnimation);
        }

        if (!this.level().isClientSide && !this.isNoAi() && this.noConflictingTasks() && this.getTarget() == null && this.tickCount % 10 == 0 && this.isNoAtRestPos() && this.nextDPTick <= 0) {
            this.nextDPTick = 1200;
            this.playAnimation(resetPosAnimation);
        }

        if (!this.level().isClientSide && !this.isNoAi() && this.noConflictingTasks() && this.hurtCount >= MAX_HURT_COUNT && this.getTarget() != null && this.tickCount % 2 == 0 && (!this.level().getNearbyEntities(LivingEntity.class, IGNORE_ALLIES, this, this.getBoundingBox().inflate(2)).isEmpty() || targetDistance <= 5.0F && ModEntityUtils.checkTargetComingCloser(this, this.getTarget()) || this.targetDistance < 4.0F)) {
            this.hurtCount = 0;
            this.playAnimation(attackAnimation);
        }

        if (!this.level().isClientSide && !this.isNoAi() && this.tickCount % 40 == 0 && (this.getTarget() != null || this.getHealthPercentage() != 100) && this.noConflictingTasks() && this.nextHealTick <= 0) {
            this.entities = this.level().getNearbyEntities(EntityCorpse.class, TargetingConditions.forNonCombat().selector(e -> this.getTarget() == null || (e instanceof EntityCorpse c && (!c.hasEffect(EffectInit.FRENZY_EFFECT.get()) || this.getHealthPercentage() < 50) && !c.valuable)), this, this.getBoundingBox().inflate(32, 16, 32))
                    .stream()
                    .filter(e -> this == e.getOwner())
                    .limit(10)
                    .collect(Collectors.toList());
            if (!this.entities.isEmpty()) {
                this.resetHealTick();
                this.playAnimation(vampireAnimation);
            }
        }

        if (this.getAnimation() == vampireAnimation) {
            int tick = this.getAnimationTick();
            if (!this.level().isClientSide) {
                for (EntityCorpse corps : this.entities) {
                    if (corps == null || !corps.isActive())
                        continue;
                    Vec3 diff = corps.position().subtract(this.position().add(0F, 5F, 0F));
                    float strength = 0.05F;
                    if (this.distanceTo(corps) >= 16) strength += 0.05F;
                    diff = diff.normalize().scale(strength);
                    corps.setDeltaMovement(corps.getDeltaMovement().subtract(diff));
                    if (corps.getY() < this.getY() + 3) {
                        corps.setDeltaMovement(corps.getDeltaMovement().add(0, 0.075, 0));
                    }
                }
                LivingEntity target = this.getTarget();
                if (tick == 50) {
                    this.entities.forEach(LivingEntity::kill);
                    this.setIsHeal((target != null && this.getHealthPercentage() < 50) || (target == null && this.getHealthPercentage() != 100));
                    this.performRangedAttack(target, 1.0F);
                } else if (tick > 60) {
                    this.setDeltaMovement(0, 0, 0);
                    if (!this.isHeal() && target != null) {
                        this.getLookControl().setLookAt(target, 30F, 30F);
                    }
                } else {
                    this.setDeltaMovement(0, 0.12F, 0);
                }
            } else {
                if (tick == 20) {
                    Vec3 vec3 = this.position();
                    float yRot = this.yBodyRot;
                    float offsetX = (float) (Math.cos(Math.toRadians(yRot + 90)) * 1.5F);
                    float offsetZ = (float) (Math.sin(Math.toRadians(yRot + 90)) * 1.5F);
                    this.myPos[0] = new Vec3(vec3.x + offsetX, vec3.y + getBbHeight() * 0.6, vec3.z + offsetZ);
                    AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.CRIMSON.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 10F, 1, 1, 1, 1, 1, 30, true, false, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(this.myPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.oscillate(0.25F, -0.24F, 12), true),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Y, AnimData.startAndEnd(0, 8F), true),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_X, AnimData.startAndEnd(0F, -offsetX), true),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Z, AnimData.startAndEnd(0F, -offsetZ), true),
                    });
                } else if (tick == 50) {
                    this.myPos[1] = this.position().add(0, 5, 0);
                    for (int i = 0; i < 64; i++) {
                        double d0 = this.myPos[1].x + (double) Mth.randomBetween(this.random, -0.5F, 0.5F);
                        double d1 = this.myPos[1].y;
                        double d2 = this.myPos[1].z + (double) Mth.randomBetween(this.random, -0.5F, 0.5F);
                        this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()), d0, d1, d2, 0D, 0D, 0D);
                    }
                    this.level().playLocalSound(this.myPos[1].x, this.myPos[1].y, this.myPos[1].z, SoundInit.CORPSE_WARLOCK_TEAR.get(), this.getSoundSource(), this.getSoundVolume(), this.getVoicePitch(), false);
                } else if (this.isHeal() && tick == 80) {
                    this.doHealEffect();
                }
            }
        } else if (this.getAnimation() == teleportAnimation || this.getAnimation() == resetPosAnimation) {
            int tick = this.getAnimationTick();
            if (tick == 1) {
                this.setInvisible(true);
                this.doTeleportEffect();
            } else if (tick == 15) {
                this.doTeleportEffect();
            } else if (tick >= 20) {
                this.setInvisible(false);
            }
        } else if (this.getAnimation() == babbleAnimation) {
            if (this.getTarget() != null && this.distanceTo(this.getTarget()) < 12) {
                this.setAnimation(this.getNoAnimation());//中断施法
            }
            this.doCrimsonEyeEffect(2.5F);
        } else if (this.getAnimation() == attackAnimation) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            if (this.level().isClientSide && this.getAnimationTick() == 5) {
                ModParticleUtils.randomAnnularParticleOutburst(this.level(), 50, new ParticleOptions[]{ParticleTypes.SMOKE}, this.getX(), this.getY(), this.getZ(), 0.4F);
            }
        } else if (this.getAnimation() == robustAnimation) {
            int tick = this.getAnimationTick();
            if (tick > 35) {
                this.doCrimsonEyeEffect(-1F);
                this.setDeltaMovement(0, 0, 0);
            } else {
                this.setDeltaMovement(0, 0.25F, 0);
            }
        }
    }

    private void doHealEffect() {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.1D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }


    private void doCrimsonEyeEffect(float offset) {
        if (this.level().isClientSide) {
            int tick = this.getAnimationTick();
            if (tick % 20 == 0) {
                Vec3 vec3 = this.position();
                float yRot = this.yBodyRot;
                float offsetX = (float) (Math.cos(Math.toRadians(yRot + 90)) * offset);
                float offsetZ = (float) (Math.sin(Math.toRadians(yRot + 90)) * offset);
                this.myPos[0] = new Vec3(vec3.x + offsetX, vec3.y + getBbHeight() * 1.5, vec3.z + offsetZ);
                AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.CRIMSON_EYE.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 10F, 1, 1, 1, 1, 1, 20, true, false, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(this.myPos)
                });
            }
            if (tick % 5 == 0) {
                ModParticleUtils.advAttractorParticle(ParticleInit.ADV_ORB.get(), this, 12, 0f, 2.5f, 20, new ParticleComponent[]{
                        new ParticleComponent.Attractor(this.myPos, 1.2f, 0f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0f, 0.6f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.KeyTrack.startAndEnd(3f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.BLUE, AnimData.KeyTrack.constant(0F), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.GREEN, AnimData.KeyTrack.constant(0F), false),
                }, false);
            }
        }
    }

    private void doTeleportEffect() {
        this.setDeltaMovement(0, 0, 0);
        if (this.level().isClientSide) {
            this.myPos[0] = this.position().add(0, this.getBbHeight() * 0.6, 0);
            AnimData.KeyTrack keyTrack1 = AnimData.KeyTrack.oscillate(0, 2, 24);
            AnimData.KeyTrack keyTrack2 = new AnimData.KeyTrack(new float[]{0, 40, 40, 0}, new float[]{0, 0.2f, 0.8f, 1});
            AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.CRIMSON.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 14, true, false, false, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(this.myPos),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack1, true),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack2, false),
            });
        }
    }


    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.nextDPTick > 0) {
                this.nextDPTick--;
            }

            if (this.nextHealTick > 0) {
                this.nextHealTick--;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (this.level().isClientSide) {
            return false;
        } else if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (entity != null) {
                Animation animation = this.getAnimation();
                if (animation == teleportAnimation) {
                    return false;
                } else if (animation == babbleAnimation) {
                    this.setAnimation(this.getNoAnimation());
                } else if (animation == vampireAnimation || animation == robustAnimation) {
                    damage *= 0.2F;
                }
                if (this.random.nextFloat() < 0.6F) this.hurtCount++;
                Double maxDistance = EMConfigHandler.COMMON.MOB.CORPSES.CORPSE_WARLOCK.maxDistanceTakeDamage.get();
                if (ModEntityUtils.isProjectileSource(source) && this.distanceTo(entity) >= maxDistance) return false;
                float maxDamageCap = (float) (EMConfigHandler.COMMON.MOB.CORPSES.CORPSE_WARLOCK.maximumDamageCap.damageCap.get() * 1.0F);
                float maxHurtDamage = this.getMaxHealth() * maxDamageCap;
                damage = Math.min(damage, maxHurtDamage);
                return super.hurt(source, damage);
            } else if (source.is(EMTagKey.GENERAL_UNRESISTANT_TO)) {
                return super.hurt(source, damage);
            }
        }
        return false;
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }
        if (source.is(EMTagKey.MAGIC_UNRESISTANT_TO)) {
            damage *= 0.5F;//魔法造成的伤害减少50%
        }
        return damage;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 14) {
            this.doHealEffect();
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void makePoofParticles() {
        for (int j = 0; j < 64; ++j) {
            double d0 = (double) j / 63.0D;
            float f = (this.random.nextFloat() - 0.5F) * 0.2F;
            float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
            float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
            double d1 = Mth.lerp(d0, this.xo, this.getX()) + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
            double d2 = Mth.lerp(d0, this.yo, this.getY()) + this.random.nextDouble() * (double) this.getBbHeight();
            double d3 = Mth.lerp(d0, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
            this.level().addParticle(ParticleTypes.SMOKE, d1, d2, d3, f, f1, f2);
            if (j < 10) this.level().addParticle(ParticleTypes.SOUL, d1, d2, d3, f, f1, f2);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        //this.setRestPos(this.blockPosition());
        return super.finalizeSpawn(level, instance, spawnType, groupData, tag);
    }

    public boolean tryTeleportToTargetBehind(@Nullable Entity target) {
        double radian;
        if (target != null && target.isAlive()) {
            radian = Math.toRadians(this.getAngleBetweenEntities(this, target) + 90);
        } else {
            radian = Math.toRadians(this.random.nextInt(360));
        }
        double d0 = this.getX() - (this.random.nextInt(12) + 12) * Math.cos(radian);
        double d1 = this.getY() + (double) (this.random.nextInt(64) - 32);
        double d2 = this.getZ() - (this.random.nextInt(12) + 12) * Math.sin(radian);
        EntityTeleportEvent event = ForgeEventFactory.onEntityTeleportCommand(this, d0, d1, d2);
        if (event.isCanceled()) return false;
        boolean flag = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), false);
        if (flag) {
            if (!this.isSilent()) {
                this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
        return flag;
    }

    public void tryTeleportToRestPosition() {
        Optional<BlockPos> restPos = this.getRestPos();
        if (restPos.isPresent()) {
            BlockPos pos = restPos.get();
            if (this.getTarget() != null) return;
            EntityTeleportEvent event = ForgeEventFactory.onEntityTeleportCommand(this, pos.getX(), pos.getY(), pos.getZ());
            if (event.isCanceled()) return;
            boolean flag = this.randomTeleport(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, false);
            if (flag) {
                if (!this.isSilent()) {
                    this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
        }
    }

    public boolean checkCanSummonCorpse() {
        return !this.hasEffect(EffectInit.VERTIGO_EFFECT.get()) && this.getNearByEntities(EntityCorpse.class, 16, 16, 16, 16).size() <= 12;
    }

    public void summonCorpse(Vec3 vec3) {
        EntityType<? extends EntityCorpse> entityType = this.random.nextBoolean() ? EntityInit.CORPSE.get() : EntityInit.CORPSE_VILLAGER.get();
        EntityCorpse entity = entityType.create(this.level());
        if (entity != null && vec3 != null) {
            if (!this.level().isClientSide) {
                entity.setInitSpawn();
                entity.finalizeSpawn((ServerLevel) this.level(), this.level().getCurrentDifficultyAt(BlockPos.containing(vec3.x, vec3.y, vec3.z)), MobSpawnType.MOB_SUMMONED, null, null);
                entity.setOwner(this);
                entity.moveTo(vec3);
                this.level().addFreshEntity(entity);
            }
        }
    }

    /**
     * 评估攻击目标对于自身的威胁程度
     */
    public TargetType assessTargetAI() {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (target instanceof Player) {
                return TargetType.KNOTTY;
            }
            AttributeInstance attribute = target.getAttribute(Attributes.ATTACK_DAMAGE);
            AttributeInstance attribute1 = target.getAttribute(Attributes.ARMOR);
            float health = target.getMaxHealth();
            float attack = 0;
            float armor = 0;
            if (attribute != null) {
                attack = (float) attribute.getBaseValue();
            }
            if (attribute1 != null) {
                armor = (float) attribute1.getBaseValue();
            }
            //计算方式类似于运费计算:取体积与重量之间的最值
            float threat = Math.max(attack + armor, health / 10F);
            if (threat >= 10F && threat <= 30F) {
                return TargetType.KNOTTY;
            } else if (threat > 30F) {
                return TargetType.HARD;
            }
        }
        return TargetType.COMMON;
    }


    /**
     * Attack the specified entity using a ranged attack.
     *
     * @param entity
     * @param velocity
     */
    @Override
    public void performRangedAttack(@Nullable LivingEntity entity, float velocity) {
        EntityBloodBall bloodBall = new EntityBloodBall(this.level(), 30, this.isHeal(), this.entities.size());
        if (entity != null) {
            bloodBall.setTargetUUID(entity.getUUID());
        }
        bloodBall.setOwner(this);
        bloodBall.setPos(this.position().add(0, 5, 0));
        this.level().addFreshEntity(bloodBall);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (!this.level().isClientSide && target == null && this.hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            this.resetHealTick();
        }
        super.setTarget(target);
    }

    private void resetHealTick() {
        this.nextHealTick = NEXT_HEAL_TIME.sample(this.random);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_HEAL, false);
        this.entityData.define(DATA_REST_POSITION, Optional.empty());
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(source, pLooting, pRecentlyHit);
        Entity entity = source.getEntity();
        if (entity instanceof Player) {
            ItemEntity itementity = this.spawnAtLocation(ItemInit.HEART_OF_PAGAN.get());
            if (itementity != null) {
                itementity.setExtendedLifetime();
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(SoundInit.CORPSE_WARLOCK_HURT.get(), this.getSoundVolume(), this.getVoicePitch() - 0.5F);
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        this.playSound(SoundInit.CORPSE_WARLOCK_HURT.get(), this.getSoundVolume() * 0.8F, this.getVoicePitch() + 0.25F);
        return null;
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
    }

    @Override
    public boolean noConflictingTasks() {
        return this.getAnimation() == this.getNoAnimation();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.getRestPos().ifPresent(pos -> compound.put("spawnPos", NbtUtils.writeBlockPos(pos)));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("spawnPos")) {
            this.setRestPos(NbtUtils.readBlockPos(compound.getCompound("spawnPos")));
        }
    }

    @Override
    public boolean isGlow() {
        return this.getAnimation() == vampireAnimation || this.getAnimation() == teleportAnimation || this.getAnimation() == robustAnimation;
    }

    public boolean isHeal() {
        return this.entityData.get(DATA_IS_HEAL);
    }

    public void setIsHeal(boolean isHeal) {
        this.entityData.set(DATA_IS_HEAL, isHeal);
    }

    private boolean isNoAtRestPos() {
        Optional<BlockPos> restPos = this.getRestPos();
        return restPos.filter(blockPos -> blockPos.distSqr(blockPosition()) > 9).isPresent();
    }

    public Optional<BlockPos> getRestPos() {
        return this.entityData.get(DATA_REST_POSITION);
    }

    public void setRestPos(BlockPos pos) {
        this.entityData.set(DATA_REST_POSITION, Optional.ofNullable(pos));
    }

    public int getNextHealTick() {
        return this.nextHealTick;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D).
                add(Attributes.MOVEMENT_SPEED, 0.32D).
                add(Attributes.FOLLOW_RANGE, 36.0D).
                add(Attributes.ATTACK_DAMAGE, 5.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }

    @Override
    public Animation[] getAnimations() {
        return animations;
    }

    public enum TargetType {
        COMMON(0, 200),
        KNOTTY(1, 300),
        HARD(2, 400);

        public final int type;
        public final int duration;

        TargetType(int type, int duration) {
            this.type = type;
            this.duration = duration;
        }
    }

    static class WarlockAnimationSimpleAI extends AnimationSimpleAI<EntityCorpseWarlock> {
        private final boolean lookAtTarget;

        public WarlockAnimationSimpleAI(EntityCorpseWarlock entity, Supplier<Animation> animationSupplier, boolean lookAtTarget) {
            super(entity, animationSupplier);
            this.lookAtTarget = lookAtTarget;
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.lookAtTarget && target != null) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
            }
        }
    }

    static class WarlockSummonGoal extends AnimationSpellAI<EntityCorpseWarlock> {

        public WarlockSummonGoal(EntityCorpseWarlock spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.spellCaster.getTarget();
            boolean flag = this.spellCaster.getNextHealTick() <= 0;
            boolean flag1 = this.spellCaster.tickCount >= this.nextAttackTickCount;
            boolean flag2 = this.spellCaster.checkCanSummonCorpse();
            if (target != null && target.isAlive()) {
                if (!this.spellCaster.noConflictingTasks()) {
                    return false;
                } else {
                    return flag1 && flag2;
                }
            }
            return flag && flag1 && flag2 && this.spellCaster.getHealthPercentage() != 100;
        }

        @Override
        public boolean canContinueToUse() {
            return this.attackDelay > 0;
        }

        @Override
        protected void inSpellCasting() {
            LivingEntity target = this.spellCaster.getTarget();
            double minY = this.spellCaster.getY();
            double maxY = this.spellCaster.getY() + 1.0D;
            if (target != null) {
                minY = Math.min(target.getY(), this.spellCaster.getY());
                maxY = Math.max(target.getY(), this.spellCaster.getY()) + 1.0D;
            }
            TargetType targetType = this.spellCaster.assessTargetAI();
            int count = EntityCorpseWarlock.SPAWN_COUNT[Math.min(targetType.type, SPAWN_COUNT.length - 1)];
            for (int i = 0; i < count; ++i) {
                float f1 = (float) (this.spellCaster.getYRot() + i * (float) Math.PI * (2.0 / count));
                double x = this.spellCaster.getX() + Mth.cos(f1) * 3D;
                double z = this.spellCaster.getZ() + Mth.sin(f1) * 3D;
                this.spellCaster.summonCorpse(ModEntityUtils.checkSummonEntityPoint(this.spellCaster, x, z, minY, maxY));
            }
        }

        @Override
        protected int getSpellCastingCooling() {
            return this.spellCaster.assessTargetAI().duration;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return null;
        }

        @Override
        protected Animation getEMAnimation() {
            return this.spellCaster.summonAnimation;
        }
    }

    static class WarlockReinforceGoal extends AnimationSpellAI<EntityCorpseWarlock> {
        private final TargetingConditions reinforceConditions = TargetingConditions.forCombat().range(16).selector(e -> e instanceof EntityCorpse c && c.valuable);
        private List<EntityCorpse> list;

        public WarlockReinforceGoal(EntityCorpseWarlock spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            if (this.spellCaster.tickCount % 5 != 0) return false;
            this.list = this.spellCaster.level().getNearbyEntities(EntityCorpse.class, this.reinforceConditions, this.spellCaster, this.spellCaster.getBoundingBox().inflate(32, 16, 32));
            return !this.list.isEmpty();
        }

        @Override
        protected void inSpellCasting() {
            if (this.list != null && !this.list.isEmpty()) {
                this.list.stream().filter(c -> c.isAlive() && this.spellCaster == c.getOwner() && !c.hasEffect(EffectInit.FRENZY_EFFECT.get())).forEach(c -> {
                    c.addEffect(new MobEffectInstance(EffectInit.FRENZY_EFFECT.get(), 10 * 20, this.spellCaster.assessTargetAI().type));
                    c.valuable = false;
                });
            }
        }

        @Override
        protected int getSpellCastingCooling() {
            return this.spellCaster.assessTargetAI().duration;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return null;
        }

        @Override
        protected Animation getEMAnimation() {
            return this.spellCaster.frenzyAnimation;
        }
    }

    static class WarlockTeleportGoal extends AnimationSpellAI<EntityCorpseWarlock> {

        public WarlockTeleportGoal(EntityCorpseWarlock spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            if (super.canUse()) {
                LivingEntity target = this.spellCaster.getTarget();
                if (target != null) {
                    float distance = this.spellCaster.distanceTo(target) - target.getBbWidth() / 2f;
                    return (distance < 16F && this.spellCaster.getRandom().nextInt(200) == 0) || (distance < 8F && ModEntityUtils.checkTargetComingCloser(this.spellCaster, target)) || distance < 6F;
                }
            }
            return false;
        }

        @Override
        protected void inSpellCasting() {
        }

        @Override
        protected int getSpellCastingCooling() {
            return 150 + this.spellCaster.random.nextInt(90);
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return null;
        }

        @Override
        protected Animation getEMAnimation() {
            return this.spellCaster.teleportAnimation;
        }
    }

    static class WarlockTearApartGoal extends AnimationSpellAI<EntityCorpseWarlock> {
        private double preX;
        private double preZ;
        private double targetY;
        private double targetX;
        private double targetZ;
        private LivingEntity target;

        public WarlockTearApartGoal(EntityCorpseWarlock spellCaster) {
            super(spellCaster);
        }

        @Override
        public void start() {
            super.start();
            LivingEntity target = this.spellCaster.getTarget();
            if (target != null) {
                this.target = target;
                this.preX = target.getX();
                this.preZ = target.getZ();
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (this.target == null) {
                return;
            }
            if (this.attackDelay > 5) {
                this.spellCaster.getLookControl().setLookAt(target, 30F, 30F);
            }
            if (this.attackDelay <= 5) {
                this.spellCaster.getLookControl().setLookAt(targetX, targetY, targetZ, 30F, 30F);
            }

            if (this.attackDelay == 6) {
                double x = this.target.getX();
                this.targetY = Mth.floor(this.target.getY() + 1.0);
                double z = this.target.getZ();
                double vx = (x - this.preX) / 9;
                double vz = (z - this.preZ) / 9;
                int radius = 32;
                this.targetX = Mth.floor(x + vx * radius);
                this.targetZ = Mth.floor(z + vz * radius);
                //计算目标移动距离
                double dx = this.targetX - this.spellCaster.getX();
                double dz = this.targetZ - this.spellCaster.getZ();
                double dist = dx * dx + dz * dz;
                if (dist < 3) {
                    this.targetX = Mth.floor(this.target.getX());
                    this.targetZ = Mth.floor(this.target.getZ());
                }
            }
        }

        @Override
        protected void inSpellCasting() {
            if (!this.spellCaster.level().isClientSide) {
                double radians = 0;
                if (this.target != null) {
                    radians = Math.toRadians(this.spellCaster.getAngleBetweenEntities(this.spellCaster, this.target) + 90);
                    this.targetY = Mth.floor(this.target.getY() + 1.0);
                }
                EntityCrimsonCrack crack = new EntityCrimsonCrack(this.spellCaster.level(), this.spellCaster, new Vec3(this.targetX - 2.0F * Math.cos(radians), this.targetY + 0.56125F, this.targetZ - 2.0F * Math.sin(radians)));
                this.spellCaster.level().addFreshEntity(crack);
            }
        }

        @Override
        protected int getSpellCastingCooling() {
            return 300;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return null;
        }

        @Override
        protected Animation getEMAnimation() {
            return this.spellCaster.tearSpaceAnimation;
        }
    }

    static class WarlockRobustGoal extends AnimationSpellAI<EntityCorpseWarlock> {

        public WarlockRobustGoal(EntityCorpseWarlock spellCaster) {
            super(spellCaster);
        }

        @Override
        public boolean canUse() {
            if (super.canUse()) {
                LivingEntity target = this.spellCaster.getTarget();
                boolean flag = this.spellCaster.getHealthPercentage() < 75 || this.spellCaster.tickCount > 1200;
                if (target != null) {
                    return flag && this.spellCaster.distanceTo(target) - target.getBbWidth() / 2f < 16;
                }
            }
            return false;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.spellCaster.getAnimation() == this.spellCaster.robustAnimation;
        }

        @Override
        public void tick() {
            int tick = this.spellCaster.getAnimationTick();
            LivingEntity target = this.spellCaster.getTarget();
            if (target != null) {
                if (tick > 40) {
                    this.spellCaster.getLookControl().setLookAt(target, 30F, 30F);
                } else {
                    this.spellCaster.setYRot(this.spellCaster.yRotO);
                }
                double minY = Math.min(target.getY(), this.spellCaster.getY()) - EntityCrimsonRay.ATTACK_RADIUS;
                double maxY = Math.max(target.getY(), this.spellCaster.getY()) + 1.0D;
                if (tick >= 40 && tick % 10 == 0) {
                    for (int i = 0; i < 5; i++) {
                        double rz = target.getZ() + this.spellCaster.random.nextGaussian() * 5;
                        double rx = target.getX() + this.spellCaster.random.nextGaussian() * 5;
                        this.spawnRay(rx, rz, minY, maxY);
                    }
                }
                if (this.spellCaster.random.nextBoolean() && (tick == 45 || tick == 55 || tick == 65 || tick == 75 || tick == 85)) {
                    this.spawnRay(target.getX(), target.getZ(), minY, maxY);
                }
            }
        }

        private void spawnRay(double x, double z, double minY, double maxY) {
            if (!this.spellCaster.level().isClientSide) {
                Vec3 sPos = ModEntityUtils.checkSummonEntityPoint(this.spellCaster, x, z, minY, maxY);
                EntityCrimsonRay.PreAttack ray = new EntityCrimsonRay.PreAttack(this.spellCaster.level(), sPos, this.spellCaster);
                ray.setPos(sPos);
                this.spellCaster.level().addFreshEntity(ray);
            }
        }

        @Override
        protected void inSpellCasting() {
        }

        @Override
        protected int getSpellCastingCooling() {
            return 800 - this.spellCaster.assessTargetAI().duration;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return null;
        }

        @Override
        protected Animation getEMAnimation() {
            return this.spellCaster.robustAnimation;
        }
    }

}
