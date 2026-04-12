package com.eeeab.eeeabsmobs.sever.entity.trigger.components;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.trigger.CombatTrigger;
import com.eeeab.eeeabsmobs.sever.message.PopupNotificationMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

public abstract class BaseCombatTrigger implements CombatTrigger {
    protected final ResourceLocation entityId;
    protected final boolean oncePerEncounter;
    protected ResourceLocation triggerId;
    protected int level;

    public BaseCombatTrigger(ResourceLocation triggerId, ResourceLocation entityId, int level, boolean oncePerEncounter) {
        this.triggerId = triggerId;
        this.entityId = entityId;
        this.level = level;
        this.oncePerEncounter = oncePerEncounter;
    }

    @Override
    public void trigger(ServerPlayer player) {
        EEEABMobs.NETWORK.sendTo(new PopupNotificationMessage(getTriggerKey(), level), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        //if (!oncePerEncounter) CooldownManager.setCooldown(player.getUUID(), getTriggerKey());
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

    public void setTriggerKey(ResourceLocation triggerId) {
        this.triggerId = triggerId;
    }

    public void setMsgLevel(int promptLevel) {
        this.level = promptLevel;
    }
}