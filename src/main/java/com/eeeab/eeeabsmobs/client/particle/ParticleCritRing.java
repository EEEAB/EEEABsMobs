package com.eeeab.eeeabsmobs.client.particle;

import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleCritRing extends ParticleRing {

    public ParticleCritRing(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float yRot, float xRot, int duration, float red, float green, float blue, float opacity, float scale, boolean facesCamera, EnumRingBehavior behavior, SpriteSet sprite) {
        super(world, x, y, z, motionX, motionY, motionZ, yRot, xRot, duration, red, green, blue, opacity, scale, facesCamera, behavior);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class CritRingFactory implements ParticleProvider<CritRingData> {
        private final SpriteSet spriteSet;

        public CritRingFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(CritRingData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleCritRing particle = new ParticleCritRing(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getyRot(), typeIn.getxRot(), typeIn.getDuration(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getScale(), typeIn.getFacesCamera(), typeIn.getBehavior(), this.spriteSet);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    public static class CritRingData extends RingData {
        public static final Deserializer<CritRingData> DESERIALIZER = new Deserializer<CritRingData>() {
            public CritRingData fromCommand(ParticleType<CritRingData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float yaw = (float) reader.readDouble();
                reader.expect(' ');
                float pitch = (float) reader.readDouble();
                reader.expect(' ');
                float r = (float) reader.readDouble();
                reader.expect(' ');
                float g = (float) reader.readDouble();
                reader.expect(' ');
                float b = (float) reader.readDouble();
                reader.expect(' ');
                float a = (float) reader.readDouble();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                reader.expect(' ');
                boolean facesCamera = reader.readBoolean();
                return new CritRingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.GROW);
            }

            public CritRingData fromNetwork(ParticleType<CritRingData> particleTypeIn, FriendlyByteBuf buffer) {
                return new CritRingData(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), EnumRingBehavior.GROW);
            }
        };

        public CritRingData(float yRot, float pitch, int duration, float red, float g, float blue, float alpha, float scale, boolean facesCamera, EnumRingBehavior behavior) {
            super(yRot, pitch, duration, red, g, blue, alpha, scale, facesCamera, behavior);
        }

        @Override
        public ParticleType<CritRingData> getType() {
            return ParticleInit.CRIT_RING.get();
        }

        public static Codec<CritRingData> CRCODEC(ParticleType<CritRingData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("yRot").forGetter(RingData::getyRot),
                            Codec.FLOAT.fieldOf("xRot").forGetter(RingData::getxRot),
                            Codec.FLOAT.fieldOf("red").forGetter(RingData::getRed),
                            Codec.FLOAT.fieldOf("green").forGetter(RingData::getGreen),
                            Codec.FLOAT.fieldOf("blue").forGetter(RingData::getBlue),
                            Codec.FLOAT.fieldOf("alpha").forGetter(RingData::getAlpha),
                            Codec.FLOAT.fieldOf("scale").forGetter(RingData::getScale),
                            Codec.INT.fieldOf("duration").forGetter(RingData::getDuration),
                            Codec.BOOL.fieldOf("facesCamera").forGetter(RingData::getFacesCamera),
                            Codec.STRING.fieldOf("behavior").forGetter((ringData) -> ringData.getBehavior().toString())
                    ).apply(codecBuilder, (yRot, xRot, red, green, blue, alpha, scale, duration, facesCamera, behavior) ->
                            new CritRingData(yRot, xRot, duration, red, green, blue, alpha, scale, facesCamera, EnumRingBehavior.valueOf(behavior)))
            );
        }
    }

}
