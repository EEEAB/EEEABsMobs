package com.eeeab.eeeabsmobs.sever.entity.impl.effect;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityGuardianBlade extends EntityMagicEffects {
    public final ControlledAnimation alphaControlled = new ControlledAnimation(10);
    private static final float[][] BLOCK_OFFSETS = {
            {-0.5F, -0.5F},
            {-0.5F, 0.5F},
            {0.5F, 0.5F},
            {0.5F, -0.5F},
    };


    public EntityGuardianBlade(EntityType<?> type, Level level) {
        super(type, level);
    }


    public EntityGuardianBlade(Level level, LivingEntity caster, double x, double y, double z, float yRot) {
        super(EntityInit.GUARDIAN_BLADE.get(), level);
        this.caster = caster;
        this.setYRot((yRot * (180F / (float) Math.PI)) - 90F);
        this.setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        alphaControlled.updatePrevTimer();
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.tickCount == 1) {
            Vec3 lookAngle = this.getLookAngle();
            this.shoot(lookAngle.x, lookAngle.y, lookAngle.z, 1);
        }

        //判断是否触碰撞箱
        //if (!this.level().noCollision(this, this.getBoundingBox().inflate(0.15))) {
        //    this.discard();
        //}

        if (tickCount % 8 == 0 && this.tickCount <= 40) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 0.6, 0.6));
            this.alphaControlled.increaseTimer(2);
        }

        //List<LivingEntity> entities = this.getNearByLivingEntities(0.3, 3, 0.3, 0.3);

        if (this.tickCount > 45) {
            this.discard();
        }
        if (this.tickCount <= 40) {
            this.breakBlockEffect();
            this.doHurtTarget();
        }
    }

    private void shoot(double x, double y, double z, double speed) {
        this.setDeltaMovement(x * speed, y * speed, z * speed);
    }

    private void doHurtTarget() {
        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.15));
        float damage = 10 - 10 * (1 - speed);
        if (!entities.isEmpty()) {
            for (LivingEntity livingEntity : entities) {
                if (livingEntity == caster) continue;
                livingEntity.hurt(this.damageSources().indirectMagic(this, livingEntity), damage);
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
            for (float[] robustAttackBlockOffset : BLOCK_OFFSETS) {
                float ox = robustAttackBlockOffset[0], oy = robustAttackBlockOffset[1];
                int hitX = Mth.floor(x + ox);
                int hitZ = Mth.floor(z + oy);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = level().getBlockState(hit);
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    int count = 16 - (int) (this.tickCount * 0.5);
                    if (count > 0) {
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
            }
        }
    }
}
