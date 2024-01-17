package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTester;
import com.eeeab.eeeabsmobs.client.render.layer.LayerMobModelOuter;
import com.eeeab.eeeabsmobs.sever.entity.impl.test.EntityTester;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderTester extends MobRenderer<EntityTester, ModelTester> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/tester/tester.png");

    public RenderTester(EntityRendererProvider.Context context) {
        super(context, new ModelTester(), 0.3F);
        this.addLayer(new LayerMobModelOuter<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/tester/tester_plains.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTester entity) {
        return TEXTURE;
    }

    @Override
    protected void renderNameTag(EntityTester pEntity, Component pDisplayName, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        double distance = this.entityRenderDispatcher.distanceToSqr(pEntity);
        if (ForgeHooksClient.isNameplateInRenderDistance(pEntity, distance)) {
            pDisplayName = pEntity.getDamage();
            float f = pEntity.getBbHeight() + 0.5F;
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0F, f, 0.0F);
            pMatrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pMatrixStack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            Font font = this.getFont();
            float f2 = (float) (-font.width(pDisplayName) / 2);
            font.drawInBatch(pDisplayName, f2, (float)-10, 553648127, false, matrix4f, pBuffer, true, j, pPackedLight);
            font.drawInBatch(pDisplayName, f2, (float)-10, -1, false, matrix4f, pBuffer, false, 0, pPackedLight);
            pMatrixStack.popPose();
        }
    }
}
