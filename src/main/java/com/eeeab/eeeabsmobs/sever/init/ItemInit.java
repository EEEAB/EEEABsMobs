package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.item.*;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemGulingEye;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemBloodyAltarEye;
import com.eeeab.eeeabsmobs.sever.item.util.EMToolsTier;
import com.eeeab.eeeabsmobs.sever.util.EMTabGroup;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
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
    public static final int IMMORTAL_BACK_COLOR = rgb2dec(26, 26, 26);
    public static final int CORPSE_GREEN_COLOR = rgb2dec(28, 47, 49);
    //物品
    public static final RegistryObject<Item> LOGO_ITEM = ITEMS.register("logo_item", ItemLogo::new);
    public static final RegistryObject<Item> REMOVE_MOB = ITEMS.register("remove_mob", () -> new ItemRemoveMob(new Item.Properties().tab(EMTabGroup.TABS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> IMMORTAL_BONE = ITEMS.register("immortal_bone", () -> new ItemImmortalBone(new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_DEBRIS = ITEMS.register("immortal_debris", () -> new ItemImmortalDebris(new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_INGOT = ITEMS.register("immortal_ingot", () -> new Item(new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> BLOODY_ALTAR_EYE = ITEMS.register("bloody_altar_eye", () -> new ItemBloodyAltarEye(new Item.Properties().tab(EMTabGroup.TABS).rarity(Rarity.RARE).stacksTo(16).fireResistant()));
    public static final RegistryObject<Item> GULING_EYE = ITEMS.register("guling_eye", () -> new ItemGulingEye(new Item.Properties().tab(EMTabGroup.TABS).rarity(Rarity.RARE).stacksTo(16).fireResistant()));
    public static final RegistryObject<Item> HEART_OF_PAGAN = ITEMS.register("heart_of_pagan", () -> new Item(new Item.Properties().tab(EMTabGroup.TABS).stacksTo(1).fireResistant()));
    //防具
    public static final RegistryObject<Item> IMMORTAL_HELMET = ITEMS.register("immortal_helmet", () -> new ItemArmorImmortal(EquipmentSlot.HEAD, new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_CHEST_PLATE = ITEMS.register("immortal_chest_plate", () -> new ItemArmorImmortal(EquipmentSlot.CHEST, new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_BOOTS = ITEMS.register("immortal_boots", () -> new ItemArmorImmortal(EquipmentSlot.FEET, new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_LEGGINGS = ITEMS.register("immortal_leggings", () -> new ItemArmorImmortal(EquipmentSlot.LEGS, new Item.Properties().tab(EMTabGroup.TABS)));
    //武器
    public static final RegistryObject<Item> IMMORTAL_AXE = ITEMS.register("immortal_axe", () -> new ItemImmortalAxe(EMToolsTier.IMMORTAL_TIER, 6F, -2.8F, new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_SWORD = ITEMS.register("immortal_sword", () -> new ItemImmortalSword(EMToolsTier.IMMORTAL_TIER, 5, -2.4F, new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_STAFF = ITEMS.register("immortal_staff", () -> new ItemImmortalStaff(new Item.Properties().tab(EMTabGroup.TABS)));
    public static final RegistryObject<ItemGuardianAxe> GUARDIAN_AXE = ITEMS.register("guardian_axe", () -> new ItemGuardianAxe(EMToolsTier.GUARDIAN_AXE_TIER, new Item.Properties().tab(EMTabGroup.TABS).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> GUARDIAN_CORE = ITEMS.register("guardian_core", () -> new ItemGuardianCore(new Item.Properties().tab(EMTabGroup.TABS).rarity(Rarity.EPIC).defaultDurability(100).fireResistant()));
    //唱片
    public static final RegistryObject<Item> GUARDIANS_MUSIC_DISC = ITEMS.register("guardians_music_disc", () -> new RecordItem(8, SoundInit.GUARDIANS, new Item.Properties().tab(EMTabGroup.TABS).stacksTo(1).rarity(Rarity.RARE), 3640));
    //刷怪蛋
    public static final RegistryObject<Item> TESTER = registerEgg("tester_egg", EntityInit.TESTER, new Item.Properties().tab(EMTabGroup.TABS), rgb2dec(70, 145, 190), rgb2dec(95, 195, 255));
    public static final RegistryObject<Item> IMMORTAL_SKELETON_EGG = registerEgg("immortal_skeleton_egg", EntityInit.IMMORTAL_SKELETON, new Item.Properties().tab(EMTabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(90, 180, 204));
    public static final RegistryObject<Item> IMMORTAL_KNIGHT_EGG = registerEgg("immortal_knight_egg", EntityInit.IMMORTAL_KNIGHT, new Item.Properties().tab(EMTabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(0, 143, 204));
    public static final RegistryObject<Item> IMMORTAL_SHAMAN_EGG = registerEgg("immortal_shaman_egg", EntityInit.IMMORTAL_SHAMAN, new Item.Properties().tab(EMTabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(56, 195, 255));
    public static final RegistryObject<Item> IMMORTAL_GOLEM_EGG = registerEgg("immortal_golem_egg", EntityInit.IMMORTAL_GOLEM, new Item.Properties().tab(EMTabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(158, 226, 255));
    public static final RegistryObject<Item> IMMORTAL_BOSS_EGG = registerEgg("immortal_boss_egg", EntityInit.IMMORTAL_BOSS, new Item.Properties().tab(EMTabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(83, 137, 173));
    public static final RegistryObject<Item> NAMELESS_GUARDIAN_EGG = registerEgg("nameless_guardian_egg", EntityInit.NAMELESS_GUARDIAN, new Item.Properties().tab(EMTabGroup.TABS), rgb2dec(15, 15, 15), rgb2dec(100, 100, 105));
    public static final RegistryObject<Item> GULING_SENTINEL_EGG = registerEgg("guling_sentinel_egg", EntityInit.GULING_SENTINEL, new Item.Properties().tab(EMTabGroup.TABS), rgb2dec(25, 25, 25), rgb2dec(110, 130, 160));
    public static final RegistryObject<Item> GULING_SENTINEL_HEAVY_EGG = registerEgg("guling_sentinel_heavy_egg", EntityInit.GULING_SENTINEL_HEAVY, new Item.Properties().tab(EMTabGroup.TABS), rgb2dec(15, 15, 15), rgb2dec(100, 120, 150));
    public static final RegistryObject<Item> CORPSE_EGG = registerEgg("corpse_egg", EntityInit.CORPSE, new Item.Properties().tab(EMTabGroup.TABS), CORPSE_GREEN_COLOR, rgb2dec(72, 135, 115));
    public static final RegistryObject<Item> CORPSE_VILLAGER_EGG = registerEgg("corpse_villager_egg", EntityInit.CORPSE_VILLAGER, new Item.Properties().tab(EMTabGroup.TABS), CORPSE_GREEN_COLOR, rgb2dec(72, 129, 125));
    public static final RegistryObject<Item> CORPSE_WARLOCK_EGG = registerEgg("corpse_warlock_egg", EntityInit.CORPSE_WARLOCK, new Item.Properties().tab(EMTabGroup.TABS), rgb2dec(49, 47, 50), rgb2dec(222, 16, 16));

    public static RegistryObject<Item> registerEgg(String name, RegistryObject<? extends EntityType<? extends Mob>> EggObject, Item.Properties properties, int backgroundColor, int highlightColor) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(EggObject, backgroundColor, highlightColor, properties));
    }

    public static Item findBlockItem(RegistryObject<Block> block) {
        return block.get().asItem();
    }

    private static int rgb2dec(int r, int g, int b) {
        int n = 0;
        n += (r << 16);
        n += (g << 8);
        n += b;
        return n;
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static void initializeAttributes() {
        GUARDIAN_AXE.get().refreshAttributesFromConfig();
    }
}
