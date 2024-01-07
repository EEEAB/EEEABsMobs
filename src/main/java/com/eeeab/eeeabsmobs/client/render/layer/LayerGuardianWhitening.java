package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
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
public class LayerGuardianWhitening extends RenderLayer<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation NAMELESS_GUARDIAN_TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian/nameless_guardian.png");

    public LayerGuardianWhitening(RenderLayerParent<EntityNamelessGuardian, ModelNamelessGuardian> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getParentModel().renderToBuffer(stack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(NAMELESS_GUARDIAN_TEXTURE)), packedLightIn, getOverlayCoords(Mth.clamp(entity.getExplodeCoefficient(partialTicks), 0F, 1F)), 1, 1, 1, 1);
    }

    private static int getOverlayCoords(float pU) {
        return OverlayTexture.pack(OverlayTexture.u(pU), OverlayTexture.v(false));
    }
}
