package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.item.*;
import com.eeeab.eeeabsmobs.sever.item.ItemGhostWarriorArmor;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemCoreforgeRuinsEye;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemBloodyAltarEye;
import com.eeeab.eeeabsmobs.sever.item.util.ModSmithingTemplate;
import com.eeeab.eeeabsmobs.sever.item.util.ModToolsTier;
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

    //刷怪蛋
    public static final RegistryObject<Item> RELIC_OBSERVER_EGG = registerEgg("relic_observer_egg", EntityInit.RELIC_OBSERVER, rgb2dec(35, 35, 35), rgb2dec(113, 113, 113));
    public static final RegistryObject<Item> RELIC_RIPPER_EGG = registerEgg("relic_ripper_egg", EntityInit.RELIC_RIPPER, rgb2dec(126, 126, 126), rgb2dec(59, 59, 59));
    public static final RegistryObject<Item> RELIC_EARTHSHAKER_EGG = registerEgg("relic_earthshaker_egg", EntityInit.RELIC_EARTHSHAKER, rgb2dec(35, 35, 35), rgb2dec(43, 101, 131));
    public static final RegistryObject<Item> RELIC_ANNIHILATOR_EGG = registerEgg("relic_annihilator_egg", EntityInit.RELIC_ANNIHILATOR, rgb2dec(48, 48, 48), rgb2dec(59, 132, 167));
    public static final RegistryObject<Item> REALM_WARDEN_EGG = registerEgg("realm_warden_egg", EntityInit.REALM_WARDEN, rgb2dec(48, 48, 48), rgb2dec(162, 162, 162));
    public static final RegistryObject<Item> NAMELESS_GUARDIAN_EGG = registerEgg("nameless_guardian_egg", EntityInit.NAMELESS_GUARDIAN, rgb2dec(15, 15, 15), rgb2dec(100, 100, 105));

    public static final RegistryObject<Item> CORPSE_EGG = registerEgg("corpse_egg", EntityInit.CORPSE, rgb2dec(28, 47, 49), rgb2dec(72, 135, 115));
    public static final RegistryObject<Item> CORPSE_VILLAGER_EGG = registerEgg("corpse_villager_egg", EntityInit.CORPSE_VILLAGER, rgb2dec(28, 47, 49), rgb2dec(72, 129, 125));
    public static final RegistryObject<Item> CORPSE_WARLOCK_EGG = registerEgg("corpse_warlock_egg", EntityInit.CORPSE_WARLOCK, rgb2dec(49, 47, 50), rgb2dec(222, 16, 16));
    public static final RegistryObject<Item> TESTER_EGG = registerEgg("tester_egg", EntityInit.TESTER, rgb2dec(70, 145, 190), rgb2dec(95, 195, 255));

    public static final RegistryObject<Item> IMMORTAL_SKELETON_EGG = registerEgg("immortal_skeleton_egg", EntityInit.IMMORTAL_SKELETON, rgb2dec(147, 147, 143), rgb2dec(83, 83, 77));
    public static final RegistryObject<Item> IMMORTAL_ARCHER_EGG = registerEgg("immortal_archer_egg", EntityInit.IMMORTAL_ARCHER, rgb2dec(62, 53, 43), rgb2dec(131, 136, 135));
    public static final RegistryObject<Item> IMMORTAL_KNIGHT_EGG = registerEgg("immortal_knight_egg", EntityInit.IMMORTAL_KNIGHT, rgb2dec(30, 41, 47), rgb2dec(151, 155, 154));
    public static final RegistryObject<Item> IMMORTAL_MAGE_EGG = registerEgg("immortal_mage_egg", EntityInit.IMMORTAL_MAGE, rgb2dec(97, 108, 116), rgb2dec(46, 54, 59));
    public static final RegistryObject<Item> IMMORTAL_WARRIOR_EGG = registerEgg("immortal_warrior_egg", EntityInit.IMMORTAL_WARRIOR, rgb2dec(75, 81, 84), rgb2dec(151, 155, 154));
    public static final RegistryObject<Item> IMMORTAL_SHAMAN_EGG = registerEgg("immortal_shaman_egg", EntityInit.IMMORTAL_SHAMAN, rgb2dec(117, 143, 146), rgb2dec(56, 67, 69));
    public static final RegistryObject<Item> IMMORTAL_GOLEM_EGG = registerEgg("immortal_golem_egg", EntityInit.MAGIC_GOLEM, rgb2dec(150, 163, 143), rgb2dec(90, 97, 84));
    public static final RegistryObject<Item> IMMORTAL_EXECUTIONER_EGG = registerEgg("immortal_executioner_egg", EntityInit.IMMORTAL_EXECUTIONER, rgb2dec(32, 35, 36), rgb2dec(131, 151, 161));
    public static final RegistryObject<Item> IMMORTAL_BOSS_EGG = registerEgg("immortal_boss_egg", EntityInit.IMMORTAL_BOSS, rgb2dec(16, 37, 51), rgb2dec(116, 175, 216));

    //物品
    public static final RegistryObject<Item> LOGO_ITEM = ITEMS.register("logo_item", ItemLogo::new);
    public static final RegistryObject<Item> REMOVE_MOB = ITEMS.register("remove_mob", ItemRemoveMob::new);
    public static final RegistryObject<Item> ANIMATION_CONTROLLER = ITEMS.register("animation_controller", ItemAnimationController::new);
    public static final RegistryObject<Item> BLOODY_ALTAR_EYE = ITEMS.register("bloody_altar_eye", ItemBloodyAltarEye::new);
    public static final RegistryObject<Item> HEART_OF_PAGAN = ITEMS.register("heart_of_pagan", () -> new ItemHeartOfPagan(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> SOUL_SUMMON_NECKLACE = ITEMS.register("soul_summon_necklace", () -> new ItemSoulSummonNecklace(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> COREFORGE_RUINS_EYE = ITEMS.register("coreforge_ruins_eye", ItemCoreforgeRuinsEye::new);
    public static final RegistryObject<Item> ANCIENT_DRIVE_CRYSTAL = ITEMS.register("ancient_drive_crystal", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> BOUNDARY_BRICK = ITEMS.register("boundary_brick", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHAIN_GEAR = ITEMS.register("chain_gear", () -> new ItemSlidingDoorKey(new Item.Properties().rarity(Rarity.EPIC).fireResistant(), 1));
    public static final RegistryObject<Item> GUARDIAN_CUBE = ITEMS.register("guardian_cube", () -> new ItemSlidingDoorKey(new Item.Properties().rarity(Rarity.EPIC).fireResistant(), 2));
    public static final RegistryObject<Item> GUARDIAN_CORE = ITEMS.register("guardian_core", ItemGuardianCore::new);
    public static final RegistryObject<Item> DEMOLISHER = ITEMS.register("demolisher", () -> new ItemDemolisher(ModToolsTier.RELICRON_TIER, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<ItemBusterGauntlet> BUSTER_GAUNTLET = ITEMS.register("buster_gauntlet", () -> new ItemBusterGauntlet(ModToolsTier.RELICRON_TIER, new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<ItemChainsword> CHAINSWORD = ITEMS.register("chainsword", () -> new ItemChainsword(ModToolsTier.RELICRON_TIER, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<ItemSkyfallHammer> SKYFALL_HAMMER = ITEMS.register("skyfall_hammer", () -> new ItemSkyfallHammer(ModToolsTier.RELICRON_TIER, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<ItemDoomboltAxe> DOOMBOLT_AXE = ITEMS.register("doombolt_battleaxe", () -> new ItemDoomboltAxe(ModToolsTier.RELICRON_TIER, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<ItemGuardianAxe> GUARDIAN_AXE = ITEMS.register("guardian_axe", () -> new ItemGuardianAxe(ModToolsTier.RELICRON_TIER, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> RELICRON_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("relicron_upgrade_smithing_template", ModSmithingTemplate::createRelicronUpgradeTemplate);
    public static final RegistryObject<Item> IMMORTAL_BONE = ITEMS.register("immortal_bone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_STAFF = ITEMS.register("immortal_staff", ItemImmortalStaff::new);
    public static final RegistryObject<Item> IMMORTAL_DEBRIS = ITEMS.register("immortal_debris", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_INGOT = ITEMS.register("immortal_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GHOST_STEEL_INGOT = ITEMS.register("ghost_steel_ingot", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> GHOST_WARRIOR_HELMET = ITEMS.register("ghost_warrior_helmet", () -> new ItemGhostWarriorArmor(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> GHOST_WARRIOR_CHESTPLATE = ITEMS.register("ghost_warrior_chestplate", () -> new ItemGhostWarriorArmor(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> GHOST_WARRIOR_BOOTS = ITEMS.register("ghost_warrior_boots", () -> new ItemGhostWarriorArmor(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> GHOST_WARRIOR_LEGGINGS = ITEMS.register("ghost_warrior_leggings", () -> new ItemGhostWarriorArmor(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("ghost_warrior_upgrade_smithing_template", ModSmithingTemplate::createGhostWarriorUpgradeTemplate);
    public static final RegistryObject<Item> IMMORTAL_AXE = ITEMS.register("immortal_axe", () -> new AxeItem(ModToolsTier.IMMORTAL_TIER, 6F, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_SWORD = ITEMS.register("immortal_sword", () -> new ItemImmortalSword(ModToolsTier.IMMORTAL_TIER, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<ItemNetherworldKatana> THE_NETHERWORLD_KATANA = ITEMS.register("netherworld_katana", () -> new ItemNetherworldKatana(ModToolsTier.GHOST_STEEL_TIER, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    //唱片
    public static final RegistryObject<Item> GUARDIANS_MUSIC_DISC = ITEMS.register("nameless_guardian_theme_music_disc", () -> new RecordItem(8, SoundInit.NAMELESS_GUARDIAN_THEME, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant(), 3640));
    public static final RegistryObject<Item> STEEL_PSALM_MUSIC_DISC = ITEMS.register("realm_warden_theme_music_disc", () -> new RecordItem(8, SoundInit.REALM_WARDEN_THEME_DISC, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant(), 3700));
    public static final RegistryObject<Item> THE_ARMY_OF_MINOTAUR_MUSIC_DISC = ITEMS.register("the_immortal_theme_music_disc", () -> new RecordItem(8, SoundInit.THE_IMMORTAL_THEME, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant(), 3220));

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
        BUSTER_GAUNTLET.get().refreshAttributesFromConfig();
        CHAINSWORD.get().refreshAttributesFromConfig();
        DOOMBOLT_AXE.get().refreshAttributesFromConfig();
        GUARDIAN_AXE.get().refreshAttributesFromConfig();
        //THE_NETHERWORLD_KATANA.get().refreshAttributesFromConfig();
    }
}
