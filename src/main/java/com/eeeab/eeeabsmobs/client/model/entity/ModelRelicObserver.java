package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRelicObserver;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicObserver;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelRelicObserver extends ModHierarchicalModel<EntityRelicObserver> {
    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart upper;
    private final ModelPart block4;
    private final ModelPart block3;
    private final ModelPart block2;
    private final ModelPart block1;
    private final ModelPart lower;
    private final ModelPart block8;
    private final ModelPart block7;
    private final ModelPart block6;
    private final ModelPart block5;

    public ModelRelicObserver(ModelPart root) {
        this.root = root.getChild("root");
        this.core = this.root.getChild("core");
        this.upper = this.root.getChild("upper");
        this.block4 = this.upper.getChild("block4");
        this.block3 = this.upper.getChild("block3");
        this.block2 = this.upper.getChild("block2");
        this.block1 = this.upper.getChild("block1");
        this.lower = this.root.getChild("lower");
        this.block8 = this.lower.getChild("block8");
        this.block7 = this.lower.getChild("block7");
        this.block6 = this.lower.getChild("block6");
        this.block5 = this.lower.getChild("block5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));
        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));
        PartDefinition block4 = upper.addOrReplaceChild("block4", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 4.0F));
        PartDefinition cube_r1 = block4.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, -3.1416F, 0.0F));
        PartDefinition block3 = upper.addOrReplaceChild("block3", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 4.0F));
        PartDefinition cube_r2 = block3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition block2 = upper.addOrReplaceChild("block2", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-12.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 0.0F, -4.0F));
        PartDefinition block1 = upper.addOrReplaceChild("block1", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -4.0F));
        PartDefinition lower = root.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition block8 = lower.addOrReplaceChild("block8", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, -4.0F));
        PartDefinition cube_r3 = block8.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, -1.5708F, 0.0F));
        PartDefinition block7 = lower.addOrReplaceChild("block7", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, -4.0F));
        PartDefinition cube_r4 = block7.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));
        PartDefinition block6 = lower.addOrReplaceChild("block6", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 4.0F));
        PartDefinition cube_r5 = block6.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));
        PartDefinition block5 = lower.addOrReplaceChild("block5", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 4.0F));
        PartDefinition cube_r6 = block5.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 3.1416F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityRelicObserver entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        //LookAt
        lookAtAnimation(netHeadYaw, headPitch, 1F, this.core);
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        //Idle
        Animation animation = entity.getAnimation();
        if (entity.isActive() && (entity.isNoAnimation() || animation == entity.hurtAnimation)) {
            setStaticRotationPoint(root, 0F, -12F, 0F);
            setStaticRotationPoint(block1, 2F, -2F, -2F);
            setStaticRotationPoint(block6, -2F, 2F, 2F);
            setStaticRotationPoint(block2, -2F, -2F, -2F);
            setStaticRotationPoint(block5, 2F, 2F, 2F);
            setStaticRotationPoint(block3, -2F, -2F, 2F);
            setStaticRotationPoint(block7, 2F, 2F, -2F);
            setStaticRotationPoint(block4, 2F, -2F, 2F);
            setStaticRotationPoint(block8, -2F, 2F, -2F);
        }
        if (entity.isActive() && animation != entity.activeAnimation && animation != entity.deactivateAnimation && animation != entity.getDeathAnimation()) {
            float baseSpeed = 0.45F;
            float baseDegree = 0.6F;
            float[] phaseOffsets = {0F, 0.2F, 0.4F, 0.6F, 0.8F, 1.0F, 1.2F, 1.4F};
            this.waveBob(root, baseSpeed, baseDegree, 0, frame);
            this.waveBob(block1, baseSpeed, baseDegree * 0.9F, phaseOffsets[0], frame);
            this.waveBob(block6, baseSpeed, baseDegree * 0.9F, phaseOffsets[1], frame);
            this.waveBob(block2, baseSpeed, baseDegree * 0.8F, phaseOffsets[2], frame);
            this.waveBob(block5, baseSpeed, baseDegree * 0.8F, phaseOffsets[3], frame);
            this.waveBob(block3, baseSpeed, baseDegree * 0.7F, phaseOffsets[4], frame);
            this.waveBob(block7, baseSpeed, baseDegree * 0.7F, phaseOffsets[5], frame);
            this.waveBob(block4, baseSpeed, baseDegree * 0.6F, phaseOffsets[6], frame);
            this.waveBob(block8, baseSpeed, baseDegree * 0.6F, phaseOffsets[7], frame);
            float progress = entity.rotControlled.getAnimationFraction(delta);
            float timeBasedRotation = (ageInTicks * 15F) % 360F;
            this.upper.yRot = (-timeBasedRotation * progress) * ((float) Math.PI / 180F);
            this.lower.yRot = (-timeBasedRotation * progress) * ((float) Math.PI / 180F);
        }
        if (animation == entity.hurtAnimation) {
            float x = (float) (Math.random() - 0.5) * 1.5F;
            this.upper.x += x;
            this.lower.x += x;
            float y = (float) (Math.random() - 0.5) * 1.5F;
            this.upper.y += y;
            this.lower.y += y;
            float z = (float) (Math.random() - 0.5) * 1.5F;
            this.upper.z += z;
            this.lower.z += z;
        }
        this.animate(entity.dieAnimation, AnimationRelicObserver.DEACTIVATE, ageInTicks);
        this.animate(entity.activeAnimation, AnimationRelicObserver.ACTIVE, ageInTicks);
        this.animate(entity.deactivateAnimation, AnimationRelicObserver.DEACTIVATE, ageInTicks);
        this.animate(entity.shootLaserAnimation, AnimationRelicObserver.SHOOT_LASER, ageInTicks);
        this.animate(entity.stormAnimation, AnimationRelicObserver.STORM, ageInTicks);
    }

    private void waveBob(ModelPart box, float speed, float degree, float phaseOffset, float frame) {
        float movementScale = this.getMovementScale();
        degree *= movementScale;
        speed *= movementScale;
        float bob = (float) (Math.sin(frame * speed + phaseOffset) * (float) 1 * degree - degree);
        box.y += bob;
    }
}
