package com.eeeab.eeeabsmobs.sever.entity.util;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.QuaternionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 通用环形冲击波效果工具类
 *
 * @author EEEAB
 */
public class ShockWaveUtils {

    /**
     * 通用撼地攻击
     *
     * @param level           服务端
     * @param center          生成中心坐标
     * @param radius          生成半径
     * @param baseBouncing    基础起伏
     * @param customLife      是否自定义存在时长
     * @param defaultLifeTime 默认存在时长
     * @return 包含在范围内的实体集合
     */
    public static List<LivingEntity> doRingShockWave(Level level, Vec3 center, double radius, float baseBouncing, boolean customLife, int defaultLifeTime) {
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
                BlockPos pos = new BlockPos.MutableBlockPos(vec2.x(), checkSpawnY(level, vec2.x(), vec2.z(), y), vec2.z());
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
                    Quaternionf rotator = QuaternionUtils.rotation(axis, (float) (distance / radius) * 15F + level.random.nextFloat() * 10F - 5F, true);
                    rotator.mul(QuaternionUtils.XP.rotationDegrees(level.random.nextFloat() * 12F - 6F));
                    rotator.mul(QuaternionUtils.YP.rotationDegrees(level.random.nextFloat() * 40F - 20F));
                    rotator.mul(QuaternionUtils.ZP.rotationDegrees(level.random.nextFloat() * 12F - 6F));
                    float bouncing = (float) (baseBouncing + distance * bounceExponent);
                    int liftTime = defaultLifeTime;
                    if (!customLife) {
                        liftTime = defaultLifeTime + level.random.nextInt((int) radius * defaultLifeTime);
                    }
                    ModEntityUtils.spawnFallingBlockByPos((ServerLevel) level, pos, rotator, liftTime, bouncing);
                }
            }
        }
        if (level.isClientSide) {
            return new ArrayList<>();
        }
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(xFrom, center.y - radius, zFrom, xTo, center.y + radius, zTo));
    }

    /**
     * 通用撼地攻击
     *
     * @param attacker           攻击者
     * @param distance           距离
     * @param maxFallingDistance 最大y轴起伏
     * @param spreadArc          攻击角度
     * @param offset             前后偏移
     * @param randomOffset       是否生成方块随机y轴偏移
     * @param continuous         是否在同一时刻发生
     * @param hitProvider        提供具体攻击的函数
     * @param knockBackStrength  击飞方块强度 !randomOffset生效
     */
    public static void doAdvShockWave(LivingEntity attacker, int distance, float maxFallingDistance, double spreadArc, double offset, boolean randomOffset, boolean continuous, Consumer<Entity> hitProvider, float knockBackStrength) {
        ServerLevel level = (ServerLevel) attacker.level();
        double perpFacing = attacker.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double spread = Math.PI * spreadArc;
        int arcLen = Mth.ceil(distance * spread);
        double minY = attacker.getBoundingBox().minY - 2D;
        double maxY = attacker.getBoundingBox().maxY;
        int hitY = Mth.floor(attacker.getBoundingBox().minY - 0.5);
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = attacker.getX() + vx * distance + offset * Math.cos((double) (attacker.yBodyRot + 90.0F) * Math.PI / 180.0D);
            double pz = attacker.getZ() + vz * distance + offset * Math.sin((double) (attacker.yBodyRot + 90.0F) * Math.PI / 180.0D);
            AABB aabb = new AABB(px - 1.5D, minY, pz - 1.5D, px + 1.5D, maxY, pz + 1.5D);
            List<Entity> entities = attacker.level().getEntitiesOfClass(Entity.class, aabb, entity -> entity.isAttackable() && attacker != entity);
            for (Entity entity : entities) hitProvider.accept(entity);
            float factor = 1F - ((float) distance / 2F - 2F) / maxFallingDistance;
            if (continuous || attacker.getRandom().nextFloat() < 0.6F) {
                int hitX = Mth.floor(px);
                int hitZ = Mth.floor(pz);
                BlockPos pos = new BlockPos.MutableBlockPos(hitX, checkSpawnY(level, hitX, hitZ, hitY), hitZ);
                if (randomOffset) ModEntityUtils.spawnFallingBlockByPos(level, pos, factor);
                else {
                    double d0 = hitX - attacker.getX();
                    double d1 = hitZ - attacker.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
                    ModEntityUtils.spawnFallingBlockByPos(level, pos, d0 / d2 * knockBackStrength, d1 / d2 * knockBackStrength);
                }
            }
        }
    }

    /**
     * @return 用于确定实体生成的y轴位置
     */
    private static double checkSpawnY(Level level, double x, double z, double y) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, Math.round(y), z);
        if (!level.getBlockState(pos).isAir()) {
            if (!level.getBlockState(pos.above()).isAir()) {
                return pos.getY() + 1;
            }
            return y;
        }
        for (int offset = 1; offset <= EMConfigHandler.COMMON.ENTITY.fallingBlockBelowCheckRange.get(); offset++) {
            BlockState stateBelow = level.getBlockState(pos.below(offset));
            if (!stateBelow.isAir()) {
                return pos.getY() - offset;
            }
        }
        return y;
    }


    private record Vec2(float x, float z) {
    }
}
