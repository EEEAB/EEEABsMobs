package com.eeeab.eeeabsmobs.sever.entity.trigger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class CombatTriggerHandler {
    private static final Map<ResourceLocation, Set<CombatTrigger>> TRIGGERS = new HashMap<>();
    private static final Map<UUID, Set<ResourceLocation>> PLAYER_TRIGGERED = new HashMap<>();

    public static void init() {
        TriggerRegister.register();
    }

    //注册触发规则
    public static void registerTrigger(CombatTrigger trigger) {
        TRIGGERS.computeIfAbsent(trigger.getEntityKey(), k -> new HashSet<>()).add(trigger);
    }

    public static boolean canTriggerEntityType(ResourceLocation location) {
        return TRIGGERS.containsKey(location);
    }

    public static void checkAndTrigger(ServerPlayer player, ResourceLocation entityId, TriggerContext context) {
        Set<CombatTrigger> triggers = TRIGGERS.get(entityId);
        if (triggers == null) return;
        for (CombatTrigger trigger : triggers) {
            if (trigger.shouldTrigger(player, entityId, context)) {
                if (shouldSkipTrigger(player, trigger)) {
                    continue;
                }
                trigger.trigger(player);
                recordTrigger(player, trigger);
                break;
            }
        }
    }

    //检查是否应该跳过
    private static boolean shouldSkipTrigger(ServerPlayer player, CombatTrigger trigger) {
        if (!trigger.isOncePerEncounter()) {
            return false;
        }
        Set<ResourceLocation> triggered = PLAYER_TRIGGERED.getOrDefault(player.getUUID(), new HashSet<>());
        return triggered.contains(trigger.getTriggerKey());
    }

    //记录触发
    private static void recordTrigger(ServerPlayer player, CombatTrigger trigger) {
        if (trigger.isOncePerEncounter()) {
            PLAYER_TRIGGERED.computeIfAbsent(player.getUUID(), k -> new HashSet<>()).add(trigger.getTriggerKey());
        }
    }

    //重置玩家的触发记录
    public static void resetPlayerTriggers(ServerPlayer player) {
        PLAYER_TRIGGERED.remove(player.getUUID());
    }
}