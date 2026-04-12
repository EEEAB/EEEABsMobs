package com.eeeab.eeeabsmobs.client.render.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LightningPathProvider {

    /**
     * 生成闪电路径
     *
     * @param start         起点
     * @param end           终点
     * @param segments      分段数
     * @param parallelNoise 线段长度随机因子[0-1] 0表示均匀分段
     * @param spreadFactor  线段扩散弯曲幅度[0-1] 0无弯曲
     * @param random        随机数生成器
     */
    public static List<PathPoint> generateLightningPath(Vec3 start, Vec3 end, int segments, float parallelNoise, float spreadFactor, RandomSource random) {
        List<PathPoint> points = new ArrayList<>();
        Vec3 diff = end.subtract(start);
        float totalDist = (float) diff.length();
        Vec3 prevPerp = Vec3.ZERO;
        float progress = 0;

        points.add(new PathPoint(start, 0F));

        while (progress < 1) {
            float nextProgress = progress + (1F / segments) * (1 - parallelNoise + random.nextFloat() * parallelNoise * 2);
            if (nextProgress >= 1) nextProgress = 1;

            float spreadScale = (float) Math.sin(Math.PI * progress);
            float maxDiff = spreadFactor * spreadScale * totalDist * random.nextFloat();
            Vec3 rand = new Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
            Vec3 randOrth = diff.cross(rand).normalize();
            Vec3 perpAdd = randOrth.scale(maxDiff);
            Vec3 perp = prevPerp.add(perpAdd);

            Vec3 segmentEnd = start.add(diff.scale(nextProgress)).add(perp);
            points.add(new PathPoint(segmentEnd, nextProgress));

            prevPerp = perp;
            progress = nextProgress;
        }
        return points;
    }

    public record PathPoint(Vec3 point, float progress) {
    }
}
