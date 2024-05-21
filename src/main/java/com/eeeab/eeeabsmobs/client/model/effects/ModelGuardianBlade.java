package com.eeeab.eeeabsmobs.client.model.effects;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianBlade;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelGuardianBlade extends EMHierarchicalModel<EntityGuardianBlade> {
    public ModelPart root;

    public ModelGuardianBlade(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -24.0F, -10.0F, 6.0F, 48.0F, 56.0F, new CubeDeformation(0.0F))
                .texOffs(110, 136).addBox(-4.0F, -32.0F, -2.0F, 6.0F, 8.0F, 48.0F, new CubeDeformation(0.0F))
                .texOffs(0, 206).addBox(-4.0F, -40.0F, 8.0F, 6.0F, 8.0F, 42.0F, new CubeDeformation(0.0F))
                .texOffs(110, 206).addBox(-4.0F, 32.0F, 8.0F, 6.0F, 8.0F, 42.0F, new CubeDeformation(0.0F))
                .texOffs(0, 142).addBox(-4.0F, 24.0F, -2.0F, 6.0F, 8.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.2182F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityGuardianBlade entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
