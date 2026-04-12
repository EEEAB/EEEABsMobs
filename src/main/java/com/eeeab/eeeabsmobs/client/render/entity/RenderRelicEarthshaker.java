package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelRelicEarthshaker;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerBreath;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.mob.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicEarthshaker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderRelicEarthshaker extends MobRenderer<EntityRelicEarthshaker, ModelRelicEarthshaker> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_earthshaker.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_earthshaker_glow.png");
    private static final ResourceLocation HOT_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_earthshaker_hot.png");
    private static final String[] RIGHT_FINGER = new String[]{"upper", "body", "rightArm", "rightHand", "finger1"};
    private static final String[] LEFT_FINGER = new String[]{"upper", "body", "leftArm", "leftHand", "finger4"};

    public RenderRelicEarthshaker(EntityRendererProvider.Context context) {
        super(context, new ModelRelicEarthshaker(context.bakeLayer(ModModelLayer.RELIC_EARTHSHAKER)), 1.5F);
        this.addLayer(new LayerBreath<>(this, GLOW_LAYER, 0.5F, e -> e.isGlow() && e.getAnimation() != EntityRelicEarthshaker.ACTIVE_ANIMATION && e.getAnimation() != EntityRelicEarthshaker.ELECTROMAGNETIC_ANIMATION, 0.075F));
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, e -> !e.glowControlled.isStop(), (entity, partialTicks) -> {
            float timer = entity.glowControlled.getPrevTimer();
            if (entity.getAnimation() == EntityRelicEarthshaker.ELECTROMAGNETIC_ANIMATION) {
                timer += entity.electromagneticControlled.getTimer();
            }
            return Math.min(timer * 0.05F, 1F);
        }));
        this.addLayer(new LayerGlow<>(this, HOT_LAYER, GlowEntity::isGlow,
                (entity, partialTicks) -> entity.hotControlled.getAnimationFraction(partialTicks)) {
            @Override
            protected RenderType getRenderType(EntityRelicEarthshaker entity) {
                return RenderType.entityTranslucentEmissive(this.location);
            }
        });
    }

    @Override
    protected float getFlipDegrees(EntityRelicEarthshaker entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRelicEarthshaker sentinelHeavy) {
        return TEXTURE;
    }

    @Override
    public void render(EntityRelicEarthshaker entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        Animation animation = entity.getAnimation();
        if (!entity.hotControlled.isStop() || EntityRelicEarthshaker.ACTIVE_ANIMATION == animation || EntityRelicEarthshaker.DEACTIVATE_ANIMATION == animation
                || EntityRelicEarthshaker.ELECTROMAGNETIC_ANIMATION == animation || EntityRelicEarthshaker.RANGE_ATTACK_ANIMATION == animation) {
            entity.hand[0] = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), LEFT_FINGER);
            entity.hand[1] = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), RIGHT_FINGER);
        }
    }
}
