package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.namelessguardian.EntityNamelessGuardian;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerGuardianLaser extends LayerGlow<EntityNamelessGuardian, ModelNamelessGuardian> {
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/nameless_guardian/nameless_guardian_shoot.png");

    public LayerGuardianLaser(RenderLayerParent<EntityNamelessGuardian, ModelNamelessGuardian> renderLayerParent) {
        super(renderLayerParent, GLOW_LAYER);
    }

    @Override
    protected float getBrightness(EntityNamelessGuardian entity) {
        float timer = entity.accumulationControlled.getPrevTimer();
        return timer * 0.1F;
    }
}
