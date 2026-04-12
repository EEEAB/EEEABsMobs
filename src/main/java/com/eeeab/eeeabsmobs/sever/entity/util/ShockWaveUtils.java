package com.eeeab.eeeabsmobs.sever.entity.util;

import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityFallingBlock;
import com.eeeab.eeeabsmobs.sever.util.ModMathUtils;
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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 通用环形冲击波效果工具类
 *
 * @author EEEAB
 */
public class ShockWaveUtils {

    /**
     * 通用撼地攻击
     *
     * @param attacker        攻击者
     * @param center          生成中心坐标
     * @param radius          生成半径
     * @param baseBouncing    基础起伏
     * @param customLife      是否自定义存在时长
     * @param defaultLifeTime 默认存在时长
     * @return 包含在范围内的实体集合
     */
    public static List<LivingEntity> doRingShockWave(LivingEntity attacker, Vec3 center, double radius, float baseBouncing, boolean customLife, int defaultLifeTime) {
        Level level = attacker.level();
        if (level.isClientSide) return Collections.emptyList();
        radius = Math.max(0.5F, radius);
        defaultLifeTime = Math.max(1, defaultLifeTime);
        int xFrom = (int) Math.floor(center.x - radius);
        int xTo = (int) Math.ceil(center.x + radius);
        int zFrom = (int) Math.floor(center.z - radius);
        int zTo = (int) Math.ceil(center.z + radius);
        for (int i = zFrom; i <= zTo; i++) {
            for (int j = xFrom; j <= xTo; j++) {
                double y = center.y;
                BlockPos pos = new BlockPos.MutableBlockPos(j, getValidBlockState(level, j, i, y), i);
                Vec3 blockCenter = new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                Vec3 centerToBlock = blockCenter.subtract(center);
                double distance = centerToBlock.horizontalDistance();
                double distanceToMax = radius - distance;
                if (radius < distance) continue;
                double bounceExponent = Math.min(1D / (radius * radius), 0.1D) * (0.75D + distanceToMax / radius * 1.25D);
                Vec3 rotAxis = new Vec3(0D, -1D, 0D).cross(centerToBlock).normalize();
                Vector3f axis = new Vector3f((float) rotAxis.x, (float) rotAxis.y, (float) rotAxis.z);
                Quaternionf rotator = ModMathUtils.rotation(axis, (float) (distance / radius) * 15F + level.random.nextFloat() * 10F - 5F, true);
                rotator.mul(ModMathUtils.XP.rotationDegrees(level.random.nextFloat() * 12F - 6F));
                rotator.mul(ModMathUtils.YP.rotationDegrees(level.random.nextFloat() * 40F - 20F));
                rotator.mul(ModMathUtils.ZP.rotationDegrees(level.random.nextFloat() * 12F - 6F));
                float bouncing = (float) (baseBouncing + distance * bounceExponent);
                int lifeTime = customLife ? defaultLifeTime : defaultLifeTime + level.random.nextInt((int) radius * defaultLifeTime);
                trySpawnFallingBlock((ServerLevel) level, pos, rotator, lifeTime, bouncing, false, 0, 0);
            }
        }
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(xFrom, center.y - radius, zFrom, xTo, center.y + radius, zTo), e -> e != attacker && !attacker.isAlliedTo(e));
    }

    /**
     * 通用撼地攻击
     *
     * @param attacker           攻击者
     * @param distance           距离
     * @param maxFallingDistance 最大y轴起伏
     * @param spreadArc          攻击角度[0-2]对应0-360度
     * @param offset             前后偏移
     * @param attackY            攻击纵深
     * @param randomOffset       是否生成方块随机y轴偏移
     * @param continuous         是否在同一时刻发生
     * @param knockBackStrength  击飞方块强度 !randomOffset生效
     * @return 包含在范围内的实体集合
     */
    public static List<Entity> doAdvShockWave(LivingEntity attacker, int distance, float maxFallingDistance, double spreadArc, double offset, double attackY, boolean randomOffset, boolean continuous, float knockBackStrength) {
        Level level = attacker.level();
        if (level.isClientSide) return Collections.emptyList();
        Set<Entity> processedEntities = new HashSet<>();
        double perpFacing = attacker.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double spread = Math.PI * Mth.clamp(spreadArc, 0, 2);
        int arcLen = Mth.ceil(distance * spread);
        double minY = attacker.getBoundingBox().minY - attackY;
        double maxY = attacker.getBoundingBox().maxY + attackY;
        int hitY = Mth.floor(attacker.getBoundingBox().minY - 0.5);
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = attacker.getX() + vx * distance + offset * Math.cos((double) (attacker.yBodyRot + 90.0F) * Math.PI / 180.0D);
            double pz = attacker.getZ() + vz * distance + offset * Math.sin((double) (attacker.yBodyRot + 90.0F) * Math.PI / 180.0D);
            AABB aabb = new AABB(px - 1.5D, minY, pz - 1.5D, px + 1.5D, maxY, pz + 1.5D);
            processedEntities.addAll(attacker.level().getEntitiesOfClass(Entity.class, aabb, e -> e != attacker && e.isAttackable() && !attacker.isAlliedTo(e)));
            float factor = 1F - ((float) distance / 2F - 2F) / maxFallingDistance;
            if (continuous || attacker.getRandom().nextFloat() < 0.6F) {
                int hitX = Mth.floor(px);
                int hitZ = Mth.floor(pz);
                BlockPos pos = new BlockPos.MutableBlockPos(hitX, getValidBlockState(level, hitX, hitZ, hitY), hitZ);
                double d0 = hitX - attacker.getX();
                double d1 = hitZ - attacker.getZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
                trySpawnFallingBlock((ServerLevel) level, pos, null, 10, factor, randomOffset, d0 / d2 * knockBackStrength, d1 / d2 * knockBackStrength);
            }
        }
        return processedEntities.stream().toList();
    }

    /**
     * @return 用于确定实体生成的y轴位置
     */
    private static double getValidBlockState(Level level, double x, double z, double y) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, Math.round(y), z);
        if (!level.getBlockState(pos).isAir()) {
            if (!level.getBlockState(pos.above()).isAir()) {
                return pos.getY() + 1;
            }
            return y;
        }
        for (int offset = 1; offset <= ModConfigHandler.COMMON.entities.fallingBlockConfig2.get(); offset++) {
            BlockState stateBelow = level.getBlockState(pos.below(offset));
            if (!stateBelow.isAir()) {
                return pos.getY() - offset;
            }
        }
        return y;
    }

    private static BlockState getValidBlockState(ServerLevel level, BlockPos pos) {
        if (!ModConfigHandler.COMMON.entities.fallingBlockConfig1.get()) return null;
        BlockPos abovePos = new BlockPos(pos).above();
        BlockState block = level.getBlockState(pos);
        BlockState blockAbove = level.getBlockState(abovePos);
        boolean flag = !block.isAir() && !block.hasBlockEntity() && !blockAbove.blocksMotion();
        return flag ? block : null;
    }

    private static void trySpawnFallingBlock(ServerLevel level, BlockPos pos,
                                             @Nullable Quaternionf rot, int lifeTime, float bounce,
                                             boolean useRandomOffset, double mx, double mz) {
        BlockState block = getValidBlockState(level, pos);
        if (block == null) return;
        EntityFallingBlock entity;
        if (rot != null) {
            entity = new EntityFallingBlock(level, block, rot, lifeTime, bounce);
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        } else if (useRandomOffset) {
            entity = new EntityFallingBlock(level, block, (float) (0.32 + bounce * 0.2));
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        } else {
            entity = new EntityFallingBlock(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, block, lifeTime);
            entity.push(mx, 0.2 + level.random.nextGaussian() * 0.2, mz);
        }
        level.addFreshEntity(entity);
    }
}
