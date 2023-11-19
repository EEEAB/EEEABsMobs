package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.item.*;
import com.eeeab.eeeabsmobs.sever.item.util.EEArmorMaterial;
import com.eeeab.eeeabsmobs.sever.item.ItemArmorImmortal;
import com.eeeab.eeeabsmobs.sever.item.eye.ItemAncientTombEye;
import com.eeeab.eeeabsmobs.sever.item.util.EEToolsTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
    //字体样式
    public static final Style TIPS_GRAY = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));
    public static final Style TIPS_GREEN = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN));
    public static final int IMMORTAL_BACK_COLOR = rgb2dec(26, 26, 26);

    //刷怪蛋
    public static final RegistryObject<Item> IMMORTAL_SKELETON_EGG =
            ITEMS.register("immortal_skeleton_egg",
                    () -> new ForgeSpawnEggItem(EntityInit.IMMORTAL_SKELETON, IMMORTAL_BACK_COLOR, rgb2dec(204, 0, 41),
                            new Item.Properties()));

    public static final RegistryObject<Item> IMMORTAL_KNIGHT_EGG =
            ITEMS.register("immortal_knight_egg",
                    () -> new ForgeSpawnEggItem(EntityInit.IMMORTAL_KNIGHT, IMMORTAL_BACK_COLOR, rgb2dec(0, 143, 204),
                            new Item.Properties()));

    public static final RegistryObject<Item> IMMORTAL_SHAMAN_EGG =
            ITEMS.register("immortal_shaman_egg",
                    () -> new ForgeSpawnEggItem(EntityInit.IMMORTAL_SHAMAN, IMMORTAL_BACK_COLOR, rgb2dec(56, 195, 255),
                            new Item.Properties()));

    public static final RegistryObject<Item> IMMORTAL_GOLEM_EGG =
            ITEMS.register("immortal_golem_egg",
                    () -> new ForgeSpawnEggItem(EntityInit.IMMORTAL_GOLEM, IMMORTAL_BACK_COLOR, rgb2dec(158, 226, 255),
                            new Item.Properties()));

    public static final RegistryObject<Item> NAMELESS_GUARDIAN_EGG =
            ITEMS.register("nameless_guardian_egg",
                    () -> new ForgeSpawnEggItem(EntityInit.NAMELESS_GUARDIAN, rgb2dec(31, 31, 31), rgb2dec(77, 126, 159),
                            new Item.Properties()));

    //物品
    public static final RegistryObject<Item> IMMORTAL_BONE = ITEMS.register("immortal_bone",
            () -> new ItemImmortalBone(new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_HELMET = ITEMS.register("immortal_helmet",
            () -> new ItemArmorImmortal(EEArmorMaterial.IMMORTAL_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_CHEST_PLATE = ITEMS.register("immortal_chest_plate",
            () -> new ItemArmorImmortal(EEArmorMaterial.IMMORTAL_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_LEGGINGS = ITEMS.register("immortal_leggings",
            () -> new ItemArmorImmortal(EEArmorMaterial.IMMORTAL_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_BOOTS = ITEMS.register("immortal_boots",
            () -> new ItemArmorImmortal(EEArmorMaterial.IMMORTAL_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_AXE = ITEMS.register("immortal_axe",
            () -> new ItemImmortalAxe(EEToolsTier.IMMORTAL_TIER, 6F, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_SWORD = ITEMS.register("immortal_sword",
            () -> new ImmortalSword(EEToolsTier.IMMORTAL_TIER, 5, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> IMMORTAL_STAFF = ITEMS.register("immortal_staff",
            () -> new ItemImmortalStaff(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> GUARDIAN_AXE = ITEMS.register("guardian_axe",
            () -> new ItemGuardianAxe(EEToolsTier.GUARDIAN_AXE_TIER, 10F, -3F, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> LOGO_ITEM = ITEMS.register("logo_item", ItemLogo::new);
    public static final RegistryObject<Item> REMOVE_MOB = ITEMS.register("remove_mob", ItemRemoveMob::new);
    public static final RegistryObject<Item> IMMORTAL_DEBRIS = ITEMS.register("immortal_debris", ItemImmortalDebris::new);
    public static final RegistryObject<Item> IMMORTAL_INGOT = ITEMS.register("immortal_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_TOMB_EYE = ITEMS.register("ancient_tomb_eye", ItemAncientTombEye::new);
    public static final RegistryObject<Item> GUARDIANS_MUSIC_DISC = ITEMS.register("guardians_music_disc",
            () -> new RecordItem(8, SoundInit.GUARDIANS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 3640));


    public static RegistryObject<Item> registerEgg(String name, RegistryObject<EntityType<? extends Mob>> EggObject, int backgroundColor, int highlightColor) {
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
