package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityInfraredRay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderInfraredRay extends RenderAbsBeam<EntityInfraredRay> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/infrared_ray.png");
    private static final ResourceLocation FLASH_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/infrared_ray_flash.png");

    public RenderInfraredRay(EntityRendererProvider.Context mgr) {
        super(mgr, 0.15F, 0.15F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityInfraredRay entity) {
        return entity.isBlinking() ? FLASH_TEXTURE : TEXTURE;
    }

    @Override
    protected void renderEnd(EntityInfraredRay entity, int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, float delta, int packedLightIn) {
        side = null;
        super.renderEnd(entity, frame, side, matrixStackIn, builder, delta, packedLightIn);
    }

    @Override
    protected void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1F, 1F, 1F, 0.75F).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
