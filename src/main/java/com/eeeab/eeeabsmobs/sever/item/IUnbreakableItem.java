package com.eeeab.eeeabsmobs.sever.item;

/**
 * 无法摧毁的物品标记
 *
 * @author EEEAB
 */
public interface IUnbreakableItem {
    /**
     * 是否可以被摧毁
     *
     * @return 为false时为物品添加Unbreakable标签
     */
    boolean canBreakItem();
}