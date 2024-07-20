package com.eeeab.eeeabsmobs.sever.integration.curios.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class ModelSoulSummoningNecklace extends HumanoidModel<LivingEntity> {
    public ModelSoulSummoningNecklace(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.getChild("body");
        body.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
