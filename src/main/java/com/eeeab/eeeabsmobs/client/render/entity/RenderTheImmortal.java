package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.layer.LayerOuter;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTheImmortal;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerImmortalCoreSwirl;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalShaman;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RenderTheImmortal extends MobRenderer<EntityTheImmortal, ModelTheImmortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal.png");
    private static final ResourceLocation R_NETHERWORLD_KATANA = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_r_netherworld_katana.png");
    private static final ResourceLocation L_NETHERWORLD_KATANA = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_l_netherworld_katana.png");

    public RenderTheImmortal(EntityRendererProvider.Context context) {
        super(context, new ModelTheImmortal(context.bakeLayer(EMModelLayer.IMMORTAL)), 2.5F);
        this.addLayer(new LayerImmortalCoreSwirl(this));
        this.addLayer(new LayerOuter<>(this, R_NETHERWORLD_KATANA, false,
                e -> (EntityTheImmortal.Stage.STAGE2 == e.getStage() || EntityTheImmortal.Stage.STAGE3 == e.getStage()) && e.getAnimation() != e.switchStage2Animation));
        this.addLayer(new LayerOuter<>(this, L_NETHERWORLD_KATANA, false,
                e -> EntityTheImmortal.Stage.STAGE3 == e.getStage() && e.getAnimation() != e.switchStage3Animation));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTheImmortal immortal) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityTheImmortal entity, BlockPos pos) {
        return 15;
    }
}
