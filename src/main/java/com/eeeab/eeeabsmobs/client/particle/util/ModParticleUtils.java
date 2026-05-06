package com.eeeab.eeeabsmobs.client.particle.util;

import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.component.RibbonComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedParticleData;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;

public class ModParticleUtils {
    private static final RandomSource random = RandomSource.createNewThreadLocalInstance();

    private ModParticleUtils() {
    }

    /**
     * 自定义形状粒子效果
     *
     * @param points         生成数量
     * @param speedModifiers 速度乘数
     */
    public static void particleOutburst(Level level, int points, ParticleOptions[] particles,
                                        double x, double y, double z, float[][] speedModifiers) {
        particleOutburst(level, points, particles, x, y, z, speedModifiers, 1);
    }

    /**
     * 自定义形状粒子效果(可控制速度)
     *
     * @param points        生成数量
     * @param velDividers   移动速度
     * @param speedModifier 速度乘数
     */
    public static void particleOutburst(Level level, int points, ParticleOptions[] particles,
                                        double x, double y, double z, float[][] velDividers, double speedModifier) {
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        for (int j = 0; j < points; ++j) {
            double newX = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d) * speedModifier;
            double newZ = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e) * speedModifier;
            double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
            for (int i = 0; i < particles.length; i++) {
                level.addParticle(particles[i], x, y, z, newX / velDividers[i][0], newY / velDividers[i][1], newZ / velDividers[i][2]);
            }
        }
    }

    /**
     * 球形粒子爆发(小范围)
     *
     * @param points       生成数量
     * @param sizeModifier 效果大小
     */
    public static void roundParticleOutburst(Level level, double points, ParticleOptions[] particles, double x, double y, double z, float sizeModifier) {
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i / (points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY * velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            float sideOffset = (float) (random.nextGaussian() * 0.2D) * (random.nextBoolean() ? 1 : -1);
            for (ParticleOptions particle : particles) {
                level.addParticle(particle, true, x, y, z, velocityX * (sizeModifier + sideOffset), velocityY * sizeModifier, velocityZ * (sizeModifier + sideOffset));
            }
        }
    }


    /**
     * 环形粒子爆发(y轴偏移)
     *
     * @param points        生成数量
     * @param speedModifier 速度乘数
     * @param yOffSet       y轴偏移
     */
    public static void annularParticleOutburst(Level level, double points, ParticleOptions particle,
                                               double x, double y, double z, double speedModifier, double yOffSet) {
        annularParticleOutburst(level, points, particle, x, y, z, speedModifier, yOffSet, 360F, 0F, 0F);
    }


    /**
     * 环形粒子爆发(可控角度 y轴偏移)
     *
     * @param points        生成数量
     * @param speedModifier 速度乘数
     * @param yOffset       y轴偏移
     * @param angle         yaw角度
     * @param yMoveModifier y轴移动乘数
     * @param entityYaw     实体yaw
     */
    public static void annularParticleOutburst(Level level, double points, ParticleOptions particle,
                                               double x, double y, double z, double speedModifier,
                                               double yOffset, float angle, float yMoveModifier, float entityYaw) {
        for (int i = 0; i < points; i++) {
            double currentAngle = entityYaw - (angle / 2) + (i * angle) / (points - 1);
            double radians = Math.toRadians(currentAngle);
            double xSpeed = -speedModifier * Math.sin(radians);
            double zSpeed = speedModifier * Math.cos(radians);
            float sideOffset = (float) (random.nextGaussian() * 0.1D * speedModifier) * (random.nextBoolean() ? 1 : -1);
            level.addParticle(particle, x, y + yOffset, z, xSpeed + sideOffset, (random.nextFloat() - random.nextFloat()) * 0.1F * yMoveModifier, zSpeed + sideOffset);
        }
    }

    /**
     * 球形粒子爆发
     *
     * @param points        生成数量
     * @param yOffset       y轴偏移
     * @param inFrontOffset 前后偏移
     * @param sideOffset    左右偏移
     * @param speedModifier 速度乘数
     */
    public static void sphericalParticleOutburst(Level level, float points, ParticleOptions[] particles, LivingEntity entity,
                                                 float yOffset, double inFrontOffset, double sideOffset, double speedModifier) {
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
     * 生成方向性方块粒子效果
     *
     * @param theta              旋转角度（弧度）
     * @param points             生成数量
     * @param blockOffsetOffsets 块的偏移量数组
     * @param lengthFactor       大小系数
     */
    public static void blockParticleDirectionality(Level level, double x, double y, double z,
                                                   double theta, int points, float[][] blockOffsetOffsets, double lengthFactor) {
        blockParticleDirectionality(level, x, y, z, -0.2F, theta, points, blockOffsetOffsets, lengthFactor, (hardness) -> true
                , (pos, state) -> new BlockParticleOption(ParticleTypes.BLOCK, state));
    }

    public static void blockParticleDirectionality(Level level, double x, double y, double z, float yOffset,
                                                   double theta, int points, float[][] blockOffsetOffsets,
                                                   double lengthFactor, Predicate<Float> predicate, BlockParticleTypeGetter getter) {
        double perpX = Math.cos(theta);
        double perpZ = Math.sin(theta);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        int hitY = Mth.floor(y + yOffset);
        for (float[] offset : blockOffsetOffsets) {
            float ox = offset[0], oy = offset[1];
            int hitX = Mth.floor(x + ox);
            int hitZ = Mth.floor(z + oy);
            BlockPos pos = new BlockPos(hitX, hitY, hitZ);
            BlockState blockState = level.getBlockState(pos);
            if (blockState.getRenderShape() != RenderShape.INVISIBLE && predicate.test(blockState.getDestroySpeed(level, pos))) {
                for (int n = 0; n < points; n++) {
                    double pa = Math.random() * 2 * Math.PI;
                    double pd = Math.random() * (0.6 * lengthFactor) - (0.1 * lengthFactor);
                    double px = x + Math.cos(pa) * pd;
                    double pz = z + Math.sin(pa) * pd;
                    double magnitude = Math.random() * (4 * lengthFactor) + (5 * lengthFactor);
                    double velX = perpX * magnitude;
                    double velY = Math.random() * (3 * lengthFactor) + (6 * lengthFactor);
                    double velZ = perpZ * magnitude;
                    if (vecX * (pz - z) - vecZ * (px - x) > 0) {
                        velX = -velX;
                        velZ = -velZ;
                    }
                    ParticleOptions data = getter.get(pos, blockState);
                    if (data != null) {
                        level.addParticle(data, px, y, pz, velX, velY, velZ);
                    }
                }
            }
        }
    }

    /**
     * 在指定位置周围生成方块粒子效果
     *
     * @param minRadius       粒子生成最小半径
     * @param maxRadius       粒子生成最大半径
     * @param minOutwardSpeed 粒子水平方向最小向外速度
     * @param maxOutwardSpeed 粒子水平方向最大向外速度
     * @param minYSpeed       粒子向上最小垂直速度
     * @param maxYSpeed       粒子向上最大垂直速度
     * @param yOffset         Y轴偏移量，用于检测方块位置
     * @param particleYOffset 粒子生成Y轴偏移量
     */
    public static void blockParticlesAround(Level level, double x, double y, double z, int points,
                                            double minRadius, double maxRadius, double minOutwardSpeed,
                                            double maxOutwardSpeed, double minYSpeed, double maxYSpeed,
                                            double yOffset, double particleYOffset) {
        blockParticlesAround(level, x, y, z, points, minRadius, maxRadius, minOutwardSpeed, maxOutwardSpeed
                , minYSpeed, maxYSpeed, yOffset, particleYOffset, (pos, state) -> new BlockParticleOption(ParticleTypes.BLOCK, state));
    }

    public static void blockParticlesAround(Level level, double x, double y, double z, int points,
                                            double minRadius, double maxRadius, double minOutwardSpeed,
                                            double maxOutwardSpeed, double minYSpeed, double maxYSpeed,
                                            double yOffset, double particleYOffset, BlockParticleTypeGetter getter) {
        for (int i = 0; i < points; i++) {
            double angle = i * ((2 * Math.PI) / points);
            double radius = minRadius + random.nextDouble() * (maxRadius - minRadius);
            double posX = x + radius * Math.cos(angle);
            double posZ = z + radius * Math.sin(angle);
            double outwardSpeed = minOutwardSpeed + random.nextDouble() * (maxOutwardSpeed - minOutwardSpeed);
            double xSpeed = Math.cos(angle) * outwardSpeed;
            double zSpeed = Math.sin(angle) * outwardSpeed;
            double ySpeed = minYSpeed + random.nextDouble() * (maxYSpeed - minYSpeed);
            BlockPos pos = new BlockPos(Mth.floor(posX), Mth.floor(y + yOffset), Mth.floor(posZ));
            BlockState state = level.getBlockState(pos);
            if (state.getRenderShape() != RenderShape.INVISIBLE) {
                level.addParticle(getter.get(pos, state), posX, y + particleYOffset, posZ, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    @FunctionalInterface
    public interface BlockParticleTypeGetter {
        ParticleOptions get(BlockPos pos, BlockState state);
    }

    /**
     * 在指定坐标围绕圆心生成多层碗装线状粒子爆发 标*代表需要与radii长度一致
     *
     * @param centerPos 圆心坐标
     * @param duration  持续时间
     * @param particles *每层粒子数量
     * @param radii     *每层环行大小
     * @param speeds    *每层运动速度
     * @param angles    *每层运动角度
     * @param scales    *每层线段粗细
     * @param color     粒子颜色 数组长度必须≥4
     * @param friction  空气阻力
     */
    public static void multiLayerBowlParticles(Level level, Vec3 centerPos, int duration, int[] particles, double[] radii,
                                               double[] speeds, double[] angles, double[] color, double @Nullable [] scales, float friction) {
        duration = duration + random.nextInt(duration);
        if (scales == null) {
            scales = new double[radii.length];
            Arrays.fill(scales, 1);
        }
        for (int layer = 0; layer < radii.length; layer++) {
            for (int i = 0; i < particles[layer]; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double radius = radii[layer] * (0.8 + random.nextDouble() * 0.4);
                double startX = centerPos.x + radius * Math.cos(angle);
                double startZ = centerPos.z + radius * Math.sin(angle);
                double startY = centerPos.y;
                double toParticleX = startX - centerPos.x;
                double toParticleZ = startZ - centerPos.z;
                double length = Math.sqrt(toParticleX * toParticleX + toParticleZ * toParticleZ);
                if (length > 0) {
                    toParticleX /= length;
                    toParticleZ /= length;
                }
                double angleRad = Math.toRadians(angles[layer]);
                double speed = speeds[layer] * (0.8 + random.nextDouble() * 0.4);
                double horizontalSpeed = speed * Math.cos(angleRad);
                double verticalSpeed = speed * Math.sin(angleRad);
                AdvancedParticleBase.spawnParticle(level, ParticleInit.ADV_ORB.get(), startX, startY, startZ, toParticleX * horizontalSpeed, verticalSpeed, toParticleZ * horizontalSpeed,
                        true, 0, 0, 0, 0, 0, 1, 1, 1, 0, friction, duration,
                        true, true, false, new ParticleComponent[]{
                                new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), 4, 0, 0, 0, 0.08F, color[0], color[1], color[2], color[3], true, true,
                                        new ParticleComponent[]{
                                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.ALPHA,
                                                        AnimData.KeyTrack.startAndEnd((float) color[3], 0F)),
                                                new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE,
                                                        AnimData.KeyTrack.startAndEnd(0F, (float) scales[layer])),
                                        }, false),
                        });
            }
        }
    }

    public static void advAttractorParticle(ParticleType<AdvancedParticleData> advParticleType, Entity entity,
                                            int points, float yOffset, float scale, float duration,
                                            ParticleComponent[] components, boolean isAnimation) {
        while (--points != 0) {
            double radius = 2f * entity.getBbWidth();
            double yaw = random.nextFloat() * 2 * Math.PI;
            double pitch = random.nextFloat() * 2 * Math.PI;
            double ox = radius * Math.sin(yaw) * Math.sin(pitch);
            double oy = radius * Math.cos(pitch);
            double oz = radius * Math.cos(yaw) * Math.sin(pitch);
            double rootX = entity.getX();
            double rootY = entity.getY() + entity.getBbHeight() / 2f + yOffset;
            double rootZ = entity.getZ();
            AdvancedParticleBase.spawnParticle(entity.level(), advParticleType, rootX + ox, rootY + oy, rootZ + oz, 0, 0, 0,
                    true, 0, 0, 0, 0, scale, 1, 1, 1, 1, 1,
                    duration, true, false, isAnimation, components);
        }
    }
}
