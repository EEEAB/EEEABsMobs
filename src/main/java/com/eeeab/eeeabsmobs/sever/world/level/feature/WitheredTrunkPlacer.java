package com.eeeab.eeeabsmobs.sever.world.level.feature;

import com.eeeab.eeeabsmobs.sever.init.WorldGenInit;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class WitheredTrunkPlacer extends TrunkPlacer {
    public static final Codec<WitheredTrunkPlacer> CODEC = RecordCodecBuilder.create((placerInstance) -> trunkPlacerParts(placerInstance).and(placerInstance.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("top_branch_chance").forGetter((placer) -> placer.topBranchChance),
            Codec.intRange(1, 5).fieldOf("max_top_branches").forGetter((placer) -> placer.maxTopBranches),
            Codec.intRange(1, 5).fieldOf("base_top_branch_length").forGetter((placer) -> placer.baseTopBranchLength),
            Codec.intRange(1, 5).fieldOf("random_top_branch_length").forGetter((placer) -> placer.randomTopBranchLength),
            Codec.intRange(1, 3).fieldOf("mid_branch_max_length").forGetter((placer) -> placer.midBranchMaxLength)
    )).apply(placerInstance, WitheredTrunkPlacer::new));
    private static final List<FoliagePlacer.FoliageAttachment> EMPTY_FOLIAGE = Lists.newArrayList();
    private final float topBranchChance;
    private final int maxTopBranches;
    private final int midBranchMaxLength;
    private final int baseTopBranchLength;
    private final int randomTopBranchLength;

    public WitheredTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, float topBranchChance, int maxTopBranches, int midBranchMaxLength, int baseTopBranchLength, int randomTopBranchLength) {
        super(baseHeight, heightRandA, heightRandB);
        this.topBranchChance = topBranchChance;
        this.maxTopBranches = maxTopBranches;
        this.midBranchMaxLength = midBranchMaxLength;
        this.baseTopBranchLength = baseTopBranchLength;
        this.randomTopBranchLength = randomTopBranchLength;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return WorldGenInit.DEAD_TREE.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                                            RandomSource random, int treeHeight, BlockPos pos, TreeConfiguration config) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        // 生成主树干
        for (int height = 0; height < treeHeight; height++) {
            int y = pos.getY() + height;
            placeLog(level, blockSetter, random, mutablePos.set(pos.getX(), y, pos.getZ()), config);
            // 中间部分生成短分支
            if (height > 1 && height < treeHeight - 3) {
                tryGenerateMidBranch(level, blockSetter, random, y, pos, config);
            }
        }
        // 顶部生成斜向分支
        generateTopBranches(level, blockSetter, random, treeHeight, pos, config);
        return EMPTY_FOLIAGE;
    }

    private void tryGenerateMidBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                                      int currentY, BlockPos pos, TreeConfiguration config) {
        if (random.nextFloat() < 0.6F) {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int branchLength = random.nextInt(midBranchMaxLength) + 1;
            generateSlopedBranch(level, blockSetter, random, currentY, pos, direction, branchLength, config, random.nextFloat() < 0.85F);
        }
    }

    private void generateTopBranches(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                                     int treeHeight, BlockPos pos, TreeConfiguration config) {
        int branchesGenerated = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (branchesGenerated >= maxTopBranches) break;
            if (random.nextFloat() < topBranchChance) {
                int branchLength = random.nextInt(randomTopBranchLength) + baseTopBranchLength;
                generateSlopedBranch(level, blockSetter, random, pos.getY() + treeHeight - 1, pos, direction, branchLength, config, true);
                branchesGenerated++;
            }
        }
    }

    private void generateSlopedBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                                      int startY, BlockPos trunkPos, Direction direction, int length, TreeConfiguration config, boolean isTopBranch) {
        BlockPos.MutableBlockPos branchPos = new BlockPos.MutableBlockPos(trunkPos.getX(), startY, trunkPos.getZ());
        int upwardSlope = isTopBranch ? 1 : 0; // 顶部分支有更大的向上斜率
        for (int i = 0; i < length; i++) {
            branchPos.move(direction);
            branchPos.move(Direction.UP, upwardSlope);
            if (placeLog(level, blockSetter, random, branchPos, config)) {
                // 随机生成次级分支
                if (i > 0 && random.nextFloat() < 0.025F) {
                    generateSecondaryBranch(level, blockSetter, random, branchPos, direction, config);
                }
            } else {
                break;
            }
        }
    }

    private void generateSecondaryBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                                         BlockPos.MutableBlockPos startPos, Direction mainDirection, TreeConfiguration config) {
        Direction secondaryDirection = mainDirection.getClockWise();
        if (random.nextBoolean()) {
            secondaryDirection = mainDirection.getCounterClockWise();
        }
        int length = random.nextInt(2) + 1;
        for (int i = 0; i < length; i++) {
            startPos.move(secondaryDirection);
            if (!placeLog(level, blockSetter, random, startPos, config)) {
                break;
            }
        }
    }
}