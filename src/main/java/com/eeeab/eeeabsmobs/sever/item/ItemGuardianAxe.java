package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class ItemGuardianAxe extends AxeItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private static final UUID GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("BFF48EEA-FF5B-45B6-88FC-3C8FBBAF78FA");

    public ItemGuardianAxe(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamageModifier + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedModifier, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 0.1D, AttributeModifier.Operation.ADDITION));
        defaultModifiers = builder.build();
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

    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(Component.translatable("item.unable_depleted_tip").setStyle(ItemInit.TIPS_GRAY));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }
}
