package com.eeeab.eeeabsmobs.sever.trigger;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.trigger.components.BaseAnimationTrigger;
import com.eeeab.eeeabsmobs.sever.trigger.components.FirstEncounterTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;

public class TriggerRegister {

    public static void vanillaRegister() {
        CombatTriggerHandler.registerTrigger(
                new FirstEncounterTrigger(
                        new ResourceLocation("wither_summon"),
                        new ResourceLocation("wither"), 2
                ) {
                    @Override
                    protected boolean checkSpecificConditions(ServerPlayer player, TriggerContext context) {
                        if (super.checkSpecificConditions(player, context)) {
                            Entity entity = context.getEntity();
                            if (entity instanceof WitherBoss wither && wither.getType() == EntityType.WITHER) {
                                return wither.getInvulnerableTicks() > 0;
                            }
                        }
                        return false;
                    }
                }
        );
    }

    public static void modRegister() {
        CombatTriggerHandler.registerTrigger(
                new BaseAnimationTrigger(EntityInit.RELIC_ANNIHILATOR.getId(), 0, true)
        );
    }
}