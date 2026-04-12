package com.eeeab.eeeabsmobs.sever.entity.effect.projectile;

import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityExplode;
import com.eeeab.eeeabsmobs.sever.entity.effect.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRealmWarden;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class EntityAnnihilatorMissile extends Projectile implements IEntity, VariantHolder<EntityAnnihilatorMissile.ElementType> {
    private static final EntityDataAccessor<Integer> DATA_ELEMENT = SynchedEntityData.defineId(EntityAnnihilatorMissile.class, EntityDataSerializers.INT);
    private static final int MAX_ACTIVE = 200;
    private float damage = 8F;
    @Nullable
    private Vec3 targetPos;
    @Nullable
    private LivingEntity target;
    private float startSpeed;
    private int trackingDelayTime = -1;
    private int accelerationStartTick = -1;
    private boolean sentSpikeEvent;

    public EntityAnnihilatorMissile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public EntityAnnihilatorMissile(Level level, LivingEntity caster, ElementType element) {
        this(EntityInit.ANNIHILATOR_MISSILE.get(), level);
        this.setOwner(caster);
        this.setVariant(element);
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if ((hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))) {
                if (!this.sentSpikeEvent) {
                    this.level().broadcastEntityEvent(this, (byte) 4);
                    this.sentSpikeEvent = true;
                }
                this.onHit(hitresult);
            }
            if (!this.level().isClientSide) {
                if (this.tickCount == trackingDelayTime && target != null) {
                    this.targetPos = this.target.position().add((this.random.nextDouble() - 0.5) * 4.5, this.target.getBbHeight() * 0.1D, (this.random.nextDouble() - 0.5) * 4.5);
                } else if (this.tickCount > trackingDelayTime && this.targetPos != null) {
                    Vec3 toTarget = this.targetPos.subtract(this.position());
                    Vec3 dirToTarget = toTarget.normalize();
                    Vec3 currentMove = this.getDeltaMovement();
                    double currentSpeed = currentMove.length();
                    Vec3 currentDir = currentMove.normalize();
                    double turnFactor = 0.16;
                    Vec3 newDir = currentDir.scale(1.0 - turnFactor).add(dirToTarget.scale(turnFactor)).normalize();
                    if (accelerationStartTick == -1) {
                        accelerationStartTick = this.tickCount;
                        startSpeed = (float) currentSpeed;
                    }
                    int ticksSinceAccel = this.tickCount - accelerationStartTick;
                    int accelerationDuration = 15;
                    float progress = Mth.clamp(ticksSinceAccel / (float) accelerationDuration, 0.0F, 1.0F);
                    float targetSpeed = 2F;
                    float newSpeed = startSpeed + (targetSpeed - startSpeed) * progress;
                    this.setDeltaMovement(newDir.scale(newSpeed));
                }
            }
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 1F);
            float f = 1F;
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * f1, d1 - vec3.y * f1, d2 - vec3.z * f1, vec3.x, vec3.y, vec3.z);
                }
                f = 0.9F;
            }
            if (this.tickCount > MAX_ACTIVE) {
                this.discard();
            }
            if (this.level().isClientSide && this.tickCount == 2) {
                ElementType variant = this.getVariant();
                AdvancedParticleBase.spawnParticle(this.level(), ParticleInit.ADV_ORB.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F,
                        1, 1, 1, 1, 1, MAX_ACTIVE - 3, true, false, false, new ParticleComponent[]{
                                new ParticleComponent.PinLocationWithEntity(this, new Vec3(0, getBbHeight() / 2, 0)),
                                new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 4, 0, 0, 0, 0.12F, 1, 1, 1, 0.75, true, true,
                                        new ParticleComponent[]{
                                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0.5F, 1F)),
                                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, AnimData.KeyTrack.startAndEnd(1F, 0F)),
                                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.RED, AnimData.startAndEnd(variant.startColor.r, variant.endColor.r)),
                                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.GREEN, AnimData.startAndEnd(variant.startColor.g, variant.endColor.g)),
                                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.BLUE, AnimData.startAndEnd(variant.startColor.b, variant.endColor.b))
                                        }, false),
                        });
            }
            //if (this.tickCount > 3) {
            //    Vec3 deltaMovement = this.getDeltaMovement();
            //    double speed = deltaMovement.length();
            //    int particleCount = (int) (1 + speed * 2);
            //    particleCount = Math.min(particleCount, 3);
            //    double trailLength = speed * 0.5;
            //    for (int i = 0; i < particleCount; i++) {
            //        double trailProgress = this.random.nextDouble() * trailLength;
            //        double trailX = this.getX() - deltaMovement.x() * trailProgress;
            //        double trailY = this.getY() - deltaMovement.y() * trailProgress;
            //        double trailZ = this.getZ() - deltaMovement.z() * trailProgress;
            //        double spread = 0.1;
            //        double offsetX = (this.random.nextDouble() - 0.5) * spread;
            //        double offsetY = (this.random.nextDouble() - 0.5) * spread * 0.5;
            //        double offsetZ = (this.random.nextDouble() - 0.5) * spread;
            //        double speedVariation = 0.7 + this.random.nextDouble() * 0.6;
            //        this.level().addParticle(
            //                ParticleTypes.SMOKE,
            //                trailX + offsetX, trailY + offsetY, trailZ + offsetZ,
            //                -deltaMovement.x() * 0.25F * speedVariation,
            //                -deltaMovement.y() * 0.25F * speedVariation,
            //                -deltaMovement.z() * 0.25F * speedVariation
            //        );
            //    }
            //}
            this.setDeltaMovement(vec3.scale(f));
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            ElementType variant = this.getVariant();
            Vec3 center = this.position().add(0, getBbHeight() / 2, 0);
            double x = center.x;
            double y = center.y;
            double z = center.z;
            Entity owner = this.getOwner();
            float range = 5;
            AABB aabb = ModEntityUtils.makeAABBWithSize(x, y, z, 0, range, range, range);
            for (Entity target : this.level().getEntities(this, aabb, e -> e != owner && (owner == null || !owner.isAlliedTo(e)) && !e.ignoreExplosion())) {
                double dist = Math.sqrt(target.distanceToSqr(center)) / range;
                if (dist <= 1) {
                    double d0 = target.getX() - x;
                    double d1 = target.getEyeY() - y;
                    double d2 = target.getZ() - z;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    if (d3 != 0) {
                        if (variant == ElementType.VOLT || variant == ElementType.SPARKFERNO) {
                            if (target instanceof LivingEntity living) living.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(),
                                    300, 0, false, false, true), this);
                        }
                        if (variant == ElementType.BLAZE || variant == ElementType.SPARKFERNO) {
                            if (!target.fireImmune()) target.setSecondsOnFire(5);
                        }
                        float damage = getDamage();
                        float percent = 1;
                        if (!(owner instanceof EntityRealmWarden)) {
                            percent = EntityExplode.getSeenPercent(new Vec3(x, y, z), target);
                        }
                        double strength = (1.0D - dist) * percent;
                        if (target instanceof LivingEntity livingEntity) {
                            strength *= (float) (1.0F - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                            if (owner instanceof IMob mob) {
                                damage += mob.getDamageAmountByTargetHealthPct(livingEntity);
                            }
                        }
                        target.hurt(this.damageSources().explosion(this, owner), damage * percent);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        d0 *= strength;
                        d1 *= strength;
                        d2 *= strength;
                        target.setDeltaMovement(target.getDeltaMovement().add(d0, d1, d2));
                    }
                }
            }
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            double x = this.getX();
            double y = this.getY(0.5);
            double z = this.getZ();
            this.level().playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 3.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            ElementType variant = this.getVariant();
            if (variant == ElementType.BLAZE) {
                this.level().addParticle(ParticleInit.BLAZE_EXPLOSION_EMITTER.get(), x, y, z, 2.0D, 2.0D, 0.0D);
            } else if (variant == ElementType.VOLT) {
                this.level().addParticle(ParticleInit.VOLT_EXPLOSION_EMITTER.get(), x, y, z, 2.0D, 2.0D, 0.0D);
            } else {
                for (int i = 0; i < 2; i++) {
                    this.level().addParticle(ParticleInit.BLAZE_EXPLOSION.get(), this.getRandomX(2), this.getRandomY(), this.getRandomZ(2), 0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleInit.VOLT_EXPLOSION.get(), this.getRandomX(2), this.getRandomY(), this.getRandomZ(2), 0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }
        d0 *= 64.0D;
        return distance < d0 * d0;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ELEMENT, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_ELEMENT, compound.getInt("element"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("element", this.entityData.get(DATA_ELEMENT));
    }

    @Override
    public void setVariant(ElementType variant) {
        this.entityData.set(DATA_ELEMENT, variant.id);
    }

    @Override
    public ElementType getVariant() {
        return ElementType.byId(this.entityData.get(DATA_ELEMENT));
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    public float getDamage() {
        if (this.getOwner() instanceof EntityRealmWarden) {
            return ModConfigHandler.COMMON.mobs.relicrons.realmwarden.annihilatorMissile.damage.get().floatValue();
        }
        return this.getOwner() instanceof EntityRelicAnnihilator ? ModConfigHandler.COMMON.mobs.relicrons.relicAnnihilator.annihilatorMissile.damage.get().floatValue() : damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setTargetPos(@Nullable LivingEntity target, Vec3 targetPos) {
        this.target = target;
        this.targetPos = targetPos;
        this.trackingDelayTime = 3 + this.random.nextInt(8);
    }

    public enum ElementType implements StringRepresentable {
        VOLT(0, new ColorInfo(134 / 255F, 241 / 255F, 1.0F), new ColorInfo(54 / 255F, 148 / 255F, 168 / 255F), "volt"),
        BLAZE(1, new ColorInfo(251 / 255F, 241 / 255F, 55 / 255F), new ColorInfo(222 / 255F, 112 / 255F, 37 / 255F), "blaze"),
        SPARKFERNO(2, new ColorInfo(134 / 255F, 241 / 255F, 1.0F), new ColorInfo(251 / 255F, 241 / 255F, 55 / 255F), "sparkferno");;

        private static final IntFunction<ElementType> BY_ID = ByIdMap.sparse(c -> c.id, values(), VOLT);
        public final int id;
        public final String name;
        public final ColorInfo startColor;
        public final ColorInfo endColor;

        ElementType(int id, ColorInfo startColor, ColorInfo endColor, String name) {
            this.id = id;
            this.startColor = startColor;
            this.endColor = endColor;
            this.name = name;
        }

        public static ElementType byId(int id) {
            return BY_ID.apply(id);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public record ColorInfo(float r, float g, float b) {
        }
    }
}
