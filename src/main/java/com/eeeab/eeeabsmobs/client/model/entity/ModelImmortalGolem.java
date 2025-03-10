package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationCommon;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationImmortalGolem;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class ModelImmortalGolem extends EMHierarchicalModel<EntityImmortalGolem> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ModelImmortalGolem(ModelPart root) {
        this.root = root.getChild("root");
        ModelPart upper = this.root.getChild("upper");
        this.head = upper.getChild("head");
        this.body = upper.getChild("body");
        this.leftArm = body.getChild("leftArm");
        this.rightArm = body.getChild("rightArm");
        ModelPart lower = this.root.getChild("lower");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftLeg = lower.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, -18.0F, 0.0F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));
        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(33, 0).addBox(-1.5F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(33, 0).mirror().addBox(-0.5F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(42, 0).addBox(-1.0F, 1.0F, -1.1F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -9.0F, 0.1F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(42, 0).mirror().addBox(-1.0F, 1.0F, -1.1F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -9.0F, 0.1F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityImmortalGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        //Walk
        float walkSpeed = 0.8F;
        float walkDegree = 0.8F;
        this.flap(this.root, walkSpeed, walkDegree * 0.05F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftArm, walkSpeed, walkDegree * 0.2F, false, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        this.walk(this.rightArm, walkSpeed, walkDegree * 0.2F, true, 0.0F, -0.05F, limbSwing, limbSwingAmount);
        //Idle
        float speed = 0.08F;
        float degree = 0.05F;
        if (entity.isAlive()) {
            this.walk(head, speed, degree, false, 0.5F, -0.05F, frame, 1);
            this.walk(rightArm, speed, degree, true, 0, -0.05F, frame, 1);
            this.swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.walk(leftArm, speed, degree, false, 0, -0.05F, frame, 1);
            this.swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
        if (entity.getAnimation() == entity.spawnAnimation) {
            setStaticRotationAngle(leftArm, 90, 0, 0);
            setStaticRotationAngle(rightArm, 90, 0, 0);
        }
        if (entity.isDangerous() && entity.getAnimation() != entity.spawnAnimation) {
            setStaticRotationAngle(leftArm, 20, 25, 0);
            setStaticRotationAngle(rightArm, 20, -25, 0);
        }
        this.animate(entity.spawnAnimation, AnimationCommon.SPAWN, ageInTicks);
        this.animate(entity.hurtAnimation, AnimationImmortalGolem.HURT, ageInTicks);
        this.animate(entity.attackAnimation, AnimationImmortalGolem.ATTACK, ageInTicks);
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        boolean flag = humanoidArm == HumanoidArm.RIGHT;
        ModelPart model$part = flag ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        model$part.translateAndRotate(poseStack);
        poseStack.scale(0.6F, 0.6F, 0.6F);
        this.offsetStackPosition(poseStack, flag);
    }

    private void offsetStackPosition(PoseStack translate, boolean isRightArm) {
        if (isRightArm) {
            translate.translate(0.125, 0.1625, 0);
        } else {
            translate.translate(-0.125, 0.1625, 0);
        }
    }
}