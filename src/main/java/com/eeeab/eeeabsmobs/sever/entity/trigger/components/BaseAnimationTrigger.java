package com.eeeab.eeeabsmobs.sever.entity.trigger.components;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.AnimationNotification;
import com.eeeab.eeeabsmobs.sever.entity.trigger.TriggerContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.util.Strings;

public class BaseAnimationTrigger extends BaseCombatTrigger {
    public BaseAnimationTrigger(ResourceLocation entityId, boolean oncePerEncounter) {
        super(null, entityId, 0, oncePerEncounter);
    }

    @Override
    public boolean shouldTrigger(ServerPlayer player, ResourceLocation entityId, TriggerContext context) {
        if (getTriggerKey() == null) {
            if (context.getEntity() instanceof AnimatedEntity entity && entity.getAnimation() instanceof AnimationNotification animation) {
                String msgId = animation.getMsgId();
                setTriggerKey(Strings.isNotBlank(msgId) ? entityId.withSuffix("_" + msgId) : entityId);
                setMsgLevel(animation.getLevel());
            }
        }
        return getTriggerKey() != null;
    }
}
