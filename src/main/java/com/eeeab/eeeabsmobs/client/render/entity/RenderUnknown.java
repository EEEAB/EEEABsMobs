package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelUnKnown;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

//未知模型渲染 作为占位符使用
public class RenderUnknown<T extends EEEABMobLibrary & EMAnimatedEntity> extends MobRenderer<T, ModelUnKnown<T>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/unknown.png");
    private final float scale;

    public RenderUnknown(EntityRendererProvider.Context context, float scale, float shadowRadius) {
        super(context, new ModelUnKnown<>(context.bakeLayer(EMModelLayer.UNKNOWN)), shadowRadius);
        this.scale = scale;
    }

    @Override
    protected void scale(T entity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
