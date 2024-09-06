package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.client.render.FlatTextureRenderer;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityAlienPortal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderAlienPortal extends FlatTextureRenderer<EntityAlienPortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/alien_portal.png");
    private static final float TEXTURE_WIDTH = 320;
    private static final float TEXTURE_HEIGHT = 64;
    private static final float SIZE = 1.9F;

    public RenderAlienPortal(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAlienPortal entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityAlienPortal portal, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer consumer = bufferIn.getBuffer(EMRenderType.getGlowingEffect(TEXTURE));
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F - portal.getYRot()));
        matrixStackIn.translate(0.0, 0.001F, 0.0);
        matrixStackIn.scale(-SIZE, -SIZE, -SIZE);
        float minU = 0 + 64F / TEXTURE_WIDTH * portal.getPhase();
        float minV = 0;
        float maxU = minU + 64F / TEXTURE_WIDTH;
        float maxV = minV + 64F / TEXTURE_HEIGHT;
        renderFlatQuad(matrixStackIn, consumer, packedLightIn, 1F, minU, minV, maxU, maxV, SIZE, Quad.XZ);
        matrixStackIn.popPose();
    }
}
