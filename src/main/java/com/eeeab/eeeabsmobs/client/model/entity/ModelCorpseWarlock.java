package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
import com.eeeab.animate.client.model.EMHierarchicalModel;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelCorpseWarlock extends EMHierarchicalModel<EntityCorpseWarlock> {
    private final ModelPart root;
    private final ModelPart upper;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart cloak;
    public final ModelPart head;

    public ModelCorpseWarlock(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.head = this.upper.getChild("head");
        this.rightArm = this.upper.getChild("rightArm");
        this.leftArm = this.upper.getChild("leftArm");
        this.cloak = this.upper.getChild("body").getChild("cloak");
        ModelPart lower = this.root.getChild("lower");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftLeg = lower.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));
        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(34, 0).addBox(-3.7F, -4.8F, -4.3F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0872F, 0.7844F, 0.0617F));
        PartDefinition head_r2 = head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.8F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(62, 18).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 17.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, -11.5338F, 2.7579F));
        PartDefinition cloak_r1 = cloak.addOrReplaceChild("cloak_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-6.5F, -1.0F, 0.0F, 13.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(26, 18).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-5.4F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(26, 18).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(5.5F, -10.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(44, 18).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(44, 18).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityCorpseWarlock entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //Walk
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        flap(root, walkSpeed, walkDegree * 0.08F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        walk(leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        walk(rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entity.getAnimation() == entity.getNoAnimation()) {
            walk(rightArm, walkSpeed, walkDegree, true, -0.2F, -0.1F, limbSwing, limbSwingAmount);
            walk(leftArm, walkSpeed, walkDegree, false, -0.2F, 0.1F, limbSwing, limbSwingAmount);
            walk(cloak, walkSpeed * 0.5F, walkDegree * 0.5F, false, 0F, 0F, limbSwing, limbSwingAmount);
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
        this.animate(entity.attackAnimation, AnimationCorpseWarlock.ATTACK, ageInTicks, 1.0F);
        this.animate(entity.tearSpaceAnimation, AnimationCorpseWarlock.TEAR_SPACE, ageInTicks, 1.0F);
        this.animate(entity.teleportAnimation, AnimationCorpseWarlock.TELEPORT, ageInTicks, 1.0F);
        this.animate(entity.vampireAnimation, entity.isHeal() ? AnimationCorpseWarlock.VAMPIRE_HEAL : AnimationCorpseWarlock.VAMPIRE_ATTACK, ageInTicks, 1.0F);
        this.animate(entity.robustAnimation, AnimationCorpseWarlock.ROBUST_ATTACK, ageInTicks, 1.0F);
        if (entity.getAnimation() == entity.summonAnimation || entity.getAnimation() == entity.frenzyAnimation || entity.getAnimation() == entity.babbleAnimation) {
            float animationSpeed = 1.0F;
            if (entity.getAnimation() == entity.babbleAnimation) {
                head.xRot = -10F * Mth.PI / 180F;
                animationSpeed = 0.5F;
            }
            rightArm.z = 0.0F;
            rightArm.x = -6.0F;
            leftArm.z = 0.0F;
            leftArm.x = 6.0F;
            rightArm.xRot = Mth.cos(ageInTicks * 0.6666F * animationSpeed) * 0.5F;
            leftArm.xRot = Mth.cos(ageInTicks * 0.6666F * animationSpeed) * 0.5F;
            rightArm.zRot = 2.3561945F;
            leftArm.zRot = -2.3561945F;
            rightArm.yRot = 0.0F;
            leftArm.yRot = 0.0F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static class AnimationCorpseWarlock {

        public static final AnimationDefinition VAMPIRE_ATTACK = AnimationDefinition.Builder.withLength(4.5f)
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(40f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-45f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(-45f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(-0.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-90f, 0f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-90f, 80f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(-90f, -20f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-90f, 0f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, -150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, -150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-90f, -80f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(-90f, 20f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightLeg",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 1f, -1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(0f, 2f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.posVec(0f, 2f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.posVec(0f, 1f, -1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("cloak",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition VAMPIRE_HEAL = AnimationDefinition.Builder.withLength(4.5f)
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-45f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(-45f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.posVec(-0.75f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.posVec(-0.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-90f, 0f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.posVec(0.75f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.posVec(0.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, -150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, -150f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-90f, 0f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightLeg",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.posVec(0f, 2f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5f, KeyframeAnimations.posVec(0f, 2f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.posVec(0f, 1f, -1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("cloak",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition TELEPORT = AnimationDefinition.Builder.withLength(1.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, -360f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 90f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, -90f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition TEAR_SPACE = AnimationDefinition.Builder.withLength(1.5f)
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-90f, 65f, -15f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(-122.5f, 22.5f, -15f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-50f, -10f, -15f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-90f, -65f, 15f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(-122.5f, -22.5f, 15f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-50f, 10f, 15f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("cloak",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.9167666f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.0834333f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition ROBUST_ATTACK = AnimationDefinition.Builder.withLength(5f)
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(6.67f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(21.25f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(32.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-20f, -20f, 30f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-20f, -20f, 30f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(-55f, -35f, 52.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(-90f, -50f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.degreeVec(0f, -20f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, -20f, 75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-20f, 20f, -30f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-20f, 20f, -30f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(-55f, 35f, -52.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(-90f, 50f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.degreeVec(0f, 20f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 20f, -75f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.degreeVec(0f, 0f, 10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, 10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(0f, 180f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.degreeVec(0f, 270f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 360f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 360f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftLeg",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 2f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(0f, 2f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.posVec(0f, 2f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.posVec(0f, 1f, -1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftLeg",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75f, KeyframeAnimations.degreeVec(0f, 0f, -10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5f, KeyframeAnimations.degreeVec(0f, 0f, -10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.958343f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
        public static final AnimationDefinition ATTACK = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(12.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(12.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("cloak",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(20f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(-28.92f, -8.09f, -18.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(-5.24f, 14.08f, 69.35f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(-28.92f, 8.09f, 18.17f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(-5.24f, -14.08f, -69.35f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR))).build();
    }
}