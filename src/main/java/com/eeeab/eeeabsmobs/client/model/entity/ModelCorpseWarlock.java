package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelCorpseWarlock extends ModelAbsCorpse<EntityCorpseWarlock> {
    private final AdvancedModelBox cube_r1;
    private final AdvancedModelBox cube_r2;
    private final AdvancedModelBox cube_r3;
    private final AdvancedModelBox cube_r4;
    private final AdvancedModelBox cube_r5;

    public ModelCorpseWarlock() {
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

        cube_r1 = new AdvancedModelBox(this, "cube_r1");
        offsetAndRotation(cube_r1, 0.0F, -7.599F, 4.0F, 0.48F, 0.0F, 0.0F);
        cube_r1.setTextureOffset(25, 0).addBox(-1.0F, -0.9F, -0.4F, 2.0F, 1.0F, 2.0F);
        head.addChild(cube_r1);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, 0.0F, 0.0F);
        body.setTextureOffset(16, 22).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                .setTextureOffset(56, 22).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, -0.2F);
        body.setTextureOffset(0, 40).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 18.0F, 4.0F, 0.5F);
        upper.addChild(body);

        rightArm = new AdvancedModelBox(this, "rightArm");
        rightArm.setPos(-5.0F, -10.0F, 0.0F);
        rightArm.setTextureOffset(40, 22).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        setRotationAngle(rightArm, 0, 0, toRadians(5));
        upper.addChild(rightArm);

        cube_r2 = new AdvancedModelBox(this, "cube_r2");
        offsetAndRotation(cube_r2, -4.5F, -0.5F, 0.0F, 0.0F, -1.5708F, 0.0F);
        cube_r2.setTextureOffset(34, -2).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 2.0F);
        rightArm.addChild(cube_r2);

        cube_r3 = new AdvancedModelBox(this, "cube_r3");
        offsetAndRotation(cube_r3, -1.5F, -0.5F, -3.0F, 3.1416F, 0.0F, 0.0F);
        cube_r3.setTextureOffset(34, -2).addBox(0.0F, -1.5F, -1.0F, 0.0F, 3.0F, 2.0F);
        rightArm.addChild(cube_r3);

        leftArm = new AdvancedModelBox(this, "leftArm");
        leftArm.setPos(5.0F, -10.0F, 0.0F);
        leftArm.setTextureOffset(40, 22).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, true);
        setRotationAngle(leftArm, 0, 0, toRadians(-5));
        upper.addChild(leftArm);

        cube_r4 = new AdvancedModelBox(this, "cube_r4");
        offsetAndRotation(cube_r4, 4.5F, -0.5F, 0.0F, 0.0F, 1.5708F, 0.0F);
        cube_r4.mirror = true;
        cube_r4.setTextureOffset(34, -2).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 2.0F);
        leftArm.addChild(cube_r4);

        cube_r5 = new AdvancedModelBox(this, "cube_r5");
        offsetAndRotation(cube_r5, 1.5F, -0.5F, -3.0F, 3.1416F, 0.0F, 0.0F);
        cube_r5.mirror = true;
        cube_r5.setTextureOffset(34, -2).addBox(0.0F, -1.5F, -1.0F, 0.0F, 3.0F, 2.0F);
        leftArm.addChild(cube_r5);

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
        return ImmutableList.of(root, upper, lower, head, rightArm, leftArm, body, rightLeg, leftLeg, cube_r1, cube_r2, cube_r3, cube_r4, cube_r5);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(root);
    }

    @Override
    protected void setupAnim(EntityCorpseWarlock entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        //LookAt
        faceTarget(netHeadYaw, headPitch, 1.0F, head);

        //Walk
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        flap(root, walkSpeed, walkDegree * 0.08F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        walk(leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        walk(rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            walk(rightArm, walkSpeed, walkDegree, true, -0.2F, -0.1F, limbSwing, limbSwingAmount);
            walk(leftArm, walkSpeed, walkDegree, false, -0.2F, 0.1F, limbSwing, limbSwingAmount);
            flap(rightArm, walkSpeed * 0.2F, walkDegree * 0.2F, true, 0.2F, -0.2F, limbSwing, limbSwingAmount);
            flap(leftArm, walkSpeed * 0.2F, walkDegree * 0.2F, true, 0.2F, 0.2F, limbSwing, limbSwingAmount);
        }

        //Idle
        float speed = 0.12F;
        float degree = 0.1F;
        if (entity.isAlive()) {
            walk(upper, 0.1F, 0.005F, true, 0, -0.005F, frame, 1);
            walk(rightArm, speed, degree, true, 0, 0, frame, 1);
            swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            walk(leftArm, speed, degree, false, 0, 0, frame, 1);
            swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
    }

    private void animate(EntityCorpseWarlock entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        if (entity.getAnimation() == EntityCorpseWarlock.SUMMON_ANIMATION || entity.getAnimation() == EntityCorpseWarlock.FRENZY_ANIMATION || entity.getAnimation() == EntityCorpseWarlock.BABBLE_ANIMATION) {
            float speed = 1.0F;
            if (entity.getAnimation() == EntityCorpseWarlock.BABBLE_ANIMATION) {
                head.rotateAngleX = toRadians(-10F);
                speed = 0.5F;
            }
            rightArm.rotationPointZ = 0.0F;
            rightArm.rotationPointX = -6.0F;
            leftArm.rotationPointZ = 0.0F;
            leftArm.rotationPointX = 6.0F;
            rightArm.rotateAngleX = Mth.cos(ageInTicks * 0.6666F * speed) * 0.5F;
            leftArm.rotateAngleX = Mth.cos(ageInTicks * 0.6666F * speed) * 0.5F;
            rightArm.rotateAngleZ = 2.3561945F;
            leftArm.rotateAngleZ = -2.3561945F;
            rightArm.rotateAngleY = 0.0F;
            leftArm.rotateAngleY = 0.0F;
        } else if (animator.setAnimation(EntityCorpseWarlock.TELEPORT_ANIMATION)) {
            animator.setStaticKeyframe(5);
            animator.startKeyframe(15);
            animator.rotate(root, 0, toRadians(-360), 0);
            animator.rotate(leftArm, 0, 0, toRadians(-90));
            animator.rotate(rightArm, 0, 0, toRadians(90));
            animator.endKeyframe();
            animator.resetKeyframe(10);
        } else if (animator.setAnimation(EntityCorpseWarlock.VAMPIRE_ANIMATION)) {
            animator.startKeyframe(10);//0.5
            animator.rotate(head, toRadians(22.5), 0, 0);
            animator.rotate(upper, toRadians(40), 0, 0);
            animator.rotate(rightArm, toRadians(-90), 0, toRadians(75));
            animator.move(rightArm, -0.5F, 0, 0);
            animator.rotate(leftArm, toRadians(-90), 0, toRadians(-75));
            animator.move(leftArm, 0.5F, 0, 0);
            animator.move(leftLeg, 0, -1F, -1.5F);
            animator.endKeyframe();

            animator.startKeyframe(10);//1
            animator.rotate(head, toRadians(-45), 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, 0, 0, toRadians(150));
            animator.move(rightArm, -1F, 0, 0);
            animator.rotate(leftArm, 0, 0, toRadians(-150));
            animator.move(leftArm, 1F, 0, 0);
            animator.move(leftLeg, 0, -2F, -3F);
            animator.endKeyframe();

            animator.setStaticKeyframe(50);//3.5

            if (entity.isHeal()) {
                animator.startKeyframe(5);//3.75
                animator.rotate(head, toRadians(22.5), 0, 0);
                animator.rotate(upper, toRadians(-5), 0, 0);
                animator.rotate(rightArm, toRadians(-90), 0, toRadians(75));
                animator.move(rightArm, -0.75F, 0, 0);
                animator.rotate(leftArm, toRadians(-90), 0, toRadians(-75));
                animator.move(leftArm, 0.75F, 0, 0);
                animator.move(leftLeg, 0, -1F, -1.5F);
                animator.endKeyframe();

                animator.startKeyframe(5);//4
                animator.rotate(head, toRadians(15), 0, 0);
                animator.rotate(upper, toRadians(-10), 0, 0);
                animator.rotate(rightArm, 0, 0, toRadians(75));
                animator.move(rightArm, -0.5F, 0, 0);
                animator.rotate(leftArm, 0, 0, toRadians(-75));
                animator.move(leftArm, 0.5F, 0, 0);
                animator.move(leftLeg, 0, 0, 0);
                animator.endKeyframe();
            } else {
                animator.startKeyframe(5);//3.75
                animator.rotate(head, toRadians(5), 0, 0);
                animator.rotate(upper, toRadians(-20), 0, 0);
                animator.rotate(rightArm, toRadians(-90), toRadians(80), toRadians(75));
                animator.move(rightArm, -1F, 0, 0);
                animator.rotate(leftArm, toRadians(-90), toRadians(-80), toRadians(-75));
                animator.move(leftArm, 1F, 0, 0);
                animator.rotate(leftLeg, toRadians(7.5), 0, 0);
                animator.move(leftLeg, 0, -1F, -1.5F);
                animator.endKeyframe();

                animator.startKeyframe(5);//4
                animator.rotate(head, toRadians(-20), 0, 0);
                animator.rotate(upper, toRadians(30), 0, 0);
                animator.rotate(rightArm, toRadians(-90), toRadians(-20), toRadians(75));
                animator.move(rightArm, 0, 0, 0);
                animator.rotate(leftArm, toRadians(-90), toRadians(20), toRadians(-75));
                animator.move(leftArm, 0, 0, 0);
                animator.move(leftLeg, 0, 0, 0);
                animator.rotate(leftLeg, 0, 0, 0);
                animator.endKeyframe();
            }

            animator.resetKeyframe(10);//4.5
        } else if (animator.setAnimation(EntityCorpseWarlock.TEARAPARTSPACE_ANIMATION) || animator.setAnimation(EntityCorpseWarlock.REST_POS_ANIMATION)) {
            animator.startKeyframe(15);//0.75
            animator.rotate(leftArm, toRadians(-90), toRadians(-65), toRadians(15));
            animator.rotate(rightArm, toRadians(-90), toRadians(65), toRadians(-15));
            animator.rotate(upper, toRadians(-5), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(3);//0.87
            animator.rotate(leftArm, toRadians(-122.5), toRadians(-22.5), toRadians(15));
            animator.rotate(rightArm, toRadians(-122.5), toRadians(22.5), toRadians(-15));
            animator.rotate(upper, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(2);//1
            animator.rotate(leftArm, toRadians(-50), toRadians(10), toRadians(15));
            animator.rotate(rightArm, toRadians(-50), toRadians(-10), toRadians(-15));
            animator.rotate(upper, toRadians(5), 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(5);
            animator.resetKeyframe(5);
        } else if (animator.setAnimation(EntityCorpseWarlock.ATTACK_ANIMATION)) {
            animator.startKeyframe(5);
            animator.rotate(head, toRadians(22.5), 0, 0);
            animator.rotate(upper, toRadians(10), 0, 0);
            animator.rotate(rightArm, toRadians(-90), 0, toRadians(75));
            animator.move(rightArm, -0.75F, 0, 0);
            animator.rotate(leftArm, toRadians(-90), 0, toRadians(-75));
            animator.move(leftArm, 0.75F, 0, 0);
            animator.move(leftLeg, 0, -1F, -1.5F);
            animator.endKeyframe();

            animator.startKeyframe(3);
            animator.rotate(head, toRadians(15), 0, 0);
            animator.rotate(upper, toRadians(-5), 0, 0);
            animator.rotate(rightArm, 0, 0, toRadians(75));
            animator.move(rightArm, -0.5F, 0, 0);
            animator.rotate(leftArm, 0, 0, toRadians(-75));
            animator.move(leftArm, 0.5F, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);

            animator.resetKeyframe(5);
        }
    }

    @Override
    protected double handsOffset() {
        return -180;
    }
}
