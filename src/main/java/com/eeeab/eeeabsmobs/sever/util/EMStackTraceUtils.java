package com.eeeab.eeeabsmobs.sever.util;

/**
 * 线程堆栈追踪工具类
 *
 * @author EEEAB
 */
public class EMStackTraceUtils {

    /**
     * 适用于在重写父类方法中
     *
     * @return 是否是原版或本模组正在调用
     */
    public static boolean isNotMinecraftOrMyModInvoking() {
        return isNotMinecraftOrMyModInvoking(4);
    }

    /**
     * 通过获取线程跟踪栈信息判断调用某个方法是否是原版或本模组正在调用
     *
     * @param index 线程栈索引
     * @return 是否是原版或本模组正在调用
     */
    public static boolean isNotMinecraftOrMyModInvoking(int index) {
        try {
            StackTraceElement caller = Thread.currentThread().getStackTrace()[index];
            if (caller != null) {
                return !(caller.getClassName().startsWith("net.minecraft.") || caller.getClassName().startsWith("com.mojang.") || caller.getClassName().startsWith("com.eeeab."));
            }
        } catch (Exception ignored) {}
        return false;
    }
}
