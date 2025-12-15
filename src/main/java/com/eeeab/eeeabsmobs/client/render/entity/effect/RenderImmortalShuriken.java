package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.effect.ModelImmortalShuriken;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityImmortalShuriken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderImmortalShuriken extends EntityRenderer<EntityImmortalShuriken> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/immortal_shuriken.png");
    private final ModelImmortalShuriken model;

    public RenderImmortalShuriken(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelImmortalShuriken(context.bakeLayer(ModModelLayer.IMMORTAL_SHURIKEN));
    }

    @Override
    public void render(EntityImmortalShuriken entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        poseStack.translate(0F, 0.93875F, 0F);
        poseStack.scale(-0.5F, -0.5F, 0.5F);
        float f = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        VertexConsumer buffer = bufferIn.getBuffer(ModRenderType.getGlowingCutOutEffect(getTextureLocation(entity), false));
        this.model.setupAnim(entity, 0.0F, 0.0F, partialTicks, f, f1);
        this.model.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalShuriken entity) {
        return TEXTURE;
    }
}
