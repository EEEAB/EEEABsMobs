package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EEEABMobs.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEMS_TAB = TABS.register(EEEABMobs.MOD_ID + "_tab1", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + EEEABMobs.MOD_ID + ".creative_tab"))
            .icon(() -> new ItemStack(ItemInit.GUARDIAN_AXE.get()))
            .displayItems((enabledFeatures, entries) -> {
                /* OP Item */
                entries.accept(ItemInit.REMOVE_MOB.get());
                entries.accept(ItemInit.ANIMATION_CONTROLLER.get());

                /* Guling Structure Series */
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DUNGEON_BRICK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.IRON_GRATE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BOUNDARY_LAMP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.ANCIENT_BOUNDARY_STONE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.ROUGH_BOUNDARY_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.ROUGH_BOUNDARY_BRICK_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.ROUGH_BOUNDARY_BRICK_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.ROUGH_BOUNDARY_BRICK_WALL));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CRACKED_ROUGH_BOUNDARY_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CHISELED_ROUGH_BOUNDARY_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.ROUGH_BOUNDARY_STONE_PILLAR));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.STEPPING_SHOCK_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.REDSTONE_POISON_DART_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_STONE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_STONE_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_STONE_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_BRICK_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_BRICK_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_BRICK_WALL));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CRACKED_POLISHED_BOUNDARY_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CHISELED_POLISHED_BOUNDARY_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_BOUNDARY_STONE_PILLAR));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.STEPPING_FLAME_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.UNCARVED_BOUNDARY_STONE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.RUNIC_BOUNDARY_STONE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BOUNDARY_CORE));

                /* Misc Item */
                entries.accept(ItemInit.HEART_OF_PAGAN.get());
                entries.accept(ItemInit.BOUNDARY_BRICK.get());
                entries.accept(ItemInit.ANCIENT_DRIVE_CRYSTAL.get());
                entries.accept(ItemInit.BLOODY_ALTAR_EYE.get());
                entries.accept(ItemInit.CORE_BASTION_EYE.get());
                entries.accept(ItemInit.SOUL_SUMMONING_NECKLACE.get());
                /* Fight Item */
                entries.accept(ItemInit.DEMOLISHER.get());
                entries.accept(ItemInit.GUARDIAN_CORE.get());
                entries.accept(ItemInit.GUARDIAN_AXE.get());
                /* Disc Item */
                entries.accept(ItemInit.GUARDIANS_MUSIC_DISC.get());
                /* Spawn Egg */
                entries.accept(ItemInit.TESTER_EGG.get());
                entries.accept(ItemInit.CORPSE_EGG.get());
                entries.accept(ItemInit.CORPSE_VILLAGER_EGG.get());
                entries.accept(ItemInit.CORPSE_WARLOCK_EGG.get());
                entries.accept(ItemInit.RELIC_OBSERVER_EGG.get());
                entries.accept(ItemInit.RELIC_RIPPER_EGG.get());
                entries.accept(ItemInit.RELIC_EARTHSHAKER_EGG.get());
                entries.accept(ItemInit.RELIC_ANNIHILATOR_EGG.get());
                entries.accept(ItemInit.NAMELESS_GUARDIAN_EGG.get());
            }).build());

    public static final RegistryObject<CreativeModeTab> DEV_TAB = TABS.register(EEEABMobs.MOD_ID + "_tab2", () -> CreativeModeTab.builder()
            .title(Component.translatable("dev_group." + EEEABMobs.MOD_ID + ".creative_tab"))
            .icon(() -> ItemInit.LOGO_ITEM.get().getDefaultInstance())
            .displayItems((enabledFeatures, entries) -> {
                /* Void Crack Series */
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.EROSION_DEEPSLATE_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_SAPLING));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_LOG));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_WOOD));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.STRIPPED_BLIGHTED_OAK_LOG));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.STRIPPED_BLIGHTED_OAK_WOOD));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_PLANKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_FENCE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_FENCE_GATE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_DOOR));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_TRAPDOOR));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_PRESSURE_PLATE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_OAK_BUTTON));

                entries.accept(ItemInit.findBlockItemToStack(BlockInit.ERODED_SOIL));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARKENED_COAL_ORE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARKENED_IRON_ORE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_STONE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_STONE_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_STONE_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_STONE_PRESSURE_PLATE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_STONE_BUTTON));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_COBBLESTONE));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_COBBLESTONE_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_COBBLESTONE_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.BLIGHTED_COBBLESTONE_WALL));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.VOIDSHARD));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.VOIDSHARD_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.VOIDSHARD_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.VOIDSHARD_WALL));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_VOIDSHARD));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_VOIDSHARD_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_VOIDSHARD_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARK_EROSION_ROCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_DARK_EROSION_ROCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_DARK_EROSION_ROCK_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_DARK_EROSION_ROCK_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.POLISHED_DARK_EROSION_ROCK_WALL));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARK_EROSION_ROCK_PILLAR));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARK_EROSION_ROCK_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CRACKED_DARK_EROSION_ROCK_BRICKS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARK_EROSION_ROCK_BRICKS_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARK_EROSION_ROCK_BRICKS_SLAB));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.DARK_EROSION_ROCK_BRICKS_WALL));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CHISELED_DARK_EROSION_ROCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.STEPPING_POISON_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.STEPPING_SKELETON_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.IMMORTAL_BLOCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.GHOST_STEEL_BLOCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CUT_GHOST_STEEL_BLOCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB));

                /* Misc Item */
                entries.accept(ItemInit.IMMORTAL_BONE.get());
                entries.accept(ItemInit.IMMORTAL_DEBRIS.get());
                entries.accept(ItemInit.IMMORTAL_INGOT.get());
                entries.accept(ItemInit.GHOST_STEEL_INGOT.get());
                /* Fight Item */
                entries.accept(ItemInit.IMMORTAL_AXE.get());
                entries.accept(ItemInit.IMMORTAL_SWORD.get());
                entries.accept(ItemInit.IMMORTAL_STAFF.get());
                entries.accept(ItemInit.THE_NETHERWORLD_KATANA.get());
                entries.accept(ItemInit.GHOST_WARRIOR_HELMET.get());
                entries.accept(ItemInit.GHOST_WARRIOR_CHESTPLATE.get());
                entries.accept(ItemInit.GHOST_WARRIOR_LEGGINGS.get());
                entries.accept(ItemInit.GHOST_WARRIOR_BOOTS.get());
                /*  Smithing Template */
                entries.accept(ItemInit.GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE.get());
                /* Disc Item */
                entries.accept(ItemInit.THE_ARMY_OF_MINOTAUR_MUSIC_DISC.get());
                /* Spawn Egg */
                entries.accept(ItemInit.IMMORTAL_GOLEM_EGG.get());
                entries.accept(ItemInit.IMMORTAL_SKELETON_EGG.get());
                entries.accept(ItemInit.IMMORTAL_ARCHER_EGG.get());
                entries.accept(ItemInit.IMMORTAL_MAGE_EGG.get());
                entries.accept(ItemInit.IMMORTAL_WARRIOR_EGG.get());
                entries.accept(ItemInit.IMMORTAL_KNIGHT_EGG.get());
                entries.accept(ItemInit.IMMORTAL_SHAMAN_EGG.get());
                entries.accept(ItemInit.IMMORTAL_EXECUTIONER_EGG.get());
                entries.accept(ItemInit.IMMORTAL_BOSS_EGG.get());
            }).build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
