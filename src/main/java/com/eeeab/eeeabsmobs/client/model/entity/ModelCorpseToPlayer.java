package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationCommon;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationCorpse;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseToPlayer;
import net.minecraft.client.model.geom.ModelPart;

public class ModelCorpseToPlayer extends EMHierarchicalModel<EntityCorpseToPlayer> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart upper;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ModelCorpseToPlayer(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.head = this.upper.getChild("head");
        this.rightArm = this.upper.getChild("rightArm");
        this.leftArm = this.upper.getChild("leftArm");
        ModelPart lower = this.root.getChild("lower");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftLeg = lower.getChild("leftLeg");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityCorpseToPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        setStaticRotationAngle(this.rightArm, -40, 0, 0);
        setStaticRotationAngle(this.leftArm, -40, 0, 0);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //Walk
        float walkSpeed = 0.85F;
        float walkDegree = 0.8F;
        this.flap(this.root, walkSpeed, walkDegree * 0.08F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entity.isNoAnimation()) {
            this.walk(this.rightArm, walkSpeed, walkDegree, true, -1, -0.1F, limbSwing, limbSwingAmount);
            this.walk(this.leftArm, walkSpeed, walkDegree, false, -1, 0.1F, limbSwing, limbSwingAmount);
            this.flap(this.rightArm, walkSpeed, walkDegree, true, -1, -0.5F, limbSwing, limbSwingAmount);
            this.flap(this.leftArm, walkSpeed, walkDegree, true, -1, 0.5F, limbSwing, limbSwingAmount);
        }

        //Idle
        float speed = 0.12F;
        float degree = 0.1F;
        if (entity.isAlive()) {
            this.walk(head, speed, degree, false, 0.5F, -0.05F, frame, 1);
            this.walk(upper, speed, degree, true, 0.5F, -0.05F, frame, 1);
            this.walk(rightArm, speed, degree, true, 0, -0.05F, frame, 1);
            this.swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            this.walk(leftArm, speed, degree, false, 0, -0.05F, frame, 1);
            this.swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
        this.animate(entity.attackAnimation1, AnimationCorpse.ATTACK_1, ageInTicks);
        this.animate(entity.attackAnimation2, AnimationCorpse.ATTACK_2, ageInTicks);
        this.animate(entity.attackAnimation3, AnimationCorpse.ATTACK_3, ageInTicks);
        this.animate(entity.spawnAnimation, AnimationCommon.SPAWN,ageInTicks);
    }
}
