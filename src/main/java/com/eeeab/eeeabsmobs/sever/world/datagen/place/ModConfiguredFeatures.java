package com.eeeab.eeeabsmobs.sever.world.datagen.place;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import com.eeeab.eeeabsmobs.sever.world.level.feature.EmptyFoliagePlacer;
import com.eeeab.eeeabsmobs.sever.world.level.feature.WitheredTrunkPlacer;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class ModConfiguredFeatures {
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        /* Tree Configured */
        //register(context, ModResourceKey.EROSION_OAK, Feature.TREE,
        //        (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockInit.EROSION_OAK_LOG.get()),
        //                new StraightTrunkPlacer(7, 2, 3), BlockStateProvider.simple(BlockInit.EROSION_OAK_LEAVES.get()),
        //                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)),
        //                new TwoLayersFeatureSize(2, 0, 2)))
        //                .decorators(List.of(
        //                        new ErosionOakLeavesDecorator(),
        //                        new AttachedToLeavesDecorator(0.14F, 2, 1, BlockStateProvider.simple(BlockInit.EROSION_OAK_BERRY.get().defaultBlockState().setValue(BlockErosionOakBerry.HANGING, true)), 1, List.of(Direction.DOWN))
        //                ))
        //                .ignoreVines().build());

        //register(context, ModResourceKey.MEGA_EROSION_OAK, Feature.TREE,
        //        (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockInit.EROSION_OAK_LOG.get()),
        //                new GiantTrunkPlacer(16, 2, 15), BlockStateProvider.simple(BlockInit.EROSION_OAK_LEAVES.get()),
        //                new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(13, 17)),
        //                new TwoLayersFeatureSize(1, 1, 2)))
        //                .decorators(ImmutableList.of(
        //                        new ErosionOakLeavesDecorator(),
        //                        new AttachedToLeavesDecorator(0.15F, 1, 0, BlockStateProvider.simple(BlockInit.EROSION_OAK_BERRY.get().defaultBlockState().setValue(BlockErosionOakBerry.HANGING, true)), 1, List.of(Direction.DOWN)),
        //                        new AlterGroundDecorator(BlockStateProvider.simple(BlockInit.ERODED_SOIL.get()))
        //                ))
        //                .build());

        register(context, ModResourceKey.BLIGHTED_OAK, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockInit.BLIGHTED_OAK_LOG.get()),
                new WitheredTrunkPlacer(6, 3, 2, 0.8F, 4, 3, 2, 3),
                BlockStateProvider.simple(Blocks.AIR),
                new EmptyFoliagePlacer(),
                new TwoLayersFeatureSize(0, 0, 0))
                .build());
        /* Ore Configured */
        TagMatchTest rule1 = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        register(context, ModResourceKey.VOIDSHARD, Feature.ORE, new OreConfiguration(rule1, BlockInit.VOIDSHARD.get().defaultBlockState(), 33));
        register(context, ModResourceKey.DARKENED_COAL_ORE, Feature.ORE, new OreConfiguration(rule1, BlockInit.DARKENED_COAL_ORE.get().defaultBlockState(), 17));
        register(context, ModResourceKey.DARKENED_IRON_ORE, Feature.ORE, new OreConfiguration(rule1, BlockInit.DARKENED_IRON_ORE.get().defaultBlockState(), 9));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}