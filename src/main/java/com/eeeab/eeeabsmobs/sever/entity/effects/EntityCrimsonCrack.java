package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityCrimsonCrack extends EntityMagicEffects {
    public static final float ATTACK_RANGE = 3F;
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EntityCrimsonCrack.class, EntityDataSerializers.INT);
    public final ControlledAnimation phaseControlled = new ControlledAnimation(30);
    public final ControlledAnimation displayControlled = new ControlledAnimation(20);
    @OnlyIn(Dist.CLIENT)
    public Vec3[] myPos;

    public EntityCrimsonCrack(EntityType<?> type, Level level) {
        super(type, level);
        if (this.level.isClientSide) {
            myPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
        this.displayControlled.setTimer(20);
        this.phaseControlled.setTimer(20);
    }

    public EntityCrimsonCrack(Level level, LivingEntity caster, Vec3 pos) {
        this(EntityInit.CRIMSON_CRACK.get(), level);
        this.setPos(pos);
        this.caster = caster;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }


    @Override
    public void tick() {
        super.tick();
        displayControlled.updatePrevTimer();
        this.doAttractEffect();
        switch (this.getPhase()) {
            case 0, 1, 2:
                if (this.phaseControlled.increaseTimerChain().isEnd()) {
                    this.nextPhase();
                    this.phaseControlled.setTimer(switch (this.getPhase()) {
                        case 1 -> 20;
                        case 2 -> 25;
                        default -> 0;
                    });
                }
                break;
            case 3:
                if (this.phaseControlled.increaseTimerChain().isEnd()) {
                    this.displayControlled.decreaseTimer();
                } else {
                    if (!this.level.isClientSide) {
                        for (LivingEntity target : this.getNearByEntities(LivingEntity.class, 5, 5, 5, 5)) {
                            if (target == this.caster)
                                continue;
                            target.setDeltaMovement(target.getDeltaMovement().add(this.position().subtract(target.position()).normalize().scale(0.1F)));
                            if (this.distanceTo(target) <= ATTACK_RANGE) {
                                boolean flag = target.hurt(DamageSource.indirectMagic(this, caster), 5F + target.getMaxHealth() * 0.015F);
                                if (flag && target.isAlive()) {
                                    ModEntityUtils.addEffectStackingAmplifier(null, target, EffectInit.ARMOR_LOWER_EFFECT.get(), 300, 5, true, true, true, true, false);
                                    this.doEnchantDamageEffects(this.caster, target);
                                }
                            }
                        }
                    }
                }
                if (this.displayControlled.isStop()) this.discard();
                break;
        }
    }

    private void doAttractEffect() {
        if (this.tickCount % 3 == 0 && this.level.isClientSide) {
            this.myPos[0] = this.position();
            int particleCount = Mth.clamp(Mth.floor(5 * this.displayControlled.getAnimationFraction()), 1, 5);
            while (--particleCount != 0) {
                double radius = ATTACK_RANGE;
                double yaw = random.nextFloat() * 2 * Math.PI;
                double pitch = random.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                double rootX = this.getX();
                double rootY = this.getY();
                double rootZ = this.getZ();
                AdvancedParticleBase.spawnParticle(this.level, ParticleInit.ADV_ORB.get(), rootX + ox, rootY + oy, rootZ + oz, 0, 0, 0, true, 0, 0, 0, 0, 2.5F, 1, 1, 1, 1, 1, 12, true, false, false, new ParticleComponent[]{
                        new ParticleComponent.Attractor(this.myPos, 1.0f, ATTACK_RANGE / 2, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0f, 0.6f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, AnimData.KeyTrack.constant(0.57F), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.BLUE, AnimData.KeyTrack.constant(0.13F), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.GREEN, AnimData.KeyTrack.constant(0.13F), false),
                });
            }
        }
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE, 0);
    }

    public void nextPhase() {
        int phase = this.getPhase();
        if (phase < 3) {
            this.setPhase(phase + 1);
        }
        this.playSound(SoundInit.CRIMSON_CRACK_BREAK.get(), 1.5F, (this.random.nextFloat() - this.random.nextFloat()) * 0.5F + 1.5F);
    }

    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setPhase(compoundTag.getInt("phase"));
        if (this.getPhase() < 3) {
            this.displayControlled.setTimer(20);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("phase", this.getPhase());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }
}
