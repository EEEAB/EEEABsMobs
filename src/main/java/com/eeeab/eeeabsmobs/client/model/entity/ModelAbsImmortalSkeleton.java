package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortalSkeleton;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalKnight;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.world.entity.HumanoidArm;

public class ModelAbsImmortalSkeleton extends EMCanSpawnEntityModel<EntityAbsImmortalSkeleton> implements ArmedModel {
    private final AdvancedModelBox outerHead;
    private final AdvancedModelBox armorHead;
    private final AdvancedModelBox lowerJaw;
    private final AdvancedModelBox cube_r1;
    private final AdvancedModelBox hemal;
    private final AdvancedModelBox hemal_r1;
    private final AdvancedModelBox heart;
    private final AdvancedModelBox outerBody;
    private final AdvancedModelBox armorBody;
    private final AdvancedModelBox armorLeftArm;
    private final AdvancedModelBox armorRightArm;
    private final AdvancedModelBox armorLeftLeg;
    private final AdvancedModelBox armorRightLeg;
    private final AdvancedModelBox armorLeftFoot;
    private final AdvancedModelBox armorRightFoot;

    public ModelAbsImmortalSkeleton() {
        texHeight = 128;
        texWidth = 128;

        root = new AdvancedModelBox(this, "root");
        root.setPos(-1.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(1.0F, -12.0F, 0.0F);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(-1.0F, -12.0F, 0.0F);
        upper.addChild(head);
        head.setTextureOffset(0, 0).addBox(-3.5F, -8.0F, -3.75F, 8.0F, 8.0F, 8.0F)
                .setTextureOffset(96, 1).addBox(-3.5F, -8.0F, -3.75F, 8.0F, 8.0F, 8.0F, -1.0F);

        outerHead = new AdvancedModelBox(this, "outerHead");
        outerHead.setPos(0.5F, 0.0F, 0.0F);
        head.addChild(outerHead);
        outerHead.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -3.75F, 8.0F, 8.0F, 8.0F);

        armorHead = new AdvancedModelBox(this, "armorHead");
        armorHead.setPos(0.5F, 0.0F, 0.0F);
        head.addChild(armorHead);
        armorHead.setTextureOffset(37, 18).addBox(-4.0F, -8.0F, -3.75F, 8.0F, 8.0F, 8.0F, 0.4F);

        lowerJaw = new AdvancedModelBox(this, "lowerJaw");
        lowerJaw.setPos(1.0F, -0.887F, -2.5617F);
        head.addChild(lowerJaw);

        cube_r1 = new AdvancedModelBox(this, "cube_r1");
        offsetAndRotation(cube_r1, 0.0F, -0.113F, 0.4617F, 0.3491F, 0.0F, 0.0F);
        lowerJaw.addChild(cube_r1);
        cube_r1.setTextureOffset(25, 18).addBox(-3.5F, 0.0F, -1.9F, 6.0F, 1.0F, 3.0F);

        body = new AdvancedModelBox(this, "body");
        body.setPos(-0.5F, -12.75F, 0.0F);
        upper.addChild(body);
        body.setTextureOffset(0, 18).addBox(-4.0F, 0.75F, -1.75F, 8.0F, 12.0F, 4.0F);

        hemal = new AdvancedModelBox(this, "hemal");
        hemal.setPos(0.0F, 0.75F, 0.0F);
        body.addChild(hemal);
        hemal.setTextureOffset(90, 36).addBox(-4.8F, -0.4F, -1.35F, 9.6F, 2.8F, 3.2F, -1.0F);

        hemal_r1 = new AdvancedModelBox(this, "hemal_r1");
        offsetAndRotation(hemal_r1, 0.0F, 3.0F, 0.25F, 0.0F, 0.0F, 1.5708F);
        hemal.addChild(hemal_r1);
        hemal_r1.setTextureOffset(90, 36).addBox(-5.8F, -1.4F, -1.6F, 15.6F, 2.8F, 3.2F, -1.0F);

        heart = new AdvancedModelBox(this, "heart");
        heart.setPos(-0.5F, 3.75F, 0.0F);
        body.addChild(heart);
        heart.setTextureOffset(104, 20).addBox(-2.5F, -3.8F, -1.75F, 6.0F, 7.6F, 4.0F, -1.0F);

        outerBody = new AdvancedModelBox(this, "outerBody");
        outerBody.setPos(0.0F, 0.0F, 0.0F);
        body.addChild(outerBody);
        outerBody.setTextureOffset(0, 18).addBox(-4.0F, 0.75F, -1.75F, 8.0F, 12.0F, 4.0F, 0.0F);

        armorBody = new AdvancedModelBox(this, "armorBody");
        armorBody.setPos(0.0F, 0.0F, 0.0F);
        body.addChild(armorBody);
        armorBody.setTextureOffset(72, 18).addBox(-4.0F, 0.75F, -1.75F, 8.0F, 12.0F, 4.0F, 0.75F);

        leftArm = new AdvancedModelBox(this, "leftArm");
        offsetAndRotation(leftArm, 5.1F, -10.5F, 0.0F, 0.0F, 0.0F, -0.0873F);
        upper.addChild(leftArm);
        leftArm.setTextureOffset(34, 1).addBox(-1.1F, -2.0F, -1.0F, 2.0F, 12.5F, 2.0F, 0.4F, true);
        setRotationAngle(leftArm, toRadians(-5), 0, toRadians(-10));

        armorLeftArm = new AdvancedModelBox(this, "armorLeftArm");
        armorLeftArm.setPos(0.0F, 0.0F, 0.0F);
        leftArm.addChild(armorLeftArm);
        armorLeftArm.setTextureOffset(60, 1).addBox(-1.1F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.75F, true);

        rightArm = new AdvancedModelBox(this, "rightArm");
        offsetAndRotation(rightArm, -6.0F, -10.5F, 0.0F, 0.0F, 0.0F, 0.0873F);
        upper.addChild(rightArm);
        rightArm.setTextureOffset(34, 1).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.5F, 2.0F, 0.4F);
        setRotationAngle(rightArm, toRadians(-5), 0, toRadians(10));

        armorRightArm = new AdvancedModelBox(this, "armorRightArm");
        armorRightArm.setPos(0.0F, 0.0F, 0.0F);
        rightArm.addChild(armorRightArm);
        armorRightArm.setTextureOffset(60, 1).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.75F);

        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(0.0F, 0.0F, 0.0F);
        root.addChild(lower);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        leftLeg.setPos(3.2F, -12.0F, 0.0F);
        lower.addChild(leftLeg);
        leftLeg.mirror = true;
        leftLeg.setTextureOffset(34, 1).addBox(-1.2F, -0.5F, -1.0F, 2.0F, 12.5F, 2.0F, 0.4F);

        armorLeftLeg = new AdvancedModelBox(this, "armorLeftLeg");
        armorLeftLeg.setPos(-0.2F, 0.0F, 0.0F);
        leftLeg.addChild(armorLeftLeg);
        armorLeftLeg.mirror = true;
        armorLeftLeg.setTextureOffset(78, 1).addBox(-2.0F, 0.0F, -1.8F, 4.0F, 12.0F, 4.0F, 0.5F);

        armorLeftFoot = new AdvancedModelBox(this, "armorLeftFoot");
        armorLeftFoot.setPos(-0.2F, 0.0F, 0.0F);
        leftLeg.addChild(armorLeftFoot);
        armorLeftFoot.mirror = true;
        armorLeftFoot.setTextureOffset(43, 1).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.75F);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        rightLeg.setPos(-2.0F, -12.0F, 0.0F);
        lower.addChild(rightLeg);
        rightLeg.setTextureOffset(34, 1).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 12.5F, 2.0F, 0.4F);

        armorRightLeg = new AdvancedModelBox(this, "armorRightLeg");
        armorRightLeg.setPos(0.0F, 0.0F, 0.0F);
        rightLeg.addChild(armorRightLeg);
        armorRightLeg.setTextureOffset(78, 1).addBox(-2.0F, 0.0F, -1.8F, 4.0F, 12.0F, 4.0F, 0.5F);

        armorRightFoot = new AdvancedModelBox(this, "armorRightFoot");
        armorRightFoot.setPos(0.0F, 0.0F, 0.0F);
        rightLeg.addChild(armorRightFoot);
        armorRightFoot.setTextureOffset(43, 1).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.75F);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    protected Animation getSpawnAnimation() {
        return EntityAbsImmortalSkeleton.SPAWN_ANIMATION;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.upper, this.lower, this.head, this.body, this.leftArm, this.rightArm,
                this.leftLeg, this.rightLeg, this.outerHead, this.armorHead, this.lowerJaw, this.cube_r1, this.hemal, this.hemal_r1,
                this.heart, this.outerBody, this.armorBody, this.armorLeftArm, this.armorRightArm,
                this.armorLeftLeg, this.armorRightLeg, this.armorLeftFoot, this.armorRightFoot);
    }

    @Override
    protected void setupAnim(EntityAbsImmortalSkeleton entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        //LookAt
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);

        //Walk
        float walkSpeed = 0.8F;
        float walkDegree = 0.8F;
        if (entity instanceof EntityImmortalKnight) walkSpeed = walkDegree = 0.6F;
        this.flap(this.root, walkSpeed, walkDegree * 0.05F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.head, walkSpeed, 0.08F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.bob(this.head, walkSpeed, walkDegree * 0.6F, false, limbSwing, limbSwingAmount);
        this.bob(this.root, walkSpeed, walkDegree * 0.6F, true, limbSwing, limbSwingAmount);
        this.bob(this.heart, walkSpeed, walkDegree * 1.2F, true, limbSwing, limbSwingAmount);
        if (entity.getAnimation() == IAnimatedEntity.NO_ANIMATION || entity.getAnimation() == EntityAbsImmortalSkeleton.HURT_ANIMATION) {
            this.walk(this.leftArm, walkSpeed, walkDegree * 1.2F, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
            this.walk(this.rightArm, walkSpeed, walkDegree * 1.2F, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        }

        //Idle
        if (entity.isAlive()) {
            this.walk(this.upper, 0.1F, 0.005F, true, 0, -0.005F, frame, 1);
            this.walk(this.leftArm, 0.15F, 0.05F, true, 0, 0, frame, 1);
            this.walk(this.rightArm, 0.15F, 0.05F, false, 0, 0, frame, 1);
            this.bob(this.upper, walkSpeed * 0.2F, walkDegree * 0.2F, false, frame, 1);
            this.bob(this.leftArm, walkSpeed * 0.2F, walkDegree * 0.2F, false, frame, 1);
            this.bob(this.rightArm, walkSpeed * 0.2F, walkDegree * 0.2F, false, frame, 1);
            this.bob(this.head, walkSpeed * -0.2F, walkDegree * -0.2F, false, frame, 1);
        }
    }

    private void animate(EntityAbsImmortalSkeleton entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        if (animator.setAnimation(EntityAbsImmortalSkeleton.HURT_ANIMATION)) {
            animator.startKeyframe(2);
            animator.rotate(lowerJaw, toRadians(24), 0, 0);
            animator.rotate(upper, toRadians(4), toRadians(5), toRadians(1));
            animator.move(upper, 0, -0.5F, 0);
            animator.move(rightArm, 0, -0.75F, 0);
            animator.move(leftArm, 0, -0.75F, 0);
            animator.move(head, 0, -1.5F, 0);
            animator.rotate(head, toRadians(-1.33), 0, toRadians(0.33));
            animator.move(heart, 0, -0.23F, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(2);
            animator.resetKeyframe(2);
            animator.setStaticKeyframe(2);
        } else if (animator.setAnimation(EntityAbsImmortalSkeleton.DIE_ANIMATION)) {
            animator.startKeyframe(2);//0.16
            animator.rotate(rightArm, 0, 0, toRadians(10));
            animator.rotate(leftArm, 0, 0, toRadians(-10));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(root, toRadians(10), 0, 0);
            animator.rotate(head, toRadians(7.27), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(8);//0.64
            animator.rotate(rightArm, toRadians(-26.67), 0, 0);
            animator.rotate(leftArm, toRadians(-26.67), 0, 0);
            animator.rotate(lowerJaw, toRadians(30), 0, 0);
            animator.rotate(root, toRadians(11.48), 0, 0);
            animator.move(root, 0F, -0.54F, 0F);
            animator.rotate(head, toRadians(1.82), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(8);//1.08
            animator.rotate(rightArm, toRadians(-45), 0, 0);
            animator.rotate(leftArm, toRadians(-45), 0, 0);
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(root, toRadians(-87.5), 0, 0);
            animator.move(root, 0F, -2.0F, 0F);
            animator.rotate(head, toRadians(-15), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);//1.2
            animator.rotate(rightArm, toRadians(2.7768), toRadians(66.6067), toRadians(44.4631));
            animator.rotate(leftArm, toRadians(-1.3314), toRadians(-34.124), toRadians(-42.6612));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(root, toRadians(-87.5), 0, 0);
            animator.move(root, 0F, -3.0F, 0F);
            animator.rotate(head, toRadians(-18.45), toRadians(-25.85), toRadians(8.71));
            animator.rotate(rightLeg, 0, toRadians(7.5), toRadians(5));
            animator.rotate(leftLeg, 0, toRadians(-7.5), toRadians(-5));
            animator.endKeyframe();

            animator.startKeyframe(4);//1.32
            animator.rotate(rightArm, toRadians(2.7768), toRadians(66.6067), toRadians(44.4631));
            animator.rotate(leftArm, toRadians(-1.3314), toRadians(-34.124), toRadians(-42.6612));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(root, toRadians(-87.5), 0, 0);
            animator.move(root, 0F, -2.0F, 0F);
            animator.rotate(head, toRadians(-20.7536), toRadians(-43.0795), toRadians(14.5108));
            animator.rotate(rightLeg, 0, toRadians(7.5), toRadians(5));
            animator.rotate(leftLeg, 0, toRadians(-7.5), toRadians(-5));
            animator.endKeyframe();

            animator.setStaticKeyframe(34);
        } else if (animator.setAnimation(EntityAbsImmortalSkeleton.MELEE_ATTACK_1_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, 0, 0, 0);
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(root, toRadians(-9.5), 0, 0);
            animator.rotate(upper, toRadians(-5), toRadians(1), 0);
            animator.rotate(head, 0, toRadians(-5), 0);
            animator.rotate(rightArm, toRadians(-135.19), toRadians(20), toRadians(-10));
            animator.move(rightArm, 0, -1F, 0);
            animator.rotate(leftArm, toRadians(60), toRadians(20), toRadians(-20));
            animator.rotate(lowerJaw, toRadians(60), 0, 0);
            animator.rotate(rightLeg, toRadians(25), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(root, toRadians(4.5), 0, 0);
            animator.rotate(upper, toRadians(9.95), toRadians(-9.94), 0);
            animator.rotate(head, 0, toRadians(10), 0);
            animator.rotate(rightArm, toRadians(-38.22), toRadians(-28.51), toRadians(5.38));
            animator.move(rightArm, 0, 0.5F, 0);
            animator.rotate(leftArm, toRadians(-15), 0, toRadians(-7.5));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.endKeyframe();

            animator.resetKeyframe(6);

        } else if (animator.setAnimation(EntityAbsImmortalSkeleton.MELEE_ATTACK_2_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, 0, 0, 0);
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(root, toRadians(-6.5), 0, 0);
            animator.rotate(upper, toRadians(-5), toRadians(-40), 0);
            animator.rotate(head, toRadians(-2.5288), toRadians(9.9857), toRadians(-0.6568));
            animator.rotate(rightArm, toRadians(-106.6988), toRadians(-14.109), toRadians(61.691));
            animator.move(rightArm, 0, -1F, 0);
            animator.rotate(leftArm, toRadians(54.0444), toRadians(36.8505), toRadians(-32.3398));
            animator.rotate(lowerJaw, toRadians(60), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(root, toRadians(6.5), 0, 0);
            animator.rotate(upper, toRadians(10), toRadians(20), 0);
            animator.rotate(head, toRadians(10.1144), toRadians(9.9233), toRadians(0.8803));
            animator.rotate(rightArm, toRadians(4.0129), toRadians(-8.9263), toRadians(70.2446));
            animator.move(rightArm, 0, 1F, 0);
            animator.rotate(leftArm, toRadians(53.1253), toRadians(26.0762), toRadians(-24.7812));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-2), toRadians(5), 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-32.2414), toRadians(-6.2584), toRadians(54.4935));
            animator.move(rightArm, 0, 0.29F, 0);
            animator.rotate(leftArm, toRadians(-6.64), toRadians(-0.83), toRadians(-6.73));
            animator.endKeyframe();

            animator.resetKeyframe(4);
        } else if (animator.setAnimation(EntityAbsImmortalSkeleton.MELEE_ATTACK_3_ANIMATION)) {
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
        }
        if (animator.setAnimation(EntityAbsImmortalSkeleton.RANGED_ATTACK_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, 0, 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.32
            animator.rotate(root, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-90), toRadians(-10), toRadians(20));
            animator.rotate(leftArm, toRadians(-90), toRadians(30), 0);
            animator.move(leftArm, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(11);//0.88
            animator.rotate(root, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-90), toRadians(-10), toRadians(20));
            animator.rotate(leftArm, toRadians(-90), toRadians(30), 0);
            animator.move(leftArm, 0, 0, 2);
            animator.endKeyframe();

            animator.startKeyframe(5);//1.16
            animator.rotate(root, toRadians(2.5), 0, 0);
            animator.rotate(head, toRadians(-2.24), 0, 0);
            animator.rotate(rightArm, toRadians(-89.5), toRadians(-10), toRadians(20));
            animator.rotate(leftArm, toRadians(-115), toRadians(-10), 0);
            animator.move(leftArm, 0, 0, 2);
            animator.endKeyframe();

            animator.resetKeyframe(8);
        } else if (animator.setAnimation(EntityAbsImmortalSkeleton.BLOCK_ANIMATION)) {
            animator.startKeyframe(2);
            animator.rotate(head, toRadians(-7.5), toRadians(-12.5), 0);
            animator.rotate(upper, toRadians(5), toRadians(15), 0);
            animator.rotate(leftArm, toRadians(-60), toRadians(60), toRadians(20));
            animator.move(leftArm, 0, 0, -3F);
            animator.rotate(rightArm, toRadians(7.5), 0, toRadians(10));
            animator.endKeyframe();
            animator.setStaticKeyframe(6);


            animator.resetKeyframe(2);
        } else if (animator.setAnimation(EntityImmortalKnight.ROAR_ANIMATION)) {
            animator.startKeyframe(0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, 0, 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(8);//0.44
            animator.move(root, 0, 0, -1F);
            animator.rotate(upper, toRadians(-20), toRadians(5), toRadians(-2.5));
            animator.rotate(head, toRadians(15), toRadians(-5), toRadians(-2.5));
            animator.rotate(rightArm, toRadians(-90.5), toRadians(-10), toRadians(-20));
            animator.rotate(leftArm, toRadians(-90.5), toRadians(10), toRadians(20));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(rightLeg, toRadians(-50), 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(leftLeg, toRadians(20), 0, 0);
            animator.move(leftLeg, 0, 0, 2F);
            animator.endKeyframe();

            animator.startKeyframe(3);//0.6
            animator.move(root, 0, 0, -3F);
            animator.rotate(upper, toRadians(45), toRadians(-2.5), toRadians(-2.5));
            animator.rotate(head, toRadians(-40.5), 0, 0);
            animator.rotate(rightArm, toRadians(30.5), 0, toRadians(20));
            animator.rotate(leftArm, toRadians(30.5), 0, toRadians(-20));
            animator.rotate(lowerJaw, toRadians(40), 0, 0);
            animator.rotate(rightLeg, toRadians(-10), 0, 0);
            animator.move(rightLeg, 0, 0, -2F);
            animator.rotate(leftLeg, toRadians(10), 0, 0);
            animator.move(leftLeg, 0, 0, 1F);
            animator.endKeyframe();

            animator.setStaticKeyframe(16);//1.4

            animator.startKeyframe(3);//1.56
            animator.move(root, 0, 0, -3F);
            animator.rotate(upper, toRadians(27.63), toRadians(-6.13), toRadians(-2.5));
            animator.rotate(head, toRadians(-28.12), 0, 0);
            animator.rotate(rightArm, toRadians(30.5), 0, toRadians(10));
            animator.rotate(leftArm, toRadians(30.5), 0, toRadians(-10));
            animator.rotate(lowerJaw, toRadians(30), 0, 0);
            animator.rotate(rightLeg, toRadians(-10), 0, 0);
            animator.move(rightLeg, 0, 0, -2F);
            animator.rotate(leftLeg, toRadians(10), 0, 0);
            animator.move(leftLeg, 0, 0, 1F);
            animator.endKeyframe();

            animator.startKeyframe(4);//1.76
            animator.move(root, 0, 0, -1.33F);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-28.12), 0, 0);
            animator.rotate(rightArm, 0, 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(lowerJaw, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(-4.44), 0, 0);
            animator.move(rightLeg, 0, 0, -0.89F);
            animator.rotate(leftLeg, toRadians(4.44), 0, 0);
            animator.move(leftLeg, 0, 0, 0.44F);
            animator.endKeyframe();

            animator.resetKeyframe(3);
            animator.setStaticKeyframe(8);
        }

    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        boolean isRightArm = humanoidArm == HumanoidArm.RIGHT;
        AdvancedModelBox model$part = isRightArm ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(poseStack);
        this.upper.translateAndRotate(poseStack);
        model$part.translateAndRotate(poseStack);
        offsetStackPosition(poseStack, isRightArm);
        poseStack.scale(1F, 1F, 1F);
    }

    private void offsetStackPosition(PoseStack poseStack, boolean isRightArm) {
        if (isRightArm) {
            poseStack.translate(0.0625, 0, 0);
        } else {
            poseStack.translate(-0.0625, 0, 0);
        }
    }
}
