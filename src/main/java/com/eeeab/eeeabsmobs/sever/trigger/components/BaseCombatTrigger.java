package com.eeeab.eeeabsmobs.sever.trigger.components;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.message.PopupNotificationMessage;
import com.eeeab.eeeabsmobs.sever.trigger.CombatTrigger;
import com.eeeab.eeeabsmobs.sever.trigger.CooldownManager;
import com.eeeab.eeeabsmobs.sever.trigger.TriggerContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

public abstract class BaseCombatTrigger implements CombatTrigger {
    protected final ResourceLocation triggerId;
    protected final ResourceLocation entityId;
    protected final int promptLevel;
    protected final boolean oncePerEncounter;

    public BaseCombatTrigger(ResourceLocation triggerId, ResourceLocation entityId, int promptLevel, boolean oncePerEncounter) {
        this.triggerId = triggerId;
        this.entityId = entityId;
        this.promptLevel = promptLevel;
        this.oncePerEncounter = oncePerEncounter;
    }

    @Override
    public boolean shouldTrigger(ServerPlayer player, ResourceLocation entityId, TriggerContext context) {
        double range = context.getFollowRange();
        if (player.distanceToSqr(context.getEntity()) > (range * range)) {
            return false;
        }
        return checkSpecificConditions(player, context);
    }

    @Override
    public void trigger(ServerPlayer player) {
        EEEABMobs.NETWORK.sendTo(
                new PopupNotificationMessage(getTriggerKey(), promptLevel, false),
                player.connection.connection,
                NetworkDirection.PLAY_TO_CLIENT
        );
        if (!oncePerEncounter) CooldownManager.setCooldown(player.getUUID(), getTriggerKey());
    }

    @Override
    public ResourceLocation getTriggerKey() {
        return triggerId;
    }

    @Override
    public ResourceLocation getEntityKey() {
        return entityId;
    }

    @Override
    public boolean isOncePerEncounter() {
        return oncePerEncounter;
    }

    protected abstract boolean checkSpecificConditions(ServerPlayer player, TriggerContext context);
}