package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianLaser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
//参考自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/render/entity/RenderSolarBeam.java
public class RenderGuardianLaser extends EntityRenderer<EntityGuardianLaser> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/guardian_laser.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float GUARDIAN_START_RADIUS = 1.09f;
    private static final float PLAYER_START_RADIUS = 0.8f;
    private static final float GUARDIAN_BEAM_RADIUS = 0.9f;
    private static final float PLAYER_BEAM_RADIUS = 0.6f;
    private boolean playerView;
    private boolean isPlayer;

    public RenderGuardianLaser(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGuardianLaser entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityGuardianLaser laser, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        playerView = laser.caster instanceof Player && Minecraft.getInstance().player == laser.caster && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        isPlayer = laser.isPlayer() || laser.caster instanceof Player;
        double collidePosX = laser.prevCollidePosX + (laser.collidePosX - laser.prevCollidePosX) * delta;
        double collidePosY = laser.prevCollidePosY + (laser.collidePosY - laser.prevCollidePosY) * delta;
        double collidePosZ = laser.prevCollidePosZ + (laser.collidePosZ - laser.prevCollidePosZ) * delta;
        double posX = laser.xo + (laser.getX() - laser.xo) * delta;
        double posY = laser.yo + (laser.getY() - laser.yo) * delta;
        double posZ = laser.zo + (laser.getZ() - laser.zo) * delta;
        float yaw = laser.prevYHeadRotAngle + (laser.yHeadRotAngle - laser.prevYHeadRotAngle) * delta;
        float pitch = laser.prevXHeadRotAngle + (laser.xHeadRotAngle - laser.prevXHeadRotAngle) * delta;

        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = Mth.floor((laser.displayControlled.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        if (laser.isAccumulating()) return;
        VertexConsumer vertex$builder = bufferIn.getBuffer(EMRenderType.getStrongGlowingEffect(getTextureLocation(laser)));
        renderStart(frame, matrixStackIn, vertex$builder, packedLightIn);
        renderBeam(length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, matrixStackIn, vertex$builder, packedLightIn);

        matrixStackIn.pushPose();
        matrixStackIn.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        renderEnd(frame, laser.blockSide, matrixStackIn, vertex$builder, packedLightIn);
        matrixStackIn.popPose();
    }

    //圆
    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, boolean inGround) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        float SIZE = !isPlayer ? GUARDIAN_START_RADIUS + (inGround ? 0.2F : 0) : PLAYER_START_RADIUS;

        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -SIZE, -SIZE, 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, SIZE, 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, SIZE, 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, -SIZE, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderStart(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        if (playerView) return;
        matrixStackIn.pushPose();
        Quaternionf quaternionf = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quaternionf);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, false);
        matrixStackIn.popPose();
    }

    private void renderEnd(int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        //渲染激光后半部分
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

    //光束
    private void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;// 通过控制帧数,来渲染二维UV的最高位置
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        float offset = playerView ? -1 : 0;
        float SIZE = isPlayer ? PLAYER_BEAM_RADIUS : GUARDIAN_BEAM_RADIUS;
        drawVertex(matrix4f, matrix3f, builder, -SIZE, offset, 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, length, 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, length, 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, offset, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
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

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
