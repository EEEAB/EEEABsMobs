package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.effects.ModelGuardianBlade;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianBlade;
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
        this.model = new ModelGuardianBlade(context.bakeLayer(EMModelLayer.GUARDIAN_BLADE));
        //初始化文件地址
        for (int i = 0; i < TEXTURES.length; i++) {
            TEXTURES[i] = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/guardian_blade/gb_" + (i + 1) + ".png");
        }
    }

    @Override
    public void render(EntityGuardianBlade entity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0F, 2.0F, 0F);
        pPoseStack.scale(1F, -1.0F, -1.0F);
        float f = entity.controlled.getAnimationProgressTemporaryInvesed();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(EMRenderType.getGlowingCutOutEffect(getTextureLocation(entity))), pPackedLight, OverlayTexture.NO_OVERLAY, f, f, f, f);
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
