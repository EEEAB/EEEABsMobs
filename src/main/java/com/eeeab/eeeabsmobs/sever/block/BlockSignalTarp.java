package com.eeeab.eeeabsmobs.sever.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;

/**
 * 红石信号触发陷阱方块抽象类
 */
public abstract class BlockSignalTarp extends Block {
    protected static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected int interval;

    public BlockSignalTarp(Properties properties, int interval) {
        super(properties);
        this.interval = interval;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.tick(state, level, pos, false);
    }

    protected void tick(BlockState state, Level level, BlockPos pos, boolean onOrOff) {
        if (level.isClientSide) return;
        boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above()) || level.hasNeighborSignal(pos.below());
        boolean flag1 = state.getValue(LIT);
        if (flag && !flag1) {
            if (level instanceof ServerLevel serverLevel) {
                this.active(state, serverLevel, pos);
            } else {
                this.effect(state, level, pos);
            }
        } else if (flag1 && onOrOff) {
            level.scheduleTick(pos, this, this.interval);
            level.setBlock(pos, state.setValue(LIT, false), 2);
        } else {
            level.setBlock(pos, state.setValue(LIT, false), 2);
        }
    }

    /**
     * 在服务端激活时调用此方法
     */
    protected void effect(BlockState state, Level level, BlockPos pos) {

    }

    /**
     * 在客户端激活时调用此方法
     */
    protected void active(BlockState state, ServerLevel level, BlockPos pos) {

    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        this.tick(state, level, pos, true);
    }

    /**
     * 初始化自定义方块状态
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos())).setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

}
