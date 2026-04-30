package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelRealmWarden;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRealmWarden;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderRealmWarden extends MobRenderer<EntityRealmWarden, ModelRealmWarden> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/realm_warden.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/realm_warden_glow.png");
    private static final ResourceLocation FLAME_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/realm_warden_exhaust.png");
    private static final String[] LEFT_GLOVE = new String[]{"upper", "leftArm", "leftArmUnder", "leftGlove", "lGloveJoint"};
    private static final String[] RIGHT_GLOVE = new String[]{"upper", "rightArm", "rightArmUnder", "rightGlove", "rGloveJoint"};
    private static final String[] LEFT_BLADE = new String[]{"upper", "mainBooster", "axe_back", "leftSocket", "leftBlade"};
    private static final String[] RIGHT_BLADE = new String[]{"upper", "mainBooster", "axe_back", "rightSocket", "rightBlade"};

    public RenderRealmWarden(EntityRendererProvider.Context context) {
        super(context, new ModelRealmWarden(context.bakeLayer(ModModelLayer.REALM_WARDEN)), 2F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, e -> !e.glowControlled.isStop(),
                (entity, partialTicks) -> entity.glowControlled.getAnimationFraction(partialTicks)));
        this.addLayer(new LayerGlow<>(this, FLAME_LAYER) {
            @Override
            protected RenderType getRenderType(EntityRealmWarden entity) {
                return ModRenderType.getGlowingTranslucentEffect(location);
            }

            @Override
            protected void renderLayer(EntityRealmWarden entity, PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float r, float g, float b, float alpha) {
                super.renderLayer(entity, stack, vertexConsumer, packedLightIn, r, g, b, 0.4F);
            }
        });
    }

    @Override
    protected void scale(EntityRealmWarden entity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(1.16F, 1.16F, 1.16F);
    }

    @Override
    protected float getFlipDegrees(EntityRealmWarden entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRealmWarden sentinelHeavy) {
        return TEXTURE;
    }

    @Override
    public void render(EntityRealmWarden entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (!entity.alphaControlled.isStop()) {
            render(entity, partialTicks, matrixStack, buffer, packedLight);
        } else super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        Animation animation = entity.getAnimation();
        if (animation != AnimatedEntity.NO_ANIMATION) {
            ModelPart root = this.model.root();
            String[] lParts = LEFT_GLOVE;
            String[] rParts = RIGHT_GLOVE;
            if (animation == EntityRealmWarden.BACKSTEP_LANDING_ANIMATION) {
                rParts = RIGHT_BLADE;
                lParts = LEFT_BLADE;
            }
            entity.modelParts[0] = ModelPartUtils.getWorldPosition(entity, entity.getYRot(), root, lParts);
            entity.modelParts[1] = ModelPartUtils.getWorldPosition(entity, entity.getYRot(), root, rParts);
        }
    }

    private void render(EntityRealmWarden entity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(entity, this, partialTicks, matrixStack, buffer, packedLight))) return;
        matrixStack.pushPose();
        this.model.attackTime = this.getAttackAnim(entity, partialTicks);
        this.model.riding = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
        this.model.young = entity.isBaby();
        float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float f2 = f1 - f;
        float f6 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float f7 = this.getBob(entity, partialTicks);
        this.setupRotations(entity, matrixStack, f7, f, partialTicks);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, matrixStack, partialTicks);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
        float alpha = 1 - entity.alphaControlled.getAnimationProgressSinSqrt(partialTicks);
        this.model.prepareMobModel(entity, 0, 0, partialTicks);
        this.model.setupAnim(entity, 0, 0, f7, f2, f6);
        VertexConsumer vertexconsumer = buffer.getBuffer(ModRenderType.entityTranslucentCull(TEXTURE));
        this.model.renderToBuffer(matrixStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, alpha, alpha, alpha, alpha);
        matrixStack.popPose();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(entity, this, partialTicks, matrixStack, buffer, packedLight));
    }
}
