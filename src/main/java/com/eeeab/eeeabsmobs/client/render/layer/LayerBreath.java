package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 生物呼吸发光图层
 *
 * @param <T> 实体
 * @param <M> 实体模型
 * @see com.eeeab.eeeabsmobs.sever.entity.GlowEntity
 */
@OnlyIn(Dist.CLIENT)
public class LayerBreath<T extends LivingEntity & GlowEntity, M extends EntityModel<T>> extends LayerGlow<T, M> {
    private final float speed;

    public LayerBreath(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness, float speed) {
        this(renderLayerParent, location, brightness, GlowEntity::isGlow, speed);
    }

    public LayerBreath(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, float brightness, GlowPredicate<T> predicate, float speed) {
        super(renderLayerParent, location, brightness, predicate);
        this.speed = speed;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (predicate.glow(entity)) {
            float alpha = Mth.clamp((Mth.cos(ageInTicks * speed) * 2.0F) - 1.0F, 0, 1.0F);
            renderLayer(entity, stack, bufferSource.getBuffer(RenderType.eyes(this.location)), packedLightIn, alpha, alpha, alpha, 1.0F);
        }
    }
}
