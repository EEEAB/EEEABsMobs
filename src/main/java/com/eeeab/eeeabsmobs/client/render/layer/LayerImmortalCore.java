package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTheImmortal;
import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerImmortalCore extends RenderLayer<EntityTheImmortal, ModelTheImmortal> {
    public static final ResourceLocation LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_core.png");

    public LayerImmortalCore(RenderLayerParent<EntityTheImmortal, ModelTheImmortal> render) {
        super(render);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, EntityTheImmortal entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.translate(0F, -1F, 0F);
        this.getParentModel().core.render(poseStack, buffer.getBuffer(EMRenderType.getMovingViewEffect(LAYER)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
