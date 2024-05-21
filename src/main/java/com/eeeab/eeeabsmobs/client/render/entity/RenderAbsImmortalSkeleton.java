package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.layer.LayerOuter;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalKnight;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderAbsImmortalSkeleton extends MobRenderer<EntityAbsImmortalSkeleton, ModelAbsImmortalSkeleton> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_skeleton/immortal_skeleton.png");
    private static final ResourceLocation VARIANTS_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_skeleton/immortal_skeleton_variants.png");
    private static final ResourceLocation WARRIOR_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_skeleton/career/warrior.png");
    private static final ResourceLocation KNIGHT_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_skeleton/career/knight.png");
    private static final ResourceLocation ARCHER_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_skeleton/career/archer.png");
    private static final ResourceLocation MAGE_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_skeleton/career/mage.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_skeleton/immortal_skeleton_eyes.png");

    public RenderAbsImmortalSkeleton(EntityRendererProvider.Context context) {
        super(context, new ModelAbsImmortalSkeleton(context.bakeLayer(EMModelLayer.IMMORTAL_SKELETON)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER));
        this.addLayer(new LayerOuter<>(this, MAGE_LAYER, false, EntityAbsImmortalSkeleton::isMage));
        this.addLayer(new LayerOuter<>(this, ARCHER_LAYER, false, EntityAbsImmortalSkeleton::isArcher));
        this.addLayer(new LayerOuter<>(this, WARRIOR_LAYER, false, EntityAbsImmortalSkeleton::isWarrior));
        this.addLayer(new LayerOuter<>(this, KNIGHT_LAYER, false, EntityAbsImmortalSkeleton::isKnight));
    }

    @Override
    protected void scale(EntityAbsImmortalSkeleton entity, PoseStack poseStack, float partialTickTime) {
        float scale = entity.getCareerType().scale;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAbsImmortalSkeleton entity) {
        return entity instanceof EntityImmortalKnight || entity.getCareerType() == EntityAbsImmortalSkeleton.CareerType.WARRIOR ? VARIANTS_TEXTURE : TEXTURE;
    }
}
