package com.eeeab.eeeabsmobs.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelGhostWarriorArmor extends HumanoidModel<LivingEntity> {
    public ModelGhostWarriorArmor(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        PartDefinition body = partdefinition.getChild("body");
        PartDefinition right_arm = partdefinition.getChild("right_arm");
        PartDefinition left_arm = partdefinition.getChild("left_arm");
        PartDefinition right_leg = partdefinition.getChild("right_leg");
        PartDefinition left_leg = partdefinition.getChild("left_leg");
        PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(64, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition antler2 = head.addOrReplaceChild("antler2", CubeListBuilder.create().texOffs(64, 36).mirror().addBox(-1.6384F, -1.6737F, -4.3361F, 3.0F, 3.0F, 6.0F, new CubeDeformation(-0.35F)).mirror(false), PartPose.offsetAndRotation(4.5F, -7.799F, -3.25F, -0.3697F, -0.5793F, 0.3371F));
        PartDefinition cube_r1 = antler2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(90, 53).mirror().addBox(-1.6384F, -7.4524F, -1.3382F, 3.0F, 8.0F, 3.0F, new CubeDeformation(-0.55F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.001F, -3.25F, 0.5236F, 0.0F, 0.0F));
        PartDefinition antler1 = head.addOrReplaceChild("antler1", CubeListBuilder.create().texOffs(64, 36).addBox(-1.3616F, -1.6737F, -4.3361F, 3.0F, 3.0F, 6.0F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(-4.5F, -7.799F, -3.25F, -0.3697F, 0.5793F, -0.3371F));
        PartDefinition cube_r2 = antler1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(90, 53).addBox(-1.3616F, -7.4524F, -1.3382F, 3.0F, 8.0F, 3.0F, new CubeDeformation(-0.55F)), PartPose.offsetAndRotation(0.0F, -0.001F, -3.25F, 0.5236F, 0.0F, 0.0F));
        PartDefinition chestplate1 = body.addOrReplaceChild("chestplate1", CubeListBuilder.create().texOffs(64, 46).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 14.0F, 4.0F, new CubeDeformation(0.35F))
                .texOffs(104, 1).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 11.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition chestplate2 = body.addOrReplaceChild("chestplate2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cube_r3 = chestplate2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(97, 35).mirror().addBox(-2.5F, -2.5F, -3.0F, 7.0F, 5.0F, 5.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(5.0F, 2.0F, 0.5F, 0.0F, 0.0F, -0.1745F));
        PartDefinition cube_r4 = chestplate2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(97, 35).addBox(-4.5F, -2.5F, -3.0F, 7.0F, 5.0F, 5.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.5F, 0.0F, 0.0F, 0.1745F));
        PartDefinition cube_r5 = chestplate2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(98, 22).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(-0.0858F, 3.0F, -0.4123F, 0.3419F, 0.7549F, 0.236F));
        PartDefinition chestplate3 = body.addOrReplaceChild("chestplate3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cube_r6 = chestplate3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(89, 47).addBox(-2.0F, -1.5F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -2.0F, 2.0F, 0.0F, -0.1745F, -0.7854F));
        PartDefinition cube_r7 = chestplate3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(89, 47).addBox(-2.0F, -1.5F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, -2.0F, -2.0F, 0.0F, 0.1745F, -0.7854F));
        PartDefinition cube_r8 = chestplate3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(89, 47).mirror().addBox(-3.0F, -1.5F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-9.0F, -2.0F, 2.0F, 0.0F, 0.1745F, 0.7854F));
        PartDefinition cube_r9 = chestplate3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(89, 47).mirror().addBox(-3.0F, -1.5F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-9.5F, -2.0F, -2.0F, 0.0F, -0.1745F, 0.7854F));
        PartDefinition right_gardebras = right_arm.addOrReplaceChild("right_gardebras", CubeListBuilder.create().texOffs(105, 48).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition left_gardebras = left_arm.addOrReplaceChild("left_gardebras", CubeListBuilder.create().texOffs(105, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition right_leggings = right_leg.addOrReplaceChild("right_leggings", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }
}