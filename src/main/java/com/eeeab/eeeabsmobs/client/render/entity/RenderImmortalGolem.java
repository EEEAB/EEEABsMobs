package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortalGolem;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortalGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderImmortalGolem extends MobRenderer<EntityImmortalGolem, ModelImmortalGolem> {
    public RenderImmortalGolem(EntityRendererProvider.Context context) {
        super(context, new ModelImmortalGolem(), 0.3f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerGlow<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_golem/immortal_golem_eyes.png")));
        //this.addLayer(new LayerBreathingGlow<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/reset/immortal_golem/immortal_golem_eyes.png"), 0.05f));
        //this.addLayer(new LayerArmorHumanoid<>(this, context.getModelManager()));
    }

    @Override
    protected void scale(EntityImmortalGolem entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.8F, 0.8F, 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalGolem p_114482_) {
        return new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_golem/immortal_golem.png");
    }
}
