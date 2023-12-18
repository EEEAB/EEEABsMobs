package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortal;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ModelImmortalBase<T extends EntityImmortal & IAnimatedEntity> extends EEAdvancedEntityModel<T> {
    protected AdvancedModelBox root;
    protected AdvancedModelBox upper;
    protected AdvancedModelBox lower;
    protected AdvancedModelBox head;
    protected AdvancedModelBox body;
    protected AdvancedModelBox leftArm;
    protected AdvancedModelBox rightArm;
    protected AdvancedModelBox leftLeg;
    protected AdvancedModelBox rightLeg;
    protected ModelAnimator animator;

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        this.spawnAnimate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        this.setRotationAnglesSpawn(entity, frame);
    }

    protected void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {

    }


    protected void spawnAnimate(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float delta) {
        this.resetToDefaultPose();
        animator.update(entity);
        if (animator.setAnimation(getSpawnAnimation())) {
            animator.startKeyframe(0);
            animator.move(root, 0, 35, 0);
            animator.rotate(head, toRadians(-50), 0, 0);
            animator.rotate(leftArm, toRadians(handsOffset()), 0, 0);
            animator.rotate(rightArm, toRadians(handsOffset()), 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(35);
            animator.move(root, 0, 0, 0);
            animator.rotate(head, toRadians(-40), 0, 0);
            animator.rotate(leftArm, toRadians(handsOffset()), 0, 0);
            animator.rotate(rightArm, toRadians(handsOffset()), 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
        this.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, delta);
    }

    public void setRotationAnglesSpawn(T entityIn, float frame) {
        Animation spawnAnimation = getSpawnAnimation();
        if (entityIn.getAnimation() == spawnAnimation) {
            if (entityIn.getAnimationTick() < spawnAnimation.getDuration()) {
                this.walk(rightArm, 0.5F, 0.5F, true, 1, 0, frame, 1);
                this.walk(leftArm, 0.5F, 0.5F, false, 1, 0, frame, 1);
            }
        }
    }

    protected double handsOffset() {
        return -180;
    }

    protected abstract Animation getSpawnAnimation();
}
