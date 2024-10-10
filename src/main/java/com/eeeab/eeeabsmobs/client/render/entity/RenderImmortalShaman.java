package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.client.render.layer.LayerOuter;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortalShaman;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerBreath;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalShaman;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class RenderImmortalShaman extends MobRenderer<EntityImmortalShaman, ModelImmortalShaman> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_shaman/immortal_shaman.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_shaman/immortal_shaman_decoration.png");
    private static final ResourceLocation OUTER_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_shaman/immortal_shaman_outer.png");
    public static final ResourceLocation EYES_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_shaman/immortal_shaman_eyes.png");

    public RenderImmortalShaman(EntityRendererProvider.Context context) {
        super(context, new ModelImmortalShaman(context.bakeLayer(EMModelLayer.IMMORTAL_SHAMAN)), 0.45F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerOuter<>(this, OUTER_LAYER));
        this.addLayer(new LayerGlow<>(this, EYES_LAYER));
        this.addLayer(new LayerBreath<>(this, GLOW_LAYER, 0.25F, LivingEntity::isAlive, 0.18F));
    }

    @Override
    protected float getFlipDegrees(EntityImmortalShaman entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalShaman entity) {
        return TEXTURE;
    }
}
