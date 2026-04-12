package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.sever.entity.effect.EntityTelegraph;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RendererTelegraph extends EntityRenderer<EntityTelegraph> {
    private static final ResourceLocation WHITE_TEXTURE = new ResourceLocation("minecraft:textures/block/white_concrete.png");
    private static final float GROUND_LEVEL = 0.05F;
    private static final float OUTER_BOTTOM_ALPHA = 0.4F;
    private static final float INNER_BOTTOM_ALPHA = 0.3F;
    private static final float WALL_BASE_HEIGHT = 0.3F;
    private static final float WALL_MAX_HEIGHT = 1F;
    private static final float WALL_BASE_ALPHA = 0.2F;
    private static final float WALL_MAX_ALPHA = 0.6F;

    public RendererTelegraph(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityTelegraph entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.controlled == null) return;
        float progress = entity.controlled.getAnimationProgressSinSqrt(partialTick);
        float interimProgress = entity.interimControlled.getAnimationProgressSinSqrt(partialTick);
        float radius = entity.getRadius();
        int color = entity.getColor();

        float[] rgb = extractRgb(color);
        float red = rgb[0];
        float green = rgb[1];
        float blue = rgb[2];

        if (interimProgress > 0) {
            red = Math.min(1f, red + interimProgress);
            green = Math.min(1f, green + interimProgress);
            blue = Math.min(1f, blue + interimProgress);
        }

        float controlledFraction = entity.controlled.getAnimationFraction(partialTick);
        float controlledTimer = controlledFraction * entity.controlled.getDuration();
        float controlledFadeIn = Math.min(controlledTimer / 3.0f, 1.0f);
        float interimFraction = entity.interimControlled.getAnimationFraction(partialTick);
        float interimTimer = interimFraction * entity.interimControlled.getDuration();
        float remaining = entity.interimControlled.getDuration() - interimTimer;
        float interimFadeOut = Math.min(remaining / 3.0f, 1.0f);
        float fadeFactor = controlledFadeIn * interimFadeOut;

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(WHITE_TEXTURE));
        //外圈
        float outerBottomAlpha = OUTER_BOTTOM_ALPHA * fadeFactor;
        renderSquare(poseStack, consumer, packedLight, radius, GROUND_LEVEL, red, green, blue, outerBottomAlpha);

        //外圈垂直面
        float wallHeight = WALL_BASE_HEIGHT + (WALL_MAX_HEIGHT - WALL_BASE_HEIGHT) * progress;
        float wallAlphaBottom = WALL_BASE_ALPHA + (WALL_MAX_ALPHA - WALL_BASE_ALPHA) * progress;
        float wallAlpha = wallAlphaBottom * fadeFactor;
        renderOuterWalls(poseStack, consumer, packedLight, radius, GROUND_LEVEL, wallHeight, wallAlpha, red, green, blue);

        //内圈
        float innerRadius = radius * progress;
        if (innerRadius > 0.01F) {
            renderSquare(poseStack, consumer, packedLight, innerRadius, GROUND_LEVEL, red, green, blue, INNER_BOTTOM_ALPHA);
        }
    }

    private static float[] extractRgb(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return new float[]{r / 255.0f, g / 255.0f, b / 255.0f};
    }


    //渲染平面的方形底面
    private static void renderSquare(PoseStack poseStack, VertexConsumer consumer, int packedLight,
                                     float size, float y, float red, float green, float blue, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();
        float nx = 0, ny = 1, nz = 0;
        consumer.vertex(matrix, -size, y, -size).color(red, green, blue, alpha).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, -size, y, size).color(red, green, blue, alpha).uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, size, y, size).color(red, green, blue, alpha).uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, size, y, -size).color(red, green, blue, alpha).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
    }


    //渲染四个垂直面
    private static void renderOuterWalls(PoseStack poseStack, VertexConsumer consumer, int packedLight,
                                         float radius, float yBase, float height, float alphaBottom,
                                         float red, float green, float blue) {
        float[][] faceNormals = {{-1, 0, 0}, {1, 0, 0}, {0, 0, -1}, {0, 0, 1}};
        for (int i = 0; i < 4; i++) {
            float nx = faceNormals[i][0];
            float ny = faceNormals[i][1];
            float nz = faceNormals[i][2];
            float x1, z1, x2, z2;
            switch (i) {
                case 0:
                    x1 = radius;
                    z1 = -radius;
                    x2 = radius;
                    z2 = radius;
                    break;
                case 1:
                    x1 = -radius;
                    z1 = -radius;
                    x2 = -radius;
                    z2 = radius;
                    break;
                case 2:
                    x1 = -radius;
                    z1 = radius;
                    x2 = radius;
                    z2 = radius;
                    break;
                case 3:
                    x1 = -radius;
                    z1 = -radius;
                    x2 = radius;
                    z2 = -radius;
                    break;
                default:
                    continue;
            }
            renderVerticalFace(poseStack, consumer, packedLight, x1, z1, x2, z2, nx, ny, nz, yBase, height, alphaBottom, red, green, blue);
        }
    }

    //渲染单个垂直矩形面
    private static void renderVerticalFace(PoseStack poseStack, VertexConsumer consumer, int packedLight,
                                           float x1, float z1, float x2, float z2,
                                           float nx, float ny, float nz,
                                           float yBase, float height, float alphaBottom,
                                           float red, float green, float blue) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();
        float alphaTop = 0F;
        consumer.vertex(matrix, x1, yBase, z1).color(red, green, blue, alphaBottom).uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, x2, yBase, z2).color(red, green, blue, alphaBottom).uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, x2, yBase + height, z2).color(red, green, blue, alphaTop).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, x1, yBase + height, z1).color(red, green, blue, alphaTop).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTelegraph entity) {
        return WHITE_TEXTURE;
    }
}
