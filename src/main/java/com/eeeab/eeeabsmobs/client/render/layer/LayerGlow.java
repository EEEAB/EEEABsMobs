package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
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
 * 生物发光图层
 *
 * @param <T> 实体
 * @param <M> 实体模型
 * @see com.eeeab.eeeabsmobs.sever.entity.GlowEntity
 */
@OnlyIn(Dist.CLIENT)
public class LayerGlow<T extends LivingEntity & GlowEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    protected final ResourceLocation location;
    protected final GlowPredicate<T> predicate;
    protected final float brightness;

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location) {
        this(renderLayerParent, location, 1.0F, GlowEntity::isGlow);
    }

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness) {
        this(renderLayerParent, location, brightness, GlowEntity::isGlow);
    }

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness, GlowPredicate<T> predicate) {
        super(renderLayerParent);
        this.location = location;
        this.brightness = brightness;
        this.predicate = predicate;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (predicate.glow(entity)) {
            float brightness = getBrightness(entity);
            this.renderLayer(stack, bufferSource.getBuffer(RenderType.eyes(this.location)), packedLightIn, brightness, brightness, brightness, brightness);
        }
    }

    protected void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float r, float g, float b, float alpha) {
        this.getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
    }

    protected void renderLayer(T entity, PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float r, float g, float b, float alpha) {
        this.getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0F), r, g, b, alpha);
    }
    
    /**
     * 获取渲染图层的亮度
     *
     * @param entity 实体
     * @return 亮度值
     */
    protected float getBrightness(T entity) {
        return this.brightness;
    }


    @FunctionalInterface
    public interface GlowPredicate<T> {
        /**
         * 渲染发光图层条件
         */
        boolean glow(T t);
    }
}
