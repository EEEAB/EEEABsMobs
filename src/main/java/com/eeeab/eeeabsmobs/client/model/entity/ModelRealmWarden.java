package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.animation.KeyframeAnimations;
import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRealmWarden;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRealmWarden2;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRealmWarden;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRealmWarden extends ModHierarchicalModel<EntityRealmWarden> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart exhaust1;
    private final ModelPart exhaust2;
    private final ModelPart exhaust3;

    public ModelRealmWarden(ModelPart root) {
        this.root = root.getChild("root");
        ModelPart upper = this.root.getChild("upper");
        this.head = upper.getChild("head");
        this.exhaust1 = upper.getChild("mainBooster").getChild("exhaust1");
        this.leftArm = upper.getChild("leftArm");
        this.exhaust2 = leftArm.getChild("leftArmUnder").getChild("leftGlove").getChild("exhaust2");
        this.rightArm = upper.getChild("rightArm");
        this.exhaust3 = rightArm.getChild("rightArmUnder").getChild("rightGlove").getChild("exhaust3");
        this.exhaust1.visible = false;
        this.exhaust2.visible = false;
        this.exhaust3.visible = false;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -35.68F, 0.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r1 = upper.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-29.817F, -3.2154F, -3.3638F, 34.0F, 7.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(12.5F, -16.32F, 1.0F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r2 = upper.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 14).addBox(-10.0F, -5.6443F, -4.946F, 20.0F, 13.0F, 13.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, -13.82F, -1.5F, 0.6109F, 0.0F, 0.0F));
        PartDefinition cube_r3 = upper.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(115, 115).addBox(-4.7044F, -8.4128F, -4.2956F, 9.0F, 11.0F, 9.0F, new CubeDeformation(-0.6F)), PartPose.offsetAndRotation(0.0F, -3.82F, 0.5F, 0.0F, 0.7854F, 0.0F));
        PartDefinition cube_r4 = upper.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(49, 23).mirror().addBox(-3.1188F, -2.8379F, -8.9242F, 8.0F, 10.0F, 17.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-4.9245F, -17.677F, -0.9538F, 0.5603F, -0.0934F, 0.1476F));
        PartDefinition cube_r5 = upper.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(49, 23).addBox(-4.8812F, -2.8379F, -8.9242F, 8.0F, 10.0F, 17.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(4.9245F, -17.677F, -0.9538F, 0.5603F, 0.0934F, -0.1476F));
        PartDefinition waist = upper.addOrReplaceChild("waist", CubeListBuilder.create().texOffs(40, 87).addBox(-5.9667F, -5.8644F, -5.4989F, 12.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(132, 88).addBox(-3.9667F, 0.0842F, -5.4158F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(141, 59).addBox(-3.9667F, 0.0842F, 0.5842F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0913F, 3.6392F, 0.6957F));
        PartDefinition cube_r6 = waist.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(138, 80).addBox(-3.5F, -2.0048F, -1.7181F, 7.0F, 5.0F, 3.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-0.0667F, 6.2127F, -7.0113F, -0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r7 = waist.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(146, 66).addBox(-4.5F, -2.0048F, -1.7181F, 9.0F, 11.0F, 3.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-0.0667F, -3.7017F, -5.706F, -0.1309F, 0.0F, 0.0F));
        PartDefinition hydraumatic = upper.addOrReplaceChild("hydraumatic", CubeListBuilder.create(), PartPose.offset(0.0F, -4.32F, 0.0F));
        PartDefinition hydraumatic1 = hydraumatic.addOrReplaceChild("hydraumatic1", CubeListBuilder.create().texOffs(82, 25).addBox(-1.0F, -4.375F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(90, 25).addBox(-1.5F, -2.125F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-5.0316F, -0.5361F, -2.2762F, 0.2182F, 0.0F, -0.2182F));
        PartDefinition hydraumatic2 = hydraumatic.addOrReplaceChild("hydraumatic2", CubeListBuilder.create().texOffs(82, 25).mirror().addBox(-1.0F, -4.375F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(90, 25).mirror().addBox(-1.5F, -2.125F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(5.0316F, -0.5361F, -2.2762F, 0.2182F, 0.0F, 0.2182F));
        PartDefinition hydraumatic3 = hydraumatic.addOrReplaceChild("hydraumatic3", CubeListBuilder.create().texOffs(82, 25).mirror().addBox(-1.0F, -4.375F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(90, 25).mirror().addBox(-1.5F, -2.125F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(5.031F, -1.9337F, 3.118F, -0.0873F, 0.0F, 0.2182F));
        PartDefinition hydraumatic4 = hydraumatic.addOrReplaceChild("hydraumatic4", CubeListBuilder.create().texOffs(82, 25).addBox(-1.0F, -4.375F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(90, 25).addBox(-1.5F, -2.125F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-5.031F, -1.9337F, 3.118F, -0.0873F, 0.0F, -0.2182F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.1245F, -18.5718F, -2.8738F, -0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(28, 105).addBox(-4.5F, -2.3499F, -6.6139F, 9.0F, 9.0F, 12.0F, new CubeDeformation(-1.4F)), PartPose.offsetAndRotation(0.0F, -3.8068F, -2.3388F, 0.7854F, 0.0F, 0.0F));
        PartDefinition mainBooster = upper.addOrReplaceChild("mainBooster", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.229F, -18.2619F, 4.5499F, 0.0F, 0.0F, -0.7854F));
        PartDefinition cube_r9 = mainBooster.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(82, 0).addBox(-6.266F, -4.734F, -7.591F, 11.0F, 11.0F, 14.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition exhaust1 = mainBooster.addOrReplaceChild("exhaust1", CubeListBuilder.create(), PartPose.offset(-0.766F, 0.766F, 12.409F));
        PartDefinition cube_r10 = exhaust1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(8, 163).addBox(-4.266F, -2.734F, -5.591F, 7.0F, 7.0F, 14.0F, new CubeDeformation(-1.5F)).texOffs(8, 163).addBox(-4.266F, -2.734F, -5.591F, 7.0F, 7.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.766F, -0.766F, 3.591F, 0.0F, 0.0F, 0.0F));
        PartDefinition axe_back = mainBooster.addOrReplaceChild("axe_back", CubeListBuilder.create().texOffs(65, 124).addBox(-6.0F, -4.4166F, -1.591F, 12.0F, 11.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.2453F, -0.4427F, 6.3188F, 0.0F, 0.0F, 0.7854F));
        PartDefinition haft = axe_back.addOrReplaceChild("haft", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));
        PartDefinition join1 = haft.addOrReplaceChild("join1", CubeListBuilder.create().texOffs(72, 140).addBox(-1.5F, -7.4166F, -0.591F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -1.0F, 0.0F));
        PartDefinition join2 = join1.addOrReplaceChild("join2", CubeListBuilder.create().texOffs(84, 140).addBox(-1.5F, -7.4166F, -0.591F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        PartDefinition join3 = join2.addOrReplaceChild("join3", CubeListBuilder.create().texOffs(56, 140).addBox(-1.5F, -12.1666F, -0.591F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.75F, 0.0F));
        PartDefinition cube_r11 = join3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(118, 73).addBox(-3.266F, -1.734F, -1.591F, 5.0F, 5.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 3.75F, 0.0F, 0.0F, 0.0F, -0.7854F));
        PartDefinition leftSocket = axe_back.addOrReplaceChild("leftSocket", CubeListBuilder.create(), PartPose.offsetAndRotation(5.3333F, -0.3333F, 0.0F, 0.1309F, -0.4363F, -0.2618F));
        PartDefinition leftJoint = leftSocket.addOrReplaceChild("leftJoint", CubeListBuilder.create().texOffs(155, 40).addBox(-1.2033F, -3.0062F, -0.7023F, 7.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftBlade = leftSocket.addOrReplaceChild("leftBlade", CubeListBuilder.create().texOffs(88, 96).addBox(-13.25F, -15.75F, -2.5F, 13.0F, 23.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(100, 62).addBox(-18.25F, 7.25F, -2.5F, 18.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.0467F, 4.7438F, 0.7977F, 0.0F, 0.0F, 0.0F));
        PartDefinition rightSocket = axe_back.addOrReplaceChild("rightSocket", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.3659F, -0.3333F, 0.0F, 0.1309F, 0.4363F, 0.2618F));
        PartDefinition rightJoint = rightSocket.addOrReplaceChild("rightJoint", CubeListBuilder.create().texOffs(155, 40).mirror().addBox(-5.7967F, -3.0062F, -0.7023F, 7.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition rightBlade = rightSocket.addOrReplaceChild("rightBlade", CubeListBuilder.create().texOffs(88, 96).mirror().addBox(0.25F, -15.75F, -2.5F, 13.0F, 23.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(100, 62).mirror().addBox(0.25F, 7.25F, -2.5F, 18.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-19.0467F, 4.7438F, 0.7977F, 0.0F, 0.0F, 0.0F));
        PartDefinition leftPauldron = upper.addOrReplaceChild("leftPauldron", CubeListBuilder.create().texOffs(0, 40).addBox(-6.1295F, -3.0258F, -9.3091F, 13.0F, 8.0F, 18.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(13.2993F, -24.8152F, 0.5669F, 0.2182F, 0.0F, 0.3491F));
        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(20.4229F, -17.9057F, 0.8014F, 0.1745F, 0.1745F, -0.1309F));
        PartDefinition cube_r12 = leftArm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(32, 143).addBox(-3.1604F, 2.7916F, -1.4824F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2845F, -3.8086F, -0.3014F, 0.0F, 0.7854F, 0.0F));
        PartDefinition cube_r13 = leftArm.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(86, 73).addBox(-4.05F, -3.3111F, -6.3803F, 9.0F, 9.0F, 14.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(-1.2311F, -0.8478F, -0.2446F, 0.0436F, 0.0F, 0.6109F));
        PartDefinition leftScrew1 = leftArm.addOrReplaceChild("leftScrew1", CubeListBuilder.create().texOffs(147, 111).addBox(-1.8552F, -2.4351F, -1.8291F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(1.809F, -5.9262F, -0.4142F, 0.0F, 0.0F, 0.7854F));
        PartDefinition leftScrew2 = leftArm.addOrReplaceChild("leftScrew2", CubeListBuilder.create(), PartPose.offset(0.1205F, 10.3762F, 1.7858F));
        PartDefinition cube_r14 = leftScrew2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(147, 111).addBox(-2.464F, -4.1709F, -2.2284F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-0.15F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.7854F));
        PartDefinition leftArmUnder = leftArm.addOrReplaceChild("leftArmUnder", CubeListBuilder.create().texOffs(0, 106).addBox(-3.5361F, -1.2739F, -2.3291F, 7.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.174F, 11.4066F, 0.422F));
        PartDefinition leftVambrace = leftArmUnder.addOrReplaceChild("leftVambrace", CubeListBuilder.create().texOffs(99, 127).addBox(-1.5361F, -6.7739F, -3.3291F, 3.0F, 12.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(4.1F, 5.5F, 0.0F));
        PartDefinition leftFist = leftArmUnder.addOrReplaceChild("leftFist", CubeListBuilder.create().texOffs(124, 99).addBox(-3.5361F, -3.5728F, -2.4783F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.0276F, 12.7118F, 0.1752F, 0.7854F, 0.0F, 0.0F));
        PartDefinition leftGlove = leftArmUnder.addOrReplaceChild("leftGlove", CubeListBuilder.create().texOffs(148, 24).addBox(-2.5968F, -3.7623F, 5.9461F, 5.0F, 11.0F, 5.0F, new CubeDeformation(-0.25F)).texOffs(0, 66).addBox(-5.0415F, -0.7091F, -4.3006F, 10.0F, 30.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1121F, 11.801F, 0.4861F, -0.5236F, 0.0F, 0.0F));
        PartDefinition cube_r15 = leftGlove.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(118, 0).addBox(-8.0361F, -4.2373F, -2.5607F, 16.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0571F, 23.7812F, 5.2639F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r16 = leftGlove.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(118, 0).addBox(-8.0361F, -4.2373F, -2.5607F, 16.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0429F, 12.9581F, 5.2442F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r17 = leftGlove.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(132, 10).addBox(-5.5361F, -4.109F, -0.8062F, 11.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0574F, 4.582F, -2.8609F, 1.0472F, 0.0F, 0.0F));
        PartDefinition lGloveJoint = leftGlove.addOrReplaceChild("lGloveJoint", CubeListBuilder.create(), PartPose.offset(-0.0415F, 16.836F, 4.7639F));
        PartDefinition cube_r18 = lGloveJoint.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(50, 54).addBox(-6.0361F, -13.3219F, -3.8711F, 13.0F, 21.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4639F, 2.7263F, -2.25F, -0.0436F, 0.0F, 0.0F));
        PartDefinition exhaust2 = leftGlove.addOrReplaceChild("exhaust2", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.0905F, -4.7977F, 8.4321F, 1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r19 = exhaust2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(8, 163).addBox(-3.5F, -3.5F, -3.0F, 7.0F, 7.0F, 14.0F, new CubeDeformation(-1.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition rightPauldron = upper.addOrReplaceChild("rightPauldron", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-6.8705F, -3.0258F, -9.3091F, 13.0F, 8.0F, 18.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-13.2993F, -24.8152F, 0.5669F, 0.2182F, 0.0F, -0.3491F));
        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-20.4229F, -17.9057F, 0.8014F, 0.1745F, -0.1745F, 0.1309F));
        PartDefinition cube_r20 = rightArm.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(32, 143).mirror().addBox(-2.9319F, 1.8002F, -1.3901F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.2845F, -2.8086F, -0.3014F, 0.0F, -0.7854F, 0.0F));
        PartDefinition cube_r21 = rightArm.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(86, 73).mirror().addBox(-4.95F, -3.3111F, -6.3803F, 9.0F, 9.0F, 14.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(1.2311F, -0.8478F, -0.2446F, 0.0436F, 0.0F, -0.6109F));
        PartDefinition rightScrew1 = rightArm.addOrReplaceChild("rightScrew1", CubeListBuilder.create().texOffs(147, 111).mirror().addBox(-3.1448F, -2.4351F, -1.8291F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-1.809F, -5.9262F, -0.4142F, 0.0F, 0.0F, -0.7854F));
        PartDefinition rightScrew2 = rightArm.addOrReplaceChild("rightScrew2", CubeListBuilder.create(), PartPose.offset(-0.1205F, 10.3762F, 1.7858F));
        PartDefinition cube_r22 = rightScrew2.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(147, 111).mirror().addBox(-1.9272F, -4.1709F, -3.0218F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.15F, 1.0F, 0.0F, -1.5708F, 0.0F, -0.7854F));
        PartDefinition rightArmUnder = rightArm.addOrReplaceChild("rightArmUnder", CubeListBuilder.create().texOffs(0, 106).mirror().addBox(-3.4639F, -1.2739F, -2.3291F, 7.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.174F, 11.4066F, 0.422F));
        PartDefinition rightVambrace = rightArmUnder.addOrReplaceChild("rightVambrace", CubeListBuilder.create().texOffs(99, 127).mirror().addBox(-1.4639F, -6.7739F, -3.3291F, 3.0F, 12.0F, 8.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(-4.1F, 5.5F, 0.0F));
        PartDefinition rightFist = rightArmUnder.addOrReplaceChild("rightFist", CubeListBuilder.create().texOffs(124, 99).mirror().addBox(-3.4639F, -3.5728F, -2.4783F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(0.0276F, 12.7118F, 0.1752F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rightGlove = rightArmUnder.addOrReplaceChild("rightGlove", CubeListBuilder.create().texOffs(0, 66).addBox(-4.9585F, -0.7091F, -4.3006F, 10.0F, 30.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(148, 24).addBox(-2.2727F, -2.7708F, 5.9461F, 5.0F, 11.0F, 5.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1121F, 11.801F, 0.4861F, -0.5236F, 0.0F, 0.0F));
        PartDefinition cube_r23 = rightGlove.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(118, 0).addBox(-7.9639F, -4.2373F, -2.5607F, 16.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0571F, 23.7812F, 5.2639F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r24 = rightGlove.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(118, 0).addBox(-7.9639F, -4.2373F, -2.5607F, 16.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0429F, 12.9581F, 5.2442F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r25 = rightGlove.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(132, 10).addBox(-5.4639F, -4.109F, -0.8062F, 11.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0574F, 4.582F, -2.8609F, 1.0472F, 0.0F, 0.0F));
        PartDefinition rGloveJoint = rightGlove.addOrReplaceChild("rGloveJoint", CubeListBuilder.create(), PartPose.offset(0.0415F, 16.836F, 4.7639F));
        PartDefinition cube_r26 = rGloveJoint.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(50, 54).mirror().addBox(-6.9639F, -13.3219F, -3.8711F, 13.0F, 21.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.4639F, 2.7263F, -2.25F, -0.0436F, 0.0F, 0.0F));
        PartDefinition exhaust3 = rightGlove.addOrReplaceChild("exhaust3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0905F, -3.7977F, 8.4321F, 1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r27 = exhaust3.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(8, 163).mirror().addBox(-3.5F, -3.5F, -3.0F, 7.0F, 7.0F, 14.0F, new CubeDeformation(-1.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, -34.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(9.0F, 0.0F, 0.0F));
        PartDefinition cube_r28 = leftLeg.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(32, 126).addBox(-2.5F, -4.5F, -4.0F, 5.0F, 9.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.2F, 16.4441F, -1.2583F, -0.0436F, 0.0F, 0.0F));
        PartDefinition cube_r29 = leftLeg.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(88, 38).mirror().addBox(-3.5F, -6.0F, -6.0F, 8.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.3F, 3.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r30 = leftLeg.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(116, 25).mirror().addBox(-3.0F, -1.076F, -3.6318F, 7.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.3F, 4.6437F, -0.2941F, -0.1745F, 0.0F, 0.0F));
        PartDefinition leftLegUnder = leftLeg.addOrReplaceChild("leftLegUnder", CubeListBuilder.create(), PartPose.offset(0.2F, 18.1628F, 0.0F));
        PartDefinition cube_r31 = leftLegUnder.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 158).mirror().addBox(-1.5F, -3.5F, -3.5F, 3.0F, 7.0F, 7.0F, new CubeDeformation(-0.55F)).mirror(false).texOffs(0, 158).addBox(-9.5F, -3.5F, -3.5F, 3.0F, 7.0F, 7.0F, new CubeDeformation(-0.55F)), PartPose.offsetAndRotation(4.0F, -1.869F, -0.3426F, 0.5236F, 0.0F, 0.0F));
        PartDefinition cube_r32 = leftLegUnder.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(53, 14).addBox(-4.5F, -2.0F, -1.5F, 9.0F, 4.0F, 3.0F, new CubeDeformation(-0.55F)), PartPose.offsetAndRotation(0.0F, -1.936F, 0.2734F, 0.3927F, 0.0F, 0.0F));
        PartDefinition cube_r33 = leftLegUnder.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(152, 99).addBox(-3.5F, -4.0F, 0.5F, 7.0F, 8.0F, 4.0F, new CubeDeformation(-0.55F)), PartPose.offsetAndRotation(0.0F, -2.436F, 0.9074F, 0.1745F, 0.0F, 0.0F));
        PartDefinition cube_r34 = leftLegUnder.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(0, 123).mirror().addBox(-3.5F, -1.5F, -4.5F, 7.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 9.6997F, -1.1433F, 0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r35 = leftLegUnder.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 135).mirror().addBox(-1.5F, -13.5F, -5.5F, 5.0F, 12.0F, 11.0F, new CubeDeformation(0.15F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 10.8372F, 1.5F, 0.1745F, 0.0F, 0.0F));
        PartDefinition leftFoot = leftLegUnder.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(121, 136).addBox(-2.5F, -3.4725F, -2.9834F, 5.0F, 6.0F, 6.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 12.3097F, 1.4834F));
        PartDefinition cube_r36 = leftFoot.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(58, 105).addBox(0.5F, -1.9329F, 1.4526F, 5.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(58, 105).addBox(-5.5F, -1.9329F, 1.4526F, 5.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0275F, 0.0166F, -0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r37 = leftFoot.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(70, 115).addBox(-3.0F, -2.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.5F, -1.4725F, 4.0166F, -0.2618F, 0.0F, 0.0F));
        PartDefinition cube_r38 = leftFoot.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(128, 46).addBox(-4.0F, -1.7615F, -10.4886F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.0275F, 0.0166F, 0.0873F, 0.0F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-9.0F, 0.0F, 0.0F));
        PartDefinition cube_r39 = rightLeg.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(32, 126).addBox(-2.5F, -4.5F, -4.0F, 5.0F, 9.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-0.2F, 16.4441F, -1.2583F, -0.0436F, 0.0F, 0.0F));
        PartDefinition cube_r40 = rightLeg.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(88, 38).addBox(-4.5F, -6.0F, -6.0F, 8.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3F, 3.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition cube_r41 = rightLeg.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(116, 25).addBox(-4.0F, -1.076F, -3.6318F, 7.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3F, 4.6437F, -0.2941F, -0.1745F, 0.0F, 0.0F));
        PartDefinition rightLegUnder = rightLeg.addOrReplaceChild("rightLegUnder", CubeListBuilder.create(), PartPose.offset(-0.2F, 18.1628F, 0.0F));
        PartDefinition cube_r42 = rightLegUnder.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(0, 158).addBox(-1.5F, -3.5F, -3.5F, 3.0F, 7.0F, 7.0F, new CubeDeformation(-0.55F)).texOffs(0, 158).mirror().addBox(6.5F, -3.5F, -3.5F, 3.0F, 7.0F, 7.0F, new CubeDeformation(-0.55F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -1.869F, -0.3426F, 0.5236F, 0.0F, 0.0F));
        PartDefinition cube_r43 = rightLegUnder.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(53, 14).mirror().addBox(-4.5F, -2.0F, -1.5F, 9.0F, 4.0F, 3.0F, new CubeDeformation(-0.55F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.936F, 0.2734F, 0.3927F, 0.0F, 0.0F));
        PartDefinition cube_r44 = rightLegUnder.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(0, 123).addBox(-3.5F, -1.5F, -4.5F, 7.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.6997F, -1.1433F, 0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r45 = rightLegUnder.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(152, 99).mirror().addBox(-3.5F, -4.0F, 0.5F, 7.0F, 8.0F, 4.0F, new CubeDeformation(-0.55F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.436F, 0.9074F, 0.1745F, 0.0F, 0.0F));
        PartDefinition cube_r46 = rightLegUnder.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(0, 135).addBox(-3.5F, -13.5F, -5.5F, 5.0F, 12.0F, 11.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(1.0F, 10.8372F, 1.5F, 0.1745F, 0.0F, 0.0F));
        PartDefinition rightFoot = rightLegUnder.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(121, 136).addBox(-2.5F, -3.4725F, -2.9834F, 5.0F, 6.0F, 6.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 12.3097F, 1.4834F));
        PartDefinition cube_r47 = rightFoot.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(70, 115).mirror().addBox(-2.0F, -2.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -1.4725F, 4.0166F, -0.2618F, 0.0F, 0.0F));
        PartDefinition cube_r48 = rightFoot.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(58, 105).addBox(-5.5F, -1.9329F, 1.4526F, 5.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(58, 105).addBox(0.5F, -1.9329F, 1.4526F, 5.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0275F, 0.0166F, -0.1309F, 0.0F, 0.0F));
        PartDefinition cube_r49 = rightFoot.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(128, 46).addBox(-5.0F, -1.7615F, -10.4886F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0275F, 0.0166F, 0.0873F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(EntityRealmWarden entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        lookAtTarget(netHeadYaw, headPitch, 2.0F, this.head);
        playAnimation(this, entity, EntityRealmWarden.HEAVY_SWING_ANIMATION, AnimationRealmWarden.HEAVY_SWING, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.DERIVED_HEAVY_SWING_ANIMATION, AnimationRealmWarden.HEAVY_SWING_DERIVED, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.HEAVY_SMASH_ANIMATION, AnimationRealmWarden.HEAVY_SMASH, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.ACTIVATE_ANIMATION, AnimationRealmWarden2.ACTIVE, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.DIE_ANIMATION, AnimationRealmWarden2.DIE, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.BACKSTEP_ANIMATION, AnimationRealmWarden2.BACKSTEP_START, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.LEAP_ANIMATION, AnimationRealmWarden2.LEAP_START, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.LEAP_LANDING_ANIMATION, AnimationRealmWarden2.LEAP_LANDING, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.JUMP_SMASH_ANIMATION, AnimationRealmWarden2.JUMP_SMASH, ageInTicks);
        playAnimation(this, entity, EntityRealmWarden.JUMP_SMASH_START_ANIMATION, AnimationRealmWarden2.JUMP_SMASH_START, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.SWEEP_ANIMATION, AnimationRealmWarden.SWEEP, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.GROUND_POUND_ANIMATION, AnimationRealmWarden.GROUND_POUND, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.TURNAROUND_SWEEP_ANIMATION, AnimationRealmWarden.TURNAROUND_SWEEP, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.STOMP_ANIMATION, AnimationRealmWarden.STOMP, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.STOMP_ANIMATION2, AnimationRealmWarden.STOMP, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.ELBOW_STRIKE_ANIMATION, AnimationRealmWarden.ELBOW_STRIKE, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.BACKSTEP_LANDING_ANIMATION, AnimationRealmWarden2.BACKSTEP_LANDING, ageInTicks);
        playOverlapAnimation(this, entity, EntityRealmWarden.DOUBLE_FIST_SLAM_ANIMATION, AnimationRealmWarden2.DOUBLE_FIST_SLAM, ageInTicks);
        //1 main 2 left 3 right
        boolean exhaust1Visible = false;
        boolean exhaust2Visible = false;
        boolean exhaust3Visible = false;
        if (entity.isAlive()) {
            int tick = entity.getAnimationTick();
            Animation animation = entity.getAnimation();
            if (entity.isActive() && (animation != EntityRealmWarden.ACTIVATE_ANIMATION || tick >= 75)) {
                KeyframeAnimations.animate(this, AnimationRealmWarden.IDLE, (long) (ageInTicks * 1000 / 20), 1F, ModHierarchicalModel.ANIMATION_VECTOR_CACHE);
                float scale = 1F;
                if (animation != AnimatedEntity.NO_ANIMATION) {
                    scale = 0.3F;
                    float delta = ageInTicks - entity.tickCount;
                    float frame = entity.frame + delta;
                    if (animation == EntityRealmWarden.HEAVY_SWING_ANIMATION) {
                        scale = 0.15F;
                    } else if (animation == EntityRealmWarden.JUMP_SMASH_START_ANIMATION) {
                        exhaust1Visible = true;
                        scale = 0.5F;
                    } else if (animation == EntityRealmWarden.DERIVED_HEAVY_SWING_ANIMATION) {
                        scale = 0.15F;
                        exhaust1Visible = tick >= 67 && tick <= 71;
                        if (tick >= 35 && tick <= 66) shakingHands(frame);
                    } else if (animation == EntityRealmWarden.SWEEP_ANIMATION) {
                        scale = 0.15F;
                        exhaust3Visible = tick >= 49 && tick <= 52;
                    } else if (animation == EntityRealmWarden.HEAVY_SMASH_ANIMATION) {
                        scale = 0.15F;
                        exhaust1Visible = tick >= 25 && tick <= 30;
                        if (tick <= 26 && tick > 5) shakingHands(frame);
                    } else if (animation == EntityRealmWarden.DOUBLE_FIST_SLAM_ANIMATION) {
                        if (tick >= 14 && tick <= 17) {
                            exhaust2Visible = true;
                            exhaust3Visible = true;
                        }
                    } else if (animation == EntityRealmWarden.LEAP_LANDING_ANIMATION) {
                        if (tick >= 38 && tick <= 42) {
                            exhaust1Visible = true;
                            exhaust2Visible = true;
                            exhaust3Visible = true;
                        }
                    }
                    if (exhaust1Visible) exhaust1.zScale += (float) (Math.random() - 0.5) * 1.5F;
                    if (exhaust2Visible) exhaust2.zScale += (float) (Math.random() - 0.5) * 1.5F;
                    if (exhaust3Visible) exhaust3.zScale += (float) (Math.random() - 0.5) * 1.5F;
                }
                this.animateWalk(AnimationRealmWarden.WALK, limbSwing, limbSwingAmount * scale, 1.25F, 3F);
            } else if (entity.isNoAnimation()) {
                KeyframeAnimations.animate(this, AnimationRealmWarden2.DEACTIVATE_HOLD, (long) (ageInTicks * 1000 / 20), 1F, ModHierarchicalModel.ANIMATION_VECTOR_CACHE);
            }
        }
        exhaust1.visible = exhaust1Visible;
        exhaust2.visible = exhaust2Visible;
        exhaust3.visible = exhaust3Visible;
    }

    private void shakingHands(float frame) {
        this.swing(this.leftArm, 3.5F, 0.035F, false, 0, 0, frame, 1);
        this.flap(this.leftArm, 3.5F, 0.02F, false, 0, 0, frame, 1);
        this.swing(this.rightArm, 3.5F, 0.035F, true, 0, 0, frame, 1);
        this.flap(this.rightArm, 3.5F, 0.02F, true, 0, 0, frame, 1);
    }
}
