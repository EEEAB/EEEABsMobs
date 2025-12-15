package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelRelicAnnihilator;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderRelicAnnihilator extends MobRenderer<EntityRelicAnnihilator, ModelRelicAnnihilator> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_annihilator.png");
    private static final ResourceLocation EYE_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_annihilator_eye.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_annihilator_glow.png");
    private static final String[] MUZZLE = new String[]{"upper", "leftArm", "leftHand", "muzzle"};
    private static final String[] SAW = new String[]{"upper", "rightArm", "rightHand", "saw"};

    public RenderRelicAnnihilator(EntityRendererProvider.Context context) {
        super(context, new ModelRelicAnnihilator(context.bakeLayer(ModModelLayer.RELIC_ANNIHILATOR)), 1.3F);
        this.addLayer(new LayerGlow<>(this, EYE_LAYER, 1F, e -> e.isAlive() && !e.isBlinded()));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER));
    }

    @Override
    protected float getFlipDegrees(EntityRelicAnnihilator entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRelicAnnihilator corpse) {
        return TEXTURE;
    }

    @Override
    public void render(EntityRelicAnnihilator entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if (!entity.isNoAnimation()) {
            ModelPart root = this.model.root();
            entity.muzzle = ModelPartUtils.getWorldPosition(entity, entity.getYRot(), root, MUZZLE);
            entity.saw = ModelPartUtils.getWorldPosition(entity, entity.getYRot(), root, SAW);
        }
    }
}
