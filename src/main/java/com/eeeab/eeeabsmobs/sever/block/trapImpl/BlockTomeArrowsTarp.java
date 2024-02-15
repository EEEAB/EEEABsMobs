package com.eeeab.eeeabsmobs.sever.block.trapImpl;

import com.eeeab.eeeabsmobs.sever.block.BlockSignalTarp;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityPoisonArrow;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockTomeArrowsTarp extends BlockSignalTarp {

    public BlockTomeArrowsTarp(Properties properties) {
        super(properties, 20);
    }

    protected void active(BlockState state, ServerLevel level, BlockPos pos) {
        Position position = DispenserBlock.getDispensePosition(new BlockSourceImpl(level, pos));
        Direction direction = state.getValue(FACING);
        EntityPoisonArrow arrow = new EntityPoisonArrow(level, position);
        arrow.shoot(direction.getStepX(), direction.getStepY() + 0.1D, direction.getStepZ(), 1.5F, 1);
        level.addFreshEntity(arrow);
        level.setBlock(pos, state.setValue(LIT, true), 2);
        level.scheduleTick(pos, this, this.interval);
        level.levelEvent(1002, pos, 0);
        level.levelEvent(2000, pos, direction.get3DDataValue());
    }

}
