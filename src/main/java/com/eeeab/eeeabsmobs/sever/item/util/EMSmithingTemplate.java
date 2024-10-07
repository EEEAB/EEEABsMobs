package com.eeeab.eeeabsmobs.sever.item.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class EMSmithingTemplate {
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    private static final ResourceLocation EMPTY_SLOT_INGOT = new ResourceLocation("item/empty_slot_ingot");
    private static final ResourceLocation EMPTY_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_HOE = new ResourceLocation("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_AXE = new ResourceLocation("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = new ResourceLocation("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = new ResourceLocation("item/empty_slot_shovel");
    private static final ResourceLocation EMPTY_SLOT_PICKAXE = new ResourceLocation("item/empty_slot_pickaxe");
    private static final Component GHOST_WARRIOR_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", new ResourceLocation(EEEABMobs.MOD_ID, "ghost_warrior_upgrade"))).withStyle(TITLE_FORMAT);
    private static final Component GHOST_WARRIOR_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(EEEABMobs.MOD_ID, "smithing_template.ghost_warrior_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component GHOST_WARRIOR_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(EEEABMobs.MOD_ID, "smithing_template.ghost_warrior_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component GHOST_WARRIOR_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(EEEABMobs.MOD_ID, "smithing_template.ghost_warrior_upgrade.base_slot_description")));
    private static final Component GHOST_WARRIOR_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(EEEABMobs.MOD_ID, "smithing_template.ghost_warrior_upgrade.additions_slot_description")));


    public static SmithingTemplateItem createGhostWarriorUpgradeTemplate() {
        return new SmithingTemplateItem(GHOST_WARRIOR_UPGRADE_APPLIES_TO, GHOST_WARRIOR_UPGRADE_INGREDIENTS, GHOST_WARRIOR_UPGRADE, GHOST_WARRIOR_UPGRADE_BASE_SLOT_DESCRIPTION, GHOST_WARRIOR_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createGhostWarriorUpgradeIconList(), List.of(EMPTY_SLOT_INGOT));
    }

    private static List<ResourceLocation> createGhostWarriorUpgradeIconList() {
        return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS);
    }
}
