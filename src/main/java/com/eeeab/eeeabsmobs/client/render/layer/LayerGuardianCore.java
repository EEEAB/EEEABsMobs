package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerGuardianCore extends RenderLayer<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation location = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian/nameless_guardian_core.png");
    //private static final float defaultSpeed = 0.08F;

    public LayerGuardianCore(RenderLayerParent<EntityNamelessGuardian, ModelNamelessGuardian> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isAlive()) {
            float timer = entity.coreControlled.getPrevTimer();
            //float speed = defaultSpeed + timer * 0.05F;
            //float brightness = Mth.clamp((Mth.cos(ageInTicks * speed) * 2.0F) - 1.0F, timer * 0.05F, 1F);
            float brightness = timer * 0.08F;
            this.renderLayer(stack, bufferSource.getBuffer(RenderType.eyes(location)), packedLightIn, brightness, brightness, brightness);
        }
    }

    private void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float r, float g, float b) {
        this.getParentModel().renderToBuffer(stack, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
    }
}
