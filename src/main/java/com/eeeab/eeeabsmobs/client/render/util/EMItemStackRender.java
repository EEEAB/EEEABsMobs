package com.eeeab.eeeabsmobs.client.render.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.item.ModelTheNetherworldKatana;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EMItemStackRender extends BlockEntityWithoutLevelRenderer {
    private static boolean init;
    private static ModelTheNetherworldKatana THE_NETHERWORLD_KATANA;
    private static final ResourceLocation THE_NETHERWORLD_KATANA_TEX = new ResourceLocation(EEEABMobs.MOD_ID, "textures/item/netherworld_katana.png");

    public static void init() {
        init = true;
        THE_NETHERWORLD_KATANA = new ModelTheNetherworldKatana(Minecraft.getInstance().getEntityModels().bakeLayer(EMModelLayer.THE_NETHERWORLD_KATANA));
    }

    public EMItemStackRender() {
        super(null, null);
    }


    @Override
    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!init) {
            init();
        }
        VertexConsumer vertexConsumer;
        if (itemStackIn.getItem() == ItemInit.THE_NETHERWORLD_KATANA.get()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            matrixStackIn.scale(0.5F, -0.5F, -0.5F);
            vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(THE_NETHERWORLD_KATANA_TEX), false, itemStackIn.hasFoil());
            THE_NETHERWORLD_KATANA.renderToBuffer(matrixStackIn, vertexConsumer, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            matrixStackIn.popPose();
        }
    }
}
