package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.item.*;
import com.eeeab.eeeabsmobs.sever.item.ItemArmorImmortal;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemAncientTombEye;
import com.eeeab.eeeabsmobs.sever.item.util.EEToolsTier;
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
    public static final int IMMORTAL_BACK_COLOR = rgb2dec(26, 26, 26);

    //刷怪蛋
    public static final RegistryObject<Item> IMMORTAL_SKELETON_EGG = registerEgg("immortal_skeleton_egg", EntityInit.IMMORTAL_SKELETON, IMMORTAL_BACK_COLOR, rgb2dec(204, 0, 41));
    public static final RegistryObject<Item> IMMORTAL_KNIGHT_EGG = registerEgg("immortal_knight_egg", EntityInit.IMMORTAL_KNIGHT, IMMORTAL_BACK_COLOR, rgb2dec(0, 143, 204));
    public static final RegistryObject<Item> IMMORTAL_SHAMAN_EGG = registerEgg("immortal_shaman_egg", EntityInit.IMMORTAL_SHAMAN, IMMORTAL_BACK_COLOR, rgb2dec(56, 195, 255));
    public static final RegistryObject<Item> IMMORTAL_GOLEM_EGG = registerEgg("immortal_golem_egg", EntityInit.IMMORTAL_GOLEM, IMMORTAL_BACK_COLOR, rgb2dec(158, 226, 255));
    public static final RegistryObject<Item> NAMELESS_GUARDIAN_EGG = registerEgg("nameless_guardian_egg", EntityInit.NAMELESS_GUARDIAN, rgb2dec(31, 31, 31), rgb2dec(77, 126, 159));
    public static final RegistryObject<Item> TESTER = registerEgg("tester_egg", EntityInit.TESTER, rgb2dec(70, 145, 190), rgb2dec(95, 195, 255));
    //物品
    public static final RegistryObject<Item> LOGO_ITEM = ITEMS.register("logo_item", ItemLogo::new);
    public static final RegistryObject<Item> REMOVE_MOB = ITEMS.register("remove_mob", ItemRemoveMob::new);
    public static final RegistryObject<Item> IMMORTAL_BONE = ITEMS.register("immortal_bone", ItemImmortalBone::new);
    public static final RegistryObject<Item> IMMORTAL_STAFF = ITEMS.register("immortal_staff", ItemImmortalStaff::new);
    public static final RegistryObject<Item> IMMORTAL_DEBRIS = ITEMS.register("immortal_debris", ItemImmortalDebris::new);
    public static final RegistryObject<Item> ANCIENT_TOMB_EYE = ITEMS.register("ancient_tomb_eye", ItemAncientTombEye::new);
    public static final RegistryObject<Item> IMMORTAL_INGOT = ITEMS.register("immortal_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_HELMET = ITEMS.register("immortal_helmet", () -> new ItemArmorImmortal(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> IMMORTAL_CHEST_PLATE = ITEMS.register("immortal_chest_plate", () -> new ItemArmorImmortal(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> IMMORTAL_BOOTS = ITEMS.register("immortal_boots", () -> new ItemArmorImmortal(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> IMMORTAL_LEGGINGS = ITEMS.register("immortal_leggings", () -> new ItemArmorImmortal(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> IMMORTAL_AXE = ITEMS.register("immortal_axe", () -> new ItemImmortalAxe(EEToolsTier.IMMORTAL_TIER, 6F, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_SWORD = ITEMS.register("immortal_sword", () -> new ImmortalSword(EEToolsTier.IMMORTAL_TIER, 5, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> GUARDIAN_AXE = ITEMS.register("guardian_axe", () -> new ItemGuardianAxe(EEToolsTier.GUARDIAN_AXE_TIER, 10F, -3F, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> GUARDIANS_MUSIC_DISC = ITEMS.register("guardians_music_disc", () -> new RecordItem(8, SoundInit.GUARDIANS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 3640));

    public static RegistryObject<Item> registerEgg(String name, RegistryObject<? extends EntityType<? extends Mob>> EggObject, int backgroundColor, int highlightColor) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(EggObject, backgroundColor, highlightColor, new Item.Properties()));
    }

    public static ItemStack findBlockItemToStack(RegistryObject<Block> block) {
        return block.get().asItem().getDefaultInstance();
    }

    public static Item findBlockItem(RegistryObject<Block> block) {
        return block.get().asItem();
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private static int rgb2dec(int r, int g, int b) {
        int n = 0;
        n += (r << 16);
        n += (g << 8);
        n += b;
        return n;
    }
}
