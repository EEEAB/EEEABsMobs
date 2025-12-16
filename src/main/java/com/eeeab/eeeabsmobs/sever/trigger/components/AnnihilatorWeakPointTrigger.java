package com.eeeab.eeeabsmobs.sever.trigger.components;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import com.eeeab.eeeabsmobs.sever.trigger.TriggerContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AnnihilatorWeakPointTrigger extends BaseCombatTrigger {
    public AnnihilatorWeakPointTrigger(ResourceLocation triggerId, ResourceLocation entityId, int promptLevel) {
        super(triggerId, entityId, promptLevel, true);
    }

    @Override
    protected boolean checkSpecificConditions(ServerPlayer player, TriggerContext context) {
        if (context.getEntity() instanceof EntityRelicAnnihilator annihilator) {
            Animation animation = annihilator.getAnimation();
            return animation == annihilator.shot1Animation
                    || animation == annihilator.trickshot1Animation
                    || animation == annihilator.laserAnimation;
        }
        return false;
    }
}
