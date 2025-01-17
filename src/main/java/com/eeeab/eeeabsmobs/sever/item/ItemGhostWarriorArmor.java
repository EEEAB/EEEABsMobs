package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.item.util.EMArmorMaterial;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ItemGhostWarriorArmor extends ArmorItem implements IUnbreakableItem {
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
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (slot == EquipmentSlot.LEGS) {
            return LEGS_LOCATION.toString();
        }
        return LOCATION.toString();
    }

    @Override
    public boolean canBreakItem() {
        return EMConfigHandler.COMMON.ITEM.enableGhostWarriorSeriesItemDurability.get();
    }
}
