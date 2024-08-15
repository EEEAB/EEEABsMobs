package com.eeeab.eeeabsmobs.sever.integration.curios;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.integration.curios.render.RenderSoulSummoningNecklace;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

/**
 * Curios API模组兼容
 *
 * @author EEEAB
 * @version 1.0
 */
public final class CuriosRegistry {
    private static final String MOD_ID = "curios";
    private static CuriosRegistry instance;

    public static CuriosRegistry getInstance() {
        if (ModList.get().isLoaded(MOD_ID)) {
            if (instance == null) {
                instance = new CuriosRegistry();
            }
            return instance;
        }
        return null;
    }

    public static void register() {
        var ins = getInstance();
        if (ins == null) return;
        ins.registerImpl();
    }

    private void registerImpl() {
        EEEABMobs.LOGGER.info("Starting to register Curios item...");
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, CuriosItemFactory::attachItemStackCapability);
        //注册槽位
        InterModComms.sendTo(MOD_ID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
    }

    public static void clientRegister() {
        var ins = getInstance();
        if (ins == null) return;
        ins.clientRegisterImpl();
    }

    private void clientRegisterImpl() {
        EEEABMobs.LOGGER.info("Starting to register Curios item render...");
        CuriosRendererRegistry.register(ItemInit.SOUL_SUMMONING_NECKLACE.get(), RenderSoulSummoningNecklace::new);
    }
}
