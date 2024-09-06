package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.client.render.FlatTextureRenderer;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCrimsonCrack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class RenderCrimsonCrack extends FlatTextureRenderer<EntityCrimsonCrack> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/crimson_crack.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;

    public RenderCrimsonCrack(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCrimsonCrack entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityCrimsonCrack crack, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float alpha = crack.displayControlled.getAnimationFraction();
        VertexConsumer consumer = bufferIn.getBuffer(EMRenderType.getGlowingEffect(TEXTURE));
        matrixStackIn.pushPose();
        Quaternionf quaternionf = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quaternionf);
        matrixStackIn.scale(-2F, -2F, -2F);
        float minU = 0 + 32F / TEXTURE_WIDTH * crack.getPhase();
        float minV = 0;
        float maxU = minU + 32F / TEXTURE_WIDTH;
        float maxV = minV + 32F / TEXTURE_HEIGHT;
        renderFlatQuad(matrixStackIn, consumer, packedLightIn, alpha, minU, minV, maxU, maxV, 1F, Quad.XY);
        matrixStackIn.popPose();
    }
}
