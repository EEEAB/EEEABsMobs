package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalLaser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class RenderImmortalLaser extends RenderAbsBeam<EntityImmortalLaser> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/immortal_laser.png");

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
