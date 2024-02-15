package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTheImmortal;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderTheImmortal extends MobRenderer<EntityTheImmortal, ModelTheImmortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/test/test.png");

    public RenderTheImmortal(EntityRendererProvider.Context context) {
        super(context, new ModelTheImmortal(), 0.3f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected void scale(EntityTheImmortal entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1F, 1F, 1F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTheImmortal p_114482_) {
        return TEXTURE;
    }
}
