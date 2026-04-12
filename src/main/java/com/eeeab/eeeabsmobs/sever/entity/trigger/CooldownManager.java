package com.eeeab.eeeabsmobs.sever.entity.trigger;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private static final Map<UUID, Map<ResourceLocation, Long>> PLAYER_COOLDOWNS = new HashMap<>();
    private static final long DEFAULT_COOLDOWN = 30;
    
    public static boolean isOnCooldown(UUID playerId, ResourceLocation triggerId) {
        Map<ResourceLocation, Long> cooldowns = PLAYER_COOLDOWNS.get(playerId);
        if (cooldowns == null) return false;
        
        Long lastTrigger = cooldowns.get(triggerId);
        if (lastTrigger == null) return false;
        
        return System.currentTimeMillis() - lastTrigger < DEFAULT_COOLDOWN;
    }
    
    public static void setCooldown(UUID playerId, ResourceLocation triggerId) {
        PLAYER_COOLDOWNS
            .computeIfAbsent(playerId, k -> new HashMap<>())
            .put(triggerId, System.currentTimeMillis());
    }
    
    public static void clearCooldown(UUID playerId, ResourceLocation triggerId) {
        Map<ResourceLocation, Long> cooldowns = PLAYER_COOLDOWNS.get(playerId);
        if (cooldowns != null) {
            cooldowns.remove(triggerId);
        }
    }
    
    //定期清理过期冷却
    public static void cleanup() {
        long now = System.currentTimeMillis();
        PLAYER_COOLDOWNS.values().forEach(cooldowns -> 
            cooldowns.entrySet().removeIf(entry -> now - entry.getValue() > DEFAULT_COOLDOWN)
        );
    }
}