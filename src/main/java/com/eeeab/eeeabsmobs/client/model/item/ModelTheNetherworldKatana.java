package com.eeeab.eeeabsmobs.client.model.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelTheNetherworldKatana extends EntityModel<Entity> {
    private final ModelPart root;

    public ModelTheNetherworldKatana(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 22.0F, 1.0F, -1.5708F, 0.0F, -3.1416F));
        PartDefinition blade = root.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(206, 2).mirror().addBox(-4.5F, -72.4F, -2.3F, 9.0F, 71.0F, 5.0F, new CubeDeformation(-1.5F)).mirror(false)
                .texOffs(230, 74).mirror().addBox(-2.0F, -25.4F, -2.3F, 7.0F, 24.0F, 5.0F, new CubeDeformation(-1.4F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -6.1F, 0.8F, 0.0F, -1.5708F, 0.0F));
        PartDefinition cusp = blade.addOrReplaceChild("cusp", CubeListBuilder.create(), PartPose.offset(-2.5F, -70.6F, 0.0F));
        PartDefinition cube_r1 = cusp.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(207, 105).mirror().addBox(-2.0F, -15.5F, -2.3F, 4.0F, 17.0F, 5.0F, new CubeDeformation(-1.5001F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.384F));
        PartDefinition cube_r2 = cusp.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(207, 83).mirror().addBox(-2.0F, -4.5F, -2.3F, 4.0F, 6.0F, 5.0F, new CubeDeformation(-1.501F)).mirror(false), PartPose.offsetAndRotation(1.1F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0175F));
        PartDefinition cube_r3 = cusp.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(207, 83).mirror().addBox(-2.0F, -6.5F, -2.3F, 4.0F, 8.0F, 5.0F, new CubeDeformation(-1.501F)).mirror(false), PartPose.offsetAndRotation(2.1F, -0.1F, 0.0F, 0.0F, 0.0F, 0.0175F));
        PartDefinition cube_r4 = cusp.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(207, 83).mirror().addBox(-2.0F, -9.5F, -2.3F, 4.0F, 11.0F, 5.0F, new CubeDeformation(-1.501F)).mirror(false), PartPose.offsetAndRotation(3.0F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0175F));
        PartDefinition cube_r5 = cusp.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(231, 106).mirror().addBox(-2.0F, -14.5F, -2.3F, 4.0F, 16.0F, 5.0F, new CubeDeformation(-1.49F)).mirror(false), PartPose.offsetAndRotation(5.0F, -0.3F, 0.0F, 0.0F, 0.0F, 0.0175F));
        PartDefinition cube_r6 = cusp.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(207, 83).mirror().addBox(-2.0F, -11.5F, -2.3F, 4.0F, 13.0F, 5.0F, new CubeDeformation(-1.501F)).mirror(false), PartPose.offsetAndRotation(4.0F, -0.3F, 0.0F, 0.0F, 0.0F, 0.0175F));
        PartDefinition hilt = root.addOrReplaceChild("hilt", CubeListBuilder.create(), PartPose.offset(-0.75F, -8.1F, 0.8F));
        PartDefinition cube_r7 = hilt.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(208, 130).mirror().addBox(-5.0F, -2.5F, -4.3F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.75F, -0.5F, -0.5F, 0.0F, -0.7854F, 0.0F));
        PartDefinition cube_r8 = hilt.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(236, 44).mirror().addBox(-2.5F, -10.5F, -2.3F, 5.0F, 21.0F, 5.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.25F, 9.5F, 0.0F, 0.0F, -0.7854F, 0.0F));
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