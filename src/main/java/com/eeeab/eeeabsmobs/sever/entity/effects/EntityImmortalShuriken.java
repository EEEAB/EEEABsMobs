package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleOrb;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ModMobType;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.EMMathUtils;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import com.eeeab.eeeabsmobs.sever.util.damage.EMDamageSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityImmortalShuriken extends EntityMagicEffects {
    private static final double TRACKING_DISTANCE_THRESHOLD = 3D;
    private static final double RE_FIND_RANGE = 20D;
    private static final int MAX_ACTIVE = 600;
    private LivingEntity target;
    private UUID targetUUID;
    private boolean closeFlag;
    private double preX;
    private double preY;
    private double preZ;
    private int difficultyLevel;
    private static final EntityDataAccessor<Integer> DATA_DURATION = SynchedEntityData.defineId(EntityImmortalShuriken.class, EntityDataSerializers.INT);
    private final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = entity -> entity.getMobType() != ModMobType.IMMORTAL && !entity.getType().is(EMTagKey.IMMORTAL_IGNORE_HUNT_TARGETS) && entity.isAttackable()
            && (entity instanceof Enemy || entity instanceof NeutralMob || (entity instanceof Player player && !player.isCreative() && !player.isSpectator())) && (this.caster == null || !this.caster.isAlliedTo(entity));

    public EntityImmortalShuriken(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public EntityImmortalShuriken(Level level, LivingEntity caster, LivingEntity target, int duration) {
        super(EntityInit.IMMORTAL_SHURIKEN.get(), level);
        this.caster = caster;
        this.target = target;
        this.difficultyLevel = this.level().getDifficulty().getId();
        this.setDuration(duration);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide || (caster == null || !caster.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && this.checkCanShoot()) {
                this.onHit(hitresult);
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.discard();
            }

            if (!this.level().isClientSide) {
                if (this.target != null) {
                    this.preX = this.target.getX();
                    this.preY = this.target.getY(0.3);
                    this.preZ = this.target.getZ();
                } else if (this.targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
                    this.target = (LivingEntity) serverLevel.getEntity(this.targetUUID);
                }

                if (!this.closeFlag && this.tickCount % 5 + this.random.nextInt(3) == 0 && (this.target == null || !this.target.isAlive())) {
                    LivingEntity entity = this.reFindTarget();
                    if (entity == null) {
                        this.target = null;
                        this.targetUUID = null;
                        this.closeFlag = false;
                    } else {
                        this.target = entity;
                    }
                }
            }

            this.checkInsideBlocks();

            if (this.tickCount > MAX_ACTIVE) {
                this.discard();
            } else if (!this.level().isClientSide) {
                if (this.checkCanShoot()) {
                    Vec3 direction = this.findTargetPoint();
                    double distance = direction.length();
                    if (distance == 0) {
                        this.setDeltaMovement(Vec3.ZERO);
                        return;
                    }
                    float distanced = this.distanceToPre();
                    double speed = Math.min(distance * 0.4 + (0.2 * EMMathUtils.getTickFactor(distanced, 20, true)), 0.6);
                    if (this.closeFlag || distanced <= TRACKING_DISTANCE_THRESHOLD) {
                        if (!this.closeFlag)
                            this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.03, 0).normalize());
                        this.setDeltaMovement(this.getDeltaMovement().scale(1.01));
                        this.closeFlag = true;
                        return;
                    }
                    Vec3 movement = direction.scale(speed);
                    this.setDeltaMovement(movement);
                } else {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95));
                }
                ProjectileUtil.rotateTowardsMovement(this, 0.5F);
            } else {
                Vec3 movement = this.getDeltaMovement();
                //float partialTicks = Minecraft.getInstance().getFrameTime();
                //double interpolatedX = Mth.lerp(partialTicks, this.xOld, this.getX());
                //double interpolatedY = Mth.lerp(partialTicks, this.yOld + this.getBbHeight() / 2, this.getY(0.5));
                //double interpolatedZ = Mth.lerp(partialTicks, this.zOld, this.getZ());
                //AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_ORB.get(), interpolatedX, interpolatedY, interpolatedZ, movement.x, movement.y, movement.z, true, 0, 0, 0, 0, 0F,
                //        0.36F, 0.74F, 0.98F, 1, 1, 1, true, false, false, new ParticleComponent[]{
                //                new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 6, 0, 0, 0, 0.12F, 0.36F, 0.74F, 0.98F, 1, true, true,
                //                        new ParticleComponent[]{
                //                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, AnimData.KeyTrack.startAndEnd(0.7125F, 0F))
                //                        }, false)
                //        });
                boolean shoot = this.checkCanShoot() && movement.horizontalDistanceSqr() > 2.5000003E-7D;
                double ySpeed = shoot ? -movement.y * 0.25F : 0.125F + 0.125F * this.random.nextFloat();
                double xSpeed = -movement.x * 0.25F;
                double zSpeed = -movement.z * 0.25F;
                for (int i = 0; i < (shoot ? 5 : 2); i++) {
                    double x = this.getX() + this.random.nextGaussian() * 0.2;
                    double y = this.getY(0.3) + this.random.nextGaussian() * 0.1D;
                    double z = this.getZ() + this.random.nextGaussian() * 0.2;
                    float colorOffset = 0.15F * this.random.nextFloat();
                    this.level().addParticle(new ParticleDust.DustData(ParticleInit.DUST.get(), 0.18F - colorOffset, 0.44F - colorOffset, 0.6F - colorOffset, (float) (5D + random.nextDouble() * 5D), 12, ParticleDust.EnumDustBehavior.SHRINK, 1, true), x, y, z, xSpeed, ySpeed, zSpeed);
                }
                if (this.tickCount % 5 == 0) {
                    this.level().addParticle(new ParticleOrb.OrbData(0.08F, 0.12F, 0.17F, 2F, 10), this.getRandomX(0.5), this.getY(0.5), this.getRandomZ(0.5), xSpeed, ySpeed, xSpeed);
                }
            }
        } else {
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            Vec3 movement = this.getDeltaMovement();
            ParticleDust.DustData particle1 = new ParticleDust.DustData(ParticleInit.DUST.get(), 0.18F, 0.44F, 0.6F, 15F, 20, ParticleDust.EnumDustBehavior.SHRINK, 0.9F, true);
            ParticleOrb.OrbData particle2 = new ParticleOrb.OrbData(0.08F, 0.12F, 0.17F, 2F, 15);
            ModParticleUtils.roundParticleOutburst(this.level(), 8, new ParticleOptions[]{particle1, particle2}, this.getX() + movement.x, this.getY(0.3), this.getZ() + movement.z, 0.25F);
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity hitEntity) {
            float baseDamage = 1.5F;
            MobEffectInstance instance = hitEntity.getEffect(EffectInit.ERODE_EFFECT.get());
            if (instance != null) baseDamage += (instance.getAmplifier() + 1) * 0.5F;
            if (entity.hurt(EMDamageSource.immortalMagicAttack(this, this.caster), baseDamage + hitEntity.getMaxHealth() * 0.025F)) {
                ModEntityUtils.addEffectStackingAmplifier(hitEntity, EffectInit.ERODE_EFFECT.get(), 300, this.difficultyLevel, true, true, true, true);
            }
        } else {
            entity.hurt(this.damageSources().magic(), 11.4514F);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DURATION, 20);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.target != null) {
            compoundTag.putUUID("target", this.target.getUUID());
        }
        compoundTag.putInt("difficultyLevel", this.difficultyLevel);
        compoundTag.putInt("duration", this.getDuration());
        compoundTag.putDouble("preX", this.preX);
        compoundTag.putDouble("preY", this.preY);
        compoundTag.putDouble("preZ", this.preZ);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("target")) {
            this.targetUUID = compoundTag.getUUID("target");
        }
        this.difficultyLevel = compoundTag.getInt("difficultyLevel");
        this.setDuration(compoundTag.getInt("duration"));
        this.preX = compoundTag.getDouble("preX");
        this.preY = compoundTag.getDouble("preY");
        this.preZ = compoundTag.getDouble("preZ");
    }

    private boolean checkCanShoot() {
        return this.tickCount > this.getDuration();
    }

    private @Nullable LivingEntity reFindTarget() {
        return this.level().getEntitiesOfClass(LivingEntity.class, ModEntityUtils.makeAABBWithSize(this.getX(), this.getY(), this.getZ(), 0, RE_FIND_RANGE, RE_FIND_RANGE, RE_FIND_RANGE), LIVING_ENTITY_SELECTOR).stream().findFirst().orElse(null);
    }

    private boolean canHitEntity(Entity entity) {
        if (!entity.canBeHitByProjectile() || (entity == caster) || (entity instanceof EntityAbsImmortal && EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get())) {
            return false;
        } else {
            return caster == null || !caster.isPassengerOfSameVehicle(entity);
        }
    }

    private float distanceToPre() {
        float d0 = (float) (this.getX() - this.preX);
        float d1 = (float) (this.getY() - this.preY);
        float d2 = (float) (this.getZ() - this.preZ);
        return Mth.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    private Vec3 findTargetPoint() {
        if (this.target == null) return Vec3.ZERO;
        double d1 = this.preX - this.getX();
        double d2 = this.preY - this.getY();
        double d3 = this.preZ - this.getZ();
        double d0 = Math.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        return (new Vec3(d1, d2 + d0, d3)).normalize();
    }

    public int getDuration() {
        return this.entityData.get(DATA_DURATION);
    }

    public void setDuration(int duration) {
        this.entityData.set(DATA_DURATION, duration);
    }
}
