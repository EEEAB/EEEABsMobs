package com.eeeab.eeeabsmobs.client.render.block;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.block.ModelBlockTombstone;
import com.eeeab.eeeabsmobs.sever.entity.block.EntityBlockTombstone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBlockTombstone<T extends EntityBlockTombstone> implements BlockEntityRenderer<T> {
    private static final ModelBlockTombstone MODEL = new ModelBlockTombstone();
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/block/tombstone.png");


    public RenderBlockTombstone(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(T entity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.5F, 0.5F);
        matrixStackIn.scale(1.0F, -1.0F, -1.0F);
        MODEL.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }
}
