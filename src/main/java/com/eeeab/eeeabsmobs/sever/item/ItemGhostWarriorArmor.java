package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.item.util.EMArmorMaterial;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;

public class ItemGhostWarriorArmor extends ArmorItem {
    public static final ResourceLocation LOCATION = new ResourceLocation(EEEABMobs.MOD_ID, "textures/armor/ghost_warrior_armor.png");
    public static final ResourceLocation LEGS_LOCATION = new ResourceLocation(EEEABMobs.MOD_ID, "textures/armor/ghost_warrior_armor_legs.png");

    public ItemGhostWarriorArmor(Type type) {
        super(EMArmorMaterial.GHOST_WARRIOR_MATERIAL, type, new Item.Properties().rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getASTEProperties());
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        stack.getOrCreateTag();
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (slot == EquipmentSlot.LEGS) {
            return LEGS_LOCATION.toString();
        }
        return LOCATION.toString();
    }

    @Override
    public boolean canBeDepleted() {
        return EMConfigHandler.COMMON.ITEM.enableGhostWarriorSeriesItemDurability.get() && super.canBeDepleted();
    }


    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        if (!EMConfigHandler.COMMON.ITEM.enableGhostWarriorSeriesItemDurability.get()) tooltip.add(EMTUtils.UNABLE_BREAKS);
    }
}
