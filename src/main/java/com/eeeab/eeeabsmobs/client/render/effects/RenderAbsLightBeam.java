package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityAbsLightBeam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
//基于自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/render/entity/RenderSolarBeam.java
public abstract class RenderAbsLightBeam<T extends EntityAbsLightBeam> extends EntityRenderer<T> {
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private final float quadRadius;
    private final float beamRadius;

    public RenderAbsLightBeam(EntityRendererProvider.Context mgr, float quadRadius, float beamRadius) {
        super(mgr);
        this.quadRadius = quadRadius;
        this.beamRadius = beamRadius;
    }

    @Override
    public abstract ResourceLocation getTextureLocation(T entity);

    @Override
    public void render(T laser, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        double collidePosX = laser.prevCollidePosX + (laser.collidePosX - laser.prevCollidePosX) * delta;
        double collidePosY = laser.prevCollidePosY + (laser.collidePosY - laser.prevCollidePosY) * delta;
        double collidePosZ = laser.prevCollidePosZ + (laser.collidePosZ - laser.prevCollidePosZ) * delta;
        double posX = laser.xo + (laser.getX() - laser.xo) * delta;
        double posY = laser.yo + (laser.getY() - laser.yo) * delta;
        double posZ = laser.zo + (laser.getZ() - laser.zo) * delta;
        float yaw = laser.preYaw + (laser.yaw - laser.preYaw) * delta;
        float pitch = laser.prePitch + (laser.pitch - laser.prePitch) * delta;
        //计算两点坐标+碰撞检测得出激光长度
        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = Mth.floor((laser.displayControlled.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        if (!laser.isAccumulating()) return;
        VertexConsumer vertex$builder = bufferIn.getBuffer(EMRenderType.getGlowingEffect(getTextureLocation(laser)));
        renderStart(frame, matrixStackIn, vertex$builder, packedLightIn);
        renderBeam(length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, matrixStackIn, vertex$builder, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        renderEnd(frame, laser.blockSide, matrixStackIn, vertex$builder, packedLightIn);
        matrixStackIn.popPose();
    }

    //渲染四边形平面
    protected void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, boolean inGround) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        float SIZE = this.quadRadius + (inGround ? 0.2F : 0);
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -SIZE, -SIZE, 0, minU, minV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, SIZE, 0, minU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, SIZE, 0, maxU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, -SIZE, 0, maxU, minV, packedLightIn);
    }

    //渲染激光起始位置
    protected void renderStart(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternionf quaternionf = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quaternionf);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, false);
        matrixStackIn.popPose();
    }

    //渲染激光后半部分
    protected void renderEnd(int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, false);
        matrixStackIn.popPose();
        //判断有没有接触地面
        if (side == null) {
            return;
        }
        //渲染激光照射在地面的纹理
        matrixStackIn.pushPose();
        Quaternionf rotation = side.getRotation();
        rotation.mul(Axis.XP.rotationDegrees(90F));
        matrixStackIn.mulPose(rotation);
        matrixStackIn.translate(0, 0, -0.01f);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, true);
        matrixStackIn.popPose();
    }

    //渲染光束
    protected void renderBeam(float length, float yaw, float pitch, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        //旋转角度
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(yaw - 90F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-pitch));

        //绘制一根射线
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 90F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        //绘制一根射线
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 90F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        //绘制一根射线
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 180F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        //绘制一根射线
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 180F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        matrixStackIn.popPose();
    }

    //绘制光束
    protected void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        //通过控制帧数,来渲染二维UV的最高位置
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        float offset = 0;
        float SIZE = this.beamRadius;
        drawVertex(matrix4f, matrix3f, builder, -SIZE, offset, 0, minU, minV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, length, 0, minU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, length, 0, maxU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, offset, 0, maxU, minV, packedLightIn);
    }

    protected void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1F, 1F, 1F, 1F).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
