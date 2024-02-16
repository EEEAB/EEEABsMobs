package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityAbsCorpse;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public abstract class ModelAbsCorpse<T extends EntityAbsCorpse> extends EMCanSpawnEntityModel<T> {

    public ModelAbsCorpse() {
        texHeight = 128;
        texWidth = 128;

        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, -12.0F, 0.0F);
        setRotationAngle(upper, toRadians(5), 0, 0);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, -12.0F, 0.0F);
        head.setTextureOffset(0, 0).addBox(-4.0F, -8.1F, -4.0F, 8.0F, 8.0F, 8.0F)
                .setTextureOffset(32, 0).addBox(-4.0F, -8.1F, -4.0F, 8.0F, 8.0F, 8.0F, -0.3F);
        upper.addChild(head);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, 0.0F, 0.0F);
        body.setTextureOffset(16, 22).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                .setTextureOffset(56, 22).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, -0.2F);
        body.setTextureOffset(0, 40).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 18.0F, 4.0F, 0.5F);
        upper.addChild(body);

        leftArm = new AdvancedModelBox(this, "leftArm");
        leftArm.setPos(5.0F, -10.0F, 0.0F);
        leftArm.setTextureOffset(40, 22).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, true);
        setRotationAngle(leftArm, toRadians(-40), 0, 0);
        upper.addChild(leftArm);

        rightArm = new AdvancedModelBox(this, "rightArm");
        rightArm.setPos(-5.0F, -10.0F, 0.0F);
        rightArm.setTextureOffset(40, 22).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        setRotationAngle(rightArm, toRadians(-40), 0, 0);
        upper.addChild(rightArm);

        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(0.0F, -12.0F, 0.0F);
        root.addChild(lower);

        leftLeg = new AdvancedModelBox(this, "leftLeg");
        leftLeg.setPos(2.0F, 0.0F, 0.0F);
        leftLeg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, true);
        lower.addChild(leftLeg);

        rightLeg = new AdvancedModelBox(this, "rightLeg");
        rightLeg.setPos(-2.0F, 0.0F, 0.0F);
        rightLeg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        lower.addChild(rightLeg);

        animator = ModelAnimator.create();
        updateDefaultPose();
    }


    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.upper, this.lower, this.head, this.rightArm, this.leftArm, this.body, this.rightLeg, this.leftLeg);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }


}
