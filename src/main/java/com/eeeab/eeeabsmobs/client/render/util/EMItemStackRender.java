package com.eeeab.eeeabsmobs.client.render.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.item.ModelDemolisher;
import com.eeeab.eeeabsmobs.client.model.item.ModelGuardianBattleaxe;
import com.eeeab.eeeabsmobs.client.model.item.ModelTheNetherworldKatana;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EMItemStackRender extends BlockEntityWithoutLevelRenderer {
    private static boolean init;
    private static ModelTheNetherworldKatana THE_NETHERWORLD_KATANA;
    private static ModelGuardianBattleaxe THE_GUARDIAN_BATTLEAXE;
    private static ModelDemolisher THE_DEMOLISHER;
    private static final ResourceLocation THE_NETHERWORLD_KATANA_TEX = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/immortal/immortal_boss/immortal_netherworld_katana.png");
    private static final ResourceLocation THE_GUARDIAN_BATTLEAXE_TEX = new ResourceLocation(EEEABMobs.MOD_ID, "textures/entity/guling/nameless_guardian/nameless_guardian.png");
    private static final ResourceLocation DEMOLISHER_TEX = new ResourceLocation(EEEABMobs.MOD_ID, "textures/item/demolisher.png");

    public static void initStaticModel() {
        init = true;
        THE_NETHERWORLD_KATANA = new ModelTheNetherworldKatana(Minecraft.getInstance().getEntityModels().bakeLayer(EMModelLayer.THE_NETHERWORLD_KATANA));
        THE_GUARDIAN_BATTLEAXE = new ModelGuardianBattleaxe(Minecraft.getInstance().getEntityModels().bakeLayer(EMModelLayer.THE_GUARDIAN_BATTLEAXE));
        THE_DEMOLISHER = new ModelDemolisher(Minecraft.getInstance().getEntityModels().bakeLayer(EMModelLayer.DEMOLISHER));
    }

    public EMItemStackRender() {
        super(null, null);
    }


    @Override
    public void renderByItem(ItemStack itemStackIn, ItemTransforms.TransformType transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!init) {
            initStaticModel();
        }
        float partialTick = Minecraft.getInstance().getPartialTick();
        if (itemStackIn.is(ItemInit.THE_NETHERWORLD_KATANA.get())) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            matrixStackIn.scale(0.5F, -0.5F, -0.5F);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(THE_NETHERWORLD_KATANA_TEX), false, itemStackIn.hasFoil());
            THE_NETHERWORLD_KATANA.renderToBuffer(matrixStackIn, vertexConsumer, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            matrixStackIn.popPose();
        } else if (itemStackIn.is(ItemInit.GUARDIAN_AXE.get())) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            matrixStackIn.scale(0.5F, -0.5F, -0.5F);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(THE_GUARDIAN_BATTLEAXE_TEX), false, itemStackIn.hasFoil());
            THE_GUARDIAN_BATTLEAXE.renderToBuffer(matrixStackIn, vertexConsumer, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            matrixStackIn.popPose();
        } else if (itemStackIn.is(ItemInit.DEMOLISHER.get())) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.85F, 0.5F);
            matrixStackIn.scale(-0.5F, -0.45F, 0.5F);
            float ageInTicks = Minecraft.getInstance().player == null ? 0F : Minecraft.getInstance().player.tickCount + partialTick;
            THE_DEMOLISHER.setupAnim(itemStackIn, ageInTicks);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(DEMOLISHER_TEX), false, itemStackIn.hasFoil());
            THE_DEMOLISHER.renderToBuffer(matrixStackIn, vertexConsumer, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            matrixStackIn.popPose();
        }
    }
}
