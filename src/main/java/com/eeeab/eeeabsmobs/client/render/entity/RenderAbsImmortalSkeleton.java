package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerOuter;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalKnight;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalSkeleton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderAbsImmortalSkeleton extends MobRenderer<EntityAbsImmortalSkeleton, ModelAbsImmortalSkeleton> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/immortal_skeleton.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/immortal_skeleton_glow.png");
    private static final ResourceLocation ARMOR_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/immortal_skeleton_armor.png");
    private static final ResourceLocation PLAINS_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/immortal_skeleton_plains.png");

    public RenderAbsImmortalSkeleton(EntityRendererProvider.Context context) {
        super(context, new ModelAbsImmortalSkeleton(), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER));
        this.addLayer(new LayerOuter<>(this, ARMOR_LAYER, false, entity -> entity instanceof EntityImmortalKnight));
        this.addLayer(new LayerOuter<>(this, PLAINS_LAYER, true, entity -> entity instanceof EntityImmortalSkeleton));
    }

    @Override
    protected float getFlipDegrees(EntityAbsImmortalSkeleton entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    protected void scale(EntityAbsImmortalSkeleton entity, PoseStack poseStack, float partialTickTime) {
        if (entity instanceof EntityImmortalKnight) {
            poseStack.scale(1.2F, 1.2F, 1.2F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAbsImmortalSkeleton entity) {
        return TEXTURE;
    }
}
