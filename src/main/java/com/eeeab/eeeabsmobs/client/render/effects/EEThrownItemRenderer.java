package com.eeeab.eeeabsmobs.client.render.effects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

public class EEThrownItemRenderer<T extends Entity & ItemSupplier> extends ThrownItemRenderer<T> {
    private final boolean shouldWobble;
    private final boolean shouldGradient;
    private final float scale;
    private final RandomSource random = RandomSource.create();
    private final ItemRenderer itemRenderer;

    public EEThrownItemRenderer(EntityRendererProvider.Context context, float scale, boolean fullBright, boolean shouldWobble) {
        super(context, scale, fullBright);
        this.itemRenderer = context.getItemRenderer();
        this.shouldWobble = shouldWobble;
        this.shouldGradient = shouldWobble;
        this.scale = scale;
    }

    @Override
    public Vec3 getRenderOffset(T entity, float partialTicks) {
        if (shouldWobble) {
            double d0 = 0.01D;
            return new Vec3(this.random.nextGaussian() * d0, 0.0D, this.random.nextGaussian() * d0);
        }
        return super.getRenderOffset(entity, partialTicks);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (shouldGradient) {
            if (entity.tickCount <= (scale * 10) / 2) {
                matrixStack.pushPose();
                matrixStack.scale(entity.tickCount * 0.2F, entity.tickCount * 0.2F, entity.tickCount * 0.2F);
                matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
                matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, entity.level(), entity.getId());
                matrixStack.popPose();
            } else {
                super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            }
        } else {
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        }
    }
}
