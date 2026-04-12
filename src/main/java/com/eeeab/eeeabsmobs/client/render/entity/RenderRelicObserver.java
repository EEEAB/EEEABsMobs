package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelRelicObserver;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerCrackiness;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.mob.CrackinessEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicObserver;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderRelicObserver extends MobRenderer<EntityRelicObserver, ModelRelicObserver> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_observer.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_observer_eyes.png");
    private static final Map<CrackinessEntity.CrackinessType, ResourceLocation> RESOURCE_LOCATION_MAP = ImmutableMap.of(
            CrackinessEntity.CrackinessType.HIGH, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_observer_high.png"),
            CrackinessEntity.CrackinessType.MEDIUM, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_observer_medium.png"),
            CrackinessEntity.CrackinessType.LOW, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_observer_low.png")
    );

    public RenderRelicObserver(EntityRendererProvider.Context context) {
        super(context, new ModelRelicObserver(context.bakeLayer(ModModelLayer.RELIC_OBSERVER)), 0.8F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER));
        this.addLayer(new LayerCrackiness<>(this, RESOURCE_LOCATION_MAP));
    }

    @Override
    protected float getFlipDegrees(EntityRelicObserver entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRelicObserver corpse) {
        return TEXTURE;
    }
}
