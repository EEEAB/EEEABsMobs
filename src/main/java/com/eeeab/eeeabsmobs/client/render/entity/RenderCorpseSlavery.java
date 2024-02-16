package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelCorpseSlavery;
import com.eeeab.eeeabsmobs.client.model.entity.ModelCorpseVillager;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerOuter;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseSlavery;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseVillager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class RenderCorpseSlavery extends MobRenderer<EntityCorpseSlavery, ModelCorpseSlavery> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse_slavery/corpse_slavery.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse_slavery/corpse_slavery_eyes.png");
    private static final ResourceLocation LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse_slavery/corpse_slavery_plains.png");

    public RenderCorpseSlavery(EntityRendererProvider.Context context) {
        super(context, new ModelCorpseSlavery(), 0.3F);
        this.addLayer(new LayerOuter<>(this, LAYER));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, 0.5F));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCorpseSlavery corpse) {
        return TEXTURE;
    }
}
