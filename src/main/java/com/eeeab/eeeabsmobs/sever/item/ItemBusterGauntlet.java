package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.animate.client.util.ItemAnimationUtils;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;

public class ItemBusterGauntlet extends SwordItem implements ConfigurableItem {
    private final Multimap<Attribute, AttributeModifier> offHandModifiers;
    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemBusterGauntlet(Tier tier, Properties properties) {
        super(tier, (int) (-3D + ModConfigHandler.COMMON.items.busterGauntlet.attackDamageValue), (float) (-4D + ModConfigHandler.COMMON.items.busterGauntlet.attackSpeedValue), properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier("E34F88B5-C372-4D99-B4CF-E75E80750BFA", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        offHandModifiers = builder.build();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        //InteractionHand anotherHand = usedHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        //ItemStack anotherStack = player.getItemInHand(anotherHand);
        //if (anotherStack.canPerformAction(ToolActions.SHIELD_BLOCK) && !player.getCooldowns().isOnCooldown(anotherStack.getItem())) {
        //    return InteractionResultHolder.fail(player.getItemInHand(usedHand));
        //}
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        }
        AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (capability != null) {
            player.getCooldowns().addCooldown(this, (int) (ModConfigHandler.COMMON.items.busterGauntletConfig3.get() * 20));
            player.startUsingItem(usedHand);
            AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.BUSTER_GAUNTLET_ABILITY);
            if (level.isClientSide) ItemAnimationUtils.start(player.getItemInHand(usedHand), player.tickCount);
            itemstack.hurtAndBreak(1, player, (event) -> event.broadcastBreakEvent(usedHand));
            return InteractionResultHolder.consume(player.getItemInHand(usedHand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
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
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        if (TranslateUtils.SHOW_ITEM_CD) tooltip.add(TranslateUtils.itemCoolTime(ModConfigHandler.COMMON.items.busterGauntletConfig3.get()));
        tooltip.add(TranslateUtils.simpleItemText(this.getDescriptionId(), ModConfigHandler.COMMON.items.busterGauntletConfig1.get()));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.OFFHAND) return offHandModifiers;
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> creatAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", ModConfigHandler.COMMON.items.busterGauntlet.attackDamageValue - 1D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", ModConfigHandler.COMMON.items.busterGauntlet.attackSpeedValue - 4D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = creatAttributesFromConfig();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }
}
