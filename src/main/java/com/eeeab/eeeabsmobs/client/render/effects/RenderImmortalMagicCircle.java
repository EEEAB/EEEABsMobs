package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.client.render.FlatTextureRenderer;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalMagicCircle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderImmortalMagicCircle extends FlatTextureRenderer<EntityImmortalMagicCircle> {
    private static final ResourceLocation NONE_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/immortal_magic_circle/none.png");
    private static final ResourceLocation SPEED_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/immortal_magic_circle/speed.png");
    private static final ResourceLocation POWER_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/immortal_magic_circle/power.png");
    private static final ResourceLocation HARMFUL_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/immortal_magic_circle/harmful.png");
    private static final ResourceLocation BENEFICIAL_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/immortal_magic_circle/beneficial.png");

    public RenderImmortalMagicCircle(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalMagicCircle entity) {
        return switch (entity.getMagicCircleType()) {
            case SPEED -> SPEED_TEXTURE;
            case POWER -> POWER_TEXTURE;
            case HARMFUL -> HARMFUL_TEXTURE;
            case BENEFICIAL -> BENEFICIAL_TEXTURE;
            default -> NONE_TEXTURE;
        };
    }

    @Override
    public void render(EntityImmortalMagicCircle entity, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer consumer = bufferIn.getBuffer(EMRenderType.getGlowingEffect(getTextureLocation(entity)));
        matrixStackIn.pushPose();
        float speed = (entity.tickCount + delta) * entity.getSpeed();
        float scale = entity.NO ? entity.getScale() * entity.processController.getAnimationFraction(delta) : entity.getScale();
        float alpha = entity.NO ? 1F : entity.processController.getAnimationFraction(delta);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-entity.getYaw() + 180F + speed));
        matrixStackIn.translate(0.0, 0.001F, 0.0);
        matrixStackIn.scale(-scale, -scale, -scale);
        renderFlatQuad(matrixStackIn, consumer, packedLightIn, alpha, 0F, 0F, 1F, 1F, 1F, Quad.XZ);
        matrixStackIn.popPose();
    }
}
