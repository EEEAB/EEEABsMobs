package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.effects.ModelImmortalFireball;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalFireball;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderImmortalFireball extends EntityRenderer<EntityImmortalFireball> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/immortal_fireball.png");
    private final ModelImmortalFireball model;

    public RenderImmortalFireball(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelImmortalFireball(context.bakeLayer(EMModelLayer.IMMORTAL_FIREBALL));
    }

    @Override
    public void render(EntityImmortalFireball entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        poseStack.translate(0, 1.25F, 0);
        poseStack.scale(-0.8F, -0.8F, 0.8F);
        float f = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        this.model.setupAnim(entity, 0.0F, 0.0F, partialTicks, f, f1);
        this.model.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    protected int getBlockLightLevel(EntityImmortalFireball entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalFireball entity) {
        return TEXTURE;
    }
}
