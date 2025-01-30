package com.eeeab.eeeabsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleImmortalExplosion extends TextureSheetParticle {
    private final float rotSpeed;
    private final SpriteSet sprites;
    private final boolean invert;

    public ParticleImmortalExplosion(ClientLevel level, double x, double y, double z, double quadSizeMultiplier, SpriteSet sprites) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.lifetime = 6 + this.random.nextInt(5);
        float f = this.random.nextFloat() * 0.2F;
        this.invert = this.random.nextBoolean();
        this.rCol = 0.6F + f;
        this.gCol = 0.8F + f;
        this.bCol = 1F;
        this.quadSize = 2F * (1.0F - (float) quadSizeMultiplier * 0.5F);
        this.rotSpeed = ((float) Math.random() - 0.5F) * 0.1F;
        this.roll = this.oRoll = (float) Math.toRadians(this.random.nextInt(360));
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
    }

    public int getLightColor(float partialTick) {
        return 15728880;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.oRoll = this.roll;
            float arc = (float) Math.PI * this.rotSpeed * 2.0F;
            if (this.invert) this.roll -= arc;
            else this.roll += arc;
            this.setSpriteFromAge(this.sprites);
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleImmortalExplosion(level, x, y, z, xSpeed, this.sprites);
        }
    }
}