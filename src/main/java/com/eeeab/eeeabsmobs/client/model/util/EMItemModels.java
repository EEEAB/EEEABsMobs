package com.eeeab.eeeabsmobs.client.model.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class EMItemModels {
    public static final String[] HAND_MODEL_ITEMS = new String[]{"guardian_axe", "demolisher"};

    @SubscribeEvent
    public static void onModifyBakingResultEvent(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> map = event.getModels();
        for (String item : HAND_MODEL_ITEMS) {
            ResourceLocation modelInventory = new ModelResourceLocation(new ResourceLocation(EEEABMobs.MOD_ID, item), "inventory");
            ResourceLocation modelHand = new ModelResourceLocation(new ResourceLocation(EEEABMobs.MOD_ID, item + "_in_hand"), "inventory");

            if (!map.containsKey(modelInventory) || !map.containsKey(modelHand)) {
                EEEABMobs.LOGGER.error("Registration failed! Cause: resource key is not containsKey from models...");
                return;
            }

            BakedModel bakedModelDefault = map.get(modelInventory);
            BakedModel bakedModelHand = map.get(modelHand);
            BakedModel modelWrapper = new BakedModel() {
                @Override
                public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
                    return bakedModelDefault.getQuads(state, side, rand);
                }

                @Override
                public boolean useAmbientOcclusion() {
                    return bakedModelDefault.useAmbientOcclusion();
                }

                @Override
                public boolean isGui3d() {
                    return bakedModelDefault.isGui3d();
                }

                @Override
                public boolean usesBlockLight() {
                    return false;
                }

                @Override
                public boolean isCustomRenderer() {
                    return bakedModelDefault.isCustomRenderer();
                }

                @Override
                public TextureAtlasSprite getParticleIcon() {
                    return bakedModelDefault.getParticleIcon();
                }

                @Override
                public ItemOverrides getOverrides() {
                    return bakedModelDefault.getOverrides();
                }

                @Override
                public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
                    BakedModel modelToUse = bakedModelDefault;
                    if (transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                            || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                        modelToUse = bakedModelHand;
                    }
                    return ForgeHooksClient.handleCameraTransforms(poseStack, modelToUse, transformType, applyLeftHandTransform);
                }
            };
            map.put(modelInventory, modelWrapper);
        }
    }
}
