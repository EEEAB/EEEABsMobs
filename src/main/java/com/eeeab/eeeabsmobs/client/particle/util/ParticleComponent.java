package com.eeeab.eeeabsmobs.client.particle.util;

import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

//Edited from https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/particle/util/ParticleComponent.java base code
public abstract class ParticleComponent {
    public ParticleComponent() {

    }

    public void init(AdvancedParticleBase particle) {

    }

    public void preUpdate(AdvancedParticleBase particle) {

    }

    public void postUpdate(AdvancedParticleBase particle) {

    }

    public void preRender(AdvancedParticleBase particle, float partialTicks) {

    }

    public void postRender(AdvancedParticleBase particle, VertexConsumer buffer, Camera renderInfo, float partialTicks, int lightmap) {

    }

    //public abstract static class AnimData {
    //    public float evaluate(float t) {
    //        return 0;
    //    }
    //}
    //
    //public static class KeyTrack extends AnimData {
    //    float[] values;
    //    float[] times;
    //
    //    public KeyTrack(float[] values, float[] times) {
    //        this.values = values;
    //        this.times = times;
    //        if (values.length != times.length)
    //            System.out.println("Malformed key track. Must have same number of keys and values or key track will evaluate to 0.");
    //    }
    //
    //    @Override
    //    public float evaluate(float t) {
    //        if (values.length != times.length) return 0;
    //        for (int i = 0; i < times.length; i++) {
    //            float time = times[i];
    //            if (t == time) return values[i];
    //            else if (t < time) {
    //                if (i == 0) return values[0];
    //                float a = (t - times[i - 1]) / (time - times[i - 1]);
    //                return values[i - 1] * (1 - a) + values[i] * a;
    //            } else {
    //                if (i == values.length - 1) return values[i];
    //            }
    //        }
    //        return 0;
    //    }
    //
    //    public static KeyTrack startAndEnd(float startValue, float endValue) {
    //        return new KeyTrack(new float[]{startValue, endValue}, new float[]{0, 1});
    //    }
    //
    //    public static KeyTrack oscillate(float value1, float value2, int frequency) {
    //        if (frequency <= 1) new KeyTrack(new float[]{value1, value2}, new float[]{0, 1});
    //        float step = 1.0f / frequency;
    //        float[] times = new float[frequency + 1];
    //        float[] values = new float[frequency + 1];
    //        for (int i = 0; i < frequency + 1; i++) {
    //            float value = i % 2 == 0 ? value1 : value2;
    //            times[i] = step * i;
    //            values[i] = value;
    //        }
    //        return new KeyTrack(values, times);
    //    }
    //}
    //
    //public static class Oscillator extends AnimData {
    //    float value1, value2;
    //    float frequency;
    //    float phaseShift;
    //
    //    public Oscillator(float value1, float value2, float frequency, float phaseShift) {
    //        this.value1 = value1;
    //        this.value2 = value2;
    //        this.frequency = frequency;
    //        this.phaseShift = phaseShift;
    //    }
    //
    //    @Override
    //    public float evaluate(float t) {
    //        float a = (value2 - value1) / 2f;
    //        return (float) (value1 + a + a * Math.cos(t * frequency + phaseShift));
    //    }
    //}
    //
    //public static class Constant extends AnimData {
    //    float value;
    //
    //    public Constant(float value) {
    //        this.value = value;
    //    }
    //
    //    @Override
    //    public float evaluate(float t) {
    //        return value;
    //    }
    //}

    //public static Constant constant(float value) {
    //    return new Constant(value);
    //}

    /** 通用粒子效果组件*/
    public static class PropertyControl extends ParticleComponent {
        public enum EnumParticleProperty {
            /* Moving particle */
            POS_X, POS_Y, POS_Z,
            MOTION_X, MOTION_Y, MOTION_Z,
            AIR_DIFFUSION_SPEED,
            /* Render particle */
            RED, GREEN, BLUE, ALPHA,
            SCALE,
            YAW, PITCH, ROLL, // For not facing camera
            PARTICLE_ANGLE // For facing camera
        }

        private final AnimData animData;
        private final EnumParticleProperty property;
        private final boolean additive;

        public PropertyControl(EnumParticleProperty property, AnimData animData, boolean additive) {
            this.property = property;
            this.animData = animData;
            this.additive = additive;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            float value = animData.evaluate(0);
            applyUpdate(particle, value);
            applyRender(particle, value);
        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            float ageFrac = (particle.getAge() + partialTicks) / particle.getLifetime();
            float value = animData.evaluate(ageFrac);
            applyRender(particle, value);
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getLifetime();
            float value = animData.evaluate(ageFrac);
            applyUpdate(particle, value);
        }

        private void applyUpdate(AdvancedParticleBase particle, float value) {
            if (property == EnumParticleProperty.POS_X) {
                if (additive) particle.setPosX(particle.getPosX() + value);
                else particle.setPosX(value);
            } else if (property == EnumParticleProperty.POS_Y) {
                if (additive) particle.setPosY(particle.getPosY() + value);
                else particle.setPosY(value);
            } else if (property == EnumParticleProperty.POS_Z) {
                if (additive) particle.setPosZ(particle.getPosZ() + value);
                else particle.setPosZ(value);
            } else if (property == EnumParticleProperty.MOTION_X) {
                if (additive) particle.setMotionX(particle.getMotionX() + value);
                else particle.setMotionX(value);
            } else if (property == EnumParticleProperty.MOTION_Y) {
                if (additive) particle.setMotionY(particle.getMotionY() + value);
                else particle.setMotionY(value);
            } else if (property == EnumParticleProperty.MOTION_Z) {
                if (additive) particle.setMotionZ(particle.getMotionZ() + value);
                else particle.setMotionZ(value);
            } else if (property == EnumParticleProperty.AIR_DIFFUSION_SPEED) {
                if (additive) particle.airDiffusionSpeed += value;
                else particle.airDiffusionSpeed = value;
            }
        }

        private void applyRender(AdvancedParticleBase particle, float value) {
            if (property == EnumParticleProperty.RED) {
                if (additive) particle.red += value;
                else particle.red = value;
            } else if (property == EnumParticleProperty.GREEN) {
                if (additive) particle.green += value;
                else particle.green = value;
            } else if (property == EnumParticleProperty.BLUE) {
                if (additive) particle.blue += value;
                else particle.blue = value;
            } else if (property == EnumParticleProperty.ALPHA) {
                if (additive) particle.alpha += value;
                else particle.alpha = value;
            } else if (property == EnumParticleProperty.SCALE) {
                if (additive) particle.scale += value;
                else particle.scale = value;
            } else if (property == EnumParticleProperty.YAW) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    //ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    if (additive) eulerRot.yaw += value;
                    else eulerRot.yaw = value;
                }
            } else if (property == EnumParticleProperty.PITCH) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    //ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    if (additive) eulerRot.pitch += value;
                    else eulerRot.pitch = value;
                }
            } else if (property == EnumParticleProperty.ROLL) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    //ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    if (additive) eulerRot.roll += value;
                    else eulerRot.roll = value;
                }
            } else if (property == EnumParticleProperty.PARTICLE_ANGLE) {
                if (particle.rotation instanceof ParticleRotation.FaceCamera faceCameraRot) {
                    //ParticleRotation.FaceCamera faceCameraRot = (ParticleRotation.FaceCamera) particle.rotation;
                    if (additive) faceCameraRot.faceCameraAngle += value;
                    else faceCameraRot.faceCameraAngle = value;
                }
            }
        }
    }

    /**
     * 静止粒子效果组件
     */
    public static class PinLocation extends ParticleComponent {
        private final Vec3[] location;

        /**
         * PinLocation
         *
         * @param location 静止坐标
         */
        public PinLocation(Vec3[] location) {
            this.location = location;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            if (location != null && location.length > 0) {
                particle.setPos(location[0].x, location[0].y, location[0].z);
            }
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            if (location != null && location.length > 0) {
                particle.setPos(location[0].x, location[0].y, location[0].z);
            }
        }
    }

    /**
     * 吸引粒子效果组件
     */
    public static class Attractor extends ParticleComponent {
        public enum EnumAttractorBehavior {
            LINEAR,
            EXPONENTIAL,
            SIMULATED,
        }

        private final Vec3[] location;
        private final float strength;
        private final float killDist;
        private final EnumAttractorBehavior behavior;
        private Vec3 startLocation;

        /**
         * Attractor
         *
         * @param location 目标坐标数组
         * @param strength 吸引强度
         * @param killDist 到指定距离消散
         * @param behavior 吸引类型
         */
        public Attractor(Vec3[] location, float strength, float killDist, EnumAttractorBehavior behavior) {
            this.location = location;
            this.strength = strength;
            this.killDist = killDist;
            this.behavior = behavior;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            startLocation = new Vec3(particle.getPosX(), particle.getPosY(), particle.getPosZ());
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / (particle.getLifetime() - 1);
            if (location.length > 0) {
                Vec3 destinationVec = location[0];
                Vec3 currPos = new Vec3(particle.getPosX(), particle.getPosY(), particle.getPosZ());
                Vec3 diff = destinationVec.subtract(currPos);
                if (diff.length() < killDist) particle.remove();
                if (behavior == EnumAttractorBehavior.EXPONENTIAL) {
                    Vec3 path = destinationVec.subtract(startLocation).scale(Math.pow(ageFrac, strength)).add(startLocation).subtract(currPos);
                    particle.move(path.x, path.y, path.z);
                } else if (behavior == EnumAttractorBehavior.LINEAR) {
                    Vec3 path = destinationVec.subtract(startLocation).scale(ageFrac).add(startLocation).subtract(currPos);
                    particle.move(path.x, path.y, path.z);
                } else {
                    double dist = Math.max(diff.length(), 0.001);
                    diff = diff.normalize().scale(strength / (dist * dist));
                    particle.setMotionX(Math.min(particle.getMotionX() + diff.x, 5));
                    particle.setMotionY(Math.min(particle.getMotionY() + diff.y, 5));
                    particle.setMotionZ(Math.min(particle.getMotionZ() + diff.z, 5));
                }
            }
        }
    }

    /**
     * 特定轨迹粒子效果组件
     */
    public static class Orbit extends ParticleComponent {
        private final Vec3[] location;
        private final AnimData phase;
        private final AnimData radius;
        private final AnimData axisX;
        private final AnimData axisY;
        private final AnimData axisZ;
        private final boolean faceCamera;

        /**
         * Orbit
         *
         * @param location   目标坐标
         * @param phase      AnimData
         * @param radius     半径
         * @param axisX      轨迹X(faceCamera为false时生效)
         * @param axisY      轨迹Y(faceCamera为false时生效)
         * @param axisZ      轨迹Z(faceCamera为false时生效)
         * @param faceCamera 面向相机
         */
        public Orbit(Vec3[] location, AnimData phase, AnimData radius, AnimData axisX, AnimData axisY, AnimData axisZ, boolean faceCamera) {
            this.location = location;
            this.phase = phase;
            this.radius = radius;
            this.axisX = axisX;
            this.axisY = axisY;
            this.axisZ = axisZ;
            this.faceCamera = faceCamera;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            apply(particle, 0);
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getLifetime();
            apply(particle, ageFrac);
        }

        private void apply(AdvancedParticleBase particle, float t) {
            float p = phase.evaluate(t);
            float r = radius.evaluate(t);
            Vector3f axis;
            if (faceCamera && Minecraft.getInstance().player != null) {
                axis = new Vector3f(Minecraft.getInstance().player.getLookAngle().toVector3f());
            } else {
                axis = new Vector3f(axisX.evaluate(t), axisY.evaluate(t), axisZ.evaluate(t));
            }
            axis.normalize();

            Quaternionf quat = new Quaternionf(axis.x, axis.y, axis.z, p * (float) Math.PI * 2);
            Vector3f up = new Vector3f(0, 1, 0);
            Vector3f start = axis;
            if (Math.abs(axis.dot(up)) > 0.99) {
                start = new Vector3f(1, 0, 0);
            }
            start.cross(up);
            start.normalize();
            Vector3f newPos = start;
            //newPos.transform(quat);
            newPos.rotate(quat);
            newPos.mul(r);

            if (location.length > 0 && location[0] != null) {
                newPos.add((float) location[0].x, (float) location[0].y, (float) location[0].z);
            }
            particle.setPos(newPos.x(), newPos.y(), newPos.z());
        }
    }

    /**面向相机粒子效果组件(不适用ParticleRotation.FaceCamera)*/
    public static class FaceMotion extends ParticleComponent {
        public FaceMotion() {

        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            double dx = particle.getPosX() - particle.getPrevPosX();
            double dy = particle.getPosY() - particle.getPrevPosY();
            double dz = particle.getPosZ() - particle.getPrevPosZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (d != 0) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    //ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    double a = dy / d;
                    a = Math.max(-1, Math.min(1, a));
                    float pitch = -(float) Math.asin(a);
                    float yaw = -(float) (Math.atan2(dz, dx) + Math.PI);
                    eulerRot.roll = pitch;
                    eulerRot.yaw = yaw;
//                particle.roll = (float) Math.PI / 2;
                } else if (particle.rotation instanceof ParticleRotation.OrientVector orientRot) {
                    //ParticleRotation.OrientVector orientRot = (ParticleRotation.OrientVector) particle.rotation;
                    orientRot.orientation = new Vec3(dx, dy, dz).normalize();
                }
            }
        }
    }


}
