package com.eeeab.eeeabsmobs.sever.integration;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public class JEIModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(EEEABMobs.MOD_ID, "jei_plugin");
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        //物品描述
        registration.addItemStackInfo(List.of(
                        ItemInit.ANCIENT_TOMB_EYE.get().getDefaultInstance(),
                        ItemInit.BLOODY_ALTAR_EYE.get().getDefaultInstance()
                ), EMTUtils.simpleText("jei.", "eye_of_structure", null));
    }
}
