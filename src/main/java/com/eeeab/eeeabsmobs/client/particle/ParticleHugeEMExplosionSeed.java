package com.eeeab.eeeabsmobs.client.particle;

import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleHugeEMExplosionSeed extends NoRenderParticle {
    private int life;

    ParticleHugeEMExplosionSeed(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void tick() {
        int lifeTime = 8;
        for (int i = 0; i < 3; ++i) {
            double d0 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 2.0D;
            double d1 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 2.0D;
            double d2 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 2.0D;
            this.level.addParticle(ParticleInit.OVERLOAD_EXPLOSION.get(), d0, d1, d2, (float) this.life / (float) lifeTime, 0.0D, 0.0D);
        }
        ++this.life;
        if (this.life == lifeTime) {
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleHugeEMExplosionSeed(level, x, y, z);
        }
    }
}