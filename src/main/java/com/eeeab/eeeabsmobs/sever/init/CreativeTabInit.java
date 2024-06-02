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

    public static final RegistryObject<CreativeModeTab> EEEAB_MOBS_TAB = TABS.register("eeeabmobs_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + EEEABMobs.MOD_ID + ".creative_tab"))
            .icon(() -> new ItemStack(ItemInit.IMMORTAL_AXE.get()))
            .displayItems((enabledFeatures, entries) -> {
                /* Block Item */
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.IMMORTAL_BLOCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.SOUL_LIGHT));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.TOMB_GAS_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.TOMB_SUMMON_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.TOMB_ARROWS_TRAP));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.EROSION_DEEPSLATE_BRICKS));

                /* Misc Item */
                entries.accept(ItemInit.REMOVE_MOB.get());
                entries.accept(ItemInit.IMMORTAL_BONE.get());
                entries.accept(ItemInit.IMMORTAL_DEBRIS.get());
                entries.accept(ItemInit.IMMORTAL_INGOT.get());
                entries.accept(ItemInit.BLOODY_ALTAR_EYE.get());
                entries.accept(ItemInit.ANCIENT_TOMB_EYE.get());
                entries.accept(ItemInit.HEART_OF_PAGAN.get());

                /* Fight Item */
                entries.accept(ItemInit.IMMORTAL_HELMET.get());
                entries.accept(ItemInit.IMMORTAL_CHEST_PLATE.get());
                entries.accept(ItemInit.IMMORTAL_LEGGINGS.get());
                entries.accept(ItemInit.IMMORTAL_BOOTS.get());
                entries.accept(ItemInit.IMMORTAL_AXE.get());
                entries.accept(ItemInit.IMMORTAL_SWORD.get());
                entries.accept(ItemInit.IMMORTAL_STAFF.get());
                entries.accept(ItemInit.GUARDIAN_AXE.get());
                entries.accept(ItemInit.GUARDIAN_CORE.get());

                /* Disc Item */
                entries.accept(ItemInit.GUARDIANS_MUSIC_DISC.get());

                /* Spawn Egg */
                entries.accept(ItemInit.TESTER.get());
                entries.accept(ItemInit.CORPSE_EGG.get());
                entries.accept(ItemInit.CORPSE_VILLAGER_EGG.get());
                entries.accept(ItemInit.CORPSE_WARLOCK_EGG.get());
                entries.accept(ItemInit.GULING_SENTINEL_EGG.get());
                entries.accept(ItemInit.GULING_SENTINEL_HEAVY_EGG.get());
                entries.accept(ItemInit.NAMELESS_GUARDIAN_EGG.get());
                entries.accept(ItemInit.IMMORTAL_SKELETON_EGG.get());
                entries.accept(ItemInit.IMMORTAL_KNIGHT_EGG.get());
                entries.accept(ItemInit.IMMORTAL_SHAMAN_EGG.get());
                entries.accept(ItemInit.IMMORTAL_GOLEM_EGG.get());
                entries.accept(ItemInit.IMMORTAL_BOSS_EGG.get());
            }).build());


    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
