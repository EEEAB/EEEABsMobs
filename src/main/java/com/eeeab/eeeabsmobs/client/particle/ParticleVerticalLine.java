package com.eeeab.eeeabsmobs.client.particle;

import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleVerticalLine extends TextureSheetParticle {
    protected ParticleVerticalLine(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.hasPhysics = false;
        this.lifetime = 2 + this.random.nextInt(3);
        this.quadSize *= 1.5F;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age >= this.lifetime) {
            this.remove();
        }
        this.age++;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @OnlyIn(Dist.CLIENT)
    public static class VerticalLineFactory implements ParticleProvider<SimpleParticleType> {
        SpriteSet spriteSet;

        public VerticalLineFactory(SpriteSet sprites) {
            this.spriteSet = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleVerticalLine verticalLine = new ParticleVerticalLine(level, x, y, z);
            verticalLine.pickSprite(this.spriteSet);
            return verticalLine;
        }
    }

}
