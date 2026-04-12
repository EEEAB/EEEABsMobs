package com.eeeab.eeeabsmobs.client.render.entity;

import com.eeeab.animate.client.util.ModelPartUtils;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.entity.ModelRelicRipper;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.render.layer.LayerGlow;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicRipper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderRelicRipper extends MobRenderer<EntityRelicRipper, ModelRelicRipper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_ripper.png");
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/relic_ripper_glow.png");
    private static final String[] SAW = new String[]{"upper", "rightArm", "rightArmJoint", "rightHand", "sawJoint", "saw"};

    public RenderRelicRipper(EntityRendererProvider.Context context) {
        super(context, new ModelRelicRipper(context.bakeLayer(ModModelLayer.RELIC_RIPPER)), 1.4F);
        this.addLayer(new LayerGlow<>(this, GLOW_LAYER, e -> !e.glowControlled.isStop(),
                (entity, partialTicks) -> entity.glowControlled.getAnimationFraction(partialTicks)));
    }

    @Override
    protected void scale(EntityRelicRipper entity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.95F, 0.95F, 0.95F);
    }

    @Override
    protected float getFlipDegrees(EntityRelicRipper entity) {
        return 0;//获取死亡翻转角度
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRelicRipper corpse) {
        return TEXTURE;
    }

    @Override
    public void render(EntityRelicRipper entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if (!entity.isNoAnimation()) {
            entity.saw = ModelPartUtils.getWorldPosition(entity, entity.yBodyRot, this.model.root(), SAW);
        }
    }
}
