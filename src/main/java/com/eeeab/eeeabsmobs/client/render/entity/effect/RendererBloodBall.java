package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.effect.ModelBloodBall;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityBloodBall;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RendererBloodBall extends EntityRenderer<EntityBloodBall> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/blood_ball.png");
    private final ModelBloodBall model;

    public RendererBloodBall(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelBloodBall(context.bakeLayer(ModModelLayer.BLOOD_BALL));
    }

    @Override
    public void render(EntityBloodBall entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float scale = 2F + entity.getPower() * 0.1F;
        poseStack.pushPose();
        float progress = entity.scaleControlled.getAnimationFraction(partialTick);
        float f = ((float) entity.tickCount + partialTick) * Mth.clamp(2F * progress, 0.1F, 1F);
        VertexConsumer vertexconsumer = buffer.getBuffer(ModRenderType.entityTranslucent(TEXTURE));
        poseStack.translate(0, 0.5F, 0);
        poseStack.scale(scale * progress, scale * progress, scale * progress);
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f * 0.1F) * 180.0F));
        this.model.ball_3.render(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f * 0.1F) * 180.0F));
        this.model.ball_2.render(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f * 0.1F) * 180.0F));
        this.model.ball_1.render(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        this.model.setupAnim(entity, 0, 0, 0, Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot()), f);
        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBloodBall entity) {
        return TEXTURE;
    }
}
