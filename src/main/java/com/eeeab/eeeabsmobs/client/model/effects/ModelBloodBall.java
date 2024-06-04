package com.eeeab.eeeabsmobs.client.model.effects;

import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityBloodBall;
import com.eeeab.animate.client.model.EMHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelBloodBall extends EMHierarchicalModel<EntityBloodBall> {
    public final ModelPart ball_1;
    public final ModelPart ball_2;
    public final ModelPart ball_3;

    public ModelBloodBall(ModelPart root) {
        this.ball_1 = root.getChild("ball_1");
        this.ball_2 = root.getChild("ball_2");
        this.ball_3 = root.getChild("ball_3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("ball_1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
        partdefinition.addOrReplaceChild("ball_2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
        partdefinition.addOrReplaceChild("ball_3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.ball_1;
    }

    public void setupAnim(EntityBloodBall bloodBall, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.ball_1.yRot = (float) (netHeadYaw * (Math.PI / 180F));
        this.ball_1.xRot = (float) (headPitch * (Math.PI / 180F));
    }
}
