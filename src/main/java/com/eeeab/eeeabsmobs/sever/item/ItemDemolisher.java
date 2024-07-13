package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.function.Predicate;

public class ItemDemolisher extends ProjectileWeaponItem {
    private static final Predicate<ItemStack> ONLY_STONE = stack -> stack.is(Tags.Items.STONE);

    public ItemDemolisher(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (capability != null) {
            boolean instabuild = !player.getAbilities().instabuild;
            //获取弹药类型 只要物品标签包含#stone都支持
            ItemStack projectile = player.getProjectile(player.getItemInHand(usedHand));
            if (projectile.isEmpty() && instabuild) {
                player.displayClientMessage(EMTUtils.simpleOtherText(this.getDescriptionId(), null), true);
                return InteractionResultHolder.fail(player.getItemInHand(usedHand));
            }
            AbilityHandler.INSTANCE.sendPlayerAbilityMessage(player, AbilityHandler.HOWITZER_ABILITY_TYPE);
            player.getCooldowns().addCooldown(this, EMConfigHandler.COMMON.ITEM.itemHowitzerCoolingTime.get() * 20);
            player.playSound(SoundEvents.WITHER_SHOOT);
            player.swing(usedHand, true);
            //判断是否需要消耗弹药
            if (instabuild) {
                projectile.shrink(1);
                if (projectile.isEmpty()) {
                    player.getInventory().removeItem(projectile);
                }
            }
            return ItemUtils.startUsingInstantly(level, player, usedHand);
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ONLY_STONE;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(EMTUtils.itemCoolTime(EMConfigHandler.COMMON.ITEM.itemHowitzerCoolingTime.get()));
        tooltip.add(EMTUtils.simpleWeaponText(this.getDescriptionId(), EMTUtils.STYLE_GRAY, EMConfigHandler.COMMON.ITEM.itemHowitzerCoolingTime.get()));
    }
}
