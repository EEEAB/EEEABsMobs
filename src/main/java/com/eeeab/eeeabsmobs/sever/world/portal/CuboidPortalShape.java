package com.eeeab.eeeabsmobs.sever.world.portal;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class CuboidPortalShape {
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;
    private static final BlockBehaviour.StatePredicate FRAME = (blockState, getter, blockPos) -> blockState.getBlock() == BlockInit.EROSION_DEEPSLATE_BRICKS.get();
    private static final float SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F;
    private static final double SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0D;
    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private final int width;

    public static Optional<CuboidPortalShape> findEmptyPortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Direction.Axis axis) {
        return findPortalShape(levelAccessor, blockPos, (cuboidPortalShape) -> cuboidPortalShape.isValid() && cuboidPortalShape.numPortalBlocks == 0, axis);
    }

    public static Optional<CuboidPortalShape> findPortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Predicate<CuboidPortalShape> predicate, Direction.Axis axis) {
        Optional<CuboidPortalShape> optional = Optional.of(new CuboidPortalShape(levelAccessor, blockPos, axis)).filter(predicate);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis direction$axis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new CuboidPortalShape(levelAccessor, blockPos, direction$axis)).filter(predicate);
        }
    }

    public CuboidPortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Direction.Axis axis) {
        this.level = levelAccessor;
        this.axis = axis;
        this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(blockPos);
        if (this.bottomLeft == null) {
            this.bottomLeft = blockPos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }
    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos blockPos) {
        for (int i = Math.max(this.level.getMinBuildHeight(), blockPos.getY() - 21); blockPos.getY() > i && isEmpty(this.level.getBlockState(blockPos.below())); blockPos = blockPos.below()) {
        }
        Direction direction = this.rightDir.getOpposite();
        int j = this.getDistanceUntilEdgeAboveFrame(blockPos, direction) - 1;
        return j < 0 ? null : blockPos.relative(direction, j);
    }

    private int calculateWidth() {
        int i = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return i >= MIN_WIDTH && i <= MAX_WIDTH ? i : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos blockPos, Direction direction) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= 21; ++i) {
            blockpos$mutableblockpos.set(blockPos).move(direction, i);
            BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos);
            if (!isEmpty(blockstate)) {
                if (FRAME.test(blockstate, this.level, blockpos$mutableblockpos)) {
                    return i;
                }
                break;
            }
            BlockState blockstate1 = this.level.getBlockState(blockpos$mutableblockpos.move(Direction.DOWN));
            if (!FRAME.test(blockstate1, this.level, blockpos$mutableblockpos)) {
                break;
            }
        }
        return 0;
    }


    private int calculateHeight() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.getDistanceUntilTop(blockpos$mutableblockpos);
        return i >= MIN_HEIGHT && i <= MAX_HEIGHT && this.hasTopFrame(blockpos$mutableblockpos, i) ? i : 0;
    }

    private boolean hasTopFrame(BlockPos.MutableBlockPos p_77731_, int p_77732_) {
        for (int i = 0; i < this.width; ++i) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = p_77731_.set(this.bottomLeft).move(Direction.UP, p_77732_).move(this.rightDir, i);
            if (!FRAME.test(this.level.getBlockState(blockpos$mutableblockpos), this.level, blockpos$mutableblockpos)) {
                return false;
            }
        }
        return true;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos mutableBlockPos) {
        for (int i = 0; i < 21; ++i) {
            mutableBlockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!FRAME.test(this.level.getBlockState(mutableBlockPos), this.level, mutableBlockPos)) {
                return i;
            }
            mutableBlockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!FRAME.test(this.level.getBlockState(mutableBlockPos), this.level, mutableBlockPos)) {
                return i;
            }
            for (int j = 0; j < this.width; ++j) {
                mutableBlockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockstate = this.level.getBlockState(mutableBlockPos);
                if (!isEmpty(blockstate)) {
                    return i;
                }
                if (blockstate.getBlock() == BlockInit.EROSION_PORTAL.get()) {
                    ++this.numPortalBlocks;
                }
            }
        }
        return 21;
    }

    private static boolean isEmpty(BlockState blockState) {
        return blockState.isAir() || blockState.getBlock() == BlockInit.EROSION_PORTAL.get();
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= MIN_WIDTH && this.width <= MAX_WIDTH && this.height >= MIN_HEIGHT && this.height <= MAX_HEIGHT;
    }

    public void createPortalBlocks() {
        BlockState blockstate = BlockInit.EROSION_PORTAL.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach((p_77725_) -> {
            this.level.setBlock(p_77725_, blockstate, 18);
            if (this.level instanceof ServerLevel)
                ((ServerLevel) this.level).getPoiManager().add(p_77725_, EMTeleporter.poi);
        });

    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public static Vec3 getRelativePosition(BlockUtil.FoundRectangle rectangle, Direction.Axis axis, Vec3 vec3, EntityDimensions dimensions) {
        double d0 = (double) rectangle.axis1Size - (double) dimensions.width;
        double d1 = (double) rectangle.axis2Size - (double) dimensions.height;
        BlockPos blockpos = rectangle.minCorner;
        double d2;
        if (d0 > 0.0D) {
            float f = (float) blockpos.get(axis) + dimensions.width / 2.0F;
            d2 = Mth.clamp(Mth.inverseLerp(vec3.get(axis) - (double) f, 0.0D, d0), 0.0D, SAFE_TRAVEL_MAX_VERTICAL_DELTA);
        } else {
            d2 = 0.5D;
        }
        double d4;
        if (d1 > 0.0D) {
            Direction.Axis direction$axis = Direction.Axis.Y;
            d4 = Mth.clamp(Mth.inverseLerp(vec3.get(direction$axis) - (double) blockpos.get(direction$axis), 0.0D, d1), 0.0D, SAFE_TRAVEL_MAX_VERTICAL_DELTA);
        } else {
            d4 = 0.0D;
        }
        Direction.Axis direction$axis1 = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double d3 = vec3.get(direction$axis1) - ((double) blockpos.get(direction$axis1) + 0.5D);
        return new Vec3(d2, d4, d3);
    }

    public static PortalInfo createPortalInfo(ServerLevel level, BlockUtil.FoundRectangle rectangle, Direction.Axis axis, Vec3 pos1, Entity entity, Vec3 pos2, float yRot, float xRot) {
        BlockPos blockpos = rectangle.minCorner;
        BlockState blockstate = level.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double d0 = rectangle.axis1Size;
        double d1 = rectangle.axis2Size;
        EntityDimensions entitydimensions = entity.getDimensions(entity.getPose());
        int i = axis == direction$axis ? 0 : 90;
        Vec3 vec3 = axis == direction$axis ? pos2 : new Vec3(pos2.z, pos2.y, -pos2.x);
        double d2 = (double) entitydimensions.width / 2.0D + (d0 - (double) entitydimensions.width) * pos1.x();
        double d3 = (d1 - (double) entitydimensions.height) * pos1.y();
        double d4 = 0.5D + pos1.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec31 = new Vec3((double) blockpos.getX() + (flag ? d2 : d4), (double) blockpos.getY() + d3, (double) blockpos.getZ() + (flag ? d4 : d2));
        Vec3 vec32 = findCollisionFreePosition(vec31, level, entity, entitydimensions);
        return new PortalInfo(vec32, vec3, yRot + (float) i, xRot);
    }

    private static Vec3 findCollisionFreePosition(Vec3 pos, ServerLevel level, Entity entity, EntityDimensions dimensions) {
        if (!(dimensions.width > SAFE_TRAVEL_MAX_ENTITY_XY) && !(dimensions.height > SAFE_TRAVEL_MAX_ENTITY_XY)) {
            double d0 = (double)dimensions.height / 2.0D;
            Vec3 vec3 = pos.add(0.0D, d0, 0.0D);
            VoxelShape voxelshape = Shapes.create(AABB.ofSize(vec3, dimensions.width, 0.0D, dimensions.width).expandTowards(0.0D, 1.0D, 0.0D).inflate(1.0E-6D));
            Optional<Vec3> optional = level.findFreePosition(entity, voxelshape, vec3, dimensions.width, dimensions.height, dimensions.width);
            Optional<Vec3> optional1 = optional.map((p_259019_) -> p_259019_.subtract(0.0D, d0, 0.0D));
            return optional1.orElse(pos);
        } else {
            return pos;
        }
    }
}
