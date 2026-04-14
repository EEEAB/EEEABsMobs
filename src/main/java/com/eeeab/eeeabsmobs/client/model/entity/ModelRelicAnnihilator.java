package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.animation.KeyframeAnimations;
import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRelicAnnihilator;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
    private final ModelPart leftExhaust;
    private final ModelPart rightExhaust;

    public ModelRelicAnnihilator(ModelPart root) {
        this.root = root.getChild("root");
        this.upper = this.root.getChild("upper");
        this.head = this.upper.getChild("head");
        this.leftArm = this.upper.getChild("leftArm");
        this.rightArm = this.upper.getChild("rightArm");
        this.leftHand = this.leftArm.getChild("leftHand");
        this.leftExhaust = this.leftArm.getChild("leftBooster").getChild("leftExhaust");
        this.rightHand = this.rightArm.getChild("rightHand");
        this.rightExhaust = this.rightArm.getChild("rightBooster").getChild("rightExhaust");
        this.saw = this.rightHand.getChild("saw");
        ModelPart lower = this.root.getChild("lower");
        this.leftLeg = lower.getChild("leftLeg");
        this.rightLeg = lower.getChild("rightLeg");
        this.leftWheel = this.leftLeg.getChild("leftLegUnder").getChild("leftWheelJoint").getChild("leftWheel");
        this.rightWheel = this.rightLeg.getChild("rightLegUnder").getChild("rightWheelJoint").getChild("rightWheel");
        this.leftExhaust.visible = false;
        this.rightExhaust.visible = false;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create().texOffs(0, 18).addBox(-8.5F, -26.5F, -5.0F, 17.0F, 21.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-14.5F, -25.5F, -4.5F, 29.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(1, 94).addBox(-4.5F, -5.5F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(67, 0).addBox(14.5F, -22.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(67, 0).mirror().addBox(-19.5F, -22.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -35.5F, 2.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -19.75F, -6.0F));
        PartDefinition up = head.addOrReplaceChild("up", CubeListBuilder.create().texOffs(72, 32).addBox(-4.5F, -3.0F, -4.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(-0.45F)), PartPose.offset(0.0F, -3.35F, -3.0F));
        PartDefinition down = head.addOrReplaceChild("down", CubeListBuilder.create().texOffs(76, 0).addBox(-4.5F, -3.0F, -4.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(-0.45F)), PartPose.offset(0.0F, 1.75F, -3.0F));
        PartDefinition scope = head.addOrReplaceChild("scope", CubeListBuilder.create().texOffs(62, 71).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.5F)), PartPose.offset(0.0F, -1.25F, -1.0F));
        PartDefinition infrared = head.addOrReplaceChild("infrared", CubeListBuilder.create().texOffs(108, 8).addBox(-0.5F, -1.1F, -1.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(-0.5F)), PartPose.offset(-1.0F, 1.75F, -1.7F));
        PartDefinition leftArm = upper.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(16.5F, -21.0F, 0.0F));
        PartDefinition cube_r1 = leftArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(44, 18).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 18.5F, 0.0F, 0.0873F, 0.0611F, 1.5708F));
        PartDefinition cube_r2 = leftArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(21, 96).addBox(-4.5F, -4.5F, -5.0F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2071F, -1.7929F, 0.0F, 0.0873F, 0.0F, -0.0436F));
        PartDefinition cube_r3 = leftArm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 49).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 15.0F, 7.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(1.5F, 9.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition leftBooster = leftArm.addOrReplaceChild("leftBooster", CubeListBuilder.create(), PartPose.offset(2.2071F, -1.6213F, 4.0F));
        PartDefinition cube_r4 = leftBooster.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 80).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 1.5708F, 0.0F, -0.7854F));
        PartDefinition leftExhaust = leftBooster.addOrReplaceChild("leftExhaust", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 5.35F));
        PartDefinition cube_r5 = leftExhaust.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(4, 108).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.45F, 1.5708F, 0.0F, -0.7854F));
        PartDefinition cube_r6 = leftExhaust.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 106).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.6F, 1.5708F, 0.0F, -0.7854F));
        PartDefinition leftHand = leftArm.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(45, 40).addBox(-4.0F, -1.0F, -4.5F, 9.0F, 22.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 20.5F, 0.0F));
        PartDefinition muzzle = leftHand.addOrReplaceChild("muzzle", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
        PartDefinition cover = leftHand.addOrReplaceChild("cover", CubeListBuilder.create().texOffs(54, 18).addBox(-5.7365F, 3.0284F, -5.5F, 9.0F, 3.0F, 11.0F, new CubeDeformation(0.15F)).texOffs(46, 79).addBox(3.2635F, -2.9716F, -4.0F, 3.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 17.0F, 0.0F));
        PartDefinition rightArm = upper.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-16.5F, -21.0F, 0.0F));
        PartDefinition cube_r7 = rightArm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(21, 96).mirror().addBox(-4.5F, -4.5F, -5.0F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.2071F, -1.7929F, 0.0F, 0.0873F, 0.0F, 0.0436F));
        PartDefinition cube_r8 = rightArm.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(44, 18).mirror().addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 18.5F, 0.0F, -0.0873F, 0.0611F, 1.5708F));
        PartDefinition cube_r9 = rightArm.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-3.5F, -7.0F, -3.5F, 7.0F, 15.0F, 7.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 9.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition rightBooster = rightArm.addOrReplaceChild("rightBooster", CubeListBuilder.create(), PartPose.offset(-2.2071F, -1.6213F, 4.0F));
        PartDefinition cube_r10 = rightBooster.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(24, 80).mirror().addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 1.5708F, 0.0F, 0.7854F));
        PartDefinition rightExhaust = rightBooster.addOrReplaceChild("rightExhaust", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 5.35F));
        PartDefinition cube_r11 = rightExhaust.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(2, 108).mirror().addBox(-1.5F, -2.5F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 1.45F, 1.5708F, 0.0F, 0.7854F));
        PartDefinition cube_r12 = rightExhaust.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 106).mirror().addBox(-2.5F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 1.6F, 1.5708F, 0.0F, 0.7854F));
        PartDefinition rightHand = rightArm.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(45, 40).mirror().addBox(-5.0F, -1.0F, -4.5F, 9.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(110, 89).mirror().addBox(-4.3F, 12.0F, -3.5F, 2.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(110, 89).addBox(1.3F, 12.0F, -3.5F, 2.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(67, 0).addBox(-3.0F, 20.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 20.5F, 0.0F));
        PartDefinition saw = rightHand.addOrReplaceChild("saw", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 22.5F, 0.0F, 0.0F, 0.0F, -1.5708F));
        PartDefinition cube_r13 = saw.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(70, 89).mirror().addBox(-3.9393F, -2.6F, -5.3536F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -0.5F, 0.0F, -0.7854F, 0.0F));
        PartDefinition blade1 = saw.addOrReplaceChild("blade1", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 0.0F, 3.5F, 0.0873F, 0.0F, -0.1309F));
        PartDefinition cube_r14 = blade1.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(54, 32).mirror().addBox(0.3011F, -1.8847F, -2.2353F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(-1.2701F, 0.6468F, 4.7571F, -0.0632F, -0.2196F, -0.048F));
        PartDefinition cube_r15 = blade1.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(81, 58).mirror().addBox(-2.8627F, -2.419F, -4.3756F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-1.4521F, 1.1468F, 0.9538F, 0.0F, -0.7854F, 0.0F));
        PartDefinition blade2 = saw.addOrReplaceChild("blade2", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, -3.5F, -0.0873F, 0.0F, 0.1309F));
        PartDefinition cube_r16 = blade2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(98, 26).mirror().addBox(-4.1723F, -2.2928F, -2.2157F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(1.2701F, 0.734F, -5.7532F, 0.0632F, -0.2196F, 0.048F));
        PartDefinition cube_r17 = blade2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(83, 14).mirror().addBox(-3.0468F, -2.7662F, -5.3382F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(1.4521F, 1.234F, -1.9499F, 0.0F, -0.7854F, 0.0F));
        PartDefinition blade3 = saw.addOrReplaceChild("blade3", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 0.0F, -3.5F, 0.1304F, -0.0114F, 0.0865F));
        PartDefinition cube_r18 = blade3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(103, 0).mirror().addBox(-5.5027F, -2.0185F, -1.4065F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(-4.7831F, 0.5168F, -2.2469F, 0.2802F, 1.346F, 0.2117F));
        PartDefinition cube_r19 = blade3.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(81, 46).mirror().addBox(-3.7341F, -2.6147F, -3.9223F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-0.9651F, 1.0168F, -2.4436F, 0.0F, 0.7854F, 0.0F));
        PartDefinition blade4 = saw.addOrReplaceChild("blade4", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, 4.5F, -0.1304F, -0.0114F, -0.0865F));
        PartDefinition cube_r20 = blade4.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(106, 58).mirror().addBox(-1.0293F, -2.159F, -1.4262F, 6.0F, 3.0F, 5.0F, new CubeDeformation(-0.7F)).mirror(false), PartPose.offsetAndRotation(4.7604F, 0.7769F, 0.264F, -0.2802F, 1.346F, -0.2117F));
        PartDefinition cube_r21 = blade4.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(94, 77).mirror().addBox(-3.5501F, -2.5705F, -2.9598F, 8.0F, 3.0F, 9.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.9424F, 1.2769F, 0.4607F, 0.0F, 0.7854F, 0.0F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create().texOffs(94, 70).addBox(-5.0F, -0.5F, -2.0F, 10.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -35.5F, 2.0F));
        PartDefinition leftLeg = lower.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(6.5F, 1.5477F, 0.6121F));
        PartDefinition cube_r22 = leftLeg.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(44, 18).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(1.0F, 13.2523F, -4.9121F, 0.0F, -0.3491F, 1.5708F));
        PartDefinition cube_r23 = leftLeg.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 71).addBox(-2.5F, -7.5F, -2.5F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 3.4523F, -2.1121F, -0.2618F, 0.0F, 0.0F));
        PartDefinition leftLegUnder = leftLeg.addOrReplaceChild("leftLegUnder", CubeListBuilder.create(), PartPose.offset(1.0F, 14.4523F, -3.1121F));
        PartDefinition cube_r24 = leftLegUnder.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(106, 34).addBox(-2.5F, -5.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition leftWheelJoint = leftLegUnder.addOrReplaceChild("leftWheelJoint", CubeListBuilder.create().texOffs(28, 49).addBox(0.5F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 6.5F, 2.0F));
        PartDefinition leftWheel = leftWheelJoint.addOrReplaceChild("leftWheel", CubeListBuilder.create().texOffs(24, 62).addBox(-4.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.5F, 0.0F));
        PartDefinition rightLeg = lower.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-6.5F, 1.5477F, 0.6121F));
        PartDefinition cube_r25 = rightLeg.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(44, 18).mirror().addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 13.2523F, -4.9121F, 0.0F, 0.3491F, -1.5708F));
        PartDefinition cube_r26 = rightLeg.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 71).addBox(-3.5F, -7.5F, -2.5F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.4523F, -2.1121F, -0.2618F, 0.0F, 0.0F));
        PartDefinition rightLegUnder = rightLeg.addOrReplaceChild("rightLegUnder", CubeListBuilder.create(), PartPose.offset(-1.0F, 14.4523F, -3.1121F));
        PartDefinition cube_r27 = rightLegUnder.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(106, 34).addBox(-2.5F, -5.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition rightWheelJoint = rightLegUnder.addOrReplaceChild("rightWheelJoint", CubeListBuilder.create().texOffs(28, 49).mirror().addBox(-2.5F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 6.5F, 2.0F));
        PartDefinition rightWheel = rightWheelJoint.addOrReplaceChild("rightWheel", CubeListBuilder.create().texOffs(24, 62).mirror().addBox(-0.5F, -4.5F, -4.5F, 5.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 8.5F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityRelicAnnihilator entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        playAnimation(this, entity, EntityRelicAnnihilator.DIE_ANIMATION, AnimationRelicAnnihilator.STUN, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.ACTIVE_ANIMATION, AnimationRelicAnnihilator.ACTIVE, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.DEACTIVATE_ANIMATION, AnimationRelicAnnihilator.DEACTIVATE, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.STUN_ANIMATION, AnimationRelicAnnihilator.STUN, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.CYCLONE_ANIMATION, AnimationRelicAnnihilator.CYCLONE, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.SHOT_ANIMATION1, AnimationRelicAnnihilator.SHOT_START, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.SHOT_ANIMATION2, AnimationRelicAnnihilator.SHOT_LOOP, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.SHOT_ANIMATION3, AnimationRelicAnnihilator.SHOT_END, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.TRICKSHOT_ANIMATION1, AnimationRelicAnnihilator.TRICK_SHOT_START, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.TRICKSHOT_ANIMATION2, AnimationRelicAnnihilator.TRICK_SHOT_LOOP, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.TRICKSHOT_ANIMATION3, AnimationRelicAnnihilator.TRICK_SHOT_END, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.LASER_ANIMATION, AnimationRelicAnnihilator.LASER, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.GROUND_POUND_ANIMATION, AnimationRelicAnnihilator.GROUND_POUND, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.GROUND_POUND_ANIMATION2, AnimationRelicAnnihilator.GROUND_POUND2, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.GROUND_SLAM_ANIMATION1, AnimationRelicAnnihilator.GROUNDS_SLAM_START, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.GROUND_SLAM_ANIMATION2, AnimationRelicAnnihilator.GROUNDS_SLAM_KEEP, ageInTicks);
        playAnimation(this, entity, EntityRelicAnnihilator.GROUND_SLAM_ANIMATION3, AnimationRelicAnnihilator.GROUNDS_SLAM_END, ageInTicks);
        playOverlapAnimation(this, entity, EntityRelicAnnihilator.SLASH_ANIMATION, AnimationRelicAnnihilator.SLASH, ageInTicks);
        playOverlapAnimation(this, entity, EntityRelicAnnihilator.SWING_ANIMATION, AnimationRelicAnnihilator.SWING, ageInTicks);
        playOverlapAnimation(this, entity, EntityRelicAnnihilator.STAB_ANIMATION, AnimationRelicAnnihilator.STAB, ageInTicks);
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (!entity.isActive() || (animation == EntityRelicAnnihilator.ACTIVE_ANIMATION && tick < 40)) {
            if (animation == AnimatedEntity.NO_ANIMATION) applyStatic(this, AnimationRelicAnnihilator.DEACTIVATE_HOLD);
            return;
        }
        if (!entity.isAlive()) return;
        boolean leftExhaust = false, rightExhaust = false;
        if (animation == EntityRelicAnnihilator.CYCLONE_ANIMATION) {
            if (tick >= 20 && tick <= 45) {
                if (tick < 30) leftExhaust = true;
                rightExhaust = true;
            }
        } else if (animation == EntityRelicAnnihilator.STAB_ANIMATION) {
            rightExhaust = tick >= 25 && tick <= 35;
        } else if (animation == EntityRelicAnnihilator.GROUND_SLAM_ANIMATION1) {
            leftExhaust = tick > 12;
            rightExhaust = tick > 12;
        } else if (animation == EntityRelicAnnihilator.GROUND_SLAM_ANIMATION2) {
            rightExhaust = leftExhaust = true;
        }
        this.leftExhaust.visible = leftExhaust;
        this.rightExhaust.visible = rightExhaust;
        if (rightExhaust) this.rightExhaust.zScale += (float) (Math.random() - 0.5) * 0.5F;
        if (leftExhaust) this.leftExhaust.zScale += (float) (Math.random() - 0.5) * 0.5F;
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
        float progress = entity.handControlled.getAnimationFraction(delta);
        lookAtTarget(netHeadYaw * progress, headPitch * progress, 3F, this.leftArm);

        this.leftArm.xRot += (Mth.cos(limbSwing * cycle) * -0.5F) * limbSwingAmount * 0.1F + 0.0524F - idle * 0.1F;
        this.leftArm.zRot += (Mth.cos(limbSwing * cycle) * 0.15F) * limbSwingAmount * 0.1F - 0.0524F;
        this.leftArm.y += Mth.sin(rebound * 3.1415927F) * 0.5F * limbSwingAmount;
        this.rightArm.xRot += (Mth.cos(limbSwing * cycle - 3.1415927F) * -0.5F) * limbSwingAmount * 0.1F + 0.0524F - idle * 0.1F;
        this.rightArm.zRot -= (-Mth.cos(limbSwing * cycle) * -0.15F) * limbSwingAmount * 0.1F - 0.0524F;
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
        if (entity.getAnimation() == EntityRelicAnnihilator.GROUND_SLAM_ANIMATION2) return;
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
