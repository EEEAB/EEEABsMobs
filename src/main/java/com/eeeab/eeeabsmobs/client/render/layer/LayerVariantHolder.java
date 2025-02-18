package com.eeeab.eeeabsmobs.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.VariantHolder;

import java.util.Map;

/**
 * 生物变种图层
 *
 * @param <V> 变种标记
 * @param <T> 实体
 * @param <M> 实体模型
 */
public class LayerVariantHolder<V, T extends LivingEntity & VariantHolder<V>, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final Map<V, ResourceLocation> locationMap;

    public LayerVariantHolder(RenderLayerParent<T, M> renderer, Map<V, ResourceLocation> locationMap) {
        super(renderer);
        this.locationMap = locationMap;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            ResourceLocation resourcelocation = locationMap.get(entity.getVariant());
            if (resourcelocation == null) {
                return;
            }
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityTranslucent(resourcelocation));
            this.getParentModel().renderToBuffer(stack, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1F, 1F, 1F, 1F);
        }
    }
}
