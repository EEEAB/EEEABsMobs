package com.eeeab.eeeabsmobs.client.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 生成实体残影效果
 *
 * @author EEEAB
 */
@OnlyIn(Dist.CLIENT)
public class EntityAfterImageHelper<T extends Entity> {
    private final List<AfterImage> afterImages = new ArrayList<>();
    private final List<ModelPart> allParts;
    private final ModelPart rootPart;
    private long lastRecordTick = 0;
    private int maxAfterImages = 3;
    private int recordInterval = 2;
    private int duration = 20;

    public EntityAfterImageHelper(List<ModelPart> allParts, ModelPart rootPart) {
        this.allParts = allParts;
        this.rootPart = rootPart;
    }

    public EntityAfterImageHelper(List<ModelPart> allParts, ModelPart rootPart, int maxAfterImages, int recordInterval, int duration) {
        this(allParts, rootPart);
        this.maxAfterImages = maxAfterImages;
        this.recordInterval = recordInterval;
        this.duration = duration;
    }

    private AfterImage capture(T entity, long currentTick) {
        AfterImage img = new AfterImage(allParts.size());
        img.x = entity.xo;
        img.y = entity.yo;
        img.z = entity.zo;
        img.bodyYaw = entity.getVisualRotationYInDegrees();
        img.creationTick = currentTick;
        for (int i = 0; i < allParts.size(); i++) {
            ModelPart part = allParts.get(i);
            img.xRot[i] = part.xRot;
            img.yRot[i] = part.yRot;
            img.zRot[i] = part.zRot;
            img.partX[i] = part.x;
            img.partY[i] = part.y;
            img.partZ[i] = part.z;
            img.visible[i] = part.visible;
        }
        return img;
    }

    private void apply(AfterImage img) {
        for (int i = 0; i < allParts.size(); i++) {
            ModelPart part = allParts.get(i);
            part.xRot = img.xRot[i];
            part.yRot = img.yRot[i];
            part.zRot = img.zRot[i];
            part.x = img.partX[i];
            part.y = img.partY[i];
            part.z = img.partZ[i];
            part.visible = img.visible[i];
        }
    }

    public void recordIfNeeded(T entity, long currentTick) {
        if (currentTick - lastRecordTick >= recordInterval) {
            AfterImage img = capture(entity, currentTick);
            afterImages.add(img);
            if (afterImages.size() > maxAfterImages) {
                afterImages.remove(0);
            }
            lastRecordTick = currentTick;
        }
    }

    public void renderAfterImages(T entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTicks, long currentTick, Function<T, ResourceLocation> textureProvider) {
        float currentTimeWithPartial = currentTick + partialTicks;
        afterImages.removeIf(img -> currentTimeWithPartial - img.creationTick > duration);
        AfterImage currentState = capture(entity, currentTick);

        for (AfterImage img : afterImages) {
            float age = currentTimeWithPartial - img.creationTick;
            if (age <= 0) continue;
            float alpha = 1 - age / duration;
            alpha = Mth.clamp(alpha, 0, 1);
            apply(img);
            poseStack.pushPose();
            double interpX = Mth.lerp(partialTicks, entity.xo, entity.getX());
            double interpY = Mth.lerp(partialTicks, entity.yo, entity.getY());
            double interpZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
            poseStack.translate(img.x - interpX, img.y - interpY, img.z - interpZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(180F - img.bodyYaw));
            poseStack.scale(-1, -1, 1);
            poseStack.translate(0, -1.501F, 0);

            ResourceLocation texture = textureProvider.apply(entity);
            RenderType renderType = RenderType.entityTranslucentCull(texture);
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            rootPart.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
            poseStack.popPose();
        }

        apply(currentState);
    }

    private static class AfterImage {
        double x, y, z;
        float bodyYaw;
        long creationTick;
        final float[] xRot, yRot, zRot;
        final float[] partX, partY, partZ;
        final boolean[] visible;

        AfterImage(int partCount) {
            xRot = new float[partCount];
            yRot = new float[partCount];
            zRot = new float[partCount];
            partX = new float[partCount];
            partY = new float[partCount];
            partZ = new float[partCount];
            visible = new boolean[partCount];
        }
    }
}