package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortalShaman;
import com.eeeab.eeeabsmobs.client.render.layer.LayerBreathingGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.util.ModRenderUtils;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortalShaman;
import com.eeeab.eeeabsmobs.sever.entity.util.MobSkinStyle;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderImmortalShaman extends MobRenderer<EntityImmortalShaman, ModelImmortalShaman> {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_shaman/immortal_shaman_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_shaman/immortal_shaman_1.png");

    public RenderImmortalShaman(EntityRendererProvider.Context context) {
        super(context, new ModelImmortalShaman(), 0.45F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new LayerGlow<>(this, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_shaman/immortal_shaman_glow.png")));
        this.addLayer(new LayerBreathingGlow<>(this,
                new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_shaman/immortal_shaman_glow.png"), 0.45f, (entity) -> entity.isWeakness() && entity.isAlive()));
    }

    @Override
    protected float getFlipDegrees(EntityImmortalShaman guardian) {
        return 0;//获取死亡翻转角度
    }


    @Override
    public ResourceLocation getTextureLocation(EntityImmortalShaman entity) {
        if (entity.getStyle() == MobSkinStyle.DEFAULT_STYLE) {
            return TEXTURE_0;
        } else {
            return TEXTURE_1;
        }
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
