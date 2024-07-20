package com.eeeab.eeeabsmobs.sever.integration.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

/**
 * 自定义Curios API模组方法
 *
 * @author EEEAB
 * @version 1.0
 */
public enum ICuriosApi {
    INSTANCE;

    /**
     * 尚未初始化则始终为false
     */
    public static boolean isLoaded() {
        return CuriosRegistry.getInstance() != null;
    }

    /**
     * 检查实体饰品栏中存在物品
     *
     * @param entity 实体
     * @param item   饰品
     * @return 是否存在
     */
    public boolean isPresentInventory(LivingEntity entity, Item item) {
        Optional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(entity).resolve();
        if (optional.isPresent()) {
            ICuriosItemHandler handler = optional.get();
            return handler.findFirstCurio(item).isPresent();
        }
        return false;
    }
}
