package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerGuardianCore extends LayerGlow<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/nameless_guardian/nameless_guardian_glow.png");

    public LayerGuardianCore(RenderLayerParent<EntityNamelessGuardian, ModelNamelessGuardian> renderLayerParent) {
        super(renderLayerParent, GLOW_LAYER, 0.8F, guardian -> !guardian.coreControlled.isStop());
    }

    @Override
    protected float getBrightness(EntityNamelessGuardian entity, float partialTicks) {
        float timer = entity.coreControlled.getPrevTimer();
        return timer * 0.08F;
    }
}
