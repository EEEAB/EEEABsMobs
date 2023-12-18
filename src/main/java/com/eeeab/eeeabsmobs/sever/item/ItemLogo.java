package com.eeeab.eeeabsmobs.sever.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

//没有实际作用,用于纪念我在开发模组中添加的第一个内容
public class ItemLogo extends Item {
    public ItemLogo() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }
}
