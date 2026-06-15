package com.eeeab.eeeabsmobs.server.entity.effect;

import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.ParticleRotation;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.server.entity.mob.IMob;
import com.eeeab.eeeabsmobs.server.entity.util.damage.ModDamageSource;
import com.eeeab.eeeabsmobs.server.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.server.init.EntityInit;
import com.eeeab.eeeabsmobs.server.init.ParticleInit;
import com.eeeab.eeeabsmobs.server.init.SoundInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySurge extends EntityMagicEffects implements IEntity {
    private static final ParticleComponent[] COMPONENTS = new ParticleComponent[]{
            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, AnimData.oscillate(0.38F, 0.85F, 4), false),
            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.GREEN, AnimData.oscillate(0.8F, 0.95F, 4), false),
            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.BLUE, AnimData.oscillate(0.89F, 1F, 4), false)
    };
    private ItemStack useItemStack;
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 10;

    public EntitySurge(EntityType<? extends EntitySurge> pEntityType, Level level) {
        super(pEntityType, level);
        this.noCulling = true;
        this.setNoGravity(true);
    }

    public EntitySurge(Level level, double pX, double pY, double pZ, float yRot, int warmupDelay, LivingEntity owner) {
        this(EntityInit.SURGE.get(), level);
        this.warmupDelayTicks = warmupDelay;
        this.setOwner(owner);
        this.setYRot(yRot * (180F / (float) Math.PI));
        this.setPos(pX, pY, pZ);
    }

    public EntitySurge(Level pLevel, double pX, double pY, double pZ, float pYRot, int warmupDelay, ItemStack stack, LivingEntity owner) {
        this(pLevel, pX, pY, pZ, pYRot, warmupDelay, owner);
        this.useItemStack = stack;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.warmupDelayTicks = compoundTag.getInt("warmup");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("warmup", this.warmupDelayTicks);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 pos = this.position().offsetRandom(this.random, 0.5F);
            this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), pos.x, pos.y + 0.2F, pos.z, 0, 0, 0);
            return;
        }
        LivingEntity owner = this.getOwner();
        if (--this.warmupDelayTicks < 0) {
            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte) 4);
                this.sentSpikeEvent = true;
            }
            if (--this.lifeTicks < 0) {
                this.discard();
            } else for (LivingEntity hitEntity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.25, 0, 0.25))) {
                if (hitEntity == owner) continue;
                float damage = getDamage();
                if (hitEntity.isInWaterRainOrBubble()) damage *= 2;
                if (useItemStack != null) {
                    damage += EnchantmentHelper.getDamageBonus(this.useItemStack, hitEntity.getMobType());
                }
                if (owner == null) {
                    hitEntity.hurt(ModDamageSource.surge(this, this), damage);
                } else {
                    if (owner.isAlliedTo(hitEntity)) return;
                    if (owner instanceof IMob iMob) damage += iMob.getDamageAmountByTargetHealthPct(hitEntity);
                    hitEntity.hurt(ModDamageSource.surge(this, owner), damage);
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            doSpawnSurgeParticle();
        }
    }

    private void doSpawnSurgeParticle() {
        ParticleRotation.FaceCameraVertical vertical = new ParticleRotation.FaceCameraVertical();
        int duration = 10 + this.random.nextInt(7);
        this.level().addParticle(AdvancedParticleBase.createParticleData(ParticleInit.SURGE.get(), vertical, 20F, 1, 1, 1, 1, 1,
                duration, true, false, COMPONENTS, true), true, getX(), getY() + 2, getZ(), 0, 0, 0);
        this.level().addParticle(AdvancedParticleBase.createParticleData(ParticleInit.GLOW.get(), new ParticleRotation.EulerAngles(0, (float) (Math.PI / 2F), 0), 10F, 1, 1, 1, 1, 1,
                duration - 2, true, false, new ParticleComponent[]{
                        //new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.oscillate(0.2F, 0.6F, 8), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.oscillate(5F, 8F, 8), false),
                        COMPONENTS[0],
                        COMPONENTS[1],
                        COMPONENTS[2],
                }, false), getX(), getY() + 0.1, getZ(), 0, 0, 0);
        if (!this.isSilent()) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundInit.SURGE.get(), this.getSoundSource(), 0.8F, this.random.nextFloat() * 0.2F + 0.85F, false);
        }
    }

    @Override
    protected float getDamage() {
        if (getOwner() instanceof Player) return ModConfigHandler.COMMON.items.surge.damage.get().floatValue();
        return ModConfigHandler.COMMON.mobs.relicrons.realmwarden.surge.damage.get().floatValue();
    }
}
