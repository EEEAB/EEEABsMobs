package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.effects.ModelGrenade;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGrenade;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderGrenade extends EntityRenderer<EntityGrenade> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/grenade.png");
    private final ModelGrenade model;

    public RenderGrenade(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelGrenade(context.bakeLayer(EMModelLayer.GRENADE));
    }


    @Override
    public void render(EntityGrenade entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        float f = ((float) entity.tickCount + partialTicks) * Math.min(30, entity.tickCount * 2);
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0F, 0.5F, 0F);
        this.model.renderToBuffer(poseStack, bufferIn.getBuffer(EMRenderType.getGlowingEffect(getTextureLocation(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.model.setupAnim(entity, 0, 0, 0, Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot()), f);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGrenade entity) {
        return TEXTURE;
    }
}
