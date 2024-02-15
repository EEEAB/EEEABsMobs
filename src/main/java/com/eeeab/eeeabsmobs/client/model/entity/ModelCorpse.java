package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpse;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;

public class ModelCorpse extends ModelAbsCorpse<EntityCorpse> {

    @Override
    protected void setupAnim(EntityCorpse entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        //LookAt
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);

        //Walk
        float walkSpeed = 0.85F;
        float walkDegree = 0.8F;
        this.flap(this.root, walkSpeed, walkDegree * 0.08F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.walk(this.rightArm, walkSpeed, walkDegree, true, -1, -0.1F, limbSwing, limbSwingAmount);
            this.walk(this.leftArm, walkSpeed, walkDegree, false, -1, 0.1F, limbSwing, limbSwingAmount);
            this.flap(this.rightArm, walkSpeed, walkDegree, true, -1, -0.5F, limbSwing, limbSwingAmount);
            this.flap(this.leftArm, walkSpeed, walkDegree, true, -1, 0.5F, limbSwing, limbSwingAmount);
        }

        //Idle
        float speed = 0.12F;
        float degree = 0.1F;
        if (entity.isAlive()) {
            this.walk(head, speed, degree, false, 0.5F, -0.05F, frame, 1);
            this.walk(upper, speed, degree, true, 0.5F, -0.05F, frame, 1);
            this.walk(rightArm, speed, degree, true, 0, -0.05F, frame, 1);
            this.swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.walk(leftArm, speed, degree, false, 0, -0.05F, frame, 1);
            this.swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
    }

    private void animate(EntityCorpse entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        if (this.animator.setAnimation(EntityCorpse.ATTACK_ANIMATION_HANDS)) {
            animator.setStaticKeyframe(5);
            animator.startKeyframe(5);
            animator.rotate(root, toRadians(-6), 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-150), toRadians(5), 0);
            animator.rotate(rightArm, toRadians(-150), toRadians(-5), 0);
            animator.endKeyframe();
            animator.startKeyframe(3);
            animator.rotate(root, toRadians(6), 0, 0);
            animator.rotate(head, toRadians(-30), 0, 0);
            animator.rotate(leftArm, toRadians(40), toRadians(30), 0);
            animator.rotate(rightArm, toRadians(40), toRadians(-30), 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(3);
            animator.resetKeyframe(4);
        } else if (this.animator.setAnimation(EntityCorpse.ATTACK_ANIMATION_LEFT)) {
            animator.startKeyframe(5);
            animator.rotate(rightArm, toRadians(10), 0, toRadians(10));
            animator.rotate(head, 0, toRadians(15), 0);
            animator.rotate(upper, 0, toRadians(-15), 0);
            animator.rotate(leftArm, toRadians(30), 0, toRadians(-70));
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.rotate(rightArm, toRadians(10), 0, toRadians(10));
            animator.rotate(head, 0, toRadians(-15), 0);
            animator.rotate(upper, 0, toRadians(15), 0);
            animator.rotate(leftArm, toRadians(-70), toRadians(20), toRadians(30));
            animator.endKeyframe();
            animator.resetKeyframe(5);
        } else if (this.animator.setAnimation(EntityCorpse.ATTACK_ANIMATION_RIGHT)) {
            animator.startKeyframe(5);
            animator.rotate(rightArm, toRadians(30), 0, toRadians(70));
            animator.rotate(head, 0, toRadians(-15), 0);
            animator.rotate(upper, 0, toRadians(15), 0);
            animator.rotate(leftArm, toRadians(10), 0, toRadians(-10));
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.rotate(rightArm, toRadians(-70), toRadians(-20), toRadians(30));
            animator.rotate(head, 0, toRadians(15), 0);
            animator.rotate(upper, 0, toRadians(-15), 0);
            animator.rotate(leftArm, toRadians(10), 0, toRadians(-10));
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
    }

    @Override
    protected Animation getSpawnAnimation() {
        return null;
    }
}
