package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.EMHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationCorpseWarlock;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelCorpseWarlock extends EMHierarchicalModel<EntityCorpseWarlock> {
    private final ModelPart root;
    private final ModelPart upper;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart cloak;
    public final ModelPart head;

    public ModelCorpseWarlock(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.head = this.upper.getChild("head");
        this.rightArm = this.upper.getChild("rightArm");
        this.leftArm = this.upper.getChild("leftArm");
        this.cloak = this.upper.getChild("body").getChild("cloak");
        ModelPart lower = this.root.getChild("lower");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftLeg = lower.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -11.8F, 0.0F));
        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-3.7F, -4.7F, -4.3F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0872F, 0.7844F, 0.0617F));
        PartDefinition head_r2 = head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.7F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition body = upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(26, 36).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 17.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, -11.5338F, 2.7579F));
        PartDefinition cloak_r1 = cloak.addOrReplaceChild("cloak_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-6.5F, -1.0F, 0.0F, 13.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(26, 18).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(26, 18).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.5F, -10.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(44, 18).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 0.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(44, 18).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityCorpseWarlock entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        lookAtAnimation(netHeadYaw, headPitch, 1.0F, this.head);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //Walk
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        flap(root, walkSpeed, walkDegree * 0.08F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        walk(leftLeg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        walk(rightLeg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        if (entity.isNoAnimation()) {
            walk(rightArm, walkSpeed, walkDegree, true, -0.2F, -0.1F, limbSwing, limbSwingAmount);
            walk(leftArm, walkSpeed, walkDegree, false, -0.2F, 0.1F, limbSwing, limbSwingAmount);
            walk(cloak, walkSpeed * 0.5F, walkDegree * 0.5F, false, 0F, 0F, limbSwing, limbSwingAmount);
            flap(rightArm, walkSpeed * 0.2F, walkDegree * 0.2F, true, 0.2F, -0.2F, limbSwing, limbSwingAmount);
            flap(leftArm, walkSpeed * 0.2F, walkDegree * 0.2F, true, 0.2F, 0.2F, limbSwing, limbSwingAmount);
        }

        //Idle
        float speed = 0.12F;
        float degree = 0.1F;
        if (entity.isAlive()) {
            walk(upper, 0.1F, 0.005F, true, 0, -0.005F, frame, 1);
            walk(rightArm, speed, degree, true, 0, 0, frame, 1);
            swing(rightArm, speed, degree, true, 0, 0, frame, 1);
            walk(leftArm, speed, degree, false, 0, 0, frame, 1);
            swing(leftArm, speed, degree, false, 0, 0, frame, 1);
        }
        this.animate(entity.attackAnimation, AnimationCorpseWarlock.ATTACK, ageInTicks, 1.0F);
        this.animate(entity.tearSpaceAnimation, AnimationCorpseWarlock.TEAR_SPACE, ageInTicks, 1.0F);
        this.animate(entity.teleportAnimation, AnimationCorpseWarlock.TELEPORT, ageInTicks, 1.0F);
        this.animate(entity.vampireAnimation, entity.isHeal() ? AnimationCorpseWarlock.VAMPIRE_HEAL : AnimationCorpseWarlock.VAMPIRE_ATTACK, ageInTicks, 1.0F);
        this.animate(entity.robustAnimation, AnimationCorpseWarlock.ROBUST_ATTACK, ageInTicks, 1.0F);
        if (entity.getAnimation() == entity.summonAnimation || entity.getAnimation() == entity.frenzyAnimation || entity.getAnimation() == entity.babbleAnimation) {
            float animationSpeed = 1.0F;
            if (entity.getAnimation() == entity.babbleAnimation) {
                head.xRot = -10F * Mth.PI / 180F;
                animationSpeed = 0.5F;
            }
            rightArm.z = 0.0F;
            rightArm.x = -6.0F;
            leftArm.z = 0.0F;
            leftArm.x = 6.0F;
            rightArm.xRot = Mth.cos(ageInTicks * 0.6666F * animationSpeed) * 0.5F;
            leftArm.xRot = Mth.cos(ageInTicks * 0.6666F * animationSpeed) * 0.5F;
            rightArm.zRot = 2.3561945F;
            leftArm.zRot = -2.3561945F;
            rightArm.yRot = 0.0F;
            leftArm.yRot = 0.0F;
        }
    }
}