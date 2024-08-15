package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortalExecutioner;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalExecutioner;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RenderImmortalExecutioner extends MobRenderer<EntityImmortalExecutioner, ModelImmortalExecutioner> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_executioner/immortal_executioner.png");
    private static final String[] FIRE = new String[]{"upper", "body", "fire"};

    public RenderImmortalExecutioner(EntityRendererProvider.Context context) {
        super(context, new ModelImmortalExecutioner(context.bakeLayer(EMModelLayer.IMMORTAL_EXECUTIONER)), 0.75F);
    }

    @Override
    protected float getFlipDegrees(EntityImmortalExecutioner entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    protected void scale(EntityImmortalExecutioner entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1F, 1F, 1F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalExecutioner entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityImmortalExecutioner entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(EntityImmortalExecutioner entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if (entity.fire != null && entity.fire.length > 0) {
            entity.fire[0] = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), FIRE);
        }
    }
}
