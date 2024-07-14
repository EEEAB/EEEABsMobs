package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.layer.LayerOuter;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelTheImmortal;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class RenderTheImmortal extends MobRenderer<EntityTheImmortal, ModelTheImmortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_eyes.png");
    private static final ResourceLocation R_NETHERWORLD_KATANA = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_r_netherworld_katana.png");
    private static final ResourceLocation L_NETHERWORLD_KATANA = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_l_netherworld_katana.png");

    public RenderTheImmortal(EntityRendererProvider.Context context) {
        super(context, new ModelTheImmortal(context.bakeLayer(EMModelLayer.IMMORTAL)), 2.5F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER) {
            @Override
            protected float getBrightness(EntityTheImmortal entity) {
                return entity.glowControllerAnimation.getAnimationFraction();
            }
        });
        this.addLayer(new LayerOuter<>(this, R_NETHERWORLD_KATANA, true, e -> e.getHeldIndex() > 0 && !e.isInvisible()));
        this.addLayer(new LayerOuter<>(this, L_NETHERWORLD_KATANA, true, e -> e.getHeldIndex() > 1 && !e.isInvisible()));
    }

    @Override
    protected float getFlipDegrees(EntityTheImmortal entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public Vec3 getRenderOffset(EntityTheImmortal entity, float partialTicks) {
        if (entity.getAnimation() == entity.teleportAnimation && entity.getAnimationTick() > 10) {
            double d0 = 0.05D;
            RandomSource random = entity.getRandom();
            return new Vec3(random.nextGaussian() * d0, 0.0D, random.nextGaussian() * d0);
        }
        return super.getRenderOffset(entity, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTheImmortal immortal) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityTheImmortal entity, BlockPos pos) {
        return (int) Math.max(15 * entity.glowControllerAnimation.getAnimationFraction(), super.getBlockLightLevel(entity, pos));
    }
}
