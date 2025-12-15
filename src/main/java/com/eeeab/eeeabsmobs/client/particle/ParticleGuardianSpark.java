package com.eeeab.eeeabsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ParticleGuardianSpark extends TextureSheetParticle {
    private final SpriteSet sprite;

    protected ParticleGuardianSpark(SpriteSet sprite, ClientLevel level, double x, double y, double z, Vec3 speed) {
        super(level, x, y, z, speed.x, speed.y, speed.z);
        this.sprite = sprite;
        this.hasPhysics = false;
        this.xd = speed.x;
        this.yd = speed.y;
        this.zd = speed.z;
        this.quadSize *= 2.0F;
        this.lifetime = (int)(8.0 / (Math.random() * 0.5 + 0.95));
        this.setSpriteFromAge(sprite);
        this.roll = this.oRoll = (float) (random.nextInt(4) * Math.PI / 2);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprite);
        }
        this.age++;
    }

    @Override
    //粒子渲染效果 半透明
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ParticleGuardianSpark(this.sprite, level, x, y, z, new Vec3(dx, dy, dz));
        }
    }
}
