package com.eeeab.eeeabsmobs.sever.block.trapImpl;

import com.eeeab.eeeabsmobs.sever.block.BlockStepOnTrap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSteppingFlameTrap extends BlockStepOnTrap {
    public BlockSteppingFlameTrap(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (state.getValue(OPEN) && !entity.getType().fireImmune()) {
            entity.setSecondsOnFire(8);
        }
    }

    @Override
    protected void doActiveEffect(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(OPEN)) {
            for (int i = 0; i < 3; i++) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
                double y = pos.getY() + 1;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
                level.addParticle(random.nextFloat() < 0.2F ? ParticleTypes.SMOKE : ParticleTypes.FLAME, x, y, z, 0, random.nextDouble() * 0.2F, 0);
            }
        }
    }
}
