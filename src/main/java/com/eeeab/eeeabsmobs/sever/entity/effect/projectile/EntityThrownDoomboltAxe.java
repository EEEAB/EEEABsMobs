package com.eeeab.eeeabsmobs.sever.entity.effect.projectile;

import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.client.particle.ParticleRing;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityExplode;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntitySurge;
import com.eeeab.eeeabsmobs.sever.entity.effect.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.List;

public class EntityThrownDoomboltAxe extends AbstractArrow implements IEntity {
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(EntityThrownDoomboltAxe.class, EntityDataSerializers.BOOLEAN);
    private ItemStack doomboltAxeItem = new ItemStack(ItemInit.DOOMBOLT_AXE.get());
    public int clientSideReturnTridentTickCount;
    private boolean dealtDamage;
    private boolean sentSpikeEvent;

    public EntityThrownDoomboltAxe(EntityType<? extends EntityThrownDoomboltAxe> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public EntityThrownDoomboltAxe(Level pLevel, LivingEntity shooter, ItemStack stack) {
        super(EntityInit.DOOMBOLT_AXE.get(), shooter, pLevel);
        this.doomboltAxeItem = stack.copy();
        this.entityData.set(ID_FOIL, stack.hasFoil());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4 || this.tickCount > 15 && !this.inGround) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                //this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * 3, this.getZ());
                //if (this.level().isClientSide) {
                //    this.yOld = this.getY();
                //}
                //double d0 = 0.05D * 1;
                //this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                double d0 = 1.5;
                this.setDeltaMovement(vec3.normalize().scale(d0));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundInit.DOOMBOLTAXE_RETURN.get(), 10.0F, 0.8F + (this.random.nextFloat() - 0.5F) * 0.15F);
                }
                ++this.clientSideReturnTridentTickCount;
            }
        } else if (this.level().isClientSide && !this.inGround) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.lengthSqr() > 1e-4) {
                double x = this.getX();
                double y = this.getY(0.5);
                double z = this.getZ();
                Vec3 dir = motion.normalize();
                float yaw = (float) Math.atan2(dir.x, dir.z);
                float pitch = (float) -Math.asin(dir.y);
                this.level().addParticle(new ParticleRing.RingData(yaw, pitch, 4, 0.8f, 0.8f, 0.9f,
                        0.5F, 50f, false, ParticleRing.EnumRingBehavior.GROW), x, y, z, 0, 0, 0
                );
            }
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.doomboltAxeItem.copy();
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        if (entity == owner) return;
        float damage = (float) ModConfigHandler.COMMON.items.doomboltAxe.attackDamageValue;
        if (entity instanceof LivingEntity livingentity) {
            damage += EnchantmentHelper.getDamageBonus(this.doomboltAxeItem, livingentity.getMobType());
        }
        this.dealtDamage = true;
        DamageSource damagesource = this.damageSources().trident(this, owner == null ? this : owner);
        SoundEvent soundevent = SoundInit.DOOMBOLTAXE_HIT.get();
        float volume = 1F;
        float pitch = 0.7F;
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        boolean isThundering = false;
        if (this.level() instanceof ServerLevel && this.level().isThundering()) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningbolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                    this.level().addFreshEntity(lightningbolt);
                    if (!this.sentSpikeEvent) {
                        this.level().broadcastEntityEvent(this, (byte) 4);
                        this.sentSpikeEvent = true;
                    }
                    isThundering = true;
                    soundevent = SoundInit.DOOMBOLTAXE_THUNDER.get();
                    damage *= 1.5F;
                    volume = 3F;
                    pitch = 1.2F;
                }
            }
        }
        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity target) {
                target.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 200, 0, false, false, true), owner);
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(target, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, target);
                }
                this.doPostHurtEffects(target);
            }
        }
        applyAreaDamage(result.getLocation(), damage, isThundering);
        this.playSound(soundevent, volume, pitch);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        applyAreaDamage(result.getLocation(), 0, false);
    }

    private void applyAreaDamage(Vec3 center, float damage, boolean isThundering) {
        if (this.level().isClientSide) return;
        if (isThundering) {
            AABB aabb = ModEntityUtils.makeAABBWithSize(center.x, center.y, center.z, 0, 6, 6, 6);
            aabb.expandTowards(this.getDeltaMovement());
            Entity owner = this.getOwner();
            List<Entity> entities = this.level().getEntities(this, aabb, e -> e != owner);
            for (Entity entity : entities) {
                float percent = EntityExplode.getSeenPercent(center, entity);
                if (entity.hurt(this.damageSources().indirectMagic(this, owner == null ? this : owner), damage * percent)) {
                    if (entity instanceof LivingEntity target) {
                        target.addEffect(new MobEffectInstance(EffectInit.ELECTRIFIED_EFFECT.get(), 200, 0, false, false, true), owner);
                        if (owner instanceof LivingEntity) {
                            EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, target);
                        }
                    }
                }
            }
        }
        int layers = 4;
        double baseRadius = 1.5;
        double radiusStep = 1.25;
        int baseCount = 5;
        int countStep = 3;
        double angleOffsetStep = 0.8;
        for (int layer = 0; layer < layers; layer++) {
            double radius = baseRadius + layer * radiusStep;
            int count = baseCount + layer * countStep;
            double angleOffset = layer * angleOffsetStep;
            double step = 2 * Math.PI / count;
            for (int i = 0; i < count; i++) {
                float angle = (float) (angleOffset + i * step);
                double x = center.x + Math.cos(angle) * radius;
                double z = center.z + Math.sin(angle) * radius;
                int warmupDelay = layer * 3;
                Vec3 point = ModEntityUtils.checkSummonEntityPointNullable(this.level(), x, z, center.y - 2, center.y + 2);
                if (point == null) continue;/*point = new Vec3(x, center.y, z)*/
                Entity owner = getOwner();
                EntitySurge surge = new EntitySurge(this.level(), point.x, point.y, point.z, angle, warmupDelay, this.doomboltAxeItem, owner instanceof LivingEntity ? (LivingEntity) owner : null);
                this.level().addFreshEntity(surge);
            }
        }
        EntityCameraShake.cameraShake(this.level(), center, isThundering ? 30 : 15, 0.15F, 5, 10);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            com.eeeab.eeeabsmobs.client.render.LightningBolt.LightningBoltBuilder builder = com.eeeab.eeeabsmobs.client.render.LightningBolt.DEFAULT;
            double phi = Math.PI * (3 - Math.sqrt(5));
            double count = 6;
            for (int i = 0; i < count; i++) {
                double velocityY = 1 - (i / (count - 1)) * 2;
                double radius = Math.sqrt(1 - velocityY * velocityY);
                double theta = phi * i;
                double velocityX = Math.cos(theta) * radius;
                double velocityZ = Math.sin(theta) * radius;
                float sideOffset = (float) (random.nextGaussian() * 0.2D) * (random.nextBoolean() ? 1 : -1);
                Vec3 pos = this.position().add(0, this.getBbHeight() / 2, 0);
                ClientProxy.LIGHTNING_RENDER.update(this, builder.color(new Vector4f(0.73F, 0.92F, 0.96F, 0.8F)).size(0.2F).lifespan(8).spreadFactor(0.07F).fadeFunction(com.eeeab.eeeabsmobs.client.render.LightningBolt.FadeFunction.fade(0.4F))
                        .build(pos, pos.add(velocityX * 5, velocityY * 5, velocityZ * 5).offsetRandom(this.random, sideOffset), this.random));
            }
        }
    }

    @Override
    protected boolean tryPickup(Player player) {
        boolean pickup = super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
        if (pickup) {
            player.getCooldowns().addCooldown(doomboltAxeItem.getItem(), (int) (ModConfigHandler.COMMON.items.doomboltAxeConfig.get() * 2 * 20));
        }
        return pickup;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundInit.DOOMBOLTAXE_HIT_GROUND.get();
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("doomboltAxe", 10)) {
            this.doomboltAxeItem = ItemStack.of(pCompound.getCompound("doomboltAxe"));
        }
        this.dealtDamage = pCompound.getBoolean("dealtDamage");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("doomboltAxe", this.doomboltAxeItem.save(new CompoundTag()));
        pCompound.putBoolean("dealtDamage", this.dealtDamage);
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
}