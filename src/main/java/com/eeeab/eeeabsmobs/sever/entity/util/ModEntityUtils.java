package com.eeeab.eeeabsmobs.sever.entity.util;

import com.eeeab.eeeabsmobs.sever.entity.effects.EntityFallingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

import java.util.Random;

public class ModEntityUtils {

    private ModEntityUtils() {
    }

    //伤害源是弹射物伤害
    public static boolean isProjectileSource(DamageSource source) {
        Entity entity = source.getDirectEntity();
        return entity instanceof Projectile || source.isProjectile();
    }

    //寻找冲向目标的坐标
    public static Vec3 findPounceTargetPoint(Entity attacker, Entity target, double overshoot) {
        double vx = target.getX() - attacker.getX();
        double vz = target.getZ() - attacker.getZ();
        float angle = (float) (Math.atan2(vz, vx));

        double distance = Mth.sqrt((float) (vx * vx + vz * vz));

        double dx = Mth.cos(angle) * (distance + overshoot);
        double dz = Mth.sin(angle) * (distance + overshoot);

        return new Vec3(attacker.getX() + dx, target.getY(), attacker.getZ() + dz);
    }

    public static void spawnFallingBlockByPos(ServerLevel level, BlockPos pos, float fallingFactor) {
        Random random = new Random();
        BlockPos abovePos = new BlockPos(pos).above();//获取上面方块的坐标,以用来判断是否需要生成下落的方块
        BlockState block = level.getBlockState(pos);//获取下落方块,以用于渲染方块材质
        BlockState blockAbove = level.getBlockState(abovePos);//获取上面方块的状态,,以用来判断是否需要生成下落的方块

        //随机扰动
        if (random.nextBoolean()) {
            fallingFactor += 0.4 + random.nextGaussian() * 0.2;
        } else {
            fallingFactor -= Mth.clamp(0.2 + random.nextGaussian() * 0.2, 0.2, fallingFactor - 0.1);
        }

        if (block.getMaterial() != Material.AIR && block.isRedstoneConductor(level, pos) && !block.hasBlockEntity() && !blockAbove.getMaterial().blocksMotion()) {
            EntityFallingBlock fallingBlock = new EntityFallingBlock(level, block, (float) (0.32 + fallingFactor * 0.2));
            fallingBlock.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            level.addFreshEntity(fallingBlock);
        }
    }

    public static void spawnFallingBlockByPos(ServerLevel level, BlockPos pos) {
        RandomSource random = RandomSource.create();
        BlockPos abovePos = new BlockPos(pos).above();//获取上面方块的坐标,以用来判断是否需要生成下落的方块
        BlockState block = level.getBlockState(pos);//获取下落方块,以用于渲染方块材质
        BlockState blockAbove = level.getBlockState(abovePos);//获取上面方块的状态,,以用来判断是否需要生成下落的方块

        if (block.getMaterial() != Material.AIR && block.isRedstoneConductor(level, pos) && !block.hasBlockEntity() && !blockAbove.getMaterial().blocksMotion()) {
            EntityFallingBlock fallingBlock = new EntityFallingBlock(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, block, 10);
            fallingBlock.push(0, 0.2 + random.nextGaussian() * 0.2, 0);
            level.addFreshEntity(fallingBlock);
        }
    }

    //判断指定y轴的生成位置
    public static Vec3 checkSummonEntityPoint(LivingEntity summoner, double pX, double pZ, double pMinY, double pMaxY) {
        BlockPos blockpos = new BlockPos(pX, pMaxY, pZ);
        boolean flag = false;
        double d0 = 0.0D;
        ServerLevel level = (ServerLevel) summoner.level;

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
        return new Vec3(summoner.getX(), summoner.getY(), summoner.getZ());
    }

    //获取是否目标逐渐逼近
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

    //判断方块是否可以摧毁
    public static boolean canDestroyBlock(Level world, BlockPos pos, BlockState state, float maxBlockHardness, Entity entity) {
        float hardness = state.getDestroySpeed(world, pos);
        return hardness >= 0f && hardness <= maxBlockHardness && !state.isAir()
                && state.getBlock().canEntityDestroy(state, world, pos, entity)
                && (/* 强制条件 */!(entity instanceof LivingEntity)
                || ForgeEventFactory.onEntityDestroyBlock((LivingEntity) entity, pos, state));
    }

    //判断是否开启生物破坏规则
    public static boolean canMobDestroy(Entity entity) {
        return ForgeEventFactory.getMobGriefingEvent(entity.level, entity);
    }

    //击退生物(受事件影响)
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
            attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }


    //获取与目标实体相对角度
    public static float getTargetRelativeAngle(LivingEntity entity, LivingEntity target) {
        float entityHitAngle = (float) ((Math.atan2(target.getZ() - entity.getZ(), target.getX() - entity.getX()) * (180 / Math.PI) - 90) % 360);
        float entityAttackingAngle = entity.yBodyRot % 360;
        if (entityHitAngle < 0) {
            entityHitAngle += 360;
        }
        if (entityAttackingAngle < 0) {
            entityAttackingAngle += 360;
        }
        return entityHitAngle - entityAttackingAngle;
    }

    //获取与目标坐标相对角度
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

    //添加药水效果(可堆叠等级 上限5级)
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
}
