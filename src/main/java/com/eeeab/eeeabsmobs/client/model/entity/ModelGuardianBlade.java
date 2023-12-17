package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityGuardianBlade;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelGuardianBlade extends AdvancedEntityModel<EntityGuardianBlade> {
    public AdvancedModelBox root;
    //private AdvancedModelBox upper;
    //private AdvancedModelBox lower;

    public ModelGuardianBlade() {
        texHeight = 256;
        texWidth = 256;

        root = new AdvancedModelBox(this, "root");
        root.setRotationPoint(0.0F, 12.0F, 0.0F);
        root.rotateAngleX = 0.2182F;
        root.setTextureOffset(0, 0).addBox(-4.0F, -24.0F, -10.0F, 6.0F, 48.0F, 56.0F)
                .setTextureOffset(110, 136).addBox(-4.0F, -32.0F, -2.0F, 6.0F, 8.0F, 48.0F)
                .setTextureOffset(0, 206).addBox(-4.0F, -40.0F, 8.0F, 6.0F, 8.0F, 42.0F)
                .setTextureOffset(110, 206).addBox(-4.0F, 32.0F, 8.0F, 6.0F, 8.0F, 42.0F)
                .setTextureOffset(0, 142).addBox(-4.0F, 24.0F, -2.0F, 6.0F, 8.0F, 48.0F);

        //root = new AdvancedModelBox(this, "root");
        //root.setPos(0.0F, 24.0F, 0F);
        //root.mirror = true;
        //root.setTextureOffset(0, 0).addBox(-2.0F, -13.0F, -11.0F, 4.0F, 13.0F, 35.0F)
        //        .setTextureOffset(0, 180).addBox(0.0F, -13.0F, 24.0F, 0.0F, 13.0F, 35.0F);
        //
        //upper = new AdvancedModelBox(this, "upper");
        //upper.setPos(0.0F, 0.0F, 0F);
        //root.addChild(upper);
        //upper.setTextureOffset(89, 0).addBox(-1.0F, -34.0F, -11.0F, 2.0F, 21.0F, 24.0F)
        //        .setTextureOffset(0, 164).addBox(0.0F, -34.0F, 13.0F, 0.0F, 21.0F, 24.0F)
        //        .setTextureOffset(0, 154).addBox(0.0F, -55.0F, -3.0F, 0.0F, 21.0F, 8.0F)
        //        .setTextureOffset(0, 0).addBox(-0.5F, -55.0F, -11.0F, 1.0F, 21.0F, 8.0F)
        //        .setTextureOffset(0, 107).addBox(0.0F, -63.0F, -11.0F, 0.0F, 8.0F, 42.0F)
        //        .setTextureOffset(0, 0).addBox(-0.5F, -55.0F, -11.0F, 1.0F, 21.0F, 8.0F);
        //
        //lower = new AdvancedModelBox(this, "lower");
        //lower.setPos(0.0F, -14.0F, 0F);
        //root.addChild(lower);
        //lower.mirror = true;
        //lower.setTextureOffset(7, 53).addBox(-1.0F, 13.0F, -11.0F, 2.0F, 21.0F, 35.0F)
        //        .setTextureOffset(0, 174).addBox(0.0F, 14.0F, 24.0F, 0.0F, 20.0F, 60.0F);
        updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(root/*,upper,lower*/);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(root);
    }

    @Override
    public void setupAnim(EntityGuardianBlade entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
