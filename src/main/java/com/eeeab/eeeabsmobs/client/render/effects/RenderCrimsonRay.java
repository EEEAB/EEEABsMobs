package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.client.render.FlatTextureRenderer;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCrimsonRay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RenderCrimsonRay extends RenderAbsBeam<EntityCrimsonRay> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/crimson_ray.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.2f;
    private static final float BEAM_RADIUS = 0.8f;

    public RenderCrimsonRay(EntityRendererProvider.Context mgr) {
        super(mgr, START_RADIUS, BEAM_RADIUS);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCrimsonRay entity) {
        return TEXTURE;
    }

    @Override
    protected void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, boolean faceCamera) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        float SIZE = START_RADIUS;

        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        if (faceCamera) {
            drawVertex(matrix4f, matrix3f, builder, -SIZE, -SIZE, 0, minU, minV, packedLightIn);
            drawVertex(matrix4f, matrix3f, builder, -SIZE, SIZE, 0, minU, maxV, packedLightIn);
            drawVertex(matrix4f, matrix3f, builder, SIZE, SIZE, 0, maxU, maxV, packedLightIn);
            drawVertex(matrix4f, matrix3f, builder, SIZE, -SIZE, 0, maxU, minV, packedLightIn);
        } else {
            drawVertex(matrix4f, matrix3f, builder, -SIZE, 0, -SIZE, minU, minV, packedLightIn);
            drawVertex(matrix4f, matrix3f, builder, -SIZE, 0, SIZE, minU, maxV, packedLightIn);
            drawVertex(matrix4f, matrix3f, builder, SIZE, 0, SIZE, maxU, maxV, packedLightIn);
            drawVertex(matrix4f, matrix3f, builder, SIZE, 0, -SIZE, maxU, minV, packedLightIn);
        }
    }

    @Override
    protected void renderStart(EntityCrimsonRay ray, int frame, PoseStack matrixStackIn, VertexConsumer builder, float delta, int packedLightIn) {
        matrixStackIn.pushPose();
        float speed = (ray.tickCount + delta) * 3F;
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F - ray.getYRot() + speed));
        matrixStackIn.translate(0.0, 0.001F, 0.0);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, false);
        matrixStackIn.popPose();
    }

    @Override
    protected void renderEnd(EntityCrimsonRay ray, int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, float delta, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        matrixStackIn.translate(0, 0.1F, 0);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, true);
        matrixStackIn.popPose();
        //判断有没有接触方块
        if (side == null) {
            return;
        }
        matrixStackIn.pushPose();
        Quaternion rotation = side.getRotation();
        rotation.mul(Vector3f.XP.rotationDegrees(90F));
        matrixStackIn.mulPose(rotation);
        matrixStackIn.translate(0, 0, -0.01F);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, true);
        matrixStackIn.popPose();
    }

    public static class RenderPreAttack extends FlatTextureRenderer<EntityCrimsonRay.PreAttack> {
        private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/crimson_ray_pre.png");
        private static final float TEXTURE_WIDTH = 256;
        private static final float TEXTURE_HEIGHT = 32;
        private static final float SIZE = 1.2F;

        public RenderPreAttack(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public ResourceLocation getTextureLocation(EntityCrimsonRay.PreAttack entity) {
            return TEXTURE;
        }

        @Override
        public void render(EntityCrimsonRay.PreAttack entity, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
            VertexConsumer consumer = bufferIn.getBuffer(EMRenderType.getGlowingEffect(TEXTURE));
            matrixStackIn.pushPose();
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F - entity.getYRot()));
            matrixStackIn.translate(0.0, 0.001F, 0.0);
            matrixStackIn.scale(-SIZE, -SIZE, -SIZE);
            float minU = 0 + 32F / TEXTURE_WIDTH * entity.getPhase();
            float minV = 0;
            float maxU = minU + 32F / TEXTURE_WIDTH;
            float maxV = minV + 32F / TEXTURE_HEIGHT;
            renderFlatQuad(matrixStackIn, consumer, packedLightIn, 1F, minU, minV, maxU, maxV, SIZE, Quad.XZ);
            matrixStackIn.popPose();
        }
    }

    @Override
    protected void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1F, 1F, 1F, 1F).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 1F, 0F, 1F).endVertex();
    }
}
