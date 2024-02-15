package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseSlavery;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;

public class ModelCorpseSlavery extends ModelAbsCorpse<EntityCorpseSlavery> {

    public ModelCorpseSlavery() {
        texHeight = 128;
        texWidth = 128;

        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, -12.0F, 0.0F);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, -12.0F, 0.0F);
        head.setTextureOffset(0, 0).addBox(-4.0F, -8.1F, -4.0F, 8.0F, 8.0F, 8.0F)
                .setTextureOffset(32, 0).addBox(-4.0F, -8.1F, -4.0F, 8.0F, 8.0F, 8.0F, -0.3F);
        upper.addChild(head);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, 0.0F, 0.0F);
        body.setTextureOffset(16, 22).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                .setTextureOffset(56, 22).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, -0.2F);
        body.setTextureOffset(0, 40).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 18.0F, 4.0F, 0.5F);
        upper.addChild(body);

        leftArm = new AdvancedModelBox(this, "leftArm");
        leftArm.setPos(5.0F, -10.0F, 0.0F);
        leftArm.setTextureOffset(40, 22).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, true);
        setRotationAngle(leftArm, 0, 0, toRadians(-5));
        upper.addChild(leftArm);

        rightArm = new AdvancedModelBox(this, "rightArm");
        rightArm.setPos(-5.0F, -10.0F, 0.0F);
        rightArm.setTextureOffset(40, 22).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        setRotationAngle(rightArm, 0, 0, toRadians(5));
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
    protected void setupAnim(EntityCorpseSlavery entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        //LookAt
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);

        //Walk
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        this.flap(this.root, walkSpeed, walkDegree * 0.08F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.walk(this.rightArm, walkSpeed, walkDegree, true, -0.2F, -0.1F, limbSwing, limbSwingAmount);
            this.walk(this.leftArm, walkSpeed, walkDegree, false, -0.2F, 0.1F, limbSwing, limbSwingAmount);
            this.flap(this.rightArm, walkSpeed * 0.2F, walkDegree * 0.2F, true, 0.2F, -0.2F, limbSwing, limbSwingAmount);
            this.flap(this.leftArm, walkSpeed * 0.2F, walkDegree * 0.2F, true, 0.2F, 0.2F, limbSwing, limbSwingAmount);
        }

        //Idle
        float speed = 0.12F;
        float degree = 0.1F;
        if (entity.isAlive()) {
            this.walk(this.upper, 0.1F, 0.005F, true, 0, -0.005F, frame, 1);
            this.walk(head, speed, degree, false, 0, 0, frame, 1);
            this.walk(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.walk(leftArm, speed, degree, false, 0, 0, frame, 1);
            this.swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
    }

    private void animate(EntityCorpseSlavery entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {

    }

    @Override
    protected Animation getSpawnAnimation() {
        return null;
    }
}
