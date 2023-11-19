package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTest;
import com.eeeab.eeeabsmobs.sever.entity.impl.test.EntityTest;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderTest extends MobRenderer<EntityTest, ModelTest> {
    public RenderTest(EntityRendererProvider.Context context) {
        super(context, new ModelTest(), 0.3f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected void scale(EntityTest entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1F, 1F, 1F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTest p_114482_) {
        return new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/test/test.png");
    }
}
