package com.eeeab.eeeabsmobs.client.util;

import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleData;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleComponent;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * 粒子效果工具类
 *
 * @author EEEAB
 */
public class ModParticleUtils {
    private static final RandomSource random = RandomSource.createNewThreadLocalInstance();

    private ModParticleUtils() {
    }


    /**
     * 自定义形状粒子效果
     *
     * @param points         生成数量
     * @param particles      粒子
     * @param speedModifiers 速度乘数
     */
    public static void particleOutburst(Level world, int points, ParticleOptions[] particles, double x, double y, double z, float[][] speedModifiers) {
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        for (int j = 0; j < points; ++j) {
            double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
            double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
            double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
            for (int i = 0; i < particles.length; i++) {
                world.addParticle(particles[i], x, y, z, newX / speedModifiers[i][0], newY / speedModifiers[i][1], newZ / speedModifiers[i][2]);
            }
        }
    }

    /**
     * 自定义形状粒子效果(可控制速度)
     *
     * @param points        生成数量
     * @param particles     粒子
     * @param velDividers   移动速度
     * @param speedModifier 速度乘数
     */
    public static void particleOutburst(Level world, int points, ParticleOptions[] particles, double x, double y, double z, float[][] velDividers, double speedModifier) {
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        for (int j = 0; j < points; ++j) {
            double newX = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d) * speedModifier;
            double newZ = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e) * speedModifier;
            double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
            for (int i = 0; i < particles.length; i++) {
                world.addParticle(particles[i], x, y, z, newX / velDividers[i][0], newY / velDividers[i][1], newZ / velDividers[i][2]);
            }
        }
    }

    /**
     * 球形粒子爆发(小范围)
     *
     * @param points       生成数量
     * @param particles    粒子
     * @param sizeModifier 效果大小
     */
    public static void roundParticleOutburst(Level world, double points, ParticleOptions[] particles, double x, double y, double z, float sizeModifier) {
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i / (points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY * velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            float sideOffset = (float) (random.nextGaussian() * 0.2D) * (random.nextBoolean() ? 1 : -1);
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, true, x, y, z, velocityX * (sizeModifier + sideOffset), velocityY * sizeModifier, velocityZ * (sizeModifier + sideOffset));
            }
        }
    }


    /**
     * 环形粒子爆发(y轴偏移)
     *
     * @param points        生成数量
     * @param particles     粒子
     * @param speedModifier 速度乘数
     * @param yOffSet       y轴偏移
     */
    public static void annularParticleOutburst(Level world, double points, ParticleOptions[] particles, double x, double y, double z, double speedModifier, double yOffSet) {
        for (int i = 1; i <= points; i++) {
            double yaw = i * 360F / points;
            double xSpeed = speedModifier * Math.cos(Math.toRadians(yaw));
            double zSpeed = speedModifier * Math.sin(Math.toRadians(yaw));
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, x, y + yOffSet, z, xSpeed, 0, zSpeed);
            }
        }
    }


    /**
     * 环形粒子爆发(可控角度 y轴偏移)
     *
     * @param points        生成数量
     * @param particles     粒子
     * @param speedModifier 速度乘数
     * @param yOffSet       y轴偏移
     * @param angle         角度
     */
    public static void annularParticleOutburst(Level world, double points, ParticleOptions[] particles, double x, double y, double z, double speedModifier, double yOffSet, float angle) {
        for (int i = 1; i <= points; i++) {
            double yaw = i * angle / points;
            double xSpeed = speedModifier * Math.cos(Math.toRadians(yaw));
            double zSpeed = speedModifier * Math.sin(Math.toRadians(yaw));
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, x, y + yOffSet, z, xSpeed, 0, zSpeed);
            }
        }
    }

    /**
     * 球形粒子爆发
     *
     * @param points        生成数量
     * @param particles     粒子
     * @param entity        实体
     * @param yOffset       y轴偏移
     * @param inFrontOffset 前后偏移
     * @param sideOffset    左右偏移
     * @param speedModifier 速度乘数
     */
    public static void sphericalParticleOutburst(Level level, float points, ParticleOptions[] particles, LivingEntity entity, float yOffset, double inFrontOffset, double sideOffset, double speedModifier) {
        double perpFacing = entity.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double vx = Math.cos(facingAngle) * inFrontOffset;
        double vz = Math.sin(facingAngle) * inFrontOffset;
        double perpX = Math.cos(perpFacing);
        double perpZ = Math.sin(perpFacing);
        double px = entity.getX() + vx + perpX * sideOffset;
        double py = entity.getY() + yOffset;
        double pz = entity.getZ() + vz + perpZ * sideOffset;
        ++perpFacing;
        for (float i = -points; i <= points; ++i) {
            for (float j = -points; j <= points; ++j) {
                for (float k = -points; k <= points; ++k) {
                    double d0 = (double) j + (random.nextDouble() - random.nextDouble()) * 0.5D;
                    double d1 = (double) i + (random.nextDouble() - random.nextDouble()) * 0.5D;
                    double d2 = (double) k + (random.nextDouble() - random.nextDouble()) * 0.5D;
                    double d3 = (double) Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2)) / 0.5D + random.nextDouble() - random.nextDouble() * 0.05D;
                    for (ParticleOptions type : particles) {
                        level.addParticle(type, px, py, pz, d0 / d3 * speedModifier, d1 / d3 * speedModifier, d2 / d3 * speedModifier);
                    }
                    if (i != -points && i != points && j != -points && j != points) {
                        k += points * 2.0F - 1.0F;
                    }
                }
            }
        }
    }

    /**
     * 环形粒子在地面爆发
     *
     * @param particle       粒子
     * @param entity         实体
     * @param quantity       生成最少数量
     * @param randomQuantity 生成最大随机值
     * @param sizeModifier   效果大小
     * @param inFrontOffset  前后偏移
     * @param sideOffset     左右偏移
     * @param speedModifier  速度乘数
     */
    public static void annularParticleOutburstOnGround(Level level, ParticleOptions particle, LivingEntity entity, int quantity, int randomQuantity, double sizeModifier, double inFrontOffset, double sideOffset, double speedModifier) {
        double perpFacing = entity.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        //final double inFrontOffset = 0.8, sideOffset = -0.5;
        double vx = Math.cos(facingAngle) * inFrontOffset;
        double vz = Math.sin(facingAngle) * inFrontOffset;
        double perpX = Math.cos(perpFacing);
        double perpZ = Math.sin(perpFacing);
        double fx = entity.getX() + vx + perpX * sideOffset;
        double fy = entity.getBoundingBox().minY + 0.1;
        double fz = entity.getZ() + vz + perpZ * sideOffset;
        int amount = quantity + random.nextInt(randomQuantity);

        while (amount-- > 0) {
            double theta = random.nextDouble() * Math.PI * 2;
            double dist = random.nextDouble() * 0.1 + sizeModifier;
            double sx = Math.cos(theta);
            double sz = Math.sin(theta);
            double px = fx + sx * dist;
            double py = fy + random.nextDouble() * 0.1;
            double pz = fz + sz * dist;
            level.addParticle(particle, px, py, pz, sx * speedModifier, 0, sz * speedModifier);
        }
    }

    /**
     * 随机偏移环形粒子爆发
     *
     * @param points        生成数量
     * @param particles     粒子
     * @param speedModifier 速度乘数
     */
    public static void randomAnnularParticleOutburst(Level world, double points, ParticleOptions[] particles, double x, double y, double z, float speedModifier) {
        for (int i = 0; i < points; i++) {
            double yaw = i * ((2 * Math.PI) / points);
            double vy = random.nextFloat() * 0.1F - 0.05F;
            double vx = speedModifier * Mth.cos((float) yaw);
            double vz = speedModifier * Mth.sin((float) yaw);
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, x, y, z, vx, vy, vz);
            }
        }
    }

    public static void advAttractorParticle(ParticleType<AdvancedParticleData> advParticleType, Entity entity, int particleCount, float yOffset, float scale, float duration, ParticleComponent[] components, boolean isAnimation) {
        while (--particleCount != 0) {
            double radius = 2f * entity.getBbWidth();
            double yaw = random.nextFloat() * 2 * Math.PI;
            double pitch = random.nextFloat() * 2 * Math.PI;
            double ox = radius * Math.sin(yaw) * Math.sin(pitch);
            double oy = radius * Math.cos(pitch);
            double oz = radius * Math.cos(yaw) * Math.sin(pitch);
            double rootX = entity.getX();
            double rootY = entity.getY() + entity.getBbHeight() / 2f + yOffset;
            double rootZ = entity.getZ();
            AdvancedParticleBase.spawnParticle(entity.level, advParticleType, rootX + ox, rootY + oy, rootZ + oz, 0, 0, 0, true, 0, 0, 0, 0, scale, 1, 1, 1, 1, 1, duration, true, false, isAnimation, components);
        }
    }

}
