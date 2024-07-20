package com.eeeab.eeeabsmobs.sever.integration.curios;

import com.eeeab.eeeabsmobs.sever.item.ICuriosItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;

/**
 * Curios 物品注册
 *
 * @author EEEAB
 * @version 1.0
 */
public class CuriosItemFactory {

    public static void attachItemStackCapability(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack itemStack = event.getObject();
        Item item = itemStack.getItem();
        if (item instanceof ICuriosItem) {
            event.addCapability(CuriosCapability.ID_ITEM, CuriosApi.createCurioProvider(() -> itemStack));
        }
    }
}
