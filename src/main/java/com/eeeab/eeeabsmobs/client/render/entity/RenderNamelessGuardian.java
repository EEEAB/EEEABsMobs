package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGuardianCore;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RenderNamelessGuardian extends MobRenderer<EntityNamelessGuardian, ModelNamelessGuardian> {
    public RenderNamelessGuardian(EntityRendererProvider.Context context) {
        super(context, new ModelNamelessGuardian(), 1.5F);
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
    public ResourceLocation getTextureLocation(EntityNamelessGuardian guardian) {
        return new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian/nameless_guardian.png");
    }
}
