package com.eeeab.eeeabsmobs.client.model.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class ModelDoomboltAxe extends Model {
    private final ModelPart root;

    public ModelDoomboltAxe(ModelPart root) {
        super(RenderType::entitySolid);
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.0284F, -1.0028F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(20, 0).addBox(-1.5F, -7.0284F, -1.5028F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(10, 24).addBox(-1.5F, 1.4716F, -1.5028F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(10, 24).addBox(-1.5F, 17.9716F, -1.5028F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 21).addBox(-1.0F, 19.9716F, -1.0028F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(10, 24).addBox(-1.5F, 6.9716F, -1.5028F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(29, 7).addBox(-1.0F, -6.5284F, 1.4972F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(30, 7).addBox(-0.5F, -6.5284F, -4.5028F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(15, 0).addBox(-1.0F, -6.5284F, 6.4972F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(29, 7).addBox(-1.0F, -2.5284F, 1.4972F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(30, 7).addBox(-0.5F, -2.5284F, -2.5028F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(15, 0).addBox(-1.0F, -2.5284F, 6.4972F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 10).addBox(-1.5F, -7.5284F, 4.4972F, 3.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 0).addBox(-0.51F, -9.0384F, -9.5628F, 1.02F, 8.02F, 5.02F, new CubeDeformation(0.0F)).texOffs(31, 17).addBox(-0.5F, -11.1246F, -0.9635F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0284F, 0.0028F, 0.0F, 1.5708F, 0.0F));
        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(6, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -11.1246F, 0.0258F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 26).addBox(-0.9985F, -8.0564F, 7.2751F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0015F, 3.8235F, -0.8252F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r3 = root.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(30, 12).addBox(-0.4985F, -1.986F, -6.3661F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(8, 13).addBox(-0.4985F, -7.986F, -6.3661F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0015F, 3.9235F, -0.6252F, 0.3927F, 0.0F, 0.0F));
        PartDefinition cube_r4 = root.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(30, 7).addBox(-0.01F, -1.01F, -1.51F, 1.02F, 2.02F, 3.02F, new CubeDeformation(-0.02F)), PartPose.offsetAndRotation(-0.5F, -0.7284F, -2.9028F, 0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r5 = root.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(20, 20).addBox(-1.9985F, -7.5276F, -6.3645F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0015F, 3.9235F, -0.8252F, -0.7854F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}