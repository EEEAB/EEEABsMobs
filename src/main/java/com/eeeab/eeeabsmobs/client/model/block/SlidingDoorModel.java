package com.eeeab.eeeabsmobs.client.model.block;

import com.eeeab.animate.client.animation.AnimationChannel;
import com.eeeab.animate.client.animation.AnimationDefinition;
import com.eeeab.animate.client.animation.Keyframe;
import com.eeeab.animate.client.animation.KeyframeAnimations;
import com.eeeab.animate.client.model.ModHierarchicalModel;
import com.eeeab.eeeabsmobs.sever.entity.block.EntitySlidingDoorBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class SlidingDoorModel extends ModHierarchicalModel<Entity> {
    private final ModelPart root;

    public SlidingDoorModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 5).addBox(-24.0F, -1.0F, -2.0F, 48.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-24.0F, -48.0F, -2.0F, 48.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition left_part1 = root.addOrReplaceChild("left_part1", CubeListBuilder.create().texOffs(32, 10).addBox(-6.0F, -23.0F, -1.0F, 12.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(60, 10).addBox(-12.0F, -7.0F, -2.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -24.0F, 0.0F));
        PartDefinition right_part1 = root.addOrReplaceChild("right_part1", CubeListBuilder.create().texOffs(32, 10).mirror().addBox(-6.0F, -23.0F, -1.0F, 12.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -24.0F, 0.0F));
        PartDefinition left_part2 = root.addOrReplaceChild("left_part2", CubeListBuilder.create().texOffs(0, 10).addBox(-6.0F, -23.0F, -2.0F, 12.0F, 46.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(18.0F, -24.0F, 0.0F));
        PartDefinition right_part2 = root.addOrReplaceChild("right_part2", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-6.0F, -23.0F, -2.0F, 12.0F, 46.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-18.0F, -24.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void animate(EntitySlidingDoorBlock entity, float partialTick) {
        this.resetToDefaultPose();
        float ageInTicks = entity.tickCount + partialTick;
        animate(this, entity.openingAnimationState, OPEN, ageInTicks);
        animate(this, entity.closingAnimationState, CLOSE, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static final AnimationDefinition OPEN = AnimationDefinition.Builder.withLength(0.5f)
            .addAnimation("left_part1",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(12f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, KeyframeAnimations.posVec(14f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right_part1",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(-12f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, KeyframeAnimations.posVec(-14f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left_part2",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.125f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(6f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.posVec(8f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right_part2",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.125f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(-6f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.5f, KeyframeAnimations.posVec(-8f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition CLOSE = AnimationDefinition.Builder.withLength(0.5f)
            .addAnimation("left_part1",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(14f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(4f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4167667f, KeyframeAnimations.posVec(2f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4583433f, KeyframeAnimations.posVec(-0.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right_part1",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(-14f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(-4f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4167667f, KeyframeAnimations.posVec(-2f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4583433f, KeyframeAnimations.posVec(0.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left_part2",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(8f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4167667f, KeyframeAnimations.posVec(-0.13f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4583433f, KeyframeAnimations.posVec(-1f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right_part2",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(-8f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4167667f, KeyframeAnimations.posVec(0.13f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4583433f, KeyframeAnimations.posVec(1f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
}