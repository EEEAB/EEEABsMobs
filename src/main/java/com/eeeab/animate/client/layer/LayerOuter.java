package com.eeeab.animate.client.layer;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * EEEAB 模型外图层
 *
 * @param <T> 实体
 * @param <M> 实体模型
 */
@OnlyIn(Dist.CLIENT)
public class LayerOuter<T extends LivingEntity, M extends EMHierarchicalModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation resourceLocation;
    private final OuterPredicate<T> predicate;
    private final boolean overlayTexture;

    public LayerOuter(RenderLayerParent<T, M> parent, ResourceLocation resourceLocation) {
        this(parent, resourceLocation, true, t -> true);
    }

    public LayerOuter(RenderLayerParent<T, M> parent, ResourceLocation resourceLocation, boolean overlayTexture) {
        this(parent, resourceLocation, overlayTexture, t -> true);
    }

    public LayerOuter(RenderLayerParent<T, M> parent, ResourceLocation resourceLocation, boolean overlayTexture, OuterPredicate<T> predicate) {
        super(parent);
        this.resourceLocation = resourceLocation;
        this.overlayTexture = overlayTexture;
        this.predicate = predicate;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (predicate.outer(entity))
            renderLayer(stack, bufferSource.getBuffer(RenderType.entityTranslucent(resourceLocation)), packedLightIn, partialTicks, entity);
    }

    private void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float partialTicks, T entity) {
        int i = overlayTexture ? LivingEntityRenderer.getOverlayCoords(entity, 0F) : OverlayTexture.NO_OVERLAY;
        float alpha = getAlpha(entity, partialTicks);
        getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, i, alpha, alpha, alpha, alpha);
    }

    /**
     * 获取渲染图层透明度
     *
     * @param entity 实体
     * @return 透明度
     */
    protected float getAlpha(T entity, float partialTicks) {
        return 1F;
    }

    @FunctionalInterface
    public interface OuterPredicate<T> {
        /**
         * 渲染外图层条件
         */
        boolean outer(T t);
    }
}
