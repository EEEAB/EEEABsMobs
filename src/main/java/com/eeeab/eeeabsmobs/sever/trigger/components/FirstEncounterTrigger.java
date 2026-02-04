package com.eeeab.eeeabsmobs.sever.trigger.components;

import com.eeeab.eeeabsmobs.sever.trigger.TriggerContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FirstEncounterTrigger extends BaseCombatTrigger {
    public FirstEncounterTrigger(ResourceLocation triggerId, ResourceLocation entityId, int promptLevel) {
        super(triggerId, entityId, promptLevel, true);
    }

    @Override
    public boolean shouldTrigger(ServerPlayer player, ResourceLocation entityId, TriggerContext context) {
        return context.isFirstEncounter();
    }
}