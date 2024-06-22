package com.eeeab.eeeabsmobs.client.particle.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//参考自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/particle/util/RibbonParticleData.java
public class RibbonParticleData extends AdvancedParticleData {
    public static final Deserializer<RibbonParticleData> DESERIALIZER = new Deserializer<RibbonParticleData>() {
        public RibbonParticleData fromCommand(ParticleType<RibbonParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double airDrag = reader.readDouble();
            reader.expect(' ');
            double red = reader.readDouble();
            reader.expect(' ');
            double green = reader.readDouble();
            reader.expect(' ');
            double blue = reader.readDouble();
            reader.expect(' ');
            double alpha = reader.readDouble();
            reader.expect(' ');
            String rotationMode = reader.readString();
            reader.expect(' ');
            double scale = reader.readDouble();
            reader.expect(' ');
            double yaw = reader.readDouble();
            reader.expect(' ');
            double pitch = reader.readDouble();
            reader.expect(' ');
            double roll = reader.readDouble();
            reader.expect(' ');
            boolean emissive = reader.readBoolean();
            reader.expect(' ');
            double duration = reader.readDouble();
            reader.expect(' ');
            reader.readDouble();
            reader.expect(' ');
            int length = reader.readInt();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) 0);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3(yaw, pitch, roll));
            return new RibbonParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDrag, duration, emissive, length, false);
        }

        public RibbonParticleData fromNetwork(ParticleType<RibbonParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            double airDrag = buffer.readFloat();
            double red = buffer.readFloat();
            double green = buffer.readFloat();
            double blue = buffer.readFloat();
            double alpha = buffer.readFloat();
            String rotationMode = buffer.readUtf();
            double scale = buffer.readFloat();
            double yaw = buffer.readFloat();
            double pitch = buffer.readFloat();
            double roll = buffer.readFloat();
            boolean emissive = buffer.readBoolean();
            double duration = buffer.readFloat();
            buffer.readFloat();
            int length = buffer.readInt();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) 0);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3(yaw, pitch, roll));
            return new RibbonParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDrag, duration, emissive, length, false);
        }
    };

    private final int length;

    public RibbonParticleData(ParticleType<? extends RibbonParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length, boolean isAnimation) {
        this(type, rotation, scale, r, g, b, a, drag, duration, emissive, length, new ParticleComponent[]{}, isAnimation);
    }

    public RibbonParticleData(ParticleType<? extends RibbonParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length, ParticleComponent[] components, boolean isAnimation) {
        super(type, rotation, scale, r, g, b, a, drag, duration, emissive, false, components, isAnimation);
        this.length = length;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        super.writeToNetwork(buffer);
        buffer.writeInt(this.length);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String writeToString() {
        return super.writeToString() + " " + this.length;
    }

    @OnlyIn(Dist.CLIENT)
    public int getLength() {
        return this.length;
    }

    public static Codec<RibbonParticleData> CODEC_RIBBON(ParticleType<RibbonParticleData> particleType) {
        return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                        Codec.DOUBLE.fieldOf("scale").forGetter(RibbonParticleData::getScale),
                        Codec.DOUBLE.fieldOf("r").forGetter(RibbonParticleData::getRed),
                        Codec.DOUBLE.fieldOf("g").forGetter(RibbonParticleData::getGreen),
                        Codec.DOUBLE.fieldOf("b").forGetter(RibbonParticleData::getBlue),
                        Codec.DOUBLE.fieldOf("a").forGetter(RibbonParticleData::getAlpha),
                        Codec.DOUBLE.fieldOf("airDiffusionSpeed").forGetter(RibbonParticleData::getAirDiffusionSpeed),
                        Codec.DOUBLE.fieldOf("duration").forGetter(RibbonParticleData::getDuration),
                        Codec.BOOL.fieldOf("emissive").forGetter(RibbonParticleData::isEmissive),
                        Codec.INT.fieldOf("length").forGetter(RibbonParticleData::getLength),
                        Codec.BOOL.fieldOf("isAnimation").forGetter(AdvancedParticleData::isAnimation)
                ).apply(codecBuilder, (scale, r, g, b, a, drag, duration, emissive, length, isAnimation) ->
                        new RibbonParticleData(particleType, new ParticleRotation.FaceCamera(0), scale, r, g, b, a, drag, duration, emissive, length, new ParticleComponent[]{}, isAnimation))
        );
    }
}
