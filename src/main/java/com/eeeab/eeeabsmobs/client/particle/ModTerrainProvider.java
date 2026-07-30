package com.eeeab.eeeabsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModTerrainProvider extends TerrainParticle.Provider {
    @Override
    public Particle createParticle(BlockParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        BlockState blockstate = type.getState();
        if (blockstate.isAir() || blockstate.is(Blocks.MOVING_PISTON)) return null;
        Particle particle = (new TerrainParticle(level, x, y, z, 0, 0, 0, blockstate)).updateSprite(blockstate, type.getPos());
        particle.setLifetime(particle.getLifetime() * 2);
        particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        return particle;
    }
}
