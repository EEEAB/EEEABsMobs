package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemGuardianAxe extends AxeItem implements ConfigurableItem {
    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private static final UUID GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("BFF48EEA-FF5B-45B6-88FC-3C8FBBAF78FA");

    public ItemGuardianAxe(Tier tier, Properties properties) {
        super(tier, (float) (-3D + EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackDamageValue), (float) (-4D + EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackSpeedValue), properties);
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (Objects.equals(hand, InteractionHand.MAIN_HAND) && HitResult.Type.BLOCK == result.getType() && Direction.UP == result.getDirection()) {
            player.swing(hand, true);
            AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (capability != null) {
                player.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1F + player.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(level, player.position(), 8, 0.125F, 0, 20);
                if (!level.isClientSide) AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.GUARDIAN_AXE_ABILITY_TYPE);
                player.getCooldowns().addCooldown(this, (int) (EMConfigHandler.COMMON.ITEM.itemGuardianAxeCoolingTime.get() * 20));
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity hitEntity, LivingEntity livingEntity) {
        if (!hitEntity.level().isClientSide) {
            hitEntity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1F, 0.2F);
        }
        return super.hurtEnemy(itemStack, hitEntity, livingEntity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(EMTUtils.UNABLE_BREAKS);
        if (EMTUtils.SHOW_ITEM_CD) tooltip.add(EMTUtils.itemCoolTime(EMConfigHandler.COMMON.ITEM.itemGuardianAxeCoolingTime.get()));
        int i = (int) (SweepingEdgeEnchantment.getSweepingDamageRatio(EMConfigHandler.COMMON.ITEM.itemGuardianAxeSweepingLevel.get()) * 100);
        tooltip.addAll(EMTUtils.complexText(EMTUtils.ITEM_PREFIX, 2, ChatFormatting.GRAY, this.getDescriptionId(), Component.literal(i > 0 ? i + "%" : "1.0").withStyle(ChatFormatting.YELLOW)));
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        if (Enchantments.SWEEPING_EDGE.equals(enchantment)) {
            return EMConfigHandler.COMMON.ITEM.itemGuardianAxeSweepingLevel.get();
        }
        return super.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.SWORD_SWEEP == toolAction || super.canPerformAction(stack, toolAction);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> creatAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackDamageValue - 1D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackSpeedValue - 4D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 0.1D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = this.creatAttributesFromConfig();
    }
}
