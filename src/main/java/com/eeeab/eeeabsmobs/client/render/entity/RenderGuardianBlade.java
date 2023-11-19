package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelGuardianBlade;
import com.eeeab.eeeabsmobs.client.render.EERenderType;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityGuardianBlade;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderGuardianBlade extends EntityRenderer<EntityGuardianBlade> {
    private final ModelGuardianBlade MODEL = new ModelGuardianBlade();
    private final ResourceLocation[] LOCATIONS = new ResourceLocation[6];

    public RenderGuardianBlade(EntityRendererProvider.Context pContext) {
        super(pContext);
        //初始化文件地址
        for (int i = 0; i < LOCATIONS.length; i++) {
            LOCATIONS[i] = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/guardian_blade/0" + (i + 1) + ".png");//TODO 待更新纹理
        }
    }

    @Override
    public void render(EntityGuardianBlade blade, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 1.5F, 0.5F);
        pPoseStack.scale(1F, -1.0F, -1.0F);
        int timer = blade.alphaControlled.getPrevTimer();
        float alpha = 1.0F - (timer * 0.1F);
        MODEL.renderToBuffer(pPoseStack, pBuffer.getBuffer(EERenderType.getGlowingCutOutEffect(getTextureLocation(blade))), pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGuardianBlade blade) {
        return LOCATIONS[Mth.clamp((int) (blade.tickCount * 0.5 % LOCATIONS.length), 0, LOCATIONS.length - 1)];
    }

    @Override
    protected int getBlockLightLevel(EntityGuardianBlade pEntity, BlockPos pPos) {
        return 15;
    }
}
