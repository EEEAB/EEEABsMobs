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
        this.lifetime = 20;
        this.setSpriteFromAge(sprite);
        this.roll = this.oRoll = (float) (random.nextInt(4) * Math.PI / 2);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprite);
            this.yd += 0.004D;
            this.move(this.xd, this.yd, this.zd);
            if (this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

            this.xd *= 0.96D;
            this.yd *= 0.96D;
            this.zd *= 0.96D;
            if (this.onGround) {
                this.xd *= 0.7D;
                this.zd *= 0.7D;
            }
        }
        this.age++;
    }

    @Override
    //粒子渲染效果 半透明
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float p_107249_) {
        return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class GuardianSparkFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public GuardianSparkFactory(SpriteSet sprite) {
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
