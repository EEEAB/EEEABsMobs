package com.eeeab.eeeabsmobs.sever.item;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * 可配置物品接口
 */
public interface ConfigurableItem {

    /**
     * 用于初始化、覆盖原有物品属性
     *
     * @return 属性集合
     */
    Multimap<Attribute, AttributeModifier> creatAttributesFromConfig();

    /**
     * 用于提供外部重置
     */
    void refreshAttributesFromConfig();
}
