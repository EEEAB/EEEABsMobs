package com.eeeab.eeeabsmobs.client.render.util;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ModItemStackRenderProperties implements IClientItemExtensions {
    public ModItemStackRenderProperties() {
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new ModItemStackRender();
    }
}
