package com.eeeab.eeeabsmobs.sever.block;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlockMirroredFlower extends FlowerBlock {
    public static final BooleanProperty MIRRORED = BooleanProperty.create("mirrored");

    public BlockMirroredFlower(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
        super(effectSupplier, effectDuration, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MIRRORED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MIRRORED);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(MIRRORED, context.getLevel().getRandom().nextBoolean());
    }
}
