package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerVariantHolder;
import com.eeeab.eeeabsmobs.sever.entity.mob.immortal.EntityAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.sever.entity.mob.immortal.EntityAbsImmortalSkeleton.ClassType;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderAbsImmortalSkeleton extends MobRenderer<EntityAbsImmortalSkeleton, ModelAbsImmortalSkeleton> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton_eyes.png");
    private static final Map<ClassType, ResourceLocation> RESOURCE_LOCATION_MAP = ImmutableMap.of(
            ClassType.ARCHER, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/archer.png"),
            ClassType.KNIGHT, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/knight.png"),
            ClassType.MAGE, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/mage.png"),
            ClassType.WARRIOR, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_skeleton/warrior.png")
    );

    public RenderAbsImmortalSkeleton(EntityRendererProvider.Context context) {
        super(context, new ModelAbsImmortalSkeleton(context.bakeLayer(ModModelLayer.IMMORTAL_SKELETON)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER));
        this.addLayer(new LayerVariantHolder<>(this, RESOURCE_LOCATION_MAP));
    }

    @Override
    protected void scale(EntityAbsImmortalSkeleton entity, PoseStack poseStack, float partialTickTime) {
        float scale = entity.getVariant().scale;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAbsImmortalSkeleton entity) {
        return TEXTURE;
    }
}
