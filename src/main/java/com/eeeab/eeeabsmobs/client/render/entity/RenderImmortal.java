package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.eeeabsmobs.client.render.EMRenderType;
import com.eeeab.eeeabsmobs.client.render.layer.LayerImmortalGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerOuter;
import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortal;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class RenderImmortal extends MobRenderer<EntityImmortal, ModelImmortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_eyes.png");
    private static final ResourceLocation CORE_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_core.png");
    private static final ResourceLocation NETHERWORLD_KATANA = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_netherworld_katana.png");
    private static final String[] RIGHT_FIST = new String[]{"upper", "body", "rightArm", "rightArmUnder", "rightFist"};
    private static final String[] LEFT_FIST = new String[]{"upper", "body", "leftArm", "leftArmUnder", "leftFist"};

    public RenderImmortal(EntityRendererProvider.Context context) {
        super(context, new ModelImmortal(context.bakeLayer(EMModelLayer.IMMORTAL)), 2F);
        this.addLayer(new LayerImmortalGlow(this, GLOW_LAYER));
        this.addLayer(new LayerOuter<>(this, CORE_LAYER, true, e -> !e.isInvisible()) {
            @Override
            protected void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float partialTicks, EntityImmortal entity, boolean overlayTexture) {
                overlayTexture = entity.getAnimation() != entity.unleashEnergyAnimation;
                super.renderLayer(stack, vertexConsumer, packedLightIn, partialTicks, entity, overlayTexture);
            }

            @Override
            protected float getAlpha(EntityImmortal entity, float partialTicks) {
                return 1F - entity.alphaControllerAnimation.getAnimationFraction(partialTicks);
            }
        });
        LayerOuter.OuterPredicate<EntityImmortal> predicate1 = e -> e.isHoldKatana() && !e.isInvisible();
        this.addLayer(new LayerOuter<>(this, NETHERWORLD_KATANA, true, predicate1) {
            @Override
            protected float getAlpha(EntityImmortal entity, float partialTicks) {
                return 1F - entity.alphaControllerAnimation.getAnimationFraction(partialTicks);
            }
        });
    }

    @Override
    protected void scale(EntityImmortal entity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.86625F, 0.86625F, 0.86625F);
    }

    @Override
    protected float getFlipDegrees(EntityImmortal entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public void render(EntityImmortal entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (!entity.alphaControllerAnimation.isStop()) {
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(entity, this, partialTicks, matrixStack, buffer, packedLight)))
                return;
            matrixStack.pushPose();
            float alpha = 1F - entity.alphaControllerAnimation.getAnimationFraction(partialTicks);
            this.shadowRadius *= alpha;
            this.shadowStrength *= alpha;
            this.model.attackTime = this.getAttackAnim(entity, partialTicks);
            this.model.riding = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
            this.model.young = entity.isBaby();
            float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
            float f1 = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
            float f2 = f1 - f;

            float f6 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
            if (isEntityUpsideDown(entity)) {
                f6 *= -1.0F;
                f2 *= -1.0F;
            }

            float f7 = this.getBob(entity, partialTicks);
            this.setupRotations(entity, matrixStack, f7, f, partialTicks);
            matrixStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(entity, matrixStack, partialTicks);
            matrixStack.translate(0.0F, -1.501F, 0.0F);

            this.model.prepareMobModel(entity, 0.0F, 0.0F, partialTicks);
            this.model.setupAnim(entity, 0.0F, 0.0F, f7, f2, f6);

            VertexConsumer vertexconsumer = buffer.getBuffer(EMRenderType.getGlowingCutOutEffect(getTextureLocation(entity), true));
            int i = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
            this.model.renderToBuffer(matrixStack, vertexconsumer, packedLight, i, alpha, alpha, alpha, alpha);

            if (!entity.isSpectator()) {
                for (RenderLayer<EntityImmortal, ModelImmortal> renderlayer : this.layers) {
                    renderlayer.render(matrixStack, buffer, packedLight, entity, 0.0F, 0.0F, partialTicks, f7, f2, f6);
                }
            }
            matrixStack.popPose();
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(entity, this, partialTicks, matrixStack, buffer, packedLight));
        } else {
            this.shadowStrength = 1F;
            this.shadowRadius = 2F;
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        }
        Animation animation = entity.getAnimation();
        if (animation == entity.shoryukenAnimation || animation == entity.attractAnimation || animation == entity.punchRightAnimation
                || animation == entity.smashGround1Animation || animation == entity.smashGround2Animation || animation == entity.smashGround3Animation
                || animation == entity.punchLeftAnimation || animation == entity.pounceSmashAnimation
                || animation == entity.hardPunchLeftAnimation || animation == entity.hardPunchRightAnimation) {
            entity.hand[0] = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), LEFT_FIST);
            entity.hand[1] = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), RIGHT_FIST);
        }
    }

    @Override
    public Vec3 getRenderOffset(EntityImmortal entity, float partialTicks) {
        if (entity.getAnimation() == entity.teleportAnimation && entity.getAnimationTick() > 10) {
            double d0 = 0.05D;
            RandomSource random = entity.getRandom();
            return new Vec3(random.nextGaussian() * d0, 0.0D, random.nextGaussian() * d0);
        }
        return super.getRenderOffset(entity, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortal immortal) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityImmortal entity, BlockPos pos) {
        return (int) Math.max(15 * entity.glowControllerAnimation.getAnimationFraction(), super.getBlockLightLevel(entity, pos));
    }
}