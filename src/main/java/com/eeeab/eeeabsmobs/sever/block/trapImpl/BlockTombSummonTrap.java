package com.eeeab.eeeabsmobs.sever.block.trapImpl;

import com.eeeab.eeeabsmobs.sever.block.BlockStepOnTrap;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalGolem;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockTombSummonTrap extends BlockStepOnTrap {

    public BlockTombSummonTrap(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void active(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!state.getValue(OPEN) && checkStepOnEntity(entity)) {
            EntityImmortalGolem golem = EntityInit.IMMORTAL_GOLEM.get().create(level);
            if (level instanceof ServerLevel server && golem != null) {
                golem.setInitSpawn();
                golem.finalizeSpawn(server, level.getCurrentDifficultyAt(BlockPos.containing(pos.getX(), pos.getY(), pos.getZ())),
                        MobSpawnType.MOB_SUMMONED, null, null);
                golem.setDangerous(level.random.nextBoolean());
                golem.moveTo((double) pos.getX() + 0.5, pos.getY() + 1, (double) pos.getZ() + 0.5, 0.0F, 0.0F);
                server.addFreshEntity(golem);
            }
            level.playSound(null, pos, SoundInit.IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING.get(), SoundSource.BLOCKS);
            level.setBlock(pos, state.setValue(OPEN, true), 3);
        }
    }
}
