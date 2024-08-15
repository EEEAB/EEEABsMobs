package com.eeeab.eeeabsmobs.sever.integration.curios;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

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
        Optional<ICuriosItemHandler> optional = CuriosApi.getCuriosHelper().getCuriosHandler(entity).resolve();
        if (optional.isPresent()) {
            ICuriosItemHandler handler = optional.get();
            return findFirstCurio(entity, item, handler.getCurios()).isPresent();
        }
        return false;
    }

    /**
     * 移植Curios API 1.20.1版本API
     * <br>
     * 获取与过滤器匹配的Curios插槽中装备的第一个匹配物品
     *
     * @param entity 实体
     * @param item   饰品
     * @param curios 饰品集
     * @return 返回可能为空的结果
     */
    public Optional<SlotResult> findFirstCurio(LivingEntity entity, Item item, Map<String, ICurioStacksHandler> curios) {
        for (String id : curios.keySet()) {
            ICurioStacksHandler stacksHandler = curios.get(id);
            IDynamicStackHandler stackHandler = stacksHandler.getStacks();

            for (int i = 0; i < stackHandler.getSlots(); i++) {
                ItemStack stack = stackHandler.getStackInSlot(i);

                if (!stack.isEmpty() && stack.getItem() == item) {
                    NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                    return Optional.of(new SlotResult(new SlotContext(id, entity, i, false, renderStates.size() > i && renderStates.get(i)), stack));
                }
            }
        }
        return Optional.empty();
    }
}
