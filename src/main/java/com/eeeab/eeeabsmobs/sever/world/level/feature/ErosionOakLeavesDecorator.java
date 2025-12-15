package com.eeeab.eeeabsmobs.sever.world.level.feature;

import com.eeeab.eeeabsmobs.sever.block.BlockErosionOakLeaves;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.WorldGenInit;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class ErosionOakLeavesDecorator extends TreeDecorator {
    public static final Codec<ErosionOakLeavesDecorator> CODEC = Codec.unit(ErosionOakLeavesDecorator::new);

    @Override
    public void place(Context context) {
        List<BlockPos> leaves = context.leaves();
        if (leaves.isEmpty()) return;
        int topY = leaves.stream().mapToInt(BlockPos::getY).max().orElse(0);
        int bottomY = context.logs().stream().mapToInt(BlockPos::getY).min().orElse(topY);
        for (BlockPos pos : leaves) {
            float heightRatio = (float) (pos.getY() - bottomY) / (topY - bottomY);
            int layer = (int) (heightRatio * 3);
            layer = 3 - Mth.clamp(layer, 0, 3);
            BlockState state = BlockInit.EROSION_OAK_LEAVES.get()
                    .defaultBlockState()
                    .setValue(BlockErosionOakLeaves.PERSISTENT, true)
                    .setValue(BlockErosionOakLeaves.WATERLOGGED, context.level().isFluidAtPosition(pos, fluidState -> fluidState.is(Fluids.WATER)))
                    .setValue(BlockErosionOakLeaves.LAYER, layer);
            context.setBlock(pos, state);
        }
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return WorldGenInit.EROSION_OAK_LEAVES.get();
    }
}