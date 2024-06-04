package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTheImmortal;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerImmortalCoreSwirl extends RenderLayer<EntityTheImmortal, ModelTheImmortal> {
    public static final ResourceLocation LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_core_swirl.png");

    public LayerImmortalCoreSwirl(RenderLayerParent<EntityTheImmortal, ModelTheImmortal> render) {
        super(render);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, EntityTheImmortal entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = (float)entity.tickCount + partialTick;
        poseStack.pushPose();
        poseStack.scale(1.2F, 1.2F, 1.2F);
        poseStack.translate(0F, -0.8F, -0.1F);
        this.getParentModel().core.render(poseStack, buffer.getBuffer(RenderType.energySwirl(LAYER, Mth.cos(partialTick * 0.02F) % 1.0F, f * 0.01F % 1.0F)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
