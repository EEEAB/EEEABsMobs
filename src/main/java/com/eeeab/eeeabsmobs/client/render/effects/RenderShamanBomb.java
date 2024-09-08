package com.eeeab.eeeabsmobs.client.render.effects;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.FlatTextureRenderer;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityShamanBomb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderShamanBomb extends FlatTextureRenderer<EntityShamanBomb> {
    private static final float SCALE = 2.0F;
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
    public ResourceLocation getTextureLocation(EntityShamanBomb entity) {
        return entity.isDangerous() ? D_TEXTURE : N_TEXTURE;
    }

    @Override
    public void render(EntityShamanBomb entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        float fraction = entity.scaleControlled.getAnimationFraction(partialTicks);
        float scale = SCALE * fraction;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0F, 0.5F - 0.5F * fraction, 0F);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
        renderFlatQuad(matrixStack, vertexconsumer, packedLight, 1F, 0F, 0F, 1F, 1F, 1F, Quad.XY);
        matrixStack.popPose();
    }

    @Override
    protected void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        offsetX = offsetX < 0 ? 0 : offsetX;
        offsetY = offsetY < 0 ? 0 : offsetY;
        vertexBuilder.vertex(matrix, offsetX - 0.5F, offsetY - 0.25F, offsetZ)
                .color(1F, 1F, 1F, alpha)
                .uv(textureX, textureY)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLightIn)
                .normal(normals, 0F, 1F, 0F)
                .endVertex();
    }
}
