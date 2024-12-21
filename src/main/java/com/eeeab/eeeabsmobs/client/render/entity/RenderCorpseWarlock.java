package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.client.render.layer.LayerOuter;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelCorpseWarlock;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderCorpseWarlock extends MobRenderer<EntityCorpseWarlock, ModelCorpseWarlock> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_warlock/corpse_warlock.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_warlock/corpse_warlock_eyes.png");
    private static final ResourceLocation NECKLACE_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/corpse/corpse_warlock/corpse_warlock_necklace.png");

    public RenderCorpseWarlock(EntityRendererProvider.Context context) {
        super(context, new ModelCorpseWarlock(context.bakeLayer(EMModelLayer.CORPSE_SLAVERY)), 0.3F);
        this.addLayer(new LayerOuter<>(this, NECKLACE_LAYER, true, e -> !e.isInvisible()));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, 1F, e -> !e.isInvisible() && !e.glowControllerAnimation.isStop(), true) {
            @Override
            protected RenderType getRenderType(EntityCorpseWarlock entity) {
                return RenderType.entityTranslucentEmissive(this.location);
            }

            @Override
            protected float getBrightness(EntityCorpseWarlock entity, float partialTicks) {
                return entity.glowControllerAnimation.getAnimationFraction(partialTicks);
            }
        });

    }

    @Override
    public ResourceLocation getTextureLocation(EntityCorpseWarlock corpse) {
        return TEXTURE;
    }
}
