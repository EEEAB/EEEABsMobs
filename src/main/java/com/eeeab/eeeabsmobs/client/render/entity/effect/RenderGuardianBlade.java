package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.effect.ModelGuardianBlade;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityGuardianBlade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderGuardianBlade extends EntityRenderer<EntityGuardianBlade> {
    private final ModelGuardianBlade model;
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[6];

    public RenderGuardianBlade(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelGuardianBlade(context.bakeLayer(ModModelLayer.GUARDIAN_BLADE));
        //初始化文件地址
        for (int i = 0; i < TEXTURES.length; i++) {
            TEXTURES[i] = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/guardian_blade/index_" + (i + 1) + ".png");
        }
    }

    @Override
    public void render(EntityGuardianBlade entity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0F, 1.5F, 0F);
        pPoseStack.scale(0.65F, -1.0F, -0.65F);
        float animationProgress = Math.min(entity.controlled.getAnimationFraction(pPartialTick) + 0.1F, 1F);
        float f;
        if (animationProgress > 0.5) {
            f = 1.0F - (animationProgress - 0.5F) * 2.0F;
        } else {
            f = 1.0F;
        }
        pPoseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(ModRenderType.getGlowingCutOutEffect(getTextureLocation(entity))), pPackedLight, OverlayTexture.NO_OVERLAY, f, f, f, f);
        pPoseStack.popPose();
        super.render(entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGuardianBlade entity) {
        return TEXTURES[Mth.clamp((int) (entity.tickCount * 0.5 % TEXTURES.length), 0, TEXTURES.length - 1)];
    }

    @Override
    protected int getBlockLightLevel(EntityGuardianBlade entity, BlockPos pos) {
        return 15;
    }
}
