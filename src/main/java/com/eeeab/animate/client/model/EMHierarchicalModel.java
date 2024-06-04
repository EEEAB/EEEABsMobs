package com.eeeab.animate.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public abstract class EMHierarchicalModel<E extends Entity> extends EntityModel<E> {
    private static Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    private float movementScale = 1.0F;

    public abstract ModelPart root();

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public abstract void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    /**
     * 重置动画
     * <br>
     * 这是必要的，不加模型动作将会看上去十分夸张
     */
    public void resetToDefaultPose() {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    /**
     * 看向目标动画
     */
    public static void lookAtAnimation(float yaw, float pitch, float rotationDivisor, ModelPart... boxes) {
        int length = boxes.length;
        float actualRotationDivisor = rotationDivisor * (float) length;
        float yawAmount = yaw / 57.295776F / actualRotationDivisor;
        float pitchAmount = pitch / 57.295776F / actualRotationDivisor;
        for (ModelPart box : boxes) {
            box.yRot += yawAmount;
            box.xRot += pitchAmount;
        }
    }

    /**
     * 模型固定旋转坐标
     */
    protected void setStaticRotationPoint(ModelPart box, float px, float py, float pz) {
        box.x += px;
        box.y += py;
        box.z += pz;
    }

    /**
     * 模型固定旋转角度
     */
    protected void setStaticRotationAngle(ModelPart box, float rx, float ry, float rz) {
        box.xRot += toRadians(rx);
        box.yRot += toRadians(ry);
        box.zRot += toRadians(rz);
    }

    /**
     * 模型上下浮动动画
     */
    public void bob(ModelPart box, float speed, float degree, boolean bounce, float frame, float scale) {
        float movementScale = this.getMovementScale();
        degree *= movementScale;
        speed *= movementScale;
        float bob = (float) (Math.sin(frame * speed) * (double) scale * (double) degree - (double) (scale * degree));
        if (bounce) {
            bob = (float) (-Math.abs(Math.sin(frame * speed) * (double) scale * (double) degree));
        }
        box.y += bob;
    }

    /**
     * 模型x摇摆动画
     */
    public void walk(ModelPart box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        box.xRot += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * 模型z摇摆动画
     */
    public void flap(ModelPart box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        box.zRot += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * 模型y摇摆动画
     */
    public void swing(ModelPart box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        box.yRot += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * 计算旋转
     */
    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float frame, float scale) {
        float movementScale = this.getMovementScale();
        float rotation = Mth.cos(frame * speed * movementScale + offset) * degree * movementScale * scale + weight * scale;
        return invert ? -rotation : rotation;
    }

    /**
     * 模型链y摇摆动画
     */
    public void chainSwing(ModelPart[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; ++index) {
            boxes[index].yRot += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * 模型链x摇摆动画
     */
    public void chainWave(ModelPart[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; ++index) {
            boxes[index].xRot += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }

    }

    /**
     * 模型链z摇摆动画
     */
    public void chainFlap(ModelPart[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; ++index) {
            boxes[index].zRot += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * 计算链偏移
     */
    private float calculateChainOffset(double rootOffset, ModelPart... boxes) {
        return (float) (rootOffset * Math.PI / (double) (2 * boxes.length));
    }

    /**
     * 计算链旋转
     */
    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex) {
        return Mth.cos(swing * speed * this.movementScale + offset * (float) boxIndex) * swingAmount * degree * this.movementScale;
    }


    protected static float toRadians(double degree) {
        return (float) degree * ((float) Math.PI / 180F);
    }

    public float getMovementScale() {
        return movementScale;
    }

    public void setMovementScale(float movementScale) {
        this.movementScale = movementScale;
    }

    /****************** 以下是基于 AnimationDefinition 播放动画方法 ****************** */

    public Optional<ModelPart> getAnyDescendantWithName(String name) {
        return name.equals("root") ? Optional.of(this.root()) : this.root().getAllParts().filter((part) -> {
            return part.hasChild(name);
        }).findFirst().map((part) -> {
            return part.getChild(name);
        });
    }

    protected void animate(AnimationState animationState, AnimationDefinition animationDefinition, float ageInTicks) {
        this.animate(animationState, animationDefinition, ageInTicks, 1.0F);
    }

    protected void animate(AnimationState animationState, AnimationDefinition animationDefinition, float ageInTicks, float speed) {
        animationState.updateTime(ageInTicks, speed);
        animationState.ifStarted((state) -> {
            EMKeyframeAnimations.animate(this, animationDefinition, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }

    protected void animateWalk(AnimationDefinition animationDefinition, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor) {
        long i = (long) (limbSwing * 50.0F * maxAnimationSpeed);
        float f = Math.min(limbSwingAmount * animationScaleFactor, 1.0F);
        EMKeyframeAnimations.animate(this, animationDefinition, i, f, ANIMATION_VECTOR_CACHE);
    }

}
