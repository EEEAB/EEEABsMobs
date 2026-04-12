package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelRelicAnnihilator;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.util.EntityAfterImageHelper;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class RenderRelicAnnihilator extends MobRenderer<EntityRelicAnnihilator, ModelRelicAnnihilator> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_annihilator.png");
    private static final ResourceLocation EYE_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_annihilator_eye.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_annihilator_glow.png");
    private static final ResourceLocation FLAME_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_annihilator_exhaust.png");
    private static final String[] MUZZLE = new String[]{"upper", "leftArm", "leftHand", "muzzle"};
    private static final String[] SAW = new String[]{"upper", "rightArm", "rightHand", "saw"};
    private final EntityAfterImageHelper<EntityRelicAnnihilator> afterImageHelper;

    public RenderRelicAnnihilator(EntityRendererProvider.Context context) {
        super(context, new ModelRelicAnnihilator(context.bakeLayer(ModModelLayer.RELIC_ANNIHILATOR)), 1.3F);
        this.addLayer(new LayerGlow<>(this, EYE_LAYER, entity -> entity.isGlow() && !entity.isBlinded(), (entity, partialTicks) -> entity.glowControlled.getAnimationFraction(partialTicks)));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, entity -> !entity.glowControlled.isStop(),(annihilator, partialTicks) -> annihilator.glowControlled.getAnimationFraction(partialTicks)));
        this.addLayer(new LayerGlow<>(this, FLAME_LAYER) {
            @Override
            protected RenderType getRenderType(EntityRelicAnnihilator entity) {
                return ModRenderType.getGlowingTranslucentEffect(location);
            }
        });
        afterImageHelper = new EntityAfterImageHelper<>(model.root().getAllParts().collect(Collectors.toList()), model.root(), 2, 1, 2);
    }

    @Override
    protected float getFlipDegrees(EntityRelicAnnihilator entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRelicAnnihilator corpse) {
        return TEXTURE;
    }

    @Override
    public void render(EntityRelicAnnihilator entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        Animation animation = entity.getAnimation();
        if (animation != AnimatedEntity.NO_ANIMATION) {
            ModelPart root = this.model.root();
            entity.muzzle = ModelPartUtils.getWorldPosition(entity, entity.getYRot(), root, MUZZLE);
            entity.saw = ModelPartUtils.getWorldPosition(entity, entity.getYRot(), root, SAW);
            boolean afterImage = animation == EntityRelicAnnihilator.BACKDASH_ANIMATION;
            if (animation == EntityRelicAnnihilator.STAB_ANIMATION) {
                int tick = entity.getAnimationTick();
                afterImage = tick >= 24 && tick < 29;
            }
            if (afterImage) afterImageHelper.recordIfNeeded(entity, entity.level().getGameTime());
        }
        afterImageHelper.renderAfterImages(entity, poseStack, buffer, packedLight, partialTicks, entity.level().getGameTime(), e -> TEXTURE);
    }
}
