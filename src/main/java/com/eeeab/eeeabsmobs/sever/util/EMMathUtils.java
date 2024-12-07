package com.eeeab.eeeabsmobs.sever.util;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * 数学工具类
 *
 * @author EEEAB
 */
public class EMMathUtils {

    /**
     * 将给定的值限制在指定的最小值和最大值之间
     *
     * @param value 限制的数值
     * @param min   最小值。
     * @param max   最大值。
     * @return 在[min, max]区间内的值
     */
    public static float clamp(float value, float min, float max) {
        return value < min ? min : Math.min(value, max);
    }

    /**
     * 计算基于当前tick和总持续时间的进度因子
     *
     * @param tick      游戏刻
     * @param duration  总持续时间
     * @param inversion 是否反转进度因子计算
     * @return 进度因子 范围0~1之间
     */
    public static float getTickFactor(float tick, float duration, boolean inversion) {
        float v = clamp(tick / duration, 0F, 1F);
        return inversion ? 1F - v : v;
    }

    /**
     * 计算基于时间进程和距离因子的速度乘数
     *
     * @param tickFactor     当前的时间进程因子 范围在0~1之间
     * @param distanceFactor 实体与目标之间的距离因子 范围在0~1之间
     * @param exponent       控制速度变化曲线的指数，指数越高初始速度越快衰减越快 反之则趋于平滑
     * @param speedModifier  用于调整最终速度乘数的常数因子
     * @return 速度乘数
     */
    public static float calculateSpeedMultiplier(float tickFactor, float distanceFactor, float exponent, float speedModifier) {
        float speedFactor = (float) Math.pow(tickFactor, exponent) * (float) Math.pow(2, -exponent * (1 - tickFactor)) * distanceFactor;
        return speedModifier * speedFactor;
    }

    public static EMMathUtils.Axis XN = new EMMathUtils.Axis(-1.0F, 0.0F, 0.0F);
    public static EMMathUtils.Axis XP = new EMMathUtils.Axis(1.0F, 0.0F, 0.0F);
    public static EMMathUtils.Axis YN = new EMMathUtils.Axis(0.0F, -1.0F, 0.0F);
    public static EMMathUtils.Axis YP = new EMMathUtils.Axis(0.0F, 1.0F, 0.0F);
    public static EMMathUtils.Axis ZN = new EMMathUtils.Axis(0.0F, 0.0F, -1.0F);
    public static EMMathUtils.Axis ZP = new EMMathUtils.Axis(0.0F, 0.0F, 1.0F);

    /**
     * 指定轴旋转
     *
     * @param axis    转轴方向矢量
     * @param angle   轴旋转角度
     * @return 新复合旋转四元数实例
     */
    public static Quaternionf rotation(Vector3f axis, float angle, boolean degrees) {
        if (degrees) {
            angle *= (float) (Math.PI / 180F);
        }
        return new Quaternionf().setAngleAxis(angle, axis.x, axis.y, axis.z);
    }

    /**
     * 复合旋转
     *
     * @param x       X轴旋转角度
     * @param y       Y轴旋转角度
     * @param z       Z轴旋转角度
     * @return 新复合旋转四元数实例
     */
    public static Quaternionf rotationXYZ(float x, float y, float z, boolean degrees) {
        if (degrees) {
            x *= ((float) Math.PI / 180F);
            y *= ((float) Math.PI / 180F);
            z *= ((float) Math.PI / 180F);
        }
        return new Quaternionf().rotationXYZ(x, y, z);
    }

    public static class Axis {
        private final Vector3f axis;

        public Axis(float x, float y, float z) {
            this.axis = new Vector3f(x, y, z);
        }

        public Quaternionf rotation(float angle) {
            return EMMathUtils.rotation(axis, angle, false);
        }

        public Quaternionf rotationDegrees(float degrees) {
            return EMMathUtils.rotation(axis, degrees, true);
        }
    }
}
