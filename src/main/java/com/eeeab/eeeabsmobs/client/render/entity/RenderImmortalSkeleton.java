package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortalSkeleton;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerMobModelOuter;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.AbstractImmortalSkeleton;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortalKnight;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderImmortalSkeleton extends MobRenderer<AbstractImmortalSkeleton, ModelImmortalSkeleton> {

    public RenderImmortalSkeleton(EntityRendererProvider.Context context) {
        super(context, new ModelImmortalSkeleton(), 0.5F);
        this.addLayer(new LayerGlow<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/immortal_skeleton_glow.png")));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerMobModelOuter<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/immortal_skeleton_armor.png"), false) {
            @Override
            protected boolean test(AbstractImmortalSkeleton immortalSkeleton) {
                return immortalSkeleton instanceof EntityImmortalKnight;
            }
        });
    }

    @Override
    protected float getFlipDegrees(AbstractImmortalSkeleton entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    protected void scale(AbstractImmortalSkeleton entity, PoseStack poseStack, float partialTickTime) {
        if (entity instanceof EntityImmortalKnight) {
            poseStack.scale(1.2F, 1.2F, 1.2F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractImmortalSkeleton entity) {
        return new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/immortal_skeleton.png");
    }
}
