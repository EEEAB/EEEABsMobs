package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCrimsonCrack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RenderCrimsonCrack extends EntityRenderer<EntityCrimsonCrack> {
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
        this.renderFlatQuad(crack.getPhase(),matrixStackIn,consumer,packedLightIn,alpha);
        matrixStackIn.popPose();
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn,float alpha) {
        float minU = 0 + 32F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 32F / TEXTURE_WIDTH;
        float maxV = minV + 32F / TEXTURE_HEIGHT;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -1, -1, 0, minU, minV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -1, 1, 0, minU, maxV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, 1, 1, 0, maxU, maxV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, 1, -1, 0, maxU, minV, alpha, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
