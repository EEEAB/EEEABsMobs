package com.eeeab.eeeabsmobs.sever.entity.trigger;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.entity.trigger.components.BaseAnimationTrigger;

public class TriggerRegister {
    public static void register() {
        CombatTriggerHandler.registerTrigger(new BaseAnimationTrigger(EntityInit.RELIC_OBSERVER.getId(), true));
        CombatTriggerHandler.registerTrigger(new BaseAnimationTrigger(EntityInit.RELIC_EARTHSHAKER.getId(), true));
        CombatTriggerHandler.registerTrigger(new BaseAnimationTrigger(EntityInit.RELIC_ANNIHILATOR.getId(), true));
        CombatTriggerHandler.registerTrigger(new BaseAnimationTrigger(EntityInit.REALM_WARDEN.getId(), true));
    }
}