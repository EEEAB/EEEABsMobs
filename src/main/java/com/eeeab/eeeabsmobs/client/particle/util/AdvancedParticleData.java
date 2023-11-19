package com.eeeab.eeeabsmobs.client.particle.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

//Edited from https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/particle/util/AdvancedParticleData.java base code
public class AdvancedParticleData implements ParticleOptions {
    public static final Deserializer<AdvancedParticleData> DESERIALIZER = new Deserializer<AdvancedParticleData>() {
        public AdvancedParticleData fromCommand(ParticleType<AdvancedParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double airDiffusionSpeed = reader.readDouble();
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
            double faceCameraAngle = reader.readDouble();
            reader.expect(' ');
            boolean canCollide = reader.readBoolean();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) faceCameraAngle);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3(yaw, pitch, roll));
            return new AdvancedParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDiffusionSpeed, duration, emissive, canCollide);
        }

        public AdvancedParticleData fromNetwork(ParticleType<AdvancedParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            double airDiffusionSpeed = buffer.readFloat();
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
            double faceCameraAngle = buffer.readFloat();
            boolean canCollide = buffer.readBoolean();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) faceCameraAngle);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3(yaw, pitch, roll));
            return new AdvancedParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDiffusionSpeed, duration, emissive, canCollide);
        }
    };

    private final ParticleType<? extends AdvancedParticleData> type;
    private final float airDiffusionSpeed;
    private final float red, green, blue, alpha;
    private final ParticleRotation rotation;
    private final float scale;
    private final boolean emissive;
    private final float duration;
    private final boolean canCollide;
    private final boolean isAnimation;

    private final ParticleComponent[] components;

    public AdvancedParticleData(ParticleType<? extends AdvancedParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide) {
        this(type, rotation, scale, r, g, b, a, airDiffusionSpeed, duration, emissive, canCollide, new ParticleComponent[]{}, false);
    }

    public AdvancedParticleData(ParticleType<? extends AdvancedParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components, boolean isAnimation) {
        this.type = type;
        this.rotation = rotation;
        this.scale = (float) scale;
        this.red = (float) r;
        this.green = (float) g;
        this.blue = (float) b;
        this.alpha = (float) a;
        this.emissive = emissive;
        this.airDiffusionSpeed = (float) airDiffusionSpeed;
        this.duration = (float) duration;
        this.canCollide = canCollide;
        this.components = components;
        this.isAnimation = isAnimation;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        String rotationMode;
        float faceCameraAngle = 0;
        float yaw = 0;
        float pitch = 0;
        float roll = 0;
        if (rotation instanceof ParticleRotation.FaceCamera) {
            rotationMode = "face_camera";
            faceCameraAngle = ((ParticleRotation.FaceCamera) rotation).faceCameraAngle;
        } else if (rotation instanceof ParticleRotation.EulerAngles) {
            rotationMode = "euler";
            yaw = ((ParticleRotation.EulerAngles) rotation).yaw;
            pitch = ((ParticleRotation.EulerAngles) rotation).pitch;
            roll = ((ParticleRotation.EulerAngles) rotation).roll;
        } else {
            rotationMode = "orient";
            Vec3 vec = ((ParticleRotation.OrientVector) rotation).orientation;
            yaw = (float) vec.x;
            pitch = (float) vec.y;
            roll = (float) vec.z;
        }

        buffer.writeFloat(this.airDiffusionSpeed);
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
        buffer.writeFloat(this.alpha);
        buffer.writeUtf(rotationMode);
        buffer.writeFloat(this.scale);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
        buffer.writeFloat(roll);
        buffer.writeBoolean(this.emissive);
        buffer.writeFloat(this.duration);
        buffer.writeFloat(faceCameraAngle);
        buffer.writeBoolean(canCollide);
        buffer.writeBoolean(isAnimation);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String writeToString() {
        String rotationMode;
        float faceCameraAngle = 0;
        float yaw = 0;
        float pitch = 0;
        float roll = 0;
        if (rotation instanceof ParticleRotation.FaceCamera) {
            rotationMode = "face_camera";
            faceCameraAngle = ((ParticleRotation.FaceCamera) rotation).faceCameraAngle;
        } else if (rotation instanceof ParticleRotation.EulerAngles) {
            rotationMode = "euler";
            yaw = ((ParticleRotation.EulerAngles) rotation).yaw;
            pitch = ((ParticleRotation.EulerAngles) rotation).pitch;
            roll = ((ParticleRotation.EulerAngles) rotation).roll;
        } else {
            rotationMode = "orient";
            Vec3 vec = ((ParticleRotation.OrientVector) rotation).orientation;
            yaw = (float) vec.x;
            pitch = (float) vec.y;
            roll = (float) vec.z;
        }

        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %s %.2f %.2f %.2f %.2f %b %.2f %.2f %b %b", ForgeRegistries.PARTICLE_TYPES.getKey(getType()),
                this.airDiffusionSpeed, this.red, this.green, this.blue, this.alpha, rotationMode, this.scale, yaw, pitch, roll, this.emissive, this.duration, faceCameraAngle, canCollide, isAnimation);
    }

    @Override
    public ParticleType<? extends AdvancedParticleData> getType() {
        return type;
    }

    @OnlyIn(Dist.CLIENT)
    public double getRed() {
        return this.red;
    }

    @OnlyIn(Dist.CLIENT)
    public double getGreen() {
        return this.green;
    }

    @OnlyIn(Dist.CLIENT)
    public double getBlue() {
        return this.blue;
    }

    @OnlyIn(Dist.CLIENT)
    public double getAlpha() {
        return this.alpha;
    }

    @OnlyIn(Dist.CLIENT)
    public double getAirDiffusionSpeed() {
        return this.airDiffusionSpeed;
    }

    @OnlyIn(Dist.CLIENT)
    public ParticleRotation getRotation() {
        return this.rotation;
    }

    @OnlyIn(Dist.CLIENT)
    public double getScale() {
        return this.scale;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmissive() {
        return this.emissive;
    }

    @OnlyIn(Dist.CLIENT)
    public double getDuration() {
        return this.duration;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getCanCollide() {
        return canCollide;
    }

    @OnlyIn(Dist.CLIENT)
    public ParticleComponent[] getComponents() {
        return this.components;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isAnimation() {
        return this.isAnimation;
    }

    public static Codec<AdvancedParticleData> CODEC(ParticleType<AdvancedParticleData> particleType) {
        return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                Codec.DOUBLE.fieldOf("scale").forGetter(AdvancedParticleData::getScale),
                Codec.DOUBLE.fieldOf("r").forGetter(AdvancedParticleData::getRed),
                Codec.DOUBLE.fieldOf("g").forGetter(AdvancedParticleData::getGreen),
                Codec.DOUBLE.fieldOf("b").forGetter(AdvancedParticleData::getBlue),
                Codec.DOUBLE.fieldOf("a").forGetter(AdvancedParticleData::getAlpha),
                Codec.DOUBLE.fieldOf("airDiffusionSpeed").forGetter(AdvancedParticleData::getAirDiffusionSpeed),
                Codec.DOUBLE.fieldOf("duration").forGetter(AdvancedParticleData::getDuration),
                Codec.BOOL.fieldOf("emissive").forGetter(AdvancedParticleData::isEmissive),
                Codec.BOOL.fieldOf("canCollide").forGetter(AdvancedParticleData::getCanCollide),
                Codec.BOOL.fieldOf("isAnimation").forGetter(AdvancedParticleData::isAnimation)
                ).apply(codecBuilder, (scale, r, g, b, a, airDiffusionSpeed, duration, emissive, canCollide, isAnimation) ->
                        new AdvancedParticleData(particleType, new ParticleRotation.FaceCamera(0), scale, r, g, b, a, airDiffusionSpeed, duration, emissive, canCollide, new ParticleComponent[]{}, isAnimation))
        );
    }
}
