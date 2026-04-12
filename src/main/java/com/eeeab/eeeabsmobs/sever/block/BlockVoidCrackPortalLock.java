package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.sever.item.ItemGuardianCore;
import com.eeeab.eeeabsmobs.sever.world.portal.PortalStructureHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class BlockVoidCrackPortalLock extends HorizontalDirectionalBlock {
    public BlockVoidCrackPortalLock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() instanceof ItemGuardianCore && !player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                if (PortalStructureHelper.tryActivatePortalFromLock(level, pos)) {
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        //if (random.nextInt(10) == 0) {
        //    double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        //    double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        //    double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        //    level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0, 0, 0);
        //}
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}