package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.sever.entity.block.EntitySlidingDoorBlock;
import com.eeeab.eeeabsmobs.sever.init.BlockEntityInit;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.item.SlidingDoorLockKey;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockSlidingDoor extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final IntegerProperty Y_OFFSET = IntegerProperty.create("y_offset", 0, 2);
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 2);
    private static final VoxelShape FULL_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    private static final int DOOR_SIZE = 3;
    //private static final int AUTO_CLOSE_TICKS = 60;

    public BlockSlidingDoor() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .noOcclusion()
                .dynamicShape()
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .lightLevel(value -> 10)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
        registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
                .setValue(LOCKED, true)
                .setValue(PART, Part.CENTER)
                .setValue(Y_OFFSET, 0)
                .setValue(LEVEL, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, LOCKED, PART, Y_OFFSET, LEVEL);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FULL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(LIT) ? Shapes.empty() : FULL_SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EntitySlidingDoorBlock(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.SLIDING_DOOR_BE.get(), EntitySlidingDoorBlock::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos basePos = getBasePos(state, pos);
        BlockState baseState = world.getBlockState(basePos);
        if (!baseState.is(this)) return InteractionResult.PASS;

        if (baseState.getValue(LOCKED)) {
            ItemStack held = player.getItemInHand(hand);
            int level = baseState.getValue(LEVEL);
            if (held.getItem() instanceof SlidingDoorLockKey key) {
                if (key.getKeyLevel() < level) {
                    doSendPopupToPlayer(world, player, level);
                    return InteractionResult.PASS;
                }
                if (!world.isClientSide) {
                    setOpenState(world, basePos, true);
                    world.setBlock(basePos, baseState.setValue(LOCKED, false).setValue(LIT, true), 2);
                    world.blockEvent(basePos, this, 1, 0);
                    //world.scheduleTick(basePos, this, AUTO_CLOSE_TICKS);
                    world.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.25F + 0.55F);
                }
                return InteractionResult.sidedSuccess(world.isClientSide);
            } else doSendPopupToPlayer(world, player, level);
            return InteractionResult.PASS;
        }
        if (!world.isClientSide) {
            boolean open = !baseState.getValue(LIT);
            setOpenState(world, basePos, open);
            world.setBlock(basePos, baseState.setValue(LIT, open), 2);
            world.blockEvent(basePos, this, open ? 1 : 2, 0);
            if (open) {
                //world.scheduleTick(basePos, this, AUTO_CLOSE_TICKS);
                world.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.25F + 0.55F);
            } else {
                world.playSound(null, basePos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.25F + 0.55F);
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    //@Override
    //public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    //    if (state.is(this) && state.getValue(LIT) && !state.getValue(LOCKED)) {
    //        setOpenState(level, pos, false);
    //        level.setBlock(pos, state.setValue(LIT, false), 2);
    //        level.blockEvent(pos, this, 2, 0);
    //        level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.25F + 0.55F);
    //    }
    //}

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (checkSpace(pos, facing, level)) {
            int lockLevel = 1;
            Player player = context.getPlayer();
            if (player != null) {
                if (player.getOffhandItem().getItem() instanceof SlidingDoorLockKey key) {
                    lockLevel = key.getKeyLevel();
                }
            }
            return defaultBlockState().setValue(FACING, facing).setValue(LEVEL, lockLevel);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            int lockLevel = state.getValue(LEVEL);
            for (int y = 0; y < DOOR_SIZE; y++) {
                BlockPos rowPos = pos.above(y);
                for (int x = -1; x <= 1; x++) {
                    BlockPos target = getOffsetPos(rowPos, facing, x);
                    Part part = Part.fromX(x);
                    if (!target.equals(pos) || y != 0) {
                        level.setBlock(target, state.setValue(PART, part).setValue(Y_OFFSET, y).setValue(LEVEL, lockLevel), 3);
                    }
                }
            }
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BlockPos basePos = getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(this)) {
                Direction facing = baseState.getValue(FACING);
                for (int y = 0; y < DOOR_SIZE; y++) {
                    BlockPos rowPos = basePos.above(y);
                    for (int x = -1; x <= 1; x++) {
                        BlockPos target = getOffsetPos(rowPos, facing, x);
                        BlockState bs = level.getBlockState(target);
                        if (bs.is(this)) {
                            level.setBlock(target, Blocks.AIR.defaultBlockState(), 35);
                            level.levelEvent(player, 2001, target, Block.getId(bs));
                        }
                    }
                }
                level.setBlock(basePos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, basePos, Block.getId(baseState));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * 获取相对于中心基座的位置偏移
     *
     * @param xOffset -1左列，0中列，1右列
     */
    private BlockPos getOffsetPos(BlockPos rowPos, Direction facing, int xOffset) {
        if (xOffset == -1) {
            return rowPos.relative(facing.getCounterClockWise());
        } else if (xOffset == 1) {
            return rowPos.relative(facing.getClockWise());
        } else {
            return rowPos;
        }
    }

    private BlockPos getBasePos(BlockState state, BlockPos pos) {
        Part part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        int yOffset = state.getValue(Y_OFFSET);
        BlockPos base = pos.below(yOffset);
        if (part == Part.LEFT) {
            base = base.relative(facing.getCounterClockWise().getOpposite());
        } else if (part == Part.RIGHT) {
            base = base.relative(facing.getClockWise().getOpposite());
        }
        return base;
    }

    private boolean checkSpace(BlockPos basePos, Direction facing, Level level) {
        for (int y = 0; y < DOOR_SIZE; y++) {
            BlockPos rowPos = basePos.above(y);
            for (int x = -1; x <= 1; x++) {
                BlockPos target = getOffsetPos(rowPos, facing, x);
                if (!level.getBlockState(target).canBeReplaced()) return false;
            }
        }
        return true;
    }

    private void setOpenState(Level level, BlockPos basePos, boolean open) {
        BlockState baseState = level.getBlockState(basePos);
        Direction facing = baseState.getValue(FACING);
        for (int y = 0; y < DOOR_SIZE; y++) {
            BlockPos rowPos = basePos.above(y);
            for (int x = -1; x <= 1; x++) {
                BlockPos target = getOffsetPos(rowPos, facing, x);
                BlockState targetState = level.getBlockState(target);
                if (targetState.is(this)) {
                    level.setBlock(target, targetState.setValue(LIT, open), 2);
                }
            }
        }
    }

    private static void doSendPopupToPlayer(Level world, Player player, int level) {
        if (world.isClientSide && !player.isCreative() && !player.isSpectator()) {
            ResourceLocation id = BlockInit.SLIDING_DOOR.getId();
            player.displayClientMessage(TranslateUtils.simpleText(TranslateUtils.BLOCK_PREFIX, id.getPath() + "_level" + level, ChatFormatting.WHITE), true);
        }
    }

    public enum Part implements StringRepresentable {
        LEFT("left"),
        CENTER("center"),
        RIGHT("right");

        private final String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        public static Part fromX(int x) {
            if (x == -1) return LEFT;
            if (x == 1) return RIGHT;
            return CENTER;
        }
    }
}