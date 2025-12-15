package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRelicEarthshaker;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicEarthshaker;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelRelicEarthshaker extends ModHierarchicalModel<EntityRelicEarthshaker> {
    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart upper;
    private final ModelPart cube_r1;
    private final ModelPart lower;

    public ModelRelicEarthshaker(ModelPart root) {
        this.root = root.getChild("root");
        this.cube_r1 = this.root.getChild("cube_r1");
        this.upper = this.root.getChild("upper");
        this.lower = this.root.getChild("lower");
        ModelPart body = this.upper.getChild("body");
        this.head = body.getChild("head");
        this.core = body.getChild("core");
        this.rightArm = body.getChild("rightArm");
        this.leftArm = body.getChild("leftArm");
        this.rightLeg = this.lower.getChild("rightLeg");
        this.leftLeg = this.lower.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(197, 12).addBox(-9.0F, -7.0F, -2.7F, 18.0F, 14.0F, 2.0F, new CubeDeformation(-0.16F))
                .texOffs(72, 6).addBox(-11.0F, -7.0F, -1.0F, 22.0F, 14.0F, 8.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -28.9F, 0.0F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.2F, 1.6F));
        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 28).addBox(-20.0F, -16.0F, -22.0F, 40.0F, 20.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.2F, 4.4F, 1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(-3.5F, -4.5F, -3.0F, 7.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0948F, -24.0716F, -12.2363F, 0.0F, 0.0F, 1.5708F));
        PartDefinition core = body.addOrReplaceChild("core", CubeListBuilder.create().texOffs(132, 16).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.2F, 6.4F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -21.2F, -16.6F));
        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -3.75F, -11.0F, 16.0F, 12.0F, 16.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 1.6755F, 0.0F, 0.0F));
        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(31, 80).addBox(-14.5F, -8.0F, -6.0F, 14.0F, 24.0F, 12.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(-19.5F, -27.2F, -1.6F, 0.0F, 0.0F, 0.0873F));
        PartDefinition cube_r5 = rightArm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 81).addBox(-3.5349F, -5.8744F, -2.6363F, 7.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 17.5F, -1.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition rightHand = rightArm.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(83, 82).addBox(-5.5F, -0.25F, -6.0F, 11.0F, 22.0F, 12.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(-12.5F, 16.25F, 0.0F, 0.0F, 0.0F, -0.1309F));
        PartDefinition finger1 = rightHand.addOrReplaceChild("finger1", CubeListBuilder.create(), PartPose.offset(-2.0F, 22.75F, -2.5F));
        PartDefinition cube_r6 = finger1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(145, 80).addBox(-1.5F, -5.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(0.0087F, 3.8002F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition finger2 = rightHand.addOrReplaceChild("finger2", CubeListBuilder.create(), PartPose.offset(-2.0F, 21.75F, 3.25F));
        PartDefinition cube_r7 = finger2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(145, 80).addBox(-1.5F, -5.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(0.0087F, 4.8002F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition finger3 = rightHand.addOrReplaceChild("finger3", CubeListBuilder.create(), PartPose.offset(3.75F, 21.75F, -2.5F));
        PartDefinition cube_r8 = finger3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(129, 82).addBox(-1.5F, -4.0F, -2.5F, 3.0F, 8.0F, 5.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(-0.9913F, 3.8002F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(31, 80).mirror().addBox(0.0F, -8.0F, -6.0F, 14.0F, 24.0F, 12.0F, new CubeDeformation(-0.08F)).mirror(false), PartPose.offsetAndRotation(20.0F, -27.2F, -1.6F, 0.0F, 0.0F, -0.0873F));
        PartDefinition cube_r9 = leftArm.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(-3.4651F, -5.8744F, -2.6363F, 7.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, 17.5F, -1.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition leftHand = leftArm.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(83, 82).mirror().addBox(-6.0F, 0.0F, -6.0F, 11.0F, 22.0F, 12.0F, new CubeDeformation(-0.08F)).mirror(false), PartPose.offsetAndRotation(11.5F, 16.0F, 0.0F, 0.0F, 0.0F, 0.1309F));
        PartDefinition finger4 = leftHand.addOrReplaceChild("finger4", CubeListBuilder.create(), PartPose.offset(1.5F, 23.0F, -2.5F));
        PartDefinition cube_r10 = finger4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(145, 80).mirror().addBox(-1.5F, -5.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(-0.08F)).mirror(false), PartPose.offsetAndRotation(-0.0087F, 3.8002F, 0.0F, 0.0F, -3.1416F, 0.0F));
        PartDefinition finger5 = leftHand.addOrReplaceChild("finger5", CubeListBuilder.create(), PartPose.offset(1.5F, 22.0F, 3.25F));
        PartDefinition cube_r11 = finger5.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(145, 80).mirror().addBox(-1.5F, -5.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(-0.08F)).mirror(false), PartPose.offsetAndRotation(-0.0087F, 4.8002F, 0.0F, 0.0F, -3.1416F, 0.0F));
        PartDefinition finger6 = leftHand.addOrReplaceChild("finger6", CubeListBuilder.create(), PartPose.offset(-3.25F, 22.0F, -2.5F));
        PartDefinition cube_r12 = finger6.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(129, 82).mirror().addBox(-1.5F, -4.0F, -2.5F, 3.0F, 8.0F, 5.0F, new CubeDeformation(-0.08F)).mirror(false), PartPose.offsetAndRotation(-0.0087F, 3.8002F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(144, 48).mirror().addBox(-6.0F, 0.0F, -6.0F, 12.0F, 20.0F, 12.0F, new CubeDeformation(-0.08F)).mirror(false), PartPose.offsetAndRotation(14.0F, -20.0F, 0.0F, 0.0F, 0.0F, -0.0175F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(144, 48).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 20.0F, 12.0F, new CubeDeformation(-0.08F)), PartPose.offsetAndRotation(-14.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0175F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityRelicEarthshaker entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        if (entity.isNoAnimation()) {
            if (!entity.isActive()) {
                setStaticRotationPoint(core, 0F, 0F, -5F);
                setStaticRotationAngle(rightArm, -35F, 0F, 0F);
                setStaticRotationPoint(rightArm, 0F, 8F, -5F);
                setStaticRotationAngle(leftArm, -35F, 0F, 0F);
                setStaticRotationPoint(leftArm, 0F, 8F, -5F);
                setStaticRotationAngle(upper, 35F, 0F, 0F);
                setStaticRotationPoint(upper, 0F, 3F, 0F);
            }
        }
        Animation animation = entity.getAnimation();
        if (animation == entity.electromagneticAnimation) {
            float amount = entity.electromagneticControlled.getAnimationFraction();
            this.core.x += (float) (Math.random() - 0.5) * 1.5F * amount;
            this.core.y += (float) (Math.random() - 0.5) * 1.5F * amount;
            this.root.yRot *= 1.0F - amount;
            this.root.x += (float) (Math.random() - 0.5) * amount;
            this.root.z += (float) (Math.random() - 0.5) * amount;
        }
        this.animate(entity.dieAnimation, AnimationRelicEarthshaker.DIE, ageInTicks);
        this.animate(entity.activeAnimation, AnimationRelicEarthshaker.ACTIVE, ageInTicks);
        this.animate(entity.deactivateAnimation, AnimationRelicEarthshaker.DEACTIVATE, ageInTicks);
        this.animate(entity.electromagneticAnimation, AnimationRelicEarthshaker.ELECTROMAGNETIC, ageInTicks);
        this.animate(entity.attackAnimationLeft, AnimationRelicEarthshaker.ATTACK_LEFT, ageInTicks);
        this.animate(entity.attackAnimationRight, AnimationRelicEarthshaker.ATTACK_RIGHT, ageInTicks);
        this.animate(entity.smashAttackAnimation, AnimationRelicEarthshaker.SMASH_ATTACK, ageInTicks);
        this.animate(entity.rangeAttackAnimation, AnimationRelicEarthshaker.RANGE_ATTACK, ageInTicks);
        this.animate(entity.rangeAttackStopAnimation, AnimationRelicEarthshaker.RANGE_ATTACK_END, ageInTicks);
        //LootAt & Idle & Walk
        if (!entity.isAlive() || !entity.isActive() || animation == entity.activeAnimation)
            return;
        float pitch = headPitch * 0.017453292F;
        float headYaw = netHeadYaw * 0.017453292F;
        float cycle = 0.5F;
        float idle = (Mth.sin(ageInTicks * cycle * 0.1F) + 1.0F) * (1.0F - limbSwingAmount);
        float rebound = limbSwing * cycle % Mth.PI / Mth.PI;
        rebound = 1.0F - rebound;
        rebound *= rebound;
        this.head.xRot += pitch - idle * 0.1F;
        this.head.yRot += headYaw * 0.5F;
        this.upper.yRot += headYaw * 0.25F;
        this.upper.y += Mth.sin(rebound * 3.1415927F) * 2.0F * limbSwingAmount;
        this.cube_r1.xRot += idle * 0.05F;
        this.leftArm.xRot += (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.1F - 0.2F - idle * -0.1F;
        this.leftArm.y += Mth.sin(rebound * 3.1415927F) * 2.5F * limbSwingAmount;
        this.rightArm.xRot += (Mth.cos(limbSwing * cycle + 3.1415927F) - 2.0F) * limbSwingAmount * 0.1F - 0.2F - idle * -0.1F;
        this.rightArm.y += Mth.sin(rebound * 3.1415927F) * 2.5F * limbSwingAmount;
        this.lower.y += Mth.sin(rebound * 3.1415927F) * 2.0F * limbSwingAmount;
        this.lower.yRot += headYaw * 0.25F;
        this.lower.zRot += Mth.sin(limbSwing * cycle) * limbSwingAmount * 0.05F;
        this.leftLeg.xRot += Mth.cos(limbSwing * cycle + 4.3982296F) * limbSwingAmount * 0.2F;
        this.leftLeg.y += Mth.clamp(Mth.sin(limbSwing * cycle) * limbSwingAmount * 3.0F, Float.NEGATIVE_INFINITY, 0F);
        this.leftLeg.z += Mth.cos(limbSwing * cycle + 3.1415927F) * limbSwingAmount * 8.0F;
        this.rightLeg.xRot += Mth.cos(limbSwing * cycle + 1.2566371F) * limbSwingAmount * 0.2F;
        this.rightLeg.y += Mth.clamp(Mth.sin(limbSwing * cycle + 3.1415927F) * limbSwingAmount * 3.0F, Float.NEGATIVE_INFINITY, 0F);
        this.rightLeg.z += Mth.cos(limbSwing * cycle) * limbSwingAmount * 8.0F;
    }
}
