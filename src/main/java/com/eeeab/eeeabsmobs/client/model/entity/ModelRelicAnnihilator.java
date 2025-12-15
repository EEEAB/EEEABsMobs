package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRelicAnnihilator;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ModelRelicAnnihilator extends ModHierarchicalModel<EntityRelicAnnihilator> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart upper;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart saw;
    private final ModelPart leftHand;
    private final ModelPart rightHand;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart leftWheel;
    private final ModelPart rightWheel;

    public ModelRelicAnnihilator(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.head = this.upper.getChild("head");
        this.leftArm = this.upper.getChild("leftArm");
        this.rightArm = this.upper.getChild("rightArm");
        this.leftHand = this.leftArm.getChild("leftHand");
        this.rightHand = this.rightArm.getChild("rightHand");
        this.saw = this.rightHand.getChild("saw");
        ModelPart lower = this.root.getChild("lower");
        this.leftLeg = lower.getChild("leftLeg");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftWheel = this.leftLeg.getChild("leftLegJoint1").getChild("leftWheelJoint").getChild("leftWheel");
        this.rightWheel = this.rightLeg.getChild("rightLegJoint1").getChild("rightWheelJoint").getChild("rightWheel");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create().texOffs(0, 18).addBox(-8.5F, -26.5F, -5.0F, 17.0F, 21.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-14.5F, -25.5F, -4.5F, 29.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(52, 87).addBox(-4.5F, -5.5F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(28, 62).addBox(13.5F, -22.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 62).mirror().addBox(-18.5F, -22.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -35.5F, 2.0F));

        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -19.75F, -6.0F));

        PartDefinition up = head.addOrReplaceChild("up", CubeListBuilder.create().texOffs(72, 32).addBox(-4.5F, -4.5F, -4.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.25F, -3.0F));

        PartDefinition down = head.addOrReplaceChild("down", CubeListBuilder.create().texOffs(76, 0).addBox(-4.5F, -5.0F, -4.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.25F, -3.0F));

        PartDefinition scope = head.addOrReplaceChild("scope", CubeListBuilder.create().texOffs(58, 71).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.5F)), PartPose.offset(0.0F, -0.75F, -1.0F));

        PartDefinition infrared = head.addOrReplaceChild("infrared", CubeListBuilder.create().texOffs(18, 110).addBox(-3.5F, -3.5F, -2.5F, 7.0F, 7.0F, 9.0F, new CubeDeformation(-2.4F)), PartPose.offset(0.0F, 2.25F, -2.7F));

        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 49).addBox(-2.0F, -6.5F, -3.5F, 7.0F, 24.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(44, 18).addBox(-1.0F, 17.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(16.5F, -21.0F, 0.0F));

        PartDefinition leftHand = leftArm.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(45, 40).addBox(-4.0F, -1.0F, -4.5F, 9.0F, 22.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 20.5F, 0.0F));

        PartDefinition muzzle = leftHand.addOrReplaceChild("muzzle", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition cover = leftHand.addOrReplaceChild("cover", CubeListBuilder.create().texOffs(54, 18).addBox(-5.7365F, 3.0284F, -5.5F, 9.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(74, 90).addBox(3.2635F, -2.9716F, -4.0F, 3.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 17.0F, 0.0F));

        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-5.0F, -6.5F, -3.5F, 7.0F, 24.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(44, 18).mirror().addBox(-4.0F, 17.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-16.5F, -21.0F, 0.0F));

        PartDefinition rightHand = rightArm.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(45, 40).mirror().addBox(-5.0F, -1.0F, -4.5F, 9.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 104).mirror().addBox(-4.3F, 12.0F, -3.5F, 2.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 104).addBox(1.3F, 12.0F, -3.5F, 2.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(28, 62).addBox(-3.0F, 20.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 20.5F, 0.0F));

        PartDefinition saw = rightHand.addOrReplaceChild("saw", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 22.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r1 = saw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 71).mirror().addBox(-3.9393F, -2.6F, -5.3536F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

        PartDefinition blade1 = saw.addOrReplaceChild("blade1", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 0.0F, 3.5F, 0.0873F, 0.0F, -0.1309F));

        PartDefinition cube_r2 = blade1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(54, 32).mirror().addBox(0.3011F, -1.8847F, -2.2353F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(-1.2701F, 0.6468F, 4.7571F, -0.0632F, -0.2196F, -0.048F));

        PartDefinition cube_r3 = blade1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(81, 58).mirror().addBox(-2.8627F, -2.419F, -4.3756F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-1.4521F, 1.1468F, 0.9538F, 0.0F, -0.7854F, 0.0F));

        PartDefinition blade2 = saw.addOrReplaceChild("blade2", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, -3.5F, -0.0873F, 0.0F, 0.1309F));

        PartDefinition cube_r4 = blade2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(19, 102).mirror().addBox(-4.1723F, -2.2928F, -2.2157F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(1.2701F, 0.734F, -5.7532F, 0.0632F, -0.2196F, 0.048F));

        PartDefinition cube_r5 = blade2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(83, 14).mirror().addBox(-3.0468F, -2.7662F, -5.3382F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(1.4521F, 1.234F, -1.9499F, 0.0F, -0.7854F, 0.0F));

        PartDefinition blade3 = saw.addOrReplaceChild("blade3", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 0.0F, -3.5F, 0.1304F, -0.0114F, 0.0865F));

        PartDefinition cube_r6 = blade3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(98, 26).mirror().addBox(-5.5027F, -2.0185F, -1.4065F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(-4.7831F, 0.5168F, -2.2469F, 0.2802F, 1.346F, 0.2117F));

        PartDefinition cube_r7 = blade3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(81, 46).mirror().addBox(-3.7341F, -2.6147F, -3.9223F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-0.9651F, 1.0168F, -2.4436F, 0.0F, 0.7854F, 0.0F));

        PartDefinition blade4 = saw.addOrReplaceChild("blade4", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, 4.5F, -0.1304F, -0.0114F, -0.0865F));

        PartDefinition cube_r8 = blade4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(47, 98).mirror().addBox(-1.0293F, -2.159F, -1.4262F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(4.7604F, 0.7769F, 0.264F, -0.2802F, 1.346F, -0.2117F));

        PartDefinition cube_r9 = blade4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(81, 78).mirror().addBox(-3.5501F, -2.5705F, -2.9598F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.9424F, 1.2769F, 0.4607F, 0.0F, 0.7854F, 0.0F));

        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create().texOffs(82, 70).addBox(-6.5F, -0.5F, -2.0F, 13.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -35.5F, 2.0F));

        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(7.5F, 1.5477F, 0.6121F));

        PartDefinition leftLegJoint2 = leftLeg.addOrReplaceChild("leftLegJoint2", CubeListBuilder.create(), PartPose.offset(1.0F, 12.5477F, -3.8879F));

        PartDefinition cube_r10 = leftLegJoint2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 80).addBox(-2.5F, -7.5F, -2.5F, 6.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -9.0953F, 1.7759F, -0.2618F, 0.0F, 0.0F));

        PartDefinition leftLegJoint1 = leftLeg.addOrReplaceChild("leftLegJoint1", CubeListBuilder.create(), PartPose.offset(1.0F, 14.4523F, -3.1121F));

        PartDefinition cube_r11 = leftLegJoint1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(96, 90).addBox(-2.5F, -5.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition leftWheelJoint = leftLegJoint1.addOrReplaceChild("leftWheelJoint", CubeListBuilder.create().texOffs(28, 49).addBox(0.5F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 6.5F, 2.0F));

        PartDefinition leftWheel = leftWheelJoint.addOrReplaceChild("leftWheel", CubeListBuilder.create().texOffs(24, 84).addBox(-4.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.5F, 0.0F));

        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-7.5F, 1.5477F, 0.6121F));

        PartDefinition rightLegJoint2 = rightLeg.addOrReplaceChild("rightLegJoint2", CubeListBuilder.create(), PartPose.offset(-1.0F, 12.5477F, -3.8879F));

        PartDefinition cube_r12 = rightLegJoint2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 80).mirror().addBox(-3.5F, -7.5F, -2.5F, 6.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, -9.0953F, 1.7759F, -0.2618F, 0.0F, 0.0F));

        PartDefinition rightLegJoint1 = rightLeg.addOrReplaceChild("rightLegJoint1", CubeListBuilder.create(), PartPose.offset(-1.0F, 14.4523F, -3.1121F));

        PartDefinition cube_r13 = rightLegJoint1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(96, 90).mirror().addBox(-2.5F, -5.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rightWheelJoint = rightLegJoint1.addOrReplaceChild("rightWheelJoint", CubeListBuilder.create().texOffs(28, 49).mirror().addBox(-2.5F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 6.5F, 2.0F));

        PartDefinition rightWheel = rightWheelJoint.addOrReplaceChild("rightWheel", CubeListBuilder.create().texOffs(24, 84).mirror().addBox(-0.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 8.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityRelicAnnihilator entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity.dieAnimation, AnimationRelicAnnihilator.STUN, ageInTicks);
        this.animate(entity.stunAnimation, AnimationRelicAnnihilator.STUN, ageInTicks);
        this.animate(entity.slashAnimation, AnimationRelicAnnihilator.SLASH, ageInTicks);
        this.animate(entity.swingAnimation, AnimationRelicAnnihilator.SWING, ageInTicks);
        this.animate(entity.stabAnimation, AnimationRelicAnnihilator.STAB, ageInTicks);
        this.animate(entity.cycloneAnimation, AnimationRelicAnnihilator.CYCLONE, ageInTicks);
        this.animate(entity.shot1Animation, AnimationRelicAnnihilator.SHOT_START, ageInTicks);
        this.animate(entity.shot2Animation, AnimationRelicAnnihilator.SHOT_LOOP, ageInTicks);
        this.animate(entity.shot3Animation, AnimationRelicAnnihilator.SHOT_END, ageInTicks);
        this.animate(entity.trickshot1Animation, AnimationRelicAnnihilator.TRICK_SHOT_START, ageInTicks);
        this.animate(entity.trickshot2Animation, AnimationRelicAnnihilator.TRICK_SHOT_LOOP, ageInTicks);
        this.animate(entity.trickshot3Animation, AnimationRelicAnnihilator.TRICK_SHOT_END, ageInTicks);
        this.animate(entity.laserAnimation, AnimationRelicAnnihilator.LASER, ageInTicks);
        this.animate(entity.groundPoundAnimation, AnimationRelicAnnihilator.GROUND_POUND, ageInTicks);
        this.animate(entity.groundsSlam1Animation, AnimationRelicAnnihilator.GROUNDS_SLAM_START, ageInTicks);
        this.animate(entity.groundsSlam2Animation, AnimationRelicAnnihilator.GROUNDS_SLAM_KEEP, ageInTicks);
        this.animate(entity.groundsSlam3Animation, AnimationRelicAnnihilator.GROUNDS_SLAM_END, ageInTicks);
        //LootAt & Idle & Walk
        float pitch = headPitch * 0.017453292F;
        float headYaw = netHeadYaw * 0.017453292F;
        float cycle = 0.5F;
        float idle = ((Mth.sin(ageInTicks * cycle * 0.1F) + 1.0F) * (1.0F - limbSwingAmount)) * 0.75F;
        float rebound = limbSwing * cycle % Mth.PI / Mth.PI;
        float delta = ageInTicks - entity.tickCount;
        rebound = 1.0F - rebound;
        rebound *= rebound;
        this.head.xRot += pitch * 0.75F + idle * 0.1F;
        this.head.yRot += headYaw * 0.5F;
        this.upper.yRot += headYaw * 0.25F;
        this.upper.y += Mth.sin(rebound * 3.1415927F) * 1.5F * limbSwingAmount + idle * 0.4F;
        float progress = entity.controlled.getAnimationFraction(delta);
        if (entity.isActive()) lookAtAnimation(netHeadYaw * progress, headPitch * progress, 1F, this.leftArm);

        this.leftArm.xRot += (Mth.cos(limbSwing * cycle) * -0.5F) * limbSwingAmount * 0.1F + 0.0524F - idle * 0.1F;
        this.leftArm.zRot += (Mth.cos(limbSwing * cycle) * 0.25F) * limbSwingAmount * 0.1F - 0.0959F;
        this.leftArm.y += Mth.sin(rebound * 3.1415927F) * 0.5F * limbSwingAmount;
        this.rightArm.xRot += (Mth.cos(limbSwing * cycle - 3.1415927F) * -0.5F) * limbSwingAmount * 0.1F + 0.0524F - idle * 0.1F;
        this.rightArm.zRot -= (-Mth.cos(limbSwing * cycle) * -0.25F) * limbSwingAmount * 0.1F - 0.0959F;
        this.rightArm.y += Mth.sin(rebound * 3.1415927F) * 0.5F * limbSwingAmount;

        this.leftHand.xRot += (Mth.cos(limbSwing * cycle) * 0.5F) * limbSwingAmount * 0.1F - 0.1047F;
        this.rightHand.xRot += (Mth.cos(limbSwing * cycle - 3.1415927F) * 0.5F) * limbSwingAmount * 0.1F - 0.1047F;

        progress = entity.sawControlled.getAnimationFraction(delta);
        float timeBasedRotation = (ageInTicks * 50F) % 360F;
        this.saw.yRot = (-timeBasedRotation * progress) * ((float) Math.PI / 180F);

        this.leftLeg.y += Mth.sin(rebound * 3.1415927F) * 2.5F * limbSwingAmount;
        this.rightLeg.y += Mth.sin(rebound * 3.1415927F) * 2.5F * limbSwingAmount;
        this.applyWheelAnimations((limbSwing * 0.3f) % 1.0f, limbSwingAmount, entity);
    }

    private void applyWheelAnimations(float limbSwing, float limbSwingAmount, EntityRelicAnnihilator entity) {
        if (entity.getAnimation() == entity.groundsSlam2Animation) return;
        float wheelSpeed = 0.1F + limbSwingAmount * 0.2F;
        float direction = getMovementDirection(entity);
        float wheelTime = (limbSwing * wheelSpeed * direction) % 1F;
        if (wheelTime < 0) wheelTime += 1F;
        float wheelRotation = Mth.lerp(wheelTime, 0F, 360F) * 0.017453292F;
        this.leftWheel.xRot += wheelRotation;
        this.rightWheel.xRot += wheelRotation;
    }


    private float getMovementDirection(EntityRelicAnnihilator entity) {
        Vec3 motion = entity.getDeltaMovement();
        if (motion.length() < 0.03) return 1F;
        float yawRad = entity.getYRot() * 0.017453292F;
        Vec3 lookDirection = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
        Vec3 horizontalMotion = new Vec3(motion.x, 0, motion.z);
        if (horizontalMotion.length() < 0.03) return 1.0F;
        double dotProduct = lookDirection.dot(horizontalMotion.normalize());
        return dotProduct < -0.6 ? -1F : 1F;
    }
}
