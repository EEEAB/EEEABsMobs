package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGuardianCore;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGuardianExplode;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderNamelessGuardian extends MobRenderer<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/nameless_guardian/nameless_guardian.png");

    public RenderNamelessGuardian(EntityRendererProvider.Context context) {
        super(context, new ModelNamelessGuardian(context.bakeLayer(EMModelLayer.NAMELESS_GUARDIAN)), 1.5F);
        this.addLayer(new LayerGuardianExplode(this));
        this.addLayer(new LayerGuardianLaser(this));
        this.addLayer(new LayerGuardianCore(this));
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
