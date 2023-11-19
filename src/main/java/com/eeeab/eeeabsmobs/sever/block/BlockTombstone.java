package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.sever.entity.block.EntityBlockTombstone;
import com.eeeab.eeeabsmobs.sever.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

//TODO 未完成方块
public class BlockTombstone extends BaseEntityBlock {
    private static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape AXIS_AABB = Stream.of(Block.box(0D, 0.0D, 0D, 16.0D, 15.9D, 16.0D)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public BlockTombstone() {
        super(BlockBehaviour.Properties.of().lightLevel((blockState) -> 7).
                hasPostProcess((pState, pLevel, pPos) -> true).strength(-1.0F, 3600000.0F).sound(SoundType.STONE));
    }


    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return AXIS_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AXIS_AABB;
    }


    //@Override
    //public boolean useShapeForLightOcclusion(BlockState pState) {
    //    return true;
    //}

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        //ItemStack itemStack = player.getItemInHand(hand);
        //EntityBlockTombstone blockEntity = (EntityBlockTombstone) level.getBlockEntity(blockPos);
        //if (blockEntity != null && itemStack.getItem() != this.asItem()) {
        //    ItemStack copy = itemStack.copy();
        //    copy.setCount(1);
        //    if (blockEntity.getItem(0).isEmpty()) {
        //        blockEntity.setItem(0, copy);
        //        if (!player.isCreative()) {
        //            itemStack.shrink(1);
        //        }
        //    } else {
        //        popResource(level, blockPos, blockEntity.getItem(0).copy());
        //        blockEntity.setItem(0, ItemStack.EMPTY);
        //    }
        //    return InteractionResult.SUCCESS;
        //}

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EntityBlockTombstone(blockPos, blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, BlockEntityInit.ENTITY_TOMBSTONE.get(), EntityBlockTombstone::Ticker);
    }
}
