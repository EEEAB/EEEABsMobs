package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EERenderType;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityScorch;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderEntityScorch extends EntityRenderer<EntityScorch> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/scorch.png");
    private static final float TEXTURE_WIDTH = 32;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float RING_FRAME_SIZE = 16;
    private static final float LINGER_RADIUS = 1f;
    private static final float SCORCH_MIN_U = 0f / TEXTURE_WIDTH;
    private static final float SCORCH_MAX_U = SCORCH_MIN_U + RING_FRAME_SIZE / TEXTURE_WIDTH;
    private static final float SCORCH_MIN_V = 0f / TEXTURE_HEIGHT;
    private static final float SCORCH_MAX_V = SCORCH_MIN_V + RING_FRAME_SIZE / TEXTURE_HEIGHT;


    public RenderEntityScorch(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityScorch scorch, float entityYaw, float delta, PoseStack poseStack, MultiBufferSource bufferIn, int packedLight) {
        poseStack.pushPose();
        float opacity = (scorch.controlled.getTimer() + delta) * 0.1F;
        if (opacity > 1) {
            opacity = 1;
        }
        VertexConsumer vertexConsumer = bufferIn.getBuffer(EERenderType.getGlowingEffect(TEXTURE));
        drawScorch(scorch, delta, poseStack, vertexConsumer, packedLight, opacity);
        poseStack.popPose();
    }

    private void drawScorch(EntityScorch scorch, float delta, PoseStack matrixStack, VertexConsumer builder, int packedLightIn, float opacity) {
        Level world = scorch.getCommandSenderWorld();
        double ex = scorch.xOld + (scorch.getX() - scorch.xOld) * delta;
        double ey = scorch.yOld + (scorch.getY() - scorch.yOld) * delta;
        double ez = scorch.zOld + (scorch.getZ() - scorch.zOld) * delta;
        int minX = Mth.floor(ex - LINGER_RADIUS);
        int maxX = Mth.floor(ex + LINGER_RADIUS);
        int minY = Mth.floor(ey - LINGER_RADIUS);
        int maxY = Mth.floor(ey);
        int minZ = Mth.floor(ez - LINGER_RADIUS);
        int maxZ = Mth.floor(ez + LINGER_RADIUS);
        for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
            BlockState block = world.getBlockState(pos.below());
            if (!block.isAir() && world.getMaxLocalRawBrightness(pos) > 3) {
                drawScorchBlock(world, block, pos, ex, ey, ez, matrixStack, builder, packedLightIn, opacity);
            }
        }
    }


    private void drawScorchBlock(Level world, BlockState block, BlockPos pos, double ex, double ey, double ez, PoseStack matrixStack, VertexConsumer builder, int packedLightIn, float opacity) {
        PoseStack.Pose matrixstack$entry = matrixStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        if (block.isRedstoneConductor(world, pos)) {
            int bx = pos.getX(), by = pos.getY(), bz = pos.getZ();
            //渲染偏移(用于适应在不同高低差)
            VoxelShape shape = block.getBlockSupportShape(world, pos);
            float minX;
            float maxX;
            float y;
            float minZ;
            float maxZ;
            if (!shape.isEmpty()) {//fix #6 极端情况下导致崩溃
                AABB aabb = shape.bounds();
                minX = (float) (bx + aabb.minX - ex);
                maxX = (float) (bx + aabb.maxX - ex);
                y = (float) (by + aabb.minY - ey + 0.015625f);
                minZ = (float) (bz + aabb.minZ - ez);
                maxZ = (float) (bz + aabb.maxZ - ez);
                float minU = (minX / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                float maxU = (maxX / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_U - SCORCH_MIN_U) + SCORCH_MIN_U;
                float minV = (minZ / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
                float maxV = (maxZ / 2f / LINGER_RADIUS + 0.5f) * (SCORCH_MAX_V - SCORCH_MIN_V) + SCORCH_MIN_V;
                drawVertex(matrix4f, matrix3f, builder, minX, y, minZ, minU, minV, opacity, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, minX, y, maxZ, minU, maxV, opacity, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, maxX, y, maxZ, maxU, maxV, opacity, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, maxX, y, minZ, maxU, minV, opacity, packedLightIn);
            }
        }
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityScorch entity) {
        return TEXTURE;
    }
}
