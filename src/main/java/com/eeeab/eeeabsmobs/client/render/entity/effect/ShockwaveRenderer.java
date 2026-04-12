package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class ShockwaveRenderer /*extends EntityRenderer<EntityShockwave> */{

    // 渲染方形轮廓（不跟随相机旋转）
    private void renderSquareOutline(PoseStack poseStack, MultiBufferSource buffer,
                                     float size, float alpha, int packedLight) {
        // 使用线条RenderType来绘制轮廓
        VertexConsumer consumer = buffer.getBuffer(RenderType.LINES);
        Matrix4f matrix = poseStack.last().pose();
        // 计算方形轮廓的四个角
        float halfSize = size * 0.5f;
        // 线条颜色（橙色，带透明度）
        int r = 255;
        int g = 100;
        int b = 0;
        int a = (int) (alpha * 255);
        // 绘制方形的四条边
        // 第一条边：从 (-halfSize, 0, -halfSize) 到 (halfSize, 0, -halfSize)
        consumer.vertex(matrix, -halfSize, 0, -halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)  // 线条不需要法线，但可以提供
                .endVertex();
        consumer.vertex(matrix, halfSize, 0, -halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)
                .endVertex();
        // 第二条边：从 (halfSize, 0, -halfSize) 到 (halfSize, 0, halfSize)
        consumer.vertex(matrix, halfSize, 0, -halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)
                .endVertex();
        consumer.vertex(matrix, halfSize, 0, halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)
                .endVertex();
        // 第三条边：从 (halfSize, 0, halfSize) 到 (-halfSize, 0, halfSize)
        consumer.vertex(matrix, halfSize, 0, halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)
                .endVertex();
        consumer.vertex(matrix, -halfSize, 0, halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)
                .endVertex();
        // 第四条边：从 (-halfSize, 0, halfSize) 到 (-halfSize, 0, -halfSize)
        consumer.vertex(matrix, -halfSize, 0, halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)
                .endVertex();
        consumer.vertex(matrix, -halfSize, 0, -halfSize)
                .color(r, g, b, a)
                .normal(0, 1, 0)
                .endVertex();
    }
}