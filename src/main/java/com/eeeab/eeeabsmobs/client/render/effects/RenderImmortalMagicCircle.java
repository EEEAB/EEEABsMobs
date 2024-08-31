package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalMagicCircle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderImmortalMagicCircle extends EntityRenderer<EntityImmortalMagicCircle> {
    private static final ResourceLocation SPEED_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/magic_circle/speed.png");
    private static final ResourceLocation STRENGTH_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/magic_circle/strength.png");
    private static final ResourceLocation NONE_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/magic_circle/none.png");

    public RenderImmortalMagicCircle(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityImmortalMagicCircle entity, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer consumer = bufferIn.getBuffer(EMRenderType.getGlowingEffect(getTextureLocation(entity)));
        matrixStackIn.pushPose();
        float speed = (entity.tickCount + delta) * entity.getSpeed();
        float scale = entity.NO ? entity.getScale() * entity.processController.getAnimationFraction(delta) : entity.getScale();
        float alpha = entity.NO ? 1F : entity.processController.getAnimationFraction(delta);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-entity.getYaw() + 180F + speed));
        matrixStackIn.translate(0.0, 0.001F, 0.0);
        matrixStackIn.scale(-scale, -scale, -scale);
        this.renderFlatQuad(matrixStackIn, consumer, packedLightIn, alpha);
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalMagicCircle entity) {
        switch (entity.getMagicCircleType()) {
            case SPEED -> {
                return SPEED_TEXTURE;
            }
            case STRENGTH -> {
                return STRENGTH_TEXTURE;
            }
            default -> {
                return NONE_TEXTURE;
            }
        }
    }

    private void renderFlatQuad(PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float alpha) {
        float minU = 0;
        float minV = 0;
        float maxU = 1F;
        float maxV = 1F;
        float size = 1F;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -size, 0, -size, minU, minV, packedLightIn, alpha);
        drawVertex(matrix4f, matrix3f, builder, -size, 0, size, minU, maxV, packedLightIn, alpha);
        drawVertex(matrix4f, matrix3f, builder, size, 0, size, maxU, maxV, packedLightIn, alpha);
        drawVertex(matrix4f, matrix3f, builder, size, 0, -size, maxU, minV, packedLightIn, alpha);
    }

    public static void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, int packedLightIn, float alpha) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1F, 1F, 1F, alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0F, 1F, 0F).endVertex();
    }
}
