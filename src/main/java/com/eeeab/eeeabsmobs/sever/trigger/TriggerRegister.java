package com.eeeab.eeeabsmobs.sever.trigger;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.trigger.components.BaseAnimationTrigger;

public class TriggerRegister {
    public static void register() {
        CombatTriggerHandler.registerTrigger(new BaseAnimationTrigger(EntityInit.RELIC_ANNIHILATOR.getId(), true));
    }
}