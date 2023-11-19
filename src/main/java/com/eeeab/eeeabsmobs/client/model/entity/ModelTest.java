package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.impl.test.EntityTest;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.world.entity.HumanoidArm;

public class ModelTest extends AdvancedEntityModel<EntityTest> implements ArmedModel {
    private final AdvancedModelBox root;
    private final AdvancedModelBox upper;
    private final AdvancedModelBox head;
    private final AdvancedModelBox body;
    private final AdvancedModelBox rightArm;
    private final AdvancedModelBox leftArm;
    private final AdvancedModelBox lower;
    private final AdvancedModelBox rightLeg;
    private final AdvancedModelBox leftLeg;
    private final ModelAnimator animator;


    public ModelTest() {
        texHeight = 64;
        texWidth = 64;
        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, -24.0F, 0.0F);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, 0.0F, 0.0F);
        upper.addChild(head);
        head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, false)
                .setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, 0.0F, 0.0F);
        upper.addChild(body);
        body.setTextureOffset(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, false)
                .setTextureOffset(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F);

        rightArm = new AdvancedModelBox(this, "rightArm");
        rightArm.setPos(-5.0F, 2.0F, 0.0F);
        body.addChild(rightArm);
        rightArm.setTextureOffset(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, false)
                .setTextureOffset(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F);

        leftArm = new AdvancedModelBox(this, "leftArm");
        leftArm.setPos(5.0F, 2.0F, 0.0F);
        body.addChild(leftArm);
        leftArm.setTextureOffset(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, false)
                .setTextureOffset(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F);


        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(0.0F, 0.0F, 0.0F);
        root.addChild(lower);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        rightLeg.setPos(-1.9F, -12.0F, 0.0F);
        lower.addChild(rightLeg);
        rightLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, false)
                .setTextureOffset(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        leftLeg.setPos(1.9F, -12.0F, 0.0F);
        lower.addChild(leftLeg);
        leftLeg.setTextureOffset(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, false)
                .setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F);

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

    private void animate(EntityTest entityTest, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animator.update(entityTest);
        if (entityTest.getAnimation() == EntityTest.ATTACK_ANIMATION) {
            this.animator.setAnimation(EntityTest.ATTACK_ANIMATION);
            this.animator.setStaticKeyframe(5);
            this.animator.startKeyframe(5);
            this.animator.rotate(root, toRadians(-6), 0, 0);
            this.animator.rotate(head, 0, 0, 0);
            this.animator.rotate(leftArm, toRadians(-150), toRadians(5), 0);
            this.animator.rotate(rightArm, toRadians(-150), toRadians(-5), 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.animator.rotate(root, toRadians(6), 0, 0);
            this.animator.rotate(head, toRadians(-30), 0, 0);
            this.animator.rotate(leftArm, toRadians(40), toRadians(30), 0);
            this.animator.rotate(rightArm, toRadians(40), toRadians(-30), 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(3);
            this.animator.resetKeyframe(4);
        }

    }

    @Override
    public void setupAnim(EntityTest entityTest, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entityTest, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.6F;
        float walkDegree = 0.6F;
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);
        this.flap(this.root, walkSpeed, walkDegree * 0.05F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.upper, walkSpeed * 0.3F, walkDegree * 0.3F, false, ageInTicks, 1);
        this.bob(this.leftArm, walkSpeed * 0.2F, walkDegree * 0.2F, false, ageInTicks, 1);
        this.bob(this.rightArm, walkSpeed * 0.2F, walkDegree * 0.2F, false, ageInTicks, 1);
        this.bob(this.head, walkSpeed * -0.2F, walkDegree * -0.2F, false, ageInTicks, 1);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    public static float toRadians(double degree) {
        return (float) degree * ((float) Math.PI / 180F);
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack stack) {
        this.getArm(arm).translateAndRotate(stack);
    }

    protected AdvancedModelBox getArm(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }
}