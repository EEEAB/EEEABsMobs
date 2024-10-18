package com.eeeab.eeeabsmobs.client.model.effects;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalShuriken;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelImmortalShuriken extends EMHierarchicalModel<EntityImmortalShuriken> {
    private final ModelPart root;
    private final ModelPart bladeGroup;

    public ModelImmortalShuriken(ModelPart root) {
        this.root = root.getChild("root");
        this.bladeGroup = this.root.getChild("bladeGroup");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.5F, 0.0F));
        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition bladeGroup = root.addOrReplaceChild("bladeGroup", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 0.0F));
        PartDefinition blade1 = bladeGroup.addOrReplaceChild("blade1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.1309F));
        PartDefinition cube_r2 = blade1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(35, 17).addBox(-5.1213F, -1.5F, -2.5F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(4.9749F, -1.5F, 8.9602F, -0.0632F, 0.2196F, 0.048F));
        PartDefinition cube_r3 = blade1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(5.1569F, -1.0F, 5.1569F, 0.0F, 0.7854F, 0.0F));
        PartDefinition blade2 = bladeGroup.addOrReplaceChild("blade2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, -0.1309F));
        PartDefinition cube_r4 = blade2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(35, 43).addBox(-0.8787F, -1.5F, -2.5F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(-4.9749F, -1.5F, -8.9602F, 0.0632F, 0.2196F, -0.048F));
        PartDefinition cube_r5 = blade2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 39).addBox(-4.0F, -2.0F, -5.0F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-5.1569F, -1.0F, -5.1569F, 0.0F, 0.7854F, 0.0F));
        PartDefinition blade3 = bladeGroup.addOrReplaceChild("blade3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1304F, 0.0114F, -0.0865F));
        PartDefinition cube_r6 = blade3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(35, 30).addBox(-0.8787F, -1.5F, -2.5F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(8.9749F, -1.5F, -4.9602F, 0.2802F, -1.346F, -0.2117F));
        PartDefinition cube_r7 = blade3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 26).addBox(-4.0F, -2.0F, -5.0F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(5.1569F, -1.0F, -5.1569F, 0.0F, -0.7854F, 0.0F));
        PartDefinition blade4 = bladeGroup.addOrReplaceChild("blade4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1304F, 0.0114F, 0.0865F));
        PartDefinition cube_r8 = blade4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(35, 56).addBox(-5.1213F, -1.5F, -2.5F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(-8.9749F, -1.5F, 4.9602F, -0.2802F, -1.346F, 0.2117F));
        PartDefinition cube_r9 = blade4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 52).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-5.1569F, -1.0F, 5.1569F, 0.0F, -0.7854F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityImmortalShuriken entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.yRot = toRadians(netHeadYaw);
        this.root.xRot = toRadians(headPitch);
        this.bladeGroup.yRot += ageInTicks * (float) Math.max(0.01F, Math.min(entity.getDeltaMovement().length() / 0.55F * 0.1F + 0.01F, 0.15F));
    }
}