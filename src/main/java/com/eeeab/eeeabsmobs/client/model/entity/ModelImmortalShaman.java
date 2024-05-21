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
    private final ModelPart lowerJaw;
    private final ModelPart main;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart armRightArm;
    private final ModelPart armLeftArm;
    private final ModelPart armRightLeg;
    private final ModelPart armLeftLeg;

    public final ModelPart heart;

    public ModelImmortalShaman(ModelPart root) {
        this.root = root.getChild("root");
        this.main = this.root.getChild("main");
        this.head = this.main.getChild("head");
        this.lowerJaw = this.head.getChild("lowerJaw");
        this.leftArm = this.main.getChild("leftArm");
        this.armLeftArm = this.leftArm.getChild("armLeftArm");
        this.rightArm = this.main.getChild("rightArm");
        this.armRightArm = this.rightArm.getChild("armRightArm");
        this.rightLeg = this.root.getChild("rightLeg");
        this.armRightLeg = this.root.getChild("armRightLeg");
        this.leftLeg = this.root.getChild("leftLeg");
        this.armLeftLeg = this.root.getChild("armLeftLeg");
        this.heart = this.main.getChild("body").getChild("heart");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 0.0F));
        PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(96, 94).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.5F)), PartPose.offset(0.0F, -9.0F, 0.0F));
        PartDefinition capeHead = head.addOrReplaceChild("capeHead", CubeListBuilder.create().texOffs(43, 95).addBox(-4.0F, -5.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition armHead = head.addOrReplaceChild("armHead", CubeListBuilder.create().texOffs(0, 112).addBox(-4.0F, -5.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.8F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition kok = armHead.addOrReplaceChild("kok", CubeListBuilder.create(), PartPose.offset(0.0373F, -7.522F, -4.2179F));
        PartDefinition bone_r1 = kok.addOrReplaceChild("bone_r1", CubeListBuilder.create().texOffs(18, 99).mirror().addBox(-0.1759F, -1.4319F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(-4.2614F, 1.9855F, 0.7179F, 0.2618F, 0.0F, -0.2618F));
        PartDefinition bone_r2 = kok.addOrReplaceChild("bone_r2", CubeListBuilder.create().texOffs(0, 92).addBox(1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(1.9627F, 1.522F, -0.6821F, 0.0801F, -0.1796F, -0.1381F));
        PartDefinition bone_r3 = kok.addOrReplaceChild("bone_r3", CubeListBuilder.create().texOffs(18, 99).addBox(-1.4241F, -1.4319F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(3.7869F, 1.9855F, 0.7179F, 0.2618F, 0.0F, 0.2618F));
        PartDefinition mouthKok = armHead.addOrReplaceChild("mouthKok", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0708F, 1.5761F, -2.5983F, 0.5236F, 0.0F, 0.0F));
        PartDefinition bone_r4 = mouthKok.addOrReplaceChild("bone_r4", CubeListBuilder.create().texOffs(9, 99).addBox(-1.1F, -0.2599F, -2.0017F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(3.6357F, -2.9162F, 1.2F, 0.0F, 0.0F, 0.1745F));
        PartDefinition bone_r5 = mouthKok.addOrReplaceChild("bone_r5", CubeListBuilder.create().texOffs(0, 99).addBox(-0.2935F, 1.7401F, -2.0017F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-4.7773F, -4.9162F, 1.2F, 0.0F, 0.0F, -0.1745F));
        PartDefinition lowerJaw = head.addOrReplaceChild("lowerJaw", CubeListBuilder.create(), PartPose.offset(0.1F, -0.3148F, -1.4832F));
        PartDefinition cube_r1 = lowerJaw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 107).addBox(-2.5F, 5.0F, -3.9F, 6.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-0.6F, -6.1852F, -0.1168F, 0.3491F, 0.0F, 0.0F));
        PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(106, 77).addBox(-3.5F, -9.0F, -2.0F, 7.0F, 10.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition hemal = body.addOrReplaceChild("hemal", CubeListBuilder.create().texOffs(67, 78).addBox(-1.0F, -20.0F, -1.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(86, 88).addBox(-4.1F, -19.0F, -1.0F, 7.5F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 11.0F, 0.0F));
        PartDefinition heart = body.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(75, 85).addBox(-1.2F, -0.5F, -0.8F, 2.4F, 3.5F, 1.6F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, -0.5F));
        PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.offset(0.0F, -9.5988F, 2.6548F));
        PartDefinition bone_r6 = cape.addOrReplaceChild("bone_r6", CubeListBuilder.create().texOffs(60, 112).addBox(-6.3F, 0.2F, 2.75F, 10.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.219F, -3.1119F, 0.0436F, 0.0F, 0.0F));
        PartDefinition armBody = body.addOrReplaceChild("armBody", CubeListBuilder.create().texOffs(35, 114).addBox(-4.0F, -19.0F, -2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 10.0F, 0.0F));
        PartDefinition leftArm = main.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(96, 115).mirror().addBox(-0.4811F, -0.4319F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.0349F));
        PartDefinition armLeftArm = leftArm.addOrReplaceChild("armLeftArm", CubeListBuilder.create().texOffs(80, 113).mirror().addBox(-0.7311F, -1.4319F, -1.5F, 3.5F, 11.0F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition handLeftArm = leftArm.addOrReplaceChild("handLeftArm", CubeListBuilder.create().texOffs(0, 0).addBox(9.5378F, -0.2018F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.5189F, 8.7699F, -0.5F));
        PartDefinition rightArm = main.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(96, 115).addBox(-2.5189F, -0.4319F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0349F));
        PartDefinition armRightArm = rightArm.addOrReplaceChild("armRightArm", CubeListBuilder.create().texOffs(80, 113).addBox(-3.2689F, -1.4319F, -1.5F, 3.5F, 11.0F, 3.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition handRightArm = rightArm.addOrReplaceChild("handRightArm", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.2018F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5189F, 8.7699F, -0.5F));
        PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(113, 114).mirror().addBox(-1.9F, -1.0F, -1.5F, 3.5F, 11.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(1.9F, -10.0F, 0.0F));
        PartDefinition armLeftLeg = root.addOrReplaceChild("armLeftLeg", CubeListBuilder.create().texOffs(78, 96).mirror().addBox(-1.9F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, -10.0F, 0.0F));
        PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(113, 114).addBox(-1.6F, -1.0F, -1.5F, 3.5F, 11.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-1.9F, -10.0F, 0.0F));
        PartDefinition armRightLeg = root.addOrReplaceChild("armRightLeg", CubeListBuilder.create().texOffs(78, 96).addBox(-2.1F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, -10.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityImmortalShaman entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        if (entity.isAlive() && entity.isWeakness()) {
            this.setStaticRotationAngle(head, toRadians(1.5), 0, 0);
            this.walk(head, 0.2F, 0.15F, false, 0, 0, frame, 1);
            this.walk(root, 0.1F, 0.1F, false, 0, 0, frame, 1);
        }
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        //Walk
        float walkSpeed = 0.6F;
        float walkDegree = 0.6F;
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.armLeftLeg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
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
        this.animate(entity.spellCastingBombAnimation, AnimationImmortalShaman.BOMB, ageInTicks);
        this.animate(entity.spellCastingHealAnimation, AnimationImmortalShaman.HEAL, ageInTicks);
        this.animate(entity.spellCastingFRAnimation, AnimationImmortalShaman.ATTACK, ageInTicks);
        this.animate(entity.spellCastingSummonAnimation, AnimationImmortalShaman.SUMMON, ageInTicks);
        this.animate(entity.spellCastingWololoAnimation, AnimationImmortalShaman.SUMMON, ageInTicks);
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
        ModelPart model$part = isRightArm ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(poseStack);
        this.main.translateAndRotate(poseStack);
        model$part.translateAndRotate(poseStack);
        poseStack.scale(1F, 1F, 1F);
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
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("armLeftLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("armRightLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("cape",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("lowerJaw",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
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
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("armLeftLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.2f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("armRightLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.2f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("cape",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.2f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("lowerJaw",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.36f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
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
                .addAnimation("main",
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
                .addAnimation("mouthKok",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(120f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.12f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("lowerJaw",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.96f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.12f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("cape",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8f, KeyframeAnimations.degreeVec(60f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.16f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
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
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("main",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(52.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-20.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("cape",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.8f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.88f, KeyframeAnimations.degreeVec(40f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-72.5f, 20f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-90f, -80f, -30f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-72.5f, -20f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-90f, 80f, 30f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
    }
}
