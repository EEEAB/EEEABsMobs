package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityNamelessGuardian;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderNamelessGuardian extends MobRenderer<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian.png");
    private static final ResourceLocation SHOOT_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian_shoot.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian_glow.png");

    public RenderNamelessGuardian(EntityRendererProvider.Context context) {
        super(context, new ModelNamelessGuardian(context.bakeLayer(ModModelLayer.NAMELESS_GUARDIAN)), 1.5F);
        this.addLayer(new RenderLayer<>(this) {
            @Override
            public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                float coefficient = entity.getExplodeCoefficient(partialTicks);
                if (coefficient > 0) {
                    this.getParentModel().renderToBuffer(stack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE)), packedLightIn, OverlayTexture.pack(OverlayTexture.u(Mth.clamp(coefficient, 0F, 1F)), OverlayTexture.v(false)), coefficient, coefficient, coefficient, coefficient);
                }
            }
        });
        this.addLayer(new LayerGlow<>(this, SHOOT_LAYER) {
            @Override
            protected float getBrightness(EntityNamelessGuardian entity, float partialTicks) {
                float timer = entity.accumulationControlled.getPrevTimer();
                return timer * 0.1F;
            }
        });
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, 0.8F, guardian -> !guardian.coreControlled.isStop()) {
            @Override
            protected float getBrightness(EntityNamelessGuardian entity, float partialTicks) {
                float timer = entity.coreControlled.getPrevTimer();
                return timer * 0.08F;
            }
        });
    }

    @Override
    protected float getFlipDegrees(EntityNamelessGuardian entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    protected void scale(EntityNamelessGuardian entity, PoseStack poseStack, float partialTickTime) {
        float f = entity.getExplodeCoefficient(partialTickTime);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.2F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        poseStack.scale(f2, f3, f2);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityNamelessGuardian entity) {
        return TEXTURE;
    }
}
