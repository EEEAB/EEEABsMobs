package com.eeeab.eeeabsmobs.sever.entity.util;

import com.mojang.math.Quaternion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * 环形冲击波效果通用工具类
 *
 * @author EEEAB
 */
public class ShockWaveUtils {

    public static List<LivingEntity> doRingShockWave(Level level, Vec3 center, double radius, float baseBouncing) {
        Vec3 closestEdge = new Vec3(Math.round(center.x), Math.floor(center.y), Math.round(center.z));
        Vec3 centerOfBlock = new Vec3(Math.floor(center.x) + 0.5D, Math.floor(center.y), Math.floor(center.z) + 0.5D);
        center = (closestEdge.distanceToSqr(center) < centerOfBlock.distanceToSqr(center)) ? closestEdge : centerOfBlock;
        radius = Math.max(0.5F, radius);
        int xFrom = (int) Math.floor(center.x - radius);
        int xTo = (int) Math.ceil(center.x + radius);
        int zFrom = (int) Math.floor(center.z - radius);
        int zTo = (int) Math.ceil(center.z + radius);
        for (int i = zFrom; i <= zTo; i++) {
            for (int j = xFrom; j <= xTo; j++) {
                Vec2 vec2 = new Vec2(j, i);
                double y = center.y;
                BlockPos pos = new BlockPos.MutableBlockPos(vec2.x(), y, vec2.z());
                Vec3 blockCenter = new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                Vec3 centerToBlock = blockCenter.subtract(center);
                double distance = centerToBlock.horizontalDistance();
                double distanceToMax = radius - distance;
                if (radius < distance) {
                    continue;
                }
                double bounceExponent = Math.min(1D / (radius * radius), 0.1D) * (1D + distanceToMax / radius * 2D);
                if (!level.isClientSide) {
                    Vec3 rotAxis = new Vec3(0D, -1D, 0D).cross(centerToBlock).normalize();
                    Vector3f axis = new Vector3f((float) rotAxis.x, (float) rotAxis.y, (float) rotAxis.z);
                    Quaternion rotator = axis.rotationDegrees((float) (distance / radius) * 15F + level.random.nextFloat() * 10F - 5F);
                    rotator.mul(Vector3f.XP.rotationDegrees(level.random.nextFloat() * 12F - 6F));
                    rotator.mul(Vector3f.YP.rotationDegrees(level.random.nextFloat() * 40F - 20F));
                    rotator.mul(Vector3f.ZP.rotationDegrees(level.random.nextFloat() * 12F - 6F));
                    float bouncing = (float) (baseBouncing + distance * bounceExponent);
                    int lifeTime = 20 + level.random.nextInt((int) radius * 20);
                    ModEntityUtils.spawnFallingBlockByPos((ServerLevel) level, pos, rotator, lifeTime, bouncing);
                }
            }
        }
        if (level.isClientSide) {
            return new ArrayList<>();
        }
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(xFrom, center.y - radius, zFrom, xTo, center.y + radius, zTo));
    }

    private record Vec2(float x, float z) {
    }
}
