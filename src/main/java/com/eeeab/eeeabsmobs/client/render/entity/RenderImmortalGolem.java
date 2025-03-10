package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortalGolem;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderImmortalGolem extends MobRenderer<EntityImmortalGolem, ModelImmortalGolem> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_golem/immortal_golem.png");

    public RenderImmortalGolem(EntityRendererProvider.Context context) {
        super(context, new ModelImmortalGolem(context.bakeLayer(EMModelLayer.IMMORTAL_GOLEM)), 0.3f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected void scale(EntityImmortalGolem entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.8F, 0.8F, 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalGolem entity) {
        return TEXTURE;
    }
}
