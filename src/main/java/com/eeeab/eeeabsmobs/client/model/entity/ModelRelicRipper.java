package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRelicRipper;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicRipper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelRelicRipper extends ModHierarchicalModel<EntityRelicRipper> {
    private final ModelPart root;
    private final ModelPart upper;
    private final ModelPart rightArm;
    private final ModelPart rightHand;
    private final ModelPart saw;

    public ModelRelicRipper(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.rightArm = this.upper.getChild("rightArm");
        this.rightHand = this.rightArm.getChild("rightArmJoint").getChild("rightHand");
        this.saw = this.rightHand.getChild("sawJoint").getChild("saw");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -28.2992F, -2.9734F));

        PartDefinition cube_r1 = upper.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-14.0F, -4.5F, -4.5F, 28.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.2461F, -0.0981F, 0.0785F, 0.0F, 0.0F));

        PartDefinition cube_r2 = upper.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -8.0F, -8.5F, 13.0F, 16.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.7539F, 0.0981F, 0.0785F, 0.0F, 0.0F));

        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-13.0F, -10.7008F, -1.0266F));

        PartDefinition cube_r3 = rightArm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 11).addBox(-2.5F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -0.2265F, 0.1986F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r4 = rightArm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(52, 69).addBox(-3.5F, -6.0342F, -4.0221F, 7.0F, 20.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, -0.7564F, 0.6555F, 0.1309F, 0.0F, 0.0F));

        PartDefinition rightArmJoint = rightArm.addOrReplaceChild("rightArmJoint", CubeListBuilder.create(), PartPose.offset(-7.0F, 14.6588F, -0.4352F));

        PartDefinition cube_r5 = rightArmJoint.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.5F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition rightHand = rightArmJoint.addOrReplaceChild("rightHand", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.0F, -2.5F));

        PartDefinition cube_r6 = rightHand.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 89).addBox(-2.5F, -0.9294F, -3.4222F, 6.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.4222F, 2.0706F, -1.5708F, 0.0F, 0.0F));

        PartDefinition sawJoint = rightHand.addOrReplaceChild("sawJoint", CubeListBuilder.create(), PartPose.offset(-0.9F, -0.0778F, -10.4294F));

        PartDefinition cube_r7 = sawJoint.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(43, 0).addBox(-1.5F, -1.9294F, -2.4222F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition saw = sawJoint.addOrReplaceChild("saw", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.6F, -0.081F, -6.1355F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r8 = saw.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 33).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -1.8483F, 0.0132F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r9 = saw.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(70, 41).addBox(-5.3536F, -2.6F, -4.6464F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -0.5F, 0.0F, 0.7854F, 0.0F));

        PartDefinition blade1 = saw.addOrReplaceChild("blade1", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, 3.5F, 0.0873F, 0.0F, 0.1309F));

        PartDefinition cube_r10 = blade1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(93, 54).addBox(-5.3432F, -2.076F, -2.0215F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(1.2701F, 0.6468F, 4.7571F, -0.0632F, 0.2196F, 0.048F));

        PartDefinition cube_r11 = blade1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(27, 87).addBox(-4.4442F, -2.549F, -3.6665F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(1.4521F, 1.1468F, 0.9538F, 0.0F, 0.7854F, 0.0F));

        PartDefinition blade2 = saw.addOrReplaceChild("blade2", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 0.0F, -3.5F, -0.0873F, 0.0F, -0.1309F));

        PartDefinition cube_r12 = blade2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(27, 77).addBox(-0.8698F, -2.1014F, -2.0018F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(-1.2701F, 0.734F, -5.7532F, 0.0632F, 0.2196F, -0.048F));

        PartDefinition cube_r13 = blade2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(80, 85).addBox(-4.2602F, -2.6362F, -4.6291F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-1.4521F, 1.234F, -1.9499F, 0.0F, 0.7854F, 0.0F));

        PartDefinition blade3 = saw.addOrReplaceChild("blade3", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, -3.5F, 0.1304F, 0.0114F, -0.0865F));

        PartDefinition cube_r14 = blade3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(78, 97).addBox(-0.2843F, -1.993F, -2.3832F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(4.7831F, 0.5168F, -2.2469F, 0.2802F, -1.346F, -0.2117F));

        PartDefinition cube_r15 = blade3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(87, 0).addBox(-3.5615F, -2.5276F, -4.6267F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.9651F, 1.0168F, -2.4436F, 0.0F, -0.7854F, 0.0F));

        PartDefinition blade4 = saw.addOrReplaceChild("blade4", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 0.0F, 4.5F, -0.1304F, 0.0114F, 0.0865F));

        PartDefinition cube_r16 = blade4.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(56, 96).addBox(-4.7577F, -2.1844F, -2.4029F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(-4.7604F, 0.7769F, 0.264F, -0.2802F, -1.346F, 0.2117F));

        PartDefinition cube_r17 = blade4.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(80, 73).addBox(-3.7455F, -2.6576F, -3.6642F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-0.9424F, 1.2769F, 0.4607F, 0.0F, -0.7854F, 0.0F));

        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, -24.7597F, -4.1693F));

        PartDefinition cube_r18 = lower.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -1.5F, -1.0F, 7.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.3403F, -4.0307F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r19 = lower.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(43, 0).addBox(-6.0F, -2.0F, -10.0F, 16.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.2597F, 6.1693F, 0.0349F, 0.0F, 0.0F));

        PartDefinition cube_r20 = lower.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(33, 51).addBox(-4.0F, -3.0F, -13.0F, 11.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -1.7403F, 7.6693F, 0.0349F, 0.0F, 0.0F));

        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(9.32F, 0.5688F, 5.132F, -0.48F, -0.0436F, 0.0F));

        PartDefinition cube_r21 = leftLeg.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 51).addBox(-3.5F, -5.2071F, -3.2218F, 7.0F, 7.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.78F, 10.2428F, -5.7874F, 0.9599F, 0.0F, 0.0F));

        PartDefinition leftLegJoint1 = leftLeg.addOrReplaceChild("leftLegJoint1", CubeListBuilder.create(), PartPose.offset(1.78F, 9.8558F, -4.8484F));

        PartDefinition cube_r22 = leftLegJoint1.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(94, 12).addBox(-2.4782F, -6.6886F, -3.2688F, 5.0F, 15.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, -0.3876F, 5.0198F, 1.7017F, 0.0F, 0.0F));

        PartDefinition leftLegJoint2 = leftLegJoint1.addOrReplaceChild("leftLegJoint2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5334F, 11.2075F, 0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r23 = leftLegJoint2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(65, 16).addBox(-2.5F, -3.0F, -2.3431F, 5.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5872F, -6.5038F, 0.7854F, 0.0F, 0.0F));

        PartDefinition leftFoot = leftLegJoint2.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(0, 77).addBox(-4.5F, 3.5F, -3.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(73, 57).addBox(-2.0F, 2.6F, -4.5F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0872F, -8.0038F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r24 = leftFoot.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(33, 51).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.32F, 0.5688F, 5.132F, -0.48F, 0.0436F, 0.0F));

        PartDefinition cube_r25 = rightLeg.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 51).mirror().addBox(-3.5F, -5.2071F, -3.2218F, 7.0F, 7.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.78F, 10.2428F, -5.7874F, 0.9599F, 0.0F, 0.0F));

        PartDefinition rightLegJoint1 = rightLeg.addOrReplaceChild("rightLegJoint1", CubeListBuilder.create(), PartPose.offset(-1.78F, 9.8558F, -4.8484F));

        PartDefinition cube_r26 = rightLegJoint1.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(94, 12).mirror().addBox(-2.5218F, -6.6886F, -3.2688F, 5.0F, 15.0F, 5.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.3876F, 5.0198F, 1.7017F, 0.0F, 0.0F));

        PartDefinition rightLegJoint2 = rightLegJoint1.addOrReplaceChild("rightLegJoint2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5334F, 11.2075F, 0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r27 = rightLegJoint2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(65, 16).mirror().addBox(-2.5F, -3.0F, -2.3431F, 5.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.5872F, -6.5038F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rightFoot = rightLegJoint2.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(0, 77).mirror().addBox(-4.5F, 3.5F, -3.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(73, 57).mirror().addBox(-2.0F, 2.6F, -4.5F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 9.0872F, -8.0038F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r28 = rightFoot.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(33, 51).mirror().addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.1309F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityRelicRipper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1.2F, this.upper);
        //Idle & Walk
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        if (entity.isActive() && entity.isAlive()) {
            float walkSpeed = 0.2F;
            float walkDegree = 0.2F;
            this.bob(this.upper, walkSpeed, walkDegree, false, frame, 1);
            this.walk(this.rightArm, walkSpeed * 0.3F, walkDegree * 0.3F, false, 0F, 0F, frame, 1);
        }
        float progress = entity.sawControlled.getAnimationFraction(delta);
        float timeBasedRotation = (ageInTicks * 50F) % 360F;
        this.saw.yRot = (-timeBasedRotation * progress) * ((float) Math.PI / 180F);
        this.rightHand.y = (float) (Math.sin(ageInTicks * 3) * 0.6F + Math.cos(ageInTicks * 3 * 1.7F) * 0.4F) * 0.5F * progress;
        this.animateWalk(AnimationRelicRipper.WALK, limbSwing, limbSwingAmount, 2F, 2F);
        //Animation
        this.animate(entity.dieAnimation, AnimationRelicRipper.DIE, ageInTicks);
        this.animate(entity.activeAnimation, AnimationRelicRipper.ACTIVE, ageInTicks);
        this.animate(entity.deactivateAnimation, AnimationRelicRipper.DEACTIVATE, ageInTicks);
        this.animate(entity.sweep1Animation, AnimationRelicRipper.SWEEP1, ageInTicks);
        this.animate(entity.sweep2Animation, AnimationRelicRipper.SWEEP2, ageInTicks);
        this.animate(entity.smashAnimation, AnimationRelicRipper.SMASH, ageInTicks);
        this.animate(entity.cuttingStartAnimation, AnimationRelicRipper.CUTTING_START, ageInTicks);
        this.animate(entity.cuttingKeepAnimation, AnimationRelicRipper.CUTTING_KEEP, ageInTicks);
        this.animate(entity.cuttingEndAnimation, AnimationRelicRipper.CUTTING_END, ageInTicks);
    }
}
