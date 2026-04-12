package com.eeeab.eeeabsmobs.sever.world.level.noise;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import com.eeeab.eeeabsmobs.sever.world.level.ModSurfaceRuleData;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;

import java.util.List;

public class ModNoiseSettings {
    public static NoiseGeneratorSettings voidCrack(BootstapContext<?> context) {
        HolderGetter<DensityFunction> densityFunction = context.lookup(Registries.DENSITY_FUNCTION);
        DensityFunction densityFunction1 = DensityFunctions.cache2d(new CrackIslandDensity(0L));
        //DensityFunction densityFunction2 = postProcess(slideEndLike(new DensityFunctions.HolderHolder(densityFunction.getOrThrow(ModResourceKey.SLOPED_CHEESE_VOID_CRACK)), 0, 128));
        DensityFunction densityFunction2 = null;
        NoiseRouter router = new NoiseRouter(
                DensityFunctions.zero(), DensityFunctions.zero(),
                DensityFunctions.zero(), DensityFunctions.zero(),
                DensityFunctions.zero(), DensityFunctions.zero(),
                DensityFunctions.zero(),
                densityFunction1,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                slideEndLike(DensityFunctions.add(
                        densityFunction1,
                        DensityFunctions.constant(-0.703125D)
                ), 0, 128),
                densityFunction2, DensityFunctions.zero(),
                DensityFunctions.zero(), DensityFunctions.zero()
        );
        return new NoiseGeneratorSettings(
                NoiseSettings.create(0, 128, 2, 1),
                BlockInit.BLIGHTED_STONE.get().defaultBlockState(),
                Blocks.AIR.defaultBlockState(),
                router,
                ModSurfaceRuleData.voidCrack(),
                List.of(),
                0, true, false, false, true
        );
    }

    private static DensityFunction slideEndLike(DensityFunction func, int minY, int height) {
        return slide(func, minY, height, 72, -184, -23.4375D, 4, 32, -0.234375D);
    }

    private static DensityFunction slide(DensityFunction func, int minY, int height, int startY, int endY, double startVal, int bottomStart, int bottomEnd, double bottomVal) {
        DensityFunction gradient = DensityFunctions.yClampedGradient(
                minY + height - startY,
                minY + height - endY,
                1.0, 0.0
        );
        DensityFunction lerped = DensityFunctions.lerp(gradient, startVal, func);
        DensityFunction bottomGradient = DensityFunctions.yClampedGradient(
                minY + bottomStart,
                minY + bottomEnd,
                0.0, 1.0
        );
        return DensityFunctions.lerp(bottomGradient, bottomVal, lerped);
    }

    private static DensityFunction postProcess(DensityFunction function) {
        DensityFunction densityfunction = DensityFunctions.blendDensity(function);
        return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction), DensityFunctions.constant(0.64D)).squeeze();
    }
}

