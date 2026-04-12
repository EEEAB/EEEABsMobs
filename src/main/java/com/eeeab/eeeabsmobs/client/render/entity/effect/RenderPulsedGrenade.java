package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.effect.ModelPulsedGrenade;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityPulsedGrenade;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderPulsedGrenade extends EntityRenderer<EntityPulsedGrenade> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/pulsed_grenade.png");
    private final ModelPulsedGrenade model;

    public RenderPulsedGrenade(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelPulsedGrenade(context.bakeLayer(ModModelLayer.PULSED_GRENADE));
    }

    @Override
    public void render(EntityPulsedGrenade entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        float f = ((float) entity.tickCount + partialTicks) * Math.min(30, entity.tickCount * 2);
        float rotationY = entity.getViewYRot(partialTicks);
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0F, 0.5F, 0F);
        this.model.setupAnim(entity, 0, 0, 0, rotationY, f);
        this.model.renderToBuffer(poseStack, bufferIn.getBuffer(ModRenderType.getGlowingEffect(getTextureLocation(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPulsedGrenade entity) {
        return TEXTURE;
    }
}
