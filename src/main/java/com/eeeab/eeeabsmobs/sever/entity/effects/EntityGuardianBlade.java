package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.entity.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityGuardianBlade extends EntityMagicEffects {
    public final ControlledAnimation controlled = new ControlledAnimation(100);
    private static final float MAX_DAMAGE = 30F;
    private static final int DURATION = 35;
    private boolean moveOffset;
    private double damage = 1D;
    private static final float[][] BLOCK_OFFSETS = {
            {-0.5F, -0.5F},
            {-0.5F, 0.5F},
            {0.5F, 0.5F},
            {0.5F, -0.5F},
    };


    public EntityGuardianBlade(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }


    public EntityGuardianBlade(Level level, LivingEntity caster, double x, double y, double z, float yRot, boolean moveOffset) {
        this(EntityInit.GUARDIAN_BLADE.get(), level);
        this.moveOffset = moveOffset;
        this.caster = caster;
        this.setYRot((yRot * (180F / (float) Math.PI)) - 90F);
        this.setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        this.controlled.updatePrevTimer();
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.controlled.isStop()) {
            Vec3 lookAngle = this.getLookAngle();
            float speed = 1.35F;
            if (moveOffset) speed += this.random.nextFloat() * 0.5F;
            this.shoot(lookAngle.x, lookAngle.y, lookAngle.z, speed);
            if (this.caster != null) this.damage = this.caster.getAttributeValue(Attributes.ATTACK_DAMAGE);
            this.controlled.increaseTimer(1);
        } else if (this.controlled.increaseTimerChain().getTimer() <= DURATION) {
            if (this.controlled.getTimer() % 5 == 0) {
                double ac = DURATION / 70D;
                this.setDeltaMovement(this.getDeltaMovement().multiply(ac, ac, ac));
            }
        }
        if (this.controlled.getTimer() > DURATION || this.tickCount > this.controlled.getDuration()) {
            this.discard();
        } else {
            this.breakBlockEffect();
            this.doHurtTarget();
        }
    }

    private void shoot(double x, double y, double z, double speed) {
        this.setDeltaMovement(x * speed, y * speed, z * speed);
    }

    private void doHurtTarget() {
        if (!this.level().isClientSide) {
            float progress = this.controlled.getAnimationProgressTemporaryInvesed();
            if (progress < 0.2) return;
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2));
            for (LivingEntity target : entities) {
                if (target == caster) continue;
                if (caster instanceof EntityNamelessGuardian) damage += target.getMaxHealth() * 0.05D;
                damage = Mth.clamp(damage * progress, 1, MAX_DAMAGE);
                target.hurt(this.damageSources().indirectMagic(target, caster), (float) damage);
            }
        }
    }

    private void breakBlockEffect() {
        if (this.level().isClientSide) {
            double theta = this.getYRot() * (Math.PI / 180);
            double perpX = Math.cos(theta);
            double perpZ = Math.sin(theta);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            double x = getX() + 1 * vecX;
            double y = getBoundingBox().minY + 0.1;
            double z = getZ() + 1 * vecZ;
            int hitY = Mth.floor(getY() - 0.2);
            for (float[] floats : BLOCK_OFFSETS) {
                float ox = floats[0], oy = floats[1];
                int hitX = Mth.floor(x + ox);
                int hitZ = Mth.floor(z + oy);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = level().getBlockState(hit);
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    double count = Math.max(0, Math.floor(15 * this.controlled.getAnimationProgressTemporaryInvesed()) - 2);
                    for (int n = 0; n < count; n++) {
                        double pa = random.nextDouble() * 2 * Math.PI;
                        double pd = random.nextDouble() * 0.6 - 0.1;
                        double px = x + Math.cos(pa) * pd;
                        double pz = z + Math.sin(pa) * pd;
                        double magnitude = random.nextDouble() * 4 + 5;
                        double velX = perpX * magnitude;
                        double velY = random.nextDouble() * 3 + 6;
                        double velZ = perpZ * magnitude;
                        if (vecX * (pz - getZ()) - vecZ * (px - getX()) > 0) {
                            velX = -velX;
                            velZ = -velZ;
                        }
                        level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), px, y, pz, velX, velY, velZ);
                    }
                }
            }
            if (this.controlled.getTimer() % 6 == 0) {
                level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), getRandomX(0.5F), getRandomY(), getRandomZ(0.5F), 0, this.random.nextFloat() * 0.05F, 0);
            }
        }
    }
}
