package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityAlienPortal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderAlienPortal extends EntityRenderer<EntityAlienPortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/alien_portal.png");
    private static final float TEXTURE_WIDTH = 320;
    private static final float TEXTURE_HEIGHT = 64;
    private static final float SIZE = 1.9F;

    public RenderAlienPortal(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityAlienPortal portal, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer consumer = bufferIn.getBuffer(EMRenderType.getGlowingEffect(TEXTURE));
        matrixStackIn.pushPose();
        //float speed = (portal.tickCount + delta);
        //float fraction = portal.scaleControlled.getAnimationFraction(delta) * SIZE;
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F - portal.getYRot()/* + speed*/));
        matrixStackIn.translate(0.0, 0.001F, 0.0);
        //matrixStackIn.scale(-fraction, -fraction, -fraction);
        matrixStackIn.scale(-SIZE, -SIZE, -SIZE);
        this.renderFlatQuad(portal.getPhase(), matrixStackIn, consumer, packedLightIn);
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAlienPortal entity) {
        return TEXTURE;
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0 + 64F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 64F / TEXTURE_WIDTH;
        float maxV = minV + 64F / TEXTURE_HEIGHT;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -SIZE, 0, -SIZE, minU, minV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, 0, SIZE, minU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, 0, SIZE, maxU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, 0, -SIZE, maxU, minV, packedLightIn);
    }

    public static void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1F, 1F, 1F, 1F).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 1F, 0F, 1F).endVertex();
    }
}
