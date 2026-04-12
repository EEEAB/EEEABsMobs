package com.eeeab.eeeabsmobs.client.model.entity.effect;

import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityAnnihilatorMissile;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelAnnihilatorMissile extends ModHierarchicalModel<EntityAnnihilatorMissile> {
    private final ModelPart root;
    private final ModelPart missile;

    public ModelAnnihilatorMissile(ModelPart root) {
        this.root = root.getChild("root");
        this.missile = this.root.getChild("missile");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition missile = root.addOrReplaceChild("missile", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -6.5F, 3.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F))
                .texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));
        PartDefinition cube_r1 = missile.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -5.0F, -1.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.5708F));
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
    }
}
