package com.eeeab.eeeabsmobs.client.render.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
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
 * Citadel 模型外图层
 *
 * @param <T> 实体
 * @param <M> 实体模型
 */
@OnlyIn(Dist.CLIENT)
public class LayerOuter<T extends LivingEntity, M extends AdvancedEntityModel<T>> extends RenderLayer<T, M> {
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
            renderLayer(stack, bufferSource.getBuffer(RenderType.entityTranslucent(resourceLocation)), packedLightIn, entity);
    }

    private void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, T entity) {
        int i = overlayTexture ? LivingEntityRenderer.getOverlayCoords(entity, 0F) : OverlayTexture.NO_OVERLAY;
        this.getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, i, 1, 1, 1, 1);
    }

    @FunctionalInterface
    public interface OuterPredicate<T> {
        /**
         * 渲染外图层条件
         */
        boolean outer(T t);
    }
}
