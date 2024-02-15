package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import com.eeeab.eeeabsmobs.sever.world.portal.CuboidPortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public class BlockErosionPortal extends NetherPortalBlock {
    public BlockErosionPortal(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
    }

    public static void portalSpawn(Level level, BlockPos pos) {
        Optional<CuboidPortalShape> optional = CuboidPortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X);
        optional.ifPresent(CuboidPortalShape::createPortalBlocks);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingState, LevelAccessor levelAccessor, BlockPos currentPos, BlockPos facingPos) {
        Direction.Axis direction$axis = facing.getAxis();
        Direction.Axis direction$axis1 = blockState.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && !facingState.is(this) && !(new CuboidPortalShape(levelAccessor, currentPos, direction$axis1)).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, facing, facingState, levelAccessor, currentPos, facingPos);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
/*        for (int i = 0; i < 4; i++) {
            double px = pos.getX() + random.nextFloat();
            double py = pos.getY() + random.nextFloat();
            double pz = pos.getZ() + random.nextFloat();
            double vx = (random.nextFloat() - 0.5) / 2.;
            double vy = (random.nextFloat() - 0.5) / 2.;
            double vz = (random.nextFloat() - 0.5) / 2.;
            int j = random.nextInt(4) - 1;
            if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
                px = pos.getX() + 0.5 + 0.25 * j;
                vx = random.nextFloat() * 2 * j;
            } else {
                pz = pos.getZ() + 0.5 + 0.25 * j;
                vz = random.nextFloat() * 2 * j;
            }
            world.addParticle(ParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
        }*/
        if (random.nextInt(100) == 0) {
            world.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            ResourceKey<Level> resourceKey = entity.level().dimension() == EMResourceKey.EROSION ? Level.OVERWORLD : EMResourceKey.EROSION;
            ServerLevel portalDimension = server.getLevel(resourceKey);
            if (portalDimension != null && !entity.isPassenger() && entity.canChangeDimensions()) {
                if (entity.isOnPortalCooldown()) {
                    entity.setPortalCooldown();
                } else {
                    entity.setPortalCooldown();
                    //维度未实现
                    //entity.changeDimension(portalDimension, new EMTeleporter(portalDimension, pos));
                    //if (resourceKey == EMResourceKey.EROSION)
                    //    EEEABMobs.LOGGER.debug("Entity '{}' enters the erosion dimension", entity.getName().getString());
                }
            }
        }
    }


}
