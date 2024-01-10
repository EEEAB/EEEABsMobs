package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EETabGroup {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EEEABMobs.MOD_ID);

    public static final RegistryObject<CreativeModeTab> EEEAB_MOBS_TAB = TABS.register("eeeabmobs_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + EEEABMobs.MOD_ID + ".creative_tab"))
            .icon(() -> new ItemStack(ItemInit.IMMORTAL_AXE.get()))
            .displayItems((enabledFeatures, entries) -> {
                /* misc Item */
                entries.accept(ItemInit.REMOVE_MOB.get());
                entries.accept(ItemInit.IMMORTAL_BONE.get());
                entries.accept(ItemInit.IMMORTAL_DEBRIS.get());
                entries.accept(ItemInit.IMMORTAL_INGOT.get());
                entries.accept(ItemInit.ANCIENT_TOMB_EYE.get());

                /* Block Item */
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.IMMORTAL_BLOCK));
                entries.accept(ItemInit.findBlockItemToStack(BlockInit.SOUL_LIGHT));

                /* fight Item */
                entries.accept(ItemInit.IMMORTAL_HELMET.get());
                entries.accept(ItemInit.IMMORTAL_CHEST_PLATE.get());
                entries.accept(ItemInit.IMMORTAL_LEGGINGS.get());
                entries.accept(ItemInit.IMMORTAL_BOOTS.get());
                entries.accept(ItemInit.IMMORTAL_AXE.get());
                entries.accept(ItemInit.IMMORTAL_SWORD.get());
                entries.accept(ItemInit.IMMORTAL_STAFF.get());
                entries.accept(ItemInit.GUARDIAN_AXE.get());

                /* Spawn Egg */
                entries.accept(ItemInit.IMMORTAL_SKELETON_EGG.get());
                entries.accept(ItemInit.IMMORTAL_KNIGHT_EGG.get());
                entries.accept(ItemInit.IMMORTAL_SHAMAN_EGG.get());
                entries.accept(ItemInit.IMMORTAL_GOLEM_EGG.get());
                entries.accept(ItemInit.NAMELESS_GUARDIAN_EGG.get());

                /* Disc Item */
                entries.accept(ItemInit.GUARDIANS_MUSIC_DISC.get());
            }).build());


    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
