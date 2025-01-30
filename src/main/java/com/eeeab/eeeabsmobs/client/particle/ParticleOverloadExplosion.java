package com.eeeab.eeeabsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleOverloadExplosion extends ParticleImmortalExplosion {
    public ParticleOverloadExplosion(ClientLevel level, double x, double y, double z, double quadSizeMultiplier, SpriteSet sprites) {
        super(level, x, y, z, quadSizeMultiplier, sprites);
        this.lifetime = 4 + this.random.nextInt(4);
        float f = this.random.nextFloat() * 0.2F;
        this.rCol = 0.6F + f;
        this.gCol = 0.2F + f;
        this.bCol = 0.05F + f * 0.5F;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class EMOverloadExplosionFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public EMOverloadExplosionFactory(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleOverloadExplosion(level, x, y, z, xSpeed, this.sprites);
        }
    }
}