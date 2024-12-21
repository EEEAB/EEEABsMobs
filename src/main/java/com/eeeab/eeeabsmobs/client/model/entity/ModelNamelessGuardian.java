package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationNamelessGuardian;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationNamelessGuardian2;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelNamelessGuardian extends EMHierarchicalModel<EntityNamelessGuardian> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightArmUnder;
    private final ModelPart axe;
    private final ModelPart upper;

    public ModelNamelessGuardian(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        ModelPart lower = this.root.getChild("lower");
        this.head = this.upper.getChild("head");
        this.body = this.upper.getChild("body");
        this.rightArm = this.body.getChild("rightArm");
        this.rightArmUnder = this.rightArm.getChild("rightArmUnder");
        this.axe = this.rightArmUnder.getChild("axe");
        this.leftArm = this.body.getChild("leftArm");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftLeg = lower.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.18F, 24.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offsetAndRotation(2.18F, -28.0851F, 0.0F, 0.0436F, 0.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -22.4649F, -10.06F, 0.1745F, 0.0F, 0.0F));
        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(141, 1).addBox(-6.3297F, -12.551F, -5.6703F, 12.0F, 16.0F, 12.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.2563F, -3.9949F, -1.1028F, 0.0F, 0.7854F, 0.0F));
        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(149, 31).addBox(-4.0297F, -10.0551F, -3.9703F, 8.0F, 11.0F, 8.0F, new CubeDeformation(1.1F)), PartPose.offsetAndRotation(0.2563F, -3.9949F, -0.6028F, 0.0F, 0.7854F, 0.0F));
        PartDefinition tooth = head.addOrReplaceChild("tooth", CubeListBuilder.create(), PartPose.offsetAndRotation(0.1163F, -4.7879F, -2.8632F, 0.2182F, 0.0F, 0.0F));
        PartDefinition cube_r3 = tooth.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(108, 27).mirror().addBox(-1.4127F, -4.8649F, 4.0723F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.9651F, 3.692F, -4.944F, 0.6109F, 0.2618F, -0.1745F));
        PartDefinition cube_r4 = tooth.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(108, 27).addBox(-2.0127F, -4.8649F, 4.0723F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.7325F, 3.692F, -4.944F, 0.6109F, -0.2618F, 0.1745F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(7, 37).addBox(-10.8532F, -13.2245F, -7.5671F, 22.0F, 14.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(81, 1).addBox(-9.3958F, 0.6606F, -6.0245F, 19.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8514F, 0.0F, 0.0436F, 0.0F, 0.0F));
        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 1).addBox(-14.0F, -7.5F, -8.5F, 28.0F, 15.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2319F, -19.0651F, -2.4809F, 0.1745F, 0.0F, 0.0F));
        PartDefinition heart = body.addOrReplaceChild("heart", CubeListBuilder.create(), PartPose.offset(-0.1F, -4.9952F, 0.2181F));
        PartDefinition cube_r6 = heart.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(6, 68).addBox(-5.1169F, -5.2872F, -5.9841F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3488F, -12.8827F, -2.7149F, 0.3491F, 0.0F, 0.0F));
        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-13.3405F, -21.4257F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition rightArmAbove = rightArm.addOrReplaceChild("rightArmAbove", CubeListBuilder.create().texOffs(196, 2).addBox(-7.849F, -5.4852F, -5.3222F, 15.0F, 12.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(190, 111).addBox(-8.849F, 5.0148F, -6.3222F, 16.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6087F, -1.1068F, -3.0851F, -0.2618F, 0.2618F, 0.0F));
        PartDefinition cube_r7 = rightArmAbove.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(220, 29).addBox(-6.4019F, 5.9513F, -3.0089F, 9.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3488F, -0.5688F, 0.0159F, 0.3054F, 0.0F, 0.1745F));
        PartDefinition rightArmUnder = rightArm.addOrReplaceChild("rightArmUnder", CubeListBuilder.create().texOffs(222, 83).addBox(-4.9004F, 11.336F, -5.3773F, 9.0F, 7.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-6.2364F, 13.798F, 1.0F, -0.3054F, 0.2618F, -0.0436F));
        PartDefinition cube_r8 = rightArmUnder.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(208, 53).addBox(-6.7039F, -6.359F, -2.9365F, 12.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4252F, 5.1048F, -3.205F, 0.0436F, 0.0F, 0.0F));
        PartDefinition axe = rightArmUnder.addOrReplaceChild("axe", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5556F, 13.8073F, 0.9916F, -0.0873F, 0.0F, 0.0F));
        PartDefinition lever = axe.addOrReplaceChild("lever", CubeListBuilder.create().texOffs(0, 156).addBox(3.0801F, 4.9444F, -16.8786F, 3.0F, 3.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(85, 174).addBox(2.4501F, 2.5444F, -26.4786F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(60, 198).addBox(2.0801F, 1.1444F, -39.6786F, 5.0F, 5.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(19, 205).addBox(2.7801F, 3.5444F, 19.1214F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(3, 209).addBox(3.1801F, 4.6444F, 25.1214F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -4.4F, 0.0F));
        PartDefinition cube_r9 = lever.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(84, 186).addBox(-2.3102F, 5.7362F, -8.4468F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7303F, 0.6472F, -11.2938F, -0.4363F, 0.0F, 0.0F));
        PartDefinition blade = axe.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(222, 196).addBox(2.7801F, 4.3444F, -37.1786F, 4.0F, 10.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(222, 196).addBox(2.7801F, -8.0556F, -37.1786F, 4.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -4.4F, 0.0F));
        PartDefinition cube_r10 = blade.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(102, 192).addBox(-2.3F, -8.5F, -4.5F, 5.0F, 16.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7301F, -8.9005F, -34.8688F, 1.9199F, 0.0F, 0.0F));
        PartDefinition cube_r11 = blade.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(132, 197).addBox(-1.8949F, -14.3568F, -2.9102F, 4.0F, 13.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.6F, -13.2029F, -31.8596F, -2.2078F, 0.0F, 0.0F));
        PartDefinition cube_r12 = blade.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(182, 186).addBox(-1.8699F, -5.8044F, -3.1078F, 4.0F, 16.0F, 15.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.6F, 16.0029F, -31.8596F, -1.9199F, 0.0F, 0.0F));
        PartDefinition cube_r13 = blade.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(156, 191).addBox(-1.8949F, -6.3321F, -9.8793F, 4.0F, 18.0F, 8.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.6F, 16.0029F, -31.8596F, 2.2078F, 0.0F, 0.0F));
        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(14.3405F, -21.4257F, 0.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition leftArmAbove = leftArm.addOrReplaceChild("leftArmAbove", CubeListBuilder.create().texOffs(196, 2).mirror().addBox(-6.1277F, -5.4852F, -5.3222F, 15.0F, 12.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(190, 111).mirror().addBox(-6.1277F, 5.0148F, -6.3222F, 16.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.6125F, -1.1939F, -3.0851F, -0.2618F, -0.2618F, 0.0F));
        PartDefinition cube_r14 = leftArmAbove.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(220, 29).mirror().addBox(-1.698F, 5.9513F, -3.0089F, 9.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.3488F, -0.5688F, 0.0159F, 0.3054F, 0.0F, -0.1745F));
        PartDefinition leftArmUnder = leftArm.addOrReplaceChild("leftArmUnder", CubeListBuilder.create().texOffs(222, 83).mirror().addBox(-4.9466F, 11.6421F, -3.2054F, 9.0F, 7.0F, 8.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(7.2402F, 13.7108F, 0.0F, -0.3054F, -0.2618F, 0.0436F));
        PartDefinition cube_r15 = leftArmUnder.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(208, 53).mirror().addBox(-5.5641F, -6.559F, -2.3677F, 12.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.3772F, 5.6109F, -2.3331F, 0.0436F, 0.0F, 0.0F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(2.18F, -28.0851F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.7128F, 3.404F, 0.0F, 0.0F, 0.0436F, 0.0F));
        PartDefinition rightLegAbove = rightLeg.addOrReplaceChild("rightLegAbove", CubeListBuilder.create().texOffs(147, 54).addBox(-4.0235F, -1.7708F, -6.1544F, 9.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition rightLegUnder = rightLeg.addOrReplaceChild("rightLegUnder", CubeListBuilder.create(), PartPose.offset(0.0F, 8.798F, -1.0F));
        PartDefinition cube_r16 = rightLegUnder.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(74, 38).addBox(-5.4574F, -3.0F, -9.2554F, 11.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(142, 86).addBox(-5.8297F, -15.0F, -6.1703F, 12.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3488F, 15.3143F, 1.0159F, 0.1309F, 0.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(7.7128F, 3.404F, 0.0F, 0.0F, -0.0436F, 0.0F));
        PartDefinition leftLegAbove = leftLeg.addOrReplaceChild("leftLegAbove", CubeListBuilder.create().texOffs(147, 54).mirror().addBox(-4.0235F, -1.7708F, -6.1544F, 9.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftLegUnder = leftLeg.addOrReplaceChild("leftLegUnder", CubeListBuilder.create(), PartPose.offset(0.0F, 8.798F, -1.0F));
        PartDefinition cube_r17 = leftLegUnder.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(74, 38).mirror().addBox(-5.4574F, -3.0F, -9.2554F, 11.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(142, 86).mirror().addBox(-5.8297F, -15.0F, -6.1703F, 12.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.3488F, 15.3143F, 1.0159F, 0.1309F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityNamelessGuardian entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        //Animation
        this.animate(entity.dieAnimation, AnimationNamelessGuardian.DIE, ageInTicks);
        this.animate(entity.concussionAnimation, AnimationNamelessGuardian.CONCUSSION, ageInTicks);
        this.animate(entity.activateAnimation, AnimationNamelessGuardian.ACTIVE, ageInTicks);
        this.animate(entity.deactivateAnimation, AnimationNamelessGuardian.DEACTIVATE, ageInTicks);
        this.animate(entity.attackAnimation1, AnimationNamelessGuardian.ATTACK_1, ageInTicks);
        this.animate(entity.attackAnimation2, AnimationNamelessGuardian.ATTACK_2, ageInTicks);
        this.animate(entity.attackAnimation3, AnimationNamelessGuardian.ATTACK_3, ageInTicks);
        this.animate(entity.attackAnimation4, AnimationNamelessGuardian.ATTACK_4, ageInTicks);
        this.animate(entity.attackAnimation5, AnimationNamelessGuardian.ATTACK_5, ageInTicks);
        this.animate(entity.attackAnimation6, AnimationNamelessGuardian.ATTACK_6, ageInTicks);
        this.animate(entity.pounceAttackAnimation1, AnimationNamelessGuardian.PRE_POUNCE, ageInTicks);
        this.animate(entity.pounceAttackAnimation2, AnimationNamelessGuardian.POUNCE, ageInTicks, 1.5F);
        this.animate(entity.pounceAttackAnimation3, AnimationNamelessGuardian.AFTER_POUNCE, ageInTicks);
        this.animate(entity.roarAnimation, AnimationNamelessGuardian2.ROAR, ageInTicks);
        this.animate(entity.robustAttackAnimation, AnimationNamelessGuardian2.ROBUST_ATTACK, ageInTicks);
        this.animate(entity.smashAttackAnimation, AnimationNamelessGuardian2.SMASH_ATTACK, ageInTicks);
        this.animate(entity.weakAnimation1, AnimationNamelessGuardian2.WEAK_START, ageInTicks);
        this.animate(entity.weakAnimation2, AnimationNamelessGuardian2.WEAK_HOLD, ageInTicks);
        this.animate(entity.weakAnimation3, AnimationNamelessGuardian2.WEAK_END, ageInTicks);
        this.animate(entity.leapAnimation, AnimationNamelessGuardian2.LEAP, ageInTicks);
        this.animate(entity.smashDownAnimation, AnimationNamelessGuardian2.SMASH_DOWN, ageInTicks);
        this.animate(entity.laserAnimation, AnimationNamelessGuardian2.SHOOT_BEAM, ageInTicks);
        this.animate(entity.shakeGroundAttackAnimation1, AnimationNamelessGuardian2.SHAKE_GROUND_1, ageInTicks);
        this.animate(entity.shakeGroundAttackAnimation2, AnimationNamelessGuardian2.SHAKE_GROUND_2, ageInTicks);
        this.animate(entity.shakeGroundAttackAnimation3, AnimationNamelessGuardian2.SHAKE_GROUND_3, ageInTicks);
        //Pose
        if (entity.isNoAnimation()) {
            if (entity.isActive()) {
                setStaticRotationAngle(axe, -2.5F, -10, 20);
                setStaticRotationPoint(axe, 0, -1F, -5F);
                setStaticRotationAngle(rightArm, -30, 25, 25);
                setStaticRotationAngle(rightArmUnder, -95, 0, 0);
            } else {
                setStaticRotationAngle(rightArm, -35, 2.5F, 4);
                setStaticRotationAngle(rightArmUnder, -31.1076F, -18.1723F, -6.2528F);
                setStaticRotationAngle(head, 40, 0, 0);
                setStaticRotationPoint(head, 0, -2, 1);
                setStaticRotationAngle(upper, 5, 0, 0);
                setStaticRotationAngle(axe, 1, 0, 0);
                setStaticRotationPoint(axe, 0F, 0, 9.5F);
            }
        }
        if (entity.isActive() && entity.isAlive()) {
            //Idle
            this.bob(rightArm, 0.1F, 0.5F, false, frame, 1);
            this.bob(leftArm, 0.1F, 0.5F, false, frame, 1);
            this.bob(body, 0.1F, 0.2F, false, frame, 1);
            this.bob(head, 0.1F, 0.4F, false, frame, 1);
            this.walk(upper, 0.1F, 0.05F, false, 0, 0, frame, 1);
            this.walk(head, 0.1F, 0.05F, true, 0, 0, frame, 1);
            //Walk
            if (entity.isNoAnimation() && entity.getDeltaMovement().horizontalDistanceSqr() > 0) {
                this.animateWalk(AnimationNamelessGuardian.WALK, limbSwing, limbSwingAmount, 1.5F, 1.5F);
            }
            float speed = 0.2F;
            float degree = 0.6F;
            this.walk(this.root, speed * 2F, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.flap(this.upper, speed, degree * 0.12F, true, 0, 0, limbSwing, limbSwingAmount);
            this.swing(this.upper, speed, degree * 0.12F, false, 0, 0, limbSwing, limbSwingAmount);
            this.walk(this.leftArm, speed, degree * 0.8F, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
            this.walk(this.rightArm, speed * 0.8F, 0.2F, false, 0.0F, 0, limbSwing, limbSwingAmount);
        }
        //Animation effect
        int tick = entity.getAnimationTick();
        if (entity.getAnimation() == entity.smashAttackAnimation) {
            if (tick < 20) upper.zRot += (float) (upper.x * 0.01 * Math.cos(2.0F * frame));
        } else if (entity.getAnimation() == entity.pounceAttackAnimation2) {
            upper.zRot += (float) (root.x * 0.01 * Math.sin(frame));
        } else if (entity.getAnimation() == entity.leapAnimation) {
            if (tick > 14) {
                rightArm.xRot += (float) (root.x * 0.01 * Math.cos(frame));
                rightLeg.xRot += (float) (root.x * 0.01 * Math.sin(frame));
                leftArm.xRot += (float) (root.x * 0.01 * Math.sin(frame));
                leftLeg.xRot += (float) (root.x * 0.01 * Math.cos(frame));
                root.xRot += (float) (root.x * 0.01 * Math.cos(0.5F * frame));
            }
        } else if (entity.getAnimation() == entity.roarAnimation) {
            if (tick <= 24 && tick > 5) {
                upper.zRot += (float) (upper.x * 0.01 * Math.cos(Mth.clamp(tick * 0.1, 0, 2.5) * frame));
                upper.xRot += (float) (upper.x * 0.01 * Math.cos(Mth.clamp(tick * 0.1, 0, 2.5) * frame));
            } else if (tick > 24 && tick < 70) {
                rightArm.xRot += (float) (root.x * 0.01 * Math.cos(frame));
                rightLeg.xRot += (float) (root.x * 0.01 * Math.sin(frame));
                leftArm.xRot += (float) (root.x * 0.01 * Math.sin(frame));
                leftLeg.xRot += (float) (root.x * 0.01 * Math.cos(frame));
                root.xRot += (float) (root.x * 0.01 * Math.cos(3F * frame));
                head.xRot += (float) (root.x * 0.01 * Math.cos(3F * frame));
                head.zRot += (float) (root.x * 0.01 * Math.cos(3F * frame));
            }
        } else if (entity.getAnimation() == entity.laserAnimation) {
            if (tick > 24 && tick < 100) {
                rightArm.xRot += (float) (root.x * 0.01 * Math.cos(frame));
                leftArm.xRot += (float) (root.x * 0.01 * Math.sin(frame));
                upper.xRot += (float) (root.x * 0.01 * Math.cos(3F * frame));
                head.xRot += (float) (root.x * 0.01 * Math.cos(3F * frame));
                head.zRot += (float) (root.x * 0.01 * Math.cos(3F * frame));
            }
        } else if (entity.getAnimation() == entity.dieAnimation) {
            float explodeCoefficient = entity.getExplodeCoefficient(ageInTicks - entity.tickCount);
            rightArm.xRot += (float) (root.x * explodeCoefficient * 0.01 * Math.cos(2 * frame));
            rightLeg.xRot += (float) (root.x * explodeCoefficient * 0.01 * Math.sin(2 * frame));
            leftArm.xRot += (float) (root.x * explodeCoefficient * 0.01 * Math.sin(2 * frame));
            leftLeg.xRot += (float) (root.x * explodeCoefficient * 0.01 * Math.cos(2 * frame));
            root.xRot += (float) (root.x * explodeCoefficient * 0.01 * Math.cos(4F * frame));
            head.xRot += (float) (root.x * explodeCoefficient * 0.01 * Math.cos(4F * frame));
            head.zRot += (float) (root.x * explodeCoefficient * 0.01 * Math.cos(4F * frame));
        } else if (entity.getAnimation() == entity.weakAnimation2) {
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

