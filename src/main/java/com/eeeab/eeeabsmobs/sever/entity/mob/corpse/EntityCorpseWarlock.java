package com.eeeab.eeeabsmobs.sever.entity.mob.corpse;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.AnimationSpellAI;
import com.eeeab.animate.server.ai.animation.AnimationDie;
import com.eeeab.animate.server.ai.animation.AnimationRepel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent;
import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.KeepDistanceGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ModLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCrimsonCrack;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCrimsonRay;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityBloodBall;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
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
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityCorpseWarlock extends EntityAbsCorpse implements RangedAttackMob {
    public static final Animation DIE_ANIMATION = Animation.create(0);
    public static final Animation ATTACK_ANIMATION = Animation.create(15);
    public static final Animation SUMMON_ANIMATION = Animation.create(30);
    public static final Animation FRENZY_ANIMATION = Animation.create(30);
    public static final Animation TELEPORT_ANIMATION = Animation.create(30);
    public static final Animation RESET_POS_ANIMATION = Animation.create(30);
    public static final Animation VAMPIRE_ANIMATION = Animation.create(90);
    public static final Animation ROBUST_ANIMATION = Animation.create(100);
    public static final Animation BABBLE_ANIMATION = Animation.create(666);
    public static final Animation TEARSPACE_ANIMATION = Animation.create(30);
    private static final Animation[] ANIMATIONS = new Animation[]{
            DIE_ANIMATION,
            ATTACK_ANIMATION,
            SUMMON_ANIMATION,
            FRENZY_ANIMATION,
            TELEPORT_ANIMATION,
            RESET_POS_ANIMATION,
            VAMPIRE_ANIMATION,
            ROBUST_ANIMATION,
            BABBLE_ANIMATION,
            TEARSPACE_ANIMATION,
    };
    private static final int MAX_HURT_COUNT = 3;
    private static final int[] SPAWN_COUNT = new int[]{2, 4, 6};
    private static final UniformInt NEXT_HEAL_TIME = TimeUtil.rangeOfSeconds(15, 25);
    private static final TargetingConditions IGNORE_ALLIES = TargetingConditions.forCombat().selector(e -> {
        if (e instanceof EntityAbsCorpse) {
            return !ModConfigHandler.COMMON.others.enableSameMobsTypeInjury.get();
        }
        return true;
    });
    private static final EntityDataAccessor<Boolean> DATA_IS_HEAL = SynchedEntityData.defineId(EntityCorpseWarlock.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_REST_POSITION = SynchedEntityData.defineId(EntityCorpseWarlock.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    //周期检查是否处于出生点坐标计时
    private int nextDPTick;
    private int hurtCount;
    private int nextHealTick;
    private List<EntityCorpse> entities = new ArrayList<>();
    @OnlyIn(Dist.CLIENT)
    public Vec3[] myPos;
    public final ControlledAnimation glowControlled = new ControlledAnimation(10);

    public EntityCorpseWarlock(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
        this.active = true;
        if (this.level().isClientSide) {
            myPos = new Vec3[]{new Vec3(0, 0, 0), new Vec3(0, 0, 0)};
        }
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.ELITE;
    }


    @Override//被方块阻塞
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override//减少实体在水下的空气供应
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
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
    public int getTeamColor() {
        return this.getTeam() == null ? 11998484 : super.getTeamColor();
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.corpses.corpseWarlock.combatConfig;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == TELEPORT_ANIMATION) {
                this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 50, 0, false, false));
            }
        }
    }

    @Override
    protected void registerCorpseGoals() {
        super.registerCorpseGoals();
        this.targetSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, false).setUnseenMemoryTicks(300));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this) {
            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !EntityCorpseWarlock.this.isStunned();
            }
        });
        this.goalSelector.addGoal(1, new AnimationDie<>(this));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, SUMMON_ANIMATION, true));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, TELEPORT_ANIMATION, true) {
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
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, RESET_POS_ANIMATION, false) {
            @Override
            public void start() {
                super.start();
                this.entity.tryTeleportToRestPosition();
            }
        });
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, FRENZY_ANIMATION, true));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, TEARSPACE_ANIMATION, true));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, VAMPIRE_ANIMATION, false));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, BABBLE_ANIMATION, false));
        this.goalSelector.addGoal(1, new WarlockAnimationSimpleAI(this, ROBUST_ANIMATION, false));
        this.goalSelector.addGoal(1, new AnimationRepel<>(this, ATTACK_ANIMATION, 3F, 6, 2F, 2F, false));
        this.goalSelector.addGoal(2, new WarlockTeleportGoal(this));
        this.goalSelector.addGoal(3, new WarlockSummonGoal(this));
        this.goalSelector.addGoal(3, new WarlockTearApartGoal(this));
        this.goalSelector.addGoal(4, new WarlockReinforceGoal(this));
        this.goalSelector.addGoal(5, new WarlockRobustGoal(this));
        this.goalSelector.addGoal(6, new KeepDistanceGoal<>(this, 0.96D, 18F, 2F));
        this.goalSelector.addGoal(7, new ModLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new ModLookAtGoal(this, Mob.class, 6.0F));
    }

    @Override
    public void tick() {
        super.tick();
        this.glowControlled.updatePrevTimer();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level().isClientSide && !this.isNoAi() && !this.isStunned()) {
            if (this.isNoAnimation() && this.getTarget() == null && this.tickCount % 2 == 0 && this.random.nextInt(400) == 0) {
                this.playAnimation(BABBLE_ANIMATION);
            }
            if (this.isNoAnimation() && this.getTarget() == null && this.tickCount % 10 == 0 && this.isNoAtRestPos() && this.nextDPTick <= 0) {
                this.nextDPTick = 1200;
                this.playAnimation(RESET_POS_ANIMATION);
            }
            if (this.isNoAnimation() && this.hurtCount >= MAX_HURT_COUNT && this.getTarget() != null && this.tickCount % 2 == 0 && (!this.level().getNearbyEntities(LivingEntity.class, IGNORE_ALLIES, this, this.getBoundingBox().inflate(2)).isEmpty() || targetDistance <= 4.0F && ModEntityUtils.checkTargetComingCloser(this, this.getTarget()) || this.targetDistance < 3.5F)) {
                this.hurtCount = 0;
                this.playAnimation(ATTACK_ANIMATION);
            }
            if (this.isNoAnimation() && this.tickCount % 40 == 0 && (this.getTarget() != null || this.getHealthPercentage() != 1) && this.nextHealTick <= 0) {
                this.entities = this.level().getNearbyEntities(EntityCorpse.class, TargetingConditions.forNonCombat().selector(e -> this.getTarget() == null || (e instanceof EntityCorpse c && (!c.hasEffect(EffectInit.FRENZY_EFFECT.get()) || this.getHealthPercentage() < 0.5F) && !c.valuable)), this, this.getBoundingBox().inflate(32, 16, 32))
                        .stream()
                        .filter(e -> this == e.getOwner() || e instanceof EntityCorpseToPlayer)
                        .limit(10)
                        .collect(Collectors.toList());
                if (!this.entities.isEmpty()) {
                    this.resetHealTick();
                    this.playAnimation(VAMPIRE_ANIMATION);
                }
            }
        }
        Animation animation = this.getAnimation();
        if (animation == VAMPIRE_ANIMATION) {
            int tick = this.getAnimationTick();
            if (!this.level().isClientSide) {
                for (EntityCorpse corps : this.entities) {
                    if (corps == null || !corps.isActive()) continue;
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
                    boolean isNotEmpty = this.entities.stream().anyMatch(LivingEntity::isAlive);
                    this.entities.forEach(LivingEntity::kill);
                    this.setIsHeal(isNotEmpty && ((target != null && this.getHealthPercentage() < 0.5F) || (target == null && this.getHealthPercentage() != 1)));
                    if (!isNotEmpty) this.level().broadcastEntityEvent(this, (byte) 13);
                    this.performRangedAttack(target, !isNotEmpty ? 5F : this.entities.size());
                } else if (tick > 60) {
                    this.setDeltaMovement(0, 0, 0);
                    if (!this.isHeal() && target != null) {
                        this.getLookControl().setLookAt(target, 30F, 30F);
                    }
                } else this.setDeltaMovement(0, 0.12F, 0);
            } else {
                if (tick == 20) {
                    Vec3 vec3 = this.position();
                    float yRot = this.yBodyRot;
                    float offsetX = (float) (Math.cos(Math.toRadians(yRot + 90)) * 1.5F);
                    float offsetZ = (float) (Math.sin(Math.toRadians(yRot + 90)) * 1.5F);
                    this.myPos[0] = new Vec3(vec3.x + offsetX, vec3.y + getBbHeight() * 0.6, vec3.z + offsetZ);
                    AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.CRIMSON.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 40F, 1, 1, 1, 1, 1, 30, true, false, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(this.myPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.oscillate(0F, 30F, 12), false),
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
                }
            }
        } else if (animation == TELEPORT_ANIMATION || animation == RESET_POS_ANIMATION) {
            int tick = this.getAnimationTick();
            if (tick == 1) {
                this.setInvisible(true);
                this.doTeleportEffect();
            } else if (tick == 15) {
                this.doTeleportEffect();
            } else if (tick >= 20) {
                this.setInvisible(this.hasEffect(MobEffects.INVISIBILITY));
            }
        } else if (animation == BABBLE_ANIMATION) {
            if (this.getTarget() != null && this.distanceTo(this.getTarget()) < 12) {
                this.playAnimation(TELEPORT_ANIMATION);//中断施法
            }
            this.doCrimsonEyeEffect(2.5F);
        } else if (animation == ATTACK_ANIMATION) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            if (this.level().isClientSide && this.getAnimationTick() == 5) {
                ModParticleUtils.annularParticleOutburst(this.level(), 30, ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.2, 0);
            }
        } else if (animation == ROBUST_ANIMATION) {
            int tick = this.getAnimationTick();
            if (tick > 35) {
                this.doCrimsonEyeEffect(-1F);
                this.setDeltaMovement(0, 0, 0);
            } else this.setDeltaMovement(0, 0.25F, 0);
        }
    }

    private void doAroundSelfEffect(ParticleOptions particleOptions) {
        for (int i = 0; i < 10; ++i) {
            double d0 = this.random.nextGaussian() * 0.15D;
            double d1 = this.random.nextGaussian() * 0.15D;
            double d2 = this.random.nextGaussian() * 0.15D;
            this.level().addParticle(particleOptions, this.getRandomX(1.5D), this.getRandomY() + 0.15D, this.getRandomZ(1.5D), d0, d1, d2);
        }
    }


    private void doCrimsonEyeEffect(float offset) {
        if (this.level().isClientSide) {
            int tick = this.getAnimationTick();
            if (tick % 20 == 0) {
                this.myPos[0] = this.getPosOffset(false, offset, 0F, getBbHeight() * 1.5F);
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
                    new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 8, 0, 0, 0, 0.12F, 0.95, 0.35, 0.35, 0.75, true, true, new ParticleComponent[]{
                            new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, AnimData.KeyTrack.startAndEnd(1, 0))}, false)
            });
        }
    }


    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.nextDPTick > 0) this.nextDPTick--;
            if (this.nextHealTick > 0) this.nextHealTick--;
        }
        this.glowControlled.incrementOrDecreaseTimer(this.isGlow());
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (this.level().isClientSide) {
            return false;
        } else if (this.isInvulnerableTo(source)) {
            return false;
        }
        Animation animation = this.getAnimation();
        if (animation == TELEPORT_ANIMATION) {
            return false;
        } else if (animation == VAMPIRE_ANIMATION || animation == ROBUST_ANIMATION) {
            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) damage *= 0.2F;
        }
        if (entity != null) {
            if (checkAttackDistance(source, 576D, entity)) return false;
            if (animation == BABBLE_ANIMATION) this.playAnimation(TELEPORT_ANIMATION);
        }
        boolean flag = super.hurt(source, damage);
        if (flag && entity != null && this.random.nextFloat() < 0.6F) this.hurtCount++;
        return flag;
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }
        if (source.is(ModTagKey.MAGIC_RESISTANT_TO)) {
            damage *= 0.5F;//魔法造成的伤害减少50%
        }
        return damage;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 13) {
            this.doAroundSelfEffect(ParticleTypes.ANGRY_VILLAGER);
        } else if (id == 14) {
            this.doAroundSelfEffect(ParticleInit.WARLOCK_HEAL.get());
        } else super.handleEntityEvent(id);
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
            radian = Math.toRadians(this.getYRot() + this.random.nextIntBetweenInclusive(-180, 180));
        }
        double d0 = this.getX() - (this.random.nextInt(12) + 12) * Math.cos(radian);
        double d1 = this.getY() + (double) (this.random.nextInt(64) - 32);
        double d2 = this.getZ() - (this.random.nextInt(12) + 12) * Math.sin(radian);
        BlockPos restPos = this.getRestPos().orElse(null);
        if (restPos != null && this.position().distanceTo(restPos.getCenter()) > 32) {
            d0 = restPos.getX() + 0.5F;
            d1 = restPos.getY();
            d2 = restPos.getZ() + 0.5F;
        }
        EntityTeleportEvent event = ForgeEventFactory.onEntityTeleportCommand(this, d0, d1, d2);
        if (event.isCanceled()) return false;
        boolean flag = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), false);
        if (flag) {
            if (!this.isSilent()) {
                this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.level().playSound(null, BlockPos.containing(this.position()), SoundEvents.BELL_RESONATE, this.getSoundSource(), 1.0F, 1.1F);
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
        return !this.isStunned() && this.getNearByEntities(EntityCorpse.class, 16, 16, 16, 16).size() <= 12;
    }

    public void summonCorpse(Vec3 vec3) {
        EntityType<? extends EntityCorpse> entityType = this.random.nextBoolean() ? EntityInit.CORPSE.get() : EntityInit.CORPSE_VILLAGER.get();
        EntityCorpse entity = entityType.create(this.level());
        if (entity != null && vec3 != null) {
            if (!this.level().isClientSide) {
                entity.finalizeSpawn((ServerLevel) this.level(), this.level().getCurrentDifficultyAt(BlockPos.containing(vec3.x, vec3.y, vec3.z)), MobSpawnType.MOB_SUMMONED, null, null);
                entity.setOwner(this);
                entity.moveTo(vec3);
                this.level().addFreshEntity(entity);
                entity.afterSpawn();
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
                switch (this.level().getDifficulty()) {
                    case HARD -> {
                        return TargetType.HARD;
                    }
                    case NORMAL -> {
                        return TargetType.KNOTTY;
                    }
                    default -> {
                        return TargetType.COMMON;
                    }
                }
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
     * 使用远程攻击攻击指定的实体
     *
     * @param entity   攻击目标
     * @param strength 强度
     */
    @Override
    public void performRangedAttack(@Nullable LivingEntity entity, float strength) {
        EntityBloodBall bloodBall = new EntityBloodBall(this.level(), 30, this.isHeal(), (int) strength);
        if (entity != null) {
            bloodBall.setTargetUUID(entity.getUUID());
        }
        bloodBall.setOwner(this);
        bloodBall.setPos(this.position().add(0, 5, 0));
        this.level().addFreshEntity(bloodBall);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (!this.level().isClientSide && target == null && this.isStunned()) {
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
        if (source.getEntity() != null || this.getTarget() != null) {
            ItemEntity itementity = this.spawnAtLocation(ItemInit.HEART_OF_PAGAN.get());
            ItemEntity itementity2 = this.spawnAtLocation(ItemInit.SOUL_SUMMON_NECKLACE.get());
            if (itementity != null && itementity2 != null) {
                itementity.setExtendedLifetime();
                itementity.setGlowingTag(true);
                itementity2.setExtendedLifetime();
                itementity2.setGlowingTag(true);
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
        return DIE_ANIMATION;
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
        Animation animation = this.getAnimation();
        return this.isAlive() && (animation == VAMPIRE_ANIMATION || animation == ATTACK_ANIMATION || animation == TEARSPACE_ANIMATION || animation == ROBUST_ANIMATION || animation == BABBLE_ANIMATION);
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
        return createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.FOLLOW_RANGE, 36.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(),1D);
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
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

        public WarlockAnimationSimpleAI(EntityCorpseWarlock entity, Animation animation, boolean lookAtTarget) {
            super(entity, animation);
            this.lookAtTarget = lookAtTarget;
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.lookAtTarget && target != null) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
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
            boolean flag3 = this.spellCaster.isNoAnimation() && !this.spellCaster.isStunned();
            if (target != null && target.isAlive()) {
                if (!flag3) {
                    return false;
                } else {
                    return flag1 && flag2;
                }
            }
            return flag && flag1 && flag2 && flag3 && this.spellCaster.getHealthPercentage() != 1;
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
        protected Animation getEntityAnimation() {
            return SUMMON_ANIMATION;
        }
    }

    static class WarlockReinforceGoal extends AnimationSpellAI<EntityCorpseWarlock> {
        private final TargetingConditions reinforceConditions = TargetingConditions.forCombat().range(32);
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
                this.list.stream().filter(e -> e.isAlive() && (this.spellCaster == e.getOwner() || e instanceof EntityCorpseToPlayer) && !e.hasEffect(EffectInit.FRENZY_EFFECT.get()))
                        .forEach(corpse -> {
                            if (corpse instanceof EntityCorpseToPlayer corpseToPlayer) {
                                corpseToPlayer.brainwashing();
                            }
                            if (corpse.valuable) {
                                int type = this.spellCaster.assessTargetAI().type;
                                corpse.addEffect(new MobEffectInstance(EffectInit.FRENZY_EFFECT.get(), 200 + 100 * type, type));
                                corpse.valuable = false;
                            }
                            corpse.setTarget(this.spellCaster.getTarget());
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
        protected Animation getEntityAnimation() {
            return FRENZY_ANIMATION;
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
                    return ((distance < 16F || !this.spellCaster.getSensing().hasLineOfSight(target)) && this.spellCaster.getRandom().nextInt(100) == 0) || (distance < 8F && ModEntityUtils.checkTargetComingCloser(this.spellCaster, target)) || distance < 6F;
                }
            }
            return false;
        }

        @Override
        protected void inSpellCasting() {
        }

        @Override
        protected int getSpellCastingCooling() {
            return 150 + this.spellCaster.random.nextInt(101);
        }

        @Nullable
        @Override
        protected SoundEvent getSpellCastingSound() {
            return null;
        }

        @Override
        protected Animation getEntityAnimation() {
            return TELEPORT_ANIMATION;
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
            this.spellCaster.lookAt(target, 30F, 30F);
            if (this.attackDelay == 7) {
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
                double radians = 0, targetWidth = 0;
                if (this.target != null) {
                    radians = Math.toRadians(this.target.yBodyRot + 180);
                    targetWidth = Math.min(this.target.getBbWidth(), 1.5);
                }
                Vec3 pos = new Vec3(this.targetX - targetWidth * Math.cos(radians), this.targetY + 0.56125F, this.targetZ - targetWidth * Math.sin(radians));
                this.spellCaster.level().addFreshEntity(new EntityCrimsonCrack(this.spellCaster.level(), this.spellCaster, pos));
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
        protected Animation getEntityAnimation() {
            return TEARSPACE_ANIMATION;
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
                boolean flag = this.spellCaster.getHealthPercentage() < 0.75F || this.spellCaster.tickCount > 1200;
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
            return this.spellCaster.getAnimation() == ROBUST_ANIMATION;
        }

        @Override
        public void tick() {
            int tick = this.spellCaster.getAnimationTick();
            LivingEntity target = this.spellCaster.getTarget();
            if (target != null) {
                if (tick > 30) {
                    this.spellCaster.getLookControl().setLookAt(target, 180F, 180F);
                    this.spellCaster.lookAt(target, 180F, 180F);
                } else {
                    this.spellCaster.setYRot(this.spellCaster.yRotO);
                }
                double minY = Math.min(target.getY(), this.spellCaster.getY()) - 16;
                double maxY = Math.max(target.getY(), this.spellCaster.getY()) + 1.0D;
                if (tick >= 40 && tick % 10 == 0) {
                    for (int i = 0; i < 5; i++) {
                        double rz = target.getZ() + this.spellCaster.random.nextGaussian() * 5;
                        double rx = target.getX() + this.spellCaster.random.nextGaussian() * 5;
                        this.spawnRay(rx, rz, minY, maxY);
                    }
                }
                if ((tick == 45 || tick == 55 || tick == 65 || tick == 75 || tick == 85) && this.spellCaster.random.nextBoolean()) {
                    this.spawnRay(target.getX(), target.getZ(), minY, maxY);
                }
            }
        }

        private void spawnRay(double x, double z, double minY, double maxY) {
            if (!this.spellCaster.level().isClientSide) {
                Vec3 sPos = ModEntityUtils.checkSummonEntityPoint(this.spellCaster, x, z, minY, maxY);
                EntityCrimsonRay.PreAttack ray = new EntityCrimsonRay.PreAttack(this.spellCaster.level(), sPos, this.spellCaster, 16 + this.spellCaster.getRandom().nextInt(5));
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
        protected Animation getEntityAnimation() {
            return ROBUST_ANIMATION;
        }
    }
}
