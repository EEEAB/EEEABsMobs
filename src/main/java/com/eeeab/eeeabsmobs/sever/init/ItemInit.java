package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.item.*;
import com.eeeab.eeeabsmobs.sever.item.ItemGhostWarriorArmor;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemGulingEye;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemBloodyAltarEye;
import com.eeeab.eeeabsmobs.sever.item.util.EMSmithingTemplate;
import com.eeeab.eeeabsmobs.sever.item.util.EMToolsTier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EEEABMobs.MOD_ID);
    public static final int CORPSE_GREEN_COLOR = rgb2dec(28, 47, 49);

    //刷怪蛋
    public static final RegistryObject<Item> IMMORTAL_SKELETON_EGG = registerEgg("immortal_skeleton_egg", EntityInit.IMMORTAL_SKELETON, rgb2dec(147, 147, 143), rgb2dec(83, 83, 77));
    public static final RegistryObject<Item> IMMORTAL_ARCHER_EGG = registerEgg("immortal_archer_egg", EntityInit.IMMORTAL_ARCHER, rgb2dec(62, 53, 43), rgb2dec(131, 136, 135));
    public static final RegistryObject<Item> IMMORTAL_KNIGHT_EGG = registerEgg("immortal_knight_egg", EntityInit.IMMORTAL_KNIGHT, rgb2dec(30, 41, 47), rgb2dec(151, 155, 154));
    public static final RegistryObject<Item> IMMORTAL_MAGE_EGG = registerEgg("immortal_mage_egg", EntityInit.IMMORTAL_MAGE, rgb2dec(97, 108, 116), rgb2dec(46, 54, 59));
    public static final RegistryObject<Item> IMMORTAL_WARRIOR_EGG = registerEgg("immortal_warrior_egg", EntityInit.IMMORTAL_WARRIOR, rgb2dec(75, 81, 84), rgb2dec(151, 155, 154));
    public static final RegistryObject<Item> IMMORTAL_SHAMAN_EGG = registerEgg("immortal_shaman_egg", EntityInit.IMMORTAL_SHAMAN, rgb2dec(117, 143, 146), rgb2dec(56, 67, 69));
    public static final RegistryObject<Item> IMMORTAL_GOLEM_EGG = registerEgg("immortal_golem_egg", EntityInit.IMMORTAL_GOLEM, rgb2dec(150, 163, 143), rgb2dec(90, 97, 84));
    public static final RegistryObject<Item> IMMORTAL_EXECUTIONER_EGG = registerEgg("immortal_executioner_egg", EntityInit.IMMORTAL_EXECUTIONER, rgb2dec(32, 35, 36), rgb2dec(131, 151, 161));
    public static final RegistryObject<Item> IMMORTAL_BOSS_EGG = registerEgg("immortal_boss_egg", EntityInit.IMMORTAL_BOSS, rgb2dec(16, 37, 51), rgb2dec(116, 175, 216));
    public static final RegistryObject<Item> NAMELESS_GUARDIAN_EGG = registerEgg("nameless_guardian_egg", EntityInit.NAMELESS_GUARDIAN, rgb2dec(15, 15, 15), rgb2dec(100, 100, 105));
    public static final RegistryObject<Item> GULING_SENTINEL_EGG = registerEgg("guling_sentinel_egg", EntityInit.GULING_SENTINEL, rgb2dec(25, 25, 25), rgb2dec(110, 130, 160));
    public static final RegistryObject<Item> GULING_SENTINEL_HEAVY_EGG = registerEgg("guling_sentinel_heavy_egg", EntityInit.GULING_SENTINEL_HEAVY, rgb2dec(15, 15, 15), rgb2dec(100, 120, 150));
    public static final RegistryObject<Item> CORPSE_EGG = registerEgg("corpse_egg", EntityInit.CORPSE, CORPSE_GREEN_COLOR, rgb2dec(72, 135, 115));
    public static final RegistryObject<Item> CORPSE_VILLAGER_EGG = registerEgg("corpse_villager_egg", EntityInit.CORPSE_VILLAGER, CORPSE_GREEN_COLOR, rgb2dec(72, 129, 125));
    public static final RegistryObject<Item> CORPSE_WARLOCK_EGG = registerEgg("corpse_warlock_egg", EntityInit.CORPSE_WARLOCK, rgb2dec(49, 47, 50), rgb2dec(222, 16, 16));
    public static final RegistryObject<Item> TESTER_EGG = registerEgg("tester_egg", EntityInit.TESTER, rgb2dec(70, 145, 190), rgb2dec(95, 195, 255));
    //物品
    public static final RegistryObject<Item> LOGO_ITEM = ITEMS.register("logo_item", ItemLogo::new);
    public static final RegistryObject<Item> REMOVE_MOB = ITEMS.register("remove_mob", ItemRemoveMob::new);
    public static final RegistryObject<Item> IMMORTAL_BONE = ITEMS.register("immortal_bone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_STAFF = ITEMS.register("immortal_staff", ItemImmortalStaff::new);
    public static final RegistryObject<Item> IMMORTAL_DEBRIS = ITEMS.register("immortal_debris", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_TOMB_EYE = ITEMS.register("guling_eye", ItemGulingEye::new);
    public static final RegistryObject<Item> BLOODY_ALTAR_EYE = ITEMS.register("bloody_altar_eye", ItemBloodyAltarEye::new);
    public static final RegistryObject<Item> GUARDIAN_CORE = ITEMS.register("guardian_core", ItemGuardianCore::new);
    public static final RegistryObject<Item> ANIMATION_CONTROLLER = ITEMS.register("animation_controller", ItemAnimationController::new);
    public static final RegistryObject<Item> IMMORTAL_INGOT = ITEMS.register("immortal_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GHOST_STEEL_INGOT = ITEMS.register("ghost_steel_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> GHOST_WARRIOR_HELMET = ITEMS.register("ghost_warrior_helmet", () -> new ItemGhostWarriorArmor(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> GHOST_WARRIOR_CHESTPLATE = ITEMS.register("ghost_warrior_chestplate", () -> new ItemGhostWarriorArmor(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> GHOST_WARRIOR_BOOTS = ITEMS.register("ghost_warrior_boots", () -> new ItemGhostWarriorArmor(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> GHOST_WARRIOR_LEGGINGS = ITEMS.register("ghost_warrior_leggings", () -> new ItemGhostWarriorArmor(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("ghost_warrior_upgrade_smithing_template", EMSmithingTemplate::createGhostWarriorUpgradeTemplate);
    public static final RegistryObject<Item> IMMORTAL_AXE = ITEMS.register("immortal_axe", () -> new AxeItem(EMToolsTier.IMMORTAL_TIER, 6F, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_SWORD = ITEMS.register("immortal_sword", () -> new ItemImmortalSword(EMToolsTier.IMMORTAL_TIER, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<ItemGuardianAxe> GUARDIAN_AXE = ITEMS.register("guardian_axe", () -> new ItemGuardianAxe(EMToolsTier.GUARDIAN_AXE_TIER, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> GUARDIANS_MUSIC_DISC = ITEMS.register("guardians_music_disc", () -> new RecordItem(8, SoundInit.MONO_GUARDIANS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant(), 3640));
    public static final RegistryObject<Item> THE_ARMY_OF_MINOTAUR_MUSIC_DISC = ITEMS.register("the_army_of_minotaur_music_disc", () -> new RecordItem(8, SoundInit.MONO_THE_ARMY_OF_MINOTAUR, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant(), 3220));
    public static final RegistryObject<Item> HEART_OF_PAGAN = ITEMS.register("heart_of_pagan", () -> new ItemHeartOfPagan(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> SOUL_SUMMONING_NECKLACE = ITEMS.register("soul_summoning_necklace", () -> new ItemSoulSummoningNecklace(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<ItemNetherworldKatana> THE_NETHERWORLD_KATANA = ITEMS.register("netherworld_katana", () -> new ItemNetherworldKatana(EMToolsTier.NETHERWORLD_KATANA_TIER, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> ANCIENT_DRIVE_CRYSTAL = ITEMS.register("ancient_drive_crystal", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<ItemDemolisher> DEMOLISHER = ITEMS.register("demolisher", () -> new ItemDemolisher(EMToolsTier.DEMOLISHER_TIER, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant()));

    public static RegistryObject<Item> registerEgg(String name, RegistryObject<? extends EntityType<? extends Mob>> EggObject, int backgroundColor, int highlightColor) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(EggObject, backgroundColor, highlightColor, new Item.Properties()));
    }

    public static ItemStack findBlockItemToStack(RegistryObject<Block> block) {
        return block.get().asItem().getDefaultInstance();
    }

    private static int rgb2dec(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static void initializeAttributes() {
        GUARDIAN_AXE.get().refreshAttributesFromConfig();
        THE_NETHERWORLD_KATANA.get().refreshAttributesFromConfig();
        DEMOLISHER.get().refreshAttributesFromConfig();
    }
}
