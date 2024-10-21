package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianLaser;
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
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderGuardianLaser extends RenderAbsLightBeam<EntityGuardianLaser> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/effects/guardian_laser.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float GUARDIAN_START_RADIUS = 1.09f;
    private static final float PLAYER_START_RADIUS = 0.8f;
    private static final float GUARDIAN_BEAM_RADIUS = 0.9f;
    private static final float PLAYER_BEAM_RADIUS = 0.6f;
    private boolean playerView;
    private boolean isPlayer;

    public RenderGuardianLaser(EntityRendererProvider.Context mgr) {
        super(mgr, GUARDIAN_START_RADIUS, GUARDIAN_BEAM_RADIUS);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGuardianLaser entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityGuardianLaser laser, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        playerView = laser.caster instanceof Player && Minecraft.getInstance().player == laser.caster && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        isPlayer = laser.isPlayer() || laser.caster instanceof Player;
        super.render(laser, entityYaw, delta, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, boolean inGround) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        float SIZE = !isPlayer ? GUARDIAN_START_RADIUS + (inGround ? 0.2F : 0) : PLAYER_START_RADIUS;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -SIZE, -SIZE, 0, minU, minV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, SIZE, 0, minU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, SIZE, 0, maxU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, -SIZE, 0, maxU, minV, packedLightIn);
    }

    @Override
    protected void renderStart(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        if (playerView) return;
        super.renderStart(frame, matrixStackIn, builder, packedLightIn);
    }

    @Override
    protected void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose matrix$stack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrix$stack$entry.pose();
        Matrix3f matrix3f = matrix$stack$entry.normal();
        float offset = playerView ? -1 : 0;
        float SIZE = isPlayer ? PLAYER_BEAM_RADIUS : GUARDIAN_BEAM_RADIUS;
        drawVertex(matrix4f, matrix3f, builder, -SIZE, offset, 0, minU, minV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -SIZE, length, 0, minU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, length, 0, maxU, maxV, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, SIZE, offset, 0, maxU, minV, packedLightIn);
    }
}
