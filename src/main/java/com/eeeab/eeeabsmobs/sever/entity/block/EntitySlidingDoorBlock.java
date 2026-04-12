package com.eeeab.eeeabsmobs.sever.entity.block;

import com.eeeab.animate.server.animation.OverlapAnimationState;
import com.eeeab.eeeabsmobs.sever.block.BlockSlidingDoor;
import com.eeeab.eeeabsmobs.sever.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntitySlidingDoorBlock extends BlockEntity {
    public AnimationState closingAnimationState = new OverlapAnimationState(10);
    public AnimationState openingAnimationState = new AnimationState();
    public int tickCount;
    public Direction facing;

    public EntitySlidingDoorBlock(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SLIDING_DOOR_BE.get(), pos, state);
        this.facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EntitySlidingDoorBlock be) {
        be.tickCount++;
        if (level.isClientSide) {
            if (state.getBlock() instanceof BlockSlidingDoor) {
                be.openingAnimationState.animateWhen(state.getValue(BlockSlidingDoor.LIT), be.tickCount);
            }
        }
    }

    @Override
    public boolean triggerEvent(int type, int data) {
        if (type == 1) {
            openingAnimationState.start(tickCount);
            return true;
        } else if (type == 2) {
            openingAnimationState.stop();
            closingAnimationState.start(tickCount);
            return true;
        }
        return super.triggerEvent(type, data);
    }

    @Override
    public AABB getRenderBoundingBox() {
        AABB aabb = super.getRenderBoundingBox();
        aabb = aabb.expandTowards(new Vec3(facing.getClockWise().step()));
        aabb = aabb.expandTowards(new Vec3(facing.getCounterClockWise().step()));
        return aabb.expandTowards(0, 3, 0);
    }
}