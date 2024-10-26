package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalLaser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class RenderImmortalLaser extends RenderAbsBeam<EntityImmortalLaser> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/immortal_laser.png");

    public RenderImmortalLaser(EntityRendererProvider.Context mgr) {
        super(mgr, 1F, 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalLaser entity) {
        return TEXTURE;
    }


    @Override
    protected void renderEnd(EntityImmortalLaser laser, int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, float delta, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees((laser.tickCount + delta) * 20 % 360));
        if (side != null) matrixStackIn.translate(0, side.getStepY() * 0.25F, 0);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, false);
        matrixStackIn.popPose();
    }
}
