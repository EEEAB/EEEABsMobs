package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelGulingSentinelHeavy;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerBreath;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityGulingSentinelHeavy;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderGulingSentinelHeavy extends MobRenderer<EntityGulingSentinelHeavy, ModelGulingSentinelHeavy> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/heavy/guling_sentinel_heavy.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/heavy/guling_sentinel_heavy_glow.png");
    private static final ResourceLocation HOT_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/heavy/guling_sentinel_heavy_hot.png");

    public RenderGulingSentinelHeavy(EntityRendererProvider.Context context) {
        super(context, new ModelGulingSentinelHeavy(context.bakeLayer(EMModelLayer.GULING_SENTINEL_HEAVY)), 1.2F);
        this.addLayer(new LayerBreath<>(this, GLOW_LAYER, 0.05F, e -> e.isGlow() && e.getAnimation() != e.activeAnimation, 0.075F));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, 0.5F) {
            @Override
            protected float getBrightness(EntityGulingSentinelHeavy entity) {
                float timer = entity.glowControlled.getPrevTimer();
                return timer * 0.05F;
            }
        });
        this.addLayer(new LayerGlow<>(this, HOT_LAYER, 1F) {
            @Override
            protected float getBrightness(EntityGulingSentinelHeavy entity) {
                return entity.hotControlled.getAnimationFraction();
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGulingSentinelHeavy sentinelHeavy) {
        return TEXTURE;
    }

    @Override
    public void render(EntityGulingSentinelHeavy entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        //if (!entity.hotControlled.isStop()) {
        //    entity.hand[0] = ModelPartUtils.getWorldPosition(entity, entityYaw, this.getModel().finger4);
        //    entity.hand[1] = ModelPartUtils.getWorldPosition(entity, entityYaw, this.getModel().finger1);
        //}
    }
}
