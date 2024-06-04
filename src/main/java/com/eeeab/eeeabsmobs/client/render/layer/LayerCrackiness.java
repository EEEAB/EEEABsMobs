package com.eeeab.eeeabsmobs.client.render.layer;

import com.eeeab.eeeabsmobs.sever.entity.CrackinessEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

/**
 * 生物开裂纹理图层
 *
 * @param <T> 实体
 * @param <M> 模型
 * @see com.eeeab.eeeabsmobs.sever.entity.CrackinessEntity
 */
@OnlyIn(Dist.CLIENT)
public class LayerCrackiness<T extends LivingEntity & CrackinessEntity<T>, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final Map<CrackinessEntity.CrackinessType, ResourceLocation> locationMap;

    public LayerCrackiness(RenderLayerParent<T, M> renderer, Map<CrackinessEntity.CrackinessType, ResourceLocation> locationMap) {
        super(renderer);
        this.locationMap = locationMap;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            CrackinessEntity.CrackinessType crackiness = entity.getCrackiness(entity);
            if (crackiness != CrackinessEntity.CrackinessType.NONE) {
                ResourceLocation resourcelocation = locationMap.get(crackiness);
                if (resourcelocation == null) {
                    System.out.println("The '" + crackiness + "' phase resource location is missing");
                    return;
                }
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityTranslucent(resourcelocation));
                this.getParentModel().renderToBuffer(stack, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1F, 1F, 1F, 1F);
            }
        }
    }
}
