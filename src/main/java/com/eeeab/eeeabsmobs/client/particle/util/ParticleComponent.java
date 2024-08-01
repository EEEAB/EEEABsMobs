package com.eeeab.eeeabsmobs.client.particle.util;

import com.eeeab.eeeabsmobs.client.particle.util.anim.AnimData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

//参考自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/particle/util/ParticleComponent.java
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

    /**
     * 通用粒子效果组件
     */
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
                    if (additive) eulerRot.yaw += value;
                    else eulerRot.yaw = value;
                }
            } else if (property == EnumParticleProperty.PITCH) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    if (additive) eulerRot.pitch += value;
                    else eulerRot.pitch = value;
                }
            } else if (property == EnumParticleProperty.ROLL) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    if (additive) eulerRot.roll += value;
                    else eulerRot.roll = value;
                }
            } else if (property == EnumParticleProperty.PARTICLE_ANGLE) {
                if (particle.rotation instanceof ParticleRotation.FaceCamera faceCameraRot) {
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
            newPos.rotate(quat);
            newPos.mul(r);

            if (location.length > 0 && location[0] != null) {
                newPos.add((float) location[0].x, (float) location[0].y, (float) location[0].z);
            }
            particle.setPos(newPos.x(), newPos.y(), newPos.z());
        }
    }

    /**
     * 面向相机粒子效果组件(不适用ParticleRotation.FaceCamera)
     */
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
                    double a = dy / d;
                    a = Math.max(-1, Math.min(1, a));
                    float pitch = -(float) Math.asin(a);
                    float yaw = -(float) (Math.atan2(dz, dx) + Math.PI);
                    eulerRot.roll = pitch;
                    eulerRot.yaw = yaw;
                } else if (particle.rotation instanceof ParticleRotation.OrientVector orientRot) {
                    orientRot.orientation = new Vec3(dx, dy, dz).normalize();
                }
            }
        }
    }


    /**
     * 旋转移动组件
     *
     * @author EEEAB
     */
    public static class RotatingMotion extends ParticleComponent {
        private final Vec3[] location;
        private final AnimData strength;
        private final float rotation;

        /**
         * RotatingMotion
         *
         * @param location 环绕中心坐标
         * @param strength 旋转强度
         * @param rotation 旋转弧度
         */
        public RotatingMotion(Vec3[] location, AnimData strength, float rotation) {
            this.location = location;
            this.strength = strength;
            this.rotation = rotation;
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / (particle.getLifetime() - 1);
            if (location.length > 0) {
                Vec3 destinationVec = location[0];
                Vec3 currPos = new Vec3(particle.getPosX(), particle.getPosY(), particle.getPosZ());
                Vec3 diff = destinationVec.subtract(currPos);
                //根据旋转角度计算切线方向的力 y轴作为旋转轴
                Vec3 tangentForce = diff.cross(new Vec3(0, 1, 0));
                tangentForce = tangentForce.normalize().scale(Mth.sin(rotation) * strength.evaluate(ageFrac));
                particle.setMotionX(tangentForce.x);
                particle.setMotionY(tangentForce.y);
                particle.setMotionZ(tangentForce.z);
            }
        }
    }


}
