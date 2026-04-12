package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.ability.abilities.SkyfallHammerAbility;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

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
