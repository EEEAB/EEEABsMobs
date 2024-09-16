package com.eeeab.eeeabsmobs.client.model.effects;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalFireball;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelImmortalFireball extends EMHierarchicalModel<EntityImmortalFireball> {
    private final ModelPart root;
    private final ModelPart in;
    private final ModelPart out;

    public ModelImmortalFireball(ModelPart root) {
        this.root = root.getChild("root");
        this.in = this.root.getChild("in");
        this.out = this.root.getChild("out");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 36).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(-1.5F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition out = root.addOrReplaceChild("out", CubeListBuilder.create().texOffs(0, 18).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -4.5F, 0.0F));
        PartDefinition in = root.addOrReplaceChild("in", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityImmortalFireball entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.yRot = toRadians(netHeadYaw);
        this.root.xRot = toRadians(headPitch);
        autorotation(this.in, ageInTicks, true);
        autorotation(this.out, ageInTicks, false);
    }

    private static void autorotation(ModelPart part, float ageInTicks, boolean opposite) {
        part.xRot += (opposite ? -0.3F : 0.3F) * toRadians(Mth.sin(ageInTicks * 0.01F) * 180F);
        part.yRot += (opposite ? -0.3F : 0.3F) * toRadians(Mth.sin(ageInTicks * 0.01F) * 180F);
        part.zRot += (opposite ? -0.3F : 0.3F) * toRadians(Mth.sin(ageInTicks * 0.01F) * 180F);
    }
}