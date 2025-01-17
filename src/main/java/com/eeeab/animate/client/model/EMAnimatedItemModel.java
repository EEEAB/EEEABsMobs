package com.eeeab.animate.client.model;

import com.eeeab.animate.client.util.ItemAnimationUtils;
import com.mojang.math.Vector3f;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EMAnimatedItemModel extends EMHierarchicalModel<Entity> {
    private static final Vector3f ITEM_ANIMATION_VECTOR_CACHE = new Vector3f();

    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public abstract void setupAnim(ItemStack stack, float ageInTicks);

    protected void animateItem(ItemStack itemStackIn, AnimationDefinition animationDefinition, float ageInTicks, float scale) {
        animateItem(itemStackIn, animationDefinition, ageInTicks, 1F, scale);
    }

    protected void animateItem(ItemStack itemStackIn, AnimationDefinition animationDefinition, float ageInTicks, float speed, float scale) {
        if (ItemAnimationUtils.updateTime(itemStackIn, ageInTicks, speed)) {
            long accumulatedTime = ItemAnimationUtils.getAccumulatedTime(itemStackIn);
            if (accumulatedTime / 1000F * 20F > animationDefinition.lengthInSeconds() * 20F) {
                ItemAnimationUtils.stop(itemStackIn);
                return;
            }
            EMKeyframeAnimations.animate(this, animationDefinition, accumulatedTime, scale, ITEM_ANIMATION_VECTOR_CACHE);
        }
    }
}
