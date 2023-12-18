package com.eeeab.eeeabsmobs.client.render.util;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class EEItemStackRenderProperties implements IClientItemExtensions {
    public EEItemStackRenderProperties() {
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new EEItemStackRender();
    }
}
