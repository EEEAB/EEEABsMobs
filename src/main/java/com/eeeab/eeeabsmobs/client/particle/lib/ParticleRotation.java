package com.eeeab.eeeabsmobs.client.particle.lib;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

//参考自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/particle/util/ParticleRotation.java
public abstract class ParticleRotation {
    public void setPrevValues() {

    }

    public static class FaceCamera extends ParticleRotation {
        public float faceCameraAngle;
        public float prevFaceCameraAngle;

        public FaceCamera(float faceCameraAngle) {
            this.faceCameraAngle = faceCameraAngle;
        }

        @Override
        public void setPrevValues() {
            prevFaceCameraAngle = faceCameraAngle;
        }
    }

    public static class EulerAngles extends ParticleRotation {
        public float yaw, pitch, roll;
        public float prevYaw, prevPitch, prevRoll;

        public EulerAngles(float yaw, float pitch, float roll) {
            this.yaw = this.prevYaw = yaw;
            this.pitch = this.prevPitch = pitch;
            this.roll = this.prevRoll = roll;
        }

        @Override
        public void setPrevValues() {
            prevYaw = yaw;
            prevPitch = pitch;
            prevRoll = roll;
        }
    }

    public static class OrientVector extends ParticleRotation {
        public Vec3 orientation;
        public Vec3 prevOrientation;

        public OrientVector(Vec3 orientation) {
            this.orientation = this.prevOrientation = orientation;
        }

        @Override
        public void setPrevValues() {
            prevOrientation = orientation;
        }
    }

    public static class FaceCameraVertical extends ParticleRotation {
        public Quaternionf getRotation(Camera camera, Vec3 particlePos) {
            Vec3 cameraPos = camera.getPosition();
            Vec3 dir = particlePos.subtract(cameraPos).normalize();
            Vec3 dirHor = new Vec3(dir.x, 0, dir.z).normalize();
            return new Quaternionf().rotateY((float) Math.atan2(dirHor.x, dirHor.z) + (float) Math.PI);
        }
    }
}
