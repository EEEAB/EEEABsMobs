package com.eeeab.animate.client.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

/**
 * 物品动画工具类
 *
 * @author EEEAB
 */
public class ItemAnimationUtils {
    private static final String ACCUMULATED_TIME_KEY = "accumulatedTime";
    private static final String ANIMATION_INDEX = "animationIndex";
    private static final String LAST_TIME_KEY = "lastTime";


    public static CompoundTag start(ItemStack stack, int tickCount) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong(LAST_TIME_KEY, (long) tickCount * 1000L / 20L);
        tag.putLong(ACCUMULATED_TIME_KEY, 0L);
        return tag;
    }

    public static void start(ItemStack stack, int tickCount, int index) {
        start(stack, tickCount).putInt(ANIMATION_INDEX, index);
    }

    public static boolean updateTime(ItemStack stack, float ageInTicks, float speed) {
        boolean flag = isStarted(stack);
        if (flag) {
            CompoundTag tag = stack.getOrCreateTag();
            long i = Mth.lfloor(ageInTicks * 1000.0F / 20.0F);
            long lastTime = tag.getLong(LAST_TIME_KEY);
            tag.putLong(ACCUMULATED_TIME_KEY, tag.getLong(ACCUMULATED_TIME_KEY) + (long) ((float) (i - lastTime) * speed));
            tag.putLong(LAST_TIME_KEY, i);
        }
        return flag;
    }

    public static void stop(ItemStack stack) {
        stack.getOrCreateTag().putLong(LAST_TIME_KEY, Long.MAX_VALUE);
    }

    public static long getAccumulatedTime(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getLong(ACCUMULATED_TIME_KEY) : 0L;
    }

    public static int getAnimationIndex(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt(ANIMATION_INDEX) : 0;
    }

    public static boolean isStarted(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getLong(LAST_TIME_KEY) != Long.MAX_VALUE;
    }
}
