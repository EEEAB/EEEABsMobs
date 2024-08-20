package com.eeeab.eeeabsmobs.sever.item.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class EMItemStackUtils {
    private EMItemStackUtils() {
    }

    /**
     * 初始化NBT标签
     *
     * @param itemStack 物品栈
     */
    private static void initNBT(ItemStack itemStack) {
        if (itemStack.getTag() == null) {
            itemStack.setTag(new CompoundTag());
        }
    }

    /**
     * 判断是否存在指定的NBT标签数据
     *
     * @param itemStack 物品栈
     * @param keyName   键名
     * @return 是否存在
     */
    public static boolean hasNBT(ItemStack itemStack, String keyName) {
        return !itemStack.isEmpty() && itemStack.getTag() != null && itemStack.getTag().contains(keyName);
    }

    /**
     * 获取NBT标签数据
     *
     * @param itemStack 物品栈
     * @param keyName   键名
     * @return NBT标签数据
     */
    public static CompoundTag getNBT(ItemStack itemStack, String keyName) {
        initNBT(itemStack);
        if (!itemStack.getTag().contains(keyName)) {
            putNBT(itemStack, keyName, new CompoundTag());
        }
        return itemStack.getTag().getCompound(keyName);
    }

    /**
     * 设置物品栈的NBT标签数据
     *
     * @param itemStack 物品栈
     * @param keyName   键名
     * @param compound  NBT标签
     */
    public static void putNBT(ItemStack itemStack, String keyName, CompoundTag compound) {
        initNBT(itemStack);
        itemStack.getTag().put(keyName, compound);
    }

    /**
     * 移除物品栈中指定NBT标签数据
     *
     * @param stack   物品栈
     * @param keyName 键名
     */
    public static void removeNbt(ItemStack stack, String keyName) {
        if (stack.getTag() != null) {
            stack.getTag().remove(keyName);
        }
    }
}
