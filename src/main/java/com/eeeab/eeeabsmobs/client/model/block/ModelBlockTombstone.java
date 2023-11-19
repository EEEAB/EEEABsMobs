package com.eeeab.eeeabsmobs.client.model.block;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class ModelBlockTombstone extends AdvancedEntityModel<Entity> {
    private final AdvancedModelBox root;

    public ModelBlockTombstone() {
        texHeight = 64;
        texWidth = 64;
        root = new AdvancedModelBox(this, "root");
        root.setPos(0.0F, 16.0F, 0.0F);
        root.setTextureOffset(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);
        updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }
}
