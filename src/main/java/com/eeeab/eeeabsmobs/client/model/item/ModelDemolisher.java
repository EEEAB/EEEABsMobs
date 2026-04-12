package com.eeeab.eeeabsmobs.client.model.item;

import com.eeeab.animate.client.animation.AnimationChannel;
import com.eeeab.animate.client.animation.AnimationDefinition;
import com.eeeab.animate.client.animation.Keyframe;
import com.eeeab.animate.client.animation.KeyframeAnimations;
import com.eeeab.animate.client.model.AnimatedItemModel;
import com.eeeab.eeeabsmobs.sever.item.ItemDemolisher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelDemolisher extends AnimatedItemModel {
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
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-5.65F, -11.2F, -5.85F, 11.0F, 22.0F, 12.0F, new CubeDeformation(-0.08F))
                .texOffs(0, 97).addBox(-3.65F, 3.5F, -5.85F, 11.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 97).addBox(-3.65F, -7.7F, -5.85F, 11.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.65F, 3.25F, -0.15F));
        PartDefinition shoulder = root.addOrReplaceChild("shoulder", CubeListBuilder.create(), PartPose.offset(-1.45F, -15.25F, 0.25F));
        PartDefinition cube_r1 = shoulder.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 74).mirror().addBox(-6.0F, -5.0F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-0.7853F, 9.058F, -0.1F, 3.1416F, 0.0F, 2.9147F));
        PartDefinition cube_r2 = shoulder.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 49).addBox(-6.9F, -6.0F, -6.05F, 14.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.8F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition finger1 = root.addOrReplaceChild("finger1", CubeListBuilder.create(), PartPose.offset(-3.05F, 10.5F, -2.7875F));
        PartDefinition cube_r3 = finger1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 34).addBox(-1.5F, -5.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 4.3F, 0.0F, 0.0F, 2.7925F, 0.0F));
        PartDefinition finger2 = root.addOrReplaceChild("finger2", CubeListBuilder.create(), PartPose.offset(-3.05F, 10.5F, 3.1625F));
        PartDefinition cube_r4 = finger2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 34).addBox(-1.4F, -5.0F, -2.55F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.1F, 4.3F, -0.05F, 0.0F, -2.7925F, 0.0F));
        PartDefinition finger3 = root.addOrReplaceChild("finger3", CubeListBuilder.create().texOffs(16, 34).addBox(-1.4F, -0.7F, -2.55F, 3.0F, 8.0F, 5.0F, new CubeDeformation(-0.08F)), PartPose.offset(2.5F, 10.5F, 0.1375F));
        return LayerDefinition.create(meshdefinition, 64, 128);
    }

    @Override
    public void setupAnim(ItemStack stack, float ageInTicks) {
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}