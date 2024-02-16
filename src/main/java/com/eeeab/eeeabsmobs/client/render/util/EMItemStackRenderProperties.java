package com.eeeab.eeeabsmobs.client.render.util;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class EMItemStackRenderProperties implements IClientItemExtensions {
    public EMItemStackRenderProperties() {
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new EMItemStackRender();
    }
}
