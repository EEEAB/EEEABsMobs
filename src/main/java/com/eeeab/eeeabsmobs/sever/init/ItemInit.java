package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.item.*;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemAncientTombEye;
import com.eeeab.eeeabsmobs.sever.item.util.EEBlockEntityItemRender;
import com.eeeab.eeeabsmobs.sever.item.util.EEToolsTier;
import com.eeeab.eeeabsmobs.sever.util.EETabGroup;
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

    //方块
    public static final RegistryObject<Item> IMMORTAL_BLOCK = registerBlock("immortal_block", BlockInit.IMMORTAL_BLOCK, false, new Item.Properties().tab(EETabGroup.TABS));
    public static final RegistryObject<Item> SOUL_LIGHT = registerBlock("soul_light", BlockInit.SOUL_LIGHT, false, new Item.Properties().tab(EETabGroup.TABS));
    public static final RegistryObject<Item> TOMBSTONE = registerBlock("tombstone", BlockInit.TOMBSTONE, true, new Item.Properties());
    //物品
    public static final RegistryObject<Item> LOGO_ITEM = ITEMS.register("logo_item", ItemLogo::new);
    public static final RegistryObject<Item> REMOVE_MOB = ITEMS.register("remove_mob", () -> new ItemRemoveMob(new Item.Properties().tab(EETabGroup.TABS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> IMMORTAL_BONE = ITEMS.register("immortal_bone", () -> new ItemImmortalBone(new Item.Properties().tab(EETabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_DEBRIS = ITEMS.register("immortal_debris", () -> new ItemImmortalDebris(new Item.Properties().tab(EETabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_INGOT = ITEMS.register("immortal_ingot", () -> new Item(new Item.Properties().tab(EETabGroup.TABS)));//.tab(EETabGroup.TABS)
    public static final RegistryObject<Item> ANCIENT_TOMB_EYE = ITEMS.register("ancient_tomb_eye", () -> new ItemAncientTombEye(new Item.Properties().tab(EETabGroup.TABS).rarity(Rarity.RARE).stacksTo(16)));
    //防具
    public static final RegistryObject<Item> IMMORTAL_HELMET = ITEMS.register("immortal_helmet", () -> new ItemArmorImmortal(EquipmentSlot.HEAD, new Item.Properties().tab(EETabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_CHEST_PLATE = ITEMS.register("immortal_chest_plate", () -> new ItemArmorImmortal(EquipmentSlot.CHEST, new Item.Properties().tab(EETabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_BOOTS = ITEMS.register("immortal_boots", () -> new ItemArmorImmortal(EquipmentSlot.FEET, new Item.Properties().tab(EETabGroup.TABS)));
    public static final RegistryObject<Item> IMMORTAL_LEGGINGS = ITEMS.register("immortal_leggings", () -> new ItemArmorImmortal(EquipmentSlot.LEGS, new Item.Properties().tab(EETabGroup.TABS)));
    //武器
    public static final RegistryObject<Item> IMMORTAL_AXE = ITEMS.register("immortal_axe", () -> new ItemImmortalAxe(EEToolsTier.IMMORTAL_TIER, 6F, -2.8F, new Item.Properties().tab(EETabGroup.TABS)));//.tab(EETabGroup.TABS)
    public static final RegistryObject<Item> IMMORTAL_SWORD = ITEMS.register("immortal_sword", () -> new ImmortalSword(EEToolsTier.IMMORTAL_TIER, 5, -2.4F, new Item.Properties().tab(EETabGroup.TABS)));//.tab(EETabGroup.TABS)
    public static final RegistryObject<Item> IMMORTAL_STAFF = ITEMS.register("immortal_staff", () -> new ItemImmortalStaff(new Item.Properties().tab(EETabGroup.TABS)));
    public static final RegistryObject<Item> GUARDIAN_AXE = ITEMS.register("guardian_axe", () -> new ItemGuardianAxe(EEToolsTier.GUARDIAN_AXE_TIER, 10F, -3F, new Item.Properties().tab(EETabGroup.TABS).rarity(Rarity.EPIC).fireResistant()));//.tab(EETabGroup.TABS)
    //刷怪蛋
    public static final RegistryObject<Item> IMMORTAL_SKELETON_EGG = registerEgg("immortal_skeleton_egg", EntityInit.IMMORTAL_SKELETON, new Item.Properties().tab(EETabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(204, 0, 41));
    public static final RegistryObject<Item> IMMORTAL_KNIGHT_EGG = registerEgg("immortal_knight_egg", EntityInit.IMMORTAL_KNIGHT, new Item.Properties().tab(EETabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(0, 143, 204));
    public static final RegistryObject<Item> IMMORTAL_SHAMAN_EGG = registerEgg("immortal_shaman_egg", EntityInit.IMMORTAL_SHAMAN, new Item.Properties().tab(EETabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(56, 195, 255));
    public static final RegistryObject<Item> IMMORTAL_GOLEM_EGG = registerEgg("immortal_golem_egg", EntityInit.IMMORTAL_GOLEM, new Item.Properties().tab(EETabGroup.TABS), IMMORTAL_BACK_COLOR, rgb2dec(158, 226, 255));
    public static final RegistryObject<Item> NAMELESS_GUARDIAN_EGG = registerEgg("nameless_guardian_egg", EntityInit.NAMELESS_GUARDIAN, new Item.Properties().tab(EETabGroup.TABS), rgb2dec(31, 31, 31), rgb2dec(77, 126, 159));
    public static final RegistryObject<Item> TESTER = registerEgg("tester_egg", EntityInit.TESTER, new Item.Properties(), rgb2dec(70, 145, 190), rgb2dec(95, 195, 255));
    //唱片
    public static final RegistryObject<Item> GUARDIANS_MUSIC_DISC = ITEMS.register("guardians_music_disc", () -> new RecordItem(8, SoundInit.GUARDIANS, new Item.Properties().tab(EETabGroup.TABS).stacksTo(1).rarity(Rarity.RARE), 3640));//.tab(EETabGroup.TABS)

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static RegistryObject<Item> registerEgg(String name, RegistryObject<? extends EntityType<? extends Mob>> EggObject, Item.Properties properties, int backgroundColor, int highlightColor) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(EggObject, backgroundColor, highlightColor, properties));
    }

    public static RegistryObject<Item> registerBlock(String name, RegistryObject<Block> block, boolean isEntityBlock, Item.Properties properties) {
        return ItemInit.ITEMS.register(name, () -> isEntityBlock ? new EEBlockEntityItemRender(block.get(), properties) : new BlockItem(block.get(), properties));
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
}
