package com.eeeab.eeeabsmobs.client.model.item;

import com.eeeab.animate.client.model.EMAnimatedItemModel;
import com.eeeab.eeeabsmobs.sever.item.ItemDemolisher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelDemolisher extends EMAnimatedItemModel {
    private final ModelPart root;
    private final ModelPart finger1;
    private final ModelPart finger2;
    private final ModelPart finger3;

    public ModelDemolisher(ModelPart root) {
        this.root = root.getChild("root");
        this.finger1 = this.root.getChild("finger1");
        this.finger2 = this.root.getChild("finger2");
        this.finger3 = this.root.getChild("finger3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-5.65F, -11.0F, -5.85F, 11.0F, 22.0F, 12.0F, new CubeDeformation(-0.08F)), PartPose.offset(0.65F, 3.25F, -0.15F));
        PartDefinition shoulder = root.addOrReplaceChild("shoulder", CubeListBuilder.create(), PartPose.offset(-1.45F, -15.25F, 0.25F));
        PartDefinition cube_r1 = shoulder.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 49).addBox(-6.9F, -6.0F, -6.05F, 14.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition finger1 = root.addOrReplaceChild("finger1", CubeListBuilder.create().texOffs(0, 34).addBox(-1.5F, -1.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offset(-2.85F, 10.5F, -2.8375F));
        PartDefinition finger2 = root.addOrReplaceChild("finger2", CubeListBuilder.create().texOffs(0, 34).addBox(-1.5F, -1.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offset(-2.85F, 10.5F, 3.0625F));
        PartDefinition finger3 = root.addOrReplaceChild("finger3", CubeListBuilder.create().texOffs(16, 34).addBox(-1.4F, 0.0F, -2.55F, 3.0F, 8.0F, 5.0F, new CubeDeformation(-0.08F)), PartPose.offset(2.5F, 10.5F, 0.1375F));
        return LayerDefinition.create(meshdefinition, 64, 128);
    }

    @Override
    public void setupAnim(ItemStack stack, float ageInTicks) {
        this.resetToDefaultPose();
        this.animateItem(stack, AnimationDemolisher.SHOOT, ageInTicks, 1.5F);
        if (ItemDemolisher.getWeaponState(stack) == 1) {
            setStaticRotationAngle(this.finger1, 0F, -90F, 0F);
            setStaticRotationPoint(this.finger1, 1F, 0F, 1.5F);
            setStaticRotationAngle(this.finger2, 0F, 90F, 0F);
            setStaticRotationPoint(this.finger2, 1F, 0F, -1.5F);
            setStaticRotationPoint(this.finger3, -1F, 0F, 0F);
        }
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
    private static class AnimationDemolisher {

        public static final AnimationDefinition SHOOT = AnimationDefinition.Builder.withLength(0.25f)
                .addAnimation("finger1",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.posVec(0f, 3f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("finger1",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.041676664f, KeyframeAnimations.scaleVec(1.1f, 1.1f, 1.1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("finger2",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.posVec(0f, 3f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("finger2",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.041676664f, KeyframeAnimations.scaleVec(1.1f, 1.1f, 1.1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("finger3",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125f, KeyframeAnimations.posVec(0f, 3f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("finger3",
                        new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.041676664f, KeyframeAnimations.scaleVec(1.1f, 1.1f, 1.1f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                        AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("root",
                        new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.041676664f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.16766666f, KeyframeAnimations.posVec(0f, 1f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                        AnimationChannel.Interpolations.CATMULLROM))).build();
    }
}