package com.eeeab.eeeabsmobs.sever.entity.util;

import com.eeeab.eeeabsmobs.sever.entity.effects.EntityFallingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import org.joml.Quaternionf;

import java.util.Random;

/**
 * 实体工具类
 *
 * @author EEEAB
 */
public class ModEntityUtils {

    private ModEntityUtils() {
    }

    /**
     * 检查药水是否是有益的
     *
     * @param effect 药水
     * @return 布尔值
     */
    public static boolean isBeneficial(MobEffect effect) {
        return effect != null && (effect.getCategory() == MobEffectCategory.BENEFICIAL || effect.isBeneficial());
    }

    /**
     * 检查伤害源是否是弹射物
     *
     * @param source 伤害源
     * @return 布尔值
     */
    public static boolean isProjectileSource(DamageSource source) {
        Entity entity = source.getDirectEntity();
        return entity instanceof Projectile || source.is(DamageTypeTags.IS_PROJECTILE);
    }


    public static boolean checkDirectEntityConsistency(DamageSource source) {
        return source.getEntity() == source.getDirectEntity();
    }

    /**
     * 检查目标实体是否正在接近当前实体
     *
     * @param entity 当前实体
     * @param target 目标实体
     * @return 布尔值
     */
    public static boolean checkTargetComingCloser(LivingEntity entity, LivingEntity target) {
        Vec3 betweenEntitiesVec = entity.position().subtract(target.position());
        return target.getDeltaMovement().dot(betweenEntitiesVec) > 0 && target.getDeltaMovement().lengthSqr() > 0.015;
    }

    public static boolean canDestroyBlock(Level world, BlockPos pos, Entity entity, float maxBlockHardness) {
        return canDestroyBlock(world, pos, world.getBlockState(pos), maxBlockHardness, entity);
    }

    public static boolean canDestroyBlock(Level world, BlockPos pos, Entity entity) {
        return canDestroyBlock(world, pos, world.getBlockState(pos), 50f, entity);
    }

    /**
     * 判断给定区块方块是否可被破坏
     *
     * @param world            服务端&客户端
     * @param pos              区块坐标
     * @param state            方块状态
     * @param maxBlockHardness 最大硬度
     * @param entity           实体
     * @return 布尔值
     */
    public static boolean canDestroyBlock(Level world, BlockPos pos, BlockState state, float maxBlockHardness, Entity entity) {
        float hardness = state.getDestroySpeed(world, pos);
        return hardness >= 0f && hardness <= maxBlockHardness && !state.isAir()
                && state.getBlock().canEntityDestroy(state, world, pos, entity)
                && (/* 强制条件 */!(entity instanceof LivingEntity)
                || ForgeEventFactory.onEntityDestroyBlock((LivingEntity) entity, pos, state));
    }

    /**
     * 检查游戏规则:生物破坏
     *
     * @param entity 实体
     * @return 布尔值
     */
    public static boolean canMobDestroy(Entity entity) {
        return ForgeEventFactory.getMobGriefingEvent(entity.level(), entity);
    }

    /**
     * 根据指定坐标计算获得轴对称边界框
     *
     * @param yOffset 边界框y轴偏移
     * @return 特定大小的边界框
     */
    public static AABB makeAABBWithSize(double x, double y, double z, double yOffset, double sizeX, double sizeY, double sizeZ) {
        y += yOffset;
        return new AABB(x - sizeX / 2.0, y - sizeY / 2.0, z - sizeZ / 2.0, x + sizeX / 2.0, y + sizeY / 2.0, z + sizeZ / 2.0);
    }

    /**
     * 寻找到目标的直线延展坐标
     *
     * @param attacker  当前实体
     * @param target    目标
     * @param overshoot 延展距离
     * @return 坐标
     */
    public static Vec3 findPounceTargetPoint(Entity attacker, Entity target, double overshoot) {
        double vx = target.getX() - attacker.getX();
        double vz = target.getZ() - attacker.getZ();
        float angle = (float) (Math.atan2(vz, vx));

        double distance = Mth.sqrt((float) (vx * vx + vz * vz));

        double dx = Mth.cos(angle) * (distance + overshoot);
        double dz = Mth.sin(angle) * (distance + overshoot);

        return new Vec3(attacker.getX() + dx, target.getY(), attacker.getZ() + dz);
    }

    /**
     * 检查生成实体坐标
     *
     * @param summoner 召唤者
     * @param pX       x轴
     * @param pZ       z轴
     * @param pMinY    起步y轴
     * @param pMaxY    最终y轴
     * @return 坐标
     */
    public static Vec3 checkSummonEntityPoint(LivingEntity summoner, double pX, double pZ, double pMinY, double pMaxY) {
        BlockPos blockpos = BlockPos.containing(pX, pMaxY, pZ);
        boolean flag = false;
        double d0 = 0.0D;
        ServerLevel level = (ServerLevel) summoner.level();

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(level, blockpos1, Direction.UP)) {
                if (!level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(pMinY) - 1);
        if (flag) {
            return new Vec3(pX, blockpos.getY() + d0, pZ);
        }
        return new Vec3(pX, pMinY, pZ);
    }

    /**
     * 根据护甲值、盔甲韧性与盔甲减少乘数计算实际受到的伤害
     *
     * @param damage                   造成伤害
     * @param armor                    护甲值
     * @param toughness                盔甲韧性
     * @param armorReductionMultiplier 盔甲减少乘数[0~1]
     * @return 实际受到的伤害
     */
    public static float actualDamageIsCalculatedBasedOnArmor(float damage, float armor, float toughness, float armorReductionMultiplier) {
        float adjustedArmor = armor * armorReductionMultiplier;
        return CombatRules.getDamageAfterAbsorb(damage, adjustedArmor, toughness);
    }

    public static float getTargetRelativeAngle(LivingEntity entity, LivingEntity target) {
        return getTargetRelativeAngle(entity, target.position());
    }

    /**
     * 获取击中时的角度与该实体面向角度之间的差值
     */
    public static float getTargetRelativeAngle(LivingEntity entity, Vec3 position) {
        float entityHitAngle = (float) ((Math.atan2(position.z() - entity.getZ(), position.x() - entity.getX()) * (180 / Math.PI) - 90) % 360);
        float entityAttackingAngle = entity.yBodyRot % 360;
        if (entityHitAngle < 0) {
            entityHitAngle += 360;
        }
        if (entityAttackingAngle < 0) {
            entityAttackingAngle += 360;
        }
        return entityHitAngle - entityAttackingAngle;
    }

    /**
     * 击退指定实体(受事件影响)
     */
    public static void forceKnockBack(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockBackResistanceReduction, boolean canceledEventKnockBack) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
        if (canceledEventKnockBack && event.isCanceled()) return;
        strength = event.getStrength();
        ratioX = event.getRatioX();
        ratioZ = event.getRatioZ();
        strength = (float) ((double) strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockBackResistanceReduction));
        if (!(strength <= 0.0F)) {
            attackTarget.hasImpulse = true;
            Vec3 vector3d = attackTarget.getDeltaMovement();
            Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale((double) strength);
            attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.onGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }

    /**
     * 生成下落方块(渲染移动)
     *
     * @param level         服务端
     * @param pos           区块坐标
     * @param fallingFactor y轴下降系数
     */
    public static void spawnFallingBlockByPos(ServerLevel level, BlockPos pos, float fallingFactor) {
        Random random = new Random();
        BlockPos abovePos = new BlockPos(pos).above();//获取上面方块的坐标,以用来判断是否需要生成下落的方块
        BlockState block = level.getBlockState(pos);//获取下落方块,以用于渲染方块材质
        BlockState blockAbove = level.getBlockState(abovePos);//获取上面方块的状态,,以用来判断是否需要生成下落的方块

        //随机扰动
        if (random.nextBoolean()) {
            fallingFactor += (float) (0.4 + random.nextGaussian() * 0.2);
        } else {
            fallingFactor -= (float) Mth.clamp(0.2 + random.nextGaussian() * 0.2, 0.2, fallingFactor - 0.1);
        }

        if (!block.isAir() && block.isRedstoneConductor(level, pos) && !block.hasBlockEntity() && !blockAbove.blocksMotion()) {
            EntityFallingBlock fallingBlock = new EntityFallingBlock(level, block, (float) (0.32 + fallingFactor * 0.2));
            fallingBlock.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            level.addFreshEntity(fallingBlock);
        }
    }

    /**
     * 生成下落方块(整体移动)
     *
     * @param level 服务端
     * @param pos   区块坐标
     */
    public static void spawnFallingBlockByPos(ServerLevel level, BlockPos pos) {
        RandomSource random = RandomSource.create();
        BlockPos abovePos = new BlockPos(pos).above();//获取上面方块的坐标,以用来判断是否需要生成下落的方块
        BlockState block = level.getBlockState(pos);//获取下落方块,以用于渲染方块材质
        BlockState blockAbove = level.getBlockState(abovePos);//获取上面方块的状态,,以用来判断是否需要生成下落的方块

        if (!block.isAir() && block.isRedstoneConductor(level, pos) && !block.hasBlockEntity() && !blockAbove.blocksMotion()) {
            EntityFallingBlock fallingBlock = new EntityFallingBlock(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, block, 10);
            fallingBlock.push(0, 0.2 + random.nextGaussian() * 0.2, 0);
            level.addFreshEntity(fallingBlock);
        }
    }

    /**
     * 生成模拟裂纹方块(渲染移动)
     *
     * @param level         服务端
     * @param pos           区块坐标
     * @param quaternionf   四元数
     * @param fallingFactor y轴下降系数
     */
    public static void spawnFallingBlockByPos(ServerLevel level, BlockPos pos, Quaternionf quaternionf, int duration, float fallingFactor) {
        BlockPos abovePos = new BlockPos(pos).above();//获取上面方块的坐标,以用来判断是否需要生成下落的方块
        BlockState block = level.getBlockState(pos);//获取下落方块,以用于渲染方块材质
        BlockState blockAbove = level.getBlockState(abovePos);//获取上面方块的状态,,以用来判断是否需要生成下落的方块

        if (!block.isAir() && block.isRedstoneConductor(level, pos) && !block.hasBlockEntity() && !blockAbove.blocksMotion()) {
            EntityFallingBlock fallingBlock = new EntityFallingBlock(level, block, quaternionf, duration, fallingFactor);
            fallingBlock.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            level.addFreshEntity(fallingBlock);
        }
    }

    /**
     * 添加药水效果(可堆叠等级 上限5级)
     */
    public static void addEffectStackingAmplifier(LivingEntity target, MobEffect mobEffect, int duration, boolean ambient, boolean visible, boolean showIcon) {
        if (!target.hasEffect(mobEffect)) {
            target.addEffect(new MobEffectInstance(mobEffect, duration, 0, ambient, visible, showIcon));
        } else {
            MobEffectInstance instance = target.getEffect(mobEffect);
            if (instance != null) {
                int level = instance.getAmplifier();
                if (level < 4) {
                    level++;
                }
                target.addEffect(new MobEffectInstance(mobEffect, duration, level, ambient, visible, showIcon));
            }
        }
    }

    /**
     * 破坏当前实体坐标为基准周围的方块
     *
     * @param level            服务端
     * @param entity           实体
     * @param maxBlockHardness 最高破坏硬度
     * @param destroyRangeX    破坏范围x轴
     * @param destroyRangeY    破坏范围y轴
     * @param destroyRangeZ    破坏范围z轴
     * @param offset           前后偏移量
     * @param dropBlock        是否掉落方块
     * @param playSound        是否播放破坏方块的音效
     * @return 是否破坏成功
     */
    public static boolean advancedBreakBlocks(Level level, LivingEntity entity, float maxBlockHardness, int destroyRangeX, int destroyRangeY, int destroyRangeZ, int offsetY, float offset, boolean dropBlock, boolean playSound) {
        double radians = Math.toRadians(entity.getYRot() + 90);
        int j1 = Mth.floor(entity.getY());
        int i2 = Mth.floor(entity.getX() + Math.cos(radians) * offset);
        int j2 = Mth.floor(entity.getZ() + Math.sin(radians) * offset);
        boolean flag = false;
        for (int j = -destroyRangeX; j <= destroyRangeX; ++j) {
            for (int k2 = offsetY; k2 <= destroyRangeY; ++k2) {
                for (int k = -destroyRangeZ; k <= destroyRangeZ; ++k) {
                    int l2 = i2 + j;
                    int l = j1 + k2;
                    int i1 = j2 + k;
                    BlockPos blockpos = new BlockPos(l2, l, i1);
                    BlockState blockstate = level.getBlockState(blockpos);
                    if (blockstate.canEntityDestroy(level, blockpos, entity) && canDestroyBlock(level, blockpos, entity, maxBlockHardness)) {
                        flag = level.destroyBlock(blockpos, dropBlock, entity) || flag;
                    }
                }
            }
        }
        if (flag && playSound) {
            level.levelEvent(null, 1022, entity.blockPosition(), 0);
        }
        return flag;
    }
}
