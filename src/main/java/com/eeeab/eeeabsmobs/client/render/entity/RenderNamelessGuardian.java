package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGuardianCore;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGuardianWhitening;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Creeper;

public class RenderNamelessGuardian extends MobRenderer<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation NAMELESS_GUARDIAN_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian/nameless_guardian.png");

    public RenderNamelessGuardian(EntityRendererProvider.Context context) {
        super(context, new ModelNamelessGuardian(), 1.5F);
        this.addLayer(new LayerGuardianWhitening(this));
        this.addLayer(new LayerGuardianCore(this));
        this.addLayer(new LayerGlow<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian/nameless_guardian_eyes.png")));
        this.addLayer(new LayerGlow<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian/nameless_guardian_shoot.png")) {
            @Override
            public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.inShoot && entity.isAlive())
                    super.render(stack, bufferSource, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        });
    }

    @Override
    protected float getFlipDegrees(EntityNamelessGuardian guardian) {
        return 0;//获取死亡翻转角度
    }

    @Override
    protected int getBlockLightLevel(EntityNamelessGuardian guardian, BlockPos blockPos) {
        return 15;
    }

    @Override
    protected void scale(EntityNamelessGuardian guardian, PoseStack poseStack, float partialTickTime) {
        float f = guardian.getExplodeCoefficient(partialTickTime);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.2F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        poseStack.scale(f2, f3, f2);
    }


    //@Override
    //protected float getWhiteOverlayProgress(EntityNamelessGuardian guardian, float partialTicks) {
    //    float f = guardian.getExplodeCoefficient(partialTicks);
    //    return Mth.clamp(f, 0.0F, 1.0F);
    //}


    @Override
    public ResourceLocation getTextureLocation(EntityNamelessGuardian guardian) {
        return NAMELESS_GUARDIAN_TEXTURE;
    }
}
