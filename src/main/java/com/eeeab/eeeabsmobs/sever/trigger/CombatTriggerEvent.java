package com.eeeab.eeeabsmobs.sever.trigger;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.event.AnimationEvent;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.google.common.base.Strings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public final class CombatTriggerEvent {

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!ModConfigHandler.COMMON.others.enableCombatPrompts.get()) return;
        if (event.getLevel().isClientSide || event.loadedFromDisk()) return;
        if (event.getEntity() instanceof LivingEntity entity) {
            ResourceLocation entityId = EntityType.getKey(entity.getType());
            if (CombatTriggerHandler.canTriggerEntityType(entityId)) {
                List<ServerPlayer> nearbyPlayers = event.getLevel().getEntitiesOfClass(
                        ServerPlayer.class,
                        entity.getBoundingBox().inflate(32),
                        EntitySelector.NO_CREATIVE_OR_SPECTATOR
                );
                for (ServerPlayer player : nearbyPlayers) {
                    CombatTriggerHandler.checkAndTrigger(player, entityId, new TriggerContext(entity, true));
                }
            }
        }
    }

    @SubscribeEvent
    public void onAnimationStart(AnimationEvent.Start<EEEABMobLibrary> event) {
        if (!ModConfigHandler.COMMON.others.enableCombatPrompts.get()) return;
        EEEABMobLibrary entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (Strings.isNullOrEmpty(event.getAnimation().getHintId())) return;
        ResourceLocation entityId = EntityType.getKey(entity.getType());
        if (CombatTriggerHandler.canTriggerEntityType(entityId)) {
            List<ServerPlayer> nearbyPlayers = entity.level().getEntitiesOfClass(
                    ServerPlayer.class,
                    entity.getBoundingBox().inflate(32),
                    EntitySelector.NO_CREATIVE_OR_SPECTATOR
            );
            Animation animation = entity.getAnimation();
            for (ServerPlayer player : nearbyPlayers) {
                CombatTriggerHandler.checkAndTrigger(player, entityId, new TriggerContext(entity, animation));
            }
        }
    }

    //@SubscribeEvent
    //public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    //    if (!ModConfigHandler.COMMON.others.enableCombatPrompts.get()) return;
    //    if (event.phase != TickEvent.Phase.END) return;
    //    if (event.player.level().isClientSide()) return;
    //    ServerPlayer player = (ServerPlayer) event.player;
    //    if (player.tickCount % 1200 == 0) {
    //        CooldownManager.cleanup();
    //    }
    //}

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!ModConfigHandler.COMMON.others.enableCombatPrompts.get()) return;
        CombatTriggerHandler.resetPlayerTriggers((ServerPlayer) event.getEntity());
    }
}