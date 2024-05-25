package com.eeeab.eeeabsmobs.client.model.effects;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGrenade;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelGrenade extends EMHierarchicalModel<EntityGrenade> {
    public final ModelPart ball;

    public ModelGrenade(ModelPart root) {
        this.ball = root.getChild("ball");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("ball", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.ball;
    }

    public void setupAnim(EntityGrenade entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.ball.yRot = (float) (netHeadYaw * (Math.PI / 180F));
        this.ball.xRot = (float) (headPitch * (Math.PI / 180F));
    }
}
