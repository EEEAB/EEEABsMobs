package com.eeeab.eeeabsmobs.client.particle.util;

import com.eeeab.eeeabsmobs.client.render.EERenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//参考自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/particle/util/AdvancedParticleBase.java
@OnlyIn(Dist.CLIENT)
public class AdvancedParticleBase extends TextureSheetParticle {
    public float airDiffusionSpeed;
    public float red, green, blue, alpha;
    public float prevRed, prevGreen, prevBlue, prevAlpha;
    public float scale, prevScale, particleScale;
    public ParticleRotation rotation;
    public boolean emissive;
    public double prevMotionX, prevMotionY, prevMotionZ;
    public SpriteSet spriteSet;

    public ParticleComponent[] components;
    //public ParticleRibbon ribbon;

    /**
     * AdvancedParticle
     *
     * @param worldIn           level
     * @param pX                初始坐标X
     * @param pY                初始坐标Y
     * @param pZ                初始坐标Z
     * @param motionX           移动X坐标
     * @param motionY           移动Y坐标
     * @param motionZ           移动Z坐标
     * @param rotation          粒子旋转方式
     * @param scale             粒子大小
     * @param r                 红
     * @param g                 绿
     * @param b                 蓝
     * @param a                 透明度
     * @param airDiffusionSpeed 传播速度
     * @param duration          存在时长
     * @param emissive          发光
     * @param canCollide        重力
     * @param components        粒子组件
     * @param spriteSet         null
     */
    protected AdvancedParticleBase(ClientLevel worldIn, double pX, double pY, double pZ, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components, SpriteSet spriteSet) {
        super(worldIn, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.red = (float) (r);
        this.green = (float) (g);
        this.blue = (float) (b);
        this.alpha = (float) (a);
        this.scale = (float) scale;
        this.lifetime = (int) duration;
        this.airDiffusionSpeed = (float) airDiffusionSpeed;
        this.rotation = rotation;
        this.components = components;
        this.emissive = emissive;
        //this.ribbon = null;

        for (ParticleComponent component : components) {
            component.init(this);
        }

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.prevRed = this.red;
        this.prevGreen = this.green;
        this.prevBlue = this.blue;
        this.prevAlpha = this.alpha;
        this.rotation.setPrevValues();
        this.prevScale = this.scale;
        this.hasPhysics = canCollide;
        this.spriteSet = spriteSet;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EERenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        if (emissive) {
            int k = i >> 16 & 255;
            return 240 | k << 16;
        } else return i;
    }

    @Override
    public void tick() {
        prevRed = red;
        prevGreen = green;
        prevBlue = blue;
        prevAlpha = alpha;
        prevScale = scale;
        rotation.setPrevValues();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        prevMotionX = xd;
        prevMotionY = yd;
        prevMotionZ = zd;

        for (ParticleComponent component : components) {
            component.preUpdate(this);
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            if (spriteSet != null) {
                this.setSpriteFromAge(spriteSet);
            }
        }

        updatePosition();

        for (ParticleComponent component : components) {
            component.postUpdate(this);
        }

//        if (ribbon != null) {
//            ribbon.setPos(x, y, z);
//            ribbon.positions[0] = new Vec3(x, y, z);
//            ribbon.prevPositions[0] = getPrevPos();
//        }
    }

    protected void updatePosition() {
        //this.motionY -= 0.04D * (double)this.particleGravity;
        this.move(this.xd, this.yd, this.zd);

        if (this.onGround && hasPhysics) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }

        this.xd *= airDiffusionSpeed;
        this.yd *= airDiffusionSpeed;
        this.zd *= airDiffusionSpeed;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        alpha = prevAlpha + (alpha - prevAlpha) * partialTicks;
        if (alpha < 0.01) alpha = 0.01f;
        rCol = prevRed + (red - prevRed) * partialTicks;
        gCol = prevGreen + (green - prevGreen) * partialTicks;
        bCol = prevBlue + (blue - prevBlue) * partialTicks;
        particleScale = prevScale + (scale - prevScale) * partialTicks;

        for (ParticleComponent component : components) {
            component.preRender(this, partialTicks);
        }

        Vec3 Vector3d = renderInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - Vector3d.x());
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - Vector3d.y());
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - Vector3d.z());


        Quaternion quaternion = new Quaternion(0, 0, 0, 1);
        if (rotation instanceof ParticleRotation.FaceCamera) {
            ParticleRotation.FaceCamera faceCameraRot = (ParticleRotation.FaceCamera) rotation;
            if (faceCameraRot.faceCameraAngle == 0.0F && faceCameraRot.prevFaceCameraAngle == 0.0F) {
                quaternion = renderInfo.rotation();
            } else {
                quaternion = new Quaternion(renderInfo.rotation());
                //quaternion.mul(Mth.lerp(partialTicks, faceCameraRot.prevFaceCameraAngle, faceCameraRot.faceCameraAngle));
                float f3 = Mth.lerp(partialTicks, faceCameraRot.prevFaceCameraAngle, faceCameraRot.faceCameraAngle);
                quaternion.mul(Vector3f.ZP.rotation(f3));
            }
        } else if (rotation instanceof ParticleRotation.EulerAngles) {
            ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) rotation;
            float rotX = eulerRot.prevPitch + (eulerRot.pitch - eulerRot.prevPitch) * partialTicks;
            float rotY = eulerRot.prevYaw + (eulerRot.yaw - eulerRot.prevYaw) * partialTicks;
            float rotZ = eulerRot.prevRoll + (eulerRot.roll - eulerRot.prevRoll) * partialTicks;
            //Quaternion quaternionf = new Quaternion(0,0,0,1);
            Quaternion quatX = new Quaternion(rotX, 0, 0, false);
            Quaternion quatY = new Quaternion(0, rotY, 0, false);
            Quaternion quatZ = new Quaternion(0, 0, rotZ, false);
            //Quaternionf quatX = new Quaternionf(quaternionf.rotateZYX(rotX, 0, 0));
            //Quaternionf quatY = new Quaternionf(quaternionf.rotateZYX(0, rotY, 0));
            //Quaternionf quatZ = new Quaternionf(quaternionf.rotateZYX(0, 0, rotZ));
            quaternion.mul(quatZ);
            quaternion.mul(quatY);
            quaternion.mul(quatX);
        } else if (rotation instanceof ParticleRotation.OrientVector) {
            ParticleRotation.OrientVector orientRot = (ParticleRotation.OrientVector) rotation;
            double x = orientRot.prevOrientation.x + (orientRot.orientation.x - orientRot.prevOrientation.x) * partialTicks;
            double y = orientRot.prevOrientation.y + (orientRot.orientation.y - orientRot.prevOrientation.y) * partialTicks;
            double z = orientRot.prevOrientation.z + (orientRot.orientation.z - orientRot.prevOrientation.z) * partialTicks;
            float pitch = (float) Math.asin(-y);
            float yaw = (float) (Mth.atan2(x, z));
            //Quaternionf quaternionf = new Quaternionf();
            Quaternion quatX = new Quaternion(pitch, 0, 0, false);
            Quaternion quatY = new Quaternion(0, yaw, 0, false);
            //Quaternionf quatX = new Quaternionf(quaternionf.rotateZYX(pitch, 0, 0));
            //Quaternionf quatY = new Quaternionf(quaternionf.rotateZYX(0, yaw, 0));
            quaternion.mul(quatY);
            quaternion.mul(quatX);
        }

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = particleScale * 0.1f;

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            //vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(partialTicks);
        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

        for (ParticleComponent component : components) {
            component.postRender(this, buffer, renderInfo, partialTicks, j);
        }
    }

    public float getAge() {
        return age;
    }

    public double getPosX() {
        return x;
    }

    public void setPosX(double posX) {
        setPos(posX, this.y, this.z);
    }

    public double getPosY() {
        return y;
    }

    public void setPosY(double posY) {
        setPos(this.x, posY, this.z);
    }

    public double getPosZ() {
        return z;
    }

    public void setPosZ(double posZ) {
        setPos(this.x, this.y, posZ);
    }

    public double getMotionX() {
        return xd;
    }

    public void setMotionX(double motionX) {
        this.xd = motionX;
    }

    public double getMotionY() {
        return yd;
    }

    public void setMotionY(double motionY) {
        this.yd = motionY;
    }

    public double getMotionZ() {
        return zd;
    }

    public void setMotionZ(double motionZ) {
        this.zd = motionZ;
    }

    public Vec3 getPrevPos() {
        return new Vec3(xo, yo, zo);
    }

    public double getPrevPosX() {
        return xo;
    }

    public double getPrevPosY() {
        return yo;
    }

    public double getPrevPosZ() {
        return zo;
    }

    public Level getWorld() {
        return level;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<AdvancedParticleData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(AdvancedParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AdvancedParticleBase particle;
            if (typeIn.isAnimation()) {
                particle = new AdvancedParticleBase(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDiffusionSpeed(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getCanCollide(), typeIn.getComponents(), spriteSet);
                particle.setSpriteFromAge(spriteSet);
            } else {
                particle = new AdvancedParticleBase(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDiffusionSpeed(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getCanCollide(), typeIn.getComponents(), null);
                particle.pickSprite(spriteSet);
            }
            particle.setColor((float) typeIn.getRed(), (float) typeIn.getGreen(), (float) typeIn.getBlue());
            return particle;
        }
    }

    public static void spawnParticle(Level level, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide, boolean isAnimation, ParticleComponent[] components) {
        ParticleRotation rotation = faceCamera ? new ParticleRotation.FaceCamera((float) faceCameraAngle) : new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
        level.addParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, airDiffusionSpeed, duration, emissive, canCollide, components, isAnimation), x, y, z, motionX, motionY, motionZ);
    }

    public static void spawnEmptyComponentParticle(Level level, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide, boolean isAnimation) {
        spawnParticle(level, particle, x, y, z, motionX, motionY, motionZ, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, r, g, b, a, airDiffusionSpeed, duration, emissive, canCollide, isAnimation, new ParticleComponent[]{});
    }

    public static void spawnCustomRotationParticle(Level level, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components, boolean isAnimation) {
        level.addParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, airDiffusionSpeed, duration, emissive, canCollide, components, isAnimation), x, y, z, motionX, motionY, motionZ);
    }
}
