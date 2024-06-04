package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalShaman;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelImmortalShaman extends EMHierarchicalModel<EntityImmortalShaman> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart upper;
    private final ModelPart rightArm;
    private final ModelPart rightHand;
    private final ModelPart leftArm;
    private final ModelPart leftHand;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ModelImmortalShaman(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.head = this.upper.getChild("head");
        this.leftArm = this.upper.getChild("leftArm");
        this.leftHand = this.leftArm.getChild("leftArmUnder");
        this.rightArm = this.upper.getChild("rightArm");
        this.rightHand = this.rightArm.getChild("rightArmUnder");
        ModelPart lower = this.root.getChild("lower");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftLeg = lower.getChild("leftLeg");

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(1.0F, -21.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -3.7F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.8F)), PartPose.offset(0.0F, -10.5F, -0.3F));
        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 70).addBox(-10.5F, -0.1F, -9.5116F, 20.0F, 0.1F, 20.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -4.9406F, -0.1884F, 1.0353F, 0.4718F, 0.6537F));
        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 33).addBox(-4.0F, -5.8F, -4.1F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.3807F, 0.6939F, -0.3054F, 0.0F, 0.0F));
        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -4.0F, -0.2F, 0.1745F, 0.0F, 0.0F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 17).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 51).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(9, 16).addBox(-0.1038F, -1.4128F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -8.5F, 0.0F, 0.0F, 0.0F, -0.0436F));
        PartDefinition leftArmUnder = leftArm.addOrReplaceChild("leftArmUnder", CubeListBuilder.create().texOffs(9, 16).addBox(-1.1038F, 0.5872F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 8.0F, 0.0F));
        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(9, 16).mirror().addBox(-1.8962F, -1.4128F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.1F, -8.5F, 0.0F, 0.0F, 0.0F, 0.0436F));
        PartDefinition rightArmUnder = rightArm.addOrReplaceChild("rightArmUnder", CubeListBuilder.create().texOffs(9, 16).mirror().addBox(-0.8962F, 0.5872F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 8.0F, 0.0F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(1.0F, -21.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.2F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 51).addBox(-2.2F, -0.2F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.22F)), PartPose.offset(2.2F, 1.0F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 51).mirror().addBox(-2.0F, -0.2F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.22F)).mirror(false), PartPose.offset(-2.0F, 1.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityImmortalShaman entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        if (entity.isAlive() && entity.isWeakness()) {
            this.setStaticRotationAngle(head, toRadians(1.5), 0, 0);
            this.walk(head, 0.2F, 0.15F, false, 0, 0, frame, 1);
            this.walk(root, 0.1F, 0.1F, false, 0, 0, frame, 1);
        }
        //Walk
        float walkSpeed = 0.6F;
        float walkDegree = 0.6F;
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 1.2F, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 1.2F, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        //Idle
        float speed = 0.16F;
        float degree = 0.04F;
        if (entity.isAlive() && !entity.isWeakness()) {
            this.walk(head, speed, degree, false, 0.5F, -0.05F, frame, 1);
            this.flap(rightArm, speed, degree * 0.5F, true, 0, 0, frame, 1);
            this.swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.flap(leftArm, speed, degree * 0.5F, false, 0, 0, frame, 1);
            this.swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
        this.animate(entity.spellCastingBombAnimation, ModelImmortalShaman.AnimationImmortalShaman.BOMB, ageInTicks);
        this.animate(entity.spellCastingHealAnimation, ModelImmortalShaman.AnimationImmortalShaman.HEAL, ageInTicks);
        this.animate(entity.spellCastingFRAnimation, ModelImmortalShaman.AnimationImmortalShaman.ATTACK, ageInTicks);
        this.animate(entity.spellCastingSummonAnimation, ModelImmortalShaman.AnimationImmortalShaman.SUMMON, ageInTicks);
        this.animate(entity.spellCastingWololoAnimation, ModelImmortalShaman.AnimationImmortalShaman.SUMMON, ageInTicks);
        int tick = entity.getAnimationTick();
        if (entity.getAnimation() == entity.spellCastingSummonAnimation || entity.getAnimation() == entity.spellCastingWololoAnimation) {
            if (tick < 34) {
                generalShake(frame);
            }
        } else if (entity.getAnimation() == entity.spellCastingHealAnimation) {
            if (tick < 50) {
                generalShake(frame);
            }

        } else if (entity.getAnimation() == entity.spellCastingFRAnimation) {
            if (tick < 12) {
                this.walk(root, 2.5F, 0.125F - tick * 0.01F, false, 0, 0, frame, 1);
                this.flap(root, 1.5F, 0.1F - tick * 0.01F, false, 0, 0, frame, 1);
            }

        } else if (entity.getAnimation() == entity.spellCastingBombAnimation) {
            if (tick < 15) {
                this.walk(head, 1.2F, 0.08F, false, 0, 0, frame, 1);
                this.swing(head, 1.4F, 0.1F, false, 0, 0, frame, 1);
            }
        }
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        boolean isRightArm = humanoidArm == HumanoidArm.RIGHT;
        ModelPart model$part = isRightArm ? this.rightHand : this.leftHand;
        this.root.translateAndRotate(poseStack);
        this.upper.translateAndRotate(poseStack);
        if (isRightArm) {
            this.rightArm.translateAndRotate(poseStack);
        } else {
            this.leftArm.translateAndRotate(poseStack);
        }
        model$part.translateAndRotate(poseStack);
        poseStack.scale(1F, 1F, 1F);
        this.offsetStackPosition(poseStack, isRightArm);
    }

    private void offsetStackPosition(PoseStack translate, boolean isRightArm) {
        if (isRightArm) {
            translate.translate(0.125, -0.125, 0);
        } else {
            translate.translate(-0.125, -0.125, 0);
        }
    }

    private void generalShake(float frame) {
        this.walk(head, 1.4F, 0.04F, false, 0, 0, frame, 1);
        this.swing(head, 1.6F, 0.08F, false, 0, 0, frame, 1);
        this.walk(leftArm, 0.45F, 0.2F, false, 0, 0, frame, 1);
        this.walk(rightArm, 0.45F, 0.2F, false, 0, 0, frame, 1);
        this.bob(root, 0.4F, 1.2F, false, frame, 1);
    }


    @OnlyIn(Dist.CLIENT)
    private static class AnimationImmortalShaman {


        public static final AnimationDefinition SUMMON = AnimationDefinition.Builder.withLength(2.2f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.64f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.8f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.04f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-3.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.72f, KeyframeAnimations.degreeVec(-5.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-90f, -24.97f, -1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.72f, KeyframeAnimations.degreeVec(-90.24f, -24.97f, -1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-90f, 24.97f, 1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.72f, KeyframeAnimations.degreeVec(-90.24f, 24.97f, 1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition HEAL = AnimationDefinition.Builder.withLength(2.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.64f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.48f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-18.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.16f, KeyframeAnimations.degreeVec(-18.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-90f, -24.97f, -1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.16f, KeyframeAnimations.degreeVec(-90.24f, -24.97f, -1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-90f, 24.97f, 1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.16f, KeyframeAnimations.degreeVec(-90.24f, 24.97f, 1.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition BOMB = AnimationDefinition.Builder.withLength(1.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.24f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.52f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.76f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.24f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.52f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.76f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.24f, KeyframeAnimations.degreeVec(-10.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-10.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.68f, KeyframeAnimations.degreeVec(-25f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.92f, KeyframeAnimations.degreeVec(-15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.12f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.24f, KeyframeAnimations.degreeVec(-110f, 30f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-115f, 30f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(60f, 20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.12f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.24f, KeyframeAnimations.degreeVec(-110f, -30f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-115f, -30f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(60f, -20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.12f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition ATTACK = AnimationDefinition.Builder.withLength(1.52f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.76f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(52.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.76f, KeyframeAnimations.degreeVec(-20.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.76f, KeyframeAnimations.degreeVec(-22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-72.5f, 20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.76f, KeyframeAnimations.degreeVec(-90f, -80f, -30f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.28f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-72.5f, -20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.76f, KeyframeAnimations.degreeVec(-90f, 80f, 30f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.28f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
    }

}
