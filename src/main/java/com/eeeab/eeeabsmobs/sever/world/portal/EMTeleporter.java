package com.eeeab.eeeabsmobs.sever.world.portal;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import net.minecraft.BlockUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class EMTeleporter implements ITeleporter {
    public static final TicketType<BlockPos> CUSTOM_PORTAL = TicketType.create(EEEABMobs.MOD_ID + "portal", Vec3i::compareTo, 300);
    private final ServerLevel level;
    private final BlockPos entityEnterPos;
    public static Holder<PoiType> poi = null;

    public static void onRegisterPointOfInterest(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.POI_TYPES, registerHelper -> {
            PoiType poiType = new PoiType(Set.copyOf(BlockInit.EROSION_PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1);
            registerHelper.register(EMResourceKey.EROSION_PORTAL, poiType);
            poi = ForgeRegistries.POI_TYPES.getHolder(poiType).get();
        });
    }

    public EMTeleporter(ServerLevel worldServer, BlockPos entityEnterPos) {
        this.level = worldServer;
        this.entityEnterPos = entityEnterPos;
    }

    public Optional<BlockUtil.FoundRectangle> findPortalAround(BlockPos blockPos, boolean isNether, WorldBorder worldBorder) {
        PoiManager poimanager = this.level.getPoiManager();
        int i = isNether ? 16 : 128;
        poimanager.ensureLoadedAndValid(this.level, blockPos, i);
        Optional<PoiRecord> optional = poimanager
                .getInSquare((typeHolder) -> typeHolder.is(poi.unwrapKey().get()), blockPos, i, PoiManager.Occupancy.ANY)
                .filter((poiRecord) -> worldBorder.isWithinBounds(poiRecord.getPos()))
                .sorted(Comparator.<PoiRecord>comparingDouble((poiRecord) -> poiRecord.getPos().distSqr(blockPos))
                        .thenComparingInt((poiRecord) -> poiRecord.getPos().getY())).filter((poiRecord) -> this.level.getBlockState(poiRecord.getPos())
                        .hasProperty(BlockStateProperties.HORIZONTAL_AXIS)).findFirst();
        return optional.map((poiRecord) -> {
            BlockPos blockpos = poiRecord.getPos();
            this.level.getChunkSource().addRegionTicket(CUSTOM_PORTAL, new ChunkPos(blockpos), 3, blockpos);
            BlockState blockstate = this.level.getBlockState(blockpos);
            return BlockUtil.getLargestRectangleAround(blockpos, blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (p_192978_) -> this.level.getBlockState(p_192978_) == blockstate);
        });
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos blockPos, Direction.Axis axis) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double d0 = -1.0D;
        BlockPos blockpos = null;
        double d1 = -1.0D;
        BlockPos blockpos1 = null;
        WorldBorder worldborder = this.level.getWorldBorder();
        int i = Math.min(this.level.getMaxBuildHeight(), this.level.getMinBuildHeight() + this.level.getLogicalHeight()) - 1;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = blockPos.mutable();
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos1 : BlockPos.spiralAround(blockPos, 16, Direction.EAST, Direction.SOUTH)) {
            int j = Math.min(i, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getZ()));
            int k = 1;
            if (worldborder.isWithinBounds(blockpos$mutableblockpos1) && worldborder.isWithinBounds(blockpos$mutableblockpos1.move(direction, 1))) {
                blockpos$mutableblockpos1.move(direction.getOpposite(), 1);
                for (int l = j; l >= this.level.getMinBuildHeight(); --l) {
                    blockpos$mutableblockpos1.setY(l);
                    if (this.canPortalReplaceBlock(blockpos$mutableblockpos1)) {
                        int i1;
                        for (i1 = l; l > this.level.getMinBuildHeight() && this.canPortalReplaceBlock(blockpos$mutableblockpos1.move(Direction.DOWN)); --l) {
                        }
                        if (l + 4 <= i) {
                            int j1 = i1 - l;
                            if (j1 <= 0 || j1 >= 3) {
                                blockpos$mutableblockpos1.setY(l);
                                if (this.canHostFrame(blockpos$mutableblockpos1, blockpos$mutableblockpos, direction, 0)) {
                                    double d2 = blockPos.distSqr(blockpos$mutableblockpos1);
                                    if (this.canHostFrame(blockpos$mutableblockpos1, blockpos$mutableblockpos, direction, -1) && this.canHostFrame(blockpos$mutableblockpos1, blockpos$mutableblockpos, direction, 1) && (d0 == -1.0D || d0 > d2)) {
                                        d0 = d2;
                                        blockpos = blockpos$mutableblockpos1.immutable();
                                    }
                                    if (d0 == -1.0D && (d1 == -1.0D || d1 > d2)) {
                                        d1 = d2;
                                        blockpos1 = blockpos$mutableblockpos1.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (d0 == -1.0D && d1 != -1.0D) {
            blockpos = blockpos1;
            d0 = d1;
        }
        if (d0 == -1.0D) {
            int k1 = Math.max(this.level.getMinBuildHeight() - -1, 70);
            int i2 = i - 9;
            if (i2 < k1) {
                return Optional.empty();
            }
            blockpos = (new BlockPos(blockPos.getX(), Mth.clamp(blockPos.getY(), k1, i2), blockPos.getZ())).immutable();
            Direction direction1 = direction.getClockWise();
            if (!worldborder.isWithinBounds(blockpos)) {
                return Optional.empty();
            }
            for (int i3 = -1; i3 < 2; ++i3) {
                for (int j3 = 0; j3 < 2; ++j3) {
                    for (int k3 = -1; k3 < 3; ++k3) {
                        BlockState blockstate1 = k3 < 0 ? BlockInit.EROSION_DEEPSLATE_BRICKS.get().defaultBlockState() : Blocks.AIR.defaultBlockState();
                        blockpos$mutableblockpos.setWithOffset(blockpos, j3 * direction.getStepX() + i3 * direction1.getStepX(), k3, j3 * direction.getStepZ() + i3 * direction1.getStepZ());
                        this.level.setBlockAndUpdate(blockpos$mutableblockpos, blockstate1);
                    }
                }
            }
        }
        for (int l1 = -1; l1 < 3; ++l1) {
            for (int j2 = -1; j2 < 4; ++j2) {
                if (l1 == -1 || l1 == 2 || j2 == -1 || j2 == 3) {
                    blockpos$mutableblockpos.setWithOffset(blockpos, l1 * direction.getStepX(), j2, l1 * direction.getStepZ());
                    this.level.setBlock(blockpos$mutableblockpos, BlockInit.EROSION_DEEPSLATE_BRICKS.get().defaultBlockState(), 3);
                }
            }
        }
        BlockState blockstate = BlockInit.EROSION_PORTAL.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);
        for (int k2 = 0; k2 < 2; ++k2) {
            for (int l2 = 0; l2 < 3; ++l2) {
                blockpos$mutableblockpos.setWithOffset(blockpos, k2 * direction.getStepX(), l2, k2 * direction.getStepZ());
                this.level.setBlock(blockpos$mutableblockpos, blockstate, 18);
                this.level.getPoiManager().add(blockpos$mutableblockpos, poi);
            }
        }
        return Optional.of(new BlockUtil.FoundRectangle(blockpos.immutable(), 2, 3));
    }

    private boolean canHostFrame(BlockPos blockPos, BlockPos.MutableBlockPos mutableBlockPos, Direction facing, int offsetFactor) {
        Direction direction = facing.getClockWise();
        for (int i = -1; i < 3; ++i) {
            for (int j = -1; j < 4; ++j) {
                mutableBlockPos.setWithOffset(blockPos, facing.getStepX() * i + direction.getStepX() * offsetFactor, j, facing.getStepZ() * i + direction.getStepZ() * offsetFactor);
                if (j < 0 && !this.level.getBlockState(mutableBlockPos).getMaterial().isSolid()) {
                    return false;
                }
                if (j >= 0 && !this.canPortalReplaceBlock(mutableBlockPos)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel server, float yaw, Function<Boolean, Entity> repositionEntity) {
        PortalInfo portalinfo = getPortalInfo(entity, server);
        if (entity instanceof ServerPlayer player) {
            player.setLevel(server);
            server.addDuringPortalTeleport(player);
            player.connection.teleport(portalinfo.pos.x, portalinfo.pos.y, portalinfo.pos.z, portalinfo.yRot, portalinfo.xRot);
            player.connection.resetPosition();
            CriteriaTriggers.CHANGED_DIMENSION.trigger(player, currentWorld.dimension(), server.dimension());
            return entity;
        } else {
            Entity entityNew = entity.getType().create(server);
            if (entityNew != null) {
                entityNew.restoreFrom(entity);
                entityNew.moveTo(portalinfo.pos.x, portalinfo.pos.y, portalinfo.pos.z, portalinfo.yRot, entityNew.getXRot());
                entityNew.setDeltaMovement(portalinfo.speed);
                server.addDuringTeleport(entityNew);
            }
            return entityNew;
        }
    }

    private PortalInfo getPortalInfo(Entity entity, ServerLevel server) {
        WorldBorder worldborder = server.getWorldBorder();
        double d0 = DimensionType.getTeleportationScale(entity.level.dimensionType(), server.dimensionType());
        BlockPos blockpos1 = worldborder.clampToBounds(entity.getX() * d0, entity.getY(), entity.getZ() * d0);
        return this.getExitPortal(entity, blockpos1, worldborder).map(repositioner -> {
            BlockState blockstate = entity.level.getBlockState(this.entityEnterPos);
            Direction.Axis direction$axis;
            Vec3 vector3d;
            if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
                direction$axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                BlockUtil.FoundRectangle teleportationrepositioner$result = BlockUtil.getLargestRectangleAround(this.entityEnterPos, direction$axis, 21, Direction.Axis.Y, 21, pos -> entity.level.getBlockState(pos) == blockstate);
                vector3d = CuboidPortalShape.getRelativePosition(teleportationrepositioner$result, direction$axis, entity.position(), entity.getDimensions(entity.getPose()));
            } else {
                direction$axis = Direction.Axis.X;
                vector3d = new Vec3(0.5, 0, 0);
            }
            return CuboidPortalShape.createPortalInfo(server, repositioner, direction$axis, vector3d, entity, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
        }).orElse(new PortalInfo(entity.position(), Vec3.ZERO, entity.getYRot(), entity.getXRot()));
    }

    protected Optional<BlockUtil.FoundRectangle> getExitPortal(Entity entity, BlockPos pos, WorldBorder worldBorder) {
        Optional<BlockUtil.FoundRectangle> optional = this.findPortalAround(pos, false, worldBorder);
        if (entity instanceof ServerPlayer) {
            if (optional.isPresent()) {
                return optional;
            } else {
                Direction.Axis direction$axis = entity.level.getBlockState(this.entityEnterPos).getOptionalValue(NetherPortalBlock.AXIS).orElse(Direction.Axis.X);
                return this.createPortal(pos, direction$axis);
            }
        } else {
            return optional;
        }
    }

    private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
        BlockState blockstate = this.level.getBlockState(pos);
        return blockstate.getMaterial().isReplaceable() && blockstate.getFluidState().isEmpty();
    }
}

