package com.eeeab.eeeabsmobs.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EmptyRender extends EntityRenderer<Entity> {
    public EmptyRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Entity entity, float p_114486_, float p_114487_, PoseStack stack, MultiBufferSource source, int p_114490_) { }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return null;
    }
}
