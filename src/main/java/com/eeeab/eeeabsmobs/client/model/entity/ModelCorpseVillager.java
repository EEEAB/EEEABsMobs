package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpse;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseVillager;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelCorpseVillager extends ModelAbsCorpse<EntityCorpseVillager> {

    public ModelCorpseVillager() {
        texHeight = 128;
        texWidth = 128;

        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, -12.0F, 0.0F);
        setRotationAngle(upper, toRadians(5), 0, 0);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, -12.0F, 0.0F);
        head.setTextureOffset(0, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 10.0F, 8.0F)
                .setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 9.0F, 8.0F, -0.2F);
        head.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F);
        upper.addChild(head);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, 0.0F, 0.0F);
        body.setTextureOffset(16, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F)
                .setTextureOffset(60, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, -0.2F);
        body.setTextureOffset(0, 38).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F);
        upper.addChild(body);

        leftArm = new AdvancedModelBox(this, "leftArm");
        leftArm.setPos(5.0F, -10.0F, 0.0F);
        leftArm.setTextureOffset(44, 22).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, true);
        setRotationAngle(leftArm, toRadians(-40), 0, 0);
        upper.addChild(leftArm);

        rightArm = new AdvancedModelBox(this, "rightArm");
        rightArm.setPos(-5.0F, -10.0F, 0.0F);
        rightArm.setTextureOffset(44, 22).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        setRotationAngle(rightArm, toRadians(-40), 0, 0);
        upper.addChild(rightArm);

        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(0.0F, -12.0F, 0.0F);
        root.addChild(lower);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        leftLeg.setPos(2.0F, 0.0F, 0.0F);
        leftLeg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, true);
        lower.addChild(leftLeg);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        rightLeg.setPos(-2.0F, 0.0F, 0.0F);
        rightLeg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        lower.addChild(rightLeg);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.upper, this.lower, this.head, this.rightArm, this.leftArm, this.body, this.rightLeg, this.leftLeg);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    protected void setupAnim(EntityCorpseVillager entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
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

    private void animate(EntityCorpseVillager entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
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
}
