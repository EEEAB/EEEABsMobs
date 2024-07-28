package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationTheImmortal;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelTheImmortal extends EMHierarchicalModel<EntityTheImmortal> {
    private final ModelPart root;
    private final ModelPart upper;
    private final ModelPart lower;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart miniRightArm;
    private final ModelPart miniRightArmUnder;
    private final ModelPart leftArm;
    private final ModelPart miniLeftArm;
    private final ModelPart miniLeftArmUnder;
    private final ModelPart rightFist;
    private final ModelPart leftFist;
    private final ModelPart core;
    private final ModelPart[] spine;

    public ModelTheImmortal(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.lower = this.root.getChild("lower");
        ModelPart body = this.upper.getChild("body");
        this.head = this.upper.getChild("head");
        this.core = this.upper.getChild("core");
        this.rightArm = body.getChild("rightArm");
        this.miniRightArm = body.getChild("miniRightArm");
        this.miniRightArmUnder = this.miniRightArm.getChild("miniRightArmUnder");
        this.rightFist = this.rightArm.getChild("rightArmUnder").getChild("rightFist");
        this.leftArm = body.getChild("leftArm");
        this.miniLeftArm = body.getChild("miniLeftArm");
        this.miniLeftArmUnder = this.miniLeftArm.getChild("miniLeftArmUnder");
        this.leftFist = this.leftArm.getChild("leftArmUnder").getChild("leftFist");
        this.spine = new ModelPart[]{
                body.getChild("spine1"),
                body.getChild("spine1").getChild("spine2"),
        };
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -41.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0845F, -23.234F, -12.1972F, -0.1745F, 0.0F, 0.0F));
        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(171, 8).mirror().addBox(-1.6F, -11.0F, -2.5F, 3.0F, 11.0F, 4.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(-10.1668F, -12.4982F, -9.2005F, 0.5421F, 0.5951F, -0.304F));
        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(142, 10).mirror().addBox(-1.0F, -1.0F, -7.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.2828F, -13.4982F, -3.9373F, 0.0F, 0.5236F, -0.1745F));
        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(171, 8).addBox(-1.4F, -11.0F, -2.5F, 3.0F, 11.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(9.9978F, -12.4982F, -9.2005F, 0.5421F, -0.5951F, 0.304F));
        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(142, 10).addBox(-3.0F, -1.0F, -7.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.1138F, -13.4982F, -3.9373F, 0.0F, -0.5236F, 0.1745F));
        PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(1, 1).addBox(-5.5F, -5.5F, -5.5F, 11.0F, 12.0F, 11.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.1138F, -7.4982F, -1.9373F, 0.5442F, -0.7142F, -0.3745F));
        PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(65, 24).addBox(-5.5F, -4.7F, -5.5F, 11.0F, 11.0F, 11.0F, new CubeDeformation(-0.9F)), PartPose.offsetAndRotation(0.1138F, -6.7982F, -1.9373F, 0.6172F, -0.6879F, -0.4202F));
        PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(48, 1).addBox(-5.5F, -5.5F, -5.5F, 11.0F, 11.0F, 11.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.1138F, -7.4982F, -1.9373F, 0.6172F, -0.6879F, -0.4202F));
        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offset(0.0999F, -3.1325F, -0.4374F));
        PartDefinition cube_r8 = jaw.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(95, 6).addBox(-5.5F, -3.0F, -5.5F, 11.0F, 6.0F, 11.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, 1.2F, 0.6F, 0.4531F, -0.6976F, -0.2957F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0045F, -10.5267F, -4.6403F));
        PartDefinition cube_r9 = body.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(54, 73).addBox(-1.5F, -10.5F, -1.5F, 21.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0596F, 3.7466F, -5.3846F, 0.4235F, -0.4444F, -0.4091F));
        PartDefinition cube_r10 = body.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(104, 73).addBox(-6.0F, -1.5F, -0.9F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.9907F, 7.5703F, -1.2237F, 0.4235F, -0.4444F, -0.4091F));
        PartDefinition cube_r11 = body.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(104, 73).mirror().addBox(-6.0F, -1.5F, -0.9F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.9996F, 7.5703F, -1.2237F, 0.4235F, 0.4444F, 0.4091F));
        PartDefinition cube_r12 = body.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 73).addBox(-4.5F, -7.8F, -6.0F, 15.0F, 15.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.932F, -5.7637F, 3.6325F, 0.4235F, -0.4444F, -0.4091F));
        PartDefinition cube_r13 = body.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 73).mirror().addBox(-10.5F, -7.8F, -6.0F, 15.0F, 15.0F, 21.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-10.9409F, -5.7637F, 3.6325F, 0.4235F, 0.4444F, 0.4091F));
        PartDefinition cube_r14 = body.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(54, 73).mirror().addBox(-19.5F, -10.5F, -1.5F, 21.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0685F, 3.7466F, -5.3846F, 0.4235F, 0.4444F, 0.4091F));
        PartDefinition cube_r15 = body.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(86, 47).addBox(-8.5F, -8.5F, -1.5F, 17.0F, 18.0F, 5.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.5161F, -21.0845F, 9.7112F, 0.988F, -0.6624F, 0.372F));
        PartDefinition cube_r16 = body.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(93, 82).mirror().addBox(-6.0F, 2.3F, -13.8F, 12.0F, 9.0F, 12.0F, new CubeDeformation(1.0F)).mirror(false)
                .texOffs(111, 31).mirror().addBox(-7.0F, -4.5F, -2.5F, 12.0F, 12.0F, 3.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offsetAndRotation(-8.3769F, -25.0072F, -0.2448F, 1.5143F, -0.7362F, -0.3241F));
        PartDefinition cube_r17 = body.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(111, 31).addBox(-5.0F, -4.5F, -2.5F, 12.0F, 12.0F, 3.0F, new CubeDeformation(1.0F))
                .texOffs(93, 82).addBox(-6.0F, 2.3F, -12.8F, 12.0F, 9.0F, 12.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(8.3679F, -25.0072F, -0.2448F, 1.5143F, 0.7362F, 0.3241F));
        PartDefinition cube_r18 = body.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 28).addBox(-10.2F, -10.5F, -10.8F, 21.0F, 21.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0872F, -1.2852F, 4.9646F, 0.3046F, 0.7708F, 0.2142F));
        PartDefinition spine1 = body.addOrReplaceChild("spine1", CubeListBuilder.create(), PartPose.offset(-0.0909F, 15.3647F, 15.9944F));
        PartDefinition cube_r19 = spine1.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(94, 225).addBox(-5.5F, -8.0F, -5.5F, 11.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, 0.3896F, 0.7554F, 0.2708F));
        PartDefinition spine2 = spine1.addOrReplaceChild("spine2", CubeListBuilder.create(), PartPose.offset(0.0772F, 14.2908F, 8.5204F));
        PartDefinition cube_r20 = spine2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(94, 225).addBox(-3.7474F, 0.649F, -7.2673F, 11.0F, 16.0F, 11.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-0.0175F, -8.3909F, -3.5547F, 0.8337F, 0.6138F, 0.5551F));
        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(25.2895F, -19.037F, 15.8276F));
        PartDefinition cube_r21 = leftArm.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(38, 192).addBox(-3.5478F, -10.9907F, -10.6662F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5801F, -17.7127F, 2.286F, -0.9286F, 0.4523F, -0.3292F));
        PartDefinition cube_r22 = leftArm.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(38, 192).addBox(2.6883F, -11.9726F, -10.7928F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.7921F, -15.5094F, -3.2545F, 0.1018F, 0.9985F, 1.0466F));
        PartDefinition cube_r23 = leftArm.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(60, 166).addBox(0.6691F, 2.2101F, -0.2976F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.5786F, 2.2736F, 8.2848F, 1.1771F, 0.9776F, -1.1045F));
        PartDefinition cube_r24 = leftArm.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(149, 173).addBox(-10.6182F, -10.8169F, 4.6838F, 17.0F, 23.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(196, 161).addBox(6.3818F, -10.8169F, -12.3162F, 5.0F, 23.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.2182F, 8.9395F, 9.9153F, 0.7952F, 0.4822F, -0.6788F));
        PartDefinition cube_r25 = leftArm.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(90, 107).addBox(-2.5F, -11.5F, -11.5F, 5.0F, 23.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.2913F, -5.0065F, -8.505F, 0.6156F, 0.316F, -0.4619F));
        PartDefinition cube_r26 = leftArm.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(148, 125).addBox(-20.0F, -14.0F, 3.3F, 23.0F, 23.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.6752F, -6.2154F, 0.9397F, 0.6156F, 0.316F, -0.4619F));
        PartDefinition cube_r27 = leftArm.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(143, 73).addBox(-5.7401F, -2.8132F, -4.528F, 11.0F, 27.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.7401F, -8.8132F, 0.472F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5167F, -2.5532F, -5.5802F, 0.4988F, -0.4328F, -0.6645F));
        PartDefinition leftArmUnder = leftArm.addOrReplaceChild("leftArmUnder", CubeListBuilder.create(), PartPose.offset(14.4779F, 14.8159F, 3.2208F));
        PartDefinition cube_r28 = leftArmUnder.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(76, 162).addBox(-2.3126F, -7.3598F, -3.4177F, 9.0F, 13.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5481F, 24.4179F, -1.9504F, 1.354F, 0.0271F, 0.1101F));
        PartDefinition cube_r29 = leftArmUnder.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(1, 114).addBox(-4.4225F, -5.5F, -25.1487F, 11.0F, 11.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7211F, -0.9818F, 2.0572F, 1.4029F, -0.0479F, -0.2752F));
        PartDefinition leftFist = leftArmUnder.addOrReplaceChild("leftFist", CubeListBuilder.create(), PartPose.offset(4.419F, 25.0349F, -3.6601F));
        PartDefinition cube_r30 = leftFist.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(50, 117).addBox(39.1809F, -5.1008F, -3.5475F, 10.0F, 10.0F, 9.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-44.1908F, 3.8069F, 0.0518F, 1.3091F, 0.0045F, 0.0258F));
        PartDefinition l_netherworld_katana = leftFist.addOrReplaceChild("l_netherworld_katana", CubeListBuilder.create(), PartPose.offsetAndRotation(0.8092F, 5.7129F, 1.252F, 1.3963F, 0.0F, 0.0F));
        PartDefinition blade2 = l_netherworld_katana.addOrReplaceChild("blade2", CubeListBuilder.create().texOffs(206, 83).mirror().addBox(-4.5F, -72.4F, -2.5F, 9.0F, 71.0F, 5.0F, new CubeDeformation(-1.5F)).mirror(false)
                .texOffs(169, 223).mirror().addBox(-2.0F, -25.4F, -2.5F, 7.0F, 24.0F, 5.0F, new CubeDeformation(-1.4F)).mirror(false), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition cube_r31 = blade2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(206, 84).mirror().addBox(-5.4458F, -11.1522F, -2.5F, 9.0F, 8.0F, 5.0F, new CubeDeformation(-2.0F)).mirror(false), PartPose.offsetAndRotation(1.1F, -73.8F, 0.0F, 0.0F, 0.0F, 0.3142F));
        PartDefinition cube_r32 = blade2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(206, 83).mirror().addBox(-4.3F, -7.8F, -2.5F, 9.0F, 10.0F, 5.0F, new CubeDeformation(-1.8F)).mirror(false), PartPose.offsetAndRotation(0.3F, -73.8F, 0.0F, 0.0F, 0.0F, 0.2182F));
        PartDefinition cube_r33 = blade2.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(206, 83).mirror().addBox(-4.3F, -7.8F, -2.5F, 9.0F, 10.0F, 5.0F, new CubeDeformation(-1.65F)).mirror(false), PartPose.offsetAndRotation(0.0F, -69.8F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition hilt2 = l_netherworld_katana.addOrReplaceChild("hilt2", CubeListBuilder.create(), PartPose.offset(-0.25F, -10.0F, 0.0F));
        PartDefinition cube_r34 = hilt2.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(185, 205).mirror().addBox(-5.0F, -2.5F, -4.5F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.75F, -0.5F, -0.5F, 0.0F, -0.7854F, 0.0F));
        PartDefinition cube_r35 = hilt2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(218, 226).mirror().addBox(-2.5F, -10.5F, -2.5F, 5.0F, 21.0F, 5.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.25F, 9.5F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-25.2984F, -19.037F, 15.8276F));
        PartDefinition cube_r36 = rightArm.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.7401F, -8.8132F, 0.472F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(143, 73).mirror().addBox(-5.2599F, -2.8132F, -4.528F, 11.0F, 27.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5167F, -2.5532F, -5.5802F, 0.4988F, 0.4328F, 0.6645F));
        PartDefinition cube_r37 = rightArm.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(38, 192).mirror().addBox(-2.6883F, -11.9726F, -10.7928F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-13.7921F, -15.5094F, -3.2545F, 0.1018F, -0.9985F, -1.0466F));
        PartDefinition cube_r38 = rightArm.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(38, 192).mirror().addBox(3.5478F, -10.9907F, -10.6662F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-10.5801F, -17.7127F, 2.286F, -0.9286F, -0.4523F, 0.3292F));
        PartDefinition cube_r39 = rightArm.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(60, 166).mirror().addBox(-5.6691F, 2.2101F, -0.2976F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-18.5786F, 2.2736F, 8.2848F, 1.1771F, -0.9776F, 1.1045F));
        PartDefinition cube_r40 = rightArm.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(149, 173).mirror().addBox(-6.3818F, -10.8169F, 4.6838F, 17.0F, 23.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(196, 161).mirror().addBox(-11.3818F, -10.8169F, -12.3162F, 5.0F, 23.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-18.2182F, 8.9395F, 9.9153F, 0.7952F, -0.4822F, 0.6788F));
        PartDefinition cube_r41 = rightArm.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(90, 107).mirror().addBox(-8.0F, -14.0F, -19.7F, 5.0F, 23.0F, 23.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(148, 125).mirror().addBox(-3.0F, -14.0F, 3.3F, 23.0F, 23.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.6752F, -6.2154F, 0.9397F, 0.6156F, -0.316F, 0.4619F));
        PartDefinition rightArmUnder = rightArm.addOrReplaceChild("rightArmUnder", CubeListBuilder.create(), PartPose.offset(-14.4779F, 14.8159F, 3.2208F));
        PartDefinition cube_r42 = rightArmUnder.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(76, 162).mirror().addBox(-6.6874F, -7.3598F, -3.4177F, 9.0F, 13.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5481F, 24.4179F, -1.9504F, 1.354F, -0.0271F, -0.1101F));
        PartDefinition cube_r43 = rightArmUnder.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(1, 114).mirror().addBox(-6.5775F, -5.5F, -25.1487F, 11.0F, 11.0F, 25.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.7211F, -0.9818F, 2.0572F, 1.4029F, 0.0479F, 0.2752F));
        PartDefinition rightFist = rightArmUnder.addOrReplaceChild("rightFist", CubeListBuilder.create(), PartPose.offset(-4.419F, 25.0349F, -3.6601F));
        PartDefinition cube_r44 = rightFist.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(50, 117).mirror().addBox(-49.1809F, -5.1008F, -3.5475F, 10.0F, 10.0F, 9.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(44.1908F, 3.8069F, 0.0518F, 1.3091F, -0.0045F, -0.0258F));
        PartDefinition r_netherworld_katana = rightFist.addOrReplaceChild("r_netherworld_katana", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8092F, 5.7129F, 1.252F, 1.3963F, 0.0F, 0.0F));
        PartDefinition blade = r_netherworld_katana.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(206, 2).addBox(-4.5F, -72.4F, -2.5F, 9.0F, 71.0F, 5.0F, new CubeDeformation(-1.5F))
                .texOffs(142, 223).addBox(-5.0F, -25.4F, -2.5F, 7.0F, 24.0F, 5.0F, new CubeDeformation(-1.4F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        PartDefinition cube_r45 = blade.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(206, 1).addBox(-4.7F, -7.8F, -2.5F, 9.0F, 10.0F, 5.0F, new CubeDeformation(-1.65F)), PartPose.offsetAndRotation(0.0F, -69.8F, 0.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition cube_r46 = blade.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(206, 1).addBox(-4.7F, -7.8F, -2.5F, 9.0F, 10.0F, 5.0F, new CubeDeformation(-1.8F)), PartPose.offsetAndRotation(-0.3F, -73.8F, 0.0F, 0.0F, 0.0F, -0.2182F));
        PartDefinition cube_r47 = blade.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(206, 2).addBox(-3.5542F, -11.1522F, -2.5F, 9.0F, 8.0F, 5.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-1.1F, -73.8F, 0.0F, 0.0F, 0.0F, -0.3142F));
        PartDefinition hilt = r_netherworld_katana.addOrReplaceChild("hilt", CubeListBuilder.create(), PartPose.offset(0.25F, -10.0F, 0.0F));
        PartDefinition cube_r48 = hilt.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(141, 205).addBox(-5.0F, -2.5F, -4.5F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -0.5F, -0.5F, 0.0F, 0.7854F, 0.0F));
        PartDefinition cube_r49 = hilt.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(196, 226).addBox(-2.5F, -10.5F, -2.5F, 5.0F, 21.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-0.25F, 9.5F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition miniLeftArm = body.addOrReplaceChild("miniLeftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(21.2878F, 2.1086F, 7.1009F, -0.2233F, 0.2129F, -0.0479F));
        PartDefinition cube_r50 = miniLeftArm.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(0, 155).addBox(3.2626F, -16.2447F, -1.6113F, 5.0F, 21.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5261F, 14.0016F, -8.494F, -0.5368F, 0.1339F, -0.6748F));
        PartDefinition miniLeftArmUnder = miniLeftArm.addOrReplaceChild("miniLeftArmUnder", CubeListBuilder.create(), PartPose.offset(7.5336F, 15.0423F, -11.4097F));
        PartDefinition cube_r51 = miniLeftArmUnder.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(0, 155).addBox(-2.0126F, -1.8234F, -1.3558F, 5.0F, 21.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-0.4866F, 1.0169F, 1.0386F, -2.7484F, -0.3018F, -1.5377F));
        PartDefinition claw1 = miniLeftArmUnder.addOrReplaceChild("claw1", CubeListBuilder.create(), PartPose.offset(-15.5371F, -2.0599F, -7.5473F));
        PartDefinition cube_r52 = claw1.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(22, 173).addBox(-3.5F, -5.0F, 0.6F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1012F, -1.3441F, -1.8301F, -2.7527F, 0.2655F, -1.308F));
        PartDefinition claw2 = miniLeftArmUnder.addOrReplaceChild("claw2", CubeListBuilder.create(), PartPose.offsetAndRotation(-16.5371F, -2.0599F, -5.5473F, -0.6811F, -0.1671F, -0.2024F));
        PartDefinition cube_r53 = claw2.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(22, 173).addBox(-4.1F, -5.0F, 1.6F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.7667F, -2.1193F, -0.4729F, -2.7527F, 0.2655F, -1.308F));
        PartDefinition claw3 = miniLeftArmUnder.addOrReplaceChild("claw3", CubeListBuilder.create(), PartPose.offsetAndRotation(-17.1588F, -0.9028F, -8.6392F, 2.4164F, -0.5871F, 0.0364F));
        PartDefinition cube_r54 = claw3.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(22, 173).addBox(-6.1145F, -3.6076F, -0.202F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0822F, -0.5564F, -0.9716F, -2.7527F, 0.2655F, -1.308F));
        PartDefinition miniRightArm = body.addOrReplaceChild("miniRightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-21.2967F, 2.1086F, 7.1009F, -0.2233F, -0.2129F, 0.0479F));
        PartDefinition cube_r55 = miniRightArm.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(0, 155).mirror().addBox(-8.2626F, -16.2447F, -1.6113F, 5.0F, 21.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5261F, 14.0016F, -8.494F, -0.5368F, -0.1339F, 0.6748F));
        PartDefinition miniRightArmUnder = miniRightArm.addOrReplaceChild("miniRightArmUnder", CubeListBuilder.create(), PartPose.offset(-7.5336F, 15.0423F, -11.4097F));
        PartDefinition cube_r56 = miniRightArmUnder.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(0, 155).mirror().addBox(-2.9874F, -1.8234F, -1.3558F, 5.0F, 21.0F, 5.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.4866F, 1.0169F, 1.0386F, -2.7484F, 0.3018F, 1.5377F));
        PartDefinition claw4 = miniRightArmUnder.addOrReplaceChild("claw4", CubeListBuilder.create(), PartPose.offset(15.5371F, -2.0599F, -7.5473F));
        PartDefinition cube_r57 = claw4.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(22, 173).mirror().addBox(-3.5F, -5.0F, 0.6F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.1012F, -1.3441F, -1.8301F, -2.7527F, -0.2655F, 1.308F));
        PartDefinition claw5 = miniRightArmUnder.addOrReplaceChild("claw5", CubeListBuilder.create(), PartPose.offsetAndRotation(16.5371F, -2.0599F, -5.5473F, -0.6811F, 0.1671F, 0.2024F));
        PartDefinition cube_r58 = claw5.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(22, 173).mirror().addBox(-2.9F, -5.0F, 1.6F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.7667F, -2.1193F, -0.4729F, -2.7527F, -0.2655F, 1.308F));
        PartDefinition claw6 = miniRightArmUnder.addOrReplaceChild("claw6", CubeListBuilder.create(), PartPose.offsetAndRotation(17.1588F, -0.9028F, -8.6392F, 2.4164F, 0.5871F, -0.0364F));
        PartDefinition cube_r59 = claw6.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(22, 173).mirror().addBox(-0.8855F, -3.6076F, -0.202F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0822F, -0.5564F, -0.9716F, -2.7527F, -0.2655F, 1.308F));
        PartDefinition core = upper.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.9014F, -0.109F, 0.2618F, 0.0F, 0.0F));
        PartDefinition cube_r60 = core.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(143, 28).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3171F, 0.8035F, 0.232F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -38.0F, 2.8344F, 0.2618F, 0.0F, 0.0F));
        PartDefinition cube_r61 = lower.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(0, 216).addBox(-0.9853F, -0.1706F, -10.6247F, 3.0F, 15.0F, 21.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(13.5777F, 12.6478F, 12.9483F, 0.3656F, -0.7279F, -0.5069F));
        PartDefinition cube_r62 = lower.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(50, 216).addBox(2.6412F, -1.6601F, -7.1467F, 3.0F, 18.0F, 18.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(8.7433F, 2.3785F, 4.9792F, 0.4973F, -0.6546F, -0.7127F));
        PartDefinition cube_r63 = lower.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(0, 216).mirror().addBox(-2.0147F, -0.1706F, -10.6247F, 3.0F, 15.0F, 21.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-13.5777F, 12.6478F, 12.9483F, 0.3656F, 0.7279F, 0.5069F));
        PartDefinition cube_r64 = lower.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(50, 216).mirror().addBox(-5.6412F, -1.6601F, -7.1467F, 3.0F, 18.0F, 18.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-8.7433F, 2.3785F, 4.9792F, 0.4973F, 0.6546F, 0.7127F));
        PartDefinition cube_r65 = lower.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(0, 184).addBox(-9.0F, -6.5398F, -3.0F, 15.0F, 27.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(1.5F, 2.1635F, -11.6503F, -0.4363F, 0.0F, 0.0F));
        PartDefinition cube_r66 = lower.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(38, 165).addBox(-1.5F, -5.0F, -1.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5418F, -19.4711F, -0.583F, -0.6956F, 0.3999F));
        PartDefinition cube_r67 = lower.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(38, 186).mirror().addBox(-16.3677F, -1.2821F, -0.8157F, 15.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.6178F, -15.7229F, -0.4998F, 0.1958F, 0.0779F));
        PartDefinition cube_r68 = lower.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(38, 186).addBox(1.3677F, -1.2821F, -0.8157F, 15.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6178F, -15.7229F, -0.4998F, -0.1958F, -0.0779F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityTheImmortal entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        //LookAt
        Animation animation = entity.getAnimation();
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        if (animation == entity.getNoAnimation() && !entity.isActive()) {
            setStaticRotationPoint(root, 0F, 12F, 0F);
            setStaticRotationPoint(upper, 0F, 8F, 0F);
            setStaticRotationAngle(head, 22.5F, 0F, 0F);
            setStaticRotationPoint(head, 0F, 3F, 0F);
            setStaticRotationPoint(leftArm, -9F, 2F, -1F);
            setStaticRotationPoint(rightArm, 9F, 2F, -1F);
            setStaticRotationAngle(miniLeftArm, 20F, 0F, 0F);
            setStaticRotationAngle(miniRightArm, 20F, 0F, 0F);
            setStaticRotationAngle(miniRightArmUnder, -14.1614F, 37.8095F, 65.1283F);
            setStaticRotationAngle(miniLeftArmUnder, -14.1614F, -37.8095F, -65.1283F);
            setStaticRotationAngle(lower, -5F, 0F, 0F);
        }
        if (entity.isActive() && !entity.isDeadOrDying()) {
            //Idle
            float speed = 0.1F;
            float degree = 0.8F;
            this.bob(root, speed, degree, false, frame, 1);
            this.bob(head, speed, speed, false, frame, 1);
            this.walk(head, speed, degree * 0.1F, true, 0, 0, frame, 1);
            this.walk(rightFist, speed, degree * 0.05F, true, 0, 0, frame, 1);
            this.walk(leftFist, speed, degree * 0.05F, true, 0, 0, frame, 1);
            this.bob(core, speed - 0.02F, degree * 0.1F, false, frame, 1);
            this.bob(rightArm, speed, degree + 0.2F, false, frame, 1);
            this.bob(leftArm, speed, degree + 0.2F, false, frame, 1);
            this.chainSwing(spine, speed + 0.1F, degree, -3, limbSwing, limbSwingAmount);
            this.chainSwing(spine, speed, degree * 0.25F, -1.5, frame, 1);
            this.core.xRot += Mth.sin(frame * 0.01F) * 180F * Mth.PI / 180F;
            this.core.yRot += Mth.sin(frame * 0.01F) * 180F * Mth.PI / 180F;
            this.core.zRot += Mth.sin(frame * 0.015F) * 360F * Mth.PI / 180F;
            float fraction = entity.coreControllerAnimation.getAnimationFraction(ageInTicks);
            if (fraction > 0) {
                this.core.x += (float) (Math.random() - 0.5) * fraction;
                this.core.y += (float) (Math.random() - 0.5) * fraction;
                this.core.z += (float) (Math.random() - 0.5) * fraction;
            }
            //Walk
            this.animateWalk(AnimationTheImmortal.WALK, limbSwing, limbSwingAmount, 1F, 1F);
            //float cycle = 0.2F;
            //if (animation != entity.getNoAnimation() || entity.getDeltaMovement().horizontalDistanceSqr() < 2.5000003E-7F) {
            //    cycle = 0.1F;
            //}
            //float idle = (Mth.sin(ageInTicks * cycle * 0.1F) + 1.0F) * (1.0F - limbSwingAmount);
            //float rebound = limbSwing * cycle % Mth.PI / Mth.PI;
            //rebound = 1.0F - rebound;
            //rebound *= rebound;
            //float dampingFactor = 1.0F - limbSwingAmount * 0.5F; // 引入的衰减因子，根据移动速度调整
            //this.root.xRot -= (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.1F * dampingFactor - 0.1F * (-idle) * -0.1F;
            ////this.root.xRot -= (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.1F - 0.1F;
            ////this.lower.xRot -= (Mth.cos(limbSwing * cycle + 3.1415927F)) * limbSwingAmount * 0.1F - 0.1F /*- idle*/ * -0.1F;
            //this.head.xRot += (Mth.cos(limbSwing * cycle) - 2.0F) * limbSwingAmount * 0.1F * dampingFactor - 0.1F * (-idle) * -0.1F;
            //this.upper.y += Mth.sin(rebound) * limbSwingAmount;
        }
        //Animation
        this.animate(entity.dieAnimation, AnimationTheImmortal.DIE, ageInTicks);
        this.animate(entity.spawnAnimation, AnimationTheImmortal.SPAWN, ageInTicks);
        this.animate(entity.switchStage2Animation, AnimationTheImmortal.STAGE2, ageInTicks);
        this.animate(entity.switchStage3Animation, AnimationTheImmortal.STAGE3, ageInTicks);
        this.animate(entity.teleportAnimation, AnimationTheImmortal.TELEPORT, ageInTicks);
    }
}