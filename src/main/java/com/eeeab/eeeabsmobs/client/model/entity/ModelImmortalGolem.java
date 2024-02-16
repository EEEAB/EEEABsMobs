package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalGolem;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.world.entity.HumanoidArm;

public class ModelImmortalGolem extends EMCanSpawnEntityModel<EntityImmortalGolem> implements ArmedModel {

    public ModelImmortalGolem() {
        texHeight = 128;
        texWidth = 128;

        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, 0.0F, 0.0F);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, -18.0F, 0.0F);
        upper.addChild(head);
        head.setTextureOffset(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, -0.3F);
        head.setTextureOffset(34, 0).addBox(-4.0F, -6.5F, -4.0F, 8.0F, 8.0F, 8.0F, -0.6F);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, -18.0F, 0.0F);
        upper.addChild(body);
        body.setTextureOffset(11, 18).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 10.0F, 4.0F);

        leftArm = new AdvancedModelBox(this, "leftArm");
        leftArm.setPos(5.0F, 1.0F, 0.0F);
        body.addChild(leftArm);
        leftArm.setTextureOffset(35, 20).addBox(-1.5F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, true);
        setRotationAngle(leftArm, toRadians(-90), 0, 0);

        rightArm = new AdvancedModelBox(this, "rightArm");
        rightArm.setPos(-5.0F, 1.0F, 0.0F);
        body.addChild(rightArm);
        rightArm.setTextureOffset(35, 20).addBox(-0.5F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F);
        setRotationAngle(rightArm, toRadians(-90), 0, 0);


        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(0.0F, 0.0F, 0.0F);
        root.addChild(lower);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        leftLeg.setPos(2.0F, -9.0F, 0.1F);
        lower.addChild(leftLeg);
        leftLeg.setTextureOffset(1, 22).addBox(-1.0F, 1.0F, -1.1F, 2.0F, 8.0F, 2.0F, true);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        rightLeg.setPos(-2.0F, -9.0F, 0.1F);
        lower.addChild(rightLeg);
        rightLeg.setTextureOffset(1, 22).addBox(-1.0F, 1.0F, -1.1F, 2.0F, 8.0F, 2.0F);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }


    private void animate(EntityImmortalGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        if (this.animator.setAnimation(EntityImmortalGolem.ATTACK_ANIMATION)) {
            this.animator.setStaticKeyframe(2);
            this.animator.startKeyframe(5);
            this.animator.rotate(root, toRadians(-6), 0, 0);
            this.animator.rotate(head, 0, 0, 0);
            this.animator.rotate(leftArm, toRadians(-70), toRadians(5), 0);
            this.animator.rotate(rightArm, toRadians(-70), toRadians(-5), 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.animator.rotate(root, toRadians(6), 0, 0);
            this.animator.rotate(head, toRadians(-30), 0, 0);
            this.animator.rotate(leftArm, toRadians(40), toRadians(12), 0);
            this.animator.rotate(rightArm, toRadians(40), toRadians(-12), 0);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(2);
        }
        if (this.animator.setAnimation(EntityImmortalGolem.HURT_ANIMATION)) {
            this.animator.startKeyframe(3);
            this.animator.rotate(upper, toRadians(2.5), 0, toRadians(-2.5));
            this.animator.rotate(rightArm, toRadians(2.5), 0, toRadians(-2.5));
            this.animator.rotate(leftArm, toRadians(2.5), 0, toRadians(-2.5));
            this.animator.rotate(root, toRadians(2.5), 0, toRadians(-1.5));
            this.animator.move(upper, 0, 1, 0);
            this.animator.move(head, 0, 1.5F, 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.animator.rotate(upper, toRadians(-2.5), 0, toRadians(2.5));
            this.animator.rotate(rightArm, toRadians(-2.5), 0, toRadians(2.5));
            this.animator.rotate(leftArm, toRadians(-2.5), 0, toRadians(2.5));
            this.animator.rotate(root, toRadians(-2.5), 0, toRadians(1.5));
            this.animator.move(upper, 0, 1, 0);
            this.animator.move(head, 0, 1.5F, 0);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(4);
        }
    }

    @Override
    protected Animation getSpawnAnimation() {
        return EntityImmortalGolem.SPAWN_ANIMATION;
    }

    @Override
    public void setupAnim(EntityImmortalGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        //LookAt
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);

        //Walk
        float walkSpeed = 0.8F;
        float walkDegree = 0.8F;
        this.flap(this.root, walkSpeed, walkDegree * 0.05F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 0.2F, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 0.2F, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);

        //Idle
        float speed = 0.08F;
        float degree = 0.05F;
        if (entity.isAlive()) {
            this.walk(head, speed, degree, false, 0.5F, -0.05F, frame, 1);
            this.walk(rightArm, speed, degree, true, 0, -0.05F, frame, 1);
            this.swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.walk(leftArm, speed, degree, false, 0, -0.05F, frame, 1);
            this.swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }

        if (entity.isDangerous() && entity.getAnimation() != EntityImmortalGolem.SPAWN_ANIMATION) {
            setStaticRotationAngle(leftArm, toRadians(20), toRadians(25), 0);
            setStaticRotationAngle(rightArm, toRadians(20), toRadians(-25), 0);
        }
    }


    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.upper, this.lower, this.head, this.rightArm, this.leftArm, this.body, this.rightLeg, this.leftLeg);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }


    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        boolean flag = humanoidArm == HumanoidArm.RIGHT;
        AdvancedModelBox model$part = flag ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        model$part.translateAndRotate(poseStack);
        poseStack.scale(0.6F, 0.6F, 0.6F);
        this.offsetStackPosition(poseStack, flag);
    }

    private void offsetStackPosition(PoseStack translate, boolean isRightArm) {
        if (isRightArm) {
            translate.translate(0.125, 0.1625, 0);
        } else {
            translate.translate(-0.125, 0.1625, 0);
        }
    }

    @Override
    protected double handsOffset() {
        return -90;
    }
}