package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationCommon;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationImmortalSkeleton;
import com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton.EntityAbsImmortalSkeleton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

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
                .texOffs(0, 31).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(32, 41).addBox(-1.0F, -9.0F, -5.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(38, 41).addBox(3.5F, -7.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F))
                .texOffs(50, 41).addBox(4.5F, -10.0F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F))
                .texOffs(50, 41).mirror().addBox(-6.5F, -10.0F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(38, 41).mirror().addBox(-6.5F, -7.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offset(0.0F, -12.0F, 0.0F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(26, 17).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 47).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(9, 16).addBox(-0.1038F, -1.4128F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -10.5F, 0.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition cube_r1 = leftArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 47).addBox(0.0F, -1.6F, -3.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.1038F, -0.4128F, 1.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(9, 16).mirror().addBox(-1.9924F, -1.3257F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -10.5F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition cube_r2 = rightArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 47).mirror().addBox(-4.0F, -1.6F, -3.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.0038F, -0.4128F, 1.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(1.0F, -12.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.2F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 47).addBox(-2.2F, -0.2F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.22F)), PartPose.offset(2.2F, 0.0F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 47).mirror().addBox(-2.0F, -0.2F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.22F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 0.0F));
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
            float cycle = 0.65F;
            this.walk(this.upper, 0.1F, 0.005F, true, 0, -0.025F, frame, 1);
            this.bob(this.upper, cycle * 0.2F, cycle * 0.2F, false, frame, 1);
            this.bob(this.leftArm, cycle * 0.2F, cycle * 0.2F, false, frame, 1);
            this.bob(this.rightArm, cycle * 0.2F, cycle * 0.2F, false, frame, 1);
            this.flap(this.leftArm, cycle, cycle * 0.2F, true, 0, 0.15F, limbSwing, limbSwingAmount);
            this.flap(this.rightArm, cycle, cycle * 0.2F, true, 0, -0.15F, limbSwing, limbSwingAmount);
            this.bob(this.head, cycle * -0.2F, cycle * -0.2F, false, frame, 1);
            this.bob(this.head, cycle, cycle * 0.6F, false, limbSwing, limbSwingAmount);
            if (entity.getAnimation() != entity.blockAnimation) {
                this.walk(this.leftArm, 0.15F, 0.05F, true, 0.15F, 0, frame, 1);
                this.walk(this.rightArm, 0.15F, 0.05F, false, 0.15F, 0, frame, 1);
            }
            this.walk(this.leftLeg, cycle, cycle * 1.4F, false, 0, 0, limbSwing, limbSwingAmount);
            this.walk(this.rightLeg, cycle, cycle * 1.4F, true, 0, 0, limbSwing, limbSwingAmount);
            if (entity.getVariant() == EntityAbsImmortalSkeleton.CareerType.ARCHER) {
                this.animateWalk(AnimationImmortalSkeleton.ARCH, limbSwing, limbSwingAmount, 1F, 2F);
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
        poseStack.translate(0D, -0.005D, 0.058D);
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
