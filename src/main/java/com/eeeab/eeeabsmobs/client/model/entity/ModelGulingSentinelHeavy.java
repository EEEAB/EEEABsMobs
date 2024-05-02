package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.guling.EntityGulingSentinelHeavy;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelGulingSentinelHeavy extends EMAdvancedEntityModel<EntityGulingSentinelHeavy> {
    private final AdvancedModelBox root;
    private final AdvancedModelBox cube_r1;
    private final AdvancedModelBox upper;
    private final AdvancedModelBox body;
    private final AdvancedModelBox cube_r2;
    private final AdvancedModelBox head;
    private final AdvancedModelBox cube_r3;
    private final AdvancedModelBox rightArm;
    public final AdvancedModelBox finger1;
    private final AdvancedModelBox cube_r4;
    private final AdvancedModelBox finger2;
    private final AdvancedModelBox cube_r5;
    private final AdvancedModelBox finger3;
    private final AdvancedModelBox cube_r6;
    private final AdvancedModelBox leftArm;
    public final AdvancedModelBox finger4;
    private final AdvancedModelBox cube_r7;
    private final AdvancedModelBox finger5;
    private final AdvancedModelBox cube_r8;
    private final AdvancedModelBox finger6;
    private final AdvancedModelBox cube_r9;
    private final AdvancedModelBox lower;
    private final AdvancedModelBox leftHand;
    private final AdvancedModelBox rightHand;
    private final AdvancedModelBox leftLeg;
    private final AdvancedModelBox rightLeg;
    protected ModelAnimator animator;

    public final AdvancedModelBox core;


    public ModelGulingSentinelHeavy() {
        texHeight = 256;
        texWidth = 256;

        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        cube_r1 = new AdvancedModelBox(this, "cube_r1");
        offsetAndRotation(cube_r1, 0.0F, -22.0F, 0.0F, 1.5708F, 0.0F, 0.0F);
        root.addChild(cube_r1);
        cube_r1.setTextureOffset(197, 12).addBox(-9.0F, -7.0F, -2.7F, 18.0F, 14.0F, 2.0F, -0.16F);
        cube_r1.setTextureOffset(72, 6).addBox(-11.0F, -7.0F, -1.0F, 22.0F, 14.0F, 8.0F, -0.08F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, -29.0F, 0.0F);
        root.addChild(upper);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, 0.2F, 1.6F);
        upper.addChild(body);

        cube_r2 = new AdvancedModelBox(this, "cube_r2");
        offsetAndRotation(cube_r2, 0.0F, -22.2F, 4.4F, 1.5708F, 0.0F, 0.0F);
        body.addChild(cube_r2);
        cube_r2.setTextureOffset(0, 28).addBox(-20.0F, -16.0F, -22.0F, 40.0F, 20.0F, 32.0F);

        core = new AdvancedModelBox(this, "core");
        core.setPos(0.0F, -14.2F, 6.4F);
        body.addChild(core);
        core.setTextureOffset(132, 16).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, -21.2F, -16.6F);
        body.addChild(head);

        cube_r3 = new AdvancedModelBox(this, "cube_r3");
        offsetAndRotation(cube_r3, 0.0F, -5.0F, -3.0F, 1.6755F, 0.0F, 0.0F);
        head.addChild(cube_r3);
        cube_r3.setTextureOffset(0, 0).addBox(-8.0F, -3.75F, -11.0F, 16.0F, 12.0F, 16.0F, -0.08F);

        rightArm = new AdvancedModelBox(this, "rightArm");
        offsetAndRotation(rightArm, -19.5F, -27.2F, -1.6F, 0.0F, 0.0F, 0.0873F);
        body.addChild(rightArm);
        rightArm.setTextureOffset(31, 80).addBox(-14.5F, -8.0F, -6.0F, 14.0F, 24.0F, 12.0F, -0.08F);

        rightHand = new AdvancedModelBox(this, "rightHand");
        offsetAndRotation(rightHand, -12.5F, 16.25F, 0.0F, 0.0F, 0.0F, -0.1309F);
        rightArm.addChild(rightHand);
        rightHand.setTextureOffset(83, 82).addBox(-5.5F, -0.25F, -6.0F, 11.0F, 22.0F, 12.0F, -0.08F);

        finger1 = new AdvancedModelBox(this, "finger1");
        finger1.setPos(-2.0F, 21.75F, -2.5F);
        rightHand.addChild(finger1);

        cube_r4 = new AdvancedModelBox(this, "cube_r4");
        offsetAndRotation(cube_r4, 33.75F, -11.625F, 1.7813F, 0.0F, 3.1416F, 0.0F);
        finger1.addChild(cube_r4);
        cube_r4.setTextureOffset(145, 80).addBox(32.25F, 11.625F, -0.7188F, 3.0F, 10.0F, 5.0F, -0.08F);

        finger2 = new AdvancedModelBox(this, "finger2");
        finger2.setPos(-2.0F, 21.75F, 3.25F);
        rightHand.addChild(finger2);

        cube_r5 = new AdvancedModelBox(this, "cube_r5");
        offsetAndRotation(cube_r5, 33.75F, -11.625F, -3.9688F, 0.0F, 3.1416F, 0.0F);
        finger2.addChild(cube_r5);
        cube_r5.setTextureOffset(145, 80).addBox(32.25F, 11.625F, -6.4688F, 3.0F, 10.0F, 5.0F, -0.08F);

        finger3 = new AdvancedModelBox(this, "finger3");
        finger3.setPos(2.75F, 22.75F, -2.5F);
        rightHand.addChild(finger3);

        cube_r6 = new AdvancedModelBox(this, "cube_r6");
        offsetAndRotation(cube_r6, 29.0F, -12.625F, 1.7813F, 0.0F, 3.1416F, 0.0F);
        finger3.addChild(cube_r6);
        cube_r6.setTextureOffset(129, 82).addBox(27.5F, 11.625F, -0.7188F, 3.0F, 8.0F, 5.0F, -0.08F);

        leftArm = new AdvancedModelBox(this, "leftArm");
        offsetAndRotation(leftArm, 20.0F, -27.2F, -1.6F, 0.0F, 0.0F, -0.0873F);
        body.addChild(leftArm);
        leftArm.setTextureOffset(31, 80).addBox(0.0F, -8.0F, -6.0F, 14.0F, 24.0F, 12.0F, -0.08F, true);

        leftHand = new AdvancedModelBox(this, "leftHand");
        offsetAndRotation(leftHand, 11.5F, 16.0F, 0.0F, 0.0F, 0.0F, 0.1309F);
        leftArm.addChild(leftHand);
        leftHand.setTextureOffset(83, 82).addBox(-6.0F, 0.0F, -6.0F, 11.0F, 22.0F, 12.0F, -0.08F, true);

        finger4 = new AdvancedModelBox(this, "finger4");
        finger4.setPos(1.5F, 23.0F, -2.5F);
        leftHand.addChild(finger4);

        cube_r7 = new AdvancedModelBox(this, "cube_r7");
        offsetAndRotation(cube_r7, -33.25F, -12.625F, 1.7813F, 0.0F, -3.1416F, 0.0F);
        finger4.addChild(cube_r7);
        cube_r7.setTextureOffset(145, 80).addBox(-34.75F, 11.625F, -0.7188F, 3.0F, 10.0F, 5.0F, -0.08F, true);

        finger5 = new AdvancedModelBox(this, "finger5");
        finger5.setPos(1.5F, 22.0F, 3.25F);
        leftHand.addChild(finger5);

        cube_r8 = new AdvancedModelBox(this, "cube_r8");
        offsetAndRotation(cube_r8, -33.25F, -11.625F, -3.9688F, 0.0F, -3.1416F, 0.0F);
        finger5.addChild(cube_r8);
        cube_r8.setTextureOffset(145, 80).addBox(-34.75F, 11.625F, -6.4688F, 3.0F, 10.0F, 5.0F, -0.08F, true);

        finger6 = new AdvancedModelBox(this, "finger6");
        finger6.setPos(-3.25F, 22.0F, -2.5F);
        leftHand.addChild(finger6);

        cube_r9 = new AdvancedModelBox(this, "cube_r9");
        offsetAndRotation(cube_r9, -28.5F, -11.625F, 1.7813F, 0.0F, 3.1416F, 0.0F);
        finger6.addChild(cube_r9);
        cube_r9.setTextureOffset(129, 82).addBox(-30.0F, 11.625F, -0.7188F, 3.0F, 8.0F, 5.0F, -0.08F, true);

        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(0.0F, 0.0F, 0.0F);
        root.addChild(lower);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        offsetAndRotation(leftLeg, 14.0F, -20.0F, 0.0F, 0.0F, 0.0F, -0.0175F);
        lower.addChild(leftLeg);
        leftLeg.setTextureOffset(144, 48).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 20.0F, 12.0F, -0.08F, true);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        offsetAndRotation(rightLeg, -14.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0175F);
        lower.addChild(rightLeg);
        rightLeg.setTextureOffset(144, 48).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 20.0F, 12.0F, -0.08F);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }


    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.upper, this.lower, this.body, this.core, this.head, this.leftArm, this.leftHand, this.rightArm, this.rightHand, this.leftLeg, this.rightLeg,
                this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, this.cube_r8, this.cube_r9,
                this.finger1, this.finger2, this.finger3, this.finger4, this.finger5, this.finger6);
    }

    @Override
    public void setupAnim(EntityGulingSentinelHeavy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        //LookAt
        if (entity.getAnimation() == EntityGulingSentinelHeavy.RANGE_ATTACK_ANIMATION && entity.getAnimationTick() > 10) {
            this.faceTarget(netHeadYaw, headPitch, 3F, this.upper);
        } else {
            this.faceTarget(netHeadYaw, headPitch, 1.5F, this.head);
        }

        //Idle & Walk
        if (!entity.isAlive() || !entity.isActive() || entity.getAnimation() == EntityGulingSentinelHeavy.ACTIVE_ANIMATION)
            return;
        float pitch = headPitch * 0.017453292F;
        float headYaw = netHeadYaw * 0.017453292F;
        float cycle = 0.5F;
        float idle = (Mth.sin(ageInTicks * cycle * 0.1F) + 1.0F) * (1.0F - limbSwingAmount);
        float rebound = limbSwing * cycle % Mth.PI / Mth.PI;
        rebound = 1.0F - rebound;
        rebound *= rebound;
        this.head.rotateAngleX += pitch - idle * 0.1F;
        this.head.rotateAngleY += headYaw * 0.5F;
        this.upper.rotateAngleX += idle * 0.05F;
        this.upper.rotateAngleY += headYaw * 0.25F;
        this.upper.rotationPointY += Mth.sin(rebound * 3.1415927F) * 2.0F * limbSwingAmount;
        this.cube_r1.rotateAngleX += idle * 0.05F;
        this.leftArm.rotateAngleX += (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.1F - 0.2F - idle * -0.1F;
        this.leftArm.rotationPointY += Mth.sin(rebound * 3.1415927F) * 2.5F * limbSwingAmount;
        this.rightArm.rotateAngleX += (Mth.cos(limbSwing * cycle + 3.1415927F) - 2.0F) * limbSwingAmount * 0.1F - 0.2F - idle * -0.1F;
        this.rightArm.rotationPointY += Mth.sin(rebound * 3.1415927F) * 2.5F * limbSwingAmount;
        this.lower.rotationPointY += Mth.sin(rebound * 3.1415927F) * 2.0F * limbSwingAmount;
        this.lower.rotateAngleY += headYaw * 0.25F;
        this.lower.rotateAngleZ += Mth.sin(limbSwing * cycle) * limbSwingAmount * 0.05F;
        this.leftLeg.rotateAngleX += Mth.cos(limbSwing * cycle + 4.3982296F) * limbSwingAmount * 0.2F;
        this.leftLeg.rotationPointY += Mth.clamp(Mth.sin(limbSwing * cycle) * limbSwingAmount * 3.0F, Float.NEGATIVE_INFINITY, 0F);
        this.leftLeg.rotationPointZ += Mth.cos(limbSwing * cycle + 3.1415927F) * limbSwingAmount * 8.0F;
        this.rightLeg.rotateAngleX += Mth.cos(limbSwing * cycle + 1.2566371F) * limbSwingAmount * 0.2F;
        this.rightLeg.rotationPointY += Mth.clamp(Mth.sin(limbSwing * cycle + 3.1415927F) * limbSwingAmount * 3.0F, Float.NEGATIVE_INFINITY, 0F);
        this.rightLeg.rotationPointZ += Mth.cos(limbSwing * cycle) * limbSwingAmount * 8.0F;
    }

    private void animate(EntityGulingSentinelHeavy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animator.update(entity);

        if (entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            if (!entity.isActive()) {
                setStaticRotationPoint(core, 0F, 0F, -5F);
                setStaticRotationAngle(head, toRadians(6F), 0F, 0F);
                setStaticRotationAngle(rightArm, 0F, 0F, toRadians(-5F));
                setStaticRotationPoint(rightArm, 0F, 5F, 0F);
                setStaticRotationAngle(rightHand, 0F, 0F, toRadians(7.5F));
                setStaticRotationPoint(rightHand, 4F, 0F, 0F);
                setStaticRotationAngle(leftArm, 0F, 0F, toRadians(5F));
                setStaticRotationPoint(leftArm, 0F, 5F, 0F);
                setStaticRotationAngle(leftHand, 0F, 0F, toRadians(-7.5F));
                setStaticRotationPoint(leftHand, -4F, 0F, 0F);
                setStaticRotationAngle(rightLeg, 0F, 0F, toRadians(-1F));
                setStaticRotationAngle(leftLeg, 0F, 0F, toRadians(-1F));
                setStaticRotationPoint(upper, 0F, 3F, 0F);
            }
        }
        if (animator.setAnimation(EntityGulingSentinelHeavy.ACTIVE_ANIMATION)) {
            animator.startKeyframe(0);
            animator.move(core, 0F, 0F, -5F);
            animator.rotate(head, toRadians(6F), 0F, 0F);
            animator.rotate(rightArm, 0F, 0F, toRadians(-5F));
            animator.move(rightArm, 0F, 5F, 0F);
            animator.rotate(rightHand, 0F, 0F, toRadians(7.5F));
            animator.move(rightHand, 4F, 0F, 0F);
            animator.rotate(leftArm, 0F, 0F, toRadians(5F));
            animator.move(leftArm, 0F, 5F, 0F);
            animator.rotate(leftHand, 0F, 0F, toRadians(-7.5F));
            animator.move(leftHand, -4F, 0F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(-1F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-1F));
            animator.move(upper, 0F, 3F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(5);//0.25
            animator.move(core, 0F, 0F, -5F);
            animator.rotate(head, 0F, 0F, 0F);
            animator.rotate(rightArm, 0F, 0F, toRadians(-5F));
            animator.move(rightArm, 0F, 5F, 0F);
            animator.rotate(rightHand, 0F, 0F, toRadians(7.5F));
            animator.move(rightHand, 4F, 0F, 0F);
            animator.rotate(leftArm, 0F, 0F, toRadians(5F));
            animator.move(leftArm, 0F, 5F, 0F);
            animator.rotate(leftHand, 0F, 0F, toRadians(-7.5F));
            animator.move(leftHand, -4F, 0F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(-1F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-1F));
            animator.move(upper, 0F, 3F, 0F);
            animator.endKeyframe();

            animator.setStaticKeyframe(5);//0.5

            animator.startKeyframe(5);//0.75
            animator.move(core, 0F, 0F, -5F);
            animator.rotate(head, 0F, 0F, 0F);
            animator.rotate(rightArm, 0F, 0F, toRadians(-5F));
            animator.move(rightArm, 0F, 5F, 0F);
            animator.rotate(rightHand, 0F, 0F, toRadians(7.5F));
            animator.move(rightHand, 4F, 0F, 0F);
            animator.rotate(leftArm, 0F, 0F, toRadians(5F));
            animator.move(leftArm, 0F, 5F, 0F);
            animator.rotate(leftHand, 0F, 0F, toRadians(-7.5F));
            animator.move(leftHand, -4F, 0F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(-1F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-1F));
            animator.move(upper, 0F, 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(5);//1
            animator.move(core, 0F, 0F, -5F);
            animator.rotate(head, 0F, 0F, 0F);
            animator.rotate(rightArm, 0F, 0F, toRadians(-5F));
            animator.move(rightArm, 0F, 0F, 0F);
            animator.rotate(rightHand, 0F, 0F, toRadians(7.5F));
            animator.move(rightHand, 4F, 0F, 0F);
            animator.rotate(leftArm, 0F, 0F, toRadians(5F));
            animator.move(leftArm, 0F, 0F, 0F);
            animator.rotate(leftHand, 0F, 0F, toRadians(-7.5F));
            animator.move(leftHand, -4F, 0F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(-1F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-1F));
            animator.move(upper, 0F, 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(5);//1.25
            animator.move(core, 0F, 0F, -5F);
            animator.rotate(head, 0F, 0F, 0F);
            animator.rotate(rightArm, 0F, 0F, toRadians(-5F));
            animator.move(rightArm, 0F, 0F, 0F);
            animator.rotate(rightHand, 0F, 0F, toRadians(7.5F));
            animator.move(rightHand, 0F, 0F, 0F);
            animator.rotate(leftArm, 0F, 0F, toRadians(5F));
            animator.move(leftArm, 0F, 0F, 0F);
            animator.rotate(leftHand, 0F, 0F, toRadians(-7.5F));
            animator.move(leftHand, 0F, 0F, 0F);
            animator.rotate(rightLeg, 0F, 0F, 0);
            animator.rotate(leftLeg, 0F, 0F, 0);
            animator.move(upper, 0F, 0F, 0F);
            animator.endKeyframe();

            animator.resetKeyframe(5);//1.5
        } else if (animator.setAnimation(EntityGulingSentinelHeavy.DEACTIVATE_ANIMATION)) {
            animator.startKeyframe(30);
            animator.move(core, 0F, 0F, -5F);
            animator.rotate(head, toRadians(6F), 0F, 0F);
            animator.rotate(rightArm, 0F, 0F, toRadians(-5F));
            animator.move(rightArm, 0F, 5F, 0F);
            animator.rotate(rightHand, 0F, 0F, toRadians(7.5F));
            animator.move(rightHand, 4F, 0F, 0F);
            animator.rotate(leftArm, 0F, 0F, toRadians(5F));
            animator.move(leftArm, 0F, 5F, 0F);
            animator.rotate(leftHand, 0F, 0F, toRadians(-7.5F));
            animator.move(leftHand, -4F, 0F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(-1F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-1F));
            animator.move(upper, 0F, 3F, 0F);
            animator.endKeyframe();
        } else if (animator.setAnimation(EntityGulingSentinelHeavy.ATTACK_ANIMATION_LEFT)) {
            animator.startKeyframe(4);//0.2
            animator.rotate(upper, toRadians(10F), toRadians(40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(rightArm, toRadians(25F), 0F, 0F);
            animator.move(rightArm, 0F, 0F, 1F);
            animator.rotate(rightHand, toRadians(-80F), 0F, 0F);
            animator.rotate(leftArm, toRadians(-17.5F), 0F, 0F);
            animator.move(leftArm, 0F, 0F, 1F);
            animator.rotate(leftHand, toRadians(-25F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.4
            animator.rotate(upper, toRadians(10F), toRadians(-40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(rightArm, toRadians(-70F), toRadians(25F), 0F);
            animator.move(rightArm, 0F, 0F, 1F);
            animator.rotate(rightHand, toRadians(-15F), 0F, 0F);
            animator.rotate(leftArm, toRadians(20F), toRadians(5F), toRadians(-20F));
            animator.move(leftArm, 0F, 0F, 1F);
            animator.rotate(leftHand, toRadians(-35F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.52
            animator.rotate(upper, toRadians(10F), toRadians(-40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(rightArm, toRadians(-70F), toRadians(25F), 0F);
            animator.move(rightArm, -5F, 0F, -20F);
            animator.rotate(rightHand, toRadians(-15F), 0F, 0F);
            animator.rotate(leftArm, toRadians(20F), toRadians(5F), toRadians(-20F));
            animator.move(leftArm, 0F, 0F, 1F);
            animator.rotate(leftHand, toRadians(-35F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.64
            animator.rotate(upper, toRadians(10F), toRadians(-40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(rightArm, toRadians(-70F), toRadians(25F), 0F);
            animator.move(rightArm, 0F, 0F, -5F);
            animator.rotate(rightHand, toRadians(-15F), 0F, 0F);
            animator.rotate(leftArm, toRadians(20F), toRadians(5F), toRadians(-20F));
            animator.move(leftArm, 0F, 0F, 1F);
            animator.rotate(leftHand, toRadians(-35F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.84
            animator.rotate(upper, 0F, toRadians(75F), toRadians(5F));
            animator.move(upper, -1F, 1F, 0F);
            animator.rotate(rightArm, toRadians(20F), toRadians(25F), 0F);
            animator.move(rightArm, 1F, 1F, 1F);
            animator.rotate(rightHand, toRadians(-27.5F), toRadians(10F), 0F);
            animator.rotate(leftArm, toRadians(-40F), toRadians(65F), toRadians(-55F));
            animator.move(leftArm, 3F, 0F, 7F);
            animator.rotate(leftHand, toRadians(-65F), 0F, toRadians(-25F));
            animator.endKeyframe();

            animator.startKeyframe(4);//1.04
            animator.rotate(upper, toRadians(5F), toRadians(-15F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(rightArm, toRadians(8.07F), toRadians(-1.93F), toRadians(36.93F));
            animator.move(rightArm, 0F, 0F, 0.67F);
            animator.rotate(rightHand, toRadians(-63.46F), toRadians(-1.15F), 0F);
            animator.rotate(leftArm, toRadians(66.17F), toRadians(-28.85F), toRadians(-72.34F));
            animator.move(leftArm, 6.23F, 5.08F, 1F);
            animator.rotate(leftHand, toRadians(-25F), 0F, 0F);
            animator.endKeyframe();

            animator.resetKeyframe(4);
        } else if (animator.setAnimation(EntityGulingSentinelHeavy.ATTACK_ANIMATION_RIGHT)) {
            animator.startKeyframe(4);//0.2
            animator.rotate(upper, toRadians(10F), toRadians(-40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(leftArm, toRadians(25F), 0F, 0F);
            animator.move(leftArm, 0F, 0F, 1F);
            animator.rotate(leftHand, toRadians(-80F), 0F, 0F);
            animator.rotate(rightArm, toRadians(-17.5F), 0F, 0F);
            animator.move(rightArm, 0F, 0F, -1F);
            animator.rotate(rightHand, toRadians(-25F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.4
            animator.rotate(upper, toRadians(10F), toRadians(40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(leftArm, toRadians(-70F), toRadians(-25F), 0F);
            animator.move(leftArm, 0F, 0F, 1F);
            animator.rotate(leftHand, toRadians(-35F), 0F, 0F);
            animator.rotate(rightArm, toRadians(20F), toRadians(-5F), toRadians(20F));
            animator.move(rightArm, 0F, 0F, -1F);
            animator.rotate(rightHand, toRadians(-15F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.52
            animator.rotate(upper, toRadians(10F), toRadians(40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(leftArm, toRadians(-70F), toRadians(-25F), 0F);
            animator.move(leftArm, 5F, 0F, -20F);
            animator.rotate(leftHand, toRadians(-35F), 0F, 0F);
            animator.rotate(rightArm, toRadians(20F), toRadians(-5F), toRadians(20F));
            animator.move(rightArm, 0F, 0F, -1F);
            animator.rotate(rightHand, toRadians(-15F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.64
            animator.rotate(upper, toRadians(10F), toRadians(40F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(leftArm, toRadians(-70F), toRadians(-25F), 0F);
            animator.move(leftArm, 0F, 0F, -5F);
            animator.rotate(rightHand, toRadians(-15F), 0F, 0F);
            animator.rotate(rightArm, toRadians(20F), toRadians(-5F), toRadians(20F));
            animator.move(rightArm, 0F, 0F, -1F);
            animator.rotate(leftHand, toRadians(-35F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.84
            animator.rotate(upper, 0F, toRadians(-75F), toRadians(-5F));
            animator.move(upper, -1F, 1F, 0F);
            animator.rotate(leftArm, toRadians(20F), toRadians(-25F), 0F);
            animator.move(leftArm, -1F, -1F, 1F);
            animator.rotate(leftHand, toRadians(-65F), 0F, toRadians(-25F));
            animator.rotate(rightArm, toRadians(-40F), toRadians(-65F), toRadians(55F));
            animator.move(rightArm, -3F, 0F, -7F);
            animator.rotate(rightHand, toRadians(-27.5F), toRadians(10F), 0F);
            animator.endKeyframe();

            animator.startKeyframe(4);//1.04
            animator.rotate(upper, toRadians(5F), toRadians(15F), 0F);
            animator.move(upper, 0F, -1F, 0F);
            animator.rotate(leftArm, toRadians(8.07F), toRadians(1.93F), toRadians(-36.93F));
            animator.move(leftArm, 0F, 0F, 0.67F);
            animator.rotate(leftHand, toRadians(-25F), 0F, 0F);
            animator.rotate(rightArm, toRadians(66.17F), toRadians(28.85F), toRadians(72.34F));
            animator.move(rightArm, -6.23F, 5.08F, -1F);
            animator.rotate(rightHand, toRadians(-63.46F), toRadians(-1.15F), 0F);
            animator.endKeyframe();

            animator.resetKeyframe(4);
        } else if (animator.setAnimation(EntityGulingSentinelHeavy.SMASH_ATTACK_ANIMATION)) {
            animator.startKeyframe(15);//0.75
            animator.move(root, 0F, 1F, 0F);
            animator.move(core, 0F, 0F, 3F);
            animator.rotate(head, toRadians(10F), 0F, 0F);
            animator.rotate(upper, toRadians(-30F), 0F, 0F);
            animator.move(upper, 0F, 1F, 0F);
            animator.move(lower, 0F, -1F, 0F);
            animator.rotate(rightArm, toRadians(-120F), 0F, 0F);
            animator.move(rightArm, 0F, 0F, 0F);
            animator.rotate(rightHand, toRadians(-25F), 0F, 0F);
            animator.rotate(leftArm, toRadians(-120F), 0F, 0F);
            animator.move(leftArm, 0F, 0F, 0F);
            animator.rotate(leftHand, toRadians(-25F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.87
            animator.move(root, 0F, 1F, 0F);
            animator.move(core, 0F, 0F, -3F);
            animator.rotate(head, toRadians(-25F), 0F, 0F);
            animator.rotate(upper, toRadians(60F), 0F, 0F);
            animator.move(upper, 0F, 1F, 0F);
            animator.move(lower, 0F, -1F, 0F);
            animator.rotate(rightArm, toRadians(-120F), 0F, 0F);
            animator.move(rightArm, 0F, 0F, 0F);
            animator.rotate(rightHand, toRadians(-25F), 0F, 0F);
            animator.rotate(leftArm, toRadians(-120F), 0F, 0F);
            animator.move(leftArm, 0F, 0F, 0F);
            animator.rotate(leftHand, toRadians(-25F), 0F, 0F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.96
            animator.move(root, 0F, 1.5F, 0F);
            animator.move(core, 0F, 0F, -3F);
            animator.rotate(head, toRadians(-25F), 0F, 0F);
            animator.rotate(upper, toRadians(60F), 0F, 0F);
            animator.move(upper, 0F, 3F, 0F);
            animator.move(lower, 0F, -1.5F, 0F);
            animator.rotate(rightArm, toRadians(-60F), 0F, 0F);
            animator.move(rightArm, 0F, 5F, -5F);
            animator.rotate(rightHand, toRadians(-50F), 0F, 0F);
            animator.rotate(leftArm, toRadians(-60F), 0F, 0F);
            animator.move(leftArm, 0F, 5F, -5F);
            animator.rotate(leftHand, toRadians(-50F), 0F, 0F);
            animator.endKeyframe();

            animator.resetKeyframe(11);
        } else if (animator.setAnimation(EntityGulingSentinelHeavy.RANGE_ATTACK_ANIMATION)) {
            animator.startKeyframe(10);
            animator.rotate(upper, toRadians(5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 0F);
            animator.rotate(rightArm, toRadians(15F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 0F);
            animator.rotate(leftArm, toRadians(15F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.setStaticKeyframe(15);

            animator.startKeyframe(1);
            animator.rotate(upper, toRadians(-5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 2F);
            animator.rotate(rightArm, toRadians(25F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 2F);
            animator.rotate(leftArm, toRadians(25F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 2F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(upper, toRadians(5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 0F);
            animator.rotate(rightArm, toRadians(15F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 0F);
            animator.rotate(leftArm, toRadians(15F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.setStaticKeyframe(15);

            animator.startKeyframe(1);
            animator.rotate(upper, toRadians(-5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 2F);
            animator.rotate(rightArm, toRadians(25F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 2F);
            animator.rotate(leftArm, toRadians(25F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 2F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(upper, toRadians(5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 0F);
            animator.rotate(rightArm, toRadians(15F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 0F);
            animator.rotate(leftArm, toRadians(15F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.setStaticKeyframe(15);

            animator.startKeyframe(1);
            animator.rotate(upper, toRadians(-5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 2F);
            animator.rotate(rightArm, toRadians(25F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 2F);
            animator.rotate(leftArm, toRadians(25F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 2F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(upper, toRadians(5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 0F);
            animator.rotate(rightArm, toRadians(15F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 0F);
            animator.rotate(leftArm, toRadians(15F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.setStaticKeyframe(15);

            animator.startKeyframe(1);
            animator.rotate(upper, toRadians(-5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 2F);
            animator.rotate(rightArm, toRadians(25F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 2F);
            animator.rotate(leftArm, toRadians(25F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 2F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(upper, toRadians(5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 0F);
            animator.rotate(rightArm, toRadians(15F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 0F);
            animator.rotate(leftArm, toRadians(15F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();

            animator.resetKeyframe(10);
        } else if (animator.setAnimation(EntityGulingSentinelHeavy.RANGE_ATTACK_END_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(upper, toRadians(5F), 0F, 0F);
            animator.move(upper, 0F, 2F, 0F);
            animator.rotate(rightArm, toRadians(15F), 0F, 0F);
            animator.rotate(rightHand, toRadians(-100F), toRadians(-5F), toRadians(15F));
            animator.move(rightHand, 0F, 6F, 0F);
            animator.rotate(leftArm, toRadians(15F), 0F, 0F);
            animator.rotate(leftHand, toRadians(-100F), toRadians(5F), toRadians(-15F));
            animator.move(leftHand, 0F, 6F, 0F);
            animator.rotate(rightLeg, 0F, 0F, toRadians(5F));
            animator.rotate(leftLeg, 0F, 0F, toRadians(-5F));
            animator.endKeyframe();
            animator.resetKeyframe(10);
        } else if (animator.setAnimation(EntityGulingSentinelHeavy.ELECTROMAGNETIC_ANIMATION)) {
        }
    }

}
