package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.sever.entity.block.EntityBlockTombstone;
import com.eeeab.eeeabsmobs.sever.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

//TODO 未完成方块
public class BlockTombstone extends BaseEntityBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public BlockTombstone() {
        super(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(POWER)).hasPostProcess((state, level, pos) -> state.getValue(POWER) > 0).strength(-1.0F, 3600000.0F).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        EntityBlockTombstone blockEntity = (EntityBlockTombstone) level.getBlockEntity(blockPos);
        if (blockEntity != null && itemStack.getItem() != this.asItem()) {
            ItemStack copy = itemStack.copy();
            copy.setCount(1);
            if (blockEntity.getItem(0).isEmpty()) {
                blockEntity.setItem(0, copy);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
            } else {
                popResource(level, blockPos, blockEntity.getItem(0).copy());
                blockEntity.setItem(0, ItemStack.EMPTY);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EntityBlockTombstone(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWER);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, BlockEntityInit.ENTITY_TOMBSTONE.get(), EntityBlockTombstone::Ticker);
    }
}
