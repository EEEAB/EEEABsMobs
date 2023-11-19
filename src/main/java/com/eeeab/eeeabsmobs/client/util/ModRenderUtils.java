package com.eeeab.eeeabsmobs.client.util;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

public class ModRenderUtils {
    private ModRenderUtils() {
    }
    public static void poseStackFromModel(PoseStack poseStack, AdvancedModelBox box) {
        AdvancedModelBox boxParent = box.getParent();
        if (boxParent != null) poseStackFromModel(poseStack, boxParent);
        box.translateRotate(poseStack);
    }

    public static Vec3 getWorldPosition(Entity entity, float entityYaw, AdvancedModelBox box) {
        PoseStack poseStack = new PoseStack();
        poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
        poseStack.mulPose((new Quaternionf().rotationY((float) ((-entityYaw + 180F) * Math.PI / 180F))));
        poseStack.scale(-1F, -1F, 1F);
        poseStack.translate(0, -1.5F, 0);
        ModRenderUtils.poseStackFromModel(poseStack, box);
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();

        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        Vector4f mul = vector4f.mul(matrix4f);
        return new Vec3(mul.x, mul.y, mul.z);
    }
}
