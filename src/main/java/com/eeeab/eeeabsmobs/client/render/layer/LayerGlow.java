package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobEntity;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerGlow<T extends EEEABMobEntity & GlowEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation location;
    private final float brightness;

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location) {
        this(renderLayerParent, location, 1.0F);
    }

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness) {
        super(renderLayerParent);
        this.location = location;
        this.brightness = brightness;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isGlow()) {
            this.renderLayer(stack, bufferSource.getBuffer(RenderType.eyes(this.location)), packedLightIn, brightness, brightness, brightness, brightness);
        }
    }

    private void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float r, float g, float b, float alpha) {
        this.getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
    }


}
