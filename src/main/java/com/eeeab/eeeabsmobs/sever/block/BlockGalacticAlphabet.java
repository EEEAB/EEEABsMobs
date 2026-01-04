package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.properties.SGACharacter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BlockGalacticAlphabet extends Block {
    public static final EnumProperty<SGACharacter> CHARACTER = EnumProperty.create(EEEABMobs.MOD_ID + "_character", SGACharacter.class);
    private final EnumSet<SGACharacter> characters;

    public BlockGalacticAlphabet(Properties properties, EnumSet<SGACharacter> characters) {
        super(properties);
        this.characters = characters;
    }

    /**
     * 初始化自定义方块状态
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARACTER);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(CHARACTER, getCharacters().stream().findAny().orElse(SGACharacter.A));
    }

    public EnumSet<SGACharacter> getCharacters() {
        return characters;
    }
}