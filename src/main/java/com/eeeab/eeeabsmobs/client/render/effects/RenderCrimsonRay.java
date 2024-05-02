package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCrimsonRay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderCrimsonRay extends EntityRenderer<EntityCrimsonRay> {
    private static final ResourceLocation R_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/crimson_ray.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.2f;
    private static final float BEAM_RADIUS = 0.8f;

    public RenderCrimsonRay(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCrimsonRay entity) {
        return R_TEXTURE;
    }

    @Override
    public void render(EntityCrimsonRay ray, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        double collidePosX = ray.prevCollidePosX + (ray.collidePosX - ray.prevCollidePosX) * delta;
        double collidePosY = ray.prevCollidePosY + (ray.collidePosY - ray.prevCollidePosY) * delta;
        double collidePosZ = ray.prevCollidePosZ + (ray.collidePosZ - ray.prevCollidePosZ) * delta;
        double posX = ray.xo + (ray.getX() - ray.xo) * delta;
        double posY = ray.yo + (ray.getY() - ray.yo) * delta;
        double posZ = ray.zo + (ray.getZ() - ray.zo) * delta;
        float yaw = ray.preYaw + (ray.yaw - ray.preYaw) * delta;
        float pitch = ray.prePitch + (ray.pitch - ray.prePitch) * delta;

        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = Mth.floor((ray.displayControlled.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        if (!ray.isAccumulating()) return;
        VertexConsumer vertex$builder = bufferIn.getBuffer(EMRenderType.getGlowingEffect(getTextureLocation(ray)));
        renderStart(ray, frame, matrixStackIn, vertex$builder, delta, packedLightIn);
        renderBeam(length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, matrixStackIn, vertex$builder, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        renderEnd(frame, ray.blockSide, matrixStackIn, vertex$builder, packedLightIn);
        matrixStackIn.popPose();
    }

    //圆
    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, boolean faceCamera) {
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

    private void renderStart(EntityCrimsonRay ray, int frame, PoseStack matrixStackIn, VertexConsumer builder, float delta, int packedLightIn) {
        matrixStackIn.pushPose();
        float speed = (ray.tickCount + delta) * 3F;
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F - ray.getYRot() + speed));
        matrixStackIn.translate(0.0, 0.001F, 0.0);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, false);
        matrixStackIn.popPose();
    }

    private void renderEnd(int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
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

    //光束
    private void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;// 通过控制帧数,来渲染二维UV的最高位置
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        float SIZE = BEAM_RADIUS;
        drawVertex(matrix4f, matrix3f, builder, -SIZE, 0, 0, minU, minV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, length, 0, minU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, length, 0, maxU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, 0, 0, maxU, minV, packedLightIn);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(yaw - 90F));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-pitch));

        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 90F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 90F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 180F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 180F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        matrixStackIn.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1F, 1F, 1F, 1F).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 1F, 0F, 1F).endVertex();
    }
}
