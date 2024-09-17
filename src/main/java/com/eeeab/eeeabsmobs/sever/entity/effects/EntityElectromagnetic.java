package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityElectromagnetic extends EntityMagicEffects {
    private float shockRange = 10;
    private float damage = 5.0F;
    private float shockSpeed = 5;
    private float yaw = 0;
    private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(EntityElectromagnetic.class, EntityDataSerializers.FLOAT);

    public EntityElectromagnetic(EntityType<? extends EntityElectromagnetic> type, Level world) {
        super(type, world);
    }

    public EntityElectromagnetic(Level world, LivingEntity caster) {
        super(EntityInit.ELECTROMAGNETIC.get(), world);
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
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getY() - 0.2F);
        int k = Mth.floor(this.getZ());
        BlockPos pos = new BlockPos(i, j, k);
        BlockState blockState = this.level().getBlockState(pos);
        if (!blockState.isAir()) {
            if (this.tickCount % 5 == 0) {
                SoundType soundtype = blockState.getSoundType(this.level(), pos, this);
                this.playSound(soundtype.getStepSound(), 1.0F, soundtype.getPitch());
            }

            for (int p = 0; p < 4 * Mth.clamp((int) Math.pow((double) this.entityData.get(DATA_SIZE), 1.5), 1, 30); ++p) {
                if (this.random.nextFloat() < 0.8F) {
                    this.level().addParticle((new BlockParticleOption(ParticleTypes.BLOCK, blockState)).setPos(pos), this.getX() + ((double) this.random.nextFloat() - 0.5) * (double) this.entityData.get(DATA_SIZE), this.getY(), this.getZ() + ((double) this.random.nextFloat() - 0.5) * (double) this.entityData.get(DATA_SIZE), 4.0 * ((double) this.random.nextFloat() - 0.5), (double) this.random.nextFloat() * 5.0 + 0.5, ((double) this.random.nextFloat() - 0.5) * 4.0);
                } else {
                    this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), this.getX() + ((double) this.random.nextFloat() - 0.5) * (double) this.entityData.get(DATA_SIZE), this.getY(), this.getZ() + ((double) this.random.nextFloat() - 0.5) * (double) this.entityData.get(DATA_SIZE), 0, 0, 0);
                }
            }
        }

        if (!this.level().isClientSide && this.tickCount % this.shockSpeed == 0) {
            Vec3 lookVec = this.calculateViewVector(0.0F, this.yaw);
            this.setPos(this.getX() + lookVec.x, this.getY(), this.getZ() + lookVec.z);
            AABB attackRange = ModEntityUtils.makeAABBWithSize(this.getX(), this.getY(), this.getZ(), 0.0, (double) this.entityData.get(DATA_SIZE), 0.6, (double) this.entityData.get(DATA_SIZE));
            for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, attackRange)) {
                if (this.caster == null) {
                    if (livingentity.hurt(this.damageSources().magic(), this.damage)) {
                        this.strongKnockBlock(livingentity);
                    }
                } else if (livingentity != this.caster && !livingentity.isAlliedTo(this.caster) && livingentity.hurt(this.damageSources().mobAttack(this.caster), this.damage)) {
                    this.strongKnockBlock(livingentity);
                }
            }
            if (this.shockRange > 0) {
                --this.shockRange;
            } else {
                this.discard();
            }
        }

    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SIZE, 2.0F);
    }

    private void strongKnockBlock(Entity entity) {
        double d0 = entity.getX() - this.getX();
        double d1 = entity.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001) * 2.0;
        entity.push(d0 * 0.5 / d2, 0.5 * 0.5, d1 * 0.5 / d2);
    }

    public void setSize(float input) {
        this.entityData.set(DATA_SIZE, input);
    }

    public static void shoot(Level world, LivingEntity caster, float damage, float size, int range, int speed, float yaw) {
        EntityElectromagnetic entity = new EntityElectromagnetic(world, caster);
        entity.moveTo(caster.getX(), caster.getY(), caster.getZ());
        entity.shockRange = range;
        entity.shockSpeed = speed;
        entity.setSize(size);
        entity.damage = damage;
        entity.yaw = yaw;
        world.addFreshEntity(entity);
    }
}