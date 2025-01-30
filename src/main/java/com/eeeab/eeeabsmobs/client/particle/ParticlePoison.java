package com.eeeab.eeeabsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticlePoison extends PortalParticle {
    ParticlePoison(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.quadSize *= 1.5F;
        this.lifetime = (int) (Math.random() * 2.0D) + 60;
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = f * 0.6F;
        this.gCol = f;
        this.bCol = f * 0.3F;
    }

    public float getQuadSize(float pScaleFactor) {
        float f = 1.0F - ((float) this.age + pScaleFactor) / ((float) this.lifetime * 1.5F);
        return this.quadSize * f;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float) this.age / (float) this.lifetime;
            this.x += this.xd * (double) f;
            this.y += this.yd * (double) f;
            this.z += this.zd * (double) f;
            this.setPos(this.x, this.y, this.z);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Factory(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ParticlePoison particlePoison = new ParticlePoison(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            particlePoison.pickSprite(this.sprite);
            return particlePoison;
        }
    }
}
