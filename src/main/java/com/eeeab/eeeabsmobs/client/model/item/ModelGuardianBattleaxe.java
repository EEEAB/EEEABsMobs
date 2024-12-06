package com.eeeab.eeeabsmobs.client.model.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelGuardianBattleaxe extends EntityModel<Entity> {
    private final ModelPart root;

    public ModelGuardianBattleaxe(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.8674F, 19.243F, 4.9916F, 3.1416F, 0.0F, -3.1416F));
        PartDefinition lever = root.addOrReplaceChild("lever", CubeListBuilder.create().texOffs(0, 156).addBox(3.0801F, 4.9444F, -16.8786F, 3.0F, 3.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(85, 174).addBox(2.4501F, 2.0444F, -26.9786F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(60, 198).addBox(2.0801F, 1.1444F, -39.6786F, 5.0F, 5.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(19, 205).addBox(2.7801F, 3.5444F, 19.1214F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(3, 209).addBox(3.1801F, 4.6444F, 25.1214F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -4.4F, 0.0F));
        PartDefinition cube_r1 = lever.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(84, 186).addBox(-2.3102F, 5.7362F, -8.4468F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7303F, 0.6472F, -11.7938F, -0.4363F, 0.0F, 0.0F));
        PartDefinition blade = root.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(222, 196).addBox(2.7801F, 4.3444F, -37.1786F, 4.0F, 10.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(222, 196).addBox(2.7801F, -8.0556F, -37.1786F, 4.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -4.4F, 0.0F));
        PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(102, 192).addBox(-2.3F, -8.5F, -4.5F, 5.0F, 16.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7301F, -8.9005F, -34.8688F, 1.9199F, 0.0F, 0.0F));
        PartDefinition cube_r3 = blade.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(132, 197).addBox(-1.8949F, -14.3568F, -2.9102F, 4.0F, 13.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.6F, -13.2029F, -31.8596F, -2.2078F, 0.0F, 0.0F));
        PartDefinition cube_r4 = blade.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(182, 186).addBox(-1.8699F, -5.8044F, -3.1078F, 4.0F, 16.0F, 15.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.6F, 16.0029F, -31.8596F, -1.9199F, 0.0F, 0.0F));
        PartDefinition cube_r5 = blade.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(156, 191).addBox(-1.8949F, -6.3321F, -9.8793F, 4.0F, 18.0F, 8.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.6F, 16.0029F, -31.8596F, 2.2078F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}