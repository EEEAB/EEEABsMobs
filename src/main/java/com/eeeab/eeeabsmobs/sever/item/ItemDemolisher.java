package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.animate.client.util.ItemAnimationUtils;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemDemolisher extends SwordItem implements ConfigurableItem {
    private static final Predicate<ItemStack> SUPPORTED_PROJECTILES_PREDICATE = stack -> stack.is(EMTagKey.DEMOLISHER_SUPPORTED_PROJECTILES);
    private static final Component MELEE_MODE = EMTUtils.simpleText(EMTUtils.OTHER_PREFIX, "melee_mode", ChatFormatting.YELLOW);
    private static final Component RANGED_MODE = EMTUtils.simpleText(EMTUtils.OTHER_PREFIX, "ranged_mode", ChatFormatting.YELLOW);
    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemDemolisher(Tier tier, Properties properties) {
        super(tier, (int) (-3D + EMConfigHandler.COMMON.ITEM.DEMOLISHER_TOOL.attackDamageValue), (float) (-4D + EMConfigHandler.COMMON.ITEM.DEMOLISHER_TOOL.attackSpeedValue), properties);
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        boolean shiftFlag = player.isShiftKeyDown();
        if (shiftFlag) {
            player.swing(usedHand, true);
            changeWeaponState(itemStack);
            return InteractionResultHolder.pass(itemStack);
        } else if (getWeaponState(itemStack) == 0) {
            return InteractionResultHolder.fail(itemStack);
        }
        ItemStack projectile = getProjectile(itemStack, player);
        if (projectile.isEmpty() && !player.getAbilities().instabuild) {
            player.displayClientMessage(EMTUtils.simpleOtherText(this.getDescriptionId(), null), true);
            return InteractionResultHolder.fail(itemStack);
        }
        return ItemUtils.startUsingInstantly(level, player, usedHand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainingUseDuration) {
        if (livingEntity instanceof Player player) {
            AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (capability != null) {
                boolean instabuild = !player.getAbilities().instabuild;
                ItemStack projectile = getProjectile(itemStack, player);
                if (player.getCooldowns().isOnCooldown(this)) {
                    return;
                } else if ((projectile.isEmpty() && instabuild)) {
                    player.displayClientMessage(EMTUtils.simpleOtherText(this.getDescriptionId(), null), true);
                    player.stopUsingItem();
                    return;
                }
                player.getCooldowns().addCooldown(this, (int) (EMConfigHandler.COMMON.ITEM.itemHowitzerCoolingTime.get() * 20));
                if (!level.isClientSide) AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.HOWITZER_ABILITY_TYPE);
                else ItemAnimationUtils.start(itemStack, player.tickCount);
                //判断是否需要消耗弹药
                if (instabuild) {
                    projectile.shrink(1);
                    if (projectile.isEmpty()) {
                        player.getInventory().removeItem(projectile);
                    }
                }
                player.stopUsingItem();
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(EMTUtils.UNABLE_BREAKS);
        boolean flag = getWeaponState(stack) == 1;
        Component component = EMTUtils.simpleText(EMTUtils.ITEM_PREFIX, this.getDescriptionId(), ChatFormatting.GRAY, EMTUtils.TIP_SUFFIX, flag ? RANGED_MODE : MELEE_MODE);
        if (flag) {
            tooltip.add(EMTUtils.itemCoolTime(EMConfigHandler.COMMON.ITEM.itemHowitzerCoolingTime.get()));
            tooltip.add(component);
            tooltip.add(EMTUtils.simpleText(EMTUtils.ITEM_PREFIX, this.getDescriptionId(), ChatFormatting.GRAY, EMTUtils.TIP_SUFFIX + "_1", EMConfigHandler.COMMON.ITEM.itemHowitzerGrenadeDamage.get()));
        } else {
            tooltip.add(component);
            tooltip.add(EMTUtils.simpleText(EMTUtils.ITEM_PREFIX, this.getDescriptionId(), ChatFormatting.GRAY, EMTUtils.TIP_SUFFIX + "_2"));
        }
        tooltip.add(EMTUtils.simpleText(EMTUtils.ITEM_PREFIX, this.getDescriptionId(), ChatFormatting.GRAY, EMTUtils.TIP_SUFFIX + "_3"));
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.SWORD_DIG == toolAction;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> creatAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.DEMOLISHER_TOOL.attackDamageValue - 1D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.DEMOLISHER_TOOL.attackSpeedValue - 4D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = creatAttributesFromConfig();
    }

    /**
     * @return 获取玩家库存中的弹药:含有#demolisher_supported_projectiles标签的物品
     */
    public static ItemStack getProjectile(ItemStack itemStack, Player player) {
        ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(player, SUPPORTED_PROJECTILES_PREDICATE);
        if (!itemstack.isEmpty()) {
            return net.minecraftforge.common.ForgeHooks.getProjectile(player, itemStack, itemstack);
        } else {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemStackIn = inventory.getItem(i);
                if (SUPPORTED_PROJECTILES_PREDICATE.test(itemStackIn)) {
                    return net.minecraftforge.common.ForgeHooks.getProjectile(player, itemStack, itemStackIn);
                }
            }
            return ItemStack.EMPTY;
        }
    }

    /**
     * 获取当前武器状态
     *
     * @return 0=近战(默认) 1=远程
     */
    public static int getWeaponState(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt("demolisherState") : 0;
    }

    public static void changeWeaponState(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("demolisherState", tag.getInt("demolisherState") == 0 ? 1 : 0);
    }
}
