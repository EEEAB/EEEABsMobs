package com.eeeab.eeeabsmobs.sever.block.trapImpl;

import com.eeeab.eeeabsmobs.sever.block.BlockStepOnTrap;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockTombGasTrap extends BlockStepOnTrap {
    public BlockTombGasTrap(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (state.getValue(OPEN)) {
            if (entity instanceof LivingEntity livingEntity && !livingEntity.hasEffect(MobEffects.POISON)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1), null);
            }
        }
    }

    @Override
    protected void doActiveEffect(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(OPEN)) {
            double range = 0.5625;
            Direction[] directions = Direction.values();
            for (Direction direction : directions) {
                Direction.Axis axis = direction.getAxis();
                double x = axis == Direction.Axis.X ? 0.5 + range * direction.getStepX() : random.nextFloat();
                double y = axis == Direction.Axis.Y ? 0.5 + range * direction.getStepY() : random.nextFloat();
                double z = axis == Direction.Axis.Z ? 0.5 + range * direction.getStepZ() : random.nextFloat();
                level.addParticle(ParticleInit.POISON.get(), pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0, 0.007, 0);
            }
        }
    }
}
