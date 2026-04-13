package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.ParticleRotation;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedParticleData;
import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.ModDamageSource;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class EntitySurge extends EntityMagicEffects implements IEntity {
    private static final ParticleComponent[] COMPONENTS = new ParticleComponent[]{
            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, AnimData.oscillate(0.373F, 0.73F, 4), false),
            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.GREEN, AnimData.oscillate(0.8F, 0.92F, 4), false),
            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.BLUE, AnimData.oscillate(0.89F, 0.96F, 4), false)
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
        if (this.level().isClientSide) return;
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
                if (useItemStack != null) {
                    damage += EnchantmentHelper.getDamageBonus(this.useItemStack, hitEntity.getMobType());
                }
                if (owner == null) {
                    hitEntity.hurt(ModDamageSource.bypassShield(this, this), damage);
                } else {
                    if (owner.isAlliedTo(hitEntity)) return;
                    if (owner instanceof IMob iMob) damage += iMob.getDamageAmountByTargetHealthPct(hitEntity);
                    hitEntity.hurt(ModDamageSource.bypassShield(this, owner), damage);
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
        AdvancedParticleData data = AdvancedParticleBase.createParticleData(ParticleInit.SURGE.get(), vertical, 20F, 1, 1, 1, 1, 1,
                duration, true, false, COMPONENTS, true);
        this.level().addParticle(data, true, getX(), getY() + 2, getZ(), 0, 0, 0);
        for (int i = 0; i < 3; i++) {
            duration = 5 + this.random.nextInt(5);
            AdvancedParticleData data2 = AdvancedParticleBase.createParticleData(ParticleInit.SPARK.get(), new ParticleRotation.FaceCamera(0), 3F + (this.random.nextFloat() - 0.5F), 1, 1, 1, 1, 1,
                    duration, true, false, COMPONENTS, true);
            this.level().addParticle(data2, true, getRandomX(1.5), getY() + 0.2 + this.random.nextFloat() * 0.75, getRandomZ(1.5), 0, 0, 0);
        }
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
