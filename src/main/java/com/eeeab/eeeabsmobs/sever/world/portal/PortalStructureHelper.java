package com.eeeab.eeeabsmobs.sever.world.portal;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

// 重新设计 PortalStructureHelper
public class PortalStructureHelper {
    public static final TicketType<BlockPos> CUSTOM_PORTAL = TicketType.create(
            EEEABMobs.MOD_ID + "erosion_portal",
            Vec3i::compareTo,
            300
    );
    public static Holder<PoiType> poi = null;
    // 尺寸常量
    private static final int OUTER_SIZE = 5;
    private static final int INNER_SIZE = 3;
    private static final int FRAME_OFFSET = 2;

    // 方块引用
    private static class PortalBlocks {
        public static final Block PORTAL_FRAME_BLOCK = BlockInit.EROSION_DEEPSLATE_BRICKS.get();
        public static final Block CORNER_BLOCK = BlockInit.RUNIC_BOUNDARY_STONE.get();
        public static final Block FRAME_BLOCK = BlockInit.UNCARVED_BOUNDARY_STONE.get();
        public static final Block PORTAL_BLOCK = BlockInit.VOID_CRACK_PORTAL.get();
        public static final Block LOCK_BLOCK = BlockInit.BOUNDARY_CORE.get();
    }

    @SubscribeEvent
    public static void onRegisterPointOfInterest(RegisterEvent event) {
        if (!event.getRegistryKey().equals(ForgeRegistries.Keys.POI_TYPES)) return;
        event.register(ForgeRegistries.Keys.POI_TYPES, helper -> {
            Set<BlockState> portalStates = Set.copyOf(
                    BlockInit.VOID_CRACK_PORTAL.get().getStateDefinition().getPossibleStates()
            );
            PoiType poiType = new PoiType(portalStates, 0, 1);
            helper.register(ModResourceKey.EROSION_PORTAL, poiType);
            poi = ForgeRegistries.POI_TYPES.getHolder(poiType).orElse(null);
        });
    }

    /**
     * 尝试从锁方块位置激活传送门
     */
    public static boolean tryActivatePortalFromLock(LevelAccessor level, BlockPos lockPos) {
        if (!(level instanceof ServerLevel serverLevel)) return false;
        return searchPortalStructure(level, lockPos)
                .filter(result -> activatePortalAtStructure(serverLevel, result.startPos))
                .isPresent();
    }

    /**
     * 搜索有效的传送门结构
     */
    private static Optional<StructureCheckResult> searchPortalStructure(LevelAccessor level, BlockPos lockPos) {
        return IntStream.rangeClosed(-FRAME_OFFSET, FRAME_OFFSET)
                .boxed()
                .flatMap(dx -> IntStream.rangeClosed(-FRAME_OFFSET, FRAME_OFFSET)
                        .mapToObj(dz -> new BlockPos(lockPos.getX() + dx, lockPos.getY(), lockPos.getZ() + dz)))
                .map(centerPos -> checkStructureWithLock(level, centerPos, lockPos))
                .filter(StructureCheckResult::isValid)
                .findFirst();
    }

    /**
     * 检查结构是否完整（带锁方块检查）
     */
    private static StructureCheckResult checkStructureWithLock(LevelAccessor level, BlockPos centerPos, BlockPos lockPos) {
        BlockPos startPos = centerPos.offset(-FRAME_OFFSET, 0, -FRAME_OFFSET);

        // 检查锁方块位置
        Optional<FramePosition> lockPosition = findLockPosition(startPos, lockPos);
        if (lockPosition.isEmpty()) return StructureCheckResult.INVALID;

        // 验证整个结构
        for (int x = 0; x < OUTER_SIZE; x++) {
            for (int z = 0; z < OUTER_SIZE; z++) {
                BlockPos checkPos = startPos.offset(x, 0, z);
                if (!isPositionValid(level, checkPos, x, z, lockPosition.get())) {
                    return StructureCheckResult.INVALID;
                }
            }
        }

        return new StructureCheckResult(true, startPos);
    }

    /**
     * 检查位置是否有效
     */
    private static boolean isPositionValid(LevelAccessor level, BlockPos pos, int x, int z, FramePosition lockPos) {
        BlockState state = level.getBlockState(pos);

        Block block = state.getBlock();
        if (isCorner(x, z)) {
            return block == PortalBlocks.CORNER_BLOCK || block == PortalBlocks.PORTAL_FRAME_BLOCK;
        } else if (isFrame(x, z)) {
            if (x == lockPos.x && z == lockPos.z) {
                return block == PortalBlocks.LOCK_BLOCK;
            }
            return block == PortalBlocks.FRAME_BLOCK || block == PortalBlocks.PORTAL_FRAME_BLOCK;
        } else { // 内部位置
            return state.isAir() || state.canBeReplaced();
        }
    }

    /**
     * 查找锁方块在结构中的位置
     */
    private static Optional<FramePosition> findLockPosition(BlockPos startPos, BlockPos lockPos) {
        for (int x = 0; x < OUTER_SIZE; x++) {
            for (int z = 0; z < OUTER_SIZE; z++) {
                if (isFrame(x, z) && startPos.offset(x, 0, z).equals(lockPos)) {
                    return Optional.of(new FramePosition(x, z));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 在完整结构上激活传送门
     */
    private static boolean activatePortalAtStructure(ServerLevel level, BlockPos startPos) {
        // 放置内部的传送门方块
        placePortalBlocks(level, startPos);
        // 播放激活效果
        playPortalEffects(level, startPos.offset(FRAME_OFFSET, 0, FRAME_OFFSET), 30);
        return true;
    }

    /**
     * 检查传送门结构是否完整
     */
    public static boolean isPortalStructureComplete(LevelAccessor level, BlockPos portalPos) {
        return IntStream.rangeClosed(-FRAME_OFFSET, FRAME_OFFSET)
                .boxed()
                .flatMap(dx -> IntStream.rangeClosed(-FRAME_OFFSET, FRAME_OFFSET)
                        .mapToObj(dz -> portalPos.offset(dx, 0, dz)))
                .map(centerPos -> centerPos.offset(-FRAME_OFFSET, 0, -FRAME_OFFSET))
                .anyMatch(startPos -> checkCompleteStructure(level, startPos));
    }

    /**
     * 检查完整结构（可选择是否检查内部传送门）
     */
    private static boolean checkCompleteStructure(LevelAccessor level, BlockPos startPos) {
        for (int x = 0; x < OUTER_SIZE; x++) {
            for (int z = 0; z < OUTER_SIZE; z++) {
                BlockPos checkPos = startPos.offset(x, 0, z);
                BlockState state = level.getBlockState(checkPos);
                Block block = state.getBlock();
                if (isCorner(x, z)) {
                    if (block != PortalBlocks.CORNER_BLOCK && block != PortalBlocks.PORTAL_FRAME_BLOCK) return false;
                } else if (isFrame(x, z)) {
                    if (block != PortalBlocks.FRAME_BLOCK && block != PortalBlocks.LOCK_BLOCK && block != PortalBlocks.PORTAL_FRAME_BLOCK) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 创建完整的传送门结构（可选择是否包含锁方块）
     */
    public static boolean createCompletePortalStructure(LevelAccessor level, BlockPos centerPos) {
        if (!(level instanceof ServerLevel serverLevel) || !canCreatePortalAt(level, centerPos)) {
            return false;
        }
        BlockPos startPos = centerPos.offset(-FRAME_OFFSET, 0, -FRAME_OFFSET);
        for (int x = 0; x < OUTER_SIZE; x++) {
            for (int z = 0; z < OUTER_SIZE; z++) {
                BlockPos pos = startPos.offset(x, 0, z);
                Block block = isFrame(x, z) || isCorner(x, z) ? PortalBlocks.PORTAL_FRAME_BLOCK : Blocks.AIR;
                level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        }
        placePortalBlocks(serverLevel, startPos);
        playPortalEffects(serverLevel, centerPos, 40);
        return true;
    }

    /**
     * 检查是否可以在此位置创建完整传送门
     */
    public static boolean canCreatePortalAt(LevelAccessor level, BlockPos centerPos) {
        BlockPos startPos = centerPos.offset(-FRAME_OFFSET, 0, -FRAME_OFFSET);
        return IntStream.range(0, OUTER_SIZE)
                .boxed()
                .flatMap(x -> IntStream.range(0, OUTER_SIZE)
                        .mapToObj(z -> startPos.offset(x, 0, z)))
                .allMatch(pos -> {
                    if (!level.getWorldBorder().isWithinBounds(pos)) return false;
                    BlockState state = level.getBlockState(pos);
                    return state.isAir() || state.canBeReplaced();
                });
    }


    /**
     * 放置传送门方块
     */
    private static void placePortalBlocks(ServerLevel level, BlockPos startPos) {
        for (int x = 1; x <= INNER_SIZE; x++) {
            for (int z = 1; z <= INNER_SIZE; z++) {
                BlockPos pos = startPos.offset(x, 0, z);
                level.setBlock(pos, PortalBlocks.PORTAL_BLOCK.defaultBlockState(), 18);
            }
        }
    }

    /**
     * 查找最近的传送门
     */
    public static Optional<BlockPos> findNearestPortal(ServerLevel level, BlockPos centerPos) {
        if (poi == null) return Optional.empty();
        return level.getPoiManager()
                .getInSquare(poiHolder -> poiHolder.is(poi.unwrapKey().get()),
                        centerPos, 128, PoiManager.Occupancy.ANY)
                .filter(poiRecord -> level.getWorldBorder().isWithinBounds(poiRecord.getPos()))
                .filter(poiRecord -> isPortalStructureComplete(level, poiRecord.getPos()))
                .min(Comparator.comparingDouble(poiRecord -> poiRecord.getPos().distSqr(centerPos)))
                .map(poiRecord -> {
                    BlockPos portalPos = poiRecord.getPos();
                    level.getChunkSource().addRegionTicket(CUSTOM_PORTAL, new ChunkPos(portalPos), 3, portalPos);
                    return portalPos;
                });
    }

    /**
     * 获取传送门的中心位置
     */
    public static Vec3 getPortalCenterPosition(LevelAccessor level, BlockPos portalBlockPos) {
        return findPortalCenter(level, portalBlockPos)
                .map(Vec3::atCenterOf)
                .orElse(Vec3.atCenterOf(portalBlockPos));
    }

    /**
     * 查找传送门中心位置
     */
    private static Optional<BlockPos> findPortalCenter(LevelAccessor level, BlockPos portalBlockPos) {
        return IntStream.rangeClosed(-FRAME_OFFSET, FRAME_OFFSET)
                .boxed()
                .flatMap(dx -> IntStream.rangeClosed(-FRAME_OFFSET, FRAME_OFFSET)
                        .mapToObj(dz -> portalBlockPos.offset(dx, 0, dz)))
                .filter(centerPos -> checkCompleteStructure(level,
                        centerPos.offset(-FRAME_OFFSET, 0, -FRAME_OFFSET)))
                .findFirst();
    }

    /**
     * 播放传送门效果
     */
    private static void playPortalEffects(ServerLevel level, BlockPos centerPos, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            double dx = centerPos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * (particleCount / 7.5);
            double dy = centerPos.getY() + 0.5 + (level.random.nextDouble() - 0.5) * (particleCount / 7.5);
            double dz = centerPos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * (particleCount / 7.5);
            level.sendParticles(ParticleTypes.PORTAL, dx, dy, dz, 1, 0, 0, 0, 0.1);
        }
    }

    /**
     * 位置判断辅助方法
     */
    private static boolean isCorner(int x, int z) {
        return (x == 0 || x == OUTER_SIZE - 1) && (z == 0 || z == OUTER_SIZE - 1);
    }

    private static boolean isFrame(int x, int z) {
        return (x == 0 || x == OUTER_SIZE - 1 || z == 0 || z == OUTER_SIZE - 1) && !isCorner(x, z);
    }

    /**
     * 内部记录类
     */
    private record StructureCheckResult(boolean isValid, BlockPos startPos) {
        static final StructureCheckResult INVALID = new StructureCheckResult(false, null);
    }

    private record FramePosition(int x, int z) {
    }
}

