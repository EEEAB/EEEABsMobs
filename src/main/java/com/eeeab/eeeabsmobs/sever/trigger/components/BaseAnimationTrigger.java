package com.eeeab.eeeabsmobs.sever.trigger.components;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.AnimationNotification;
import com.eeeab.eeeabsmobs.sever.trigger.TriggerContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class BaseAnimationTrigger extends BaseCombatTrigger {
    public BaseAnimationTrigger(ResourceLocation entityId, boolean oncePerEncounter) {
        super(null, entityId, 0, oncePerEncounter);
    }

    @Override
    public boolean shouldTrigger(ServerPlayer player, ResourceLocation entityId, TriggerContext context) {
        if (getTriggerKey() == null) {
            if (context.getEntity() instanceof AnimatedEntity entity && entity.getAnimation() instanceof AnimationNotification animation) {
                setTriggerKey(new ResourceLocation(entityId.getNamespace(), entityId.getPath() + "_" + animation.getMsgId()));
                setMsgLevel(animation.getLevel());
            }
        }
        return getTriggerKey() != null;
    }
}
