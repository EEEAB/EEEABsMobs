package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelImmortal;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.client.render.layer.LayerOuter;
import com.eeeab.eeeabsmobs.sever.entity.mob.immortal.EntityImmortalBoss;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderImmortal extends MobRenderer<EntityImmortalBoss, ModelImmortal> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_eyes.png");
    private static final ResourceLocation CORE_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_core.png");
    private static final ResourceLocation NETHERWORLD_KATANA = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal_netherworld_katana.png");
    private static final String[] RIGHT_FIST = new String[]{"upper", "body", "rightArm", "rightArmUnder", "rightFist"};
    private static final String[] LEFT_FIST = new String[]{"upper", "body", "leftArm", "leftArmUnder", "leftFist"};

    public RenderImmortal(EntityRendererProvider.Context context) {
        super(context, new ModelImmortal(context.bakeLayer(ModModelLayer.IMMORTAL)), 2F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, e -> e.isGlow() && !e.isInvisible(),
                (entity, partialTicks) -> entity.glowControlled.getAnimationFraction(partialTicks)) {
            @Override
            protected RenderType getRenderType(EntityImmortalBoss entity) {
                return RenderType.entityTranslucentEmissive(this.location);
            }
        });
        this.addLayer(new LayerOuter<>(this, CORE_LAYER, true, e -> !e.isInvisible()) {
            @Override
            protected void renderLayer(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, float partialTicks, EntityImmortalBoss entity, boolean overlayTexture) {
                overlayTexture = entity.getAnimation() != EntityImmortalBoss.UNLEASH_ENERGY_ANIMATION && entity.hurtControlled.isStop();
                super.renderLayer(stack, vertexConsumer, packedLightIn, partialTicks, entity, overlayTexture);
            }

            @Override
            protected float getAlpha(EntityImmortalBoss entity, float partialTicks) {
                return 1F - entity.alphaControlled.getAnimationFraction(partialTicks);
            }
        });
        LayerOuter.OuterPredicate<EntityImmortalBoss> predicate1 = e -> e.isHoldKatana() && !e.isInvisible();
        this.addLayer(new LayerOuter<>(this, NETHERWORLD_KATANA, true, predicate1) {
            @Override
            protected float getAlpha(EntityImmortalBoss entity, float partialTicks) {
                return 1F - entity.alphaControlled.getAnimationFraction(partialTicks);
            }
        });
    }

    @Override
    protected void scale(EntityImmortalBoss entity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.86625F, 0.86625F, 0.86625F);
    }

    @Override
    protected float getFlipDegrees(EntityImmortalBoss entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public void render(EntityImmortalBoss entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (!entity.alphaControlled.isStop()) {
            float alpha = 1F - entity.alphaControlled.getAnimationFraction(partialTicks);
            this.shadowRadius *= alpha;
            this.shadowStrength *= alpha;
            int packedOverlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
            render(ModRenderType.getGlowingCutOutEffect(getTextureLocation(entity), true), entity, partialTicks, alpha, matrixStack, buffer, packedLight, packedOverlay);
        } else if (!entity.hurtControlled.isStop()) {
            this.shadowStrength = 1F;
            this.shadowRadius = 2F;
            float alpha = 1F - entity.hurtControlled.getAnimationFraction(partialTicks) * 0.85F;
            render(ModRenderType.entityTranslucent(getTextureLocation(entity)), entity, partialTicks, alpha, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        } else {
            this.shadowStrength = 1F;
            this.shadowRadius = 2F;
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        }
        Animation animation = entity.getAnimation();
        if (animation == EntityImmortalBoss.SHORYUKEN_ANIMATION || animation == EntityImmortalBoss.ATTRACT_ANIMATION || animation == EntityImmortalBoss.PUNCH_RIGHT_ANIMATION
                || animation == EntityImmortalBoss.SMASH_GROUND_ANIMATION1 || animation == EntityImmortalBoss.SMASH_GROUND_ANIMATION2 || animation == EntityImmortalBoss.SMASH_GROUND_ANIMATION3
                || animation == EntityImmortalBoss.PUNCH_LEFT_ANIMATION || animation == EntityImmortalBoss.POUNCE_SMASH_ANIMATION || animation == EntityImmortalBoss.POUNCE_PICK_ANIMATION
                || animation == EntityImmortalBoss.HARDPUNCH_LEFT_ANIMATION || animation == EntityImmortalBoss.HARDPUNCH_RIGHT_ANIMATION) {
            entity.hand[0] = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), LEFT_FIST);
            entity.hand[1] = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), RIGHT_FIST);
        }
    }

    private void render(RenderType renderType, EntityImmortalBoss entity, float partialTicks, float alpha, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(entity, this, partialTicks, matrixStack, buffer, packedLight))) return;
        matrixStack.pushPose();
        this.model.attackTime = this.getAttackAnim(entity, partialTicks);
        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
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

        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            f8 = entity.walkAnimation.speed(partialTicks);
            f5 = entity.walkAnimation.position(partialTicks);
            if (entity.isBaby()) {
                f5 *= 3.0F;
            }
            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }
        this.model.prepareMobModel(entity, f5, f8, partialTicks);
        this.model.setupAnim(entity, f5, f8, f7, f2, f6);

        VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
        this.model.renderToBuffer(matrixStack, vertexconsumer, packedLight, packedOverlay, alpha, alpha, alpha, alpha);
        for (RenderLayer<EntityImmortalBoss, ModelImmortal> renderlayer : this.layers) {
            renderlayer.render(matrixStack, buffer, packedLight, entity, f5, f8, partialTicks, f7, f2, f6);
        }
        matrixStack.popPose();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(entity, this, partialTicks, matrixStack, buffer, packedLight));
    }

    @Override
    public Vec3 getRenderOffset(EntityImmortalBoss entity, float partialTicks) {
        RandomSource random = entity.getRandom();
        if (entity.getAnimation() == EntityImmortalBoss.TELEPORT_ANIMATION && entity.getAnimationTick() > 10) {
            double d0 = 0.05D;
            return new Vec3(random.nextGaussian() * d0, 0.0D, random.nextGaussian() * d0);
        } else if (!entity.hurtControlled.isStop()) {
            double d0 = entity.hurtControlled.getAnimationFraction(partialTicks) * 0.1D;
            return new Vec3(random.nextGaussian() * d0, 0.0D, random.nextGaussian() * d0);
        }
        return super.getRenderOffset(entity, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityImmortalBoss immortal) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityImmortalBoss entity, BlockPos pos) {
        return (int) Math.max(15 * entity.glowControlled.getAnimationFraction(), super.getBlockLightLevel(entity, pos));
    }
}
