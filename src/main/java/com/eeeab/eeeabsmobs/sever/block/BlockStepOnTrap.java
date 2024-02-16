package com.eeeab.eeeabsmobs.sever.block;

import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 踩踏触发陷阱方块抽象类
 */
public abstract class BlockStepOnTrap extends Block {
    protected static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public BlockStepOnTrap(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(OPEN, false));
    }

    /**
     * 当玩家踩在方块是调用此方法
     */
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        this.active(level, pos, state, entity);
    }

    /**
     * 执行块掉落的副作用 例如创建蠹虫
     */
    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        this.afterBreak(state, level, pos, stack);
    }

    /**
     * 当isRandomlyTicking()为true需要随机滴答
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (state.getValue(OPEN)) {
            level.setBlock(pos, state.setValue(OPEN, false), 3);
        }
    }

    /**
     * @param state 方块状态
     * @return 此块是否需要随机滴答
     */
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(OPEN);
    }

    /**
     * 在玩家附近的方块上定期调用客户端以显示效果（如熔炉火焰粒子）
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        super.animateTick(state, level, pos, randomSource);
        this.doActiveEffect(state, level, pos, randomSource);
    }

    /**
     * 初始化自定义方块状态
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    /**
     * 检查实体是否能触发陷阱
     */
    public static boolean checkStepOnEntity(Entity entity) {
        if (entity instanceof LivingEntity livingEntity && !livingEntity.getType().is(EMTagKey.TRAP_WHITELIST)) {
            if (livingEntity instanceof Player player) {
                return !(player.isCreative() || player.isSpectator());
            }
            return !(livingEntity instanceof ArmorStand);
        }
        return false;
    }

    protected void active(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!state.getValue(OPEN) && checkStepOnEntity(entity)) {
            level.setBlock(pos, state.setValue(OPEN, true), 3);
        }
    }

    protected void doActiveEffect(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {

    }

    protected void afterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack) {
    }
}
