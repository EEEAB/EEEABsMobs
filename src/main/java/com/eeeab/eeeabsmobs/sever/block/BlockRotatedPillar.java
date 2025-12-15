package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockRotatedPillar extends RotatedPillarBlock {
    private static Map<Block, Block> STRIPPABLES;
    private final boolean flammable;
    private final int flammability;
    private final int fireSpreadSpeed;

    public BlockRotatedPillar(Properties properties) {
        super(properties);
        this.flammable = false;
        this.flammability = 0;
        this.fireSpreadSpeed = 0;
    }

    //可燃方块使用此构造器
    public BlockRotatedPillar(Properties properties, int flammability, int fireSpreadSpeed) {
        super(properties);
        this.flammable = true;
        this.flammability = flammability;
        this.fireSpreadSpeed = fireSpreadSpeed;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return flammable;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return fireSpreadSpeed;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        ItemStack itemStack = context.getItemInHand();
        if (!itemStack.canPerformAction(toolAction))
            return null;
        if (ToolActions.AXE_STRIP == toolAction) {
            if (STRIPPABLES == null) {
                STRIPPABLES = new ImmutableMap.Builder<Block, Block>()
                        .put(BlockInit.EROSION_OAK_LOG.get(), BlockInit.STRIPPED_EROSION_OAK_LOG.get())
                        .put(BlockInit.EROSION_OAK_WOOD.get(), BlockInit.STRIPPED_EROSION_OAK_WOOD.get())
                        .put(BlockInit.BLIGHTED_OAK_LOG.get(), BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get())
                        .put(BlockInit.BLIGHTED_OAK_WOOD.get(), BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get())
                        .build();
            }
            Block block = STRIPPABLES.get(state.getBlock());
            return block != null ? block.defaultBlockState().setValue(AXIS, state.getValue(AXIS)) : null;
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
