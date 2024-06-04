package com.eeeab.animate.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector4f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

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
    @Deprecated
    public static Vec3 getWorldPosition(Entity entity, float yaw, ModelPart box) {
        PoseStack poseStack = new PoseStack();
        poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
        poseStack.mulPose(new Quaternion(0, -yaw + 180F,0,true));
        poseStack.scale(-1F, -1F, 1F);
        poseStack.translate(0, -1.0F, 0);
        poseStackFromModel(poseStack, box);
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.transform(matrix4f);
        return new Vec3(vector4f.x(), vector4f.y(), vector4f.z());
    }

    /**
     * 手动指定模型部件链路位于世界坐标
     *
     * @param entity        实体
     * @param yaw           偏航
     * @param root          根部件
     * @param modelPartName 查找链路(子组件靠后)
     */
    public static Vec3 getWorldPosition(Entity entity, float yaw, ModelPart root, String... modelPartName) {
        if (modelPartName == null || modelPartName.length == 0)
            throw new IllegalArgumentException("The lookup link cannot be null");
        PoseStack poseStack = new PoseStack();
        poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
        poseStack.mulPose(new Quaternion(0, -yaw + 180F,0,true));
        poseStack.scale(-1F, -1F, 1F);
        ModelPart nextPart = null;
        for (int i = 0; i < modelPartName.length; i++) {
            if (i == 0) {
                nextPart = root.getChild(modelPartName[0]);
                nextPart.translateAndRotate(poseStack);
            } else {
                ModelPart child = nextPart.getChild(modelPartName[i]);
                child.translateAndRotate(poseStack);
                nextPart = child;
            }
        }
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.transform(matrix4f);
        return new Vec3(vector4f.x(), vector4f.y(), vector4f.z());
    }
}
