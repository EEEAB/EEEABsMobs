package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityImmortalShuriken;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class ImmortalMagic {

    public static void spawnImmortalLaser(LivingEntity caster, int count, float totalOffset, int duration, Vec3 spawnPos) {
        if (count <= 0) count = 1;
        Vec3 looking = caster.getLookAngle();
        float angleStep = totalOffset / (count + 1);
        for (int i = 0; i < count; i++) {
            float angle = -totalOffset / 2.0F + (i + 1) * angleStep;
            Vec3 vec3 = looking.yRot(angle);
            float f0 = (float) Mth.atan2(vec3.z, vec3.x);
            EntityImmortalLaser laser = new EntityImmortalLaser(caster.level(), caster, spawnPos.x, spawnPos.y, spawnPos.z, f0, duration);
            if (i == 0) EEEABMobs.PROXY.playImmortalLaserSound(laser);
            caster.level().addFreshEntity(laser);
        }
    }

    public static void spawnShurikenWithTargets(LivingEntity caster, int count, List<LivingEntity> entities, double radius, double angle, boolean flat) {
        if (count <= 0) count = 1;
        float angleRange = (float) Math.toRadians(angle);
        LivingEntity[] targets = new LivingEntity[count];
        assignTargets(count, entities, targets);
        for (int i = 0; i < count; ++i) {
            float divideAngle = (i + 0.5F) * angleRange / count - angleRange / 2;
            float yaw = caster.getYRot();
            float pitch = caster.getXRot();
            double x = caster.getX();
            double y = caster.getY(0.5);
            double z = caster.getZ();
            int duration = (15 + i) + i * 15;
            if (count > 6) {
                duration = (10 + i) + i * 10;
            }
            if (flat) {
                double motionX = Math.cos(divideAngle) * radius;
                double motionY = Math.cos(Math.toRadians(pitch)) * radius;
                double motionZ = Math.sin(divideAngle) * radius;
                spawnShuriken(caster, targets[i], x, y, z, motionX, motionY, motionZ, 0.4F, duration);
            } else {
                double cosYaw = Math.cos(Math.toRadians(yaw));
                double sinYaw = Math.sin(Math.toRadians(yaw));
                double xInPlane = Math.sin(divideAngle) * radius;
                double zInPlane = Math.sin(Math.toRadians(pitch)) * radius;
                double motionX = xInPlane * cosYaw - zInPlane * sinYaw;
                double motionY = Math.cos(divideAngle) * radius;
                double motionZ = xInPlane * sinYaw + zInPlane * cosYaw;
                spawnShuriken(caster, targets[i], x + motionX, y + motionY, z + motionZ, motionX, motionY, motionZ, 0.2F, duration);
            }
        }
    }

    public static void spawnShuriken(LivingEntity caster, LivingEntity target, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float velocity, int duration) {
        EntityImmortalShuriken shuriken = new EntityImmortalShuriken(caster.level(), caster, target, duration);
        shuriken.setPos(posX, posY, posZ);
        shuriken.shoot(motionX, motionY, motionZ, velocity, 3F);
        caster.level().addFreshEntity(shuriken);
    }

    public static void assignTargets(int count, List<LivingEntity> entities, LivingEntity[] targets) {
        if (entities.isEmpty()) return;
        int size = entities.size();
        int distribution = count / size;
        int remainder = count % size;
        int j = 0;
        for (int i = 0; i < size && j < count; i++) {
            int start = i * distribution + Math.min(i, remainder);
            int end = (i + 1) * distribution + Math.min(i + 1, remainder);
            for (int k = start; k < end && j < count; k++, j++) {
                targets[j] = entities.get(i);
            }
        }
        if (size == 1) {
            Arrays.fill(targets, 0, count, entities.get(0));
        }
    }
}
