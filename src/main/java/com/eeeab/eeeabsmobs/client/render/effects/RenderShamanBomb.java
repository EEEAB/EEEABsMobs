package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.impl.projectile.EntityShamanBomb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderShamanBomb extends EntityRenderer<EntityShamanBomb> {
    private final float SCALE = 2.0F;
    private static final ResourceLocation N_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/projectile/shaman_bomb.png");
    private static final ResourceLocation D_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/projectile/shaman_bomb_dangerous.png");
    private final RandomSource random = RandomSource.create();

    public RenderShamanBomb(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(EntityShamanBomb entity, BlockPos pos) {
        return 15;
    }

    @Override
    public Vec3 getRenderOffset(EntityShamanBomb entity, float partialTicks) {
        double d0 = 0.005D;
        return new Vec3(this.random.nextGaussian() * d0, 0.0D, this.random.nextGaussian() * d0);
    }

    @Override
    public void render(EntityShamanBomb entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        int timer = entity.scaleControlled.getTimer();
        float scale = timer * 0.1F;
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        drawCircle(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);

    }

    private void drawCircle(EntityShamanBomb entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        PoseStack.Pose posestack$pose = matrixStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer;
        if (!entity.isDangerous()) {
            vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(N_TEXTURE));
        } else {
            vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(D_TEXTURE));
        }
        vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 0.0F, 1, 0, 0);
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix4f, Matrix3f matrix3f, int lightMapUV, float x, int y, int u, int v) {
        consumer.vertex(matrix4f, x - 0.5F, (float) y - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float) u, (float) v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lightMapUV).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityShamanBomb entity) {
        return entity.isDangerous() ? D_TEXTURE : N_TEXTURE;
    }
}
