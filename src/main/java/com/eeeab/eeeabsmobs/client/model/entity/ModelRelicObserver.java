package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.client.model.animation.AnimationRelicObserver;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicObserver;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRelicObserver extends ModHierarchicalModel<EntityRelicObserver> {
    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart blocks;
    private final ModelPart bone1;
    private final ModelPart bone2;
    private final ModelPart bone3;
    private final ModelPart bone4;
    private final ModelPart bone5;
    private final ModelPart bone6;
    private final ModelPart bone7;
    private final ModelPart bone8;

    public ModelRelicObserver(ModelPart root) {
        this.root = root.getChild("root");
        this.core = this.root.getChild("core");
        this.blocks = this.root.getChild("blocks");
        ModelPart left = this.blocks.getChild("left");
        this.bone1 = left.getChild("bone1");
        this.bone2 = left.getChild("bone2");
        this.bone3 = left.getChild("bone3");
        this.bone4 = left.getChild("bone4");
        ModelPart right = this.blocks.getChild("right");
        this.bone5 = right.getChild("bone5");
        this.bone6 = right.getChild("bone6");
        this.bone7 = right.getChild("bone7");
        this.bone8 = right.getChild("bone8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));
        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition blocks = root.addOrReplaceChild("blocks", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));
        PartDefinition left = blocks.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(4.0F, -8.0F, 0.0F));
        PartDefinition bone1 = left.addOrReplaceChild("bone1", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -4.0F));
        PartDefinition bone2 = left.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 4.0F));
        PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -3.1416F, 0.0F));
        PartDefinition bone3 = left.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, -4.0F));
        PartDefinition cube_r2 = bone3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));
        PartDefinition bone4 = left.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 4.0F));
        PartDefinition cube_r3 = bone4.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 3.1416F));
        PartDefinition right = blocks.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bone5 = right.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -12.0F, -4.0F));
        PartDefinition bone6 = right.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(-4.0F, -12.0F, 4.0F));
        PartDefinition cube_r4 = bone6.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition bone7 = right.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offset(-4.0F, -4.0F, -4.0F));
        PartDefinition cube_r5 = bone7.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, -1.5708F, 0.0F));
        PartDefinition bone8 = right.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offset(-4.0F, -4.0F, 4.0F));
        PartDefinition cube_r6 = bone8.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EntityRelicObserver entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        lookAtTarget(netHeadYaw, headPitch, 1F, this.core);
        playAnimation(this, entity, EntityRelicObserver.DIE_ANIMATION, AnimationRelicObserver.DEACTIVATE, ageInTicks);
        playAnimation(this, entity, EntityRelicObserver.ACTIVE_ANIMATION, AnimationRelicObserver.ACTIVE, ageInTicks);
        playAnimation(this, entity, EntityRelicObserver.DEACTIVATE_ANIMATION, AnimationRelicObserver.DEACTIVATE, ageInTicks);
        playAnimation(this, entity, EntityRelicObserver.ATTACK_ANIMATION, AnimationRelicObserver.ATTACK, ageInTicks);
        playAnimation(this, entity, EntityRelicObserver.LASER_ANIMATION, AnimationRelicObserver.LASER, ageInTicks);
        playAnimation(this, entity, EntityRelicObserver.STORM_ANIMATION, AnimationRelicObserver.STORM, ageInTicks);
        Animation animation = entity.getAnimation();
        float progress = Mth.clamp(entity.hurtTime / 10F, 0F, 1F);
        if (progress > 0) {
            float x = (float) (Math.random() - 0.5) * 3F * progress;
            this.blocks.x += x;
            float y = (float) (Math.random() - 0.5) * 3F * progress;
            this.blocks.y += y;
            float z = (float) (Math.random() - 0.5) * 3F * progress;
            this.blocks.z += z;
        }
        float delta = ageInTicks - entity.tickCount;
        float frame = entity.frame + delta;
        if (entity.isActive() && (AnimatedEntity.NO_ANIMATION == animation || EntityRelicObserver.HURT_ANIMATION == animation)) {
            setStaticRotationPoint(root, 0F, -12F, 0F);
            setStaticRotationPoint(bone1, 2F, -2F, -2F);
            setStaticRotationPoint(bone2, 2F, -2F, 2F);
            setStaticRotationPoint(bone3, 2F, 2F, -2F);
            setStaticRotationPoint(bone4, 2F, 2F, 2F);
            setStaticRotationPoint(bone5, -2F, -2F, -2F);
            setStaticRotationPoint(bone6, -2F, -2F, 2F);
            setStaticRotationPoint(bone7, -2F, 2F, -2F);
            setStaticRotationPoint(bone8, -2F, 2F, 2F);
        }
        if (entity.isActive() && EntityRelicObserver.ACTIVE_ANIMATION != animation && EntityRelicObserver.DEACTIVATE_ANIMATION != animation && entity.getDeathAnimation() != animation) {
            float baseSpeed = 0.45F;
            float baseDegree = 0.6F;
            float[] phaseOffsets = {0F, 0.2F, 0.4F, 0.6F, 0.8F, 1.0F, 1.2F, 1.4F};
            this.waveBob(root, baseSpeed, baseDegree, 0, frame);
            this.waveBob(bone1, baseSpeed, baseDegree * 0.9F, phaseOffsets[0], frame);
            this.waveBob(bone2, baseSpeed, baseDegree * 0.6F, phaseOffsets[6], frame);
            this.waveBob(bone3, baseSpeed, baseDegree * 0.7F, phaseOffsets[5], frame);
            this.waveBob(bone4, baseSpeed, baseDegree * 0.8F, phaseOffsets[3], frame);
            this.waveBob(bone5, baseSpeed, baseDegree * 0.8F, phaseOffsets[2], frame);
            this.waveBob(bone6, baseSpeed, baseDegree * 0.7F, phaseOffsets[4], frame);
            this.waveBob(bone7, baseSpeed, baseDegree * 0.6F, phaseOffsets[7], frame);
            this.waveBob(bone8, baseSpeed, baseDegree * 0.9F, phaseOffsets[1], frame);
            progress = entity.rotControlled.getAnimationFraction(delta);
            float timeBasedRotation = (ageInTicks * 15F) % 360F;
            this.blocks.yRot = (-timeBasedRotation * progress) * ((float) Math.PI / 180F);
        }
    }

    private void waveBob(ModelPart box, float speed, float degree, float phaseOffset, float frame) {
        float movementScale = this.getMovementScale();
        degree *= movementScale;
        speed *= movementScale;
        float bob = (float) (Math.sin(frame * speed + phaseOffset) * (float) 1 * degree - degree);
        box.y += bob;
    }
}
