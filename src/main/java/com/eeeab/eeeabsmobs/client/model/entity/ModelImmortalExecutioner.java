package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalExecutioner;
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

public class ModelImmortalExecutioner extends EMHierarchicalModel<EntityImmortalExecutioner> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart[] spine;
    private final ModelPart spine1;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public ModelImmortalExecutioner(ModelPart root) {
        this.root = root.getChild("root");
        ModelPart upper = this.root.getChild("upper");
        this.head = upper.getChild("head");
        ModelPart body = upper.getChild("body");
        this.leftArm = body.getChild("leftArm");
        this.rightArm = body.getChild("rightArm");
        this.spine = new ModelPart[]{
                this.spine1 = body.getChild("spine1"),
                body.getChild("spine1").getChild("spine2"),
                body.getChild("spine1").getChild("spine2").getChild("spine3")
        };
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -21.5F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -18.5F, -5.5F));
        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(-0.75F)), PartPose.offsetAndRotation(0.0F, -4.0F, 2.0F, 0.1742F, 0.7816F, 0.1231F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(64, 0).addBox(-0.5F, -4.0F, -3.0F, 1.0F, 8.0F, 6.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(5.5F, -8.0F, -6.0F, 0.0F, -0.6981F, 0.0F));
        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(49, 11).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 6.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(2.8214F, -18.5F, 3.383F, 2.9234F, -0.5236F, -3.1416F));
        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(49, 11).mirror().addBox(-0.5F, -2.5F, -2.5F, 1.0F, 6.0F, 5.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-2.8214F, -18.5F, 3.383F, 2.9234F, 0.5236F, -3.1416F));
        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(49, 11).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 6.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(3.8214F, -15.5F, -7.383F, 0.0F, -0.5236F, 0.0F));
        PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(49, 11).mirror().addBox(-0.5F, -2.5F, -2.5F, 1.0F, 6.0F, 5.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-3.8214F, -15.5F, -7.383F, 0.0F, 0.5236F, 0.0F));
        PartDefinition cube_r7 = body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-0.5F, -4.0F, -3.0F, 1.0F, 8.0F, 6.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-5.5F, -8.0F, -6.0F, 0.0F, 0.6981F, 0.0F));
        PartDefinition cube_r8 = body.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.5F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -14.0F, -1.5F, 0.3367F, -0.741F, -0.2339F));
        PartDefinition fire = body.addOrReplaceChild("fire", CubeListBuilder.create(), PartPose.offset(-0.0231F, -15.2513F, -3.262F));
        PartDefinition cube_r9 = fire.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1742F, 0.7816F, 0.1231F));
        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(9.4571F, -14.4272F, -0.8872F));
        PartDefinition cube_r10 = leftArm.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(37, 23).addBox(-2.0F, -3.0F, -4.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(-0.4571F, 1.5272F, 1.3872F, 0.2986F, 0.0651F, -0.2084F));
        PartDefinition leftArmUnder = leftArm.addOrReplaceChild("leftArmUnder", CubeListBuilder.create(), PartPose.offsetAndRotation(2.2886F, 8.3346F, 2.9979F, -0.4925F, 0.2334F, -0.0251F));
        PartDefinition cube_r11 = leftArmUnder.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(60, 21).addBox(-2.8787F, -6.9724F, -2.2117F, 5.0F, 14.0F, 6.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(1.1764F, 5.7857F, 1.074F, 0.2986F, 0.0651F, -0.2084F));
        PartDefinition claw1 = leftArmUnder.addOrReplaceChild("claw1", CubeListBuilder.create().texOffs(0, -1).addBox(-0.2313F, -0.46F, -0.6427F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.8039F, 10.1728F, 1.3583F));
        PartDefinition outer_claw1 = claw1.addOrReplaceChild("outer_claw1", CubeListBuilder.create().texOffs(0, 4).addBox(-0.2313F, -0.9113F, -1.4696F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.8727F, 0.0F, 0.0F));
        PartDefinition claw2 = leftArmUnder.addOrReplaceChild("claw2", CubeListBuilder.create().texOffs(0, -1).addBox(-0.2313F, -0.46F, -0.6427F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8039F, 9.3728F, 0.8583F, 0.0F, 0.0F, -0.1658F));
        PartDefinition outer_claw2 = claw2.addOrReplaceChild("outer_claw2", CubeListBuilder.create().texOffs(0, 4).addBox(-0.2313F, -0.9113F, -1.4696F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.8727F, 0.0F, 0.0F));
        PartDefinition claw3 = leftArmUnder.addOrReplaceChild("claw3", CubeListBuilder.create().texOffs(0, -1).addBox(-0.2313F, -0.46F, -0.6427F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2039F, 10.2728F, 1.3583F, 0.0F, 0.0F, -0.2182F));
        PartDefinition outer_claw3 = claw3.addOrReplaceChild("outer_claw3", CubeListBuilder.create().texOffs(0, 4).addBox(-0.2313F, -0.9113F, -1.4696F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.8727F, 0.0F, 0.0F));
        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-9.4571F, -14.4272F, -0.8872F));
        PartDefinition cube_r12 = rightArm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(37, 23).mirror().addBox(-3.0F, -3.0F, -4.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(0.4571F, 1.5272F, 1.3872F, 0.2986F, -0.0651F, 0.2084F));
        PartDefinition rightArmUnder = rightArm.addOrReplaceChild("rightArmUnder", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.2886F, 8.3346F, 2.9979F, -0.4925F, -0.2334F, 0.0251F));
        PartDefinition cube_r13 = rightArmUnder.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(60, 21).mirror().addBox(-2.1213F, -6.9724F, -2.2117F, 5.0F, 14.0F, 6.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(-1.1764F, 5.7857F, 1.074F, 0.2986F, -0.0651F, 0.2084F));
        PartDefinition claw4 = rightArmUnder.addOrReplaceChild("claw4", CubeListBuilder.create().texOffs(0, -1).mirror().addBox(0.2313F, -0.46F, -0.6427F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.8039F, 10.1728F, 1.3583F));
        PartDefinition outer_claw4 = claw4.addOrReplaceChild("outer_claw4", CubeListBuilder.create().texOffs(0, 4).mirror().addBox(0.2313F, -0.9113F, -1.4696F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.8727F, 0.0F, 0.0F));
        PartDefinition claw5 = rightArmUnder.addOrReplaceChild("claw5", CubeListBuilder.create().texOffs(0, -1).mirror().addBox(0.2313F, -0.46F, -0.6427F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.8039F, 9.3728F, 0.8583F, 0.0F, 0.0F, 0.1658F));
        PartDefinition outer_claw5 = claw5.addOrReplaceChild("outer_claw5", CubeListBuilder.create().texOffs(0, 4).mirror().addBox(0.2313F, -0.9113F, -1.4696F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.8727F, 0.0F, 0.0F));
        PartDefinition claw6 = rightArmUnder.addOrReplaceChild("claw6", CubeListBuilder.create().texOffs(0, -1).mirror().addBox(0.2313F, -0.46F, -0.6427F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.2039F, 10.2728F, 1.3583F, 0.0F, 0.0F, 0.2182F));
        PartDefinition outer_claw6 = claw6.addOrReplaceChild("outer_claw6", CubeListBuilder.create().texOffs(0, 4).mirror().addBox(0.2313F, -0.9113F, -1.4696F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.8727F, 0.0F, 0.0F));
        PartDefinition spine1 = body.addOrReplaceChild("spine1", CubeListBuilder.create(), PartPose.offset(0.021F, -3.1512F, 1.2201F));
        PartDefinition cube_r14 = spine1.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 58).addBox(-4.0F, 0.5F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.021F, -4.8488F, -1.2201F, 0.3335F, -0.7295F, -0.2312F));
        PartDefinition spine2 = spine1.addOrReplaceChild("spine2", CubeListBuilder.create(), PartPose.offset(0.0188F, 8.3274F, 3.0855F));
        PartDefinition cube_r15 = spine2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 41).addBox(-3.5F, -4.5F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4882F, -0.6912F, -0.3347F));
        PartDefinition spine3 = spine2.addOrReplaceChild("spine3", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 4.5F));
        PartDefinition cube_r16 = spine3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(29, 42).addBox(-3.5F, -4.5F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6279F, -0.6353F, -0.4209F));
        PartDefinition leftArmor = body.addOrReplaceChild("leftArmor", CubeListBuilder.create(), PartPose.offsetAndRotation(5.5F, -14.559F, -1.3601F, 0.0F, 0.0F, -0.2618F));
        PartDefinition cube_r17 = leftArmor.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(58, 52).addBox(-4.5F, -1.0F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(3.0F, -0.5436F, 4.1997F, -0.6545F, 0.0F, 0.0F));
        PartDefinition cube_r18 = leftArmor.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(58, 43).addBox(-4.5F, -1.5F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(3.0F, 1.7346F, -3.6268F, 1.0472F, 0.0F, 0.0F));
        PartDefinition cube_r19 = leftArmor.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(37, 2).addBox(-5.5F, -0.5F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 1.059F, 0.3601F, 0.2382F, -0.1096F, 0.4232F));
        PartDefinition cube_r20 = leftArmor.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(37, 2).addBox(-4.5F, -1.5F, -3.0F, 7.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -0.941F, -0.1399F, 0.2618F, 0.0F, 0.0F));
        PartDefinition rightArmor = body.addOrReplaceChild("rightArmor", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.5F, -14.559F, -1.3601F, 0.0F, 0.0F, 0.2618F));
        PartDefinition cube_r21 = rightArmor.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(37, 2).mirror().addBox(-1.5F, -0.5F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.5F, 1.059F, 0.3601F, 0.2382F, 0.1096F, -0.4232F));
        PartDefinition cube_r22 = rightArmor.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(58, 52).mirror().addBox(-2.5F, -1.0F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -0.5436F, 4.1997F, -0.6545F, 0.0F, 0.0F));
        PartDefinition cube_r23 = rightArmor.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(58, 43).mirror().addBox(-2.5F, -1.5F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 1.7346F, -3.6268F, 1.0472F, 0.0F, 0.0F));
        PartDefinition cube_r24 = rightArmor.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(37, 2).mirror().addBox(-2.5F, -1.5F, -3.0F, 7.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -0.941F, -0.1399F, 0.2618F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityImmortalExecutioner entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //Idle
        float speed = 0.1F;
        float degree = 0.6F;
        this.bob(root, speed, degree, false, frame, 1);
        this.bob(root, speed, degree + 0.2F, false, limbSwing, limbSwingAmount);
        this.bob(head, speed, degree * 0.4F, false, frame, 1);
        this.chainSwing(spine, speed + 0.1F, degree, -3, limbSwing, limbSwingAmount);
        this.chainSwing(spine, speed, degree * 0.25F, -1.5, frame, 1);
        this.flap(rightArm, speed + 0.05F, degree * 0.08F, true, 0, -0.1F, frame, 1);
        this.flap(leftArm, speed + 0.05F, degree * 0.08F, false, 0, -0.1F, frame, 1);
        //Walk
        float cycle = 0.2F;
        float idle = (Mth.sin(ageInTicks * cycle * 0.1F) + 1.0F) * (1.0F - limbSwingAmount);
        float dampingFactor = 1.0F - limbSwingAmount * 0.45F; // 引入的衰减因子，根据移动速度调整
        this.root.xRot -= (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.1F * dampingFactor - 0.1F * (-idle) * -0.1F;
        this.spine1.xRot -= (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.2F * dampingFactor - 0.1F * (-idle) * -0.1F;
        //this.head.xRot += (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.1F * dampingFactor - 0.1F * (-idle) * -0.1F;
        this.animate(entity.dieAnimation, AnimationImmortalExecutioner.DIE, ageInTicks);
        this.animate(entity.avoidAnimation, AnimationImmortalExecutioner.AVOID, ageInTicks);
        this.animate(entity.blockAnimation, AnimationImmortalExecutioner.BLOCK, ageInTicks);
        this.animate(entity.counterAnimation, AnimationImmortalExecutioner.COUNTER, ageInTicks);
        this.animate(entity.attackAnimationLeft, AnimationImmortalExecutioner.ATTACK_LEFT, ageInTicks);
        this.animate(entity.attackAnimationRight, AnimationImmortalExecutioner.ATTACK_RIGHT, ageInTicks);
        this.animate(entity.sidesWayAnimationLeft, AnimationImmortalExecutioner.SIDESWAY_LEFT, ageInTicks);
        this.animate(entity.sidesWayAnimationRight, AnimationImmortalExecutioner.SIDESWAY_RIGHT, ageInTicks);
        this.animate(entity.impactStorageAnimation, AnimationImmortalExecutioner.IMPACT_STORAGE, ageInTicks);
        this.animate(entity.impactHoldAnimation, AnimationImmortalExecutioner.IMPACT_HOLD, ageInTicks);
        this.animate(entity.impactStopAnimation, AnimationImmortalExecutioner.IMPACT_STOP, ageInTicks);
        this.animate(entity.cullStorageAnimation, AnimationImmortalExecutioner.CULL_STORAGE, ageInTicks);
        this.animate(entity.cullHoldAnimation, AnimationImmortalExecutioner.CULL_HOLD, ageInTicks);
        this.animate(entity.cullStopAnimation, AnimationImmortalExecutioner.CULL_STOP, ageInTicks);
    }

    @OnlyIn(Dist.CLIENT)
    private static class AnimationImmortalExecutioner {

        public static final AnimationDefinition DIE = AnimationDefinition.Builder.withLength(1.75f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.posVec(0f, -1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, -1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7916766f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.9583434f, KeyframeAnimations.posVec(0f, 0.33f, 4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.125f, KeyframeAnimations.posVec(0f, -6f, 7f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.1676667f, KeyframeAnimations.posVec(0f, -5f, 7f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, -6f, 7f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(67.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("fire",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.125f, KeyframeAnimations.posVec(0f, -6f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(1.1676667f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(17.5f, 0f, -15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(17.36f, -2.25f, -7.84f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.125f, KeyframeAnimations.degreeVec(-5f, 0f, -15f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.125f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2916767f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.041676664f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(17.5f, 0f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7916766f, KeyframeAnimations.degreeVec(17.36f, 2.25f, 7.84f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.1676667f, KeyframeAnimations.degreeVec(-5f, 0f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.041676664f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7916766f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.1676667f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(1.1676667f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7916766f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.125f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2083433f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine2",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.375f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine2",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(1.0416767f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.1676667f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.375f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine3",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(1.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.375f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.4167667f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine3",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(1.125f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2083433f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.375f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.4167667f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.125f, KeyframeAnimations.degreeVec(-77.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2916767f, KeyframeAnimations.degreeVec(-47.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.041676664f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7916766f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.1676667f, KeyframeAnimations.degreeVec(-77.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.3433333f, KeyframeAnimations.degreeVec(-47.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition BLOCK = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, -1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.posVec(0f, -1.04f, 3.04f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(0f, -1.04f, 3.04f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-99.48f, 28.32f, -21.4f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-99.48f, 28.32f, -21.4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-35f, 22.5f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-35f, 22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(-1f, -1f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(-1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-99.48f, -28.32f, 21.4f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-99.48f, -28.32f, 21.4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-17.5f, -22.5f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-17.5f, -22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition AVOID = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.posVec(0f, -1f, -1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.posVec(0f, -1f, -1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(-12.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(0f, 0f, -10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(0f, 0f, 10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-12.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-23.33f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(-15.16f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.6766666f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition COUNTER = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, -1.04f, 3.04f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, -0.04f, 3.04f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0.04f, -3.04f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(0f, 0.04f, -3.04f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(0f, 0f, 0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(12.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.625f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.posVec(1f, -1f, -1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-99.48f, 28.32f, -21.4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-111.98f, 28.32f, -21.4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-49.44f, -9.21f, -49.3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(15.49f, -4.73f, -41.48f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(15.49f, -4.73f, -41.48f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-35f, 22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-35f, 22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(11.87f, 12.88f, 7.02f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-26.9f, 14.76f, 15.19f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(-1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.posVec(-1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.posVec(-1f, -1f, -1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.posVec(-1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.posVec(-1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-99.48f, -28.32f, 21.4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-104.11f, -25.92f, 20.59f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-49.44f, 9.21f, 49.3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(15.49f, 4.73f, 41.48f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(15.49f, 4.73f, 41.48f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, -22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-17.5f, -22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(11.87f, -12.88f, -7.02f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-26.9f, -14.76f, -15.19f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition ATTACK_LEFT = AnimationDefinition.Builder.withLength(0.8343334f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(1f, 2f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.posVec(0f, -1f, -5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(-0.06f, -0.69f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, -20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, 20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.posVec(0f, -0.5f, -0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 12.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(7.33f, -12.54f, 0.67f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-124.02f, -35.4f, -8.35f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-61.05f, 4.76f, 20.74f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-61.05f, 4.76f, 20.74f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, -60f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(38.29f, -60.52f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(42.5f, -60f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.degreeVec(28.66f, -25.04f, 0.13f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(11.24f, 0.33f, -6.7f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(11.24f, 0.33f, -6.7f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(7.25f, -1.94f, 14.88f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition ATTACK_RIGHT = AnimationDefinition.Builder.withLength(0.8343334f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(-1f, 2f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.posVec(0f, -1f, -5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0.06f, -0.69f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, -20f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.posVec(0f, -0.5f, -0.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, -12.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(7.33f, 12.54f, -0.67f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(7.25f, 1.94f, -14.88f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(11.24f, -0.33f, 6.7f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(11.24f, -0.33f, 6.7f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-124.02f, 35.4f, 8.35f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-61.05f, -4.76f, -20.74f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-61.05f, -4.76f, -20.74f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 60f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(38.29f, 60.52f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5416766f, KeyframeAnimations.degreeVec(42.5f, 60f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083434f, KeyframeAnimations.degreeVec(28.66f, 25.04f, -0.13f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition IMPACT_STORAGE = AnimationDefinition.Builder.withLength(3f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(0f, 1.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 2f, 4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(0f, 2f, 4f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, -2f, -2f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.posVec(0f, -2f, -2f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(35f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(35f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(33.65f, 23.04f, -30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(33.65f, 23.04f, -30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-72.39f, 4.99f, 0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-72.39f, 4.99f, 0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw2",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw3",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, -25f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, -25f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 25f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 25f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(33.65f, -23.04f, 30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(33.65f, -23.04f, 30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-72.39f, -4.99f, -0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-72.39f, -4.99f, -0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw4",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw6",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition IMPACT_HOLD = AnimationDefinition.Builder.withLength(1f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 1.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 1.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 2f, 4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 2f, 4f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0f, -5f, -7f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-15f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(20f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, -2f, -2f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, -2f, -2f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(35f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(35f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(33.65f, 23.04f, -30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(33.65f, 23.04f, -30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-77.31f, 18.73f, -33.18f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-72.39f, 4.99f, 0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-72.39f, 4.99f, 0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-16.74f, -14.13f, 20.5f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw2",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw3",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, -25f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, -25f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-10f, 0f, -15f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 25f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 25f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-10f, 0f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(33.65f, -23.04f, 30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(33.65f, -23.04f, 30.45f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-77.31f, -18.73f, 33.18f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-72.39f, -4.99f, -0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-72.39f, -4.99f, -0.39f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-16.74f, 14.13f, -20.5f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw4",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw6",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition IMPACT_STOP = AnimationDefinition.Builder.withLength(0.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 1.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, -5f, -7f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("upper",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(20f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-77.31f, 18.73f, -33.18f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-16.74f, -14.13f, 20.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw2",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw3",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-10f, 0f, -15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-10f, 0f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-77.31f, -18.73f, 33.18f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-16.74f, 14.13f, -20.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw4",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw6",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-30f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition SIDESWAY_LEFT = AnimationDefinition.Builder.withLength(0.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.posVec(3f, -2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(7.5f, 0f, 10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 0f, 7.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 0f, 7.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.degreeVec(0f, 0f, 10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition SIDESWAY_RIGHT = AnimationDefinition.Builder.withLength(0.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.posVec(-3f, -2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(7.5f, 0f, -10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 0f, -7.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 0f, -7.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.degreeVec(0f, 0f, -10f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition CULL_STORAGE = AnimationDefinition.Builder.withLength(1f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0f, -2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-5f, 11.25f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(0f, 22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(0f, 22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.posVec(0.1f, -0.3f, -0.2f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.posVec(0.1f, -0.3f, -0.2f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(7.84f, -22.43f, -1.06f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(7.84f, -22.43f, -1.06f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-14.15f, -31.17f, 11.55f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(-14.15f, -31.17f, 11.55f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(5f, 0f, 5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(5f, 0f, 5f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(0f, 5f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(0f, 5f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.degreeVec(2.14f, -21.59f, 60.57f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(28.4f, -27.03f, 33.91f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(28.4f, -27.03f, 33.91f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.posVec(1f, -2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.posVec(1f, -2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-75.63f, 0.72f, -119.6f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(-75.63f, 0.72f, -119.6f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw4",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-47.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-47.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw6",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-47.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(-9.99f, -0.43f, -2.46f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.degreeVec(-9.99f, -0.43f, -2.46f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("claw5",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.scaleVec(1.5f, 1.5f, 1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.875f, KeyframeAnimations.scaleVec(1.5f, 1.5f, 1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition CULL_HOLD = AnimationDefinition.Builder.withLength(0.75f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.posVec(-1f, -1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3433333f, KeyframeAnimations.posVec(2f, 0f, -5f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 22.5f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(5.31f, 37.48f, 0.55f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(4.48f, -19.84f, -4.21f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0.1f, -0.3f, -0.2f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(7.84f, -22.43f, -1.06f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(4.57f, -25.74f, 6.81f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(17.05f, 11.21f, 10.21f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(7.25f, 2.99f, -9.55f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(14.61f, 3.73f, -11.94f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-14.15f, -31.17f, 11.55f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(5f, 0f, 5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(5f, 0f, 5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.degreeVec(9.96f, -0.65f, 12.47f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 5f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 5f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-35f, 5f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(28.4f, -27.03f, 33.91f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(49.41f, -6.63f, 11.19f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(43.95f, -30.56f, 37.95f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-131.38f, -27.69f, 90.33f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(1f, -2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.posVec(1f, -2f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4583433f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-75.63f, 0.72f, -119.6f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-107.37f, -4.47f, -150.26f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-129.87f, -4.47f, -150.26f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw4",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw6",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-9.99f, -0.43f, -2.46f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("claw5",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1.5f, 1.5f, 1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
        public static final AnimationDefinition CULL_STOP = AnimationDefinition.Builder.withLength(0.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(2f, 0f, -5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(4.48f, -19.84f, -4.21f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("head",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(17.05f, 11.21f, 10.21f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(14.61f, 3.73f, -11.94f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("leftArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-14.15f, -31.17f, 11.55f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("spine1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(9.96f, -0.65f, 12.47f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmor",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-35f, 5f, 15f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, -3f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArm",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-131.38f, -27.69f, 90.33f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(1f, -1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("rightArmUnder",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-129.87f, -4.47f, -150.26f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw4",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("outer_claw6",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("claw5",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(-9.99f, -0.43f, -2.46f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("claw5",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1.5f, 1.5f, 1.5f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
    }
}