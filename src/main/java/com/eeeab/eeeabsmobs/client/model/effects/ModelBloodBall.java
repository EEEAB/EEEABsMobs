package com.eeeab.eeeabsmobs.client.model.effects;

import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityBloodBall;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelBloodBall extends AdvancedEntityModel<EntityBloodBall> {
    public final AdvancedModelBox root;
    public final AdvancedModelBox cube_r1;
    public final AdvancedModelBox cube_r2;

    public ModelBloodBall() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
        this.cube_r1 = new AdvancedModelBox(this, "cube_r1");
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
        this.cube_r2 = new AdvancedModelBox(this, "cube_r2");
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.cube_r2.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
    }

    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.cube_r1, this.cube_r2);
    }

    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root, this.cube_r1, this.cube_r2);
    }

    public void setupAnim(EntityBloodBall entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.rotateAngleY = (float) (netHeadYaw * (Math.PI / 180F));
        this.root.rotateAngleX = (float) (headPitch * (Math.PI / 180F));
    }
}
