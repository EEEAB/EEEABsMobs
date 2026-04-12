package com.eeeab.eeeabsmobs.client.render.block;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.block.SlidingDoorModel;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.sever.block.BlockSlidingDoor;
import com.eeeab.eeeabsmobs.sever.entity.block.EntitySlidingDoorBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class RenderSlidingDoor implements BlockEntityRenderer<EntitySlidingDoorBlock> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/block/sliding_door.png");
    private static final ResourceLocation UNLOCK_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/block/sliding_door_unlock.png");
    private final SlidingDoorModel model;

    public RenderSlidingDoor(BlockEntityRendererProvider.Context context) {
        model = new SlidingDoorModel(context.bakeLayer(ModModelLayer.SLIDING_DOOR));
    }

    @Override
    public void render(EntitySlidingDoorBlock be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();
        if (!(state.getBlock() instanceof BlockSlidingDoor)) return;
        if (state.getValue(BlockSlidingDoor.PART) == BlockSlidingDoor.Part.CENTER && state.getValue(BlockSlidingDoor.Y_OFFSET) == 0) {
            poseStack.pushPose();
            Direction facing = state.getValue(BlockSlidingDoor.FACING);
            boolean locked = state.getValue(BlockSlidingDoor.LOCKED);
            if (!(facing == Direction.UP || facing == Direction.DOWN)) {
                poseStack.translate(0.5F, 1.501F, 0.5F);
            }
            poseStack.mulPose(facing.getRotation());
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            model.animate(be, partialTick);
            model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(locked ? TEXTURE : UNLOCK_TEXTURE)), packedLight, packedOverlay, 1, 1, 1, 1);
            poseStack.popPose();
        }
    }
}