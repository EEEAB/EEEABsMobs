package com.eeeab.animate.client.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 物品动画工具类
 *
 * @author EEEAB
 */
@OnlyIn(Dist.CLIENT)
public class ItemAnimationUtils {
    private static final Map<UUID, AnimationState> ANIMATIONS = new HashMap<>();
    private static final String ANIMATION_UUID_KEY = "ClientAnimationUUID";

    private static class AnimationState {
        long lastTimeMs;
        long accumulatedTimeMs;
        int animationIndex;
        boolean started;
    }

    private static UUID getOrCreateAnimationUUID(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.hasUUID(ANIMATION_UUID_KEY)) {
            return tag.getUUID(ANIMATION_UUID_KEY);
        } else {
            UUID uuid = UUID.randomUUID();
            tag.putUUID(ANIMATION_UUID_KEY, uuid);
            return uuid;
        }
    }

    public static void start(ItemStack stack, int tickCount) {
        UUID uuid = getOrCreateAnimationUUID(stack);
        AnimationState state = new AnimationState();
        state.lastTimeMs = (long) tickCount * 1000L / 20L;
        state.accumulatedTimeMs = 0L;
        state.animationIndex = 0;
        state.started = true;
        ANIMATIONS.put(uuid, state);
    }

    public static void start(ItemStack stack, int tickCount, int index) {
        start(stack, tickCount);
        UUID uuid = getOrCreateAnimationUUID(stack);
        AnimationState state = ANIMATIONS.get(uuid);
        if (state != null) state.animationIndex = index;
    }

    public static boolean updateTime(ItemStack stack, float ageInTicks, float speed) {
        UUID uuid = getOrCreateAnimationUUID(stack);
        AnimationState state = ANIMATIONS.get(uuid);
        if (state == null || !state.started) return false;
        long currentMs = Mth.lfloor(ageInTicks * 1000.0F / 20.0F);
        long delta = currentMs - state.lastTimeMs;
        if (delta > 0) {
            state.accumulatedTimeMs += (long) (delta * speed);
            state.lastTimeMs = currentMs;
        }
        return true;
    }

    public static void stop(ItemStack stack) {
        UUID uuid = getOrCreateAnimationUUID(stack);
        ANIMATIONS.remove(uuid);
    }

    public static long getAccumulatedTime(ItemStack stack) {
        UUID uuid = getOrCreateAnimationUUID(stack);
        AnimationState state = ANIMATIONS.get(uuid);
        return state != null ? state.accumulatedTimeMs : 0L;
    }

    public static int getAnimationIndex(ItemStack stack) {
        UUID uuid = getOrCreateAnimationUUID(stack);
        AnimationState state = ANIMATIONS.get(uuid);
        return state != null ? state.animationIndex : 0;
    }

    public static boolean isStarted(ItemStack stack) {
        UUID uuid = getOrCreateAnimationUUID(stack);
        AnimationState state = ANIMATIONS.get(uuid);
        return state != null && state.started;
    }
}
