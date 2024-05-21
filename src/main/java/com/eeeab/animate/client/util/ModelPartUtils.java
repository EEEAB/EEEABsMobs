package com.eeeab.animate.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

//TODO 待完善:获取模型部件的坐标不能兼容原版动画
public class ModelPartUtils {
    private ModelPartUtils() {
    }

    /**
     * 通过递归的方式查找当前模型部件位于模型空间的坐标并加入姿势栈
     *
     * @param poseStack 姿势栈
     * @param box       模型部件
     */
    public static void poseStackFromModel(PoseStack poseStack, ModelPart box) {
        ModelPart modelPart = box.getAllParts().filter(part -> part != box).findFirst().orElse(null);
        if (modelPart != null) poseStackFromModel(poseStack, modelPart);
        box.translateAndRotate(poseStack);
    }

    /**
     * 获取指定模型部件位于世界坐标
     *
     * @param entity 实体
     * @param yaw    偏航
     * @param box    模型部件
     */
    public static Vec3 getWorldPosition(Entity entity, float yaw, ModelPart box) {
        PoseStack poseStack = new PoseStack();
        poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
        poseStack.mulPose((new Quaternionf().rotationY((float) ((-yaw + 180F) * Math.PI / 180F))));
        poseStack.scale(-1F, -1F, 1F);
        poseStack.translate(0, -1.0F, 0);
        poseStackFromModel(poseStack, box);
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        Vector4f mul = vector4f.mul(matrix4f);
        return new Vec3(mul.x, mul.y, mul.z);
    }
}
