package com.eeeab.eeeabsmobs.sever.entity.effect.projectile;

import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effect.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
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
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class EntityAnnihilatorMissile extends Projectile implements IEntity, VariantHolder<EntityAnnihilatorMissile.ElementType> {
    private static final EntityDataAccessor<Integer> DATA_ELEMENT = SynchedEntityData.defineId(EntityAnnihilatorMissile.class, EntityDataSerializers.INT);
    private static final int MAX_ACTIVE = 200;
    private float damage = 8F;

    public EntityAnnihilatorMissile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public EntityAnnihilatorMissile(Level level, LivingEntity caster, ElementType element) {
        this(EntityInit.ANNIHILATOR_MISSILE.get(), level);
        this.setOwner(caster);
        this.setRot(caster.getYRot(), caster.getXRot());
        this.setVariant(element);
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (this.tickCount >= MAX_ACTIVE || (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))) {
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.onHit(hitresult);
            }
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            if (vec3.lengthSqr() != 0.0D) {
                double distance = vec3.horizontalDistance();
                this.setYRot((float) (Mth.atan2(vec3.z, vec3.x) * (double) (180F / (float) Math.PI)) + 90.0F);
                this.setXRot((float) (Mth.atan2(distance, vec3.y) * (double) (180F / (float) Math.PI)) - 90.0F);
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }
            float f = 1.005F;
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * f1, d1 - vec3.y * f1, d2 - vec3.z * f1, vec3.x, vec3.y, vec3.z);
                }
                f = 0.8F;
            }
            if (this.tickCount > 3) {
                Vec3 deltaMovement = this.getDeltaMovement();
                double speed = deltaMovement.length();
                int particleCount = (int) (2 + speed * 3);
                particleCount = Math.min(particleCount, 5);
                double trailLength = speed * 0.5;
                for (int i = 0; i < particleCount; i++) {
                    double trailProgress = this.random.nextDouble() * trailLength;
                    double trailX = this.getX() - deltaMovement.x() * trailProgress;
                    double trailY = this.getY() - deltaMovement.y() * trailProgress;
                    double trailZ = this.getZ() - deltaMovement.z() * trailProgress;
                    double spread = 0.1;
                    double offsetX = (this.random.nextDouble() - 0.5) * spread;
                    double offsetY = (this.random.nextDouble() - 0.5) * spread * 0.5;
                    double offsetZ = (this.random.nextDouble() - 0.5) * spread;
                    double speedVariation = 0.7 + this.random.nextDouble() * 0.6;
                    this.level().addParticle(
                            ParticleInit.LARGE_SMOKE.get(),
                            trailX + offsetX, trailY + offsetY, trailZ + offsetZ,
                            -deltaMovement.x() * 0.25F * speedVariation,
                            -deltaMovement.y() * 0.25F * speedVariation,
                            -deltaMovement.z() * 0.25F * speedVariation
                    );
                }
            }
            this.setDeltaMovement(vec3.scale(f));
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (!this.level().isClientSide) {
            ElementType variant = this.getVariant();
            double x = this.getX();
            double y = this.getY(0.5);
            double z = this.getZ();
            Entity owner = this.getOwner();
            AABB aabb = ModEntityUtils.makeAABBWithSize(x, y, z, 0, 6, 6, 6);
            for (Entity entity : this.level().getEntities(this, aabb, e -> e != owner || !e.ignoreExplosion())) {
                double d12 = Math.sqrt(entity.distanceToSqr(this.position())) / 6;
                if (d12 <= 1) {
                    double d0 = entity.getX() - x;
                    double d1 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - y;
                    double d2 = entity.getZ() - z;
                    if (Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2) != 0) {
                        if (variant == ElementType.VOLT || variant == ElementType.SPARKFERNO) {
                            if (entity instanceof LivingEntity living) living.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(),
                                    300, 0, false, false, true), this);
                        }
                        if (variant == ElementType.BLAZE || variant == ElementType.SPARKFERNO) {
                            entity.setSecondsOnFire(5);
                        }
                        float finalDamage = getDamage();
                        if (owner instanceof IMob mob && entity instanceof LivingEntity livingEntity) {
                            finalDamage += mob.getDamageAmountByTargetHealthPct(livingEntity);
                        }
                        entity.hurt(this.damageSources().explosion(owner, null), finalDamage);
                    }
                }
            }
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 7) {
            double x = this.getX();
            double y = this.getY(0.5);
            double z = this.getZ();
            this.level().playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
        } else super.handleEntityEvent(pId);
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

    public float getDamage() {
        return this.getOwner() instanceof EntityRelicAnnihilator ? ModConfigHandler.COMMON.mobs.relicrons.relicAnnihilator.annihilatorMissile.damage.get().floatValue() : damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public enum ElementType implements StringRepresentable {
        VOLT(0, "volt"),
        BLAZE(1, "blaze"),
        SPARKFERNO(2, "sparkferno");

        private static final IntFunction<ElementType> BY_ID = ByIdMap.sparse(c -> c.id, values(), VOLT);
        public final int id;
        public final String name;

        ElementType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static ElementType byId(int id) {
            return BY_ID.apply(id);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
