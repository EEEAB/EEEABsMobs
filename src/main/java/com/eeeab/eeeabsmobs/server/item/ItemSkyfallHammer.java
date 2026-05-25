package com.eeeab.eeeabsmobs.server.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class ItemSkyfallHammer extends PickaxeItem {
    public ItemSkyfallHammer(Tier tier, Properties properties) {
        super(tier, 8, -2.8F, properties);
    }

    //@Override
    //public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    //    ItemStack stack = player.getMainHandItem();
    //    if (!world.isClientSide && stack.is(this)) {
    //        if (SkyfallHammerAbility.canPlayerUseAbility(player)) {
    //            AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.SKYFALL_HAMMER_ABILITY);
    //            player.getCooldowns().addCooldown(this, 20);
    //            return InteractionResultHolder.success(stack);
    //        }
    //    }
    //    return InteractionResultHolder.pass(player.getItemInHand(hand));
    //}

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return super.canPerformAction(stack, toolAction) || net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }
}
