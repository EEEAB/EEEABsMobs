package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelCorpse;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerOuter;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpse;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderCorpse extends MobRenderer<EntityCorpse, ModelCorpse> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_eyes.png");
    private static final ResourceLocation FRENZY_GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_frenzy_eyes.png");
    private static final ResourceLocation LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_plains.png");

    public RenderCorpse(EntityRendererProvider.Context context) {
        super(context, new ModelCorpse(), 0.3F);
        this.addLayer(new LayerGlow<>(this, FRENZY_GLOW_LAYER, 1F, EntityCorpse::isFrenzy));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, 0.8F));
        this.addLayer(new LayerOuter<>(this, LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCorpse corpse) {
        return TEXTURE;
    }
}
