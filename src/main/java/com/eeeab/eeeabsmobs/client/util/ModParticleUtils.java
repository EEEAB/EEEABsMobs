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

import java.util.Random;

public class ModParticleUtils {

    private ModParticleUtils() {
    }

    private static double getParticleX(double widthScale, double px, double width) {
        Random random = new Random();
        return px + width * (2.0 * random.nextDouble() - 1.0) * widthScale;
    }

    private static double getParticleY(double py, float height) {
        Random random = new Random();
        return py + height * random.nextDouble();
    }

    private static double getParticleZ(double widthScale, double pz, double width) {
        Random random = new Random();
        return pz + width * (2.0 * random.nextDouble() - 1.0) * widthScale;
    }

    //无形状粒子爆发
    public static void particleOutburst(Level world, int particleNumber, ParticleOptions[] particles, double x, double y, double z, float[][] velDividers) {
        Random random = new Random();
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        for (int j = 0; j < particleNumber; ++j) {
            double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
            double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
            double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
            for (int i = 0; i < particles.length; i++) {
                world.addParticle(particles[i], x, y, z, newX / velDividers[i][0], newY / velDividers[i][1], newZ / velDividers[i][2]);
            }
        }
    }

    //无形状粒子爆发(可控制范围)
    public static void particleOutburst(Level world, int particleNumber, ParticleOptions[] particles, double x, double y, double z, float[][] velDividers, double sizeModifier) {
        Random random = new Random();
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        for (int j = 0; j < particleNumber; ++j) {
            double newX = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d) * sizeModifier;
            double newZ = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e) * sizeModifier;
            double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
            for (int i = 0; i < particles.length; i++) {
                world.addParticle(particles[i], x, y, z, newX / velDividers[i][0], newY / velDividers[i][1], newZ / velDividers[i][2]);
            }
        }
    }

    //球形粒子爆发(小范围)
    public static void roundParticleOutburst(Level world, double points, ParticleOptions[] particles, double x, double y, double z, float sizeModifier) {
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i / (points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY * velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, true, x, y, z, velocityX * sizeModifier, velocityY * sizeModifier, velocityZ * sizeModifier);
            }
        }
    }


    //环形粒子爆发(y轴偏移)
    public static void annularParticleOutburst(Level world, double points, ParticleOptions[] particles, double x, double y, double z, double speed, double yOffSet) {
        for (int i = 1; i <= points; i++) {
            double yaw = i * 360F / points;
            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
            double zSpeed = speed * Math.sin(Math.toRadians(yaw));
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, x, y + yOffSet, z, xSpeed, 0, zSpeed);
            }
        }
    }


    //环形粒子爆发(可控角度 y轴偏移)
    public static void annularParticleOutburst(Level world, double points, float angle, ParticleOptions[] particles, double x, double y, double z, double speed, double yOffSet) {
        for (int i = 1; i <= points; i++) {
            double yaw = i * angle / points;
            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
            double zSpeed = speed * Math.sin(Math.toRadians(yaw));
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, x, y + yOffSet, z, xSpeed, 0, zSpeed);
            }
        }
    }

    //球形粒子爆发(x,y,z可控,扩散范围可控)
    public static void sphericalParticleOutburst(Level level, LivingEntity entity, ParticleOptions[] particles, float height, float size, double inFrontOffset, double sideOffset, double speed) {
        RandomSource random = entity.getRandom();
        double perpFacing = entity.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double vx = Math.cos(facingAngle) * inFrontOffset;
        double vz = Math.sin(facingAngle) * inFrontOffset;
        double perpX = Math.cos(perpFacing);
        double perpZ = Math.sin(perpFacing);
        double px = entity.getX() + vx + perpX * sideOffset;
        double py = entity.getY() + height;
        double pz = entity.getZ() + vz + perpZ * sideOffset;
        ++perpFacing;
        for (float i = -size; i <= size; ++i) {
            for (float j = -size; j <= size; ++j) {
                for (float k = -size; k <= size; ++k) {
                    double d0 = (double) j + (random.nextDouble() - random.nextDouble()) * 0.5D;
                    double d1 = (double) i + (random.nextDouble() - random.nextDouble()) * 0.5D;
                    double d2 = (double) k + (random.nextDouble() - random.nextDouble()) * 0.5D;
                    double d3 = (double) Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2)) / 0.5D + random.nextDouble() - random.nextDouble() * 0.05D;
                    //ParticleOptions type = ParticleTypes.SOUL_FIRE_FLAME;
                    for (ParticleOptions type : particles) {
                        level.addParticle(type, px, py, pz, d0 / d3 * speed, d1 / d3 * speed, d2 / d3 * speed);
                    }
                    if (i != -size && i != size && j != -size && j != size) {
                        k += size * 2.0F - 1.0F;
                    }
                }
            }
        }
    }


    public static void annularParticleOutburstOnGround(Level level, ParticleOptions options, LivingEntity entity, int quantity, int randomQuantity, double scale, double inFrontOffset, double sideOffset, double diffusionSpeed) {
        RandomSource random = entity.getRandom();
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
            double dist = random.nextDouble() * 0.1 + scale;
            double sx = Math.cos(theta);
            double sz = Math.sin(theta);
            double px = fx + sx * dist;
            double py = fy + random.nextDouble() * 0.1;
            double pz = fz + sz * dist;
            level.addParticle(options, px, py, pz, sx * diffusionSpeed, 0, sz * diffusionSpeed);
        }
    }

    //随机偏移环形粒子爆发
    public static void randomAnnularParticleOutburst(Level world, double points, float velocity, ParticleOptions[] particles, double x, double y, double z) {
        Random random = new Random();
        for (int i = 0; i < points; i++) {
            double yaw = i * ((2 * Math.PI) / points);
            double vy = random.nextFloat() * 0.1F - 0.05F;
            double vx = velocity * Mth.cos((float) yaw);
            double vz = velocity * Mth.sin((float) yaw);
            for (ParticleOptions particle : particles) {
                world.addParticle(particle, x, y, z, vx, vy, vz);
            }
        }
    }

    public static void advAttractorParticle(ParticleType<AdvancedParticleData> advParticleType, Entity entity, int particleCount, float yOffset, float scale, float duration, ParticleComponent[] components, boolean isAnimation) {
        Random random = new Random();
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
