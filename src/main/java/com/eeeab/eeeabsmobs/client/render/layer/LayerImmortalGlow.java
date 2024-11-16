package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortal;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerImmortalGlow extends LayerGlow<EntityImmortal, ModelImmortal> {
    public LayerImmortalGlow(RenderLayerParent<EntityImmortal, ModelImmortal> renderLayerParent, ResourceLocation location) {
        super(renderLayerParent, location);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, EntityImmortal entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (predicate.glow(entity)) {
            float brightness = this.brightness * entity.glowControllerAnimation.getAnimationFraction();
            this.renderLayer(entity, stack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(this.location)), packedLightIn, brightness, brightness, brightness, 1F - entity.alphaControllerAnimation.getAnimationFraction(partialTicks));
        }
    }
}
