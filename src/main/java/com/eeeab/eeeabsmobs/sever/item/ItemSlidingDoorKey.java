package com.eeeab.eeeabsmobs.sever.item;

import net.minecraft.world.item.Item;

public class ItemSlidingDoorKey extends Item implements SlidingDoorLockKey {
    private final int level;

    public ItemSlidingDoorKey(Properties properties, int level) {
        super(properties);
        this.level = level;
    }

    @Override
    public int getKeyLevel() {
        return this.level;
    }
}
