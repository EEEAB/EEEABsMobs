package com.eeeab.eeeabsmobs.client.render.entity.effect;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.effect.ModelAnnihilatorMissile;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.ModRenderType;
import com.eeeab.eeeabsmobs.client.render.layer.LayerVariantHolder;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityGuardianBlade;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityAnnihilatorMissile;
import com.eeeab.eeeabsmobs.sever.entity.mob.immortal.EntityAbsImmortalSkeleton;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Map;

public class RenderAnnihilatorMissile extends EntityRenderer<EntityAnnihilatorMissile> implements RenderLayerParent<EntityAnnihilatorMissile, ModelAnnihilatorMissile> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/annihilator_missile.png");
    private static final Map<EntityAnnihilatorMissile.ElementType, ResourceLocation> RESOURCE_LOCATION_MAP = ImmutableMap.of(
            EntityAnnihilatorMissile.ElementType.VOLT, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/annihilator_missile/volt.png"),
            EntityAnnihilatorMissile.ElementType.BLAZE, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/annihilator_missile/blaze.png"),
            EntityAnnihilatorMissile.ElementType.SPARKFERNO, new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/annihilator_missile/sparkferno.png")
    );
    private final ModelAnnihilatorMissile model;
    private final LayerVariantHolder<EntityAnnihilatorMissile.ElementType, EntityAnnihilatorMissile, ModelAnnihilatorMissile> element;
    private final LayerTrail trail;

    public RenderAnnihilatorMissile(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelAnnihilatorMissile(context.bakeLayer(ModModelLayer.ANNIHILATOR_MISSILE));
        this.element = new LayerVariantHolder<>(this, RESOURCE_LOCATION_MAP) {
            @Override
            protected RenderType getRenderType(ResourceLocation location) {
                return ModRenderType.getGlowingEffect(location);
            }
        };
        this.trail = new LayerTrail(this);
    }

    @Override
    protected int getBlockLightLevel(EntityAnnihilatorMissile entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ModelAnnihilatorMissile getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAnnihilatorMissile entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityAnnihilatorMissile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        poseStack.scale(-1F, -1F, 1F);
        poseStack.translate(0F, -1.501F, 0F);
        this.model.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot()), Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()));
        VertexConsumer vertexconsumer = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.element.render(poseStack, bufferIn, packedLightIn, entity, 0F, 0F, partialTicks, entity.tickCount + partialTicks, 0F, 0F);
        this.trail.render(poseStack, bufferIn, packedLightIn, entity, 0F, 0F, partialTicks, entity.tickCount + partialTicks, 0F, 0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    static class LayerTrail extends RenderLayer<EntityAnnihilatorMissile, ModelAnnihilatorMissile> {
        private static final ResourceLocation[] TEXTURES = new ResourceLocation[4];

        public LayerTrail(RenderLayerParent<EntityAnnihilatorMissile, ModelAnnihilatorMissile> renderLayerParent) {
            super(renderLayerParent);
            for (int i = 0; i < TEXTURES.length; i++) {
                TEXTURES[i] = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/effect/annihilator_missile/index" + (i + 1) + ".png");
            }
        }

        @Override
        public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, EntityAnnihilatorMissile entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(ModRenderType.getGlowingEffect(TEXTURES[Mth.clamp((int) (ageInTicks * 2 % TEXTURES.length), 0, TEXTURES.length - 1)]));
            this.getParentModel().renderToBuffer(stack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }
}
