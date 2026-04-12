package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityGuardianLaser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderGuardianLaser extends RenderAbsBeam<EntityGuardianLaser> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/guardian_laser.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private boolean playerView;

    public RenderGuardianLaser(EntityRendererProvider.Context mgr) {
        super(mgr, 1F, 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGuardianLaser entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityGuardianLaser laser, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        playerView = laser.getOwner() instanceof Player && Minecraft.getInstance().player == laser.getOwner() && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        EntityGuardianLaser.UserType type = laser.getUserType();
        if (type == EntityGuardianLaser.UserType.RELIC_ANNIHILATOR && !laser.scaleControlled.isEnd() && laser.displayControlled.isEnd()) {
            quadRadius = type.radius * 1.75F;
            beamRadius = type.beamRadius * 1.5F;
        } else {
            quadRadius = type.radius;
            beamRadius = type.beamRadius;
        }
        super.render(laser, entityYaw, delta, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, boolean inGround) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        float size = quadRadius + (inGround ? frame % 4 * 0.2F : 0);
        drawVertex(size, matrixStackIn, builder, packedLightIn, size, -size, minU, minV, maxV, maxU);
    }

    @Override
    protected void renderStart(EntityGuardianLaser laser, int frame, PoseStack matrixStackIn, VertexConsumer builder, float delta, int packedLightIn) {
        if (playerView) return;
        super.renderStart(laser, frame, matrixStackIn, builder, delta, packedLightIn);
    }

    @Override
    protected void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        float offset = playerView ? -1 : 0;
        drawVertex(length, matrixStackIn, builder, packedLightIn, beamRadius, offset, minU, minV, maxV, maxU);
    }
}
