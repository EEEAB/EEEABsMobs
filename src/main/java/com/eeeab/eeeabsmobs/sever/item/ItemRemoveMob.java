package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.entity.effect.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemRemoveMob extends Item {
    private static final double FIND_SIZE = 16;

    public ItemRemoveMob() {
        super(new Properties().stacksTo(1));
    }

    //右键删除16*16*16范围的模组实体
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (!player.level().isClientSide) {
            for (Entity entity : getNearByEntities(player)) {
                if (entity instanceof IEntity || entity instanceof IMob) entity.remove(Entity.RemovalReason.KILLED);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.swing(hand, true);
            player.getCooldowns().addCooldown(stack.getItem(), 40);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    //左键清除单个模组实体
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof IEntity || entity instanceof IMob) {
            if (!entity.level().isClientSide) {
                entity.remove(Entity.RemovalReason.KILLED);
            }
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.addAll(TranslateUtils.complexText(TranslateUtils.ITEM_PREFIX, 2, ChatFormatting.GRAY, this.getDescriptionId()));
    }

    private static List<Entity> getNearByEntities(Entity entity) {
        return entity.level().getEntitiesOfClass(Entity.class, entity.getBoundingBox().inflate(ItemRemoveMob.FIND_SIZE), targetEntity -> targetEntity != entity);
    }
}
