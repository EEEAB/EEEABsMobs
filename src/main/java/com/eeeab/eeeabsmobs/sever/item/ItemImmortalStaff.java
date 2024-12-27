package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemImmortalStaff extends Item {
    public ItemImmortalStaff() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant());
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (entity instanceof Player player) {
            InteractionHand hand = player.getUsedItemHand();
            AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (player.getCooldowns().isOnCooldown(this)) {
                return;
            } else if (capability != null) {
                player.getCooldowns().addCooldown(this, (int) (EMConfigHandler.COMMON.ITEM.itemImmortalStaffCoolingTime.get() * 20));
                if (!level.isClientSide) AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.IMMORTAL_STAFF_ABILITY_TYPE);
            }
            player.swing(hand, true);
        }
    }

    @Override
    @NotNull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        EMConfigHandler.Item item = EMConfigHandler.COMMON.ITEM;
        tooltip.add(EMTUtils.itemCoolTime(item.itemImmortalStaffCoolingTime.get()));
        tooltip.add(EMTUtils.simpleItemText(this.getDescriptionId()));
    }
}
