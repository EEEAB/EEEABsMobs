package com.eeeab.eeeabsmobs.client.particle.base;

import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class ParticleOrb extends TextureSheetParticle {
    private final double duration;

    public ParticleOrb(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, double scale, int duration) {
        super(world, x, y, z);
        quadSize = (float) scale * 0.1f;
        lifetime = duration;
        xd = vx;
        yd = vy;
        zd = vz;
        this.duration = duration;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public int getLightColor(float delta) {
        return 240 | super.getLightColor(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        alpha = 0.1f;
        xo = x;
        yo = y;
        zo = z;
        super.tick();
        if (age >= lifetime) {
            remove();
        }
        age++;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        alpha = Math.max(1 - ((float) age + partialTicks) / (float) duration, 0.001f);
        super.render(buffer, renderInfo, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Factory implements ParticleProvider<OrbData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(OrbData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleOrb particle = new ParticleOrb(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getScale(), typeIn.getDuration());
            particle.setSpriteFromAge(spriteSet);
            particle.setColor(typeIn.getR(), typeIn.getG(), typeIn.getB());
            return particle;
        }
    }

    public static class OrbData implements ParticleOptions {
        public static final Deserializer<OrbData> DESERIALIZER = new Deserializer<OrbData>() {
            public OrbData fromCommand(ParticleType<OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float r = (float) reader.readDouble();
                reader.expect(' ');
                float g = (float) reader.readDouble();
                reader.expect(' ');
                float b = (float) reader.readDouble();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                return new OrbData(r, g, b, scale, duration);
            }

            public OrbData fromNetwork(ParticleType<OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt());
            }
        };
        private final float r;
        private final float g;
        private final float b;
        private final float scale;
        private final int duration;

        public OrbData(float r, float g, float b, float scale, int duration) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.scale = scale;
            this.duration = duration;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                    this.r, this.g, this.b, this.scale, this.duration);
        }

        @Override
        public ParticleType<OrbData> getType() {
            return ParticleInit.ORB.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getR() {
            return this.r;
        }

        @OnlyIn(Dist.CLIENT)
        public float getG() {
            return this.g;
        }

        @OnlyIn(Dist.CLIENT)
        public float getB() {
            return this.b;
        }

        @OnlyIn(Dist.CLIENT)
        public float getScale() {
            return this.scale;
        }

        @OnlyIn(Dist.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        public static Codec<OrbData> CODEC(ParticleType<OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(OrbData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(OrbData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(OrbData::getB),
                    Codec.FLOAT.fieldOf("scale").forGetter(OrbData::getScale),
                    Codec.INT.fieldOf("duration").forGetter(OrbData::getDuration)
                    ).apply(codecBuilder, OrbData::new)
            );
        }
    }
}
