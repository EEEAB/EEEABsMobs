package com.eeeab.animate.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//参考自: 1.20.1 KeyframeAnimations
@OnlyIn(Dist.CLIENT)
public class EMKeyframeAnimations {
    public static void animate(EMHierarchicalModel<?> model, AnimationDefinition animationDefinition, long accumulatedTime, float scale, Vector3f animationVecCache) {
        float f = getElapsedSeconds(animationDefinition, accumulatedTime);
        for (Map.Entry<String, List<AnimationChannel>> entry : animationDefinition.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = model.getAnyDescendantWithName(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent((modelPart) -> list.forEach((channel) -> {
                Keyframe[] akeyframe = channel.keyframes();
                int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (timestamp) -> f <= akeyframe[timestamp].timestamp()) - 1);
                int j = Math.min(akeyframe.length - 1, i + 1);
                Keyframe keyframe = akeyframe[i];
                Keyframe keyframe1 = akeyframe[j];
                float f1 = f - keyframe.timestamp();
                float f2;
                if (j != i) {
                    f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                } else {
                    f2 = 0.0F;
                }
                keyframe1.interpolation().apply(animationVecCache, f2, akeyframe, i, j, scale);
                channel.target().apply(modelPart, animationVecCache);
            }));
        }
    }

    private static float getElapsedSeconds(AnimationDefinition animationDefinition, long accumulatedTime) {
        float f = (float) accumulatedTime / 1000.0F;
        return animationDefinition.looping() ? f % animationDefinition.lengthInSeconds() : f;
    }

    public static Vector3f posVec(float x, float y, float z) {
        return new Vector3f(x, -y, z);
    }

    public static Vector3f degreeVec(float xDegrees, float yDegrees, float zDegrees) {
        return new Vector3f(xDegrees * ((float) Math.PI / 180F), yDegrees * ((float) Math.PI / 180F), zDegrees * ((float) Math.PI / 180F));
    }

    public static Vector3f scaleVec(double xScale, double yScale, double zScale) {
        return new Vector3f((float) (xScale - 1.0D), (float) (yScale - 1.0D), (float) (zScale - 1.0D));
    }
}