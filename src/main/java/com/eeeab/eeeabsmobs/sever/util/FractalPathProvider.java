package com.eeeab.eeeabsmobs.sever.util;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 模拟电流路径生成
 *
 * @author EEEAB
 */
public class FractalPathProvider {

    /**
     * 生成分形电流轨迹
     *
     * @param start      起点坐标
     * @param end        终点坐标
     * @param iterations 分形迭代次数
     * @param roughness  粗糙度
     */
    public static List<Vec3> getFractalPath(Vec3 start, Vec3 end, int iterations, double roughness) {
        List<Vec3> path = new ArrayList<>();
        Random random = new Random();
        path.add(start);
        path.add(end);
        for (int i = 0; i < iterations; i++) {
            List<Vec3> newPath = new ArrayList<>();
            for (int j = 0; j < path.size() - 1; j++) {
                Vec3 current = path.get(j);
                Vec3 next = path.get(j + 1);
                newPath.add(current);
                Vec3 midPoint = new Vec3((current.x + next.x) / 2, (current.y + next.y) / 2, (current.z + next.z) / 2);
                Vec3 direction = next.subtract(current);
                Vec3 perpendicular = calculatePerpendicular(direction);
                double scale = roughness * Math.pow(0.5, i);
                Vec3 offset = perpendicular.scale((random.nextDouble() - 0.5) * scale);
                midPoint = midPoint.add(offset);
                newPath.add(midPoint);
            }
            newPath.add(path.get(path.size() - 1));
            path = newPath;
        }
        return path;
    }

    //计算垂直于给定向量的随机方向
    private static Vec3 calculatePerpendicular(Vec3 v) {
        if (Math.abs(v.x) < 1e-10 && Math.abs(v.y) < 1e-10 && Math.abs(v.z) < 1e-10) {
            return new Vec3(1, 0, 0);
        }
        Vec3 notParallel;
        if (Math.abs(v.x) < Math.abs(v.y) || Math.abs(v.x) < Math.abs(v.z)) {
            notParallel = new Vec3(1, 0, 0);
        } else if (Math.abs(v.y) < Math.abs(v.z)) {
            notParallel = new Vec3(0, 1, 0);
        } else {
            notParallel = new Vec3(0, 0, 1);
        }
        return crossProduct(v, notParallel).normalize();
    }

    //计算叉积
    private static Vec3 crossProduct(Vec3 a, Vec3 b) {
        return new Vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }
}
