package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelGulingSentinel;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerCrackiness;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.CrackinessEntity;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityGulingSentinel;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityGulingSentinelHeavy;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Shulker;

import java.util.Map;

public class RenderGulingSentinel extends MobRenderer<EntityGulingSentinel, ModelGulingSentinel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/guling_sentinel.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/guling_sentinel_eyes.png");
    private static final Map<CrackinessEntity.CrackinessType, ResourceLocation> RESOURCE_LOCATION_MAP = ImmutableMap.of(
            CrackinessEntity.CrackinessType.HIGH, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/guling_sentinel_high.png"),
            CrackinessEntity.CrackinessType.MEDIUM, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/guling_sentinel_medium.png"),
            CrackinessEntity.CrackinessType.LOW, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/guling_sentinel_low.png")
    );

    public RenderGulingSentinel(EntityRendererProvider.Context context) {
        super(context, new ModelGulingSentinel(context.bakeLayer(EMModelLayer.GULING_SENTINEL)), 0.8F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER));
        this.addLayer(new LayerCrackiness<>(this, RESOURCE_LOCATION_MAP));
    }

    @Override
    protected float getFlipDegrees(EntityGulingSentinel entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGulingSentinel corpse) {
        return TEXTURE;
    }
}
