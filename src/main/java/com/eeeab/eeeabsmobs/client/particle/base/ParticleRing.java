package com.eeeab.eeeabsmobs.client.particle.base;

import com.eeeab.eeeabsmobs.client.render.EERenderType;
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
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class ParticleRing extends TextureSheetParticle {
    public float red, green, blue, alpha;
    public boolean facesCamera;
    public float yRot, xRot;
    public float scale;

    private final EnumRingBehavior behavior;

    public enum EnumRingBehavior {
        SHRINK,
        GROW,
        CONSTANT,
        GROW_THEN_SHRINK
    }

    public ParticleRing(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float yRot, float xRot, int duration, float red, float green, float blue, float opacity, float scale, boolean facesCamera, EnumRingBehavior behavior) {
        super(world, x, y, z);
        super.alpha = 1;
        setSize(1, 1);
        this.scale = scale * 0.1f;
        lifetime = duration;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = opacity;
        this.yRot = yRot;
        this.xRot = xRot;
        this.facesCamera = facesCamera;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.behavior = behavior;
    }

    @Override
    public int getLightColor(float delta) {
        return 240 | super.getLightColor(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        super.tick();
        if (age >= lifetime) {
            remove();
        }
        age++;
    }

    @Override
    //Edited from net.minecraft.client.particle.SingleQuadParticle.render base code
    public void render(VertexConsumer vertexConsumer, Camera renderInfo, float partialTicks) {
        float var = (age + partialTicks) / lifetime;
        if (behavior == EnumRingBehavior.GROW) {
            quadSize = scale * var;
        } else if (behavior == EnumRingBehavior.SHRINK) {
            quadSize = scale * (1 - var);
        } else if (behavior == EnumRingBehavior.GROW_THEN_SHRINK) {
            quadSize = (float) (scale * (1 - var - Math.pow(2000, -var)));
        } else {
            quadSize = scale;
        }
        this.alpha = this.alpha * 0.95f * (1 - (age + partialTicks) / lifetime) + 0.05f;
        rCol = red;
        gCol = green;
        bCol = blue;

        Vec3 vec3 = renderInfo.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.zo, this.z) - vec3.z());
        Quaternionf quaternion = new Quaternionf();
        //ParticleRotation.FaceCamera faceCameraRot = (ParticleRotation.FaceCamera) rotation;
        if (facesCamera) {
            if (this.roll == 0.0F) {
                quaternion = renderInfo.rotation();
            } else {
                quaternion = new Quaternionf(renderInfo.rotation());
                quaternion.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
                //quaternion.mul(Axis.ZP.rotation(f3));
            }
        } else {
            Quaternionf quaternionf = new Quaternionf();
            Quaternionf quatX = new Quaternionf(quaternionf.rotateZYX(xRot, 0, 0));
            Quaternionf quatY = new Quaternionf(quaternionf.rotateZYX(0, yRot, 0));
            quaternion.mul(quatY);
            quaternion.mul(quatX);
        }

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(partialTicks);
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }
        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(partialTicks);
        vertexConsumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EERenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class RingFactory implements ParticleProvider<RingData> {
        private final SpriteSet spriteSet;

        public RingFactory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(RingData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleRing particle = new ParticleRing(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getyRot(), typeIn.getxRot(), typeIn.getDuration(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getScale(), typeIn.getFacesCamera(), typeIn.getBehavior());
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }

    public static class RingData implements ParticleOptions {
        public static final Deserializer<RingData> DESERIALIZER = new Deserializer<RingData>() {
            public RingData fromCommand(ParticleType<RingData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                return new RingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.GROW);
            }

            public RingData fromNetwork(ParticleType<RingData> particleTypeIn, FriendlyByteBuf buffer) {
                return new RingData(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), EnumRingBehavior.GROW);
            }
        };

        private final float yRot;
        private final float xRot;
        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;
        private final float scale;
        private final int duration;
        private final boolean facesCamera;
        private final EnumRingBehavior behavior;

        public RingData(float yRot, float pitch, int duration, float red, float g, float blue, float alpha, float scale, boolean facesCamera, EnumRingBehavior behavior) {
            this.yRot = yRot;
            this.xRot = pitch;
            this.red = red;
            this.green = g;
            this.blue = blue;
            this.alpha = alpha;
            this.scale = scale;
            this.duration = duration;
            this.facesCamera = facesCamera;
            this.behavior = behavior;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %d %b", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                    this.yRot, this.xRot, this.red, this.green, this.blue, this.scale, this.alpha, this.duration, this.facesCamera);
        }

        @Override
        public ParticleType<RingData> getType() {
            return ParticleInit.RING.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getyRot() {
            return this.yRot;
        }

        @OnlyIn(Dist.CLIENT)
        public float getxRot() {
            return this.xRot;
        }

        @OnlyIn(Dist.CLIENT)
        public float getRed() {
            return this.red;
        }

        @OnlyIn(Dist.CLIENT)
        public float getGreen() {
            return this.green;
        }

        @OnlyIn(Dist.CLIENT)
        public float getBlue() {
            return this.blue;
        }

        @OnlyIn(Dist.CLIENT)
        public float getAlpha() {
            return this.alpha;
        }

        @OnlyIn(Dist.CLIENT)
        public float getScale() {
            return this.scale;
        }

        @OnlyIn(Dist.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @OnlyIn(Dist.CLIENT)
        public boolean getFacesCamera() {
            return this.facesCamera;
        }

        @OnlyIn(Dist.CLIENT)
        public EnumRingBehavior getBehavior() {
            return this.behavior;
        }

        public static Codec<RingData> CODEC(ParticleType<RingData> particleType) {
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
                            new RingData(yRot, xRot, duration, red, green, blue, alpha, scale, facesCamera, EnumRingBehavior.valueOf(behavior)))
            );
        }
    }
}
