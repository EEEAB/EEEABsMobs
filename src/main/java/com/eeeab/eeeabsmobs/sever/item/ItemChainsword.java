package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.UUID;

public class ItemChainsword extends SwordItem implements ConfigurableItem, SlidingDoorLockKey {
    private static final UUID BASE_ENTITY_REACH_UUID = UUID.fromString("BAA2E9C8-632E-49AD-9A88-C7D0A4E34D2C");
    private final float damage;
    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemChainsword(Tier tier, Properties properties) {
        super(tier, (int) (-3D + ModConfigHandler.COMMON.items.chainsword.attackDamageValue), (float) (-4D + ModConfigHandler.COMMON.items.chainsword.attackSpeedValue), properties);
        this.damage = (float) ModConfigHandler.COMMON.items.chainsword.attackDamageValue;
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public float getDamage() {
        return damage;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        ModConfigHandler.Item items = ModConfigHandler.COMMON.items;
        tooltip.add(TranslateUtils.simpleItemText(this.getDescriptionId(), Component.literal("+" + items.chainswordConfig1.get() + "%"), Component.literal(items.chainswordConfig2.get() + "")));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> creatAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", ModConfigHandler.COMMON.items.chainsword.attackDamageValue - 1D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", ModConfigHandler.COMMON.items.chainsword.attackSpeedValue - 4D, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ENTITY_REACH_UUID, "Weapon modifier", 1D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public int getKeyLevel() {
        return 1;
    }
}
