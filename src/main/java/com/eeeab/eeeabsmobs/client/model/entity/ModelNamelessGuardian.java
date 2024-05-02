package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelNamelessGuardian extends EMAdvancedEntityModel<EntityNamelessGuardian> {
    private final AdvancedModelBox root;
    private final AdvancedModelBox upper;
    private final AdvancedModelBox head;
    private final AdvancedModelBox cube_r1;
    private final AdvancedModelBox tooth;
    private final AdvancedModelBox cube_r2;
    private final AdvancedModelBox cube_r3;
    private final AdvancedModelBox body;
    private final AdvancedModelBox cube_r4;
    private final AdvancedModelBox heart;
    private final AdvancedModelBox cube_r5;
    private final AdvancedModelBox rightArm;
    private final AdvancedModelBox rightArmAbove;
    private final AdvancedModelBox cube_r6;
    private final AdvancedModelBox rightArmUnder;
    private final AdvancedModelBox cube_r7;
    //Axe
    private final AdvancedModelBox axe;
    private final AdvancedModelBox lever;
    private final AdvancedModelBox cube_r8;
    private final AdvancedModelBox blade;
    private final AdvancedModelBox cube_r9;
    private final AdvancedModelBox cube_r10;
    private final AdvancedModelBox cube_r11;
    private final AdvancedModelBox cube_r12;

    private final AdvancedModelBox leftArm;
    private final AdvancedModelBox leftArmAbove;
    private final AdvancedModelBox cube_r13;
    private final AdvancedModelBox leftArmUnder;
    private final AdvancedModelBox cube_r14;
    private final AdvancedModelBox lower;
    private final AdvancedModelBox rightLeg;
    private final AdvancedModelBox rightLegAbove;
    private final AdvancedModelBox rightLegUnder;
    private final AdvancedModelBox cube_r15;
    private final AdvancedModelBox leftLeg;
    private final AdvancedModelBox leftLegAbove;
    private final AdvancedModelBox leftLegUnder;
    private final AdvancedModelBox cube_r16;
    private final ModelAnimator animator;


    public ModelNamelessGuardian() {
        texHeight = 256;
        texWidth = 256;

        root = new AdvancedModelBox(this, "root");
        offsetAndRotation(root, -2.18F, 24.0F, 0.0F, -0.0873F, 0.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        offsetAndRotation(upper, 2.18F, -28.0851F, 0.0F, 0.0436F, 0.0F, 0.0F);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        offsetAndRotation(head, 0.0F, -22.9649F, -10.06F, 0.1745F, 0.0F, 0.0F);
        upper.addChild(head);

        cube_r1 = new AdvancedModelBox(this, "cube_r1");
        offsetAndRotation(cube_r1, 0.2563F, -3.9949F, -0.6028F, 0.0F, 0.7854F, 0.0F);
        head.addChild(cube_r1);
        cube_r1.setTextureOffset(141, 1).addBox(-6.3297F, -12.551F, -5.6703F, 12.0F, 16.0F, 12.0F, false)
                .setTextureOffset(149, 31).addBox(-4.0297F, -10.0551F, -3.9703F, 8.0F, 11.0F, 8.0F, 1.5F);

        tooth = new AdvancedModelBox(this, "tooth");
        offsetAndRotation(tooth, 0.1163F, -4.7879F, -2.8632F, 0.2182F, 0.0F, 0.0F);
        head.addChild(tooth);

        cube_r2 = new AdvancedModelBox(this, "cube_r2");
        offsetAndRotation(cube_r2, -7.9651F, 3.692F, -4.944F, 0.6109F, 0.2618F, -0.1745F);
        tooth.addChild(cube_r2);
        cube_r2.setTextureOffset(109, 27).addBox(-0.9127F, -4.8649F, 4.0723F, 2.5F, 8.0F, 3.0F, 0.2F);

        cube_r3 = new AdvancedModelBox(this, "cube_r3");
        offsetAndRotation(cube_r3, 7.7325F, 3.692F, -4.944F, 0.6109F, -0.2618F, 0.1745F);
        tooth.addChild(cube_r3);
        cube_r3.setTextureOffset(109, 27).addBox(-1.5127F, -4.8649F, 4.0723F, 2.5F, 8.0F, 3.0F, 0.2F);

        body = new AdvancedModelBox(this, "body");
        offsetAndRotation(body, 0.0F, -0.8514F, 0.0F, 0.0436F, 0.0F, 0.0F);
        upper.addChild(body);
        body.setTextureOffset(7, 36).addBox(-10.8532F, -13.2245F, -7.5671F, 22.0F, 14.0F, 12.0F, false)
                .setTextureOffset(81, 1).addBox(-8.8958F, 0.6606F, -6.0245F, 18.5F, 3.0F, 9.0F, 0.0F);

        cube_r4 = new AdvancedModelBox(this, "cube_r4");
        offsetAndRotation(cube_r4, 0.2319F, -19.0651F, -2.4809F, 0.1745F, 0.0F, 0.0F);
        body.addChild(cube_r4);
        cube_r4.setTextureOffset(0, 1).addBox(-14.0F, -7.5F, -8.5F, 28.0F, 15.0F, 17.0F, false);

        heart = new AdvancedModelBox(this, "heart");
        heart.setPos(-0.1F, -4.9952F, 0.2181F);
        body.addChild(heart);

        cube_r5 = new AdvancedModelBox(this, "cube_r5");
        offsetAndRotation(cube_r5, 0.3488F, -12.8827F, -2.7149F, 0.3491F, 0.0F, 0.0F);
        heart.addChild(cube_r5);
        cube_r5.setTextureOffset(6, 68).addBox(-5.1169F, -5.2872F, -5.9841F, 10.0F, 10.0F, 10.0F, false);

        rightArm = new AdvancedModelBox(this, "rightArm");
        offsetAndRotation(rightArm, -18.3405F, -21.4257F, 0.0F, 0.0F, 0.0F, 0.0873F);
        body.addChild(rightArm);

        rightArmAbove = new AdvancedModelBox(this, "rightArmAbove");
        offsetAndRotation(rightArmAbove, 1.3723F, -1.5426F, -3.0851F, -0.2618F, 0.2618F, 0.0F);
        rightArm.addChild(rightArmAbove);
        rightArmAbove.setTextureOffset(196, 2).addBox(-7.849F, -5.4852F, -5.3222F, 15.0F, 12.0F, 15.0F, false)
                .setTextureOffset(190, 111).addBox(-8.849F, 5.0148F, -6.3222F, 16.0F, 2.0F, 17.0F, 0.0F);

        cube_r6 = new AdvancedModelBox(this, "cube_r6");
        offsetAndRotation(cube_r6, 0.3488F, -0.5688F, 0.0159F, 0.3054F, 0.0F, 0.1745F);
        rightArmAbove.addChild(cube_r6);
        cube_r6.setTextureOffset(220, 29).addBox(-6.4019F, 5.9513F, -3.0089F, 9.0F, 12.0F, 9.0F, false);

        rightArmUnder = new AdvancedModelBox(this, "rightArmUnder");
        offsetAndRotation(rightArmUnder, -3.2555F, 10.3622F, 0.0F, -0.3054F, 0.2618F, -0.0436F);

        rightArm.addChild(rightArmUnder);
        rightArmUnder.setTextureOffset(222, 83).addBox(-3.3556F, 13.8418F, -3.0677F, 9.0F, 7.0F, 8.0F, 0.5F);


        cube_r7 = new AdvancedModelBox(this, "cube_r7");
        offsetAndRotation(cube_r7, 1.1196F, 7.6107F, -0.8953F, 0.0436F, 0.0F, 0.0F);
        rightArmUnder.addChild(cube_r7);
        cube_r7.setTextureOffset(208, 53).addBox(-6.7039F, -6.359F, -2.9365F, 12.0F, 15.0F, 12.0F, false);

        //Axe
        axe = new AdvancedModelBox(this, "axe");
        offsetAndRotation(axe, 0.9892F, 16.3131F, 3.3012F, -0.0873F, 0.0F, 0.0F);
        rightArmUnder.addChild(axe);

        lever = new AdvancedModelBox(this, "lever");
        lever.setPos(-4.0F, -4.4F, 0.0F);
        axe.addChild(lever);
        lever.setTextureOffset(0, 156).addBox(3.0801F, 4.9444F, -16.8786F, 3.0F, 3.0F, 36.0F, false)
                .setTextureOffset(85, 174).addBox(2.4501F, 2.5444F, -26.4786F, 4.0F, 3.0F, 6.0F, false)
                .setTextureOffset(60, 198).addBox(2.0801F, 1.1444F, -39.6786F, 5.0F, 5.0F, 14.0F, false)
                .setTextureOffset(19, 205).addBox(2.7801F, 3.5444F, 19.1214F, 4.0F, 5.0F, 6.0F, false)
                .setTextureOffset(3, 209).addBox(3.1801F, 4.6444F, 25.1214F, 3.0F, 3.0F, 4.0F, false)
                /*.setTextureOffset(88, 201).addBox(3.1801F, 2.2444F, -42.2786F, 3.0F, 3.0F, 3.0F, false)
                .setTextureOffset(42, 204).addBox(4.0801F, 1.0944F, -48.7786F, 1.0F, 5.0F, 7.0F, false)*/;

        cube_r8 = new AdvancedModelBox(this, "cube_r8");
        offsetAndRotation(cube_r8, 4.7303F, 0.6472F, -11.2938F, -0.4363F, 0.0F, 0.0F);
        lever.addChild(cube_r8);
        cube_r8.setTextureOffset(84, 186).addBox(-2.3102F, 5.7362F, -8.4468F, 4.0F, 3.0F, 7.0F, false);

        blade = new AdvancedModelBox(this, "blade");
        blade.setPos(-4.0F, -4.4F, 0.0F);
        axe.addChild(blade);
        blade.setTextureOffset(222, 196).addBox(2.7801F, 4.3444F, -37.1786F, 4.0F, 10.0F, 11.0F)
                .setTextureOffset(222, 196).addBox(2.7801F, -8.0556F, -37.1786F, 4.0F, 10.0F, 11.0F);
        //blade.mirror = false;

        cube_r9 = new AdvancedModelBox(this, "cube_r9");
        offsetAndRotation(cube_r9, 4.7301F, -8.9005F, -34.8688F, 1.9199F, 0.0F, 0.0F);
        blade.addChild(cube_r9);
        cube_r9.setTextureOffset(102, 192).addBox(-2.3F, -8.5F, -4.5F, 5.0F, 16.0F, 9.0F, false);

        cube_r10 = new AdvancedModelBox(this, "cube_r10");
        offsetAndRotation(cube_r10, 4.6F, -13.2029F, -31.8596F, -2.2078F, 0.0F, 0.0F);
        blade.addChild(cube_r10);
        cube_r10.setTextureOffset(132, 197).addBox(-1.8949F, -14.3568F, -2.9102F, 4.0F, 13.0F, 7.0F, false);

        cube_r11 = new AdvancedModelBox(this, "cube_r11");
        offsetAndRotation(cube_r11, 4.6F, 16.0029F, -31.8596F, -1.9199F, 0.0F, 0.0F);
        blade.addChild(cube_r11);
        cube_r11.setTextureOffset(182, 186).addBox(-1.8699F, -5.8044F, -3.1078F, 4.0F, 16.0F, 15.0F, false);

        cube_r12 = new AdvancedModelBox(this, "cube_r12");
        offsetAndRotation(cube_r12, 4.6F, 16.0029F, -31.8596F, 2.2078F, 0.0F, 0.0F);
        blade.addChild(cube_r12);
        cube_r12.setTextureOffset(156, 191).addBox(-1.8949F, -6.3321F, -9.8793F, 4.0F, 18.0F, 8.0F, -0.1F, false);

        leftArm = new AdvancedModelBox(this, "leftArm");
        offsetAndRotation(leftArm, 18.3405F, -21.4257F, 0.0F, 0.0F, 0.0F, -0.0873F);
        body.addChild(leftArm);

        leftArmAbove = new AdvancedModelBox(this, "leftArmAbove");
        offsetAndRotation(leftArmAbove, -1.3723F, -1.5426F, -3.0851F, -0.2618F, -0.2618F, 0.0F);
        leftArm.addChild(leftArmAbove);
        leftArmAbove.setTextureOffset(196, 2).addBox(-6.1277F, -5.4852F, -5.3222F, 15.0F, 12.0F, 15.0F, true)
                .setTextureOffset(190, 111).addBox(-6.1277F, 5.0148F, -6.3222F, 16.0F, 2.0F, 17.0F, true);

        cube_r13 = new AdvancedModelBox(this, "cube_r13");
        offsetAndRotation(cube_r13, -0.3488F, -0.5688F, 0.0159F, 0.3054F, 0.0F, -0.1745F);
        leftArmAbove.addChild(cube_r13);
        cube_r13.setTextureOffset(220, 29).addBox(-1.698F, 5.9513F, -3.0089F, 9.0F, 12.0F, 9.0F, true);

        leftArmUnder = new AdvancedModelBox(this, "leftArmUnder");
        offsetAndRotation(leftArmUnder, 3.2554F, 10.3622F, 0.0F, -0.3054F, -0.2618F, 0.0436F);
        leftArm.addChild(leftArmUnder);
        leftArmUnder.setTextureOffset(222, 83).addBox(-4.8202F, 14.5107F, -2.3365F, 9.0F, 7.0F, 8.0F, -0.5F, true);

        cube_r14 = new AdvancedModelBox(this, "cube_r14");
        offsetAndRotation(cube_r14, -0.2508F, 8.4795F, -1.4641F, 0.0436F, 0.0F, 0.0F);
        leftArmUnder.addChild(cube_r14);
        cube_r14.setTextureOffset(208, 53).addBox(-5.5641F, -6.559F, -2.3677F, 12.0F, 15.0F, 12.0F, true);

        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(2.18F, -28.0851F, 0.0F);
        root.addChild(lower);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        offsetAndRotation(rightLeg, -7.7128F, 3.404F, 0.0F, 0.0F, 0.0436F, 0.0F);
        lower.addChild(rightLeg);

        rightLegAbove = new AdvancedModelBox(this, "rightLegAbove");
        rightLegAbove.setPos(0.0F, 0.0F, 0.0F);
        rightLeg.addChild(rightLegAbove);
        rightLegAbove.setTextureOffset(146, 54).addBox(-4.0235F, -1.7708F, -6.1544F, 9.0F, 12.0F, 9.0F, false);

        rightLegUnder = new AdvancedModelBox(this, "rightLegUnder");
        rightLegUnder.setPos(0.0F, 8.798F, -1.0F);
        rightLeg.addChild(rightLegUnder);


        cube_r15 = new AdvancedModelBox(this, "cube_r15");
        offsetAndRotation(cube_r15, 0.3488F, 15.3143F, 1.0159F, 0.1309F, 0.0F, 0.0F);
        rightLegUnder.addChild(cube_r15);
        cube_r15.setTextureOffset(74, 38).addBox(-5.5426F, -3.0851F, -9.2554F, 11.0851F, 3.0851F, 3.0851F, false)
                .setTextureOffset(142, 86).addBox(-5.8297F, -15.0F, -6.1703F, 12.0F, 15.0F, 12.0F, false);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        offsetAndRotation(leftLeg, 7.7128F, 3.404F, 0.0F, 0.0F, -0.0436F, 0.0F);
        lower.addChild(leftLeg);
        //leftLeg.mirror = true;

        leftLegAbove = new AdvancedModelBox(this, "leftLegAbove");
        leftLegAbove.setPos(0.0F, 0.0F, 0.0F);
        leftLeg.addChild(leftLegAbove);
        leftLegAbove.setTextureOffset(146, 54).addBox(-4.0235F, -1.7708F, -6.1544F, 9.0F, 12.0F, 9.0F, true);
        //leftLegAbove.mirror = true;

        leftLegUnder = new AdvancedModelBox(this, "leftLegUnder");
        leftLegUnder.setPos(0.0F, 8.798F, -1.0F);
        leftLeg.addChild(leftLegUnder);

        cube_r16 = new AdvancedModelBox(this, "cube_r16");
        offsetAndRotation(cube_r16, 0.3488F, 15.3143F, 1.0159F, 0.1309F, 0.0F, 0.0F);
        leftLegUnder.addChild(cube_r16);
        cube_r16.setTextureOffset(74, 38).addBox(-5.5426F, -3.0851F, -9.2554F, 11.0851F, 3.0851F, 3.0851F, true)
                .setTextureOffset(142, 86).addBox(-5.8297F, -15.0F, -6.1703F, 12.0F, 15.0F, 12.0F, true);
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, this.cube_r8, this.cube_r9,
                this.cube_r10, this.cube_r11, this.cube_r12, this.cube_r13, this.cube_r14, this.cube_r15, this.cube_r16, this.root, this.body, this.head, this.tooth,
                this.heart, this.rightArm, this.rightArmAbove, this.rightArmUnder, this.leftArm, this.leftArmAbove, this.leftArmUnder, this.upper, this.lower, this.axe,
                this.lever, this.blade, this.leftLeg, this.leftLegAbove, this.leftLegUnder, this.rightLeg, this.rightLegAbove, this.rightLegUnder);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void setupAnim(EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, frame);
        //LookAt
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);

        if (entity.isActive() && entity.isAlive() && entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            //Idle
            this.bob(rightArm, 0.1F, 0.5F, false, frame, 1);
            this.bob(leftArm, 0.1F, 0.5F, false, frame, 1);
            this.bob(body, 0.1F, 0.2F, false, frame, 1);
            this.bob(head, 0.1F, 0.4F, false, frame, 1);
            this.walk(upper, 0.1F, 0.05F, false, 0, 0, frame, 1);
            this.walk(head, 0.1F, 0.05F, true, 0, 0, frame, 1);
            //Walk
            float speed = 0.2F;
            float degree = 0.66F;
            //this.bob(this.lower, speed * 2F, degree * 0.24F, true, limbSwing, limbSwingAmount);
            //this.bob(this.leftLeg, speed * 2F, degree * 0.5F, false, limbSwing, limbSwingAmount);
            //this.bob(this.rightLeg, speed * 2F, degree * 0.5F, true, limbSwing, limbSwingAmount);
            this.walk(this.root, speed * 2F, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.walk(this.head, speed * 1.9F, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.flap(this.upper, speed, degree * 0.12F, true, 0, 0, limbSwing, limbSwingAmount);
            this.swing(this.upper, speed, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.swing(this.lower, speed, degree * 0.2F, true, 0, 0, limbSwing, limbSwingAmount);
            this.walk(this.leftLeg, speed, degree, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
            this.walk(this.leftLegUnder, speed, degree * 0.56F, true, 0F, 0, limbSwing, limbSwingAmount);
            this.walk(this.rightLeg, speed, degree, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
            this.walk(this.rightLegUnder, speed, degree * 0.56F, false, 0F, 0, limbSwing, limbSwingAmount);
            this.walk(this.leftArm, speed, degree, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
            this.walk(this.rightArm, speed * 0.98F, 0.2F, false, 0.0F, 0, limbSwing, limbSwingAmount);
        }
    }

    private void animate(EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float frame) {
        this.resetToDefaultPose();
        animator.update(entity);
        if (entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            if (entity.isActive()) {
                //抬起斧头姿势
                setStaticRotationAngle(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
                setStaticRotationPoint(axe, 0, -1F, -5F);
                setStaticRotationAngle(rightArm, toRadians(-30), toRadians(25), toRadians(25));
                setStaticRotationAngle(rightArmUnder, toRadians(-95), 0, 0);
            } else {
                setStaticRotationAngle(rightArm, toRadians(-35), toRadians(2.5), toRadians(4));
                setStaticRotationAngle(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
                setStaticRotationAngle(head, toRadians(30), 0, 0);
                setStaticRotationAngle(upper, toRadians(5), 0, 0);
                setStaticRotationAngle(axe, toRadians(1), 0, 0);
                setStaticRotationPoint(axe, 0F, 2F, 9.5F);
            }
        }
        if (animator.setAnimation(EntityNamelessGuardian.ATTACK_ANIMATION_1)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(8);//0.4
            animator.rotate(head, toRadians(13), toRadians(-10), toRadians(-5));
            animator.move(head, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, toRadians(-10), 0, toRadians(8));
            animator.rotate(rightArm, toRadians(-45), toRadians(15), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-27.5), 0, toRadians(-10));
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftLeg, toRadians(10), toRadians(5), 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, toRadians(-37.5), toRadians(-7.5), 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(25), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(10));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(3);//0.56

            animator.startKeyframe(2);//0.68
            animator.rotate(head, toRadians(11.5), toRadians(15), toRadians(-2.5));
            animator.move(head, 0, 0, -1.5F);
            animator.move(root, 0, 0.5F, -2.5F);
            animator.rotate(body, 0, toRadians(-2.5), 0);
            animator.rotate(upper, toRadians(-2.5), toRadians(-50), toRadians(4));
            animator.rotate(rightArm, toRadians(-44), toRadians(23), toRadians(-21));
            animator.rotate(rightArmUnder, toRadians(-25), toRadians(-7.5), toRadians(-2.5));
            animator.move(rightArm, 0, 1.5F, 0);
            animator.rotate(leftArm, toRadians(1.25), toRadians(-2.5), toRadians(-12.5));
            animator.rotate(leftArmUnder, toRadians(-8.75), 0, 0);
            animator.move(leftArm, 0, -1.5F, 0);
            animator.rotate(leftLeg, toRadians(16.25), toRadians(-7.5), toRadians(-10));
            animator.move(leftLeg, -3, 0, 5);
            animator.rotate(leftLegUnder, toRadians(12.5), 0, 0);
            animator.rotate(rightLeg, toRadians(-22.5), toRadians(-18.75), toRadians(2.5));
            animator.move(rightLeg, 1.5F, 0, -2);
            animator.rotate(rightLegUnder, toRadians(12.5), 0, 0);
            animator.rotate(axe, toRadians(3.75), toRadians(-2.5), toRadians(-2.5));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.8
            animator.rotate(head, toRadians(10), toRadians(40), 0);
            animator.move(head, 0, 0, -3F);
            animator.move(root, 0, 1F, -5F);
            animator.rotate(body, 0, toRadians(-5), 0);
            animator.rotate(upper, toRadians(-5), toRadians(-100), 0);
            animator.rotate(rightArm, toRadians(-35), toRadians(23), toRadians(-30));
            animator.rotate(rightArmUnder, toRadians(3), toRadians(20), toRadians(-13));
            animator.move(rightArm, 0, 3F, 0);
            animator.rotate(leftArm, toRadians(30), toRadians(-5), toRadians(-15));
            animator.rotate(leftArmUnder, toRadians(-17.5), 0, 0);
            animator.move(leftArm, 0, -3F, 0);
            animator.rotate(leftLeg, toRadians(22.5), toRadians(-20), toRadians(-20));
            animator.move(leftLeg, -6, 0, 10);
            animator.rotate(leftLegUnder, toRadians(25), 0, 0);
            animator.rotate(rightLeg, toRadians(-7.5), toRadians(-30), toRadians(5));
            animator.move(rightLeg, 3F, 0, -4);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(10), toRadians(5), toRadians(-15));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(7);//1.16

            animator.startKeyframe(12);//1.8
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, toRadians(2), 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

        } else if (animator.setAnimation(EntityNamelessGuardian.ATTACK_ANIMATION_2)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(10), toRadians(40), 0);
            animator.move(head, 0, 0, -3F);
            animator.move(root, 0, 1F, -5F);
            animator.rotate(body, 0, toRadians(-5), 0);
            animator.rotate(upper, toRadians(-5), toRadians(-100), 0);
            animator.rotate(rightArm, toRadians(-35), toRadians(23), toRadians(-30));
            animator.rotate(rightArmUnder, toRadians(3), toRadians(20), toRadians(-13));
            animator.move(rightArm, 0, 3F, 0);
            animator.rotate(leftArm, toRadians(30), toRadians(-5), toRadians(-15));
            animator.rotate(leftArmUnder, toRadians(-17.5), 0, 0);
            animator.move(leftArm, 0, -3F, 0);
            animator.rotate(leftLeg, toRadians(22.5), toRadians(-20), toRadians(-20));
            animator.move(leftLeg, -6, 0, 10);
            animator.rotate(leftLegUnder, toRadians(25), 0, 0);
            animator.rotate(rightLeg, toRadians(-7.5), toRadians(-30), toRadians(5));
            animator.move(rightLeg, 3F, 0, -4);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(10), toRadians(5), toRadians(-15));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(12);//0.6
            animator.rotate(head, toRadians(9), toRadians(52), toRadians(3));
            animator.move(head, 0, 0, -2F);
            animator.move(root, 0, 0, -5F);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, toRadians(-20), toRadians(-120), 0);
            animator.rotate(rightArm, toRadians(-53), toRadians(-6), toRadians(16));
            animator.rotate(rightArmUnder, toRadians(-18), toRadians(8), toRadians(-44));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-53), toRadians(-0.5), toRadians(34));
            animator.rotate(leftArmUnder, toRadians(8), toRadians(19), toRadians(35));
            animator.move(leftArm, 0, 3F, -7F);
            animator.rotate(leftLeg, toRadians(22.5), toRadians(-20), toRadians(-20));
            animator.move(leftLeg, -6, 0, 10);
            animator.rotate(leftLegUnder, toRadians(25), 0, 0);
            animator.rotate(rightLeg, toRadians(-5), toRadians(-38), toRadians(16));
            animator.move(rightLeg, 10F, 2F, -5F);
            animator.rotate(rightLegUnder, toRadians(2.5), 0, 0);
            animator.rotate(axe, toRadians(-2), toRadians(-27), toRadians(51));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(1);//0.68
            animator.rotate(head, toRadians(1), toRadians(29.5), toRadians(-0.6));
            animator.move(head, 0, 0, -1.8F);
            animator.move(root, 0, 0.1F, -5.35F);
            animator.rotate(body, 0, toRadians(2.5), 0);
            animator.rotate(upper, toRadians(25), toRadians(-57), toRadians(-13));
            animator.rotate(rightArm, toRadians(-51), toRadians(9), toRadians(26.5));
            animator.rotate(rightArmUnder, toRadians(-10), toRadians(-0.35), toRadians(-19));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(3), toRadians(36));
            animator.rotate(leftArmUnder, toRadians(10), toRadians(19.1), toRadians(35.1));
            animator.move(leftArm, 0, 3F, -7.5F);
            animator.rotate(leftLeg, toRadians(2), toRadians(-18), toRadians(-18.5));
            animator.move(leftLeg, -5.25F, 0.25F, 6.25F);
            animator.rotate(leftLegUnder, toRadians(24), 0, 0);
            animator.rotate(rightLeg, toRadians(-3.15), toRadians(-35.55), toRadians(14));
            animator.move(rightLeg, 6F, 8F, -3F);
            animator.rotate(rightLegUnder, toRadians(8), 0, 0);
            animator.rotate(axe, toRadians(25), toRadians(-45), toRadians(26));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.76
            animator.rotate(head, toRadians(-7), toRadians(7), toRadians(-4));
            animator.move(head, 0, 0, -1.5F);
            animator.move(root, 0, 0.17F, -5.65F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(13), toRadians(1), toRadians(10));
            animator.rotate(rightArm, toRadians(-49), toRadians(25), toRadians(37));
            animator.rotate(rightArmUnder, toRadians(-2), toRadians(-9), toRadians(7));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(5), toRadians(37));
            animator.rotate(leftArmUnder, toRadians(12), toRadians(19.1), toRadians(35.1));
            animator.move(leftArm, 0, 3F, -8F);
            animator.rotate(leftLeg, toRadians(-19), toRadians(-16), toRadians(-17));
            animator.move(leftLeg, -2F, 0.6F, 0.6F);
            animator.rotate(leftLegUnder, toRadians(23), 0, 0);
            animator.rotate(rightLeg, toRadians(-3.15), toRadians(-33), toRadians(12));
            animator.move(rightLeg, 1.75F, -0.25F, -1.25F);
            animator.rotate(rightLegUnder, toRadians(14), 0, 0);
            animator.rotate(axe, toRadians(53), toRadians(-62), 0);
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.84
            animator.rotate(head, toRadians(-7), toRadians(-3), toRadians(-5));
            animator.move(head, 0, 0, -1.1F);
            animator.move(root, 0, 0.25F, -6F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(7), toRadians(11), toRadians(8));
            animator.rotate(rightArm, toRadians(-50), toRadians(25), toRadians(37));
            animator.rotate(rightArmUnder, toRadians(-2), toRadians(-9), toRadians(7));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-51), toRadians(6), toRadians(39));
            animator.rotate(leftArmUnder, toRadians(14), toRadians(18), toRadians(35));
            animator.move(leftArm, 0, 3F, -8F);
            animator.rotate(leftLeg, toRadians(-17), toRadians(-15), toRadians(-16));
            animator.move(leftLeg, -1F, 1F, -5F);
            animator.rotate(leftLegUnder, toRadians(22), 0, 0);
            animator.rotate(rightLeg, toRadians(0.3), toRadians(-30), toRadians(10));
            animator.move(rightLeg, -1F, -1F, 0);
            animator.rotate(rightLegUnder, toRadians(19), 0, 0);
            animator.rotate(axe, toRadians(50), toRadians(-62), toRadians(2));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(3);//1
            animator.rotate(head, toRadians(13), toRadians(-46), toRadians(-13));
            animator.move(head, 0, 0, 0);
            animator.move(root, 0, 0.5F, -7F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(-10), toRadians(40), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(25), toRadians(37));
            animator.rotate(rightArmUnder, toRadians(-2), toRadians(-9), toRadians(7));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-45), toRadians(22), toRadians(51));
            animator.rotate(leftArmUnder, toRadians(20), toRadians(19), toRadians(35));
            animator.move(leftArm, 0, 3F, -12F);
            animator.rotate(leftLeg, toRadians(-13), toRadians(-11), toRadians(-13));
            animator.move(leftLeg, -1F, 1F, -2F);
            animator.rotate(leftLegUnder, toRadians(17.5), 0, 0);
            animator.rotate(rightLeg, toRadians(5), toRadians(-22), toRadians(6));
            animator.move(rightLeg, -1F, -1F, 0);
            animator.rotate(rightLegUnder, toRadians(35), 0, 0);
            animator.rotate(axe, toRadians(35), toRadians(-69), toRadians(19));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(6);//1.28

            animator.startKeyframe(9);//1.76
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, toRadians(2), 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

        } else if (animator.setAnimation(EntityNamelessGuardian.ATTACK_ANIMATION_3)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(13), toRadians(-46), toRadians(-13));
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0.5F, -7F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(-10), toRadians(40), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(25), toRadians(37));
            animator.rotate(rightArmUnder, toRadians(-2), toRadians(-9), toRadians(7));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-45), toRadians(22), toRadians(51));
            animator.rotate(leftArmUnder, toRadians(20), toRadians(19), toRadians(35));
            animator.move(leftArm, 0, 3F, -12F);
            animator.rotate(leftLeg, toRadians(-13), toRadians(-11), toRadians(-13));
            animator.move(leftLeg, -3F, 1F, -2F);
            animator.rotate(leftLegUnder, toRadians(17.5), 0, 0);
            animator.rotate(rightLeg, toRadians(5), toRadians(-22), toRadians(6));
            animator.move(rightLeg, -1F, -1F, 0);
            animator.rotate(rightLegUnder, toRadians(35), 0, 0);
            animator.rotate(axe, toRadians(35), toRadians(-69), toRadians(19));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.2
            animator.rotate(head, toRadians(20), toRadians(-39), toRadians(-16));
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0.3F, -7F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(-20), toRadians(44), 0);
            animator.rotate(rightArm, toRadians(-42), toRadians(30), toRadians(34));
            animator.rotate(rightArmUnder, toRadians(-2.8), toRadians(-0.3), toRadians(-27));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-35), toRadians(23), toRadians(48));
            animator.rotate(leftArmUnder, toRadians(6), toRadians(19), toRadians(20));
            animator.move(leftArm, 0, 3F, -12F);
            animator.rotate(leftLeg, toRadians(-1.5), toRadians(25), toRadians(-6));
            animator.move(leftLeg, -5F, 1F, -5F);
            animator.rotate(leftLegUnder, toRadians(-1.5), 0, 0);
            animator.rotate(rightLeg, toRadians(-62), toRadians(54), toRadians(-5));
            animator.move(rightLeg, 3F, -0.5F, 5F);
            animator.rotate(rightLegUnder, toRadians(37), 0, 0);
            animator.rotate(axe, toRadians(-11), toRadians(-75), toRadians(55));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.48
            animator.rotate(head, toRadians(25), toRadians(-34), toRadians(-18));
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, -7F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(-10), toRadians(50), 0);
            animator.rotate(rightArm, toRadians(-25), toRadians(40), toRadians(30));
            animator.rotate(rightArmUnder, toRadians(2.5), toRadians(0.3), toRadians(-12));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-18), toRadians(25), toRadians(40));
            animator.rotate(leftArmUnder, toRadians(-20), toRadians(19), toRadians(-5));
            animator.move(leftArm, 0, 3F, -12F);
            animator.rotate(leftLeg, toRadians(-3.5), toRadians(77), toRadians(5));
            animator.move(leftLeg, -8F, 1F, -8F);
            animator.rotate(leftLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(rightLeg, toRadians(5), toRadians(70), toRadians(40));
            animator.move(rightLeg, 6F, 0F, 7F);
            animator.rotate(rightLegUnder, toRadians(40), 0, 0);
            animator.rotate(axe, toRadians(-98), toRadians(-86), toRadians(124));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.72
            animator.rotate(head, toRadians(10), toRadians(-35), toRadians(-12));
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, toRadians(222.7), 0);
            animator.move(root, 0, 0, -9F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(16), toRadians(58), toRadians(17));
            animator.rotate(rightArm, toRadians(-15), toRadians(52), toRadians(67));
            animator.rotate(rightArmUnder, toRadians(2.5), toRadians(0.3), toRadians(-12));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-11), toRadians(-10), toRadians(-39));
            animator.rotate(leftArmUnder, toRadians(-38), toRadians(29), toRadians(7.75));
            animator.move(leftArm, 0, 3F, -12F);
            animator.rotate(leftLeg, toRadians(19), toRadians(62), toRadians(-4));
            animator.move(leftLeg, -6.5F, 0.5F, -7F);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, toRadians(-1.6), toRadians(66), toRadians(51));
            animator.move(rightLeg, 4.5F, 0F, 7F);
            animator.rotate(rightLegUnder, toRadians(31), toRadians(1.4), toRadians(-3));
            animator.rotate(axe, toRadians(-98), toRadians(-86), toRadians(124));
            animator.move(axe, 0, -1F, -3F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.88
            animator.rotate(head, toRadians(-0.3), toRadians(-36), toRadians(-9));
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, toRadians(327.5), 0);
            animator.move(root, 0, 0, -10F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(28), toRadians(62), toRadians(24));
            animator.rotate(rightArm, toRadians(58), toRadians(41), toRadians(141));
            animator.rotate(rightArmUnder, toRadians(2.5), toRadians(0.3), toRadians(-12));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-11), toRadians(-31), toRadians(-64));
            animator.rotate(leftArmUnder, toRadians(-46), toRadians(34), toRadians(14));
            animator.move(leftArm, 0, 3F, -12F);
            animator.rotate(leftLeg, toRadians(35), toRadians(50), toRadians(-11));
            animator.move(leftLeg, -5F, 0.1F, -6F);
            animator.rotate(leftLegUnder, toRadians(-5.83), 0, 0);
            animator.rotate(rightLeg, toRadians(-7), toRadians(45), toRadians(23));
            animator.move(rightLeg, 3F, 0F, 7F);
            animator.rotate(rightLegUnder, toRadians(24), toRadians(2.5), toRadians(-6));
            animator.rotate(axe, toRadians(-98), toRadians(-86), toRadians(124));
            animator.move(axe, 0, -1F, -3F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.96
            animator.rotate(head, toRadians(-0.3), toRadians(-36), toRadians(-9));
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, toRadians(327.5), 0);
            animator.move(root, 0, 0, -10F);
            animator.rotate(body, 0, toRadians(5), 0);
            animator.rotate(upper, toRadians(28), toRadians(62), toRadians(24));
            animator.rotate(rightArm, toRadians(58), toRadians(41), toRadians(141));
            animator.rotate(rightArmUnder, toRadians(2.5), toRadians(0.3), toRadians(-12));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-11), toRadians(-31), toRadians(-64));
            animator.rotate(leftArmUnder, toRadians(-46), toRadians(34), toRadians(14));
            animator.move(leftArm, 0, 3F, -12F);
            animator.rotate(leftLeg, toRadians(40), toRadians(47), toRadians(-13));
            animator.move(leftLeg, -5F, 0F, -6F);
            animator.rotate(leftLegUnder, toRadians(-7.5), 0, 0);
            animator.rotate(rightLeg, toRadians(-9), toRadians(39), toRadians(16));
            animator.move(rightLeg, 3F, 0F, 7F);
            animator.rotate(rightLegUnder, toRadians(22), toRadians(2.8), toRadians(-7));
            animator.rotate(axe, toRadians(-98), toRadians(-86), toRadians(124));
            animator.move(axe, 0, -1F, -3F);
            animator.endKeyframe();

            animator.setStaticKeyframe(8);//1.44

            animator.startKeyframe(6);//1.84
            animator.rotate(head, toRadians(-0.14), toRadians(-16.34), toRadians(-4.08));
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, toRadians(345.42), 0);
            animator.move(root, 0, 0, -4.49F);
            animator.rotate(body, 0, toRadians(2.24), 0);
            animator.rotate(upper, toRadians(13.87), toRadians(28.11), toRadians(10.94));
            animator.rotate(rightArm, toRadians(9.77), toRadians(32.51), toRadians(77.25));
            animator.rotate(rightArmUnder, toRadians(-72.8022), toRadians(10.0117), toRadians(-60.6723));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, toRadians(-5.04), toRadians(-14.33), toRadians(-28.95));
            animator.rotate(leftArmUnder, toRadians(-21), toRadians(15.28), toRadians(6.28));
            animator.move(leftArm, 0, 1.35F, -5.39F);
            animator.rotate(leftLeg, toRadians(18.37), toRadians(21.1), toRadians(-6.22));
            animator.move(leftLeg, -2.24F, 0F, -2.69F);
            animator.rotate(leftLegUnder, toRadians(-3.37), 0, 0);
            animator.rotate(rightLeg, toRadians(-3.85), toRadians(17.59), toRadians(7.15));
            animator.move(rightLeg, 1.35F, 0F, 3.14F);
            animator.rotate(rightLegUnder, toRadians(10.02), toRadians(1.28), toRadians(-3.11));
            animator.rotate(axe, toRadians(-2.162), toRadians(-18.677), toRadians(61.4593));
            animator.move(axe, 3F, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(6);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, toRadians(360), 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);
        } else if (animator.setAnimation(EntityNamelessGuardian.ATTACK2_ANIMATION_1)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(8);//0.4
            animator.rotate(head, toRadians(-4.6141), toRadians(-19.882), toRadians(-1.3811));
            animator.rotate(root, toRadians(2.5), 0, 0);
            animator.move(root, 0, 0, -2);
            animator.rotate(upper, 0, toRadians(30), toRadians(-2.5));
            animator.rotate(rightArm, toRadians(13.1657), toRadians(32.4813), toRadians(0.7232));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-42.5), 0, 0);
            animator.rotate(leftArm, toRadians(-23.2613), toRadians(-9.7602), toRadians(-10.1917));
            animator.rotate(leftLeg, toRadians(-5), 0, 0);
            animator.move(leftLeg, 0, 0, -6);
            animator.rotate(leftLegUnder, toRadians(6), 0, 0);
            animator.rotate(rightLeg, toRadians(10), toRadians(5), toRadians(5));
            animator.move(rightLeg, 0, 0, 3F);
            animator.rotate(rightLegUnder, toRadians(8.89), 0, 0);
            animator.rotate(axe, toRadians(69.4128), toRadians(-5.226), toRadians(5.6485));
            animator.move(axe, 0, 7F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(5);//0.64
            animator.rotate(head, toRadians(-0.18), toRadians(0.13), toRadians(2.17));
            animator.rotate(root, toRadians(3.75), toRadians(-2.5), 0);
            animator.move(root, 0, 0, -2);
            animator.rotate(upper, toRadians(-2.9453), toRadians(40.0917), toRadians(-10.8652));
            animator.rotate(rightArm, toRadians(13.1657), toRadians(32.4813), toRadians(0.7232));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-20.1092), toRadians(10.5789), toRadians(-9.0343));
            animator.rotate(leftArm, toRadians(18.52), toRadians(-6.38), toRadians(-6.92));
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, toRadians(8.44), toRadians(-10.53), toRadians(-4.1));
            animator.move(leftLeg, -1, 0, -0.5F);
            animator.rotate(leftLegUnder, toRadians(11.63), 0, 0);
            animator.rotate(rightLeg, toRadians(10), toRadians(5), toRadians(5));
            animator.move(rightLeg, 0, 0, -1F);
            animator.rotate(rightLegUnder, toRadians(13.33), 0, 0);
            animator.rotate(axe, toRadians(18.1755), toRadians(19.7208), toRadians(-52.9428));
            animator.move(axe, 0, 5F, -9F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.76
            animator.rotate(head, toRadians(2.4859), toRadians(12.1324), toRadians(4.3049));
            animator.rotate(root, toRadians(4.5), toRadians(-4), 0);
            animator.move(root, 0, 0, -2);
            animator.rotate(upper, toRadians(-6.34), toRadians(-9.4), toRadians(-10.29));
            animator.rotate(rightArm, toRadians(-34.3), toRadians(-3.24), toRadians(-3.09));
            animator.move(rightArm, -1.43F, 3.57F, -5.71F);
            animator.rotate(rightArmUnder, toRadians(6.85), toRadians(-16.7), toRadians(4.76));
            animator.rotate(leftArm, toRadians(43.5937), toRadians(-4.3537), toRadians(-4.9519));
            animator.rotate(leftArmUnder, toRadians(-33), 0, 0);
            animator.rotate(leftLeg, toRadians(16.51), toRadians(-16.84), toRadians(-6.57));
            animator.move(leftLeg, -1.6F, 0, 2.8F);
            animator.rotate(leftLegUnder, toRadians(15), 0, 0);
            animator.rotate(rightLeg, toRadians(-40.83), toRadians(1.67), toRadians(1.67));
            animator.move(rightLeg, 0, 0, -3.4F);
            animator.rotate(rightLegUnder, toRadians(16), 0, 0);
            animator.rotate(axe, toRadians(-56.925), toRadians(52.1291), toRadians(-133.0771));
            animator.move(axe, -1.14F, 6F, -3.14F);
            animator.endKeyframe();

            animator.startKeyframe(3);//0.88
            animator.rotate(head, toRadians(16.1114), toRadians(42.0217), toRadians(4.5183));
            animator.rotate(root, toRadians(5), toRadians(-5), 0);
            animator.move(root, 0, 0, -4);
            animator.rotate(upper, toRadians(-12.7765), toRadians(-42.4005), toRadians(-9.8982));
            animator.rotate(rightArm, toRadians(-95.1111), toRadians(-27.0463), toRadians(-5.6255));
            animator.move(rightArm, -2F, 5F, -8F);
            animator.rotate(rightArmUnder, toRadians(24.8171), toRadians(-34.8823), toRadians(13.9582));
            animator.move(rightArmUnder, 0, 0, -3F);
            animator.rotate(leftArm, toRadians(51.0937), toRadians(-4.3537), toRadians(-4.9519));
            animator.rotate(leftArmUnder, toRadians(-55), 0, 0);
            animator.rotate(leftLeg, toRadians(21.8848), toRadians(-21.0508), toRadians(-8.2086));
            animator.move(leftLeg, -2F, 0, 5F);
            animator.rotate(leftLegUnder, toRadians(15), 0, 0);
            animator.rotate(rightLeg, toRadians(-10), toRadians(0), toRadians(0));
            animator.move(rightLeg, 0, 0, -5F);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(-56.925), toRadians(52.1291), toRadians(-133.0771));
            animator.move(axe, -4F, 12F, -2F);
            animator.endKeyframe();

            animator.setStaticKeyframe(13);//1.56

            animator.startKeyframe(9);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
        } else if (animator.setAnimation(EntityNamelessGuardian.ATTACK2_ANIMATION_2)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(16.1114), toRadians(42.0217), toRadians(4.5183));
            animator.rotate(root, toRadians(5), toRadians(-5), 0);
            animator.move(root, 0, 0, -4);
            animator.rotate(upper, toRadians(-12.7765), toRadians(-42.4005), toRadians(-9.8982));
            animator.rotate(rightArm, toRadians(-95.1111), toRadians(-27.0463), toRadians(-5.6255));
            animator.move(rightArm, -2F, 5F, -8F);
            animator.rotate(rightArmUnder, toRadians(24.8171), toRadians(-34.8823), toRadians(13.9582));
            animator.move(rightArmUnder, 0, 0, -3F);
            animator.rotate(leftArm, toRadians(51.0937), toRadians(-4.3537), toRadians(-4.9519));
            animator.rotate(leftArmUnder, toRadians(-55), 0, 0);
            animator.rotate(leftLeg, toRadians(21.8848), toRadians(-21.0508), toRadians(-8.2086));
            animator.move(leftLeg, -2F, 0, 5F);
            animator.rotate(leftLegUnder, toRadians(15), 0, 0);
            animator.rotate(rightLeg, toRadians(-10), toRadians(0), toRadians(0));
            animator.move(rightLeg, 0, 0, -5F);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(-56.925), toRadians(52.1291), toRadians(-133.0771));
            animator.move(axe, -4F, 12F, -2F);
            animator.endKeyframe();

            animator.startKeyframe(8);//0.4
            animator.rotate(head, toRadians(10.56), toRadians(39.61), toRadians(3.65));
            animator.rotate(root, toRadians(5), toRadians(-5), 0);
            animator.move(root, 0, 0, -4);
            animator.rotate(upper, toRadians(-16.5315), toRadians(-57.0752), toRadians(-5.0253));
            animator.rotate(rightArm, toRadians(-96.3815), toRadians(-44.4645), toRadians(-3.4793));
            animator.move(rightArm, -2F, 5F, -8F);
            animator.rotate(rightArmUnder, toRadians(87.8519), toRadians(-97.6907), toRadians(-53.7422));
            animator.move(rightArmUnder, 0, 0, 0);
            animator.rotate(leftArm, toRadians(38.59), toRadians(-4.35), toRadians(-4.95));
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, toRadians(21.88), toRadians(-21.05), toRadians(-8.21));
            animator.move(leftLeg, -2F, 0, 5F);
            animator.rotate(leftLegUnder, toRadians(15), 0, 0);
            animator.rotate(rightLeg, toRadians(-10), toRadians(0), toRadians(0));
            animator.move(rightLeg, 0, 0, -5F);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(0.5457), toRadians(15.3246), toRadians(-18.2975));
            animator.move(axe, -1F, 1F, -12F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.6
            animator.rotate(head, toRadians(5), toRadians(37.2), toRadians(2.78));
            animator.rotate(root, toRadians(5), toRadians(-5), 0);
            animator.move(root, 0, 0, -4);
            animator.rotate(upper, toRadians(-20.2138), toRadians(-37.546), toRadians(1.9736));
            animator.rotate(rightArm, toRadians(-96.38), toRadians(-44.46), toRadians(-3.48));
            animator.move(rightArm, -2F, 5F, -8F);
            animator.rotate(rightArmUnder, toRadians(87.8519), toRadians(-97.6907), toRadians(-53.7422));
            animator.move(rightArmUnder, 0, 0, 0);
            animator.rotate(leftArm, toRadians(26.09), toRadians(-4.35), toRadians(-4.95));
            animator.rotate(leftLeg, toRadians(-41.3534), toRadians(-1.1897), toRadians(-7.7501));
            animator.move(leftLeg, -2F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(15), 0, 0);
            animator.rotate(rightLeg, toRadians(35), toRadians(0), toRadians(0));
            animator.move(rightLeg, 0, 0, -3F);
            animator.rotate(rightLegUnder, toRadians(-5), 0, 0);
            animator.rotate(axe, toRadians(19.681), toRadians(16.3327), toRadians(9.4499));
            animator.move(axe, -1F, 0, -12F);
            animator.endKeyframe();

            animator.startKeyframe(2);//0.72
            animator.rotate(head, toRadians(-3.516), toRadians(2.2997), toRadians(-0.0877));
            animator.rotate(root, toRadians(10), toRadians(-5), 0);
            animator.move(root, 0, 0, -6);
            animator.rotate(upper, toRadians(-6.8042), toRadians(5.8204), toRadians(-8.8916));
            animator.rotate(rightArm, toRadians(-100.8051), toRadians(64.9714), toRadians(-17.7692));
            animator.move(rightArm, -2F, 2F, -5F);
            animator.rotate(rightArmUnder, toRadians(99.1154), toRadians(-87.9401), toRadians(-65.2221));
            animator.move(rightArmUnder, 2F, 0, 0);
            animator.rotate(leftArm, toRadians(41.2195), toRadians(3.4126), toRadians(-20.6467));
            animator.rotate(leftLeg, toRadians(-31.35), toRadians(-1.19), toRadians(-7.75));
            animator.move(leftLeg, -2F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(27.5), 0, 0);
            animator.rotate(rightLeg, toRadians(35.0371), toRadians(-4.3079), toRadians(6.1409));
            animator.move(rightLeg, 0, 0, 5F);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(24.0965), toRadians(-3.0523), toRadians(4.4701));
            animator.move(axe, -1F, 2F, -12F);
            animator.endKeyframe();

            animator.setStaticKeyframe(11);//1.2

            animator.startKeyframe(11);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
        } else if (animator.setAnimation(EntityNamelessGuardian.ATTACK2_ANIMATION_3)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(-3.516), toRadians(2.2997), toRadians(-0.0877));
            animator.rotate(root, toRadians(10), toRadians(-5), 0);
            animator.move(root, 0, 0, -6);
            animator.rotate(upper, toRadians(-6.8042), toRadians(5.8204), toRadians(-8.8916));
            animator.rotate(rightArm, toRadians(-100.8051), toRadians(64.9714), toRadians(-17.7692));
            animator.move(rightArm, -2F, 2F, -5F);
            animator.rotate(rightArmUnder, toRadians(99.1154), toRadians(-87.9401), toRadians(-65.2221));
            animator.move(rightArmUnder, 2F, 0, 0);
            animator.rotate(leftArm, toRadians(41.2195), toRadians(3.4126), toRadians(-20.6467));
            animator.rotate(leftLeg, toRadians(-31.35), toRadians(-1.19), toRadians(-7.75));
            animator.move(leftLeg, -2F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(27.5), 0, 0);
            animator.rotate(rightLeg, toRadians(35.0371), toRadians(-4.3079), toRadians(6.1409));
            animator.move(rightLeg, 0, 0, 5F);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(24.0965), toRadians(-3.0523), toRadians(4.4701));
            animator.move(axe, -1F, 2F, -12F);
            animator.endKeyframe();

            animator.startKeyframe(8);//0.32
            animator.rotate(head, toRadians(16.48), toRadians(2.3), toRadians(-0.09));
            animator.rotate(root, toRadians(10), toRadians(-5), 0);
            animator.move(root, 0, 0, -7);
            animator.rotate(upper, toRadians(-25.7223), toRadians(6.4777), toRadians(1.1463));
            animator.rotate(rightArm, toRadians(-150.063), toRadians(33.617), toRadians(-78.1862));
            animator.move(rightArm, -2F, 3F, -5F);
            animator.rotate(rightArmUnder, toRadians(61.6154), toRadians(-87.9401), toRadians(-65.2221));
            animator.move(rightArmUnder, 2F, 0, 0);
            animator.rotate(leftArm, toRadians(-8.78), toRadians(3.41), toRadians(-20.65));
            animator.rotate(leftLeg, toRadians(3.65), toRadians(-1.19), toRadians(-7.75));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(27.5), 0, 0);
            animator.rotate(rightLeg, toRadians(-19.9629), toRadians(-4.3079), toRadians(6.1409));
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(11.6), toRadians(-3.05), toRadians(4.47));
            animator.move(axe, -1F, 2F, -12F);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.52
            animator.rotate(head, toRadians(26.5418), toRadians(-4.4133), toRadians(-3.4364));
            animator.rotate(root, toRadians(20), toRadians(-5), 0);
            animator.move(root, 0, 0, -7.5F);
            animator.rotate(upper, toRadians(-25.72), toRadians(6.48), toRadians(1.15));
            animator.rotate(rightArm, toRadians(-93.8005), toRadians(-23.0041), toRadians(-107.1137));
            animator.move(rightArm, -2F, 1F, -5F);
            animator.rotate(rightArmUnder, toRadians(41.563), toRadians(-87.069), toRadians(-73.0227));
            animator.move(rightArmUnder, 2F, 0, 0);
            animator.rotate(leftArm, toRadians(14.8386), toRadians(7.4477), toRadians(-35.1649));
            animator.rotate(leftLeg, toRadians(8.65), toRadians(-1.19), toRadians(-7.75));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(27.5), 0, 0);
            animator.rotate(rightLeg, toRadians(-77.4629), toRadians(-4.3079), toRadians(6.1409));
            animator.move(rightLeg, 0, 0, -3F);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(11.6), toRadians(-3.05), toRadians(4.47));
            animator.move(axe, -1F, 2F, -12F);
            animator.endKeyframe();

            animator.startKeyframe(1);//0.6
            animator.rotate(head, toRadians(-3.46), toRadians(-4.41), toRadians(-3.44));
            animator.rotate(root, toRadians(25), toRadians(-5), 0);
            animator.move(root, 0, 0, -7.7F);
            animator.rotate(upper, toRadians(-7.9093), toRadians(4.2341), toRadians(3.892));
            animator.rotate(rightArm, toRadians(-86.5983), toRadians(11.7647), toRadians(-111.8854));
            animator.move(rightArm, -1F, 0, -5F);
            animator.rotate(rightArmUnder, toRadians(85.64), toRadians(-93.7), toRadians(-94.68));
            animator.move(rightArmUnder, 2F, 0, 0);
            animator.rotate(leftArm, toRadians(38.722), toRadians(3.4119), toRadians(-20.6452));
            animator.rotate(leftArmUnder, toRadians(-8), 0, 0);
            animator.rotate(leftLeg, toRadians(11.15), toRadians(-1.19), toRadians(-7.75));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(27.5), 0, 0);
            animator.rotate(rightLeg, toRadians(-52.46), toRadians(-4.31), toRadians(6.14));
            animator.move(rightLeg, 0, 0, -3F);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(21.6), toRadians(-3.05), toRadians(4.47));
            animator.move(axe, -1F, 2F, -12F);
            animator.endKeyframe();

            animator.startKeyframe(1);//0.68
            animator.rotate(head, toRadians(-15.96), toRadians(-4.41), toRadians(-3.44));
            animator.rotate(root, toRadians(30), toRadians(-5), 0);
            animator.move(root, 0, 0, -8F);
            animator.rotate(upper, toRadians(-2.9093), toRadians(4.2341), toRadians(3.892));
            animator.rotate(rightArm, toRadians(-61.404), toRadians(54.2968), toRadians(-66.9833));
            animator.move(rightArm, -2F, 0, -2F);
            animator.rotate(rightArmUnder, toRadians(129.7221), toRadians(-100.3294), toRadians(-116.3376));
            animator.move(rightArmUnder, 2F, 0, 0);
            animator.rotate(leftArm, toRadians(43.8283), toRadians(-1.291), toRadians(-14.8035));
            animator.rotate(leftArmUnder, toRadians(-20), 0, 0);
            animator.rotate(leftLeg, toRadians(11.15), toRadians(-1.19), toRadians(-7.75));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(27.5), 0, 0);
            animator.rotate(rightLeg, toRadians(-59.96), toRadians(-4.31), toRadians(6.14));
            animator.move(rightLeg, 0, 0, -3F);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(21.6), toRadians(-3.05), toRadians(4.47));
            animator.move(axe, -1F, 2F, -12F);
            animator.endKeyframe();

            animator.setStaticKeyframe(10);//1.2

            animator.startKeyframe(10);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
        } else if (animator.setAnimation(EntityNamelessGuardian.SMASH_ATTACK_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(13);//0.64
            animator.rotate(head, toRadians(12.8), 0, 0);
            animator.rotate(root, toRadians(3.2), 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-35.5), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, toRadians(-35.5), 0, toRadians(-10));
            animator.rotate(leftArmUnder, toRadians(-15), 0, toRadians(25));
            animator.rotate(leftLeg, toRadians(5), 0, 0);
            animator.rotate(leftLegUnder, toRadians(-10), 0, 0);
            animator.rotate(rightLeg, toRadians(-70), 0, toRadians(3.2));
            animator.move(rightLeg, 0, 0, -2);
            animator.rotate(rightLegUnder, toRadians(51), 0, 0);
            animator.move(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.96
            animator.rotate(head, toRadians(20), 0, 0);
            animator.rotate(root, toRadians(5), 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-15), 0, toRadians(5));
            animator.rotate(rightArm, toRadians(-35.5), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, toRadians(-35.5), 0, toRadians(-10));
            animator.rotate(leftArmUnder, toRadians(-15), 0, toRadians(25));
            animator.rotate(leftLeg, toRadians(5), 0, 0);
            animator.rotate(leftLegUnder, toRadians(-10), 0, 0);
            animator.rotate(rightLeg, toRadians(-110), 0, toRadians(5));
            animator.move(rightLeg, 0, 0, -3);
            animator.rotate(rightLegUnder, toRadians(80), 0, 0);
            animator.move(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(3);//1.12
            animator.rotate(head, toRadians(10), 0, 0);
            animator.rotate(root, toRadians(5), 0, 0);
            animator.move(root, 0, 1.5F, 0);
            animator.rotate(upper, toRadians(5), 0, toRadians(5));
            animator.rotate(rightArm, toRadians(-25), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, toRadians(17.5), 0, 0);
            animator.rotate(leftArmUnder, toRadians(-15), 0, toRadians(25));
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.move(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(1);//1.12+1
            animator.rotate(head, toRadians(10), 0, 0);
            animator.rotate(root, toRadians(5), 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(5), 0, toRadians(5));
            animator.rotate(rightArm, toRadians(-25), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, toRadians(17.5), 0, 0);
            animator.rotate(leftArmUnder, toRadians(-15), 0, toRadians(25));
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.move(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(5);//1.44
            animator.rotate(head, toRadians(-10), 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.move(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(5);//1.68
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.move(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(7);
        } else if (animator.setAnimation(EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_1)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(8);
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(30), toRadians(-10), toRadians(-8));
            animator.rotate(rightArm, toRadians(-48), toRadians(12), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-28), toRadians(-8), 0);
            animator.rotate(leftArm, toRadians(-44), toRadians(8), 0);
            animator.rotate(leftArmUnder, toRadians(-12), toRadians(8), 0);
            animator.rotate(leftLeg, toRadians(-40), 0, 0);
            animator.rotate(leftLegUnder, toRadians(50), 0, 0);
            animator.rotate(rightLeg, toRadians(40), 0, 0);
            animator.rotate(rightLegUnder, toRadians(10), 0, 0);
            animator.rotate(axe, toRadians(65), toRadians(-40), toRadians(-67));
            animator.move(axe, 8, 0, -0.8F);
            animator.endKeyframe();

            animator.startKeyframe(3);
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(30), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-40), 0, 0);
            animator.rotate(leftLegUnder, toRadians(50), 0, 0);
            animator.rotate(rightLeg, toRadians(40), 0, 0);
            animator.rotate(rightLegUnder, toRadians(10), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(5);
        } else if (animator.setAnimation(EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_2)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(30), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-40), 0, 0);
            animator.rotate(leftLegUnder, toRadians(50), 0, 0);
            animator.rotate(rightLeg, toRadians(40), 0, 0);
            animator.rotate(rightLegUnder, toRadians(10), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.32
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(40), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(50), 0, 0);
            animator.rotate(leftLegUnder, toRadians(30), 0, 0);
            animator.rotate(rightLeg, toRadians(-60), 0, 0);
            animator.rotate(rightLegUnder, toRadians(40), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(1);//0.32+1
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 2, 0);
            animator.rotate(upper, toRadians(40), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(50), 0, 0);
            animator.rotate(leftLegUnder, toRadians(30), 0, 0);
            animator.rotate(rightLeg, toRadians(-60), 0, 0);
            animator.rotate(rightLegUnder, toRadians(40), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(5);//0.64
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(30), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-60), 0, 0);
            animator.rotate(leftLegUnder, toRadians(40), 0, 0);
            animator.rotate(rightLeg, toRadians(50), 0, 0);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(1);//0.64+1
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 2, 0);
            animator.rotate(upper, toRadians(30), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-60), 0, 0);
            animator.rotate(leftLegUnder, toRadians(40), 0, 0);
            animator.rotate(rightLeg, toRadians(50), 0, 0);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(5);//0.96
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(40), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(50), 0, 0);
            animator.rotate(leftLegUnder, toRadians(30), 0, 0);
            animator.rotate(rightLeg, toRadians(-60), 0, 0);
            animator.rotate(rightLegUnder, toRadians(40), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(1);//0.96+1
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 2, 0);
            animator.rotate(upper, toRadians(40), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(50), 0, 0);
            animator.rotate(leftLegUnder, toRadians(30), 0, 0);
            animator.rotate(rightLeg, toRadians(-60), 0, 0);
            animator.rotate(rightLegUnder, toRadians(40), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(5);//1.28
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(30), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-60), 0, 0);
            animator.rotate(leftLegUnder, toRadians(40), 0, 0);
            animator.rotate(rightLeg, toRadians(50), 0, 0);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(1);//1.28+1
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 2, 0);
            animator.rotate(upper, toRadians(30), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-60), 0, 0);
            animator.rotate(leftLegUnder, toRadians(40), 0, 0);
            animator.rotate(rightLeg, toRadians(50), 0, 0);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(2);//1.4
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(35), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-41), 0, 0);
            animator.rotate(leftLegUnder, toRadians(38), 0, 0);
            animator.rotate(rightLeg, toRadians(23), 0, 0);
            animator.rotate(rightLegUnder, toRadians(15), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);
        } else if (animator.setAnimation(EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_3)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(-20), toRadians(10), 0);
            animator.rotate(root, toRadians(5), toRadians(-5), toRadians(-2));
            animator.move(root, 0, 1, 0);
            animator.rotate(upper, toRadians(36), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(rightArmUnder, toRadians(-15), toRadians(-10), 0);
            animator.rotate(leftArm, toRadians(-52), toRadians(10), 0);
            animator.rotate(leftArmUnder, toRadians(-15), toRadians(10), 0);
            animator.rotate(leftLeg, toRadians(-41), 0, 0);
            animator.rotate(leftLegUnder, toRadians(38), 0, 0);
            animator.rotate(rightLeg, toRadians(23), 0, 0);
            animator.rotate(rightLegUnder, toRadians(15), 0, 0);
            animator.rotate(axe, toRadians(78), toRadians(-46), toRadians(-84));
            animator.move(axe, 10, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(18);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, toRadians(2), 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
        } else if (animator.setAnimation(EntityNamelessGuardian.LEAP_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(11);//0.72
            animator.rotate(head, toRadians(-22.5), 0, 0);
            animator.rotate(root, toRadians(10), 0, 0);
            animator.move(root, 0, 3, 0);
            animator.rotate(body, toRadians(-5), 0, 0);
            animator.rotate(upper, toRadians(25), 0, 0);
            animator.rotate(rightArm, toRadians(82), toRadians(-1.7), toRadians(5));
            animator.rotate(rightArmUnder, toRadians(-17.5), 0, 0);
            animator.rotate(leftArm, toRadians(82), toRadians(1.7), toRadians(-5));
            animator.rotate(leftArmUnder, toRadians(-17.5), 0, 0);
            animator.rotate(leftLeg, toRadians(45), 0, 0);
            animator.rotate(leftLegUnder, toRadians(20), 0, 0);
            animator.rotate(rightLeg, toRadians(-45), 0, 0);
            //animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(25), 0, 0);
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(4);//1.04
            animator.rotate(head, toRadians(-32.5), 0, 0);
            animator.rotate(root, toRadians(10), 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, toRadians(-5), 0, 0);
            animator.rotate(upper, toRadians(25), 0, 0);
            animator.rotate(rightArm, toRadians(-120), toRadians(-1.7), toRadians(5));
            animator.rotate(rightArmUnder, toRadians(-17.5), 0, 0);
            animator.rotate(leftArm, toRadians(-120), toRadians(1.7), toRadians(-5));
            animator.rotate(leftArmUnder, toRadians(-17.5), 0, 0);
            animator.rotate(leftLeg, toRadians(45), 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(20), 0, 0);
            animator.rotate(rightLeg, toRadians(-45), 0, 0);
            animator.rotate(rightLegUnder, toRadians(30), 0, 0);
            animator.rotate(axe, toRadians(25), 0, 0);
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(14);//1.76
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, toRadians(-25), 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, toRadians(-5), 0, 0);
            animator.rotate(upper, toRadians(20), 0, 0);
            animator.rotate(rightArm, toRadians(-100), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-34), toRadians(-24), toRadians(17));
            animator.rotate(leftArm, toRadians(-80), 0, 0);
            animator.rotate(leftArmUnder, toRadians(-35), toRadians(20), toRadians(-17));
            animator.rotate(leftLeg, toRadians(-50), 0, toRadians(-10));
            animator.rotate(leftLegUnder, toRadians(60), 0, 0);
            animator.rotate(rightLeg, toRadians(-50), 0, toRadians(10));
            animator.rotate(rightLegUnder, toRadians(60), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(79);
        } else if (animator.setAnimation(EntityNamelessGuardian.SMASH_DOWN_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, toRadians(-25), 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, toRadians(-5), 0, 0);
            animator.rotate(upper, toRadians(20), 0, 0);
            animator.rotate(rightArm, toRadians(-100), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-34), toRadians(-24), toRadians(17));
            animator.rotate(leftArm, toRadians(-80), 0, 0);
            animator.rotate(leftArmUnder, toRadians(-35), toRadians(20), toRadians(-17));
            animator.rotate(leftLeg, toRadians(-50), 0, toRadians(-10));
            animator.rotate(leftLegUnder, toRadians(60), 0, 0);
            animator.rotate(rightLeg, toRadians(-50), 0, toRadians(10));
            animator.rotate(rightLegUnder, toRadians(60), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.28
            animator.rotate(head, toRadians(5), 0, 0);
            animator.rotate(root, toRadians(-17), 0, 0);
            animator.move(root, 0, 1, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, toRadians(55), 0, 0);
            animator.rotate(rightArm, toRadians(-19), toRadians(13), toRadians(-1));
            animator.rotate(rightArmUnder, toRadians(-10), toRadians(-12), toRadians(17));
            animator.rotate(leftArm, toRadians(-20), 0, 0);
            animator.rotate(leftArmUnder, toRadians(-23), toRadians(13), toRadians(-7));
            animator.rotate(leftLeg, toRadians(-25), 0, toRadians(-7.5));
            animator.rotate(leftLegUnder, toRadians(40), 0, 0);
            animator.rotate(rightLeg, toRadians(-25), 0, toRadians(7.5));
            animator.rotate(rightLegUnder, toRadians(40), 0, 0);
            animator.rotate(axe, toRadians(1), toRadians(-11), toRadians(2));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.56
            animator.rotate(head, toRadians(-15), 0, 0);
            animator.rotate(root, toRadians(-8), 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, toRadians(20), 0, 0);
            animator.rotate(rightArm, toRadians(9), toRadians(27), toRadians(-2.5));
            animator.rotate(rightArmUnder, toRadians(-52), toRadians(-6), toRadians(8));
            animator.rotate(leftArm, toRadians(40), 0, 0);
            animator.rotate(leftArmUnder, toRadians(-12), toRadians(7), toRadians(-3));
            animator.rotate(leftLeg, 0, 0, toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(20), 0, 0);
            animator.rotate(rightLeg, 0, 0, toRadians(5));
            animator.rotate(rightLegUnder, toRadians(20), 0, 0);
            animator.rotate(axe, toRadians(4), toRadians(-12), toRadians(-15));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.84
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(3);
        } else if (animator.setAnimation(EntityNamelessGuardian.LASER_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(11);//0.56
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 2.91F, 0);
            animator.rotate(head, toRadians(27.76), 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, toRadians(-35.67), 0, 0);
            animator.rotate(body, toRadians(2.91), 0, 0);
            animator.rotate(rightArm, toRadians(17.65), toRadians(10.43), toRadians(18.74));
            animator.move(rightArm, 0, 0, -5F);
            animator.rotate(rightArmUnder, toRadians(-48.38), 0, 0);
            animator.rotate(leftArm, toRadians(31.02), 0, toRadians(-14.14));
            animator.move(leftArm, 0, 0, -5F);
            animator.rotate(leftArmUnder, toRadians(-26.22), 0, 0);
            animator.rotate(leftLeg, toRadians(29.14), 0, 0);
            animator.rotate(leftLegUnder, toRadians(44.66), 0, 0);
            animator.rotate(rightLeg, toRadians(-23.31), 0, 0);
            animator.rotate(rightLegUnder, toRadians(20.4), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(10);//1.08
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 5F, 0);
            animator.rotate(head, toRadians(-15), 0, 0);
            animator.move(head, 0, 0, -3F);
            animator.rotate(upper, toRadians(12), 0, 0);
            animator.rotate(body, toRadians(5), 0, 0);
            animator.rotate(rightArm, toRadians(52.5), 0, toRadians(15));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-15), 0, 0);
            animator.rotate(leftArm, toRadians(52.5), 0, toRadians(-25));
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, toRadians(-45), 0, 0);
            animator.rotate(leftLeg, toRadians(50), 0, 0);
            animator.rotate(leftLegUnder, toRadians(20), 0, 0);
            animator.rotate(rightLeg, toRadians(-40), 0, 0);
            animator.rotate(rightLegUnder, toRadians(35), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(75);//4.84

            animator.startKeyframe(10);//5.44
            animator.rotate(root, toRadians(0.15), 0, toRadians(-0.15));
            animator.move(root, 0, 1.74F, 0);
            animator.rotate(head, toRadians(-15), 0, 0);
            animator.move(head, 0, 0, -1.04F);
            animator.rotate(upper, toRadians(5.61), 0, 0);
            animator.rotate(body, toRadians(1.74), 0, 0);
            animator.rotate(rightArm, toRadians(-1.04), toRadians(16.3), toRadians(21.78));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, 0, 0, 0);
            animator.rotate(leftArm, toRadians(18.01), 0, toRadians(-8.95));
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, toRadians(-15.66), 0, 0);
            animator.rotate(leftLeg, toRadians(17.4), 0, 0);
            animator.rotate(leftLegUnder, toRadians(41.96), 0, 0);
            animator.rotate(rightLeg, toRadians(-13.92), 0, 0);
            animator.rotate(rightLegUnder, toRadians(12.18), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(10);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(body, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.move(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(4);
        } else if (animator.setAnimation(EntityNamelessGuardian.ROAR_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(lower, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(24);//1.2
            animator.rotate(head, toRadians(20), 0, 0);
            animator.rotate(root, toRadians(2.5), 0, 0);
            animator.rotate(upper, toRadians(22.5), 0, 0);
            animator.move(upper, 0, 1, 0);
            animator.rotate(rightArm, toRadians(-10), toRadians(-22), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-10), toRadians(-10));
            animator.rotate(leftArm, toRadians(-10), toRadians(22), toRadians(-4));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(10), toRadians(10));
            animator.rotate(lower, 0, 0, 0);
            animator.rotate(leftLeg, 0, toRadians(-5), toRadians(-5));
            animator.move(leftLeg, 1F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, 0, toRadians(5), toRadians(5));
            animator.move(rightLeg, -1F, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, 0, 0, 0);
            animator.move(axe, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);//1.4
            animator.rotate(head, toRadians(30), 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-2.5), 0, 0);
            animator.move(upper, 0, -1, 0);
            animator.rotate(rightArm, toRadians(-77), toRadians(4), toRadians(12));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-20), toRadians(-10));
            animator.rotate(leftArm, toRadians(-77), toRadians(-4), toRadians(-12));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(20), toRadians(10));
            animator.rotate(lower, 0, toRadians(2.5), 0);
            animator.rotate(leftLeg, 0, toRadians(-7.5), toRadians(-7.5));
            animator.move(leftLeg, 0.5F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(15), toRadians(7.5), toRadians(7.5));
            animator.move(rightLeg, -0.5F, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(10), 0, 0);
            animator.move(axe, 0, -1, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);//1.6
            animator.rotate(head, toRadians(20), toRadians(-10), 0);
            animator.rotate(root, toRadians(-2.5), 0, 0);
            animator.rotate(upper, toRadians(-22.5), toRadians(10), 0);
            animator.move(upper, 0, 1, 0);
            animator.rotate(rightArm, toRadians(-29), toRadians(30), toRadians(19));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-30), toRadians(-10));
            animator.rotate(leftArm, toRadians(-29), toRadians(-30), toRadians(-19));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(30), toRadians(10));
            animator.rotate(lower, 0, toRadians(5), 0);
            animator.rotate(leftLeg, 0, toRadians(-10), toRadians(-10));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(30), toRadians(10), toRadians(10));
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(20), 0, 0);
            animator.move(axe, 0, 2, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(8);//2

            animator.startKeyframe(4);//2.2
            animator.rotate(head, toRadians(7.5), 0, 0);
            animator.rotate(root, toRadians(-6.25), 0, 0);
            animator.rotate(upper, toRadians(5), toRadians(-5), 0);
            animator.move(upper, 0, -1, 0);
            animator.rotate(rightArm, toRadians(-40.5), toRadians(16), toRadians(5));
            animator.rotate(rightArmUnder, toRadians(-48.75), toRadians(-30), toRadians(-10));
            animator.rotate(leftArm, toRadians(-40.5), toRadians(-16), toRadians(-5));
            animator.rotate(leftArmUnder, toRadians(-48.75), toRadians(30), toRadians(10));
            animator.rotate(lower, 0, toRadians(-7.5), 0);
            animator.rotate(leftLeg, toRadians(2.5), toRadians(-10), toRadians(-10));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(-22.5), toRadians(12.5), toRadians(10));
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(32.5), 0, 0);
            animator.rotate(axe, toRadians(20), 0, 0);
            animator.move(axe, 0, 2, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);//2.4
            animator.rotate(head, toRadians(-5), toRadians(10), 0);
            animator.rotate(root, toRadians(15), 0, 0);
            animator.rotate(upper, toRadians(-10), toRadians(-10), 0);
            animator.move(upper, 0, 1, 0);
            animator.rotate(rightArm, toRadians(-1.7), toRadians(30), toRadians(19));
            animator.rotate(rightArmUnder, toRadians(-25), toRadians(-30), toRadians(-10));
            animator.rotate(leftArm, toRadians(-1.7), toRadians(-30), toRadians(-19));
            animator.rotate(leftArmUnder, toRadians(-25), toRadians(30), toRadians(10));
            animator.rotate(lower, 0, toRadians(-20), 0);
            animator.rotate(leftLeg, toRadians(5), toRadians(-10), toRadians(-10));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(-30), toRadians(15), toRadians(10));
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(25), 0, 0);
            animator.rotate(axe, toRadians(20), 0, 0);
            animator.move(axe, 0, 2, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(27);//3.8

            animator.startKeyframe(5);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(lower, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
        } else if (animator.setAnimation(EntityNamelessGuardian.CONCUSSION_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(lower, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(head, toRadians(30), 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-2.5), 0, 0);
            animator.move(upper, 0, -1, 0);
            animator.rotate(rightArm, toRadians(-77), toRadians(4), toRadians(12));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-20), toRadians(-10));
            animator.rotate(leftArm, toRadians(-77), toRadians(-4), toRadians(-12));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(20), toRadians(10));
            animator.rotate(lower, 0, toRadians(2.5), 0);
            animator.rotate(leftLeg, 0, toRadians(-7.5), toRadians(-7.5));
            animator.move(leftLeg, 0.5F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(15), toRadians(7.5), toRadians(7.5));
            animator.move(rightLeg, -0.5F, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(10), 0, 0);
            animator.move(axe, 0, -1, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(head, toRadians(20), toRadians(-10), 0);
            animator.rotate(root, toRadians(-2.5), 0, 0);
            animator.rotate(upper, toRadians(-22.5), toRadians(10), 0);
            animator.move(upper, 0, 1, 0);
            animator.rotate(rightArm, toRadians(-29), toRadians(30), toRadians(19));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-30), toRadians(-10));
            animator.rotate(leftArm, toRadians(-29), toRadians(-30), toRadians(-19));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(30), toRadians(10));
            animator.rotate(lower, 0, toRadians(5), 0);
            animator.rotate(leftLeg, 0, toRadians(-10), toRadians(-10));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(30), toRadians(10), toRadians(10));
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(20), 0, 0);
            animator.move(axe, 0, 2, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(8);

            animator.resetKeyframe(8);
        } else if (animator.setAnimation(EntityNamelessGuardian.ROBUST_ATTACK_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(11);//0.56
            animator.move(root, 0, 0, 0);
            animator.rotate(head, toRadians(-12.5), 0, 0);
            animator.move(head, 0, 0, -0.5F);
            animator.rotate(upper, toRadians(9.95), 0, toRadians(-2.7));
            animator.move(upper, 0, 0, 0);
            animator.rotate(body, toRadians(2.5), toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-20), toRadians(5), 0);
            animator.move(rightArm, 0, 0, -3);
            animator.rotate(rightArmUnder, toRadians(-58.91), toRadians(22.75), toRadians(-91.2));
            animator.move(rightArmUnder, 0, 2, 0);
            animator.rotate(leftArm, toRadians(-14), toRadians(26), toRadians(-8.7));
            animator.move(leftArm, 2, 0, -3);
            animator.rotate(leftArmUnder, toRadians(-53), toRadians(13.4), toRadians(-0.3));
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.4), toRadians(-0.08), toRadians(18.7));
            animator.move(axe, 0, -1F, 10F);
            animator.endKeyframe();

            animator.startKeyframe(6);//0.88
            animator.move(root, 0, 0, 0);
            animator.rotate(head, toRadians(-7.5), 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, toRadians(20), 0, toRadians(-2.85));
            animator.move(upper, 0, 0, 0);
            animator.rotate(body, 0, toRadians(-10), 0);
            animator.rotate(rightArm, toRadians(-20), toRadians(5), 0);
            animator.move(rightArm, 0, 0, -3);
            animator.rotate(rightArmUnder, toRadians(-58.91), toRadians(22.75), toRadians(-91.2));
            animator.move(rightArmUnder, 0, 2, 0);
            animator.rotate(leftArm, toRadians(-14), toRadians(26), toRadians(-8.7));
            animator.move(leftArm, 2, 0, -3);
            animator.rotate(leftArmUnder, toRadians(-53), toRadians(13.4), toRadians(-0.3));
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.4), toRadians(-0.08), toRadians(18.7));
            animator.move(axe, 0, -1F, 10F);
            animator.endKeyframe();

            animator.startKeyframe(9);//1.28
            animator.move(root, 0, 0, 0);
            animator.rotate(head, toRadians(-4.4), 0, 0);
            animator.move(head, 0, -2, 6F);
            animator.rotate(upper, toRadians(-15.6), 0, toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(body, toRadians(-15.22), toRadians(11.91), toRadians(-0.79));
            animator.rotate(rightArm, toRadians(-135.1951), toRadians(8.199), toRadians(38.8199));
            animator.move(rightArm, 1, -4, -10F);
            animator.rotate(rightArmUnder, toRadians(-23.3166), toRadians(13.8138), toRadians(-39.0883));
            animator.move(rightArmUnder, 0, 2, 0);
            animator.rotate(leftArm, toRadians(-85.9648), toRadians(29.1755), toRadians(12.1829));
            animator.move(leftArm, 3, -4, -11F);
            animator.rotate(leftArmUnder, toRadians(-15.8352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(leftLeg, toRadians(-23.53), 0, toRadians(-2.94));
            animator.rotate(leftLegUnder, toRadians(13.23), 0, 0);
            animator.rotate(rightLeg, toRadians(10.3), 0, 0);
            animator.rotate(rightLegUnder, toRadians(-4.41), 0, 0);
            animator.rotate(axe, toRadians(6.1688), toRadians(0.0843), toRadians(36.2873));
            animator.move(axe, 0, -0.41F, -2.94F);
            animator.endKeyframe();

            animator.startKeyframe(6);//1.56
            animator.move(root, 0, 0, 0);
            animator.rotate(head, toRadians(22.5), 0, 0);
            animator.move(head, 0, -2, 6F);
            animator.rotate(upper, toRadians(-20.6), 0, toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(body, toRadians(-15.37), toRadians(10.69), toRadians(-0.29));
            animator.rotate(rightArm, toRadians(-132.3443), toRadians(26.9421), toRadians(55.746));
            animator.move(rightArm, 1, -4, -10F);
            animator.rotate(rightArmUnder, toRadians(-43.6176), toRadians(20.4524), toRadians(-34.9621));
            animator.move(rightArmUnder, 0, 2, 0);
            animator.rotate(leftArm, toRadians(-84.5118), toRadians(23.7862), toRadians(-2.2991));
            animator.move(leftArm, 2, -5, -11F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(leftLeg, toRadians(-40), 0, toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(22.5), 0, 0);
            animator.rotate(rightLeg, toRadians(17.5), 0, 0);
            animator.rotate(rightLegUnder, toRadians(-7.5), 0, 0);
            animator.rotate(axe, toRadians(7.3267), toRadians(-9.2541), toRadians(41.4075));
            animator.move(axe, 0, 0F, 5F);
            animator.endKeyframe();

            animator.startKeyframe(2);//1.64
            animator.move(root, 0, 0.5F, -2.5F);
            animator.rotate(head, toRadians(-6.25), 0, 0);
            animator.move(head, 0, -2, 6F);
            animator.rotate(upper, toRadians(22.2), 0, toRadians(-1.2));
            animator.move(upper, 0, 0, 0);
            animator.rotate(body, toRadians(-15.41), toRadians(10.35), toRadians(-0.15));
            animator.rotate(rightArm, toRadians(-88.12), toRadians(-20.2537), toRadians(32.2194));
            animator.move(rightArm, 1.5F, -2.5F, -7.5F);
            animator.rotate(rightArmUnder, toRadians(-20.59), toRadians(0.61), toRadians(-17.48));
            animator.move(rightArmUnder, 0, 2, 0);
            animator.rotate(leftArm, toRadians(-79.68), toRadians(27.97), toRadians(-14.05));
            animator.move(leftArm, 2.5F, -1, -7.5F);
            animator.rotate(leftArmUnder, toRadians(-33.34), toRadians(14.21), toRadians(24.05));
            animator.rotate(leftLeg, toRadians(-20), 0, toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(21.25), 0, 0);
            animator.rotate(rightLeg, toRadians(26.25), toRadians(-5), 0);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(13.0372), toRadians(8.7962), toRadians(21.3868));
            animator.move(axe, 1.5F, 0F, -3.5F);
            animator.endKeyframe();

            animator.startKeyframe(1);//1.72
            animator.move(root, 0, 1F, -5F);
            animator.rotate(head, toRadians(-35), 0, 0);
            animator.move(head, 0, -2, 6F);
            animator.rotate(upper, toRadians(65), 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(body, toRadians(-15.45), toRadians(10), 0);
            animator.rotate(rightArm, toRadians(-48.9725), toRadians(-46.6899), toRadians(5.7718));
            animator.move(rightArm, 2F, -1F, -5F);
            animator.rotate(rightArmUnder, toRadians(2.4345), toRadians(-19.2257), 0);
            animator.move(rightArmUnder, 0, 2, 0);
            animator.rotate(leftArm, toRadians(-49.678), toRadians(27.9707), toRadians(-14.0486));
            animator.move(leftArm, 2F, 2, -4F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(leftLeg, toRadians(-20), 0, toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(20), 0, 0);
            animator.rotate(rightLeg, toRadians(35), toRadians(-10), 0);
            animator.rotate(rightLegUnder, toRadians(22.5), 0, 0);
            animator.rotate(axe, toRadians(19.7914), toRadians(22.607), toRadians(30.2137));
            animator.move(axe, -5F, 0F, -16F);
            animator.endKeyframe();

            animator.setStaticKeyframe(20);//2.08

            animator.startKeyframe(8);//2.72
            animator.move(root, 0, 0.36F, -1.8F);
            animator.rotate(head, toRadians(-17.5), 0, 0);
            animator.move(head, 0, -0.67F, 2F);
            animator.rotate(upper, toRadians(21.67), 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(body, toRadians(-5.56), toRadians(3.6), 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-59.92), toRadians(-6.92), 0);
            animator.move(rightArmUnder, 0, -0.72F, 0);
            animator.rotate(leftArm, toRadians(-17.88), toRadians(10.07), toRadians(-5.06));
            animator.move(leftArm, 0.72F, 0.72F, -1.44F);
            animator.rotate(leftArmUnder, toRadians(-12), toRadians(5.12), toRadians(8.66));
            animator.rotate(leftLeg, toRadians(-7.2), 0, toRadians(-1.8));
            animator.rotate(leftLegUnder, toRadians(7.2), 0, 0);
            animator.rotate(rightLeg, toRadians(16.58), toRadians(-4.74), toRadians(4.74));
            animator.rotate(rightLegUnder, toRadians(8.1), 0, 0);
            animator.rotate(axe, toRadians(25.04), toRadians(-2.56), toRadians(19.03));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(8);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(11);
        } else if (animator.setAnimation(EntityNamelessGuardian.SHAKE_GROUND_ATTACK_ANIMATION_1)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(5);//0.28
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, -1F, 0);
            animator.rotate(head, toRadians(-5), 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-20), toRadians(5), 0);
            animator.move(rightArm, 0, 0, -3F);
            animator.rotate(rightArmUnder, toRadians(-58.9055), toRadians(22.7504), toRadians(-91.2651));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(5.1146), toRadians(-0.0838), toRadians(18.7012));
            animator.move(axe, 0, -1F, 10F);
            animator.rotate(leftArm, toRadians(-13.3885), toRadians(16.4137), toRadians(-6.1549));
            animator.move(leftArm, 2F, 0, -3F);
            animator.rotate(leftArmUnder, toRadians(-53.4395), toRadians(13.4075), toRadians(-0.3009));
            animator.rotate(rightLeg, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(leftLegUnder, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(0), toRadians(-10), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(3);//0.44
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(9.95), toRadians(0), toRadians(-2.7));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-12.5), 0, 0);
            animator.move(head, 0, 0, -0.5F);
            animator.rotate(rightArm, toRadians(-20), toRadians(5), 0);
            animator.move(rightArm, 0, 0, -3F);
            animator.rotate(rightArmUnder, toRadians(-58.91), toRadians(22.75), toRadians(-91.27));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(-2.39), toRadians(-0.08), toRadians(18.7));
            animator.move(axe, 0, -1F, 10F);
            animator.rotate(leftArm, toRadians(-14.3245), toRadians(26.1207), toRadians(-8.717));
            animator.move(leftArm, 2F, 2F, -3F);
            animator.rotate(leftArmUnder, toRadians(-53.44), toRadians(13.41), toRadians(-0.3));
            animator.rotate(rightLeg, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(leftLegUnder, toRadians(0), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(2.5), toRadians(-10), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(3);//0.6
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(6.89), toRadians(0), toRadians(-2.66));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-18.45), 0, 0);
            animator.move(head, 0, -1F, 1.5F);
            animator.rotate(rightArm, toRadians(-57.29), toRadians(5), toRadians(2.14));
            animator.move(rightArm, 0, 1.14F, 0.43F);
            animator.rotate(rightArmUnder, toRadians(-44.5), toRadians(20.52), toRadians(-70.56));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(0.17), toRadians(1.21), toRadians(18.76));
            animator.move(axe, 0, -0.82F, 9.12F);
            animator.rotate(leftArm, toRadians(-25.52), toRadians(31.35), toRadians(1.05));
            animator.move(leftArm, 2F, 1F, -3.86F);
            animator.rotate(leftArmUnder, toRadians(-36.33), toRadians(8.57), toRadians(1.93));
            animator.rotate(rightLeg, toRadians(3.09), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-1.32), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-7.06), toRadians(0), toRadians(-0.88));
            animator.rotate(leftLegUnder, toRadians(3.97), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-6.49), toRadians(-0.39), toRadians(-0.43));
            animator.endKeyframe();

            animator.startKeyframe(4);//0.8
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-10.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-9.4), 0, 0);
            animator.move(head, 0, -1.5F, 6F);
            animator.rotate(rightArm, toRadians(-107), toRadians(5), toRadians(5));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25.2953), toRadians(17.5424), toRadians(-42.9633));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(3.5907), toRadians(2.9314), toRadians(18.8381));
            animator.move(axe, 0, -0.59F, 7.94F);
            animator.rotate(leftArm, toRadians(-71.7108), toRadians(36.5869), toRadians(10.8117));
            animator.move(leftArm, 2F, 1F, -9F);
            animator.rotate(leftArmUnder, toRadians(-13.5224), toRadians(2.116), toRadians(4.9081));
            animator.rotate(rightLeg, toRadians(7.21), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-3.09), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-16.47), toRadians(0), toRadians(-2.06));
            animator.rotate(leftLegUnder, toRadians(9.26), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.1518), toRadians(12.4347), toRadians(-0.9997));
            animator.endKeyframe();

            animator.startKeyframe(4);//1.0
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-15.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-4.4), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-135.1951), toRadians(8.199), toRadians(38.8199));
            animator.move(rightArm, 1F, -4F, -10F);
            animator.rotate(rightArmUnder, toRadians(-23.3166), toRadians(13.8138), toRadians(-39.0883));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(6.1688), toRadians(0.0843), toRadians(36.2873));
            animator.move(axe, 0, -0.41F, -2.94F);
            animator.rotate(leftArm, toRadians(-85.9648), toRadians(29.1755), toRadians(12.1829));
            animator.move(leftArm, 3F, -4F, -11F);
            animator.rotate(leftArmUnder, toRadians(-15.8352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(10.3), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-4.41), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-23.53), toRadians(0), toRadians(-2.94));
            animator.rotate(leftLegUnder, toRadians(13.23), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.22), toRadians(11.91), toRadians(-0.79));
            animator.endKeyframe();

            animator.startKeyframe(4);//1.2
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-20.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(22.5), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-132.3443), toRadians(26.9421), toRadians(55.746));
            animator.move(rightArm, 1F, -4F, -10F);
            animator.rotate(rightArmUnder, toRadians(-43.6176), toRadians(20.4524), toRadians(-34.9621));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(7.3267), toRadians(-9.2541), toRadians(41.4075));
            animator.move(axe, 2F, 0F, 5F);
            animator.rotate(leftArm, toRadians(-84.5118), toRadians(23.7862), toRadians(-2.299));
            animator.move(leftArm, 2F, -5F, -11F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(17.5), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-7.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-40), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(22.5), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.37), toRadians(10.69), toRadians(-0.29));
            animator.endKeyframe();

            animator.startKeyframe(1);//1.28
            animator.move(root, 0, 0.5F, -2.5F);
            animator.rotate(upper, toRadians(22.2), toRadians(0), toRadians(-1.2));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-6.25), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-88.12), toRadians(-20.2537), toRadians(32.2194));
            animator.move(rightArm, 1.5F, -2.5F, -7.5F);
            animator.rotate(rightArmUnder, toRadians(-20.59), toRadians(0.61), toRadians(-17.48));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(13.0372), toRadians(8.7962), toRadians(21.3868));
            animator.move(axe, 1.5F, 0F, -3.5F);
            animator.rotate(leftArm, toRadians(-78.4506), toRadians(37.7899), toRadians(-11.794));
            animator.move(leftArm, 2.5F, -1F, -7.5F);
            animator.rotate(leftArmUnder, toRadians(-33.34), toRadians(14.21), toRadians(24.05));
            animator.rotate(rightLeg, toRadians(26.25), toRadians(-5), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(7.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-20), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(21.25), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.41), toRadians(10.35), toRadians(-0.15));
            animator.endKeyframe();

            animator.startKeyframe(1);//1.36
            animator.move(root, 0, 1F, -5F);
            animator.rotate(upper, toRadians(65), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-35), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-48.9725), toRadians(-46.6899), toRadians(5.7718));
            animator.move(rightArm, 2F, -1F, -5F);
            animator.rotate(rightArmUnder, toRadians(2.4345), toRadians(-19.2257), toRadians(0));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(19.7914), toRadians(22.607), toRadians(30.2137));
            animator.move(axe, -5F, 0F, -16F);
            animator.rotate(leftArm, toRadians(-49.678), toRadians(27.9707), toRadians(-14.0486));
            animator.move(leftArm, 2F, 2F, -4F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(35), toRadians(-10), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(22.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-20), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(20), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.45), toRadians(10), toRadians(0));
            animator.endKeyframe();

            animator.setStaticKeyframe(13);//2.0

            animator.startKeyframe(10);//2.64
            animator.move(root, 0, 0.36F, -1.8F);
            animator.rotate(upper, toRadians(21.67), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-17.5), 0, 0);
            animator.move(head, 0, -0.67F, 2F);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-59.92), toRadians(-6.92), toRadians(0));
            animator.move(rightArmUnder, 0, 0.72F, 0);
            animator.rotate(axe, toRadians(25.04), toRadians(-2.56), toRadians(19.03));
            animator.move(axe, 0F, -1F, -5F);
            animator.rotate(leftArm, toRadians(-17.88), toRadians(10.07), toRadians(-5.06));
            animator.move(leftArm, 0.72F, 0.72F, -1.44F);
            animator.rotate(leftArmUnder, toRadians(-12), toRadians(5.12), toRadians(8.66));
            animator.rotate(rightLeg, toRadians(16.58), toRadians(-4.74), toRadians(4.74));
            animator.rotate(rightLegUnder, toRadians(8.1), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-7.2), toRadians(0), toRadians(-1.8));
            animator.rotate(leftLegUnder, toRadians(7.2), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-5.56), toRadians(3.6), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(7);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(5);
        } else if (animator.setAnimation(EntityNamelessGuardian.SHAKE_GROUND_ATTACK_ANIMATION_2)) {
            animator.startKeyframe(0);
            animator.move(root, 0, 1F, -5F);
            animator.rotate(upper, toRadians(65), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-35), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-48.9725), toRadians(-46.6899), toRadians(5.7718));
            animator.move(rightArm, 2F, -1F, -5F);
            animator.rotate(rightArmUnder, toRadians(2.4345), toRadians(-19.2257), toRadians(0));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(19.7914), toRadians(22.607), toRadians(30.2137));
            animator.move(axe, -5F, 0F, -16F);
            animator.rotate(leftArm, toRadians(-49.678), toRadians(27.9707), toRadians(-14.0486));
            animator.move(leftArm, 2F, 2F, -4F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(35), toRadians(-10), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(22.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-20), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(20), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.45), toRadians(10), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(5);//0.24
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(20), toRadians(0), toRadians(-2.85));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-7.5), 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-20), toRadians(5), 0);
            animator.move(rightArm, 0, 0, -3F);
            animator.rotate(rightArmUnder, toRadians(-58.9055), toRadians(22.7504), toRadians(-91.2651));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(-2.39), toRadians(-0.08), toRadians(18.7));
            animator.move(axe, 0, -1F, 10F);
            animator.rotate(leftArm, toRadians(-14.3245), toRadians(26.1207), toRadians(-8.717));
            animator.move(leftArm, 2F, 0, -3F);
            animator.rotate(leftArmUnder, toRadians(-53.4395), toRadians(13.4075), toRadians(-0.3009));
            animator.rotate(rightLeg, toRadians(12.85), toRadians(-4), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(8.21), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-7.74), toRadians(0), toRadians(-2.53));
            animator.rotate(leftLegUnder, toRadians(10.38), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(0), toRadians(-10), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(3);//0.4
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(6.89), toRadians(0), toRadians(-2.66));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-18.45), 0, 0);
            animator.move(head, 0, -1F, 1.5F);
            animator.rotate(rightArm, toRadians(-57.29), toRadians(5), toRadians(2.14));
            animator.move(rightArm, 0, 1.14F, 0.43F);
            animator.rotate(rightArmUnder, toRadians(-44.5), toRadians(20.52), toRadians(-70.56));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(0.17), toRadians(1.21), toRadians(18.76));
            animator.move(axe, 0, -0.82F, 9.12F);
            animator.rotate(leftArm, toRadians(-25.52), toRadians(31.35), toRadians(1.05));
            animator.move(leftArm, 2F, 1F, -3.86F);
            animator.rotate(leftArmUnder, toRadians(-36.33), toRadians(8.57), toRadians(1.93));
            animator.rotate(rightLeg, toRadians(-1.91), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-1.32), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(0.44), toRadians(0), toRadians(-0.88));
            animator.rotate(leftLegUnder, toRadians(3.97), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-6.49), toRadians(-0.39), toRadians(-0.43));
            animator.endKeyframe();

            animator.startKeyframe(4);//0.6
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-10.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-9.4), 0, 0);
            animator.move(head, 0, -1.5F, 6F);
            animator.rotate(rightArm, toRadians(-107), toRadians(5), toRadians(5));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25.2953), toRadians(17.5424), toRadians(-42.9633));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(3.5907), toRadians(2.9314), toRadians(18.8381));
            animator.move(axe, 0, -0.59F, 7.94F);
            animator.rotate(leftArm, toRadians(-71.7108), toRadians(36.5869), toRadians(10.8117));
            animator.move(leftArm, 2F, 1F, -9F);
            animator.rotate(leftArmUnder, toRadians(-13.5224), toRadians(2.116), toRadians(4.9081));
            animator.rotate(rightLeg, toRadians(-23.53), toRadians(0), toRadians(-2.94));
            animator.rotate(rightLegUnder, toRadians(13.23), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(10.3), toRadians(0), toRadians(-2.06));
            animator.rotate(leftLegUnder, toRadians(-4.41), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.1518), toRadians(12.4347), toRadians(-0.9997));
            animator.endKeyframe();

            animator.startKeyframe(2);//0.72
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-15.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-4.4), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-135.1951), toRadians(8.199), toRadians(38.8199));
            animator.move(rightArm, 1F, -4F, -10F);
            animator.rotate(rightArmUnder, toRadians(-23.3166), toRadians(13.8138), toRadians(-39.0883));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(6.1688), toRadians(0.0843), toRadians(36.2873));
            animator.move(axe, 0, -0.41F, -2.94F);
            animator.rotate(leftArm, toRadians(-85.9648), toRadians(29.1755), toRadians(12.1829));
            animator.move(leftArm, 3F, -4F, -11F);
            animator.rotate(leftArmUnder, toRadians(-15.8352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(-41.03), toRadians(0), toRadians(-2.94));
            animator.rotate(rightLegUnder, toRadians(13.23), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(10.3), toRadians(0), toRadians(0));
            animator.rotate(leftLegUnder, toRadians(-4.41), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.22), toRadians(11.91), toRadians(-0.79));
            animator.endKeyframe();

            animator.startKeyframe(2);//0.84
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-20.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(22.5), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-132.3443), toRadians(26.9421), toRadians(55.746));
            animator.move(rightArm, 1F, -4F, -10F);
            animator.rotate(rightArmUnder, toRadians(-43.6176), toRadians(20.4524), toRadians(-34.9621));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(7.3267), toRadians(-9.2541), toRadians(41.4075));
            animator.move(axe, 2F, 0F, 5F);
            animator.rotate(leftArm, toRadians(-84.5118), toRadians(23.7862), toRadians(-2.299));
            animator.move(leftArm, 2F, -5F, -11F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(-56.03), toRadians(0), toRadians(-2.94));
            animator.rotate(rightLegUnder, toRadians(22.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(17.5), toRadians(0), toRadians(0));
            animator.rotate(leftLegUnder, toRadians(-7.5), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.37), toRadians(10.69), toRadians(-0.29));
            animator.endKeyframe();

            animator.startKeyframe(1);//0.92
            animator.move(root, 0, 0.5F, -2.5F);
            animator.rotate(upper, toRadians(22.2), toRadians(0), toRadians(-1.2));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-6.25), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-88.12), toRadians(-20.2537), toRadians(32.2194));
            animator.move(rightArm, 1.5F, -2.5F, -7.5F);
            animator.rotate(rightArmUnder, toRadians(-20.59), toRadians(0.61), toRadians(-17.48));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(13.0372), toRadians(8.7962), toRadians(21.3868));
            animator.move(axe, 1.5F, 0F, -3.5F);
            animator.rotate(leftArm, toRadians(-78.4506), toRadians(37.7899), toRadians(-11.794));
            animator.move(leftArm, 2.5F, -1F, -7.5F);
            animator.rotate(leftArmUnder, toRadians(-33.34), toRadians(14.21), toRadians(24.05));
            animator.rotate(rightLeg, toRadians(-31.25), toRadians(-5), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(17.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(37.5), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(1.25), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.41), toRadians(10.35), toRadians(-0.15));
            animator.endKeyframe();

            animator.startKeyframe(1);//1.0
            animator.move(root, 0, 1F, -5F);
            animator.rotate(upper, toRadians(65), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-35), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-48.9725), toRadians(-46.6899), toRadians(5.7718));
            animator.move(rightArm, 2F, -1F, -5F);
            animator.rotate(rightArmUnder, toRadians(2.4345), toRadians(-19.2257), toRadians(0));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(19.7914), toRadians(22.607), toRadians(30.2137));
            animator.move(axe, -5F, 0F, -16F);
            animator.rotate(leftArm, toRadians(-49.678), toRadians(27.9707), toRadians(-14.0486));
            animator.move(leftArm, 2F, 2F, -4F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(-21.25), toRadians(-5), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(17.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(42.5), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(1.25), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.45), toRadians(10), toRadians(0));
            animator.endKeyframe();

            animator.setStaticKeyframe(18);//1.8

            animator.startKeyframe(12);//2.4
            animator.move(root, 0, 0.36F, -1.8F);
            animator.rotate(upper, toRadians(21.67), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-17.5), 0, 0);
            animator.move(head, 0, -0.67F, 2F);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-59.92), toRadians(-6.92), toRadians(0));
            animator.move(rightArmUnder, 0, 0.72F, 0);
            animator.rotate(axe, toRadians(25.04), toRadians(-2.56), toRadians(19.03));
            animator.move(axe, 0F, -1F, -5F);
            animator.rotate(leftArm, toRadians(-17.88), toRadians(10.07), toRadians(-5.06));
            animator.move(leftArm, 0.72F, 0.72F, -1.44F);
            animator.rotate(leftArmUnder, toRadians(-12), toRadians(5.12), toRadians(8.66));
            animator.rotate(rightLeg, toRadians(-8.42), toRadians(-4.74), toRadians(4.74));
            animator.rotate(rightLegUnder, toRadians(8.1), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(15.3), toRadians(0), toRadians(-1.8));
            animator.rotate(leftLegUnder, toRadians(27.2), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-5.56), toRadians(3.6), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(8);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
        } else if (animator.setAnimation(EntityNamelessGuardian.SHAKE_GROUND_ATTACK_ANIMATION_3)) {
            animator.startKeyframe(0);
            animator.move(root, 0, 1F, -5F);
            animator.rotate(upper, toRadians(65), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-35), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-48.9725), toRadians(-46.6899), toRadians(5.7718));
            animator.move(rightArm, 2F, -1F, -5F);
            animator.rotate(rightArmUnder, toRadians(2.4345), toRadians(-19.2257), toRadians(0));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(19.7914), toRadians(22.607), toRadians(30.2137));
            animator.move(axe, -5F, 0F, -16F);
            animator.rotate(leftArm, toRadians(-49.678), toRadians(27.9707), toRadians(-14.0486));
            animator.move(leftArm, 2F, 2F, -4F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(-21.25), toRadians(-5), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(17.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(42.5), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(1.25), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.45), toRadians(10), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(5);//0.24
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(6.89), toRadians(0), toRadians(-2.66));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-18.45), 0, 0);
            animator.move(head, 0, -1F, 1.5F);
            animator.rotate(rightArm, toRadians(-57.29), toRadians(5), toRadians(2.14));
            animator.move(rightArm, 0, 1.14F, 0.43F);
            animator.rotate(rightArmUnder, toRadians(-44.5), toRadians(20.52), toRadians(-70.56));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(0.17), toRadians(1.21), toRadians(18.76));
            animator.move(axe, 0, -0.82F, 9.12F);
            animator.rotate(leftArm, toRadians(-25.52), toRadians(31.35), toRadians(1.05));
            animator.move(leftArm, 2F, 1F, -3.86F);
            animator.rotate(leftArmUnder, toRadians(-36.33), toRadians(8.57), toRadians(1.93));
            animator.rotate(rightLeg, toRadians(-5.73), toRadians(-2.27), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(6.27), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(10.33), toRadians(0), toRadians(-3.4));
            animator.rotate(leftLegUnder, toRadians(5.62), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-6.49), toRadians(-0.39), toRadians(-0.43));
            animator.endKeyframe();

            animator.startKeyframe(4);//0.44
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-10.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-9.4), 0, 0);
            animator.move(head, 0, -1.5F, 6F);
            animator.rotate(rightArm, toRadians(-107), toRadians(5), toRadians(5));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25.2953), toRadians(17.5424), toRadians(-42.9633));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(3.5907), toRadians(2.9314), toRadians(18.8381));
            animator.move(axe, 0, -0.59F, 7.94F);
            animator.rotate(leftArm, toRadians(-71.7108), toRadians(36.5869), toRadians(10.8117));
            animator.move(leftArm, 2F, 1F, -9F);
            animator.rotate(leftArmUnder, toRadians(-13.5224), toRadians(2.116), toRadians(4.9081));
            animator.rotate(rightLeg, toRadians(7.21), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-3.09), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-16.47), toRadians(0), toRadians(-2.06));
            animator.rotate(leftLegUnder, toRadians(9.26), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.1518), toRadians(12.4347), toRadians(-0.9997));
            animator.endKeyframe();

            animator.startKeyframe(4);//0.64
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-15.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-4.4), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-135.1951), toRadians(8.199), toRadians(38.8199));
            animator.move(rightArm, 1F, -4F, -10F);
            animator.rotate(rightArmUnder, toRadians(-23.3166), toRadians(13.8138), toRadians(-39.0883));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(6.1688), toRadians(0.0843), toRadians(36.2873));
            animator.move(axe, 0, -0.41F, -2.94F);
            animator.rotate(leftArm, toRadians(-85.9648), toRadians(29.1755), toRadians(12.1829));
            animator.move(leftArm, 3F, -4F, -11F);
            animator.rotate(leftArmUnder, toRadians(-15.8352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(10.3), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-4.41), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-23.53), toRadians(0), toRadians(-2.94));
            animator.rotate(leftLegUnder, toRadians(13.23), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.22), toRadians(11.91), toRadians(-0.79));
            animator.endKeyframe();

            animator.startKeyframe(7);//1.0
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-20.6), toRadians(0), toRadians(-2.4));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(22.5), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-132.3443), toRadians(26.9421), toRadians(55.746));
            animator.move(rightArm, 1F, -4F, -10F);
            animator.rotate(rightArmUnder, toRadians(-43.6176), toRadians(20.4524), toRadians(-34.9621));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(7.3267), toRadians(-9.2541), toRadians(41.4075));
            animator.move(axe, 2F, 0F, 5F);
            animator.rotate(leftArm, toRadians(-84.5118), toRadians(23.7862), toRadians(-2.299));
            animator.move(leftArm, 2F, -5F, -11F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(17.5), toRadians(0), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(-7.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-40), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(22.5), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.37), toRadians(10.69), toRadians(-0.29));
            animator.endKeyframe();

            animator.setStaticKeyframe(4);//1.2

            animator.startKeyframe(1);//1.28
            animator.move(root, 0, 0.5F, -2.5F);
            animator.rotate(upper, toRadians(22.2), toRadians(0), toRadians(-1.2));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-6.25), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-88.12), toRadians(-20.2537), toRadians(32.2194));
            animator.move(rightArm, 1.5F, -2.5F, -7.5F);
            animator.rotate(rightArmUnder, toRadians(-20.59), toRadians(0.61), toRadians(-17.48));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(13.0372), toRadians(8.7962), toRadians(21.3868));
            animator.move(axe, 1.5F, 0F, -3.5F);
            animator.rotate(leftArm, toRadians(-78.4506), toRadians(37.7899), toRadians(-11.794));
            animator.move(leftArm, 2.5F, -1F, -7.5F);
            animator.rotate(leftArmUnder, toRadians(-33.34), toRadians(14.21), toRadians(24.05));
            animator.rotate(rightLeg, toRadians(26.25), toRadians(-5), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(7.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-20), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(21.25), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.41), toRadians(10.35), toRadians(-0.15));
            animator.endKeyframe();

            animator.startKeyframe(1);//1.36
            animator.move(root, 0, 1F, -5F);
            animator.rotate(upper, toRadians(65), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-35), 0, 0);
            animator.move(head, 0, -2F, 6F);
            animator.rotate(rightArm, toRadians(-48.9725), toRadians(-46.6899), toRadians(5.7718));
            animator.move(rightArm, 2F, -1F, -5F);
            animator.rotate(rightArmUnder, toRadians(2.4345), toRadians(-19.2257), toRadians(0));
            animator.move(rightArmUnder, 0, 2F, 0);
            animator.rotate(axe, toRadians(19.7914), toRadians(22.607), toRadians(30.2137));
            animator.move(axe, -5F, 0F, -16F);
            animator.rotate(leftArm, toRadians(-49.678), toRadians(27.9707), toRadians(-14.0486));
            animator.move(leftArm, 2F, 2F, -4F);
            animator.rotate(leftArmUnder, toRadians(-33.3352), toRadians(14.2125), toRadians(24.0541));
            animator.rotate(rightLeg, toRadians(35), toRadians(-10), toRadians(0));
            animator.rotate(rightLegUnder, toRadians(22.5), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-20), toRadians(0), toRadians(-5));
            animator.rotate(leftLegUnder, toRadians(20), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-15.45), toRadians(10), toRadians(0));
            animator.endKeyframe();

            animator.setStaticKeyframe(12);//2.0

            animator.startKeyframe(10);//2.64
            animator.move(root, 0, 0.36F, -1.8F);
            animator.rotate(upper, toRadians(21.67), toRadians(0), toRadians(0));
            animator.move(upper, 0, 0, 0);
            animator.rotate(head, toRadians(-17.5), 0, 0);
            animator.move(head, 0, -0.67F, 2F);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(rightArmUnder, toRadians(-59.92), toRadians(-6.92), toRadians(0));
            animator.move(rightArmUnder, 0, 0.72F, 0);
            animator.rotate(axe, toRadians(25.04), toRadians(-2.56), toRadians(19.03));
            animator.move(axe, 0F, -1F, -5F);
            animator.rotate(leftArm, toRadians(-17.88), toRadians(10.07), toRadians(-5.06));
            animator.move(leftArm, 0.72F, 0.72F, -1.44F);
            animator.rotate(leftArmUnder, toRadians(-12), toRadians(5.12), toRadians(8.66));
            animator.rotate(rightLeg, toRadians(16.58), toRadians(-4.74), toRadians(4.74));
            animator.rotate(rightLegUnder, toRadians(8.1), toRadians(0), toRadians(0));
            animator.rotate(leftLeg, toRadians(-7.2), toRadians(0), toRadians(-1.8));
            animator.rotate(leftLegUnder, toRadians(7.2), toRadians(0), toRadians(0));
            animator.rotate(body, toRadians(-5.56), toRadians(3.6), toRadians(0));
            animator.endKeyframe();

            animator.startKeyframe(7);
            animator.rotate(head, 0, 0, 0);
            animator.move(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(5);
        } else if (animator.setAnimation(EntityNamelessGuardian.WEAK_ANIMATION_1)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.2
            animator.rotate(head, toRadians(-25), 0, 0);
            animator.rotate(root, toRadians(2.93), 0, 0);
            animator.move(root, 0, 2, 3);
            animator.rotate(upper, toRadians(-30), 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-20), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, toRadians(-15), 0, 0);
            animator.rotate(leftLeg, toRadians(-25), 0, 0);
            animator.rotate(leftLegUnder, toRadians(17.5), 0, 0);
            animator.rotate(rightLeg, toRadians(45), 0, 0);
            animator.rotate(rightLegUnder, toRadians(7.5), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(4);//0.4
            animator.rotate(head, toRadians(-22.15), 0, 0);
            animator.rotate(root, toRadians(10), 0, 0);
            animator.move(root, 0, 0, 3);
            animator.rotate(upper, toRadians(-30), 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(16);//1.2
            animator.rotate(head, toRadians(28.75), 0, 0);
            animator.rotate(root, toRadians(-4.7), 0, 0);
            animator.move(root, 0, 2.72F, 3);
            animator.rotate(upper, toRadians(-3.5969), toRadians(-0.5324), toRadians(-4.9717));
            animator.move(upper, 0, -1.26F, 0);
            animator.rotate(rightArm, toRadians(2.5), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-69.29), 0, 0);
            animator.rotate(leftArm, toRadians(12.08), 0, 0);
            animator.rotate(leftLeg, toRadians(21.14), toRadians(3.02), 0);
            animator.rotate(leftLegUnder, toRadians(42.27), 0, 0);
            animator.rotate(rightLeg, toRadians(-21.14), toRadians(-3.02), 0);
            animator.rotate(rightLegUnder, toRadians(33.22), 0, 0);
            animator.rotate(axe, toRadians(15), toRadians(-15), toRadians(-5));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(8);//1.6
            animator.rotate(head, toRadians(32.5), 0, 0);
            animator.rotate(root, toRadians(-25), 0, 0);
            animator.move(root, 0, 4.5F, 3);
            animator.rotate(upper, toRadians(55), 0, 0);
            animator.move(upper, 0, -3, 0);
            animator.rotate(rightArm, toRadians(2.5), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25), 0, 0);
            animator.rotate(leftArm, toRadians(-20), 0, toRadians(5));
            animator.rotate(leftLeg, toRadians(35), toRadians(5), 0);
            animator.rotate(leftLegUnder, toRadians(70), 0, 0);
            animator.rotate(rightLeg, toRadians(-35), toRadians(-5), 0);
            animator.rotate(rightLegUnder, toRadians(55), 0, 0);
            animator.rotate(axe, toRadians(15), toRadians(-15), toRadians(-5));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(8);
        } else if (animator.setAnimation(EntityNamelessGuardian.WEAK_ANIMATION_2)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(32.5), 0, 0);
            animator.rotate(root, toRadians(-25), 0, 0);
            animator.move(root, 0, 4.5F, 3);
            animator.rotate(upper, toRadians(55), 0, 0);
            animator.move(upper, 0, -3, 0);
            animator.rotate(rightArm, toRadians(2.5), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25), 0, 0);
            animator.rotate(leftArm, toRadians(-20), 0, toRadians(5));
            animator.rotate(leftLeg, toRadians(35), toRadians(5), 0);
            animator.rotate(leftLegUnder, toRadians(70), 0, 0);
            animator.rotate(rightLeg, toRadians(-35), toRadians(-5), 0);
            animator.rotate(rightLegUnder, toRadians(55), 0, 0);
            animator.rotate(axe, toRadians(15), toRadians(-15), toRadians(-5));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
            animator.setStaticKeyframe(200);
        } else if (animator.setAnimation(EntityNamelessGuardian.WEAK_ANIMATION_3)) {
            animator.startKeyframe(0);
            animator.rotate(head, toRadians(32.5), 0, 0);
            animator.rotate(root, toRadians(-25), 0, 0);
            animator.move(root, 0, 4.5F, 3);
            animator.rotate(upper, toRadians(55), 0, 0);
            animator.move(upper, 0, -3, 0);
            animator.rotate(rightArm, toRadians(2.5), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25), 0, 0);
            animator.rotate(leftArm, toRadians(-20), 0, toRadians(5));
            animator.rotate(leftLeg, toRadians(35), toRadians(5), 0);
            animator.rotate(leftLegUnder, toRadians(70), 0, 0);
            animator.rotate(rightLeg, toRadians(-35), toRadians(-5), 0);
            animator.rotate(rightLegUnder, toRadians(55), 0, 0);
            animator.rotate(axe, toRadians(15), toRadians(-15), toRadians(-5));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(20);//1.4
            animator.rotate(head, toRadians(-2.5), 0, 0);
            animator.rotate(root, toRadians(-10.61), 0, 0);
            animator.move(root, 0, 1.91F, 1.27F);
            animator.rotate(upper, toRadians(28.67), toRadians(-1.7), toRadians(-0.15));
            animator.move(upper, 0, -2.12F, 0);
            animator.rotate(rightArm, toRadians(-87.8531), toRadians(28.9865), toRadians(16.4467));
            animator.rotate(rightArmUnder, toRadians(-39.0655), toRadians(-12.4459), toRadians(-8.4899));
            animator.rotate(leftArm, toRadians(11.06), toRadians(-3.59), toRadians(-6.46));
            animator.rotate(leftLeg, toRadians(12.48), toRadians(-4.13), toRadians(-4.91));
            animator.rotate(leftLegUnder, toRadians(75.06), 0, 0);
            animator.rotate(rightLeg, toRadians(-24.82), toRadians(-1.51), toRadians(2.9));
            animator.rotate(rightLegUnder, toRadians(38.92), 0, 0);
            animator.rotate(axe, toRadians(9.88), toRadians(-13.54), toRadians(2.31));
            animator.move(axe, 0, 0.42F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(12);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(8);
        } else if (animator.setAnimation(EntityNamelessGuardian.ACTIVATE_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(rightArm, toRadians(-35), toRadians(2.5), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
            animator.rotate(head, toRadians(30), 0, 0);
            animator.rotate(upper, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(rightArm, toRadians(-35), toRadians(2.5), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
            animator.rotate(head, toRadians(5), 0, 0);
            animator.rotate(upper, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(18);
            animator.startKeyframe(1);//1.16
            animator.rotate(head, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(2), toRadians(3.5));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
            animator.rotate(upper, toRadians(8), 0, 0);
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();

            animator.startKeyframe(2);//1.28
            animator.rotate(rightArm, toRadians(-43), toRadians(2), toRadians(3.5));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
            animator.rotate(upper, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();

            animator.startKeyframe(8);//1.72
            animator.rotate(root, toRadians(-1), 0, toRadians(1));
            animator.rotate(upper, toRadians(3), toRadians(-0.1), toRadians(0.1));
            animator.rotate(rightArm, toRadians(-80), toRadians(10), toRadians(15));
            animator.rotate(rightArmUnder, toRadians(-38), toRadians(-15), toRadians(-10));
            animator.rotate(rightLeg, toRadians(-6), toRadians(0.4), 0);
            animator.rotate(rightLegUnder, toRadians(8), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(10);//2.16
            animator.rotate(root, toRadians(-2.5), 0, toRadians(2.5));
            animator.rotate(head, toRadians(-5), 0, 0);
            animator.rotate(upper, toRadians(0.4), toRadians(0.12), toRadians(0.15));
            animator.rotate(rightArm, toRadians(-41), toRadians(21), toRadians(22));
            animator.rotate(rightArmUnder, toRadians(-68), toRadians(-8), toRadians(-3));
            animator.rotate(rightLeg, toRadians(-15), toRadians(1), 0);
            animator.rotate(rightLegUnder, toRadians(20), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(2);//2.24
            animator.rotate(root, toRadians(-2), 0, toRadians(2));
            animator.rotate(head, toRadians(-4), 0, 0);
            animator.rotate(upper, 0, toRadians(-0.07), toRadians(0.12));
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-73), toRadians(-6), toRadians(-2));
            animator.rotate(rightLeg, toRadians(-12), toRadians(0.8), 0);
            animator.rotate(rightLegUnder, toRadians(16), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(2);//2.36
            animator.rotate(root, 0, 0, 0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, toRadians(2.5));
            animator.rotate(rightArm, toRadians(-36), toRadians(25), toRadians(25));
            animator.move(rightArm, 0, 1.2F, 0);
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, toRadians(-1.5));
            animator.rotate(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(1);//2.4
            animator.rotate(rightArm, toRadians(-40), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.move(rightArm, 0, 2F, 0);
            animator.rotate(leftArm, 0, 0, toRadians(-2.5));
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(3);
            animator.rotate(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.move(rightArm, 0, 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
            animator.setStaticKeyframe(5);
        } else if (animator.setAnimation(EntityNamelessGuardian.DEACTIVATE_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(11);//0.56
            animator.rotate(upper, toRadians(1), 0, 0);
            animator.rotate(rightArm, toRadians(-59), toRadians(9), toRadians(9));
            animator.rotate(rightArmUnder, toRadians(-63), toRadians(-9), toRadians(-3));
            animator.rotate(axe, toRadians(-0.3), toRadians(-4), toRadians(8));
            animator.move(axe, 0, 1.4F, 4F);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);
            animator.startKeyframe(7);//0.9
            animator.rotate(upper, toRadians(1.5), 0, 0);
            animator.rotate(rightArm, toRadians(-75), toRadians(-15), toRadians(-5));
            animator.rotate(rightArmUnder, toRadians(-40), toRadians(-15), toRadians(-5));
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(2);
            animator.startKeyframe(4);//1.1
            animator.rotate(upper, toRadians(1.5), 0, 0);
            animator.rotate(rightArm, toRadians(-43), toRadians(2.2), toRadians(3.6));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-17), toRadians(-5));
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();


            animator.startKeyframe(1);
            animator.rotate(upper, toRadians(1.5), 0, 0);
            animator.rotate(rightArm, toRadians(-35), toRadians(2.5), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(rightArm, 0, -2, 0);
            animator.move(root, 0, 1, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();

            animator.startKeyframe(1);
            animator.rotate(upper, toRadians(1.5), 0, 0);
            animator.rotate(rightArm, toRadians(-35), toRadians(2.5), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(rightArm, 0, 0, 0);
            animator.move(root, 0, 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();

            animator.startKeyframe(7);
            animator.rotate(head, toRadians(30), 0, 0);
            animator.rotate(upper, toRadians(5), 0, 0);
            animator.rotate(rightArm, toRadians(-35), toRadians(2.5), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-31), toRadians(-18), toRadians(-6));
            animator.rotate(axe, toRadians(1), 0, 0);
            animator.move(rightArm, 0, 0, 0);
            animator.move(axe, 0F, 2F, 9.5F);
            animator.endKeyframe();
            animator.setStaticKeyframe(5);
        } else if (animator.setAnimation(EntityNamelessGuardian.DIE_ANIMATION)) {
            animator.startKeyframe(15);
            animator.rotate(head, toRadians(32.5), 0, 0);
            animator.rotate(root, toRadians(-25), 0, 0);
            animator.move(root, 0, 4.5F, 3);
            animator.rotate(upper, toRadians(55), 0, 0);
            animator.move(upper, 0, -3, 0);
            animator.rotate(rightArm, toRadians(2.5), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25), 0, 0);
            animator.rotate(leftArm, toRadians(-20), 0, toRadians(5));
            animator.rotate(leftLeg, toRadians(35), toRadians(5), 0);
            animator.rotate(leftLegUnder, toRadians(70), 0, 0);
            animator.rotate(rightLeg, toRadians(-35), toRadians(-5), 0);
            animator.rotate(rightLegUnder, toRadians(55), 0, 0);
            animator.rotate(axe, toRadians(15), toRadians(-15), toRadians(-5));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.setStaticKeyframe(10);

            animator.startKeyframe(5);
            animator.rotate(head, toRadians(40), 0, 0);
            animator.rotate(root, toRadians(-25), 0, 0);
            animator.move(root, 0, 4.5F, 3);
            animator.rotate(upper, toRadians(60), 0, 0);
            animator.move(upper, 0, -3, 0);
            animator.rotate(rightArm, toRadians(2.5), 0, 0);
            animator.rotate(rightArmUnder, toRadians(-25), 0, 0);
            animator.rotate(leftArm, toRadians(-20), 0, toRadians(5));
            animator.rotate(leftLeg, toRadians(35), toRadians(5), 0);
            animator.rotate(leftLegUnder, toRadians(70), 0, 0);
            animator.rotate(rightLeg, toRadians(-35), toRadians(-5), 0);
            animator.rotate(rightLegUnder, toRadians(55), 0, 0);
            animator.rotate(axe, toRadians(25), toRadians(-15), toRadians(-5));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();
            animator.setStaticKeyframe(10);
        } else if (animator.setAnimation(EntityNamelessGuardian.DIE_WORDS_ANIMATION)) {
            animator.startKeyframe(0);
            animator.rotate(head, 0, 0, 0);
            animator.rotate(upper, 0, 0, 0);
            animator.move(upper, 0, 0, 0);
            animator.rotate(rightArm, toRadians(-30), toRadians(25), toRadians(25));
            animator.rotate(rightArmUnder, toRadians(-95), 0, 0);
            animator.rotate(leftArm, 0, 0, 0);
            animator.rotate(leftArmUnder, 0, 0, 0);
            animator.rotate(lower, 0, 0, 0);
            animator.rotate(leftLeg, 0, 0, 0);
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, 0, 0, 0);
            animator.rotate(rightLeg, 0, 0, 0);
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, 0, 0, 0);
            animator.rotate(axe, toRadians(-2.5), toRadians(-10), toRadians(20));
            animator.move(axe, 0, -1F, -5F);
            animator.endKeyframe();

            animator.startKeyframe(24);
            animator.rotate(head, toRadians(20), 0, 0);
            animator.rotate(root, toRadians(2.5), 0, 0);
            animator.rotate(upper, toRadians(22.5), 0, 0);
            animator.move(upper, 0, 1, 0);
            animator.rotate(rightArm, toRadians(-10), toRadians(-22), toRadians(4));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-10), toRadians(-10));
            animator.rotate(leftArm, toRadians(-10), toRadians(22), toRadians(-4));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(10), toRadians(10));
            animator.rotate(lower, 0, 0, 0);
            animator.rotate(leftLeg, 0, toRadians(-5), toRadians(-5));
            animator.move(leftLeg, 1F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, 0, toRadians(5), toRadians(5));
            animator.move(rightLeg, -1F, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, 0, 0, 0);
            animator.move(axe, 0, 0, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(head, toRadians(30), 0, 0);
            animator.rotate(root, 0, 0, 0);
            animator.rotate(upper, toRadians(-2.5), 0, 0);
            animator.move(upper, 0, -1, 0);
            animator.rotate(rightArm, toRadians(-77), toRadians(4), toRadians(12));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-20), toRadians(-10));
            animator.rotate(leftArm, toRadians(-77), toRadians(-4), toRadians(-12));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(20), toRadians(10));
            animator.rotate(lower, 0, toRadians(2.5), 0);
            animator.rotate(leftLeg, 0, toRadians(-7.5), toRadians(-7.5));
            animator.move(leftLeg, 0.5F, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(15), toRadians(7.5), toRadians(7.5));
            animator.move(rightLeg, -0.5F, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(10), 0, 0);
            animator.move(axe, 0, -1, 0);
            animator.endKeyframe();

            animator.startKeyframe(4);
            animator.rotate(head, toRadians(20), toRadians(-10), 0);
            animator.rotate(root, toRadians(-2.5), 0, 0);
            animator.rotate(upper, toRadians(-22.5), toRadians(10), 0);
            animator.move(upper, 0, 1, 0);
            animator.rotate(rightArm, toRadians(-29), toRadians(30), toRadians(19));
            animator.rotate(rightArmUnder, toRadians(-17.5), toRadians(-30), toRadians(-10));
            animator.rotate(leftArm, toRadians(-29), toRadians(-30), toRadians(-19));
            animator.rotate(leftArmUnder, toRadians(-17.5), toRadians(30), toRadians(10));
            animator.rotate(lower, 0, toRadians(5), 0);
            animator.rotate(leftLeg, 0, toRadians(-10), toRadians(-10));
            animator.move(leftLeg, 0, 0, 0);
            animator.rotate(leftLegUnder, toRadians(5), 0, 0);
            animator.rotate(rightLeg, toRadians(30), toRadians(10), toRadians(10));
            animator.move(rightLeg, 0, 0, 0);
            animator.rotate(rightLegUnder, toRadians(5), 0, 0);
            animator.rotate(axe, toRadians(20), 0, 0);
            animator.move(axe, 0, 2, 0);
            animator.endKeyframe();

            animator.setStaticKeyframe(18);
        }

        int tick = entity.getAnimationTick();
        if (entity.getAnimation() == EntityNamelessGuardian.SMASH_ATTACK_ANIMATION) {
            if (tick < 20) upper.rotateAngleZ += (float) (upper.rotationPointX * 0.01 * Math.cos(2.0F * frame));
        } else if (entity.getAnimation() == EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_2) {
            upper.rotateAngleZ += (float) (root.rotationPointX * 0.01 * Math.sin(frame));
        } else if (entity.getAnimation() == EntityNamelessGuardian.LEAP_ANIMATION) {
            if (tick > 14) {
                rightArm.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(frame));
                rightLeg.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.sin(frame));
                leftArm.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.sin(frame));
                leftLeg.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(frame));
                root.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(0.5F * frame));
            }
        } else if (entity.getAnimation() == EntityNamelessGuardian.ROAR_ANIMATION) {
            if (tick <= 24 && tick > 5) {
                upper.rotateAngleZ += (float) (upper.rotationPointX * 0.01 * Math.cos(Mth.clamp(tick * 0.1, 0, 2.5) * frame));
                upper.rotateAngleX += (float) (upper.rotationPointX * 0.01 * Math.cos(Mth.clamp(tick * 0.1, 0, 2.5) * frame));
            } else if (tick > 24 && tick < 70) {
                rightArm.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(frame));
                rightLeg.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.sin(frame));
                leftArm.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.sin(frame));
                leftLeg.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(frame));
                root.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(3F * frame));
                head.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(3F * frame));
                head.rotateAngleZ += (float) (root.rotationPointX * 0.01 * Math.cos(3F * frame));
            }
        } else if (entity.getAnimation() == EntityNamelessGuardian.LASER_ANIMATION) {
            if (tick > 24 && tick < 100) {
                rightArm.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(frame));
                leftArm.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.sin(frame));
                upper.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(3F * frame));
                head.rotateAngleX += (float) (root.rotationPointX * 0.01 * Math.cos(3F * frame));
                head.rotateAngleZ += (float) (root.rotationPointX * 0.01 * Math.cos(3F * frame));
            }
        } else if (entity.getAnimation() == EntityNamelessGuardian.DIE_WORDS_ANIMATION) {
            float explodeCoefficient = entity.getExplodeCoefficient(ageInTicks - entity.tickCount);
            rightArm.rotateAngleX += (float) (root.rotationPointX * explodeCoefficient * 0.01 * Math.cos(2 * frame));
            rightLeg.rotateAngleX += (float) (root.rotationPointX * explodeCoefficient * 0.01 * Math.sin(2 * frame));
            leftArm.rotateAngleX += (float) (root.rotationPointX * explodeCoefficient * 0.01 * Math.sin(2 * frame));
            leftLeg.rotateAngleX += (float) (root.rotationPointX * explodeCoefficient * 0.01 * Math.cos(2 * frame));
            root.rotateAngleX += (float) (root.rotationPointX * explodeCoefficient * 0.01 * Math.cos(4F * frame));
            head.rotateAngleX += (float) (root.rotationPointX * explodeCoefficient * 0.01 * Math.cos(4F * frame));
            head.rotateAngleZ += (float) (root.rotationPointX * explodeCoefficient * 0.01 * Math.cos(4F * frame));
        } else if (entity.getAnimation() == EntityNamelessGuardian.WEAK_ANIMATION_2) {
            float speed = 0.5F;
            float degree = 0.2F;
            this.walk(this.root, speed * 2F, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.walk(this.head, speed * 1.9F, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.flap(this.upper, speed, degree * 0.12F, true, 0, 0, limbSwing, limbSwingAmount);
            this.swing(this.upper, speed, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.walk(this.leftArm, speed, degree, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
            this.walk(this.rightArm, speed * 0.98F, 0.2F, false, 0.0F, 0, limbSwing, limbSwingAmount);
        }

    }
}

