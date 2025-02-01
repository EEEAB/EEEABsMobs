package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.entity.IMobLevel;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityCrimsonRay extends EntityAbsBeam {
    private static final EntityDataAccessor<Integer> DATA_ATTACK_HEIGHT = SynchedEntityData.defineId(EntityCrimsonRay.class, EntityDataSerializers.INT);

    public EntityCrimsonRay(EntityType<? extends EntityCrimsonRay> type, Level level) {
        super(type, level, 1);
        this.damage = 5;
    }

    public EntityCrimsonRay(Level world, LivingEntity caster, Vec3 pos, int duration, int attackHeight) {
        this(EntityInit.CRIMSON_RAY.get(), world);
        this.caster = caster;
        this.setPitch((float) (Math.PI / 2));
        this.setYaw((float) ((this.getYRot() - 90.0F) * Math.PI / 180.0F));
        this.setDuration(duration);
        this.setPos(pos);
        this.setAttackHeight(attackHeight);
        this.calculateEndPos(16);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    public void beamTick() {
        if (this.tickCount >= this.getCountDown()) {
            if (this.tickCount == this.getCountDown()) {
                this.playSound(SoundInit.CRIMSON_RAY.get(), 0.25F, (this.random.nextFloat() - this.random.nextFloat()) * -0.2F + 1.0F);
            }
            this.calculateEndPos(this.getAttackHeight());
            List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ)).getEntities();
            if (blockSide != null) {
                if (!level().isClientSide) {
                    BlockPos minPos = new BlockPos(Mth.floor(this.collidePosX - 0.5), Mth.floor(this.collidePosY - 0.5), Mth.floor(this.collidePosZ - 0.5));
                    BlockPos maxPos = new BlockPos(Mth.floor(this.collidePosX + 0.5), Mth.floor(this.collidePosY + 0.5), Mth.floor(this.collidePosZ + 0.5));
                    BlockPos.betweenClosedStream(minPos, maxPos).forEach(pos -> {
                        if (ModEntityUtils.canDestroyBlock(this.level(), pos, this, 50) && ModEntityUtils.canMobDestroy(this)) {
                            this.level().destroyBlock(pos, false);
                        }
                    });
                } else {
                    this.spawnExplosionParticles();
                }
            }
            if (!this.level().isClientSide) {
                for (LivingEntity target : hit) {
                    if (target == this.caster) continue;
                    float finalDamage = damage;
                    if (this.caster instanceof IMobLevel mob) {
                        finalDamage += mob.getDamageAmountByTargetHealthPct(target);
                    }
                    target.hurt(this.damageSources().indirectMagic(this, caster), finalDamage);
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_HEIGHT, 16);
    }

    public int getAttackHeight() {
        return getEntityData().get(DATA_ATTACK_HEIGHT);
    }

    public void setAttackHeight(int attackHeight) {
        getEntityData().set(DATA_ATTACK_HEIGHT, attackHeight);
    }

    @Override
    protected void spawnExplosionParticles() {
        for (int i = 0; i < 2; i++) {
            final float velocity = 0.12F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * -velocity;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.LARGE_SMOKE, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096;
    }

    public static class PreAttack extends EntityMagicEffects {
        private final ControlledAnimation phaseController = new ControlledAnimation(2);
        private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(PreAttack.class, EntityDataSerializers.INT);
        public int attackHeight = 16;
        private Vec3 pos;

        public PreAttack(EntityType<?> type, Level level) {
            super(type, level);
        }

        public PreAttack(Level level, Vec3 pos, LivingEntity caster, int attackHeight) {
            this(EntityInit.CRIMSON_RAY_PRE.get(), level);
            this.pos = pos;
            this.caster = caster;
            this.attackHeight = attackHeight;
        }

        @Override
        protected void defineSynchedData() {
            super.defineSynchedData();
            this.entityData.define(DATA_PHASE, 0);
        }

        @Override
        public void tick() {
            super.tick();
            if (!this.level().isClientSide) {
                if (this.pos == null || this.caster == null) {
                    this.discard();
                    return;
                }
                switch (this.getPhase()) {
                    case 0, 1, 2, 3, 4:
                        if (this.phaseController.increaseTimerChain().isEnd()) {
                            this.setPhase(this.getPhase() + 1);
                            this.phaseController.resetTimer();
                        }
                        break;
                    case 5:
                        if (this.phaseController.increaseTimerChain().isEnd()) {
                            EntityCrimsonRay ray = new EntityCrimsonRay(this.level(), this.caster, this.pos, 10, this.attackHeight);
                            ray.setDamage((float) this.caster.getAttributeValue(Attributes.ATTACK_DAMAGE));
                            this.level().addFreshEntity(ray);
                            this.discard();
                        }
                }
            }
        }

        @Override
        public boolean shouldRenderAtSqrDistance(double distance) {
            return distance < 4096;
        }

        public int getPhase() {
            return this.entityData.get(DATA_PHASE);
        }

        public void setPhase(int phase) {
            this.entityData.set(DATA_PHASE, phase);
        }
    }
}
