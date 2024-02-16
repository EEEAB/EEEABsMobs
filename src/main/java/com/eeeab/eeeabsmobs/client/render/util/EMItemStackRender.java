package com.eeeab.eeeabsmobs.client.render.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.block.ModelBlockTombstone;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EMItemStackRender extends BlockEntityWithoutLevelRenderer {
    private static final ModelBlockTombstone TOMBSTONE_MODEL = new ModelBlockTombstone();
    private static final ResourceLocation TOMBSTONE_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/block/tombstone.png");

    public EMItemStackRender() {
        super(null, null);
    }


    @Override
    public void renderByItem(ItemStack itemStackIn, ItemTransforms.TransformType transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        //VertexConsumer vertexConsumer;
        if (itemStackIn.getItem() == ItemInit.findBlockItem(BlockInit.TOMBSTONE)) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 1.5F, 0.5F);
            matrixStackIn.scale(1.0F, -1.0F, -1.0F);
            TOMBSTONE_MODEL.resetToDefaultPose();
            TOMBSTONE_MODEL.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TOMBSTONE_TEXTURE)), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            matrixStackIn.popPose();
        }
    }
}
