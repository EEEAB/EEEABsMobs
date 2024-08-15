package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelCorpseWarlock;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderCorpseWarlock extends MobRenderer<EntityCorpseWarlock, ModelCorpseWarlock> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_warlock/corpse_warlock.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_warlock/corpse_warlock_eyes.png");

    public RenderCorpseWarlock(EntityRendererProvider.Context context) {
        super(context, new ModelCorpseWarlock(context.bakeLayer(EMModelLayer.CORPSE_SLAVERY)), 0.3F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, 0.5F));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCorpseWarlock corpse) {
        return TEXTURE;
    }
}
