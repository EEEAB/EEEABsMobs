package com.eeeab.eeeabsmobs.client.model.item;

import com.eeeab.animate.client.animation.AnimationChannel;
import com.eeeab.animate.client.animation.AnimationDefinition;
import com.eeeab.animate.client.animation.Keyframe;
import com.eeeab.animate.client.animation.KeyframeAnimations;
import com.eeeab.animate.client.model.AnimatedItemModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelBusterGauntlet extends AnimatedItemModel {
    private final ModelPart root;

    public ModelBusterGauntlet(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(22, 10).addBox(-2.98F, -0.85F, -3.5875F, 6.0F, 3.0F, 7.0F, new CubeDeformation(0.025F)).texOffs(28, 0).addBox(-2.5F, -1.0137F, -3.1125F, 5.0F, 4.0F, 6.0F, new CubeDeformation(-0.05F)).texOffs(0, 0).addBox(-3.0F, 1.0863F, -3.1125F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));
        PartDefinition joint1 = root.addOrReplaceChild("joint1", CubeListBuilder.create(), PartPose.offset(2.0F, 4.0F, -2.5F));
        PartDefinition cube_r1 = joint1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition joint2 = root.addOrReplaceChild("joint2", CubeListBuilder.create(), PartPose.offset(-2.0F, 4.0F, -2.5F));
        PartDefinition cube_r2 = joint2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition joint3 = root.addOrReplaceChild("joint3", CubeListBuilder.create().texOffs(10, 20).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.5F, 3.0F));
        PartDefinition muzzle = root.addOrReplaceChild("muzzle", CubeListBuilder.create().texOffs(16, 9).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 4.5F, 0.0F));
        PartDefinition clip = root.addOrReplaceChild("clip", CubeListBuilder.create().texOffs(28, 20).addBox(-1.5F, -2.0326F, -3.9286F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 20).addBox(-1.5F, -2.0326F, 3.0714F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 9).addBox(-2.5F, -2.0326F, -2.9286F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.8674F, -0.0714F));
        PartDefinition cube_r3 = clip.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-3.8837F, -6.3929F, -0.5709F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.4174F, 0.0214F, 0.0F, 0.3927F, 0.0F));
        PartDefinition cube_r4 = clip.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-3.9794F, -6.3929F, -1.6601F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.4174F, 0.6214F, 0.0F, -0.3927F, 0.0F));
        PartDefinition cube_r5 = clip.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(26, 0).addBox(1.8837F, -6.3929F, -0.5709F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 5.4174F, 0.0214F, 0.0F, -0.3927F, 0.0F));
        PartDefinition cube_r6 = clip.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(18, 0).addBox(1.9794F, -6.3929F, -1.6601F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 5.4174F, 0.3214F, 0.0F, 0.3927F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(ItemStack stack, float ageInTicks) {
        this.resetToDefaultPose();
        this.animateItem(stack, AnimationBusterGauntlet.SHOOT, ageInTicks, 1.5F);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @OnlyIn(Dist.CLIENT)
    private static class AnimationBusterGauntlet {

        public static final AnimationDefinition SHOOT = AnimationDefinition.Builder.withLength(0.5f)
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.scaleVec(1.1f, 1.1f, 1.1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("joint1",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-26.57f, -14.48f, -26.57f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-26.57f, -14.48f, -26.57f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("joint2",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-26.57f, 14.48f, 26.57f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-26.57f, 14.48f, 26.57f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("joint3",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("muzzle",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.08343333f, KeyframeAnimations.scaleVec(1.2f, 1f, 1.2f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2916767f, KeyframeAnimations.scaleVec(1.2f, 1f, 1.2f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("clip",
                        new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
    }
}