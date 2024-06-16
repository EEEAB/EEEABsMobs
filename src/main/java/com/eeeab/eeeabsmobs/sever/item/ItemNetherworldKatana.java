package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;
import java.util.function.Consumer;

public class ItemNetherworldKatana extends SwordItem implements ConfigurableItem {
    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private static final UUID NETHERWORLD_KATANA_BASE_ENTITY_REACH_UUID = UUID.fromString("7293EB63-DAD9-4B73-A591-C0EFA6F10647");

    public ItemNetherworldKatana(Tier tier, Properties properties) {
        super(tier, (int) (-3D + EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackDamageValue), (float) (-4D + EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackSpeedValue), properties);
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> creatAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackDamageValue - 1D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackSpeedValue - 4D, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(NETHERWORLD_KATANA_BASE_ENTITY_REACH_UUID, "Weapon modifier", 1.5D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = this.creatAttributesFromConfig();
    }
}
