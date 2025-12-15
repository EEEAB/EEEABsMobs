package com.eeeab.eeeabsmobs.sever.world.level.noise;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class CrackIslandDensity implements DensityFunction.SimpleFunction {
    public static final KeyDispatchDataCodec<CrackIslandDensity> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new CrackIslandDensity(0L)));
    private final SimplexNoise islandNoise;

    public CrackIslandDensity(long seed) {
        RandomSource randomsource = new LegacyRandomSource(seed);
        randomsource.consumeCount(17292);
        this.islandNoise = new SimplexNoise(randomsource);
    }

    @Override
    public double compute(DensityFunction.FunctionContext context) {
        return ((double)getHeightValue(this.islandNoise, context.blockX() / 8, context.blockZ() / 8) - 8.0D) / 128.0D;
    }

    private static float getHeightValue(SimplexNoise noise, int x, int z) {
        int i = x / 2;
        int j = z / 2;
        int k = x % 2;
        int l = z % 2;
        float f = 100.0F - Mth.sqrt((float)(x * x + z * z)) * 4.0F;
        f = Mth.clamp(f, -100.0F, 80.0F);

        for(int i1 = -6; i1 <= 6; ++i1) {
            for(int j1 = -6; j1 <= 6; ++j1) {
                long k1 = i + i1;
                long l1 = j + j1;
                if (k1 * k1 + l1 * l1 > 640L && noise.getValue((double)k1, (double)l1) < -0.9) {
                    float f1 = (Mth.abs((float)k1) * 3439.0F + Mth.abs((float)l1) * 147.0F) % 13.0F + 9.0F;
                    float f2 = (float)(k - i1 * 2);
                    float f3 = (float)(l - j1 * 2);
                    float f4 = 100.0F - Mth.sqrt(f2 * f2 + f3 * f3) * f1;
                    f4 = Mth.clamp(f4, -100.0F, 80.0F);
                    f = Math.max(f, f4);
                }
            }
        }

        return f;
    }

    @Override
    public double minValue() {
        return -0.84375D;
    }

    @Override
    public double maxValue() {
        return 0.5625D;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}