package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.layer.LayerOuter;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelCorpse;
import com.eeeab.eeeabsmobs.client.model.entity.ModelCorpseToPlayer;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpse;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseToPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderCorpseToPlayer extends MobRenderer<EntityCorpseToPlayer, ModelCorpseToPlayer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_eyes.png");
    private static final ResourceLocation LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_plains.png");

    public RenderCorpseToPlayer(EntityRendererProvider.Context context) {
        super(context, new ModelCorpseToPlayer(context.bakeLayer(EMModelLayer.CORPSE)), 0.3F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, 0.8F));
        this.addLayer(new LayerOuter<>(this, LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCorpseToPlayer corpse) {
        return TEXTURE;
    }
}
