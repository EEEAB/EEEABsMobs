package com.eeeab.eeeabsmobs.client.particle;

import com.eeeab.eeeabsmobs.client.render.EMRenderType;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class ParticleDust extends TextureSheetParticle {
    private final float scale;
    private final boolean emissive;
    private final EnumDustBehavior behavior;
    private final float airDiffusionSpeed;

    public enum EnumDustBehavior {
        SHRINK,
        GROW,
        CONSTANT
    }

    public ParticleDust(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, double scale, int duration, EnumDustBehavior behavior, double airDiffusionSpeed, boolean emissive) {
        super(world, x, y, z);
        this.scale = (float) scale * 0.5f * 0.1f;
        lifetime = duration;
        xd = vx * 0.5;
        yd = vy * 0.5;
        zd = vz * 0.5;
        this.behavior = behavior;
        roll = oRoll = (float) (random.nextInt(4) * Math.PI / 2);
        this.airDiffusionSpeed = (float) airDiffusionSpeed;
        this.emissive = emissive;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public void tick() {
        super.tick();
        xd *= airDiffusionSpeed;
        yd *= airDiffusionSpeed;
        zd *= airDiffusionSpeed;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return this.emissive ? 240 : super.getLightColor(partialTick);
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float var = (age + partialTicks) / (float) lifetime;
        alpha = 0.25f * ((float) (1 - Math.exp(5 * (var - 1)) - Math.pow(2000, -var)));
        if (alpha < 0.01) {
            alpha = 0.01f;
        }
        if (behavior == EnumDustBehavior.SHRINK) {
            this.quadSize = scale * ((1 - 0.7f * var) + 0.3f);
        } else if (behavior == EnumDustBehavior.GROW) {
            this.quadSize = scale * ((0.7f * var) + 0.3f);
        } else {
            this.quadSize = scale;
        }
        super.render(buffer, renderInfo, partialTicks);
    }


    @OnlyIn(Dist.CLIENT)
    public static final class DustFactory implements ParticleProvider<DustData> {
        private final SpriteSet spriteSet;

        public DustFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(DustData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleDust particleDust = new ParticleDust(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getScale(), typeIn.getDuration(), typeIn.getBehavior(), typeIn.getAirDiffusionSpeed(), typeIn.getEmissive());
            particleDust.setSpriteFromAge(spriteSet);
            particleDust.setColor(typeIn.getR(), typeIn.getG(), typeIn.getB());
            return particleDust;
        }
    }

    public static class DustData implements ParticleOptions {
        public static final Deserializer<DustData> DESERIALIZER = new Deserializer<DustData>() {
            public DustData fromCommand(ParticleType<DustData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                reader.expect(' ');
                float airDrag = (float) reader.readDouble();
                reader.expect(' ');
                boolean emissive = reader.readBoolean();
                return new DustData(particleTypeIn, r, g, b, scale, duration, EnumDustBehavior.CONSTANT, airDrag, emissive);
            }

            public DustData fromNetwork(ParticleType<DustData> particleTypeIn, FriendlyByteBuf buffer) {
                return new DustData(particleTypeIn, buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), EnumDustBehavior.CONSTANT, buffer.readFloat(), buffer.readBoolean());
            }
        };

        private final ParticleType<DustData> type;

        private final float r;
        private final float g;
        private final float b;
        private final float scale;
        private final int duration;
        private final EnumDustBehavior behavior;
        private final float airDiffusionSpeed;
        private final boolean emissive;

        public DustData(ParticleType<DustData> type, float r, float g, float b, float scale, int duration, EnumDustBehavior behavior, float airDiffusionSpeed, boolean emissive) {
            this.type = type;
            this.r = r;
            this.g = g;
            this.b = b;
            this.scale = scale;
            this.behavior = behavior;
            this.airDiffusionSpeed = airDiffusionSpeed;
            this.duration = duration;
            this.emissive = emissive;
        }


        public DustData(ParticleType<DustData> type, float r, float g, float b, float scale, int duration, EnumDustBehavior behavior, float airDiffusionSpeed) {
            this(type, r, g, b, scale, duration, behavior, airDiffusionSpeed, false);
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
            buffer.writeFloat(this.airDiffusionSpeed);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b, this.scale, this.duration, this.airDiffusionSpeed);
        }

        @Override
        public ParticleType<DustData> getType() {
            return type;
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
        public EnumDustBehavior getBehavior() {
            return this.behavior;
        }

        @OnlyIn(Dist.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @OnlyIn(Dist.CLIENT)
        public float getAirDiffusionSpeed() {
            return this.airDiffusionSpeed;
        }

        @OnlyIn(Dist.CLIENT)
        public boolean getEmissive() {
            return this.emissive;
        }

        public static Codec<DustData> CODEC(ParticleType<DustData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(DustData::getR),
                            Codec.FLOAT.fieldOf("g").forGetter(DustData::getG),
                            Codec.FLOAT.fieldOf("b").forGetter(DustData::getB),
                            Codec.FLOAT.fieldOf("scale").forGetter(DustData::getScale),
                            Codec.STRING.fieldOf("behavior").forGetter((dustData) -> dustData.getBehavior().toString()),
                            Codec.INT.fieldOf("duration").forGetter(DustData::getDuration),
                            Codec.FLOAT.fieldOf("airDiffusionSpeed").forGetter(DustData::getAirDiffusionSpeed),
                            Codec.BOOL.fieldOf("emissive").forGetter(DustData::getEmissive)
                    ).apply(codecBuilder, (r, g, b, scale, behavior, duration, airDiffusionSpeed, emissive) ->
                            new DustData(particleType, r, g, b, scale, duration, EnumDustBehavior.valueOf(behavior), airDiffusionSpeed, emissive))
            );
        }
    }
}
