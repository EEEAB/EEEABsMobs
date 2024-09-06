package com.eeeab.eeeabsmobs.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * 通用渲染平面四边形渲染器
 *
 * @author EEEAB
 */
@OnlyIn(Dist.CLIENT)
public abstract class FlatTextureRenderer<T extends Entity> extends EntityRenderer<T> {

    protected FlatTextureRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected enum Quad {
        XY, XZ, YZ
    }

    protected void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ)
                .color(1F, 1F, 1F, alpha)
                .uv(textureX, textureY)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLightIn)
                .normal(normals, 0F, 1F, 0F)
                .endVertex();
    }

    protected void renderFlatQuad(PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float alpha, float minU, float minV, float maxU, float maxV, float size, @NotNull Quad quadType) {
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        switch (quadType) {
            case XZ -> {
                drawVertex(matrix4f, matrix3f, builder, -size, 0, -size, minU, minV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, -size, 0, size, minU, maxV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, size, 0, size, maxU, maxV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, size, 0, -size, maxU, minV, alpha, packedLightIn);
            }
            case XY -> {
                drawVertex(matrix4f, matrix3f, builder, -size, -size, 0, minU, minV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, -size, size, 0, minU, maxV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, size, size, 0, maxU, maxV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, size, -size, 0, maxU, minV, alpha, packedLightIn);
            }
            case YZ -> {
                drawVertex(matrix4f, matrix3f, builder, 0, -size, -size, minU, minV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, 0, -size, size, minU, maxV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, 0, size, size, maxU, maxV, alpha, packedLightIn);
                drawVertex(matrix4f, matrix3f, builder, 0, size, -size, maxU, minV, alpha, packedLightIn);
            }
        }
    }
}
