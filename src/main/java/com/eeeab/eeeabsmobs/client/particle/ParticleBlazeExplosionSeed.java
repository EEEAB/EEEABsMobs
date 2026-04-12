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
public class ParticleBlazeExplosionSeed extends NoRenderParticle {
    private final double scale;
    private final double count;
    private int life;

    ParticleBlazeExplosionSeed(ClientLevel level, double x, double y, double z, double quadSizeMultiplier, double count) {
        super(level, x, y, z, 0, 0, 0);
        this.scale = quadSizeMultiplier;
        this.count = Math.max(count, 1);
    }

    @Override
    public void tick() {
        int lifeTime = 5;
        for (int i = 0; i < count; i++) {
            double d0 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * scale;
            double d1 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * scale;
            double d2 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * scale;
            this.level.addParticle(ParticleInit.BLAZE_EXPLOSION.get(), d0, d1, d2, (float) this.life / (float) lifeTime, 0.0D, 0.0D);
        }
        ++this.life;
        if (this.life == lifeTime) {
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleBlazeExplosionSeed(level, x, y, z, xSpeed, ySpeed);
        }
    }
}