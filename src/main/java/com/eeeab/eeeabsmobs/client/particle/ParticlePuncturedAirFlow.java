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
public class ParticlePuncturedAirFlow extends ParticleRing {
    private final SpriteSet sprite;

    public ParticlePuncturedAirFlow(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float yRot, float xRot, int duration, float red, float green, float blue, float opacity, float scale, boolean facesCamera, EnumRingBehavior behavior, SpriteSet sprite) {
        super(world, x, y, z, motionX, motionY, motionZ, yRot, xRot, duration, red, green, blue, opacity, scale, facesCamera, behavior);
        this.sprite = sprite;
        this.setSpriteFromAge(sprite);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.sprite);
        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public static final class PuncturedAirFlowFactory implements ParticleProvider<PuncturedAirFlowData> {
        private final SpriteSet spriteSet;

        public PuncturedAirFlowFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(PuncturedAirFlowData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticlePuncturedAirFlow particle = new ParticlePuncturedAirFlow(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getyRot(), typeIn.getxRot(), typeIn.getDuration(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getScale(), typeIn.getFacesCamera(), typeIn.getBehavior(), this.spriteSet);
            particle.setSpriteFromAge(this.spriteSet);
            return particle;
        }
    }

    public static class PuncturedAirFlowData extends ParticleRing.RingData {
        public static final Deserializer<PuncturedAirFlowData> DESERIALIZER = new Deserializer<PuncturedAirFlowData>() {
            public PuncturedAirFlowData fromCommand(ParticleType<PuncturedAirFlowData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                return new PuncturedAirFlowData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.GROW);
            }

            public PuncturedAirFlowData fromNetwork(ParticleType<PuncturedAirFlowData> particleTypeIn, FriendlyByteBuf buffer) {
                return new PuncturedAirFlowData(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), EnumRingBehavior.GROW);
            }
        };

        public PuncturedAirFlowData(float yRot, float pitch, int duration, float red, float g, float blue, float alpha, float scale, boolean facesCamera, EnumRingBehavior behavior) {
            super(yRot, pitch, duration, red, g, blue, alpha, scale, facesCamera, behavior);
        }

        @Override
        public ParticleType<PuncturedAirFlowData> getType() {
            return ParticleInit.PUNCTURED_AIR_FLOW.get();
        }

        public static Codec<PuncturedAirFlowData> PAFCODEC(ParticleType<PuncturedAirFlowData> particleType) {
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
                            new PuncturedAirFlowData(yRot, xRot, duration, red, green, blue, alpha, scale, facesCamera, EnumRingBehavior.valueOf(behavior)))
            );
        }
    }

}
