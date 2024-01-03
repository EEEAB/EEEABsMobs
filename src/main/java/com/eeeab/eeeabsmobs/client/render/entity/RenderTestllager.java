package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTestllager;
import com.eeeab.eeeabsmobs.client.render.layer.LayerMobModelOuter;
import com.eeeab.eeeabsmobs.sever.entity.impl.test.EntityTestllager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderTestllager extends MobRenderer<EntityTestllager, ModelTestllager> {
    public RenderTestllager(EntityRendererProvider.Context context) {
        super(context, new ModelTestllager(), 0.3F);
        this.addLayer(new LayerMobModelOuter<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/testllager/testllager_plains.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTestllager testllager) {
        return new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/testllager/testllager.png");
    }
}
