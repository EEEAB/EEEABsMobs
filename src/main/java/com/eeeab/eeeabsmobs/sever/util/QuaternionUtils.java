package com.eeeab.eeeabsmobs.sever.util;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * 四元数工具类
 *
 * @author EEEAB
 */
public class QuaternionUtils {
    public static Axis XN = new Axis(-1.0F, 0.0F, 0.0F);
    public static Axis XP = new Axis(1.0F, 0.0F, 0.0F);
    public static Axis YN = new Axis(0.0F, -1.0F, 0.0F);
    public static Axis YP = new Axis(0.0F, 1.0F, 0.0F);
    public static Axis ZN = new Axis(0.0F, 0.0F, -1.0F);
    public static Axis ZP = new Axis(0.0F, 0.0F, 1.0F);

    /**
     * 设置四元数为围绕指定轴的旋转.
     *
     * @param axis    旋转轴的方向矢量.
     * @param angle   绕轴旋转的角度, 单位为弧度或度(取决于degrees参数).
     * @param degrees 如果为true, 则将角度从度转换为弧度.
     * @return 一个新的表示该旋转的四元数实例.
     */
    public static Quaternionf rotation(Vector3f axis, float angle, boolean degrees) {
        if (degrees) {
            angle *= (float) (Math.PI / 180F);
        }
        return new Quaternionf().setAngleAxis(angle, axis.x, axis.y, axis.z);
    }

    /**
     * 设置四元数为围绕X, Y, Z轴的复合旋转.
     *
     * @param x       绕X轴旋转的角度, 单位为弧度或度(取决于degrees参数).
     * @param y       绕Y轴旋转的角度, 单位为弧度或度(取决于degrees参数).
     * @param z       绕Z轴旋转的角度, 单位为弧度或度(取决于degrees参数).
     * @param degrees 如果为true, 则将角度从度转换为弧度.
     * @return 一个新的表示复合旋转的四元数实例.
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
            return QuaternionUtils.rotation(axis, angle, false);
        }

        public Quaternionf rotationDegrees(float degrees) {
            return QuaternionUtils.rotation(axis, degrees, true);
        }
    }
}
