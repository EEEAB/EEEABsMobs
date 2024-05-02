package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortalShaman;
import com.eeeab.eeeabsmobs.client.render.layer.LayerBreath;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.util.ModRenderUtils;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalShaman;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderImmortalShaman extends MobRenderer<EntityImmortalShaman, ModelImmortalShaman> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_shaman/immortal_shaman.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_shaman/immortal_shaman_glow.png");

    public RenderImmortalShaman(EntityRendererProvider.Context context) {
        super(context, new ModelImmortalShaman(), 0.45F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER));
        this.addLayer(new LayerBreath<>(this, GLOW_LAYER, 1.0F, entity -> entity.isWeakness() && entity.isAlive(), 0.2F));
    }

    @Override
    protected float getFlipDegrees(EntityImmortalShaman entity) {
        return 0;//获取死亡翻转角度
    }


    @Override
    public ResourceLocation getTextureLocation(EntityImmortalShaman entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityImmortalShaman entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if ((entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_FR_ATTACK_ANIMATION
                || entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_BOMB_ANIMATION
                || entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_SUMMON_ANIMATION
                || entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_HEAL_ANIMATION)
                && entity.heartPos != null && entity.heartPos.length > 0)
            entity.heartPos[0] = ModRenderUtils.getWorldPosition(entity, entityYaw, getModel().heart);
    }


}
