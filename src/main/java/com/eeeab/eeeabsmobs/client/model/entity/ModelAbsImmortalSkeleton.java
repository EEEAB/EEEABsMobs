package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationCommon;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortalSkeleton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelAbsImmortalSkeleton extends EMHierarchicalModel<EntityAbsImmortalSkeleton> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart upper;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ModelAbsImmortalSkeleton(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.head = this.upper.getChild("head");
        this.leftArm = this.upper.getChild("leftArm");
        this.rightArm = this.upper.getChild("rightArm");
        ModelPart lower = this.root.getChild("lower");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftLeg = lower.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(1.0F, -12.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F))
                .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.85F))
                .texOffs(0, 31).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(26, 17).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 47).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(9, 16).addBox(-0.1038F, -1.4128F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -10.5F, 0.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition cube_r1 = leftArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 47).addBox(0.0F, -1.6F, -3.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.1038F, -0.4128F, 1.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(9, 16).mirror().addBox(-1.9924F, -1.3257F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -10.5F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition cube_r2 = rightArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 47).mirror().addBox(-4.0F, -1.6F, -3.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.0038F, -0.4128F, 1.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(1.0F, -12.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.2F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 47).addBox(-2.2F, -0.2F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.22F))
                .texOffs(32, 36).addBox(-2.2F, 5.6F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.52F)), PartPose.offset(2.2F, 0.0F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 47).mirror().addBox(-2.0F, -0.2F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.22F)).mirror(false)
                .texOffs(32, 36).mirror().addBox(-2.0F, 5.6F, -2.05F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.52F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityAbsImmortalSkeleton entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //Idle & Walk
        if (entity.isAlive()) {
            float cycle = 0.8F;
            if (!(entity.isArcher() || entity.isMage())) cycle = 0.65F;
            this.walk(this.upper, 0.1F, 0.005F, true, 0, -0.025F, frame, 1);
            this.bob(this.upper, cycle * 0.2F, cycle * 0.2F, false, frame, 1);
            this.bob(this.leftArm, cycle * 0.2F, cycle * 0.2F, false, frame, 1);
            this.bob(this.rightArm, cycle * 0.2F, cycle * 0.2F, false, frame, 1);
            this.flap(this.leftArm, cycle, cycle * 0.2F, true, -0.5F, 0.25F, limbSwing, limbSwingAmount);
            this.flap(this.rightArm, cycle, cycle * 0.2F, true, -0.5F, -0.25F, limbSwing, limbSwingAmount);
            this.bob(this.head, cycle * -0.2F, cycle * -0.2F, false, frame, 1);
            this.bob(this.head, cycle, cycle * 0.6F, false, limbSwing, limbSwingAmount);
            if (entity.getAnimation() != entity.blockAnimation) {
                this.walk(this.leftArm, 0.15F, 0.05F, true, 0.15F, 0, frame, 1);
                this.walk(this.rightArm, 0.15F, 0.05F, false, 0.15F, 0, frame, 1);
            }
            this.walk(this.leftLeg, cycle, cycle * 1.4F, false, 0, 0, limbSwing, limbSwingAmount);
            this.walk(this.rightLeg, cycle, cycle * 1.4F, true, 0, 0, limbSwing, limbSwingAmount);
            this.flap(this.root, cycle, cycle * 0.08F, true, 0, 0, limbSwing, limbSwingAmount);
            if (entity.isArcher()) {
                this.animateWalk(AnimationImmortalSkeleton.WALK, limbSwing, limbSwingAmount, 1F, 2F);
            } else if (entity.isNoAnimation()) {
                this.walk(this.leftArm, cycle, cycle * 1.2F, false, 0, 0, limbSwing, limbSwingAmount);
                this.walk(this.rightArm, cycle, cycle * 1.2F, true, 0, 0, limbSwing, limbSwingAmount);
            }
        }
        if (entity.getAnimation() == entity.castAnimation) {
            this.rightArm.z = 0.0F;
            this.rightArm.x = -5.0F;
            this.leftArm.z = 0.0F;
            this.leftArm.x = 5.0F;
            this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
            this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
            this.rightArm.zRot = 2.3561945F;
            this.leftArm.zRot = -2.3561945F;
            this.rightArm.yRot = 0.0F;
            this.leftArm.yRot = 0.0F;
        } else if (entity.getAnimation() == entity.bowAnimation) {
            setStaticRotationAngle(rightArm, -85F, -10F, -10F);
            setStaticRotationAngle(leftArm, -75F, 25F, 10F);
        } else if (entity.getAnimation() == entity.crossBowChangeAnimation) {
            AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
        } else if (entity.getAnimation() == entity.crossBowHoldAnimation) {
            AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
        }
        this.animate(entity.spawnAnimation, AnimationCommon.SPAWN, ageInTicks);
        this.animate(entity.swingArmAnimation, entity.getMainHandItem().isEmpty() ? AnimationImmortalSkeleton.SWINGARM : AnimationImmortalSkeleton.MELEE1, ageInTicks);
        this.animate(entity.meleeAnimation1, AnimationImmortalSkeleton.MELEE1, ageInTicks);
        this.animate(entity.meleeAnimation2, AnimationImmortalSkeleton.MELEE2, ageInTicks);
        this.animate(entity.roarAnimation, AnimationImmortalSkeleton.ROAR, ageInTicks);
        this.animate(entity.blockAnimation, AnimationImmortalSkeleton.BLOCK, ageInTicks);
        this.animate(entity.dieAnimation, AnimationCommon.DIE, ageInTicks);
    }


    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        ModelPart model$part = humanoidArm == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
        this.root.translateAndRotate(poseStack);
        this.upper.translateAndRotate(poseStack);
        model$part.translateAndRotate(poseStack);
        poseStack.translate(0D, 0.09D, 0.058D);
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @OnlyIn(Dist.CLIENT)
    private static class AnimationImmortalSkeleton {

        public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(0f).looping()
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-75f, 25f, 10f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(-0.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, -10f, -10f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition SWINGARM = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0.1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5416766f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-120.08f, 4.53f, -2.12f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-116.88f, 2f, 2.24f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(-21.96f, -9.14f, 18.25f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(11.54f, -6f, 2.34f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-120.08f, -4.53f, 2.12f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-116.88f, -2f, -2.24f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(-21.96f, 9.14f, -18.25f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(11.54f, 6f, -2.34f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition MELEE1 = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(60f, 20f, -20f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.52f, KeyframeAnimations.degreeVec(-15f, 0f, -7.5f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.64f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2f, KeyframeAnimations.posVec(0f, 1f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.36f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.52f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(-135.19f, 20f, -10.51f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.28f, KeyframeAnimations.degreeVec(-150f, 10f, -8f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(-52.4f, -35.14f, 9.57f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-38.22f, -28.51f, 5.38f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(-20f, -20f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.68f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(-5f, 1f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(10f, -10f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(9.95f, -9.94f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.64f, KeyframeAnimations.degreeVec(2f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(0f, -5f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(-9.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(4.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2f, KeyframeAnimations.degreeVec(25f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition MELEE2 = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, -1f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16f, KeyframeAnimations.degreeVec(54.04f, 36.85f, -32.34f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(53.13f, 26.08f, -24.78f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(-6.64f, -0.83f, -6.73f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 0f, -1f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16f, KeyframeAnimations.posVec(0f, 1f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.56f, KeyframeAnimations.posVec(0f, -0.29f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.64f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16f, KeyframeAnimations.degreeVec(-106.7f, -14.11f, 61.69f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(4.01f, -8.93f, 70.24f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(-32.24f, -6.26f, 54.49f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16f, KeyframeAnimations.degreeVec(-5f, -40f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(10f, 20f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(-2f, 5f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.72f, KeyframeAnimations.degreeVec(2f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16f, KeyframeAnimations.degreeVec(-2.53f, 9.99f, -0.66f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(10.11f, 9.92f, 0.88f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16f, KeyframeAnimations.degreeVec(-6.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4f, KeyframeAnimations.degreeVec(6.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.56f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition ROAR = AnimationDefinition.Builder.withLength(2f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.posVec(0f, 0f, -1f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.posVec(0f, 0f, -3f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.posVec(0f, 0f, -3f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.posVec(0f, 0f, -1.33f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-20f, 5f, -2.5f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.degreeVec(45f, -2.5f, -2.5f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.4f, KeyframeAnimations.degreeVec(40f, -5.71f, -2.5f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.degreeVec(27.63f, -6.13f, -2.5f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(15f, -5f, -2.5f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.degreeVec(-40.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.4f, KeyframeAnimations.degreeVec(-35.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.degreeVec(-28.12f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-90.5f, 10f, 20f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.degreeVec(30.5f, 0f, -20f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.degreeVec(30.5f, 0f, -10f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-90.5f, -10f, -20f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.degreeVec(30.5f, 0f, 20f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.degreeVec(30.5f, 0f, 10f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftLeg",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.posVec(0f, 0f, 2f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.posVec(0f, 0f, 1f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.posVec(0f, 0f, 1f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.posVec(0f, 0f, 0.44f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(20f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(4.44f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightLeg",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.posVec(0f, 0f, -2f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.posVec(0f, 0f, -2f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.posVec(0f, 0f, -0.89f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.44f, KeyframeAnimations.degreeVec(-50f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.6f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.56f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.76f, KeyframeAnimations.degreeVec(-4.44f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition BLOCK = AnimationDefinition.Builder.withLength(0.5f)
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.12f, KeyframeAnimations.degreeVec(5f, 15f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.36f, KeyframeAnimations.degreeVec(5f, 15f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.12f, KeyframeAnimations.degreeVec(-7.5f, -12.5f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.36f, KeyframeAnimations.degreeVec(-7.5f, -12.5f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.12f, KeyframeAnimations.posVec(0f, 0f, -1f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.36f, KeyframeAnimations.posVec(0f, 0f, -1f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.48f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.12f, KeyframeAnimations.degreeVec(-60f, 60f, 20f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.36f, KeyframeAnimations.degreeVec(-60f, 60f, 20f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.12f, KeyframeAnimations.degreeVec(7.5f, 0f, 10f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.36f, KeyframeAnimations.degreeVec(7.5f, 0f, 10f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
    }
}
