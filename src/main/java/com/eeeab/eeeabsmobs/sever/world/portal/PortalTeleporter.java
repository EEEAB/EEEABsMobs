package com.eeeab.eeeabsmobs.sever.world.portal;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PortalTeleporter implements ITeleporter {
    private static final int SEARCH_RADIUS = 128;
    private static final int ATTEMPT_COUNT = 10;
    private static final int VERTICAL_SEARCH_RANGE = 15;

    /**
     * 查找传送位置
     */
    public static BlockPos findTeleportPosition(ServerLevel level, BlockPos spawnPos) {
        //if (level.dimension() != ModResourceKey.VOID_CRACK_LEVEL) return spawnPos;

        return PortalStructureHelper.findNearestPortal(level, spawnPos)
                .or(() -> createNewPortal(level, spawnPos))
                .orElse(spawnPos);
    }

    /**
     * 尝试创建新传送门
     */
    private static Optional<BlockPos> createNewPortal(ServerLevel level, BlockPos spawnPos) {
        for (int attempt = 0; attempt < ATTEMPT_COUNT; attempt++) {
            BlockPos tryPos = getRandomPosition(spawnPos, level.random);
            tryPos = findSuitableHeight(level, tryPos);

            if (PortalStructureHelper.canCreatePortalAt(level, tryPos) &&
                    PortalStructureHelper.createCompletePortalStructure(level, tryPos)) {
                return Optional.of(tryPos);
            }
        }
        return Optional.empty();
    }

    /**
     * 获取随机位置
     */
    private static BlockPos getRandomPosition(BlockPos center, RandomSource random) {
        int offsetX = (random.nextInt(21) - 10) * 2;
        int offsetZ = (random.nextInt(21) - 10) * 2;
        return center.offset(offsetX, 0, offsetZ);
    }

    /**
     * 寻找合适的高度
     */
    private static BlockPos findSuitableHeight(ServerLevel level, BlockPos pos) {
        int groundY = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());

        return IntStream.range(groundY, groundY + VERTICAL_SEARCH_RANGE)
                .mapToObj(y -> new BlockPos(pos.getX(), y, pos.getZ()))
                .filter(testPos -> PortalStructureHelper.canCreatePortalAt(level, testPos))
                .findFirst()
                .orElse(new BlockPos(pos.getX(), groundY, pos.getZ()));
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel destLevel,
                              float yaw, Function<Boolean, Entity> repositionEntity) {
        return entity instanceof ServerPlayer player
                ? placePlayer(player, currentLevel, destLevel)
                : placeOtherEntity(entity, currentLevel, destLevel);
    }

    private Entity placePlayer(ServerPlayer player, ServerLevel currentLevel, ServerLevel destLevel) {
        BlockPos targetPos = findTeleportPosition(destLevel, destLevel.getSharedSpawnPos());
        Vec3 exactPos = PortalStructureHelper.getPortalCenterPosition(destLevel, targetPos);

        player.teleportTo(destLevel, exactPos.x, exactPos.y, exactPos.z,
                player.getYRot(), player.getXRot());
        player.setPortalCooldown();
        CriteriaTriggers.CHANGED_DIMENSION.trigger(player, currentLevel.dimension(), destLevel.dimension());

        return player;
    }

    private Entity placeOtherEntity(Entity entity, ServerLevel currentLevel, ServerLevel destLevel) {
        return Optional.ofNullable((Entity) entity.getType().create(destLevel))
                .map(newEntity -> {
                    newEntity.restoreFrom(entity);
                    teleportEntity(newEntity, destLevel);
                    destLevel.addDuringTeleport(newEntity);
                    return newEntity;
                })
                .orElse(entity);
    }

    private void teleportEntity(Entity entity, ServerLevel destLevel) {
        BlockPos targetPos = findTeleportPosition(destLevel, destLevel.getSharedSpawnPos());
        Vec3 exactPos = PortalStructureHelper.getPortalCenterPosition(destLevel, targetPos);

        entity.teleportTo(exactPos.x, exactPos.y, exactPos.z);
        entity.setYRot(entity.getYRot());
        entity.setXRot(entity.getXRot());
    }
}