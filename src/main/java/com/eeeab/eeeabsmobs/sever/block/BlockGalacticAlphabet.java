package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.properties.SGACharacter;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BlockGalacticAlphabet extends Block {
    public static final EnumProperty<SGACharacter> CHARACTER = EnumProperty.create(EEEABMobs.MOD_ID + "_character", SGACharacter.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final EnumSet<SGACharacter> characters;

    public BlockGalacticAlphabet(Properties properties, EnumSet<SGACharacter> characters) {
        super(properties);
        this.characters = characters;
        this.registerDefaultState(this.stateDefinition.any().setValue(CHARACTER, SGACharacter.A).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARACTER, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(CHARACTER, getCharacters().stream().findAny().orElse(SGACharacter.A)).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }


    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public EnumSet<SGACharacter> getCharacters() {
        return characters;
    }
}