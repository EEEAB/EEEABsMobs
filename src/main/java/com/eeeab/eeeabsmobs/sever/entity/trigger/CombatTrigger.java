package com.eeeab.eeeabsmobs.sever.entity.trigger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface CombatTrigger {
    /**
     * 是否应该触发提示
     */
    boolean shouldTrigger(ServerPlayer player, ResourceLocation entityId, TriggerContext context);
    
    /**
     * 触发提示
     */
    void trigger(ServerPlayer player);

    /**
     * @return 返回触发Key
     */
    ResourceLocation getTriggerKey();

    /**
     * @return 返回实体Key
     */
    ResourceLocation getEntityKey();
    
    /**
     * 是否为单次游戏中只触发一次
     */
    default boolean isOncePerEncounter() {
        return true;
    }
}
