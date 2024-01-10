package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.impl.test.EntityTester;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelTester extends AdvancedEntityModel<EntityTester> {
    private final AdvancedModelBox root;
    private final AdvancedModelBox upper;
    private final AdvancedModelBox head;
    private final AdvancedModelBox nose;
    private final AdvancedModelBox headwear;
    private final AdvancedModelBox headwear2;
    private final AdvancedModelBox body;
    private final AdvancedModelBox bodywear;
    private final AdvancedModelBox arms;
    private final AdvancedModelBox mirrored;
    private final AdvancedModelBox lower;
    private final AdvancedModelBox right_leg;
    private final AdvancedModelBox left_leg;
    private final ModelAnimator animator;


    public ModelTester() {
        texHeight = 64;
        texWidth = 64;

        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 24.0F, 0.0F);

        upper = new AdvancedModelBox(this, "upper");
        upper.setPos(0.0F, 0.0F, 0.0F);
        root.addChild(upper);

        head = new AdvancedModelBox(this, "head");
        head.setPos(0.0F, -24.0F, 0.0F);
        upper.addChild(head);
        head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F);

        nose = new AdvancedModelBox(this, "nose");
        nose.setPos(0.0F, -2.0F, 0.0F);
        head.addChild(nose);
        nose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F);

        headwear = new AdvancedModelBox(this, "headwear");
        headwear.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(headwear);
        headwear.setTextureOffset(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F);

        headwear2 = new AdvancedModelBox(this, "headwear2");
        offsetAndRotation(headwear2, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F);
        head.addChild(headwear2);
        headwear2.setTextureOffset(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F, 0.0F);

        body = new AdvancedModelBox(this, "body");
        body.setPos(0.0F, -24.0F, 0.0F);
        upper.addChild(body);
        body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F);

        bodywear = new AdvancedModelBox(this, "bodywear");
        bodywear.setPos(0.0F, 0.0F, 0.0F);
        body.addChild(bodywear);
        bodywear.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, 0.5F);

        arms = new AdvancedModelBox(this, "arms");
        offsetAndRotation(arms, 0.0F, 2.95F, -1.05F, -0.7505F, 0.0F, 0.0F);
        body.addChild(arms);
        arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, false)
                .setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, false);

        mirrored = new AdvancedModelBox(this, "mirrored");
        mirrored.setPos(0.0F, 21.05F, 1.05F);
        arms.addChild(mirrored);
        mirrored.setTextureOffset(44, 22).addBox(4.0F, -23.05F, -3.05F, 4.0F, 8.0F, 4.0F, true);


        lower = new AdvancedModelBox(this, "lower");
        lower.setPos(0.0F, 0.0F, 0.0F);
        root.addChild(lower);

        right_leg = new AdvancedModelBox(this, "right_leg");
        right_leg.setPos(-2.0F, -12.0F, 0.0F);
        lower.addChild(right_leg);
        right_leg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F);


        left_leg = new AdvancedModelBox(this, "left_leg");
        left_leg.setPos(2.0F, -12.0F, 0.0F);
        lower.addChild(left_leg);
        left_leg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F);

        animator = ModelAnimator.create();
        updateDefaultPose();

    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.upper, this.nose, this.headwear, this.headwear2, this.body, this.bodywear, this.arms, this.mirrored, this.lower, this.left_leg, this.right_leg);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void setupAnim(EntityTester entityTester, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entityTester, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float delta = ageInTicks - entityTester.tickCount;
        float frame = entityTester.frame + delta;
        float walkSpeed = 0.6F;
        float walkDegree = 0.6F;
        this.faceTarget(netHeadYaw, headPitch, 1.0F, this.head);
        this.flap(this.root, walkSpeed, walkDegree * 0.025F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_leg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.arms, walkSpeed, walkDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.upper, walkSpeed * 0.4F, walkDegree * 0.3F, false, frame, 1);
        this.bob(this.arms, walkSpeed * 0.2F, walkDegree * 0.2F, true, frame, 1);
        this.bob(this.head, walkSpeed * 0.2F, walkDegree * 0.2F, true, frame, 1);
    }

    private void animate(EntityTester entityTester, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animator.update(entityTester);
        if (animator.setAnimation(EntityTester.YES) || animator.setAnimation(EntityTester.NO)) {
            animator.startKeyframe(2);
            animator.rotate(head, toRadians(35), 0, 0);
            animator.rotate(upper, toRadians(5), 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(3);
        }
    }

    private void offsetAndRotation(AdvancedModelBox box, float px, float py, float pz, float rx, float ry, float rz) {
        box.setRotationPoint(px, py, pz);
        box.rotateAngleX = rx;
        box.rotateAngleY = ry;
        box.rotateAngleZ = rz;
    }

    private static float toRadians(double degree) {
        return (float) degree * ((float) Math.PI / 180F);
    }
}
