package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityFallingBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderFallingBlock extends EntityRenderer<EntityFallingBlock> {

    //private final BlockRenderDispatcher dispatcher;

    public RenderFallingBlock(EntityRendererProvider.Context p_174112_) {
        super(p_174112_);
        //this.dispatcher = p_174112_.getBlockRenderDispatcher();
    }

    //public void render(EntityFallingBlock p_114634_, float p_114635_, float p_114636_, PoseStack p_114637_, MultiBufferSource p_114638_, int p_114639_) {
    //    BlockState blockstate = p_114634_.getBlockState();
    //    if (blockstate.getRenderShape() == RenderShape.MODEL) {
    //        Level level = p_114634_.level();
    //        if (blockstate != level.getBlockState(p_114634_.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
    //            p_114637_.pushPose();
    //            BlockPos blockpos = BlockPos.containing(p_114634_.getX(), p_114634_.getBoundingBox().minY, p_114634_.getZ());
    //            p_114637_.translate(-0.5D, 0.0D, -0.5D);
    //            BakedModel model = this.dispatcher.getBlockModel(blockstate);
    //
    //            for (RenderType renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(p_114634_.getStartPos())), ModelData.EMPTY)) {
    //                this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, p_114637_, p_114638_.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(p_114634_.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
    //            }
    //
    //            p_114637_.popPose();
    //            super.render(p_114634_, p_114635_, p_114636_, p_114637_, p_114638_, p_114639_);
    //        }
    //    }
    //}

    @Override
    public void render(EntityFallingBlock entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (EMConfigHandler.COMMON.ENTITY.enableRenderFallingBlock.get()) {
            BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            matrixStackIn.pushPose();
            if (entityIn.getMode() == EntityFallingBlock.FallingMoveType.OVERALL_MOVE) {
                matrixStackIn.translate(-0.5f, 0, -0.5f);
                //dispatcher.renderSingleBlock(entityIn.getBlockState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
            } else {
                matrixStackIn.translate(0, 0.5f, 0);
                matrixStackIn.translate(0, Mth.lerp(partialTicks, entityIn.prevAnimY, entityIn.animY), 0);
                if (entityIn.getMode() == EntityFallingBlock.FallingMoveType.SIMULATE_RUPTURE) {
                    matrixStackIn.mulPose(entityIn.getQuaternionf());
                }
                matrixStackIn.translate(0, -1, 0);
                matrixStackIn.translate(-0.5f, -0.5f, -0.5f);
            }
            dispatcher.renderSingleBlock(entityIn.getBlockState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
            matrixStackIn.popPose();
            //super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    public ResourceLocation getTextureLocation(EntityFallingBlock p_114632_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    //public ResourceLocation getTextureLocation(EntityFallingBlock p_114632_) {
    //    return null;
    //}
}
