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
 * @see GlowEntity
 */
@OnlyIn(Dist.CLIENT)
public class LayerGlow<T extends LivingEntity & GlowEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    protected final ResourceLocation location;
    protected final GlowPredicate<T> predicate;
    protected final float brightness;
    protected final boolean overlayTexture;

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location) {
        this(renderLayerParent, location, 1.0F, GlowEntity::isGlow, false);
    }

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness) {
        this(renderLayerParent, location, brightness, GlowEntity::isGlow, false);
    }

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness, GlowPredicate<T> predicate) {
        this(renderLayerParent, location, brightness, predicate, false);
    }

    public LayerGlow(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness, GlowPredicate<T> predicate, boolean overlayTexture) {
        super(renderLayerParent);
        this.location = location;
        this.brightness = brightness;
        this.predicate = predicate;
        this.overlayTexture = overlayTexture;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (predicate.glow(entity)) {
            float brightness = getBrightness(entity, partialTicks);
            this.renderLayer(entity, stack, bufferSource.getBuffer(getRenderType(entity)), packedLightIn, brightness, brightness, brightness, brightness);
        }
    }

    protected void renderLayer(T entity, PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float r, float g, float b, float alpha) {
        int i = overlayTexture ? LivingEntityRenderer.getOverlayCoords(entity, 0F) : OverlayTexture.NO_OVERLAY;
        this.getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, i, r, g, b, alpha);
    }

    /**
     * 获取渲染图层的类型
     *
     * @param entity 实体
     * @return 渲染类型
     */
    protected RenderType getRenderType(T entity) {
        return RenderType.eyes(this.location);
    }

    /**
     * 获取渲染图层的亮度
     *
     * @param entity 实体
     * @return 亮度值
     */
    protected float getBrightness(T entity, float partialTicks) {
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
