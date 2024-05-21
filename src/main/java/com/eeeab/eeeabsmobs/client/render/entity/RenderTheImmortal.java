package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTheImmortal;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerImmortalCore;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RenderTheImmortal extends MobRenderer<EntityTheImmortal, ModelTheImmortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal.png");

    public RenderTheImmortal(EntityRendererProvider.Context context) {
        super(context, new ModelTheImmortal(context.bakeLayer(EMModelLayer.IMMORTAL)), 2.5F);
        this.addLayer(new LayerImmortalCore(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTheImmortal immortal) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityTheImmortal entity, BlockPos pos) {
        return 15;
    }
}
