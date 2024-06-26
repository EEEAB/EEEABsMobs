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
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 20.0F, -1.0F, -1.5708F, 0.0F, 0.0F));
        PartDefinition blade = root.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -55.4F, -2.5F, 9.0F, 54.0F, 5.0F, new CubeDeformation(-2.0F))
                .texOffs(28, 17).addBox(-5.0F, -25.4F, -2.5F, 7.0F, 24.0F, 5.0F, new CubeDeformation(-1.9F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.8F, -10.9F, -2.5F, 9.0F, 8.0F, 5.0F, new CubeDeformation(-2.45F)), PartPose.offsetAndRotation(-0.225F, -55.9F, 0.0F, 0.0F, 0.0F, -0.1658F));
        PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.6F, -8.6F, -2.5F, 9.0F, 8.0F, 5.0F, new CubeDeformation(-2.25F)), PartPose.offsetAndRotation(-0.325F, -54.9F, 0.0F, 0.0F, 0.0F, -0.1134F));
        PartDefinition cube_r3 = blade.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -7.0F, -2.5F, 9.0F, 9.0F, 5.0F, new CubeDeformation(-2.15F)), PartPose.offsetAndRotation(-0.125F, -52.9F, 0.0F, 0.0F, 0.0F, -0.0785F));
        PartDefinition hilt = root.addOrReplaceChild("hilt", CubeListBuilder.create(), PartPose.offset(0.25F, -10.0F, 0.0F));
        PartDefinition cube_r4 = hilt.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(33, 1).addBox(-5.0F, -2.5F, -4.5F, 10.0F, 4.0F, 10.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(-0.75F, -0.5F, -0.5F, 0.0F, 0.7854F, 0.0F));
        PartDefinition cube_r5 = hilt.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(47, 49).addBox(-2.5F, -10.5F, -2.5F, 5.0F, 21.0F, 5.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(-0.25F, 9.5F, 0.0F, 0.0F, 0.7854F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}