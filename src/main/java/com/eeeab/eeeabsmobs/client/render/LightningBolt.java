package com.eeeab.eeeabsmobs.client.render;

import com.eeeab.eeeabsmobs.client.render.util.LightningPathProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//参考自: https://github.com/AlexModGuy/Ice_and_Fire/blob/1.20/src/main/java/com/github/alexthe666/iceandfire/client/particle/LightningBoltData.java
public class LightningBolt {
    private final RandomSource random;
    private final Vec3 start;
    private final Vec3 end;
    private final int segments;
    private final int count;
    private final float size;
    private final int lifespan;
    private final boolean consecutive;
    private final float spawnDelay;
    private final float parallelNoise;
    private final float spreadFactor;
    private final Vector4f color;
    private final FadeFunction fadeFunction;

    public static final LightningBoltBuilder DEFAULT = new LightningBoltBuilder()
            .color(new Vector4f(0.45F, 0.45F, 0.5F, 0.8F))
            .count(1).size(0.1F).lifespan(5).parallelNoise(0.1F)
            .spreadFactor(0.1F).fadeFunction(FadeFunction.fade(0.2F));

    public LightningBolt(RandomSource random, Vec3 start, Vec3 end,
                         int segments, int count, float size, int lifespan,
                         boolean consecutive, float spawnDelay, float parallelNoise,
                         float spreadFactor, Vector4f color, FadeFunction fadeFunction) {
        this.random = random;
        this.start = start;
        this.end = end;
        this.segments = segments;
        this.count = count;
        this.size = size;
        this.lifespan = lifespan;
        this.consecutive = consecutive;
        this.spawnDelay = spawnDelay;
        this.parallelNoise = parallelNoise;
        this.spreadFactor = spreadFactor;
        this.color = color;
        this.fadeFunction = fadeFunction;
    }

    public int getLifespan() {
        return lifespan;
    }

    public boolean isConsecutive() {
        return consecutive;
    }

    public FadeFunction getFadeFunction() {
        return fadeFunction;
    }

    public Vector4f getColor() {
        return color;
    }

    public float getSpawnDelay() {
        return spawnDelay;
    }

    /**
     * 预计算生成所有闪电的四边形
     */
    public List<BoltQuads> generate() {
        List<BoltQuads> quads = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            QuadCache prevCache = null;
            List<LightningPathProvider.PathPoint> pathPoints = LightningPathProvider.generateLightningPath(
                    start, end, segments, parallelNoise, spreadFactor, random
            );
            for (int idx = 1; idx < pathPoints.size(); idx++) {
                LightningPathProvider.PathPoint prev = pathPoints.get(idx - 1);
                LightningPathProvider.PathPoint curr = pathPoints.get(idx);
                float segmentSize = size * (0.5F + (1 - curr.progress()) * 0.5F);
                Pair<BoltQuads, QuadCache> pair = createQuads(prevCache, prev.point(), curr.point(), segmentSize);
                quads.add(pair.getLeft());
                prevCache = pair.getRight();
            }
        }
        return quads;
    }

    private Pair<BoltQuads, QuadCache> createQuads(QuadCache cache, Vec3 start, Vec3 end, float size) {
        Vec3 diff = end.subtract(start);
        Vec3 right = diff.cross(new Vec3(0.5, 0.5, 0.5)).normalize().scale(size);
        Vec3 back = diff.cross(right).normalize().scale(size);
        Vec3 rightHalf = right.scale(0.5F);
        Vec3 s = (cache != null) ? cache.prevEnd : start;
        Vec3 sr = (cache != null) ? cache.prevEndRight : start.add(right);
        Vec3 sb = (cache != null) ? cache.prevEndBack : start.add(rightHalf).add(back);
        Vec3 er = end.add(right);
        Vec3 eb = end.add(rightHalf).add(back);
        BoltQuads quads = new BoltQuads();
        quads.addQuad(s, end, er, sr);
        quads.addQuad(sr, er, end, s);
        quads.addQuad(sr, er, eb, sb);
        quads.addQuad(sb, eb, er, sr);
        return Pair.of(quads, new QuadCache(end, er, eb));
    }

    public static class BoltQuads {
        private final List<Vec3> vecs = new ArrayList<>();

        void addQuad(Vec3... corners) {
            vecs.addAll(Arrays.asList(corners));
        }

        public List<Vec3> getVecs() {
            return vecs;
        }
    }

    private static class QuadCache {
        final Vec3 prevEnd, prevEndRight, prevEndBack;

        QuadCache(Vec3 end, Vec3 right, Vec3 back) {
            this.prevEnd = end;
            this.prevEndRight = right;
            this.prevEndBack = back;
        }
    }

    /**
     * 渐变函数接口
     */
    @FunctionalInterface
    public interface FadeFunction {
        /**
         * 无渐变
         */
        FadeFunction NONE = (total, life) -> Pair.of(0, total);

        /**
         * @param fadeDuration 渐变时长占总生命周期的比例
         */
        static FadeFunction fade(float fadeDuration) {
            return (total, life) -> {
                int start = life > 1 - fadeDuration ? (int) (total * (life - (1 - fadeDuration)) / fadeDuration) : 0;
                int end = life < fadeDuration ? (int) (total * (life / fadeDuration)) : total;
                return Pair.of(start, end);
            };
        }

        Pair<Integer, Integer> getRenderBounds(int totalSegments, float lifeScale);
    }

    public static class LightningBoltBuilder {
        private int segments;
        private int count;
        private float size;
        private int lifespan;
        private boolean consecutive;
        private float spawnDelay;
        private float parallelNoise;
        private float spreadFactor;
        private Vector4f color;
        private FadeFunction fadeFunction = FadeFunction.NONE;

        /**
         * @param segments 分段数（影响弯曲精细度）
         */
        public LightningBoltBuilder segments(int segments) {
            this.segments = segments;
            return this;
        }

        /**
         * @param count 并行渲染闪电数
         */
        public LightningBoltBuilder count(int count) {
            this.count = count;
            return this;
        }

        /**
         * @param lifespan 存在时间 单位tick
         */
        public LightningBoltBuilder lifespan(int lifespan) {
            this.lifespan = lifespan;
            return this;
        }

        /**
         * @param size 粗细
         */
        public LightningBoltBuilder size(float size) {
            this.size = size;
            return this;
        }

        /**
         * @param spawnDelay 生成延迟 单位tick
         */
        public LightningBoltBuilder spawnDelay(float spawnDelay) {
            this.spawnDelay = spawnDelay;
            return this;
        }

        /**
         * @param parallelNoise 线段长度随机因子[0-1] 0表示均匀分段
         */
        public LightningBoltBuilder parallelNoise(float parallelNoise) {
            this.parallelNoise = parallelNoise;
            return this;
        }

        /**
         * @param spreadFactor 线段扩散弯曲幅度[0-1] 0无弯曲
         */
        public LightningBoltBuilder spreadFactor(float spreadFactor) {
            this.spreadFactor = spreadFactor;
            return this;
        }

        /**
         * @param fadeFunction 渐变函数
         */
        public LightningBoltBuilder fadeFunction(FadeFunction fadeFunction) {
            this.fadeFunction = fadeFunction;
            return this;
        }

        /**
         * @param consecutive 是否连续生成
         */
        public LightningBoltBuilder consecutive(boolean consecutive) {
            this.consecutive = consecutive;
            return this;
        }

        /**
         * @param color RGBA 颜色
         */
        public LightningBoltBuilder color(Vector4f color) {
            this.color = color;
            return this;
        }

        public LightningBolt build(Vec3 start, Vec3 end, RandomSource random) {
            if (this.segments <= 0) this.segments = (int) Math.sqrt(start.distanceTo(end) * 100);
            return new LightningBolt(
                    random, start, end,
                    segments, count, size,
                    lifespan, consecutive, spawnDelay,
                    parallelNoise, spreadFactor, color
                    , fadeFunction
            );
        }
    }
}