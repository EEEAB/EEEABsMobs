package com.eeeab.eeeabsmobs.client.model.entity.effect;

import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityAnnihilatorMissile;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelAnnihilatorMissile extends ModHierarchicalModel<EntityAnnihilatorMissile> {
    private final ModelPart root;
    private final ModelPart missile;
    private final ModelPart trail;

    public ModelAnnihilatorMissile(ModelPart root) {
        this.root = root.getChild("root");
        this.missile = this.root.getChild("missile");
        this.trail = this.missile.getChild("trail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));
        PartDefinition missile = root.addOrReplaceChild("missile", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -4.5F, 3.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F))
                .texOffs(0, 0).addBox(0.0F, -3.0F, 1.1F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cube_r1 = missile.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -1.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.1F, 0.0F, 0.0F, 1.5708F));
        PartDefinition trail = missile.addOrReplaceChild("trail", CubeListBuilder.create().texOffs(8, 13).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)), PartPose.offset(0.0F, 0.0F, 5.2F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityAnnihilatorMissile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.yRot = toRadians(netHeadYaw);
        this.root.xRot = toRadians(headPitch);
        this.missile.zRot = toRadians((ageInTicks * 30F) % 360F);
        float scale = (float) (0.8 + Math.random() * 0.4);
        trail.xScale = scale;
        trail.yScale = scale;
        trail.zScale = scale;
    }
}
