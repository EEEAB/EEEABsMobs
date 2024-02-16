package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalShaman;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.world.entity.HumanoidArm;

public class ModelImmortalShaman extends EMCanSpawnEntityModel<EntityImmortalShaman> implements ArmedModel {
    private final AdvancedModelBox capeHead;
    private final AdvancedModelBox armHead;
    private final AdvancedModelBox kok;
    private final AdvancedModelBox bone_r1;
    private final AdvancedModelBox bone_r2;
    private final AdvancedModelBox bone_r3;
    private final AdvancedModelBox mouthKok;
    private final AdvancedModelBox bone_r4;
    private final AdvancedModelBox bone_r5;
    private final AdvancedModelBox lowerJaw;
    private final AdvancedModelBox cube_r1;
    private final AdvancedModelBox hemal;
    private final AdvancedModelBox cape;
    private final AdvancedModelBox bone_r6;
    private final AdvancedModelBox armBody;
    private final AdvancedModelBox armLeftArm;
    private final AdvancedModelBox armRightArm;
    private final AdvancedModelBox armleftLeg;
    private final AdvancedModelBox armRightLeg;

    public final AdvancedModelBox heart;

    public ModelImmortalShaman() {
        texHeight = 128;
        texWidth = 128;

        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, -11.0F, 0.0F);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, -8.5F, 0.0F);
        upper.addChild(head);
        head.setTextureOffset(96, 94).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, -1.5F);

        capeHead = new AdvancedModelBox(this, "capeHead");
        capeHead.setPos(0.0F, -1.5F, 0.0F);
        head.addChild(capeHead);
        capeHead.setTextureOffset(43, 95).addBox(-4.0F, -5.5F, -4.0F, 8.0F, 8.0F, 8.0F, -0.5F);

        armHead = new AdvancedModelBox(this, "armHead");
        armHead.setPos(0.0F, -1.5F, 0.0F);
        head.addChild(armHead);
        armHead.setTextureOffset(0, 112).addBox(-4.0F, -5.5F, -4.0F, 8.0F, 8.0F, 8.0F, -0.8F);

        kok = new AdvancedModelBox(this, "kok");
        kok.setPos(0.0373F, -7.522F, -4.2179F);
        armHead.addChild(kok);

        bone_r1 = new AdvancedModelBox(this, "bone_r1");
        offsetAndRotation(bone_r1, -4.2614F, 1.9855F, 0.7179F, 0.2618F, 0.0F, -0.2618F);
        kok.addChild(bone_r1);
        bone_r1.mirror = true;
        bone_r1.setTextureOffset(18, 99).addBox(-0.1759F, -1.4319F, -1.0F, 2.0F, 4.0F, 2.0F, -0.3F);

        bone_r2 = new AdvancedModelBox(this, "bone_r2");
        offsetAndRotation(bone_r2, 1.9627F, 1.522F, -0.6821F, 0.0801F, -0.1796F, -0.1381F);
        kok.addChild(bone_r2);
        bone_r2.setTextureOffset(0, 92).addBox(1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 2.0F, -0.3F);

        bone_r3 = new AdvancedModelBox(this, "bone_r3");
        offsetAndRotation(bone_r3, 3.7869F, 1.9855F, 0.7179F, 0.2618F, 0.0F, 0.2618F);
        kok.addChild(bone_r3);
        bone_r3.setTextureOffset(18, 99).addBox(-1.4241F, -1.4319F, -1.0F, 2.0F, 4.0F, 2.0F, -0.3F);

        mouthKok = new AdvancedModelBox(this, "mouthKok");
        offsetAndRotation(mouthKok, 0.0708F, 1.5761F, -2.5983F, 0.5236F, 0.0F, 0.0F);
        armHead.addChild(mouthKok);

        bone_r4 = new AdvancedModelBox(this, "bone_r4");
        offsetAndRotation(bone_r4, 3.6357F, -2.9162F, 1.2F, 0.0F, 0.0F, 0.1745F);
        mouthKok.addChild(bone_r4);
        bone_r4.setTextureOffset(9, 99).addBox(-1.1F, -0.2599F, -2.0017F, 2.0F, 4.0F, 2.0F, -0.5F);

        bone_r5 = new AdvancedModelBox(this, "bone_r5");
        offsetAndRotation(bone_r5, -4.7773F, -4.9162F, 1.2F, 0.0F, 0.0F, -0.1745F);
        mouthKok.addChild(bone_r5);
        bone_r5.setTextureOffset(0, 99).addBox(-0.2935F, 1.7401F, -2.0017F, 2.0F, 4.0F, 2.0F, -0.5F);

        lowerJaw = new AdvancedModelBox(this, "lowerJaw");
        lowerJaw.setPos(0.1F, -0.3148F, -1.4832F);
        head.addChild(lowerJaw);

        cube_r1 = new AdvancedModelBox(this, "cube_r1");
        offsetAndRotation(cube_r1, -0.6F, -6.1852F, -0.1168F, 0.3491F, 0.0F, 0.0F);
        lowerJaw.addChild(cube_r1);
        cube_r1.setTextureOffset(0, 107).addBox(-2.5F, 5.0F, -3.9F, 6.0F, 1.0F, 3.0F, -0.2F);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, 0.0F, 0.0F);
        upper.addChild(body);
        body.setTextureOffset(106, 77).addBox(-3.5F, -9.0F, -2.0F, 7.0F, 10.0F, 4.0F, 0.3F);

        hemal = new AdvancedModelBox(this, "hemal");
        hemal.setPos(0.0F, 11.0F, 0.0F);
        body.addChild(hemal);
        hemal.setTextureOffset(67, 78).addBox(-1.0F, -20.0F, -1.0F, 1.0F, 11.0F, 1.0F)
                .setTextureOffset(86, 88).addBox(-4.1F, -19.0F, -1.0F, 7.5F, 1.0F, 1.0F, -0.1F);

        heart = new AdvancedModelBox(this, "heart");
        heart.setPos(1.0F, -7.0F, -0.5F);
        body.addChild(heart);
        heart.setTextureOffset(75, 85).addBox(-1.2F, -0.5F, -0.8F, 2.4F, 3.5F, 1.6F, 0.0F);

        cape = new AdvancedModelBox(this, "cape");
        cape.setPos(0.0F, -9.5988F, 2.6548F);
        body.addChild(cape);

        bone_r6 = new AdvancedModelBox(this, "bone_r6");
        offsetAndRotation(bone_r6, 1.0F, 0.219F, -3.1119F, 0.0436F, 0.0F, 0.0F);
        cape.addChild(bone_r6);
        bone_r6.setTextureOffset(60, 112).addBox(-6.3F, 0.2F, 2.75F, 10.0F, 15.0F, 0.0F);

        armBody = new AdvancedModelBox(this, "armBody");
        armBody.setPos(0.0F, 10.0F, 0.0F);
        body.addChild(armBody);
        armBody.setTextureOffset(35, 114).addBox(-4.0F, -19.0F, -2.0F, 8.0F, 10.0F, 4.0F, -0.1F);

        leftArm = new AdvancedModelBox(this, "leftArm");
        offsetAndRotation(leftArm, 4.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.0349F);
        upper.addChild(leftArm);
        leftArm.mirror = true;
        leftArm.setTextureOffset(96, 115).addBox(-0.4811F, -0.4319F, -1.5F, 3.0F, 10.0F, 3.0F, -0.2F);
        setRotationAngle(leftArm, 0, 0, toRadians(-5));

        armLeftArm = new AdvancedModelBox(this, "armLeftArm");
        armLeftArm.setPos(0.0F, 0.0F, 0.0F);
        leftArm.addChild(armLeftArm);
        armLeftArm.mirror = true;
        armLeftArm.setTextureOffset(80, 113).addBox(-0.7311F, -1.4319F, -1.5F, 3.5F, 11.0F, 3.5F, 0.0F);

        rightArm = new AdvancedModelBox(this, "rightArm");
        offsetAndRotation(rightArm, -4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0349F);
        upper.addChild(rightArm);
        rightArm.setTextureOffset(96, 115).addBox(-2.5189F, -0.4319F, -1.5F, 3.0F, 10.0F, 3.0F, -0.2F);
        setRotationAngle(rightArm, 0, 0, toRadians(5));

        armRightArm = new AdvancedModelBox(this, "armRightArm");
        armRightArm.setPos(0.0F, 0.0F, 0.0F);
        rightArm.addChild(armRightArm);
        armRightArm.setTextureOffset(80, 113).addBox(-3.2689F, -1.4319F, -1.5F, 3.5F, 11.0F, 3.5F, 0.0F);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        leftLeg.setPos(1.9F, -10.0F, 0.0F);
        root.addChild(leftLeg);
        leftLeg.mirror = true;
        leftLeg.setTextureOffset(113, 114).addBox(-1.9F, -1.0F, -1.5F, 3.5F, 11.0F, 3.0F, -0.2F);

        armleftLeg = new AdvancedModelBox(this, "armleftLeg");
        armleftLeg.setPos(1.9F, -10.0F, 0.0F);
        root.addChild(armleftLeg);
        armleftLeg.mirror = true;
        armleftLeg.setTextureOffset(78, 96).addBox(-1.9F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        rightLeg.setPos(-1.9F, -10.0F, 0.0F);
        root.addChild(rightLeg);
        rightLeg.setTextureOffset(113, 114).addBox(-1.6F, -1.0F, -1.5F, 3.5F, 11.0F, 3.0F, -0.2F);


        armRightLeg = new AdvancedModelBox(this, "armRightLeg");
        armRightLeg.setPos(-1.9F, -10.0F, 0.0F);
        root.addChild(armRightLeg);
        armRightLeg.setTextureOffset(78, 96).addBox(-2.1F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F);


        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.upper, this.head, this.body, this.leftArm, this.rightArm, this.rightLeg, this.leftLeg, this.capeHead, this.armHead, this.kok,
                this.bone_r1, this.bone_r2, this.bone_r3, this.mouthKok, this.bone_r4, this.bone_r5, this.lowerJaw, this.cube_r1, this.hemal, this.heart, this.cape,
                this.bone_r6, this.armBody, this.armLeftArm, this.armRightArm, this.armRightLeg, this.armleftLeg);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    private void animate(EntityImmortalShaman entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        if (animator.setAnimation(EntityImmortalShaman.SPELL_CASTING_SUMMON_ANIMATION)) {
            //generalSpellAnimation(26);
            animator.startKeyframe(8);//0.4
            animator.rotate(rightArm, toRadians(-90), toRadians(24.9746), toRadians(1.1652));
            animator.rotate(leftArm, toRadians(-90), toRadians(-24.9746), toRadians(-1.1652));
            animator.rotate(head, toRadians(-3.5), 0, 0);
            animator.rotate(armRightLeg, toRadians(-5), 0, 0);
            animator.rotate(armleftLeg, toRadians(-5), 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(26);
            animator.resetKeyframe(10);
        } else if (animator.setAnimation(EntityImmortalShaman.SPELL_CASTING_HEAL_ANIMATION)) {
            //generalSpellAnimation(42);
            animator.startKeyframe(8);
            animator.rotate(rightArm, toRadians(-90), toRadians(24.9746), toRadians(1.1652));
            animator.rotate(leftArm, toRadians(-90), toRadians(-24.9746), toRadians(-1.1652));
            animator.rotate(head, toRadians(-3.5), 0, 0);
            animator.rotate(armRightLeg, toRadians(-5), 0, 0);
            animator.rotate(armleftLeg, toRadians(-5), 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(42);
            animator.resetKeyframe(10);
        } else if (animator.setAnimation(EntityImmortalShaman.SPELL_CASTING_WOLOLO_ANIMATION)) {
            animator.startKeyframe(8);
            animator.rotate(rightArm, toRadians(-90), toRadians(24.9746), toRadians(1.1652));
            animator.rotate(leftArm, toRadians(-90), toRadians(-24.9746), toRadians(-1.1652));
            animator.rotate(head, toRadians(-3.5), 0, 0);
            animator.rotate(armRightLeg, toRadians(-5), 0, 0);
            animator.rotate(armleftLeg, toRadians(-5), 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(22);
            animator.resetKeyframe(10);
        } else if (animator.setAnimation(EntityImmortalShaman.SPELL_CASTING_FR_ATTACK_ANIMATION)) {
            animator.startKeyframe(7);
            animator.rotate(rightArm, toRadians(-72.5), toRadians(-20), 0);
            animator.rotate(leftArm, toRadians(-72.5), toRadians(20), 0);
            animator.rotate(head, toRadians(22.5), 0, 0);
            animator.rotate(upper, toRadians(52.5), 0, 0);
            animator.rotate(root, toRadians(7.5), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(7);
            animator.rotate(rightArm, toRadians(-90), toRadians(80), toRadians(30));
            animator.rotate(leftArm, toRadians(-90), toRadians(-80), toRadians(-30));
            animator.rotate(head, toRadians(-22.5), 0, 0);
            animator.rotate(upper, toRadians(-20.5), 0, 0);
            animator.rotate(root, toRadians(-2.5), 0, 0);
            animator.rotate(cape, toRadians(40), 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(5);
            animator.resetKeyframe(6);
            animator.setStaticKeyframe(5);
        } else if (animator.setAnimation(EntityImmortalShaman.SPELL_CASTING_BOMB_ANIMATION)) {
            animator.startKeyframe(5);//0.24
            animator.rotate(rightArm, toRadians(-110), toRadians(-30), 0);
            animator.rotate(leftArm, toRadians(-110), toRadians(30), 0);
            animator.rotate(head, toRadians(-10.5), 0, 0);
            animator.rotate(upper, toRadians(-10), 0, 0);
            animator.rotate(root, toRadians(-5), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.44
            animator.rotate(rightArm, toRadians(-115), toRadians(-30), 0);
            animator.rotate(leftArm, toRadians(-115), toRadians(30), 0);
            animator.rotate(head, toRadians(-10.5), 0, 0);
            animator.rotate(upper, toRadians(-10), 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(cape, toRadians(-2.5), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.56
            animator.rotate(rightArm, toRadians(-27.5), toRadians(-25), 0);
            animator.rotate(leftArm, toRadians(-27.5), toRadians(25), 0);
            animator.rotate(head, toRadians(-17.1), toRadians(-0.65), 0);
            animator.rotate(upper, toRadians(10), 0, 0);
            animator.rotate(root, toRadians(2.5), 0, 0);
            animator.rotate(cape, toRadians(-2.5), 0, 0);
            animator.rotate(mouthKok, toRadians(60), 0, 0);
            animator.rotate(lowerJaw, toRadians(15), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.68
            animator.rotate(rightArm, toRadians(60), toRadians(-20), 0);
            animator.rotate(leftArm, toRadians(60), toRadians(20), 0);
            animator.rotate(head, toRadians(-25), 0, 0);
            animator.rotate(upper, toRadians(30), 0, 0);
            animator.rotate(root, toRadians(5), 0, 0);
            animator.rotate(cape, toRadians(28.75), 0, 0);
            animator.rotate(mouthKok, toRadians(120), 0, 0);
            animator.rotate(lowerJaw, toRadians(30), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.8
            animator.rotate(rightArm, toRadians(55.13), toRadians(-18.38), 0);
            animator.rotate(leftArm, toRadians(55.13), toRadians(18.38), 0);
            animator.rotate(head, toRadians(-19.78), toRadians(-0.22), 0);
            animator.rotate(upper, toRadians(20.55), 0, 0);
            animator.rotate(root, toRadians(3.43), 0, 0);
            animator.rotate(cape, toRadians(60), 0, 0);
            animator.rotate(mouthKok, toRadians(110.26), 0, 0);
            animator.rotate(lowerJaw, toRadians(30), 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(3);//0.96
            animator.rotate(rightArm, toRadians(27.57), toRadians(-9.19), 0);
            animator.rotate(leftArm, toRadians(27.57), toRadians(9.19), 0);
            animator.rotate(head, toRadians(-25), 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(cape, toRadians(38.93), 0, 0);
            animator.rotate(mouthKok, toRadians(55.13), 0, 0);
            animator.rotate(lowerJaw, toRadians(30), 0, 0);
            animator.endKeyframe();

            animator.resetKeyframe(3);
            animator.setStaticKeyframe(9);
        } else if (animator.setAnimation(EntityImmortalShaman.DIE_ANIMATION)) {
            animator.startKeyframe(2);//0.16
            animator.rotate(rightArm, 0, 0, toRadians(10));
            animator.rotate(leftArm, 0, 0, toRadians(-10));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(root, toRadians(10), 0, 0);
            animator.rotate(head, toRadians(7.27), 0, 0);
            animator.endKeyframe();

            //animator.startKeyframe(9);//0.44
            //animator.rotate(rightArm, toRadians(-17.5), 0, 0);
            //animator.rotate(leftArm, toRadians(-17.5), 0, 0);
            //animator.rotate(lowerJaw, toRadians(10), 0, 0);
            //animator.rotate(root, toRadians(7.08), 0, 0);
            //animator.rotate(root, 0, -0.15F, 0);
            //animator.rotate(head, toRadians(20), 0, 0);
            //animator.endKeyframe();

            animator.startKeyframe(8);//0.64
            animator.rotate(rightArm, toRadians(-26.67), 0, 0);
            animator.rotate(leftArm, toRadians(-26.67), 0, 0);
            animator.rotate(lowerJaw, toRadians(30), 0, 0);
            animator.rotate(root, toRadians(11.48), 0, 0);
            animator.move(root, 0F, -0.54F, 0F);
            animator.rotate(head, toRadians(1.82), 0, 0);
            animator.endKeyframe();

            //animator.startKeyframe(5);//0.88
            //animator.rotate(rightArm, toRadians(-37.67), 0, 0);
            //animator.rotate(leftArm, toRadians(-37.67), 0, 0);
            //animator.rotate(lowerJaw, toRadians(27.27), 0, 0);
            //animator.rotate(root, toRadians(-45.45), 0, 0);
            //animator.move(root, 0F, -1F, 0F);
            //animator.rotate(head, toRadians(-20), 0, 0);
            //animator.endKeyframe();

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

            animator.setStaticKeyframe(15);//2.08

            animator.startKeyframe(15);//2.8
            animator.rotate(rightArm, toRadians(2.7768), toRadians(66.6067), toRadians(44.4631));
            animator.rotate(leftArm, toRadians(-1.3314), toRadians(-34.124), toRadians(-42.6612));
            animator.rotate(lowerJaw, 0, 0, 0);
            animator.rotate(root, toRadians(-87.5), 0, 0);
            animator.move(root, 0F, 10.0F, 0F);
            animator.rotate(head, toRadians(-20.7536), toRadians(-43.0795), toRadians(14.5108));
            animator.rotate(rightLeg, 0, toRadians(7.5), toRadians(5));
            animator.rotate(leftLeg, 0, toRadians(-7.5), toRadians(-5));
            animator.endKeyframe();

            animator.setStaticKeyframe(26);
        } else if (animator.setAnimation(EntityImmortalShaman.AVOID_ANIMATION)) {
            animator.setStaticKeyframe(5);
            animator.startKeyframe(5);
            animator.rotate(root, toRadians(-20), 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
        int tick = entity.getAnimationTick();
        if (entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_SUMMON_ANIMATION) {
            if (tick < 34) {
                generalShake(frame);
            }
        } else if (entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_HEAL_ANIMATION) {
            if (tick < 50) {
                generalShake(frame);
            }

        } else if (entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_WOLOLO_ANIMATION) {
            if (tick < 30) {
                generalShake(frame);
            }
        } else if (entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_FR_ATTACK_ANIMATION) {
            if (tick < 12) {
                this.walk(root, 2.5F, 0.125F - tick * 0.01F, false, 0, 0, frame, 1);
                this.flap(root, 1.5F, 0.1F - tick * 0.01F, false, 0, 0, frame, 1);
            }

        } else if (entity.getAnimation() == EntityImmortalShaman.SPELL_CASTING_BOMB_ANIMATION) {
            if (tick < 15) {
                this.walk(head, 1.2F, 0.08F, false, 0, 0, frame, 1);
                this.swing(head, 1.4F, 0.1F, false, 0, 0, frame, 1);
            }
        }

    }

    private void twitchHead(float frame) {//头部抽搐效果 太吓人了没有使用
        this.walk(head, 0.5F, 0.5F, false, 0, 0, frame, 1);
        this.swing(head, 0.5F, 0.5F, false, 0, 0, frame, 1);
        this.flap(head, 0.5F, 0.5F, false, 0, 0, frame, 1);
    }

    private void generalShake(float frame) {
        this.walk(head, 1.4F, 0.04F, false, 0, 0, frame, 1);
        this.swing(head, 1.6F, 0.08F, false, 0, 0, frame, 1);
        this.walk(leftArm, 0.45F, 0.2F, false, 0, 0, frame, 1);
        this.walk(rightArm, 0.45F, 0.2F, false, 0, 0, frame, 1);
        this.bob(root, 0.4F, 1.2F, false, frame, 1);
    }

    @Override
    protected void setupAnim(EntityImmortalShaman entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        if (entity.isAlive() && entity.isWeakness()) {
            this.setStaticRotationAngle(head, toRadians(1.5), 0, 0);
            this.walk(head, 0.2F, 0.15F, false, 0, 0, frame, 1);
            this.walk(root, 0.1F, 0.1F, false, 0, 0, frame, 1);
        }
        //LookAt
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);

        //Walk
        float walkSpeed = 0.6F;
        float walkDegree = 0.6F;
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.armRightLeg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.armRightLeg, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 1.2F, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.armLeftArm, walkSpeed, walkDegree, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 1.2F, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.armRightArm, walkSpeed, walkDegree, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);

        //Idle
        float speed = 0.16F;
        float degree = 0.04F;
        if (entity.isAlive() && !entity.isWeakness()) {
            this.walk(head, speed, degree, false, 0.5F, -0.05F, frame, 1);
            this.walk(lowerJaw, speed, degree * 2F, false, 0, 0, frame, 1);
            this.flap(rightArm, speed, degree * 0.5F, true, 0, 0, frame, 1);
            this.swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.flap(leftArm, speed, degree * 0.5F, false, 0, 0, frame, 1);
            this.swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
    }

    @Override
    protected Animation getSpawnAnimation() {
        return null;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        boolean isRightArm = humanoidArm == HumanoidArm.RIGHT;
        AdvancedModelBox model$part = isRightArm ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(poseStack);
        this.upper.translateAndRotate(poseStack);
        model$part.translateAndRotate(poseStack);
        poseStack.scale(0.9F, 0.9F, 0.9F);
    }
}
