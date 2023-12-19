package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.util.MTUtil;
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
    private static final double findSize = 16;

    public ItemRemoveMob() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    //右键删除16*16*16范围的模组实体
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (!player.level().isClientSide) {
            List<Entity> entities = getNearByEntities(player, findSize);
            for (Entity mobEntity : entities) {
                if (mobEntity instanceof IEntity) mobEntity.remove(Entity.RemovalReason.KILLED);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.swing(hand, true);
            player.getCooldowns().addCooldown(stack.getItem(), 40);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    //左键清楚单个模组实体
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof IEntity) {
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
        tooltip.addAll(MTUtil.complexText(MTUtil.ITEM_PREFIX, 2, MTUtil.STYLE_GRAY, "remove_mob"));
    }

    private static List<Entity> getNearByEntities(Entity entity, double size) {
        return entity.level().getEntitiesOfClass(Entity.class, entity.getBoundingBox().inflate(size, size, size), targetEntity -> targetEntity != entity &&
                entity.distanceTo(targetEntity) <= size + targetEntity.getBbWidth() / 2f && targetEntity.getY() <= entity.getY() + size);
    }
}
