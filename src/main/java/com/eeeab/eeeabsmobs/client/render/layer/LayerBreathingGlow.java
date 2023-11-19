package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

//呼吸发光效果
@OnlyIn(Dist.CLIENT)
public class LayerBreathingGlow<T extends EEEABMobEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation location;
    private final float speed;
    private final Predicate<T> canGlow;


    public LayerBreathingGlow(RenderLayerParent<T, M> layerParent, ResourceLocation location, float speed, Predicate<T> predicate) {
        super(layerParent);
        this.location = location;
        this.speed = speed;
        this.canGlow = predicate;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (canGlow.test(entity)) {
            float alpha = Mth.clamp((Mth.cos(ageInTicks * speed) * 2.0F) - 1.0F, 0, 1.0F);
            renderLayer(stack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(location)), packedLightIn, alpha, alpha, alpha, 1.0F);
        }
    }

    private void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float r, float g, float b, float alpha) {
        this.getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
    }
}
