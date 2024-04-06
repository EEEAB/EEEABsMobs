package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
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
public class LayerGuardianExplode extends RenderLayer<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling_sentinel/nameless_guardian/nameless_guardian.png");

    public LayerGuardianExplode(RenderLayerParent<EntityNamelessGuardian, ModelNamelessGuardian> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float coefficient = entity.getExplodeCoefficient(partialTicks);
        if (coefficient > 0) {
            this.getParentModel().renderToBuffer(stack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(GLOW_LAYER)), packedLightIn, getOverlayCoords(Mth.clamp(coefficient, 0F, 1F)), coefficient, coefficient, coefficient, coefficient);
        }
    }

    private static int getOverlayCoords(float pU) {
        return OverlayTexture.pack(OverlayTexture.u(pU), OverlayTexture.v(false));
    }
}
